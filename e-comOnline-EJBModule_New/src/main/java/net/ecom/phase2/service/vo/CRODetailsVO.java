/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.phase2.service.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author deepa.p
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CRODetails")
public class CRODetailsVO {

    @XmlElement(name = "bookNo")
    private String bookNo = null;
    @XmlElement(name = "releaseNo")
    private String releaseNo = null;
    @XmlElement(name = "croValidity")
    private String croValidity = null;
    @XmlElement(name = "loadPort")
    private String loadPort = null;
    @XmlElement(name = "dischargePort")
    private String dischargePort = null;
    @XmlElement(name = "contRelDate")
    private String contRelDate = null;
    @XmlElement(name = "arrivalDate")
    private String arrivalDate = null;
    @XmlElement(name = "departureDate")
    private String departureDate = null;
    @XmlElement(name = "contType")
    private String contType = null;
    @XmlElement(name = "qtyReleased")
    private String qtyReleased = null;
    @XmlElement(name = "remarks")
    private String remarks = null;

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getContRelDate() {
        return contRelDate;
    }

    public void setContRelDate(String contRelDate) {
        this.contRelDate = contRelDate;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getCroValidity() {
        return croValidity;
    }

    public void setCroValidity(String croValidity) {
        this.croValidity = croValidity;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDischargePort() {
        return dischargePort;
    }

    public void setDischargePort(String dischargePort) {
        this.dischargePort = dischargePort;
    }

    public String getLoadPort() {
        return loadPort;
    }

    public void setLoadPort(String loadPort) {
        this.loadPort = loadPort;
    }

    public String getQtyReleased() {
        return qtyReleased;
    }

    public void setQtyReleased(String qtyReleased) {
        this.qtyReleased = qtyReleased;
    }

    public String getReleaseNo() {
        return releaseNo;
    }

    public void setReleaseNo(String releaseNo) {
        this.releaseNo = releaseNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
