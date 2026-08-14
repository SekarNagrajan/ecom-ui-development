package net.ecom.vds.service;

import com.svm.json.SvmJsonList;
import com.svm.json.SvmJsonObject;
import com.svm.lrp.nfr.query.annotation.VDS;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sasikumar.r
 */
public interface EcomBKGSpsService {

    @VDS(name = "getecomcustfrombooking", tenant = "svm")
    public HashMap getecomcustfrombooking(SvmJsonList paramSvmJsonObject);

    @VDS(name = "getecombkgcustomersearch", tenant = "svm")
    public HashMap getecomBookingCustomerSearch(SvmJsonList paramSvmJsonObject);

    //for validat
    @VDS(name = "putecombkgstatusupdate", tenant = "svm") // for booking/SI/BL status update
    public Map putbookingstatuupdate(SvmJsonObject paramSvmJsonObject);

    @VDS(name = "putecomcustomeracceptence", tenant = "svm")//For customer master accept
    public Map putecomcustomeracceptence(SvmJsonObject paramSvmJsonObject);

    @VDS(name = "putecomcustomerreject", tenant = "svm")//For customer reject
    public Map putecomcustomerreject(SvmJsonObject paramSvmJsonObject);

    @VDS(name = "putecomcustomerresendlink", tenant = "svm")// For resend the link
    public Map putecomcustomerresendlink(SvmJsonObject paramSvmJsonObject);

    @VDS(name = "putecomcustomerroledetails", tenant = "svm") //To update the role
    public Map putecomcustomerroledetails(SvmJsonObject paramSvmJsonObject);
    
    @VDS(name = "getecomlrpbookings", tenant = "svm")
    public HashMap getEcomLRPBookings(SvmJsonList paramSvmJsonObject);

}
