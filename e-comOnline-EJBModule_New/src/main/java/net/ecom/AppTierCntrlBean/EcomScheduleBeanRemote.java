/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.vo.Route;

/**
 *
 * @author gobinath.t
 */
@Remote
public interface EcomScheduleBeanRemote {

    public List geteBookRoutingInfo(String origin, String delivery, String fromdate, String todate,String def_route_plug, List<String> servList, List<String> feederlis) throws RemoteException;
}
