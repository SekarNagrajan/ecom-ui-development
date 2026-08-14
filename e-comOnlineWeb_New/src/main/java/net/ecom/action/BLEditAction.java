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
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.formbeans.SIEditForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BLBeanRemote;
import net.ecom.vds.serviceVo.BLChargeVo;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.SIBLMasterVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BLEditAction extends org.apache.struts.action.Action {

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
        String pol = null, pod = null, polCountry, podCountry, headerShipmentType = "", hscodeShipmentType = "", headerConfigResult = null, hscodeConfigResult = null, loadportcountry = null, dischargeportcountry = null;
        Map referencemap = null;
//         System.out.println("[ECOM-WAR] BLEditAction         =====Started to Perform BL Actions(View/Edit)======");
        String sino = null;
        try {
            HashMap SI_Addremarks = null;

            SIEditForm siform = (SIEditForm) form;
            setEmptyValuesForCust(siform);
            HttpSession session = request.getSession();
            saveToken(request);
            String type = Validation.checkNull(request.getParameter("type"));
            System.out.println("[ECOM-WAR] BLEditAction         ===bl action type is=========>" + type);
            String blno = Validation.checkNull(request.getParameter("blno"));
            String appVersion = request.getParameter("appversion");
//             System.out.println("[ECOM-WAR] BLEditAction         ===blno is=========>" + blno);
            String bookno = Validation.checkNull(request.getParameter("bookno"));
//             System.out.println("[ECOM-WAR] BLEditAction         ===bookno is=========>" + bookno);
            System.out.println("[ECOM-WAR] BLEditAction         >>appVersion>>" + appVersion);
            String fcnno = Validation.checkNull(request.getParameter("fcnno"));
//             System.out.println("[ECOM-WAR] BLEditAction         >>fcnno>>" + fcnno);
            System.out.println("[ECOM-WAR] BLEditAction         fcnno: " + fcnno + ", bookno: " + bookno + ", blno: " + blno + ", bl action type: " + type);

            ServletContext contexts = servlet.getServletContext();
            SIPartiesVO parties_routing1 = null;
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            String fakCommodity = (String) contexts.getAttribute(GeneralConf.FAKCOMMODITY);
            String SHOW_CHARGES_IN_SIBL = (String) contexts.getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
            String hidefaxno = (String) contexts.getAttribute(GeneralConf.HideFaxNo);
            String Single_SINofor_BL = (String) contexts.getAttribute(GeneralConf.Single_SINofor_BL);
            String TENS_Documentation = (String) contexts.getAttribute(GeneralConf.TENS_Documentation);
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            String fakComdtyCode = "";
            String fakComdtyName = "";
            System.out.println("[ECOM-WAR] BLEditAction         fakCommodity>>>>" + fakCommodity);
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
            // System.out.println("[ECOM-WAR] BLEditAction         <>>>vessellist>>>="+vessellist);
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PORTLIST");
            Map<SIMasterVO, SIConatainerValueVO> siDraftCntInfoHashObj = null;
            HashMap bk_port_details = null;
            Map<SIMasterVO, SIConatainerValueVO> siCntInfoHashObj = null;
            HashMap SI_CustDetails = null;
            HashMap customerMap = null;
            ArrayList slcustsList = null;
            ArrayList bltypesList = null;
            String pla_free_txt = null, pol_free_txt = null, pod_free_txt = null, pld_free_txt = null, voyage_free_txt = null;

            Set blSet = (Set) session.getAttribute("blAccessList");
            Set dashBoardSet = (Set) session.getAttribute("dashBoardAccessList");
            ENSDeclarantVo ics = null;
            if ((blSet != null && blSet.contains(blno)) || (dashBoardSet != null && dashBoardSet.contains(blno))) {
                if ("view".equalsIgnoreCase(type) || "blamend".equalsIgnoreCase(type)) {
                    if ("1".equalsIgnoreCase(appVersion)) {
                        bk_port_details = (HashMap) v1BLObj.fetchV1PortDetForBL(blno);
                    } else {
                        bk_port_details = (HashMap) locobj.fetchPortDetForBL(blno);
                    }

                    sino = locobj.getBLVersionNumber(blno);
//                     System.out.println("[ECOM-WAR] BLEditAction         ===sino is=========>" + sino);
                    SI_CustDetails = locobj.fetchSICustomerDetails(bookno, sino);
                    if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                        customerMap = locobj.fetchSICustDetail(bookno, sino);
                    }
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
                    dischargeportcountry = Validation.checkNull(routingVO.getFinalport());
//                     System.out.println("[ECOM-WAR] BLEditAction         >>routingVO.getReceipt()>>" + routingVO.getReceipt());
//                     System.out.println("[ECOM-WAR] BLEditAction         parties_routing1.getPla_free_txt() >>" + parties_routing1.getPla_free_txt());
                    if (!"1".equalsIgnoreCase(appVersion)) {
                        routingVO.setPolName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPolName()).toUpperCase())));
                        routingVO.setPodName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPodName()).toUpperCase())));
                    }
                    siform.setOriginid(Validation.checkNull(routingVO.getIn_origin()));
                    routingVO.setIn_origin(Validation.checkNull(routingVO.getIn_origin()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getIn_origin()).toUpperCase())));
                    routingVO.setIn_delivery(Validation.checkNull(routingVO.getIn_delivery()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getIn_delivery()).toUpperCase())));
