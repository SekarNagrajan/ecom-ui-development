package net.ecom.action;

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
import net.ecom.common.CommonMail;
import net.ecom.common.GeneralConf;
import net.ecom.common.NumberComparator;
import net.ecom.common.Validation;
import net.ecom.formbeans.SIEditForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1SIBeanRemote;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.SIBLMasterVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.vds.serviceVo.BLChargeVo;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.common.utils.MaskPortUtil;

public class EditSI extends org.apache.struts.action.Action {

    EcomShippingInstructionBeanRemote locobj = EJBLookUp.getSIBeanRemoteLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomV1SIBeanRemote v1SIObj = EJBLookUp.getV1SIBeanLookUp();
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomBLBeanRemote blobj = EJBLookUp.getBLBeanLookUp();
    Map stateMap = null, stateMapCountry = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String SUCCESS = "success";
        String isHeader = null, shipperSealShipmentType = "";
        String isHSCodeShow = "NO", isShipperSeal, shipperSealConfig;
        String pol = null, pod = null, polCountry, podCountry, headerShipmentType = "", hscodeShipmentType = "", headerConfigResult = null, hscodeConfigResult = null, loadportcountry = null, dischargeportcountry = null;
        try {
            System.out.println("[ECOM-WAR] EdiSI ==EditSI=== " + request.getParameter("sino"));
            SIEditForm siform = (SIEditForm) form;
            setEmptyValuesForCust(siform);
            HttpSession session = request.getSession();
            saveToken(request);
            String type = Validation.checkNull(request.getParameter("type"));
            System.out.println("[ECOM-WAR] EdiSI ==EditSI type==========" + type);
            String sino = Validation.checkNull(request.getParameter("sino"));
            String blno = Validation.checkNull(request.getParameter("blno"));
            String bookno = Validation.checkNull(request.getParameter("bookno"));
            String appVersion = request.getParameter("appversion");
            System.out.println("[ECOM-WAR] EdiSI >>appversion>>" + appVersion);
            String resubmission = Validation.checkNull(request.getParameter("resubmission"));
            siform.setResubmission(resubmission);
            String ccMailId = null, addremarks = null;
            SIBLMasterVO masterVO = null;
            System.out.println("[ECOM-WAR] EdiSI  ==bookno is=====>" + bookno);
            String bookstatus = Validation.checkNull(request.getParameter("bookstatus"));
            String custref = Validation.checkNull(request.getParameter("custref"));
            String custcode = (String) session.getAttribute("SESSION_USERCODE");
            String submittedStatus = Validation.checkNull(request.getParameter("submittedsi"));
            ServletContext contexts = servlet.getServletContext();
            SIPartiesVO parties_routing = null;
            Hashtable countryHashObj = (Hashtable) contexts.getAttribute("SESSION_COUNTRYOBJECT");
            Hashtable comlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
            Hashtable vessellist = (Hashtable) contexts.getAttribute("SESSION_VESSELLIST");
            String SHOW_CHARGES_IN_SIBL = (String) contexts.getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            Map<String, String> fieldShipmentMap = (Map<String, String>) contexts.getAttribute("FIELDCONFIGSHIPMENTMAP");
            String hidefaxno = (String) contexts.getAttribute(GeneralConf.HideFaxNo);
            //V1 Cache
            Map<String, Map<String, String>> masterMap = (Map<String, Map<String, String>>) contexts.getAttribute("ECOMV1MASTERCACHE");
            Map<String, String> v1ComdtyMap = masterMap.get("COMMODITY");
            //
            Hashtable portlist = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PORTLIST");
            String carrierCode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
            String enbleafricaReqs = (String) contexts.getAttribute(GeneralConf.Enable_africaReqsMandatory);
            String enableAddinfoBltype = (String) contexts.getAttribute(GeneralConf.ENABLE_ADDINFO_BLTYPE);
            String TENS_Documentation = (String) contexts.getAttribute(GeneralConf.TENS_Documentation);
            Map<SIMasterVO, SIConatainerValueVO> siDraftCntInfoHashObj = null;
            HashMap bk_port_details = null;
            Map<SIMasterVO, SIConatainerValueVO> siCntInfoHashObj = null;
            HashMap SI_CustDetails = null;
            HashMap customerMap = null;
            ArrayList slcustsList = null;
            ArrayList bltypesList = null;

            Map hs = null;
            String refval = null;
            String orgTelType = null;
            String pla_free_txt = null, pol_free_txt = null, pod_free_txt = null, pld_free_txt = null, voyage_free_txt = null;
            Set siSet = (Set) session.getAttribute("siAccessList");
            if (siSet.contains(bookno)) {
                if ("edit".equalsIgnoreCase(type)) {
                    // SI_CustDetails = locobj.fetchBookingDetForSI(bookno);
                    if ("1".equalsIgnoreCase(appVersion)) {
                        SI_CustDetails = v1SIObj.fetchV1BookingDetForSI(bookno);

                        siCntInfoHashObj = v1SIObj.fetchV1ContainerDetailForSI(bookno);
                        bk_port_details = (HashMap) v1SIObj.fetchPortDetForSI(bookno);
                        parties_routing = (SIPartiesVO) bk_port_details.get("routing");
                    } else {
                        SI_CustDetails = locobj.fetchSICustomerDetails(bookno, sino, custcode);
                        if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                            customerMap = locobj.fetchSICustDetail(bookno, sino, custcode);
                        }
                        siCntInfoHashObj = locobj.fetchContainerDetailForSI(bookno);
                        bk_port_details = (HashMap) locobj.fetchPortDetForSI(bookno);
                        parties_routing = (SIPartiesVO) bk_port_details.get("routing");
                        if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {
                            request.setAttribute("CHARGEDETAILS", locobj.getBookingcharges(bookno));
                        }
                    }
                    hs = (Map) locobj.getBKGReference(bookno);
                    if (hs != null) {
                        refval = (String) hs.get("ACI");
                        if (refval != null) {
                            siform.setAcidValue(refval);
                        }
                        orgTelType = (String) hs.get("ORG");
                        if (orgTelType != null) {
                            siform.setOrgTelType(orgTelType);
                        }
                    }
                    if ("Y".equalsIgnoreCase(enbleafricaReqs)) {
                        String packrefval = null, invocerefval = null;
                        if (hs != null) {
                            packrefval = (String) hs.get("PAC");
                            invocerefval = (String) hs.get("INU");
                            siform.setPackinglst(packrefval);
                            siform.setInvoiceupload(invocerefval);
                        }
                    }
                    System.out.println("[ECOM-WAR] EdiSI ==parties_routing is=====>" + parties_routing);
                    if (parties_routing != null) {
                        System.out.println("[ECOM-WAR] EdiSI >>parties_routing.getVesselName()>>>" + parties_routing.getVesselName());
                        ccMailId = Validation.checkNull(parties_routing.getCcMailId());
                        pol = Validation.checkNull(parties_routing.getPolName());
                        pod = Validation.checkNull(parties_routing.getPodName());
                        loadportcountry = Validation.checkNull(parties_routing.getPol());
                        dischargeportcountry = Validation.checkNull(parties_routing.getDelivery());
                        siform.setOriginid(Validation.checkNull(parties_routing.getReceipt()));
                        siform.setFinalportid(Validation.checkNull(parties_routing.getDelivery()));
                        System.out.println("[ECOM-WAR] EdiSI ==>" + siform.getFinalportid() + "==>" + siform.getOriginid());
                        //System.out.println("[ECOM-WAR] EdiSI     >>vessellist>>>"+vessellist);
                        if (!"1".equalsIgnoreCase(appVersion)) {
                            parties_routing.setPolName(Validation.checkNull((String) portlist.get(Validation.checkNull(parties_routing.getPolName()).toUpperCase())));
                            parties_routing.setPodName(Validation.checkNull((String) portlist.get(Validation.checkNull(parties_routing.getPodName()).toUpperCase())));
                            parties_routing.setIn_origin(Validation.checkNull(parties_routing.getReceipt()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(parties_routing.getReceipt()).toUpperCase())));
                            parties_routing.setIn_delivery(Validation.checkNull(parties_routing.getDelivery()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(parties_routing.getDelivery()).toUpperCase())));
                            parties_routing.setReceipt(Validation.checkNull((String) portlist.get(Validation.checkNull(parties_routing.getReceipt()).toUpperCase())));
                            parties_routing.setDelivery(Validation.checkNull((String) portlist.get(Validation.checkNull(parties_routing.getDelivery()).toUpperCase())));
                        }
                        parties_routing.setVesselName(Validation.checkNull((String) vessellist.get(Validation.checkNull(parties_routing.getVesselName()).toUpperCase())));
                        parties_routing.setPolat(DateConversion_New.convertDefaultFormatwithTime(parties_routing.getPolat()));
                        parties_routing.setPodat(DateConversion_New.convertDefaultFormatwithTime(parties_routing.getPodat()));
                      
                        String plaDisplayName = MaskPortUtil.getDisplayPortName(pol, parties_routing.getReceipt());
                        siform.setPla_free_txt(plaDisplayName);
                       
                        String polDisplayName = MaskPortUtil.getDisplayPortName(pol, parties_routing.getPolName());
                        siform.setPol_free_txt(polDisplayName);
                        request.setAttribute("SIAGENCYREFNO", parties_routing.getAgencyRefNo());
                        session.setAttribute("SIAGENCYREFNO_For_Mail", parties_routing.getAgencyRefNo());
                       
                        String podDisplayName = MaskPortUtil.getDisplayPortName(pod, parties_routing.getPodName());
                        siform.setPod_free_txt(podDisplayName);
                        
                        String pldDisplayName = MaskPortUtil.getDisplayPortName(pod, parties_routing.getDelivery());
                        siform.setPld_free_txt(pldDisplayName);
                        siform.setVslvoyage_free_txt(parties_routing.getVesselName() + "/" + parties_routing.getVoyagecode() + "/" + parties_routing.getBound());
                        siform.setHid_pla_free_txt(MaskPortUtil.getDisplayPortName(pol, parties_routing.getReceipt()));
                        siform.setHid_pol_free_txt(MaskPortUtil.getDisplayPortName(pol, parties_routing.getPolName()));
                        siform.setHid_pod_free_txt(MaskPortUtil.getDisplayPortName(pod, parties_routing.getPodName()));
                        siform.setHid_pld_free_txt(MaskPortUtil.getDisplayPortName(pod, parties_routing.getDelivery()));
                        siform.setHid_vslvoyage_free_txt(parties_routing.getVesselName() + " " + parties_routing.getVoyagecode() + "/" + parties_routing.getBound());
                        siform.setCcMailId(ccMailId);
                        siform.setCargoprotectid(parties_routing.getCargoprotectid());
                        siform.setEbook_t2lfiling(parties_routing.getEbook_t2lfiling());
                        siform.setEbook_nvocc(parties_routing.getEbook_nvocc());
                        siform.setEbook_EnsDocType(parties_routing.getEbook_EnsDocType());

                    }
                    siform.setHeaderShipper("");
                    siform.setHeaderConsignee("");
                    siform.setHeaderNotify("");
                    siform.setSpnameheader("");
                    siform.setConsnameheader("");
                    siform.setNpnameheader("");
                    siform.setWhnameheader("");
                    siform.setFwnameheader("");
                    session.setAttribute("SItype", "edit");
                    addremarks = (String) SI_CustDetails.get("addremarks");
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                        parties_routing.setIsmaskport(locobj.checkMaskPortForSIBL(bookno));
                    } else {
                        parties_routing.setIsmaskport(true);
                    }
                    ENSDeclarantVo ics = locobj.getLRPBookingENSTabDetails(bookno);
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
                    session.setAttribute("IsEnableENSTabInSIBL", "N");
                    if (parties_routing.isIseuport()) {
                        session.setAttribute("IsEnableENSTabInSIBL", "Y");
                    }
                } else {// common for DRAFT and VIEW
                    //sino = sino.replace(GeneralConf.SI_PREFIX, "");
//                    System.out.println("[ECOM-WAR] EdiSI >>bookno>>" + bookno);
                    if ("1".equalsIgnoreCase(appVersion)) {
                        bk_port_details = (HashMap) v1SIObj.fetchPortDetForSI(bookno);
                    } else {
                        bk_port_details = (HashMap) locobj.fetchPortDetForSI(bookno);
                    }
//                    System.out.println("[ECOM-WAR] EdiSI >>bk_port_details>>" + bk_port_details);
                    System.out.println("[ECOM-WAR] EdiSI >>bookno>> " + bookno + ", >>bk_port_details>> " + bk_port_details);

                    SIPartiesVO parties_routing1 = (SIPartiesVO) locobj.getSIMasterDetails(sino);
                    //SI_CustDetails = locobj.fetchSICustomerDetails(bookno, sino, custref);
                    SI_CustDetails = locobj.fetchSICustomerDetailsFromEcom(sino);
                    if ("Y".equalsIgnoreCase(TENS_Documentation)) {
                        customerMap = locobj.fetchSICusListFromEcom(sino);
                    }
                    siDraftCntInfoHashObj = locobj.fetchSIContainerDetail(bookno, sino);
                    /**
                     * if ("yes".equalsIgnoreCase(resubmission)) {
                     * siform.setSino(""); } else { siform.setSino(sino); }*
                     */
                    List filenamelist = ecomCntrlObj.getSubmittedFileNames(sino, (String) contexts.getAttribute(GeneralConf.BKG_FILE_UPLOAD_PATH), "Ecom");
                    request.setAttribute("FILENAMELIST", filenamelist);

                    siform.setSino(sino);
                    siCntInfoHashObj = (Map<SIMasterVO, SIConatainerValueVO>) siDraftCntInfoHashObj;
                    SIPartiesVO routingVO = (SIPartiesVO) bk_port_details.get("routing");
                    request.setAttribute("SIAGENCYREFNO", routingVO.getAgencyRefNo());
                    session.setAttribute("SIAGENCYREFNO_For_Mail", routingVO.getAgencyRefNo());
                    pol = Validation.checkNull(routingVO.getPolName());
                    pod = Validation.checkNull(routingVO.getPodName());
                    loadportcountry = Validation.checkNull(routingVO.getPol());
                    dischargeportcountry = Validation.checkNull(routingVO.getDelivery());
                    siform.setOriginid(Validation.checkNull(routingVO.getReceipt()));
                    siform.setFinalportid(Validation.checkNull(routingVO.getDelivery()));
                    System.out.println("[ECOM-WAR] EdiSI  else ==>" + siform.getFinalportid() + "==>" + siform.getOriginid());
                    if (!"1".equalsIgnoreCase(appVersion)) {
                        routingVO.setPolName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPolName()).toUpperCase())));
                        routingVO.setPodName(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getPodName()).toUpperCase())));
                        routingVO.setIn_origin(Validation.checkNull(routingVO.getReceipt()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getReceipt()).toUpperCase())));
                        routingVO.setIn_delivery(Validation.checkNull(routingVO.getDelivery()).toUpperCase() + " - " + Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getDelivery()).toUpperCase())));
                        routingVO.setReceipt(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getReceipt()).toUpperCase())));
                        routingVO.setDelivery(Validation.checkNull((String) portlist.get(Validation.checkNull(routingVO.getDelivery()).toUpperCase())));
                    }
                    routingVO.setVesselName(Validation.checkNull((String) vessellist.get(Validation.checkNull(routingVO.getVesselName()).toUpperCase())));
                    routingVO.setPolat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPolat()));
                    routingVO.setPodat(DateConversion_New.convertDefaultFormatwithTime(routingVO.getPodat()));
                    //pla_free_txt = parties_routing1.getPla_free_txt() != null ? parties_routing1.getPla_free_txt().trim() : routingVO.getReceipt();
                    //pol_free_txt = parties_routing1.getPol_free_txt() != null ? parties_routing1.getPol_free_txt().trim() : routingVO.getPolName();
                    //pod_free_txt = parties_routing1.getPod_free_txt() != null ? parties_routing1.getPod_free_txt().trim() : routingVO.getPodName();
                    //pld_free_txt = parties_routing1.getPld_free_txt() != null ? parties_routing1.getPld_free_txt().trim() : routingVO.getDelivery();
                    pla_free_txt = parties_routing1.getPla_free_txt();
