/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author salomi.a
 */
public class Booking_cust implements Serializable{

	@SerializedName("carriercode")
	@Expose
	private String carriercode;

	@SerializedName("address2")
	@Expose
	private String address2;

	@SerializedName("city")
	@Expose
	private String city;

	@SerializedName("address1")
	@Expose
	private String address1;

	@SerializedName("stateid")
	@Expose
	private String stateid;

	@SerializedName("isedited")
	@Expose
	private String isedited;

	@SerializedName("telephone")
	@Expose
	private String telephone;

	@SerializedName("book_no")
	@Expose
	private String book_no;

	@SerializedName("countryid")
	@Expose
	private String countryid;

	@SerializedName("zipcode")
	@Expose
	private String zipcode;

	@SerializedName("pobox")
	@Expose
	private String pobox;

	@SerializedName("contact")
	@Expose
	private String contact;

	@SerializedName("custid")
	@Expose
	private String custid;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("fax")
	@Expose
	private String fax;

	@SerializedName("email")
	@Expose
	private String email;

	@SerializedName("cid")
	@Expose
	private long cid;

	@SerializedName("custtypeid")
	@Expose
	private String custtypeid;

    @SerializedName("eori")
    @Expose
    private String eori;

    @SerializedName("persontype")
    @Expose
    private String persontype;

    public String getEori() {
        return eori;
    }

    public void setEori(String eori) {
        this.eori = eori;
    }

	public String getCarriercode() {
		return carriercode;
	}
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getStateid() {
		return stateid;
	}
	public void setStateid(String stateid) {
		this.stateid = stateid;
	}
	public String getIsedited() {
		return isedited;
	}
	public void setIsedited(String isedited) {
		this.isedited = isedited;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getBook_no() {
		return book_no;
	}
	public void setBook_no(String book_no) {
		this.book_no = book_no;
	}
	public String getCountryid() {
		return countryid;
	}
	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPobox() {
		return pobox;
	}
	public void setPobox(String pobox) {
		this.pobox = pobox;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public String getCusttypeid() {
		return custtypeid;
	}
	public void setCusttypeid(String custtypeid) {
		this.custtypeid = custtypeid;
	}
	public String getPersontype() {
		return persontype;
	}
	public void setPersontype(String persontype) {
		this.persontype = persontype;
	}
}
