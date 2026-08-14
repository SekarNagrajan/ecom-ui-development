/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.vo.VGMCommonVO;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomV1VGMBeanRemote {

    public String checkLoadConfirm(String bookNo, String blNo, String submissionType, String custCode) throws RemoteException;
    
    public VGMCommonVO getVGMDetails(String referenceNo, String type) throws RemoteException;
    
    public String checkDuplicateContainers(List<String> containerList, String refNo, String type) throws RemoteException;
    
    public Map<String, String> getBCCMailidForAgency(String agencyCode, String originLocation) throws RemoteException;
    
    public String saveVGMDetails(VGMCommonVO vgmVO) throws RemoteException;
    
    public String getBookingVersion(String type,String referenceNo) throws RemoteException;
}