//                    System.out.println("[ECOM-WAR] EdiSI pla_free_txt>>>>>>>>>>>>>>>>>>>>>>>>" + pla_free_txt);
                    pol_free_txt = parties_routing1.getPol_free_txt();
//                    System.out.println("[ECOM-WAR] EdiSI pol_free_txt>>>>>>>>>>>>>>>>>>>>>>>>" + pol_free_txt);
                    pod_free_txt = parties_routing1.getPod_free_txt();
//                    System.out.println("[ECOM-WAR] EdiSI pod_free_txt>>>>>>>>>>>>>>>>>>>>>>>>" + pod_free_txt);
                    pld_free_txt = parties_routing1.getPld_free_txt();
//                    System.out.println("[ECOM-WAR] EdiSI pld_free_txt>>>>>>>>>>>>>>>>>>>>>>>>" + pld_free_txt);
                    System.out.println("[ECOM-WAR] EdiSI pld_free_txt: " + pld_free_txt + ", pod_free_txt: " + pod_free_txt + ", pol_free_txt: " + pol_free_txt + ", pla_free_txt: " + pla_free_txt);

                    voyage_free_txt = routingVO.getVesselName() + "/" + routingVO.getVoyagecode() + "/" + routingVO.getBound();
                   
                    String plaDisplayNameDraft = MaskPortUtil.getDisplayPortName(pol, pla_free_txt);
                    siform.setPla_free_txt(plaDisplayNameDraft);
                    
                    String polDisplayNameDraft = MaskPortUtil.getDisplayPortName(pol, pol_free_txt);
                    siform.setPol_free_txt(polDisplayNameDraft);
                    
                    String podDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, pod_free_txt);
                    siform.setPod_free_txt(podDisplayNameDraft);
                    
                    String pldDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, pld_free_txt);
                    siform.setPld_free_txt(pldDisplayNameDraft);
                    siform.setVslvoyage_free_txt(voyage_free_txt);
