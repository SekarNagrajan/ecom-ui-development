/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

/**
 *
 * @author subashini
 */
public class RegistrationVO implements Serializable {

    private String name = "";
    private int number = 0;
    private String nonuasccustomer = "";
    private String uasccustomer = "";
    private String cust_no = "";
    private String companyname = "";
    private String company_address = "";
    private String company_address1 = "";
    private String company_postcode = "";
    private String company_city = "";
    private String company_state = "";
    private String company_domain = "";
    private String company_country_code = "";
    private String company_phone_code = "";
    private String company_phone_no = "";
    private String company_mobile_code = "";
    private String company_mobile_no = "";
    private String firstname = "";
    private String lastname = "";
    private String cust_country_code = "";
    private String cust_phone_code = "";
    private String cust_phone_no = "";
    private String mobile_code = "";
    private String mobile_no = "";
    private String email = "";
    private String country = "";
    private String location = "";
    private String geneder = "";
    private String fax_country_code = "";
    private String fax_phone_code = "";
    private String fax_phone_no = "";
    private String city_desc = "";
    private String password = "";
    private String loginname = "";
    private String companyId = "";
    private String agencycode = "";
    private String countrycode = "";
    private String hiddencustname = "";
    private String savetype = "";
    private String companyDetails = "";
    private String deflanguage = "";
    private String preflanguage = "";
    private String company_email = "";
    private String agency_email = "";
    private String other_city = "";
    private String recent_bl = "";
    private String tax_id = "";
    private String contactperson = "";
    private String contact = "";
    private String othercitystatus = "";
    private String contact_title = "";
    private String ramdomid = "";
    private String customerno = "";
    private String langType = "";
    private String timezone = "";
    private String fieldname = "";
    private String mandatory = "";
    private String usertitle = "";

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getRamdomid() {
        return Validation.checkNull(ramdomid);
    }

    public void setRamdomid(String ramdomid) {
        this.ramdomid = ramdomid;
    }

    public String getTax_id() {
        return Validation.checkNull(tax_id);
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getContact_title() {
        return Validation.checkNull(contact_title);
    }

    public void setContact_title(String contact_title) {
        this.contact_title = contact_title;
    }

    public String getOthercitystatus() {
        return Validation.checkNull(othercitystatus);
    }

    public void setOthercitystatus(String othercitystatus) {
        this.othercitystatus = othercitystatus;
    }

    public String getContact() {
        return Validation.checkNull(contact);
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactperson() {
        return Validation.checkNull(contactperson);
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getRecent_bl() {
        return Validation.checkNull(recent_bl);
    }

    public void setRecent_bl(String recent_bl) {
        this.recent_bl = recent_bl;
    }

//    Validation val = new Validation();
    public RegistrationVO() {
    }

    /**
     * @return the name
     */
    public String getName() {

        return Validation.checkNull(name);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
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
     * @return the company_mobile_code
     */
    public String getCompany_mobile_code() {
        return Validation.checkNull(company_mobile_code);
    }

    /**
     * @param company_mobile_code the company_mobile_code to set
     */
    public void setCompany_mobile_code(String company_mobile_code) {
        this.company_mobile_code = company_mobile_code;
    }

    /**
     * @return the company_mobile_no
     */
    public String getCompany_mobile_no() {
        return Validation.checkNull(company_mobile_no);
    }

    /**
     * @param company_mobile_no the company_mobile_no to set
     */
    public void setCompany_mobile_no(String company_mobile_no) {
        this.company_mobile_no = company_mobile_no;
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
     * @return the companyDetails
     */
    public String getCompanyDetails() {
        return Validation.checkNull(companyDetails);
    }

    /**
     * @param companyDetails the companyDetails to set
     */
    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.company_address != null ? this.company_address.hashCode() : 0);
        hash = 31 * hash + (this.company_address1 != null ? this.company_address1.hashCode() : 0);
        hash = 31 * hash + (this.company_postcode != null ? this.company_postcode.hashCode() : 0);
        hash = 31 * hash + (this.company_state != null ? this.company_state.hashCode() : 0);
        hash = 31 * hash + (this.company_country_code != null ? this.company_country_code.hashCode() : 0);
        hash = 31 * hash + (this.company_phone_code != null ? this.company_phone_code.hashCode() : 0);
        hash = 31 * hash + (this.company_phone_no != null ? this.company_phone_no.hashCode() : 0);
        hash = 31 * hash + (this.city_desc != null ? this.city_desc.hashCode() : 0);
        hash = 31 * hash + (this.companyId != null ? this.companyId.hashCode() : 0);
        hash = 31 * hash + (this.agencycode != null ? this.agencycode.hashCode() : 0);
        hash = 31 * hash + (this.countrycode != null ? this.countrycode.hashCode() : 0);
        hash = 31 * hash + (this.hiddencustname != null ? this.hiddencustname.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegistrationVO other = (RegistrationVO) obj;
        if ((this.company_address == null) ? (other.company_address != null) : !this.company_address.equals(other.company_address)) {
            return false;
        }
        if ((this.company_address1 == null) ? (other.company_address1 != null) : !this.company_address1.equals(other.company_address1)) {
            return false;
        }
        if ((this.company_postcode == null) ? (other.company_postcode != null) : !this.company_postcode.equals(other.company_postcode)) {
            return false;
        }
        if ((this.company_state == null) ? (other.company_state != null) : !this.company_state.equals(other.company_state)) {
            return false;
        }
        if ((this.company_country_code == null) ? (other.company_country_code != null) : !this.company_country_code.equals(other.company_country_code)) {
            return false;
        }
        if ((this.company_phone_code == null) ? (other.company_phone_code != null) : !this.company_phone_code.equals(other.company_phone_code)) {
            return false;
        }
        if ((this.company_phone_no == null) ? (other.company_phone_no != null) : !this.company_phone_no.equals(other.company_phone_no)) {
            return false;
        }
        if ((this.city_desc == null) ? (other.city_desc != null) : !this.city_desc.equals(other.city_desc)) {
            return false;
        }
        if ((this.companyId == null) ? (other.companyId != null) : !this.companyId.equals(other.companyId)) {
            return false;
        }
        if ((this.agencycode == null) ? (other.agencycode != null) : !this.agencycode.equals(other.agencycode)) {
            return false;
        }
        if ((this.countrycode == null) ? (other.countrycode != null) : !this.countrycode.equals(other.countrycode)) {
            return false;
        }
        if ((this.hiddencustname == null) ? (other.hiddencustname != null) : !this.hiddencustname.equals(other.hiddencustname)) {
            return false;
        }
        return true;
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
     * @return the company_email
     */
    public String getCompany_email() {
        return company_email;
    }

    /**
     * @param company_email the company_email to set
     */
    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    /**
     * @return the agency_email
     */
    public String getAgency_email() {
        return agency_email;
    }

    /**
     * @param agency_email the agency_email to set
     */
    public void setAgency_email(String agency_email) {
        this.agency_email = agency_email;
    }

    /**
     * @return the other_city
     */
    public String getOther_city() {
        return Validation.checkNull(other_city);
    }

    /**
     * @param other_city the other_city to set
     */
    public void setOther_city(String other_city) {
        this.other_city = other_city;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getUsertitle() {
        return usertitle;
    }

    public void setUsertitle(String usertitle) {
        this.usertitle = usertitle;
    }

}
