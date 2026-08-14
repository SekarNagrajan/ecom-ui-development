/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.common.Validation;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sun.jersey.api.client.ClientHandlerException;
import net.ecom.common.EcomConsts;
//
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import infinispan.com.google.common.collect.HashBiMap;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.captcha.VerifyRecaptcha;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.lookup.EJBLookUp;
import net.ecom.tracking.vo.TrackingActvityResult;
import net.ecom.tracking.vo.TrackingCargoActivity;
import net.ecom.tracking.vo.TrackingCargoActivityResult;
import net.ecom.tracking.vo.TrackingCargoResult;
import net.ecom.tracking.vo.TrackingMasterVo;
import net.ecom.tracking.vo.TrackingProtActivityResult;
import sun.misc.BASE64Encoder;
//

public class TrackingDetailsAction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        String SUCCESS = "success";
        try {
            HttpSession session = request.getSession();
            Hashtable portlist = (Hashtable) session.getServletContext().getAttribute("SESSION_PORTLIST");
            String userCode = Validation.checkNull((String) session.getAttribute("SESSION_USERCODE"));
            String userfuritcust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
            ServletContext context = servlet.getServletContext();
            String strMdl = null, strMdlNo = null;
            String carrierCode = (String) context.getAttribute(GeneralConf.CARRIER_CODE);
            String displayallMovement = (String) context.getAttribute(GeneralConf.TRACKING_MOVEMENT_DISPLAY_ALL);
            String TrackingRestriction = (String) context.getAttribute(GeneralConf.TRACKING_RESTRICTION);
            strMdlNo = Validation.checkNull(request.getParameter("MdlNo"));
            strMdl = Validation.checkNull(request.getParameter("Mdl"));
            String gRecaptchaResponse = Validation.checkNull(request.getParameter("gcaptresp"));
            String gRecaptchaResponse1 = Validation.checkNull(request.getParameter("gcaptresphome"));
            String trackingviahome = Validation.checkNull(request.getParameter("trackingviahome"));
            String finalgRecaptchaResponse = null;
            finalgRecaptchaResponse = (gRecaptchaResponse.length() > 0) ? gRecaptchaResponse : gRecaptchaResponse1;
            String secretKey = Validation.checkNull((String) context.getAttribute(GeneralConf.CaptchaSecretKey));
            String toDisplayGgleCaptcha = (String) context.getAttribute(GeneralConf.DisplayGoogleCaptcha);
            String display_Tracking_Cutoff = Validation.checkNull((String) context.getAttribute(GeneralConf.Display_Tracking_Cutoff));
            String show_transportplan_track = Validation.checkNull((String) context.getAttribute(GeneralConf.show_transportplan_track));
            String showDataWithNoMovementEntry = Validation.checkNull((String) context.getAttribute(GeneralConf.SHOW_DATA_WITH_NO_MOVEMENTENTRY));
            boolean hasNoMovementRecords = false;
            String isViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
            String bookno = null, blno = null, polterminal = null, loadportid = null, polarrdate = null, nextPod = null, nextPodat = null, nextActualPort = null, nextActualEta = null;   //Activity Location
            String pol = null, pod = null; //POD
            boolean isMaskPorthdn = false;
            String destination = null, eta = null, msg = null, etd = null, divation = "";
            TrackingActvityResult resultvo = null;
            TrackingActvityResult resultMailvo = null;
            ArrayList<String> searNoList = new ArrayList<String>();
            Set<String> allowedContainers = new HashSet<String>();
            Map<String, HashMap> cutoffmap = new HashMap<String, HashMap>();
            Set<String> portset = new LinkedHashSet<String>();
            String validval = null;
            String trackno = null;
            boolean flag = true;
            String invalid = "";
            Gson gson = null;
            List<TrackingActvityResult> lstresult = null;
            List<TrackingActvityResult> lstMailresult = null;
            Map<String, List> movemap = new Hashtable<String, List>();
            Map<String, List> movemapFull = new Hashtable<String, List>();
            Map<String, List> trackportmap = new Hashtable<String, List>();
            List<TrackingProtActivityResult> portackList = null;
            ArrayList allmovementList = new ArrayList();
            TrackingCargoActivity trackcarVO = null;
            String dischargeportid = null;
            strMdlNo = strMdlNo.toUpperCase();
            System.out.println("[ECOM-WAR] TrackingDetailsAction welcom:::::" + strMdlNo);
            boolean verify = VerifyRecaptcha.verify(finalgRecaptchaResponse, secretKey);
            System.out.println("[ECOM-WAR] TrackingDetailsAction ::tCaptcha Verify value" + verify);

            String[] strMdlArr = strMdlNo.split(",");
            for (int i = 0; i < strMdlArr.length; i++) {
                validval = strMdlArr[i].trim();
                flag = checkSpecialCharacter(validval);
                if (validval != null && validval.length() <= 30 && flag == true) {
                    if (!searNoList.contains(validval)) {
                        searNoList.add(strMdlArr[i].trim());
                    }
                } else {
                    if (invalid.length() == 0) {
                        invalid = strMdlArr[i].trim();
                    } else {
                        invalid += "," + strMdlArr[i].trim();
                    }
                }
            }
            int m = 0;
            int v = searNoList.size();
            System.out.println("[ECOM-WAR] TrackingDetailsAction isViaLogin==> " + isViaLogin);
            if ((!"Y".equalsIgnoreCase(toDisplayGgleCaptcha) || verify) || "Y".equalsIgnoreCase(trackingviahome) || "YES".equalsIgnoreCase(isViaLogin)) {
                for (m = 0; m < v; m++) {
                    trackno = searNoList.get(m);
                    String url = (String) context.getAttribute(GeneralConf.TRACKINGSEARCHURL) + "?key=" + trackno + "&custcode=" + userCode + "&furitcust=" + userfuritcust + "&trackingrestriction=" + TrackingRestriction;
                    //    url="http://localhost:8070/Phase2/bytrackMovement?key=" + trackno;
                    System.out.println("[ECOM-WAR] TrackingDetailsAction >>>url>>>" + url);
                    String name = (String) context.getAttribute(GeneralConf.TRACKINGUSERNAME);
                    String password = (String) context.getAttribute(GeneralConf.TRACKINGPASSWORD);
                    String authString = name + ":" + password;
                    String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
                    System.out.println("[ECOM-WAR] TrackingDetailsAction Base64 encoded auth string: " + authStringEnc);
                    Client restClient = Client.create();
                    WebResource webResource = restClient.resource(url);
                    ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + authStringEnc).get(ClientResponse.class);
                    if (resp.getStatus() == 200) {
                        String output = resp.getEntity(String.class);
                        gson = new Gson();
                        Type type = new TypeToken<TrackingCargoResult>() {
                        }.getType();
                        TrackingCargoResult lstresults = gson.fromJson(output, type);
                        msg = lstresults.getMessage();
                        System.out.println("[ECOM-WAR] TrackingDetailsAction output===============> " + output + " >>>lstresults.getCode()>>>>" + lstresults.getCode());
                        List<TrackingMasterVo> result = lstresults.getResult();
                        System.out.println("[ECOM-WAR] TrackingDetailsAction result=======> " + result);
                        lstresult = new ArrayList<TrackingActvityResult>();
                        lstMailresult = new ArrayList<TrackingActvityResult>();
                        if ((!("200").equalsIgnoreCase(lstresults.getCode())) || result == null) {
                            if (invalid.length() == 0) {
                                invalid = trackno;
                            } else {
                                invalid += "," + trackno.trim();
                            }
                            resultvo = new TrackingActvityResult();
                            resultvo.setResult(msg);
                            lstresult.add(resultvo);
                        } else if (result != null && result.size() > 0 && (("200").equalsIgnoreCase(lstresults.getCode()))) {
                            int resultsize = result.size();
                            TrackingMasterVo res = null;
                            if (!"Y".equalsIgnoreCase(displayallMovement)) {
                                if (resultsize > 0) {
                                    res = result.get(0);
                                    bookno = res.getBookNo();
                                    blno = res.getBlNo();
//                                    pod = checkNull(res.getDischargePortDesc());
                                    pol = checkNull(res.getOriginPortDesc());
                                    pod = checkNull(res.getFinalPortDesc());
                                    eta = res.getPodAt();
                                    etd = res.getPolAt();
                                    polterminal = res.getPolterminal();
                                    loadportid = res.getLoadportid();
                                    polarrdate = res.getLoadarrdate();

                                    nextPod = res.getNextPod();
                                    nextPodat = res.getNextPodat();
                                    nextActualPort = res.getNextActualPort();
                                    nextActualEta = res.getNextActualEta();
                                    
                                    String dateFormatPattern = GeneralConf.getStrConfDefDateTimeFormat();
                                    /*
                                    if (nextPodat != null && !nextPodat.trim().isEmpty() && dateFormatPattern != null && !dateFormatPattern.trim().isEmpty()) {
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                                        Date parsedDate = inputFormat.parse(nextPodat);
                                        SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormatPattern);
                                        nextPodat = outputFormat.format(parsedDate);
                                    }
 
                                    if (nextActualEta != null && !nextActualEta.trim().isEmpty() && dateFormatPattern != null && !dateFormatPattern.trim().isEmpty()) {
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                                        Date parsedDate = inputFormat.parse(nextActualEta);
                                        SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormatPattern);
                                        nextActualEta = outputFormat.format(parsedDate);
                                    }
                                    */
                                    System.out.println("[ECOM-WAR] TrackingDetailsAction nextPod " + nextPod + "  nextPodat " + nextPodat);
                                    System.out.println("[ECOM-WAR] TrackingDetailsAction nextActualPort " + nextActualPort + "  nextActualEta " + nextActualEta);
                                    
                             
                                    boolean isMaskPort = MaskPort.isMaskPorts(res.getLoadportid() + "," + res.getDischargePortDesc() + "," + res.getOriginPortCode() + "," + res.getDeliveryPortCode(), context);
                                    if (isMaskPort && checkContainerNoSearch(res.getMovementList(), trackno, (String) context.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                                        portset.add(loadportid);
                                        if ("ESL".equalsIgnoreCase(carrierCode) && (etd == null || etd.trim().length() == 0)) {
                                            String polat = ecomCntrlObj.getTrackingBKGDepartureDate(bookno);
                                            if (polat != null && polat.trim().length() > 0) {
                                                SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                SimpleDateFormat traget = new SimpleDateFormat((String) context.getAttribute(GeneralConf.TRACKINGDATEFORMAT));
                                                etd = traget.format(source.parse(polat));
                                            }
                                        }
                                        System.out.println("[ECOM-WAR] TrackingDetailsAction etdvaluetracking--->" + etd + " etavaluetracking--->" + eta);
                                        List<TrackingCargoActivityResult> movementList = res.getMovementList();
                                        portackList = res.getPortacklist();
                                        if (portackList != null && !portackList.isEmpty()) {
                                            TrackingProtActivityResult lst = null;
                                            int lastIndex = portackList.size() - 1;
                                            lst = portackList.get(lastIndex);
                                            dischargeportid = lst.getDischargeportid();
                                            System.out.println("[ECOM-WAR] TrackingDetailsAction dischargeportid===>>>" + dischargeportid);
                                        }
                                        if ("Y".equalsIgnoreCase(show_transportplan_track) && !"".equalsIgnoreCase(checkNull(etd)) && etd.trim().length() > 0 && "" != checkNull(dischargeportid)) {
                                            System.out.println("[ECOM-WAR] TrackingDetailsAction bookno===>" + bookno);
                                            Map hs = (Map) ecomCntrlObj.transportplanpod(bookno);
                                            if (hs != null && hs.size() > 0) {
                                                System.out.println("[ECOM-WAR] TrackingDetailsAction podat=>" + hs + "pod==>>" + pod);
                                                String podplaned = (String) hs.get("dischargeportid");
                                                String etaplaned = (String) hs.get("etaDate");
                                                //  if (dischargeportid.equalsIgnoreCase(podplaned)) {
                                                SimpleDateFormat source = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                                                Date inputDate = sourceFormat.parse(etaplaned);
                                                String formattedDate = source.format(inputDate);
//                                               int daysDeviation=DateConversion_New.daysDifferentBetweenTwoDays(eta,formattedDate);
                                                try {
                                                    int daysDeviation = DateConversion_New.daysDifferentBetweenTwoDaysTrackETA(eta, formattedDate);
                                                    if (daysDeviation > 0) {
                                                        divation = "(+" + daysDeviation + "d)";
                                                    } else {
                                                        divation = "(" + daysDeviation + "d )";
                                                    }
                                                } catch (ParseException e) {
                                                    System.out.println("[ECOM-WAR] TrackingDetailsAction Error parsing dates: " + e.getMessage());
                                                    e.printStackTrace();
                                                }

                                                /* } else {
                                                    divation = "";
                                                }*/ //for Jira TSHG-1633 issue --Fixed by preethi
                                            }
                                        }
                                        isMaskPorthdn=MaskPortUtil.isActualPort(res.getLoadportid()) || MaskPortUtil.isActualPort(res.getDischargePortDesc());
                                        if(isMaskPorthdn){
                                            pol=MaskPortUtil.getMaskPortNameByActualCode(res.getLoadportid());
                                            pod=MaskPortUtil.getMaskPortNameByActualCode(res.getDischargePortDesc());
                                         }
                                        if (movementList != null) {
//                                Type clstype = new TypeToken<List<TrackingCargoActivityResult>>() {
//                                }.getType();
                                            boolean hasMovementEntries = false;
                                            for (int k = 0; k < movementList.size(); k++) {
                                                TrackingCargoActivityResult trackmstvo = (TrackingCargoActivityResult) movementList.get(k);
                                                List<TrackingCargoActivity> tracact = (List<TrackingCargoActivity>) trackmstvo.getContainerActivity();
                                                if (tracact != null && tracact.size() > 0) {
                                                    hasMovementEntries = true;
                                                    trackcarVO = (TrackingCargoActivity) tracact.get(0);
                                                    resultvo = new TrackingActvityResult();
                                                    resultvo.setBookno(bookno);
                                                    resultvo.setBlno(blno);
                                                    resultvo.setContainerno(trackcarVO.getContainerNo());
                                                    resultvo.setContainertype(trackcarVO.getEqpTypeId());
                                                    resultvo.setDestination(destination);
                                                    resultvo.setPol(pol);
                                                    resultvo.setPod(pod);
                                                    resultvo.setActivitydate(trackcarVO.getActivityDate());
                                                    resultvo.setActivitylocation(trackcarVO.getPortName());
                                                    resultvo.setLatestactivity(trackcarVO.getMovementDesc());
                                                    resultvo.setEta(eta);
                                                    resultvo.setEtd(etd);
                                                    resultvo.setPolterminal(polterminal);
                                                    resultvo.setLoadportid(loadportid);
                                                    resultvo.setPolarrdate(polarrdate);
                                                    resultvo.setDivation(divation);
                                                    resultvo.setActivityloccode(trackcarVO.getActivityloccode());
                                                    resultvo.setNextport(trackcarVO.getNextpod());
                                                    resultvo.setNextportetd(trackcarVO.getNextpodETD());
                                                    resultvo.setNextPod(nextPod);
                                                    resultvo.setNextPodAt(nextPodat);
                                                    resultvo.setNextActualPort(nextActualPort);
                                                    resultvo.setNextActualEta(nextActualEta);
                                                    lstresult.add(resultvo);
                                                    String contNoForWhitelist = Validation.checkNull(trackcarVO.getContainerNo());
                                                    if (contNoForWhitelist.length() > 0) {
                                                        allowedContainers.add(contNoForWhitelist.toUpperCase());
                                                    }
                                                    for (TrackingCargoActivity trackcarallVO : tracact) {
                                                        resultMailvo = new TrackingActvityResult();
                                                        resultMailvo.setBookno(bookno);
                                                        resultMailvo.setBlno(blno);
                                                        resultMailvo.setContainerno(trackcarallVO.getContainerNo());
                                                        resultMailvo.setContainertype(trackcarallVO.getEqpTypeId());
                                                        resultMailvo.setDestination(destination);
                                                        resultMailvo.setPol(pol);
                                                        resultMailvo.setPod(pod);
                                                        resultMailvo.setActivitydate(trackcarallVO.getActivityDate());
                                                        resultMailvo.setActivitylocation(trackcarallVO.getPortName());
                                                        resultMailvo.setLatestactivity(trackcarallVO.getMovementDesc());
                                                        resultMailvo.setEta(eta);
                                                        resultMailvo.setEtd(etd);
                                                        resultMailvo.setPolterminal(polterminal);
                                                        resultMailvo.setLoadportid(loadportid);
                                                        resultMailvo.setPolarrdate(polarrdate);
                                                        resultMailvo.setDivation(divation);
                                                        resultMailvo.setActivityloccode(trackcarallVO.getActivityloccode());
                                                        resultMailvo.setNextport(trackcarallVO.getNextpod());
                                                        resultMailvo.setNextportetd(trackcarallVO.getNextpodETD());
                                                        resultMailvo.setNextActualPort(trackcarallVO.getNextActualPort());
                                                        resultMailvo.setNextActualEta(trackcarallVO.getNextActualEta());
                                                        lstMailresult.add(resultMailvo);
                                                    }
                                                }
                                            }
                                            if (!hasMovementEntries && bookno != null && bookno.length() > 0  && ("Y").equalsIgnoreCase(showDataWithNoMovementEntry)) {
                                                hasNoMovementRecords = true;
                                                System.out.println("[ECOM-WAR] TrackingDetailsAction MILAHA-1987: No movement entries for bookno:" + bookno);
                                                
                                               System.out.println("[ECOM-WAR] TrackingDetailsAction MILAHA-1987: Creating placeholder record for bookno:" + bookno);
                                                resultvo = new TrackingActvityResult();
                                                resultvo.setBookno(bookno);
                                                resultvo.setBlno(blno);
                                                resultvo.setPol(pol);
                                                resultvo.setPod(pod);
                                                resultvo.setEta(eta);
                                                resultvo.setEtd(etd);
                                                resultvo.setPolterminal(polterminal);
                                                resultvo.setLoadportid(loadportid);
                                                resultvo.setPolarrdate(polarrdate);
                                                resultvo.setDivation(divation);
                                                resultvo.setNextPod(nextPod);
                                                resultvo.setNextPodAt(nextPodat);
                                                resultvo.setNextActualPort(nextActualPort);
                                                resultvo.setNextActualEta(nextActualEta);
                                                resultvo.setIsHasNoMovementRecords(true);
                                                lstresult.add(resultvo);
                                                
                                                // Also add to mail result list
                                                resultMailvo = new TrackingActvityResult();
                                                resultMailvo.setBookno(bookno);
                                                resultMailvo.setBlno(blno);
                                                resultMailvo.setPol(pol);
                                                resultMailvo.setPod(pod);
                                                resultMailvo.setEta(eta);
                                                resultMailvo.setEtd(etd);
                                                resultMailvo.setPolterminal(polterminal);
                                                resultMailvo.setLoadportid(loadportid);
                                                resultMailvo.setPolarrdate(polarrdate);
                                                resultMailvo.setDivation(divation);
                                                resultMailvo.setNextActualPort(nextActualPort);
                                                resultMailvo.setNextActualEta(nextActualEta);
                                                resultMailvo.setIsHasNoMovementRecords(true);
                                                lstMailresult.add(resultMailvo);
                                            }
                                            
                                            if (bookno != null) {
                                                movemap.put(bookno, lstresult);
                                                movemapFull.put(bookno, lstMailresult);
//                                                System.out.println("[ECOM-WAR] TrackingDetailsAction portackList"+portackList.toString());
                                                trackportmap.put(bookno, portackList);
                                            } else {
                                                if (invalid.length() == 0) {
                                                    invalid = trackno;
                                                } else {
                                                    invalid += "," + trackno.trim();
                                                }
                                            }

                                        }
                                    } else {
                                        if (invalid.length() == 0) {
                                            invalid = trackno;
                                        } else {
                                            invalid += "," + trackno.trim();
                                        }
                                        resultvo = new TrackingActvityResult();
                                        resultvo.setResult(msg);
                                        lstresult.add(resultvo);
                                        lstMailresult.add(resultMailvo);
                                    }
                                }
                            } else {
                                if (resultsize > 0) {
                                    res = result.get(0);
                                    bookno = res.getBookNo();
                                    if (bookno != null && bookno.length() > 0) {
                                        allmovementList.add(res);
                                    } else {
                                        if (invalid.length() == 0) {
                                            invalid = trackno;
                                        } else {
                                            invalid += "," + trackno.trim();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if ("Y".equalsIgnoreCase(display_Tracking_Cutoff) && portset.size() > 0) {
                    Iterator value = portset.iterator();
                    String portcode = "";
                    HashMap tempm = null;
                    while (value.hasNext()) {
                        portcode = (String) value.next();
                        tempm = ecomCntrlObj.getRoutingCutOffDetailsFromPort(portcode);
                        if (tempm != null && tempm.size() > 0) {
                            cutoffmap.put(portcode, tempm);
                        }
                    }
                }
            }
            System.out.println("[ECOM-WAR] TrackingDetailsAction Tracking Cutoff Map=> " + cutoffmap + " Object::::::" + movemap + " trackportmap==>" + trackportmap);
            session.setAttribute("LIVEMAP_ALLOWED_CONTAINERS", allowedContainers);
            request.setAttribute("mov", movemap);

            request.setAttribute("movFullHistory", movemapFull);

            if (trackportmap != null) {
                request.setAttribute("transportlist", trackportmap);
            }
            request.setAttribute("TrackingCutOff", cutoffmap);
            SUCCESS = "success";
            request.setAttribute("trackingType", strMdl);
            request.setAttribute("Invalidnumber", invalid);
            request.setAttribute("allmovementList", allmovementList);
            request.setAttribute("portListObjj", portlist);
            //request.setAttribute("hasNoMovementRecords", hasNoMovementRecords ? "Y" : "N");
            if ("Y".equalsIgnoreCase(displayallMovement)) {
                SUCCESS = "AllMovement";
            }

            // End
        } catch (ClientHandlerException conEx) {
            SUCCESS = "failure";
            request.setAttribute("Error_report", EcomConsts.WS_CONNECTION_ERROR);
            conEx.printStackTrace();
        } /*catch (UnmarshalException parseEx) {
         SUCCESS = "failure";
         request.setAttribute("Error_report", EcomConsts.NO_RECORD);
         } */ catch (Exception e) {
            SUCCESS = "failure";
            request.setAttribute("Error_report", EcomConsts.WS_ERROR_MESSAGE);
            e.printStackTrace();
            System.out.println("[ECOM-WAR] TrackingDetailsAction ==================>>>" + e);
        }
        return mapping.findForward(SUCCESS);
    }

    private boolean checkSpecialCharacter(String s) {
        boolean flag = true;
        if (s == null || s.trim().isEmpty()) {
            flag = false;
            return flag;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if (b == true) {
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    private String checkNull(String str) {
        try {
            str = str != null ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private boolean checkContainerNoSearch(List movementlis, String key, String plugin) {
        boolean res = true;
        try {
            if ("Y".equalsIgnoreCase(plugin)) {
                if (movementlis != null && movementlis.size() == 1) {
                    TrackingCargoActivityResult trackmstvo = (TrackingCargoActivityResult) movementlis.get(0);
                    if (checkNull(key).equalsIgnoreCase(checkNull(trackmstvo.getContainerNo()))) {
                        res = false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
