package net.ecom.surcharge;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import net.ecom.vo.ContractVo;

/**
 * Same code as the standalone GWFCMSAuroSurcharge
 * {@code net.ecom.AppTierCntrlBean.RateContractBean}.
 *
 * <p>The standalone {@code getRateContract} entry point is intentionally not
 * copied: it calls usp_ecomContract over plain JDBC, whereas here the contract
 * result sets already come from VDS. Call
 * {@link #populateRateContractFromVdsMap} with the VDS {@code rs} map instead.</p>
 */
public class RateContractBean {

    public void populateRateContractFromVdsMap(Map result, Map map, String shipmentDate, String custCode,
            String pol, String pod, String eqpType, String commodity, boolean includeContractStatus) {
        List lis = null, cmlis = null;
        Map resultMap = null;
        Map codesByClass = new HashMap();
        lis = (List) map.get("rs0");
        if (lis != null) {
            HashMap cmdmap = new HashMap();
            String clas = null;
            for (int i = 0; i < lis.size(); i++) {
                resultMap = (Map) lis.get(i);
                clas = (String) resultMap.get("class");
                String commodityCode = checkNull((String) resultMap.get("commodity"));
                if (commodityCode.length() > 0) {
                    Set classCodes = (Set) codesByClass.get(clas);
                    if (classCodes == null) {
                        classCodes = new LinkedHashSet();
                        codesByClass.put(clas, classCodes);
                    }
                    classCodes.add(commodityCode);
                }
                if (cmdmap.containsKey(clas)) {
                    cmlis = (List) cmdmap.get(clas);
                    cmlis.add((String) resultMap.get("vizname"));
                    cmdmap.put(clas, cmlis);
                } else {
                    cmlis = new ArrayList();
                    cmlis.add((String) resultMap.get("vizname"));
                    cmdmap.put(clas, cmlis);
                }
            }
            result.put("commoditynamelis", cmdmap);
        }

        List validRs1Rows = new ArrayList();
        HashMap voByKey = new HashMap();
        lis = (List) map.get("rs1");
        if (lis != null) {
            HashMap conmap = new HashMap();
            TreeSet classset = new TreeSet();
            String clas = null;
            for (int i = 0; i < lis.size(); i++) {
                resultMap = (Map) lis.get(i);
                if (!isValidContractRow(resultMap, shipmentDate)) {
                    continue;
                }
                validRs1Rows.add(resultMap);
                ContractVo convo = mapContractVo(resultMap, includeContractStatus);
                voByKey.put(convo.getRateno() + "-" + convo.getItemno(), convo);
                clas = (String) resultMap.get("class");
                classset.add(clas);
                if (conmap.containsKey(clas)) {
                    cmlis = (List) conmap.get(clas);
                    cmlis.add(convo);
                    conmap.put(clas, cmlis);
                } else {
                    cmlis = new ArrayList();
                    cmlis.add(convo);
                    conmap.put(clas, cmlis);
                }
            }
            result.put("ContractDetails", conmap);
            result.put("ClassDetailsSet", classset);
        }

        Map setupByContractKey = buildSetupChargeLines((List) map.get("rs2"));

        SurchargeComputation surcharge = computeSurcharges(codesByClass, validRs1Rows, shipmentDate, commodity);
        clubSurchargeResults(result, surcharge, voByKey, setupByContractKey);
    }

    /**
     * Builds the RS2 "setup" charge lines from usp_ecomContract's charge result set,
     * keyed by contractno-itemno so they line up with each contract's all-in lookup.
     * These carry the vatos flag used by the amended (A) all-in calculation.
     */
    private Map buildSetupChargeLines(List rs2) {
        Map byContractKey = new HashMap();
        if (rs2 == null) {
            return byContractKey;
        }
        for (int i = 0; i < rs2.size(); i++) {
            Map row = (Map) rs2.get(i);
            String chargeCode = checkNull((String) row.get("chargecode"));
            if (chargeCode.length() == 0) {
                continue;
            }
            String key = checkNull((String) row.get("contractno")) + "-" + checkNull((String) row.get("itemno"));
            Map<String, AllinAmtCalculator.ChargeLine> lines =
                    (Map<String, AllinAmtCalculator.ChargeLine>) byContractKey.get(key);
            if (lines == null) {
                lines = new HashMap<String, AllinAmtCalculator.ChargeLine>();
                byContractKey.put(key, lines);
            }
            lines.put(chargeCode, new AllinAmtCalculator.ChargeLine(
                    chargeCode, parseAmount((String) row.get("amtusd")), checkNull((String) row.get("vatos")),
                    ContractSurchargeResultVo.deriveChargeBasis((String) row.get("eqptype"))));
        }
        return byContractKey;
    }

