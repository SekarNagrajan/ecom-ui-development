/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import net.ecom.common.GeneralConf;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.serviceVo.Routedetail;
import net.ecom.vds.serviceVo.RoutingVo;
import net.ecom.vo.BoundDefultVo;
import net.ecom.vo.Route;

/**
 *
 * @author gobinath.t
 */
@Stateless(name = "EcomScheduleBean")
@Remote(EcomScheduleBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/VSSDB", mappedName = "java:/VSSDS")
    ,
    @Resource(name = "jdbc/ReportDB", mappedName = "java:/REPORTDS")
})
public class EcomScheduleBean implements EcomScheduleBeanRemote {

    @Resource
    private SessionContext context;

    public Map serviceTypeMap = null;
    public String isFeederSvcReq = null, isFeederVoyReq = null, isFeederBoundReq = null;

    /*private static String GET_ROUTING = "select id,blpol,blpolTer,blpolsvc,opsPol1,opsPol1Ter,opsPolsvc1,"
            + "opsPol2,opsPol2Ter,opsPolsvc2,opsPol3,opsPol3Ter,opsPolsvc3,blpod,blpodter,"
            + "defroute,mainsvc from routedetail(nolock) "
            + "where origin=? and delivery=? and fromdate <= ? and todate>=? and status='A' order by defroute desc";*/
    private static String SCHEDULE_SEARCH = "select top 1 a.service, a.vessel, a.voyage,a.terminal as polterminal, a.bound, a.port as pol, a.arrdate as polarrdate, a.type,"
            + "a.groupcallid as polcallid,a.depdate as poldepdapte,b.arrdate as podarrdate,b.depdate as poddepdapte,b.port as pod,"
            + "b.groupcallid as podcallid,b.terminal as podterminal "
            + "from allschedules a(NOLOCK) inner join allschedules b(NOLOCK) on a.vessel = b.vessel "
            + "where a.port = ? and b.port = ? "
            //+ "and a.depdate >= ? and a.service = ? "
            + "and a.service = ? "
            + "and ('N' = ? or a.service= b.service) "
            //+ "and a.vessel = b.vessel "
            + "and ('Y'=? or a.depdate > ?) "
            + "and ('N'=? or a.arrdate > ?) "
            + "and ('N' = ? or a.bound = ?) "
            + "and ('N' = ? or a.voyage = b.voyage) "
            + "and ('N' = ? or a.bound = b.bound) "
            + "and ('N' = ? or a.depdate <= ?) "
            + "and b.arrdate > a.depdate "
            + "order by a.arrdate,b.arrdate asc ";

    @Inject
    VDSInvoker vds;

    EcomBKGVdsService ecomvdsservice;

    @PostConstruct
    public void inti() {
        ecomvdsservice = vds.create(EcomBKGVdsService.class);

    }

