/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.phase2.service.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class AmountSummaryVO implements Serializable {

    @SerializedName("currency")
    @Expose
    private String invCurrency = null;

    @SerializedName("excessamtinusd")
    @Expose
    private String excessAmtUSD = null;

    @SerializedName("totalinvamount")
    @Expose
    private String invAmount = null;

    @SerializedName("totalinvamountinusd")
    @Expose
    private String invAmtUSD = null;

    @SerializedName("totalbalanceamtinusd")
    @Expose
    private String outStandingAmtUSD = null;

    @SerializedName("totalreceiptamtinusd")
    @Expose
    private String payAmtUSD = null;
    
    @SerializedName("invamount")
    @Expose
    private String ciInvAmount = null;
    
    @SerializedName("invcurrency")
    @Expose
    private String ciInvCurrency = null;

    public String getCiInvAmount() {
        return ciInvAmount;
    }

    public void setCiInvAmount(String ciInvAmount) {
        this.ciInvAmount = ciInvAmount;
    }

    public String getCiInvCurrency() {
        return ciInvCurrency;
    }

    public void setCiInvCurrency(String ciInvCurrency) {
        this.ciInvCurrency = ciInvCurrency;
    }

    public String getExcessAmtUSD() {
        return excessAmtUSD;
    }

    public void setExcessAmtUSD(String excessAmtUSD) {
        this.excessAmtUSD = excessAmtUSD;
    }

    public String getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(String invAmount) {
        this.invAmount = invAmount;
    }

    public String getInvAmtUSD() {
        return invAmtUSD;
    }

    public void setInvAmtUSD(String invAmtUSD) {
        this.invAmtUSD = invAmtUSD;
    }

    public String getInvCurrency() {
        return invCurrency;
    }

    public void setInvCurrency(String invCurrency) {
        this.invCurrency = invCurrency;
    }

    public String getOutStandingAmtUSD() {
        return outStandingAmtUSD;
    }

    public void setOutStandingAmtUSD(String outStandingAmtUSD) {
        this.outStandingAmtUSD = outStandingAmtUSD;
    }

    public String getPayAmtUSD() {
        return payAmtUSD;
    }

    public void setPayAmtUSD(String payAmtUSD) {
        this.payAmtUSD = payAmtUSD;
    }
}
