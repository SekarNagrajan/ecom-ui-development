/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static net.ecom.common.Validation.checkNull;
import net.ecom.formbeans.SIEditForm;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;
import net.ecom.common.utils.MaskPortUtil;

/**
 *
 * @author priyadharshinim
 */
public class CommonMailForAgency {

    HashMap unitlis = new HashMap();

    {
        unitlis.put("K", "Kilos");
        unitlis.put("P", "Pounds");
    }

    public String newAgencyBookingMail(EbookingValidatorFormBeanNew ve, HashMap masterCache, ArrayList all, ReturnVO returnVO) {

        Map portlist = (Map) masterCache.get("PORTLIST");
        Map comlist = (Map) masterCache.get("comlist");
        String apname = (String) masterCache.get("apname");
        Map vessellist = (Map) masterCache.get("VESSELLIST");

        ArrayList cargolist = null;
        String message1 = "";
        String commo = null, totcomm = null, cargoDes = null, finalCargoDes = null;

        for (int i = 0; i < all.size(); i++) {
            if (i == 0) {
                cargolist = (ArrayList) all.get(i);
            }
        }
        net.ecom.vo.EbookingValidatorFormBeanNew eBookingcargoData = null;
        Set<String> commoditySet = new HashSet<String>();
        Map<String, Integer> containerMap = new HashMap<String, Integer>();
        int j = 0;
        String eqpType = null;
        int eqpCount = 0;
        for (int i = 0; i < cargolist.size(); i++) {
            eBookingcargoData = (net.ecom.vo.EbookingValidatorFormBeanNew) cargolist.get(i);
            System.out.println("[ECOM-WAR] CommonMailForAgency cntno--->" + eBookingcargoData.getEbook_eqp());
            commo = Validation.checkNull(eBookingcargoData.getEbook_eqp()) + "X" + Validation.checkNull(eBookingcargoData.getEbook_eqp_qty());
            cargoDes = checkNull((String) comlist.get(checkNull(eBookingcargoData.getEbook_commoditycode())));
            System.out.println("[ECOM-WAR] CommonMailForAgency cargodec--->" + cargoDes);
            commoditySet.add(cargoDes);
            //
            eqpType = Validation.checkNull(eBookingcargoData.getEbook_eqp());
            if (containerMap.containsKey(eqpType)) {
                eqpCount = containerMap.get(eqpType);
                eqpCount = eqpCount + Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                containerMap.put(eqpType, eqpCount);
            } else {
                eqpCount = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                containerMap.put(eqpType, eqpCount);
            }
        }
        j = 0;
        Iterator<String> it = commoditySet.iterator();
        finalCargoDes = "";
        while (it.hasNext()) {
            if (j > 0) {
                finalCargoDes = finalCargoDes + " ~~ ";
            }
            finalCargoDes = finalCargoDes + it.next();
            j++;
        }
        //
        j = 0;
        totcomm = "";
        for (Map.Entry<String, Integer> entry : containerMap.entrySet()) {
            if (j > 0) {
                totcomm = totcomm + ", ";
            }
            totcomm = totcomm + entry.getKey() + " X " + entry.getValue();
            j++;
        }

        String agencyrefno = "", agencyrefnumber = "";
        agencyrefno = Validation.checkNull(ve.getEbook_agency_reference());
        if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
            agencyrefnumber = "<br><br>Agency Reference Number:" + agencyrefno;
        }

        System.out.println("[ECOM-WAR] CommonMailForAgency totcommo" + totcomm + "CommonMailForAgency finaltotalcargodesc" + finalCargoDes);
        message1 = returnVO.getAdmMailMsg();
        String pol1 = String.valueOf(portlist.get(ve.getEbook_origin().toUpperCase()));
        pol1 = Validation.checkNull(pol1);
        String pod1 = String.valueOf(portlist.get(ve.getEbook_delivery().toUpperCase()));
        pod1 = Validation.checkNull(pod1);
        
