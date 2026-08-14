/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.inject.Inject;
import net.ecom.vds.service.EcomBKGSpsService;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomGeneralVDSService;
import net.ecom.vds.serviceVo.Booking;
import net.ecom.vo.CustomerAdvisoryVo;
import net.ecom.vo.ImageUploadVo;
import net.ecom.vo.OnGoingTransactionVo;
import net.ecom.vo.RegisterMenuVO;
import net.ecom.vo.UnclearedCargoVo;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomAdminCntrlBean")
@Remote(EcomAdminCntrlBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS"),
    @Resource(name = "jdbc/ReportDB", mappedName = "java:/REPORTDS")
})
public class EcomAdminCntrlBean implements EcomAdminCntrlBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final String GET_CUST_ADVIS_DETAILS = "select id,advisoryname,description,createddate,status from EcomCustomerAdvisory where status='A' order by createddate desc";
    private static final String INSERT_CUST_ADVIS_DETAILS = "insert into EcomCustomerAdvisory(advisoryname,description,status,createddate) values(?,?,'A',GETDATE())";
    private static final String UPDATE_CUST_ADVIS_DETAILS = "update EcomCustomerAdvisory set advisoryname=?,description=? where id=?";
    private static final String DELETE_CUST_ADVIS_DETAILS = "update EcomCustomerAdvisory set status='V' where id=?";
    //
    private static final String INSERT_IMG_UPLOAD_DETAILS = "insert into EcomImageUpload(imagename,imagepath,displayorder,status) values(?,?,?,'A')";
    private static final String GET_IMG_UPLOAD_DETAILS = "select id,imagename,imagepath,displayorder,status from EcomImageUpload order by displayorder";
    private static final String UPDATE_IMG_UPLOAD_DETAILS = "update EcomImageUpload set imagename=?,imagepath=? where id=?";
    private static final String DELETE_IMG_UPLOAD_DETAILS = "update EcomImageUpload set status=? where id=?";
    private static final String UPDATE_IMG_UPLOAD_REARR_ORDER_DETAILS = "update EcomImageUpload set displayorder=? where id=?";

    private static final String GET_ADMIN_MAIN_MENU_DETAILS = "SELECT MenuId,MenuName,RefNo,ClassValue,AttrValue,LabelValue,MenuType,ParentMenu,ScreenType,MenuOrder,Category FROM adminmenu WHERE status='A' and menutype='M' and ScreenType=? order by menuorder";
    private static final String GET_ADMIN_SUB_MENU_DETAILS = "SELECT MenuId,MenuName,RefNo,ClassValue,AttrValue,LabelValue,MenuType,ParentMenu,ScreenType,MenuOrder,Category FROM adminmenu WHERE status='A' and menutype='S' and ScreenType=? order by menuorder";
    protected static final String customerstatementsp = "exec usp_CustomerStatement ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";

    
    @Inject
    VDSInvoker vds;

    EcomBKGVdsService ecomvdsservice;

    @PostConstruct
    public void inti() {
        ecomvdsservice = vds.create(EcomBKGVdsService.class);
    }

    @Override
    public List<CustomerAdvisoryVo> getCustomerAdvisoryDetails() throws RemoteException {
        List<CustomerAdvisoryVo> custAdvisoryDets = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomerAdvisoryVo cusadvvo = null;
        try {

            custAdvisoryDets = new ArrayList<CustomerAdvisoryVo>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_CUST_ADVIS_DETAILS);
            rs = ps.executeQuery();
            while (rs.next()) {

                cusadvvo = new CustomerAdvisoryVo();
                cusadvvo.setKeyid(rs.getInt("id"));
                cusadvvo.setAdvisoryname(checkNull(rs.getString("advisoryname")));
                cusadvvo.setDescription(checkNull(rs.getString("description")));
                cusadvvo.setStatus(checkNull(rs.getString("status")));
                cusadvvo.setCreated_date(rs.getString("createddate"));
                custAdvisoryDets.add(cusadvvo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return custAdvisoryDets;
    }

    @Override
    public List getImageUploadDetails(String showactive) throws RemoteException {
        List configDetails = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ImageUploadVo imgupvo = null;
        // if showactive -->'N' show all details (or) 'Y' show active details
        try {
            configDetails = new ArrayList();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_IMG_UPLOAD_DETAILS);
            rs = ps.executeQuery();
            while (rs.next()) {

                imgupvo = new ImageUploadVo();
                imgupvo.setKeyid(rs.getInt("id"));
                imgupvo.setImagename(checkNull(rs.getString("imagename")));
                imgupvo.setImagepath(checkNull(rs.getString("imagepath")));
                imgupvo.setStatus(checkNull(rs.getString("status")));
                imgupvo.setOrder(rs.getInt("displayorder"));
                configDetails.add(imgupvo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return configDetails;
    }

    @Override
    public String saveCustomerAdvisoryDetails(CustomerAdvisoryVo cusadvvo) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String advisoryname = cusadvvo.getAdvisoryname();
            String description = cusadvvo.getDescription();

            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(INSERT_CUST_ADVIS_DETAILS);
            ps.setString(1, advisoryname);
            ps.setString(2, description);

            int i = ps.executeUpdate();
            if (i == 1) {
                result = "inserted";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String updateCustomerAdvisoryDetails(CustomerAdvisoryVo cusadvvo) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int id = cusadvvo.getKeyid();
            String advisoryname = cusadvvo.getAdvisoryname();
            String description = cusadvvo.getDescription();

            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATE_CUST_ADVIS_DETAILS);
            ps.setString(1, advisoryname);
            ps.setString(2, description);
            ps.setInt(3, id);

            int i = ps.executeUpdate();
            if (i == 1) {
                result = "inserted";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String updateImageUploadDetails(ImageUploadVo imgupvo) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int id = imgupvo.getKeyid();
            String imagename = imgupvo.getImagename();
            String imagepath = imgupvo.getImagepath();

            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATE_IMG_UPLOAD_DETAILS);
            ps.setString(1, imagename);
            ps.setString(2, imagepath);
            ps.setInt(3, id);

            int i = ps.executeUpdate();
            if (i == 1) {
                result = "inserted";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String deleteImageUploadDetails(int key, String status) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETE_IMG_UPLOAD_DETAILS);
            ps.setString(1, status);
            ps.setInt(2, key);
            int i = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String updateImageUploadRearrangedDetails(List<ImageUploadVo> rearrangelist) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int order = 0;
        int keyid = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATE_IMG_UPLOAD_REARR_ORDER_DETAILS);
            for (ImageUploadVo imgupvo : rearrangelist) {

                order++;
                keyid = imgupvo.getKeyid();

                ps.setInt(1, order);
                ps.setInt(2, keyid);
                ps.addBatch();
            }

            //
            int res[] = ps.executeBatch();

            if (res.length >= 1) {
                result = "Updated";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String saveImageUploadDetails(ImageUploadVo imgupvo) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String imagename = imgupvo.getImagename();
            String imagepath = imgupvo.getImagepath();
            int order = imgupvo.getOrder();

            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(INSERT_IMG_UPLOAD_DETAILS);
            ps.setString(1, imagename);
            ps.setString(2, imagepath);
            ps.setInt(3, order);

            int i = ps.executeUpdate();
            if (i == 1) {
                result = "inserted";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String deleteCustomerAdvisoryDetails(int key) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETE_CUST_ADVIS_DETAILS);
            ps.setInt(1, key);

            int i = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public List getEcomCpanelMenudetails() throws RemoteException {
        ArrayList mainMenus = new ArrayList();
        ArrayList Menus = new ArrayList();
        HashMap<String, ArrayList> submenu = new HashMap<String, ArrayList>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        RegisterMenuVO registerVo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ADMIN_MAIN_MENU_DETAILS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, "C");
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuName(checkNull(rs.getString("MenuName")));
                registerVo.setCategory(checkNull(rs.getString("Category")));
                registerVo.setClassValue(checkNull(rs.getString("ClassValue")));
                registerVo.setRefNo(checkNull(rs.getString("RefNo")));
                registerVo.setLabelValue(checkNull(rs.getString("LabelValue")));
                registerVo.setAttrValue(checkNull(rs.getString("AttrValue")));
                registerVo.setMenuType(checkNull(rs.getString("MenuType")));
                registerVo.setParentMenu(checkNull(rs.getString("ParentMenu")));
                registerVo.setScreenType(checkNull(rs.getString("ScreenType")));
                mainMenus.add(registerVo);
            }

            ps = con.prepareStatement(GET_ADMIN_SUB_MENU_DETAILS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, "C");
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuName(checkNull(rs.getString("MenuName")));
                registerVo.setCategory(checkNull(rs.getString("Category")));
                registerVo.setClassValue(checkNull(rs.getString("ClassValue")));
                registerVo.setRefNo(checkNull(rs.getString("RefNo")));
                registerVo.setLabelValue(checkNull(rs.getString("LabelValue")));
                registerVo.setAttrValue(checkNull(rs.getString("AttrValue")));
                registerVo.setMenuType(checkNull(rs.getString("MenuType")));
                registerVo.setParentMenu(checkNull(rs.getString("ParentMenu")));
                registerVo.setScreenType(checkNull(rs.getString("ScreenType")));
                if (submenu != null && submenu.get(checkNull(rs.getString("ParentMenu"))) != null) {
                    submenu.get(checkNull(rs.getString("ParentMenu"))).add(registerVo);
                } else {
                    submenu.put(checkNull(rs.getString("ParentMenu")), new ArrayList());
                    submenu.get(checkNull(rs.getString("ParentMenu"))).add(registerVo);
                }
            }
            Menus.add(mainMenus);
            Menus.add(submenu);
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
        return Menus;
    }

    @Override
    public List getEcomOnGoingTransactionDetails(String usercode) throws RemoteException {
        List<OnGoingTransactionVo> dashlis = null;
        Map resultmap;
        try {
            Gson gson = new Gson();
            SvmJsonList alist = new SvmJsonList();
            alist.add(checkNull(usercode));
            alist.add("");
            alist.add("");
            alist.add("N");
            alist.add("");
            resultmap = ecomvdsservice.getEcomOnGoingDashboard(alist);
            if (resultmap != null && resultmap.size() > 0) {
                resultmap = (Map) resultmap.get("rs");
                if (resultmap != null && resultmap.size() > 0) {
                    dashlis = gson.fromJson(gson.toJson((ArrayList) resultmap.get("rs0")), new TypeToken<List<OnGoingTransactionVo>>() {
                    }.getType());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dashlis;
    }

    @Override
    public List getEcomOnGoingTransactionCargoDetails(String bookno, String blno) throws RemoteException {
        List<OnGoingTransactionVo> dashlis = null;
        Map resultmap;
        List reslis;
        OnGoingTransactionVo dasvo;
        try {
            System.out.println("[ECOM] EcomAdminCntrlBean inside getEcomOnGoingTransactionCargoDetails");

            SvmJsonList alist = new SvmJsonList();
            alist.add("");
            alist.add("");
            alist.add("");
            alist.add("N");
            alist.add(bookno);

            resultmap = ecomvdsservice.getEcomOnGoingDashboard(alist);
            if (resultmap != null && resultmap.size() > 0) {
                resultmap = (Map) resultmap.get("rs");
                if (resultmap != null && resultmap.size() > 0) {
                    reslis = (List) resultmap.get("rs0");
                    if (reslis != null && reslis.size() > 0) {
                        dashlis = new ArrayList<OnGoingTransactionVo>();
                        for (int i = 0; i < reslis.size(); i++) {
                            dasvo = new OnGoingTransactionVo();
                            resultmap = (Map) reslis.get(i);
                            dasvo.setContainerno((String) resultmap.get("containerno"));
                            dasvo.setLatestact((String) resultmap.get("latestact"));
                            dashlis.add(dasvo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dashlis;
    }

    @Override
    public String getDashboardOutstandingBalance(String fromdate, String custcode) throws RemoteException {
        String outbal = null;
        Map resultmap;
        ArrayList sublis = null;
        try {
            Gson gson = new Gson();
            SvmJsonList alist = new SvmJsonList();
            alist.add(fromdate);
            alist.add(custcode);
            alist.add("");
            alist.add("jasper");
            alist.add("summary");
            resultmap = ecomvdsservice.getEcomCustStmtNew(alist);
            if (resultmap != null && resultmap.size() > 0) {
                resultmap = (Map) resultmap.get("rs");
                if (resultmap != null && resultmap.size() > 0) {
                    sublis = (ArrayList) resultmap.get("rs0");
                    if (sublis != null && sublis.size() > 0) {
                        resultmap = (Map) sublis.get(0);
                        if (resultmap != null && resultmap.size() > 0) {
                            outbal = (String) resultmap.get("total");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outbal;
    }

    @Override
    public List getEcomUnclearedCargoDetails(String custcode) throws RemoteException {
        List<UnclearedCargoVo> dashlis = null;
        Map resultmap;
        try {
            Gson gson = new Gson();
            resultmap = ecomvdsservice.getecomdasunclearedcargodetail(checkNull(custcode));
            if (resultmap != null && resultmap.size() > 0) {
                dashlis = gson.fromJson(gson.toJson((ArrayList) resultmap.get("houseblmaster")), new TypeToken<List<UnclearedCargoVo>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dashlis;

    }

    @Override
    public String getDashboardOutstandingBalfromcuststmtsp(String fromdate, String custcode) throws RemoteException {
        String outbal = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String inputType = "AS ON DATE";
            if (fromdate == null || fromdate.isEmpty()) {
                fromdate = sdf.format(new java.util.Date());
            }
            System.out.println("[ECOM] getDashboardOutstandingBalfromcuststmtsp - custCode: " + custcode + ", fromdate: " + fromdate);
            
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            ps = con.prepareStatement(customerstatementsp);
            ps.setString(1, "N/A");                    // 1-LOCATIONCODE
            ps.setString(2, "N/A");                    // 2-AGENCYCODE
            ps.setString(3, "CO");                     // 3-VIEWBY (CO for Outstanding)
            ps.setString(4, inputType);                // 4-INPUTTYPE
            ps.setString(5, checkNull(custcode));      // 5-CUSTCODE
            ps.setString(6, fromdate);                 // 6-FROMDATE
            ps.setString(7, "");                       // 7-TODATE
            ps.setString(8, "customer");               // 8-OPTION
            ps.setString(9, "");                       // 9-SERCODE
            ps.setString(10, "");                      // 10-VSLCODE
            ps.setString(11, "");                      // 11-VOYCODE
            ps.setString(12, "");                      // 12-BOUND
            ps.setString(13, "");                      // 13-PORT
            ps.setString(14, "");                      // 14-CURRENCY
            ps.setString(15, "L");                     // 15-SYSTEMTYPE
            ps.setString(16, "ALL");                   // 16-REPORTFOR
            ps.setString(17, "");                      // 17-CORPCODE
            ps.setString(18, "ALL");                   // 18-INVTYPE
            ps.setString(19, "ALL");                   // 19-CHARGETYPE
            ps.setString(20, "");                      // 20-EXECUTEFROM
            ps.setString(21, "");                      // 21-CARRIERCODE
            ps.setString(22, "");                      // 22-SESSIONID
            ps.setString(23, "N");                     // 23-AGSAILDATE
            
            boolean hasResults = ps.execute();
            
            // rs0 - Outstanding Summary - Calculate total from all records
            double totalOutstandingBal = 0.0;
            if (hasResults) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    String balUsdAmt = checkNull(rs.getString("balusdamt"));
                    if (balUsdAmt != null && !balUsdAmt.isEmpty()) {
                        try {
                            totalOutstandingBal += Double.parseDouble(balUsdAmt);
                        } catch (NumberFormatException e) {
                            System.out.println("[ECOM] getDashboardOutstandingBalfromcuststmtsp - Invalid balusdamt: " + balUsdAmt);
                        }
                    }
                }
                rs.close();
            }
            
            // Format the total outstanding balance
            if (totalOutstandingBal != 0.0) {
                outbal = String.format("%.2f", totalOutstandingBal);
            }
            System.out.println("[ECOM] getDashboardOutstandingBalfromcuststmtsp calculated totalOutstandingBal: " + outbal);
            
        } catch (Exception e) {
            System.out.println("[ECOM] getDashboardOutstandingBalfromcuststmtsp Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("[ECOM] getDashboardOutstandingBalfromcuststmtsp Error closing resources: " + e.getMessage());
            }
        }
        return outbal;
    }
}
