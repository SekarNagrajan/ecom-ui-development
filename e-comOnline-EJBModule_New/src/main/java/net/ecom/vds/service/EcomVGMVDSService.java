/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.service;

import com.svm.lrp.nfr.query.annotation.FP;
import com.svm.lrp.nfr.query.annotation.VDS;
import com.svm.lrp.nfr.query.bean.helper.Condition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.ecom.vo.VGMVOModel;

/**
 *
 * @author deepa.p
 */
public interface EcomVGMVDSService {

    @VDS(name = "getecomvgmoprbkglist", tenant = "svm")
    public Map<String, ArrayList<String>> getecomvgmoprbkglist(@FP(name = "bookno") String bookno);

    @VDS(name = "getecomvgmbkgnocheck", tenant = "svm")
    public Map<String, ArrayList<String>> getecomvgmbkgnocheck(@FP(name = "book_no") String bookno, @FP(name = "custid") String custid);
    
    @VDS(name = "getecomvgmbkgnocheck", tenant = "svm")
    public Map<String, ArrayList<String>> getecomvgmbkgnocheck(@FP(name = "book_no") String bookno);

    @VDS(name = "getecomvgmloadconfirmstatus", tenant = "svm")
    public Map getecomvgmloadconfirmstatus(@FP(name = "book_no") String bookno);

    @VDS(name = "getecomvgmviewdetails", tenant = "svm")
    public Map getecomvgmviewdetails(@FP(name = "book_no") String bookno);

    @VDS(name = "getecomblvgmviewdets", tenant = "svm")
    public Map getecomblvgmviewdets(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecomvgmcustdetails", tenant = "svm")
    public Map getecomvgmcustdetails(@FP(name = "book_no") String bookno);

    @VDS(name = "getecomblvgmcustdetails", tenant = "svm")
    public Map getecomblvgmcustdetails(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecombkgnofrombl", tenant = "svm")
    public Map getecombkgnofrombl(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecomblvgmcontainers", tenant = "svm")
    public Map getecomblvgmcontainers(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecombkgvgmcontainers", tenant = "svm")
    public Map getecombkgvgmcontainers(@FP(name = "book_no") String book_no);

    @VDS(name = "getecomlrpvgmversion", tenant = "svm")
    public Map getecomlrpvgmversion(@FP(name = "book_no") String book_no, @FP(name = "eqpid") String eqpid);

    @VDS(name = "getecomvgmblnocheck", tenant = "svm")
    public Map<String, ArrayList<String>> getecomvgmblnocheck(@FP(name = "bl_no") String bl_no, @FP(name = "custid") String custid);
    
    @VDS(name = "getecomvgmblnocheck", tenant = "svm")
    public Map<String, ArrayList<String>> getecomvgmblnocheck(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecomvgmoriginagency", tenant = "svm")
    public Map getecomvgmoriginagency(@FP(name = "portcode") String portcode);

    @VDS(name = "getecomvgmagencymail", tenant = "svm")
    public Map getecomvgmagencymail(@FP(name = "agencycode") String agencycode);

    @VDS(name = "setecomvgmlrpversion", tenant = "svm")
    public Map setecomvgmlrpversion(VGMVOModel vgmSaveList);

    @VDS(name = "setecomvgmlrpdetails", tenant = "svm")
    public Map setecomvgmlrpdetails(VGMVOModel vgmSaveList);

    @VDS(name = "getecomvalidcontainer", tenant = "svm")
    public Map getecomvalidcontainer(@FP(name = "containerno", condition = Condition.IN) String containerno);

    @VDS(name = "getecomvgmtarewt", tenant = "svm")
    public Map getEcomTarewt(@FP(name = "containerno") String containerno, @FP(name = "equipmenttype") String equipmenttype);

    @VDS(name = "getecomvgmeqpdummy", tenant = "svm")
    public Map getEcomVgmEqpdummy(@FP(name = "book_no") String book_no);

    @VDS(name = "getecombkgagencymail", tenant = "svm")
    public Map getEcomVGMAgencyMail(@FP(name = "agencycode") String agencycode);
    
    @VDS(name = "getecomtareweightforsi", tenant = "svm")
    public Map getEcomTareWeightForSI(@FP(name = "containerno") String containerno);
    
     @VDS(name = "getecomtarewtfromeqp", tenant = "svm")
    public Map getEcomTarewtFromEqp(@FP(name = "equiptype") String equipmenttype);



}
