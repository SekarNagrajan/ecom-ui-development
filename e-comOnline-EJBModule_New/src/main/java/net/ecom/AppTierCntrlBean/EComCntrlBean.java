/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.json.SvmJsonList;
import com.svm.json.SvmJsonObject;
import com.svm.lrp.nfr.mail.bean.IMail;
import com.svm.lrp.nfr.mail.utils.AttachmentModel;
import com.svm.lrp.nfr.multiAgencyUtil.MultiAgencyUtil;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import net.ecom.common.DashboardComparator;
import net.ecom.common.GeneralConf;
import net.ecom.common.Mailer;
import net.ecom.common.Validation;
import net.ecom.common.WeightConversion;
import net.ecom.phase2.service.vo.AmountSummaryVO;
import net.ecom.phase2.service.vo.CSInvoiceSummaryVO;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.v1.cntlbean.EcomV1DashboardBeanRemote;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomBKGSpsService;
import net.ecom.vds.service.EcomDashboardService;
import net.ecom.vds.service.EcomGeneralVDSService;
import net.ecom.vds.service.EcomSiVdsService;
import net.ecom.vds.service.EcomVGMVDSService;
import net.ecom.vds.serviceVo.AgencyMaster;
import net.ecom.vds.serviceVo.CustomerContacts;
import net.ecom.vds.serviceVo.CustomerMasterVo;
import net.ecom.vds.serviceVo.Routedetail;
import net.ecom.vds.serviceVo.RoutingVo;
import net.ecom.vds.serviceVo.ScheduleCommonVO;
import net.ecom.vds.serviceVo.StatsReportVo;
import net.ecom.vds.serviceVo.VesselMaster;
import net.ecom.vo.Agencydto;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.vo.Formdto;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.UniqueRandom;
import net.ecom.vo.CityVo;
import net.ecom.vo.CustomerAdvisoryVo;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.DashboardVO;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.FieldConfigVO;
import net.ecom.vo.RegisterMenuVO;
import net.ecom.vo.SecurityVO;
import net.ecom.vo.GlobalConfigVO;
import net.ecom.vo.HotlineContactsVO;
import net.ecom.vo.MailAlertVO;
import net.ecom.vo.NotificationVo;
import net.ecom.vo.RateContractSurchargeVO;
import net.ecom.vo.RatesContractsVO;
import net.ecom.vo.RegistrationVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;
import net.ecom.vo.ServiceRestrictionVO;
import net.ecom.vo.V1V2ServiceConfigVO;
import org.apache.commons.io.FileUtils;

@Stateless(name = "EComCntrlBean")
@Remote(EComCntrlBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
     @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS")
})
public class EComCntrlBean implements EComCntrlBeanRemote {

    private SessionContext context;
    protected static final String ECOM_MAILCONTENT = "select message,from_mailid,subject,activitycode from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";
    protected static final String GET_WEBSERVICERUL = "select pathvalue from globalconfigproduct where pathname='WSURL'";
    protected static final String GET_TEMPLATENAME = "select ebookno,templatename,originPortId,finalPortId from ecombooking(nolock)"
            + "where usercr=? and ebook_status = ? order by ebookno asc";
    protected static final String DELETE_GLOBALCONFIG_DETAILS = "delete globalconfigproduct where pathname=?";
    protected static final String GET_ECOM_RELEASEVERSION = "select message from ecom_messageformat where module='Version' and status='A'";
    protected static final String GET_GLOBAL_IMAGEPATH = "select distinct pathvalue from globalconfigproduct where pathname='imagepath'";

    protected static final String Forward_query = "select message from ecom_messageformat(nolock) where Status='a' and Type ='msg' "
            + " and criteria =? and Module =? and usertype=?";
    protected static final String disclaimermsg_query = "select message from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria='disclaimer'  and Module =? and usertype=?";

    protected static final String Forward_lang_query = "select message from ecom_messageformatlang(nolock) where Status='a' and Type ='msg' "
            + " and criteria =? and Module =? and usertype=? and lang=?";
    protected static final String copyRight = "select message from ecom_messageformat(nolock) where Status='A' and Type ='msg'  and criteria ='CopyRight' and Module='Home' and usertype=?";
    protected static final String version = "select message from ecom_messageformat(nolock) where Status='A' and Type ='msg'  and criteria ='Version' and Module='Home' and usertype=?";
    protected static final String bottomlogo = "select message from ecom_messageformat(nolock) where Status='A' and Type ='Img'  and criteria ='bottomlogo' and Module='Home' and usertype=?";
    protected static final String supportMailid = "select from_mailid from ecom_messageformat(nolock) where Status='A' and Type ='mail'  and criteria ='update' and Module='ForgotPasswordNew' and usertype=?";
    protected static final String MailContentQry = "select message,from_mailid,subject from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";
    protected static final String strgetactive = "Select loginname,password,firstname,lastname,emailId from ECOM_USERDETAIL where loginname=?";
    protected static final String MessageContentQry = "select message from ecom_messageformat(nolock) where Status='A' and type='msg' and Module=? and criteria = ?";
    protected static final String PUT_GLOBAL_CONFIGDETAILS = "update  globalconfigproduct set pathname = ?,pathvalue =? where pathname=?";
    protected static final String SET_GOBAL_CONFIGDETAILS = "insert into globalconfigproduct (pathname,pathvalue,status) values (?,?,'C')";

    protected static final String strAgencyLoginAdminData = "select username,usertype from ecom_adminuser_details(nolock) where userid=? and password=? and status='A'";
    protected static final String saveRegistered = "insert into MenuRegistration(MenuName,DevelopedBy,MenuStatus,Date,RefNo,Category,userttype,CreatedBy,ChgDate,menuorder,ClassValue,AttrValue,LabelValue,MenuType,ParentMenu) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    protected static final String strdeletevendorModule = "delete from vendormodulemapping where mnuname=?";
    protected static final String strinsertVendorModule = "insert into vendormodulemapping(mnuname,mnuid,status,vendorname,cruser,crdate)values(?,'mn00','A',?,'Admin',getDate())";
    protected static final String RegisteredMenusLoadQry = "SELECT IDENT_CURRENT('MenuRegistration') as MaxId, * from MenuRegistration(nolock) where userttype='U' or userttype='V' order by MenuId ";
    protected static final String RegisteredMenusNonLoadQry = "SELECT IDENT_CURRENT('MenuRegistration') as MaxId, * from MenuRegistration(nolock) where userttype=? order by MenuId ";
    protected static final String StrdeleteRegisteredQry = "delete from MenuRegistration where MenuId=?";
    protected static final String StreditRegisteredQry = "update MenuRegistration set MenuName=?, DevelopedBy=?,MenuStatus=?, Date=?, RefNo=?, Category=?, CreatedBy=?,ChgDate=?,userttype=?,menuorder=?,ClassValue=?,AttrValue=?,LabelValue=?,MenuType=?,ParentMenu=? where MenuId=?";
    protected static final String CustMenuDetail = "select menuid from SplPrivilegeMenu where custid=?";
    protected static final String removeCustMenuQry = "update SplPrivilegeMenu set menuid=? where custid=?";
    protected static final String strgetVendorModule = "Select mnuid,mnuname,status from vendormodulemapping where vendorname=? order by mnuname asc";
    protected static final String AssignableModuleQuery = "Select menuid,menuname,menustatus from MenuRegistration where MenuStatus='a' and userttype='V' order by menuname asc ";
    protected static final String GET_REG_PENDIONGMAIL = "select loginname,firstname,lastname,t1.compName,t.status from ecom_tempuserdetail t inner join ecom_tempcompany t1 on t.refid=t1.refid where t.status='P'";
    protected static final String GET_REJECTED_USERDETAILS = "select loginname,firstname,lastname,t1.compName,t.status,t.refid,t1.id from ecom_tempuserdetail t inner join ecom_tempcompany t1 on t.refid=t1.refid where t.status='V'";
    protected static final String GET_GLOBAL_CONFIGMAIL = "select pathname,pathvalue from globalconfigproduct";

    private static final String CHECKLOGINNAMEINMAIL = "select loginname from ecom_mailalert where loginname=?";
    private static final String GETMAILALERTDET = "select distinct ec.moduledesc,ea.activity,ea.activitycode,'Y' as mail from ecom_modules ec inner join ecom_activity ea "
            + " on ec.module=ea.module ";
    private static final String GETMAILALERTFORUSER = "select distinct ec.moduledesc,ea.activity,ea.activitycode,em.mail from  ecom_activity  ea left join   ecom_mailalert em "
            + " on ea.activitycode=em.activitycode inner join    ecom_modules ec on ec.module=ea.module where loginname=?";
    private static final String UPDATEUSERMAILALERT = "update ecom_mailalert set mail=?,chguser=?,chgedate=getdate() where loginname=? and activitycode=?";
    private static final String INSERTINTOMAILALERT = "insert into ecom_mailalert(loginname,activitycode,mail,chguser,chgedate) values(?,?,?,?,getdate())";

    private static final String GET_CUTOFFLIST = "select port,terminal,cfsclosing,vgmclosing,docclosing,ediclosing,fullcntrclosing,excludeweekend from ecom_cutoffconfig";
    private static final String INSERT_CUTOFFCONFIG = "insert into ecom_cutoffconfig(port,terminal,cfsclosing,vgmclosing,docclosing,ediclosing,fullcntrclosing,excludeweekend,cruser,chguser,"
            + " crdate,chgdate) values(?,?,?,?,?,?,?,?,?,?,getdate(),getdate())";
    private static final String UPDATECUTOFFCONFIG = "update ecom_cutoffconfig set cfsclosing=?,vgmclosing=?,docclosing=?,ediclosing=?,fullcntrclosing=?,excludeweekend=?,chguser=?, "
            + "chgdate=getdate() where port=? and terminal=?";
    private static final String DELETECUTOFFCONFIG = "delete from ecom_cutoffconfig where port=? and terminal=?";
    private static final String CHECKDUPCUTOFF = "select port from  ecom_cutoffconfig where port=? and terminal=?";
    private static final String GETCUTOFFFORROUTING = "select port,terminal,cfsclosing,vgmclosing,docclosing,ediclosing,fullcntrclosing,excludeweekend from ecom_cutoffconfig where port=? and terminal=?";
    //
    private static final String GET_EMAIL_MESSAGEFORMATE = "select message,subject from ecom_MessageFormat(nolock) where Module=? and criteria=? and Type='mail' and Status='A' ";
    protected static final String ECOM_MASTER_CONFIGDETAILS = "select pathname,pathvalue from globalconfigproduct";
    ////
    private static final String GET_MAILINE_SVC_VERSION = "select cutoffvoyage,cutoffdate from v1v2serviceconfig where service=? and vessel=? and svctype=?";
    private static final String V1_BL_COUNT_QRY = "select count(bl.status) as countval, bl.status from bl_cust(nolock) inner join bl(nolock) on "
            + " bl.bl_no = bl_cust.bl_no inner join customermaster cm(nolock) on cm.custcode=bl_cust.custid where custid=?  "
            + " and custtypeid='AP' and bl.status in ('C','I') and bl.polat between getdate()-90 and DATEADD(dd,30,getdate()) group by bl.status ";
    private static final String V1_SI_COUNT_QRY = "SELECT bk.book_no FROM   booking AS bk WITH (NOLOCK) INNER JOIN booking_cust AS bc WITH (NOLOCK) ON bk.book_no = bc.book_no"
            + " WHERE  bk.polAt >= getDate() - 60 AND bk.polAt <= getDate() + 30 AND bk.status IN ('c','D') AND bc.custTypeId = 'AP' AND bc.custid=?";
    private static final String GET_FIELDNAME_LIST = "select fieldcode,fieldname,shipmenttype from fieldnameconfig";
    private static final String GET_FIELDCONFIG_DETAILS = "select id,fieldcode,searchtype,searchvalue,shipmenttype,inputtype,status from fieldconfigdetails";
    private static final String SAVE_FIELD_CONFIG = "insert into fieldconfigdetails(fieldcode,searchtype,searchvalue,shipmenttype,inputtype,status)\n"
            + " values(?,?,?,?,?,?)";
    private static final String UPDATE_FIELD_CONFIG = "update fieldconfigdetails set searchtype=?,searchvalue=?,shipmenttype=?,inputtype=?,status=? where id=?";
    private static final String DELETE_FIELD_CONFIG = "delete from fieldconfigdetails where id=?";
    private static final String CHECK_FIELD_CONFIG = "select searchvalue from fieldconfigdetails where  id not in(?) and fieldcode=? and \n"
            + " searchtype=? and searchvalue=? ";
    private static final String CHECK_FIELD_CONFIG2 = "select searchvalue from fieldconfigdetails where  status in('A','I') and fieldcode=? and "
            + "searchtype=? and searchvalue=? ";
    private static final String GETFIELD_CONFIG = "select fieldcode,searchtype,searchvalue,shipmenttype,inputtype from fieldconfigdetails where searchvalue=? and fieldcode=? and status='A' ";
    private static final String GETV1V2SERVICEDET = "select id,service,vessel,cutoffvoyage,bound,svctype,cutoffdate from v1v2serviceconfig";
    private static final String SAVEV1V2SERVICECONFIG = "insert into v1v2serviceconfig(service,vessel,cutoffvoyage,bound,svctype,cutoffdate) values(?,?,?,?,?,?)";
    private static final String UPDATEV1V2SERVICECONFIG = "update v1v2serviceconfig set service=?,vessel=?,cutoffvoyage=?,bound=?,svctype=?,cutoffdate=? where id=?";
    private static final String DELETEV1V2SERVICECONFIG = "delete from v1v2serviceconfig where id=?";
    private static final String GET_ECOMMAILACTIVITY = "select module,activitycode from ecom_activity order by module asc";
    private static final String GET_SVRC_RESTRICT_LIST = "select id ,pla,pld,service,status from  ecom_service_restrict";
    private static final String SAVE_SVRC_DET = "insert into ecom_service_restrict(pla,pld,service,status)values(?,?,?,?)";
    private static final String UPDATE_SVRC_DET = "update ecom_service_restrict set pla=?,pld=?,service=?,status=? where id=?";
    private static final String DELETE_SVRC_DET = "delete from ecom_service_restrict where id=?";
    private static final String CHECK_SVRC_RESTRICT = "select pla from ecom_service_restrict where  pla=? and pld=? and service=? and id not in(?) ";
    private static final String GET_SVRC_CACHE_LIST = "select pla,pld,service from  ecom_service_restrict where status='A' order by pla,pld";
    private static final String GET_BG_IMAGE = "select id,imagename,imagepath,displayorder,status from EcomImageUpload where status='A' order by displayorder";
    private static final String GET_ADVISORY_MSG = "select id,advisoryname,description,createddate,status from EcomCustomerAdvisory where status='A' order by createddate desc";
    private static final String GETCUTOFFFORROUTINGFROMPORT = "select port,terminal,cfsclosing,vgmclosing,docclosing,ediclosing,fullcntrclosing,excludeweekend from ecom_cutoffconfig where port=? or port='All' ";
    private static final String langquery = "select langcode,langname from ecom_lang";
    private static final String GET_MSG_DETAILS = "select message,module,criteria from ecom_messageformatlang where status='A' and lang=?";
    private static final String GET_HOTLINE_CONTACTS = "select countrycode,portcode,countryname,portname,ContactName,phone,email from ecomhotlinecontacts order by countryname,portname";

    protected static final String MessageContentQryLang = "select message from ecom_messageformatLang(nolock) where Status='A' and Module=? and criteria = ? and lang=?";

    protected static final String Get_Notification = " select distinct top 20  enm.msgid,enm.message,enm.crdate,CASE WHEN es.loginname is not null THEN 'Y' ELSE 'N' END as isviewed  from EcomNotificationMaster enm \n"
            + " inner join EcomNotificationDetails ed on enm.msgid=ed.msgid \n"
            + " left join EcomNotificationStatus es on enm.msgid=es.msgid and es.loginname=?\n"
            + " where (ed.custcode='All' or ed.custcode=?) and enm.status='A' order by enm.crdate desc";

    protected static final String Insert_EcomNotificationStatus = "INSERT INTO EcomNotificationStatus(msgid,loginname) VALUES (?,?)";
    protected static final String chechCustCode_ecomCustMapping = "select apcustcode from ecom_customer_mapping where logincustcode=?";
    protected static final String GET_ECOM_REG_DETAILS = "select comp_name,Title,First_Name,Last_Name,address,user_phone,User_Fax,User_Email,city,country,Address2,State,Pin_Code,TaxID from ecom_reg_details(nolock) where user_email=?";

    protected static final String GET_CARRIER_DETAILS = "select CARRIERNAME, ADDRESS, city, ZIPCODE from CARRIER(NOLOCK) where carriercode=?";
    protected static final String GET_CITY_MASTER = "select cityname, STATECODE from CITYMASTER(NOLOCK) where citycode=?";
    

    private static final String GET_PORT_COUNTRY_MAP = "select portcode,countrycode from ports(nolock) where status='A'";


    @Inject
    VDSInvoker vds;

    @Inject
    IMail mailer;

    EcomBKGVdsService ecomvdsservice;
    EcomBKGSpsService ecomvdsspservice;
    EcomSiVdsService ecomsivdsservice;
    EcomGeneralVDSService ecomGenService;
    EcomDashboardService dashBoardService;
    EcomVGMVDSService vgmService;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomV1DashboardBean!net.ecom.v1.cntlbean.EcomV1DashboardBeanRemote")
    EcomV1DashboardBeanRemote v1DashboardBeanObj;

