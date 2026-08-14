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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.vo.VGMCommonVO;
import net.ecom.vo.VgmVO;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomV1VGMBean")
@Remote(EcomV1VGMBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
})
public class EcomV1VGMBean implements EcomV1VGMBeanRemote {

    private static final String GETCONTRDETFORBOOKING = "select b.status,b.book_no,c.shipperown,c.eqptypeid,eqpid,b.agencyid,b.originportid from booking b (nolock) inner join booking_cargo c (nolock) on "
            + "b.book_no=c.book_no where b.book_no=? and b.status<>'V'";
    private static final String GETUSERDETAILSFORBOOKING = "select b.status,b.book_no,bc.custtypeid,bc.name,b.originportid,b.finalportid,b.loadportid,b.dischargeportid from  "
            + "booking b (nolock)  inner join booking_cust bc  (nolock) on "
            + "b.book_no=bc.book_no where custtypeid in('ap','sh') and b.book_no=? and status<>'V'";
    private static final String GETCONTRDETFORBL = "select b.book_no,b.bl_no,c.shipperown,c.eqptypeid,eqpid,b.agencyid,b.originportid from bl b (nolock) inner join bl_cargo c (nolock) on "
            + "b.bl_no=c.bl_no where b.bl_no=? and b.status<>'V'";
    private static final String GETUSERDETAILSFORBL = "select b.book_no,b.bl_no,bc.custtypeid, bc.name,b.originportid,b.finalportid,b.loadportid,b.dischargeportid from  "
            + "bl b (nolock)  inner join bl_cust bc  (nolock) on "
            + "b.bl_no=bc.bl_no where custtypeid in('ap','sh') and b.bl_no=? and b.status<>'V'";
    private static final String INSERTVGMSOLAS = "insert into vgmsolas(book_no,eqptype,eqpid,vgm_source,vgm_ref_number,VGM_order_Number,vgm_obtained_date,vgm_weight,vgm_unit,"
            + "vgm_obtained_method,vgm_company_name,VGM_Address1,VGM_Address2,VGM_Zipcode,VGM_City,VGM_Country,vgm_authorized_person,Telephone,Fax,"
            + "Email,vgm_type,status,version_no,usercr,userup,oprstatus,datecr,dateup)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),getdate())";
    private static final String GETVGMVERSION = "select max(version_no) as version_no from vgmsolas(nolock) where book_no=? and eqpid=?";
    private static final String UPDATEVGMVERSION = "update vgmsolas set status='I' where book_no=? and eqpid=? and (status='A' or status='C')";
    private static final String GETTAREWEIGHT = "select tarewt1 from containerprofile(nolock) where containerno = ?";
    private static final String GETCONTAINERLIST = "select c.eqpid,c.eqpTypeId from booking_cargo c (nolock) where c.book_no=?";
    private static final String GETCONTAINERLISTFORBL = "select c.eqpid,c.eqpTypeId from bl_cargo c (nolock) where c.bl_no=?";
    private static final String LOADCONFIRM = "select  distinct c.eqpid,c.eqpTypeid,d.status from booking_cargo c (nolock) left join operationdetail d (nolock) on \n"
            + " c.book_no = d.bookno and c.eqpid = d.containerno and d.mode in('F','M')  and d.status='L'  where c.book_no=? ";
    private static final String GETOPERATIONLOADLIST = "select containerno,status from operationdetail (nolock) where bookno = ? and status = 'L' and mode in('F','M')";
    private static final String GET_BOOKING_AGENCY_MAIL = "select agc.contactemailid from agencycontacts  agc inner join agency ag on agc.agencycode=ag.agencycode "
            + " where contactdesignation='VGM Incharge' and ag.agencycode=? and ag.status='A'";
    private static final String GET_ORIGIN_AGENCY = "select agencycode from agencylocations where portcode=? and status='A'";
    private static final String GETBOOKNOFROMBL = "select book_no from bl where bl_no=? and status<>'V'";
    private static final String CHECKAPBOOKING = "select b.book_no from booking b inner join booking_cust bc on b.book_no=bc.book_no and bc.custtypeid='AP' "
            + " where b.status<>'V' and b.book_no=? and bc.custid=?";
    private static final String CHECKAPBL = "select b.bl_no,b.book_no from bl b inner join bl_cust bc on b.bl_no=bc.bl_no and bc.custtypeid='AP' "
            + " where b.status<>'V' and b.bl_no=? and bc.custid=?";

