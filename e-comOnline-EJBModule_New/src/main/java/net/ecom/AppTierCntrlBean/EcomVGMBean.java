/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.ecom.common.EcomConsts;
import net.ecom.v1.cntlbean.EcomV1VGMBeanRemote;
import net.ecom.vds.service.EcomVGMVDSService;
import net.ecom.vo.VGMCommonVO;
import net.ecom.vo.VGMVOModel;
import net.ecom.vo.VgmVO;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomVGMBean")
@Remote(EcomVGMBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
    @Resource(name = "jdbc/LRPDB", mappedName = "java:/MSSQLDS")
})
public class EcomVGMBean implements EcomVGMBeanRemote {

    private static final String INSERTECOMVGMSOLAS = "insert into ecom_vgmsolas(book_no,bl_no,eqptype,eqpid,vgm_source,vgm_type,vgm_ref_number,VGM_order_Number,vgm_obtained_date,vgm_weight,vgm_unit,"
            + "vgm_obtained_method,vgm_company_name,VGM_Address1,VGM_Address2,VGM_Zipcode,VGM_City,VGM_Country,vgm_authorized_person,Telephone,Fax,"
            + "Email,status,version_no,usercr,userup,oprstatus,datecr,dateup)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),getdate())";
    private static final String GETECOMVGMVERSION = "select max(version_no) as version_no from ecom_vgmsolas(nolock) where book_no=? and eqpid=?";
    private static final String UPDATEECOMVGMVERSION = "update ecom_vgmsolas set status='I' where book_no=? and eqpid=? and (status='A' or status='C')";

    @Override
    public void check() throws RemoteException {
        System.out.println("[ECOM] EcomVGMBean  ejb check>>>");
    }
    @Inject
    VDSInvoker vds;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomV1VGMBean!net.ecom.v1.cntlbean.EcomV1VGMBeanRemote")
    EcomV1VGMBeanRemote v1VGMBeanObj;

    EcomVGMVDSService vgmService;

    @Resource
    private SessionContext context;

    @PostConstruct
    public void inti() {
        vgmService = vds.create(EcomVGMVDSService.class);
    }

