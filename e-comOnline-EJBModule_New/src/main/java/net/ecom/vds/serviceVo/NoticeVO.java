/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import java.io.Serializable;

/**
 *
 * @author preethi.elumalai
 */
public class NoticeVO implements Serializable {
   private String title;
   private String filename;
   private String crdate;
   private String cruser;
   private String chgdate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCrdate() {
        return crdate;
    }

    public void setCrdate(String crdate) {
        this.crdate = crdate;
    }

    public String getCruser() {
        return cruser;
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public String getChgdate() {
        return chgdate;
    }

    public void setChgdate(String chgdate) {
        this.chgdate = chgdate;
    }

 
}
