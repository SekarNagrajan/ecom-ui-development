/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.service;

import com.svm.lrp.nfr.query.annotation.FP;
import com.svm.lrp.nfr.query.annotation.VDS;
import java.util.Map;
import net.ecom.vds.serviceVo.EcomRegCommonModel;

/**
 * Fss
 *
 * @author deepa.p
 */
public interface EcomRegVDSService {

    @VDS(name = "getecomcheckcompany", tenant = "svm")
    public Map getEcomCheckCompany(@FP(name = "custcode") String custcode, @FP(name = "countryid") String countrycode);

    @VDS(name = "getecomcheckcompany", tenant = "svm")
    public Map getEcomCompanyMail(@FP(name = "custcode") String custcode);

    @VDS(name = "getecomcontactcount", tenant = "svm")
    public Map getEcomContactCount(@FP(name = "custcode") String custcode);

    @VDS(name = "setecomcustomercontacts", tenant = "svm")
    public Map setEcomCustomerContacts(EcomRegCommonModel model);

    @VDS(name = "getecomregagencymail", tenant = "svm")
    public Map getEcomRegAgencyMail(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomregagencymastermail", tenant = "svm")
    public Map getEcomRegAgencyMasterMail(@FP(name = "agencycode") String agencycode);

    @VDS(name = "getecomtimezonedetails", tenant = "svm")
    public Map getTimeZoneValues();
    
    @VDS(name = "getecomagencycontact", tenant = "svm")
    public Map getEcomRegAgencyContactMail(@FP(name = "agencycode") String agencycode);
    
     @VDS(name = "getportmultipleagencysetup", tenant = "svm")
     public Map getportmultipleagencysetup(@FP(name = "portcode") String portcode);

}
