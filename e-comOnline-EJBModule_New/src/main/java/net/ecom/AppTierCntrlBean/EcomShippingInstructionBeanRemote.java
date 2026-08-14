/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;

@Remote
public interface EcomShippingInstructionBeanRemote {

    public ArrayList getSISummary(String strUserId, String custTypeConfig, String v1DataEnableStatus, String SICreForCnfrmBk) throws RemoteException;

    public ReturnVO saveToSI(String bookno, String custcode, String bookinguserid, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype, String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username, String tbl_no, String client_code, FileDetailsVO fileDetailsVO, String addremarks, String custTypeConfig, String appVersion, String ccMailId, String isCCMail, String lang, String custCountry, Map customerMap) throws RemoteException;

    public List getSICustomerInfo(String columnName, String constraints, String columnValue, String custCode) throws RemoteException;

    public Map<SIMasterVO, SIConatainerValueVO> fetchContainerDetailForSI(String bookno) throws RemoteException;

    public Map<SIMasterVO, SIConatainerValueVO> fetchSIContainerDetail(String bookno, String sino) throws RemoteException;

    public HashMap fetchPortDetForSI(String bookno);

    public HashMap fetchSICustomerDetails(String bookno, String sino, String custref) throws RemoteException;

    public String saveSI(int sicount, String bookno, String custcode, String login_name);

    public SIPartiesVO getSIMasterDetails(String siid);

    public HashMap fetchSICustomerDetailsFromEcom(String sino) throws RemoteException;

    public String getTareWtForCntr(String cntrNo, String eqpSize, String nor, String soc) throws RemoteException;

    public String getBLNoFromBookNo(String book_no) throws RemoteException;

    public String getSIDeclinedRemarks(String siNo) throws RemoteException;

    public String cancelSubmittedSI(String siNo, String bookNo) throws RemoteException;

    public String getAgencyEmail(String bookNo) throws RemoteException;

    public List getBookingcharges(String bookno);

    public List getSICharges(String sino);

    public String getPrepaidCollectLocation(String book_no, String frieght);

    public List getThirdPartyLocation(String book_no);

    public String checkContainerValidation(String book_no, String containerlisstr);

    public Map getBKGReference(String bkgno) throws RemoteException;

    public boolean checkMaskPortForSIBL(String bookno) throws RemoteException;

    public String getRateCommodityList(String refno, String Type) throws RemoteException;

    public ENSDeclarantVo getENSTabDetails(String sino) throws RemoteException;

    public List getSIMultifilingCustomerInfo(String custCode, String custTypeConfig) throws RemoteException;

    public boolean checkIsInlandPort(String lrpbookno) throws RemoteException;

    public HashMap<String, ArrayList<CustomerDetailsVO>> fetchSICusListFromEcom(String sino) throws RemoteException;

    public HashMap<String, ArrayList<CustomerDetailsVO>> fetchSICustDetail(String bookno, String sino, String custref) throws RemoteException;

    public String fetForSIENSFiling(String bookno) throws RemoteException;

    public ENSDeclarantVo getLRPBookingENSTabDetails(String lrpBookNo) throws RemoteException;
}