    @Override
    public String getBookingVersion(String type, String referenceNo) throws RemoteException {
        String result = "2";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            if ("blno".equalsIgnoreCase(type)) {
                ps = con.prepareStatement(GETCONTRDETFORBL);
            } else {
                ps = con.prepareStatement(GETCONTRDETFORBOOKING);
            }
            ps.setString(1, referenceNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "1";
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
        return result;
    }

    @Override
    public VGMCommonVO getVGMDetails(String referenceNo, String type) throws RemoteException {
        List<VgmVO> list = null;
        VgmVO vgmvo = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //
        Set<String> set = new HashSet<String>();
        Set<String> operCont = new HashSet<String>();
        String containerNo = null;
        VGMCommonVO vgmCommonVO = null;
        String tareWt = null;
        //
        try {
            list = new ArrayList<VgmVO>();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            //
            ps = con.prepareStatement(GETOPERATIONLOADLIST, 1004, 1007);
            ps.setString(1, referenceNo);
            rs = ps.executeQuery();
            //
            while (rs.next()) {
                containerNo = checkNull(rs.getString("containerno"));
                operCont.add(containerNo.toUpperCase());
            }
            //
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            if ("blno".equalsIgnoreCase(type)) {
                ps = con.prepareStatement(GETCONTRDETFORBL, 1004, 1007);
            } else {
                ps = con.prepareStatement(GETCONTRDETFORBOOKING, 1004, 1007);
            }
            ps.setString(1, referenceNo);
            rs = ps.executeQuery();
            Calendar cal = Calendar.getInstance();
            while (rs.next()) {
                containerNo = checkNull(rs.getString("eqpid"));
                containerNo = containerNo.toUpperCase();
                //
                if (!(set.contains(containerNo) || operCont.contains(containerNo))) {
                    vgmvo = new VgmVO();
                    //System.out.println("[ECOM] EcomV1VGMBean   >>>containerNo.substring(0, 4)" + containerNo.substring(0, 4));
                    if (!("blno".equalsIgnoreCase(type)) && "D".equalsIgnoreCase(rs.getString("status"))) {
                        vgmvo.setDummyCntr(true);
                    } else {
                        vgmvo.setDummyCntr(false);
                    }
                    vgmvo.setBookNo(checkNull(rs.getString("book_no")));
                    vgmvo.setEqpid(containerNo);
                    vgmvo.setTareWeight("");
                    tareWt = getTareWeight(containerNo);
                    if (tareWt != null && tareWt.length() > 0) {
                        vgmvo.setTareWeight(tareWt);
                    }
                    vgmvo.setEqptype(checkNull(rs.getString("eqptypeid")));
                    vgmvo.setVersion_no("0");
                    vgmvo.setVgm_weight("");
                    vgmvo.setVgm_unit("K");
                    vgmvo.setVgm_authorized_person("");
                    vgmvo.setShipperown(checkNull(rs.getString("shipperown")));
                    vgmvo.setVgm_obtained_method("SM1");
                    vgmvo.setVgm_company_name("");
                    vgmvo.setVgm_obtained_date(traget.format(cal.getTime()));
                    vgmvo.setAgencyid(checkNull(rs.getString("agencyid")));
                    vgmvo.setOriginportid(checkNull(rs.getString("originportid")));
                    list.add(vgmvo);
                }
            }
            if (list != null && list.size() > 0) {
                vgmCommonVO = new VGMCommonVO();
                vgmCommonVO.setShName("");
                vgmCommonVO.setSubmissionType(type);
                //System.out.println("[ECOM] EcomV1VGMBean   >>type>>+" + type + ">>" + referenceNo);
                if ("blno".equalsIgnoreCase(type)) {
                    //System.out.println("[ECOM] EcomV1VGMBean   ???entered in bl>>>" + GETUSERDETAILSFORBL);
                    ps = con.prepareStatement(GETUSERDETAILSFORBL, 1004, 1007);
                } else {
                    ps = con.prepareStatement(GETUSERDETAILSFORBOOKING, 1004, 1007);
                }
                ps.setString(1, referenceNo);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ("blno".equalsIgnoreCase(type)) {
                        vgmCommonVO.setVgmStatus("");
                        vgmCommonVO.setBlNo(checkNull(rs.getString("bl_no")));
                    } else {
                        vgmCommonVO.setBlNo(null);
                        vgmCommonVO.setVgmStatus(checkNull(rs.getString("status")));
                    }
                    vgmCommonVO.setSubmissionType(type);
                    vgmCommonVO.setBookNo(checkNull(rs.getString("book_no")));
                    vgmCommonVO.setOrigin(checkNull(rs.getString("originportid")));
                    vgmCommonVO.setDestination(checkNull(rs.getString("finalportid")));
                    vgmCommonVO.setLoadPortId(checkNull(rs.getString("loadportid")));
                    vgmCommonVO.setDischargePortId(checkNull(rs.getString("dischargeportid")));
                    if ("SH".equalsIgnoreCase(checkNull(rs.getString("custtypeid")))) {
                        vgmCommonVO.setShName(checkNull(rs.getString("name")));
                    } else if ("AP".equalsIgnoreCase(checkNull(rs.getString("custtypeid")))) {
                        vgmCommonVO.setApName(checkNull(rs.getString("name")));
                    }
                }
                vgmCommonVO.setVgmContList(list);
            } else {
                vgmCommonVO = null;
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
        return vgmCommonVO;
    }

    @Override
    public String saveVGMDetails(VGMCommonVO vgmVO) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psSel = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        VgmVO vo = null;
        List<VgmVO> vgmList = null;
        String containerResult = "";
        int version = 0;
        boolean isBookingQueryReq = false;
        Map<String, String> hmCon = new HashMap<String, String>();
        String containerNo = null;
        String eqpType = null;
        String bookNo = null;
        String blNo = null, submitType = null;
        String vgmStatus = "A";
        String oprStatus = null;
        try {
            result = "success";
            vgmList = vgmVO.getVgmContList();
            bookNo = vgmVO.getBookNo();
            blNo = vgmVO.getBlNo();
            submitType = vgmVO.getSubmissionType();
            if (vgmList != null) {
                con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                psSel = con.prepareStatement(GETVGMVERSION, 1004, 1007);
                psUpdate = con.prepareStatement(UPDATEVGMVERSION);
                for (int i = 0; i < vgmList.size(); i++) {
                    vo = vgmList.get(i);
                    //System.out.println("[ECOM] EcomV1VGMBean   >> vo.getBookNo()>>" + vo.getBookNo() + ">>> vo.getEqpid()>>" + vo.getEqpid());
                    vo.setVersion_no("0");
                    psSel.setString(1, checkNull(bookNo).trim());
                    psSel.setString(2, checkNull(vo.getEqpid()).trim());
                    rs = psSel.executeQuery();
                    //
                    if (rs.next()) {
                        version = rs.getInt("version_no");
                        //System.out.println("[ECOM] EcomV1VGMBean   >>get version>>>" + version);
                        psUpdate.setString(1, bookNo);
                        psUpdate.setString(2, vo.getEqpid());
                        psUpdate.addBatch();
                        vo.setVersion_no(String.valueOf(version + 1));
                    }

                    vgmList.set(i, vo);
                }
                //
                psUpdate.executeBatch();
                //

                if ("blno".equalsIgnoreCase(submitType)) {
                    ps = con.prepareStatement(GETBOOKNOFROMBL, 1004, 1007);
                    ps.setString(1, blNo);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        bookNo = checkNull(rs.getString("book_no"));
                    }
                    psSel = con.prepareStatement(GETCONTAINERLISTFORBL, 1004, 1007);
                } else {
                    psSel = con.prepareStatement(GETCONTAINERLIST, 1004, 1007);
                }
                psSel.setString(1, bookNo);
                rs = psSel.executeQuery();
                while (rs.next()) {
                    containerNo = checkNull(rs.getString("eqpid"));
                    containerNo = containerNo.toUpperCase();
                    hmCon.put(containerNo, rs.getString("eqpTypeId"));
                }

                //set VGM status
                if (!("blno".equalsIgnoreCase(submitType)) && "D".equalsIgnoreCase(vgmVO.getVgmStatus())) {
                    vgmStatus = "C";
                    oprStatus = "E";
                }
                //insert vgm details into ecom db
                /**
                 * ps = con_ecom.prepareStatement(DELETEECOMVGMDET);
                 * ps.setString(1, bookNo); ps.executeUpdate();*
                 */

                //insert vgm details into LRP db
                //System.out.println("[ECOM] EcomV1VGMBean   >>>vgmList>>>" + vgmList.size());
                ps = con.prepareStatement(INSERTVGMSOLAS);
                for (int i = 0; i < vgmList.size(); i++) {
                    vo = vgmList.get(i);
                    containerNo = checkNull(vo.getEqpid());
                    eqpType = checkNull(vo.getEqptype());
                    eqpType = eqpType.length() > 0 ? eqpType : hmCon.get(containerNo.toUpperCase());
                    //
                    //System.out.println("[ECOM] EcomV1VGMBean   vo.getBookNo " + vo.getBookNo() + " vo.getEqpType " + eqpType
                         //   + " vo.getContainerNo " + containerNo + " vo.getObtainedDate " + vo.getVgm_obtained_date()
                       //     + " vo.getVgmWeight " + vo.getVgm_weight() + " vo.getVgmUnit " + vo.getVgm_unit()
                      //      + " vo.getObtaniedMethod " + vo.getVgm_obtained_method() + " vo.getAuthorizedPerson " + vo.getVgm_authorized_person());
                    ps.setString(1, bookNo);
                    ps.setString(2, eqpType);
                    ps.setString(3, containerNo);
                    ps.setString(4, "C");
                    ps.setString(5, vo.getVgm_ref_number());
                    ps.setString(6, vo.getVgm_order_number());
                    ps.setString(7, vo.getVgm_obtained_date());
                    ps.setString(8, vo.getVgm_weight());
                    ps.setString(9, vo.getVgm_unit());
                    ps.setString(10, vo.getVgm_obtained_method());
                    ps.setString(11, checkNull(vo.getVgm_company_name()).toUpperCase());
                    ps.setString(12, vo.getVgm_address1());
                    ps.setString(13, vo.getVgm_address2());
                    ps.setString(14, vo.getVgm_zipcode());
                    ps.setString(15, vo.getVgm_city());
                    ps.setString(16, vo.getVgm_country());
                    ps.setString(17, checkNull(vo.getVgm_authorized_person()).toUpperCase());
                    ps.setString(18, vo.getTelephone());
                    ps.setString(19, vo.getFax());
                    ps.setString(20, vo.getEmail());
                    ps.setString(21, "");
                    ps.setString(22, vgmStatus);
                    ps.setString(23, vo.getVersion_no());
                    ps.setString(24, "ECOM");
                    ps.setString(25, "ECOM");
                    ps.setString(26, oprStatus);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

        } catch (Exception e) {
            result = "failure";
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psSel != null) {
                    psSel.close();
                }
                if (psUpdate != null) {
                    psUpdate.close();
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
        return result;
    }

    @Override
    public String checkLoadConfirm(String bookNo, String blNo, String submissionType, String custCode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String isLoadConfirm = "NO";
        int loadCount = 0;
        int nonLoadCount = 0;
        String status = null;
        boolean isValidBooking = false;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            if ("blno".equalsIgnoreCase(submissionType)) {
                ps = con.prepareStatement(CHECKAPBL, 1004, 1007);
                ps.setString(1, blNo);
                ps.setString(2, custCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    isValidBooking = true;
                    bookNo = checkNull(rs.getString("book_no"));
                } else {
                    isValidBooking = false;
                    bookNo = null;
                }
            } else {
                ps = con.prepareStatement(CHECKAPBOOKING, 1004, 1007);
                ps.setString(1, bookNo);
                ps.setString(2, custCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    isValidBooking = true;
                } else {
                    isValidBooking = false;
                }
            }
            if (isValidBooking) {
                if (bookNo != null && bookNo.length() > 0) {
                    ps = con.prepareStatement(LOADCONFIRM, 1004, 1007);
                    ps.setString(1, bookNo);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        status = checkNull(rs.getString("status"));
                        if ("L".equalsIgnoreCase(status)) {
                            loadCount = 1;
                        } else {
                            nonLoadCount = 1;
                        }
                    }
                    //
                    if (loadCount == 1 && nonLoadCount == 0) {
                        isLoadConfirm = "YES";
                    }
                } else {
                    isLoadConfirm = "NO";
                }
            } else {
                isLoadConfirm = "NOTVALIDBOOKING";
            }
            //System.out.println("[ECOM] EcomV1VGMBean   >>>isLoadConfirm>>>" + isLoadConfirm);
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
        return isLoadConfirm;
    }
    //

    @Override
    public String checkDuplicateContainers(List<String> containerList, String refNo, String type) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> set = new HashSet<String>();
        String container = null;
        StringBuffer stb = new StringBuffer();
        boolean isValidationFail = false;
        int size = 0;
        int isFirst = 0;
        String bookNo = null;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            if ("blno".equalsIgnoreCase(container)) {
                ps = con.prepareStatement(GETBOOKNOFROMBL, 1004, 1007);
                ps.setString(1, refNo);
                rs = ps.executeQuery();
                if (rs.next()) {
                    bookNo = checkNull(rs.getString("book_no"));
                }
                ps = con.prepareStatement(GETCONTAINERLISTFORBL, 1004, 1007);
            } else {
                bookNo = refNo;
                ps = con.prepareStatement(GETCONTAINERLIST, 1004, 1007);
            }

            ps.setString(1, refNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                set.add(rs.getString("eqpid").toUpperCase().trim());
            }
            //
            size = containerList.size();
            for (int i = 0; i < size; i++) {
                container = containerList.get(i).trim().toUpperCase();
                if (!set.contains(container)) {
                    if (isFirst > 0) {
                        stb.append(",");
                    }
                    stb.append(containerList.get(i));
                    isFirst = 1;
                    isValidationFail = true;
                }
            }
            //
            stb.append("-");
            isFirst = 0;
            set.clear();
            //
            ps = con.prepareStatement(GETOPERATIONLOADLIST, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            //
            while (rs.next()) {
                set.add(checkNull(rs.getString("containerno").toUpperCase()));
            }
            //
            size = containerList.size();
            for (int i = 0; i < size; i++) {
                container = containerList.get(i).trim().toUpperCase();
                if (set.contains(container)) {
                    if (isFirst > 0) {
                        stb.append(",");
                    }
                    stb.append(containerList.get(i));
                    //System.out.println("[ECOM] EcomV1VGMBean   >>>>sb" + stb.toString());
                    isFirst = 1;
                    isValidationFail = true;
                }
            }
            //
            if (isValidationFail) {
                result = "Invalid Container Nos-" + stb.toString();
            } else {
                result = "Valid";
            }
            //System.out.println("[ECOM] EcomV1VGMBean   result>>>" + result);
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

        return result;
    }
    //

    public String getTareWeight(String containerNo) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tareWt = 0;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GETTAREWEIGHT, 1004, 1007);
            ps.setString(1, containerNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = checkNull(rs.getString("tarewt1"));
                //System.out.println("[ECOM] EcomV1VGMBean   >>>.tareweigth>>>" + result);
                result = result.isEmpty() || Float.parseFloat(result) < 1 ? "" : result;
            } else {
                result = "";
            }
            //System.out.println("[ECOM] EcomV1VGMBean   >>>container tare weight>>>" + result);
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

        return result;
    }

