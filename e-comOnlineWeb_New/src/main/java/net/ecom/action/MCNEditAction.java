/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author prakash.mani
 */
package net.ecom.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.NumberComparator;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.SIEditForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BLBeanRemote;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.SIBLMasterVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MCNEditAction extends org.apache.struts.action.Action {

    EcomBLBeanRemote locobj = EJBLookUp.getBLBeanLookUp();
    EcomShippingInstructionBeanRemote siObj = EJBLookUp.getSIBeanRemoteLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomV1BLBeanRemote v1BLObj = EJBLookUp.getV1BLBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();

    Map stateMap = null, stateMapCountry = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        String isHeader = null, shipperSealShipmentType = "";
        String isHSCodeShow = "NO", isShipperSeal, shipperSealConfig;;
        SIBLMasterVO masterVO = null;
        String pol = null, pod = null, polCountry, podCountry, headerShipmentType = "", hscodeShipmentType = "", headerConfigResult = null, hscodeConfigResult = null, loadportcountry = null;
        Map referencemap = null;
        System.out.println("[ECOM-WAR] MCNEditAction  =====Started to Perform BL Actions(View/Edit)======");
        String sino = null;
        try {
            HashMap SI_Addremarks = null;

            SIEditForm siform = (SIEditForm) form;
            setEmptyValuesForCust(siform);
            HttpSession session = request.getSession();
            saveToken(request);
            String type = Validation.checkNull(request.getParameter("type")); 
            String blno = Validation.checkNull(request.getParameter("blno"));
            String appVersion = request.getParameter("appversion"); 
            String bookno = Validation.checkNull(request.getParameter("bookno"));
            String fcnno = Validation.checkNull(request.getParameter("fcnno"));
            System.out.println("[ECOM-WAR] MCNEditAction  >>fcnno>>" + fcnno +  "===bl action type" + type + 
                   "===blno is==>" + blno + " bookno " + bookno + ">> appVersion >> " + appVersion);
            ServletContext contexts = servlet.getServletContext();
            SIPartiesVO parties_routing1 = null;
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            String fakCommodity = (String) contexts.getAttribute(GeneralConf.FAKCOMMODITY);
            String SHOW_CHARGES_IN_SIBL = (String) contexts.getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
            String hidefaxno = (String) contexts.getAttribute(GeneralConf.HideFaxNo);
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            String fakComdtyCode = "";
            String fakComdtyName = "";
            System.out.println("[ECOM-WAR] MCNEditAction  fakCommodity>>>>" + fakCommodity);
            fakCommodity = fakCommodity != null ? fakCommodity : "";
            if (fakCommodity != null && fakCommodity.length() > 0) {
                String fakCommodityArr[] = fakCommodity.split("~~");
                if (fakCommodityArr.length == 2) {
                    fakComdtyCode = fakCommodityArr[0].trim();
                    fakComdtyName = fakCommodityArr[1].trim();
                }
            }
            //V1 Cache
            Map<String, Map<String, String>> masterMap = (Map<String, Map<String, String>>) contexts.getAttribute("ECOMV1MASTERCACHE");
            Map<String, String> v1ComdtyMap = masterMap.get("COMMODITY");

            //header config
            Map<String, String> fieldShipmentMap = (Map<String, String>) contexts.getAttribute("FIELDCONFIGSHIPMENTMAP");
            //
            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
            //System.out.println("[ECOM-WAR] MCNEditAction  <>>>vessellist>>>="+vessellist);
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PORTLIST");
            Map<SIMasterVO, SIConatainerValueVO> siDraftCntInfoHashObj = null;
            HashMap bk_port_details = null;
            Map<SIMasterVO, SIConatainerValueVO> siCntInfoHashObj = null;
            HashMap SI_CustDetails = null;
            String pla_free_txt = null, pol_free_txt = null, pod_free_txt = null, pld_free_txt = null, voyage_free_txt = null;

            Set blSet = (Set) session.getAttribute("blAccessList");
            Set dashBoardSet = (Set) session.getAttribute("dashBoardAccessList");
            //if ((blSet != null && blSet.contains(blno)) || (dashBoardSet != null && dashBoardSet.contains(blno))) {
            if ("view".equalsIgnoreCase(type) || "blamend".equalsIgnoreCase(type)) {
                if ("1".equalsIgnoreCase(appVersion)) {
                    bk_port_details = (HashMap) v1BLObj.fetchV1PortDetForBL(blno);
                } else {
                    bk_port_details = (HashMap) locobj.fetchPortDetForBL(blno);
                }

                sino = locobj.getBLVersionNumber(blno);
                System.out.println("[ECOM-WAR] MCNEditAction  ===sino is=========>" + sino);
                SI_CustDetails = locobj.fetchSICustomerDetails(bookno, sino);
                siDraftCntInfoHashObj = locobj.fetchSIContainerDetail(blno, sino);
                siCntInfoHashObj = (Map<SIMasterVO, SIConatainerValueVO>) siDraftCntInfoHashObj;
                List filenamelist = ecomCntrlObj.getSubmittedFileNames(sino, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH), "Ecom");
                request.setAttribute("FILENAMELIST", filenamelist);

                SI_Addremarks = locobj.fetchAddremarks(sino, type);
                session.setAttribute("BLtype", "view");
                parties_routing1 = (SIPartiesVO) siObj.getSIMasterDetails(sino);
                SIPartiesVO routingVO = (SIPartiesVO) bk_port_details.get("routing");
                request.setAttribute("SIAGENCYREFNO", routingVO.getAgencyRefNo());
                session.setAttribute("SIAGENCYREFNO_For_Mail", routingVO.getAgencyRefNo());
                pol = Validation.checkNull(routingVO.getPolName());
                pod = Validation.checkNull(routingVO.getPodName());
                loadportcountry = Validation.checkNull(routingVO.getPol());
                System.out.println("[ECOM-WAR] MCNEditAction  >>routingVO.getReceipt()>>" + routingVO.getReceipt()+ " parties_routing1.getPla_free_txt() >>" + parties_routing1.getPla_free_txt());
                if (!"1".equalsIgnoreCase(appVersion)) {
                    routingVO.setPolName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPolName()).toUpperCase())));
                    routingVO.setPodName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPodName()).toUpperCase())));
                }
                routingVO.setIn_origin(Validation.checkNull(routingVO.getIn_origin()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getIn_origin()).toUpperCase())));
                routingVO.setIn_delivery(Validation.checkNull(routingVO.getIn_delivery()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getIn_delivery()).toUpperCase())));
                System.out.println("[ECOM-WAR] MCNEditAction  >>>routingVO.getVesselName()>>>" + routingVO.getVesselName());
                routingVO.setVesselName(Validation.checkNull((String) vessellist.get(routingVO.getVesselName())));
                routingVO.setPolat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPolat()));
                routingVO.setPodat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPodat()));
                pla_free_txt = parties_routing1.getPla_free_txt() != null && parties_routing1.getPla_free_txt().length() > 0 ? parties_routing1.getPla_free_txt().trim() : routingVO.getReceipt();
                pol_free_txt = parties_routing1.getPol_free_txt() != null && parties_routing1.getPol_free_txt().length() > 0 ? parties_routing1.getPol_free_txt().trim() : routingVO.getPolName();
                pod_free_txt = parties_routing1.getPod_free_txt() != null && parties_routing1.getPod_free_txt().length() > 0 ? parties_routing1.getPod_free_txt().trim() : routingVO.getPodName();
                pld_free_txt = parties_routing1.getPld_free_txt() != null && parties_routing1.getPld_free_txt().length() > 0 ? parties_routing1.getPld_free_txt().trim() : routingVO.getDelivery();
                voyage_free_txt = routingVO.getVesselName() + "/" + routingVO.getVoyagecode() + "/" + routingVO.getBound();
                siform.setPla_free_txt(pla_free_txt);
                siform.setPol_free_txt(pol_free_txt);
                siform.setPod_free_txt(pod_free_txt);
                siform.setPld_free_txt(pld_free_txt);
                siform.setVslvoyage_free_txt(voyage_free_txt);
                siform.setHid_pla_free_txt(routingVO.getReceipt());
                siform.setHid_pol_free_txt(routingVO.getPolName());
                siform.setHid_pod_free_txt(routingVO.getPodName());
                siform.setHid_pld_free_txt(routingVO.getDelivery());
                siform.setSino(sino);
                siform.setHid_vslvoyage_free_txt(routingVO.getVesselName() + " " + routingVO.getVoyagecode() + "/" + routingVO.getBound());
                siform.setAESDisclaimer(parties_routing1.getAESDiscliamer());
                siform.setSiAESnumber(parties_routing1.getAESNumber());
                siform.setReleasetype(parties_routing1.getReleasetype());
                siform.setDeclaredValue(parties_routing1.getDeclaredValue());
                siform.setAcidValue(parties_routing1.getACID());
                siform.setBltype(Validation.checkNull(parties_routing1.getBltype()));
                siform.setCargoprotectid(parties_routing1.getCargoprotectid());
                masterVO = (SIBLMasterVO) SI_CustDetails.get("MASTERDETAILS");
                if (masterVO != null) {
                    siform.setHeaderShipper(Validation.checkNull(masterVO.getShipperHeader()));
                    siform.setHeaderConsignee(Validation.checkNull(masterVO.getConsigneeHeader()));
                    siform.setHeaderNotify(Validation.checkNull(masterVO.getNotifyHeader()));
                    siform.setSpnameheader(Validation.checkNull(masterVO.getShipperName()));
                    siform.setConsnameheader(Validation.checkNull(masterVO.getConsigneeName()));
                    siform.setNpnameheader(Validation.checkNull(masterVO.getNotifyName()));
                }
                if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {
                    request.setAttribute("CHARGEDETAILS", siObj.getSICharges(sino));
                }

            } else {
                if ("1".equalsIgnoreCase(appVersion)) {
                    bk_port_details = (HashMap) v1BLObj.fetchV1PortDetForBL(blno);
                    SI_CustDetails = v1BLObj.fetchV1BLDetForSI(blno);
                    siCntInfoHashObj = v1BLObj.fetchContainerDetailForSI(blno);
                    parties_routing1 = (SIPartiesVO) v1BLObj.getV1BLMasterDetails((blno));
                } else {
                    System.out.println("[ECOM-WAR] MCNEditAction  ~~~~FCNNO length ~~~~~" + fcnno.length());
                    if (fcnno.length() > 0) {
                        bk_port_details = (HashMap) locobj.fetchPortDetForMCN(fcnno);
                        SI_CustDetails = locobj.fetchBookingDetForMCN(fcnno);
                        SI_Addremarks = locobj.fetchAddremarks(fcnno, type);
                        siCntInfoHashObj = locobj.fetchMCNContainerDetailForDraft(fcnno);
                        parties_routing1 = (SIPartiesVO) locobj.getMCNMasterDetails((fcnno));
                        referencemap = locobj.getMCNReference(fcnno);
                    } else {
                        bk_port_details = (HashMap) locobj.fetchPortDetForBL(blno);
                        SI_CustDetails = locobj.fetchBookingDetForSI(blno);
                        SI_Addremarks = locobj.fetchAddremarks(blno, type);
                        siCntInfoHashObj = locobj.fetchSIContainerDetailForDraft(blno);
                        parties_routing1 = (SIPartiesVO) locobj.getBLMasterDetails((blno));
                        List filenamelist = ecomCntrlObj.getSubmittedFileNames(blno, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH), "BL");
                        referencemap = locobj.getBlReference(blno);
                        request.setAttribute("FILENAMELIST", filenamelist);
                        sino = locobj.getBLVersionNumber(blno);
                        if (sino != null && Validation.checkNull(sino).length() > 0) {
                            fileCopyFromBL(sino, blno, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH));
                        }
                        if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {
                            request.setAttribute("CHARGEDETAILS", locobj.getBLCharges(blno));
                        }
                    }
                }
                session.setAttribute("BLtype", "Draft");
                System.out.println("[ECOM-WAR] MCNEditAction  >>bk_port_details.." + bk_port_details);
                SIPartiesVO routingVO = (SIPartiesVO) bk_port_details.get("routing");
                String ccMailId = (String) bk_port_details.get("CCMAILID");
                ccMailId = Validation.checkNull(ccMailId);
                request.setAttribute("SIAGENCYREFNO", routingVO.getAgencyRefNo());
                session.setAttribute("SIAGENCYREFNO_For_Mail", routingVO.getAgencyRefNo());
                pol = Validation.checkNull(routingVO.getPolName());
                pod = Validation.checkNull(routingVO.getPodName());
                loadportcountry = Validation.checkNull(routingVO.getPol());
                System.out.println("[ECOM-WAR] MCNEditAction  >>routingVO.getReceipt()>>" + routingVO.getReceipt() +" parties_routing1.getPla_free_txt() >>" + parties_routing1.getPla_free_txt());
                if (!"1".equalsIgnoreCase(appVersion)) {
                    routingVO.setPolName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPolName()).toUpperCase())));
                    routingVO.setPodName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPodName()).toUpperCase())));
                }
                routingVO.setIn_origin(Validation.checkNull(routingVO.getIn_origin()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getIn_origin()).toUpperCase())));
                routingVO.setIn_delivery(Validation.checkNull(routingVO.getIn_delivery()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getIn_delivery()).toUpperCase())));
                routingVO.setVesselName(Validation.checkNull((String) vessellist.get(Validation.checkNull(routingVO.getVesselName()).toUpperCase())));
                routingVO.setPolat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPolat()));
                routingVO.setPodat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPodat()));
                pla_free_txt = parties_routing1.getPla_free_txt() != null && parties_routing1.getPla_free_txt().length() > 0 ? parties_routing1.getPla_free_txt().trim() : routingVO.getReceipt();
                pol_free_txt = parties_routing1.getPol_free_txt() != null && parties_routing1.getPol_free_txt().length() > 0 ? parties_routing1.getPol_free_txt().trim() : routingVO.getPolName();
                pod_free_txt = parties_routing1.getPod_free_txt() != null && parties_routing1.getPod_free_txt().length() > 0 ? parties_routing1.getPod_free_txt().trim() : routingVO.getPodName();
                pld_free_txt = parties_routing1.getPld_free_txt() != null && parties_routing1.getPld_free_txt().length() > 0 ? parties_routing1.getPld_free_txt().trim() : routingVO.getDelivery();
                voyage_free_txt = parties_routing1.getVslvoyage_free_txt() != null ? parties_routing1.getVslvoyage_free_txt().trim() : routingVO.getVesselName() + "/" + routingVO.getVoyagecode() + "/" + routingVO.getBound();
                siform.setPla_free_txt(pla_free_txt);
                siform.setPol_free_txt(pol_free_txt);
                siform.setPod_free_txt(pod_free_txt);
                siform.setPld_free_txt(pld_free_txt);
                siform.setHeaderShipper(Validation.checkNull(parties_routing1.getShipperHeader()));
                siform.setHeaderConsignee(Validation.checkNull(parties_routing1.getConsigneeHeader()));
                siform.setHeaderNotify(Validation.checkNull(parties_routing1.getNotifyHeader()));
                siform.setSpnameheader(Validation.checkNull(parties_routing1.getShipperName()));
                siform.setConsnameheader(Validation.checkNull(parties_routing1.getConsigneeName()));
                siform.setNpnameheader(Validation.checkNull(parties_routing1.getNotifyName()));
                siform.setVslvoyage_free_txt(voyage_free_txt);
                siform.setHid_pla_free_txt(routingVO.getReceipt());
                siform.setHid_pol_free_txt(routingVO.getPolName());
                siform.setHid_pod_free_txt(routingVO.getPodName());
                siform.setHid_pld_free_txt(routingVO.getDelivery());
                siform.setHid_vslvoyage_free_txt(routingVO.getVesselName() + " " + routingVO.getVoyagecode() + "/" + routingVO.getBound());
                siform.setBltype(routingVO.getBltype());
                siform.setCargoprotectid(routingVO.getCargoprotectid());
                if (referencemap != null && referencemap.size() > 0) {
                    String AESDis = (String) referencemap.get("AES");
                    if (AESDis != null && AESDis.contains("No SED required")) {
                        siform.setAESDisclaimer(AESDis);
                    } else {
                        siform.setSiAESnumber(AESDis);
                    }

                    String releasetype = (String) referencemap.get(EcomConsts.REF_TYPE);
                    siform.setReleasetype(releasetype);
                    siform.setAcidValue((String) referencemap.get(EcomConsts.ACID));
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.BL_Type_From_Reference))) {
                        siform.setBltype((String) referencemap.get(EcomConsts.BLType));
                    }
                }
                siform.setCcMailId(ccMailId);
                siform.setFcnno(fcnno);
            }
            System.out.println("[ECOM-WAR] MCNEditAction  loadportcountry==> " + loadportcountry);
            if (loadportcountry != null && loadportcountry.length() > 2) {
                loadportcountry = loadportcountry.substring(0, 2);
                request.setAttribute("LoadportCountry", loadportcountry);
            }
            if (siCntInfoHashObj != null && siCntInfoHashObj.size() > 0) {
                Collection<SIMasterVO> collection = siCntInfoHashObj.keySet();
                SIMasterVO[] empArray = new SIMasterVO[siCntInfoHashObj.size()];
                SIMasterVO[] array = (SIMasterVO[]) collection.toArray(empArray);
                Arrays.sort(array, new NumberComparator());
            }

            //Mandatory configuration
            if (fieldShipmentMap != null) {
                headerShipmentType = fieldShipmentMap.get("SHC");
                hscodeShipmentType = fieldShipmentMap.get("HSC");
                shipperSealShipmentType = fieldShipmentMap.get("SHS");
            }
            isHeader = "NO";
            isHSCodeShow = "NO";
            isShipperSeal = "NO";
            System.out.println("[ECOM-WAR] MCNEditAction  >>shipmentType>>" + headerShipmentType + ">hscodeShipmentType>>" + hscodeShipmentType +" >>si pol>" + pol + "sipod>" + pod);
            headerConfigResult = "";
            polCountry = Validation.checkNull(bookingObj.getCountryCodeForPort(pol));
            podCountry = Validation.checkNull(bookingObj.getCountryCodeForPort(pod));
            if ("E".equalsIgnoreCase(headerShipmentType)) {
                headerConfigResult = ecomCntrlObj.getFieldConfg(polCountry, pol, "SHC", "E");
            } else if ("I".equalsIgnoreCase(headerShipmentType)) {
                headerConfigResult = ecomCntrlObj.getFieldConfg(podCountry, pod, "SHC", "I");
            }
            if ("Yes".equalsIgnoreCase(headerConfigResult)) {
                isHeader = "YES";
            }
            hscodeConfigResult = "";
            if ("E".equalsIgnoreCase(hscodeShipmentType)) {
                hscodeConfigResult = ecomCntrlObj.getFieldConfg(polCountry, pol, "HSC", "E");
            } else if ("I".equalsIgnoreCase(hscodeShipmentType)) {
                hscodeConfigResult = ecomCntrlObj.getFieldConfg(podCountry, pod, "HSC", "I");
            }
            if ("Yes".equalsIgnoreCase(hscodeConfigResult)) {
                isHSCodeShow = "YES";
            }
            System.out.println("[ECOM-WAR] MCNEditAction  >>isHSCodeShow>>" + isHSCodeShow);
            //Find shipper seal mandatory for this booking
            shipperSealConfig = "";
            if ("E".equalsIgnoreCase(shipperSealShipmentType)) {
                shipperSealConfig = ecomCntrlObj.getFieldConfg(polCountry, pol, "SHS", "E");
            } else if ("I".equalsIgnoreCase(hscodeShipmentType)) {
                shipperSealConfig = ecomCntrlObj.getFieldConfg(podCountry, pod, "SHS", "I");
            }
            if ("Yes".equalsIgnoreCase(shipperSealConfig)) {
                isShipperSeal = "YES";
            }
            System.out.println("[ECOM-WAR] MCNEditAction  >>>>shipper seal required>" + isShipperSeal);
            //End mandatory configuration
            if ("1".equalsIgnoreCase(appVersion)) {
                if (siCntInfoHashObj != null && siCntInfoHashObj.size() > 0) {
                    Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = siCntInfoHashObj.entrySet();
                    for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                        SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                        ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                        if (cntList != null && cntList.size() > 0) {
                            for (int j = 0; j < cntList.size(); j++) {
                                SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                siCntDetVo.setCommodityName(Validation.checkNull((String) v1ComdtyMap.get(Validation.checkNull(siCntDetVo.getStrCommodity()))));
                                siCntDetVo.setHsCodeDesc(Validation.checkNull(siCntDetVo.getHsCode()));
                                siCntDetVo.setAppVersion(appVersion);
                            }
                        }
                    }
                }
            } else {
                if (siCntInfoHashObj != null && siCntInfoHashObj.size() > 0) {
                    Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = siCntInfoHashObj.entrySet();
                    for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                        SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                        ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                        if (cntList != null && cntList.size() > 0) {
                            for (int j = 0; j < cntList.size(); j++) {
                                SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                siCntDetVo.setCommodityName(Validation.checkNull((String) comlist.get(Validation.checkNull(siCntDetVo.getStrCommodity()))));
                                siCntDetVo.setHsCodeDesc(Validation.checkNull((String) comlist.get(Validation.checkNull(siCntDetVo.getHsCode()))));
                                siCntDetVo.setAppVersion(appVersion);
                            }
                        }
                    }
                }
            }

            request.setAttribute("HEADERCONFIGURATION", isHeader);
            request.setAttribute("HSCODECONFIGURATION", isHSCodeShow);
            request.setAttribute("agencyemailid", siObj.getAgencyEmail(bookno));
            session.setAttribute("SHIPPERSEALCONFIG", isShipperSeal);
            request.setAttribute("EDITSIAPPVERSION", appVersion);
            session.setAttribute("HSCODECONFIG", isHSCodeShow);
            session.setAttribute("appVersion", appVersion);
            session.setAttribute("containerListForSI", siCntInfoHashObj);
            session.setAttribute("containerListDraftForSI", siDraftCntInfoHashObj);
            session.setAttribute("edit_bookno", bookno);
            session.setAttribute("edit_sino", "");
            session.setAttribute("edit_blno", blno);
            session.setAttribute("bk_port_details", bk_port_details);
            siform.setAppVersion(appVersion);
            //

            //
            if (SI_Addremarks != null) {
                String addremarks = (String) SI_Addremarks.get("addremarks");
                //System.out.println("[ECOM-WAR] MCNEditAction  addremarks::::::" + addremarks);
                siform.setSicustremarks(addremarks);
            }
            System.out.println("[ECOM-WAR] MCNEditAction  >>SI_CustDetails>>" + SI_CustDetails);
            SIPartiesVO sh = (SIPartiesVO) SI_CustDetails.get("SH");
            SIPartiesVO cn = (SIPartiesVO) SI_CustDetails.get("CN");
            SIPartiesVO nt = (SIPartiesVO) SI_CustDetails.get("NT");
            SIPartiesVO nt2 = (SIPartiesVO) SI_CustDetails.get("N1");
            SIPartiesVO nt3 = (SIPartiesVO) SI_CustDetails.get("N2");
            SIPartiesVO fw = (SIPartiesVO) SI_CustDetails.get("FW");
            SIPartiesVO ap = (SIPartiesVO) SI_CustDetails.get("AP");
            if (ap == null) {
                Map custlis = locobj.fetchBookingDetForSI(blno);
                ap = (SIPartiesVO) custlis.get("AP");
            }
            //
            Hashtable countryHashObj = (Hashtable) contexts.getAttribute("SESSION_COUNTRYOBJECT");
            Hashtable cityHashObj = (Hashtable) contexts.getAttribute("SESSION_CITYLIST");
            String strCountryName = "", strCityName = "";
            if (sh != null) {
                strCountryName = "";
                siform.setSpname(sh.getCustname());
                siform.setSpCustCode(sh.getCustCode());
                siform.setSpaddr(sh.getAddress1());
                siform.setSpaddr1(sh.getAddress2());
                siform.setSpcity(sh.getCity());
                siform.setSpphone(sh.getTelephone());
                if (sh.getFax() != null && sh.getFax().length() > 0 && !sh.getFax().equalsIgnoreCase("--") && !sh.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setSpfax(sh.getFax());
                } else {
                    siform.setSpfax("");
                }
                siform.setSpcountry(sh.getCountryid());
                siform.setSpemail(sh.getEmail());
                strCountryName = (String) countryHashObj.get(sh.getCountryid());
                if (strCountryName != null) {
                    strCountryName = sh.getCountryid() + " - " + strCountryName;
                    siform.setSpCountryName(strCountryName);
                } else {
                    siform.setSpCountryName("");
                }

                strCityName = "";
                strCityName = (String) cityHashObj.get(sh.getCity());
                strCityName = sh.getCity();//+" - "+strCityName;
                siform.setSpCityName(strCityName);
                siform.setSpstate(concatStateName(sh.getState(), sh.getCountryid()));
                siform.setSpzipcode(Validation.checkNull(sh.getZipcode()));
            } else {
                strCountryName = "";
                siform.setSpname("");
                siform.setSpCustCode("");
                siform.setSpaddr("");
                siform.setSpaddr1("");
                siform.setSpcity("");
                siform.setSpphone("");
                siform.setSpfax("");
                siform.setSpcountry("");
                siform.setSpemail("");
                siform.setSpstate("");
                siform.setSpCountryName("");
                siform.setSpCityName("");
            }

            if (cn != null) {
                siform.setConsname(cn.getCustname());
                siform.setConsCustCode(cn.getCustCode());
                siform.setConsaddr(cn.getAddress1());
                siform.setConsaddr1(cn.getAddress2());
                siform.setConscity(cn.getCity());
                siform.setConsphone(cn.getTelephone());
                if (cn.getFax() != null && cn.getFax().length() > 0 && !cn.getFax().equalsIgnoreCase("--") && !cn.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setConsfax(cn.getFax());
                } else {
                    siform.setConsfax("");
                }
                siform.setConsemail(cn.getEmail());
                siform.setConscountry(cn.getCountryid());
                System.out.println("[ECOM-WAR] MCNEditAction   cn.getToorder" + cn.getToorder());
                if ("Y".equalsIgnoreCase(cn.getToorder())) {
                    siform.setConsigneeordercheck("Y");
                } else {
                    siform.setConsigneeordercheck("N");
                }
                String conName = Validation.checkNull(cn.getCustname());
                if ("edit".equalsIgnoreCase(type) && conName.length() > 0) {
                    if (isTOOrder(conName)) {
                        siform.setConsigneeordercheck("Y");
                    }
                }
                strCountryName = "";
                strCountryName = (String) countryHashObj.get(cn.getCountryid());
                if (strCountryName != null) {
                    strCountryName = cn.getCountryid() + " - " + strCountryName;
                    siform.setConsCountryName(strCountryName);
                } else {
                    siform.setConsCountryName("");
                }

                strCityName = "";
                strCityName = (String) cityHashObj.get(cn.getCity());
                strCityName = cn.getCity();
                siform.setConsCityName(strCityName);
                siform.setConsstate(concatStateName(cn.getState(), cn.getCountryid()));
                siform.setConszipcode(Validation.checkNull(cn.getZipcode()));
            } else {
                siform.setConsname("");
                siform.setConsCustCode("");
                siform.setConsaddr("");
                siform.setConsaddr1("");
                siform.setConscity("");
                siform.setConsphone("");
                siform.setConsfax("");
                siform.setConsemail("");
                siform.setConscountry("");
                siform.setConsigneeordercheck("N");
                siform.setConsCountryName("");
                siform.setConsCityName("");
                siform.setConsstate("");
            }
            if (nt != null) {
                siform.setNpname(nt.getCustname());
                siform.setNpCustCode(nt.getCustCode());
                siform.setNpaddr(nt.getAddress1());
                siform.setNpaddr1(nt.getAddress2());
                siform.setNpcity(nt.getCity());
                siform.setNpphone(nt.getTelephone());
                if (nt.getFax() != null && nt.getFax().length() > 0 && !nt.getFax().equalsIgnoreCase("--") && !nt.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setNpfax(nt.getFax());
                } else {
                    siform.setNpfax("");
                }
                siform.setNpemail(nt.getEmail());
                siform.setNpcountry(nt.getCountryid());
                siform.setNotifyCheck(nt.getNotifyCheck());
                if ("Y".equalsIgnoreCase(nt.getToorder())) {
                    siform.setNotifyordercheck("Y");
                } else {
                    siform.setNotifyordercheck("N");
                }
                strCountryName = "";
                strCountryName = (String) countryHashObj.get(nt.getCountryid());
                if (strCountryName != null) {
                    strCountryName = nt.getCountryid() + " - " + strCountryName;
                    siform.setNpCountryName(strCountryName);
                } else {
                    siform.setNpCountryName("");
                }

                strCityName = "";
                strCityName = (String) cityHashObj.get(nt.getCity());
                strCityName = nt.getCity();//+" - "+strCityName;
                siform.setNpCityName(strCityName);
                siform.setNpstate(concatStateName(nt.getState(), nt.getCountryid()));
                siform.setNpzipcode(Validation.checkNull(nt.getZipcode()));
            } else {
                siform.setNpname("");
                siform.setNpCustCode("");
                siform.setNpaddr("");
                siform.setNpaddr1("");
                siform.setNpcity("");
                siform.setNpphone("");
                siform.setNpfax("");
                siform.setNpemail("");
                siform.setNpcountry("");
                siform.setNotifyCheck("");
                siform.setNotifyordercheck("N");
                siform.setNpCountryName("");
                siform.setNpCityName("");
                siform.setNpstate("");
            }
            // notity 2
            if (nt2 != null) {
                siform.setNp2name(nt2.getCustname());
                siform.setNp2CustCode(nt2.getCustCode());
                siform.setNp2addr(nt2.getAddress1());
                siform.setNp2addr1(nt2.getAddress2());
                siform.setNp2city(nt2.getCity());
                siform.setNp2phone(nt2.getTelephone());
                if (nt2.getFax() != null && nt2.getFax().length() > 0 && !nt2.getFax().equalsIgnoreCase("--") && !nt2.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setNp2fax(nt2.getFax());
                } else {
                    siform.setNp2fax("");
                }
                siform.setNp2email(nt2.getEmail());
                siform.setNp2country(nt2.getCountryid());
                siform.setNotify2Check(nt2.getNotifyCheck());
                if ("Y".equalsIgnoreCase(nt2.getToorder())) {
                    siform.setNotify2ordercheck("Y");
                } else {
                    siform.setNotify2ordercheck("N");
                }
                strCountryName = "";
                strCountryName = (String) countryHashObj.get(nt2.getCountryid());
                if (strCountryName != null) {
                    strCountryName = nt2.getCountryid() + " - " + strCountryName;
                    siform.setNp2CountryName(strCountryName);
                } else {
                    siform.setNp2CountryName("");
                }

                strCityName = "";
                strCityName = (String) cityHashObj.get(nt2.getCity());
                strCityName = nt2.getCity();//+" - "+strCityName;
                siform.setNp2CityName(strCityName);
                siform.setNp2state(concatStateName(nt2.getState(), nt2.getCountryid()));
            } else {
                siform.setNp2name("");
                siform.setNp2CustCode("");
                siform.setNp2addr("");
                siform.setNp2addr1("");
                siform.setNp2city("");
                siform.setNp2phone("");
                siform.setNp2fax("");
                siform.setNp2email("");
                siform.setNp2country("");
                siform.setNotify2Check("");
                siform.setNotify2ordercheck("N");
                siform.setNp2CountryName("");
                siform.setNp2CityName("");
                siform.setNp2state("");
            }

            // notity 3
            if (nt3 != null) {
                siform.setNp3name(nt3.getCustname());
                siform.setNp3CustCode(nt3.getCustCode());
                siform.setNp3addr(nt3.getAddress1());
                siform.setNp3addr1(nt3.getAddress2());
                siform.setNp3city(nt3.getCity());
                siform.setNp3phone(nt3.getTelephone());
                if (nt3.getFax() != null && nt3.getFax().length() > 0 && !nt3.getFax().equalsIgnoreCase("--") && !nt3.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setNp3fax(nt3.getFax());
                } else {
                    siform.setNp3fax("");
                }
                siform.setNp3email(nt3.getEmail());
                siform.setNp3country(nt3.getCountryid());
                siform.setNotify3Check(nt3.getNotifyCheck());
                if ("Y".equalsIgnoreCase(nt3.getToorder())) {
                    siform.setNotify3ordercheck("Y");
                } else {
                    siform.setNotify3ordercheck("N");
                }
                strCountryName = "";
                strCountryName = (String) countryHashObj.get(nt3.getCountryid());
                if (strCountryName != null) {
                    strCountryName = nt3.getCountryid() + " - " + strCountryName;
                    siform.setNp3CountryName(strCountryName);
                } else {
                    siform.setNp3CountryName("");
                }

                strCityName = "";
                strCityName = (String) cityHashObj.get(nt3.getCity());
                strCityName = nt3.getCity();//+" - "+strCityName;
                siform.setNp3CityName(strCityName);
                siform.setNp3state(concatStateName(nt3.getState(), nt3.getCountryid()));
            } else {
                siform.setNp3name("");
                siform.setNp3CustCode("");
                siform.setNp3addr("");
                siform.setNp3addr1("");
                siform.setNp3city("");
                siform.setNp3phone("");
                siform.setNp3fax("");
                siform.setNp3email("");
                siform.setNp3country("");
                siform.setNotify3Check("");
                siform.setNotify3ordercheck("N");
                siform.setNp3CountryName("");
                siform.setNp3CityName("");
                siform.setNp3state("");
            }

            //FW
            if (fw != null) {
                strCountryName = "";
                siform.setFwname(fw.getCustname());
                siform.setFwCustCode(fw.getCustCode());
                siform.setFwaddr(fw.getAddress1());
                siform.setFwaddr1(fw.getAddress2());
                siform.setFwcity(fw.getCity());
                siform.setFwphone(fw.getTelephone());
                if (fw.getFax() != null && fw.getFax().length() > 0 && !fw.getFax().equalsIgnoreCase("--") && !fw.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setFwfax(fw.getFax());
                } else {
                    siform.setFwfax("");
                }
                siform.setFwcountry(fw.getCountryid());
                siform.setFwemail(fw.getEmail());
                strCountryName = (String) countryHashObj.get(fw.getCountryid());
                if (strCountryName != null) {
                    strCountryName = fw.getCountryid() + " - " + strCountryName;
                    siform.setFwCountryName(strCountryName);
                } else {
                    siform.setFwCountryName("");
                }
                strCityName = fw.getCity();//+" - "+strCityName;
                siform.setFwCityName(strCityName);
                siform.setFwstate(concatStateName(fw.getState(), fw.getCountryid()));
            } else {
                strCountryName = "";
                siform.setFwname("");
                siform.setFwCustCode("");
                siform.setFwaddr("");
                siform.setFwaddr1("");
                siform.setFwcity("");
                siform.setFwphone("");
                siform.setFwfax("");
                siform.setFwcountry("");
                siform.setFwemail("");
                siform.setFwCountryName("");
                siform.setFwCityName("");
                siform.setFwstate("");
            }
            //AP
            if (ap != null) {
                System.out.println("[ECOM-WAR] MCNEditAction  =====apList.size() is===>" + ap);
                strCountryName = "";
                siform.setApname(ap.getCustname());
                siform.setApCustCode(ap.getCustCode());
                siform.setApaddr(ap.getAddress1());
                siform.setApaddr1(ap.getAddress2());
                siform.setApcity(ap.getCity());
                siform.setApphone(ap.getTelephone());
                if (ap.getFax() != null && ap.getFax().length() > 0 && !ap.getFax().equalsIgnoreCase("--") && !ap.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                    siform.setApfax(ap.getFax());
                } else {
                    siform.setApfax("");
                }
                siform.setApcountry(ap.getCountryid());
                System.out.println("[ECOM-WAR] MCNEditAction  ====contry id is====>" + ap.getCountryid());
                siform.setApemail(ap.getEmail());
                strCountryName = (String) countryHashObj.get(ap.getCountryid());
                System.out.println("[ECOM-WAR] MCNEditAction  ===country name is====>" + strCountryName);
                if (strCountryName != null) {
                    strCountryName = ap.getCountryid() + " - " + strCountryName;
                    siform.setApCountryName(strCountryName);
                } else {
                    siform.setApCountryName("");
                }
                strCityName = ap.getCity();//+" - "+strCityName;
                siform.setApCityName(strCityName);
                siform.setApstate(concatStateName(ap.getState(), ap.getCountryid()));
            }

            if ("View".equalsIgnoreCase(type) || "DashBLView".equalsIgnoreCase(type)) {
                session.setAttribute("BLtype", "view");
                SUCCESS = "siview";
            }
            /*} else {
                SUCCESS = "sessionex";
            }*/
            System.out.println("[ECOM-WAR] MCNEditAction  =====Ended in Performing BL Actions(View/Edit) and result is======" + SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);

    }

    private void setEmptyValuesForCust(SIEditForm siform) {
        siform.setSpname("");
        siform.setSpCustCode("");
        siform.setSpaddr("");
        siform.setSpaddr1("");
        siform.setSpcity("");
        siform.setSpphone("");
        siform.setSpfax("");
        siform.setSpcountry("");
        siform.setSpemail("");
        siform.setSpstate("");
        siform.setSpCountryName("");
        siform.setSpCityName("");
        siform.setConsname("");
        siform.setConsCustCode("");
        siform.setConsaddr("");
        siform.setConsaddr1("");
        siform.setConscity("");
        siform.setConsphone("");
        siform.setConsfax("");
        siform.setConsemail("");
        siform.setConsstate("");
        siform.setConscountry("");
        siform.setConsigneeordercheck("N");
        siform.setConsCountryName("");
        siform.setConsCityName("");
        siform.setNpname("");
        siform.setNpCustCode("");
        siform.setNpaddr("");
        siform.setNpaddr1("");
        siform.setNpcity("");
        siform.setNpphone("");
        siform.setNpfax("");
        siform.setNpemail("");
        siform.setNpstate("");
        siform.setNpcountry("");
        siform.setNotifyCheck("");
        siform.setNotifyordercheck("N");
        siform.setNpCountryName("");
        siform.setNpCityName("");

        siform.setFwname("");
        siform.setFwCustCode("");
        siform.setFwaddr("");
        siform.setFwaddr1("");
        siform.setFwcity("");
        siform.setFwphone("");
        siform.setFwfax("");
        siform.setFwemail("");
        siform.setFwstate("");
        siform.setFwcountry("");
        siform.setFwCountryName("");
        siform.setFwCityName("");

    }

    private boolean isTOOrder(String consigneeName) {
        boolean result = false;
        try {
            String[] splited = consigneeName.split(" "); //split on word boundries
            List<String> wordList = Arrays.asList(splited);
            wordList.forEach(str -> {
                str.trim().toUpperCase();
            });
            System.out.println("[ECOM-WAR] MCNEditAction  splited>>" + wordList);
            if (wordList.contains("ORDER")) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String concatStateName(String state, String country) {
        try {

            stateMapCountry = (HashMap) stateMap.get(Validation.checkNull(country).toUpperCase());
            if (state != null && state.length() > 0 && stateMapCountry != null) {
                state = state + " - " + Validation.checkNull((String) stateMapCountry.get(state));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Validation.checkNull(state);
    }

    public void fileCopyFromBL(String sino, String blno, String path) {
        try {
            String ecompath = path + File.separator + sino;
            String lrppath = path.replace("Ecom", "BL") + File.separator + blno;
            File directory = new File(lrppath);
            if (directory.exists()) {
                File srcDir = new File(lrppath);
                File destDir = new File(ecompath);
                FileUtils.deleteDirectory(destDir);
                FileUtils.copyDirectory(srcDir, destDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
