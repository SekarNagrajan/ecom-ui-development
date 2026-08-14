/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.lookup;

import com.svm.lrp.exp.croserviceSessionbean.croServiceSessionbean;
import com.svm.lrp.imp.getjasperinputs.ejb.getjasperinputsBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import lrp.documentation.bl.print.bean.BLPrintServiceSessionBean;
import lrp.documentation.mft.print.bean.ManifestPrintServiceSessionBean;
import net.ecom.AppTierCntrlBean.EcomBLBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomMaskBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAmendmentMailBeanRemote;
import net.ecom.AppTierCntrlBean.EcomNoticeBean;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.AppTierCntrlBean.EcomVGMBeanRemote;
import net.ecom.v1.cntlbean.EcomV1AmendmentBookingBeanRemote;
import net.ecom.v1.cntlbean.EcomV1BLBeanRemote;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.v1.cntlbean.EcomV1MasterBeanRemote;
import net.ecom.v1.cntlbean.EcomV1SIBeanRemote;
import net.ecom.v1.cntlbean.EcomV1VGMBeanRemote;
import svm.lrp.ecom.imp.bean.CreditControlPrintLocal;
import net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote;
import net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote;

public class EJBLookUp {

    public static getjasperinputsBeanLocal getEcomAVNPrintBeanLookUp() {
        InitialContext ctx;
        getjasperinputsBeanLocal ecomLookUpObj = null;
        if (ecomLookUpObj == null) {
            try {
                ctx = new InitialContext();
                ecomLookUpObj = (getjasperinputsBeanLocal) ctx.lookup(
                        "java:global/getjasperinputs/getjasperinputsBean!com.svm.lrp.imp.getjasperinputs.ejb.getjasperinputsBeanLocal");
            } catch (NamingException ex) {
                Logger.getLogger(EJBLookUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ecomLookUpObj;
    }

    public static BLPrintServiceSessionBean getEcomBLPrintBeanLookUp() {
        InitialContext ctx;
        BLPrintServiceSessionBean ecomLookUpObj = null;
        if (ecomLookUpObj == null) {
            try {
                ctx = new InitialContext();
                ecomLookUpObj = (BLPrintServiceSessionBean) ctx.lookup(
                        "java:global/BLPrintServiceEJB/BLPrintServiceSessionBean!lrp.documentation.bl.print.bean.BLPrintServiceSessionBean");
            } catch (NamingException ex) {
                Logger.getLogger(EJBLookUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ecomLookUpObj;
    }

    public static EComCntrlBeanRemote getEcomCntrlBeanLookUp() {
        InitialContext ctx;
        EComCntrlBeanRemote ecomLookUpObj = null;
        if (ecomLookUpObj == null) {
            try {
                ctx = new InitialContext();
                ecomLookUpObj = (EComCntrlBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EComCntrlBean!net.ecom.AppTierCntrlBean.EComCntrlBeanRemote");
            } catch (NamingException ex) {
                Logger.getLogger(EJBLookUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ecomLookUpObj;
    }

    public static EcomRegistrationBeanRemote getRegistrationBeanLookUp() {
        InitialContext ctx;
        EcomRegistrationBeanRemote registrationLookUpObj = null;
        if (registrationLookUpObj == null) {
            try {
                ctx = new InitialContext();
                registrationLookUpObj = (EcomRegistrationBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomRegistrationBean!net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return registrationLookUpObj;
    }

    public static EcomShippingInstructionBeanRemote getSIBeanRemoteLookUp() {
        InitialContext ctx;
        EcomShippingInstructionBeanRemote siLookUpObj = null;
        if (siLookUpObj == null) {
            try {
                ctx = new InitialContext();
                siLookUpObj = (EcomShippingInstructionBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomShippingInstructionBean!net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return siLookUpObj;
    }

    public static EcomBLBeanRemote getBLBeanLookUp() {
        InitialContext ctx;
        EcomBLBeanRemote blLookUpObj = null;
        if (blLookUpObj == null) {
            try {
                ctx = new InitialContext();
                blLookUpObj = (EcomBLBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomBLBean!net.ecom.AppTierCntrlBean.EcomBLBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return blLookUpObj;
    }

    public static EcomNoticeBean getNoticeBeanLookUp() {
        InitialContext ctx;
        EcomNoticeBean noticeLookUpObj = null;
        if (noticeLookUpObj == null) {
            try {
                ctx = new InitialContext();
                noticeLookUpObj = (EcomNoticeBean) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomNoticeBean!net.ecom.AppTierCntrlBean.EcomNoticeBean");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return noticeLookUpObj;
    }

    public static EcomBookingBeanRemote getBookingBeanLookUp() {
        InitialContext ctx;
        EcomBookingBeanRemote bookLookUpObj = null;
        if (bookLookUpObj == null) {
            try {
                ctx = new InitialContext();
                bookLookUpObj = (EcomBookingBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomBookingBean!net.ecom.AppTierCntrlBean.EcomBookingBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return bookLookUpObj;
    }

    public static EcomVGMBeanRemote getESLVGMBeanLookUp() {
        InitialContext ctx;
        EcomVGMBeanRemote vgmLookUpObj = null;
        if (vgmLookUpObj == null) {
            try {
                ctx = new InitialContext();
                vgmLookUpObj = (EcomVGMBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomVGMBean!net.ecom.AppTierCntrlBean.EcomVGMBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return vgmLookUpObj;
    }

    public static EcomAmendmentBookingBeanRemote getAmendmentBeanLookUp() {
        InitialContext ctx;
        EcomAmendmentBookingBeanRemote amendmentLookUpObj = null;
        if (amendmentLookUpObj == null) {
            try {
                ctx = new InitialContext();
                amendmentLookUpObj = (EcomAmendmentBookingBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomAmendmentBookingBean!net.ecom.AppTierCntrlBean.EcomAmendmentBookingBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return amendmentLookUpObj;
    }

    public static EcomPhase2BeanRemote getPhase2BeanLookUp() {
        InitialContext ctx;
        EcomPhase2BeanRemote croLookupObj = null;
        if (croLookupObj == null) {
            try {
                ctx = new InitialContext();
                croLookupObj = (EcomPhase2BeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomPhase2Bean!net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return croLookupObj;
    }

    public static EcomV1BookingBeanRemote getV1BKGBeanLookUp() {
        InitialContext ctx;
        EcomV1BookingBeanRemote v1BkgLookupObj = null;
        if (v1BkgLookupObj == null) {
            try {
                ctx = new InitialContext();
                v1BkgLookupObj = (EcomV1BookingBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomV1BookingBean!net.ecom.v1.cntlbean.EcomV1BookingBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return v1BkgLookupObj;
    }

    public static EcomV1SIBeanRemote getV1SIBeanLookUp() {
        InitialContext ctx;
        EcomV1SIBeanRemote v1SILookupObj = null;
        if (v1SILookupObj == null) {
            try {
                ctx = new InitialContext();
                v1SILookupObj = (EcomV1SIBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomV1SIBean!net.ecom.v1.cntlbean.EcomV1SIBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return v1SILookupObj;
    }

    public static EcomV1VGMBeanRemote getV1VGMBeanLookUp() {
        InitialContext ctx;
        EcomV1VGMBeanRemote v1VGMLookupObj = null;
        if (v1VGMLookupObj == null) {
            try {
                ctx = new InitialContext();
                v1VGMLookupObj = (EcomV1VGMBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomV1VGMBean!net.ecom.v1.cntlbean.EcomV1VGMBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return v1VGMLookupObj;
    }

    public static EcomV1MasterBeanRemote getV1MasterBeanLookUp() {
        InitialContext ctx;
        EcomV1MasterBeanRemote v1MasLookupObj = null;
        if (v1MasLookupObj == null) {
            try {
                ctx = new InitialContext();
                v1MasLookupObj = (EcomV1MasterBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomV1MasterBean!net.ecom.v1.cntlbean.EcomV1MasterBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return v1MasLookupObj;
    }

    public static EcomV1AmendmentBookingBeanRemote getV1AmendmentBKGLookUp() {
        InitialContext ctx;
        EcomV1AmendmentBookingBeanRemote v1AmendBkgLookupObj = null;
        if (v1AmendBkgLookupObj == null) {
            try {
                ctx = new InitialContext();
                v1AmendBkgLookupObj = (EcomV1AmendmentBookingBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomV1AmendmentBookingBean!net.ecom.v1.cntlbean.EcomV1AmendmentBookingBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return v1AmendBkgLookupObj;
    }

    public static EcomV1BLBeanRemote getV1BLBeanLookUp() {
        InitialContext ctx;
        EcomV1BLBeanRemote v1BLBeanLookupObj = null;
        if (v1BLBeanLookupObj == null) {
            try {
                ctx = new InitialContext();
                v1BLBeanLookupObj = (EcomV1BLBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomV1BLBean!net.ecom.v1.cntlbean.EcomV1BLBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return v1BLBeanLookupObj;
    }

    public static croServiceSessionbean getCROBeanLookUp() {
        InitialContext ctx;
        croServiceSessionbean croBeanLookUpObj = null;
        if (croBeanLookUpObj == null) {
            try {
                ctx = new InitialContext();
                croBeanLookUpObj = (croServiceSessionbean) ctx.lookup(
                        "java:global/CroServiceejb/Croserviceejb!com.svm.lrp.exp.croserviceSessionbean.croServiceSessionbean");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return croBeanLookUpObj;
    }

    public static EcomAdminCntrlBeanRemote getAdminBeanLookUp() {
        InitialContext ctx;
        EcomAdminCntrlBeanRemote adminLookUpObj = null;
        if (adminLookUpObj == null) {
            try {
                ctx = new InitialContext();
                adminLookUpObj = (EcomAdminCntrlBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomAdminCntrlBean!net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return adminLookUpObj;
    }

    public static CreditControlPrintLocal getCreditControlPrintLookUp() {
        InitialContext ctx;
        CreditControlPrintLocal creditcontrolLookUpObj = null;
        if (creditcontrolLookUpObj == null) {
            try {
                ctx = new InitialContext();
                creditcontrolLookUpObj = (CreditControlPrintLocal) ctx.lookup(
                        "java:global/CreditControlPrintService/CreditControlPrint!svm.lrp.ecom.imp.bean.CreditControlPrintLocal");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return creditcontrolLookUpObj;
    }

    public static ManifestPrintServiceSessionBean getManifestPrintServiceLookUp() {
        InitialContext ctx;
        ManifestPrintServiceSessionBean ManifestLookUpObj = null;
        if (ManifestLookUpObj == null) {
            try {
                ctx = new InitialContext();
                ManifestLookUpObj = (ManifestPrintServiceSessionBean) ctx.lookup(
                        "java:global/ManifestPrintServiceEJB/ManifestPrintServiceSessionBean!lrp.documentation.mft.print.bean.ManifestPrintServiceSessionBean");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return ManifestLookUpObj;
    }

    public static EcomAmendmentMailBeanRemote getEcomAmendmentMailLookUp() {
        InitialContext ctx;
        EcomAmendmentMailBeanRemote ecomAmendmentMailLookUpObj = null;
        if (ecomAmendmentMailLookUpObj == null) {
            try {
                ctx = new InitialContext();
                ecomAmendmentMailLookUpObj = (EcomAmendmentMailBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomAmendmentMailBean!net.ecom.AppTierCntrlBean.EcomAmendmentMailBeanRemote");

            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return ecomAmendmentMailLookUpObj;

    }

    public static EcomMaskBeanRemote getEcomMaskBeanLookUp() {
        InitialContext ctx;
        EcomMaskBeanRemote ecomMaskLookUpObj = null;
        if (ecomMaskLookUpObj == null) {
            try {
                ctx = new InitialContext();
                ecomMaskLookUpObj = (EcomMaskBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomMaskBean!net.ecom.AppTierCntrlBean.EcomMaskBeanRemote");
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return ecomMaskLookUpObj;
    }

    public static EcomAnalyticsBeanRemote getEcomAnalyticsBeanLookUp() {
        System.out.println("EJBLookUp.getEcomAnalyticsBeanLookUp() called");
        InitialContext ctx;
        EcomAnalyticsBeanRemote analyticsLookUpObj = null;
        if (analyticsLookUpObj == null) {
            try {
                System.out.println("Creating InitialContext for EcomAnalyticsBean lookup");
                ctx = new InitialContext();
                System.out.println(
                        "Looking up EcomAnalyticsBean with JNDI name: java:global/e-comOnline-EJBModule_New/EcomAnalyticsBean!net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote");
                analyticsLookUpObj = (EcomAnalyticsBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomAnalyticsBean!net.ecom.AppTierCntrlBean.EcomAnalyticsBeanRemote");
                System.out.println(
                        "EcomAnalyticsBean lookup successful: " + (analyticsLookUpObj != null ? "not null" : "null"));
            } catch (NamingException ex) {
                System.err.println("Error in EcomAnalyticsBean lookup: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return analyticsLookUpObj;
    }
    
    private static EcomOnlinePaymentBeanRemote paymentLookUpObj = null;
    
    public static EcomOnlinePaymentBeanRemote getEcomOnlinePaymentBean() {
        if (paymentLookUpObj == null) {
            try {
                InitialContext ctx = new InitialContext();
                paymentLookUpObj = (EcomOnlinePaymentBeanRemote) ctx.lookup(
                        "java:global/e-comOnline-EJBModule_New/EcomOnlinePaymentBean!net.ecom.AppTierCntrlBean.EcomOnlinePaymentBeanRemote");
                System.out.println(
                        "EcomOnlinePaymentBean lookup successful: " + (paymentLookUpObj != null ? "not null" : "null"));
            } catch (NamingException ex) {
                System.err.println("Error in EcomOnlinePaymentBean lookup: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return paymentLookUpObj;
    }
}