    @Override
    public Map<String, String> getBCCMailidForAgency(String agencyCode, String originLocation) throws RemoteException {
        Map<String, String> mailMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String originAgency = null;
        boolean checkFirst = true, mailCheck = false;
        String agencymailId = null;
        try {
            mailMap = new HashMap<String, String>();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GET_ORIGIN_AGENCY, 1004, 1007);
            ps.setString(1, originLocation);
            rs = ps.executeQuery();
            if (rs.next()) {
                originAgency = checkNull(rs.getString("agencycode"));
            }
            //System.out.println("[ECOM] EcomV1VGMBean   >>>originAgency>>" + originAgency);
            ps = con.prepareStatement(GET_BOOKING_AGENCY_MAIL, 1004, 1007);
            ps.setString(1, agencyCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                mailCheck = true;
                if (checkFirst) {
                    checkFirst = false;
                    agencymailId = checkNull(rs.getString("contactemailid"));
                } else {
                    agencymailId = agencymailId + "," + checkNull(rs.getString("contactemailid"));
                }
            }
            if (mailCheck) {
                mailMap.put("BOOKINGAGENCY", agencymailId);
            } else {
                mailMap.put("BOOKINGAGENCY", "");
            }
            mailMap.put("ORIGINAGENCY", "");
            if (originAgency != null && originAgency.equalsIgnoreCase(agencyCode)) {
                mailMap.put("AGENCYEQUAL", "Equal");
            } else {
                mailMap.put("AGENCYEQUAL", "Not Equal");
                ps = con.prepareStatement(GET_BOOKING_AGENCY_MAIL, 1004, 1007);
                agencymailId = null;
                mailCheck = false;
                checkFirst = true;
                ps.setString(1, originAgency);
                rs = ps.executeQuery();
                while (rs.next()) {
                    mailCheck = true;
                    if (checkFirst) {
                        checkFirst = false;
                        agencymailId = checkNull(rs.getString("contactemailid"));
                    } else {
                        agencymailId = agencymailId + "," + checkNull(rs.getString("contactemailid"));
                    }
                }
                if (mailCheck) {
                    mailMap.put("ORIGINAGENCY", agencymailId);
                } else {
                    mailMap.put("ORIGINAGENCY", "");
                }
            }
            //System.out.println("[ECOM] EcomV1VGMBean   mailMap>>>" + mailMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mailMap;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
