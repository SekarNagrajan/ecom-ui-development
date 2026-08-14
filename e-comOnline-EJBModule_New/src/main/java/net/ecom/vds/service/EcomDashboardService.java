/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.service;

import com.svm.lrp.nfr.query.annotation.FP;
import com.svm.lrp.nfr.query.annotation.VDS;
import java.util.Map;

/**
 *
 * @author deepa.p
 */
public interface EcomDashboardService {

    @VDS(name = "getecomtopdestinationdashboard", tenant = "svm")
    public Map getEcomTopDestinationDashboard(@FP(name = "custid") String custCode);
    
    @VDS(name = "getecomtopportparidashboard", tenant = "svm")
    public Map getEcomTopPortPairDashboard(@FP(name = "custid") String custCode);
    
    @VDS(name = "getecomtopdestinationteudashbo", tenant = "svm")
    public Map getEcomTopDestinationTeuDashboard(@FP(name = "custid") String custCode);
}
