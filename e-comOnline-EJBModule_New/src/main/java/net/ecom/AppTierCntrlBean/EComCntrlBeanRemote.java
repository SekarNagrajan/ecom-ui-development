/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.common.Mailer;
import net.ecom.vds.serviceVo.ScheduleCommonVO;
import net.ecom.vo.Agencydto;
import net.ecom.vo.Portdto;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.CityVo;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.FieldConfigVO;
import net.ecom.vo.HotlineContactsVO;
import net.ecom.vo.MailAlertVO;
import net.ecom.vo.NotificationVo;
import net.ecom.vo.RateContractSurchargeVO;
import net.ecom.vo.RatesContractsVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.ServiceRestrictionVO;
import net.ecom.vo.V1V2ServiceConfigVO;

@Remote
public interface EComCntrlBeanRemote {

    public List<ScheduleCommonVO> getByPorts(String portcode, String date, String isLCT) throws RemoteException;

    public String getContainerValidate(String containerno, String soc, String eqpSize) throws RemoteException;

    public List<ScheduleCommonVO> getByVessel(String vesselcode, String date, String isLCT) throws RemoteException;

    public HashMap<String, ArrayList<CityVo>> fetchCountryCityList() throws RemoteException;

    public String getCityList(String countrycode) throws RemoteException;

    public List getCityByCountry(String countrycode) throws RemoteException;

    public String getCarriageTermName(String carriageTermCode) throws RemoteException;

    public ArrayList getCustMenuDetails(String CustId, String Type) throws RemoteException;

    public String getLocationList(String Countrycode) throws RemoteException;

    public String getCity(String countrycode) throws RemoteException;

    public String getState(String countrycode) throws RemoteException;

    public List<DropDownVO> getCountryNameandCode() throws RemoteException;

    public String checkports(String port) throws RemoteException;

    public String checkvessel(String vessel) throws RemoteException;

    public MailFormatVO getMailContent(String module, String criteria, String usertype) throws RemoteException;

    public String getForwardMessage(String module, String criteria, String usertype) throws RemoteException;

    public String getForwardMessageForDisclaimer(String module, String usertype) throws RemoteException;

    public String getBottomMessage(String module, String criteria, String usertype) throws RemoteException;

    public Hashtable getAuthenticationMail() throws RemoteException;

    public String getEqpimentSize(String type) throws RemoteException;

    public String checkcountry(String country) throws RemoteException;

    public String checkAgency(String strAgencyCode) throws RemoteException;

    public List getPersonDetails(String loginUser) throws RemoteException;

    public String checkDGDetails(String unno, String shipname, String dgclass) throws RemoteException;

    public ArrayList getTemplateNo(String loginUser, String flag) throws RemoteException;

    public String getMessageContent(String module, String criteria) throws RemoteException;

    public HashMap<String, List<String>> getAgencyLocation() throws RemoteException;

    public List<Agencydto> getAgencyDetail(String strLocation, String strCountry, String strFormSubmit, String strAgencyName) throws RemoteException;

    public ArrayList getShipmentTerms() throws RemoteException;

    public List getAPDetails(String cust_code) throws RemoteException;

    public List getecomcustomermapping(String userCode);

    public ArrayList saveRegisteredMenu(String MenuName, String DevelopedBy, String Status, String Date, String RefNo, String CreatedBy, String Category, String Usertype, String menuorder, String classvalue, String attrvalue, String labelvalue, String menutype, String parentmenu) throws RemoteException;

    public ArrayList getRegisteredMenus(String Message, String usertype) throws RemoteException;

    public ArrayList deleteRegisteredMenu(int MenuId, String UserType) throws RemoteException;

    public ArrayList editRegisteredMenu(String MenuName, String DevelopedBy, String Status, String Date, String RefNo, String CreatedBy, String Category, int MenuId, String Usertype, String menuorder, String classvalue, String attrvalue, String labelvalue, String menutype, String parentmenu) throws RemoteException;

    public ArrayList removeCustMenu(String CustId, String MenuId, String Type) throws RemoteException;

    public ArrayList addCustMenu(String CustId, String MenuId, String Type) throws RuntimeException;

    public String setDBConst(String ColumnName, String Value, String split) throws RemoteException;

    public String setAgencyConstraints(String columnName, String con, String Val) throws RemoteException;

    public ArrayList getAssignedModule(String vendorname) throws RemoteException;

    public ArrayList getAssignableModules() throws RemoteException;

    public String saveVendorModules(ArrayList params, String compname) throws RemoteException;

    public String DeleteVendorModules(ArrayList params, String compname) throws RemoteException;

    public String getemailupdateformate(String updateformate, String strmoduls, String stractivity, String subject) throws RemoteException;

    public HashMap getBLCount(String custno, String custTypeId, String v1DataEnableStatus) throws RemoteException;

    public HashMap getSiCount(String custcode, String custTypeId, String v1DataEnableStatus) throws RemoteException;

