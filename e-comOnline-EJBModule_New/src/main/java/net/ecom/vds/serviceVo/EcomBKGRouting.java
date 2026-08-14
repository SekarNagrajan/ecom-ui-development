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
public class EcomBKGRouting implements Serializable {

    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("originmode")
    @Expose
    private String originmode;
    @SerializedName("pol")
    @Expose
    private String pol;
    @SerializedName("polter")
    @Expose
    private String polter;
    @SerializedName("polser")
    @Expose
    private String polser;
    @SerializedName("polmode")
    @Expose
    private String polmode;
    @SerializedName("trans1")
    @Expose
    private String trans1;
    @SerializedName("trans1ter")
    @Expose
    private String trans1ter;
    @SerializedName("trans1ser")
    @Expose
    private String trans1ser;
    @SerializedName("trans1mode")
    @Expose
    private String trans1mode;
    @SerializedName("trans2")
    @Expose
    private String trans2;
    @SerializedName("trans2ter")
    @Expose
    private String trans2ter;
    @SerializedName("trans2ser")
    @Expose
    private String trans2ser;
    @SerializedName("trans2mode")
    @Expose
    private String trans2mode;
    @SerializedName("trans3")
    @Expose
    private String trans3;
    @SerializedName("trans3ter")
    @Expose
    private String trans3ter;
    @SerializedName("trans3ser")
    @Expose
    private String trans3ser;
    @SerializedName("trans3mode")
    @Expose
    private String trans3mode;
    @SerializedName("pod")
    @Expose
    private String pod;
    @SerializedName("podter")
    @Expose
    private String podter;
    @SerializedName("podmode")
    @Expose
    private String podmode;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("svccode")
    @Expose
    private String svccode;
    @SerializedName("book_no")
    @Expose
    private String book_no;
    @SerializedName("edireaderid")
    @Expose
    private String edireaderid;
    @SerializedName("defrout")
    @Expose
    private String defrout;
    @SerializedName("opstrans1mde")
    @Expose
    private String opstrans1mde;

    @SerializedName("opstrans2mde")
    @Expose
    private String opstrans2mde;

    @SerializedName("routeid")
    @Expose
    private int routeid;

    public String getOpstrans1mde() {
        return opstrans1mde;
    }

    public void setOpstrans1mde(String opstrans1mde) {
        this.opstrans1mde = opstrans1mde;
    }

    public String getOpstrans2mde() {
        return opstrans2mde;
    }