        // Check if mask port swap is needed
        boolean isMaskPort = MaskPortUtil.isActualPort(ve.getEbook_origin()) || MaskPortUtil.isActualPort(ve.getEbook_delivery());
        if (isMaskPort) {
            message1 = message1.replace("@vesselandvoyage", "");
            // Swap actual port names to mask port names
            pol1 = MaskPortUtil.swapActualPortToMaskPortWithName(ve.getEbook_origin() + " - " + pol1);
            pod1 = MaskPortUtil.swapActualPortToMaskPortWithName(ve.getEbook_delivery() + " - " + pod1);
            System.out.println("[ECOM-WAR] CommonMailForAgency Mask port detected - hiding vessel/voyage, swapped origin: " + pol1 + ", delivery: " + pod1);
        } else {
            message1 = message1.replace("@vesselandvoyage", vessellist.get(ve.getEbook_vesselcode()) + "/" + ve.getEbook_voyage() + "/" + ve.getEbook_bound_leg());
        }
        
        message1 = message1.replace("@apname", apname);
        message1 = message1.replace("@pol", pol1);
        message1 = message1.replace("@pod", pod1 + agencyrefnumber);
        message1 = message1.replace("@cargodesc", finalCargoDes);
        message1 = message1.replace("@cntinfo", totcomm);
        message1 = message1.replace("@customer_remarks", Validation.checkNull(ve.getEbook_status_remarks()));
        message1 = message1.replace("@remarks", Validation.checkNull(ve.getEbook_status_remarks()));
        message1 = message1.replace("@salesagreed", Validation.checkNull(ve.getSalesRateAgree()));
        System.out.println("[ECOM-WAR] CommonMailForAgency agency<<returnVO.getmsg" + message1);

