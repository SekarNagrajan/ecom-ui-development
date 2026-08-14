package net.ecom.AppTierCntrlBean;

import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.annotation.Resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;
import javax.ejb.SessionContext;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author prakash.mani
 */

@Stateless(name = "EcomAnalyticsBean")
@Remote(EcomAnalyticsBeanRemote.class)
@Resources({
        @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
})
public class EcomAnalyticsBean implements EcomAnalyticsBeanRemote {
    @Resource
    private SessionContext context;

    @PostConstruct
    public void inti() {

    }

    @Override
    public long insertStart(String countryCode, String custCode, String moduleCode) {
        System.out.println("EcomAnalyticsBean.insertStart()----->");

        final String sql = "INSERT INTO ecomUsageAnalyticsLog (countrycode, custcode, modulecode, starttime) VALUES (?, ?, ?, getdate())";
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, countryCode);
            ps.setString(2, custCode);
            ps.setString(3, moduleCode);
            int count = ps.executeUpdate();

            if (count >= 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    System.out.println("Inserted ID: " + id);
                    return id;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public int closeEndTime(long id) {
        System.out.println("EcomAnalyticsBean.closeEndTime()----->");
        final String sql = "UPDATE ecomUsageAnalyticsLog SET endtime = getdate() WHERE id = ?";
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            System.out.println("closeEndTime: " + ps);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long insertModuleStatus(String custcode, String moduleCode, String actionStatus) {
        String cc = (custcode == null || custcode.trim().isEmpty()) ? null : custcode.trim();
        String mod = moduleCode == null ? "" : moduleCode.trim();
        String st = actionStatus == null ? "" : actionStatus.trim();
        if (mod.length() > 3)
            mod = mod.substring(0, 3);
        if (st.length() > 2)
            st = st.substring(0, 2);

        final String sql = "INSERT INTO ecomWorkflowAnalyticsLog (custcode, modulecode, actionStatus,crDate) VALUES (?, ?, ?, getdate())";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (cc == null)
                ps.setNull(1, Types.VARCHAR);
            else
                ps.setString(1, cc);
            ps.setString(2, mod);
            ps.setString(3, st);

            int updated = ps.executeUpdate();
            if (updated == 0)
                return 0L;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs != null && rs.next()) {
                    long id = rs.getLong(1);
                    System.out.println("insertModuleStatus Inserted ID: " + id);
                    return id;
                }
                ;
            }

            // Fallback for drivers that don’t return keys reliably
            try (Statement s2 = con.createStatement();
                    ResultSet r2 = s2.executeQuery("SELECT CAST(SCOPE_IDENTITY() AS BIGINT)")) {
                if (r2.next())
                    return r2.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public int closeModuleStatus(long id) {
        final String sql = "UPDATE ecomWorkflowAnalyticsLog SET actionStatus='C' WHERE id=?";
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate(); // 1 on success, 0 if not found

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public long insertRouteLog(String custcode, String originPort, String destPort) {
        String org = originPort.trim().toUpperCase();
        if (org.length() > 5)
            org = org.substring(0, 5);
        String dst = destPort.trim().toUpperCase();
        if (dst.length() > 5)
            dst = dst.substring(0, 5);

        final String sql = "INSERT INTO dbo.ecomRouteSearchLog (custcode, originport, destport,crDate) VALUES (?, ?, ?, getDate())";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, custcode);
            ps.setString(2, org);
            ps.setString(3, dst);

            int updated = ps.executeUpdate();
            if (updated == 0)
                return 0L;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs != null && rs.next()) {
                    long id = rs.getLong(1);
                    System.out.println("insertModuleStatus Inserted ID: " + id);
                    return id;
                }
                ;
            }
            try (Statement s2 = con.createStatement();
                    ResultSet r2 = s2.executeQuery("SELECT CAST(SCOPE_IDENTITY() AS BIGINT)")) {
                if (r2.next())
                    return r2.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    // 1) Module Usage Tracking (visit counts within date range)
    @Override
    public List<Map<String, Object>> moduleUsageCounts(String startDate, String endDate, String custcode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT modulecode, COUNT(*) AS visit_count ");
        sql.append("FROM dbo.ecomUsageAnalyticsLog(NOLOCK) ");
        sql.append("WHERE starttime >= ? AND starttime <= ? ");
        
        if (custcode != null && !custcode.trim().isEmpty()) {
            sql.append("AND custcode = ? ");
        }
        
        sql.append("GROUP BY modulecode ");
        sql.append("ORDER BY visit_count DESC");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            int paramIndex = 3;
            if (custcode != null && !custcode.trim().isEmpty()) {
                ps.setString(paramIndex, custcode.trim());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("module_code", rs.getString("modulecode"));
                    row.put("visit_count", rs.getLong("visit_count"));
                    out.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    // 2) Time Spent per Module (within date range)
    @Override
    public List<Map<String, Object>> hoursThisWeekPerModule(String startDate, String endDate, String custcode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT modulecode, ");
        sql.append("CAST(SUM(CASE WHEN endtime IS NOT NULL THEN DATEDIFF(SECOND, starttime, endtime) ELSE 0 END) / 3600.0 AS DECIMAL(18,2)) AS hours_this_week ");
        sql.append("FROM dbo.ecomUsageAnalyticsLog(NOLOCK) ");
        sql.append("WHERE starttime >= ? AND starttime <= ? AND endtime IS NOT NULL ");
        
        if (custcode != null && !custcode.trim().isEmpty()) {
            sql.append("AND custcode = ? ");
        }
        
        sql.append("GROUP BY modulecode ");
        sql.append("ORDER BY hours_this_week DESC");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            int paramIndex = 3;
            if (custcode != null && !custcode.trim().isEmpty()) {
                ps.setString(paramIndex, custcode.trim());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("module_code", rs.getString("modulecode"));
                    row.put("hours_this_week", rs.getBigDecimal("hours_this_week")); // DECIMAL(18,2)
                    out.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    // 3) Completed vs Abandoned (anything not 'C' is Abandoned) within date range
    @Override
    public List<Map<String, Object>> completedVsAbandoned(String startDate, String endDate, String custcode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT modulecode, ");
        sql.append("SUM(CASE WHEN actionStatus = 'C' THEN 1 ELSE 0 END) AS completed, ");
        sql.append("SUM(CASE WHEN actionStatus <> 'C' THEN 1 ELSE 0 END) AS abandoned, ");
        sql.append("COUNT(*) AS total, ");
        sql.append("CAST(100.0 * SUM(CASE WHEN actionStatus <> 'C' THEN 1 ELSE 0 END) / COUNT(*) AS DECIMAL(6,2)) AS abandoned_pct ");
        sql.append("FROM dbo.ecomWorkflowAnalyticsLog(NOLOCK) ");
        sql.append("WHERE crDate >= ? AND crDate <= ? ");
        
        if (custcode != null && !custcode.trim().isEmpty()) {
            sql.append("AND custcode = ? ");
        }
        
        sql.append("GROUP BY modulecode ");
        sql.append("ORDER BY modulecode");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            int paramIndex = 3;
            if (custcode != null && !custcode.trim().isEmpty()) {
                ps.setString(paramIndex, custcode.trim());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("module_code", rs.getString("modulecode"));
                    row.put("completed", rs.getLong("completed"));
                    row.put("abandoned", rs.getLong("abandoned"));
                    row.put("total", rs.getLong("total"));
                    row.put("abandoned_pct", rs.getBigDecimal("abandoned_pct")); // DECIMAL(6,2)
                    out.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    // 4) User Location (Country-Based): distinct customers per country within date range
    @Override
    public List<Map<String, Object>> uniqueCustomersByCountry(String startDate, String endDate, String custcode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT countrycode, COUNT(DISTINCT Custcode) AS unique_customers ");
        sql.append("FROM dbo.ecomUsageAnalyticsLog (NOLOCK) ");
        sql.append("WHERE starttime >= ? AND starttime <= ? ");
        
        if (custcode != null && !custcode.trim().isEmpty()) {
            sql.append("AND custcode = ? ");
        }
        
        sql.append("GROUP BY countrycode ");
        sql.append("ORDER BY unique_customers DESC");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            int paramIndex = 3;
            if (custcode != null && !custcode.trim().isEmpty()) {
                ps.setString(paramIndex, custcode.trim());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("country_code", rs.getString("countrycode"));
                    row.put("unique_customers", rs.getLong("unique_customers"));
                    out.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    // 5) Top Search Routes within date range, return Top K (origin, dest, count)
    @Override
    public List<Map<String, Object>> topRoutes(String startDate, String endDate, String custcode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TOP 10 ");
        sql.append("UPPER(originport) + '-' + UPPER(destport) AS route, ");
        sql.append("COUNT(*) AS search_count ");
        sql.append("FROM dbo.ecomRouteSearchLog (NOLOCK)");
        sql.append("WHERE crDate >= ? AND crDate <= ? ");
        
        if (custcode != null && !custcode.trim().isEmpty()) {
            sql.append("AND custcode = ? ");
        }
        
        sql.append("GROUP BY UPPER(originport), UPPER(destport) ");
        sql.append("ORDER BY search_count DESC");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            int paramIndex = 3;
            if (custcode != null && !custcode.trim().isEmpty()) {
                ps.setString(paramIndex, custcode.trim());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("route", rs.getString("route"));
                    row.put("search_count", rs.getLong("search_count"));
                    out.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public List<Map<String, Object>> customerSearch(String searchTerm) {
        System.out.println("EcomAnalyticsBean.customerSearch()----->");
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.custcode, m.custname ");
        sql.append("FROM ecomuserdetails e (NOLOCK) ");
        sql.append("INNER JOIN customermaster m (NOLOCK) ON m.custcode = e.customercode ");
        sql.append("INNER JOIN customercontacts c (NOLOCK) ON m.custcode = c.custcode ");
        sql.append("WHERE c.onlineactive = 'A' AND c.status = 'A' AND c.contacttype = 'online' ");
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append("AND (UPPER(m.custname) LIKE UPPER(?) OR UPPER(m.custcode) LIKE UPPER(?)) ");
        }
        
        sql.append("ORDER BY m.custname");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String searchPattern = "%" + searchTerm.trim() + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("custcode", rs.getString("custcode"));
                    row.put("custname", rs.getString("custname"));
                    out.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public List<Map<String, Object>> loadAllCustomers() {
        System.out.println("EcomAnalyticsBean.loadAllCustomers()----->");
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.custcode, m.custname ");
        sql.append("FROM ecomuserdetails e (NOLOCK) ");
        sql.append("INNER JOIN customermaster m (NOLOCK) ON m.custcode = e.customercode ");
        sql.append("INNER JOIN customercontacts c (NOLOCK) ON m.custcode = c.custcode ");
        sql.append("WHERE c.onlineactive = 'A' AND c.status = 'A' AND c.contacttype = 'online' ");
        sql.append("ORDER BY m.custname");
        
        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            System.out.println("Executing SQL: " + sql.toString());
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("custcode", rs.getString("custcode"));
                    row.put("custname", rs.getString("custname"));
                    out.add(row);
                    count++;
                }
                System.out.println("loadAllCustomers: Found " + count + " customers");
            }
        } catch (Exception e) {
            System.err.println("Error in loadAllCustomers: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("loadAllCustomers: Returning " + out.size() + " customers");
        return out;
    }

}
