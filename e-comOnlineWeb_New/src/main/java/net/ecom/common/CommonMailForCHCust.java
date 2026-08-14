/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import static java.lang.Float.parseFloat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static net.ecom.common.Validation.checkNull;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.formbeans.SIEditForm;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;

/**
 *
 * @author priyadharshinim
 */
public class CommonMailForCHCust {

    HashMap unitlis = new HashMap();

    {
        unitlis.put("K", "Kilos");
        unitlis.put("P", "Pounds");
    }
    DecimalFormat df = new DecimalFormat("0.000");

    public String newCHBookingMail(EbookingValidatorFormBeanNew ve, HashMap masterCache, ArrayList all, ReturnVO returnVO) {

        String custCompany = (String) masterCache.get("CustCompany");
        Map portlist = (Map) masterCache.get("PORTLIST");
        Map comlist = (Map) masterCache.get("comlist");
        Map vessellist = (Map) masterCache.get("VESSELLIST");
        ArrayList cargolist = null;
        String message = "";

        String cargoWeight = null, cargoWtUnit = null, commo = null, totcomm = null, cargoDes = null, finalCargoDes = null, strTTLCargoWT = null;
        Double totalCargoWt = 0.000;
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
            System.out.println("[ECOM-WAR] CommonMailForCHCust cargowt--->" + eBookingcargoData.getEbook_cargoweight() + " cntno--->" + eBookingcargoData.getEbook_eqp());
            //commo = Validation.checkNull(eBookingcargoData.getEbook_eqp()) + "X" + Validation.checkNull(eBookingcargoData.getEbook_eqp_qty());
            cargoWeight = checkNull(eBookingcargoData.getEbook_cargoweight());

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

            System.out.println("[ECOM-WAR] CommonMailForCHCust cargodec--->" + cargoDes);
            cargoWtUnit = checkNull(eBookingcargoData.getEbook_weightunit());
            System.out.println("[ECOM-WAR] CommonMailForCHCust cargowtunit--->" + cargoWtUnit);

            if (cargoWtUnit != "" && cargoWtUnit.length() > 0 && cargoWtUnit.equals("K")) {
                cargoWeight = cargoWeight;
                //cargoWeight= String.valueOf(Double.valueOf(df.format(cargoWeight)));

                System.out.println("[ECOM-WAR] CommonMailForCHCust kilos cw" + cargoWeight);
            } else {

                cargoWeight = Double.toString(convertWeightUnit(cargoWeight, eBookingcargoData.getEbook_weightunit(), "K"));

                System.out.println("[ECOM-WAR] CommonMailForCHCust aftrcnv cw" + cargoWeight);
            }
            if (j == 0) {
                // totcomm = commo;
                totalCargoWt = Double.parseDouble(cargoWeight) * Double.parseDouble(eBookingcargoData.getEbook_eqp_qty());
                System.out.println("[ECOM-WAR] CommonMailForCHCust totalCargoWtvalue" + totalCargoWt);

                strTTLCargoWT = String.format("%1.2f", totalCargoWt) + " " + "Kilos";

            } else {
                // totcomm = totcomm + "," + commo;
                //finalCargoDes = finalCargoDes + "~~" + cargoDes;
                totalCargoWt = totalCargoWt + (Double.parseDouble(cargoWeight) * Double.parseDouble(eBookingcargoData.getEbook_eqp_qty()));
                System.out.println("[ECOM-WAR] CommonMailForCHCust totalCargoWtvalue" + totalCargoWt);
                strTTLCargoWT = String.format("%1.2f", totalCargoWt) + " " + "Kilos";

            }

            j++;
        }
        //
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

        //
        System.out.println("[ECOM-WAR] CommonMailForCHCust totcommo " + checkNull(totcomm) + " inttotalcargowt " + totalCargoWt + " Stringtotalcargowt " + strTTLCargoWT + " finaltotalcargodesc " + finalCargoDes);

