/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import java.io.Serializable;

/**
 *
 * @author sasikumar.r
 */
public class RouteVo implements Serializable {

    private String origin = null;
    private String orgmode = null;
    private String blpol = null;
    private String blpolter = null;
    private String blpolsvc = null;
    private String mode = null;
    private String opspol1 = null;
    private String opspol1ter = null;
    private String opspolsvc1 = null;
    private String opsmode1 = null;
    private String opspol2 = null;
    private String opspol2ter = null;
    private String opspolsvc2 = null;
    private String opsmode2 = null;
    private String opspol3 = null;
    private String opspol3ter = null;
    private String opspolsvc3 = null;
    private String opsmode3 = null;
    private String blpod = null;
    private String blpodter = null;
    private String delmode = null;
    private String delivery = null;
    private String mainsvc = null;
    private String defroute = null;
    private String id = null;
    private String opstrans1mde = null;
    private String opstrans2mde = null;

    private String originmode = null;
    private String pol = null;
    private String polter = null;
    private String polser = null;
    private String polmode = null;
    private String trans1 = null;
    private String trans1ter = null;
    private String trans1ser = null;
    private String trans1mode = null;
    private String trans2 = null;
    private String trans2ter = null;
    private String trans2ser = null;
    private String trans2mode = null;
    private String trans3 = null;
    private String trans3ter = null;
    private String trans3ser = null;
    private String trans3mode = null;
    private String pod = null;
    private String podter = null;
    private String podmode = null;
    private String svccode = null;
    //
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
     * @return the orgmode
     */
    public String getOrgmode() {
        return orgmode;
    }

    /**
     * @param orgmode the orgmode to set
     */
    public void setOrgmode(String orgmode) {
        this.orgmode = orgmode;
    }

    /**
     * @return the blpol
     */
    public String getBlpol() {
        return blpol;
    }

    /**
     * @param blpol the blpol to set
     */
    public void setBlpol(String blpol) {
        this.blpol = blpol;
    }

    /**
     * @return the blpolter
     */
    public String getBlpolter() {
        return blpolter;
    }

    /**
     * @param blpolter the blpolter to set
     */
    public void setBlpolter(String blpolter) {
        this.blpolter = blpolter;
    }

    /**
     * @return the blpolsvc
     */
    public String getBlpolsvc() {
        return blpolsvc;
    }

    /**
     * @param blpolsvc the blpolsvc to set
     */
    public void setBlpolsvc(String blpolsvc) {
        this.blpolsvc = blpolsvc;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the opspol1
     */
    public String getOpspol1() {
        return opspol1;
    }

    /**
     * @param opspol1 the opspol1 to set
     */
    public void setOpspol1(String opspol1) {
        this.opspol1 = opspol1;
    }

    /**
     * @return the opspol1ter
     */
    public String getOpspol1ter() {
        return opspol1ter;
    }

    /**
     * @param opspol1ter the opspol1ter to set
     */
    public void setOpspol1ter(String opspol1ter) {
        this.opspol1ter = opspol1ter;
    }

    /**
     * @return the opspolsvc1
     */
    public String getOpspolsvc1() {
        return opspolsvc1;
    }

    /**
     * @param opspolsvc1 the opspolsvc1 to set
     */
    public void setOpspolsvc1(String opspolsvc1) {
        this.opspolsvc1 = opspolsvc1;
    }

    /**
     * @return the opsmode1
     */
    public String getOpsmode1() {
        return opsmode1;
    }

    /**
     * @param opsmode1 the opsmode1 to set
     */
    public void setOpsmode1(String opsmode1) {
        this.opsmode1 = opsmode1;
    }

    /**
     * @return the opspol2
     */
    public String getOpspol2() {
        return opspol2;
    }

    /**
     * @param opspol2 the opspol2 to set
     */
    public void setOpspol2(String opspol2) {
        this.opspol2 = opspol2;
    }

    /**
     * @return the opspol2ter
     */
    public String getOpspol2ter() {
        return opspol2ter;
    }

    /**
     * @param opspol2ter the opspol2ter to set
     */
    public void setOpspol2ter(String opspol2ter) {
        this.opspol2ter = opspol2ter;
    }

    /**
     * @return the opspolsvc2
     */
    public String getOpspolsvc2() {
        return opspolsvc2;
    }

    /**
     * @param opspolsvc2 the opspolsvc2 to set
     */
    public void setOpspolsvc2(String opspolsvc2) {
        this.opspolsvc2 = opspolsvc2;
    }

    /**
     * @return the opsmode2
     */
    public String getOpsmode2() {
        return opsmode2;
    }

    /**
     * @param opsmode2 the opsmode2 to set
     */
    public void setOpsmode2(String opsmode2) {
        this.opsmode2 = opsmode2;
    }

    /**
     * @return the opspol3
     */
    public String getOpspol3() {
        return opspol3;
    }

    /**
     * @param opspol3 the opspol3 to set
     */
    public void setOpspol3(String opspol3) {
        this.opspol3 = opspol3;
    }

    /**
     * @return the opspol3ter
     */
    public String getOpspol3ter() {
        return opspol3ter;
    }

    /**
     * @param opspol3ter the opspol3ter to set
     */
    public void setOpspol3ter(String opspol3ter) {
        this.opspol3ter = opspol3ter;
    }

    /**
     * @return the opspolsvc3
     */
    public String getOpspolsvc3() {
        return opspolsvc3;
    }

    /**
     * @param opspolsvc3 the opspolsvc3 to set
     */
    public void setOpspolsvc3(String opspolsvc3) {
        this.opspolsvc3 = opspolsvc3;
    }

    /**
     * @return the opsmode3
     */
    public String getOpsmode3() {
        return opsmode3;
    }

    /**
     * @param opsmode3 the opsmode3 to set
     */
    public void setOpsmode3(String opsmode3) {
        this.opsmode3 = opsmode3;
    }

    /**
     * @return the blpod
     */
    public String getBlpod() {
        return blpod;
    }

    /**
     * @param blpod the blpod to set
     */
    public void setBlpod(String blpod) {
        this.blpod = blpod;
    }

    /**
     * @return the blpodter
     */
    public String getBlpodter() {
        return blpodter;
    }

    /**
     * @param blpodter the blpodter to set
     */
    public void setBlpodter(String blpodter) {
        this.blpodter = blpodter;
    }

    /**
     * @return the delmode
     */
    public String getDelmode() {
        return delmode;
    }

    /**
     * @param delmode the delmode to set
     */
    public void setDelmode(String delmode) {
        this.delmode = delmode;
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
     * @return the mainsvc
     */
    public String getMainsvc() {
        return mainsvc;
    }

    /**
     * @param mainsvc the mainsvc to set
     */
    public void setMainsvc(String mainsvc) {
        this.mainsvc = mainsvc;
    }

    /**
     * @return the defroute
     */
    public String getDefroute() {
        return defroute;
    }

    /**
     * @param defroute the defroute to set
     */
    public void setDefroute(String defroute) {
        this.defroute = defroute;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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

    public int getRouteid() {
        return routeid;
    }

    public void setRouteid(int routeid) {
        this.routeid = routeid;
    }
}