    @PostConstruct
    public void init() {
        try {
            ecomvdsservice = vds.create(EcomBKGVdsService.class);
            ecomvdsspservice = vds.create(EcomBKGSpsService.class);
            //  getdata();
            ecomsivdsservice = vds.create(EcomSiVdsService.class);
            ecomGenService = vds.create(EcomGeneralVDSService.class);
            dashBoardService = vds.create(EcomDashboardService.class);
            vgmService = vds.create(EcomVGMVDSService.class);
            //   getdata();
//            getVesselDetails("UAS1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void getdata() {
        try {
            SvmJsonObject spjsonobj = new SvmJsonObject();
            spjsonobj.put("book", "BKON386");
            spjsonobj.put("status", "V");
            spjsonobj.put("remarks", "Dedclined");
            spjsonobj.put("booktype", "BKG");
            spjsonobj.put("tbookno", "ESLSVMNLC000127");
            // System.out.println("[ECOM] EComCntrlBean   =>Calling the getdata<=======");
            Map hs = (Map) ecomvdsspservice.putbookingstatuupdate(spjsonobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, List<String>> getAgencyLocation() throws RemoteException {

        String countryCode = "";
        List<String> portList = new ArrayList<String>();
        String strPort = "";
        HashMap<String, List<String>> agencyLocationMap = new HashMap<String, List<String>>();
        try {
            Map hs = (Map) ecomvdsservice.getAgencyLocation();
            List lst = (List) hs.get("ports");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    countryCode = checkNull((String) rs.get("countrycode")).toUpperCase();
                    if (agencyLocationMap.get(countryCode) != null) {
                        strPort = checkNull((String) rs.get("portcode")).toUpperCase().trim() + " - " + checkNull((String) rs.get("portname")).trim().toUpperCase();
                        portList.add(strPort);
                    } else {
                        portList = new ArrayList<String>();
                        strPort = checkNull((String) rs.get("portcode")).toUpperCase() + " - " + checkNull((String) rs.get("portname")).toUpperCase();
                        portList.add(strPort);
                    }
                    agencyLocationMap.put(countryCode, portList);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return agencyLocationMap;
    }

    @Override
    public List<Agencydto> getAgencyDetail(String strLocation, String strCountry, String strFormSubmit, String strAgencyName) throws RemoteException {

        List<Agencydto> agencyList = new ArrayList<Agencydto>();
        Agencydto agencyObj = new Agencydto();
        Map hs = null;
        List<AgencyMaster> agencylst = null;
        Gson gson = new Gson();
        try {
            if ("location".equalsIgnoreCase(strFormSubmit)) {
                hs = (Map) ecomvdsservice.getecomagencydetailswithloaction(strCountry, strLocation);
            } else {
                hs = (Map) ecomvdsservice.getecomagencydetails(strAgencyName);
            }
            agencylst = gson.fromJson(gson.toJson((ArrayList) hs.get("agency")), new TypeToken<List<AgencyMaster>>() {
            }.getType());
            if (agencylst.size() > 0) {
                AgencyMaster rs = (AgencyMaster) agencylst.get(0);
                agencyObj = new Agencydto();
                agencyObj.setAgencycode(checkNull(rs.getAgencycode()));
                agencyObj.setAgencyname(checkNull(rs.getAgencyname()));
                agencyObj.setAgencyAddr(checkNull(rs.getAddress()));
                agencyObj.setAgencyCountry(checkNull(rs.getCountryname()));
                agencyObj.setAgencyTele(checkNull(rs.getTelephone()));
                agencyObj.setAgencyFax(checkNull(rs.getFax()));
                agencyObj.setAgencyEmailId(checkNull(rs.getEmail()));
                agencyObj.setAgencyPort(checkNull(rs.getLocation()));
                agencyObj.setAgencyCity(checkNull(rs.getCity()));
                agencyObj.setAgencycontactname(checkNull(rs.getContactname()));
                agencyObj.setAgencycontactdesignation(checkNull(rs.getContactdesignation()));
                agencyObj.setAgencycontactphoneno(checkNull(rs.getContactphoneno()));
                agencyObj.setAgencycontactemailid(checkNull(rs.getContactemailid()));
                agencyList.add(agencyObj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return agencyList;
    }

    @Override
    public HashMap<String, ArrayList<CityVo>> fetchCountryCityList() throws RemoteException {
        HashMap<String, ArrayList<CityVo>> countryCityObj = new HashMap<String, ArrayList<CityVo>>();
        String strCountryCode = "", strCityName = "";
        ArrayList cityList = new ArrayList();
        try {
            Map hs = (Map) ecomvdsservice.getecomcitydetails();
            List lst = (List) hs.get("city");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    strCountryCode = Validation.checkNull((String) rs.get("countryname"));
                    strCountryCode = (strCountryCode.length() > 0) ? strCountryCode.toUpperCase() : "";
                    if (countryCityObj.containsKey(strCountryCode)) {
                        CityVo cityObj = new CityVo();
                        strCityName = Validation.checkNull((String) rs.get("cityname"));
                        strCityName = (strCityName.length() > 0) ? strCityName.toUpperCase() : "";
                        cityObj.setStrCityName(strCityName);
                        cityList.add(cityObj);
                    } else {
                        cityList = new ArrayList();
                        CityVo cityObj = new CityVo();
                        strCityName = Validation.checkNull((String) rs.get("cityname"));
                        strCityName = (strCityName.length() > 0) ? strCityName.toUpperCase() : "";
                        cityObj.setStrCityName(strCityName);
                        cityList.add(cityObj);
                    }
                    countryCityObj.put(strCountryCode, cityList);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return countryCityObj;
    }

    @Override
    @SuppressWarnings("static-access")
    public String getCityList(String countrycode) throws RemoteException {

        StringBuffer sb = new StringBuffer("");
        try {
            Map hs = (Map) ecomvdsservice.getecomcityincountry(countrycode);
            List lst = (List) hs.get("city");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    sb.append(checkNull((String) rs.get("cityname")).replaceAll("'", "&apos;"));
                    sb.append("--");
                    sb.append(checkNull((String) rs.get("citycode")));
                    sb.append("~~");
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   Exception in getCity method: " + e);
        }
        return sb.toString();
    }

    @Override
    @SuppressWarnings("static-access")
    public List getCityByCountry(String countrycode) throws RemoteException {

        List citylist = new ArrayList();
        Formdto dto = null;
        try {
            Map hs = (Map) ecomvdsservice.getecomcityincountry(countrycode);
            List lst = (List) hs.get("city");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    dto = new Formdto();
                    dto.setCitycode(checkNull((String) rs.get("cityname")));
                    dto.setCityname(checkNull((String) rs.get("citycode")));
                    citylist.add(dto);
                }
            } else {
                dto = new Formdto();
                dto.setCitycode("NA");
                dto.setCityname("No Cities Available");
                citylist.add(dto);
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   Exception in getCity method: " + e);
        }
        return citylist;
    }

    @Override
    @SuppressWarnings("static-access")
    public String getCarriageTermName(String carriageTermCode) throws RemoteException {

        String result = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomtransportterms(carriageTermCode);
            List lst = (List) hs.get("transportterms");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                result = checkNull((String) rs.get("trptterm"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList getVesselDetails(String vesselCode) throws RemoteException {

        ArrayList arr_vessel = new ArrayList();
        List<VesselMaster> vessellst = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecomvesseldetails(vesselCode);
            vessellst = gson.fromJson(gson.toJson((ArrayList) hs.get("vesselmaster")), new TypeToken<List<VesselMaster>>() {
            }.getType());
            if (vessellst.size() > 0) {
                VesselMaster rs = (VesselMaster) vessellst.get(0);
                arr_vessel.add(rs.getVesselname());
                arr_vessel.add(rs.getVesselcode());
                arr_vessel.add(rs.getCallsign());
                arr_vessel.add(rs.getVsloperator());
                arr_vessel.add(rs.getVesselowner());
                arr_vessel.add(rs.getVesseltype());
                arr_vessel.add(rs.getFlag());
                arr_vessel.add(rs.getPortofregistry());
                arr_vessel.add(rs.getBuilt());
                arr_vessel.add(rs.getImo());
                arr_vessel.add(rs.getLoa());
                arr_vessel.add(rs.getNominaltues());
                arr_vessel.add(rs.getGrt());
                arr_vessel.add(rs.getNrt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr_vessel;
    }

    private String setConstraints(String columnName, String con) throws Exception {
        String condition = "";
        switch (con.toLowerCase()) {
            case "con":
                condition = columnName + " LIKE ?";
                break;
            case "equal":
                condition = columnName + " = ?";
                break;
            case "notequal":
                condition = columnName + " != ?";
                break;
            case "start":
                condition = columnName + " LIKE ?";
                break;
            case "dstart":
                condition = columnName + " NOT LIKE ?";
                break;
            case "end":
                condition = columnName + " LIKE ?";
                break;
            case "dend":
                condition = columnName + " NOT LIKE ?";
                break;
            case "dcon":
                condition = columnName + " NOT LIKE ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid condition: " + con);
        }
        return condition;
    }

    @Override
    public List<DropDownVO> getCountryNameandCode() throws RemoteException {

        DropDownVO ddVO = null;
        List<DropDownVO> dropDownList = new ArrayList<DropDownVO>();
        try {

            ddVO = new DropDownVO();
            ddVO.setCode("");
            ddVO.setName("Select Country");
            dropDownList.add(ddVO);
            Map hs = (Map) ecomvdsservice.getecomcountrydetails();
            List lst = (List) hs.get("country");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    ddVO = new DropDownVO();
                    ddVO.setCode(checkNull((String) rs.get("countrycode")));
                    ddVO.setName(checkNull((String) rs.get("countryname")));
                    dropDownList.add(ddVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dropDownList;
    }

    @Override
    public Hashtable getAuthenticationMail() throws RemoteException {

        Hashtable allObj = new Hashtable();
        String value = "";
        String keycode = "";
        try {

            Map hs = (Map) ecomvdsservice.getecommailauthservice();
            List lst = (List) hs.get("defaultemailsetup");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    value = checkNull((String) rs.get("value"));
                    keycode = checkNull((String) rs.get("keycode"));
                    allObj.put(keycode, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allObj;
    }

    public String getEqpimentSize(String type) throws RemoteException {

        Map hs = null;
        StringBuffer eqp = null;
        if ("H".equalsIgnoreCase(type)) {
            type = "D";
        }
        try {
            if (type.equalsIgnoreCase("L")) {
                hs = (Map) ecomvdsservice.getecommequipmentsdetails(type);
            } else {
                hs = (Map) ecomvdsservice.getecommequipmentsdetails(type);
            }
            List lst = (List) hs.get("ecom_equipments");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    eqp.append(checkNull((String) rs.get("equipdesc"))).append("##");
                    eqp.append(checkNull((String) rs.get("equiptype"))).append("~~");
                }
            } else {
                eqp = new StringBuffer();
                eqp.append("noequiptype##noequipdesc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eqp.toString();
    }

    @Override
    public List getPersonDetails(String loginuser) throws RemoteException {
        List personalInfo = new ArrayList();
        String country = "";
        List<CustomerContacts> custcontactlst = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecommcustomercontact(loginuser);
            custcontactlst = gson.fromJson(gson.toJson((ArrayList) hs.get("customercontacts")), new TypeToken<List<CustomerContacts>>() {
            }.getType());
            if (custcontactlst.size() > 0) {
                CustomerContacts rs = (CustomerContacts) custcontactlst.get(0);
                personalInfo.add(checkNull(rs.getContactname()));
                personalInfo.add(checkNull(rs.getCustname()));
                personalInfo.add(checkNull(rs.getAddress1()));
                personalInfo.add(checkNull(rs.getPhone()));
                personalInfo.add(checkNull(rs.getFax()));
                personalInfo.add(checkNull(rs.getEmail()));
                personalInfo.add(checkNull(rs.getCity()));
                country = checkNull(rs.getCountryid());
            }
            hs = (Map) ecomvdsservice.getecomcountryincode(country);
            List lst = (List) hs.get("country");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                personalInfo.add(country + "-" + rs.get("Countryname"));
            }
            System.out.println("[ECOM] EComCntrlBean   ====>>>>personalInfo" + personalInfo.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personalInfo;
    }

    @Override
    public List getPersonDetailsForIncompletedReg(String loginuser) throws RemoteException {
        List personalInfo = new ArrayList();
        String phone = null, fax = null; 
        String country = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_REG_DETAILS, 1004, 1007);
            ps.setString(1, loginuser);
            rs = ps.executeQuery();
            if (rs.next()) {
                personalInfo.add(checkNull(loginuser));
                personalInfo.add(checkNull(rs.getString("comp_name")));
                personalInfo.add(checkNull(rs.getString("address")));
                phone = checkNull(rs.getString("user_phone"));
                if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                    personalInfo.add(phone);
                } else {
                    personalInfo.add("");
                }
                fax = checkNull(rs.getString("User_Fax"));
                if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                    personalInfo.add(fax);
                } else {
                    personalInfo.add("");
                }
                personalInfo.add(checkNull(rs.getString("User_Email")));
                personalInfo.add(checkNull(rs.getString("city")));
                country = checkNull(rs.getString("country"));
                Map hs = (Map) ecomvdsservice.getecomcountryincode(country);
                List lst = (List) hs.get("country");
                if (lst.size() > 0) {
                Map countryMap = (Map) lst.get(0);
                personalInfo.add(country + "-" + countryMap.get("countryname"));
                }
                personalInfo.add(checkNull(rs.getString("Address2")));
                personalInfo.add(checkNull(rs.getString("State")));
                personalInfo.add(checkNull(rs.getString("Pin_Code")));
                personalInfo.add(checkNull(rs.getString("TaxID")));
                personalInfo.add("");
            }
            System.out.println("[ECOM] EComCntrlBean   ====>>>>getPersonDetailsForIncompletedReg personalInfo size: " + personalInfo.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (ps != null) ps.close(); } catch (Exception e) { }
            try { if (con != null) con.close(); } catch (Exception e) { }
        }
        return personalInfo;
    }

    @Override
    public String getLocationList(String Countrycode) throws RemoteException {

        System.out.println("[ECOM] EComCntrlBean   *******getLocationList***" + Countrycode);
        StringBuffer ports = null;
        try {
            Map hs = (Map) ecomvdsservice.getecomagencydetailswithcountrycode(Countrycode);
            //
            List lst = (List) hs.get("agency");
            ports = new StringBuffer();
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    ports.append(checkNull((String) rs.get("portcode"))).append(" ~~ ");
                    ports.append(checkNull((String) rs.get("portname"))).append("##");
                    ports.append(checkNull((String) rs.get("agencycode"))).append("~#~");
                }
            } else {
                ports = new StringBuffer();
                ports.append("noports##noports");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ports.toString();
    }

    @Override
    public String getCity(String countrycode) throws RemoteException {

        System.out.println("[ECOM] EComCntrlBean   *******getCity***" + countrycode);
        StringBuffer city = null;
        try {
            city = new StringBuffer();
            Map hs = (Map) ecomvdsservice.getecomcityincountry(countrycode);
            List lst = (List) hs.get("city");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    city.append(checkNull((String) rs.get("cityname"))).append("##");
                    city.append(checkNull((String) rs.get("cityname"))).append("~~");
                }
            } else {
                city = new StringBuffer();
                city.append("nocity##nocitydesc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city.toString();
    }

    @Override
    public String getState(String countrycode) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *******getState***" + countrycode);
        StringBuffer state = null;
        try {
            state = new StringBuffer();
            Map hs = (Map) ecomvdsservice.getecomstatedetailswithcountrycode(countrycode);
            List lst = (List) hs.get("statesmaster");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    state.append(checkNull((String) rs.get("statedesc"))).append("##");
                    state.append(checkNull((String) rs.get("statecode"))).append("~~");
                }
            } else {
                state = new StringBuffer();
                state.append("nostate##nostatedesc");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return state.toString();
    }

    @Override
    public String getForwardMessage(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Forward_query, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public String getForwardMessageForDisclaimer(String module, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(disclaimermsg_query, 1004, 1007);

            ps.setString(1, module);
            ps.setString(2, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            System.out.println("[ECOM] EComCntrlBean   dismsg" + str);

            return str;

        }
    }

    public String getForwardMessageLang(String module, String criteria, String usertype, String lang) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Forward_lang_query, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            ps.setString(4, lang);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public String getBottomMessage(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(copyRight, 1004, 1007);
            ps.setString(1, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }
            ps = con.prepareStatement(version, 1004, 1007);
            ps.setString(1, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = str + "~" + checkNull(rs.getString("message"));
            }
            ps = con.prepareStatement(bottomlogo, 1004, 1007);
            ps.setString(1, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = str + "~" + checkNull(rs.getString("message"));
            }
            ps = con.prepareStatement(supportMailid, 1004, 1007);
            ps.setString(1, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = str + "~" + checkNull(rs.getString("from_mailid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public MailFormatVO getMailContent(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(MailContentQry, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                mailFormatObj.setStrMailContent(checkNull(rs.getString("message")));
                mailFormatObj.setStrFromAddr(checkNull(rs.getString("from_mailid")));
                mailFormatObj.setStrSubject(checkNull(rs.getString("subject")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return mailFormatObj;
    }

    public ArrayList getusernamePassword(String loginname) {
        ArrayList alsactivateuser = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(strgetactive, 1004, 1007);
            ps.setString(1, loginname);
            rs = ps.executeQuery();
            while (rs.next()) {
                alsactivateuser.add(checkNull(rs.getString("loginname")));
                alsactivateuser.add(checkNull(rs.getString("password")));
                alsactivateuser.add(checkNull(rs.getString("firstname")));
                alsactivateuser.add(checkNull(rs.getString("lastname")));
                alsactivateuser.add(checkNull(rs.getString("emailId")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return alsactivateuser;

    }

    @Override
    public String getMessageContent(String module, String criteria) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(MessageContentQry, 1004, 1007);
            ps.setString(1, module);
            ps.setString(2, criteria);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public String checkports(String port) throws RemoteException {

        String str = "";
        if (port.contains("-")) {
            port = port.substring(0, port.indexOf("-")).trim();
        }
        try {
            Map hs = (Map) ecomvdsservice.getecomports(port);
            //
            System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
            List lst = (List) hs.get("ports");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                str = (String) bkg.get("portcode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    @Override
    public String checkvessel(String vessel) throws RemoteException {

        String str = "";
        if (vessel.contains("-")) {
            vessel = vessel.substring(0, vessel.indexOf("-")).trim();
        }
        try {
            Map hs = (Map) ecomvdsservice.getecomvesseldetails(vessel.trim());
            List lst = (List) hs.get("vesselmaster");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = checkNull((String) rs.get("vesselcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String checkcountry(String country) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   ******checkcountry********" + country);

        String str = "";
        if (country.contains("-")) {
            country = country.substring(0, country.indexOf("-")).trim();
        }
        try {
            Map hs = (Map) ecomvdsservice.getecomcountryincode(country);
            List lst = (List) hs.get("country");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = checkNull((String) rs.get("countrycode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String checkAgency(String strAgencyCode) throws RemoteException {

        System.out.println("[ECOM] EComCntrlBean   ******checkAgency********" + strAgencyCode);

        String str = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomagencydetails(strAgencyCode);
            List lst = (List) hs.get("agency");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = checkNull((String) rs.get("agencycode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public ArrayList getShipmentTerms() throws RemoteException {

        String ShipTerms = "";
        String termsDesc = "";
        ArrayList view = new ArrayList();
        try {
            Map hs = (Map) ecomvdsservice.getecomshipmentterm();
            //////
            List lst = (List) hs.get("shipmentterms");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    ShipTerms = checkNull((String) rs.get("shipterms"));
                    termsDesc = checkNull((String) rs.get("termsdesc"));

                    view.add(ShipTerms);
                    view.add(termsDesc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void SaveToLog(String username, String module, String activity, String remarks, String referenceno, String compcode) {
        Connection logcon = null;
        PreparedStatement logpstmt = null;
        try {
            String strLogSQL = "insert into EcommuserlogDetails(username,module,activity,remarks,referenceno,Logdate,customercode)values('" + username + "','" + module + "','" + activity + "','" + remarks + "','" + referenceno + "',GETDATE(),'" + compcode + "')";
            logcon = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            logpstmt = logcon.prepareStatement(strLogSQL);
            logpstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (logpstmt != null) {
                    logpstmt.close();
                }
                if (logcon != null) {
                    logcon.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String rightTrim(String data) {
        if (data.length() == 0) {
            return (data);
        }
        int index = data.length();
        for (int x = index - 1; x >= 0; x--) {
            if (Character.isWhitespace(data.charAt(x))) {
                index = x;
            } else {
                break;
            }
        }
        return (data.substring(0, index));
    }

    public String getSecurityCode() {
        UniqueRandom urGen = null;
        try {
            String chars[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "X", "Y", "Z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            urGen = new UniqueRandom(chars, 6);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   Exception at calling the securityCode" + e);
            e.printStackTrace();
        }
        return urGen.nextUnique();
    }

    @Override
    public ArrayList getAPDetails(String cust_code) throws RemoteException {
        ArrayList personalInfo = new ArrayList();

        String phone = null, fax = null;
        Gson gson = new Gson();
        try {
            Map hs = (Map) ecomvdsservice.getecomapdetails(cust_code);

            List<CustomerMasterVo> lst = gson.fromJson(gson.toJson((ArrayList) hs.get("customermaster")), new TypeToken<List<CustomerMasterVo>>() {
            }.getType());
            if (lst.size() > 0) {
                CustomerMasterVo rs = (CustomerMasterVo) lst.get(0);
                phone = checkNull(rs.getPhone());
                fax = checkNull(rs.getFax());
                personalInfo.add(checkNull(rs.getCmpcode()));
                personalInfo.add(checkNull(rs.getCustname()));
                personalInfo.add(checkNull(rs.getAddress1()));
                if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                    personalInfo.add(phone);
                } else {
                    personalInfo.add("");
                }
                if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                    personalInfo.add(fax);
                } else {
                    personalInfo.add("");
                }
                personalInfo.add(checkNull(rs.getEmail()));
                personalInfo.add(checkNull(rs.getCity()));
                personalInfo.add(checkNull(rs.getCountryname()));
                personalInfo.add(checkNull(rs.getAddress2()));
                personalInfo.add(checkNull(rs.getState()));
                personalInfo.add(checkNull(rs.getZipcode()));
                personalInfo.add(checkNull(rs.getEori()));
                personalInfo.add(checkNull(rs.getPersontype()));
                personalInfo.add(checkNull(rs.getBillcity()));
                personalInfo.add(checkNull(rs.getBillstate()));
                personalInfo.add(checkNull(rs.getBillzipcode()));
                personalInfo.add(checkNull(rs.getBillcountry()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personalInfo;
    }

    public List getecomcustomermapping(String userCode) {
        ArrayList apconfigCustCode = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        String strResult = "";
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(chechCustCode_ecomCustMapping, 1004, 1007);
            ps.setString(1, userCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                strResult = rs.getString("apcustcode");
                apconfigCustCode.add(strResult);
            }
            System.out.println("[ECOM] EComCntrlBean   strResult----------" + strResult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return apconfigCustCode;
    }

    public ArrayList getAgencyLoginDetails(String username, String password) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String logintype = "", loginname = "";
        ArrayList arry = new ArrayList();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(strAgencyLoginAdminData, 1004, 1007);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                loginname = rs.getString("username");
                logintype = rs.getString("usertype");

            } else {
                loginname = "";
                logintype = "INVALID USER NAME AND PASSWORD";
            }
            arry.add(loginname);
            arry.add(logintype);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arry;
    }

    public ArrayList saveRegisteredMenu(String MenuName, String DevelopedBy, String Status, String Date, String RefNo, String CreatedBy, String Category, String Usertype, String menuorder, String classvalue, String attrvalue, String labelvalue, String menutype, String parentmenu) throws RemoteException {

        ArrayList RegisterMenuList = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        SimpleDateFormat source = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.sql.Date today = new java.sql.Date(date.getTime());

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String Message = "Menu Saved Successfully";
            ps = con.prepareStatement(saveRegistered);
            ps.setString(1, MenuName);
            ps.setString(2, DevelopedBy);
            ps.setString(3, Status);
            if (!Date.equals("") && Date.trim().length() > 0) {
                ps.setString(4, target.format(source.parse(Date)));
            } else {
                ps.setString(4, Date);
            }
            ps.setString(5, RefNo);
            ps.setString(6, Category);
            ps.setString(7, Usertype);
            ps.setString(8, CreatedBy);
            ps.setDate(9, today);
            ps.setString(10, menuorder);
            ps.setString(11, classvalue);
            ps.setString(12, attrvalue);
            ps.setString(13, labelvalue);
            ps.setString(14, menutype);
            ps.setString(15, parentmenu);
            ps.executeUpdate();
            RegisterMenuList = getRegisteredMenus(Message, "Load");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RegisterMenuList;
    }

    public ArrayList getRegisteredMenus(String Message, String usertype) throws RemoteException {
        ArrayList RegisteredMenus = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        RegisterMenuVO registerVo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            System.out.println("[ECOM] EComCntrlBean   >>>Message>>>>" + Message + ">>>usertype>>>" + usertype);
            if (usertype.equalsIgnoreCase("Load")) {
                ps = con.prepareStatement(RegisteredMenusLoadQry);
            } else {
                ps = con.prepareStatement(RegisteredMenusNonLoadQry);
                ps.setString(1, usertype);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuId(rs.getInt("MenuId"));
                registerVo.setMenuName(rs.getString("MenuName"));
                registerVo.setDevelopedBy(rs.getString("DevelopedBy"));
                registerVo.setMenuStatus(rs.getString("MenuStatus"));
                if (rs.getString("Date").startsWith("1900")) {
                    registerVo.setDate("");
                } else {
                    registerVo.setDate(rs.getString("Date"));
                }
                registerVo.setRefNo(rs.getString("RefNo"));
                registerVo.setCategory(rs.getString("Category"));
                registerVo.setCreatedBy(rs.getString("CreatedBy"));
                registerVo.setChgDate(rs.getString("ChgDate"));
                registerVo.setMaxId(rs.getInt("MaxId"));
                registerVo.setUserType(rs.getString("userttype"));
                registerVo.setMenuOrder(rs.getString("menuorder"));
                registerVo.setClassValue(rs.getString("ClassValue"));
                registerVo.setAttrValue(rs.getString("AttrValue"));
                registerVo.setLabelValue(rs.getString("LabelValue"));
                registerVo.setMenuType(rs.getString("MenuType"));
                registerVo.setParentMenu(rs.getString("ParentMenu"));
                registerVo.setMessage(Message);
                RegisteredMenus.add(registerVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RegisteredMenus;
    }

    @Override
    public ArrayList deleteRegisteredMenu(int MenuId, String UserType) throws RemoteException {

        ArrayList regiserList = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Message = "Menu Deleted Successfully";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(StrdeleteRegisteredQry);
            ps.setInt(1, MenuId);
            ps.executeUpdate();
            regiserList = getRegisteredMenus(Message, "Load");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return regiserList;
    }

    @Override
    public ArrayList editRegisteredMenu(String MenuName, String DevelopedBy, String Status, String Date, String RefNo, String CreatedBy, String Category, int MenuId, String Usertype, String menuorder, String classvalue, String attrvalue, String labelvalue, String menutype, String parentmenu) throws RemoteException {
        ArrayList RegisterMenuList = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat source = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.sql.Date today = new java.sql.Date(date.getTime());
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String Message = "Menu Edited Successfully";
            ps = con.prepareStatement(StreditRegisteredQry);
            ps.setString(1, MenuName);
            ps.setString(2, DevelopedBy);
            ps.setString(3, Status);
            if (!Date.equals("") && Date.trim().length() > 0) {
                ps.setString(4, target.format(source.parse(Date)));
            } else {
                ps.setString(4, " ");
            }
            ps.setString(5, RefNo);
            ps.setString(6, Category);
            ps.setString(7, CreatedBy);
            ps.setDate(8, today);
            ps.setString(9, Usertype);
            ps.setString(10, menuorder);
            ps.setString(11, classvalue);
            ps.setString(12, attrvalue);
            ps.setString(13, labelvalue);
            ps.setString(14, menutype);
            ps.setString(15, parentmenu);
            ps.setInt(16, MenuId);
            ps.executeUpdate();

            RegisterMenuList = getRegisteredMenus(Message, "Load");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RegisterMenuList;
    }

    @Override
    public String setDBConst(String ColumnName, String Value, String split) throws RemoteException {
        {
            String result = "";
            String Values[] = Value.split(split);
            for (int i = 0; i < Values.length; i++) {
                if (i == Values.length - 1) {
                    result = result + "'" + Values[i] + "'";
                } else {
                    result = result + "'" + Values[i] + "',";
                }
            }
            return result;
        }
    }

    @Override
    public ArrayList getCustMenuDetailsList(String CustId, String Type) throws RemoteException {
        ArrayList PriviMenuList = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rst = null;
        Connection conn = null;
        String menuid = null, result = null;
        try {
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = conn.prepareStatement(CustMenuDetail, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, CustId);
            rst = pst.executeQuery();
            while (rst.next()) {
                menuid = String.valueOf(rst.getString("menuid"));
                result = "(" + setDBConst(" ", menuid, "_") + ")";
                String CustMenuDetail1 = "select MR.refno from MenuRegistration MR where MR.MenuId in " + result;
                pst = conn.prepareStatement(CustMenuDetail1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rst = pst.executeQuery();
                while (rst.next()) {
                    PriviMenuList.add(checkNull(rst.getString("refno")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return PriviMenuList;
    }

    @Override
    public ArrayList getCustMenuDetails(String CustId, String Type) throws RemoteException {
        ArrayList PriviMenuList = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rst = null;
        Connection conn = null;
        RegisterMenuVO registerVo = null;
        String menuid = "";
        String result = "";
        try {
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = conn.prepareStatement(CustMenuDetail, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, CustId);
            rst = pst.executeQuery();
            while (rst.next()) {
                menuid = String.valueOf(rst.getString("menuid"));
                System.out.println("[ECOM] EComCntrlBean   getCustMenuDetails== menuid => " + menuid);
                result = "(" + setDBConst(" ", menuid, "_") + ")";
                String CustMenuDetail1 = "select MR.MenuId,MR.MenuName,MR.Category from MenuRegistration MR where MR.MenuId in " + result;
                pst = conn.prepareStatement(CustMenuDetail1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rst = pst.executeQuery();
                while (rst.next()) {
                    registerVo = new RegisterMenuVO();
                    registerVo.setMenuId(rst.getInt("MenuId"));
                    registerVo.setMenuName(rst.getString("MenuName"));
                    registerVo.setCategory("NP");
                    PriviMenuList.add(registerVo);
                }
            }
            result = "(" + setDBConst(" ", menuid, "_") + ")";
            String CustMenuDetail2 = "select MenuId,MenuName,Category from MenuRegistration where MenuId not in " + result + " and MenuStatus='A' and userttype='U' ";
            pst = conn.prepareStatement(CustMenuDetail2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rst = pst.executeQuery();
            while (rst.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuId(rst.getInt("MenuId"));
                registerVo.setMenuName(rst.getString("MenuName"));
                registerVo.setCategory(rst.getString("Category"));
                registerVo.setCustid(CustId);
                PriviMenuList.add(registerVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return PriviMenuList;
    }

    @Override
    public ArrayList removeCustMenu(String CustId, String MenuId, String Type) throws RemoteException {
        ArrayList PriviMenuRList = new ArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        try {
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = conn.prepareStatement(removeCustMenuQry);
            pst.setString(1, MenuId);
            pst.setString(2, CustId);
            pst.executeUpdate();
            PriviMenuRList = getCustMenuDetails(CustId, Type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return PriviMenuRList;
    }

    public ArrayList addCustMenu(String CustId, String MenuId, String Type) throws RuntimeException {

        ArrayList PriviMenuAList = new ArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        try {
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String Query = "";
            Query = "select * from SplPrivilegeMenu where custid=?";
            pst = conn.prepareStatement(Query);
            pst.setString(1, CustId);
            rst = pst.executeQuery();
            if (!rst.next()) {
                Query = "insert into SplPrivilegeMenu values (?,?)";
            } else {
                Query = "update SplPrivilegeMenu set menuid=? where custid=?";
            }
            pst = conn.prepareStatement(Query);
            pst.setString(1, MenuId);
            pst.setString(2, CustId);
            pst.executeUpdate();
            PriviMenuAList = getCustMenuDetails(CustId, Type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return PriviMenuAList;
    }

    public String setAgencyConstraints(String columnName, String con, String Val) throws RemoteException {
        String result = "";
        try {
            if (con.equalsIgnoreCase("Con")) {
                result = columnName + " is not NULL";
            } else if (con.equalsIgnoreCase("equal")) {
                result = columnName + " ='" + Val + "'";
            } else if (con.equalsIgnoreCase("notEqual")) {
                result = columnName + " !='" + Val + "'";
            } else if (con.equalsIgnoreCase("start")) {
                result = columnName + " Like '" + Val + "%'";
            } else if (con.equalsIgnoreCase("DStart")) {
                result = columnName + " not Like '" + Val + "%'";
            } else if (con.equalsIgnoreCase("End")) {
                result = columnName + " Like '%" + Val + "'";
            } else if (con.equalsIgnoreCase("DEnd")) {
                result = columnName + " not Like '%" + Val + "'";
            } else if (con.equalsIgnoreCase("DCon")) {
                result = columnName + " not Like '%" + Val + "%'";
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public ArrayList getAssignedModule(String vendorname) throws RemoteException {

        ArrayList alsvendar = new ArrayList();
        Connection cont = null;
        ResultSet rst = null;
        PreparedStatement pst = null;
        String strmnuname = "";
        String strmnucode = "";
        try {
            cont = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = cont.prepareStatement(strgetVendorModule, 1004, 1007);
            pst.setString(1, vendorname);
            rst = pst.executeQuery();
            while (rst.next()) {
                strmnuname = rst.getString("mnuname");
                strmnucode = rst.getString("mnuid");
                alsvendar.add(strmnuname);
                alsvendar.add(strmnucode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (cont != null) {
                    cont.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return alsvendar;
    }

    @Override
    public ArrayList getAssignableModules() throws RemoteException {

        ArrayList alsAssignableModule = new ArrayList();
        Connection conB = null;
        PreparedStatement pstB = null;
        ResultSet rsB = null;
        String strMoulename = "";
        String strModuleID = "";
        try {
            conB = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pstB = conB.prepareStatement(AssignableModuleQuery, 1004, 1007);
            rsB = pstB.executeQuery();

            while (rsB.next()) {
                strMoulename = rsB.getString("menuid");
                strModuleID = rsB.getString("menuname");
                alsAssignableModule.add(strMoulename);
                alsAssignableModule.add(strModuleID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsB != null) {
                    rsB.close();
                }
                if (pstB != null) {
                    pstB.close();
                }
                if (conB != null) {
                    conB.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return alsAssignableModule;
    }

    @Override
    public String saveVendorModules(ArrayList params, String compname) throws RemoteException {

        String strResult = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            for (int nb = 0; nb < params.size(); nb++) {
                String strgetModulename = params.get(nb).toString();
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                pstmt = con.prepareStatement(strinsertVendorModule);
                pstmt.setString(1, strgetModulename);
                pstmt.setString(2, compname);
                pstmt.executeUpdate();
                strResult = "Added";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    @Override
    public String DeleteVendorModules(ArrayList params, String compname) throws RemoteException {
        String strResult = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            for (int nb = 0; nb < params.size(); nb++) {
                String strgetModulename = params.get(nb).toString();
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                pstmt = con.prepareStatement(strdeletevendorModule);
                pstmt.setString(1, strgetModulename);
                pstmt.executeUpdate();
                strResult = "Deleted";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return strResult;

    }

    @Override
    public Map getemailformate(String strmodules, String stractivity) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String emailformates = null;
        String emailSub = null;
        Map<String, String> map = new HashMap<>();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_EMAIL_MESSAGEFORMATE, 1004, 1007);
            ps.setString(1, strmodules);
            ps.setString(2, stractivity);
            rs = ps.executeQuery();
            while (rs.next()) {
                emailformates = rs.getString(1);
                emailSub = rs.getString(2);
            }
            map.put("msg", emailformates);
            map.put("sub", emailSub);
            System.out.println("[ECOM] EComCntrlBean   strmodules=>" + strmodules + " stractivity=>" + stractivity + "emailformates=>" + emailformates
                    + " sub=>" + emailSub);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return map;
    }

    @Override
    public String getemailupdateformate(String updateformate, String strmoduls, String stractivity, String subject) throws RemoteException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String emailformates = "Successfully Updated.";
        String query = "update ecom_MessageFormat set Message=?, subject=? where Module=? and criteria=? and Type='mail' and Status='A' ";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, updateformate);
            pstmt.setString(2, subject);
            pstmt.setString(3, strmoduls);
            pstmt.setString(4, stractivity);
            int len = pstmt.executeUpdate();
            System.out.println("[ECOM] EComCntrlBean   executeUpdate");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return emailformates;
    }

    @Override
    public HashMap getBLCount(String custno, String custTypeId, String v1DataEnableStatus) throws RemoteException {

        HashMap bldeatils = new HashMap();
        String conf = "", issu = "";
        int v1Confirm = 0, v1Issue = 0, v2Confirm = 0, v2Issue = 0;
        Gson gson = new Gson();
        List<DashboardVO> countList = null;
        DashboardVO vo = null;
        ResultSet rs = null;
        Connection v1Con = null;
        PreparedStatement ps = null;
        try {
            Map hs = (Map) ecomvdsservice.getecomblcount(custno, custTypeId);
            //
            System.out.println("[ECOM] EComCntrlBean   >>bl county map>>>" + hs);

            if (hs != null && hs.size() > 0) {
                countList = gson.fromJson(gson.toJson((ArrayList) hs.get("bl_cust")), new TypeToken<List<DashboardVO>>() {
                }.getType());
            }
            if (countList != null && countList.size() > 0) {
                for (int i = 0; i < countList.size(); i++) {
                    vo = countList.get(i);
                    if ("C".equalsIgnoreCase(vo.getStatus())) {
                        conf = vo.getCountval();
                    } else if ("I".equalsIgnoreCase(vo.getStatus())) {
                        issu = vo.getCountval();
                    }
                }
            }
            conf = conf != null && conf.length() > 0 ? conf : "0";
            issu = issu != null && issu.length() > 0 ? issu : "0";
            v2Confirm = Integer.parseInt(conf);
            v2Issue = Integer.parseInt(issu);
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                v1Con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                ps = v1Con.prepareStatement(V1_BL_COUNT_QRY);
                ps.setString(1, custno);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ("C".equalsIgnoreCase(rs.getString("status"))) {
                        v1Confirm = rs.getInt("countval");
                    } else if ("I".equalsIgnoreCase(rs.getString("status"))) {
                        v1Issue = rs.getInt("countval");
                    }
                }

            }
            v2Confirm = v2Confirm + v1Confirm;
            v2Issue = v2Issue + v1Issue;
            bldeatils.put("Confirmed", String.valueOf(v2Confirm));
            bldeatils.put("Issued", String.valueOf(v2Issue));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ECOM] EComCntrlBean   Exception" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (v1Con != null) {
                    v1Con.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return bldeatils;
    }

    @Override
    public HashMap getSiCount(String custcode, String custTypeId, String v1DataEnableStatus) throws RemoteException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con_Ecom = null;
        HashMap map = new HashMap();
        StringBuffer booknobuffer = new StringBuffer();
        List<DashboardVO> countList = null;
        DashboardVO vo;
        String convstr = "";
        Gson gson = new Gson();
        Connection v1Con = null;
        int dc = 0, sc = 0, ac = 0;
        try {
            Map hs = (Map) ecomvdsservice.getecomsicount(custcode, custTypeId);
            System.out.println("[ECOM] EComCntrlBean   >>>sicount map>>" + hs);
            //
            if (hs != null && hs.size() > 0) {
                countList = gson.fromJson(gson.toJson((ArrayList) hs.get("booking")), new TypeToken<List<DashboardVO>>() {
                }.getType());
            }
            if (countList != null && countList.size() > 0) {
                for (int i = 0; i < countList.size(); i++) {
                    vo = countList.get(i);
                    booknobuffer.append("'");
                    booknobuffer.append(checkNull(vo.getBook_no()));
                    booknobuffer.append("'");
                    booknobuffer.append(",");
                }
            }
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                v1Con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                ps = v1Con.prepareStatement(V1_SI_COUNT_QRY);
                ps.setString(1, custcode);
                rs = ps.executeQuery();
                while (rs.next()) {
                    booknobuffer.append("'");
                    booknobuffer.append(checkNull(rs.getString("book_no")));
                    booknobuffer.append("'");
                    booknobuffer.append(",");
                }
            }
            if (booknobuffer.length() > 0) {
                booknobuffer.replace(booknobuffer.lastIndexOf(","), booknobuffer.lastIndexOf(",") + 1, "");
                convstr = booknobuffer.toString();
            }
            if (convstr.length() <= 0) {
                convstr = "''";
            }
            //
            con_Ecom = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String draftquery = " select count(status) as count, status from ecomSI  where  editbl=0 and bookno in(" + convstr + ")"
                    + " group by status";
            ps = con_Ecom.prepareStatement(draftquery, 1004, 1007);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (checkNull(rs.getString("status")).equalsIgnoreCase("D")) {
                    dc = rs.getInt("count");
                } else if (checkNull(rs.getString("status")).equalsIgnoreCase("S")) {
                    sc = rs.getInt("count");
                } else if (checkNull(rs.getString("status")).equalsIgnoreCase("A")) {
                    ac = rs.getInt("count");
                }
            }
            map.put("Drafts", (dc));
            map.put("Submitted", (sc));
            map.put("Accepted", (ac));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con_Ecom != null) {
                    con_Ecom.close();
                }
                if (v1Con != null) {
                    v1Con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public List getSQLBlockedData() throws RemoteException {
        List alsgetbloclData = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        SecurityVO svo = null;
        int rwcount = 0;
        try {
            String strgetBlockedQuery = "Select carrier,blockedchars,blockedwords,createddate,updateddate,status from ecom_SecurityConfig order by carrier asc";
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = con.prepareStatement(strgetBlockedQuery);
            rs = pst.executeQuery();
            while (rs.next()) {
                if ((rs.getString("blockedchars") != null && rs.getString("blockedchars").toString().trim().length() > 0) || (rs.getString("blockedwords") != null && rs.getString("blockedwords").toString().trim().length() > 0)) {
                    svo = new SecurityVO();
                    rwcount = rwcount + 1;
                    svo.setCarriercode(rs.getString("carrier"));
                    svo.setBlockchar(rs.getString("blockedchars"));
                    svo.setBlockwords(rs.getString("blockedwords"));
                    svo.setCrdate(rs.getString("createddate"));
                    svo.setUpdate(rs.getString("updateddate"));
                    svo.setStatus(rs.getString("status"));
                    String strEditAction = rs.getString("carrier").toString() + "SGM#" + rs.getString("blockedwords").toString() + "SGM#" + rs.getString("blockedchars") + "SGM#" + "U";
                    svo.setEditAction(strEditAction);
                    svo.setGetRowcount(rwcount);
                    alsgetbloclData.add(svo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alsgetbloclData;
    }

    @Override
    public String saveSQLblock(String strcarname, String strblockchar, String strwords, String strsavestate) throws RemoteException {
        if ((strwords == null) || (strwords.length() == 0)) {
            strwords = "";
        }
        if ((strblockchar == null) || (strblockchar.length() == 0)) {
            strblockchar = "";
        }
        String strResult = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlinsert = "";
        String sqlUpdate = "";
        String strSplit[] = strwords.split("SGMS");
        String strconcat = "";
        for (int i = 0; i < strSplit.length; i++) {
            if (strSplit[i].toString().trim().length() > 0) {
                strconcat = strconcat + strSplit[i] + "#" + " ";
            } else {
                strconcat = "";
            }
        }
        strwords = strconcat;
        if (strsavestate.equalsIgnoreCase("U")) {
            sqlUpdate = "update  ecom_SecurityConfig set  blockedchars =?, blockedwords =?,updateddate=getDate() where carrier='" + strcarname + "'";
        } else {
            sqlinsert = "Insert into ecom_SecurityConfig(carrier,blockedchars,blockedwords,createddate,status,updateddate)values(?,?,?,getDate(),'A',getDate())";
        }
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if (strsavestate.equalsIgnoreCase("N")) {

                pstmt = con.prepareStatement(sqlinsert);
                pstmt.setString(1, strcarname);
                pstmt.setString(2, strblockchar);
                pstmt.setString(3, strwords);
                pstmt.executeUpdate();
                strResult = "Inserted";
            } else {
                pstmt = con.prepareStatement(sqlUpdate);
                pstmt.setString(1, strblockchar);
                pstmt.setString(2, strwords);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    @Override
    public String SavechangeStatus(String strcarname, String strstatechange) throws RemoteException {

        String strResult = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlUpdate = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            sqlUpdate = "update  ecom_SecurityConfig set   status =?,updateddate=getDate() where carrier='" + strcarname + "'";
            pstmt = con.prepareStatement(sqlUpdate);
            pstmt.setString(1, strstatechange);
            pstmt.executeUpdate();
            strResult = "Updated";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    //changed by Jayashree
    @Override
    public List UserModuleMappingCustomerInfo(String inputColumn, String constraints, String columnValue, String usercode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List parties = new ArrayList();
        String result = "";
        CustomerProfile profile = null;
        String query = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            if (columnValue.trim().length() > 0) {
                String columnName;
                if ("Email".equalsIgnoreCase(inputColumn)) {
                    columnName = "user_email";
                } else if ("CustCode".equalsIgnoreCase(inputColumn)) {
                    columnName = "cust_code";
                } else {
                    columnName = "comp_name";
                }
                String condition = setConstraints(columnName, constraints);
                query = "SELECT DISTINCT loginname, cust_code, address, ecom_reg_details.city, user_email, comp_name, statecode, registered_date, country "
                        + "FROM ecom_reg_details "
                        + "INNER JOIN customermaster ON cust_code = CustCode AND customermaster.status = 'A' "
                        + "WHERE user_email IS NOT NULL AND cust_code IS NOT NULL AND LEN(cust_code) > 0 "
                        + "AND ecom_reg_details.status = 'A' AND " + condition;

                ps = con.prepareStatement(query, 1004, 1007);

// Set the parameter based on the operator
                switch (constraints.toLowerCase()) {
                    case "con":
                        ps.setString(1, "%" + columnValue + "%");
                        break;
                    case "dcon":
                        ps.setString(1, "%" + columnValue + "%");
                        break;
                    case "start":
                        ps.setString(1, columnValue + "%");
                        break;
                    case "dstart":
                        ps.setString(1, columnValue + "%");
                        break;
                    case "end":
                        ps.setString(1, "%" + columnValue);
                        break;
                    case "dend":
                        ps.setString(1, "%" + columnValue);
                        break;
                    case "equal":
                        ps.setString(1, columnValue);
                        break;
                    case "notequal":
                        ps.setString(1, columnValue);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid constraint type: " + constraints);
                }
            } else {
                query = "select distinct loginname,cust_code,address,ecom_reg_details.city,user_email,comp_name,statecode,registered_date,country from ecom_reg_details \n"
                        + " inner join customermaster on cust_code=CustCode and customermaster.status='A' \n"
                        + "where user_email is not null and cust_code is not null and len(cust_code)>0 and ecom_reg_details.status='A'";
                ps = con.prepareStatement(query, 1004, 1007);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                profile = new CustomerProfile();
                String getwebid = checkNull(rs.getString("loginname"));
                if (getwebid.length() > 0) {
                    profile.setCustId(checkNull(rs.getString("cust_code")));
                    profile.setCustName(checkNull(rs.getString("loginname")).toUpperCase());
                    profile.setAddr1(checkNull(rs.getString("address")).toUpperCase());
                    profile.setCity(checkNull(rs.getString("city")).toUpperCase());
                    profile.setCitycode(checkNull(rs.getString("city")).toUpperCase());
                    profile.setEmailId(checkNull(rs.getString("user_email")));
                    profile.setCustomerName(checkNull(rs.getString("comp_name")));
                    profile.setState(checkNull(rs.getString("statecode")));
                    profile.setCountry(checkNull(rs.getString("country")));
                    profile.setRegistereddate(checkNull(rs.getString("registered_date")));
                    parties.add(profile);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parties;
    }

    @Override
    public List getIPBlockedData() throws RemoteException {
        List alsgetIPbloclData = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        SecurityVO svo = null;
        int rwcount = 0;
        String blip = "";
        String bliprange = "";
        try {
            String strgetBlockedQuery = "Select carrier,blockedip,blockediprange,createddate,updateddate,status from ecom_SecurityConfig(nolock) where blockedip is not null or blockediprange is not null order by carrier asc";
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = con.prepareStatement(strgetBlockedQuery);
            rs = pst.executeQuery();
            while (rs.next()) {
                if ((rs.getString("blockediprange") != null && rs.getString("blockediprange").toString().trim().length() > 0) || (rs.getString("blockedip") != null && rs.getString("blockedip").toString().trim().length() > 0)) {
                    svo = new SecurityVO();
                    rwcount = rwcount + 1;
                    svo.setCarriercode(rs.getString("carrier"));
                    if (rs.getString("blockedip") != null && rs.getString("blockedip").toString().trim().length() > 0) {
                        svo.setBlockedip(rs.getString("blockedip"));
                        blip = rs.getString("blockedip");
                    } else {
                        svo.setBlockedip("");
                        blip = "";
                    }
                    if (rs.getString("blockediprange") != null && rs.getString("blockediprange").toString().trim().length() > 0) {
                        svo.setBlockiprange(rs.getString("blockediprange"));
                        bliprange = rs.getString("blockediprange");
                    } else {
                        svo.setBlockiprange("");
                        bliprange = "";
                    }
                    svo.setCrdate(rs.getString("createddate"));
                    svo.setUpdate(rs.getString("updateddate"));
                    svo.setStatus(rs.getString("status"));
                    String strEditAction = rs.getString("carrier").toString() + "SGM#" + blip + "SGM#" + bliprange + "SGM#" + "U";
                    svo.setEditAction(strEditAction);
                    svo.setGetRowcount(rwcount);
                    alsgetIPbloclData.add(svo);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alsgetIPbloclData;
    }

    @Override
    public String saveIPblock(String strcarname, String strblockchar, String strwords, String strsavestate) throws RemoteException {
        if ((strwords == null) || (strwords.length() == 0)) {
            strwords = "";
        }
        if ((strblockchar == null) || (strblockchar.length() == 0)) {
            strblockchar = "";
        }
        String strResult = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlinsert = "";
        String sqlUpdate = "";

        Connection congetR = null;
        ResultSet rsR = null;
        PreparedStatement pstmtR = null;
        String strgetCarrier = "";
        try {
            String strGetSQL = "Select carrier from ecom_SecurityConfig where carrier='" + strcarname + "'";
            congetR = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pstmtR = congetR.prepareStatement(strGetSQL);
            rsR = pstmtR.executeQuery();
            while (rsR.next()) {
                strgetCarrier = rsR.getString("carrier");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsR != null) {
                    rsR.close();
                }
                if (pstmtR != null) {
                    pstmtR.close();
                }
                if (congetR != null) {
                    congetR.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (strgetCarrier.trim().length() > 0) {

            strsavestate = "U";
        }
        String strSplit[] = strblockchar.split("SGMS");
        String strconcat = "";
        for (int i = 0; i < strSplit.length; i++) {
            if (strSplit[i].toString().trim().length() > 0) {
                strconcat = strconcat + strSplit[i] + "#";
            } else {
                strconcat = "";
            }
        }
        strblockchar = strconcat;
        if (strsavestate.equalsIgnoreCase("U")) {
            sqlUpdate = "update  ecom_SecurityConfig set  blockedIP =?, blockediprange =?,updateddate=getDate() where carrier='" + strcarname + "'";
        } else {
            sqlinsert = "Insert into ecom_SecurityConfig(carrier,blockedip,blockediprange,createddate,status,updateddate)values(?,?,?,getDate(),'A',getDate())";
        }
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if (strsavestate.equalsIgnoreCase("N")) {

                pstmt = con.prepareStatement(sqlinsert);
                pstmt.setString(1, strcarname);
                pstmt.setString(2, strblockchar);
                pstmt.setString(3, strwords);
                pstmt.executeUpdate();
                strResult = "Inserted";

            } else {
                pstmt = con.prepareStatement(sqlUpdate);
                pstmt.setString(1, strblockchar);
                pstmt.setString(2, strwords);
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    @Override
    public String SavechangeIPStatus(String strcarname, String strstatechange) throws RemoteException {
        String strResult = "";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlUpdate = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            sqlUpdate = "update  ecom_SecurityConfig set   blockedip =?,blockediprange=?,updateddate=getDate() where carrier='" + strcarname + "'";
            pstmt = con.prepareStatement(sqlUpdate);
            pstmt.setString(1, "");
            pstmt.setString(2, "");
            pstmt.executeUpdate();
            strResult = "Updated";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    @Override
    public String CheckUserEmail(String compid, String useremail) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "New";
        String newquery = "select loginname from ecom_reg_details(nolock) where loginname=?";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(newquery);
            ps.setString(1, useremail);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = "Exists";
            }
            if (compid.trim().length() == 0) {
                str = "New";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public Hashtable getglobalconfigMail() throws RemoteException {
        Hashtable allObj = new Hashtable();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String value = "";
        String keycode = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_GLOBAL_CONFIGMAIL);
            rs = ps.executeQuery();
            while (rs.next()) {
                value = checkNull(rs.getString("pathvalue"));
                keycode = checkNull(rs.getString("pathname"));
                allObj.put(keycode, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (rs != null) {
                    rs.close();

                }
                if (con != null) {
                    con.close();

                }
                if (ps != null) {
                    ps.close();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allObj;
    }

    @Override
    public List getGlobalConfigDetails(String adminType) throws RemoteException {
        List configDetails = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GlobalConfigVO configVO = null;
        String query = "select pathname,pathvalue from globalconfigproduct";
        try {
            if (adminType.equalsIgnoreCase("loginmenupage")) {
                query = "select pathname,pathvalue from globalconfigproduct where status='A'";
            }
            configDetails = new ArrayList();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                configVO = new GlobalConfigVO();
                configVO.setKey(checkNull(rs.getString("pathname")));
                configVO.setValue(checkNull(rs.getString("pathvalue")));
                configDetails.add(configVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return configDetails;
    }

    @Override
    public List getRegistrationPendingMailDetails() throws RemoteException {
        List registerpendingdetails = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RegistrationVO registervo = null;
        try {
            registerpendingdetails = new ArrayList();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_REG_PENDIONGMAIL);
            rs = ps.executeQuery();
            while (rs.next()) {
                registervo = new RegistrationVO();
                registervo.setLoginname(checkNull(rs.getString("loginname")));
                registervo.setFirstname(checkNull(rs.getString("firstname")));
                registervo.setLastname(checkNull(rs.getString("lastname")));
                registervo.setCompanyname(checkNull(rs.getString("compName")));
                registerpendingdetails.add(registervo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registerpendingdetails;
    }

    @Override
    public List getRejectedUserDetails() throws RemoteException {
        List rejectedusers = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RegistrationVO registervo = null;
        try {
            rejectedusers = new ArrayList();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_REJECTED_USERDETAILS);
            rs = ps.executeQuery();
            while (rs.next()) {
                registervo = new RegistrationVO();
                registervo.setLoginname(checkNull(rs.getString("loginname")));
                registervo.setFirstname(checkNull(rs.getString("firstname")));
                registervo.setLastname(checkNull(rs.getString("lastname")));
                registervo.setCompanyname(checkNull(rs.getString("compName")));
                registervo.setCompanyId(checkNull(rs.getString("refid")));
                registervo.setCust_no(checkNull(rs.getString("id")));
                rejectedusers.add(registervo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rejectedusers;
    }

    @Override
    public String getGlobalImagePath() throws RemoteException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_GLOBAL_IMAGEPATH);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("pathvalue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String getEcomReleaseVersion() throws RemoteException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_RELEASEVERSION);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String saveGlobalConfigDetails(String key, String value) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(SET_GOBAL_CONFIGDETAILS);
            ps.setString(1, key);
            ps.setString(2, value);
            int i = ps.executeUpdate();
            if (i == 1) {
                result = "inserted";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String updateGlobalConfigDetails(String key, String value) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(PUT_GLOBAL_CONFIGDETAILS);
            ps.setString(1, key);
            ps.setString(2, value);
            ps.setString(3, key);
            int i = ps.executeUpdate();
            if (i == 1) {
                result = "inserted";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String getCutomerOblStatus(String usercode) throws RemoteException {

        String result = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomcustomermaster(usercode);
            //
            List lst = (List) hs.get("customermaster");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                result = (String) bkg.get("oblprint");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @SuppressWarnings("static-access")
    public ArrayList getTemplateNo(String loginUser, String flag) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EbookingValidatorFormBeanNew eBooking = null;
        List<DropDownVO> temp_no = new ArrayList<DropDownVO>();
        DropDownVO ddVO = null;
        ArrayList backup_temp = new ArrayList();
        ArrayList Managetemp = new ArrayList();
        ArrayList all = new ArrayList();
        ddVO = new DropDownVO();
        ddVO.setCode("Select Template");
        ddVO.setName("Select Template");
        temp_no.add(ddVO);
        backup_temp.add("Select");
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_TEMPLATENAME, 1004, 1007);
            ps.setString(1, loginUser);
            ps.setString(2, flag);
            rs = ps.executeQuery();
            String Bookno = "";
            String templatename = "";
            String originport = "";
            String finalport = "";
            while (rs.next()) {
                eBooking = new EbookingValidatorFormBeanNew();
                Bookno = checkNull(rs.getString("ebookno"));
                templatename = checkNull(rs.getString("templatename"));
                originport = checkNull(rs.getString("originPortId"));
                finalport = checkNull(rs.getString("finalPortId"));
                eBooking.setEbook_templateno(Bookno);
                eBooking.setEbook_templatename(templatename);
                eBooking.setEbook_pol(originport);
                eBooking.setEbook_pod(finalport);
                ddVO = new DropDownVO();
                ddVO.setCode(Bookno);
                ddVO.setName(templatename);
                temp_no.add(ddVO);
                backup_temp.add(templatename);
                Managetemp.add(eBooking);
            }
            all.add(temp_no);
            all.add(backup_temp);
            all.add(Managetemp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return all;
    }

    @Override
    public String checkDGDetails(String unno, String shipname, String dgclass) throws RemoteException {
        String result = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomcheckdgdetails(unno, shipname, dgclass);
            //
            List lst = (List) hs.get("dgdetail");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                result = (String) bkg.get("dgno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getWebserviceUrl() throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_WEBSERVICERUL);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = checkNull(rs.getString("pathvalue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String getClientNameForProperty() throws RemoteException {
        String clientcode = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomcarrier();
            //
            List lst = (List) hs.get("carrier");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                clientcode = (String) bkg.get("carriercode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientcode;
    }

    public boolean checkSpecialCharacter(String s) {
        boolean flag = true;
        if (s == null || s.trim().isEmpty()) {
            flag = false;
            return flag;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if (b == true) {
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    @Override
    public String deleteGlobalConfigDetails(String key) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETE_GLOBALCONFIG_DETAILS);
            ps.setString(1, key);
            int i = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String getEmptyPickPoint(String port, String type) throws RemoteException {
        String result = null;
        int i = 0;
        try {
            System.out.println("[ECOM] EComCntrlBean   ===>type===" + type);
            if ("D".equalsIgnoreCase(type)) {
                Map hs = (Map) ecomvdsservice.getecomemptypickpoint(port);
                List lst = (List) hs.get("depotcode");
                if (lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        Map rs = (Map) lst.get(j);
                        if (i == 0) {
                            result = checkNull((String) rs.get("depot")) + "##" + checkNull((String) rs.get("description"));
                        } else {
                            result = result + "~~" + checkNull((String) rs.get("depot")) + "##" + checkNull((String) rs.get("description"));
                        }
                        i++;
                    }
                }
            } else {
                Map hs = (Map) ecomvdsservice.getecomemptypickpoint_terminal(port);
                List lst = (List) hs.get("terminals");
                if (lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        Map rs = (Map) lst.get(j);
                        if (i == 0) {
                            result = checkNull((String) rs.get("terminalcode")) + "##" + checkNull((String) rs.get("terminalname"));
                        } else {
                            result = result + "~~" + checkNull((String) rs.get("terminalcode")) + "##" + checkNull((String) rs.get("terminalname"));
                        }
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, String> getEmptyPickUpMap() throws RemoteException {
        Map<String, String> map = null;

        try {
            Map hs = (Map) ecomvdsservice.getecomemptypickup();
            List lst = (List) hs.get("depotcode");
            map = new HashMap<String, String>();
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    map.put((String) rs.get("depot"), (String) rs.get("description"));
                }
            }

            Map hss = (Map) ecomvdsservice.getecomemptypickpoint_view();
            List lst_terminal = (List) hss.get("terminals");
            if (lst_terminal.size() > 0) {
                for (int j = 0; j < lst_terminal.size(); j++) {
                    Map rs = (Map) lst_terminal.get(j);
                    map.put((String) rs.get("terminalcode"), (String) rs.get("terminalname"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<MailAlertVO> getMailAlertActivities(String loginName) throws RemoteException {
        List<MailAlertVO> listAlertVO = null;
        MailAlertVO alertVO = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean check = false;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECKLOGINNAMEINMAIL);
            ps.setString(1, loginName);
            rs = ps.executeQuery();
            if (rs.next()) {
                check = true;
            }
            listAlertVO = new ArrayList<MailAlertVO>();
            if (check) {
                ps = con.prepareStatement(GETMAILALERTFORUSER);
                ps.setString(1, loginName);
            } else {
                ps = con.prepareStatement(GETMAILALERTDET);

            }
            rs = ps.executeQuery();
            while (rs.next()) {
                alertVO = new MailAlertVO();
                alertVO.setModule(checkNull(rs.getString("moduledesc")));
                alertVO.setActivity(checkNull(rs.getString("activity")));
                alertVO.setActivityCode(checkNull(rs.getString("activitycode")));
                alertVO.setMailAlert(rs.getString("mail"));
                if (check) {
                    alertVO.setAlreadyExist("Y");
                } else {
                    alertVO.setAlreadyExist("N");
                }
                listAlertVO.add(alertVO);
            }
            System.out.println("[ECOM] EComCntrlBean   >>listAlertVO.size>>" + listAlertVO.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listAlertVO;
    }

    @Override
    public String saveMailAlertActivities(List<MailAlertVO> listAlertVO) throws RemoteException {
        String result = "updated";
        MailAlertVO alertVO = null;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if (listAlertVO != null && listAlertVO.size() > 0) {
                for (int i = 0; i < listAlertVO.size(); i++) {
                    alertVO = listAlertVO.get(i);
                    if (alertVO.getAlreadyExist().equalsIgnoreCase("Y")) {
                        ps = con.prepareStatement(UPDATEUSERMAILALERT);
                        ps.setString(1, alertVO.getMailAlert());
                        ps.setString(2, alertVO.getLoginName());
                        ps.setString(3, alertVO.getLoginName());
                        ps.setString(4, alertVO.getActivityCode());
                        ps.executeUpdate();
                    } else {
                        ps = con.prepareStatement(INSERTINTOMAILALERT);
                        ps.setString(1, alertVO.getLoginName());
                        ps.setString(2, alertVO.getActivityCode());
                        ps.setString(3, alertVO.getMailAlert());
                        ps.setString(4, alertVO.getLoginName());
                        ps.executeUpdate();
                    }
                }
            }
            if ("en".equalsIgnoreCase(alertVO.getLangType())) {

                result = checkNull(getForwardMessage("Myalert", "update", "c"));
            } else if ("ch".equalsIgnoreCase(alertVO.getLangType())) {
                result = checkNull(getForwardMessageLang("Myalert", "update", "c", "ch"));
            } else if ("ma".equalsIgnoreCase(alertVO.getLangType())) {
                result = checkNull(getForwardMessageLang("Myalert", "update", "c", "ma"));
            } else if ("es".equalsIgnoreCase(alertVO.getLangType())) {
                result = checkNull(getForwardMessageLang("Myalert", "update", "c", "es"));
            }
        } catch (Exception e) {
            result = "not updated";
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getPODIntransitMap() throws RemoteException {
        Map<String, String> map = null;
        try {
            Map hs = (Map) ecomvdsservice.getecomtransit();
            List lst = (List) hs.get("dmsintransit");
            map = new HashMap<String, String>();
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    map.put((String) rs.get("dmsfinaldest"), (String) rs.get("dmsfinaldestdesc"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<CutoffConfigVO> getCutOffList() throws RemoteException {
        List<CutoffConfigVO> cutOffList = null;
        CutoffConfigVO cutOffVO = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cutOffList = new ArrayList<CutoffConfigVO>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_CUTOFFLIST);
            rs = ps.executeQuery();
            while (rs.next()) {
                cutOffVO = new CutoffConfigVO();
                cutOffVO.setPort(checkNull(rs.getString("port")));
                cutOffVO.setTerminal(checkNull(rs.getString("terminal")));
                cutOffVO.setCfsGateClosing(checkNull(rs.getString("cfsclosing")));
                cutOffVO.setVgmClosing(checkNull(rs.getString("vgmclosing")));
                cutOffVO.setDocClosing(checkNull(rs.getString("docclosing")));
                cutOffVO.setFullContClosing(checkNull(rs.getString("fullcntrclosing")));
                cutOffVO.setEdiDecClosing(checkNull(rs.getString("ediclosing")));
                cutOffVO.setExcludeweekend(checkNull(rs.getString("excludeweekend")));
                cutOffList.add(cutOffVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cutOffList;
    }

    @Override
    public int saveCutoffConfigDetails(CutoffConfigVO cutOffVO) throws RemoteException {
        int i = 0;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if ("save".equalsIgnoreCase(cutOffVO.getSaveType())) {
                ps = con.prepareStatement(INSERT_CUTOFFCONFIG);
                ps.setString(1, cutOffVO.getPort());
                ps.setString(2, cutOffVO.getTerminal());
                ps.setString(3, cutOffVO.getCfsGateClosing());
                ps.setString(4, cutOffVO.getVgmClosing());
                ps.setString(5, cutOffVO.getDocClosing());
                ps.setString(6, cutOffVO.getEdiDecClosing());
                ps.setString(7, cutOffVO.getFullContClosing());
                ps.setString(8, cutOffVO.getExcludeweekend());
                ps.setString(9, cutOffVO.getCruser());
                ps.setString(10, cutOffVO.getCruser());
                i = ps.executeUpdate();
                System.out.println("[ECOM] EComCntrlBean   >>CUTOFFINSERT>>" + i);
            } else if ("update".equalsIgnoreCase(cutOffVO.getSaveType())) {
                ps = con.prepareStatement(UPDATECUTOFFCONFIG);
                ps.setString(1, cutOffVO.getCfsGateClosing());
                ps.setString(2, cutOffVO.getVgmClosing());
                ps.setString(3, cutOffVO.getDocClosing());
                ps.setString(4, cutOffVO.getEdiDecClosing());
                ps.setString(5, cutOffVO.getFullContClosing());
                ps.setString(6, cutOffVO.getExcludeweekend());
                ps.setString(7, cutOffVO.getCruser());
                ps.setString(8, cutOffVO.getPort());
                ps.setString(9, cutOffVO.getTerminal());
                i = ps.executeUpdate();
                System.out.println("[ECOM] EComCntrlBean   >>CUTOFFUPDATE>>" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    @Override
    public int deleteCutoffConfig(String port, String terminal) throws RemoteException {
        int i = 0;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETECUTOFFCONFIG);
            ps.setString(1, port);
            ps.setString(2, terminal);
            i = ps.executeUpdate();
            System.out.println("[ECOM] EComCntrlBean   >>CUTOFFDELETE>>" + i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    @Override
    public String checkCutOffDuplicate(String port, String terminal) throws RemoteException {
        String result = "Not Exist";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECKDUPCUTOFF);
            ps.setString(1, port);
            ps.setString(2, terminal);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Exist";
            }
            System.out.println("[ECOM] EComCntrlBean   >>check duplicatie>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private CutoffConfigVO setCutoffConfiVo(ResultSet rs) throws RemoteException {
        CutoffConfigVO cutOffVO = null;
        try {
            cutOffVO = new CutoffConfigVO();
            cutOffVO.setPort(checkNull(rs.getString("port")));
            cutOffVO.setTerminal(checkNull(rs.getString("terminal")));
            cutOffVO.setCfsGateClosing(checkNull(rs.getString("cfsclosing")));
            cutOffVO.setVgmClosing(checkNull(rs.getString("vgmclosing")));
            cutOffVO.setDocClosing(checkNull(rs.getString("docclosing")));
            cutOffVO.setFullContClosing(checkNull(rs.getString("fullcntrclosing")));
            cutOffVO.setEdiDecClosing(checkNull(rs.getString("ediclosing")));
            cutOffVO.setExcludeweekend(checkNull(rs.getString("excludeweekend")));
        } catch (SQLException ex) {
            Logger.getLogger(EComCntrlBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cutOffVO;
    }

    @Override
    public CutoffConfigVO getRoutingCutOffDetails(String port, String terminal) throws RemoteException {
        CutoffConfigVO cutOffVO = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isResultAvb = true;
        System.out.println("[ECOM] EComCntrlBean   >>entered in cutoff calc");
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETCUTOFFFORROUTING);

            ps.setString(1, port);
            ps.setString(2, terminal);
            rs = ps.executeQuery();
            if (rs.next()) {
                cutOffVO = setCutoffConfiVo(rs);
                isResultAvb = false;
            }
            if (isResultAvb) {
                ps.setString(1, port);
                ps.setString(2, "All");
                rs = ps.executeQuery();
                if (rs.next()) {
                    cutOffVO = setCutoffConfiVo(rs);
                    isResultAvb = false;
                }
            }
            if (isResultAvb) {
                ps.setString(1, "All");
                ps.setString(2, "All");
                rs = ps.executeQuery();
                if (rs.next()) {
                    cutOffVO = setCutoffConfiVo(rs);
                    isResultAvb = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cutOffVO;
    }

    @Override
    public String checkContainer(String containterid) throws RemoteException {
        String str = "";
        try {
            Map hs = (Map) ecomsivdsservice.getecomcontainer(containterid);
            //
            System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
            List lst = (List) hs.get("containerprofile");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = checkNull((String) rs.get("containerno")).trim();
            }
            if (str.equalsIgnoreCase(containterid)) {
                str = "Exists";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   str==>" + str);
        return str;
    }

    public MailFormatVO getMailContentFromCommon(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(ECOM_MAILCONTENT);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                mailFormatObj.setStrMailContent(checkNull(rs.getString("message")));
                mailFormatObj.setStrFromAddr(checkNull(rs.getString("from_mailid")));
                mailFormatObj.setStrSubject(checkNull(rs.getString("subject")));
                mailFormatObj.setActivityCode(checkNull(rs.getString("activitycode")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return mailFormatObj;
    }

    public Map getMasterConfigDetails() throws RemoteException {
        Map configDetails = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            configDetails = new HashMap();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(ECOM_MASTER_CONFIGDETAILS);
            rs = ps.executeQuery();
            while (rs.next()) {
                configDetails.put(rs.getString("pathname"), rs.getString("pathvalue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return configDetails;
        }
    }

    public String getPortType(String portcode) throws RemoteException {

        String type = "";
        portcode = checkNull(portcode);
        try {
            Map hs = (Map) ecomvdsservice.getecomports(portcode);
            //
            System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
            List lst = (List) hs.get("ports");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                type = (String) bkg.get("porttype");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    public String getContainerValidate(String containerno, String soc, String eqpSize) throws RemoteException {

        String contno = "", soctype = "", result = "", starcare = null;
        try {
            Map hs = (Map) ecomvdsservice.getecomcheckcontainerwithtype(containerno, checkNull(eqpSize));
            //
            System.out.println("[ECOM] EComCntrlBean   getContainerValidate######>" + hs);
            List lst = (List) hs.get("equipmentpassport");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                contno = (String) bkg.get("containerno");
                soctype = (String) bkg.get("shipperownedunit");
                starcare = (String) bkg.get("fleetgroup");
                if ("N".equalsIgnoreCase(soc)) {
                    result = soctype != null && soctype.length() > 0 && "N".equalsIgnoreCase(soctype) ? "valid" : "Invalid";
                } else {
                    result = soctype != null && soctype.length() > 0 ? "valid" : "Invalid";
                }
                /* if ("STAR CARE".equalsIgnoreCase(starcare)) {
                    result = "starcare";
                }*/
            } else {
                result = "Invalid";
            }
            System.out.println("[ECOM] EComCntrlBean   getContainerValidate==>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<ScheduleCommonVO> getByPorts(String portcode, String date, String isLCT) throws RemoteException {

        portcode = checkNull(portcode);
        Gson gson = null;
        List<ScheduleCommonVO> routlst = null;
        try {
            Map hs = null;
            if ("Y".equalsIgnoreCase(isLCT)) {
                hs = (Map) ecomvdsservice.getEcomByPortForLCT(portcode, date);
            } else {
                hs = (Map) ecomvdsservice.getEcomByPort(portcode, date);
            }
            gson = new Gson();
            routlst = gson.fromJson(gson.toJson((ArrayList) hs.get("uvs_scheduleview")), new TypeToken<List<ScheduleCommonVO>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routlst;
    }

    @Override
    public List<ScheduleCommonVO> getByVessel(String vesselcode, String date, String isLCT) throws RemoteException {

        Gson gson = null;
        List<ScheduleCommonVO> routlst = null;
        try {
            Map hs = null;
            if ("Y".equalsIgnoreCase(isLCT)) {
                hs = (Map) ecomvdsservice.getecombyvesselForLCT(vesselcode, date);
            } else {
                hs = (Map) ecomvdsservice.getecombyvessel(vesselcode, date);
            }
            gson = new Gson();
            routlst = gson.fromJson(gson.toJson((ArrayList) hs.get("uvs_scheduleview")), new TypeToken<List<ScheduleCommonVO>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routlst;
    }

    public void sendMailFromEjb(Mailer mail, String fromMailId, String initiatorID, String carrierCode) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   carriercodevalueejb" + carrierCode);
        String disclaimerMsg = null;

        String fromId = fromMailId;
        //   String carrierCode = "SVM";
        //      String initiatorID = "ECOM-TRA";//either module id or service id
        List<String> bccList = null;
        List<String> ccList = null;
        String subject = mail.getSubject();
        String bodyContent = mail.getMessage();
        String toemail = mail.getTo();
        String bccMail = mail.getBcc();
        String ccMail = mail.getCc();
        String contentType = "text/html; charset=utf-8";
        System.out.println("[ECOM] EComCntrlBean   contentype mail===>" + contentType);
        List<String> toList = new ArrayList();
        String to_email[] = toemail.split(",");
        String bcc_email[] = null;
        String cc_email[] = null;
        disclaimerMsg = getForwardMessageForDisclaimer("All", "c");
        if (disclaimerMsg != null && disclaimerMsg.length() > 0 && disclaimerMsg != "") {
            bodyContent = bodyContent + disclaimerMsg;
        } else {

            bodyContent = bodyContent;
        }

        System.out.println("[ECOM] EComCntrlBean   ####>>>>toemail" + toemail);
        for (int i = 0; i < to_email.length; i++) {
            toList.add(to_email[i]);
        }
        if (bccMail != null && bccMail.length() > 0) {
            bccList = new ArrayList<String>();
            bcc_email = bccMail.split(",");
            for (int i = 0; i < bcc_email.length; i++) {
                bccList.add(bcc_email[i]);
            }
        }
        if (ccMail != null && ccMail.length() > 0) {
            ccList = new ArrayList<String>();
            cc_email = ccMail.split(",");
            for (int i = 0; i < cc_email.length; i++) {
                ccList.add(cc_email[i]);
            }
        }
        mailer.setAttachmentList(null);
        List<AttachmentModel> attachmentModelList = new ArrayList();
        String att_path = mail.getAttachmentpath();
        String attache = mail.getAttachment();
        System.out.println("[ECOM] EComCntrlBean   >>attache>>" + attache);
        if (attache != null && attache.length() > 0) {
            String[] stratt = attache.split("~~~");
            for (int j = 0; j < stratt.length; j++) {
                String filepath = att_path + stratt[j];
                String filename = new File(stratt[j]).getName();
                //  System.out.println("[ECOM] EComCntrlBean   >>>filename>>" + filename);
                attachmentModelList.add(getFileFromSpecifiedPath(filepath, filename));
                mailer.setAttachmentList(attachmentModelList);
            }
        }
        mailer.setContentType(contentType);
        mailer.setFrmId(fromId);
        mailer.setCarrierCode(carrierCode);
        mailer.setInitiatorID(initiatorID);
        mailer.setBccList(bccList);
        mailer.setCcList(ccList);
        mailer.setToList(toList);
        mailer.setSubject(subject);
        mailer.setBody(bodyContent);
        mailer.sendMail();
        System.out.println("[ECOM] EComCntrlBean   sendMailFromEjb finish !!! ");
    }

    private AttachmentModel getFileFromSpecifiedPath(String filepath, String filename) {

        AttachmentModel fileAttachmentFromDisk = new AttachmentModel();
        fileAttachmentFromDisk.setFilePath(filepath);
        // fileAttachmentFromDisk.setContentType("application/vnd.ms-excel");
        fileAttachmentFromDisk.setFileName(filename);
        fileAttachmentFromDisk.setContentDisposition(AttachmentModel.CONTENTDISPOSITION.ATTACHMENT);
        return fileAttachmentFromDisk;
    }

    // ExcelUpload
    @Override
    public String checkEQPSize(String containterid) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *******checkEQPSize***" + containterid);
        String str = "";
        try {
            Map hs = (Map) ecomsivdsservice.getecomequiptype(containterid);
            //
            //  System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
            List lst = (List) hs.get("containerprofile");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = (String) rs.get("equipmenttype");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  System.out.println("[ECOM] EComCntrlBean   str==>" + str);
        return str;
    }

    @Override
    public Map getEqpMap() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getEqpList*******");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String type = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomeqpmap();
            List lst = (List) hs.get("ecom_equipments");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("equiptype"));
                    type = checkNull((String) rs.get("type"));
                    eqpListObj.put(eqptype, type);
                }
            }
            //   System.out.println("[ECOM] EComCntrlBean   getEqpMap:::::::" + eqpListObj);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean    Exception Caught AT getEqpList==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public Map getEqpments() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getEqpments*******");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String type = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomeqpments();
            List lst = (List) hs.get("ecom_equipments");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("equiptype"));
                    type = checkNull((String) rs.get("equipdesc"));
                    eqpListObj.put(eqptype, type);
                }
            }

            //  System.out.println("[ECOM] EComCntrlBean   getEqpments:::::::" + eqpListObj);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean     Exception Caught AT getEqpList==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public Map getUnno() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getEqpList*******");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String type = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomimdgclass();
            List lst = (List) hs.get("imdgclass");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("unno"));
                    type = checkNull((String) rs.get("imdgname"));
                    eqpListObj.put(eqptype, type);
                }
            }
            //  System.out.println("[ECOM] EComCntrlBean   getUnno:::::::::" + eqpListObj);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT getUNmno==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public Map getIMGClass() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getEqpList*******");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String imdgname = null;
        String imdgclass = null;
        String key = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomimdgclass();
            List lst = (List) hs.get("imdgclass");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("unno"));
                    imdgname = checkNull((String) rs.get("imdgname"));
                    imdgclass = checkNull((String) rs.get("imdgclass"));
                    key = eqptype + " " + "~~" + " " + imdgname;
                    eqpListObj.put(key, imdgclass);
                }
            }
            ///  System.out.println("[ECOM] EComCntrlBean   getIMGClass:::::::::" + eqpListObj);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT getIMGClass==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public String checkContainerForSI(String containterid) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean    checkContainerForSI***" + containterid);
        String str = "Not exists";
        try {
            Map hs = (Map) ecomsivdsservice.getecomcontainer(containterid);
            //
            //  System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
            List lst = (List) hs.get("containerprofile");
            if (lst != null && lst.size() > 0) {
                str = "Exists";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String checkContainerAndEqpType(String containterid, String eqpType, String nor, String oldtype, String pluginEnable) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   containterid=" + containterid + " &eqpType=" + eqpType + " &nor=" + nor + " &oldtype=" + oldtype + " &pluginEnable=" + pluginEnable);
        String str = "Not exists", neweqptype = null, coneqptype = null, mappednor = null, newtype = null;
        Map hs = null;
        List lst = null;
        try {
            if ("Y".equalsIgnoreCase(pluginEnable)) {
                if ("R".equalsIgnoreCase(oldtype)) {
                    hs = (Map) ecomsivdsservice.getecomeqpsizemapping(containterid, eqpType, nor, nor, nor, nor);
                } else {
                    hs = (Map) ecomsivdsservice.getecomeqpsizemapping(containterid, eqpType);
                }
                System.out.println("[ECOM] EComCntrlBean   getecomeqpsizemapping=> " + hs);
                if (hs != null) {
                    lst = (List) hs.get("eqpsizemapping");
                    if (lst != null && lst.size() > 0) {
                        hs = (Map) lst.get(0);
                        str = "Exists~~" + (String) hs.get("shipperownedunit") + "~~" + checkNull((String) hs.get("fleetgroup"));
                        neweqptype = checkNull((String) hs.get("mappedtype"));
                        // mappednor = checkNull((String) hs.get("mappednor"));
                        //get new eqp type
                        if (!eqpType.equalsIgnoreCase(neweqptype)) {
                            hs = (Map) ecomsivdsservice.getEcomTarewtFromEqp(neweqptype);
                            lst = (List) hs.get("ecom_equipments");
                            if (lst != null && lst.size() > 0) {
                                hs = (Map) lst.get(0);
                                newtype = checkNull((String) hs.get("type"));
                            }
                            if (!oldtype.equalsIgnoreCase(newtype)) {
                                if ("R".equalsIgnoreCase(newtype)) {
                                    str = str + "~~" + neweqptype + "~~" + "R" + "~~" + "Y";
                                } else {
                                    str = str + "~~" + neweqptype + "~~" + "N" + "~~" + "N";
                                }

                            } else {
                                str = str + "~~" + neweqptype + "~~" + oldtype + "~~" + nor;
                            }
                        }
                    }
                }

            } else {
                hs = (Map) ecomsivdsservice.checkCntrAndEqptype(containterid, eqpType);
                //
                System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
                lst = (List) hs.get("containerprofile");
                if (lst != null && lst.size() > 0) {
                    hs = (Map) lst.get(0);
                    //coneqptype = (String) hs.get("equipmenttype");
                    str = "Exists~~" + (String) hs.get("shipperownedunit") + "~~" + checkNull((String) hs.get("fleetgroup"));
                }
            }
            System.out.println("[ECOM] EComCntrlBean   str  === >  " + str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public Map getCommodity() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getCommodity*******");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String type = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomcommodity();
            List lst = (List) hs.get("commodity");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("comdtycode"));
                    type = checkNull((String) rs.get("comdtyname"));
                    eqptype = eqptype.toUpperCase();
                    type = type.toUpperCase();
                    eqpListObj.put(eqptype, type);
                }
            }

            //  System.out.println("[ECOM] EComCntrlBean   getEqpments:::::::" + eqpListObj);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT getCommodity==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public Map getHSCode() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getHSCode*******");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String type = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomhscode();
            List lst = (List) hs.get("commodity");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("comdtycode"));
                    type = checkNull((String) rs.get("comdtyname"));
                    eqptype = eqptype.toUpperCase();
                    type = type.toUpperCase();
                    eqpListObj.put(eqptype, type);
                }
            }
            //  "grpcode": "1",
            //  "comdtytype": "23",
            // "comdtyname": "Live animals.",
            // "comdtycode": "0100000000"
            //  System.out.println("[ECOM] EComCntrlBean   getEqpments:::::::" + eqpListObj);
            System.out.println("[ECOM] EComCntrlBean   >>>eqpListObj>>" + eqpListObj.size());
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT getHSCode==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public Map getcommodityrecords() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean    Inside getcommodityrecords ");
        Map eqpListObj = new HashMap();
        Map rs = new HashMap();
        String eqptype = null;
        String type = null;
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomcommodityrecords();
            List lst = (List) hs.get("commodity");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("comdtyname")).trim() + " ~~ " + checkNull((String) rs.get("comdtycode")).trim();
                    type = checkNull((String) rs.get("grpcode")).trim();
                    eqptype = eqptype.toUpperCase();
                    type = type.toUpperCase();
                    eqpListObj.put(eqptype, type);
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean    Exception Caught AT getcommodityrecords==>" + e);
        }
        System.out.println("[ECOM] EComCntrlBean   >>eqpListObj>>>11>>" + eqpListObj);
        return eqpListObj;
    }

    @Override
    public Map getcommoditygroupcodemaps(String openCommodityArr) throws RemoteException {
        // First Part
        Map eqpListObj = new HashMap();
        ArrayList commoditygrouplist = new ArrayList();
        ArrayList commoditygroup = new ArrayList();
        ArrayList allhscodelis = new ArrayList();
        Map commHSCode = new HashMap();
        //
        Map rs = new HashMap();
        String eqptype = null;
        String eqpkey = null;
        String type = null;
        //
        int i = 0;
        try {
            Map hs = (Map) ecomsivdsservice.getecomcommodity();
            List lst = (List) hs.get("commodity");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("comdtycode")).trim();
                    //    type = checkNull((String) rs.get("comdtyname")) + " " + "~~" + " " + checkNull((String) rs.get("comdtycode"));
                    type = checkNull((String) rs.get("comdtyname")).trim() + "000" + checkNull((String) rs.get("comdtycode")).trim();
                    eqptype = eqptype.toUpperCase();
                    type = type.toUpperCase();
                    commoditygrouplist.add(eqptype);
                    eqpListObj.put(eqptype, type);
                }
            }
        } catch (Exception ex) {
            System.out.println("[ECOM] EComCntrlBean   First step*************Exception Caught AT getcommodityrecords==>" + ex);
        }

        //   Second part
        //System.out.println("[ECOM] EComCntrlBean   *****Inside getcommodityrecords*");
        try {
            System.out.println("[ECOM] EComCntrlBean   >>>232432");
            Map hs = (Map) ecomsivdsservice.getecomcommodityrecords();
            List lst = (List) hs.get("commodity");
            if (lst != null && lst.size() > 0) {
                for (i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    //    eqptype = checkNull((String) rs.get("comdtyname")) + " " + "000" + " " + checkNull((String) rs.get("comdtycode"));
                    //   eqptype = checkNull((String) rs.get("comdtyname")) + "000" + checkNull((String) rs.get("comdtycode"));
                    eqptype = checkNull((String) rs.get("comdtyname")).trim() + " ~~ " + checkNull((String) rs.get("comdtycode")).trim();
                    type = checkNull((String) rs.get("grpcode")).trim();
                    eqptype = eqptype.toUpperCase();
                    type = type.toUpperCase();
                    if (commoditygrouplist.contains(type)) {
                        eqpkey = checkNull((String) eqpListObj.get(type));
                        commoditygroup = (ArrayList) commHSCode.get(eqpkey);
                        if (commoditygroup == null) {
                            commoditygroup = new ArrayList();
                            commoditygroup.add(eqptype);
                        } else {
                            commoditygroup.add(eqptype);
                        }
                        allhscodelis.add(eqptype);
                        commHSCode.put(eqpkey, commoditygroup);
                    }
                }
            }
            List<String> openCommodityList = new Gson().fromJson(openCommodityArr, new TypeToken<List<String>>() {
            }.getType());
            if (openCommodityList != null && openCommodityList.size() > 0) {
                String groupstr = null, groupcode = null;

                for (int j = 0; j < openCommodityList.size(); j++) {
                    //   System.out.println("[ECOM] EComCntrlBean   openCommodityList--> " + openCommodityList.get(j));
                    groupcode = openCommodityList.get(j);
                    if (commoditygrouplist.contains(groupcode)) {
                        groupstr = checkNull((String) eqpListObj.get(groupcode));
                        //   System.out.println("[ECOM] EComCntrlBean   groupstr==> " + groupstr);
                        commHSCode.put(groupstr, allhscodelis);
                    }
                }
            }
            System.out.println("[ECOM] EComCntrlBean   >>commHSCode>111>" + commHSCode);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   Second ste*************Exception Caught AT getcommodityrecords==>" + e);
        }
        return commHSCode;
    }

    /*
     @Override
     public String checkEQP(String containterid) throws RemoteException {
     System.out.println("[ECOM] EComCntrlBean   *******CheckUserId***" + containterid);
     String equiptype = "";
     try {
     Map hs = (Map) ecomvdsservice.getecomequiptype(containterid);
     //
     System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
     List lst = (List) hs.get("containerprofile");
     if (lst!=null && lst.size() > 0) {
     Map rs = (Map) lst.get(0);
     str = (String) rs.get("equipmenttype");
     }
     } catch (Exception e) {
     e.printStackTrace();
     }
     return equiptype;
     }
     */
    //
    @Override
    public String getScheuleVersion(String service, String vessel,
            String voyage, String bound,
            String depDate, String svcType,
            String feederCutOffDate) throws RemoteException {
        String result = "2";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        int screenVoy = 0, dbVoy = 0;
        String dbDepDate;
        try {
            if ("M".equalsIgnoreCase(svcType)) {
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                ps = con.prepareStatement(GET_MAILINE_SVC_VERSION);
                ps.setString(1, service);
                ps.setString(2, vessel);
                //ps.setString(3, bound);
                ps.setString(3, svcType);
                rs = ps.executeQuery();
                if (rs.next()) {
                    if ("M".equalsIgnoreCase(svcType)) {
                        screenVoy = Integer.parseInt(voyage);
                        dbVoy = rs.getInt("cutoffvoyage");
                        System.out.println("[ECOM] EComCntrlBean   >>dbVoy>>" + dbVoy);
                        if (screenVoy >= dbVoy) {
                            result = "2";
                        } else {
                            result = "1";
                        }
                    }
                }
            } else {
                dbDepDate = feederCutOffDate;
                String compare = compareTwoDate(depDate, dbDepDate);
                if ("N".equalsIgnoreCase(compare)) {
                    result = "1";
                } else {
                    result = "2";
                }
            }
            System.out.println("[ECOM] EComCntrlBean   >>schedule version>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String compareTwoDate(String date1, String date2) {
        String strConfDefDate = "yyyy-MM-dd HH:mm:ss";
        String result = "N";
        try {
            if (date1 != null && date1.length() > 0 && date2 != null && date2.length() > 0) {
                SimpleDateFormat source = new SimpleDateFormat(strConfDefDate);
                Date d1 = source.parse(date1);
                Date d2 = source.parse(date2);
                System.out.println("[ECOM] EComCntrlBean   >>d1.compareTo(d2)>>>" + d1.compareTo(d2));
                if (d1.compareTo(d2) >= 0) {
                    result = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<FieldConfigVO> getFieldNameConfigList() throws RemoteException {
        List<FieldConfigVO> fieldNameList = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        FieldConfigVO configVO = null;
        try {
            fieldNameList = new ArrayList<FieldConfigVO>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_FIELDNAME_LIST);
            rs = ps.executeQuery();
            while (rs.next()) {
                configVO = new FieldConfigVO();
                configVO.setFieldCode(checkNull(rs.getString("fieldcode")));
                configVO.setFieldName(checkNull(rs.getString("fieldname")));
                configVO.setShipmentType(checkNull(rs.getString("shipmenttype")));
                fieldNameList.add(configVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fieldNameList;
    }

    @Override
    public List<FieldConfigVO> getFieldConfigDetailsList() throws RemoteException {
        List<FieldConfigVO> fieldConfigList = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        FieldConfigVO configVO = null;
        try {
            fieldConfigList = new ArrayList<FieldConfigVO>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_FIELDCONFIG_DETAILS);
            rs = ps.executeQuery();
            while (rs.next()) {
                configVO = new FieldConfigVO();
                configVO.setFieldCode(checkNull(rs.getString("fieldcode")));
                configVO.setSearchType(checkNull(rs.getString("searchtype")));
                configVO.setSearchCode(checkNull(rs.getString("searchvalue")));
                configVO.setShipmentType(checkNull(rs.getString("shipmenttype")));
                configVO.setInputType(checkNull(rs.getString("inputtype")));
                configVO.setStatus(checkNull(rs.getString("status")));
                configVO.setId(checkNull(rs.getString("id")));
                fieldConfigList.add(configVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fieldConfigList;
    }

    @Override
    public int saveFieldConfigurationDet(FieldConfigVO configVO) throws RemoteException {
        int count = 0;
        PreparedStatement ps = null;
        Connection con = null;
        String saveType = null;
        try {
            if (configVO != null) {
                saveType = configVO.getSaveType();
                System.out.println("[ECOM] EComCntrlBean   >>saveType>>" + saveType);
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                if ("save".equalsIgnoreCase(saveType)) {
                    ps = con.prepareStatement(SAVE_FIELD_CONFIG);
                    ps.setString(1, checkNull(configVO.getFieldCode()).trim());
                    ps.setString(2, checkNull(configVO.getSearchType()).trim());
                    ps.setString(3, checkNull(configVO.getSearchValue()).trim());
                    ps.setString(4, checkNull(configVO.getShipmentType()).trim());
                    ps.setString(5, checkNull(configVO.getInputType()).trim());
                    ps.setString(6, checkNull(configVO.getStatus()).trim());
                } else {
                    ps = con.prepareStatement(UPDATE_FIELD_CONFIG);
                    ps.setString(1, checkNull(configVO.getSearchType()).trim());
                    ps.setString(2, checkNull(configVO.getSearchValue()).trim());
                    ps.setString(3, checkNull(configVO.getShipmentType()).trim());
                    ps.setString(4, checkNull(configVO.getInputType()).trim());
                    ps.setString(5, checkNull(configVO.getStatus()).trim());
                    ps.setString(6, checkNull(configVO.getId()).trim());
                }
                count = ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public String fieldConfigDuplication(FieldConfigVO configVO) throws RemoteException {
        String result = "No";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String inputType = null, saveType = null;
        try {
            inputType = configVO.getInputType();
            saveType = configVO.getSaveType();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if ("edit".equalsIgnoreCase(saveType)) {
                ps = con.prepareStatement(CHECK_FIELD_CONFIG);
                ps.setString(1, configVO.getId());
                ps.setString(2, configVO.getFieldCode());
                ps.setString(3, configVO.getSearchType());
                ps.setString(4, configVO.getSearchValue());
            } else {
                ps = con.prepareStatement(CHECK_FIELD_CONFIG2);
                ps.setString(1, configVO.getFieldCode());
                ps.setString(2, configVO.getSearchType());
                ps.setString(3, configVO.getSearchValue());
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Yes";
            }
            System.out.println("[ECOM] EComCntrlBean   >>result>for fiedld checking>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String deleteFieldConfg(String id) throws RemoteException {
        String result = "No";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETE_FIELD_CONFIG);
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String getFieldConfg(String countryId, String port, String fieldCode, String shipmentType) throws RemoteException {
        String result = "No";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String value = "";
        boolean check = true;
        try {
            System.out.println("[ECOM] EComCntrlBean    >>>port>> " + port + " >>>fieldCode>> " + fieldCode);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETFIELD_CONFIG);
            ps.setString(1, port);
            ps.setString(2, fieldCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                value = checkNull(rs.getString("inputtype")).trim();
                check = false;
            }
            if (check) {
                ps = con.prepareStatement(GETFIELD_CONFIG);
                ps.setString(1, countryId);
                ps.setString(2, fieldCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    value = checkNull(rs.getString("inputtype")).trim();
                }
            }
            System.out.println("[ECOM] EComCntrlBean   >>value>>" + value);
            if ("M".equalsIgnoreCase(value) || "E".equalsIgnoreCase(value)) {
                result = "Yes";
            }
            System.out.println("[ECOM] EComCntrlBean   >>mandatory result>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ReturnVO getRateRequestMailDets(String pol) throws RemoteException {
        ReturnVO rvo = new ReturnVO();
        String subject = "", agencyCode = null;
        String email = "";
        int i = 0;

        try {
            Map hs = (Map) ecomGenService.getEcomAgencyLocation(pol);
            //////
            List lst = (List) hs.get("agencylocations");

            if (lst != null && lst.size() > 0) {
                Map rsMap = (Map) lst.get(0);
                agencyCode = (String) rsMap.get("agencycode");
            }
            System.out.println("[ECOM] EComCntrlBean   <>>agencyCode>>rr>>" + agencyCode);
            if (agencyCode != null && agencyCode.length() > 0) {
                MailFormatVO mailFormatObj = new MailFormatVO();
                mailFormatObj = getMailContent("RateRequest", "ratereq", "C");
                subject = mailFormatObj.getStrSubject();
                rvo.setStrSubject(subject);
                hs = (Map) ecomGenService.getEcomRateRequestAgencyMail(agencyCode);
                System.out.println("[ECOM] EComCntrlBean   >>hs..." + hs);
                //////
                lst = (List) hs.get("agencycontacts");
                if (lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        Map bkg = (Map) lst.get(j);
                        String eml = (String) bkg.get("contactemailid");
                        if (i == 0) {
                            email = eml;
                        } else {
                            email = email + "," + eml;
                        }
                        i++;
                    }
                }
            }
            rvo.setStrToMailId(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvo;
    }

    @Override
    public List<RatesContractsVO> getAvailableRatesContract(String custCode, String origin, String delivery, String eqpType, String commodity, String shipmentDate) throws RemoteException {
        List<RatesContractsVO> contractList = null, bblclContractList = null, resultList = null;
        Map resultMap = null, resultMap1 = null, map = null;
        String pol = "", pod = "", rateType = "";
        SvmJsonList alist = null;
        Gson gson = new Gson();

        try {
            pol = origin;
            pod = delivery;
            rateType = "R";
            commodity = checkNull(commodity);
            eqpType = checkNull(eqpType);
            System.out.println("[ECOM] EComCntrlBean   >>eqpType>>" + eqpType);
            RoutingVo hs = null;
            /* String defRoute = "Y";
            hs = (RoutingVo) ecomGenService.getecomroutinginformation(origin, delivery, defRoute, shipmentDate, shipmentDate);
            List lst = hs.getRoutedetail();
            if (lst != null && lst.size() > 0) {
                Routedetail rst = (Routedetail) lst.get(0);
                pol = rst.getBlpol();
                pod = rst.getBlpod();

            }*/
            alist = new SvmJsonList();
            alist.add(pol);
            alist.add(pod);
            alist.add(eqpType);
            alist.add(commodity);
            alist.add(shipmentDate);
            alist.add(custCode);
            alist.add(rateType);
            if (commodity.length() > 0 && eqpType.length() > 0) {
                resultMap = ecomGenService.getEcomRateContractEqpComdty(alist);
                resultMap1 = ecomGenService.getEcomRateContractEqpComdtyBBLCL(custCode, pol, pod, eqpType, commodity, shipmentDate, shipmentDate);
            } else if (commodity.length() > 0) {
                resultMap = ecomGenService.getEcomRateContractEqpComdty(alist);
                resultMap1 = ecomGenService.getEcomRateContractComdtyBBLCL(custCode, pol, pod, commodity, shipmentDate, shipmentDate);
            } else if (eqpType.length() > 0) {
                resultMap = ecomGenService.getEcomRateContractEqpComdty(alist);
                resultMap1 = ecomGenService.getEcomRateContractEqpBBLCL(custCode, pol, pod, eqpType, shipmentDate, shipmentDate);
            } else if (pol != null && pod != null && pol.length() > 0 && pod.length() > 0) {
                resultMap = ecomGenService.getEcomRateContractEqpComdty(alist);
                resultMap1 = ecomGenService.getEcomRateContractBBLCL(custCode, pol, pod, shipmentDate, shipmentDate);
            } else {
                resultMap = ecomGenService.getEcomRateContractEqpComdty(alist);
                resultMap1 = ecomGenService.getEcomRateContractBBLCLCust(custCode, shipmentDate, shipmentDate);
            }
            System.out.println("[ECOM] EComCntrlBean   >>.result tariff map>>" + resultMap);
//            if (resultMap != null) {
//                contractList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("uvs_ecombookingrates")), new TypeToken<List<RatesContractsVO>>() {
//                }.getType());
//            }

            if (resultMap != null) {
                map = (Map) resultMap.get("rs");
                if (map != null) {
                    contractList = gson.fromJson(gson.toJson((ArrayList) map.get("rs0")), new TypeToken<List<RatesContractsVO>>() {
                    }.getType());

                }
            }
            if (resultMap1 != null) {
                bblclContractList = gson.fromJson(gson.toJson((ArrayList) resultMap1.get("bookingrates_roro")), new TypeToken<List<RatesContractsVO>>() {
                }.getType());
            }
            resultList = Stream.concat(contractList.stream(), bblclContractList.stream()).collect(Collectors.toList());

            resultList.forEach(rsl -> {
                rsl.setOriginPort(origin);
                rsl.setDeliveryPort(delivery);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;

    }

    @Override
    public List<RateContractSurchargeVO> getRateContractSurchargeDets(String rateNo, String itemNo, String rateType) throws RemoteException {
        List<RateContractSurchargeVO> contractList = null, bblclContractList = null, resultList = null;
        Map resultMap = null, resultMap1 = null;
        Gson gson = new Gson();
        try {
            resultMap = ecomGenService.getEcomContractSurcharge(rateNo, itemNo);
            resultMap1 = ecomGenService.getEcomContractSurchargeBBLCL(rateNo, itemNo);
            System.out.println("[ECOM] EComCntrlBean   >>resultMap>>" + resultMap);
            if (resultMap != null) {
                contractList = gson.fromJson(gson.toJson((ArrayList) resultMap.get("ratesurcharge")), new TypeToken<List<RateContractSurchargeVO>>() {
                }.getType());
            }
            if (resultMap1 != null) {
                bblclContractList = gson.fromJson(gson.toJson((ArrayList) resultMap1.get("bblclratesurcharge")), new TypeToken<List<RateContractSurchargeVO>>() {
                }.getType());
            }
            resultList = Stream.concat(contractList.stream(), bblclContractList.stream()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public Map<String, String> getDashboardCount(String custCode, String v1v2DataEnableStatus) throws RemoteException {
        List<net.ecom.vds.serviceVo.DashboardVO> list = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        Gson gson = new Gson();
        String topDesStr = null, topPortPairStr = null, topDestTeu = null;
        Map vdsMap;
        net.ecom.vds.serviceVo.DashboardVO dashVO = null;
        try {
            custCode = checkNull(custCode);
            list = new ArrayList<>();
            vdsMap = dashBoardService.getEcomTopDestinationDashboard(custCode);
            if (vdsMap != null) {
                list = gson.fromJson(gson.toJson((ArrayList) vdsMap.get("bl")), new TypeToken<List<net.ecom.vds.serviceVo.DashboardVO>>() {
                }.getType());
                list.forEach(vo -> {
                    vo.setColor("#67B7DC");
                });
                if ("Y".equalsIgnoreCase(v1v2DataEnableStatus)) {
                    list = v1DashboardBeanObj.getV1EcomTopDestinationDashboard(list, custCode);
                }
                list = dashboardCountCompare(list);
                if (list == null || list.size() == 0) {
                    dashVO = new net.ecom.vds.serviceVo.DashboardVO();
                    dashVO.setCountVal("");
                    dashVO.setFinalPortId("");
                    dashVO.setPortPair("");
                    dashVO.setOriginPortId("");
                    list = new ArrayList<>();
                    list.add(dashVO);
                }
                topDesStr = gson.toJson(list);
                System.out.println("[ECOM] EComCntrlBean   >>topDesStr>>" + topDesStr);
            }
            resultMap.put("TOPDESTINATIONCOUNT", topDesStr);
            vdsMap = dashBoardService.getEcomTopPortPairDashboard(custCode);
            if (vdsMap != null) {
                list = gson.fromJson(gson.toJson((ArrayList) vdsMap.get("bl")), new TypeToken<List<net.ecom.vds.serviceVo.DashboardVO>>() {
                }.getType());
                list.forEach(vo -> {
                    vo.setFinalPortId(vo.getOriginPortId() + " - " + vo.getFinalPortId());
                    vo.setColor("#f2f553");
                });
                if ("Y".equalsIgnoreCase(v1v2DataEnableStatus)) {
                    list = v1DashboardBeanObj.getV1EcomTopPortPairDashboard(list, custCode);
                }
                list = dashboardCountCompare(list);
                if (list == null || list.size() == 0) {
                    dashVO = new net.ecom.vds.serviceVo.DashboardVO();
                    dashVO.setCountVal("");
                    dashVO.setFinalPortId("");
                    dashVO.setPortPair("");
                    dashVO.setOriginPortId("");
                    list = new ArrayList<>();
                    list.add(dashVO);
                }
                topPortPairStr = gson.toJson(list);
            }
            resultMap.put("TOPPORTPAIRCOUNT", topPortPairStr);
            vdsMap = dashBoardService.getEcomTopDestinationTeuDashboard(custCode);
            if (vdsMap != null) {
                list = gson.fromJson(gson.toJson((ArrayList) vdsMap.get("bl")), new TypeToken<List<net.ecom.vds.serviceVo.DashboardVO>>() {
                }.getType());
                list.forEach(vo -> {
                    vo.setColor("#84B761");
                });
                if ("Y".equalsIgnoreCase(v1v2DataEnableStatus)) {
                    list = v1DashboardBeanObj.getV1EcomTopDestinationTeuDashboard(list, custCode);
                }
                list = dashboardCountCompare(list);
                if (list == null || list.size() == 0) {
                    dashVO = new net.ecom.vds.serviceVo.DashboardVO();
                    dashVO.setCountVal("");
                    dashVO.setFinalPortId("");
                    dashVO.setPortPair("");
                    dashVO.setOriginPortId("");
                    list = new ArrayList<>();
                    list.add(dashVO);
                }
                topDestTeu = gson.toJson(list);
            }
            resultMap.put("TOPDESTINATIONTEUCOUNT", topDestTeu);
            System.out.println("[ECOM] EComCntrlBean   >>resultMapdashboard count>>" + resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private List<net.ecom.vds.serviceVo.DashboardVO> dashboardCountCompare(List<net.ecom.vds.serviceVo.DashboardVO> list) {
        try {
            net.ecom.vds.serviceVo.DashboardVO vo1 = null, vo2 = null;
            if (list != null) {
                for (int i = 0; i < list.size() - 1; i++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        vo1 = list.get(i);
                        vo2 = list.get(j);
                        if (checkNull(vo1.getFinalPortId()).trim().equalsIgnoreCase(checkNull(vo2.getFinalPortId()).trim())) {
                            int strDt1 = Integer.parseInt(vo1.getCountVal());
                            int strDt2 = Integer.parseInt(vo2.getCountVal());
                            strDt2 = strDt1 + strDt2;
                            vo1.setCountVal(String.valueOf(strDt2));
                            list.set(i, vo1);
                            list.remove(j);
                        }
                    }

                }
                DashboardComparator objcmp = new DashboardComparator();
                Collections.sort(list, objcmp);
                if (list.size() > 5) {
                    for (int i = list.size() - 1; i >= 5; i--) {
                        list.remove(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return list;
    }

    @Override
    public List<V1V2ServiceConfigVO> getV1V2ServiceConfigDetList() throws RemoteException {
        List<V1V2ServiceConfigVO> v1v2SerConfigList = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        V1V2ServiceConfigVO serviceVO = null;
        try {
            v1v2SerConfigList = new ArrayList<V1V2ServiceConfigVO>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETV1V2SERVICEDET);
            rs = ps.executeQuery();
            while (rs.next()) {
                serviceVO = new V1V2ServiceConfigVO();
                serviceVO.setId(checkNull(rs.getString("id")));
                serviceVO.setService(checkNull(rs.getString("service")));
                serviceVO.setVessel(checkNull(rs.getString("vessel")));
                serviceVO.setCutOffVoyage(checkNull(rs.getString("cutoffvoyage")));
                serviceVO.setBound(checkNull(rs.getString("bound")));
                serviceVO.setServiceType(checkNull(rs.getString("svctype")));
                serviceVO.setCutOffDate(checkNull(rs.getString("cutoffdate")));
                v1v2SerConfigList.add(serviceVO);
            }
            System.out.println("[ECOM] EComCntrlBean   >>v1v2SerConfigList>>" + v1v2SerConfigList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return v1v2SerConfigList;
    }

    @Override
    public String saveServiceConfigDetList(V1V2ServiceConfigVO serviceVO) throws RemoteException {
        String result = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   >>>serviceVO.getCutOffDate()>>" + serviceVO.getCutOffDate());
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(SAVEV1V2SERVICECONFIG);
            ps.setString(1, checkNull(serviceVO.getService()));
            ps.setString(2, checkNull(serviceVO.getVessel()));
            ps.setString(3, checkNull(serviceVO.getCutOffVoyage()));
            ps.setString(4, checkNull(serviceVO.getBound()));
            ps.setString(5, checkNull(serviceVO.getServiceType()));
            ps.setString(6, checkNull(serviceVO.getCutOffDate()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String updateServiceConfigDetList(V1V2ServiceConfigVO serviceVO) throws RemoteException {
        String result = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   >>>serviceVO.getCutOffDate()>>" + serviceVO.getCutOffDate());
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATEV1V2SERVICECONFIG);
            ps.setString(1, checkNull(serviceVO.getService()));
            ps.setString(2, checkNull(serviceVO.getVessel()));
            ps.setString(3, checkNull(serviceVO.getCutOffVoyage()));
            ps.setString(4, checkNull(serviceVO.getBound()));
            ps.setString(5, checkNull(serviceVO.getServiceType()));
            ps.setString(6, checkNull(serviceVO.getCutOffDate()));
            ps.setString(7, checkNull(serviceVO.getId()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String deleteServiceConfigDetList(String id) throws RemoteException {
        String result = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   >>>id()>>" + id);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETEV1V2SERVICECONFIG);
            ps.setString(1, checkNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getEcomMailAcitvityList() throws RemoteException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        Map<String, List<String>> map = null;
        List<String> list = null;
        String prev = null, next = null;
        int i = 0;
        boolean check = false;
        try {
            map = new HashMap<String, List<String>>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOMMAILACTIVITY);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (i == 0) {
                    i++;
                    list = new ArrayList<>();
                    prev = rs.getString("module");
                }
                next = rs.getString("module");
                // System.out.println("[ECOM] EComCntrlBean   >>next>>>" + next);
                check = prev.equalsIgnoreCase(next);
                if (!check) {
                    map.put(prev, list);
                    list = new ArrayList<>();
                }
                list.add(rs.getString("activitycode"));
                prev = next;
            }
            map.put(next, list);
            //  System.out.println("[ECOM] EComCntrlBean   >>mail alret activity>>" + map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public String getCorporteAgency(String agencyCode) throws RemoteException {
        String result = "";
        List corpList = null;
        try {
            Map corpAgency = ecomvdsservice.getEcomCorporateAgency(agencyCode);
            System.out.println("[ECOM] EComCntrlBean   >>corpAgency>>" + corpAgency);
            if (corpAgency != null && corpAgency.size() > 0) {
                corpList = (List) corpAgency.get("agencymatrix");
                if (corpList != null && corpList.size() > 0) {
                    corpAgency = (Map) corpList.get(0);
                    result = checkNull((String) corpAgency.get("corpagency"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //
    @Override
    public List<ServiceRestrictionVO> getSvrcRestrictDetailsList() throws RemoteException {
        List<ServiceRestrictionVO> srvcRestrictList = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        ServiceRestrictionVO configVO = null;
        try {
            srvcRestrictList = new ArrayList<ServiceRestrictionVO>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_SVRC_RESTRICT_LIST);
            rs = ps.executeQuery();
            while (rs.next()) {
                configVO = new ServiceRestrictionVO();
                configVO.setPla(checkNull(rs.getString("pla")));
                configVO.setPld(checkNull(rs.getString("pld")));
                configVO.setService(checkNull(rs.getString("service")));
                configVO.setId(checkNull(rs.getString("id")));
                configVO.setStatus(checkNull(rs.getString("status")));
                srvcRestrictList.add(configVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return srvcRestrictList;
    }

    @Override
    public String saveSvrcRestriction(ServiceRestrictionVO vo) throws RemoteException {
        String result = "success";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String saveType = vo.getSaveType();
        try {
            System.out.println("[ECOM] EComCntrlBean   >>saveType>>" + saveType);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if ("save".equalsIgnoreCase(saveType)) {
                ps = con.prepareStatement(SAVE_SVRC_DET);
                ps.setString(1, vo.getPla());
                ps.setString(2, vo.getPld());
                ps.setString(3, vo.getService());
                ps.setString(4, vo.getStatus());
            } else {
                ps = con.prepareStatement(UPDATE_SVRC_DET);
                ps.setString(1, vo.getPla());
                ps.setString(2, vo.getPld());
                ps.setString(3, vo.getService());
                ps.setString(4, vo.getStatus());
                ps.setString(5, vo.getId());
            }
            ps.executeUpdate();
        } catch (Exception e) {
            result = "fail";
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String deleteSvrcRestrict(String id) throws RemoteException {
        String result = "success";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(DELETE_SVRC_DET);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            result = "fail";
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String checkSvcRestrict(String id, String pla, String pld, String service) throws RemoteException {
        String result = "Not Exist";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECK_SVRC_RESTRICT);
            ps.setString(1, pla);
            ps.setString(2, pld);
            ps.setString(3, service);
            ps.setString(4, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Exist";
            }
        } catch (Exception e) {
            result = "fail";
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getSvrcRestrictCacheList() throws RemoteException {
        Map<String, List<String>> map = new HashMap<>();
        List<String> srvcRestrictList = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        int i = 0;
        boolean check = false;
        String prevPla = null, nextPla = null, prevPld = null, nextPld = null, portPair = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_SVRC_CACHE_LIST);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (i == 0) {
                    i++;
                    srvcRestrictList = new ArrayList<>();
                    prevPla = checkNull(rs.getString("pla")).trim();
                    prevPld = checkNull(rs.getString("pld")).trim();
                }
                nextPla = checkNull(rs.getString("pla")).trim();
                nextPld = checkNull(rs.getString("pld")).trim();
                check = prevPla.equalsIgnoreCase(nextPla) && prevPld.equalsIgnoreCase(nextPld);
                portPair = prevPla + "#" + prevPld;
                if (!check) {
                    map.put(portPair, srvcRestrictList);
                    srvcRestrictList = new ArrayList<>();
                }
                prevPla = nextPla;
                prevPld = nextPld;
                portPair = prevPla + "#" + prevPld;
                srvcRestrictList.add(checkNull(rs.getString("service")).trim().toUpperCase());
            }
            map.put(portPair, srvcRestrictList);
            System.out.println("[ECOM] EComCntrlBean   >>service restriction list>>" + map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public HashMap getEcomMailConfig(String profileid, String carriercode) throws RemoteException {
        Map map = null;
        List list = null;
        HashMap hsobj = null;
        try {
            hsobj = new HashMap();
            map = (Map) ecomvdsservice.getEcomMailConfig(profileid, carriercode);
            list = (List) map.get("emailprofileconfig");
            if (list != null && list.size() > 0) {
                hsobj.put("SMTP_HOST", map.get("smtphost"));
                hsobj.put("MAIL_ID", map.get("authuser"));
                hsobj.put("PASSWORD", map.get("authpassword"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hsobj;
    }

    @Override
    public Map<String, List> getLoginImagAdvisoryDets() throws RemoteException {
        Map<String, List> map = new HashMap<String, List>();
        CustomerAdvisoryVo vo = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        List list = null;
        String imagePath = null, description = null;
        try {
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            list = new ArrayList();
            pst = conn.prepareStatement(GET_BG_IMAGE);
            rst = pst.executeQuery();
            while (rst.next()) {
                imagePath = checkNull(rst.getString("imagepath"));
                if (imagePath.length() > 0) {
                    list.add(imagePath);
                }
            }
            map.put("BGIMG", list);
            list = new ArrayList<String>();
            pst = conn.prepareStatement(GET_ADVISORY_MSG);
            rst = pst.executeQuery();
            while (rst.next()) {
                vo = new CustomerAdvisoryVo();
                description = checkNull(rst.getString("description"));
                if (description.length() > 0) {
                    vo.setCreated_date(checkNull(rst.getString("createddate")));
                    vo.setDescription(description);
                    vo.setAdvisoryname(checkNull(rst.getString("advisoryname")));
                    list.add(vo);
                }
            }
            map.put("ADVISORYMSG", list);
            System.out.println("[ECOM] EComCntrlBean   >bg and advisery>map>>" + map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public String checkBLBookingNo(String refNo) throws RemoteException {
        String result = null;
        List refNoList = null;
        try {
            Map refMap = vgmService.getecombkgnofrombl(refNo);
            System.out.println("[ECOM] EComCntrlBean   >>refMap>>" + refMap);
            if (refMap != null && refMap.size() > 0) {
                refNoList = (List) refMap.get("bl");
                if (refNoList != null && refNoList.size() > 0) {
                    refMap = (Map) refNoList.get(0);
                    result = checkNull((String) refMap.get("book_no"));
                }
            }
            if (result != null && result.length() > 0) {
                result = "bl";
            } else {
                result = "container";
            }
            System.out.println("[ECOM] EComCntrlBean   result>refNoCheck" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getCustomerSpecialCustomerGroup(String custcode) throws RemoteException {
        Map map = null;
        List list = null;
        String splcustgroup = null;
        try {
            map = (Map) ecomvdsservice.getEcomSpecialCustomerGroup(custcode);
            list = (List) map.get("customermaster");
            if (list != null && list.size() > 0) {
                map = (Map) list.get(0);
                splcustgroup = (String) map.get("specialcustomergroup");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return splcustgroup;
    }

    @Override
    public Map getLrpPluginDetails(String BKG_Pluginid, String EBK_Pluginid) throws RemoteException {
        Map pluginconfig = null;
        List list = null, pluginlis = null;
        Map Result = null;
        try {
            Result = new HashMap();
            pluginconfig = ecomvdsservice.getEcomBookingPluginConfig(BKG_Pluginid);
            list = (List) pluginconfig.get("pluginconfiguration");
            if (list != null && list.size() > 0) {
                pluginlis = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    pluginconfig = (Map) list.get(i);
                    pluginlis.add(pluginconfig.get("pluginid"));
                }
            }
            Result.put("BKG", pluginlis);
            pluginlis = null;
            pluginconfig = ecomvdsservice.getEcomEBKPluginConfig(EBK_Pluginid);
            list = (List) pluginconfig.get("pluginconfiguration");
            if (list != null && list.size() > 0) {
                pluginlis = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    pluginconfig = (Map) list.get(i);
                    pluginlis.add(pluginconfig.get("pluginid"));
                }
            }
            Result.put("EBK", pluginlis);
            pluginlis = null;
            final String moduleid = "ECC";
            final String pluginid = "ECA";
            pluginconfig = ecomvdsservice.getecompluginconfiginfo(moduleid, pluginid);
            list = (List) pluginconfig.get("pluginconfiguration");
            if (list != null && list.size() > 0) {
                pluginlis = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    pluginconfig = (Map) list.get(i);
                    pluginlis.add(pluginconfig.get("pluginid"));
                }
            }
            Result.put("ECC", pluginlis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   getLrpPluginDetails==> " + Result);
        return Result;
    }

    @Override
    public HashMap getRoutingCutOffDetailsFromPort(String port) throws RemoteException {
        CutoffConfigVO cutOffVO = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, CutoffConfigVO> cutoffdatemap = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            cutoffdatemap = new HashMap<>();
            ps = con.prepareStatement(GETCUTOFFFORROUTINGFROMPORT);
            System.out.println("[ECOM] EComCntrlBean   port=========//>" + port);
            ps.setString(1, port);
            rs = ps.executeQuery();
            while (rs.next()) {
                cutOffVO = setCutoffConfiVo(rs);
                cutoffdatemap.put(cutOffVO.getPort() + "&&" + cutOffVO.getTerminal(), cutOffVO);
            }
            System.out.println("[ECOM] EComCntrlBean   cutoffdatemap" + cutoffdatemap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cutoffdatemap;
    }

    @Override
    public String getEcomBKGFileUploadPath() throws RemoteException {
        Map pluginconfig = null;
        List list = null;
        String path = null;
        try {
            pluginconfig = ecomvdsservice.getEcomBkgFileUploadPath();
            list = (List) pluginconfig.get("emssetting");
            if (list != null && list.size() > 0) {
                pluginconfig = (Map) list.get(0);
                path = (String) pluginconfig.get("path");
                if (path != null && path.trim().length() > 0) {
                    path = path + File.separator + "Ecom" + File.separator;
                    File directory = new File(path);
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                }
            }
            //path = "E:\\BL_DOCS" + File.separator + "Ecom" + File.separator;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   getEcomBKGFileUploadPath==> " + path);
        return path;
    }

    @Override
    public List getCustType() throws RemoteException {
        Map pluginconfig = null;
        List list, custtypelist = null;
        try {
            pluginconfig = ecomGenService.getCustType();
            list = (List) pluginconfig.get("customertype");
            if (list != null && list.size() > 0) {
                custtypelist = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    pluginconfig = (Map) list.get(i);
                    custtypelist.add(pluginconfig.get("custtype"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   getcusttype " + custtypelist);
        return custtypelist;
    }

    @Override
    public List getRefidForAddDet() throws RemoteException {
        Map pluginconfig = null;
        List list, refidlist = null;
        try {
            pluginconfig = ecomGenService.getRefidForAddDet();
            list = (List) pluginconfig.get("reference");
            if (list != null && list.size() > 0) {
                refidlist = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    pluginconfig = (Map) list.get(i);
                    refidlist.add(pluginconfig.get("refid"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   getrefid " + refidlist);
        return refidlist;
    }

    @Override
    public String getDefaultCarriageContract() throws RemoteException {
        Map hs = null;
        List lst = null;
        String result = null;
        try {
            hs = (Map) ecomvdsservice.getEcomBkgConfigDetails("DTS");
            lst = (List) hs.get("bkgconfig");
            if (lst != null && lst.size() > 0) {
                Map rs1 = (Map) lst.get(0);
                result = (String) rs1.get("value");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   getDefaultCarriageContract" + result);
        return result;
    }

    @Override
    public String getTrackingBKGDepartureDate(String book_no) throws RemoteException {
        String result = null;
        Map hs = null;
        List lst = null;
        try {
            hs = (Map) ecomvdsservice.getecombookingdetails(book_no);
            System.out.println("[ECOM] EComCntrlBean   getTrackingBKGDepartureDate" + hs);
            //
            lst = (List) hs.get("booking");

            if (lst != null && lst.size() > 0) {
                hs = (Map) lst.get(0);
                result = checkNull((String) hs.get("polat"));
                //result = lst.entrySet().stream().filter(x -> "1".equalsIgnoreCase(x.getKey())).map(x -> x.getValue()).reduce("", String::concat);
            }
            System.out.println("[ECOM] EComCntrlBean   getTrackingBKGDepartureDate Result:::::::::::: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map getMessagesBasedOnLang() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   Inside getMessagesBasedOnLang");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null, rs1 = null;
        Map result = null, langmap = null;
        String lang = null;
        try {
            result = new HashMap();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(langquery, 1004, 1007);
            rs = ps.executeQuery();
            while (rs.next()) {
                langmap = new HashMap();
                lang = checkNull(rs.getString("langcode")).toLowerCase();
                ps = con.prepareStatement(GET_MSG_DETAILS, 1004, 1007);
                ps.setString(1, lang);
                rs1 = ps.executeQuery();
                while (rs1.next()) {
                    langmap.put(checkNull(rs1.getString("module")).toLowerCase() + "-" + checkNull(rs1.getString("criteria")).toLowerCase(), checkNull(rs1.getString("message")));
                }
                result.put(lang, langmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            System.out.println("[ECOM] EComCntrlBean   result ===> " + result);
            return result;
        }
    }

    @Override
    public Map<String, String> getEcomLangSelection() throws RemoteException {
        LinkedHashMap<String, String> map = null;

        Map hs = (Map) ecomGenService.getEcomLangSelection();
        List lst = (List) hs.get("ecom_lang");
        map = new LinkedHashMap<String, String>();
        if (lst.size() > 0) {
            for (int j = 0; j < lst.size(); j++) {
                Map rs = (Map) lst.get(j);
                map.put((String) rs.get("langcode"), (String) rs.get("langname"));

            }
        }
        return map;
    }

    @Override
    public Map<String, List<HotlineContactsVO>> getHotlineContacts() throws RemoteException {
        TreeMap<String, List<HotlineContactsVO>> map = null;
        HotlineContactsVO vo = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        List list = null;
        String countryCode = null;

        try {
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            map = new TreeMap<String, List<HotlineContactsVO>>();
            pst = conn.prepareStatement(GET_HOTLINE_CONTACTS);
            rst = pst.executeQuery();
            while (rst.next()) {
                vo = new HotlineContactsVO();
                countryCode = checkNull(rst.getString("CountryName"));
                if (map.containsKey(countryCode)) {
                    list = map.get(countryCode);
                } else {
                    list = new ArrayList();
                }
                vo.setCountryName(rst.getString("CountryName"));
                vo.setPortname(checkNull(rst.getString("portname")));
                vo.setContactName(checkNull(rst.getString("ContactName")));
                vo.setPhone(checkNull(rst.getString("phone")));
                vo.setEmail(checkNull(rst.getString("email")));
                list.add(vo);
                map.put(countryCode, list);
            }

            System.out.println("[ECOM] EComCntrlBean   valuehelplinecontactsmap" + map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public Map<String, String> getVesselDetailsRestriction() throws RemoteException {

        ArrayList vessellst;
        Map<String, String> vslmap = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecomvesseldetails();
            vslmap = new HashMap();
            vessellst = gson.fromJson(gson.toJson((ArrayList) hs.get("vesselmaster")), new TypeToken<List<VesselMaster>>() {
            }.getType());
            VesselMaster rs = null;
            for (int i = 0; i < vessellst.size(); i++) {
                rs = (VesselMaster) vessellst.get(i);
                vslmap.put(rs.getVesselcode(), rs.getVessel_info());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vslmap;
    }

    @Override
    public HashMap getStateCache() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getStateCache*******");
        HashMap StateObj = new HashMap();
        HashMap stateinnermap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement("select statecode,statedesc,countrycode from Statesmaster where status='A'", 1004, 1007);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (StateObj.containsKey(checkNull(rs.getString("countrycode")).toUpperCase())) {
                    stateinnermap = (HashMap) StateObj.get(checkNull(rs.getString("countrycode")).toUpperCase());
                    stateinnermap.put(checkNull(rs.getString("statecode").toUpperCase()), checkNull(rs.getString("statedesc").toUpperCase()));
                    StateObj.put(checkNull(rs.getString("countrycode")), stateinnermap);
                } else {
                    stateinnermap = new HashMap();
                    stateinnermap.put(checkNull(rs.getString("statecode").toUpperCase()), checkNull(rs.getString("statedesc").toUpperCase()));
                    StateObj.put(checkNull(rs.getString("countrycode").toUpperCase()), stateinnermap);
                }

            }
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   Exception Caught AT getStateCache==>" + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StateObj;
    }

    @Override
    public Map<String, String> getVersionUTC() throws RemoteException {
        LinkedHashMap<String, String> map = null;

        Map hs = (Map) ecomGenService.getVersionUTC();
        List lst = (List) hs.get("systemset");
        map = new LinkedHashMap<String, String>();
        if (lst.size() > 0) {
            for (int j = 0; j < lst.size(); j++) {
                Map rs = (Map) lst.get(j);
                map.put("UTC", (String) rs.get("value"));

            }
        }
        System.out.println("[ECOM] EComCntrlBean   versionutc value" + map);
        return map;

    }

    @Override
    public String checkPluginFEU(String moduleid, String pluginid) {
        String isEnabled = "NO";

        try {
            Map plgnmap = ecomGenService.getecompluginconfiginfo(moduleid, pluginid);

            List list = (List) plgnmap.get("pluginconfiguration");
            if (list != null && list.size() > 0) {
                isEnabled = "YES";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   checkPluginFEU==> pluginid=" + pluginid + " isEnabled=" + isEnabled);
        return isEnabled;
    }

    @Override
    public Map<String, String> getEcomTimeZoneSelection() throws RemoteException {
        LinkedHashMap<String, String> map = null;

        Map hs = (Map) ecomGenService.getTimeZoneValues();
        List lst = (List) hs.get("usertimezone");
        map = new LinkedHashMap<String, String>();
        if (lst.size() > 0) {
            for (int j = 0; j < lst.size(); j++) {
                Map rs = (Map) lst.get(j);
                map.put((String) rs.get("location"), (String) rs.get("locationdisplay"));

            }
        }
        return map;
    }

    @Override
    public HashMap<String, String> getlocnew(String Countrycode) throws RemoteException {
        String portCodeValue = "";
        List<Map<String, String>> evaluateValueList = new ArrayList();
        HashMap<String, String> resultData = new HashMap();
        HashMap<String, String> valueMap = new HashMap();
        HashMap<String, String> finalresultData = new HashMap();
        List lst = null;
        Map hs = null;
        System.out.println("[ECOM] EComCntrlBean    getLocationList***" + Countrycode);
        try {
            hs = (Map) ecomvdsservice.getecomagencydetailswithcountrycode(Countrycode);
            lst = (List) hs.get("agency");
            //  System.out.println("[ECOM] EComCntrlBean   lst=> " + lst);
            String currDateee = null;

            if (lst == null || lst.size() == 0) {
                System.out.println("[ECOM] EComCntrlBean   inside getecomagencydetailswithports");
                hs = (Map) ecomvdsservice.getecomagencydetailswithports(Countrycode);
                lst = (List) hs.get("agency");
            }
            //    System.out.println("[ECOM] EComCntrlBean   lstlstlst = " + lst);
            if (lst.size() > 0) {
                Set<String> stPort = new HashSet<String>();
                for (int i = 0; i < lst.size(); i++) {

                    Map rs = (Map) lst.get(i);
                    portCodeValue = (String) rs.get("portcode");
                    stPort.add(portCodeValue);
                    valueMap = new HashMap();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    currDateee = dtf.format(now);
                    valueMap.put("portcode", portCodeValue);
                    valueMap.put("type", "E");
                    valueMap.put("saildate", currDateee);
                    valueMap.put("bookdate", currDateee);
                    evaluateValueList.add(valueMap);
                }
                resultData = (HashMap<String, String>) MultiAgencyUtil.getAgency(evaluateValueList);
                System.out.println("[ECOM] EComCntrlBean   resultData===>  " + resultData);
                String key = null, value = null;
                for (Map.Entry<String, String> entry : resultData.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    if (stPort.contains(key)) {
                        if (value != null && !("null".equalsIgnoreCase(value))) {
                            finalresultData.put(key, value);
                        }
                    }
                }
                System.out.println("[ECOM] EComCntrlBean   FinalresultData===>  " + finalresultData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalresultData;
    }

    @Override
    public String getPayloadValidation(String containerno, String eqptype, String cargowt, String grosswt, String wtunit, List pluginList, Map EQPWeight) throws RemoteException {
        String payloadwt = null, result = "valid";
        double actweight, payload;
        try {
            System.out.println("[ECOM] EComCntrlBean   containerno=" + containerno + "&eqptype=" + eqptype + "&cargowt=" + cargowt + "&grosswt=" + grosswt + "&wtunit=" + wtunit + "&pluginList=" + pluginList + "&EQPWeight=" + EQPWeight);

            if (pluginList != null && pluginList.contains("TW")) {
                Map hs = (Map) ecomsivdsservice.getecomcontainer(containerno, 0, 0);
                //
                // System.out.println("[ECOM] EComCntrlBean   ######>" + hs);
                List lst = (List) hs.get("containerprofile");
                if (lst != null && lst.size() > 0) {
                    hs = (Map) lst.get(0);
                    payloadwt = (String) hs.get("grosswt");
                    if (pluginList.contains("VPC")) {
                        payloadwt = (String) hs.get("payload");
                        System.out.println("[ECOM] EComCntrlBean   container profile payload weight=> " + payloadwt);
                    }
                }
            }
            System.out.println("[ECOM] EComCntrlBean   $$$$$$$$payloadwt => " + payloadwt);
            if (payloadwt == null || payloadwt.length() == 0) {
                System.out.println("[ECOM] EComCntrlBean   inside eqptype payload validation");
                payloadwt = (String) EQPWeight.get(checkNull(eqptype));
            }
            payloadwt = WeightConversion.weightConversion("K", payloadwt, wtunit);
            payload = Double.parseDouble(checkNull(payloadwt));
            if (pluginList != null && pluginList.contains("VPC")) {
                actweight = Double.parseDouble(checkNull(cargowt));
            } else {
                actweight = Double.parseDouble(checkNull(grosswt));
            }
            System.out.println("[ECOM] EComCntrlBean   actweight = " + actweight + " payload = " + payload);
            if (actweight > payload) {
                result = "invalid~~";
                if (pluginList != null && pluginList.contains("VPC")) {
                    result = result + "Cargo~~";
                } else {
                    result = result + "Gross~~";
                }
                result = result + payloadwt;
            }
            System.out.println("[ECOM] EComCntrlBean   result==> " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, String> getEqptypeAndMaxWeight() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getEqptypeAndMaxWeight*******");
        Map eqpListObj = new HashMap();
        Map rs = null;
        String eqptype = null;
        String grosswt = null;
        try {
            Map hs = (Map) ecomsivdsservice.getecomeqpmap();
            List lst = (List) hs.get("ecom_equipments");
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    eqptype = checkNull((String) rs.get("equiptype"));
                    grosswt = checkNull((String) rs.get("grosskg"));
                    eqpListObj.put(eqptype, grosswt);
                }
            }
            System.out.println("[ECOM] EComCntrlBean   getEqptypeAndMaxWeight:::::::" + eqpListObj);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT getEqptypeAndMaxWeight==>" + e);
        }
        return eqpListObj;
    }

    @Override
    public List<String> getSubmittedFileNames(String refno, String path, String type) throws RemoteException {
        ArrayList filenamelist = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   inside getSubmittedFileNames");
            filenamelist = new ArrayList<String>();
            if ("BL".equalsIgnoreCase(type)) {
                path = path.replace("Ecom", "BL");
            } else if ("BKG".equalsIgnoreCase(type)) {
                path = path.replace("Ecom", "BKG");
            }
            path = path + refno;
            File root = new File(path);
            if (root.exists()) {
                Collection files = FileUtils.listFiles(root, null, true);
                for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                    File file = (File) iterator.next();
                    filenamelist.add(checkNull(file.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EComCntrlBean   filenamelist==> " + filenamelist);
        return filenamelist;
    }

    @Override
    public String deleteSILock(String type, String refno) {
        System.out.println("[ECOM] EComCntrlBean   inside deleteSILock");
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String sql = "";
            if ("BKG".equalsIgnoreCase(type)) {
                sql = "delete from ECOMSILOCK where book_no=?";
            } else {
                sql = "delete from ECOMSILOCK where bl_no=?";
            }
            ps = con.prepareStatement(sql, 1004, 1007);
            ps.setString(1, refno);
            ps.executeUpdate();
            result = "Success!!";
        } catch (Exception e) {
            result = "ERROR!!";
            e.printStackTrace();
        } finally {
            try {

                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String insertSILock(String bookno, String blno, String createduser) {
        System.out.println("[ECOM] EComCntrlBean   inside insertSILock");
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String sql = "insert into ECOMSILOCK (book_no,bl_no,cruser,crdate,status) values (?,?,?,getdate(),?)";
            ps = con.prepareStatement(sql, 1004, 1007);
            ps.setString(1, bookno);
            ps.setString(2, blno);
            ps.setString(3, createduser);
            ps.setString(4, "A");
            ps.executeUpdate();
            result = "Success!!";
        } catch (Exception e) {
            result = "ERROR!!";
            e.printStackTrace();
        } finally {
            try {

                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    @Override
    public Map getEcomPasswordConfig(String attribute) {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getEcomPasswordConfig*******");
        Map confmap = new HashMap();
        Map rs = null;
        String attr = null;
        String value = null;
        try {
            Map hs = (Map) ecomsivdsservice.getEcomPasswordConfig(attribute);
            List lst = (List) hs.get("passwordconfig");
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    rs = (Map) lst.get(i);
                    attr = checkNull((String) rs.get("attribute"));
                    value = checkNull((String) rs.get("value"));
                    confmap.put(attr, value);
                }
            }
            System.out.println("[ECOM] EComCntrlBean   getEcomPasswordConfig:::::::" + confmap);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT getEcomPasswordConfig==>" + e);
        }
        return confmap;
    }

    @Override
    public List getStatsReportDetails(String usercode, String fromdate, String todate, String options, String service, String vessel, String voyage, String pla, String pol, String pod, String pld, String commodity, String equipmenttype, String contractno, String northbound, String southbound, String intertropics, String europe, String bound, String amendmentno) {
        List<StatsReportVo> statsreplis = null;
        SvmJsonList alist = null;
        try {
            Gson gson = new Gson();
            alist = new SvmJsonList();
            alist.add(checkNull(options));//1
            alist.add(checkNull(service));//2
            alist.add(checkNull(vessel));//3
            alist.add(checkNull(voyage));//4
            alist.add(checkNull(bound));//5
            alist.add("");//6
            alist.add("");//7
            alist.add(checkNull(pol));//8
            alist.add(checkNull(pod));//9
            alist.add(checkNull(fromdate));//10
            alist.add(checkNull(todate));//11
            alist.add("");//12
            alist.add("");//13
            alist.add("Ecom");//14
            alist.add("");//15
            alist.add("S");//16
            alist.add(checkNull(usercode));//17
            alist.add(checkNull(contractno));//18
            alist.add(checkNull(amendmentno));//19
            alist.add(checkNull(northbound));//20
            alist.add(checkNull(southbound));//21
            alist.add(checkNull(intertropics));//22
            alist.add(checkNull(europe));//23
            alist.add(checkNull(pla));//24
            alist.add(checkNull(pld));//25
            alist.add(checkNull(commodity));//26
            alist.add(checkNull(equipmenttype));//27
            alist.add("RST");//28

            Map resultMap = ecomGenService.getEcomStatsReportDetails(alist);
            System.out.println("[ECOM] EComCntrlBean   resultMap==> " + resultMap);
            if (resultMap != null) {
                resultMap = (Map) resultMap.get("rs");
                if (resultMap != null) {
                    statsreplis = gson.fromJson(gson.toJson((ArrayList) resultMap.get("rs0")), new TypeToken<List<StatsReportVo>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statsreplis;
    }

    @Override
    public String getVesselDetailsFromService(String service) throws RemoteException {
        String result = "";
        try {
            System.out.println("[ECOM] EComCntrlBean   inside getVesselDetailsFromService");
            Map hs = (Map) ecomsivdsservice.getEcomVesselMaster(service);
            List lst = (List) hs.get("vesselmaster");
            if (lst != null && lst.size() > 0) {
                Gson gson = new Gson();
                result = gson.toJson(lst);
            }
            System.out.println("[ECOM] EComCntrlBean   json => " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List getVoyageDetails(String service, String vessel) throws RemoteException {
        List lst = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   inside getVoyageDetails");
            Map hs = (Map) ecomsivdsservice.getEcomVoyageDetails(service, vessel);
            lst = (List) hs.get("bl");
            System.out.println("[ECOM] EComCntrlBean   lst => " + lst);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }

    @Override
    public List getContractDetails(String custcode) throws RemoteException {
        List lst = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   inside getContractDetails");
            Map hs = (Map) ecomsivdsservice.getEcomContractDetails(custcode);
            lst = (List) hs.get("bookingrates");
            System.out.println("[ECOM] EComCntrlBean   lst => " + lst);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }

    @Override
    public Map getVesselIMODetails() throws RemoteException {
        ArrayList vessellst;
        Map<String, String> vslmap = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecomvesseldetails();
            vslmap = new HashMap();
            vessellst = gson.fromJson(gson.toJson((ArrayList) hs.get("vesselmaster")), new TypeToken<List<VesselMaster>>() {
            }.getType());
            VesselMaster rs = null;
            for (int i = 0; i < vessellst.size(); i++) {
                rs = (VesselMaster) vessellst.get(i);
                vslmap.put(rs.getVesselcode(), rs.getImo());
            }
            // System.out.println("[ECOM] EComCntrlBean   getVesselIMODetails==> "+vslmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vslmap;
    }

    @Override
    public String putInsuranceSurveyList(String surveyjson) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   inside putInsuranceSurveyList");
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   surveyjson=> " + surveyjson);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            String sql = "UPDATE ecom_messageformat set Message =? where module ='E-Booking' and type='msg' and criteria='ins_survey' and status='A'";
            ps = con.prepareStatement(sql, 1004, 1007);
            ps.setString(1, surveyjson);
            ps.executeUpdate();
            result = "Success";
        } catch (Exception e) {
            result = "ERROR";
            e.printStackTrace();
        } finally {
            try {

                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public String checkInsuranceAccess(String custcode) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside checkInsuranceAccess*******");
        String result = "N";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement("select customercode from EcomInsuranceAccess where status='A' and customercode=?", 1004, 1007);
            ps.setString(1, custcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Y";
            }
            System.out.println("[ECOM] EComCntrlBean   result=> " + result);
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   *************Exception Caught AT checkInsuranceAccess==>" + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public String getMessageContentLang(String module, String criteria, String lang) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(MessageContentQryLang, 1004, 1007);
            ps.setString(1, module);
            ps.setString(2, criteria);
            ps.setString(3, lang);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public List<NotificationVo> getEcomNotificationDetails(String custcode, String loginname) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<NotificationVo> res = null;
        NotificationVo vo = null;
        try {
            res = new ArrayList<NotificationVo>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Get_Notification, 1004, 1007);
            ps.setString(1, loginname);
            ps.setString(2, custcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                vo = new NotificationVo();
                vo.setMessage(checkNull(rs.getString("message")));
                vo.setNotificationdate(checkNull(rs.getString("crdate")));
                vo.setMsgid(checkNull(rs.getString("msgid")));
                if ("Y".equalsIgnoreCase(checkNull(rs.getString("isviewed")))) {
                    vo.setIsviewed(true);
                }
                res.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return res;
        }
    }

    @Override
    public void setEcomNotificationViewStatus(String msgid, String loginname) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Insert_EcomNotificationStatus, 1004, 1007);
            ps.setString(1, msgid);
            ps.setString(2, loginname);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

    @Override
    public Map<String, Map> getDashboardCounts(String custno, String monthyr,String enable_dashboard_filter) throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   monthis" + monthyr + "custno" + custno);
        Map<String, Map> countMap = new HashMap<String, Map>();
        Connection con = null;
        Connection congate = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int dc = 0, sa = 0, draft = 0, cf = 0, a = 0;
        try {
            Map<String, String> bookdetails = new HashMap<String, String>();
            if (monthyr != null && monthyr.length() > 0) {
                String str[] = monthyr.split("-");
                String month = str[0];
                String year = str[1];
                int sa1 = 0, ap = 0;
                Map<String, String> bldeatils = new HashMap<String, String>();
                StringBuffer booknobuffer = new StringBuffer();
                String convstr = "";
                int dc1 = 0, sc1 = 0, ac1 = 0;
                Map<String, String> map = new HashMap<String, String>();
                String Confirmed = null, issued = null;

                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                /*String ecomquery = "select  ebook_status as status,count(ebook_status)as count from ecombooking where cust_code =?"
                        + " and ebook_status in ('S','D','V') and bookdate  between getDate() - 60 and getDate() + 30 group by ebook_status";//month(bookDate)=?  and year(bookDate)=?
                String acceptedquery = "select count(b.book_no)as count,b.status from booking b inner join booking_cust c on b.book_no=c.book_no"
                        + " inner join  booking_reference  bref on b.book_no=bref.book_no and bref.refid = 'DAK'"
                        + " where c.custid=? and c.custtypeid in ('BP','AP') and bookdate  between getDate() - 60 and getDate() + 30 and  b.status ='D' group by status";// month(bookDate) =?   and year(bookDate) =?  
                
                
                ps = con.prepareStatement(ecomquery, 1004, 1007);
                ps.setString(1, custno);
                // ps.setString(2, month);
                //ps.setString(3, year);
                rs = ps.executeQuery();
                while (rs.next()) {
                    //System.out.println("[ECOM] EComCntrlBean   .........status........count........" + rs.getString("status") + rs.getString("count"));
                    if ("D".equalsIgnoreCase(rs.getString("status"))) {
                        draft = rs.getInt("count");
                    } else if ("S".equalsIgnoreCase(rs.getString("status"))) {
                        sa = rs.getInt("count");
                    } else if ("V".equalsIgnoreCase(rs.getString("status"))) {
                        dc = rs.getInt("count");
                    }
                }
                bookdetails.put("Draft", String.valueOf(draft));
                bookdetails.put("Send to Agency", String.valueOf(sa));
                bookdetails.put("Declined", String.valueOf(dc));
                //congate = JDBCServiceLocatorFactory.getGateConnection();
                ps = con.prepareStatement(acceptedquery, 1004, 1007);
                ps.setString(1, custno);
                //ps.setString(2, month);
                //ps.setString(3, year);
                rs = ps.executeQuery();
                while (rs.next()) {
                    a = rs.getInt("count");
                }
                bookdetails.put("Approved", String.valueOf(a));
                String gateconfirmquery = "select count(b.book_no)as count from booking b inner join booking_cust c on b.book_no=c.book_no "
                        + "where c.custid=? and c.custtypeid in ('BP','AP') and bookdate  between getDate() - 60 and getDate() + 30  and b.status='C' ";//month(bookDate) = ?  and year(bookDate) = ? 
                ps = con.prepareStatement(gateconfirmquery, 1004, 1007);
                ps.setString(1, custno);
                //ps.setString(2, month);
                //ps.setString(3, year);
                rs = ps.executeQuery();
                while (rs.next()) {
                    cf = rs.getInt("count");
                }
                bookdetails.put("Confirmed", String.valueOf(cf));
                countMap.put("Booking", bookdetails);*/

                String query = "", gatequery = "";
                // ps = con.prepareStatement("select pathvalue from globalconfigproduct where pathname ='Enable_DashBoard_Filter'");
                // rs = ps.executeQuery();
                // if (rs.next()) {
                //     enable_dashboard_filter = checkNull(rs.getString("pathvalue"));
                // }

                if ("Y".equalsIgnoreCase(enable_dashboard_filter)) {
                    gatequery = "select COUNT(DISTINCT bl.bl_no) as countval, bl.status from bl_cust(nolock) inner join bl(nolock) on "
                            + " bl.bl_no = bl_cust.bl_no inner join customermaster cm(nolock) on cm.custcode=bl_cust.custid where custid=? "
                            + " and custtypeid in ('BP','AP') and bl.status in ('C','I')  and month( bl.polat) =? and  year( bl.polat) =? group by bl.status";
                    query = "SELECT bk.book_no FROM   booking AS bk WITH (NOLOCK) INNER JOIN booking_cust AS bc WITH (NOLOCK) ON bk.book_no = bc.book_no"
                            + " WHERE  bk.status in ('C','D') AND bc.custTypeId in ('BP','AP') AND bc.custid=? and month(bk.polAt) = ?  AND  year(bk.polAt) = ?  ";
                } else {
                    gatequery = "select COUNT(DISTINCT bl.bl_no) as countval, bl.status from bl_cust(nolock) inner join bl(nolock) on "
                            + " bl.bl_no = bl_cust.bl_no inner join customermaster cm(nolock) on cm.custcode=bl_cust.custid where custid=? "
                            + " and custtypeid in ('BP','AP') and bl.status in ('C','I') and  bl.polat BETWEEN GETDATE() - 90 AND GETDATE() + 30   group by bl.status ";// month( bl.polat) =? and  year( bl.polat) =?
                    query = "SELECT bk.book_no FROM   booking AS bk WITH (NOLOCK) INNER JOIN booking_cust AS bc WITH (NOLOCK) ON bk.book_no = bc.book_no"
                            + " WHERE bk.polat BETWEEN GETDATE() - 60 AND GETDATE() + 30 and  bk.status in ('C','D') AND bc.custTypeId in ('BP','AP') AND bc.custid=?";// month(bk.polAt) = ?  AND  year(bk.polAt) = ?  

                }

                ps = con.prepareStatement(gatequery, 1004, 1007);
                ps.setString(1, custno);
                if ("Y".equalsIgnoreCase(enable_dashboard_filter)) {
                    ps.setString(2, month);
                    ps.setString(3, year);
                }
                // ps.setString(2, month);
                //ps.setString(3, year);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ("C".equalsIgnoreCase(rs.getString("status"))) {
                        sa1 = rs.getInt("countval");
                    } else if ("I".equalsIgnoreCase(rs.getString("status"))) {
                        ap = rs.getInt("countval");
                    }
                }
                bldeatils.put("Confirmed", String.valueOf(sa1));
                bldeatils.put("Issued", String.valueOf(ap));
                countMap.put("BL", bldeatils);
                ps = con.prepareStatement(query, 1004, 1007);

                // ps.setString(1, month);
                // ps.setString(2, year);
                ps.setString(1, custno);
                if ("Y".equalsIgnoreCase(enable_dashboard_filter)) {
                    ps.setString(2, month);
                    ps.setString(3, year);
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    booknobuffer.append("'");
                    booknobuffer.append(checkNull(rs.getString("book_no")));
                    booknobuffer.append("'");
                    booknobuffer.append(",");
                }
                if (booknobuffer.length() > 0) {
                    booknobuffer.replace(booknobuffer.lastIndexOf(","), booknobuffer.lastIndexOf(",") + 1, "");
                    convstr = booknobuffer.toString();
                }
                if (convstr.length() <= 0) {
                    convstr = "''";
                }

                //System.out.println("[ECOM] EComCntrlBean   .........book nos..........." + convstr);
                String draftquery = " select count(status) as count, status from ecomsi  where bookno in(" + convstr + ") and blversion= 0 and status not in ('I') "
                        + " group by status";
                ps = con.prepareStatement(draftquery, 1004, 1007);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (checkNull(rs.getString("status")).equalsIgnoreCase("D")) {
                        dc1 = rs.getInt("count");
                    } else if (checkNull(rs.getString("status")).equalsIgnoreCase("S")) {
                        sc1 = rs.getInt("count");
                    } else if (checkNull(rs.getString("status")).equalsIgnoreCase("A")) {
                        ac1 = rs.getInt("count");
                    }
                }

                //--LRPS-7614 
                //LSCSAIL-1930
//                ps = con.prepareStatement("select distinct count(book_no) as count from BL(nolock) where book_no in(" + convstr + ") ", 1004, 1007);
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    ac1 = rs.getInt("count");
//                }
                map.put("Drafts", String.valueOf(dc1));
                map.put("Submitted", String.valueOf(sc1));
                map.put("Accepted", String.valueOf(ac1));
                countMap.put("SI", map);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
                if (congate != null) {
                    congate.close();
                }
            } catch (Exception es) {
                System.out.println("[ECOM] EComCntrlBean   Exception " + es);
            }
        }
        return countMap;

    }

    @Override
    public HashMap<String, String> getagencyloc() throws RemoteException {
        String portCodeValue = "";
        List<Map<String, String>> evaluateValueList = new ArrayList();
        HashMap<String, String> resultData = new HashMap();
        HashMap<String, String> valueMap = new HashMap();
        HashMap<String, String> finalresultData = new HashMap();
        List lst = null;
        Map hs = null;
        try {
            hs = (Map) ecomvdsservice.getecomagencywithports();
            lst = (List) hs.get("agency");
            String currDateee = null;
            if (lst == null || lst.size() == 0) {
                hs = (Map) ecomvdsservice.getecomagencylocationdetails();
                lst = (List) hs.get("agency");
            }
            System.out.println("[ECOM] EComCntrlBean   size of lst ===>" + lst.size());
            if (lst.size() > 0) {
                Set<String> stPort = new HashSet<String>();
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    portCodeValue = (String) rs.get("portcode");
                    stPort.add(portCodeValue);
                }
                Iterator<String> i = stPort.iterator();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                currDateee = dtf.format(now);
                while (i.hasNext()) {
                    valueMap = new HashMap();
                    valueMap.put("portcode", i.next());
                    valueMap.put("type", "E");
                    valueMap.put("saildate", currDateee);
                    valueMap.put("bookdate", currDateee);
                    evaluateValueList.add(valueMap);
                }
                resultData = (HashMap<String, String>) MultiAgencyUtil.getAgency(evaluateValueList);
                for (Map.Entry<String, String> entry : resultData.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (stPort.contains(key)) {
                        if (value != null && !("null".equalsIgnoreCase(value))) {
                            finalresultData.put(key, value);
                        }
                    }
                }
                System.out.println("[ECOM] EComCntrlBean   FinalresultData===>  " + finalresultData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalresultData;
    }

    public Map transportplanpod(String book_no) throws RemoteException {
        List list = null;
        Map<String, String> hs = null;
        Map rsl = null;
        Map vdsMap = (Map) ecomvdsservice.getecombookingleg(book_no);
        list = (List) vdsMap.get("booking_leg");

        if (list != null && list.size() > 0) {
            int lastIndx = list.size() - 1;
            rsl = new HashMap();
            hs = (Map) list.get(lastIndx);
            rsl.put("dischargeportid", checkNull((String) hs.get("dischargeportid")));
            rsl.put("etaDate", checkNull((String) hs.get("etadate")));
        }
        return rsl;
    }

    private String getPortName(String portcode) {
        String result = null;
        List portList = null;
        Map resultMap = null;
        try {
            result = "";
            Map hs = (Map) ecomvdsservice.getecomports(portcode);
            if (hs != null) {
                portList = (List) hs.get("ports");
                if (portList != null && portList.size() > 0) {
                    resultMap = (Map) portList.get(0);
                    result = checkNull((String) resultMap.get("portname"));
                }
            }
            System.out.println("[ECOM] EComCntrlBean   >>>getPortName>>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String getOpenCommodityList() throws RemoteException {
        String res = "";
        List portList = null, resList = null;
        Map resultMap = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   getOpenCommodityList");
            resList = new ArrayList();
            Map hs = (Map) ecomvdsservice.getEcomOpenCommodityList();
            if (hs != null) {
                portList = (List) hs.get("commoditygrp_edit");
                if (portList != null && portList.size() > 0) {
                    for (int i = 0; i < portList.size(); i++) {
                        resultMap = (Map) portList.get(i);
                        resList.add(checkNull((String) resultMap.get("commoditygrpid")));
                    }
                }
            }
            res = new Gson().toJson(resList);
            System.out.println("[ECOM] EComCntrlBean   res==> " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    @Override
    public String getEUCountryList() throws RemoteException {
        String res = "";
        List cunList = null, resList = null;
        Map resultMap = null;
        try {
            System.out.println("[ECOM] EComCntrlBean   getEUCountryList");
            resList = new ArrayList();
            Map hs = (Map) ecomvdsservice.getEcomEuropeCountries();
            if (hs != null) {
                cunList = (List) hs.get("regioncountrymapping");
                if (cunList != null && cunList.size() > 0) {
                    for (int i = 0; i < cunList.size(); i++) {
                        resultMap = (Map) cunList.get(i);
                        resList.add(checkNull((String) resultMap.get("countrycode")));
                    }
                }
            }
            res = new Gson().toJson(resList);
            System.out.println("[ECOM] EComCntrlBean   res==> " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ArrayList getCarrierDetails(String carriercode) throws RemoteException {
        ArrayList carrierDetails = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rst = null;
        Connection conn = null;
        try {
            conn = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            pst = conn.prepareStatement(GET_CARRIER_DETAILS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, carriercode);
            rst = pst.executeQuery();
            if (rst.next()) {
                String carrierName = checkNull(rst.getString("CARRIERNAME"));
                String carrierAddress = checkNull(rst.getString("ADDRESS"));
                String cityCode = checkNull(rst.getString("city"));
                String carrierZip = checkNull(rst.getString("ZIPCODE"));

                String cityName = "";
                String stateName = "";
                if (cityCode.length() > 0) {
                    PreparedStatement pst2 = conn.prepareStatement(GET_CITY_MASTER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst2.setString(1, cityCode);
                    ResultSet rst2 = pst2.executeQuery();
                    if (rst2.next()) {
                        cityName = checkNull(rst2.getString("cityname"));
                        stateName = checkNull(rst2.getString("statecode"));
                    }
                    rst2.close();
                    pst2.close();
                }

                carrierDetails.add(carrierName);
                carrierDetails.add(carrierAddress);
                carrierDetails.add(cityName);
                carrierDetails.add(stateName);
                carrierDetails.add(carrierZip);
            }
            System.out.println("[ECOM] EComCntrlBean   getCarrierDetails== carrierDetails => " + carrierDetails);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return carrierDetails;
    }

    @Override
    public HashMap<String, String> getPortCountryMap() throws RemoteException {
        System.out.println("[ECOM] EComCntrlBean   *****Inside getPortCountryMap*******");
        HashMap<String, String> portCountryMap = new HashMap<String, String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement(GET_PORT_COUNTRY_MAP, 1004, 1007);
            rs = ps.executeQuery();
            while (rs.next()) {
                String portCode = checkNull(rs.getString("portcode")).toUpperCase();
                String countryCode = checkNull(rs.getString("countrycode")).toUpperCase();
                if (portCode.length() > 0) {
                    portCountryMap.put(portCode, countryCode);
                }
            }
            System.out.println("[ECOM] EComCntrlBean   getPortCountryMap size==> " + portCountryMap.size());
        } catch (Exception e) {
            System.out.println("[ECOM] EComCntrlBean   Exception Caught AT getPortCountryMap==>" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return portCountryMap;
    }

}
