package net.ecom.AppTierCntrlBean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.vo.BkgContainerHaulageDetails;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.CargoProtectVo;
import net.ecom.vo.CommoditySearchVO;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;
import net.ecom.vo.SurveyList;

@Remote
public interface EcomBookingBeanRemote {

    public ArrayList getBookingDetail(String usercode, String fromDate, String toDate, String type, String custType, String v1DataEnableStatus) throws RemoteException;

    public Map<String, List> geteBookRoutingInfo(String origin, String delivery, String gateinDate, String preCarriage, String onCarriage, String strDateFormat, String porttype, String pol, String pod, String fromdate, String todate, String origintype, String deliverytype, List<String> servList, List<String> feederlis, String def_route_plug, String search_type, Map ecomplugin) throws RemoteException;

    public ArrayList getEbookingDetails(String bookNo, String flag, String action) throws RemoteException;

    public List getEbookingCargoDetails(String bookNo,String flag, String clearTemperatureWhileCopy) throws RemoteException;

    public ArrayList getBookingColumns(String userCode) throws RemoteException;

    public List getCustomerData(String columnName, String constraints, String columnValue) throws RemoteException;

    public List geteBookingSearch() throws RemoteException;

    public List geteBookingCustomerInfo(String columnName, String constraints, String columnValue, String custCode, String custTypeConfig, String fieldName) throws RemoteException;

    public ReturnVO saveToBooking(EbookingValidatorFormBeanNew eBookingData, ArrayList all, String custcode, String custname, String bookno, String savetype, ArrayList voidCargoListObj, ArrayList voidComListObj, String eqpQuantity, String client_code, String strtempname, String slottype, String ApEmailID, FileDetailsVO fls, String custTypeConfig, String custCountry,Map custMap) throws RemoteException;

    public String getbkgconfigDetails(String agencycode, String maxcode, String mincode) throws RemoteException;

    public String getAgencydetails(String port) throws RemoteException;

    public String getPortType(String portcode) throws RemoteException;

    public ArrayList getLRPBookingViewDetails(String bookNo) throws RemoteException;

    public List getMoreCustomerDetails(String custCode, String custType) throws RemoteException;

    public ArrayList getLRPBookingViewCargoDetails(String bookNo, String ecombookno) throws RemoteException;

    public String getUNODetails(String eqpsize) throws RemoteException;

    public String getIMDGClassDetails(String unno) throws RemoteException;

    public ArrayList getEbookingRoutingDetailsfrombookingleg(String bookNo) throws RemoteException;

    public String getTareweight(String eqptype, String equipdesc) throws RemoteException;

    public HashMap getBookingCount(String custno, String custtypeconfig, String v1DataEnableStatus,String monthyr) throws RemoteException;

    public ArrayList getLRPBookingRoutingDetails(String bookNo) throws RemoteException;

    public String checkTemplateName(String templateName, String loginname) throws RemoteException;

    public ArrayList getTransportService() throws RemoteException;

    public String deleteTemplateBooking(String Bookno) throws RemoteException;

    public String getCountryCodeForPort(String port) throws RemoteException;

    public String getPODIntransit(String port) throws RemoteException;

    public String getseaportfrominland(String port) throws RemoteException;

    public float getTareWtForAmendment(String containerno, String eqpType, String eqpDesc) throws RemoteException;

    public String getBookingDeclinedRemarks(String ecombookno, String status) throws RemoteException;

    public String printBookingJaserSaveData(String type, String agencycode, Hashtable countryHashObj) throws RemoteException;

    public String getecomjasperpath(String agencycode, String locationcode) throws RemoteException;

    public String getecomcorpagency(String agencycode) throws RemoteException;

    public String getecompagencylocation(String agencycode) throws RemoteException;

    public String cancelEcomBooking(String ecomBookNo, String lrpBookNo) throws RemoteException;

    public List<CommoditySearchVO> getComdtySearchList(String value, String columnName, String constraint, String groupcode) throws RemoteException;

    public ReturnVO cancelLRPBooking(String bookno, String canceluser, String reason, String ebookno) throws RemoteException;

    public String validateCancelLRPBooking(String bookno, String validationtype, String disableConfirmBkgValidation) throws RemoteException;

    public String getEcomCustMappingAgency(String custCode) throws RemoteException;

    public List getFeederService() throws RemoteException;

    public String getBookingHaulageDocType(String originCode, String custCode) throws RemoteException;

    public List getBkgHaulCustDetails(String custcode) throws RemoteException;

    public List getBkgHaulContactDetails(String custcode) throws RemoteException;

    public Map getBkgHaulTMSClientDetails(String loadport) throws RemoteException;

    public List<BookingHaulagedetailsVo> getEcomBookingHaulageDetails(String ebookno) throws RemoteException;

    public List getCutOffConfigDetails(String callid, String pol, String pod) throws RemoteException;

    public CutoffConfigVO getEcomBookingConfDetails(String bookno) throws RemoteException;

    public void setBkgPrint(String bookNo, String sessionid) throws RemoteException;

    public void delBkgPrint(String bookNo, String sessionid) throws RemoteException;

    public String getbkgconfigDetailsForHaulage(String attrib) throws RemoteException;

    public String getBKGJasperIdFromAgencycode(String agency) throws RemoteException;

    public void setInsurancedetails(List inslist, String readerid) throws RemoteException;

    public void removeInsuranceDetails(String quoteno, String book_no, List surveylis) throws RemoteException;

    public String validateConfirmBookingEdit(String bookno) throws RemoteException;

    public List<BkgContainerHaulageDetails> getBookingHaulageDetails(String bookno) throws RemoteException;

    public List<CargoProtectVo> getCargoProtectDetails(String type, String value) throws RemoteException;

    public List getMaskPortList() throws RemoteException;

    public HashMap<String, String> checkEUPort(String pol, String pod) throws RemoteException;

    public String isChkAPEnsFilingAgreement(String apcode) throws RemoteException;

    public HashSet<String> getDisableDraftPrintAgency(String disableCountry) throws RemoteException;

    public Map getEbookingViewCustDetails(String bookNo, String action) throws RemoteException;

     public Map getLRPBookingCutomerDetails(String bookNo) throws RemoteException;

     public ENSDeclarantVo getENSTabDetails(String lrpBookNo) throws RemoteException;

    public List getTOS() throws RemoteException;

    public Map<String, Map<String, String>> getCargoProtectRestrictionCountryDG() throws RemoteException;

    public ReturnVO cancelConfirmedBooking(String lrpbookno, String ecombookno, String cancelReason, String cancelRemarks, String cancelUser, String userCode) throws RemoteException;

}