        String agencyrefno = "", agencyrefnumber = "";
        agencyrefno = Validation.checkNull(ve.getEbook_agency_reference());
        if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
            agencyrefnumber = "<br><br> 代理商订舱号/Agency Reference Number:" + agencyrefno;
        }

        message = returnVO.getStrMailMsg();
        String origin = String.valueOf(portlist.get(ve.getEbook_origin().toUpperCase()));
        origin = Validation.checkNull(origin);
        String delivery = String.valueOf(portlist.get(ve.getEbook_delivery().toUpperCase()));
        delivery = Validation.checkNull(delivery);
        message = message.replace("@vesselandvoyage", vessellist.get(ve.getEbook_vesselcode()) + "/" + ve.getEbook_voyage() + "/" + ve.getEbook_bound_leg());
        message = message.replace("@origin", origin);
        message = message.replace("@delivery", delivery + agencyrefnumber);
        message = message.replace("@cargodesc", finalCargoDes);
        message = message.replace("@cargowt", strTTLCargoWT);
        message = message.replace("@cntinfo", totcomm);
        message = message.replace("@company", custCompany);
        message = message.replace("@remark", Validation.checkNull(ve.getEbook_status_remarks()));
        System.out.println("[ECOM-WAR] CommonMailForCHCust <<returnVO.getmsg" + message);
        return message;

    }

    public String newCHamendmentBookingMail(AmendmentBookingFormBean eBookingMasterData, HashMap masterCache, List cargolist, ReturnVO returnVO) {
        String message = "";
        String custCompany = (String) masterCache.get("CustCompany");
        Map portlist = (Map) masterCache.get("PORTLIST");
        Map comlist = (Map) masterCache.get("comlist");
        Map vessellist = (Map) masterCache.get("VESSELLIST");

        String cargoWeight = null, cargoWtUnit = null, commo = null, totcomm = null, cargoDes = null, finalCargoDes = null, strTTLCargoWT = null;
        Double totalCargoWt = 0.000;
        int j = 0;
        AmendmentBookingFormBean eBookingcargoData = null;
        Set<String> commoditySet = new HashSet<String>();
        Map<String, Integer> containerMap = new HashMap<String, Integer>();

        String eqpType = null;
        int eqpCount = 0;
        for (int i = 0; i < cargolist.size(); i++) {
            eBookingcargoData = (AmendmentBookingFormBean) cargolist.get(i);
            System.out.println("[ECOM-WAR] CommonMailForCHCust cargowt--->" + eBookingcargoData.getEbook_cargoweight()+" cntno--->" + eBookingcargoData.getEbook_eqp());
            commo = Validation.checkNull(eBookingcargoData.getEbook_eqp()) + "X" + Validation.checkNull(eBookingcargoData.getEbook_eqp_qty());
            cargoWeight = checkNull(eBookingcargoData.getEbook_cargoweight());
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
            System.out.println("[ECOM-WAR] CommonMailForCHCust cargodec--->" + cargoDes);
            cargoWtUnit = checkNull(eBookingcargoData.getEbook_weightunit());
            System.out.println("[ECOM-WAR] CommonMailForCHCust cargowtunit--->" + cargoWtUnit);

            if (cargoWtUnit != "" && cargoWtUnit.length() > 0 && cargoWtUnit.equals("K")) {
                cargoWeight = cargoWeight;
                //cargoWeight= String.valueOf(Double.valueOf(df.format(cargoWeight)));

                System.out.println("[ECOM-WAR] CommonMailForCHCust kilos cw" + cargoWeight);
            } else {
                System.out.println("[ECOM-WAR] CommonMailForCHCust beforeconv cw" + cargoWeight + " enetered in esle cargotw pnd conv");
                cargoWeight = Double.toString(convertWeightUnit(cargoWeight, eBookingcargoData.getEbook_weightunit(), "K"));
                System.out.println("[ECOM-WAR] CommonMailForCHCust aftrcnv cw" + cargoWeight);
            }

            if (j == 0) {
                // totcomm = commo;
                totalCargoWt = Double.parseDouble(cargoWeight);

                //strTTLCargoWT = Double.toString(totalCargoWt) + " " + "Kilos";
                strTTLCargoWT = String.format("%1.2f", totalCargoWt) + " " + "Kilos";
                System.out.println("[ECOM-WAR] CommonMailForCHCust strrrrrrrcwt1" + strTTLCargoWT);
                // finalCargoDes = cargoDes;
            } else {
                // totcomm = totcomm + "," + commo;
                // finalCargoDes = finalCargoDes + "~~" + cargoDes;
                totalCargoWt = totalCargoWt + Double.parseDouble(cargoWeight);

                System.out.println("[ECOM-WAR] CommonMailForCHCust dbllllcwt" + totalCargoWt);
                //strTTLCargoWT = Double.toString(totalCargoWt) + " " + "Kilos";
                strTTLCargoWT = String.format("%1.2f", totalCargoWt) + " " + "Kilos";

                System.out.println("[ECOM-WAR] CommonMailForCHCust strrrrrrrcwt" + strTTLCargoWT);
            }

            j++;
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

  System.out.println("[ECOM-WAR] CommonMailForCHCust totcommo " + totcomm +    " inttotalcargowt " + totalCargoWt +   " Stringtotalcargowt " + strTTLCargoWT + 
                   " finaltotalcargodesc " + finalCargoDes);


        String agencyrefno = "", agencyrefnumber = "";
        agencyrefno = Validation.checkNull(eBookingMasterData.getEbook_agency_reference());
        if (agencyrefno.length() > 0 && "CN".equalsIgnoreCase(returnVO.getAgencycountry())) {
            agencyrefnumber = "<br><br> 代理商订舱号/Agency Reference Number:" + agencyrefno;
        }

        message = returnVO.getStrMailMsg();
        String origin = Validation.checkNull(String.valueOf(portlist.get(eBookingMasterData.getEbook_origin().toUpperCase())));
        origin = Validation.checkNull(origin);
        String delivery = Validation.checkNull(String.valueOf(portlist.get(eBookingMasterData.getEbook_delivery().toUpperCase())));
        delivery = Validation.checkNull(delivery);
        message = message.replace("@vesselandvoyage", vessellist.get(eBookingMasterData.getEbook_vesselcode()) + "/" + eBookingMasterData.getEbook_voyage() + "/" + eBookingMasterData.getEbook_bound_leg());
        message = message.replace("@origin", origin);
        message = message.replace("@delivery", delivery + agencyrefnumber);
        message = message.replace("@cargodesc", finalCargoDes);
        message = message.replace("@cargowt", strTTLCargoWT);
        message = message.replace("@cntinfo", totcomm);
        message = message.replace("@company", custCompany);
        message = message.replace("@remark", Validation.checkNull(eBookingMasterData.getEbook_status_remarks()));
        System.out.println("[ECOM-WAR] CommonMailForCHCust <<returnVO.getmsg" + message);

        return message;
    }

    public String newCHSIMail(HashMap masterCache, SIEditForm siOBJ, SIPartiesVO parties_routing, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, ReturnVO returnVO) {

        String message = "";
        String custCompany = (String) masterCache.get("CustCompany");
        Map portlist = (Map) masterCache.get("PORTLIST");
        Map comlist = (Map) masterCache.get("comlist");
        Map vessellist = (Map) masterCache.get("VESSELLIST");

        String cargoWeight = null, cargoWtUnit = null, commo = null, totcomm = null, cargoDes = null, finalCargoDes = null, strTTLCargoWT = null;
        Double totalCargoWt = 0.000;
        //
        Set<String> commoditySet = new HashSet<String>();
        Map<String, Integer> containerMap = new HashMap<String, Integer>();

        String eqpType = null;
        int eqpCount = 0;
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

                        System.out.println("[ECOM-WAR] CommonMailForCHCust cargowt--->" + checkNull(siCntDetVo.getStrWgt()) + " cntno--->" + checkNull(siMasterVO.getStrType()));
                        // commo = Validation.checkNull(eBookingcargoData.getEbook_eqp()) + "X" + Validation.checkNull(eBookingcargoData.getEbook_eqp_qty());
                        commo = checkNull(siMasterVO.getStrType());
                        cargoWeight = checkNull(siCntDetVo.getStrWgt());
                        cargoDes = siCntDetVo.getHsCodeDesc();
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

                        cargoWtUnit = checkNull(siCntDetVo.getStrUnit());
                        System.out.println("[ECOM-WAR] CommonMailForCHCust cargowtunit--->" + cargoWtUnit);

                        if (cargoWtUnit != "" && cargoWtUnit.length() > 0 && cargoWtUnit.equals("K")) {
                            cargoWeight = cargoWeight;

                            System.out.println("[ECOM-WAR] CommonMailForCHCust kilos cw" + cargoWeight);
                        } else {

                            cargoWeight = Double.toString(convertWeightUnit(cargoWeight, checkNull(siCntDetVo.getStrUnit()), "K"));
                            System.out.println("[ECOM-WAR] CommonMailForCHCust aftrcnv cw" + cargoWeight);
                        }

                        if (jj == 0) {
                            //  totcomm = commo;
                            totalCargoWt = Double.parseDouble(cargoWeight);
                            strTTLCargoWT = String.format("%1.2f", totalCargoWt) + " " + "Kilos";
                            // finalCargoDes = cargoDes;
                        } else {
                            // totcomm = totcomm + "," + commo;
                            // finalCargoDes = finalCargoDes + "~~" + cargoDes;
                            totalCargoWt = totalCargoWt + Double.parseDouble(cargoWeight);
                            strTTLCargoWT = String.format("%1.2f", totalCargoWt) + " " + "Kilos";
                        }

                        jj++;
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

System.out.println("[ECOM-WAR] CommonMailForCHCust totcommo " + totcomm +   " inttotalcargowt " + totalCargoWt +    " Stringtotalcargowt " + strTTLCargoWT +   " finaltotalcargodesc " + finalCargoDes);


        message = returnVO.getStrMailMsg();
        String pol = (String) portlist.get(Validation.checkNull(parties_routing.getPol()));

        String pod = (String) portlist.get(Validation.checkNull(parties_routing.getPod()));
        message = message.replace("@vesselandvoyage", parties_routing.getVesselName() + "/" + parties_routing.getVoyagecode() + "/" + parties_routing.getBound());
        message = message.replace("@pol", pol);
        message = message.replace("@pod", pod);
        message = message.replace("@cargodesc", finalCargoDes);
        message = message.replace("@cargowt", strTTLCargoWT);
        message = message.replace("@cntinfo", totcomm);
        message = message.replace("@company", custCompany);
        message = message.replace("@remark", Validation.checkNull(siOBJ.getSicustremarks()));
        System.out.println("[ECOM-WAR] CommonMailForCHCust <<sireturnVO.getmsg" + message);

        return message;
    }

    public Double convertWeightUnit(String value1, String fromUnit, String toUnit) {
        DecimalFormat df = new DecimalFormat("0.000");
        Double value = 0.000;
        if (value1 != "" && value1.length() > 0) {
            if ("K".equals(fromUnit)) {
                if ("T".equals(toUnit)) {

                    value = parseFloat(value1) / 1000.0;
                } else if ("P".equals(toUnit)) {
                    value = parseFloat(value1) * 2.20462;
                }

            } else if ("T".equals(fromUnit)) {

                if ("K".equals(toUnit)) {
                    value = parseFloat(value1) * 1000.0;
                } else if ("P".equals(toUnit)) {
                    value = parseFloat(value1) * 2204.6226200000001;
                }

            } else if ("P".equals(fromUnit)) {

                if ("K".equals(toUnit)) {
                    value = parseFloat(value1) / 2.20462;
                } else if ("T".equals(toUnit)) {
                    value = parseFloat(value1) / 2204.6226200000001;
                }
            }
            value = Double.valueOf(df.format(value));
        }

        return value;
    }
}