//                     System.out.println("[ECOM-WAR] BLEditAction         >>>routingVO.getVesselName()>>>" + routingVO.getVesselName());
                    System.out.println("[ECOM-WAR] BLEditAction         Vessel Name: " + routingVO.getVesselName() + ", Pla Free Txt: " + parties_routing1.getPla_free_txt() + ", Receipt: " + routingVO.getReceipt());
                    routingVO.setVesselName(Validation.checkNull((String) vessellist.get(routingVO.getVesselName())));
                    routingVO.setPolat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPolat()));
                    routingVO.setPodat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPodat()));
                    pla_free_txt = parties_routing1.getPla_free_txt() != null && parties_routing1.getPla_free_txt().length() > 0 ? parties_routing1.getPla_free_txt().trim() : routingVO.getReceipt();
                    pol_free_txt = parties_routing1.getPol_free_txt() != null && parties_routing1.getPol_free_txt().length() > 0 ? parties_routing1.getPol_free_txt().trim() : routingVO.getPolName();
                    pod_free_txt = parties_routing1.getPod_free_txt() != null && parties_routing1.getPod_free_txt().length() > 0 ? parties_routing1.getPod_free_txt().trim() : routingVO.getPodName();
                    pld_free_txt = parties_routing1.getPld_free_txt() != null && parties_routing1.getPld_free_txt().length() > 0 ? parties_routing1.getPld_free_txt().trim() : routingVO.getDelivery();
                    voyage_free_txt = routingVO.getVesselName() + "/" + routingVO.getVoyagecode() + "/" + routingVO.getBound();
                    
                    String plaDisplayName = MaskPortUtil.getDisplayPortName(pol, pla_free_txt);
                    siform.setPla_free_txt(plaDisplayName);
                    
                    String polDisplayName = MaskPortUtil.getDisplayPortName(pol, pol_free_txt);
                    siform.setPol_free_txt(polDisplayName);
                    
                    String podDisplayName = MaskPortUtil.getDisplayPortName(pod, pod_free_txt);
                    siform.setPod_free_txt(podDisplayName);
                    
                    String pldDisplayName = MaskPortUtil.getDisplayPortName(pod, pld_free_txt);
                    siform.setPld_free_txt(pldDisplayName);
                    siform.setVslvoyage_free_txt(voyage_free_txt);
                    siform.setHid_pla_free_txt(MaskPortUtil.getDisplayPortName(pol, routingVO.getReceipt()));
                    siform.setHid_pol_free_txt(MaskPortUtil.getDisplayPortName(pol,routingVO.getPolName()));
                    siform.setHid_pod_free_txt(MaskPortUtil.getDisplayPortName(pod, routingVO.getPodName()));
                    siform.setHid_pld_free_txt(MaskPortUtil.getDisplayPortName(pod, routingVO.getDelivery()));
                    siform.setSino(sino);
                    siform.setHid_vslvoyage_free_txt(routingVO.getVesselName() + " " + routingVO.getVoyagecode() + "/" + routingVO.getBound());
                    siform.setAESDisclaimer(parties_routing1.getAESDiscliamer());
                    siform.setSiAESnumber(parties_routing1.getAESNumber());
                    siform.setReleasetype(parties_routing1.getReleasetype());
                    siform.setDeclaredValue(parties_routing1.getDeclaredValue());
                    siform.setAcidValue(parties_routing1.getACID());
                    siform.setBltype(Validation.checkNull(parties_routing1.getBltype()));
                    siform.setCargoprotectid(parties_routing1.getCargoprotectid());
                    siform.setFinalportid(Validation.checkNull(routingVO.getFinalport()));
                    siform.setEbook_t2lfiling(Validation.checkNull(routingVO.getEbook_t2lfiling()));
                    siform.setEbook_nvocc(Validation.checkNull(parties_routing1.getEbook_nvocc()));
                    routingVO.setEbook_nvocc(parties_routing1.getEbook_nvocc());
                    siform.setPackinglst(Validation.checkNull(parties_routing1.getPackinglst()));
                    siform.setInvoiceupload(Validation.checkNull(parties_routing1.getInvoiceupload()));
                    siform.setOrgTelType(Validation.checkNull(parties_routing1.getOrgTelType()));
                    siform.setEbook_EnsDocType(Validation.checkNull(parties_routing1.getEbook_EnsDocType()));
                    siform.setBlType_UAE(Validation.checkNull(parties_routing1.getBltype_UAE()));
                    siform.setMpciId_UAE(Validation.checkNull(parties_routing1.getMpci_id()));

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
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                        routingVO.setIsmaskport(siObj.checkMaskPortForSIBL(bookno));
                    } else {
                        routingVO.setIsmaskport(true);
                    }
                    session.setAttribute("IsEnableENSTabInSIBL", "N");
                    if (routingVO.isIseuport()) {
                        session.setAttribute("IsEnableENSTabInSIBL", "Y");
                    }

                    ics = siObj.getENSTabDetails(sino);
                } else {
                    if ("1".equalsIgnoreCase(appVersion)) {
                        bk_port_details = (HashMap) v1BLObj.fetchV1PortDetForBL(blno);
                        SI_CustDetails = v1BLObj.fetchV1BLDetForSI(blno);
                        siCntInfoHashObj = v1BLObj.fetchContainerDetailForSI(blno);
                        parties_routing1 = (SIPartiesVO) v1BLObj.getV1BLMasterDetails((blno));
                    } else {
                        System.out.println("[ECOM-WAR] BLEditAction         ~~~~FCNNO length ~~~~~" + fcnno.length());
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
                            if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                                customerMap = locobj.fetchBookingCustForSI(blno);
                            }
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
//                     System.out.println("[ECOM-WAR] BLEditAction         >>bk_port_details.." + bk_port_details);
                    SIPartiesVO routingVO = (SIPartiesVO) bk_port_details.get("routing");
                    String ccMailId = (String) bk_port_details.get("CCMAILID");
                    ccMailId = Validation.checkNull(ccMailId);
                    request.setAttribute("SIAGENCYREFNO", routingVO.getAgencyRefNo());
                    session.setAttribute("SIAGENCYREFNO_For_Mail", routingVO.getAgencyRefNo());
                    pol = Validation.checkNull(routingVO.getPolName());
                    pod = Validation.checkNull(routingVO.getPodName());
                    loadportcountry = Validation.checkNull(routingVO.getPol());
                    dischargeportcountry = Validation.checkNull(routingVO.getFinalport());
//                     System.out.println("[ECOM-WAR] BLEditAction         >>routingVO.getReceipt()>>" + routingVO.getReceipt());
//                     System.out.println("[ECOM-WAR] BLEditAction         parties_routing1.getPla_free_txt() >>" + parties_routing1.getPla_free_txt());
                    System.out.println("[ECOM-WAR] BLEditAction         bk_port_details: " + bk_port_details + ", routingVO.getReceipt: " + routingVO.getReceipt() + ", parties_routing1.getPla_free_txt: " + parties_routing1.getPla_free_txt());

                    if (!"1".equalsIgnoreCase(appVersion)) {
                        routingVO.setPolName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPolName()).toUpperCase())));
                        routingVO.setPodName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPodName()).toUpperCase())));
                    }
                    siform.setOriginid(Validation.checkNull(routingVO.getIn_origin()));
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
                    
                    String plaDisplayNameDraft = MaskPortUtil.getDisplayPortName(pol, pla_free_txt);
                    siform.setPla_free_txt(plaDisplayNameDraft);
                    
                    String polDisplayNameDraft = MaskPortUtil.getDisplayPortName(pol, pol_free_txt);
                    siform.setPol_free_txt(polDisplayNameDraft);
                    
                    String podDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, pod_free_txt);
                    siform.setPod_free_txt(podDisplayNameDraft);
                    
                    String pldDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, pld_free_txt);
                    siform.setPld_free_txt(pldDisplayNameDraft);
                    siform.setHeaderShipper(Validation.checkNull(parties_routing1.getShipperHeader()));
                    siform.setHeaderConsignee(Validation.checkNull(parties_routing1.getConsigneeHeader()));
                    siform.setHeaderNotify(Validation.checkNull(parties_routing1.getNotifyHeader()));
                    siform.setSpnameheader(Validation.checkNull(parties_routing1.getShipperName()));
                    siform.setConsnameheader(Validation.checkNull(parties_routing1.getConsigneeName()));
                    siform.setNpnameheader(Validation.checkNull(parties_routing1.getNotifyName()));
                    String telxRel = Validation.checkNull(parties_routing1.getOrgTelType());
                    System.out.println("telxRel==>" + telxRel);
                    telxRel = "Y".equalsIgnoreCase(telxRel) ? "T" : "O";
                    siform.setOrgTelType(telxRel);
                    System.out.println("telxRel" + siform.getOrgTelType());
                    siform.setVslvoyage_free_txt(voyage_free_txt);
                    
                    String hidPlaDisplayNameDraft = MaskPortUtil.getDisplayPortName(siform.getOriginid(), routingVO.getReceipt());
                    siform.setHid_pla_free_txt(hidPlaDisplayNameDraft);
                    String hidPolDisplayNameDraft = MaskPortUtil.getDisplayPortName(pol, routingVO.getPolName());
                    siform.setHid_pol_free_txt(hidPolDisplayNameDraft);
                    String hidPodDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, routingVO.getPodName());
                    siform.setHid_pod_free_txt(hidPodDisplayNameDraft);
                    String hidPldDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, routingVO.getDelivery());
                    siform.setHid_pld_free_txt(hidPldDisplayNameDraft);
                    siform.setHid_vslvoyage_free_txt(routingVO.getVesselName() + " " + routingVO.getVoyagecode() + "/" + routingVO.getBound());
                    siform.setBltype(routingVO.getBltype());
                    siform.setCargoprotectid(routingVO.getCargoprotectid());
                    siform.setEbook_t2lfiling(routingVO.getEbook_t2lfiling());
                    siform.setEbook_nvocc(routingVO.getEbook_nvocc());
                    siform.setEbook_EnsDocType(routingVO.getEbook_EnsDocType());
                    System.out.println(routingVO.getEbook_t2lfiling() + "routingVO.getIn_origin()" + routingVO.getIn_origin() + "//" + routingVO.getFinalport());
                    siform.setFinalportid(Validation.checkNull(routingVO.getFinalport()));
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
                        if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.ENABLE_ADDINFO_BLTYPE))) {
                        siform.setBlType_UAE((String) referencemap.get(EcomConsts.bltype_UAE));
                        siform.setMpciId_UAE((String) referencemap.get(EcomConsts.mpci_id));
                        }
                        if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.BL_Type_From_Reference))) {
                            siform.setBltype((String) referencemap.get(EcomConsts.BLType));
                        }
                        siform.setPackinglst((String) referencemap.get(EcomConsts.packinglst));
                        siform.setInvoiceupload((String) referencemap.get(EcomConsts.invoiceupload));
                    }
                    siform.setCcMailId(ccMailId);
                    if ("Y".equalsIgnoreCase(Single_SINofor_BL)) {
                        siform.setSino(sino);
                    }
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                        routingVO.setIsmaskport(siObj.checkMaskPortForSIBL(bookno));
                    } else {
                        routingVO.setIsmaskport(true);
                    }
                    session.setAttribute("IsEnableENSTabInSIBL", "N");
                    if (routingVO.isIseuport()) {
                        session.setAttribute("IsEnableENSTabInSIBL", "Y");
                    }

                    ics = locobj.getBLENSTabDetails(blno);
                }
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.SI_CommodityGroup_Edit_Option))) {
                    String rateCmdtyJsonarr = siObj.getRateCommodityList(blno, "BL");
                    session.setAttribute("SIBLRateCmdtyJsonarr", rateCmdtyJsonarr);
                }
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.TENS_Documentation))) {
                    String ensFiling = locobj.fetForBLENSFiling(blno);
                    System.out.println("ensFiling==>" + ensFiling);

                    siform.setEbook_EnsFiling(ensFiling);
                }
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Display_Charge_Tab))) {
                    System.out.println("[ECOM-WAR] BLEditAction         Display_Charge_Tab=>" + (String) contexts.getAttribute(GeneralConf.Display_Charge_Tab));
                    List<BLChargeVo> blChargeSummary = locobj.getBlCgargeSummaryList(blno);
                    session.setAttribute("SIBLChargeSummary", blChargeSummary);
                }
                if (ics != null) {
                    siform.setDeclarantName(ics.getDeclarantName());
                    siform.setDeclarantAddress(ics.getDeclarantAddress());
                    siform.setDeclarantAddress1(ics.getDeclarantAddress1());
                    siform.setDeclarantCity(ics.getDeclarantCity());
                    siform.setDeclarantCountry(ics.getDeclarantCountry());
                    siform.setDeclarantState(ics.getDeclarantState());
                    siform.setDeclarantTelephone(ics.getDeclarantTelephone());
                    siform.setDeclarantEORI(ics.getDeclarantEORI());
                    siform.setDeclarantEmail(ics.getDeclarantEmail());
                    siform.setDeclarantFillingType(ics.getDeclarantFillingType());
                    siform.setPaymentMethod(ics.getPaymentMethod());
                    siform.setBlTypeENS(ics.getBlTypeENS());
                    siform.setEnsFillingType(ics.getEnsFillingType());
                    siform.setEuCustZone(ics.getEuCustZone());
                    siform.setDeclarantZipcode(ics.getDeclarantZipcode());
                }
                System.out.println("[ECOM-WAR] BLEditAction         loadportcountry==> " + loadportcountry);
                if (loadportcountry != null && loadportcountry.length() > 2) {
                    loadportcountry = loadportcountry.substring(0, 2);
                    request.setAttribute("LoadportCountry", loadportcountry);
                }
                if (dischargeportcountry != null && dischargeportcountry.length() > 2) {
                    dischargeportcountry = dischargeportcountry.substring(0, 2);
                    request.setAttribute("dischargeportcountry", dischargeportcountry);
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
//                 System.out.println("[ECOM-WAR] BLEditAction         >>shipmentType>>" + headerShipmentType + ">hscodeShipmentType>>" + hscodeShipmentType);
//                 System.out.println("[ECOM-WAR] BLEditAction         >>si pol>" + pol + "sipod>" + pod);
                System.out.println("[ECOM-WAR] BLEditAction         shipmentType: " + headerShipmentType + ", hscodeShipmentType: " + hscodeShipmentType + ", si pol: " + pol + ", si pod: " + pod);
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
//                 System.out.println("[ECOM-WAR] BLEditAction         >>isHSCodeShow>>" + isHSCodeShow);
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
//                 System.out.println("[ECOM-WAR] BLEditAction         >>>>shipper seal required>" + isShipperSeal);
                System.out.println("[ECOM-WAR] BLEditAction         shipper seal required: " + isShipperSeal + ", isHSCodeShow: " + isHSCodeShow);

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
                    // System.out.println("[ECOM-WAR] BLEditAction         addremarks::::::" + addremarks);
                    siform.setSicustremarks(addremarks);
                }
                System.out.println("[ECOM-WAR] BLEditAction         >>SI_CustDetails>>" + SI_CustDetails);
                SIPartiesVO sh = (SIPartiesVO) SI_CustDetails.get("SH");
                SIPartiesVO cn = (SIPartiesVO) SI_CustDetails.get("CN");
                SIPartiesVO nt = (SIPartiesVO) SI_CustDetails.get("NT");
                SIPartiesVO nt2 = (SIPartiesVO) SI_CustDetails.get("N1");
                SIPartiesVO nt3 = (SIPartiesVO) SI_CustDetails.get("N2");
                SIPartiesVO fw = (SIPartiesVO) SI_CustDetails.get("FW");
                SIPartiesVO ap = (SIPartiesVO) SI_CustDetails.get("AP");
                SIPartiesVO by = (SIPartiesVO) SI_CustDetails.get("BY");
                SIPartiesVO sl = (SIPartiesVO) SI_CustDetails.get("SL");
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
                    siform.setSpeori(Validation.checkNull(sh.getEori()));
                    siform.setSppersontype(Validation.checkNull(sh.getPersontype()));
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
                    siform.setSpeori("");
                    siform.setSppersontype("");
                    siform.setSpzipcode("");
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
                    System.out.println(cn.getToorder());
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
                    siform.setConseori(Validation.checkNull(cn.getEori()));
                    siform.setConspersontype(Validation.checkNull(cn.getPersontype()));
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
                    siform.setConseori("");
                    siform.setConspersontype("");
                    siform.setConszipcode("");
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
                    siform.setNpeori(Validation.checkNull(nt.getEori()));
                    siform.setNppersontype(Validation.checkNull(nt.getPersontype()));
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
                    siform.setNpeori("");
                    siform.setNppersontype("");
                    siform.setNpzipcode("");
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
                    siform.setNp2eori(Validation.checkNull(nt2.getEori()));
                    siform.setNp2persontype(Validation.checkNull(nt2.getPersontype()));
                    siform.setNp2zipcode(Validation.checkNull(nt2.getZipcode()));
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
                    siform.setNp2eori("");
                    siform.setNp2persontype("");
                    siform.setNp2zipcode("");
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
                    siform.setNp3eori(Validation.checkNull(nt3.getEori()));
                    siform.setNp3persontype(Validation.checkNull(nt3.getPersontype()));
                    siform.setNp3zipcode(Validation.checkNull(nt3.getZipcode()));
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
                    siform.setNp3eori("");
                    siform.setNp3persontype("");
                    siform.setNp3zipcode("");
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
                    siform.setFweori(Validation.checkNull(fw.getEori()));
                    siform.setFwpersontype(Validation.checkNull(fw.getPersontype()));
                    siform.setFwzipcode(Validation.checkNull(fw.getZipcode()));
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
                    siform.setFweori("");
                    siform.setFwpersontype("");
                    siform.setFwzipcode("");
                }
                //BY

                if (by != null) {
                    strCountryName = "";
                    siform.setByname(by.getCustname());
                    siform.setByCustCode(by.getCustCode());
                    siform.setByaddr(by.getAddress1());
                    siform.setByaddr1(by.getAddress2());
                    siform.setBycity(by.getCity());
                    siform.setByphone(Validation.checkNull(by.getTelephone()).replace("--", ""));
                    if (by.getFax() != null && by.getFax().length() > 0 && !by.getFax().equalsIgnoreCase("--") && !by.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                        siform.setByfax(Validation.checkNull(by.getFax()).replace("--", ""));
                    } else {
                        siform.setByfax("");
                    }
                    siform.setBycountry(by.getCountryid());
                    siform.setByemail(by.getEmail());
                    strCountryName = (String) countryHashObj.get(by.getCountryid());
                    if (strCountryName != null) {
                        strCountryName = by.getCountryid() + " - " + strCountryName;
                        siform.setByCountryName(strCountryName);
                    } else {
                        siform.setByCountryName("");
                    }
                    strCityName = by.getCity();//+" - "+strCityName;
                    siform.setByCityName(strCityName);
                    siform.setBystate(concatStateName(by.getState(), by.getCountryid()));
                    siform.setByeori(Validation.checkNull(by.getEori()));
                    siform.setBypersontype(Validation.checkNull(by.getPersontype()));
                    siform.setByzipcode(Validation.checkNull(by.getZipcode()));
                }

                //SL
                if (sl != null) {
                    strCountryName = "";
                    siform.setSlname(sl.getCustname());
                    siform.setSlCustCode(sl.getCustCode());
                    siform.setSladdr(sl.getAddress1());
                    siform.setSladdr1(sl.getAddress2());
                    siform.setSlcity(sl.getCity());
                    siform.setSlphone(Validation.checkNull(sl.getTelephone()).replace("--", ""));
                    if (sl.getFax() != null && sl.getFax().length() > 0 && !sl.getFax().equalsIgnoreCase("--") && !sl.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                        siform.setSlfax(Validation.checkNull(sl.getFax()).replace("--", ""));
                    } else {
                        siform.setSlfax("");
                    }
                    siform.setSlcountry(sl.getCountryid());
                    siform.setSlemail(sl.getEmail());
                    strCountryName = (String) countryHashObj.get(sl.getCountryid());
                    if (strCountryName != null) {
                        strCountryName = sl.getCountryid() + " - " + strCountryName;
                        siform.setSlCountryName(strCountryName);
                    } else {
                        siform.setSlCountryName("");
                    }
                    strCityName = sl.getCity();//+" - "+strCityName;
                    siform.setSlCityName(strCityName);
                    siform.setSlstate(concatStateName(sl.getState(), sl.getCountryid()));
                    siform.setSleori(Validation.checkNull(sl.getEori()));
                    siform.setSlpersontype(Validation.checkNull(sl.getPersontype()));
                    siform.setSlzipcode(Validation.checkNull(sl.getZipcode()));
                }

                //AP
                if (ap != null) {
//                     System.out.println("[ECOM-WAR] BLEditAction         =====apList.size() is===>" + ap);
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
//                     System.out.println("[ECOM-WAR] BLEditAction         ====contry id is====>" + ap.getCountryid());
                    siform.setApemail(ap.getEmail());
                    strCountryName = (String) countryHashObj.get(ap.getCountryid());
//                     System.out.println("[ECOM-WAR] BLEditAction         ===country name is====>" + strCountryName);
//                    System.out.println("[ECOM-WAR] BLEditAction         Country ID: " + ap.getCountryid() + ", Country Name: " + strCountryName);

                    if (strCountryName != null) {
                        strCountryName = ap.getCountryid() + " - " + strCountryName;
                        siform.setApCountryName(strCountryName);
                    } else {
                        siform.setApCountryName("");
                    }
                    strCityName = ap.getCity();//+" - "+strCityName;
                    siform.setApCityName(strCityName);
                    siform.setApstate(concatStateName(ap.getState(), ap.getCountryid()));
                    siform.setApeori(Validation.checkNull(ap.getEori()));
                    siform.setSppersontype(Validation.checkNull(ap.getPersontype()));
                    siform.setApzipcode(Validation.checkNull(ap.getZipcode()));
                }
                if (customerMap != null && !customerMap.isEmpty()) {
                    slcustsList = (ArrayList) customerMap.get("Seller");
                    bltypesList = (ArrayList) customerMap.get("Buyer");

                    if (bltypesList != null && !bltypesList.isEmpty()) {
//                        System.out.println("[ECOM-WAR] EdiSI =byList.size() is===>" + bltypesList.size());
                        for (int i = 0; i < bltypesList.size(); i++) {
                            CustomerDetailsVO vo = (CustomerDetailsVO) bltypesList.get(i);
                            if (vo != null) {
                                strCountryName = "";
                                vo.setPhoneNo(Validation.checkNull(vo.getPhoneNo()).replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                                if (vo.getFax() != null && vo.getFax().length() > 0 && !vo.getFax().equalsIgnoreCase("--") && !vo.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    vo.setFax(Validation.checkNull(vo.getFax()).replace("--", ""));
                                } else {
                                    vo.setFax("");
                                }
//                                System.out.println("[ECOM-WAR] EdiSI ===contry id is====>" + vo.getCountry());
                                strCountryName = (String) countryHashObj.get(vo.getCountry());
//                                System.out.println("[ECOM-WAR] EdiSI =country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = vo.getCountry() + " - " + strCountryName;
                                    vo.setCountry(strCountryName);
                                } else {
                                    vo.setCountry("");
                                }
                                vo.setState(concatStateName(vo.getState(), vo.getCountry()));
                            }
                        }
                    }
                    //Seller

                    if (slcustsList != null && !slcustsList.isEmpty()) {
                        System.out.println("[ECOM-WAR] EdiSI =slcustsList.size() is===>" + slcustsList.size());
                        for (int i = 0; i < slcustsList.size(); i++) {
//                            System.out.println("[ECOM-WAR] EdiSI === i is=======>" + i);
                            CustomerDetailsVO cvo = (CustomerDetailsVO) slcustsList.get(i);
                            if (cvo != null) {
                                strCountryName = "";
                                cvo.setPhoneNo(Validation.checkNull(cvo.getPhoneNo()).replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                                if (cvo.getFax() != null && cvo.getFax().length() > 0 && !cvo.getFax().equalsIgnoreCase("--") && !cvo.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    cvo.setFax(Validation.checkNull(cvo.getFax()).replace("--", ""));
                                } else {
                                    cvo.setFax("");
                                }
//                                System.out.println("[ECOM-WAR] EdiSI ===contry id is====>" + cvo.getCountry());
                                strCountryName = (String) countryHashObj.get(cvo.getCountry());
//                                System.out.println("[ECOM-WAR] EdiSI =country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = cvo.getCountry() + " - " + strCountryName;
                                    cvo.setCountry(strCountryName);
                                } else {
                                    cvo.setCountry("");
                                }
                                cvo.setState(concatStateName(cvo.getState(), cvo.getCountry()));
                            }
                        }
                    }
                }
                System.out.println("[BL EDIT] Buyer grid size: " + (bltypesList != null ? bltypesList.size() : "null"));
                System.out.println("[BL EDIT] Seller grid size: " + (slcustsList != null ? slcustsList.size() : "null"));
                session.setAttribute("ebooking_seller_grid", slcustsList);
                session.setAttribute("ebooking_buyere_grid", bltypesList);
                if ("View".equalsIgnoreCase(type) || "DashBLView".equalsIgnoreCase(type)) {
                    session.setAttribute("BLtype", "view");
                    SUCCESS = "siview";
                }
            } else {
                SUCCESS = "sessionex";
            }
            System.out.println("[ECOM-WAR] BLEditAction         =====Ended in Performing BL Actions(View/Edit) and result is======" + SUCCESS);
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
        siform.setSpzipcode("");
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
        siform.setConszipcode("");
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
        siform.setNpzipcode("");
        siform.setNp2zipcode("");
        siform.setNp3zipcode("");
        siform.setFwzipcode("");
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
            System.out.println("[ECOM-WAR] BLEditAction         splited>>" + wordList);
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
