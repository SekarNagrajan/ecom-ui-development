package net.ecom.vo;

import java.io.Serializable;
import net.ecom.common.Validation;

public class Route implements Serializable {

    /**
     * Creates a new instance of Route
     */
    public Route() {
    }
    public String origin = "";
    public String orginTer = "";
    public String originVen = "";
    public String blpol = "";
    public String blpolTer = "";
    public String blpolSVC = "";
    public String blpolMode = "";
    public String trans1 = "";
    public String trans1Ter = "";
    public String trans1Mode = "";
    public String trans1SVC = "";
    public String trans2 = "";
    public String trans2Ter = "";
    public String trans2Mode = "";
    public String trans2SVC = "";
    public String trans3 = "";
    public String trans3Ter = "";
    public String trans3Mode = "";
    public String trans3SVC = "";
    public String blpod = "";
    public String blpodTer = "";
    public String blpodSVC = "";
    public String blopodMode = "";
    public String delivery = "";
    public String deliveryTer = "";
    public String defRoute = "";
    public String mainSVC = "";
    public String hauMode = "";
    public String disHauMode = "";
    public String route = "";
    public String pol = "";
    public String pod = "";
    public String id = "";
    public String vessel = "";
    public String voyage = "";
    public String bound = "";
    public String dischargebound = "";
    public String port = "";
    public String terminal = "";
    public String gateInDate = "";
    public String arrdate = "";
    public String service = "";
    public String grpCallId = "";
    public int legNo = 0;
    public String legType = "";
    public int itemno = 0;
    public String loadport = "";
    public String disport = "";
    public String loadter = "";
    public String dister = "";
    public String ETD1 = "";
    public String ETA1 = "";
    public String serviceName = "";
    public String vesselName = "";
    public String polPortName = "";
    public String podPortName = "";
    public int preLeg = 0;
    public String preCarriage = "";
    public String onCarriage = "";
    public String input = "";
    public String routeId = "";
    public String targetdate = "";
    public boolean isSchedulePresent = false;
    public String routing_id = "";
    public String feeder = "";
    public String loadtype = "";
    public String Disctype = "";
    public String Lordporttype = "";
    public String Disporttype = "";
    public String Legtypename = "";
    public String loadcallid = "";
    public String dischargecallid = "";
    public String cutoff_polterminal = "";
    public String cutoff_polarrdate = "";
    public String legtype="";
    public String opstrans1mode="";
    public String opstrans2mode="";
    private int transhipment = 0;
    private String disPortDepDate =null;
    private String closingtime=null;
    private boolean isRouting=false;
    private String depdatefornexttrans=null;
    private String poltranstime;
    private String podtranstime;

    public String getOpstrans1mode() {
        return opstrans1mode;
    }

    public void setOpstrans1mode(String opstrans1mode) {
        this.opstrans1mode = opstrans1mode;
    }

    public String getOpstrans2mode() {
        return opstrans2mode;
    }

    public void setOpstrans2mode(String opstrans2mode) {
        this.opstrans2mode = opstrans2mode;
    }
    
    public String getLegtype() {
        return legtype;
    }

    public void setLegtype(String legtype) {
        this.legtype = legtype;
    }

    Validation val = new Validation();

