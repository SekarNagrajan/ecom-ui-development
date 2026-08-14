/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gnanamoorthi.s
 */
public class SecurityVO implements Serializable  {
    public SecurityVO()

    {
        
    }
private String carriercode="";
private String blockchar="";
private String blockwords="";
private String status="";
private String blockedip="";
private String blockiprange="";
private String crdate="";
private String update="";
private String editAction="";
private int getRowcount=0;

    /**
     * @return the carriercode
     */
    public String getCarriercode() {
        return carriercode;
    }

    /**
     * @param carriercode the carriercode to set
     */
    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    /**
     * @return the blockchar
     */
    public String getBlockchar() {
        return blockchar;
    }

    /**
     * @param blockchar the blockchar to set
     */
    public void setBlockchar(String blockchar) {
        this.blockchar = blockchar;
    }

    /**
     * @return the blockwords
     */
    public String getBlockwords() {
        return blockwords;
    }

    /**
     * @param blockwords the blockwords to set
     */
    public void setBlockwords(String blockwords) {
        this.blockwords = blockwords;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the blockedip
     */
    public String getBlockedip() {
        return blockedip;
    }

    /**
     * @param blockedip the blockedip to set
     */
    public void setBlockedip(String blockedip) {
        this.blockedip = blockedip;
    }

    /**
     * @return the blockiprange
     */
    public String getBlockiprange() {
        return blockiprange;
    }

    /**
     * @param blockiprange the blockiprange to set
     */
    public void setBlockiprange(String blockiprange) {
        this.blockiprange = blockiprange;
    }

    /**
     * @return the crdate
     */
    public String getCrdate() {
        return crdate;
    }

    /**
     * @param crdate the crdate to set
     */
    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    /**
     * @return the update
     */
    public String getUpdate() {
        return update;
    }

    /**
     * @param update the update to set
     */
    public void setUpdate(String update) {
        this.update = update;
    }

    /**
     * @return the editAction
     */
    public String getEditAction() {
        return editAction;
    }

    /**
     * @param editAction the editAction to set
     */
    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

    /**
     * @return the getRowcount
     */
    public int getGetRowcount() {
        return getRowcount;
    }

    /**
     * @param getRowcount the getRowcount to set
     */
    public void setGetRowcount(int getRowcount) {
        this.getRowcount = getRowcount;
    }

}
