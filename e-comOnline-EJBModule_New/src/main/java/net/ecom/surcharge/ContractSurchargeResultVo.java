package net.ecom.surcharge;

import java.io.Serializable;

public class ContractSurchargeResultVo implements Serializable {

    private String chargeCode;
    private String amountUsd;
    private String vatos;
    private String amtType;
    private String eqpType;

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(String amountUsd) {
        this.amountUsd = amountUsd;
    }

    public String getVatos() {
        return vatos;
    }

    public void setVatos(String vatos) {
        this.vatos = vatos;
    }

    public String getAmtType() {
        return amtType;
    }

    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }

    public String getEqpType() {
        return eqpType;
    }

    public void setEqpType(String eqpType) {
        this.eqpType = eqpType;
    }

    public String toApplicableChargeValue() {
        return checkNull(amountUsd) + "~~" + checkNull(vatos) + "~~" + getChargeBasis();
    }

    /**
     * Charge basis shown on the front end, derived from the eqpType returned by
     * usp_svcGlobalSurchargeMain: "LP" is a lumpsum charge, "TEUS" is a per-TEU
     * charge, anything else is an equipment-based charge.
     */
    public String getChargeBasis() {
        return deriveChargeBasis(eqpType);
    }

    /**
     * Maps a raw eqpType to the charge basis label shown on the front end:
     * "LP" is a lumpsum charge, "TEUS" is a per-TEU charge, anything else is an
     * equipment-based charge.
     */
    public static String deriveChargeBasis(String eqpType) {
        String type = eqpType == null ? "" : eqpType.trim();
        if ("LP".equalsIgnoreCase(type)) {
            return "Lumpsum";
        }
        if ("TEUS".equalsIgnoreCase(type)) {
            return "TEUS";
        }
        return "Equipment";
    }

    private static String checkNull(String value) {
        return value == null || "null".equalsIgnoreCase(value.trim()) ? "" : value.trim();
    }
}