    public String getOrigin() {
        return val.checkNull(origin);
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrginTer() {
        return val.checkNull(orginTer);
    }

    public void setOrginTer(String orginTer) {
        this.orginTer = orginTer;
    }

    public String getOriginVen() {
        return val.checkNull(originVen);
    }

    public void setOriginVen(String originVen) {
        this.originVen = originVen;
    }

    public String getBlpol() {
        return val.checkNull(blpol);
    }

    public void setBlpol(String blpol) {
        this.blpol = blpol;
    }

    public String getBlpolTer() {
        return val.checkNull(blpolTer);
    }

    public void setBlpolTer(String blpolTer) {
        this.blpolTer = blpolTer;
    }

    public String getBlpolSVC() {
        return val.checkNull(blpolSVC);
    }

    public void setBlpolSVC(String blpolSVC) {
        this.blpolSVC = blpolSVC;
    }

    public String getBlpolMode() {
        return val.checkNull(blpolMode);
    }

    public void setBlpolMode(String blpolMode) {
        this.blpolMode = blpolMode;
    }

    public String getTrans1() {
        return val.checkNull(trans1);
    }

    public void setTrans1(String trans1) {
        this.trans1 = trans1;
    }

    public String getTrans1Ter() {
        return val.checkNull(trans1Ter);
    }

    public void setTrans1Ter(String trans1Ter) {
        this.trans1Ter = trans1Ter;
    }

    public String getTrans1Mode() {
        return val.checkNull(trans1Mode);
    }

    public void setTrans1Mode(String trans1Mode) {
        this.trans1Mode = trans1Mode;
    }

    public String getTrans1SVC() {
        return val.checkNull(trans1SVC);
    }

    public void setTrans1SVC(String trans1SVC) {
        this.trans1SVC = trans1SVC;
    }

    public String getTrans2() {
        return val.checkNull(trans2);
    }

    public void setTrans2(String trans2) {
        this.trans2 = trans2;
    }

    public String getTrans2Ter() {
        return val.checkNull(trans2Ter);
    }

    public void setTrans2Ter(String trans2Ter) {
        this.trans2Ter = trans2Ter;
    }

    public String getTrans2Mode() {
        return val.checkNull(trans2Mode);
    }

    public void setTrans2Mode(String trans2Mode) {
        this.trans2Mode = trans2Mode;
    }

    public String getTrans2SVC() {
        return val.checkNull(trans2SVC);
    }

    public void setTrans2SVC(String trans2SVC) {
        this.trans2SVC = trans2SVC;
    }

    public String getTrans3() {
        return val.checkNull(trans3);
    }

    public void setTrans3(String trans3) {
        this.trans3 = trans3;
    }

    public String getTrans3Ter() {
        return val.checkNull(trans3Ter);
    }

    public void setTrans3Ter(String trans3Ter) {
        this.trans3Ter = trans3Ter;
    }

    public String getTrans3Mode() {
        return val.checkNull(trans3Mode);
    }

    public void setTrans3Mode(String trans3Mode) {
        this.trans3Mode = trans3Mode;
    }

    public String getTrans3SVC() {
        return val.checkNull(trans3SVC);
    }

    public void setTrans3SVC(String trans3SVC) {
        this.trans3SVC = trans3SVC;
    }

    public String getBlpod() {
        return val.checkNull(blpod);
    }

    public void setBlpod(String blpod) {
        this.blpod = blpod;
    }

    public String getBlpodTer() {
        return val.checkNull(blpodTer);
    }

    public void setBlpodTer(String blpodTer) {
        this.blpodTer = blpodTer;
    }

    public String getBlpodSVC() {
        return val.checkNull(blpodSVC);
    }

    public void setBlpodSVC(String blpodSVC) {
        this.blpodSVC = blpodSVC;
    }

    public String getBlopodMode() {
        return val.checkNull(blopodMode);
    }

    public void setBlopodMode(String blopodMode) {
        this.blopodMode = blopodMode;
    }

    public String getDelivery() {
        return val.checkNull(delivery);
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryTer() {
        return val.checkNull(deliveryTer);
    }

    public void setDeliveryTer(String deliveryTer) {
        this.deliveryTer = deliveryTer;
    }

    public String getDefRoute() {
        return val.checkNull(defRoute);
    }

    public void setDefRoute(String defRoute) {
        this.defRoute = defRoute;
    }

    public String getMainSVC() {
        return val.checkNull(mainSVC);
    }

    public void setMainSVC(String mainSVC) {
        this.mainSVC = mainSVC;
    }

    public String getHauMode() {
        return val.checkNull(hauMode);
    }

    public void setHauMode(String hauMode) {
        this.hauMode = hauMode;
    }

    public String getDisHauMode() {
        return val.checkNull(disHauMode);
    }

    public void setDisHauMode(String disHauMode) {
        this.disHauMode = disHauMode;
    }

    public String getRoute() {
        return val.checkNull(route);
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPol() {
        return val.checkNull(pol);
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return val.checkNull(pod);
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getId() {
        return val.checkNull(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVessel() {
        return val.checkNull(vessel);
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getVoyage() {
        return val.checkNull(voyage);
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getBound() {
        return val.checkNull(bound);
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getPort() {
        return val.checkNull(port);
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTerminal() {
        return val.checkNull(terminal);
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getGateInDate() {
        return val.checkNull(gateInDate);
    }

    public void setGateInDate(String gateInDate) {
        this.gateInDate = gateInDate;
    }

    public String getArrdate() {
        return val.checkNull(arrdate);
    }

    public void setArrdate(String arrdate) {
        this.arrdate = arrdate;
    }

    public String getService() {
        return val.checkNull(service);
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getGrpCallId() {
        return val.checkNull(grpCallId);
    }

    public void setGrpCallId(String grpCallId) {
        this.grpCallId = grpCallId;
    }

    public boolean isIsSchedulePresent() {
        return isSchedulePresent;
    }

    public void setIsSchedulePresent(boolean isSchedulePresent) {
        this.isSchedulePresent = isSchedulePresent;
    }

    public int getLegNo() {
        return legNo;
    }

    public void setLegNo(int legNo) {
        this.legNo = legNo;
    }

    public String getLegType() {
        return val.checkNull(legType);
    }

    public void setLegType(String legType) {
        this.legType = legType;
    }

    public int getItemno() {
        return itemno;
    }

    public void setItemno(int itemno) {
        this.itemno = itemno;
    }

    public String getLoadport() {
        return val.checkNull(loadport);
    }

    public void setLoadport(String loadport) {
        this.loadport = loadport;
    }

    public String getDisport() {
        return val.checkNull(disport);
    }

    public void setDisport(String disport) {
        this.disport = disport;
    }

    public String getLoadter() {
        return val.checkNull(loadter);
    }

    public void setLoadter(String loadter) {
        this.loadter = loadter;
    }

    public String getDister() {
        return val.checkNull(dister);
    }

    public void setDister(String dister) {
        this.dister = dister;
    }

    public String getETD1() {
        return val.checkNull(ETD1);
    }

    public void setETD1(String ETD1) {
        this.ETD1 = ETD1;
    }

    public String getETA1() {
        return val.checkNull(ETA1);
    }

    public void setETA1(String ETA1) {
        this.ETA1 = ETA1;
    }

    public String getServiceName() {
        return val.checkNull(serviceName);
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVesselName() {
        return val.checkNull(vesselName);
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getPolPortName() {
        return val.checkNull(polPortName);
    }

    public void setPolPortName(String polPortName) {
        this.polPortName = polPortName;
    }

    public String getPodPortName() {
        return val.checkNull(podPortName);
    }

    public void setPodPortName(String podPortName) {
        this.podPortName = podPortName;
    }

    public int getPreLeg() {
        return preLeg;
    }

    public void setPreLeg(int preLeg) {
        this.preLeg = preLeg;
    }

    public String getPreCarriage() {
        return val.checkNull(preCarriage);
    }

    public void setPreCarriage(String preCarriage) {
        this.preCarriage = preCarriage;
    }

    public String getOnCarriage() {
        return val.checkNull(onCarriage);
    }

    public void setOnCarriage(String onCarriage) {
        this.onCarriage = onCarriage;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getRouteId() {
        return val.checkNull(routeId);
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    /**
     * @return the targetdate
     */
    public String getTargetdate() {
        return val.checkNull(targetdate);
    }

    /**
     * @param targetdate the targetdate to set
     */
    public void setTargetdate(String targetdate) {
        this.targetdate = targetdate;
    }

    /**
     * @return the routing_id
     */
    public String getRouting_id() {
        return val.checkNull(routing_id);
    }

    /**
     * @param routing_id the routing_id to set
     */
    public void setRouting_id(String routing_id) {
        this.routing_id = routing_id;
    }

    /* Added by KB for Routing */
    public String getFeeder() {
        return val.checkNull(feeder);
    }

    /**
     * @param routing_id the routing_id to set
     */
    public void setFeeder(String feeder) {
        this.feeder = feeder;
    }

    /**
     * @return the loadtype
     */
    public String getLoadtype() {
        return loadtype;
    }

    /**
     * @param loadtype the loadtype to set
     */
    public void setLoadtype(String loadtype) {
        this.loadtype = loadtype;
    }

    /**
     * @return the Disctype
     */
    public String getDisctype() {
        return Disctype;
    }

    /**
     * @param Disctype the Disctype to set
     */
    public void setDisctype(String Disctype) {
        this.Disctype = Disctype;
    }

    /**
     * @return the Lordporttype
     */
    public String getLordporttype() {
        return Lordporttype;
    }

    /**
     * @param Lordporttype the Lordporttype to set
     */
    public void setLordporttype(String Lordporttype) {
        this.Lordporttype = Lordporttype;
    }

    /**
     * @return the Disporttype
     */
    public String getDisporttype() {
        return Disporttype;
    }

    /**
     * @param Disporttype the Disporttype to set
     */
    public void setDisporttype(String Disporttype) {
        this.Disporttype = Disporttype;
    }

    /**
     * @return the Legtypename
     */
    public String getLegtypename() {
        return Legtypename;
    }

    /**
     * @param Legtypename the Legtypename to set
     */
    public void setLegtypename(String Legtypename) {
        this.Legtypename = Legtypename;
    }

    /**
     * @return the loadcallid
     */
    public String getLoadcallid() {
        return loadcallid;
    }

    /**
     * @param loadcallid the loadcallid to set
     */
    public void setLoadcallid(String loadcallid) {
        this.loadcallid = loadcallid;
    }

    /**
     * @return the dischargecallid
     */
    public String getDischargecallid() {
        return dischargecallid;
    }

    /**
     * @param dischargecallid the dischargecallid to set
     */
    public void setDischargecallid(String dischargecallid) {
        this.dischargecallid = dischargecallid;
    }

    /**
     * @return the cutoff_polterminal
     */
    public String getCutoff_polterminal() {
        return cutoff_polterminal;
    }

    /**
     * @param cutoff_polterminal the cutoff_polterminal to set
     */
    public void setCutoff_polterminal(String cutoff_polterminal) {
        this.cutoff_polterminal = cutoff_polterminal;
    }

    /**
     * @return the cutoff_polarrdate
     */
    public String getCutoff_polarrdate() {
        return cutoff_polarrdate;
    }

    /**
     * @param cutoff_polarrdate the cutoff_polarrdate to set
     */
    public void setCutoff_polarrdate(String cutoff_polarrdate) {
        this.cutoff_polarrdate = cutoff_polarrdate;
    }

    public int getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(int transhipment) {
        this.transhipment = transhipment;
    }

    public String getDisPortDepDate() {
        return disPortDepDate;
    }

    public void setDisPortDepDate(String disPortDepDate) {
        this.disPortDepDate = disPortDepDate;
    }

    public String getClosingtime() {
        return closingtime;
    }

    public void setClosingtime(String closingtime) {
        this.closingtime = closingtime;
    }

    public String getDischargebound() {
        return dischargebound;
    }

    public void setDischargebound(String dischargebound) {
        this.dischargebound = dischargebound;
    }

    public boolean isIsRouting() {
        return isRouting;
    }

    public void setIsRouting(boolean isRouting) {
        this.isRouting = isRouting;
    }

    public String getDepdatefornexttrans() {
        return depdatefornexttrans;
    }

    public void setDepdatefornexttrans(String depdatefornexttrans) {
        this.depdatefornexttrans = depdatefornexttrans;
    }

    public String getPoltranstime() {
        return poltranstime;
    }

    public void setPoltranstime(String poltranstime) {
        this.poltranstime = poltranstime;
    }

    public String getPodtranstime() {
        return podtranstime;
    }

    public void setPodtranstime(String podtranstime) {
        this.podtranstime = podtranstime;
    }


}