//                siform.setHid_pla_free_txt(pla_free_txt);
//                siform.setHid_pol_free_txt(pol_free_txt);
//                siform.setHid_pod_free_txt(pod_free_txt);
//                siform.setHid_pld_free_txt(pld_free_txt);
//                siform.setHid_vslvoyage_free_txt(voyage_free_txt);
                   
                    String hidPlaDisplayNameDraft = MaskPortUtil.getDisplayPortName(siform.getOriginid(), routingVO.getReceipt());
                    siform.setHid_pla_free_txt(hidPlaDisplayNameDraft);
                    String hidPolDisplayNameDraft = MaskPortUtil.getDisplayPortName(pol, routingVO.getPolName());
                    siform.setHid_pol_free_txt(hidPolDisplayNameDraft);
                    String hidPodDisplayNameDraft = MaskPortUtil.getDisplayPortName(pod, routingVO.getPodName());
                    siform.setHid_pod_free_txt(hidPodDisplayNameDraft);
                    String hidPldDisplayNameDraft = MaskPortUtil.getDisplayPortName(siform.getFinalportid(), routingVO.getDelivery());
                    siform.setHid_pld_free_txt(hidPldDisplayNameDraft);
                    siform.setHid_vslvoyage_free_txt(routingVO.getVesselName() + "/" + routingVO.getVoyagecode() + "/" + routingVO.getBound());
                    siform.setAESDisclaimer(parties_routing1.getAESDiscliamer());
                    siform.setSiAESnumber(parties_routing1.getAESNumber());
                    siform.setReleasetype(parties_routing1.getReleasetype());
                    siform.setDeclaredValue(parties_routing1.getDeclaredValue());
                    siform.setAcidValue(parties_routing1.getACID());
                    siform.setPackinglst(parties_routing1.getPackinglst());
                    siform.setInvoiceupload(parties_routing1.getInvoiceupload());
                    siform.setFreightoption(parties_routing1.getFreight());
                    siform.setBltype(parties_routing1.getBltype());
                    siform.setEbook_t2lfiling(parties_routing1.getEbook_t2lfiling());
                    siform.setEbook_nvocc(parties_routing1.getEbook_nvocc());
                    routingVO.setEbook_nvocc(parties_routing1.getEbook_nvocc());
                    siform.setCargoprotectid(parties_routing1.getCargoprotectid());
                    siform.setOrgTelType(parties_routing1.getOrgTelType());
                    siform.setEbook_EnsDocType(parties_routing1.getEbook_EnsDocType());
                    siform.setBlType_UAE(parties_routing1.getBltype_UAE());
                    siform.setMpciId_UAE(parties_routing1.getMpci_id());
                    session.setAttribute("SItype", "Draft");
                    masterVO = (SIBLMasterVO) SI_CustDetails.get("MASTERDETAILS");
                    System.out.println("[ECOM-WAR] EdiSI >>>masterVO>>" + masterVO);

                    if (masterVO != null) {
//                        System.out.println("[ECOM-WAR] EdiSI >>>masterVO.getShipperHeader()>>" + masterVO.getShipperHeader());
//                        System.out.println("[ECOM-WAR] EdiSI >>masterVO.getConsigneeHeader()>>" + masterVO.getConsigneeHeader());
                        System.out.println("[ECOM-WAR] EdiSI ShipperHeader: " + masterVO.getShipperHeader() + ", ConsigneeHeader: " + masterVO.getConsigneeHeader());

                        siform.setCcMailId(Validation.checkNull(masterVO.getCcMailId()));
                        addremarks = Validation.checkNull(masterVO.getCustRemarks());
                        siform.setHeaderShipper(Validation.checkNull(masterVO.getShipperHeader()));
                        siform.setHeaderConsignee(Validation.checkNull(masterVO.getConsigneeHeader()));
                        siform.setHeaderNotify(Validation.checkNull(masterVO.getNotifyHeader()));
                        siform.setSpnameheader(Validation.checkNull(masterVO.getShipperName()));
                        siform.setConsnameheader(Validation.checkNull(masterVO.getConsigneeName()));
                        siform.setNpnameheader(Validation.checkNull(masterVO.getNotifyName()));
                        siform.setFwnameheader(Validation.checkNull(masterVO.getForwarderName()));
                        siform.setWhnameheader(Validation.checkNull(masterVO.getWarehouseName()));

                    }
                    if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {
                        request.setAttribute("CHARGEDETAILS", locobj.getSICharges(sino));
                    }
                    if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {

                        List EBK_pluginList = (List) contexts.getAttribute("EBK_PLUGIN_DETAILS");
                        boolean is_SI_restriction = false;
                        if (EBK_pluginList != null && EBK_pluginList.contains("ESP")) {
                            is_SI_restriction = true;
                        }
                        routingVO.setDeclaredValue(parties_routing1.getDeclaredValue());
                        routingVO.setACID(parties_routing1.getACID());
                        routingVO.setBltype(parties_routing1.getBltype());
                        HashMap masterCache = new HashMap();
                        Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
                        Hashtable serlist = (Hashtable) contexts.getAttribute("SESSION_SERVICELIST");
                        Hashtable packagemap = (Hashtable) contexts.getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                        String carriercode = (String) contexts.getAttribute(GeneralConf.CARRIER_CODE);
                        String Ventilationlabel = "Ventilation" + (String) contexts.getAttribute("Ventilation");
                        masterCache.put("Ventilation", Ventilationlabel);
                        masterCache.put("VESSELLIST", vessellist);
                        masterCache.put("CONTRYLIST", contrylist);
                        masterCache.put("SERVICELIST", serlist);
                        masterCache.put("PACKAGELIST", packagemap);
                        masterCache.put("PORTLIST", portlist);
                        masterCache.put("carriercode", carriercode);
                        masterCache.put("comlist", comlist);
                        masterCache.put("agencyRefNo", routingVO.getAgencyRefNo());
                        masterCache.put("SHOW_CHARGES_IN_SIBL", (String) contexts.getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL));
                        masterCache.put("is_SI_restriction", is_SI_restriction);
                        masterCache.put(GeneralConf.Display_HS_NAME_CODE, (String) contexts.getAttribute(GeneralConf.Display_HS_NAME_CODE));
                        masterCache.put("EnableReeferDetails", (String) contexts.getAttribute(GeneralConf.EnableReeferDetails));
                        masterCache.put("Enable_Eori_Details", (String) contexts.getAttribute(GeneralConf.GENS));
                        masterCache.put("enable_zipcode", (String) contexts.getAttribute(GeneralConf.Enable_Zip_Code));
                        session.setAttribute("SIPrintCache", masterCache);
                        session.setAttribute("SIPrint_SINo", sino);
                        session.setAttribute("SIPrint_BookNo", bookno);
                        session.setAttribute("SIPrint_routing", routingVO);
                        session.setAttribute("SIPrint_form", siform);
                    }
                    if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.SHOW_MASKPORT_RECORDS))) {
                        routingVO.setIsmaskport(locobj.checkMaskPortForSIBL(bookno));
                    } else {
                        routingVO.setIsmaskport(true);
                    }
                    ENSDeclarantVo ics = locobj.getENSTabDetails(sino);
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
                    session.setAttribute("IsEnableENSTabInSIBL", "N");
                    if (routingVO.isIseuport()) {
                        session.setAttribute("IsEnableENSTabInSIBL", "Y");
                    }
                }
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.SI_CommodityGroup_Edit_Option))) {
                    String rateCmdtyJsonarr = locobj.getRateCommodityList(bookno, "BKG");
                    session.setAttribute("SIBLRateCmdtyJsonarr", rateCmdtyJsonarr);
                }
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.TENS_Documentation))) {
                    String ensFiling = locobj.fetForSIENSFiling(bookno);
                    System.out.println("ensFiling===>" + ensFiling);
                    siform.setEbook_EnsFiling(ensFiling);
                }
                if ("Y".equalsIgnoreCase((String) contexts.getAttribute(GeneralConf.Display_Charge_Tab))) {
                    List<BLChargeVo> siChargeSummary = blobj.getSICgargeSummaryList(bookno);
                    session.setAttribute("SIBLChargeSummary", siChargeSummary);

                }
