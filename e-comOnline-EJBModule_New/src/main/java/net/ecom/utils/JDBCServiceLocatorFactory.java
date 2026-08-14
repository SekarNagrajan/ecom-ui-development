/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.utils;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JDBCServiceLocatorFactory {

    private static DataSource dsEcomm;
    private static DataSource dsGate;
    private static Context ctx;

    public static synchronized Connection getGateConnection() throws SQLException {
        Connection conn = null;
        try {
            ctx = getContext();
            dsGate = (DataSource) ctx.lookup("java:/MSSQLDS");
            conn = dsGate.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static synchronized Connection getEcommConnection() throws SQLException {
        Connection conn = null;
        try {
            ctx = getContext();
            dsEcomm = (DataSource) ctx.lookup("java:/ECOMTXDS");
            conn = dsEcomm.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getConnection(String ds) throws SQLException {
        if (ds != null) {
            try {
                DataSource datasource = ((DataSource) new InitialContext().lookup("java:/" + ds));
                return datasource.getConnection();
            } catch (NamingException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void close(PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    private static Context getContext() {
        try {
            Properties p = new Properties();
            p.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            p.put("java.naming.factory.url.pkgs", "org.jboss.naming");
            ctx = new InitialContext(p);

        } catch (Exception e) {
            System.out.println("[ECOM] JDBCServiceLocatorFactory Error when getting Conext");
        }
        return ctx;
    }
}
