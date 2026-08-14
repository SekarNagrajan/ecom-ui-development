/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.vo.CustomerAdvisoryVo;
import net.ecom.vo.ImageUploadVo;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomAdminCntrlBeanRemote {

    public List<CustomerAdvisoryVo> getCustomerAdvisoryDetails() throws RemoteException;

    public List getImageUploadDetails(String showactive) throws RemoteException;

    public String saveCustomerAdvisoryDetails(CustomerAdvisoryVo cusadvvo) throws RemoteException;

    public String updateCustomerAdvisoryDetails(CustomerAdvisoryVo cusadvvo) throws RemoteException;

    public String updateImageUploadDetails(ImageUploadVo imgupvo) throws RemoteException;

    public String deleteImageUploadDetails(int key, String status) throws RemoteException;

    public String updateImageUploadRearrangedDetails(List<ImageUploadVo> rearrangelist) throws RemoteException;

    public String saveImageUploadDetails(ImageUploadVo imgupvo) throws RemoteException;

    public String deleteCustomerAdvisoryDetails(int key) throws RemoteException;
    
    public List getEcomCpanelMenudetails() throws RemoteException;
    
    public List getEcomOnGoingTransactionDetails(String usercode)  throws RemoteException;
    
    public List getEcomOnGoingTransactionCargoDetails(String bookno,String blno)  throws RemoteException;
    
    public String getDashboardOutstandingBalance(String fromdate,String custcode) throws RemoteException;
    
    public List getEcomUnclearedCargoDetails(String custcode) throws RemoteException;

    public String getDashboardOutstandingBalfromcuststmtsp(String fromdate, String custcode) throws RemoteException;
}