//                System.out.println("[ECOM-WAR] EdiSI     loadportcountry==> " + loadportcountry);
                if (loadportcountry != null && loadportcountry.length() > 2) {
                    loadportcountry = loadportcountry.substring(0, 2);
                    request.setAttribute("LoadportCountry", loadportcountry);
                }
                if (dischargeportcountry != null && dischargeportcountry.length() > 2) {
                    dischargeportcountry = dischargeportcountry.substring(0, 2);
                    request.setAttribute("dischargeportcountry", dischargeportcountry);
                }
                System.out.println("[ECOM-WAR] EdiSI     ==siCntInfoHashObj.size() is=================================>" + siCntInfoHashObj.size() + ",loadportcountry:" + loadportcountry);
                if (siCntInfoHashObj != null && siCntInfoHashObj.size() > 0) {
                    Collection<SIMasterVO> collection = siCntInfoHashObj.keySet();
                    SIMasterVO[] empArray = new SIMasterVO[siCntInfoHashObj.size()];
                    SIMasterVO[] array = (SIMasterVO[]) collection.toArray(empArray);
                    Arrays.sort(array, new NumberComparator());
                }
                //String flpath = (String) SI_CustDetails.get("File_path");

                HashMap fileHM = (HashMap) SI_CustDetails.get("File_path");
                String fileNameVGM = "";
                String fileNameDG = "";
                String fileNameLOL = "";
                String fileNameMSDS = "";
                String fileNameOT = "";
                if (fileHM != null) {
                    fileNameVGM = (String) fileHM.get("VGM");
                    fileNameDG = (String) fileHM.get("DG");
                    fileNameLOL = (String) fileHM.get("LOL");
                    fileNameMSDS = (String) fileHM.get("MSD");
                    fileNameOT = (String) fileHM.get("OT");
                }

                if (fileNameVGM != null && fileNameVGM.length() > 0) {
                    siform.setFilename1(fileNameVGM);
                }
                if (fileNameDG != null && fileNameDG.length() > 0) {
                    siform.setFilename2(fileNameDG);
                }
                if (fileNameLOL != null && fileNameLOL.length() > 0) {
                    siform.setFilename3(fileNameLOL);
                }
                if (fileNameMSDS != null && fileNameMSDS.length() > 0) {
                    siform.setFilename4(fileNameMSDS);
                }
                if (fileNameOT != null && fileNameOT.length() > 0) {
                    siform.setFilename5(fileNameOT);
                }

                System.out.println("[ECOM-WAR] EdiSI     addremarks::::::" + addremarks);
                siform.setSicustremarks(Validation.checkNull(addremarks));
                if ("S".equalsIgnoreCase(submittedStatus)) {
                    siform.setSicustremarks("");
                } else {
                    siform.setSicustremarks(Validation.checkNull(addremarks));
                }
                if (fieldShipmentMap != null) {
                    headerShipmentType = fieldShipmentMap.get("SHC");
                    hscodeShipmentType = fieldShipmentMap.get("HSC");
                    shipperSealShipmentType = fieldShipmentMap.get("SHS");
                }
                isHeader = "NO";
                isHSCodeShow = "NO";
                isShipperSeal = "NO";
                System.out.println("[ECOM-WAR] EdiSI     >>shipmentType>>" + headerShipmentType + ">hscodeShipmentType>>" + hscodeShipmentType + " >>si pol>" + pol + "sipod>" + pod);
                headerConfigResult = "";
                polCountry = Validation.checkNull(bookingObj.getCountryCodeForPort(pol));
                System.out.println("[ECOM-WAR] EdiSI     polCountry................" + polCountry);
                podCountry = Validation.checkNull(bookingObj.getCountryCodeForPort(pod));
                System.out.println("[ECOM-WAR] EdiSI     podCountry................" + podCountry);
                if ("E".equalsIgnoreCase(headerShipmentType)) {
                    headerConfigResult = ecomCntrlObj.getFieldConfg(polCountry, pol, "SHC", "E");
                } else if ("I".equalsIgnoreCase(headerShipmentType)) {
                    headerConfigResult = ecomCntrlObj.getFieldConfg(podCountry, pod, "SHC", "I");
                }
                System.out.println("[ECOM-WAR] EdiSI     headerConfigResult......." + headerConfigResult);
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
                System.out.println("[ECOM-WAR] EdiSI     >>isHSCodeShow>>" + isHSCodeShow);
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
                System.out.println("[ECOM-WAR] EdiSI     >>>>shipper seal required>" + isShipperSeal);
                //
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
                                    siCntDetVo.setAppVersion(appVersion);
                                    siCntDetVo.setHsCodeDesc(Validation.checkNull(siCntDetVo.getHsCode()));
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
                                    if (siCntDetVo.getHsCode() != null && siCntDetVo.getHsCode().length() > 0) {
                                        siCntDetVo.setHsCodeDesc(Validation.checkNull((String) comlist.get(Validation.checkNull(siCntDetVo.getHsCode()))));
                                        siCntDetVo.setAppVersion(appVersion);
                                    }
                                }
                            }
                        }
                    }

                }
                request.setAttribute("HEADERCONFIGURATION", isHeader);
                request.setAttribute("HSCODECONFIGURATION", isHSCodeShow);
                request.setAttribute("agencyemailid", locobj.getAgencyEmail(bookno));
                session.setAttribute("SHIPPERSEALCONFIG", isShipperSeal);
                session.setAttribute("appVersion", appVersion);
                request.setAttribute("EDITSIAPPVERSION", appVersion);
                session.setAttribute("HSCODECONFIG", isHSCodeShow);
                session.setAttribute("containerListForSI", siCntInfoHashObj);
                session.setAttribute("containerListDraftForSI", siDraftCntInfoHashObj);
                session.setAttribute("edit_bookno", bookno);
                session.setAttribute("edit_sino", sino);
                session.setAttribute("edit_blno", blno);
                session.setAttribute("bk_port_details", bk_port_details);
                session.setAttribute("bookstatus", bookstatus);
                siform.setAppVersion(appVersion);
                //
                if ("1".equalsIgnoreCase(appVersion) && "edit".equalsIgnoreCase(type)) {
                    SIPartiesVO sh = (SIPartiesVO) SI_CustDetails.get("SH");
                    SIPartiesVO cn = (SIPartiesVO) SI_CustDetails.get("CO");
                    SIPartiesVO nt = (SIPartiesVO) SI_CustDetails.get("NT");
                    SIPartiesVO nt2 = (SIPartiesVO) SI_CustDetails.get("NT2");
                    SIPartiesVO nt3 = (SIPartiesVO) SI_CustDetails.get("NT3");
                    SIPartiesVO fw = (SIPartiesVO) SI_CustDetails.get("FW");

                    //
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
                        strCityName = sh.getCity();//+" - "+strCityName;
                        siform.setSpCityName(strCityName);
                        siform.setSpstate(concatStateName(sh.getState(), sh.getCountryid()));
                        siform.setSpzipcode(Validation.checkNull(sh.getZipcode()));

                        // siform.setSptaxId(Validation.checkNull(sh.getTaxId()));
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
                        siform.setSptaxId("");
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
                        System.out.println("[ECOM-WAR] EdiSI     TOORDER>>" + cn.getToorder());
                        if ("Y".equalsIgnoreCase(cn.getToorder())) {
                            siform.setConsigneeordercheck("Y");
                        } else {
                            siform.setConsigneeordercheck("N");
                        }
                        String conName = Validation.checkNull(cn.getCustname());

                        System.out.println("[ECOM-WAR] EdiSI     conName>>" + conName);

                        if ("edit".equalsIgnoreCase(type) && conName.length() > 0) {
                            System.out.println("[ECOM-WAR] EdiSI     inside>>");
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
                        strCityName = cn.getCity();
                        siform.setConsCityName(strCityName);
                        siform.setConsstate(concatStateName(cn.getState(), cn.getCountryid()));
                        siform.setConszipcode(Validation.checkNull(cn.getZipcode()));
                        // siform.setConsTaxId(Validation.checkNull(cn.getTaxId()));
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
                        siform.setConsTaxId("");
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
                        strCityName = nt.getCity();//+" - "+strCityName;
                        siform.setNpCityName(strCityName);
                        siform.setNpstate(concatStateName(nt.getState(), nt.getCountryid()));
                        siform.setNpzipcode(Validation.checkNull(nt.getZipcode()));
                        // siform.setNpTaxId(Validation.checkNull(nt.getTaxId()));

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
                        siform.setNpTaxId("");
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
                        strCityName = nt2.getCity();//+" - "+strCityName;
                        siform.setNp2CityName(strCityName);
                        siform.setNp2state(concatStateName(nt2.getState(), nt2.getCountryid()));
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
                        strCityName = nt3.getCity();//+" - "+strCityName;
                        siform.setNp3CityName(strCityName);
                        siform.setNp3state(concatStateName(nt3.getState(), nt3.getCountryid()));
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
                        siform.setFwzipcode("");
                    }

                } else {
                    System.out.println("[ECOM-WAR] EdiSI     >>SI_CustDetails>>>" + SI_CustDetails);
                    ArrayList shList = (ArrayList) SI_CustDetails.get("SH");
                    ArrayList cnList = (ArrayList) SI_CustDetails.get("CN");
                    ArrayList ntList = (ArrayList) SI_CustDetails.get("NT");
                    ArrayList nt2List = (ArrayList) SI_CustDetails.get("N1");
                    ArrayList nt3List = (ArrayList) SI_CustDetails.get("N2");
                    ArrayList fwList = (ArrayList) SI_CustDetails.get("FW");
                    ArrayList apList = (ArrayList) SI_CustDetails.get("AP");
                    ArrayList byList = (ArrayList) SI_CustDetails.get("BY");
                    ArrayList slList = (ArrayList) SI_CustDetails.get("SL");

                    if (apList == null || apList.size() == 0) {
                        Map custlis = locobj.fetchSICustomerDetails(bookno, sino, custcode);
                        apList = (ArrayList) custlis.get("AP");
                    }

                    //
                    String strCountryName = "", strCityName = "";
                    if (shList != null) {
                        System.out.println("[ECOM-WAR] EdiSI ==shList.size() is===>" + shList.size());
                        for (int i = 0; i < shList.size(); i++) {
                            System.out.println("[ECOM-WAR] EdiSI == i is=======>" + i);
                            CustomerDetailsVO sh = (CustomerDetailsVO) shList.get(i);
                            if (sh != null) {
                                strCountryName = "";
                                siform.setSpname(sh.getName());
                                siform.setSpCustCode(sh.getCustCode());
                                siform.setSpaddr(sh.getAddress());
                                siform.setSpaddr1(sh.getAddress1());
                                siform.setSpcity(sh.getCity());
                                siform.setSpphone(sh.getPhoneNo());
                                if (sh.getFax() != null && sh.getFax().length() > 0 && !sh.getFax().equalsIgnoreCase("--") && !sh.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setSpfax(sh.getFax());
                                } else {
                                    siform.setSpfax("");
                                }
                                siform.setSpcountry(sh.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ===contry id is====>" + sh.getCountry());
                                siform.setSpemail(sh.getEmail());
                                strCountryName = (String) countryHashObj.get(sh.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ==country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = sh.getCountry() + " - " + strCountryName;
                                    siform.setSpCountryName(strCountryName);
                                } else {
                                    siform.setSpCountryName("");
                                }
                                strCityName = sh.getCity();//+" - "+strCityName;
                                siform.setSpCityName(strCityName);
                                siform.setSpstate(concatStateName(sh.getState(), sh.getCountry()));
                                siform.setSpzipcode(Validation.checkNull(sh.getZipcode()));
                                siform.setSpeori(Validation.checkNull(sh.getEroi()));
                                siform.setSppersontype(Validation.checkNull(sh.getPerontype()));
                                siform.setSpzipcode(Validation.checkNull(sh.getZipcode()));

                            }
                        }
                    }

                    if (cnList != null) {
                        for (int j = 0; j < cnList.size(); j++) {
                            CustomerDetailsVO cn = (CustomerDetailsVO) cnList.get(j);
                            if (cn != null) {
                                siform.setConsname(cn.getName());
                                siform.setConsCustCode(cn.getCustCode());
                                siform.setConsaddr(cn.getAddress());
                                siform.setConsaddr1(cn.getAddress1());
                                siform.setConscity(cn.getCity());
                                siform.setConsphone(cn.getPhoneNo());
                                if (cn.getFax() != null && cn.getFax().length() > 0 && !cn.getFax().equalsIgnoreCase("--") && !cn.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {

                                    siform.setConsfax(cn.getFax());
                                } else {
                                    siform.setConsfax("");
                                }
                                siform.setConsemail(cn.getEmail());
                                siform.setConscountry(cn.getCountry());
                                //System.out.println(cn.getToorder());
                                if ("Y".equalsIgnoreCase(cn.getToorder())) {
                                    siform.setConsigneeordercheck("Y");
                                } else {
                                    siform.setConsigneeordercheck("N");
                                }
                                String conName = Validation.checkNull(cn.getName());
                                System.out.println("[ECOM-WAR] EdiSI conName>>" + conName);
                                if ("edit".equalsIgnoreCase(type) && conName.length() > 0) {
                                    System.out.println("[ECOM-WAR] EdiSI inside>>");
                                    if (isTOOrder(conName)) {
                                        siform.setConsigneeordercheck("Y");
                                    }
                                }
                                strCountryName = "";
                                strCountryName = (String) countryHashObj.get(cn.getCountry());
                                if (strCountryName != null) {
                                    strCountryName = cn.getCountry() + " - " + strCountryName;
                                    siform.setConsCountryName(strCountryName);
                                } else {
                                    siform.setConsCountryName("");
                                }
                                strCityName = cn.getCity();
                                siform.setConsCityName(strCityName);
                                siform.setConsstate(concatStateName(cn.getState(), cn.getCountry()));
                                siform.setConszipcode(Validation.checkNull(cn.getZipcode()));
                                siform.setConsTaxId(Validation.checkNull(cn.getTaxId()));
                                siform.setConseori(Validation.checkNull(cn.getEroi()));
                                siform.setConspersontype(Validation.checkNull(cn.getPerontype()));
                                siform.setConszipcode(Validation.checkNull(cn.getZipcode()));

                            }
                        }
                    }

                    if (ntList != null) {
                        for (int k = 0; k < ntList.size(); k++) {
                            CustomerDetailsVO nt = (CustomerDetailsVO) ntList.get(k);
                            if (nt != null) {
                                siform.setNpname(nt.getName());
                                siform.setNpCustCode(nt.getCustCode());
                                siform.setNpaddr(nt.getAddress());
                                siform.setNpaddr1(nt.getAddress1());
                                siform.setNpcity(nt.getCity());
                                siform.setNpphone(nt.getPhoneNo());
                                if (nt.getFax() != null && nt.getFax().length() > 0 && !nt.getFax().equalsIgnoreCase("--") && !nt.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setNpfax(nt.getFax());
                                } else {
                                    siform.setNpfax("");
                                }
                                siform.setNpemail(nt.getEmail());
                                siform.setNpcountry(nt.getCountry());
                                siform.setNotifyCheck(nt.getNotifyCheck());
                                if ("Y".equalsIgnoreCase(nt.getToorder())) {
                                    siform.setNotifyordercheck("Y");
                                } else {
                                    siform.setNotifyordercheck("N");
                                }
                                strCountryName = "";
                                strCountryName = (String) countryHashObj.get(nt.getCountry());
                                if (strCountryName != null) {
                                    strCountryName = nt.getCountry() + " - " + strCountryName;
                                    siform.setNpCountryName(strCountryName);
                                } else {
                                    siform.setNpCountryName("");
                                }
                                strCityName = nt.getCity();//+" - "+strCityName;
                                siform.setNpCityName(strCityName);
                                siform.setNpstate(concatStateName(nt.getState(), nt.getCountry()));
                                siform.setNpzipcode(Validation.checkNull(nt.getZipcode()));
                                siform.setNpTaxId(Validation.checkNull(nt.getTaxId()));
                                siform.setNpeori(Validation.checkNull(nt.getEroi()));
                                siform.setNppersontype(Validation.checkNull(nt.getPerontype()));
                                System.out.println("[ECOM-WAR] EdiSI getNpeori==>" + siform.getNpeori() + "///" + siform.getNppersontype());
                                siform.setNpzipcode(Validation.checkNull(nt.getZipcode()));
                            }
                        }
                    }
// notity 2
                    if (nt2List != null) {
                        for (int k = 0; k < nt2List.size(); k++) {
                            CustomerDetailsVO nt2 = (CustomerDetailsVO) nt2List.get(k);
                            if (nt2 != null) {
                                siform.setNp2name(nt2.getName());
                                siform.setNp2CustCode(nt2.getCustCode());
                                siform.setNp2addr(nt2.getAddress());
                                siform.setNp2addr1(nt2.getAddress1());
                                siform.setNp2city(nt2.getCity());
                                siform.setNp2phone(nt2.getPhoneNo());
                                if (nt2.getFax() != null && nt2.getFax().length() > 0 && !nt2.getFax().equalsIgnoreCase("--") && !nt2.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setNp2fax(nt2.getFax());
                                } else {
                                    siform.setNp2fax("");
                                }
                                siform.setNp2email(nt2.getEmail());
                                siform.setNp2country(nt2.getCountry());
                                siform.setNotify2Check(nt2.getNotifyCheck());
                                if ("Y".equalsIgnoreCase(nt2.getToorder())) {
                                    siform.setNotify2ordercheck("Y");
                                } else {
                                    siform.setNotify2ordercheck("N");
                                }
                                strCountryName = "";
                                strCountryName = (String) countryHashObj.get(nt2.getCountry());
                                if (strCountryName != null) {
                                    strCountryName = nt2.getCountry() + " - " + strCountryName;
                                    siform.setNp2CountryName(strCountryName);
                                } else {
                                    siform.setNp2CountryName("");
                                }
                                strCityName = nt2.getCity();//+" - "+strCityName;
                                siform.setNp2CityName(strCityName);
                                siform.setNp2state(concatStateName(nt2.getState(), nt2.getCountry()));
                                siform.setNp2eori(nt2.getEroi());
                                siform.setNp2persontype(nt2.getPerontype());
                                siform.setNp2zipcode(Validation.checkNull(nt2.getZipcode()));
                            }
                        }
                    }

                    // notity 3
                    if (nt3List != null) {
                        for (int k = 0; k < nt3List.size(); k++) {
                            CustomerDetailsVO nt3 = (CustomerDetailsVO) nt3List.get(k);
                            if (nt3 != null) {
                                siform.setNp3name(nt3.getName());
                                siform.setNp3CustCode(nt3.getCustCode());
                                siform.setNp3addr(nt3.getAddress());
                                siform.setNp3addr1(nt3.getAddress1());
                                siform.setNp3city(nt3.getCity());
                                siform.setNp3phone(nt3.getPhoneNo());
                                if (nt3.getFax() != null && nt3.getFax().length() > 0 && !nt3.getFax().equalsIgnoreCase("--") && !nt3.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setNp3fax(nt3.getFax());
                                } else {
                                    siform.setNp3fax("");
                                }
                                siform.setNp3email(nt3.getEmail());
                                siform.setNp3country(nt3.getCountry());
                                siform.setNotify3Check(nt3.getNotifyCheck());
                                if ("Y".equalsIgnoreCase(nt3.getToorder())) {
                                    siform.setNotify3ordercheck("Y");
                                } else {
                                    siform.setNotify3ordercheck("N");
                                }
                                strCountryName = "";
                                strCountryName = (String) countryHashObj.get(nt3.getCountry());
                                if (strCountryName != null) {
                                    strCountryName = nt3.getCountry() + " - " + strCountryName;
                                    siform.setNp3CountryName(strCountryName);
                                } else {
                                    siform.setNp3CountryName("");
                                }

                                strCityName = "";
                                strCityName = nt3.getCity();//+" - "+strCityName;
                                siform.setNp3CityName(strCityName);
                                siform.setNp3state(concatStateName(nt3.getState(), nt3.getCountry()));
                                siform.setNp3eori(nt3.getEroi());
                                siform.setNp3persontype(nt3.getPerontype());
                                siform.setNp3zipcode(Validation.checkNull(nt3.getZipcode()));
                            }
                        }
                    }
                    //FW
                    if (fwList != null) {
                        System.out.println("[ECOM-WAR] EdiSI ===fwList.size() is===>" + fwList.size());
                        for (int i = 0; i < fwList.size(); i++) {
                            System.out.println("[ECOM-WAR] EdiSI == i is=======>" + i);
                            CustomerDetailsVO fw = (CustomerDetailsVO) fwList.get(i);
                            if (fw != null) {
                                strCountryName = "";
                                siform.setFwname(fw.getName());
                                siform.setFwCustCode(fw.getCustCode());
                                siform.setFwaddr(fw.getAddress());
                                siform.setFwaddr1(fw.getAddress1());
                                siform.setFwcity(fw.getCity());
                                siform.setFwphone(fw.getPhoneNo());
                                if (fw.getFax() != null && fw.getFax().length() > 0 && !fw.getFax().equalsIgnoreCase("--") && !fw.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setFwfax(fw.getFax());
                                } else {
                                    siform.setFwfax("");
                                }
                                siform.setFwcountry(fw.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ==contry id is====>" + fw.getCountry());
                                siform.setFwemail(fw.getEmail());
                                strCountryName = (String) countryHashObj.get(fw.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ==country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = fw.getCountry() + " - " + strCountryName;
                                    siform.setFwCountryName(strCountryName);
                                } else {
                                    siform.setFwCountryName("");
                                }
                                strCityName = fw.getCity();//+" - "+strCityName;
                                siform.setFwCityName(strCityName);
                                siform.setFwstate(concatStateName(fw.getState(), fw.getCountry()));
                                siform.setFweori(fw.getEroi());
                                siform.setFwpersontype(fw.getPerontype());
                                System.out.println("[ECOM-WAR] EdiSI  ==>" + siform.getFweori());
                                siform.setFwzipcode(Validation.checkNull(fw.getZipcode()));
                            }
                        }
                    }
                    //BY
                    if (byList != null) {
                        System.out.println("[ECOM-WAR] EdiSI =byList.size() is===>" + byList.size());
                        for (int i = 0; i < byList.size(); i++) {
                            System.out.println("[ECOM-WAR] EdiSI === i is=======>" + i);
                            CustomerDetailsVO by = (CustomerDetailsVO) byList.get(i);
                            if (by != null) {
                                strCountryName = "";
                                siform.setByname(by.getName());
                                System.out.println("[ECOM-WAR] EdiSI by.getCustCode()=>" + by.getCustCode());
                                siform.setByCustCode(by.getCustCode());
                                siform.setByaddr(by.getAddress());
                                siform.setByaddr1(by.getAddress1());
                                siform.setBycity(by.getCity());
                                siform.setByphone(Validation.checkNull(by.getPhoneNo()).replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                                if (by.getFax() != null && by.getFax().length() > 0 && !by.getFax().equalsIgnoreCase("--") && !by.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setByfax(Validation.checkNull(by.getFax()).replace("--", ""));
                                } else {
                                    siform.setByfax("");
                                }
                                siform.setBycountry(by.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ===contry id is====>" + by.getCountry());
                                siform.setByemail(by.getEmail());
                                strCountryName = (String) countryHashObj.get(by.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI =country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = by.getCountry() + " - " + strCountryName;
                                    siform.setByCountryName(strCountryName);
                                } else {
                                    siform.setByCountryName("");
                                }
                                strCityName = by.getCity();//+" - "+strCityName;
                                siform.setByCityName(strCityName);
                                siform.setBystate(concatStateName(by.getState(), by.getCountry()));
                                siform.setByeori(by.getEroi());
                                siform.setBypersontype(by.getPerontype());
                                siform.setByzipcode(Validation.checkNull(by.getZipcode()));
                                System.out.println("[ECOM-WAR] EdiSI ==>" + siform.getByeori());
                            }
                        }
                    }
                    //SL
                    if (slList != null) {
                        System.out.println("[ECOM-WAR] EdiSI ==slList.size() is===>" + slList.size());
                        for (int i = 0; i < slList.size(); i++) {
                            System.out.println("[ECOM-WAR] EdiSI == i is=======>" + i);
                            CustomerDetailsVO sl = (CustomerDetailsVO) slList.get(i);
                            if (sl != null) {
                                strCountryName = "";
                                siform.setSlname(sl.getName());
                                siform.setSlCustCode(sl.getCustCode());
                                siform.setSladdr(sl.getAddress());
                                siform.setSladdr1(sl.getAddress1());
                                siform.setSlcity(sl.getCity());
                                siform.setSlphone(Validation.checkNull(sl.getPhoneNo()).replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                                if (sl.getFax() != null && sl.getFax().length() > 0 && !sl.getFax().equalsIgnoreCase("--") && !sl.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setSlfax(Validation.checkNull(sl.getFax()).replace("--", ""));
                                } else {
                                    siform.setSlfax("");
                                }
                                siform.setSlcountry(sl.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ==contry id is====>" + sl.getCountry());
                                siform.setSlemail(sl.getEmail());
                                strCountryName = (String) countryHashObj.get(sl.getCountry());
                                System.out.println("[ECOM-WAR] EdiSI ==country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = sl.getCountry() + " - " + strCountryName;
                                    siform.setSlCountryName(strCountryName);
                                } else {
                                    siform.setSlCountryName("");
                                }
                                strCityName = sl.getCity();//+" - "+strCityName;
                                siform.setSlCityName(strCityName);
                                siform.setSlstate(concatStateName(sl.getState(), sl.getCountry()));
                                siform.setSleori(sl.getEroi());
                                siform.setSlpersontype(sl.getPerontype());
                                siform.setSlzipcode(Validation.checkNull(sl.getZipcode()));
                                System.out.println("[ECOM-WAR] EdiSI     ==>" + siform.getSleori());
                            }
                        }
                    }
                    //AP
                    if (apList != null) {
                        System.out.println("[ECOM-WAR] EdiSI ===apList.size() is===>" + apList.size());
                        for (int i = 0; i < apList.size(); i++) {
//                            System.out.println("[ECOM-WAR] EdiSI  = i is=======>" + i);
                            CustomerDetailsVO ap = (CustomerDetailsVO) apList.get(i);
                            if (ap != null) {
                                strCountryName = "";
                                siform.setApname(ap.getName());
                                siform.setApCustCode(ap.getCustCode());
                                siform.setApaddr(ap.getAddress());
                                siform.setApaddr1(ap.getAddress1());
                                siform.setApcity(ap.getCity());
                                siform.setApphone(ap.getPhoneNo());
                                if (ap.getFax() != null && ap.getFax().length() > 0 && !ap.getFax().equalsIgnoreCase("--") && !ap.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                    siform.setApfax(ap.getFax());
                                } else {
                                    siform.setApfax("");
                                }
                                siform.setApcountry(ap.getCountry());
//                                System.out.println("[ECOM-WAR] EdiSI ==contry id is====>" + ap.getCountry());
                                siform.setApemail(ap.getEmail());
                                strCountryName = (String) countryHashObj.get(ap.getCountry());
//                                System.out.println("[ECOM-WAR] EdiSI ==country name is====>" + strCountryName);
                                if (strCountryName != null) {
                                    strCountryName = ap.getCountry() + " - " + strCountryName;
                                    siform.setApCountryName(strCountryName);
                                } else {
                                    siform.setApCountryName("");
                                }
                                strCityName = ap.getCity();//+" - "+strCityName;
                                siform.setApCityName(strCityName);
                                siform.setApstate(concatStateName(ap.getState(), ap.getCountry()));
                                siform.setApeori(ap.getEroi());
                                siform.setAppersontype(ap.getPerontype());
                                siform.setApzipcode(Validation.checkNull(ap.getZipcode()));
                            }
                        }
                    }
                    //Buyer

                    if (customerMap != null && !customerMap.isEmpty()) {
                        slcustsList = (ArrayList) customerMap.get("SL");
                        bltypesList = (ArrayList) customerMap.get("BY");

                        if (bltypesList != null) {
//                            System.out.println("[ECOM-WAR] EdiSI =byList.size() is===>" + bltypesList.size());
                            for (int i = 0; i < bltypesList.size(); i++) {
                                CustomerDetailsVO by = (CustomerDetailsVO) bltypesList.get(i);
                                if (by != null) {
                                    strCountryName = "";
                                    by.setPhoneNo(Validation.checkNull(by.getPhoneNo()).replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                                    if (by.getFax() != null && by.getFax().length() > 0 && !by.getFax().equalsIgnoreCase("--") && !by.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                        by.setFax(Validation.checkNull(by.getFax()).replace("--", ""));
                                    } else {
                                        by.setFax("");
                                    }
//                                    System.out.println("[ECOM-WAR] EdiSI ===contry id is====>" + by.getCountry());
                                    strCountryName = (String) countryHashObj.get(by.getCountry());
//                                    System.out.println("[ECOM-WAR] EdiSI =country name is====>" + strCountryName);
                                    if (strCountryName != null) {
                                        strCountryName = by.getCountry() + " - " + strCountryName;
                                        by.setCountry(strCountryName);
                                    } else {
                                        by.setCountry("");
                                    }
                                    by.setState(concatStateName(by.getState(), by.getCountry()));
                                }
                            }
                        }
                        //Seller

                        if (slcustsList != null) {
                            System.out.println("[ECOM-WAR] EdiSI =slcustsList.size() is===>" + slcustsList.size());
                            for (int i = 0; i < slcustsList.size(); i++) {
                                System.out.println("[ECOM-WAR] EdiSI === i is=======>" + i);
                                CustomerDetailsVO by = (CustomerDetailsVO) slcustsList.get(i);
                                if (by != null) {
                                    strCountryName = "";
                                    by.setPhoneNo(Validation.checkNull(by.getPhoneNo()).replace("--", "").replaceAll("[^0-9+\\-]", "").replaceAll("--+", "").replaceAll("^-+", "").replaceAll("-+$", ""));
                                    if (by.getFax() != null && by.getFax().length() > 0 && !by.getFax().equalsIgnoreCase("--") && !by.getFax().equalsIgnoreCase("-") && !hidefaxno.equalsIgnoreCase("y")) {
                                        by.setFax(Validation.checkNull(by.getFax()).replace("--", ""));
                                    } else {
                                        by.setFax("");
                                    }
                                    System.out.println("[ECOM-WAR] EdiSI ===contry id is====>" + by.getCountry());
                                    strCountryName = (String) countryHashObj.get(by.getCountry());
                                    System.out.println("[ECOM-WAR] EdiSI =country name is====>" + strCountryName);
                                    if (strCountryName != null) {
                                        strCountryName = by.getCountry() + " - " + strCountryName;
                                        by.setCountry(strCountryName);
                                    } else {
                                        by.setCountry("");
                                    }
                                    by.setState(concatStateName(by.getState(), by.getCountry()));
                                }
                            }
                        }
                    }
                    System.out.println("[DEBUG] Buyer grid size: " + (bltypesList != null ? bltypesList.size() : "null"));
                    System.out.println("[DEBUG] Seller grid size: " + (slcustsList != null ? slcustsList.size() : "null"));
                    session.setAttribute("ebooking_seller_grid", slcustsList);
                    session.setAttribute("ebooking_buyere_grid", bltypesList);

                }

                if ("View".equalsIgnoreCase(type)) {
                    SUCCESS = "siview";
                }
                System.out.println("[ECOM-WAR] EdiSI ==Result Page is======>" + SUCCESS);
            } else {
                SUCCESS = "sessionex";
            }
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
        siform.setSptaxId("");
        siform.setSpCityName("");
        siform.setSpzipcode("");
        siform.setConsname("");
        siform.setConsCustCode("");
        siform.setConsaddr("");
        siform.setConsaddr1("");
        siform.setConscity("");
        siform.setConsphone("");
        siform.setConsfax("");
        siform.setConsTaxId("");
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
        siform.setNpTaxId("");
        siform.setNotifyCheck("");
        siform.setNotifyordercheck("N");
        siform.setNpCountryName("");
        siform.setNpCityName("");
        siform.setNpzipcode("");
        siform.setNpTaxId("");
        siform.setNp3name("");
        siform.setNp3CustCode("");
        siform.setNp3addr("");
        siform.setNp3addr1("");
        siform.setNp3city("");
        siform.setNp3phone("");
        siform.setNp3fax("");
        siform.setNp3email("");
        siform.setNp3state("");
        siform.setNp3country("");
        siform.setNotify3Check("");
        siform.setNotify3ordercheck("N");
        siform.setNp3CountryName("");
        siform.setNp3CityName("");
        siform.setNp3zipcode("");
        siform.setNp2name("");
        siform.setNp2CustCode("");
        siform.setNp2addr("");
        siform.setNp2addr1("");
        siform.setNp2city("");
        siform.setNp2phone("");
        siform.setNp2fax("");
        siform.setNp2email("");
        siform.setNp2state("");
        siform.setNp2country("");
        siform.setNotify2Check("");
        siform.setNotify2ordercheck("N");
        siform.setNp2CountryName("");
        siform.setNp2CityName("");
        siform.setNp2zipcode("");

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
        siform.setFwzipcode("");


    }

    private boolean isTOOrder(String consigneeName) {
        boolean result = false;
        try {
            String[] splited = consigneeName.split(" "); //split on word boundries
            List<String> wordList = Arrays.asList(splited);
            wordList.forEach(str -> {
                str.trim().toUpperCase();
            });
            System.out.println("[ECOM-WAR] EdiSI splited>>" + wordList);
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
            stateMapCountry = (Map) stateMap.get(Validation.checkNull(country).toUpperCase());
            if (state != null && state.length() > 0 && stateMapCountry != null) {
                state = state + " - " + Validation.checkNull((String) stateMapCountry.get(state));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Validation.checkNull(state);
    }
}
