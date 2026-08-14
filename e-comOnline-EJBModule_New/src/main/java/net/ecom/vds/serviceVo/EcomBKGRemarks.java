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
public class EcomBKGRemarks implements Serializable {

    //EdiTempBooking_Remarks
    @SerializedName("remarksref")
    @Expose
    private String remarksref;
    @SerializedName("remarksval")
    @Expose
    private String remarksval;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("book_no")
    @Expose
    private String book_no;

    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;
    @SerializedName("version")
    @Expose
    private String version;

    /**
     * @return the remarksref
     */
    public String getRemarksref() {
        return remarksref;
    }

    /**
     * @param remarksref the remarksref to set
     */
    public void setRemarksref(String remarksref) {
        this.remarksref = remarksref;
    }

    /**
     * @return the remarksval
     */
    public String getRemarksval() {
        return remarksval;
    }

    /**
     * @param remarksval the remarksval to set
     */
    public void setRemarksval(String remarksval) {
        this.remarksval = remarksval;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

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

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
