package net.ecom.surcharge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.ecom.common.Validation;

public class ContractSurchargeInputVo implements Serializable {

    private String contractNo;
    private String itemno;
    private String loadport;
    private String loadterminal;
    private String dischport;
    private String dischterminal;
    private String tranship1;
    private String tranship1Terminal;
    private String tranship2;
    private String tranship2Terminal;
    private String tranship3;
    private String tranship3Terminal;
    private String surchargeDate;
    private String eqptype;
    private String commodityCode;
    private String dgClass;
    private String service;
    private String custCode;
    private String originport;
    private String deliveryport;
    private String weight;
    private String isDg;
    private String orgMode;
    private String delMode;
    private String soc;
    private String nor;
    private String oog;
    private String ometrotype;
    private String ometrocode;
    private String dmetrotype;
    private String dmetrocode;
    private String containerFlag;
    private String receipt;
    private String delivery;
    private String pairId;
    private final List<String> missingFields = new ArrayList<>();

    public static ContractSurchargeInputVo fromRs1Map(Map rs1Row, String shipmentDate, String commodity) {
        ContractSurchargeInputVo vo = new ContractSurchargeInputVo();
        vo.contractNo = firstNonEmpty(rs1Row, "contractno");
        vo.itemno = firstNonEmpty(rs1Row, "itemno");
        vo.loadport = firstNonEmpty(rs1Row, "pol");
        if (vo.loadport.length() == 0) {
            vo.trackMissing("loadport", "rs1 pol");
        }
        vo.dischport = firstNonEmpty(rs1Row, "pod");
        if (vo.dischport.length() == 0) {
            vo.trackMissing("dischport", "rs1 pod");
        }
        vo.loadterminal = firstNonEmpty(rs1Row, "LoadTerminal");
        vo.dischterminal = firstNonEmpty(rs1Row, "DischargeTerminal");
        vo.tranship1 = firstNonEmpty(rs1Row, "Tranship1");
        vo.tranship1Terminal = firstNonEmpty(rs1Row, "Tranship1Terminal");
        vo.tranship2 = firstNonEmpty(rs1Row, "Tranship2");
        vo.tranship2Terminal = firstNonEmpty(rs1Row, "Tranship2Terminal");
        vo.tranship3 = firstNonEmpty(rs1Row, "Tranship3");
        vo.tranship3Terminal = firstNonEmpty(rs1Row, "Tranship3Terminal");
        vo.surchargeDate = Validation.checkNull(shipmentDate);
        vo.eqptype = firstNonEmpty(rs1Row, "eqptype");
        if (vo.eqptype.length() == 0) {
            vo.trackMissing("eqptype", "rs1 eqptype");
        }
        vo.commodityCode = Validation.checkNull(commodity);
        if (vo.commodityCode.length() == 0) {
            vo.commodityCode = firstNonEmpty(rs1Row, "commodity", "commoditycode");
        }
        if (vo.commodityCode.length() == 0) {
            vo.trackMissing("commodityCode", "rs0 commodity list / rs1 commodity");
        }
        vo.dgClass = firstNonEmpty(rs1Row, "class");
        if (vo.dgClass.length() == 0) {
            vo.dgClass = "99";
            vo.trackMissing("dgClass", "rs1 class (default 99 applied)");
        }
        vo.service = firstNonEmpty(rs1Row, "Service");

        vo.custCode = firstNonEmpty(rs1Row, "custcode");
        if (vo.custCode.length() == 0) {
            vo.trackMissing("custCode", "rs1 custcode");
        }
        vo.originport = firstNonEmpty(rs1Row, "loadport");
        if (vo.originport.length() == 0) {
            vo.originport = vo.loadport;
            vo.trackMissing("originport", "rs1 loadport (fallback loadport used)");
        }
        vo.deliveryport = firstNonEmpty(rs1Row, "dischport");
        if (vo.deliveryport.length() == 0) {
            vo.deliveryport = vo.dischport;
            vo.trackMissing("deliveryport", "rs1 dischport (fallback dischport used)");
        }
        vo.weight = firstNonEmpty(rs1Row, "Weight");
        if (vo.weight.length() == 0) {
            vo.weight = "0";
            vo.trackMissing("weight", "rs1 Weight (default 0 applied)");
        }
        vo.isDg = firstNonEmpty(rs1Row, "isDG");
        if (vo.isDg.length() == 0) {
            vo.isDg = "N";
            vo.trackMissing("isDg", "rs1 isDG (default N applied)");
        }
        vo.orgMode = firstNonEmpty(rs1Row, "Orgmode");
        vo.delMode = firstNonEmpty(rs1Row, "Delmode");
        vo.soc = firstNonEmpty(rs1Row, "soc");
        if (vo.soc.length() == 0) {
            vo.soc = "N";
            vo.trackMissing("soc", "rs1 soc (default N applied)");
        }
        vo.nor = firstNonEmpty(rs1Row, "nor");
        if (vo.nor.length() == 0) {
            vo.nor = "N";
            vo.trackMissing("nor", "rs1 nor (default N applied)");
        }
        vo.oog = firstNonEmpty(rs1Row, "OOG");
        if (vo.oog.length() == 0) {
            vo.oog = "N";
            vo.trackMissing("oog", "rs1 OOG (default N applied)");
        }
        vo.ometrotype = firstNonEmpty(rs1Row, "ometrotype");
        if (vo.ometrotype.length() == 0) {
            vo.trackMissing("ometrotype", "rs1 ometrotype");
        }
        vo.ometrocode = firstNonEmpty(rs1Row, "ometrocode");
        if (vo.ometrocode.length() == 0) {
            vo.trackMissing("ometrocode", "rs1 ometrocode");
        }
        vo.dmetrotype = firstNonEmpty(rs1Row, "dmetrotype");
        if (vo.dmetrotype.length() == 0) {
            vo.trackMissing("dmetrotype", "rs1 dmetrotype");
        }
        vo.dmetrocode = firstNonEmpty(rs1Row, "dmetrocode");
        if (vo.dmetrocode.length() == 0) {
            vo.trackMissing("dmetrocode", "rs1 dmetrocode");
        }
        vo.receipt = firstNonEmpty(rs1Row, "receipt");
        vo.delivery = firstNonEmpty(rs1Row, "delivery");
        vo.containerFlag = firstNonEmpty(rs1Row, "ContainerFlag");
        if (vo.containerFlag.length() == 0) {
            vo.containerFlag = "Y";
        }        
        if (vo.service.length() == 0) {
            vo.trackMissing("service", "rs1 Service");
        }
        return vo;
    }

