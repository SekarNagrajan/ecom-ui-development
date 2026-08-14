package net.ecom.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.EbookingValidatorFormBeanNew;

public class BookingJasper extends HttpServlet {

    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String bookno = Validation.checkNull(request.getParameter("bookno"));
        String agencycode = Validation.checkNull(request.getParameter("agencyid"));
         System.out.println("[ECOM-WAR] BookingJasper bookno: " + bookno);
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String result = null;
        String dbConfig = null;
        String locationcode = "", corpagency = "", jasperid = "", countrycode = "";
        ServletContext contexts = getServletContext();
        Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
        Hashtable countryHashObj = (Hashtable) contexts.getAttribute("SESSION_COUNTRYOBJECT");
        String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
        String bkgPrintDBConfig = (String) contexts.getAttribute(GeneralConf.bkgprintdbconfig);
        List pluginlis = (List) contexts.getAttribute("LRP_PLUGIN_DETAILS");
        //    IJasperViewer jasper = null;
        if (printBookingtoSavenew(bookno, agencycode, countryHashObj) && bookno != null && bookno.length() > 0) {
            byte[] byteArray = null;
            try {
                //
                ArrayList bookinglist = new ArrayList();
                List refList = new ArrayList();
                Map inputMap = new HashMap();
                Map detailsMap = new HashMap();
                Map<String, Object> data = new HashMap<String, Object>();
                Map hm = new HashMap();
                //
                String Service = null, bookdate = null, etd = null, custremarks = "";
                String path = "";  // F:\\Jaspers
                String imagepath = "";
                //
//                 System.out.println("[ECOM-WAR] BookingJasper printConfirmedBooking");
                //
                String ebkPrintSid = (String) contexts.getAttribute(GeneralConf.EBKPRINTSID);
                String loginUser = ((String) session.getAttribute("SESSION_LOGINNAME"));
                String custcode = (String) session.getAttribute("SESSION_USERCODE");
                String bookPrintWSPath = (String) contexts.getAttribute(GeneralConf.BL_PRINT_WEBSERVICE_URL);
                String Booking_Jasperid_From_Agency = (String) contexts.getAttribute(GeneralConf.Booking_Jasperid_From_Agency);

                String pol = "";
                String chinaPorts = (String) contexts.getAttribute("BKGConfPrintShenzenAgencyPort");
                //    System.out.println("[ECOM-WAR] BookingJasper =======>bookPrintWSPath" + bookPrintWSPath);
                //
                if (bookno != null && bookno.length() > 0) {
                    //
                    bookinglist = bookingObj.getLRPBookingViewDetails(bookno);
                    if (bookinglist != null) {
                        int size = bookinglist.size();
                        for (int i = 0; i < size; i++) {
                            EbookingValidatorFormBeanNew masterdata = (EbookingValidatorFormBeanNew) bookinglist.get(i);
                            Service = Validation.checkNull((String) serlist.get(Validation.checkNull(masterdata.getEbook_servicecode()).toUpperCase()));
                            bookdate = masterdata.getEbook_date();
                            if (masterdata.getEbook_pol_etd() != null && masterdata.getEbook_pol_etd().length() > 0) {
                                etd = masterdata.getEbook_pol_etd();
                            } else {
                                etd = "-";
                            }
                            agencycode = Validation.checkNull((String) masterdata.getAgencyid());
                            pol = Validation.checkNull((String) masterdata.getEbook_pol());
                            if (agencycode != null) {
                                agencycode = agencycode.trim();
                            }
                            corpagency = bookingObj.getecomcorpagency(agencycode);
//                             System.out.println("[ECOM-WAR] BookingJasper agencycode::>>>>>>>>>>" + agencycode + "corpagency::::>>>>>" + corpagency);
                            if (corpagency == null || corpagency.length() == 0) {
                                corpagency = "";
                            }
                            countrycode = bookingObj.getecompagencylocation(corpagency);
//                             System.out.println("[ECOM-WAR] BookingJasper countrycode::::::>>>>>>>>>>>>>>>>>>>" + countrycode);
                             System.out.println("[ECOM-WAR] BookingJasper countrycode: " + countrycode + ", agencycode: " + agencycode + ", corpagency: " + corpagency);

                            if (countrycode != null && countrycode.length() > 0) {
                                if ("MY".equalsIgnoreCase(countrycode)) {
                                    jasperid = "BKM";
                                } else if ("SG".equalsIgnoreCase(countrycode)) {
                                    jasperid = "BKS";
                                } else {
                                    jasperid = "BKJ";
                                }
                            } else {
                                jasperid = "BKJ";
                            }
                            if ("Y".equalsIgnoreCase(Booking_Jasperid_From_Agency)) {
                                String jasid = bookingObj.getBKGJasperIdFromAgencycode(agencycode);
                                if (jasid != null && jasid.trim().length() > 0) {
                                    jasperid = jasid;
                                }
                            }
                            if (chinaPorts.contains(pol)) {
                                jasperid = "BPS";
                            }
                            locationcode = agencycode;
                            path = bookingObj.getecomjasperpath(agencycode, locationcode);
//                             System.out.println("[ECOM-WAR] BookingJasper path::::>>>>>" + path);
                            if (path == null || path.length() == 0) {
                                path = "";
                            }
                            path = path.trim();
                            if (!path.endsWith(File.separator)) {
                                path = path + File.separator;
                            }

//                             System.out.println("[ECOM-WAR] BookingJasper JasperId::::::>>>>>>>>>>>>>>>>>>>" + jasperid);
                             System.out.println("[ECOM-WAR] BookingJasper path: " + path + ", JasperId: " + jasperid);

                            custremarks = Validation.checkNull((String) (masterdata.getEbook_status_remarks()));
                            imagepath = new StringBuilder().append(path).append("images").append(File.separator).append("bkglogo.jpg").toString();
                            inputMap.put("Ser", Service);
                            inputMap.put("BKD", bookdate);
                            inputMap.put("ETD", etd);
                            inputMap.put("book_no", bookno);
                            inputMap.put("UserRemarks", custremarks);
                            inputMap.put("agencycode", corpagency);
                            inputMap.put("path", path);
                            inputMap.put("logo", imagepath);
                            refList = new ArrayList();
                            refList.add(bookno);
                            inputMap.put("refno", refList);
                            inputMap.put("UserName", custcode);
                            inputMap.put("usercode", custcode);
                            inputMap.put("moduleid", "BKG");
                            inputMap.put("jasperid", jasperid);
                            inputMap.put("locationcode", agencycode);
                            inputMap.put("headerimg", agencycode);
                            inputMap.put("usessionid", sessionId);
                            inputMap.put("sessionid", sessionId);
                            inputMap.put("clientdateformat", masterdata.getUserdateformat());
                            detailsMap = new HashMap();
                            if ("Y".equalsIgnoreCase(bkgPrintDBConfig)) {
                                if (pluginlis != null && pluginlis.contains("BJC")) {
                                    dbConfig = "DOC";
                                } else {
                                    dbConfig = "ReportDB";
                                }
                                detailsMap.put("datasource", dbConfig);
                            } else {
                                detailsMap.put("datasource", "DOC");
                            }
                            detailsMap.put("title", "BKG");
                            //
                            hm.put("input", inputMap);
                            hm.put("details", detailsMap);
                            //
                            //
                            Map subReportMap = new HashMap();
                            subReportMap.put("path", path);
                            //
                            //      System.out.println("[ECOM-WAR] BookingJasper custcode:::::::" + custcode);
                             System.out.println("[ECOM-WAR] BookingJasper to print bkg--->" + ebkPrintSid);
                            if ("Y".equalsIgnoreCase(ebkPrintSid)) {
                                bookingObj.setBkgPrint(bookno, sessionId);
                                 System.out.println("[ECOM-WAR] BookingJasper setecombkgprintcalled");
                            }

                            if (hm != null) {
                                data.put("input", hm.get("input"));
                                data.put("details", hm.get("details"));
                                data.put("path", path);
                                data.put("subreport", subReportMap);
                                 System.out.println("[ECOM-WAR] BookingJasper data:" + data);
                                String json = new Gson().toJson(data);
                                //
                                String url = bookPrintWSPath + GeneralConf.BL_PRINT_WS_CONTEXT;
                                //            System.out.println("[ECOM-WAR] BookingJasper url:::::::::::::::::::::::::::" + url);
                                Client jaxrsClient = ClientBuilder.newClient();
                                WebTarget webTarget = jaxrsClient.target(url);
                                Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                                Response respon = invocation.invoke();
                                String results = respon.readEntity(String.class);
                                //        System.out.println("[ECOM-WAR] BookingJasper resultss::::"+results);
                                Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
                                }.getType());
//                                 System.out.println("[ECOM-WAR] BookingJasper ===>ress" + hmdata.get("filename"));
//                                 System.out.println("[ECOM-WAR] BookingJasper ===>type" + hmdata.get("type"));
                                 System.out.println("[ECOM-WAR] BookingJasper ress: " + hmdata.get("filename") + ", type: " + hmdata.get("type"));

                                String responseType = (String) hmdata.get("status");
                                String viewType = (String) hmdata.get("type");
                                if ("200".equalsIgnoreCase(responseType)) {
                                    result = "Success";
                                     System.out.println("[ECOM-WAR] BookingJasper viewType:::>>>>>>>" + viewType);
                                    if ("P".equalsIgnoreCase(viewType)) {
                                        byteArray = (byte[]) Base64.getDecoder().decode(hmdata.get("content").toString());
                                        session.setAttribute("jasperbyte", byteArray);
                                    }
                                } else //500 Error
                                {
                                    result = EcomConsts.SERVER_RETRIVE_ERROR;
                                    session.setAttribute("jasperbyte", null);
                                }
                            } else {
                                 System.out.println("[ECOM-WAR] BookingJasper ===>HM" + hm);
                                result = EcomConsts.SERVER_RETRIVE_ERROR;
                                session.setAttribute("jasperbyte", null);
                            }
                        }
                    }
                    if ("Y".equalsIgnoreCase(ebkPrintSid)) {
                        bookingObj.delBkgPrint(bookno, sessionId);
                         System.out.println("[ECOM-WAR] BookingJasper delecombkgprintcalled");
                    }

                }
            } catch (Exception e) {
                result = EcomConsts.SERVER_RETRIVE_ERROR;
                session.setAttribute("jasperbyte", null);
                e.printStackTrace();
                //    System.out.println("[ECOM-WAR] BookingJasper Welcome Error::::::::::");
            }
        }

        out.print(result);

    }

    public boolean printBookingtoSavenew(String bookno, String agencycode, Hashtable countryHashObj) {
        boolean bln = true;
        try {
            //  BkgPrintBookingSaveVo dgvo = getInputValues(type);
//             System.out.println("[ECOM-WAR] BookingJasper printBookingtoSavenew");
            String temp = bookingObj.printBookingJaserSaveData(bookno, agencycode, countryHashObj);
            //     System.out.println("[ECOM-WAR] BookingJasper :::>>>>>>>:::" + temp);
            if (temp != null && "Success".equalsIgnoreCase(temp)) {
                bln = true;
            } else if (temp != null && "duplicate".equalsIgnoreCase(temp)) {
                bln = true;
            } else {
                bln = false;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return bln;
    }

    //
    //
//    public boolean printBookingtoSavenew(String type) {
//        boolean bln = true;
//        try {
//         //   BkgPrintBookingSaveVo dgvo = getInputValues(type);
//             System.out.println("[ECOM-WAR] BookingJasper printBookingtoSavenew");
//        Object obj = this.biz.printSaveData(dgvo);
//            System.out.println(new StringBuilder().append("Returned 1: ").append(obj).toString());
//
//            if (obj != null) {
//                if ((obj instanceof String)) {
//                    String temp = (String) obj;
//                    if ("duplicate".equalsIgnoreCase(temp)) {
//                        this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005429", this.zEmp, null);
//                    } else if ("Success".equalsIgnoreCase(temp)) {
//                        this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005430", this.zEmp, null);
//                    } else if ("Failure".equalsIgnoreCase(temp)) {
//                        this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005431", this.zEmp, null);
//
//                        bln = false;
//                    }
//                } else if ((obj instanceof Exception)) {
//                    this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005428", this.zEmp, new String[]{obj.toString()});
//                    bln = false;
//                }
//            } else {
//                this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005428", this.zEmp, null);
//                bln = false;
//            }
//        } catch (Exception exp) {
//           exp.printStackTrace();
//        }
//
//        return bln;
//    }
    //
//    public boolean getPrintBookingStatus() {
//        try {
//            String bookNo = this.mod.getTxtBookingNo();
//
//            Object obj = this.biz.printGetData(bookNo);
//            System.out.println(new StringBuilder().append("getPrintBookingStatus Returned : ").append(obj).toString());
//
//            if (obj != null) {
//                if ((obj instanceof BkgPrintBookingSaveVo)) {
//                    BkgPrintBookingSaveVo temp = (BkgPrintBookingSaveVo) obj;
//                    System.out.println(new StringBuilder().append(" getPrintBookingStatus 2 Returned : ").append(temp).toString());
//                    System.out.println(new StringBuilder().append("temp.shipperName===>").append(temp.shipperName).toString());
//
//                    this.mod.setTxtBookingNo(bookNo.trim());
//                    if (checkST(temp.shipperName)) {
//                        this.mod.setTxtShipperName(temp.shipperName.trim());
//                    }
//                    if (checkST(temp.shipperRef)) {
//                        this.mod.setTxtSHRef(temp.shipperRef.trim());
//                    }
//                    if (checkST(temp.shipperAdd)) {
//                        this.mod.setTxtShipperAddress(temp.shipperAdd.trim());
//                    }
//                    if (checkST(temp.fwdName)) {
//                        this.mod.setTxtForwarderName(temp.fwdName.trim());
//                    }
//                    if (checkST(temp.fwdRef)) {
//                        this.mod.setTxtFWRef(temp.fwdRef.trim());
//                    }
//                    if (checkST(temp.fwdAdd)) {
//                        this.mod.setTxtForwrderAddress(temp.fwdAdd.trim());
//                    }
//                    if (checkST(temp.emptyPickUp)) {
//                        this.mod.setTxtEmptyPickup(temp.emptyPickUp.trim());
//                    }
//                    if (checkST(temp.delivery)) {
//                        this.mod.setTxtpDelivery(temp.delivery.trim());
//                    }
//                    if (checkST(temp.remarks)) {
//                        this.mod.setTxtUserRemarks(temp.remarks.trim());
//                    }
//                    if (checkST(temp.OperationCroRemarks)) {
//                        this.mod.setTxtOPSCRORemarks(temp.OperationCroRemarks.trim());
//                    }
//                    if (checkST(temp.loadPortFreeDays)) {
//                        this.mod.setTxtLoadPortFreedays(temp.loadPortFreeDays.trim());
//                    }
//                    System.out.println(new StringBuilder().append("mod.setTxtLoadPortFreedays ").append(this.mod.getTxtLoadPortFreedays()).toString());
//                    return true;
//                }
//                if (getPrintBookingStatusz1(obj)) {
//                    return false;
//                }
//            } else {
//                this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005428", this.zEmp, null);
//                return false;
//            }
//        } catch (Exception exp) {
//            System.out.println(new StringBuilder().append("Exception . ").append(exp).toString());
//
//            return false;
//        }
//        return true;
//    }
//    public void printConfirmedBooking() {
//    try {
//       System.out.println("[ECOM-WAR] BookingJasper printConfirmedBooking ");
//      if ((this.mod.getTxtBookingNo() != null) && (this.mod.getTxtBookingNo().length() > 0)) {
//        HashMap map = new HashMap();
//        System.out.println(this.zEmp);
//        DateFormat sdf = new SimpleDateFormat(this.mData.getDateFormat());
//        System.out.println(new StringBuilder().append(" book.master.OceanService.trim() >>> ").append(this.book.getMaster().getOceanService()).toString());
//        System.out.println(new StringBuilder().append(" book.master.OceanVessel.trim() >>> ").append(this.book.getMaster().getOceanVessel()).toString());
//        System.out.println(new StringBuilder().append(" book.master.OceanVoyage.trim() >>> ").append(this.book.getMaster().getOceanVoyage()).toString());
//        System.out.println(new StringBuilder().append(" book.master.POLAt.trim() >>> ").append(this.book.getMaster().getPOLAt()).toString());
//
//        System.out.println(new StringBuilder().append(" book.master.OcenBound.trim() >>> ").append(this.book.getMaster().getOcenBound()).toString());
//        System.out.println(new StringBuilder().append(" book.master.BookingDate >> ").append(this.book.getMaster().getBookingDate()).toString());
//        System.out.println(new StringBuilder().append(" first Service  >>> ").append(this.fsvc).toString());
//        System.out.println(new StringBuilder().append(" second Service  >>> ").append(this.ssvc).toString());
//
//        if ((this.fsvc != null) && (this.ssvc != null)) {
//          if (!this.fsvc.trim().equalsIgnoreCase(this.ssvc.trim()))
//          {
//            map.put("Ser", this.zY);
//          }
//          else map.put("Ser", this.zN);
//        }
//        else {
//          map.put("Ser", this.zN);
//        }
//
//        String bookdates = this.zEmp;
//        if (checkST(this.bookdate)) {
//          bookdates = sdf.format(this.mod.getDpkBookingDate());
//          map.put("BKD", bookdates);
//        } else {
//          map.put("BKD", this.zEmp);
//        }
//
//        System.out.println(new StringBuilder().append(" booknum  >>>> ").append(this.booknum).toString());
//        String trans1 = this.zEmp;
//        if (checkST(this.booknum))
//        {
//          SearchUtils su = new SearchUtils();
//          su.setParameterValues(0, 10, this.booknum);
//
//          System.out.println(new StringBuilder().append("  trans1  >>>> ").append(trans1).toString());
//          if (checkCheckCode(trans1))
//             System.out.println("[ECOM-WAR] BookingJasper iff");
//          else {
//            trans1 = this.zEmp;
//          }
//        }
//
//        System.out.println(new StringBuilder().append(" trans1  >>>> ").append(trans1).toString());
//         System.out.println("[ECOM-WAR] BookingJasper <<<<<<<  1  >>>>>>> ");
//
//        System.out.println(new StringBuilder().append(" ssvc >>  ").append(this.ssvc).toString());
//        System.out.println(new StringBuilder().append(" book.master.OceanVesse >> ").append(this.book.getMaster().getOceanVessel()).toString());
//        System.out.println(new StringBuilder().append(" book.master.OceanVoyage >> ").append(this.book.getMaster().getOceanVoyage()).toString());
//        System.out.println(new StringBuilder().append(" book.master.OcenBound >> ").append(this.book.getMaster().getOcenBound()).toString());
//        System.out.println(new StringBuilder().append(" book.master.POLAt >> ").append(this.book.getMaster().getPOLAt()).toString());
//        System.out.println(new StringBuilder().append(" trans1 >> ").append(trans1).toString());
//        if ((printConfirmedBookingz1()) && (trans1 != null) && 
//          (trans1
//          .trim().length() > 0)) {
//           System.out.println("[ECOM-WAR] BookingJasper <<<<<<< 2  >>>>>>> ");
//
//          SearchUtils su = new SearchUtils();
//          su.setParameterValues(0, 10, this.ssvc.trim());
//          su.setParameterValues(1, 10, this.book.getMaster().getOceanVessel().trim());
//          su.setParameterValues(2, 10, this.book.getMaster().getOceanVoyage().trim());
//          su.setParameterValues(3, 10, this.book.getMaster().getOcenBound().trim());
//          su.setParameterValues(4, 10, trans1.trim());
//          su.setParameterValues(5, 10, this.book.getMaster().getPOLAt().trim());
//
//          String eTD = su.checkCode("BKG", "3005168");
//          System.out.println(new StringBuilder().append(" ETD >>>>  ").append(eTD).toString());
//          if (checkCheckCode(eTD))
//            map.put("ETD", eTD);
//          else
//            map.put("ETD", "-");
//        }
//        else {
//          map.put("ETD", "-");
//        }
//
//        if ((this.mod.getTxtBookingNo() != null) && (this.mod.getTxtBookingNo().length() > 0)) {
//          map.put("book_no", this.mod.getTxtBookingNo());
//        }
//        else {
//          map.put("book_no", " ");
//        }
//
//        if ((this.mod.getTxtUserRemarks() != null) && (this.mod.getTxtUserRemarks().length() > 0)) {
//          map.put("UserRemarks", this.mod.getTxtUserRemarks());
//        }
//        else {
//          map.put("UserRemarks", " ");
//        }
//
//        if (this.mData.getCorpAgency() != null)
//          map.put(this.zagencycode, this.mData.getCorpAgency().trim());
//        else {
//          map.put(this.zagencycode, this.zEmp);
//        }
//        if (this.mData.getAgencyCode() != null) {
//          map.put("locationcode", this.mData.getAgencyCode().trim());
//          map.put("headerimg", this.mData.getAgencyCode().trim());
//          System.out.println(new StringBuilder().append("agencycode=").append(this.mData.getAgencyCode().trim()).toString());
//        } else {
//          map.put(this.zagencycode, this.zEmp);
//        }
//
//        System.out.println(new StringBuilder().append(" Hash Map >>>>> ").append(map).toString());
//
//        map.put("path", this.zEmp);
//        String path = this.zEmp;
//        System.out.println(new StringBuilder().append(" Map >>> ").append(map).toString());
//        List refList = new ArrayList();
//        refList.add(this.mod.getTxtBookingNo().trim());
//        map.put("refno", refList);
//
//        SearchUtils su = new SearchUtils();
//        su.setParameterValues(0, 10, this.mData.getAgencyCode());
//        String orgcode = su.checkCode("BKG", "3005169");
//        System.out.println(new StringBuilder().append("jasper name is *********").append(orgcode).toString());
//        su = new SearchUtils();
//        su.setParameterValues(0, 10, this.mData.getLocationCode());
//        su.setParameterValues(1, 10, this.mData.getAgencyCode());
//        path = su.checkCode("REV", "12056009");
//        RequestContext.getCurrentInstance().execute("PF('BKG_JDlgPrintBooking').hide();");
//
//        Map detailsMap = new HashMap();
//        detailsMap.put("datasource", "DOC");
//        detailsMap.put("title", "BKG");
//        map.put("UserName", this.mData.getUserCode());
//
//        this.jas.setJasperData(detailsMap, map);
//        this.jas.setSubReport("path", path);
//        this.jas.invokeJasper("BKG", "BKJ");
//      }
//      else
//      {
//        this.dutil.showDialog("BKG", SMDialogUtil.type.INFO, "3005433", this.zEmp, null);
//      }
//    } catch (Exception e) {
//      this.log.fatal(e);
//    }
//  }
    //
//       protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException {
//        JasperPrint pdfname = null;
//        byte[] byteArray = null;
//        URLConnection con = null;
//        try {
//            HttpSession session = request.getSession();
//            String bookno = Validation.checkNull(request.getParameter("bookno"));
//             System.out.println("[ECOM-WAR] BookingJasper bookno: " + bookno);
//            //
//            con = getServletConnection(bookno);
//            InputStream instr = con.getInputStream();
//            ObjectInputStream inputFromServlet = new ObjectInputStream(instr);
//            Object results = inputFromServlet.readObject();
//            pdfname = ((JasperPrint) results);
//            try {
//                byteArray = (byte[]) JasperExportManager.exportReportToPdf(pdfname);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            //
//            session.setAttribute("jasperbyte", byteArray);
//             System.out.println("[ECOM-WAR] BookingJasper after pdfname" + pdfname);
//        } //end of else
//        catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private URLConnection getServletConnection(String bookno) throws IOException {
//        URLConnection conn = null;
//        ServletContext context = getServletContext();
//        try {
//            String ws_url = (String) context.getAttribute(GeneralConf.pdfviewurl) + "/PrintConfirmedBooking-war/PrintConfirmedBookingServlet?type=bkg&bookno=" + bookno;
//            URL urlServlet = new URL(ws_url);
//            conn = urlServlet.openConnection();
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setDefaultUseCaches(true);
//            conn.setRequestProperty("Content-Type", "application/octet-stream");
//        } catch (Exception e) {
//            e.printStackTrace();
//             System.out.println("[ECOM-WAR] BookingJasper connection" + e.getMessage());
//        }
//        return conn;
//    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
