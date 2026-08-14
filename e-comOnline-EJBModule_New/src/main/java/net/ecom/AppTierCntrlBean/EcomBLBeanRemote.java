/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import javax.ejb.Remote;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.ecom.vds.serviceVo.NoticeVO;
import net.ecom.vds.serviceVo.BLChargeVo;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.OBLPrintVo;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;

@Remote
public interface EcomBLBeanRemote {

    public HashMap fetchPortDetForBL(String blNum);

    public HashMap fetchBookingDetForSI(String bookno);

    public List<OBLPrintVo> getBLSummary(String Custid, String custTypeConfig, String v1DataEnableStatus, String blbatchPrint) throws RemoteException;

    public List<OBLPrintVo> getMCNSummary(String Custid, String custTypeConfig, String v1DataEnableStatus) throws RemoteException;

    public String getBlSignatureStatus(String blno, String signature, String Shipmentstatus);

    public Map<SIMasterVO, SIConatainerValueVO> fetchSIContainerDetail(String bookno, String sino) throws RemoteException;

    public Map<SIMasterVO, SIConatainerValueVO> fetchSIContainerDetailForDraft(String blno) throws RemoteException;

    public HashMap fetchSICustomerDetails(String bookno, String sino) throws RemoteException;

    public String getBLVersionNumber(String blno);

    public String UpdateBLStatus(String blno, String type, String usercode);

    public String AgencySigmature(String agency_code) throws RemoteException;

    public ReturnVO saveToBL(String bookno, String custcode, String bookinguserid, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype, String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no, String client_code, FileDetailsVO fileDetailsVO, String addremarks, String appVersion, String ccMailId, String isCCMail, Map custMap) throws RemoteException;

    public HashMap fetchAddremarks(String blno, String type) throws RemoteException;

    public SIPartiesVO getBLMasterDetails(String blno) throws RemoteException;

    public String cancelSubmittedBL(String siNo) throws RemoteException;

    public ReturnVO acceptBLByUser(String blNo, String agencycode) throws RemoteException;

    public Map getBlReference(String blno) throws RemoteException;

    public List getBLCharges(String blno) throws RemoteException;

    public String getBLAPCustomer(String blno) throws RemoteException;

    public Map<String, String> getBLStatus(String blno) throws RemoteException;

    public String getisvoyage(String blno) throws RemoteException;

    public HashMap fetchPortDetForMCN(String fcnno) throws RemoteException;

    public HashMap fetchBookingDetForMCN(String fcnno) throws RemoteException;

    public Map<SIMasterVO, SIConatainerValueVO> fetchMCNContainerDetailForDraft(String fcnno) throws RemoteException;

    public SIPartiesVO getMCNMasterDetails(String fcnno) throws RemoteException;

    public Map getMCNReference(String fcnno) throws RemoteException;

    public boolean getBLServiceIsSignature(String agencycode) throws RemoteException;

    public String checkbloperationdetails(String blNo) throws RemoteException;

    public String getMCNStatus(String fcnno) throws RemoteException;

    public ENSDeclarantVo getBLENSTabDetails(String blno) throws RemoteException;

    public List<BLChargeVo> getBlCgargeSummaryView(String blno) throws RemoteException;

    public List<BLChargeVo> getBlCgargeSummaryList(String blno) throws RemoteException;

    public List<BLChargeVo> getSICgargeSummaryList(String blno) throws RemoteException;

    public HashMap fetchBookingCustForSI(String blno);

    public HashMap fetchSICustDetail(String bookno, String sino) throws RemoteException;

    public String fetForBLENSFiling(String blNum) throws RemoteException;

}
