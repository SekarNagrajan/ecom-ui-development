package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

public class UserData implements Serializable {

    private int id = 0;
    private String loginName = "";
    private String firstName = "";
    private String userName = "";
    private String lastName = "";
    private String email = "";
    private String loginexpire = "";
    private String resData = "";
    private String systemType = "";
    private String usercode = "";
    private String usertype = "";
    private String userpreflanguage = "";
    private String roll = "";
    private String alloweduser = "";
    private String contantname = "";
    private String onlineactive = "";
    private String country = "";
    private String ccMailid = "";
    private String phone = "";
    private String admin = null;
    private String custtype = null;
    private String timezone = null;
    private String lastpwdup = null;
    private String status = null;
    private String isValid = null;

    private int wrgPsAtmpt = 0;
    private String lStatus = "";
    private String lTime = "";
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCcMailid() {
        return ccMailid;
    }

    public void setCcMailid(String ccMailid) {
        this.ccMailid = ccMailid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAlloweduser() {
        return alloweduser;
    }

    public void setAlloweduser(String alloweduser) {
        this.alloweduser = alloweduser;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return Validation.checkNull(loginName);
    }

    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return Validation.checkNull(firstName);
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return Validation.checkNull(lastName);
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the emil
     */
    public String getEmail() {
        return Validation.checkNull(email);
    }

    /**
     * @param emil the emil to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the loginexpire
     */
    public String getLoginexpire() {
        return Validation.checkNull(loginexpire);
    }

    /**
     * @param loginexpire the loginexpire to set
     */
    public void setLoginexpire(String loginexpire) {
        this.loginexpire = loginexpire;
    }

    /**
     * @return the resData
     */
    public String getResData() {
        return Validation.checkNull(resData);
    }

    /**
     * @param resData the resData to set
     */
    public void setResData(String resData) {
        this.resData = resData;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getSystemType() {
        return Validation.checkNull(systemType);
    }

    /**
     * @return the usercode
     */
    public String getUsercode() {
        return Validation.checkNull(usercode);
    }

    /**
     * @param usercode the usercode to set
     */
    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    /**
     * @return the usertype
     */
    public String getUsertype() {
        return Validation.checkNull(usertype);
    }

    /**
     * @param usertype the usertype to set
     */
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return Validation.checkNull(userName);
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userpreflanguage
     */
    public String getUserpreflanguage() {
        return Validation.checkNull(userpreflanguage);
    }

    /**
     * @param userpreflanguage the userpreflanguage to set
     */
    public void setUserpreflanguage(String userpreflanguage) {
        this.userpreflanguage = userpreflanguage;
    }

    /**
     * @return the onlineactive
     */
    public String getOnlineactive() {
        return onlineactive;
    }

    /**
     * @param onlineactive the onlineactive to set
     */
    public void setOnlineactive(String onlineactive) {
        this.onlineactive = onlineactive;
    }

    /**
     * @return the contantname
     */
    public String getContantname() {
        return contantname;
    }

    /**
     * @param contantname the contantname to set
     */
    public void setContantname(String contantname) {
        this.contantname = contantname;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCusttype() {
        return custtype;
    }

    public void setCusttype(String custtype) {
        this.custtype = custtype;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLastpwdup() {
        return lastpwdup;
    }

    public void setLastpwdup(String lastpwdup) {
        this.lastpwdup = lastpwdup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWrgPsAtmpt() {
        return wrgPsAtmpt;
    }

    public void setWrgPsAtmpt(int wrgPsAtmpt) {
        this.wrgPsAtmpt = wrgPsAtmpt;
    }

    public String getlStatus() {
        return lStatus;
    }

    public void setlStatus(String lStatus) {
        this.lStatus = lStatus;
    }

    public String getlTime() {
        return lTime;
    }

    public void setlTime(String lTime) {
        this.lTime = lTime;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
