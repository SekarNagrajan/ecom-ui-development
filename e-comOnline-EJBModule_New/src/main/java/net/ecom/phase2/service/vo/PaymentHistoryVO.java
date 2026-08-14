package net.ecom.phase2.service.vo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentHistoryVO  implements Serializable{
    
    @SerializedName("companycode")
    @Expose
    private String companycode = null;

    @SerializedName("bankname")
    @Expose
    private String bankname = null;

    @SerializedName("bankcode")
    @Expose
    private String bankcode = null;
     
    @SerializedName("currency")
    @Expose
    private String currency = null;

    @SerializedName("accountno")
    @Expose
    private String accountno = null;
    
    @SerializedName("referenceno")
    @Expose
    private String referenceno = null;
    
    @SerializedName("statementdate")
    @Expose
    private String statementdate = null;
    
    @SerializedName("customername")
    @Expose
    private String customername = null;
    
    @SerializedName("customercode")
    @Expose
    private String customercode = null;
    
    @SerializedName("customeraccountno")
    @Expose
    private String customeraccountno = null;
    
    @SerializedName("checkno")
    @Expose
    private String checkno = null;
    
    @SerializedName("checkamount")
    @Expose
    private String checkamount = null;
    
    @SerializedName("checkallocatedamount")
    @Expose
    private String checkallocatedamount = null;
    
    @SerializedName("unallocatedamount")
    @Expose
    private String unallocatedamount = null;
    
    @SerializedName("transactionno")
    @Expose
    private String transactionno = null;
    
    @SerializedName("allocationdate")
    @Expose
    private String allocationdate = null;
    
    @SerializedName("blno")
    @Expose
    private String blno = null;
    
    @SerializedName("mcnno")
    @Expose
    private String mcnno = null;
    
    @SerializedName("blamount")
    @Expose
    private String blamount = null;
    
    @SerializedName("blallocatedamount")
    @Expose
    private String blallocatedamount = null;
    
    @SerializedName("blbalanceamount")
    @Expose
    private String blbalanceamount = null;
    
    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }
    
    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }
    
    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }
    
    public String getReferenceno() {
        return referenceno;
    }

    public void setReferenceno(String referenceno) {
        this.referenceno = referenceno;
    }
    
    public String getStatementdate() {
        return statementdate;
    }

    public void setStatementdate(String statementdate) {
        this.statementdate = statementdate;
    }
    
    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }
    
    public String getCustomercode() {
        return customercode;
    }

    public void setCustomercode(String customercode) {
        this.customercode = customercode;
    }
    
    public String getCustomeraccountno() {
        return customeraccountno;
    }

    public void setCustomeraccountno(String customeraccountno) {
        this.customeraccountno = customeraccountno;
    }
    
    public String getCheckno() {
        return checkno;
    }

    public void setCheckno(String checkno) {
        this.checkno = checkno;
    }   
    
    public String getCheckamount() {
        return checkamount;
    }

    public void setCheckamount(String checkamount) {
        this.checkamount = checkamount;
    }
    
    public String getCheckallocatedamount() {
        return checkallocatedamount;
    }

    public void setCheckallocatedamount(String checkallocatedamount) {
        this.checkallocatedamount = checkallocatedamount;
    }
    
    public String getUnallocatedamount() {
        return unallocatedamount;
    }

    public void setUnallocatedamount(String unallocatedamount) {
        this.unallocatedamount = unallocatedamount;
    }
    
    public String getTransactionno() {
        return transactionno;
    }

    public void setTransactionno(String transactionno) {
        this.transactionno = transactionno;
    }
    
    public String getAllocationdate() {
        return allocationdate;
    }

    public void setAllocationdate(String allocationdate) {
        this.allocationdate = allocationdate;
    }
    
    public String getBlno() {
        return blno;
    }

    public void setBlno(String blno) {
        this.blno = blno;
    }
    
    public String getMcnno() {
        return mcnno;
    }

    public void setMcnno(String mcnno) {
        this.mcnno = mcnno;
    }
    
    public String getBlamount() {   
        return blamount;
    }

    public void setBlamount(String blamount) {
        this.blamount = blamount;
    }
    
    public String getBlallocatedamount() {
        return blallocatedamount;
    }

    public void setBlallocatedamount(String blallocatedamount) {
        this.blallocatedamount = blallocatedamount;
    }
    
    public String getBlbalanceamount() {
        return blbalanceamount;
    }
    
    public void setBlbalanceamount(String blbalanceamount) {
        this.blbalanceamount = blbalanceamount;
    }   
} 
