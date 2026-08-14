/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.v1.vo.CommonV1MasterVO;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomV1MasterBean")
@Remote(EcomV1MasterBeanRemote.class)
public class EcomV1MasterBean implements EcomV1MasterBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final String PORT_QRY = "select portcode,portname from ports(nolock) where status='A' and portcode not like 'xx%'";
    private static final String VESSEL_QRY = "SELECT DISTINCT rtrim(ltrim(VesselCode))as VesselCode, VesselName FROM  vesselmaster (nolock) where VesselCode not like 'xx%'";
    private static final String COUNTRY_QRY = "select distinct countryname,countrycode ,isdcode from country(nolock) where status = 'a' order by countryname asc";
    private static final String COMMODITY_QRY = "select distinct comdtycode,comdtyname from commodity(nolock) where status = 'A'";
    private static final String SERVICE_QRY = "select servicename,servicecode from service(nolock)  where status='Active'";
    private static final String PACKAGE_QRY = "Select packagecode,description from package(nolock) where status='A'";
    private static final String TERMINAL_QRY = "select terminalname,terminalcode  from terminalanddepot(nolock)";
    private static final String EQP_QRY = "select distinct Equiptype,equipdesc from equipments(nolock) where status = 'A'";
    private static final String CARRIER_CONTRACT_QRY = "select trptsvccode,trptsvcname from transportservice(nolock) where status='A'";
    private static final String EMPTY_PICKUP_QRY = "select depot,description from DepotCode where  status='A'";
    private static final String INTRANSIT_QRY = "select upper(dmsfinaldest) as dmsfinaldest, upper(dmsfinaldestdesc) as dmsfinaldestdesc from DMSInTransit where  pod='Y' and  status='A'";

    public Map<String, Map<String, String>> getEcomV1MasterCache() throws RemoteException {
        Map<String, Map<String, String>> masterMap = new HashMap<String, Map<String, String>>();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        Map<String, String> cacheMap = null;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            //Ports
            ps = con.prepareStatement(PORT_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("portcode")), checkNull(rs.getString("portname")));
            }
            masterMap.put("PORTS", cacheMap);

            //country
            ps = con.prepareStatement(COUNTRY_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("countrycode")), checkNull(rs.getString("countryname")));
            }
            masterMap.put("COUNTRY", cacheMap);

            //commodity
            ps = con.prepareStatement(COMMODITY_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("comdtycode")), checkNull(rs.getString("comdtyname")));
            }
            masterMap.put("COMMODITY", cacheMap);

            //package
            ps = con.prepareStatement(PACKAGE_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("packagecode")), checkNull(rs.getString("description")));
            }
            masterMap.put("PACKAGE", cacheMap);

            //service
            ps = con.prepareStatement(SERVICE_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("servicecode")), checkNull(rs.getString("servicename")));
            }
            masterMap.put("SERVICE", cacheMap);

            //vessel
            ps = con.prepareStatement(VESSEL_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("VesselCode")), checkNull(rs.getString("VesselName")));
            }
            masterMap.put("VESSEL", cacheMap);

            //Terminal
            ps = con.prepareStatement(TERMINAL_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("terminalcode")), checkNull(rs.getString("terminalname")));
            }
            masterMap.put("TERMINAL", cacheMap);

            //Equipment
            ps = con.prepareStatement(EQP_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("Equiptype")), checkNull(rs.getString("equipdesc")));
            }
            masterMap.put("EQUIPMENT", cacheMap);

            //Carrier Contract
            ps = con.prepareStatement(CARRIER_CONTRACT_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("trptsvccode")), checkNull(rs.getString("trptsvcname")));
            }
            masterMap.put("CARRIERCONTRACT", cacheMap);

            //Empty Pickup
            ps = con.prepareStatement(EMPTY_PICKUP_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("depot")), checkNull(rs.getString("description")));
            }
            masterMap.put("EMPTYPICKUP", cacheMap);

            //Intransit port
            ps = con.prepareStatement(INTRANSIT_QRY);
            rs = ps.executeQuery();
            cacheMap = new HashMap<String, String>();
            while (rs.next()) {
                cacheMap.put(checkNull(rs.getString("dmsfinaldest")), checkNull(rs.getString("dmsfinaldestdesc")));
            }
            masterMap.put("INTRNSIT", cacheMap);

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
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return masterMap;
    }

    private String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }
}