    public List getSQLBlockedData() throws RemoteException;

    public String saveSQLblock(String strcarname, String strblockchar, String strwords, String strsavestate) throws RemoteException;

    public String SavechangeStatus(String strcarname, String strstatechange) throws RemoteException;

    public List UserModuleMappingCustomerInfo(String columnName, String constraints, String columnValue, String usercode) throws RemoteException;

    public List getIPBlockedData() throws RemoteException;

    public String saveIPblock(String strcarname, String strblockchar, String strwords, String strsavestate) throws RemoteException;

    public String SavechangeIPStatus(String strcarname, String strstatechange) throws RemoteException;

    public ArrayList getAgencyLoginDetails(String strloginname, String strcustomercode) throws RemoteException;

    public String CheckUserEmail(String compid, String useremail) throws RemoteException;

    public List getGlobalConfigDetails(String adminType) throws RemoteException;

    public List getRegistrationPendingMailDetails() throws RemoteException;

    public List getRejectedUserDetails() throws RemoteException;

    public String saveGlobalConfigDetails(String key, String value) throws RemoteException;

    public String updateGlobalConfigDetails(String key, String value) throws RemoteException;

    public String getCutomerOblStatus(String usercode) throws RemoteException;

    public String getWebserviceUrl() throws RemoteException;

    public String getGlobalImagePath() throws RemoteException;

    public ArrayList getVesselDetails(String vesselCode) throws RemoteException;

    public String getClientNameForProperty() throws RemoteException;

    public Hashtable getglobalconfigMail() throws RemoteException;

    public String getEcomReleaseVersion() throws RemoteException;

    public String deleteGlobalConfigDetails(String key) throws RemoteException;

    public String getEmptyPickPoint(String port, String type) throws RemoteException;

    public Map<String, String> getEmptyPickUpMap() throws RemoteException;

    public List<MailAlertVO> getMailAlertActivities(String loginName) throws RemoteException;

    public String saveMailAlertActivities(List<MailAlertVO> listAlertVO) throws RemoteException;

    public Map<String, String> getPODIntransitMap() throws RemoteException;

    public List<CutoffConfigVO> getCutOffList() throws RemoteException;

    public int saveCutoffConfigDetails(CutoffConfigVO cutOffVO) throws RemoteException;

    public int deleteCutoffConfig(String port, String terminal) throws RemoteException;

    public String checkCutOffDuplicate(String port, String terminal) throws RemoteException;

    public CutoffConfigVO getRoutingCutOffDetails(String port, String terminal) throws RemoteException;

    public String checkContainer(String containterid) throws RemoteException;

    public MailFormatVO getMailContentFromCommon(String module, String criteria, String usertype) throws RemoteException;

    public Map getMasterConfigDetails() throws RemoteException;

    public String getPortType(String portcode) throws RemoteException;

    public void sendMailFromEjb(Mailer mail, String fromMailId, String initiatorID, String carrierCode) throws RemoteException;

    public String checkEQPSize(String containterid) throws RemoteException;

    public Map getEqpMap() throws RemoteException;

    public Map getEqpments() throws RemoteException;

    public Map getUnno() throws RemoteException;

    public Map getIMGClass() throws RemoteException;

    public String checkContainerForSI(String containterid) throws RemoteException;

    public Map getCommodity() throws RemoteException;

    public Map getHSCode() throws RemoteException;

    public Map getcommodityrecords() throws RemoteException;

    public Map getcommoditygroupcodemaps(String openCommodityArr) throws RemoteException;

    //  public String checkEQP(String containterid) throws RemoteException;
    public String checkContainerAndEqpType(String containterid, String eqpType, String nor, String type, String pluginEnable) throws RemoteException;

    public String getScheuleVersion(String service, String vessel, String voyage, String bound, String depDate, String svcType, String feederCutOffDate) throws RemoteException;

    public List<FieldConfigVO> getFieldNameConfigList() throws RemoteException;

    public List<FieldConfigVO> getFieldConfigDetailsList() throws RemoteException;

    public int saveFieldConfigurationDet(FieldConfigVO configVO) throws RemoteException;

    public String fieldConfigDuplication(FieldConfigVO configVO) throws RemoteException;

    public String deleteFieldConfg(String id) throws RemoteException;

    public String getFieldConfg(String countryId, String port, String fieldCode, String shipmentType) throws RemoteException;

    public ReturnVO getRateRequestMailDets(String pol) throws RemoteException;

    public List<RatesContractsVO> getAvailableRatesContract(String custCode, String pol, String pod, String eqpType, String commodity, String shipmentDate) throws RemoteException;

    public List<RateContractSurchargeVO> getRateContractSurchargeDets(String rateNo, String itemNo, String rateType) throws RemoteException;

    public Map<String, String> getDashboardCount(String custCode, String v1v2DataEnableStatus) throws RemoteException;

