/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.vds.serviceVo.NoticeVO;

/**
 *
 * @author preethi.elumalai
 */
@Stateless(name = "EcomNoticeBean")
//@Remote(EcomNoticeBean.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
    @Resource(name = "jdbc/LRPDB", mappedName = "java:/MSSQLDS")
})
public class EcomNoticeBean {

    public List<NoticeVO> getNoticeSummaryList(int year) throws RemoteException {
        List<NoticeVO> routlst = new ArrayList<NoticeVO>();
        NoticeVO vo = null;
        ResultSet rs1 = null;
        String sql = "select title,filename,cruser,crdate,chgdate from ecom_notice(nolock) where year(CrDate)= ? order by CrDate desc";
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(year));
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                vo = new NoticeVO();
                vo.setTitle(checkNull(rs1.getString("title")));
                vo.setFilename(checkNull(rs1.getString("filename")));
                vo.setChgdate(checkNull(rs1.getString("chgdate")));
                vo.setCrdate(checkNull(rs1.getString("crdate")));
                vo.setCruser(checkNull(rs1.getString("cruser")));
                routlst.add(vo);
            }

//           String json = new Gson().toJson(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ECOM] EcomNoticeBean Exception in getNoticeSummaryList - " + e);
        }
        return routlst;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str)) && !"null".equalsIgnoreCase(str)) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
