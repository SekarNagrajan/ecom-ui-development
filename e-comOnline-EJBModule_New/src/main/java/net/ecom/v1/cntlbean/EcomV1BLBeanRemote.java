/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
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
public interface EcomV1BLBeanRemote {

    public HashMap fetchV1BLDetForSI(String bookno) throws RemoteException;

    public SIPartiesVO getV1BLMasterDetails(String blno) throws RemoteException;

    public Map<SIMasterVO, SIConatainerValueVO> fetchContainerDetailForSI(String bookno) throws RemoteException;

    public HashMap fetchV1PortDetForBL(String bookno) throws RemoteException;

    public ReturnVO v1SaveToBL(String bookno, String custcode, String bookinguserid, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype, String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no, String client_code, String addremarks) throws RemoteException;
    
    public String cancelSubmittedV1BL(String siNo) throws RemoteException;
    
    public String getV1BLAgencySigmature(String agency_code, String bookno, String shipstatus) throws RemoteException;
    
     public String updateV1BLStatus(String blno, String type, String usercode)throws RemoteException;

}
