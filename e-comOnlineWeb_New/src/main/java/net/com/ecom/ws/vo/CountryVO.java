/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.com.ecom.ws.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MasterData")
public class CountryVO {

  
    @XmlElement(name = "countrycode")
    private String countrycode;
    @XmlElement(name = "countryname")
    private String countryname;
    @XmlElement(name = "iSDCode")
    private String iSDCode = "";


    public String getCountrycode() {
        return countrycode;
    }


    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }


    public String getCountryname() {
        return countryname;
    }


    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }


    public String getiSDCode() {
        return iSDCode;
    }


    public void setiSDCode(String iSDCode) {
        this.iSDCode = iSDCode;
    }


}
