/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class EcomBKGCommDG implements Serializable {

    @SerializedName("book_no")
    @Expose
    private String book_no;
    @SerializedName("eqpid")
    @Expose
    private String eqpid;
    @SerializedName("eqptype")
    @Expose
    private String eqptype;
    @SerializedName("unno")
    @Expose
    private String unno;
    @SerializedName("shippingname")
    @Expose
    private String shippingname;
    @SerializedName("imdgclass")
    @Expose
    private String imdgclass;
    @SerializedName("flashpoint")
    @Expose
    private String flashpoint;
    @SerializedName("flashunit")
    @Expose
    private String flashunit;
    @SerializedName("marinepollutant")
    @Expose
    private String marinepollutant;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;

    /**
     * @return the book_no
     */
    public String getBook_no() {
        return book_no;
    }

    /**
     * @param book_no the book_no to set
     */
    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    /**
     * @return the eqpid
     */
    public String getEqpid() {
        return eqpid;
    }

    /**
     * @param eqpid the eqpid to set
     */
    public void setEqpid(String eqpid) {
        this.eqpid = eqpid;
    }

    /**
     * @return the eqptype
     */
    public String getEqptype() {
        return eqptype;
    }

    /**
     * @param eqptype the eqptype to set
     */
    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    /**
     * @return the unno
     */
    public String getUnno() {
        return unno;
    }

    /**
     * @param unno the unno to set
     */
    public void setUnno(String unno) {
        this.unno = unno;
    }

    /**
     * @return the shippingname
     */
    public String getShippingname() {
        return shippingname;
    }

    /**
     * @param shippingname the shippingname to set
     */
    public void setShippingname(String shippingname) {
        this.shippingname = shippingname;
    }

    /**
     * @return the imdgclass
     */
    public String getImdgclass() {
        return imdgclass;
    }

    /**
     * @param imdgclass the imdgclass to set
     */
    public void setImdgclass(String imdgclass) {
        this.imdgclass = imdgclass;
    }

    /**
     * @return the flashpoint
     */
    public String getFlashpoint() {
        return flashpoint;
    }

    /**
     * @param flashpoint the flashpoint to set
     */
    public void setFlashpoint(String flashpoint) {
        this.flashpoint = flashpoint;
    }

    /**
     * @return the flashunit
     */
    public String getFlashunit() {
        return flashunit;
    }

    /**
     * @param flashunit the flashunit to set
     */
    public void setFlashunit(String flashunit) {
        this.flashunit = flashunit;
    }

    /**
     * @return the marinepollutant
     */
    public String getMarinepollutant() {
        return marinepollutant;
    }

    /**
     * @param marinepollutant the marinepollutant to set
     */
    public void setMarinepollutant(String marinepollutant) {
        this.marinepollutant = marinepollutant;
    }

    /**
     * @return the edireaderid
     */
    public String getEdireaderid() {
        return edireaderid;
    }

    /**
     * @param edireaderid the edireaderid to set
     */
    public void setEdireaderid(String edireaderid) {
        this.edireaderid = edireaderid;
    }

}
