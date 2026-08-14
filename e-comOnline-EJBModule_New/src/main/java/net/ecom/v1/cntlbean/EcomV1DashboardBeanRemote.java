/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomV1DashboardBeanRemote {

    public List<net.ecom.vds.serviceVo.DashboardVO> getV1EcomTopDestinationDashboard(List<net.ecom.vds.serviceVo.DashboardVO> list, String custCode) throws RemoteException;

    public List<net.ecom.vds.serviceVo.DashboardVO> getV1EcomTopDestinationTeuDashboard(List<net.ecom.vds.serviceVo.DashboardVO> list, String custCode) throws RemoteException;

    public List<net.ecom.vds.serviceVo.DashboardVO> getV1EcomTopPortPairDashboard(List<net.ecom.vds.serviceVo.DashboardVO> list, String custCode) throws RemoteException;
}
