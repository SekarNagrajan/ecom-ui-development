/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class PasswordResetTokenVo implements Serializable {
    
    private int id;
    private String emailID;
    private String token;
    private String requestDate;
    private String completeDate;
    private String status;
    private String strName;
    private String strDisplayMsg;
    private String strRefNo;
    
    public PasswordResetTokenVo() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getEmailID() {
        return emailID;
    }
    
    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getCompleteDate() {
        return completeDate;
    }
    
    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStrName() {
        return strName;
    }
    
    public void setStrName(String strName) {
        this.strName = strName;
    }
    
    public String getStrDisplayMsg() {
        return strDisplayMsg;
    }
    
    public void setStrDisplayMsg(String strDisplayMsg) {
        this.strDisplayMsg = strDisplayMsg;
    }
    
    public String getStrRefNo() {
        return strRefNo;
    }
    
    public void setStrRefNo(String strRefNo) {
        this.strRefNo = strRefNo;
    }
}