    public static Set<String> aggregateMissingFields(List<ContractSurchargeInputVo> rows) {
        Set<String> missing = new LinkedHashSet<>();
        if (rows == null) {
            return missing;
        }
        for (ContractSurchargeInputVo row : rows) {
            if (row != null && row.missingFields != null) {
                missing.addAll(row.missingFields);
            }
        }
        return missing;
    }

    private void trackMissing(String column, String source) {
        missingFields.add(column + " :: " + source);
    }

    private static String firstNonEmpty(Map map, String... keys) {
        if (map == null || keys == null) {
            return "";
        }
        for (String key : keys) {
            Object value = map.get(key == null ? null : key.toLowerCase());
            if (value != null) {
                String text = Validation.checkNull(String.valueOf(value));
                if (text.length() > 0) {
                    return text;
                }
            }
        }
        return "";
    }

    public String getContractNo() { return contractNo; }
    public String getItemno() { return itemno; }
    public void setPairId(String pairId) { this.pairId = pairId; }
    public String getPairId() { return (pairId != null && pairId.length() > 0) ? pairId : itemno; }
    public String getContractKey() { return Validation.checkNull(contractNo) + "-" + Validation.checkNull(itemno); }
    public List<String> getMissingFields() { return missingFields; }
    public String getLoadport() { return loadport; }
    public String getLoadterminal() { return loadterminal; }
    public String getDischport() { return dischport; }
    public String getDischterminal() { return dischterminal; }
    public String getTranship1() { return tranship1; }
    public String getTranship1Terminal() { return tranship1Terminal; }
    public String getTranship2() { return tranship2; }
    public String getTranship2Terminal() { return tranship2Terminal; }
    public String getTranship3() { return tranship3; }
    public String getTranship3Terminal() { return tranship3Terminal; }
    public String getSurchargeDate() { return surchargeDate; }
    public String getEqptype() { return eqptype; }
    public String getCommodityCode() { return commodityCode; }
    public String getDgClass() { return dgClass; }
    public String getService() { return service; }
    public String getCustCode() { return custCode; }
    public String getOriginport() { return originport; }
    public String getDeliveryport() { return deliveryport; }
    public String getWeight() { return weight; }
    public String getIsDg() { return isDg; }
    public String getOrgMode() { return orgMode; }
    public String getDelMode() { return delMode; }
    public String getSoc() { return soc; }
    public String getNor() { return nor; }
    public String getOog() { return oog; }
    public String getOmetrotype() { return ometrotype; }
    public String getOmetrocode() { return ometrocode; }
    public String getDmetrotype() { return dmetrotype; }
    public String getDmetrocode() { return dmetrocode; }
    public String getContainerFlag() { return containerFlag; }
    public String getReceipt() { return receipt; }
    public String getDelivery() { return delivery; }
}
