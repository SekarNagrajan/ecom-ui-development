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
import net.ecom.phase2.service.vo.ARNSummaryVO;
import net.ecom.phase2.service.vo.CROSummaryVO;
import net.ecom.phase2.service.vo.DOStatusUpdateVO;
import net.ecom.phase2.service.vo.DOSummaryVO;
import net.ecom.phase2.service.vo.PaymentHistoryVO;
import net.ecom.phase2.service.vo.SurchargeSummaryVO;
import net.ecom.phase2.service.vo.TariffSummaryVO;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomPhase2BeanRemote {

    public List<CROSummaryVO> getCROSummaryDetails(String custCode, String fromDate, String toDate, String bookNo, String releaseNo) throws RemoteException;

    public List<CROSummaryVO> getCRODetails(String bookNo, String releaseNo, String eqpType) throws RemoteException;

    public List<ARNSummaryVO> getARNSummary(String custCode, String fromDate, String toDate) throws RemoteException;

    public List<DOSummaryVO> getDOSummary(String custCode, String fromDate, String toDate) throws RemoteException;

    public List<TariffSummaryVO> getTarriffSummary(String loadPort, String dischPort, String commoditycode, String eqpType, String shipmentDate, String carriercode,String ratesplugin, String showExternalRateTotalAmount, String checkRouting) throws RemoteException;

    public List<SurchargeSummaryVO> getSurchargeSummary(String loadPort, String dischargePort, String eqpType, String shipmentDate) throws RemoteException;

    public List<SurchargeSummaryVO> getSurchargeSummaryNew(String loadPort, String dischargePort, String eqpType, String shipmentDate) throws RemoteException;

    public DOStatusUpdateVO updateDOStatus(String delOrdNo) throws RemoteException;

    public Map<String, List> getAccountSummary(String strType, String custCode, String fromDate, String toDate, String option) throws RemoteException;

    public Map<String, List> getNewAccountSummaryFromCCR(String fromDate,String custCode,String carrierCode) throws RemoteException;

    public Map<String, List> getInvoiceDetails(String strType, String custCode, String fromDate, String toDate, String option) throws RemoteException;

    public Map<String, List> getPaymentDetials(String strType, String custCode, String fromDate, String toDate, String option) throws RemoteException;

    public Map<String, List> getOutstandingDetails(String strType, String custCode, String fromDate, String toDate, String option) throws RemoteException;
    
    public Map<String, List> getCreditCustomerStatementDetails(String ondate, String custCode, String carriercode) throws RemoteException;
    
    public Map getRateContract(String shipmentdate,String custcode,String pol,String pod,String eqpType,String commodity);
    
    public Map getRateContractQuote(String shipmentdate,String custcode,String pol,String pod,String eqpType,String commodity);

    public Map<String, List> getAccountSummaryfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException;

    public Map<String, List> getInvoiceDetailsfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException;

    public Map<String, List> getPaymentDetialsfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException;

    public Map<String, List> getOutstandingDetailsfromcuststmtsp(String strType, String custCode, String fromDate, String toDate, String option, String carrierCode) throws RemoteException;

    public List<PaymentHistoryVO> getpaymentHistoryDetails(String custCode, String fromDate, String toDate, String bookNo, String paymentHistoryCompCode) throws RemoteException;
    
}