    @Override
    public VGMCommonVO getVGMDetails(String referenceNo, String type) throws RemoteException {
        List<VgmVO> list = null;
        VgmVO vgmvo = null;
        //
        Set<String> set = new HashSet<String>();
        Set<String> operCont = new HashSet<String>();
        String containerNo = null;
        VGMCommonVO vgmCommonVO = null;
        List<VgmVO> booknoList = null;
        Gson gson = new Gson();
        Map viewMap = null;
        List<VgmVO> viewList = null;
        List<VgmVO> containerList = null;
        Map tarwtMap = null;
        String blIsDummy = null;
        //
        try {
            list = new ArrayList<VgmVO>();
            Map<String, ArrayList<String>> containerMap = vgmService.getecomvgmoprbkglist(referenceNo);
            if (containerMap != null && containerMap.size() > 0) {
                System.out.println("[ECOM] EcomVGMBean  >>>yes value is there>>>>>");
                booknoList = gson.fromJson(gson.toJson((ArrayList) containerMap.get("operationdetail")), new TypeToken<List<VgmVO>>() {
                }.getType());
                if (booknoList != null && booknoList.size() > 0) {
                    for (VgmVO container : booknoList) {
                        operCont.add(container.getContainerno());
                    }
                }
            }
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            if ("blno".equalsIgnoreCase(type)) {
                viewMap = vgmService.getecomblvgmviewdets(referenceNo);
                if (viewMap != null && viewMap.size() > 0) {
                    viewList = gson.fromJson(gson.toJson((ArrayList) viewMap.get("bl")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                }
            } else {
                viewMap = vgmService.getecomvgmviewdetails(referenceNo);
                if (viewMap != null && viewMap.size() > 0) {
                    viewList = gson.fromJson(gson.toJson((ArrayList) viewMap.get("booking")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                }
            }
            System.out.println("[ECOM] EcomVGMBean  >>viewMap>>" + viewMap);
            Calendar cal = Calendar.getInstance();
            for (VgmVO vo : viewList) {
                containerNo = vo.getEqpid();
                containerNo = containerNo.toUpperCase();
                //
                if (!(set.contains(containerNo) || operCont.contains(containerNo))) {
                    vgmvo = new VgmVO();
                    System.out.println("[ECOM] EcomVGMBean  >>>vgmvo.getIsdummy()" + vo.getIsdummy());
                    if ("blno".equalsIgnoreCase(type)) {
                        blIsDummy = getTareWeight(containerNo, vo.getEqptypeid());
                        if (blIsDummy == null || blIsDummy.length() == 0) {
                            vgmvo.setDummyCntr(true);
                            vo.setIsdummy("Y");
                        } else {
                            vgmvo.setDummyCntr(false);
                            vo.setIsdummy("N");
                        }
                    } else {
                        if ("Y".equalsIgnoreCase(vo.getIsdummy())) {
                            System.out.println("[ECOM] EcomVGMBean  >>>entereed in true.");
                            vgmvo.setDummyCntr(true);
                        } else {
                            System.out.println("[ECOM] EcomVGMBean  >>>enterered in false>>");
                            vgmvo.setDummyCntr(false);
                        }
                    }
                    vgmvo.setBookNo(vo.getBook_no());
                    vgmvo.setEqpid(containerNo);
                    vgmvo.setTareWeight("");
                    tarwtMap = vgmService.getecomvalidcontainer(containerNo);
                    System.out.println("[ECOM] EcomVGMBean  >>>tarwtMap>>" + tarwtMap);
                    containerList = gson.fromJson(gson.toJson((ArrayList) tarwtMap.get("equipmentpassport")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                    System.out.println("[ECOM] EcomVGMBean  >>equipmentpassport>>" + containerList);
                    if (containerList != null && containerList.size() > 0) {
                        for (VgmVO locVO : containerList) {
                            vgmvo.setTareWeight(checkNull(locVO.getTareWeight()));

                        }
                    }
                    vgmvo.setEqptype(vo.getEqptypeid());
                    vgmvo.setVersion_no("0");
                    vgmvo.setVgm_weight("");
                    vgmvo.setShipperown(checkNull(vo.getShipperown()));
                    vgmvo.setVgm_unit("K");
                    vgmvo.setVgm_authorized_person("");
                    vgmvo.setVgm_obtained_method("SM1");
                    vgmvo.setVgm_company_name("");
                    vgmvo.setVgm_obtained_date(traget.format(cal.getTime()));
                    vgmvo.setAgencyid(vo.getAgencyid());
                    vgmvo.setOriginportid(vo.getOriginportid());
                    list.add(vgmvo);
                }
            }
            if (list != null && list.size() > 0) {
                List<VGMCommonVO> custList = null;
                Map custMap = null;
                if ("blno".equalsIgnoreCase(type)) {
                    custMap = vgmService.getecomblvgmcustdetails(referenceNo);
                    System.out.println("[ECOM] EcomVGMBean  <<custMap<<" + custMap);
                    if (custMap != null && custMap.size() > 0) {
                        custList = gson.fromJson(gson.toJson((ArrayList) custMap.get("bl")), new TypeToken<List<VGMCommonVO>>() {
                        }.getType());
                    }
                } else {
                    custMap = vgmService.getecomvgmcustdetails(referenceNo);

                    if (custMap != null && custMap.size() > 0) {
                        custList = gson.fromJson(gson.toJson((ArrayList) custMap.get("booking")), new TypeToken<List<VGMCommonVO>>() {
                        }.getType());
                    }
                }

                vgmCommonVO = new VGMCommonVO();
                vgmCommonVO.setShName("");
                vgmCommonVO.setSubmissionType(type);
                System.out.println("[ECOM] EcomVGMBean  >>type>>+" + type + ">>" + referenceNo);

                for (VGMCommonVO vo : custList) {
                    if ("blno".equalsIgnoreCase(type)) {
                        vgmCommonVO.setVgmStatus("");
                        vgmCommonVO.setBlNo(vo.getBl_no());
                    } else {
                        vgmCommonVO.setBlNo(null);
                        vgmCommonVO.setVgmStatus(vo.getStatus());
                    }
                    vgmCommonVO.setSubmissionType(type);
                    vgmCommonVO.setBookNo(vo.getBook_no());
                    vgmCommonVO.setOrigin(vo.getOriginportid());
                    vgmCommonVO.setDestination(vo.getFinalportid());
                    vgmCommonVO.setLoadPortId(vo.getLoadportid());
                    vgmCommonVO.setDischargePortId(vo.getDischargeportid());
                    if ("SH".equalsIgnoreCase(checkNull(vo.getCusttypeid()).trim())) {
                        System.out.println("[ECOM] EcomVGMBean  <<<vo.getName1()<<" + vo.getName());
                        vgmCommonVO.setShName(vo.getName());
                    } else if ("AP".equalsIgnoreCase(checkNull(vo.getCusttypeid()).trim())) {
                        System.out.println("[ECOM] EcomVGMBean  <<<vo.getName2()<<" + vo.getName());
                        vgmCommonVO.setApName(vo.getName());
                    }
                    System.out.println("[ECOM] EcomVGMBean  >>vgmCommonVO>>" + vgmCommonVO.getApName());
                }
                vgmCommonVO.setVgmContList(list);
            } else {
                vgmCommonVO = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vgmCommonVO;
    }

    @Override
    public String saveVGMDetails(VGMCommonVO vgmVO) throws RemoteException {
        String result = null;
        PreparedStatement ps = null;
        PreparedStatement psEcomSel = null, psEcomUpdate = null;
        ResultSet rs_ecomver = null;
        VgmVO vo = null;
        List<VgmVO> vgmList = null;
        Connection con_ecom = null;
        int version = 0;
        boolean isBookingQueryReq = false;
        Map<String, String> hmCon = new HashMap<String, String>();
        String containerNo;
        String eqpType;
        String bookNo;
        String blNo, submitType;
        String vgmStatus = "A";
        String oprStatus = null;
        Map commonServiceMap = null;
        List<VgmVO> commonList = null;
        List<VgmVO> containerLIst = null;
        VgmVO vgmVo = null;
        Gson gson = new Gson();
        Map commonMap = null;
        List list = null;
        boolean checkDraft = false;
        List<String> eqpList = null;
        try {
            result = "success";
            vgmList = vgmVO.getVgmContList();
            bookNo = vgmVO.getBookNo();
            blNo = vgmVO.getBlNo();
            submitType = vgmVO.getSubmissionType();
            String locVersion = null;
            //vgmList = null;
            if (vgmList != null) {
                System.out.println("[ECOM] EcomVGMBean  >>entereed in save....");
                con_ecom = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

                psEcomSel = con_ecom.prepareStatement(GETECOMVGMVERSION, 1004, 1007);
                psEcomUpdate = con_ecom.prepareStatement(UPDATEECOMVGMVERSION);
                for (int i = 0; i < vgmList.size(); i++) {
                    vo = vgmList.get(i);
                    vo.setVersion_no("0");
                    if (String.valueOf(vo.getVersion_no()).equalsIgnoreCase("0")) {
                        isBookingQueryReq = true;
                    }
                    //
                    commonServiceMap = null;
                    commonList = null;
                    System.out.println("[ECOM] EcomVGMBean  >>errroerr>>" + vo.getEqpid() + ">>error1>>>" + vo.getBookNo());
                    commonServiceMap = vgmService.getecomlrpvgmversion(vo.getBook_no(), vo.getEqpid());
                    System.out.println("[ECOM] EcomVGMBean  >>commonServiceMap>>" + commonServiceMap);
                    if (commonServiceMap != null && commonServiceMap.size() > 0) {
                        commonList = gson.fromJson(gson.toJson((ArrayList) commonServiceMap.get("vgmsolas")), new TypeToken<List<VgmVO>>() {
                        }.getType());
                        if (commonList != null && commonList.size() > 0) {
                            vgmVo = commonList.get(0);
                            version = 0;
                            locVersion = checkNull(vgmVo.getVersion_no()).length() > 0 ? vgmVo.getVersion_no() : "0";
                            if (Integer.parseInt(locVersion) > 0) {
                                version = Integer.parseInt(locVersion);
                            }
                            System.out.println("[ECOM] EcomVGMBean  >>version>>" + version);
                        } else {
                            version = 0;
                        }
                        vo.setVersion_no(String.valueOf(version + 1));
                    }

                    psEcomSel.setString(1, vo.getBookNo());
                    psEcomSel.setString(2, vo.getEqpid());
                    rs_ecomver = psEcomSel.executeQuery();

                    if (rs_ecomver.next()) {
                        psEcomUpdate.setString(1, vo.getBookNo());
                        psEcomUpdate.setString(2, vo.getEqpid());
                        psEcomUpdate.addBatch();
                    }
                    vgmList.set(i, vo);
                }
                psEcomUpdate.executeBatch();
                //
                if (isBookingQueryReq) {
                    if ("blno".equalsIgnoreCase(submitType)) {
                        containerLIst = null;
                        commonMap = vgmService.getecomblvgmcontainers(blNo);
                        if (commonMap != null && commonMap.size() > 0) {
                            containerLIst = gson.fromJson(gson.toJson((ArrayList) commonMap.get("bl_cargo")), new TypeToken<List<VgmVO>>() {
                            }.getType());
                        }
                    } else {
                        containerLIst = null;
                        commonMap = vgmService.getecombkgvgmcontainers(bookNo);
                        if (commonMap != null && commonMap.size() > 0) {
                            containerLIst = gson.fromJson(gson.toJson((ArrayList) commonMap.get("booking_cargo")), new TypeToken<List<VgmVO>>() {
                            }.getType());
                        }
                    }
                    for (VgmVO viewVO : containerLIst) {
                        containerNo = checkNull(viewVO.getEqpid());
                        containerNo = containerNo.toUpperCase();
                        hmCon.put(containerNo, viewVO.getEqptypeid());
                    }
                }
                //set VGM status
                if (!("blno".equalsIgnoreCase(submitType)) && "D".equalsIgnoreCase(vgmVO.getVgmStatus())) {
                    vgmStatus = "C";
                    oprStatus = "E";
                    commonMap = vgmService.getEcomVgmEqpdummy(bookNo);
                    if (commonMap != null) {
                        list = (List) commonMap.get("booking_cargo");
                        if (list != null) {
                            eqpList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                commonMap = (Map) list.get(i);
                                eqpList.add(checkNull((String) commonMap.get("eqpid")).trim());
                            }
                        }
                    }
                }

                ps = con_ecom.prepareStatement(INSERTECOMVGMSOLAS);
                for (int i = 0; i < vgmList.size(); i++) {
                    vo = vgmList.get(i);
                    containerNo = checkNull(vo.getEqpid());
                    eqpType = checkNull(vo.getEqptype());
                    System.out.println("[ECOM] EcomVGMBean  >>>vo.getVersionNo()>>>" + vo.getVersion_no() + ">>vo.getObtainedDate()>>>" + vo.getVgm_obtained_date());
                    eqpType = eqpType.length() > 0 ? eqpType : hmCon.get(containerNo.toUpperCase());
                    blNo = blNo == null || blNo.length() <= 0 || blNo.equalsIgnoreCase("null") ? null : blNo;
                    if (eqpList != null && eqpList.contains(containerNo.trim())) {
                        vgmStatus = "A";
                        oprStatus = "E";
                    }
                    //
                    ps.setString(1, bookNo);
                    ps.setString(2, blNo);
                    ps.setString(3, eqpType);
                    ps.setString(4, containerNo);
                    ps.setString(5, "C");
                    ps.setString(6, submitType);
                    ps.setString(7, vo.getVgm_ref_number());
                    ps.setString(8, vo.getVgm_order_number());
                    ps.setString(9, vo.getVgm_obtained_date());
                    ps.setString(10, vo.getVgm_weight());
                    ps.setString(11, vo.getVgm_unit());
                    ps.setString(12, vo.getVgm_obtained_method());
                    ps.setString(13, checkNull(vo.getVgm_company_name()).toUpperCase());
                    ps.setString(14, vo.getVgm_address1());
                    ps.setString(15, vo.getVgm_address2());
                    ps.setString(16, vo.getVgm_zipcode());
                    ps.setString(17, vo.getVgm_city());
                    ps.setString(18, vo.getVgm_country());
                    ps.setString(19, checkNull(vo.getVgm_authorized_person()).toUpperCase());
                    ps.setString(20, vo.getTelephone());
                    ps.setString(21, vo.getFax());
                    ps.setString(22, vo.getEmail());
                    ps.setString(23, vgmStatus);
                    ps.setString(24, vo.getVersion_no());
                    ps.setString(25, vgmVO.getLoginUser());
                    ps.setString(26, vgmVO.getLoginUser());
                    ps.setString(27, oprStatus);
                    ps.addBatch();
                }
                ps.executeBatch();

                if (con_ecom != null) {
                    con_ecom.close();
                }
                SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if ("1".equalsIgnoreCase(vgmVO.getAppVersion())) {
                    result = v1VGMBeanObj.saveVGMDetails(vgmVO);
                } else {
                    //insert vgm details into LRP db
                    System.out.println("[ECOM] EcomVGMBean  >>>vgmList>>>" + vgmList.size());
                    commonList = new ArrayList<VgmVO>();
                    for (int i = 0; i < vgmList.size(); i++) {
                        vo = vgmList.get(i);
                        containerNo = checkNull(vo.getEqpid());
                        eqpType = checkNull(vo.getEqptype());
                        eqpType = eqpType.length() > 0 ? eqpType : hmCon.get(containerNo.toUpperCase());
                        if (eqpList != null && eqpList.contains(containerNo.trim())) {
                            vgmStatus = "A";
                            oprStatus = "E";
                        }
                        //
                        System.out.println("[ECOM] EcomVGMBean  BookNo: " + vo.getBookNo() + ", EqpType: " + eqpType + ", ContainerNo: " + containerNo
                                + ", ObtainedDate: " + vo.getVgm_obtained_date() + ", VgmWeight: " + vo.getVgm_weight() + ", VgmUnit: " + vo.getVgm_unit()
                                + ", ObtainedMethod: " + vo.getVgm_obtained_method() + ", AuthorizedPerson: " + vo.getVgm_authorized_person());
                        //
                        vgmVo = new VgmVO();
                        vgmVo.setBook_no(bookNo);
                        vgmVo.setEqptype(eqpType);
                        vgmVo.setEqpid(containerNo);
                        vgmVo.setVgm_source("C");
                        vgmVo.setVgm_ref_number(checkNull(vo.getVgm_ref_number()));
                        vgmVo.setVgm_order_number(checkNull(vo.getVgm_order_number()));
                        vgmVo.setVgm_obtained_date(checkNull(vo.getVgm_obtained_date()));
                        vgmVo.setVgm_weight(checkNull(vo.getVgm_weight()));
                        vgmVo.setVgm_unit(checkNull(vo.getVgm_unit()));
                        vgmVo.setVgm_obtained_method(checkNull(vo.getVgm_obtained_method()));
                        vgmVo.setVgm_company_name(checkNull(vo.getVgm_company_name()).toUpperCase());
                        System.out.println("[ECOM] EcomVGMBean  >>>vo.getVGM_Address1()>>" + vo.getVgm_address1()+">>>vo.getVGM_Address2()>>" + vo.getVgm_address2());
                        vgmVo.setVgm_address1(checkNull(vo.getVgm_address1()));
                        vgmVo.setVgm_address2(checkNull(vo.getVgm_address2()));
                        vgmVo.setVgm_zipcode(checkNull(vo.getVgm_zipcode()));
                        vgmVo.setVgm_city(checkNull(vo.getVgm_city()));
                        vgmVo.setVgm_country(checkNull(vo.getVgm_country()));
                        vgmVo.setVgm_authorized_person(checkNull(vo.getVgm_authorized_person()).toUpperCase());
                        vgmVo.setTelephone(checkNull(vo.getTelephone()));
                        vgmVo.setFax(checkNull(vo.getFax()));
                        vgmVo.setEmail(checkNull(vo.getEmail()));
                        vgmVo.setVgm_type("");
                        vgmVo.setStatus(vgmStatus);
                        vgmVo.setVersion_no(vo.getVersion_no());
                        vgmVo.setUsercr("ECOM");
                        vgmVo.setUserup("ECOM");
                        vgmVo.setOprstatus(checkNull(oprStatus));
                        vgmVo.setDatecr(traget.format(new Date()));
                        vgmVo.setDateup(traget.format(new Date()));
                        commonList.add(vgmVo);
                    }
                    VGMVOModel vgmModel = new VGMVOModel();
                    vgmModel.setVgmsolas(commonList);
                    Map updateMap = vgmService.setecomvgmlrpversion(vgmModel);
                    System.out.println("[ECOM] EcomVGMBean  >>updateMap>>updateMap" + updateMap);
                    Map resultMap = vgmService.setecomvgmlrpdetails(vgmModel);
                    System.out.println("[ECOM] EcomVGMBean  >>resultMap>>" + resultMap);
                }
                FileOutputStream fos = null;
                boolean flag = false;
                String filepath = "";
                String fileName = vgmVO.getVgmFileName();
                if (vgmVO.getVgmFileData() != null && vgmVO.getVgmFileName() != null && vgmVO.getVgmFileName().length() > 0) {
                    fileName = bookNo + "_" + EcomConsts.VGM_FILE_UPLOAD + "_" + vgmVO.getVgmFileName();
                    File Ffilepath1 = new File(vgmVO.getFilePath(), fileName);
                    fos = new FileOutputStream(Ffilepath1);
                    fos.write(vgmVO.getVgmFileData());
                    flag = true;
                }
                if (flag) {
                    fos.flush();
                    fos.close();
                }
            }

        } catch (Exception e) {
            result = "failure";
            e.printStackTrace();
            context.setRollbackOnly();
        } finally {
            try {
                if (rs_ecomver != null) {
                    rs_ecomver.close();
                }
                if (psEcomSel != null) {
                    psEcomSel.close();
                }
                if (psEcomUpdate != null) {
                    psEcomUpdate.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con_ecom != null) {
                    con_ecom.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String checkLoadConfirm(String bookNo, String blNo, String submissionType, String custCode) throws RemoteException {
        String result = null;
        String isLoadConfirm = "NO";
        int loadCount = 0;
        Gson gson = new Gson();
        int nonLoadCount = 0;
        String status = null;
        boolean isValidBooking = false;
        Map loadConfirmationMap = null;
        List<VgmVO> loadConfirmList = null;
        Map bookBLMap = null;
        ArrayList noList = null;
        List<VgmVO> blList = null;
        try {
            System.out.println("[ECOM] EcomVGMBean  >>entereed in bl no check>>");
            if ("blno".equalsIgnoreCase(submissionType)) {
                if (custCode != null && checkNull(custCode).length() > 0) {
                    bookBLMap = vgmService.getecomvgmblnocheck(blNo, custCode);
                } else {
                    bookBLMap = vgmService.getecomvgmblnocheck(blNo);
                }
                System.out.println("[ECOM] EcomVGMBean  <>>bookBLMap>>" + bookBLMap);
                blList = gson.fromJson(gson.toJson((ArrayList) bookBLMap.get("bl")), new TypeToken<List<VgmVO>>() {
                }.getType());
                System.out.println("[ECOM] EcomVGMBean  >>blList>>" + blList);
                if (blList != null && blList.size() > 0) {
                    VgmVO vo = (VgmVO) blList.get(0);
                    isValidBooking = true;
                    bookNo = checkNull(vo.getBook_no());
                } else {
                    isValidBooking = false;
                    bookNo = null;
                }
                System.out.println("[ECOM] EcomVGMBean  >>>bookNo>." + bookNo);
            } else {
                System.out.println("[ECOM] EcomVGMBean  >check load confirm>custCode>>" + custCode);
                //VDS CHANGES //DEEPA
                if (custCode != null && checkNull(custCode).length() > 0) {
                    bookBLMap = vgmService.getecomvgmbkgnocheck(bookNo, custCode);
                } else {
                    bookBLMap = vgmService.getecomvgmbkgnocheck(bookNo);
                }
                if (bookBLMap != null) {
                    noList = (ArrayList) bookBLMap.get("booking");
                    System.out.println("[ECOM] EcomVGMBean  >>bookList>>" + noList);
                    if (noList != null && noList.size() > 0) {
                        isValidBooking = true;
                    } else {
                        isValidBooking = false;
                    }
                }
            }
            System.out.println("[ECOM] EcomVGMBean  >>isValidBooking>>" + isValidBooking);
            if (isValidBooking) {
                if (bookNo != null && bookNo.length() > 0) {
                    //VDS CHANGES//dEEPA
                    loadConfirmationMap = vgmService.getecomvgmloadconfirmstatus(bookNo);
                    if (loadConfirmationMap != null && loadConfirmationMap.size() > 0) {
                        loadConfirmList = gson.fromJson(gson.toJson((ArrayList) loadConfirmationMap.get("booking_cargo")), new TypeToken<List<VgmVO>>() {
                        }.getType());
                        System.out.println("[ECOM] EcomVGMBean  >>loadConfirmList>>" + loadConfirmList);
                    }
                    for (VgmVO vgmvo : loadConfirmList) {
                        status = vgmvo.getStatus();
                        System.out.println("[ECOM] EcomVGMBean  >>status>>" + status);
                        if ("L".equalsIgnoreCase(status)) {
                            loadCount = 1;
                        } else {
                            nonLoadCount = 1;
                        }
                    }
                    //
                    if (loadCount == 1 && nonLoadCount == 0) {
                        isLoadConfirm = "YES";
                    }
                } else {
                    isLoadConfirm = "NO";
                }
            } else {
                isLoadConfirm = "NOTVALIDBOOKING";
            }
            System.out.println("[ECOM] EcomVGMBean  >>>isLoadConfirm>>>" + isLoadConfirm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLoadConfirm;
    }
    //

    @Override
    public String checkDuplicateContainers(List<String> containerList, String refNo, String type, Map<String, String> eqpTypeMap) throws RemoteException {
        String result = null;
        Set<String> set = new HashSet<String>();
        String container = null;
        StringBuffer stb = new StringBuffer();
        boolean isValidationFail = false;
        int size = 0;
        int isFirst = 0;
        int cntrInc = 0;
        String cntrNo = null;
        String bookNo = null;
        List<VgmVO> containerNoList = null;
        Map getBLMap = null;
        Map commonMap = null, getMap = null;
        List<VgmVO> commonList = null;
        Gson gson = new Gson();
        try {
            if (containerList != null && containerList.size() > 0) {
                for (String cntr : containerList) {
                    if (cntrInc == 0) {
                        cntrNo = cntr;
                    } else {
                        cntrNo = cntrNo + "," + cntr;
                    }
                    cntrInc++;
                }
            }
            getMap = vgmService.getecomvalidcontainer(cntrNo);
            System.out.println("[ECOM] EcomVGMBean  >>>getMap>>" + getMap);
            commonList = gson.fromJson(gson.toJson((ArrayList) getMap.get("equipmentpassport")), new TypeToken<List<VgmVO>>() {
            }.getType());
            System.out.println("[ECOM] EcomVGMBean  >>equipmentpassport>>" + commonList);
            if (commonList != null && commonList.size() > 0) {
                for (VgmVO locVO : commonList) {
                    if (locVO.getEquipmenttype().equalsIgnoreCase(eqpTypeMap.get(locVO.getContainerno().toUpperCase()))) {
                        eqpTypeMap.remove(locVO.getContainerno().toUpperCase());
                    }
                }
                System.out.println("[ECOM] EcomVGMBean  >>>eqpTypeMap>>>" + eqpTypeMap);
                if (eqpTypeMap == null || eqpTypeMap.size() == 0) {
                    result = "Y";
                } else {
                    cntrNo = null;
                    cntrInc = 0;
                    for (Map.Entry<String, String> entry : eqpTypeMap.entrySet()) {
                        if (cntrInc == 0) {
                            cntrNo = entry.getKey();
                        } else {
                            cntrNo = cntrNo + "," + entry.getKey();
                        }
                        cntrInc++;
                    }
                    result = "Invalid Container Nos-" + cntrNo;
                }
            } else {
                cntrNo = null;
                cntrInc = 0;
                for (Map.Entry<String, String> entry : eqpTypeMap.entrySet()) {
                    if (cntrInc == 0) {
                        cntrNo = entry.getKey();
                    } else {
                        cntrNo = cntrNo + "," + entry.getKey();
                    }
                    cntrInc++;
                }
                result = "Invalid Container Nos-" + cntrNo;
            }

            if (result.equalsIgnoreCase("Y")) {
                System.out.println("[ECOM] EcomVGMBean  >>>valid container numbers");
                if ("blno".equalsIgnoreCase(container)) {
                    getBLMap = vgmService.getecombkgnofrombl(refNo);
                    commonList = gson.fromJson(gson.toJson((ArrayList) getBLMap.get("bl")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                    System.out.println("[ECOM] EcomVGMBean  >>blList>>" + commonList);
                    if (commonList != null && commonList.size() > 0) {
                        VgmVO vo = (VgmVO) commonList.get(0);
                        bookNo = checkNull(vo.getBook_no());
                    }
                    commonList = null;
                    commonMap = vgmService.getecomblvgmcontainers(refNo);
                    commonList = gson.fromJson(gson.toJson((ArrayList) commonMap.get("bl_cargo")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                } else {
                    bookNo = refNo;
                    commonList = null;
                    commonMap = vgmService.getecombkgvgmcontainers(refNo);
                    commonList = gson.fromJson(gson.toJson((ArrayList) commonMap.get("booking_cargo")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                }

                if (commonList != null && commonList.size() > 0) {
                    for (VgmVO vo : commonList) {
                        set.add(vo.getEqpid().trim());
                    }
                }
                System.out.println("[ECOM] EcomVGMBean  <<<set<<" + set);
                //
                /**
                 * size = containerList.size(); for (int i = 0; i < size; i++) {
                 * container = containerList.get(i).trim().toUpperCase();
                 * if (!set.contains(container)) {
                 * if (isFirst > 0) { stb.append(","); }
                 * stb.append(containerList.get(i)); isFirst = 1;
                 * isValidationFail = true; } } // stb.append("-");*
                 */
                isFirst = 0;
                set.clear();
                //
                Map containerMap = vgmService.getecomvgmoprbkglist(bookNo);
                System.out.println("[ECOM] EcomVGMBean  <<<>>>containerMap>>>" + containerMap);
                if (containerMap != null && containerMap.size() > 0) {
                    containerNoList = gson.fromJson(gson.toJson((ArrayList) containerMap.get("operationdetail")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                    if (containerNoList != null && containerNoList.size() > 0) {
                        for (VgmVO containerno : containerNoList) {
                            set.add(containerno.getContainerno());
                        }
                    }
                }
                size = containerList.size();
                for (int i = 0; i < size; i++) {
                    container = containerList.get(i).trim().toUpperCase();
                    if (set.contains(container)) {
                        if (isFirst > 0) {
                            stb.append(",");
                        }
                        stb.append(containerList.get(i));
                        System.out.println("[ECOM] EcomVGMBean  >>>>sb" + stb.toString());
                        isFirst = 1;
                        isValidationFail = true;
                    }
                }
                //
                if (isValidationFail) {
                    result = stb.toString();
                } else {
                    result = "Valid";
                }
            }
            System.out.println("[ECOM] EcomVGMBean  result>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, String> getBCCMailidForAgency(String agencyCode, String originLocation) {
        Map<String, String> mailMap = null;
        String originAgency = null;
        boolean checkFirst = true, mailCheck = false;
        String agencymailId = null;
        Map servicMailMap = null;
        List<VgmVO> mailList = null;
        VgmVO mailVo = null;
        Gson gson = new Gson();
        try {
            mailMap = new HashMap<String, String>();
            servicMailMap = vgmService.getecomvgmoriginagency(originLocation);
            System.out.println("[ECOM] EcomVGMBean  <<<>>>agency>>>" + servicMailMap);
            if (servicMailMap != null && servicMailMap.size() > 0) {
                mailList = gson.fromJson(gson.toJson((ArrayList) servicMailMap.get("agencylocations")), new TypeToken<List<VgmVO>>() {
                }.getType());
                if (mailList != null && mailList.size() > 0) {
                    mailVo = mailList.get(0);
                    originAgency = mailVo.getAgencycode();
                }
            }
            servicMailMap = null;
            mailList = null;
            servicMailMap = vgmService.getecomvgmagencymail(agencyCode);
            System.out.println("[ECOM] EcomVGMBean  <<<>>>agency>>>" + servicMailMap);
            if (servicMailMap != null && servicMailMap.size() > 0) {
                mailList = gson.fromJson(gson.toJson((ArrayList) servicMailMap.get("agencycontacts")), new TypeToken<List<VgmVO>>() {
                }.getType());
                if (mailList != null && mailList.size() > 0) {
                    for (VgmVO vgmvo : mailList) {
                        mailCheck = true;
                        if (checkFirst) {
                            checkFirst = false;
                            agencymailId = checkNull(vgmvo.getContactemailid());
                        } else {
                            agencymailId = agencymailId + "," + checkNull(vgmvo.getContactemailid());
                        }
                    }
                }
            }
            System.out.println("[ECOM] EcomVGMBean  >>>originAgency>>" + originAgency);
            if (mailCheck) {
                mailMap.put("BOOKINGAGENCY", agencymailId);
            } else {
                mailMap.put("BOOKINGAGENCY", "");
            }
            mailMap.put("ORIGINAGENCY", "");
            if (originAgency != null && originAgency.equalsIgnoreCase(agencyCode)) {
                mailMap.put("AGENCYEQUAL", "Equal");
            } else {
                mailMap.put("AGENCYEQUAL", "Not Equal");
                agencymailId = null;
                mailCheck = false;
                checkFirst = true;
                servicMailMap = null;
                mailList = null;
                servicMailMap = vgmService.getecomvgmagencymail(originAgency);
                System.out.println("[ECOM] EcomVGMBean  <<<>>>agency>>>" + servicMailMap);
                if (servicMailMap != null && servicMailMap.size() > 0) {
                    mailList = gson.fromJson(gson.toJson((ArrayList) servicMailMap.get("agencycontacts")), new TypeToken<List<VgmVO>>() {
                    }.getType());
                    if (mailList != null && mailList.size() > 0) {
                        for (VgmVO vgmvo : mailList) {
                            mailCheck = true;
                            if (checkFirst) {
                                checkFirst = false;
                                agencymailId = checkNull(vgmvo.getContactemailid());
                            } else {
                                agencymailId = agencymailId + "," + checkNull(vgmvo.getContactemailid());
                            }
                        }
                    }
                }
                if (mailCheck) {
                    mailMap.put("ORIGINAGENCY", agencymailId);
                } else {
                    mailMap.put("ORIGINAGENCY", "");
                }
            }
            System.out.println("[ECOM] EcomVGMBean  mailMap>>>" + mailMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailMap;
    }

    @Override
    public String getTareWeight(String containerNo, String eqpType) throws RemoteException {
        String result = null;
        Map tarwtMap = null;
        Gson gson = new Gson();
        List<VgmVO> containerList = null;
        try {
            result = "";
            tarwtMap = vgmService.getEcomTarewt(containerNo, eqpType);
            System.out.println("[ECOM] EcomVGMBean  >>>gettarwtMap>>" + tarwtMap);
            containerList = gson.fromJson(gson.toJson((ArrayList) tarwtMap.get("equipmentpassport")), new TypeToken<List<VgmVO>>() {
            }.getType());
            System.out.println("[ECOM] EcomVGMBean  >>getequipmentpassport>>" + containerList);
            if (containerList != null && containerList.size() > 0) {
                for (VgmVO locVO : containerList) {
                    result = checkNull(locVO.getTareWeight());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String checkVGMContainerNo(String containerNo, String eqpType) throws RemoteException {
        String result = null;
        List list = null;
        try {
            Map<String, List> cntrMap = null;
            System.out.println("[ECOM] EcomVGMBean  >>cntrMap>>" + cntrMap);
            if (cntrMap != null && cntrMap.size() > 0) {
                list = cntrMap.get("equipmentpassport");
                if (list != null && list.size() > 0) {
                    System.out.println("[ECOM] EcomVGMBean  >>>equipment>>" + list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String getAgencyMailForVGM(String agencycode) {
        String email = "";
        try {
            System.out.println("[ECOM] EcomVGMBean  insidevgmagencymail");
            int i = 0;
            Map hs = (Map) vgmService.getEcomVGMAgencyMail(agencycode);
            List lst = (List) hs.get("agencycontacts");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map vgm = (Map) lst.get(j);
                    String eml = (String) vgm.get("contactemailid");
                    if (i == 0) {
                        email = eml;
                    } else {
                        email = email + "," + eml;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
        }
        return email;
    }

    @Override
    public String togetTareWtForCntr(String cntrNo, String eqpSize, String nor, String soc) throws RemoteException {
        String tareWt = null, neweqptype = null, coneqptype = null, IsSOC = null;
        Map rs = null;
        try {
            Map map = (Map) vgmService.getEcomTareWeightForSI(cntrNo);
            System.out.println("[ECOM] EcomVGMBean  ######>" + map);
            List lst = (List) map.get("containerprofile");
            if (lst != null && lst.size() > 0) {
                rs = (Map) lst.get(0);
                coneqptype = checkNull((String) rs.get("equipmenttype")).trim();
                IsSOC = checkNull((String) rs.get("shipperownedunit")).trim();
                if (!"Y".equalsIgnoreCase(IsSOC)) {
                    tareWt = checkNull((String) rs.get("tarewt")).trim();
                } else {
                    tareWt = "soc";
                }

            }
//            

            if ((tareWt == null || tareWt.trim().length() == 0) && (!"Y".equalsIgnoreCase(soc) || (cntrNo.contains(eqpSize + "TNB") || (cntrNo.contains(eqpSize + "TBN"))))) {
                map = null;
                lst = null;
                rs = null;
                map = (Map) vgmService.getEcomTarewtFromEqp(eqpSize);
                System.out.println("[ECOM] EcomVGMBean  ######>" + map);
                lst = (List) map.get("ecom_equipments");
                if (lst != null && lst.size() > 0) {
                    rs = (Map) lst.get(0);
                    tareWt = checkNull((String) rs.get("tarekg")).trim();
                }
            }
            System.out.println("[ECOM] EcomVGMBean  finaltarewtresult" + tareWt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tareWt;
    }

}