        return message1;

    }

    public String newAgencyamendmentBookingMail(AmendmentBookingFormBean eBookingMasterData, HashMap masterCache, List cargolist, ReturnVO returnVO) {
        Map portlist = (Map) masterCache.get("PORTLIST");
        Map comlist = (Map) masterCache.get("comlist");
        String apname = (String) masterCache.get("apname");
        Map vessellist = (Map) masterCache.get("VESSELLIST");
        String ENABLEAMEND = (String) masterCache.get("ENABLEAMENDMENTCOMMAIL");

        String message = "";
        String commo = null, totcomm = null, cargoDes = null, finalCargoDes = null;
        int j = 0;
        AmendmentBookingFormBean eBookingcargoData = null;
        Set<String> commoditySet = new HashSet<String>();
        Map<String, Integer> containerMap = new HashMap<String, Integer>();

        String eqpType = null;
        int eqpCount = 0;
        for (int i = 0; i < cargolist.size(); i++) {
            eBookingcargoData = (AmendmentBookingFormBean) cargolist.get(i);
            System.out.println("[ECOM-WAR] CommonMailForAgency cargowt--->" + eBookingcargoData.getEbook_cargoweight()+" cntno--->" + eBookingcargoData.getEbook_eqp());
            commo = Validation.checkNull(eBookingcargoData.getEbook_eqp()) + "X" + Validation.checkNull(eBookingcargoData.getEbook_eqp_qty());
            cargoDes = checkNull((String) comlist.get(checkNull(eBookingcargoData.getEbook_commoditycode())));
            commoditySet.add(cargoDes);
            //
            eqpType = Validation.checkNull(eBookingcargoData.getEbook_eqp());
            if (containerMap.containsKey(eqpType)) {
                eqpCount = containerMap.get(eqpType);
                eqpCount = eqpCount + Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                containerMap.put(eqpType, eqpCount);
            } else {
                eqpCount = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                containerMap.put(eqpType, eqpCount);
            }
            System.out.println("[ECOM-WAR] CommonMailForAgency cargodec--->" + cargoDes);

        }
        j = 0;
        Iterator<String> it = commoditySet.iterator();
        finalCargoDes = "";
        while (it.hasNext()) {
            if (j > 0) {
                finalCargoDes = finalCargoDes + " ~~ ";
            }
            finalCargoDes = finalCargoDes + it.next();
            j++;
        }
        //
        j = 0;
        totcomm = "";
        for (Map.Entry<String, Integer> entry : containerMap.entrySet()) {

            if (j > 0) {
                totcomm = totcomm + ", ";
            }
            totcomm = totcomm + entry.getKey() + " X " + entry.getValue();
            j++;
        }

        String agencyrefno = "", agencyrefnumber = "";
        agencyrefno = Validation.checkNull(eBookingMasterData.getEbook_agency_reference());
        if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
            agencyrefnumber = "<br><br>Agency Reference Number:" + agencyrefno;
        }
        System.out.println("[ECOM-WAR] CommonMailForAgency totcommo" + totcomm+" finaltotalcargodesc" + finalCargoDes);
        message = returnVO.getAdmMailMsg();
        
        String origin = Validation.checkNull(String.valueOf(portlist.get(eBookingMasterData.getEbook_origin().toUpperCase())));
        String delivery = Validation.checkNull(String.valueOf(portlist.get(eBookingMasterData.getEbook_delivery().toUpperCase())));
        
        // Check if mask port swap is needed
        boolean isMaskPort = MaskPortUtil.isActualPort(eBookingMasterData.getEbook_origin()) || MaskPortUtil.isActualPort(eBookingMasterData.getEbook_delivery());
        if (isMaskPort) {
            message = message.replace("@vesselandvoyage", "");
            // Swap actual port names to mask port names
            origin = MaskPortUtil.swapActualPortToMaskPortWithName(eBookingMasterData.getEbook_origin() + " - " + origin);
            delivery = MaskPortUtil.swapActualPortToMaskPortWithName(eBookingMasterData.getEbook_delivery() + " - " + delivery);
            System.out.println("[ECOM-WAR] CommonMailForAgency Amendment Mask port detected - hiding vessel/voyage, swapped origin: " + origin + ", delivery: " + delivery);
        } else {
            message = message.replace("@vesselandvoyage", vessellist.get(eBookingMasterData.getEbook_vesselcode()) + "/" + eBookingMasterData.getEbook_voyage() + "/" + eBookingMasterData.getEbook_bound_leg());
        }
        
        message = message.replace("@apname", apname);
        if ("Y".equalsIgnoreCase(Validation.checkNull(ENABLEAMEND))) {
            message = message.replace("@customer_remarks", eBookingMasterData.getEbook_status_remarks() + "<p><b>Amendment Details</b></p><br>@amendmnetDetails<br>");
        } else {
            message = message.replace("@customer_remarks", eBookingMasterData.getEbook_status_remarks());
        }
        message = message.replace("@remarks", eBookingMasterData.getEbook_status_remarks());
        message = message.replace("@salesagreed", Validation.checkNull(eBookingMasterData.getSalesRateAgree()));
        message = message.replace("@pol", origin);
        message = message.replace("@pod", delivery + agencyrefnumber);
        message = message.replace("@cargodesc", finalCargoDes);
        message = message.replace("@cntinfo", totcomm);
        System.out.println("[ECOM-WAR] CommonMailForAgency amend agency<<returnVO.getmsg" + message);

        return message;
    }

    public String newAgencySIMail(HashMap masterCache, SIEditForm siOBJ, SIPartiesVO parties_routing, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, ReturnVO returnVO) {
        String message1 = "";
        Map portlist = (Map) masterCache.get("PORTLIST");
        String apname = (String) masterCache.get("apname");
        Map vessellist = (Map) masterCache.get("VESSELLIST");
        String ENABLEAMEND = (String) masterCache.get("ENABLEAMENDMENTCOMMAIL");
        Set<String> commoditySet = new HashSet<String>();
        Map<String, Integer> containerMap = new HashMap<String, Integer>();

        String eqpType = null;
        int eqpCount = 0;
        String commo = null, totcomm = null, cargoDes = null, finalCargoDes = null;
        int jj = 0;
        if (cntMapObj != null && cntMapObj.size() > 0) {
            Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
            int sno = 1;

            for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                if (cntList != null && cntList.size() > 0) {
                    for (int j = 0; j < cntList.size(); j++) {
                        SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);

                        System.out.println("[ECOM-WAR] CommonMailForAgency cargowt--->" + checkNull(siCntDetVo.getStrWgt())+" cntno--->" + checkNull(siMasterVO.getStrType()));
                        // commo = Validation.checkNull(eBookingcargoData.getEbook_eqp()) + "X" + Validation.checkNull(eBookingcargoData.getEbook_eqp_qty());
                        commo = checkNull(siMasterVO.getStrType());
                        cargoDes = siCntDetVo.getHsCodeDesc();
                        System.out.println("[ECOM-WAR] CommonMailForAgency cargodec--->" + cargoDes);
                        commoditySet.add(cargoDes);
                        //
                        eqpType = Validation.checkNull(checkNull(siMasterVO.getStrType()));
                        if (containerMap.containsKey(eqpType)) {
                            eqpCount = containerMap.get(eqpType);
                            eqpCount = eqpCount + 1;
                            containerMap.put(eqpType, eqpCount);
                        } else {
                            containerMap.put(eqpType, 1);
                        }

                    }
                }
            }
        }
        jj = 0;
        Iterator<String> it = commoditySet.iterator();
        finalCargoDes = "";
        while (it.hasNext()) {
            if (jj > 0) {
                finalCargoDes = finalCargoDes + " ~~ ";
            }
            finalCargoDes = finalCargoDes + it.next();
            jj++;
        }
        //
        jj = 0;
        totcomm = "";
        for (Map.Entry<String, Integer> entry : containerMap.entrySet()) {
            if (jj > 0) {
                totcomm = totcomm + ", ";
            }
            totcomm = totcomm + entry.getKey() + " X " + entry.getValue();
            jj++;
        }
        System.out.println("[ECOM-WAR] CommonMailForAgency totcommo " + totcomm+" finaltotalcargodesc " + finalCargoDes+" pod value " + (String) portlist.get(Validation.checkNull(parties_routing.getPod())));

        message1 = returnVO.getAdmMailMsg();
        
        String pol = (String) portlist.get(Validation.checkNull(parties_routing.getPol()));
        String pod = (String) portlist.get(Validation.checkNull(parties_routing.getPod()));
        
        // Check if mask port swap is needed
        boolean isMaskPort = MaskPortUtil.isActualPort(parties_routing.getPol()) || MaskPortUtil.isActualPort(parties_routing.getPod());
        if (isMaskPort) {
            message1 = message1.replace("@vesselandvoyage", "");
            // Swap actual port names to mask port names
            pol = MaskPortUtil.swapActualPortToMaskPortWithName(parties_routing.getPol() + " - " + pol);
            pod = MaskPortUtil.swapActualPortToMaskPortWithName(parties_routing.getPod() + " - " + pod);
            System.out.println("[ECOM-WAR] CommonMailForAgency SI Mask port detected - hiding vessel/voyage, swapped pol: " + pol + ", pod: " + pod);
        } else {
            message1 = message1.replace("@vesselandvoyage", parties_routing.getVesselName() + "/" + parties_routing.getVoyagecode() + "/" + parties_routing.getBound());
        }
        
        message1 = message1.replace("@apname", apname);
        message1 = message1.replace("@customer_remarks", Validation.checkNull(siOBJ.getSicustremarks()));
        //message1 = message1.replace("@remarks", Validation.checkNull(siOBJ.getSicustremarks()));
        message1 = message1.replace("@pol", pol);
        message1 = message1.replace("@pod", pod);
        if ("Y".equalsIgnoreCase(Validation.checkNull(ENABLEAMEND))) {
            message1 = message1.replace("@cargodesc", finalCargoDes + "<p><b>Amendment Details</b></p><br>@amendmnetDetails<br>");
        } else {
            message1 = message1.replace("@cargodesc", finalCargoDes);
        }
        message1 = message1.replace("@cntinfo", totcomm);

        System.out.println("[ECOM-WAR] CommonMailForAgency Agency<<sireturnVO.getmsg " + message1);

        return message1;
    }

}
