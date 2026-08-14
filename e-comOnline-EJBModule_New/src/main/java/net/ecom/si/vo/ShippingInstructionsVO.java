/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;

import java.io.Serializable;


public class ShippingInstructionsVO extends SiTemp implements Serializable {

    private String strBlNo = null;
    private String strModifiedDate;
    private boolean blIsDeletable;
    // On 26-12-2011 ,Done by sri. The following obj has been added for combining the same booking with the same Agreement Party, Shipper Party, Notify Party,Consignee Service, Vessel, Voyage, Bound, PLA, POL, POLTerminal, POD, POD Terminal & PLD.
    private String[] strParty = new String[4];
    private String strPolTerminal = null;
    private String strPodTerminal = null;
    private String strPla = null;
    private String strPld = null;
    //On 05-1-2012,Based on Mr.VJ suggestion,done by sri. In N:1 process we have to show the  party name in mouse overlap.
    private String[] strPartyName = new String[4];
    //On 12/09/2012, done by sri. For obl print purpose, i am adding the strFCN object
    private String strFCN = null;
    private String strBlPrintedUser = null;
    private String strBlType = null;
    private String strBlIssueAgency=null;

    /**
     * @return the strBlNo
     */
    public String getStrBlNo() {
        return strBlNo;
    }

    /**
     * @param strBlNo the strBlNo to set
     */
    public void setStrBlNo(String strBlNo) {
        this.strBlNo = strBlNo;
    }

    /**
     * @return the strModifiedDate
     */
    public String getStrModifiedDate() {
        return strModifiedDate;
    }

    /**
     * @param strModifiedDate the strModifiedDate to set
     */
    public void setStrModifiedDate(String strModifiedDate) {
        this.strModifiedDate = strModifiedDate;
    }

    /**
     * @return the blIsDeletable
     */
    public boolean isBlIsDeletable() {
        return blIsDeletable;
    }

    /**
     * @param blIsDeletable the blIsDeletable to set
     */
    public void setBlIsDeletable(boolean blIsDeletable) {
        this.blIsDeletable = blIsDeletable;
    }

    /**
     * @return the strPolTerminal
     */
    public String getStrPolTerminal() {
        return strPolTerminal;
    }

    /**
     * @param strPolTerminal the strPolTerminal to set
     */
    public void setStrPolTerminal(String strPolTerminal) {
        this.strPolTerminal = strPolTerminal;
    }

    /**
     * @return the strPodTerminal
     */
    public String getStrPodTerminal() {
        return strPodTerminal;
    }

    /**
     * @param strPodTerminal the strPodTerminal to set
     */
    public void setStrPodTerminal(String strPodTerminal) {
        this.strPodTerminal = strPodTerminal;
    }

    /**
     * @return the strPla
     */
    public String getStrPla() {
        return strPla;
    }

    /**
     * @param strPla the strPla to set
     */
    public void setStrPla(String strPla) {
        this.strPla = strPla;
    }

    /**
     * @return the strPld
     */
    public String getStrPld() {
        return strPld;
    }

    /**
     * @param strPld the strPld to set
     */
    public void setStrPld(String strPld) {
        this.strPld = strPld;
    }

    /**
     * @return the strParty
     */
    public String[] getStrParty() {
        return strParty;
    }

    /**
     * @param strParty the strParty to set
     */
    public void setStrParty(String[] strParty) {
        this.strParty = strParty;
    }

    /**
     * @return the strPartyName
     */
    public String[] getStrPartyName() {
        return strPartyName;
    }

    /**
     * @param strPartyName the strPartyName to set
     */
    public void setStrPartyName(String[] strPartyName) {
        this.strPartyName = strPartyName;
    }

    /**
     * @return the strFCN
     */
    public String getStrFCN() {
        return strFCN;
    }

    /**
     * @param strFCN the strFCN to set
     */
    public void setStrFCN(String strFCN) {
        this.strFCN = strFCN;
    }

    /**
     * @return the strBlPrintedUser
     */
    public String getStrBlPrintedUser() {
        return strBlPrintedUser;
    }

    /**
     * @param strBlPrintedUser the strBlPrintedUser to set
     */
    public void setStrBlPrintedUser(String strBlPrinterUser) {
        this.strBlPrintedUser = strBlPrinterUser;
    }

    /**
     * @return the strBlType
     */
    public String getStrBlType() {
        return strBlType;
    }

    /**
     * @param strBlType the strBlType to set
     */
    public void setStrBlType(String strBlType) {
        this.strBlType = strBlType;
    }

    /**
     * @return the strBlIssueAgency
     */
    public String getStrBlIssueAgency() {
        return strBlIssueAgency;
    }

    /**
     * @param strBlIssueAgency the strBlIssueAgency to set
     */
    public void setStrBlIssueAgency(String strBlIssueAgency) {
        this.strBlIssueAgency = strBlIssueAgency;
    }
}
