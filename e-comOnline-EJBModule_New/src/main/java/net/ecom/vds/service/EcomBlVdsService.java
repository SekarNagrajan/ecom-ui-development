/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.service;

import com.svm.lrp.nfr.query.annotation.FP;
import com.svm.lrp.nfr.query.annotation.VDS;
import com.svm.lrp.nfr.query.bean.helper.Condition;
import java.util.Map;
import net.ecom.si.vo.CompleteModel;
import net.ecom.vds.serviceVo.EcomCommonModel;

/**
 *
 * @author salomi.a
 */
public interface EcomBlVdsService {

    @VDS(name = "getecomblcust", tenant = "svm")
    public CompleteModel getecomblcust(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecomblheader", tenant = "svm")
    public CompleteModel getecomblheader(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecomblcargo", tenant = "svm")
    public CompleteModel getecomblcargo(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecomblcommodity", tenant = "svm")
    public CompleteModel getecomblcommodity(@FP(name = "bl_no") String blNumber, @FP(name = "eqpId") String containerNum);

    @VDS(name = "getecomportdetforbl", tenant = "svm")
    public CompleteModel getecomportdetforbl(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecomportdetforbldisch", tenant = "svm")
    public CompleteModel getecomportdetforbldisch(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecombldetails", tenant = "svm")
    public CompleteModel getecombldetails(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecomblcargodetails", tenant = "svm")
    public CompleteModel getecomblcargodetails(@FP(name = "bl_no") String blNumber);

    @VDS(name = "setecomblacceptreference", tenant = "svm")
    public Map setEcomBLAcceptReference(EcomCommonModel paramEcomBKGCommonModel);

    @VDS(name = "getecomblheaderinfo", tenant = "svm")
    public Map getEcomBLHeaderInfo(@FP(name = "bl_no") String blNumber);

    @VDS(name = "getecomblreference", tenant = "svm")
    public Map getEcomBlReference(@FP(name = "bl_no") String bl_no, @FP(name = "refid", condition = Condition.IN) String refid);

    @VDS(name = "getecomblcharges", tenant = "svm")
    public Map getEcomBLCharges(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getecombkgnofrombl", tenant = "svm")
    public Map getecomstatusfrombl(@FP(name = "bl_no", condition = Condition.IN) String bl_no);

    @VDS(name = "getecomportdetforfcn", tenant = "svm")
    public CompleteModel getecomportdetformcn(@FP(name = "fcnno") String fcnno);

    @VDS(name = "getecomfcnblcust", tenant = "svm")
    public CompleteModel getecomfcncust(@FP(name = "fcnno") String fcnno);

    @VDS(name = "getecomfcnblcargodetails", tenant = "svm")
    public CompleteModel getecomfcncargodetails(@FP(name = "fcnno") String fcnno);

    @VDS(name = "getecomfcnblheader", tenant = "svm")
    public CompleteModel getecomfcnheader(@FP(name = "fcnno") String fcnno);

    @VDS(name = "getecomfcnreference", tenant = "svm")
    public Map getEcomFCNReference(@FP(name = "fcnno") String bl_no, @FP(name = "refid", condition = Condition.IN) String refid);

    @VDS(name = "getbloperationdetail", tenant = "svm")
    public Map getbloperationdetail(@FP(name = "bl_no") String bl_no);

    @VDS(name = "getagencyamendmentmailid", tenant = "svm")
    public Map getagencyamendmentmailid(@FP(name = "loginname") String loginname);

    @VDS(name = "getecommcnstatus", tenant = "svm")
    public Map getEcomMCNStatus(@FP(name = "fcnno") String fcnno);

    @VDS(name = "getecomblicsnew", tenant = "svm")
    public Map getEcomBLICSnew(@FP(name = "bl_no") String bl_no);
    
    @VDS(name = "getblchangesummaryview", tenant = "svm")
    public Map getblchangesummary(@FP(name = "bl_no") String bl_no);
    
    @VDS(name = "getblchargesummarylist", tenant = "svm")
    public Map getSIblchargesummarylist(@FP(name = "bl_no") String bl_no);
    
    @VDS(name = "getsichargesummarylist", tenant = "svm")
    public Map getSIchargesummarylist(@FP(name = "book_no") String book_no);
}