    private SurchargeComputation computeSurcharges(Map codesByClass, List validRs1Rows, String shipmentDate, String commodity) {
        List surchargeInputs = new ArrayList();
        List surchargeRows = new ArrayList();
        int pairCounter = 1;
        for (int i = 0; i < validRs1Rows.size(); i++) {
            Map rs1Row = (Map) validRs1Rows.get(i);
            String clas = (String) rs1Row.get("class");
            Set classCodes = (Set) codesByClass.get(clas);
            if (classCodes == null || classCodes.isEmpty()) {
                ContractSurchargeInputVo vo = ContractSurchargeInputVo.fromRs1Map(rs1Row, shipmentDate, commodity);
                vo.setPairId(String.valueOf(pairCounter++));
                surchargeInputs.add(vo);
                surchargeRows.add(rs1Row);
            } else {
                for (Object codeObj : classCodes) {
                    ContractSurchargeInputVo vo = ContractSurchargeInputVo.fromRs1Map(rs1Row, shipmentDate,
                            (String) codeObj);
                    vo.setPairId(String.valueOf(pairCounter++));
                    surchargeInputs.add(vo);
                    surchargeRows.add(rs1Row);
                }
            }
        }
        SurchargeComputation comp = new SurchargeComputation();
        comp.inputs = surchargeInputs;
        comp.rows = surchargeRows;
        comp.result = ContractGlobalSurchargeHelper.calculateSurcharges(surchargeInputs);
        return comp;
    }

    private void clubSurchargeResults(Map result, SurchargeComputation surcharge, Map voByKey, Map setupByContractKey) {
        List surchargeInputs = surcharge.inputs;
        List surchargeRows = surcharge.rows;
        ContractSurchargeCalculationResult surchargeResult = surcharge.result;
        if (surchargeResult.getMissingFields() != null && !surchargeResult.getMissingFields().isEmpty()) {
            result.put("SurchargeMissingFields", new ArrayList(surchargeResult.getMissingFields()));
        }

        Map vdsCharges = (Map) result.get("ApplicableCharges");
        Map acmap = new HashMap();
        TreeSet chargeset = new TreeSet();
        Map chargesByPairId = surchargeResult.getChargesByPairId();
        for (int i = 0; i < surchargeInputs.size(); i++) {
            ContractSurchargeInputVo input = (ContractSurchargeInputVo) surchargeInputs.get(i);
            String contractKey = input.getContractKey();

            Map chargemap = new HashMap();
            // Seed with VDS charges as the baseline
            Map vdsContractCharges = (vdsCharges != null) ? (Map) vdsCharges.get(contractKey) : null;
            if (vdsContractCharges != null) {
                chargemap.putAll(vdsContractCharges);
                for (Object k : vdsContractCharges.keySet()) {
                    chargeset.add((String) k);
                }
            }

            // Overlay SP charges only when the contract's VDS vatos flag is Y
            Map chargeRows = chargesByPairId == null ? null : (Map) chargesByPairId.get(input.getPairId());
            if (chargeRows != null) {
                for (Object entryObj : chargeRows.entrySet()) {
                    Map.Entry entry = (Map.Entry) entryObj;
                    String chargeCode = (String) entry.getKey();
                    ContractSurchargeResultVo chargeVo = (ContractSurchargeResultVo) entry.getValue();
                    String contractVatos = extractVatosFromContractCharge((String) chargemap.get(chargeCode));
                    if ("Y".equalsIgnoreCase(contractVatos)) {
                        chargeset.add(chargeCode);
                        chargemap.put(chargeCode, chargeVo.toApplicableChargeValue());
                    }
                    // Contract vatos=N: SP value ignored, VDS contract value retained
                }
            }

            if (!chargemap.isEmpty()) {
                acmap.put(contractKey, chargemap);
            }
        }
        result.put("ApplicableCharges", acmap);
        result.put("Chargecodeset", chargeset);

        Map allinMap = new HashMap();
        Map resultByContract = surchargeResult.getResultChargesByContract();
        for (int i = 0; i < surchargeInputs.size(); i++) {
            ContractSurchargeInputVo input = (ContractSurchargeInputVo) surchargeInputs.get(i);
            Map rs1Row = (Map) surchargeRows.get(i);
            String contractKey = input.getContractKey();
            String pairId = input.getPairId();
            double baseAmt = parseAmount((String) rs1Row.get("baserate"));
            Map setupLines = setupByContractKey == null ? null : (Map) setupByContractKey.get(contractKey);
            Map resultLines = toChargeLines(resultByContract == null ? null : (Map) resultByContract.get(pairId));
            char allinType = resolveAllinType((String) rs1Row.get("allin"));
            String allin = formatAllin(AllinAmtCalculator.computeAllinAmt(
                    allinType, baseAmt, setupLines, resultLines));
            allinMap.put(contractKey, allin);
            ContractVo convo = (ContractVo) voByKey.get(contractKey);
            if (convo != null) {
                convo.setAllinrate(allin);
            }
        }
        result.put("AllinAmount", allinMap);
    }

