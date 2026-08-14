/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author salomi.a
 */
public class FileDetailsVO implements Serializable {

    private byte[] vgmFileData = null;
    private String vgmFileName = null;

    private byte[] vgmFileData1 = null;
    private String vgmFileName1 = null;

    private byte[] vgmFileData2 = null;
    private String vgmFileName2 = null;

    private byte[] dgFileData = null;
    private String dgFileName = null;

    private byte[] dgFileData1 = null;
    private String dgFileName1 = null;

    private byte[] dgFileData2 = null;
    private String dgFileName2 = null;

    private byte[] lolFileData = null;
    private String lolFileName = null;
    
    private byte[] lolFileData1 = null;
    private String lolFileName1 = null;
    
    private byte[] lolFileData2 = null;
    private String lolFileName2 = null;

    private byte[] msdsFileData = null;
    private String msdsFileName = null;
    
    private byte[] msdsFileData1 = null;
    private String msdsFileName1 = null;
    
    private byte[] msdsFileData2 = null;
    private String msdsFileName2 = null;

    private byte[] othersFileData = null;
    private String othersFileName = null;
    
    private byte[] othersFileData1 = null;
    private String othersFileName1 = null;
    
    private byte[] othersFileData2 = null;
    private String othersFileName2=  null;

    private String filePath = null;
    private boolean fileAvailable = false;
    
    private String deletedfilename =null;
    
    private String enableZipConversion=null;

    public byte[] getVgmFileData() {
        return vgmFileData;
    }

    public void setVgmFileData(byte[] vgmFileData) {
        this.vgmFileData = vgmFileData;
    }

    public String getVgmFileName() {
        return vgmFileName;
    }

    public void setVgmFileName(String vgmFileName) {
        this.vgmFileName = vgmFileName;
    }

    public byte[] getDgFileData() {
        return dgFileData;
    }

    public void setDgFileData(byte[] dgFileData) {
        this.dgFileData = dgFileData;
    }

    public String getDgFileName() {
        return dgFileName;
    }

    public void setDgFileName(String dgFileName) {
        this.dgFileName = dgFileName;
    }

    public byte[] getLolFileData() {
        return lolFileData;
    }

    public void setLolFileData(byte[] lolFileData) {
        this.lolFileData = lolFileData;
    }

    public String getLolFileName() {
        return lolFileName;
    }

    public void setLolFileName(String lolFileName) {
        this.lolFileName = lolFileName;
    }

    public byte[] getMsdsFileData() {
        return msdsFileData;
    }

    public void setMsdsFileData(byte[] msdsFileData) {
        this.msdsFileData = msdsFileData;
    }

    public String getMsdsFileName() {
        return msdsFileName;
    }

    public void setMsdsFileName(String msdsFileName) {
        this.msdsFileName = msdsFileName;
    }

    public byte[] getOthersFileData() {
        return othersFileData;
    }

    public void setOthersFileData(byte[] othersFileData) {
        this.othersFileData = othersFileData;
    }

    public String getOthersFileName() {
        return othersFileName;
    }

    public void setOthersFileName(String othersFileName) {
        this.othersFileName = othersFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isFileAvailable() {
        return fileAvailable;
    }

    public void setFileAvailable(boolean fileAvailable) {
        this.fileAvailable = fileAvailable;
    }

    public byte[] getVgmFileData1() {
        return vgmFileData1;
    }

    public void setVgmFileData1(byte[] vgmFileData1) {
        this.vgmFileData1 = vgmFileData1;
    }

    public String getVgmFileName1() {
        return vgmFileName1;
    }

    public void setVgmFileName1(String vgmFileName1) {
        this.vgmFileName1 = vgmFileName1;
    }

    public byte[] getVgmFileData2() {
        return vgmFileData2;
    }

    public void setVgmFileData2(byte[] vgmFileData2) {
        this.vgmFileData2 = vgmFileData2;
    }

    public String getVgmFileName2() {
        return vgmFileName2;
    }

    public void setVgmFileName2(String vgmFileName2) {
        this.vgmFileName2 = vgmFileName2;
    }

    public byte[] getDgFileData1() {
        return dgFileData1;
    }

    public void setDgFileData1(byte[] dgFileData1) {
        this.dgFileData1 = dgFileData1;
    }

    public String getDgFileName1() {
        return dgFileName1;
    }

    public void setDgFileName1(String dgFileName1) {
        this.dgFileName1 = dgFileName1;
    }

    public byte[] getDgFileData2() {
        return dgFileData2;
    }

    public void setDgFileData2(byte[] dgFileData2) {
        this.dgFileData2 = dgFileData2;
    }

    public String getDgFileName2() {
        return dgFileName2;
    }

    public void setDgFileName2(String dgFileName2) {
        this.dgFileName2 = dgFileName2;
    }

    public byte[] getLolFileData1() {
        return lolFileData1;
    }

    public void setLolFileData1(byte[] lolFileData1) {
        this.lolFileData1 = lolFileData1;
    }

    public String getLolFileName1() {
        return lolFileName1;
    }

    public void setLolFileName1(String lolFileName1) {
        this.lolFileName1 = lolFileName1;
    }

    public byte[] getLolFileData2() {
        return lolFileData2;
    }

    public void setLolFileData2(byte[] lolFileData2) {
        this.lolFileData2 = lolFileData2;
    }

    public String getLolFileName2() {
        return lolFileName2;
    }

    public void setLolFileName2(String lolFileName2) {
        this.lolFileName2 = lolFileName2;
    }

    public byte[] getMsdsFileData1() {
        return msdsFileData1;
    }

    public void setMsdsFileData1(byte[] msdsFileData1) {
        this.msdsFileData1 = msdsFileData1;
    }

    public String getMsdsFileName1() {
        return msdsFileName1;
    }

    public void setMsdsFileName1(String msdsFileName1) {
        this.msdsFileName1 = msdsFileName1;
    }

    public byte[] getMsdsFileData2() {
        return msdsFileData2;
    }

    public void setMsdsFileData2(byte[] msdsFileData2) {
        this.msdsFileData2 = msdsFileData2;
    }

    public String getMsdsFileName2() {
        return msdsFileName2;
    }

    public void setMsdsFileName2(String msdsFileName2) {
        this.msdsFileName2 = msdsFileName2;
    }

    public byte[] getOthersFileData1() {
        return othersFileData1;
    }

    public void setOthersFileData1(byte[] othersFileData1) {
        this.othersFileData1 = othersFileData1;
    }

    public String getOthersFileName1() {
        return othersFileName1;
    }

    public void setOthersFileName1(String othersFileName1) {
        this.othersFileName1 = othersFileName1;
    }

    public byte[] getOthersFileData2() {
        return othersFileData2;
    }

    public void setOthersFileData2(byte[] othersFileData2) {
        this.othersFileData2 = othersFileData2;
    }

    public String getOthersFileName2() {
        return othersFileName2;
    }

    public void setOthersFileName2(String othersFileName2) {
        this.othersFileName2 = othersFileName2;
    }

    public String getDeletedfilename() {
        return deletedfilename;
    }

    public void setDeletedfilename(String deletedfilename) {
        this.deletedfilename = deletedfilename;
    }

    public String getEnableZipConversion() {
        return enableZipConversion;
    }

    public void setEnableZipConversion(String enableZipConversion) {
        this.enableZipConversion = enableZipConversion;
    }

}
