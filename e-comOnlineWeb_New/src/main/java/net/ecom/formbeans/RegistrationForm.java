/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.formbeans;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.common.Validation;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author prabakarans
 */
public class RegistrationForm extends org.apache.struts.validator.ValidatorForm {

    private String name;
    private int number;
    private String nonuasccustomer;
    private String uasccustomer;
    private String cust_no;
    private String companyname = "";
    private String company_address;
    private String company_address1;
    private String company_postcode;
    private String company_city;
    private String company_state;
    private String company_domain ="";
    private String company_country_code;
    private String company_phone_code;
    private String company_phone_no;
    private String firstname;
    private String usertitle;
    private String lastname;
    private String cust_country_code;
    private String cust_phone_code;
    private String cust_phone_no;
    private String mobile_code;
    private String mobile_no;
    private String email;
    private String country;
    private String location;
    private String geneder;
    private String fax_country_code;
    private String fax_phone_code;
    private String fax_phone_no;
    private String others;
    private String city_others;
    private String city_desc;
    private String agree;
    private String password;
    private String loginname;
    private String companyId = "";
    private String agencycode = "";
    private String countrycode = "";
    private String hiddencustname = "";
    private String savetype = "";
    private String deflanguage = "";
    private String preflanguage = "";
    private String cpassword = "";
    private String captcha = "";
    private String hiddenEmail = "";
    private String comp_email = "";
    private String customerno="";

    private String other_city = "";
    private String recent_bl="";
    private String tax_id="";
    private String contactperson="";
    private String contact="";

    private String othercitystatus="";
    private String contact_title="";
    private String ramdomid="";
    private String hdncity="";
    private String timezone="";
    private String locationt="";
    private String company_mobile_code="";
    private String company_mobile_no="";

    public String getLocationt() {
        return locationt;
    }

    public void setLocationt(String locationt) {
        this.locationt = locationt;
    }


    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    

    public String getHdnagency() {
        return Validation.checkNull(hdnagency);
    }

    public void setHdnagency(String hdnagency) {
        this.hdnagency = hdnagency;
    }

    public String getHdncity() {
        return Validation.checkNull(hdncity);
    }

    public void setHdncity(String hdncity) {
        this.hdncity = hdncity;
    }
    private String hdnagency="";

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_title() {
        return contact_title;
    }

    public void setContact_title(String contact_title) {
        this.contact_title = contact_title;
    }