    public void setOpstrans2mde(String opstrans2mde) {
        this.opstrans2mde = opstrans2mde;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the originmode
     */
    public String getOriginmode() {
        return originmode;
    }

    /**
     * @param originmode the originmode to set
     */
    public void setOriginmode(String originmode) {
        this.originmode = originmode;
    }

    /**
     * @return the pol
     */
    public String getPol() {
        return pol;
    }

    /**
     * @param pol the pol to set
     */
    public void setPol(String pol) {
        this.pol = pol;
    }

    /**
     * @return the polter
     */
    public String getPolter() {
        return polter;
    }

    /**
     * @param polter the polter to set
     */
    public void setPolter(String polter) {
        this.polter = polter;
    }

    /**
     * @return the polser
     */
    public String getPolser() {
        return polser;
    }

    /**
     * @param polser the polser to set
     */
    public void setPolser(String polser) {
        this.polser = polser;
    }

    /**
     * @return the polmode
     */
    public String getPolmode() {
        return polmode;
    }

    /**
     * @param polmode the polmode to set
     */
    public void setPolmode(String polmode) {
        this.polmode = polmode;
    }

    /**
     * @return the trans1
     */
    public String getTrans1() {
        return trans1;
    }

    /**
     * @param trans1 the trans1 to set
     */
    public void setTrans1(String trans1) {
        this.trans1 = trans1;
    }

    /**
     * @return the trans1ter
     */
    public String getTrans1ter() {
        return trans1ter;
    }

    /**
     * @param trans1ter the trans1ter to set
     */
    public void setTrans1ter(String trans1ter) {
        this.trans1ter = trans1ter;
    }

    /**
     * @return the trans1ser
     */
    public String getTrans1ser() {
        return trans1ser;
    }

    /**
     * @param trans1ser the trans1ser to set
     */
    public void setTrans1ser(String trans1ser) {
        this.trans1ser = trans1ser;
    }

    /**
     * @return the trans1mode
     */
    public String getTrans1mode() {
        return trans1mode;
    }

    /**
     * @param trans1mode the trans1mode to set
     */
    public void setTrans1mode(String trans1mode) {
        this.trans1mode = trans1mode;
    }

    /**
     * @return the trans2
     */
    public String getTrans2() {
        return trans2;
    }

    /**
     * @param trans2 the trans2 to set
     */
    public void setTrans2(String trans2) {
        this.trans2 = trans2;
    }

    /**
     * @return the trans2ter
     */
    public String getTrans2ter() {
        return trans2ter;
    }

    /**
     * @param trans2ter the trans2ter to set
     */
    public void setTrans2ter(String trans2ter) {
        this.trans2ter = trans2ter;
    }

    /**
     * @return the trans2ser
     */
    public String getTrans2ser() {
        return trans2ser;
    }

    /**
     * @param trans2ser the trans2ser to set
     */
    public void setTrans2ser(String trans2ser) {
        this.trans2ser = trans2ser;
    }

    /**
     * @return the trans2mode
     */
    public String getTrans2mode() {
        return trans2mode;
    }

    /**
     * @param trans2mode the trans2mode to set
     */
    public void setTrans2mode(String trans2mode) {
        this.trans2mode = trans2mode;
    }

    /**
     * @return the trans3
     */
    public String getTrans3() {
        return trans3;
    }

    /**
     * @param trans3 the trans3 to set
     */
    public void setTrans3(String trans3) {
        this.trans3 = trans3;
    }

    /**
     * @return the trans3ter
     */
    public String getTrans3ter() {
        return trans3ter;
    }

    /**
     * @param trans3ter the trans3ter to set
     */
    public void setTrans3ter(String trans3ter) {
        this.trans3ter = trans3ter;
    }

    /**
     * @return the trans3ser
     */
    public String getTrans3ser() {
        return trans3ser;
    }

    /**
     * @param trans3ser the trans3ser to set
     */
    public void setTrans3ser(String trans3ser) {
        this.trans3ser = trans3ser;
    }

    /**
     * @return the trans3mode
     */
    public String getTrans3mode() {
        return trans3mode;
    }

    /**
     * @param trans3mode the trans3mode to set
     */
    public void setTrans3mode(String trans3mode) {
        this.trans3mode = trans3mode;
    }

    /**
     * @return the pod
     */
    public String getPod() {
        return pod;
    }

    /**
     * @param pod the pod to set
     */
    public void setPod(String pod) {
        this.pod = pod;
    }

    /**
     * @return the podter
     */
    public String getPodter() {
        return podter;
    }

    /**
     * @param podter the podter to set
     */
    public void setPodter(String podter) {
        this.podter = podter;
    }

    /**
     * @return the podmode
     */
    public String getPodmode() {
        return podmode;
    }

    /**
     * @param podmode the podmode to set
     */
    public void setPodmode(String podmode) {
        this.podmode = podmode;
    }

    /**
     * @return the delivery
     */
    public String getDelivery() {
        return delivery;
    }

    /**
     * @param delivery the delivery to set
     */
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    /**
     * @return the svccode
     */
    public String getSvccode() {
        return svccode;
    }

    /**
     * @param svccode the svccode to set
     */
    public void setSvccode(String svccode) {
        this.svccode = svccode;
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
     * @return the defRout
     */
    public String getDefrout() {
        return defrout;
    }

    /**
     * @param defRout the defRout to set
     */
    public void setDefrout(String defrout) {
        this.defrout = defrout;
    }

    public int getRouteid() {
        return routeid;
    }

    public void setRouteid(int routeid) {
        this.routeid = routeid;
    }

}
