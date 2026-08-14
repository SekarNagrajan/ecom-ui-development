/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomShippingInstructionBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.RemoveDuplicateValues;
import net.ecom.common.UserTimeZoneConversion;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.SIsummaryForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.SIsummaryVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShippingInstruction extends org.apache.struts.action.Action {

    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomShippingInstructionBeanRemote sibeanobj = EJBLookUp.getSIBeanRemoteLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomV1BookingBeanRemote v1BkgObj = EJBLookUp.getV1BKGBeanLookUp();
    Map stateMap = null, stateMapCountry = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext contexts = servlet.getServletContext();
        SIsummaryForm SIform = (SIsummaryForm) form;
        String SUCCESS = "success";
        RemoveDuplicateValues removeDup = new RemoveDuplicateValues();
        try {
            HttpSession session = request.getSession();
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            Hashtable contrylist = (Hashtable) contexts.getAttribute("SESSION_CONTRYLIST");
            stateMap = (Map) contexts.getAttribute("SESSION_STATEMAP");
            session.setAttribute("IS_ACTIVEPAGE", "SI");
            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
            strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim() : "NO";
            String strMenu = "YES";
            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                strMenu = "NO";
            } else {
                session.setAttribute("systemid", session.getId());  // session Id
            }
            session.setAttribute("IS_MENU_LOCK", strMenu);
            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
            String custcode = (String) session.getAttribute("SESSION_USERCODE");
            System.out.println("[ECOM-WAR] ShippingInstruction  custcodevalue" + custcode);
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String SICreForCnfrmBk = (String) contexts.getAttribute(GeneralConf.SI_CREATE_FOR_CONFIRMED_BOOKING);
            ArrayList SIdetails = sibeanobj.getSISummary(custcode, custTypeConfig, v1DataEnableStatus, SICreForCnfrmBk);
            String toEnableTimeZone = (String) contexts.getAttribute(GeneralConf.EnableTimeZone);
            System.out.println("[ECOM-WAR] ShippingInstruction  siENABLETIMEZONEVAlue=====>" + toEnableTimeZone);

            String preferredTimeZone = Validation.checkNull((String) session.getAttribute("USERPREFERREDTIMEZONE"));
            System.out.println("[ECOM-WAR] ShippingInstruction  sipreferredtimezoneValue=====>" + preferredTimeZone);
            Map<String, String> versionUTCMap = (Map<String, String>) contexts.getAttribute(GeneralConf.versionUTCValue);
            System.out.println("[ECOM-WAR] ShippingInstruction  siversionUTCMapValue=====>" + versionUTCMap);
            UserTimeZoneConversion obj = new UserTimeZoneConversion();

            if (SIdetails != null && SIdetails.size() > 0) {
                int len = SIdetails.size();
                for (int i = 0; i < len; i++) {
                    SIsummaryVO eSIBooking = (SIsummaryVO) SIdetails.get(i);
//                    if (eSIBooking.getDepdate().length() > 0) {
//                        eSIBooking.setDepdate(DateConversion_New.convertDefaultTimeFormat(eSIBooking.getDepdate()));
//                    }
                    //departure date
                    if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                        if (eSIBooking.getDepdate().length() > 0) {

                            String dateDept = eSIBooking.getDepdate();
                            Date resulttt = null;
                            SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());
                            resulttt = targettt.parse(dateDept);
                            System.out.println("[ECOM-WAR] ShippingInstruction  siStringtoDate========>" + resulttt);
                            String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                            System.out.println("[ECOM-WAR] ShippingInstruction  siuserTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                            System.out.println("[ECOM-WAR] ShippingInstruction  sideparture date before conversion" + eSIBooking.getDepdate());
                            if (userTimeZoneConverted.length() > 0) {
                                eSIBooking.setDepdate(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                            }
                        }
                    } else {
                        if (eSIBooking.getDepdate().length() > 0) {
                            eSIBooking.setDepdate(DateConversion_New.convertDefaultTimeFormat(eSIBooking.getDepdate()));
                        }
                    }
                    System.out.println("[ECOM-WAR] ShippingInstruction  sideparture date after conversion" + eSIBooking.getDepdate());
                    //departure date

                    //created date
                    if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                        if (eSIBooking.getDatecr().length() > 0) {

                            String dateCr = eSIBooking.getDatecr();
                            Date resulttt = null;
                            SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());
                            resulttt = targettt.parse(dateCr);
                            System.out.println("[ECOM-WAR] ShippingInstruction  siStringtoDate========>" + resulttt);
                            String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                            System.out.println("[ECOM-WAR] ShippingInstruction  siuserTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                            System.out.println("[ECOM-WAR] ShippingInstruction  sicreated date before conversion" + eSIBooking.getDatecr());
                            if (userTimeZoneConverted.length() > 0) {
                                eSIBooking.setDatecr(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                            }
                        }
                    } else {
                        if (eSIBooking.getDatecr().length() > 0) {
                            eSIBooking.setDatecr(DateConversion_New.convertDefaultTimeFormat(eSIBooking.getDatecr()));
                        }
                    }
                    System.out.println("[ECOM-WAR] ShippingInstruction  sicreated date after conversion" + eSIBooking.getDatecr());
                    //submitted date
                    if ("Y".equalsIgnoreCase(toEnableTimeZone) && preferredTimeZone.length() > 0) {
                        if (eSIBooking.getDateup().length() > 0) {

                            String dateUp = eSIBooking.getDateup();
                            Date resulttt = null;
                            SimpleDateFormat targettt = new SimpleDateFormat(GeneralConf.getStrDbFormat());
                            resulttt = targettt.parse(dateUp);
                            System.out.println("[ECOM-WAR] ShippingInstruction  siStringtoDate========>" + resulttt);
                            String userTimeZoneConverted = obj.getUserDateFormat(resulttt, preferredTimeZone, versionUTCMap);
                            System.out.println("[ECOM-WAR] ShippingInstruction  siuserTimeZoneConvertedfromejb" + userTimeZoneConverted);

//
                            System.out.println("[ECOM-WAR] ShippingInstruction  sisubmitted before conversion" + eSIBooking.getDateup());
                            if (userTimeZoneConverted.length() > 0) {
                                eSIBooking.setDateup(DateConversion_New.convertDefaultTimeFormat(userTimeZoneConverted));
                            }
                        } else {
                            if (eSIBooking.getDatecr().length() > 0) {
                                eSIBooking.setDateup(eSIBooking.getDatecr());

                            }
                        }
                    } else {
                        if (eSIBooking.getDateup().length() > 0) {
                            eSIBooking.setDateup(DateConversion_New.convertDefaultTimeFormat(eSIBooking.getDateup()));
                        } else {
                            if (eSIBooking.getDatecr().length() > 0) {
                                eSIBooking.setDateup(eSIBooking.getDatecr());
                            }
                        }
                    }

                    System.out.println("[ECOM-WAR] ShippingInstruction  sisubmitted date after conversion" + eSIBooking.getDateup());

                    // submitted date
                    SIdetails.set(i, eSIBooking);
                    eSIBooking = null;
                }
            }
            String custType = null;
            CustomerDetailsVO custVO = null;
            List shList = null;
            List cnList = null;
            List ntList = null;
            List apList = null;
            List fwList = null;
            List whList = null;
            List slList = null;
            List byList = null;
            List finalCustList = null;
            List v1CustList = null;
            List custList = bookingObj.getMoreCustomerDetails(custcode, custTypeConfig);
            finalCustList = custList;
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                v1CustList = v1BkgObj.getV1MoreCustomerDetails(custcode, "si");
                if (v1CustList != null && custList != null) {
                    finalCustList = (List) Stream.concat(custList.stream(), v1CustList.stream()).collect(Collectors.toList());
                }
            }
            if (finalCustList != null) {
                shList = new ArrayList();
                cnList = new ArrayList();
                ntList = new ArrayList();
                apList = new ArrayList();
                fwList = new ArrayList();
                whList = new ArrayList();
                slList = new ArrayList();
                byList = new ArrayList();
                String hideFaxno = (String) contexts.getAttribute(GeneralConf.HideFaxNo);

                for (int i = 0; i < finalCustList.size(); i++) {
                    custVO = (CustomerDetailsVO) finalCustList.get(i);
                    custType = Validation.checkNull(custVO.getCustType());
                    custVO.setState(concatStateName(custVO.getState(), custVO.getCountry()));
                    if (custType.equalsIgnoreCase("AP")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        apList.add(custVO);
                    } else if (custType.equalsIgnoreCase("SH")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        shList.add(custVO);
                    } else if (custType.equalsIgnoreCase("CN")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        cnList.add(custVO);
                    } else if (custType.equalsIgnoreCase("NT") || custType.equalsIgnoreCase("N1") || custType.equalsIgnoreCase("N2")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        ntList.add(custVO);
                    } else if (custType.equalsIgnoreCase("FW")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        fwList.add(custVO);
                    } else if (custType.equalsIgnoreCase("WH")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        whList.add(custVO);
                    } else if (custType.equalsIgnoreCase("SL")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        slList.add(custVO);
                    } else if (custType.equalsIgnoreCase("BY")) {
                        custVO.setCountry(Validation.checkNull(custVO.getCountry()) + " - " + Validation.checkNull((String) contrylist.get(Validation.checkNull(custVO.getCountry()))));
                        if ("Y".equalsIgnoreCase(hideFaxno)) {
                            custVO.setFax("");
                        }
                        byList.add(custVO);
                    }

                }
            }
            //remove notify duplicate 
            ntList = removeDup.removeNotifyDuplicate(ntList);
            //
            session.setAttribute("CUSTPARTIESDET_SI", custList);
            session.setAttribute("APCUSTLIST_SI", apList);
            session.setAttribute("SHCUSTLIST_SI", shList);
            session.setAttribute("CNCUSTLIST_SI", cnList);
            session.setAttribute("NTCUSTLIST_SI", ntList);
            session.setAttribute("FWCUSTLIST_SI", fwList);
            session.setAttribute("WHCUSTLIST_SI", whList);
            session.setAttribute("BYCUSTLIST_SI", byList);
            session.setAttribute("SLCUSTLIST_SI", slList);

            session.setAttribute("SIdetails", SIdetails);
            session.setAttribute("NEWDESIGNPRODUCT", "shippinginstruction");
            SIform.setResults(SIdetails);
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] ShippingInstruction  ====>" + e);
            e.printStackTrace();
        }
        return mapping.findForward("success");
    }

    public String concatStateName(String state, String country) {
        try {
            stateMapCountry = (Map) stateMap.get(Validation.checkNull(country).toUpperCase());
            if (state != null && state.length() > 0 && stateMapCountry != null) {
                state = state + " - " + Validation.checkNull((String) stateMapCountry.get(state));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Validation.checkNull(state);
    }
}