    @Override
    public List geteBookRoutingInfo(String origin, String delivery, String fromdate, String todate, String def_route_plug, List<String> servList, List<String> feederlis) {

        ArrayList alroute = new ArrayList();
        logPrinter("inside EcomScheduleBean, fromdate: " + fromdate + ", todate: " + todate
                + ", origin: " + origin + ", delivery: " + delivery);

        List routeList = new ArrayList();
        List addlst = new ArrayList();
        Route routeDTO = null;
        String targetdate = "60", Status = "N", service = null;
        boolean check = false;
        boolean checkNearestSeaPort = true;
        boolean isOriPOLDiff = false;
        boolean isDelPODDiff = false;
        HashSet servset = new HashSet();
        //        
        int transshipmentCou = 0;
        try {
            RoutingVo routevo = (RoutingVo) ("Y".equalsIgnoreCase(checkNull(def_route_plug)) ? ecomvdsservice.getecomroutinginformation(origin, delivery, fromdate, fromdate, def_route_plug) : ecomvdsservice.getecomroutinginformation(origin, delivery, fromdate, fromdate));

            List lst = routevo.getRoutedetail();
            if (lst.size() > 0) {
                Status = "Y";
                addlst.add(lst);
            }
            if (addlst.size() > 0) {
                for (int i = 0; i < addlst.size(); i++) {
                    List alst = (List) addlst.get(i);
                    for (int j = 0; j < alst.size(); j++) {
                        transshipmentCou = 0;
                        check = false;
                        Routedetail rst = (Routedetail) alst.get(j);
                        service = checkNull(rst.getMainsvc()).trim().toUpperCase();
                        routeDTO = new Route();
                        routeDTO.setBlpol(rst.getBlpol());
                        routeDTO.setBlpolTer(rst.getBlpolter());
                        routeDTO.setBlpolMode(rst.getMode());
                        routeDTO.setBlpolSVC(rst.getBlpolsvc());
                        routeDTO.setTrans1(rst.getOpspol1());
                        routeDTO.setTrans1Ter(rst.getOpspol1ter());
                        routeDTO.setTrans1Mode(rst.getOpsmode1());
                        routeDTO.setTrans1SVC(rst.getOpspolsvc1());
                        routeDTO.setTrans2(rst.getOpspol2());
                        routeDTO.setTrans2Mode(rst.getOpsmode2());
                        routeDTO.setTrans2SVC(rst.getOpspolsvc2());
                        routeDTO.setTrans2Ter(rst.getOpspol2ter());
                        routeDTO.setTrans3(rst.getOpspol3());
                        routeDTO.setTrans3Mode(rst.getOpsmode3());
                        routeDTO.setTrans3SVC(rst.getOpspolsvc3());
                        routeDTO.setTrans3Ter(rst.getOpspol3ter());
                        routeDTO.setBlpod(rst.getBlpod());
                        routeDTO.setBlpodTer(rst.getBlpodter());
                        routeDTO.setDefRoute(rst.getDefroute());
                        routeDTO.setMainSVC(rst.getMainsvc());
                        routeDTO.setHauMode(rst.getOrgmode());
                        routeDTO.setDisHauMode(rst.getDelmode());
                        if (isOriPOLDiff) {
                            routeDTO.setHauMode("T");
                        }
                        if (isDelPODDiff) {
                            routeDTO.setDisHauMode("T");
                        }
                        routeDTO.setRouteId(String.valueOf(rst.getId()));
                        //
                        routeDTO.setGateInDate(checkNull(fromdate));
                        routeDTO.setOriginVen("");
                        routeDTO.setRoute(String.valueOf(rst.getId()));
                        routeDTO.setBlopodMode("");
                        routeDTO.setBlpodSVC("");
                        routeDTO.setOrginTer(rst.getBlpolter());//
                        routeDTO.setOrigin(checkNull(origin));//
                        routeDTO.setDelivery(checkNull(delivery));//
                        routeDTO.setDeliveryTer(rst.getBlpodter());//
                        //routeDTO.setPreCarriage(preCarriage);
                        //routeDTO.setOnCarriage(onCarriage);
                        routeDTO.setTargetdate(checkNull(targetdate));
                        routeDTO.setService(rst.getMainsvc());
                        routeDTO.setOpstrans1mode(checkNull(rst.getOpstrans1mde()));
                        routeDTO.setOpstrans2mode(checkNull(rst.getOpstrans2mde()));
                        //
                        if (checkNull(routeDTO.getTrans1()).length() > 0) {
                            transshipmentCou = transshipmentCou + 1;
                        }
                        if (checkNull(routeDTO.getTrans2()).length() > 0) {
                            transshipmentCou = transshipmentCou + 1;
                        }
                        if (checkNull(routeDTO.getTrans3()).length() > 0) {
                            transshipmentCou = transshipmentCou + 1;
                        }
                        routeDTO.setTranshipment(transshipmentCou);
                        //
                        if (servList != null && servList.contains(service)) {
                            check = true;
                        }
                        if (feederlis != null && feederlis.size() > 0) {
                            if (feederlis.contains(checkNull(rst.getBlpolsvc())) || feederlis.contains(checkNull(rst.getOpspolsvc1())) || feederlis.contains(checkNull(rst.getMainsvc())) || feederlis.contains(checkNull(rst.getOpspolsvc2())) || feederlis.contains(checkNull(rst.getOpspolsvc3()))) {
                                check = true;
                            }
                        }
                        servset.add(checkNull(rst.getBlpolsvc()));
                        servset.add(checkNull(rst.getOpspolsvc1()));
                        servset.add(checkNull(rst.getOpspolsvc2()));
                        servset.add(checkNull(rst.getOpspolsvc3()));

                        if (!check) {
                            alroute.add(routeDTO);
                        }
                    }
                }
            }
            if (servset.size() > 0) {
                servset.remove("");
                serviceTypeMap = new HashMap();
                Map sertype = ecomvdsservice.getservice(String.join(",", servset));
                lst = (List) sertype.get("service");
                for (int i = 0; i < lst.size(); i++) {
                    sertype = (Map) lst.get(i);
                    serviceTypeMap.put(checkNull((String) sertype.get("servicecode")), checkNull((String) sertype.get("servicetype")));
                }
            }
            logPrinter("serviceTypeMap--->  " + serviceTypeMap+ "===>alroute " + alroute.size());
            //routeList = getSchdule_Trust(alroute, fromdate, todate, "R", "R", origin, delivery);
            routeList = getSchdule_Trust(alroute, fromdate, todate, "R", "R", origin, delivery);
            //
            //

        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeList;
    }

    public List getSchdule_Trust(ArrayList alroute, String fromdate, String todate, String origintype, String deliverytype, String origin, String delivery) {

        Connection con = null;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null, rs1 = null;
        String etd_calc = null, eta_calc = null;
        Route routeDTO = null, routeDTO1 = null;
        int count = 0;
        Route frou = null, route1 = null;
        List finallist = new ArrayList();
        List routeList = null;
        boolean isRouting = false;
        String polTerminal = null, podTerminal = null;
        Map defbound = null;
        String configbound = null;
        String isPortPairBoundReq = null;
        String configBLG = "N", configBFL = "N";
        String isBkgLegFormationReq = "N";
        String strMainRestriction = "";
        String strFeederRestriction = "";
        //
        String isMainSVCReq = null;
        String isMainVoyReq = null;
        String isMainBoundReq = null;
        //
        //
        String isSvcReq = null;
        String isVoyReq = null;
        String isBoundReq = null;
        String strSerType = null;
        String nextDepDate = fromdate;
        String resbound = null;
        //
        int totalSchedulePerRoute = 1;
        //
        int transhipmentCount = 0;
        //
        String pol = null;
        String pod = null;
        String service = null;
        String firstVslDepDate = null;
        //
        Route routeVo = null;
        Route routeOneVo = null;
        Route routeTwoVo = null;
        Route routeThreeVo = null;
        Route routeOri = null;
        Route routeDel = null;
        String bft = "N", blt = "N";
        //
        boolean isFullLegAvail = false;
        String hrs = "1";
        String pluginconfig = null;
        String boundres = null;
        //
        try {
            //con = JDBCServiceLocatorFactory.getDocDB();
            //
//            pluginconfig = ecomvdsservice.getEcomBookingPluginConfig("BLF");
//            List lis = (List) pluginconfig.get("pluginconfiguration");
//            if (lis != null && lis.size() > 0) {
//                Map configval = (Map) lis.get(0);
//                if (configval.containsValue("BLF")) {
//                    isBkgLegFormationReq = "Y";
//                }
//            }
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            isBkgLegFormationReq = "Y";
            //
            if ("Y".equalsIgnoreCase(isBkgLegFormationReq)) {
                ps = con.prepareStatement("select attribute,value from bkgconfig (NOLOCK) where attribute in ('BLF','BLM')"
                        + " and status='A'", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = ps.executeQuery();
                String strColumnName = null;
                while (rs.next()) {
                    strColumnName = rs.getString("attribute").trim();
                    if ("BLF".equalsIgnoreCase(strColumnName)) {
                        strFeederRestriction = rs.getString("value").trim();
                    } else if ("BLM".equalsIgnoreCase(strColumnName)) {
                        strMainRestriction = rs.getString("value").trim();
                    }
                }
            }
            //
            strFeederRestriction = strFeederRestriction == null ? "" : strFeederRestriction;
            strMainRestriction = strMainRestriction == null ? "" : strMainRestriction;
            //
            ps = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='RTH' and status='A'", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            if (rs.next()) {
                hrs = rs.getString("value").trim();
            }
            //
            try {
                ps = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='BFT' and status='A'", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = ps.executeQuery();
                if (rs.next()) {
                    bft = rs.getString("value").trim();
                }
                // System.out.println("[ECOM] EcomScheduleBean  BFT >>>> " + bft);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            try {
                ps = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='BLT' and status='A'", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = ps.executeQuery();
                if (rs.next()) {
                    blt = rs.getString("value").trim();
                }
                // System.out.println("[ECOM] EcomScheduleBean  BLT >>>> " + blt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            for (int j = 0; j < alroute.size(); j++) {
                transhipmentCount = 0;
                routeDTO = (Route) alroute.get(j);
                String blpol = routeDTO.getBlpol();
                String blpolSVC = routeDTO.getBlpolSVC();
                String trans1 = routeDTO.getTrans1();
                String trans1SVC = routeDTO.getTrans1SVC();
                String trans2 = routeDTO.getTrans2();
                String trans2SVC = routeDTO.getTrans2SVC();
                String trans3 = routeDTO.getTrans3();
                String trans3SVC = routeDTO.getTrans3SVC();
                String blpod = routeDTO.getBlpod();
                String routeid = routeDTO.getRouteId();
                String trans1Ter = routeDTO.getTrans1Ter();
                String trans2Ter = routeDTO.getTrans2Ter();
                String trans3Ter = routeDTO.getTrans3Ter();
                String blpolter = routeDTO.getBlpolTer();
                String blpodter = routeDTO.getBlpodTer();
                String defaultroute = routeDTO.getDefRoute();
                String opstrans1Mode = routeDTO.getOpstrans1mode();
                String opstrans2Mode = routeDTO.getOpstrans2mode();
                transhipmentCount = routeDTO.getTranshipment();
                //
                defbound = getDefaultBound(blpol, blpolSVC, trans1, trans1SVC, trans2, trans2SVC, trans3, trans3SVC, blpod);
                //               
                //   System.out.println("[ECOM] EcomScheduleBean  -configbound- " + configbound);
                //SVC-VOY-BND
                isMainSVCReq = strMainRestriction.contains("SVC") ? "N" : "Y";
                isMainVoyReq = strMainRestriction.contains("VOY") ? "N" : "Y";
                isMainBoundReq = strMainRestriction.contains("BND") ? "N" : "Y";
                //
                isFeederSvcReq = strFeederRestriction.contains("SVC") ? "N" : "Y";
                isFeederVoyReq = strFeederRestriction.contains("VOY") ? "N" : "Y";
                isFeederBoundReq = strFeederRestriction.contains("BND") ? "N" : "Y";
                //String date = fromdate + "," + todate;
                //
                isSvcReq = isMainSVCReq;
                isVoyReq = isMainVoyReq;
                isBoundReq = isMainBoundReq;
                //                
                String date = fromdate;
                nextDepDate = date;
                //
                for (int i = 0; i < totalSchedulePerRoute; i++) {
                    isFullLegAvail = false;
                    routeList = new ArrayList();
                    //check the transhipment
                    if (transhipmentCount == 0) {
                        //Direct Transhipments
                        pol = routeDTO.getBlpol();
                        pod = routeDTO.getBlpod();
                        service = routeDTO.getBlpolSVC();
                        firstVslDepDate = nextDepDate;
                        //Port pair and service mapped bound
                        resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                        boundres = (String) defbound.get(resbound);
                        isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                        //ALways first should be Depdate so "Y" by default
                        routeVo = getScheduleByPortPair(pol, pod, service,
                                isVoyReq, isSvcReq, isBoundReq, isVoyReq,
                                firstVslDepDate, con, "N", isPortPairBoundReq, boundres, "Y", todate);
                        if (routeVo != null) {
                            routeVo.routeId = routeDTO.getRouteId();
                            routeVo.defRoute = routeDTO.getDefRoute();
                            routeList.add(routeVo);
                            isFullLegAvail = true;
                            nextDepDate = addHours("1", routeVo.ETD1);
                        }
                    } else if (transhipmentCount == 1) {
                        //Single Transhipment
                        pol = routeDTO.getBlpol();
                        pod = routeDTO.getTrans1();
                        service = routeDTO.getBlpolSVC();
                        firstVslDepDate = nextDepDate;
                        //Port pair and service mapped bound
                        resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                        boundres = (String) defbound.get(resbound);
                        isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                        //

                        //ALways first should be Depdate so "Y" by default to have depdate in the where condition
                        routeVo = getScheduleByPortPair(pol, pod, service,
                                isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                con, "N", isPortPairBoundReq, boundres, "Y", todate);
                        //
                        if (routeVo != null) {
//                            System.out.println("[ECOM] EcomScheduleBean pol 1-->" + pol);
//                            System.out.println("[ECOM] EcomScheduleBean pod 1-->" + pod);
//                            System.out.println("[ECOM] EcomScheduleBean service 1-->" + service);
//                            System.out.println("[ECOM] EcomScheduleBean depDate 1-->" + firstVslDepDate);
//                            System.out.println("[ECOM] EcomScheduleBean boundres 1-->" + boundres,"Y",todate);
                            routeVo.routeId = routeDTO.getRouteId();
                            routeVo.defRoute = routeDTO.getDefRoute();
                            routeList.add(routeVo);
                            nextDepDate = addHours("1", routeVo.ETD1);
                            //
                            pol = routeDTO.getTrans1();
                            pod = routeDTO.getBlpod();
                            service = routeDTO.getTrans1SVC();
                            if ("Y".equalsIgnoreCase(bft)) {
                                firstVslDepDate = addHours(hrs, routeVo.getDisPortDepDate());//Discharge port Dep Date
                            } else {
                                firstVslDepDate = addHours(hrs, routeVo.ETA1);//Discharge port arr Date
                            }
                            // firstVslDepDate = addHours(hrs, routeVo.ETA1);
                            //Port pair and service mapped bound
                            resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                            boundres = (String) defbound.get(resbound);
                            isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                            routeOneVo = getScheduleByPortPair(pol, pod, service,
                                    isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                    con, blt, isPortPairBoundReq, boundres, "N", todate);
                            //
                            if (routeOneVo != null) {

//                                System.out.println("[ECOM] EcomScheduleBean pol Trans-->" + pol);
//                                System.out.println("[ECOM] EcomScheduleBean pod Trans-->" + pod);
//                                System.out.println("[ECOM] EcomScheduleBean service Trans-->" + service);
//                                System.out.println("[ECOM] EcomScheduleBean depDate Trans-->" + firstVslDepDate);
//                                System.out.println("[ECOM] EcomScheduleBean boundres Trans-->" + boundres,"Y",todate);
                                routeOneVo.routeId = routeDTO.getRouteId();
                                routeOneVo.defRoute = routeDTO.getDefRoute();
                                routeList.add(routeOneVo);
                                isFullLegAvail = true;
                            }
                        }
                    } else if (transhipmentCount == 2) {
                        //Two Transhipment
                        pol = routeDTO.getBlpol();
                        pod = routeDTO.getTrans1();
                        service = routeDTO.getBlpolSVC();
                        firstVslDepDate = nextDepDate;
                        //Port pair and service mapped bound
                        resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                        boundres = (String) defbound.get(resbound);
                        isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                        //ALways first should be Depdate so "Y" by default to have depdate in the where condition
                        routeVo = getScheduleByPortPair(pol, pod, service,
                                isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                con, "N", isPortPairBoundReq, boundres, "Y", todate);
                        //
                        if (routeVo != null) {
                            routeVo.routeId = routeDTO.getRouteId();
                            routeVo.defRoute = routeDTO.getDefRoute();
                            routeList.add(routeVo);
                            nextDepDate = addHours("1", routeVo.ETD1);
                            //
                            pol = routeDTO.getTrans1();
                            pod = routeDTO.getTrans2();
                            service = routeDTO.getTrans1SVC();
                            if ("Y".equalsIgnoreCase(bft)) {
                                firstVslDepDate = addHours(hrs, routeVo.getDisPortDepDate());//Discharge port Dep Date
                            } else {
                                firstVslDepDate = addHours(hrs, routeVo.ETA1);//Discharge port arr Date
                            }
                            //firstVslDepDate = addHours(hrs, routeVo.ETA1);                            
                            //Port pair and service mapped bound
                            resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                            boundres = (String) defbound.get(resbound);
                            isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                            routeOneVo = getScheduleByPortPair(pol, pod, service,
                                    isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                    con, blt, isPortPairBoundReq, boundres, "N", todate);
                            //
                            if (routeOneVo != null) {
                                routeOneVo.routeId = routeDTO.getRouteId();
                                routeOneVo.defRoute = routeDTO.getDefRoute();
                                routeList.add(routeOneVo);
                                //
                                pol = routeDTO.getTrans2();
                                pod = routeDTO.getBlpod();
                                service = routeDTO.getTrans2SVC();
                                if ("Y".equalsIgnoreCase(bft)) {
                                    firstVslDepDate = addHours(hrs, routeOneVo.getDisPortDepDate());//Discharge port Dep Date
                                } else {
                                    firstVslDepDate = addHours(hrs, routeOneVo.ETA1);//Discharge port arr Date
                                }
                                //firstVslDepDate = addHours(hrs, routeOneVo.ETA1);
                                //Port pair and service mapped bound
                                resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                                boundres = (String) defbound.get(resbound);
                                isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                                routeTwoVo = getScheduleByPortPair(pol, pod, service,
                                        isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                        con, blt, isPortPairBoundReq, boundres, "N", todate);
                                //
                                if (routeTwoVo != null) {
                                    routeTwoVo.routeId = routeDTO.getRouteId();
                                    routeTwoVo.defRoute = routeDTO.getDefRoute();
                                    routeList.add(routeTwoVo);
                                    isFullLegAvail = true;
                                }
                            }
                        }

                    } else if (transhipmentCount == 3) {
                        //Three Transhipment
                        pol = routeDTO.getBlpol();
                        pod = routeDTO.getTrans1();
                        service = routeDTO.getBlpolSVC();
                        firstVslDepDate = nextDepDate;
                        //Port pair and service mapped bound
                        resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                        boundres = (String) defbound.get(resbound);
                        isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                        //ALways first should be Depdate so "Y" by default to have depdate in the where condition
                        routeVo = getScheduleByPortPair(pol, pod, service,
                                isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                con, "N", isPortPairBoundReq, boundres, "Y", todate);
                        //
                        if (routeVo != null) {
                            routeVo.routeId = routeDTO.getRouteId();
                            routeVo.defRoute = routeDTO.getDefRoute();
                            routeList.add(routeVo);
                            nextDepDate = addHours("1", routeVo.ETD1);
                            //
                            pol = routeDTO.getTrans1();
                            pod = routeDTO.getTrans2();
                            service = routeDTO.getTrans1SVC();
                            if ("Y".equalsIgnoreCase(bft)) {
                                firstVslDepDate = addHours(hrs, routeVo.getDisPortDepDate());//Discharge port Dep Date
                            } else {
                                firstVslDepDate = addHours(hrs, routeVo.ETA1);//Discharge port arr Date
                            }
                            //Port pair and service mapped bound
                            resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                            boundres = (String) defbound.get(resbound);
                            isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                            //firstVslDepDate = addHours(hrs, routeVo.ETA1);
                            routeOneVo = getScheduleByPortPair(pol, pod, service,
                                    isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                    con, blt, isPortPairBoundReq, boundres, "N", todate);
                            //
                            if (routeOneVo != null) {
                                routeOneVo.routeId = routeDTO.getRouteId();
                                routeOneVo.defRoute = routeDTO.getDefRoute();
                                routeList.add(routeOneVo);
                                //
                                pol = routeDTO.getTrans2();
                                pod = routeDTO.getTrans3();
                                service = routeDTO.getTrans2SVC();
                                if ("Y".equalsIgnoreCase(bft)) {
                                    firstVslDepDate = addHours(hrs, routeOneVo.getDisPortDepDate());//Discharge port Dep Date
                                } else {
                                    firstVslDepDate = addHours(hrs, routeOneVo.ETA1);//Discharge port arr Date
                                }
                                //firstVslDepDate = addHours(hrs, routeOneVo.ETA1);
                                //Port pair and service mapped bound
                                resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                                boundres = (String) defbound.get(resbound);
                                isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                                routeTwoVo = getScheduleByPortPair(pol, pod, service,
                                        isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                        con, blt, isPortPairBoundReq, boundres, "N", todate);
                                //
                                if (routeTwoVo != null) {
                                    routeTwoVo.routeId = routeDTO.getRouteId();
                                    routeTwoVo.defRoute = routeDTO.getDefRoute();
                                    routeList.add(routeTwoVo);
                                    //
                                    pol = routeDTO.getTrans3();
                                    pod = routeDTO.getBlpod();
                                    service = routeDTO.getTrans3SVC();
                                    if ("Y".equalsIgnoreCase(bft)) {
                                        firstVslDepDate = addHours(hrs, routeTwoVo.getDisPortDepDate());//Discharge port Dep Date
                                    } else {
                                        firstVslDepDate = addHours(hrs, routeTwoVo.ETA1);//Discharge port arr Date
                                    }
                                    //firstVslDepDate = addHours(hrs, routeTwoVo.ETA1);
                                    //Port pair and service mapped bound
                                    resbound = pol.trim() + "~~" + pod.trim() + "~~" + service.trim();
                                    boundres = (String) defbound.get(resbound);
                                    isPortPairBoundReq = "N/A".equalsIgnoreCase(boundres) ? "N" : "Y";
                                    routeThreeVo = getScheduleByPortPair(pol, pod, service,
                                            isVoyReq, isSvcReq, isBoundReq, isVoyReq, firstVslDepDate,
                                            con, blt, isPortPairBoundReq, boundres, "N", todate);
                                    //
                                    if (routeThreeVo != null) {
                                        routeThreeVo.routeId = routeDTO.getRouteId();
                                        routeThreeVo.defRoute = routeDTO.getDefRoute();
                                        routeList.add(routeThreeVo);
                                        isFullLegAvail = true;
                                    }
                                }
                            }
                        }
                    }
                    if (isFullLegAvail) {
                        //
                        if (origin != null && !origin.equalsIgnoreCase(routeDTO.getBlpol())) {
                            //
                            routeVo = (Route) routeList.get(0);
                            pod = routeDTO.getBlpod();
                            routeOri = new Route();
                            routeOri.loadport = origin;
                            routeOri.disport = routeDTO.getBlpol();
                            routeOri.service = "";
                            routeOri.vessel = "";
                            routeOri.voyage = "";
                            routeOri.legType = routeDTO.getHauMode();
                            routeOri.routeId = routeid;
                            routeOri.bound = "";
                            routeOri.ETA1 = timeDiffForRouting(routeVo.ETD1, "origin");
                            routeOri.ETD1 = timeDiffForRouting(route1.ETA1, "origin");
                            routeOri.defRoute = defaultroute;
                            routeList.add(0, routeOri);
                        }
                        if (delivery != null && !delivery.equalsIgnoreCase(routeDTO.getBlpod())) {
                            routeVo = (Route) routeList.get(routeList.size() - 1);
                            routeDel = new Route();
                            routeDel.loadport = routeVo.getBlpod();
                            routeDel.disport = delivery;
                            routeDel.service = "";
                            routeDel.vessel = "";
                            routeDel.voyage = "";
                            routeDel.legType = routeDTO.getDisHauMode();
                            routeDel.bound = "";
                            routeDel.ETD1 = timeDiffForRouting(routeVo.ETA1, "delivery");
                            routeDel.ETA1 = timeDiffForRouting(routeDel.ETD1, "delivery");
                            routeDel.routeId = routeid;
                            routeDel.defRoute = defaultroute;
                            routeList.add(routeDel);
                        }
                        //
                        finallist.add(routeList);
                    }

                }
            }
            //finallist = removeRoutingDuplicate(finallist);
            //finallist = sortByDepDate(finallist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return finallist;
    }

    private LinkedHashMap getDefaultBound(String blpol, String blpolsvc, String opspol1, String opspolsvc1, String opspol2, String opspolsvc2, String opspol3, String opspolsvc3, String blpod) {

        LinkedHashMap lnkhsmap = null;
        String boundConfig = null;
        BoundDefultVo defbouvo = null;
        try {

            lnkhsmap = new LinkedHashMap();

            if ((blpol != null) && (blpol.trim().length() > 0)) {

                defbouvo = new BoundDefultVo();
                //
                defbouvo.setPol(blpol.trim());
                if ((opspol1 != null) && (opspol1.trim().length() > 0)) {
                    defbouvo.setPod(opspol1.trim());
                } else {
                    defbouvo.setPod(blpod.trim());
                }
                defbouvo.setSerive(blpolsvc.trim());

                boundConfig = getautolegconfig(defbouvo);

                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
                //
            }

            //To check First Transhipment
            if ((opspol1 != null) && (opspol1.trim().length() > 0)) {

                defbouvo = new BoundDefultVo();

                defbouvo.setPol(opspol1.trim());
                if ((opspol2 != null) && (opspol2.trim().length() > 0)) {
                    defbouvo.setPod(opspol2.trim());
                } else {
                    defbouvo.setPod(blpod.trim());
                }

                defbouvo.setSerive(opspolsvc1.trim());
                boundConfig = getautolegconfig(defbouvo);
                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
            }

            //To check second Transhipment
            if ((opspol2 != null) && (opspol2.trim().length() > 0)) {

                defbouvo = new BoundDefultVo();

                defbouvo.setPol(opspol2.trim());
                if ((opspol3 != null) && (opspol3.trim().length() > 0)) {
                    defbouvo.setPod(opspol3.trim());
                } else {
                    defbouvo.setPod(blpod.trim());
                }
                defbouvo.setSerive(opspolsvc2.trim());
                boundConfig = getautolegconfig(defbouvo);
                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
            }

            //To check Third Transhipment
            if ((opspol3 != null) && (opspol3.trim().length() > 0)) {
                defbouvo = new BoundDefultVo();
                defbouvo.setPol(opspol3.trim());
                defbouvo.setPod(blpod.trim());
                defbouvo.setSerive(opspolsvc3.trim());
                boundConfig = getautolegconfig(defbouvo);
                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lnkhsmap;
    }

    //
    private String getautolegconfig(BoundDefultVo defbouvo) {
        String boundConfig = "N/A";
        String result = null;
        try {
            String pol = defbouvo.getPol();
            String pod = defbouvo.getPod();
            String service = defbouvo.getSerive();
            Map hs = (Map) ecomvdsservice.getecombkgportcombination(pol, pod, service);
            List lst = (List) hs.get("bkgportcombination");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                result = checkNull((String) rs.get("bound"));
                if (result != null && result.length() > 0) {
                    boundConfig = result;
                } else {
                    boundConfig = "N/A";
                }
            }

        } catch (Exception e) {
            logPrinter("Exception in getautolegconfig " + e);
            e.printStackTrace();
        }
        return boundConfig;
    }

    //
    private String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String timeDiffForRouting(String convertDate, String port) {
        String finalDate = null;
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        try {
            SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentDate = source.parse(convertDate);
            cal.setTime(currentDate);
            if ("origin".equalsIgnoreCase(port)) {
                cal.add(Calendar.HOUR, -5);
            } else if ("delivery".equalsIgnoreCase(port)) {
                cal.add(Calendar.HOUR, 5);
            }
            Date oneHourBack = cal.getTime();
            finalDate = source.format(oneHourBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    //
    private Route getScheduleByPortPair(String pol, String pod, String service, String isVoyReq,
            String isSvcReq, String isBndReq, String isReq, String depArrDate,
            Connection con, String blt, String portPairBound, String boundRequired,
            String considerToDate, String todate) {
        String strResult = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Route routeVo = null;
        //boundRequired wil be 'N' or S or W or E
        try {

            if ("F".equalsIgnoreCase(checkNull((String) serviceTypeMap.get(service.trim())))) {
                isSvcReq = isFeederSvcReq;
                isVoyReq = isFeederVoyReq;
                isBndReq = isFeederBoundReq;
            }

            pst = con.prepareStatement(SCHEDULE_SEARCH, 1004, 1007);
            pst.setString(1, pol);
            pst.setString(2, pod);
            // pst.setString(3, depDate);
            pst.setString(3, service);
            pst.setString(4, isSvcReq);
            pst.setString(5, blt);
            pst.setString(6, depArrDate);
            pst.setString(7, blt);
            pst.setString(8, depArrDate);
            pst.setString(9, portPairBound);
            pst.setString(10, boundRequired);
            pst.setString(11, isVoyReq);
            pst.setString(12, isBndReq);
            pst.setString(13, considerToDate);
            pst.setString(14, todate);

            // System.out.println("[ECOM] EcomScheduleBean SCHEDULE_SEARCH=> "+SCHEDULE_SEARCH);
            // System.out.println(pol+","+pod+","+service+","+isSvcReq+","+blt+","+depArrDate+","+blt+","+","+depArrDate+","+","+portPairBound+","+","+boundRequired+","+","+isVoyReq+","+","+isBndReq+","+","+considerToDate+","+","+todate);
            rs = pst.executeQuery();
            //
//System.out.println("[ECOM] EcomScheduleBean query-->"+SCHEDULE_SEARCH);   
//System.out.println("[ECOM] EcomScheduleBean pol-->"+pol);
//System.out.println("[ECOM] EcomScheduleBean pod-->"+pod);
//System.out.println("[ECOM] EcomScheduleBean depDate-->"+depDate);
//System.out.println("[ECOM] EcomScheduleBean isSvcReq-->"+isSvcReq);
//System.out.println("[ECOM] EcomScheduleBean isVoyReq-->"+isVoyReq);
//System.out.println("[ECOM] EcomScheduleBean isBndReq-->"+isBndReq);
//

            if (rs.next()) {
                routeVo = new Route();
                //
                routeVo.service = rs.getString("service");
                routeVo.vessel = rs.getString("vessel");
                routeVo.voyage = rs.getString("voyage");
                routeVo.bound = rs.getString("bound");
                routeVo.blpolTer = rs.getString("polterminal");
                routeVo.loadport = rs.getString("pol");
                routeVo.legType = rs.getString("type");
                routeVo.loadcallid = rs.getString("polcallid");
                routeVo.grpCallId = rs.getString("polcallid");
                routeVo.ETD1 = rs.getString("poldepdapte");
                routeVo.disport = rs.getString("pod");
                routeVo.cutoff_polterminal = rs.getString("polterminal");
                routeVo.cutoff_polarrdate = rs.getString("polarrdate");
                //
                routeVo.ETA1 = rs.getString("podarrdate");
                routeVo.dischargecallid = rs.getString("podcallid");
                routeVo.blpodTer = rs.getString("podterminal");
                routeVo.setDisPortDepDate(rs.getString("poddepdapte"));

            }
        } catch (Exception e) {
            logPrinter("Exception in getScheduleByPortPair >>>>>>> " + e);
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                    rs = null;
                }
            } catch (Exception ex) {
                logPrinter("Data Base getScheduleByPortPair finally block");
            }
        }
        return routeVo;
    }

    //
    private String addHours(String hrs, String date) {
        String ret = date;
        try {
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date a = sm.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(a);
            c.add(10, Integer.parseInt(hrs));
            return sm.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    private void logPrinter(String message) {
        if(GeneralConf.Enable_Sout_Log) {
            System.out.println("[ECOM] EcomScheduleBean - " + message);
        }
     }
}