    public String getContactperson() {
        return Validation.checkNull(contactperson);
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getOther_city() {
        return other_city;
    }

    public void setOther_city(String other_city) {
        this.other_city = other_city;
    }

    public String getOthercitystatus() {
        return othercitystatus;
    }

    public void setOthercitystatus(String othercitystatus) {
        this.othercitystatus = othercitystatus;
    }

    public String getRamdomid() {
        return ramdomid;
    }

    public void setRamdomid(String ramdomid) {
        this.ramdomid = ramdomid;
    }

    public String getRecent_bl() {
        return Validation.checkNull(recent_bl);
    }

    public void setRecent_bl(String recent_bl) {
        this.recent_bl = recent_bl;
    }

    public String getTax_id() {
        return Validation.checkNull(tax_id);
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getComp_email() {
        return comp_email;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public void setComp_email(String comp_email) {
        this.comp_email = comp_email;
    }

    public String getHiddenEmail() {
        return hiddenEmail;
    }

    public void setHiddenEmail(String hiddenEmail) {
        this.hiddenEmail = hiddenEmail;
    }

    /**
     * @return
     */
    public String getName() {
        return Validation.checkNull(name);
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param i
     */
    public void setNumber(int i) {
        number = i;
    }

    /**
     *
     */
    public RegistrationForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ActionErrors errors = new ActionErrors();
        EcomRegistrationBeanRemote ecomCntrlObj = EJBLookUp.getRegistrationBeanLookUp();
       String pagereq = (String) session.getAttribute("RegValidate");

        /*  if (getCompanyname().length() < 1) { PraBaKaRaN.S 28/04/2011 for Amended Compnay Profile
        errors.add("er_companyname", new ActionMessage("error.reg.companyname"));
        } */
        if (getHiddencustname().length() < 1) {
            errors.add("er_companyname", new ActionMessage("error.reg.companyname"));
        }
        if (getCompany_address().length() < 1) {
//            errors.add("er_company_address", new ActionMessage("error.reg.company_address"));
        }
//        if (getCompany_postcode().length() < 1) {
//            errors.add("er_company_postcode", new ActionMessage("error.reg.company_postcode"));
//        } else {
//            String postalCode = getCompany_postcode();
//
//            if (postalCode.length() > 0) {
//                if (!Pattern.matches("[0-9]{1,20}", postalCode)) {
//                    errors.add("er_company_postcode", new ActionMessage("error.reg.company_postcode_Number"));
//                }
//                if (!isAllZero(postalCode)) {
////                    errors.add("Postal/Zip Code should not contain all Zero");
//                    errors.add("er_company_postcode", new ActionMessage("error.reg.company_postcode_Zero"));
//                } else if (postalCode.length() == 0) {
//                    errors.add("er_company_postcode", new ActionMessage("error.reg.company_postcode_empty"));
//                }
//            }
//
//        }

    /*    if (getCompany_country_code().length() < 1) {
            errors.add("er_company_country_code", new ActionMessage("error.reg.countrycode_empty"));
        } else {
            String code = getCompany_country_code();
            if (code.length() > 0) {
                if (!Pattern.matches("[0-9]{1,4}", code)) {
                    errors.add("er_company_country_phone", new ActionMessage("error.reg.countrycode"));
                }
                if (!isAllZero(code)) {

                    errors.add("er_company_country_phone", new ActionMessage("error.reg.countrycode_zero"));
                } else if (code.length() == 0) {
                    errors.add("er_company_country_phone", new ActionMessage("error.reg.countrycode_empty"));
                }
            }
        }
        if (getCompany_phone_code().length() < 1) {
          //  errors.add("er_company_phone_code", new ActionMessage("error.reg.areacode_empty"));
        } else {
            String AreaCode = getCompany_phone_code();
            if (AreaCode.length() > 0) {
                if (!Pattern.matches("[0-9]{1,4}", AreaCode)) {
         //           errors.add("er_company_phone_code", new ActionMessage("error.reg.areacode"));
                }
                if (!isAllZero(AreaCode)) {
//                    errors.add("Postal/Zip Code should not contain all Zero");
                    errors.add("er_company_phone_code", new ActionMessage("error.reg.areacode_zero"));
                } else if (AreaCode.length() == 0) {
                 //   errors.add("er_company_phone_code", new ActionMessage("error.reg.areacode_empty"));
                }
            }

        }
        if (getCompany_phone_no().length() < 1) {
            errors.add("er_company_phone_no", new ActionMessage("error.reg.phone_empty"));
        } else {
            String Phoneno = getCompany_phone_no();
            if (Phoneno.length() > 0) {
                if (!Pattern.matches("[0-9]{1,25}", Phoneno)) {
                    errors.add("er_company_phone_no", new ActionMessage("error.reg.phone_Number"));
                }
                if (!isAllZero(Phoneno)) {
//                    errors.add("Postal/Zip Code should not contain all Zero");
                    errors.add("er_company_phone_no", new ActionMessage("error.reg.phone_zero"));
                } else if (Phoneno.length() == 0) {
                    errors.add("er_company_phone_no", new ActionMessage("error.reg.phone_empty"));
                }
            }
        }*/
        if (getFirstname().length() < 1) {
            errors.add("er_firstname", new ActionMessage("error.reg.firstname"));
        }
        if (getLastname().length() < 1) {
            errors.add("er_lastname", new ActionMessage("error.reg.lastname"));
        }
        if (getCust_country_code().length() < 1) {
            //errors.add("er_cust_country_code", new ActionMessage("error.reg.countrycode_empty"));
        } else {
            String code = getCust_country_code();
            if (code.length() > 0) {
                if (!Pattern.matches("[0-9]{1,5}", code)) {
                    //errors.add("er_cust_country_code", new ActionMessage("error.reg.countrycode"));
                }
                if (!isAllZero(code)) {
//                    errors.add("Postal/Zip Code should not contain all Zero");
                    //errors.add("er_cust_country_code", new ActionMessage("error.reg.countrycode_zero"));
                } else if (code.length() == 0) {
                    //errors.add("er_cust_country_code", new ActionMessage("error.reg.countrycode_empty"));
                }
            }
        }
        if (getCust_phone_code().length() < 1) {
       //     errors.add("er_cust_phone_code", new ActionMessage("error.reg.areacode_empty"));
        } else {
            String AreaCode = getCust_phone_code();
            if (AreaCode.length() > 0) {
                if (!Pattern.matches("[0-9]{1,4}", AreaCode)) {
          //          errors.add("er_cust_phone_code", new ActionMessage("error.reg.areacode"));
                }
                if (!isAllZero(AreaCode)) {

                    errors.add("er_cust_phone_code", new ActionMessage("error.reg.areacode_zero"));
                } else if (AreaCode.length() == 0) {
           //         errors.add("er_cust_phone_code", new ActionMessage("error.reg.areacode_empty"));
                }
            }
        }
        if (getCust_phone_no().length() < 1) {
            //errors.add("er_cust_phone_no", new ActionMessage("error.reg.phone_empty"));
        } else {
            String Phoneno = getCust_phone_no();
            if (Phoneno.length() > 0) {
                if (!Pattern.matches("[0-9]{1,25}", Phoneno)) {
                    errors.add("er_cust_phone_no", new ActionMessage("error.reg.phone_Number"));
                }
                if (!isAllZero(Phoneno)) {

                   // errors.add("er_cust_phone_no", new ActionMessage("error.reg.phone_zero"));
                } else if (Phoneno.length() == 0) {
                   // errors.add("er_cust_phone_no", new ActionMessage("error.reg.phone_empty"));
                }
            }
        }

        if (getLoginname().length() < 1) {
            errors.add("er_email", new ActionMessage("error.reg.email"));
        } else {
            String emailid = getLoginname();
            boolean check = true;

            // System.out.println("[ECOM-WAR] RegistrationForm ==>" + Pattern.matches("[a-zA-Z0-9][a-zA-Z0-9\\.]{1,100}[a-zA-Z0-9]", emailid));
//            if (!Pattern.matches("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$", emailid)) {
//                errors.add("er_email", new ActionMessage("error.reg.InvalidUserId"));
//                check = false;
//            }

            char[] charEmal = emailid.toCharArray();
            int emailCount = 0;
            if (charEmal.length > 0) {
                int emailLen = charEmal.length;
                for (int eL = 0; eL < emailLen; eL++) {
                    if (eL != 0) {
                        char charVal = charEmal[eL];
                        char preVal = charEmal[eL - 1];
                        if ((charVal == '.' && preVal == '.') || (charVal == '_' && preVal == '_') || (charVal == '_' && preVal == '.') || (charVal == '.' && preVal == '_') || (charVal == '-' && preVal == '-') || (charVal == '.' && preVal == '-') || (charVal == '-' && preVal == '.') || (charVal == '-' && preVal == '_') || (charVal == '_' && preVal == '-')) {
                            emailCount = emailCount + 1;
                        }
                    }
                }
            }
            if (emailCount > 0 && check) {
                errors.add("er_email", new ActionMessage("error.reg.consecutive"));
                check = false;
            }

//            if (check) {
//                try {
//                    String result = ecomCntrlObj.CheckUserEmailId(emailid);
//                    if (result == null) {
//                        errors.add("er_email", new ActionMessage("error.reg.email_Already"));
//                    }
//                } catch (Exception ex) {
//                    Logger.getLogger(RegistrationForm.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
        }
        /*  if (getCountry().length() < 1) { PraBaKaRaN.S on 28/04/2011
        errors.add("er_country", new ActionMessage("error.reg.country"));

        } else {
        country = getCountry();
        } */
        if (getCountrycode().length() < 1) {
            errors.add("er_country", new ActionMessage("error.reg.country"));

        } else {
            country = getCountrycode();
        }
        /* if (getLocation().length() < 1) { PraBaKaRaN.S on 28/04/2011
        errors.add("er_location", new ActionMessage("error.reg.location"));
        } */
     //   if (getAgencycode().length() < 1) {
       //     errors.add("er_location", new ActionMessage("error.reg.location"));
       // }

        if (getCity_desc().length() < 1) {
          //  errors.add("er_city_desc", new ActionMessage("error.reg.city_desc"));
        }

        String mobileno = getMobile_code().trim();
        if (mobileno.length() > 0) {
            if (!Pattern.matches("[0-9]{1,5}", mobileno)) {
                errors.add("er_Mobilecode", new ActionMessage("error.reg.Moblie_Number"));
            }
            if (!isAllZero(mobileno)) {

                errors.add("er_Mobilecode", new ActionMessage("error.reg.Mobile_zero"));
            } else if (mobileno.length() == 0) {
                errors.add("er_Mobilecode", new ActionMessage("error.reg.Mobile_empty"));
            }

        }
        mobileno = getMobile_no().trim();
        if (mobileno.length() > 0) {
            if (!Pattern.matches("[0-9]{1,25}", mobileno)) {
                errors.add("er_Mobileno", new ActionMessage("error.reg.phone_Number"));
            }
            if (!isAllZero(mobileno)) {

                //errors.add("er_Mobileno", new ActionMessage("error.reg.phone_zero"));
            } else if (mobileno.length() == 0) {
                //errors.add("er_Mobileno", new ActionMessage("error.reg.phone_empty"));
            }
        }
        if (getFax_country_code().length() > 0) {
            String code = getFax_country_code();
            if (code.length() > 0 && code.length() <= 8) {
                if (!Pattern.matches("[0-9]{1,4}", code)) {
                    errors.add("er_fax_country_code", new ActionMessage("error.reg.countrycode"));
                }
                if (!isAllZero(code)) {
//                    errors.add("Postal/Zip Code should not contain all Zero");
                    errors.add("er_fax_country_code", new ActionMessage("error.reg.countrycode_zero"));
                } else if (code.length() == 0) {
                    errors.add("er_fax_country_code", new ActionMessage("error.reg.countrycode_empty"));
                }
            }

        }
        if (getFax_phone_code().length() > 0) {
            String AreaCode = getFax_phone_code();
            if (AreaCode.length() > 0 && AreaCode.length() <= 8) {
                if (!Pattern.matches("[0-9]{1,4}", AreaCode)) {
             //       errors.add("er_fax_phone_code", new ActionMessage("error.reg.areacode"));
                }
                if (!isAllZero(AreaCode)) {
                    errors.add("er_fax_phone_code", new ActionMessage("error.reg.areacode_zero"));
                } else if (AreaCode.length() == 0) {
           //         errors.add("er_fax_phone_code", new ActionMessage("error.reg.areacode_empty"));
                }
            }
        }
        if (getFax_phone_no().trim().length() > 0) {
            String Phoneno = getFax_phone_no();
            if (Phoneno.length() > 0 && Phoneno.length() <= 8) {
                if (!Pattern.matches("[0-9]{1,25}", Phoneno)) {
                    errors.add("er_fax_phone_no", new ActionMessage("error.reg.phone_Number"));
                }
                if (!isAllZero(Phoneno)) {

                    errors.add("er_fax_phone_no", new ActionMessage("error.reg.phone_zero"));
                } else if (Phoneno.length() == 0) {
                    errors.add("er_fax_phone_no", new ActionMessage("error.reg.phone_empty"));
                }
            }
        }
        if (!(getPassword().trim().length() > 0)) {
            errors.add("er_password", new ActionMessage("error.reg.password"));
        }

       if(pagereq!=null && !pagereq.equalsIgnoreCase("Saved")) {
        if ((getLoginname().trim().length() > 0)) {
            boolean check = true;
            String id = getLoginname().trim();
            /*
           if (!Pattern.matches("^[a-z A-Z 0-9][a-z A-Z 0-9]*[\\._]{0,1}[a-z A-Z 0-9]*[\\._]{0,1}[a-z A-Z 0-9]*[a-z A-Z 0-9]$", id)) 
                {
                    errors.add("er_loginname", new ActionMessage("error.reg.InvalidId"));
                    check = false;
                }
            */
         
//            System.out.println("[ECOM-WAR] RegistrationForm check--"+check);
            if (check) {
                try {
                    String result = Validation.checkNull(ecomCntrlObj.CheckUserId(id));
                    String allowexistingUser = (String) request.getServletContext().getAttribute(GeneralConf.ALLOW_EXISTING_USER_TO_REGISTER);
                    if (result!=null && "Exists".equalsIgnoreCase(result) && (!"Y".equalsIgnoreCase(allowexistingUser))) {
                        
                        errors.add("er_loginname", new ActionMessage("error.reg.userid_alreadyexists"));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(RegistrationForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            errors.add("er_loginname", new ActionMessage("error.reg.loginname"));
        }
       }
//        if (getCompany_domain().length() > 0) {
//            String domain = getCompany_domain();
//            String addr = "";
//            String one = "";
//            String two = "";
//            boolean check = false;
//            // 26/09/2011 said by mushtaq
//            if (domain.startsWith("http://www.")) {
//                addr = domain.substring(domain.lastIndexOf(".") + 1, domain.length());
//                if (addr.length() >= 2) {
////                    System.out.println("[ECOM-WAR] RegistrationForm check " + check);
//                    check = true;
//                }
//            }
//        }
        if (getCountrycode().length() > 0) {
            String countrycode = getCountrycode();
            try {
                String result = Validation.checkNull((String) ecomCntrlObj.checkcountry(countrycode));
                if (!(result.length() > 0)) {
                    errors.add("er_country", new ActionMessage("error.reg.country_invalid"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (getCompany_state().length() > 0) {
            String statecode = getCompany_state();
            try {
                String result = Validation.checkNull((String) ecomCntrlObj.checkState(statecode));
                if (!(result.length() > 0)) {
                    errors.add("er_state", new ActionMessage("error.reg.state_invalid"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (getAgencycode().length() > 0) {
            String agencycode = getAgencycode();
       
        }
//*****************************************************//Added By Gnanamoorthi.S
        if (!(getCpassword().trim().length() > 0)) {
            errors.add("er_cpassword", new ActionMessage("error.reg.cpassword"));
        }

        if (getPassword().trim().length() > 0 && getCpassword().trim().length() > 0) {
            String strpassword = getPassword().trim();
            String strCpassword = getCpassword().trim();

            if (strpassword.equals(strCpassword)) {
            } else {
                request.setAttribute("getError", "sss");
                errors.add("er_cpasswordmismatch", new ActionMessage("error.reg.cpassword_mismatch"));
            }
        }
        //*****************************************************//Added By Gnanamoorthi.S
        if (errors != null && errors.size() > 0) {
            this.setCompanyname(hiddencustname);
            String term = "";
            try {
                term = Validation.checkNull(ecomCntrlObj.getTerms("Registration"));
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            request.setAttribute("TERMS", term);
        }
        if(errors!=null && errors.size()>0 && errors.size()<10){
          session.setAttribute("regerrors","regerrors");
        }else{
            session.setAttribute("regerrors","");
        }

        if(errors!=null && errors.size()>=10){
          //  session.setAttribute("urlhit","urlhit");
        }

        return errors;
    }

    /**
     * @return the nonuasccustomer
     */
    public String getNonuasccustomer() {
        return Validation.checkNull(nonuasccustomer);
    }

    /**
     * @param nonuasccustomer the nonuasccustomer to set
     */
    public void setNonuasccustomer(String nonuasccustomer) {
        this.nonuasccustomer = nonuasccustomer;
    }

    /**
     * @return the uasccustomer
     */
    public String getUasccustomer() {
        return Validation.checkNull(uasccustomer);
    }

    /**
     * @param uasccustomer the uasccustomer to set
     */
    public void setUasccustomer(String uasccustomer) {
        this.uasccustomer = uasccustomer;
    }

    /**
     * @return the cust_no
     */
    public String getCust_no() {
        return Validation.checkNull(cust_no);
    }

    /**
     * @param cust_no the cust_no to set
     */
    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    /**
     * @return the companyname
     */
    public String getCompanyname() {
        return Validation.checkNull(companyname);
    }

    /**
     * @param companyname the companyname to set
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    /**
     * @return the company_address
     */
    public String getCompany_address() {
        return Validation.checkNull(company_address);
    }

    /**
     * @param company_address the company_address to set
     */
    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    /**
     * @return the company_address1
     */
    public String getCompany_address1() {
        return Validation.checkNull(company_address1);
    }

    /**
     * @param company_address1 the company_address1 to set
     */
    public void setCompany_address1(String company_address1) {
        this.company_address1 = company_address1;
    }

    /**
     * @return the company_postcode
     */
    public String getCompany_postcode() {
        return Validation.checkNull(company_postcode);
    }

    /**
     * @param company_postcode the company_postcode to set
     */
    public void setCompany_postcode(String company_postcode) {
        this.company_postcode = company_postcode;
    }

    /**
     * @return the company_city
     */
    public String getCompany_city() {
        return Validation.checkNull(company_city);
    }

    /**
     * @param company_city the company_city to set
     */
    public void setCompany_city(String company_city) {
        this.company_city = company_city;
    }

    /**
     * @return the company_state
     */
    public String getCompany_state() {
        return Validation.checkNull(company_state);
    }

    /**
     * @param company_state the company_state to set
     */
    public void setCompany_state(String company_state) {
        this.company_state = company_state;
    }

    /**
     * @return the company_domain
     */
    public String getCompany_domain() {
        return Validation.checkNull(company_domain);
    }

    /**
     * @param company_domain the company_domain to set
     */
    public void setCompany_domain(String company_domain) {
        this.company_domain = company_domain;
    }

    /**
     * @return the company_country_code
     */
    public String getCompany_country_code() {
        return Validation.checkNull(company_country_code);
    }

    /**
     * @param company_country_code the company_country_code to set
     */
    public void setCompany_country_code(String company_country_code) {
        this.company_country_code = company_country_code;
    }

    /**
     * @return the company_phone_code
     */
    public String getCompany_phone_code() {
        return Validation.checkNull(company_phone_code);
    }

    /**
     * @param company_phone_code the company_phone_code to set
     */
    public void setCompany_phone_code(String company_phone_code) {
        this.company_phone_code = company_phone_code;
    }

    /**
     * @return the company_phone_no
     */
    public String getCompany_phone_no() {
        return Validation.checkNull(company_phone_no);
    }

    /**
     * @param company_phone_no the company_phone_no to set
     */
    public void setCompany_phone_no(String company_phone_no) {
        this.company_phone_no = company_phone_no;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return Validation.checkNull(firstname);
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return Validation.checkNull(lastname);
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the cust_country_code
     */
    public String getCust_country_code() {
        return Validation.checkNull(cust_country_code);
    }

    /**
     * @param cust_country_code the cust_country_code to set
     */
    public void setCust_country_code(String cust_country_code) {
        this.cust_country_code = cust_country_code;
    }

    /**
     * @return the cust_phone_code
     */
    public String getCust_phone_code() {
        return Validation.checkNull(cust_phone_code);
    }

    /**
     * @param cust_phone_code the cust_phone_code to set
     */
    public void setCust_phone_code(String cust_phone_code) {
        this.cust_phone_code = cust_phone_code;
    }

    /**
     * @return the cust_phone_no
     */
    public String getCust_phone_no() {
        return Validation.checkNull(cust_phone_no);
    }

    /**
     * @param cust_phone_no the cust_phone_no to set
     */
    public void setCust_phone_no(String cust_phone_no) {
        this.cust_phone_no = cust_phone_no;
    }

    /**
     * @return the mobile_code
     */
    public String getMobile_code() {
        return Validation.checkNull(mobile_code);
    }

    /**
     * @param mobile_code the mobile_code to set
     */
    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }

    /**
     * @return the mobile_no
     */
    public String getMobile_no() {
        return Validation.checkNull(mobile_no);
    }

    /**
     * @param mobile_no the mobile_no to set
     */
    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return Validation.checkNull(email);
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return Validation.checkNull(country);
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return Validation.checkNull(location);
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the geneder
     */
    public String getGeneder() {
        return Validation.checkNull(geneder);
    }

    /**
     * @param geneder the geneder to set
     */
    public void setGeneder(String geneder) {
        this.geneder = geneder;
    }

    /**
     * @return the fax_country_code
     */
    public String getFax_country_code() {
        return Validation.checkNull(fax_country_code);
    }

    /**
     * @param fax_country_code the fax_country_code to set
     */
    public void setFax_country_code(String fax_country_code) {
        this.fax_country_code = fax_country_code;
    }

    /**
     * @return the fax_phone_code
     */
    public String getFax_phone_code() {
        return Validation.checkNull(fax_phone_code);
    }

    /**
     * @param fax_phone_code the fax_phone_code to set
     */
    public void setFax_phone_code(String fax_phone_code) {
        this.fax_phone_code = fax_phone_code;
    }

    /**
     * @return the fax_phone_no
     */
    public String getFax_phone_no() {
        return Validation.checkNull(fax_phone_no);
    }

    /**
     * @param fax_phone_no the fax_phone_no to set
     */
    public void setFax_phone_no(String fax_phone_no) {
        this.fax_phone_no = fax_phone_no;
    }

    /**
     * @return the others
     */
    public String getOthers() {
        return Validation.checkNull(others);
    }

    /**
     * @param others the others to set
     */
    public void setOthers(String others) {
        this.others = others;
    }

    /**
     * @return the city_others
     */
    public String getCity_others() {
        return Validation.checkNull(city_others);
    }

    /**
     * @param city_others the city_others to set
     */
    public void setCity_others(String city_others) {
        this.city_others = city_others;
    }

    /**
     * @return the city_desc
     */
    public String getCity_desc() {
        return Validation.checkNull(city_desc);
    }

    /**
     * @param city_desc the city_desc to set
     */
    public void setCity_desc(String city_desc) {
        this.city_desc = city_desc;
    }

    /**
     * @return the agree
     */
    public String getAgree() {
        return Validation.checkNull(agree);
    }

    /**
     * @param agree the agree to set
     */
    public void setAgree(String agree) {
        this.agree = agree;
    }

    private boolean isAllZero(String postalCode) {
        boolean value = true;
        try {
            int count = 0;
            int len = postalCode.length();
            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    char charVal = postalCode.charAt(i);
                    if (charVal == '0') {
                        count = count + 1;
                    }
                }
            }
            if (count == len) {
                value = false;
            }
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return Validation.checkNull(password);
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the loginname
     */
    public String getLoginname() {
        return Validation.checkNull(loginname);
    }

    /**
     * @param loginname the loginname to set
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return Validation.checkNull(companyId);
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the agencycode
     */
    public String getAgencycode() {
        return Validation.checkNull(agencycode);
    }

    /**
     * @param agencycode the agencycode to set
     */
    public void setAgencycode(String agencycode) {
        this.agencycode = agencycode;
    }

    /**
     * @return the countrycode
     */
    public String getCountrycode() {
        return Validation.checkNull(countrycode);
    }

    /**
     * @param countrycode the countrycode to set
     */
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    /**
     * @return the hiddencustname
     */
    public String getHiddencustname() {
        return Validation.checkNull(hiddencustname);
    }

    /**
     * @param hiddencustname the hiddencustname to set
     */
    public void setHiddencustname(String hiddencustname) {
        this.hiddencustname = hiddencustname;
    }

    /**
     * @return the savetype
     */
    public String getSavetype() {
        return Validation.checkNull(savetype);
    }

    /**
     * @param savetype the savetype to set
     */
    public void setSavetype(String savetype) {
        this.savetype = savetype;
    }

    /**
     * @return the deflanguage
     */
    public String getDeflanguage() {
        return deflanguage;
    }

    /**
     * @param deflanguage the deflanguage to set
     */
    public void setDeflanguage(String deflanguage) {
        this.deflanguage = deflanguage;
    }

    /**
     * @return the preflanguage
     */
    public String getPreflanguage() {
        return preflanguage;
    }

    /**
     * @param preflanguage the preflanguage to set
     */
    public void setPreflanguage(String preflanguage) {
        this.preflanguage = preflanguage;
    }

    /**
     * @return the cpassword
     */
    public String getCpassword() {
        return cpassword;
    }

    /**
     * @param cpassword the cpassword to set
     */
    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    /**
     * @return the captcha
     */
    public String getCaptcha() {
        return captcha;
    }

    /**
     * @param captcha the captcha to set
     */
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getUsertitle() {
        return usertitle;
    }

    public void setUsertitle(String usertitle) {
        this.usertitle = usertitle;
    }

    public String getCompany_mobile_code() {
        return Validation.checkNull(company_mobile_code);
    }

    public void setCompany_mobile_code(String company_mobile_code) {
        this.company_mobile_code = company_mobile_code;
    }
    
    public String getCompany_mobile_no() {
        return Validation.checkNull(company_mobile_no);
    }

    public void setCompany_mobile_no(String company_mobile_no) {
        this.company_mobile_no = company_mobile_no;
    }
}