    private static class SurchargeComputation {
        private List inputs;
        private List rows;
        private ContractSurchargeCalculationResult result;
    }

    private Map<String, AllinAmtCalculator.ChargeLine> toChargeLines(Map src) {
        Map<String, AllinAmtCalculator.ChargeLine> lines = new HashMap<String, AllinAmtCalculator.ChargeLine>();
        if (src != null) {
            for (Object entryObj : src.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                String chargeCode = (String) entry.getKey();
                ContractSurchargeResultVo vo = (ContractSurchargeResultVo) entry.getValue();
                lines.put(chargeCode, new AllinAmtCalculator.ChargeLine(
                        vo.getChargeCode(), parseAmount(vo.getAmountUsd()), vo.getVatos(), vo.getChargeBasis()));
            }
        }
        return lines;
    }

    /**
     * Resolves the contract's all-in type (rs1 "allin" column). Per business rule
     * the original (O) all-in follows the same concept as the amended (A) all-in,
     * so both types resolve to the amended calculation.
     */
    private char resolveAllinType(String type) {
        return AllinAmtCalculator.ALLIN_AMENDED;
    }

    private double parseAmount(String value) {
        String text = checkNull(value).replace(",", "");
        if (text.length() == 0) {
            return 0d;
        }
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0d;
        }
    }

    private String formatAllin(double amount) {
        if (Math.abs(amount - Math.round(amount)) < 0.0001d) {
            return String.valueOf(Math.round(amount));
        }
        return String.format("%.2f", amount);
    }

    private ContractVo mapContractVo(Map resultMap, boolean includeContractStatus) {
        ContractVo convo = new ContractVo();
        convo.setAllinrate((String) resultMap.get("allinrate"));
        convo.setAmendmentno((String) resultMap.get("amendmentno"));
        convo.setBaserate((String) resultMap.get("baserate"));
        convo.setDestination((String) resultMap.get("dischport"));
        convo.setDestinationcountrycode((String) resultMap.get("dischport_country"));
        convo.setDestinationstatecode((String) resultMap.get("dischport_state"));
        convo.setEffective((String) resultMap.get("effectivedate"));
        convo.setEqptype((String) resultMap.get("eqptype"));
        convo.setExpiration((String) resultMap.get("expiration"));
        convo.setNor((String) resultMap.get("nor"));
        convo.setNotes((String) resultMap.get("ratedescription"));
        convo.setOrigin((String) resultMap.get("loadport"));
        convo.setOrigincountrycode((String) resultMap.get("loadport_country"));
        convo.setOriginstatecode((String) resultMap.get("loadport_state"));
        convo.setPod((String) resultMap.get("pod"));
        convo.setPol((String) resultMap.get("pol"));
        convo.setRateno((String) resultMap.get("contractno"));
        convo.setSoc((String) resultMap.get("soc"));
        convo.setTypeofservice((String) resultMap.get("tservice"));
        convo.setValidfrom((String) resultMap.get("validfrom"));
        convo.setValidto((String) resultMap.get("validto"));
        convo.setItemno((String) resultMap.get("itemno"));
        convo.setVid((String) resultMap.get("vid"));
        if (includeContractStatus) {
            convo.setContractstatus((String) resultMap.get("contractstatus"));
        }
        return convo;
    }

    //
    private boolean isValidContractRow(Map rs1Row, String shipmentDate) {
        if (checkNull((String) rs1Row.get("baserate")).length() == 0) {
            return false;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String shipText = checkNull(shipmentDate);
            if (shipText.length() < 10) {
                return true;
            }
            Date shipDate = df.parse(shipText.substring(0, 10));
            String validFrom = checkNull((String) rs1Row.get("validfrom"));
            if (validFrom.length() >= 10 && shipDate.before(df.parse(validFrom.substring(0, 10)))) {
                return false;
            }
            String validTo = checkNull((String) rs1Row.get("validto"));
            if (validTo.length() >= 10 && shipDate.after(df.parse(validTo.substring(0, 10)))) {
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }    

    /**
     * Parses the vatos flag from a VDS contract charge value stored as
     * "amount~~vatos~~eqptype" (e.g. "175.0~~Y~~null"). Returns "N" when
     * the charge value is absent or the flag cannot be determined.
     */
    private String extractVatosFromContractCharge(String chargeValue) {
        if (chargeValue == null || chargeValue.length() == 0) {
            return "N";
        }
        String[] parts = chargeValue.split("~~");
        if (parts.length > 1) {
            return checkNull(parts[1]);
        }
        return "N";
    }

    private String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }
}
