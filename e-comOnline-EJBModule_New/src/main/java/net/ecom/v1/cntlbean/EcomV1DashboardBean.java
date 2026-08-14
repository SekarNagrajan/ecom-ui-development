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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.utils.JDBCServiceLocatorFactory;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomV1DashboardBean")
@Remote(EcomV1DashboardBeanRemote.class)
public class EcomV1DashboardBean implements EcomV1DashboardBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final String GETTOPDESTINATION = "select top 5 bl.finalportid,count(bl.bl_no) as countval from bl inner join bl_cust on bl.bl_no=bl_cust.bl_no and bl_cust.custtypeid = 'AP'\n"
            + " where bl.status<>'V' and bl_cust.custid=? group by bl.finalportid order by count(bl.bl_no) desc";
    private static final String GETTOPPORTPAIR = "select top 5 bl.originportid,bl.finalportid,count(bl.bl_no) as countval from bl inner join \n"
            + " bl_cust on bl.bl_no=bl_cust.bl_no and bl_cust.custtypeid = 'AP' \n"
            + " where  bl.status<>'V' and  bl_cust.custid=? group by bl.originportid,bl.finalportid order by count(bl.bl_no) desc";
    private static final String GETTOPTEU = "select top 5 bl.finalportid,count(bl_cargo.eqpid) as countval from bl inner join \n"
            + " bl_cust on bl.bl_no=bl_cust.bl_no and bl_cust.custtypeid = 'AP'\n"
            + " inner join bl_cargo on bl.bl_no=bl_cargo.bl_no\n"
            + " where  bl.status<>'V' and bl_cust.custid=? group by bl.finalportid order by count(bl_cargo.eqpid) desc";

    @Override
    public List<net.ecom.vds.serviceVo.DashboardVO> getV1EcomTopDestinationDashboard(List<net.ecom.vds.serviceVo.DashboardVO> list, String custCode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        net.ecom.vds.serviceVo.DashboardVO dashVO = null;
        try {
            if (list == null) {
                list = new ArrayList<>();
            }
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GETTOPDESTINATION, 1004, 1007);
            ps.setString(1, custCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                dashVO = new net.ecom.vds.serviceVo.DashboardVO();
                dashVO.setColor("#67B7DC");
                dashVO.setCountVal(rs.getString("countval"));
                dashVO.setFinalPortId(rs.getString("finalportid"));
                list.add(dashVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List<net.ecom.vds.serviceVo.DashboardVO> getV1EcomTopPortPairDashboard(List<net.ecom.vds.serviceVo.DashboardVO> list, String custCode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        net.ecom.vds.serviceVo.DashboardVO dashVO = null;
        try {
            if (list == null) {
                list = new ArrayList<>();
            }
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GETTOPPORTPAIR, 1004, 1007);
            ps.setString(1, custCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                dashVO = new net.ecom.vds.serviceVo.DashboardVO();
                dashVO.setColor("#f2f553");
                dashVO.setCountVal(rs.getString("countval"));
                dashVO.setFinalPortId(rs.getString("originportid") + " - " + rs.getString("finalportid"));
                list.add(dashVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List<net.ecom.vds.serviceVo.DashboardVO> getV1EcomTopDestinationTeuDashboard(List<net.ecom.vds.serviceVo.DashboardVO> list, String custCode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        net.ecom.vds.serviceVo.DashboardVO dashVO = null;
        try {
            if (list == null) {
                list = new ArrayList<>();
            }
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GETTOPTEU, 1004, 1007);
            ps.setString(1, custCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                dashVO = new net.ecom.vds.serviceVo.DashboardVO();
                dashVO.setColor("#84B761");
                dashVO.setCountVal(rs.getString("countval"));
                dashVO.setFinalPortId(rs.getString("finalportid"));
                list.add(dashVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