    public List<V1V2ServiceConfigVO> getV1V2ServiceConfigDetList() throws RemoteException;

    public String saveServiceConfigDetList(V1V2ServiceConfigVO serviceVO) throws RemoteException;

    public String updateServiceConfigDetList(V1V2ServiceConfigVO serviceVO) throws RemoteException;

    public String deleteServiceConfigDetList(String id) throws RemoteException;

    public Map<String, List<String>> getEcomMailAcitvityList() throws RemoteException;

    public String getCorporteAgency(String agencyCode) throws RemoteException;

    public List<ServiceRestrictionVO> getSvrcRestrictDetailsList() throws RemoteException;

    public String saveSvrcRestriction(ServiceRestrictionVO vo) throws RemoteException;

    public String deleteSvrcRestrict(String id) throws RemoteException;

    public String checkSvcRestrict(String id, String pla, String pld, String service) throws RemoteException;

    public Map<String, List<String>> getSvrcRestrictCacheList() throws RemoteException;

    public HashMap getEcomMailConfig(String profileid, String carriercode) throws RemoteException;

    public Map<String, List> getLoginImagAdvisoryDets() throws RemoteException;

    public String checkBLBookingNo(String refNo) throws RemoteException;

    public String getCustomerSpecialCustomerGroup(String custcode) throws RemoteException;

    public Map getLrpPluginDetails(String BKG_Pluginid, String EBK_Pluginid) throws RemoteException;

    public HashMap getRoutingCutOffDetailsFromPort(String port) throws RemoteException;

    public ArrayList getCustMenuDetailsList(String CustId, String Type) throws RemoteException;

    public String getEcomBKGFileUploadPath() throws RemoteException;

    public List getCustType() throws RemoteException;

    public List getRefidForAddDet() throws RemoteException;

    public String getDefaultCarriageContract() throws RemoteException;

    public String getTrackingBKGDepartureDate(String book_no) throws RemoteException;

    public Map getMessagesBasedOnLang() throws RemoteException;

    public Map<String, String> getEcomLangSelection() throws RemoteException;

    public Map<String, List<HotlineContactsVO>> getHotlineContacts() throws RemoteException;

    public Map<String, String> getVesselDetailsRestriction() throws RemoteException;

    public HashMap getStateCache() throws RemoteException;

    public Map<String, String> getVersionUTC() throws RemoteException;

    public String checkPluginFEU(String moduleid, String pluginid) throws RemoteException;

    public Map<String, String> getEcomTimeZoneSelection() throws RemoteException;

    public String getForwardMessageLang(String module, String criteria, String usertype, String lang) throws RemoteException;

    public HashMap<String, String> getlocnew(String countryCode) throws RemoteException;

    public String getPayloadValidation(String containerno, String eqptype, String cargowt, String grosswt, String wtunit, List pluginList, Map EQPWeight) throws RemoteException;

    public Map<String, String> getEqptypeAndMaxWeight() throws RemoteException;

    public List<String> getSubmittedFileNames(String refno, String path, String type) throws RemoteException;

    public String deleteSILock(String type, String refno) throws RemoteException;

    public String insertSILock(String bookno, String blno, String createduser) throws RemoteException;

    public Map getEcomPasswordConfig(String attribute) throws RemoteException;

    public List getStatsReportDetails(String usercode, String fromdate, String todate, String options, String service, String vessel, String voyage, String pla, String pol, String pod, String pld, String commodity, String equipmenttype, String contractno, String northbound, String southbound, String intertropics, String europe, String bound, String amendmentno) throws RemoteException;

    public String getVesselDetailsFromService(String service) throws RemoteException;

    public List getVoyageDetails(String service, String vessel) throws RemoteException;

    public List getContractDetails(String custcode) throws RemoteException;

    public Map getVesselIMODetails() throws RemoteException;

    public String putInsuranceSurveyList(String surveyjson) throws RemoteException;

    public String checkInsuranceAccess(String custcode) throws RemoteException;

    public String getMessageContentLang(String module, String criteria, String lang) throws RemoteException;

    public List<NotificationVo> getEcomNotificationDetails(String custcode, String loginname) throws RemoteException;

    public void setEcomNotificationViewStatus(String msgid, String loginname) throws RemoteException;

    public Map<String, Map> getDashboardCounts(String custno, String monthyr,String enable_dashboard_filter) throws RemoteException;

    public HashMap<String, String> getagencyloc() throws RemoteException;

    public Map getemailformate(String strmodules, String stractivity) throws RemoteException;

    public Map transportplanpod(String book_no) throws RemoteException;
    
    public String getOpenCommodityList() throws RemoteException;

    public String getEUCountryList() throws RemoteException;

    public List getPersonDetailsForIncompletedReg(String loginUser) throws RemoteException;

    public ArrayList getCarrierDetails(String carriercode) throws RemoteException;

    public HashMap<String, String> getPortCountryMap() throws RemoteException;

}
