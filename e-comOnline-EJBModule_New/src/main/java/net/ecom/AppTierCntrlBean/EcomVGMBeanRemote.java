/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

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
public interface EcomVGMBeanRemote {

    public void check() throws RemoteException;

    public VGMCommonVO getVGMDetails(String referenceNo, String type) throws RemoteException;

    public String saveVGMDetails(VGMCommonVO vgmVO) throws RemoteException;

    public String checkLoadConfirm(String bookNo, String blNo, String submissionType, String custCode) throws RemoteException;

    public String checkDuplicateContainers(List<String> containerList, String refNo, String type, Map<String, String> eqpTypeMap) throws RemoteException;

    public Map<String, String> getBCCMailidForAgency(String agencyCode, String originLocation) throws RemoteException;

    public String checkVGMContainerNo(String containerNo, String eqpType) throws RemoteException;

    public String getTareWeight(String containerNo, String eqpType) throws RemoteException;
    
    public String getAgencyMailForVGM(String agencycode) throws RemoteException;
    
    public String togetTareWtForCntr(String cntrNo, String eqpSize,String nor,String soc) throws RemoteException;


}
