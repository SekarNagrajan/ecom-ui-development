/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomV1SIBeanRemote {

    public HashMap fetchV1BookingDetForSI(String bookno) throws RemoteException;

    public Map<SIMasterVO, SIConatainerValueVO> fetchV1ContainerDetailForSI(String bookno) throws RemoteException;

    public HashMap fetchPortDetForSI(String bookno) throws RemoteException;

    public String checkV1BLStatus(String bookNo) throws RemoteException;

    public ReturnVO saveToSI(String bookno, String custcode, String bookinguserid, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype, String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no, String client_code, String addremarks) throws RemoteException;
    
    public List getV1SICustomerInfo(String columnName, String constraints, String columnValue, String custCode) throws RemoteException;
    
    public String cancelSubmittedV1SI(String siNo, String bookNo) throws RemoteException; 
}
