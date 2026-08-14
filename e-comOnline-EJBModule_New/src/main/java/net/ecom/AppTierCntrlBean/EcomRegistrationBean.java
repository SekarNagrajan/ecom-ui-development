package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.ecom.vo.ReturnVO;
import java.util.Random;
import net.ecom.vo.RegistrationVO;
import net.ecom.common.CryptoLibrary;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.PasswordResetTokenVo;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import net.ecom.vo.UserData;
import net.ecom.vo.RegisterMenuVO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.inject.Inject;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.RandomIdCreator;
import net.ecom.common.SHA256;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomRegVDSService;
import net.ecom.vds.serviceVo.CustomerContacts;
import net.ecom.vds.serviceVo.CustomerMasterVo;
import net.ecom.vds.serviceVo.EcomCommonModel;
import net.ecom.vds.serviceVo.EcomCommonModelVo;
import net.ecom.vds.serviceVo.EcomCommonVo;
import net.ecom.vds.serviceVo.EcomRegCommonModel;
import net.ecom.vo.AdminCustomerVo;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.UserCreationVO;

@Stateless(name = "EcomRegistrationBean")
@Remote(EcomRegistrationBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
     @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS")
})
public class EcomRegistrationBean implements EcomRegistrationBeanRemote {

    protected static final String strUserDataAdminQRY = "select top 1 cust_code,um.First_Name,um.last_name,um.first_Name+' '+um.last_Name as UserName,"
            + " um.user_Email,prefLang,'C' as userType,'C' as systemType,loginname from ecom_reg_details um "
            + " where um.cust_code=? and um.cust_code is not null and um.status='A'";

    protected static final String updateusertemp = "update ecom_reg_details set isValid=? where random_number=?";
    protected static final String updateuserremark = "update ecom_reg_details set remarks=? where random_number=?";
    protected static final String strAgency = "select a.email as agencymail,e.agencycode,a.agencyname from ecom_reg_details e inner join agency a on e.agencycode=a.agencycode  where e.loginname=?";
    protected static final String strRegmail = "select random_number,loginname,first_Name where from ecom_reg_details loginname=? and status=?";
    protected static final String MailContent = "select message,from_mailid,subject from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";
    protected static final String Forward_query = "select message from ecom_messageformat(nolock) where Status='a' and Type ='msg' "
            + " and criteria = ? and Module =? and usertype=? ";
    protected static final String Forward_lang_query = "select message from ecom_messageformatlang(nolock) where Status='a' and Type ='msg' "
            + " and criteria = ? and Module =? and usertype=? and lang=?";

    protected static final String TermsQry = "select message from ecom_messageformat(nolock) where Status='a' and Type ='msg' and criteria ='condition' and Module =?";

    protected static final String AgencyLoginDet = "select username,usertype,userid,password from ecom_adminuser_details(nolock) where userid=? and status='A'";
    protected static final String vendormodulesQry = "select distinct Module from ecom_MessageFormat(nolock) where type='Mail' and status='A'";
    protected static final String strMainAdminData = "select mnuid,mnuname, ClassValue,AttrValue,LabelValue,MenuType,ParentMenu,RefNo,Category from vendormodulemapping where status='A' and vendorcode='Y' and MenuType='M' order by menuorder";
    protected static final String strSubAdminData = "select mnuid,mnuname, ClassValue,AttrValue,LabelValue,MenuType,ParentMenu,RefNo,Category from vendormodulemapping where status='A' and vendorcode='Y' and MenuType='S' order by menuorder";
    protected static final String getMainMenuDetQRY = "select MenuId,MenuName,Category,RefNo,ClassValue,AttrValue,LabelValue,MenuType,ParentMenu from MenuRegistration where MenuStatus='A' and userttype='U' and MenuType='M' and menuorder is not null order by menuorder";
    protected static final String getSubMenuDetQRY = "select MenuId,MenuName,Category,RefNo,ClassValue,AttrValue,LabelValue,MenuType,ParentMenu from MenuRegistration where MenuStatus='A' and userttype='U' and MenuType='S' and menuorder is not null order by ParentMenu, menuorder";
    protected static final String RegMenuDetailsQry = "select MenuId,MenuName,Category from MenuRegistration where MenuStatus='A' and userttype='U'";
    protected static final String alloweduserQry = "select allowedusers from customermaster where custcode=?";
    protected static final String CustMenuDetailsQry = "select menuid from SplPrivilegeMenu where custid=?";
    protected static final String CustMenuDetailsQry1 = "select MR.MenuId,MR.MenuName,MR.Category from MenuRegistration MR where MR.MenuId in ? ";
    protected static final String CustMenuDetailsQry2 = "select MenuId,MenuName,Category from MenuRegistration where MenuId not in ? and MenuStatus='A' and userttype='U' ";

    protected static final String strAdminLoginData = "select username,usertype from ecom_adminuser_details(nolock) where userid=? and password=? and status='A' and usertype='C'";
    protected static final String AdmncustQry = " select distinct cust_code,comp_name from ecom_reg_details where  cust_code is not null and cust_code<>'' and comp_name is not null";
    protected static final String deleteRejecteduserfromEcom = "delete from ecom_tempcompany where id=? and refid=?";
    protected static final String deleteRejecteduserfromEcom1 = "delete from ecom_tempuserdetail where loginname=? and refid=? and status='V'";
    protected static final String deletRejecteduserfromlrp = "delete from ecom_reg_details where loginname=?";
    protected static final String CHECK_ADMIN_PWD = "select username,password from ecom_adminuser_details where username=?";
    protected static final String UPDATE_ADMIN_PWD = "update ecom_adminuser_details set password=? where username=?";
    protected static final String CUSTAPNAME = "select custName from customermaster where custcode=?";
//
    protected static final String ResetPasswordDetailQRY = "select usr.password,reg.first_name,usr.emailid as user_email from ecomuserdetails usr (nolock) inner join ecom_reg_Details reg(nolock) on reg.loginname=usr.emailid where usr.emailid=? and usr.status in ('A','F')";
    protected static final String strUserPwdQRY = "select emailid from ecomuserdetails(nolock) where emailid=?";
    protected static final String UserResDet = "select password from ecomuserdetails(nolock) where emailid=? and password=?";
    protected static final String UserResDet2 = "update ecomuserdetails set password=?,lastpwdup=getdate(),status='A' where emailid=?";
    protected static final String CheckPasswordQry = "select password from ecomuserdetails(nolock) where emailid=? and password=?";
    protected static final String forgetPasswordUpdate = "Update ecomuserdetails set password=?,lastpwdup=getdate(),status=? where emailid=? and status in ('A','F')";
    protected static final String forgetPasswordQry = "select usr.password,reg.first_name,usr.emailid as user_email from ecomuserdetails usr (nolock) inner join ecom_reg_Details reg(nolock) on reg.loginname=usr.emailid where usr.emailid=? and usr.status in ('A','F')";
    protected static final String checkuserquery = "select loginname from ecom_reg_Details(nolock) where loginname = ?";

    protected static final String GETUSERDETAILS_FOR_USERCREATION = "select custCode,contactname,email,webid,onlineactive from customercontacts where custCode=? and status='A' and contacttype='online'";
    protected static final String CHECK_USER_INPUT = "select emailid,ccmailid,preferredTimezone,lastpwdup,status,password from ecomuserdetails where emailid=? "; // and password=? //Removed the status conditions from this query. why because, for acvtivation and deactivation purpose 
    protected static final String SELECT_USERLANGAUAGE_FROMECOMDB = "select reg.prefLang,usr.userrole from ecom_reg_details reg (nolock) inner join ecomuserdetails usr(nolock) on reg.user_email=usr.emailid where usr.emailid=? and usr.status in ('A','F')";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? where mdlid =?";
    protected static final String VALIDUSER_TMP = "select isvalid,random_number,loginname,cust_code,control_location,agencycode,Country,Comp_Name,Address,Pin_Code,City,Comp_Phone,User_Email,transferdoc,reg_no from ecom_reg_details where random_number=?";
    protected static final String SAVE_USER_ACTIVITY_HISTORYLOG = "insert into ECOMRegHistory(email,remarks,type,crdate,cruser,status,customercode)values(?,?,?,getdate(),?,?,?);";
    protected static final String INSERT_NEW_CUSTOMER = "insert into ecom_reg_details(Comp_Name,Address,"
            + "City,Pin_Code,Country,State,Comp_Domain,Comp_Phone,First_Name,"
            + "Last_Name,User_Phone,User_Mobile,User_Fax,User_Email,type,isValid,status,Address2,"
            + "loginname,registered_Date,random_number,prefLang,password,city_other,reg_no,recentbl,control_location,agencycode,cust_code,preferredTimezone,title,TaxID,Comp_Mobile)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String INSERT_NEW_CUSTOMER_USERCREATION = "insert into ecom_reg_details(Comp_Name,Address,"
            + "City,Pin_Code,Country,State,Comp_Domain,Comp_Phone,First_Name,"
            + "Last_Name,User_Phone,User_Mobile,User_Fax,User_Email,type,isValid,status,Address2,"
            + "loginname,registered_Date,random_number,prefLang,password,city_other,cust_code,reg_no,RegType,control_location,agencycode,preferredTimezone)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),?,?,?,?,?,?,?,?,?,?)";
    protected static final String INSERT_USERDETAILS = "insert into ecomuserdetails(emailid,password,userrole,customercode,status,accepteddate,menuid,preferredTimezone)values(?,?,?,?,?,getDate(),?,?)";
    protected static final String INSERTPRIVILEGE = "insert into splprivilegemenu(menuid,custid) values(?,?)";
    protected static final String GETMENUIDS = "select menuid from menuregistration where refno in('BKG','SHI','BOL','VGM','DBD','TRK')";
    private static final String UPDATE_ECOM_REG = "update ecom_reg_details set isValid='Y',remarks=?,regtype='A',agencyreqtype='N',statusadvisebyagency='A' where random_number=?";
    private static final String GET_AUTO_REG_COUNTRY_CONFIG = "select pathvalue from globalconfigproduct(NOLOCK) where pathname='Reg_Auto_Process_Country'";
    private static final String INSERT_AUTO_REG_CUSTOMER_MASTER = "insert into CustomerMaster(custcode,custname,address1,CountryID,City,"
            + "statecode,Phone,IndustryCode,"
            + "billaddressee,billaddress1,billaddress2,billcountry,billstatecode,"
            + "billzipcode,billcity,billphone,billmobileno,billemail,custposition,"
            + "crusr,crdate,salesrepcode,custtype,carriercode,"
            + "cmpcode,inactiveflag,otherscity,otherszip,othersbillcity,othersbillzip,email,linecustcode,status,allowedusers)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,?,?,?,?,?,?,?,?,?,?,?,'5')";
    private static final String UPDATE_REG_CUSTCODE = "update ecom_reg_details set Cust_Code=?,transferdoc='Y' where loginname=?";
    private static final String GET_USER_DETAILS = "select password,user_email,cust_code,status,first_name,last_name,user_phone,user_mobile,user_fax,preferredTimezone,comp_name from ecom_reg_details where loginname=? ";
    private static final String INSER_USERDETAILS = "insert into ecomuserdetails(emailid,password,userrole,customercode,status,accepteddate,menuid,preferredTimezone)values(?,?,?,?,?,getDate(),?,?)";
    private static final String GETCCMAILID = "select ccmailid,preferredTimezone from ecomuserdetails where emailid=?";
    private static final String UPDATECCMAILID = "update ecomuserdetails set ccmailid=? where emailid=?";
    private static final String UPDATETIMEZONEVALUE = "update ecomuserdetails set preferredTimezone=? where emailid=?";

    private static final String GET_COMPY_DETAILS = "select  top 1 ag.agencycode,cm.controllocation,ag.agencyname,cm.Address1,cm.Address2,cm.city,cm.statecode, "
            + "             cm.otherscity,cm.ZipCode,cm.Phone,cm.countryid,c.CountryName,cm.CustCode,s.statedesc,cm.CustName from customermaster cm (nolock) "
            + "             inner join country c(nolock) on cm.CountryID = c.CountryCode "
            + "             left join agency ag(nolock) on ag.location=cm.controllocation and ag.type='O' and ag.status='A' and ag.Status_Agy='A'"
            + "             left outer join Statesmaster s(nolock) on s.statecode=cm.statecode "
            + "             where cm.status='A'   and cm.inactiveflag='A'  and (cm.CustCode=?)";

    private static final String UPDATELOGINDATE = "update ecomuserdetails set logindate=getdate() where Emailid=?";
    private static final String UPDATELOGOUTDATE = "update ecomuserdetails set logoutdate=getdate() where Emailid=?";
    private static final String GET_COUNTRY_FILE_DETAILS = "select country,fieldname,mandatory from ecom_reg_kyc_config where country=?";
    private static final String GET_FILE_UPLOAD_PATH = "select value from bkgconfig(nolock) where attribute='KYC'";

    private static final String UPDATEWRONGPASSATTEMPT = "update ecomuserdetails set WrgPassAtmpt= WrgPassAtmpt+1 where Emailid=?";
    private static final String RESETWRONGPASSATTEMPT = "update ecomuserdetails set WrgPassAtmpt=0, LockedStatus='N',LockedTime=null where Emailid=?";
    private static final String LOCKWRONGPASSACCOUNT = "update ecomuserdetails set LockedStatus='Y',LockedTime=getdate() where Emailid=?";
    private static final String GET_WRG_LOGIN_DETAILS = "select WrgPassAtmpt,LockedStatus,LockedTime from ecomuserdetails  where Emailid =? ";
    private static final String GET_COMPY_CITY_DETAIL = "select top 1 cm.CustName,cm.city from customermaster cm (nolock) where cm.status='A' and cm.inactiveflag='A' and (cm.CustCode=?)";
    private static final String GETREQFORACTIVATEACCOUNTDETAILS = "select Cust_Code,isValid,agencycode from ecom_reg_details(Nolock) where  User_Email = ?";
    private static final String PUTREQFORACTIVATEACCOUNT = "Update ecom_reg_details set isValid ='P' where User_Email =? ";
    private static final String ACCOUNTMOVETOPENDWITHAGENCY = "update ecom_reg_details set status='L',isValid ='P' where loginname =? ";
 private static final String GET_ACCOUNT_STATUS_MESSAGE = "select isValid,agencycode,status from ecom_reg_details where User_Email =? ";
    // Password Reset Token SQL Queries
    private static final String INSERT_PASSWORD_RESET_TOKEN = "INSERT INTO Ecom_password_reset_tokens (EmailID, token, RequestDate, Status) VALUES (?, ?, GETDATE(), 'R')";
    private static final String GET_PASSWORD_RESET_TOKEN = "SELECT id, EmailID, token, RequestDate, CompleteDate, Status FROM Ecom_password_reset_tokens WHERE token = ? ";
    private static final String UPDATE_PASSWORD_RESET_TOKEN = "UPDATE Ecom_password_reset_tokens SET CompleteDate = GETDATE(), Status = ? WHERE token = ?";
    private static final String EXPIRE_PASSWORD_RESET_TOKEN = "UPDATE Ecom_password_reset_tokens SET Status = 'E' WHERE EmailID = ? AND Status = 'R' AND RequestDate < DATEADD(MINUTE, -30, GETDATE())";

    // Password Reset Token Update Password SQL Query
    private static final String UPDATE_PASSWORD_WITH_TOKEN = "UPDATE ecomuserdetails SET password = ?, lastpwdup = GETDATE(),status='A' WHERE emailid = ?";
    private static final String GET_ECOM_REG_DETAILS = "select password,preferredTimezone,status,User_Email,Title,First_Name,Last_Name,User_Phone,isValid,country from ecom_reg_details(nolock) where user_email=?";
    private static final String INCOMPLETED_REG_CHECK_USER = "select user_email from ecom_reg_details(nolock) where user_email=? and isValid in ('M','P')";
    private static final String INCOMPLETED_REG_CHECK_PWD = "select password from ecom_reg_details(nolock) where user_email=? and password=? and isValid in ('M','P')";
    private static final String INCOMPLETED_REG_UPDATE_PWD = "update ecom_reg_details set password=? where user_email=? and isValid in ('M','P')";

    @Inject
    VDSInvoker vds;

    @Resource
    private SessionContext sessionCtx;

    EcomBKGVdsService ecomvdsservice;
    EcomRegVDSService regVDSService;

    @PostConstruct
    public void init() {
        try {
            ecomvdsservice = vds.create(EcomBKGVdsService.class);
            regVDSService = vds.create(EcomRegVDSService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReturnVO saveToRegistration(RegistrationVO regVo, String saveType, String companyName, String KYC_fileupload) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReturnVO retVo = null;
        String randomid = "", othercity = null, control_loc = null, bccMail = null, refno = null;
        saveType = checkNull(saveType);
        companyName = checkNull(companyName);
        CryptoLibrary crypt = null;
        MailFormatVO mailFormatObj = null;
        try {
            crypt = new CryptoLibrary();
            randomid = RandomIdCreator.generateRandomId();
            if (regVo != null) {
                refno = regNoGenerate("REG", GeneralConf.REG_PREFIX);
                control_loc = getControlLocation(regVo.getAgencycode());
                System.out.println("[ECOM] EcomRegistrationBean ===>control_loc" + control_loc + "===>Agencycode" + regVo.getAgencycode());
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                ps = con.prepareStatement(INSERT_NEW_CUSTOMER);
                ps.setString(1, regVo.getHiddencustname().trim());
                ps.setString(2, regVo.getCompany_address().trim());
//                if ("Y".equalsIgnoreCase(regVo.getOthercitystatus())) {
//                    ps.setString(3, regVo.getOther_city());
//                    othercity = "Y";
//                } else {
//                    ps.setString(3, regVo.getCompany_city());
//                    othercity = "N";
//                }
                ps.setString(3, regVo.getOther_city());
                othercity = "Y";
                ps.setString(4, regVo.getCompany_postcode().trim());
                ps.setString(5, regVo.getCountrycode().trim());
                ps.setString(6, regVo.getCompany_state().trim());
                ps.setString(7, regVo.getCompany_domain().trim());
                ps.setString(8, (regVo.getCompany_country_code().trim() + "-" + regVo.getCompany_phone_code().trim() + "-" + regVo.getCompany_phone_no()).trim());
                ps.setString(9, regVo.getFirstname());
                ps.setString(10, regVo.getLastname());
                ps.setString(11, regVo.getCust_country_code() + "-" + regVo.getCust_phone_code() + "-" + regVo.getCust_phone_no());
                ps.setString(12, regVo.getMobile_code() + "-" + regVo.getMobile_no());
                ps.setString(13, regVo.getFax_country_code() + "-" + regVo.getFax_phone_code() + "-" + regVo.getFax_phone_no());
                ps.setString(14, regVo.getLoginname());   // Loginname act as Email
                ps.setString(15, EcomConsts.NEW_CUSTOMER_TYPE);//type-> N
                ps.setString(16, EcomConsts.NEW_CUSTOMER_ISVALID); // Isvalid-> I
                ps.setString(17, EcomConsts.NEW_CUSTOMER_STATUS); //  Status- N
                ps.setString(18, regVo.getCompany_address1().trim());
                ps.setString(19, regVo.getLoginname());
                ps.setString(20, randomid);
                ps.setString(21, regVo.getPreflanguage());
                ps.setString(22, crypt.encrypt(regVo.getPassword()));
                ps.setString(23, othercity);
                ps.setString(24, refno);
                ps.setString(25, regVo.getRecent_bl());
                ps.setString(26, control_loc);
                ps.setString(27, regVo.getAgencycode());
                ps.setString(28, regVo.getCustomerno());
                System.out.println("[ECOM] EcomRegistrationBean timezoneejb" + regVo.getTimezone());
                ps.setString(29, regVo.getTimezone());
                ps.setString(30, regVo.getUsertitle());
                ps.setString(31, regVo.getTax_id());
                ps.setString(32, (regVo.getCompany_mobile_code() + "-" + regVo.getCompany_mobile_no()).trim());
                int i = ps.executeUpdate();

                System.out.println("[ECOM] EcomRegistrationBean KYC_fileupload------" + KYC_fileupload);
                /* if ("Y".equalsIgnoreCase(KYC_fileupload)) {
                    System.out.println("[ECOM] EcomRegistrationBean rename folder------");
                    String sharepath = getfileuploadsharepath();
                    String emailFolderPath = sharepath + regVo.getLoginname();
                    boolean folderExists = Files.exists(Paths.get(emailFolderPath));
                    System.out.println("[ECOM] EcomRegistrationBean folderExists--------" + folderExists);
                    if (folderExists) {
                        Path emailFolder = Paths.get(emailFolderPath);
                        String regnoFolderPath = sharepath + refno;
                        Path regnoFolder = Paths.get(regnoFolderPath);

                        Files.move(emailFolder, regnoFolder);
                        System.out.println("[ECOM] EcomRegistrationBean Folder renamed to regno: " + regnoFolderPath);
                    }
                } */
            }
            //GET AGENCY MAIL
            Map hs = regVDSService.getEcomRegAgencyMail(regVo.getAgencycode());
            System.out.println("[ECOM] EcomRegistrationBean agency mail in registration>>" + hs);
            int mailCount = 0;
            String tmpBccMail = null;
            //////
            List lst = (List) hs.get("agencycontacts");
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    Map mailRs = (Map) lst.get(i);
                    tmpBccMail = checkNull((String) mailRs.get("contactemailid"));

                    if (tmpBccMail.length() > 0) {
                        if (mailCount == 0) {
                            bccMail = tmpBccMail;
                            mailCount++;
                        } else {
                            bccMail = bccMail + "," + tmpBccMail;
                        }
                    }
                }
                System.out.println("[ECOM] EcomRegistrationBean >>bccMail.." + bccMail);
            }
            //
            String res = saveToUserHistory(regVo.getLoginname(), EcomConsts.NEW_CUSTOMER_REG_HISTORY_REMARKS, EcomConsts.NEW_CUSTOMER_REG_HISTORY_TYPE, EcomConsts.NEW_CUSTOMER_REG_HISTORY_STATUS, "");
            //End
            mailFormatObj = new MailFormatVO();
            retVo = new ReturnVO();
            retVo.setStrSaveType(saveType);
            mailFormatObj = getMailContent(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE);
            //
            System.out.println("[ECOM] EcomRegistrationBean langtypevalue" + regVo.getLangType());

            if ("en".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessage(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE)));
            } else if ("ch".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessageLang(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE, "ch")));

            } else if ("ma".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessageLang(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE, "ma")));

            } else if ("es".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessageLang(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE, "es")));

            }
            String msg = mailFormatObj.getStrMailContent();
            //
            msg = msg.replaceAll(EcomConsts.REG_MSG_NEWCUST_CUSTOMER, regVo.getFirstname());
            msg = msg.replaceAll(EcomConsts.REG_MSG_NEWCUST_CLIENTID, mailFormatObj.getStrRegard());
            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());
            retVo.setBccMail(bccMail);
            retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            retVo.setStrToMailId(regVo.getLoginname());
            retVo.setRandomid(randomid);
            retVo.setStrRefNo(refno);
            //  
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception While Calling SaveToRegistration" + e.getMessage());
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
                System.out.println("[ECOM] EcomRegistrationBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return retVo;
    }

    private String saveToUserHistory(String email, String remarks, String type, String status, String customercode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(SAVE_USER_ACTIVITY_HISTORYLOG);
            ps.setString(1, email);
            ps.setString(2, remarks);
            ps.setString(3, type);
            ps.setString(4, email);
            ps.setString(5, status);
            ps.setString(6, customercode);
            int User_Insert = ps.executeUpdate();
            if (User_Insert > 0) {
                result = "success";
            } else {
                result = "failed";
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception While Calling saveToUserHistroy" + e.getMessage());
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
                System.out.println("[ECOM] EcomRegistrationBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ReturnVO validateRegUserbymail(String randomId, String client_code, String verifyAgencyMailPlugin, String disableAutoRegVerification, String enableRegAutoProcess, String Multiple_agencies_location) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement pslrp = null;
        String custCode = "", compMail = null, loginDomain = null, compDomain = null;//future use
        String refno = null, ret = null, type = null, loginname = null, controlLoc = null, agencycode = null, controlocation = null;
        String country = null, compName = null, address = null, pinCode = null, city = null, compPhone = null, userEmail = null;
        String transferdoc = null, regno = null;
        boolean check = false;
        ReturnVO rvo = new ReturnVO();
        Map map = null;//future use
        List list = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(VALIDUSER_TMP);
            ps.setString(1, randomId);
            rs = ps.executeQuery();
            while (rs.next()) {
                check = true;

                custCode = checkNull(rs.getString("cust_code"));
                type = rs.getString("isvalid");
                refno = rs.getString("random_number");
                loginname = rs.getString("loginname");
                controlLoc = rs.getString("control_location");
                agencycode = rs.getString("agencycode");
                controlocation = rs.getString("control_location");
                country = checkNull(rs.getString("Country"));
                compName = checkNull(rs.getString("Comp_Name"));
                address = checkNull(rs.getString("Address"));
                pinCode = checkNull(rs.getString("Pin_Code"));
                city = checkNull(rs.getString("City"));
                compPhone = checkNull(rs.getString("Comp_Phone"));
                userEmail = checkNull(rs.getString("User_Email"));
                transferdoc = checkNull(rs.getString("transferdoc"));
                regno = checkNull(rs.getString("reg_no"));
                System.out.println("[ECOM] EcomRegistrationBean >>controlLoc>>" + controlLoc + ">>agencycode>>" + agencycode + ">>country>>" + country);
                //company domain validation - for future use 
                /**
                 * map = (Map) regVDSService.getEcomCompanyMail(custCode);
                 * System.out.println("[ECOM] EcomRegistrationBean checkcomapny
                 * email>>" + map); list = (List) map.get("customermaster"); if
                 * (list != null && list.size() > 0) { map = (Map) list.get(0);
                 * compMail = (String) map.get("email"); loginDomain =
                 * checkNull(loginname.split("@")[1]).trim(); if
                 * (compMail.length() > 0) { compDomain =
                 * checkNull(compMail.split("@")[1]).trim(); } if
                 * (loginDomain.equalsIgnoreCase(compDomain)) { type = "A"; } }
                 * System.out.println("[ECOM] EcomRegistrationBean
                 * >>loginDomain>>" + loginDomain); System.out.println("[ECOM]
                 * EcomRegistrationBean >>compDomain>>" + compDomain);*
                 */
            }
            // Auto-process: if plugin enabled and no cust_code yet, check country config and auto-create customer
            if ("Y".equalsIgnoreCase(enableRegAutoProcess) && "I".equalsIgnoreCase(type)
                    && (custCode == null || custCode.length() == 0)
                    && country != null && country.length() > 0) {
                String configuredCountries = getAutoRegCountryConfig();
                System.out.println("[ECOM] EcomRegistrationBean Auto-reg configuredCountries=>" + configuredCountries + " registeredCountry=>" + country);
                if (configuredCountries != null && configuredCountries.length() > 0) {
                    for (String configCountry : configuredCountries.split(",")) {
                        if (configCountry.trim().equalsIgnoreCase(country.trim())) {
                            System.out.println("[ECOM] EcomRegistrationBean Auto-reg country matched: " + country + " -> creating CustomerMaster");
                            String generatedCode = createAutoRegCustomer(loginname, compName, address, city, pinCode, country, compPhone, userEmail, client_code);
                            if (generatedCode != null && generatedCode.length() > 0) {
                                custCode = generatedCode;
                                transferdoc="Y";
                                System.out.println("[ECOM] EcomRegistrationBean Auto-reg customer created, custCode=" + custCode);
                            }
                            break;
                        }
                    }
                }
            }
            if (custCode != null && custCode.length() > 0 && "I".equalsIgnoreCase(type) && (!"Y".equalsIgnoreCase(disableAutoRegVerification))) {
                type = "A";
            }
            if ("A".equalsIgnoreCase(type)) {
                System.out.println("[ECOM] EcomRegistrationBean randomId - " + randomId + "  - loginname - " + loginname + "custCode - " + custCode + " - client_code - " + client_code);
                rvo = saveAutoReg(randomId, loginname, custCode, client_code, agencycode, transferdoc, regno);
            } else if ("I".equalsIgnoreCase(type)) {
                rvo = updateUserDetails(randomId, loginname, controlLoc, agencycode, verifyAgencyMailPlugin, controlocation, Multiple_agencies_location);
            } else {
                ret = "already";       // Registration already verified
                rvo.setStrDisplayMsg(ret);
            }
            if (!check) {
                ret = "failed";       // Invalid Link!
                rvo.setStrDisplayMsg(ret);
            }
            type = null;
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in validateRegUser " + e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (pslrp != null) {
                    pslrp.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in validateRegUser" + ex);
                ex.printStackTrace();
            }
        }
        return rvo;
    }

    private String getAutoRegCountryConfig() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String countries = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_AUTO_REG_COUNTRY_CONFIG);
            rs = ps.executeQuery();
            if (rs.next()) {
                countries = rs.getString(1);
            }
            System.out.println("[ECOM] EcomRegistrationBean getAutoRegCountryConfig=>" + countries);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in getAutoRegCountryConfig " + e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) { rs.close(); }
                if (ps != null) { ps.close(); }
                if (con != null) { con.close(); }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        return countries;
    }

    private String createAutoRegCustomer(String loginname, String compName, String address, String city,
            String pinCode, String country, String compPhone, String userEmail, String client_code) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String custcode = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();

            // Generate unique customer code via stored procedure
            pst = con.prepareStatement("Exec GetCRMModuleIndexValue_Sp ?,?,?");
            pst.setString(1, client_code);
            pst.setString(2, "CustomerMaster");
            pst.setString(3, "7");
            rs = pst.executeQuery();
            while (rs.next()) {
                custcode = rs.getString(1);
            }
            System.out.println("[ECOM] EcomRegistrationBean createAutoRegCustomer generated custcode=>" + custcode);

            if (custcode != null && custcode.length() > 0) {
                // Insert into CustomerMaster with Provisional status
                pst = con.prepareStatement(INSERT_AUTO_REG_CUSTOMER_MASTER);
                pst.setString(1, custcode);          // custcode
                pst.setString(2, compName);          // custname
                pst.setString(3, address);           // address1
                pst.setString(4, country);           // CountryID
                pst.setString(5, city);              // City
                pst.setString(6, null);              // statecode
                pst.setString(7, compPhone);         // Phone
                pst.setString(8, null);              // IndustryCode
                pst.setString(9, compName);          // billaddressee
                pst.setString(10, address);          // billaddress1
                pst.setString(11, "");               // billaddress2
                pst.setString(12, country);          // billcountry
                pst.setString(13, null);             // billstatecode
                pst.setString(14, pinCode);          // billzipcode
                pst.setString(15, city);             // billcity
                pst.setString(16, compPhone);        // billphone
                pst.setString(17, null);             // billmobileno
                pst.setString(18, userEmail);        // billemail
                pst.setString(19, "PRO");            // custposition
                pst.setString(20, "SYSTEM");         // crusr
                pst.setString(21, null);             // salesrepcode
                pst.setString(22, "AP-BP");             // custtype
                pst.setString(23, client_code);      // carriercode
                pst.setString(24, client_code);      // cmpcode
                pst.setString(25, "A");              // inactiveflag
                pst.setString(26, "Y");              // otherscity
                pst.setString(27, "N");              // otherszip
                pst.setString(28, "Y");              // othersbillcity
                pst.setString(29, "N");              // othersbillzip
                pst.setString(30, userEmail);        // email
                pst.setString(31, custcode);         // linecustcode
                pst.setString(32, "A");              // status -> Provisional
                int inserted = pst.executeUpdate();
                System.out.println("[ECOM] EcomRegistrationBean createAutoRegCustomer CustomerMaster inserted count=" + inserted + " custcode=" + custcode);

                // Update ecom_reg_details with the generated customer code (ECOMDB)
                updateRegCustCode(custcode, loginname);

                // Save history log for auto customer creation
                saveToUserHistory(loginname, "Auto Customer Created", "C", "Y", custcode);
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in createAutoRegCustomer " + e);
            e.printStackTrace();
            custcode = null;
        } finally {
            try {
                if (rs != null) { rs.close(); }
                if (pst != null) { pst.close(); }
                if (con != null) { con.close(); }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        return custcode;
    }

    private void updateRegCustCode(String custcode, String loginname) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = con.prepareStatement(UPDATE_REG_CUSTCODE);
            pst.setString(1, custcode);
            pst.setString(2, loginname);
            pst.executeUpdate();
            System.out.println("[ECOM] EcomRegistrationBean updateRegCustCode ecom_reg_details.Cust_Code updated to " + custcode + " for loginname=" + loginname);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in updateRegCustCode " + e);
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) { pst.close(); }
                if (con != null) { con.close(); }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void executeVdsAutoRegCalls(String loginname, String custCode, String transferdoc, String regno) throws RemoteException {
        moveFilesToCustomerMaster(loginname, custCode, transferdoc, regno);
    }

    private void moveFilesToCustomerMaster(String loginname, String custcode, String transferdoc, String regno) {
        String doccategory = null, basepath = null, filePath = null, fileFolder = null, randomnumber = null, contenttype = null;
        net.ecom.vds.serviceVo.Fsdoclog fs = null;
        File targetfile = null;
        ArrayList fileuploadlist = null;
        Path file_Path = null;
        try {
            System.out.println("[ECOM] EcomRegistrationBean moveFilesToCustomerMaster=> " + transferdoc);
            if ("Y".equalsIgnoreCase(transferdoc)) {
                fileuploadlist = new ArrayList();
                Map hs = (Map) ecomvdsservice.getEcomRegFilePath();
                List lst = (List) hs.get("bkgconfig");
                if (lst != null && lst.size() > 0) {
                    Map bkg = (Map) lst.get(0);
                    filePath = (String) bkg.get("value");
                }
                System.out.println("[ECOM] EcomRegistrationBean moveFilesToCustomerMaster filePath: " + filePath);
                fileFolder = filePath + File.separator + regno;

                File source = new File(fileFolder);
                if (source.exists()) {
                    List<File> files = (List<File>) org.apache.commons.io.FileUtils.listFiles(source, org.apache.commons.io.filefilter.TrueFileFilter.INSTANCE, org.apache.commons.io.filefilter.TrueFileFilter.INSTANCE);
                    if (files != null && files.size() > 0) {
                        hs = (Map) ecomvdsservice.getEcomFSDocConfig();
                        lst = (List) hs.get("fsdocconfig");
                        if (lst != null && lst.size() > 0) {
                            Map bkg = (Map) lst.get(0);
                            doccategory = (String) bkg.get("doccategory");
                            basepath = (String) bkg.get("basepath");
                        }
                        for (File file : files) {
                            fs = new net.ecom.vds.serviceVo.Fsdoclog();
                            randomnumber = RandomIdCreator.generateRandomId();
                            file_Path = Paths.get(file.getPath());
                            contenttype = Files.probeContentType(file_Path);
                            fs.setRefno(randomnumber);
                            fs.setDocrefno(custcode);
                            fs.setModuleid("CUM");
                            fs.setDoccategory(doccategory);
                            fs.setDocsize(String.valueOf(file.length()));
                            fs.setDoctype(contenttype);
                            fs.setUploaduser("Ecom");
                            fs.setBasepath(basepath);
                            fs.setFilename(file.getName());
                            fs.setStatus("A");
                            fs.setSysfilename(randomnumber + file.getName());
                            fileuploadlist.add(fs);
                            targetfile = new File(basepath + File.separator + randomnumber + file.getName());
                            org.apache.commons.io.FileUtils.copyFile(file, targetfile);
                        }
                        net.ecom.vds.serviceVo.EcomCommonModel cm = new net.ecom.vds.serviceVo.EcomCommonModel();
                        cm.setFsdoclog(fileuploadlist);
                        ecomvdsservice.setEcomFSDocLog(cm);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in moveFilesToCustomerMaster " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ReturnVO saveAutoReg(String randomId, String loginname, String custcode, String client_code, String agencycode, String transferdoc, String regno) {
        ReturnVO rvo = new ReturnVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int menuCount = 0;
        boolean checkUser = false;
        String menuId = null, password = null, user_email = null, cust_code = null, status = null, firstName = null;
        String lastName = null, phone = null, fax = null, mobile = null, timeZone = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATE_ECOM_REG);
            ps.setString(1, "ACCEPT-Auto Reg");
            ps.setString(2, randomId);
            ps.executeUpdate();

            //Insert ecomuserdetails
            menuId = getConfiguredMenu();
            if (checkNull(menuId).length() == 0) {
                ps = con.prepareStatement(GETMENUIDS);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (menuCount == 0) {
                        menuId = checkNull(rs.getString("menuid")).trim();
                    } else {
                        menuId = menuId + "_" + checkNull(rs.getString("menuid")).trim();
                    }
                    menuCount++;
                }
            }
            ps = con.prepareStatement(GET_USER_DETAILS);
            ps.setString(1, loginname);
            rs = ps.executeQuery();
            if (rs.next()) {
                checkUser = true;
                password = rs.getString("password");
                user_email = rs.getString("user_email");
                cust_code = rs.getString("cust_code");
                status = rs.getString("status");
                firstName = rs.getString("first_name");
                lastName = checkNull(rs.getString("last_name"));
                phone = checkNull(rs.getString("user_phone"));
                fax = checkNull(rs.getString("user_fax"));
                mobile = checkNull(rs.getString("user_mobile"));
                timeZone = checkNull(rs.getString("preferredTimezone"));
            }
            System.out.println("[ECOM] EcomRegistrationBean >>checkUser>>" + checkUser);
            //
            if (checkUser) {
                ps = con.prepareStatement(INSER_USERDETAILS);
                ps.setString(1, user_email);
                ps.setString(2, password);
                ps.setString(3, "Y");
                ps.setString(4, cust_code);
                ps.setString(5, "A");
                ps.setString(6, menuId);
                ps.setString(7, timeZone);
                ps.executeUpdate();

                System.out.println("[ECOM] EcomRegistrationBean >menuId>>>" + menuId + ">loginname>>>" + loginname + ">TimeZone>>>" + timeZone);
                //insert into splprivilege
                ps = con.prepareStatement(INSERTPRIVILEGE);
                ps.setString(1, menuId);
                ps.setString(2, loginname);
                ps.executeUpdate();

                //Insert into customercontacts
                CustomerContacts contactObj = new CustomerContacts();
                contactObj.setCmpcode(client_code);
                contactObj.setCustcode(cust_code);
                contactObj.setContactname(firstName + " " + lastName);
                contactObj.setPhone(phone);
                contactObj.setFax(fax);
                contactObj.setMobile(mobile);
                contactObj.setEmail(user_email);
                contactObj.setCarriercode(client_code);
                contactObj.setStatus("A");
                contactObj.setCrusr("WEB");
                contactObj.setOnlineactive("A");
                contactObj.setContacttype("online");
                contactObj.setWebid(loginname);
                contactObj.setAdmin("N");
                List<CustomerContacts> contactList = new ArrayList<CustomerContacts>();
                contactList.add(contactObj);
                EcomRegCommonModel regModel = new EcomRegCommonModel();
                regModel.setCustomercontacts(contactList);
                regVDSService.setEcomCustomerContacts(regModel);
                if ("Y".equalsIgnoreCase(transferdoc)) {
                    try {
                        // moveFilesToCustomerMaster runs in its own REQUIRES_NEW transaction to avoid
                        // enlisting NFRTXDS a second time in the same JTA transaction as ECOMDB.
                        sessionCtx.getBusinessObject(EcomRegistrationBeanRemote.class)
                                  .executeVdsAutoRegCalls(loginname, cust_code, transferdoc, regno);
                    } catch (Exception ex) {
                        System.out.println("[ECOM] EcomRegistrationBean Exception in executeVdsAutoRegCalls " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                //Save Registration Log
                saveToUserHistory(loginname, "Customer Registation Approved", "C", "Y", custcode);
                System.out.println("[ECOM] EcomRegistrationBean >>auto registration inserted....");
                rvo.setStrDisplayMsg("successautoreg");

                MailFormatVO mailFormatObj = new MailFormatVO();
                mailFormatObj = getMailContent("Registration", "Conmmail", "C");
                CryptoLibrary crypto = new CryptoLibrary();
                password = crypto.decrypt(password);
                rvo.setStrDisplayMsg("successautoreg");
                rvo.setFirstName(firstName);
                rvo.setLoginName(loginname);
                rvo.setPassword(password);
                rvo.setStrMailMsg(mailFormatObj.getStrMailContent());
                rvo.setStrSubject(mailFormatObj.getStrSubject());
                rvo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                rvo.setStrToMailId(loginname);

                MailFormatVO mailFormatObject = new MailFormatVO();
                mailFormatObject = getMailContent("Registration", "regmailage", "C");
                rvo.setFirstName(firstName);
                rvo.setLoginName(loginname);
                rvo.setAdmMailMsg(mailFormatObject.getStrMailContent());
                rvo.setAdmSubject(mailFormatObject.getStrSubject());
                rvo.setAdmFromMailId(mailFormatObject.getStrFromAddr());
                rvo.setAdmToMailId(checkNull(getAgencyMail(agencycode)));
                System.out.println("[ECOM] EcomRegistrationBean getAdmToMailId********" + rvo.getAdmToMailId());
            } else {
                rvo.setStrDisplayMsg("failed");
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
            } catch (Exception ex) {
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in validateRegUser " + ex);
                ex.printStackTrace();
            }
        }
        return rvo;
    }

    private ReturnVO updateUserDetails(String randomId, String loginname, String controlLoc, String agencycode, String verifyAgencyMailPlugin, String controlocation, String Multiple_agencies_location) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReturnVO rvo = new ReturnVO();
        String ret = null, firstName = null, CompanyName = null, Contact = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            boolean multiagency = false;
            if ("Y".equalsIgnoreCase(Multiple_agencies_location)) {
                Map hs = regVDSService.getportmultipleagencysetup(controlocation);
                List lst = (List) hs.get("multiagencysetup");
                if (lst != null && lst.size() > 0) {
                    multiagency = true;
                }
            }
            ps = con.prepareStatement(updateusertemp);
            if (controlLoc != null && controlLoc.length() > 0 && agencycode != null && agencycode.length() > 0) {
                if (multiagency) {
                    ps.setString(1, "M");
                } else {
                    ps.setString(1, "P");
                }

            } else {
                ps.setString(1, "N");
            }
            ps.setString(2, randomId);
            int flag = ps.executeUpdate();
            if (multiagency && flag > 0) {
                ps = con.prepareStatement(updateuserremark);
                ps.setString(1, "ACCEPT- ");
                ps.setString(2, randomId);
                ps.executeUpdate();
            }
            if (flag > 0) {
                ret = saveToUserHistory(loginname, EcomConsts.NEW_CUSTOMER_ACTIVE_LINK_HISTORY_REMARKS, EcomConsts.NEW_CUSTOMER_ACTIVE_LINK_HISTORY_TYPE, EcomConsts.NEW_CUSTOMER_ACTIVE_LINK_HISTORY_STATUS, "");
                rvo.setStrDisplayMsg(ret);
                ps = con.prepareStatement(GET_USER_DETAILS);
                ps.setString(1, loginname);
                rs = ps.executeQuery();
                if (rs.next()) {
                    firstName = rs.getString("first_name");
                    CompanyName = rs.getString("comp_name");
                    Contact = rs.getString("user_phone");
                }

                MailFormatVO mailFormatObj = new MailFormatVO();
                mailFormatObj = getMailContent("Registration", "verifymail", "C");
                rvo.setStrMailMsg(mailFormatObj.getStrMailContent());
                rvo.setFirstName(firstName);
                rvo.setStrSubject(mailFormatObj.getStrSubject());
                rvo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                rvo.setStrToMailId(loginname);
                rvo.setAdmToMailId(checkNull(getAgencyMail(agencycode)));
                if ("Y".equalsIgnoreCase(verifyAgencyMailPlugin)) {
                    MailFormatVO mailFormatObject = new MailFormatVO();
                    mailFormatObject = getMailContent("Registration", "verifyagnc", "C");
                    rvo.setAdmMailMsg(mailFormatObject.getStrMailContent());
                    rvo.setCompany(CompanyName);
                    rvo.setContact(Contact);
                    rvo.setAdmSubject(mailFormatObject.getStrSubject());
                    rvo.setAdmToMailId(checkNull(getAgencyMailCon(agencycode)));

                }

            } else {
                ret = "failed";
                rvo.setStrDisplayMsg(ret);
                System.out.println("[ECOM] EcomRegistrationBean Registration verification failed");
            }
            if (multiagency) {
                rvo.setAdmToMailId(null);
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
            } catch (Exception ex) {
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in validateRegUser " + ex);
                ex.printStackTrace();
            }
        }
        return rvo;
    }

    private String getAgencyMailCon(String agencycode) {
        String agencymail = "";
        Map hs = null;
        List lis = null;
        try {
            hs = (Map) regVDSService.getEcomRegAgencyContactMail(agencycode);
            lis = (List) hs.get("agencycontacts");
            int i = 0;
            if (lis.size() > 0) {
                for (int j = 0; j < lis.size(); j++) {
                    Map bkg = (Map) lis.get(j);
                    String eml = (String) bkg.get("contactemailid");
                    if (i == 0) {
                        agencymail = eml;
                    } else {
                        agencymail = agencymail + "," + eml;
                    }
                    i++;
                }
            }
            System.out.println("[ECOM] EcomRegistrationBean agencymail +>" + agencymail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agencymail;
    }

    @Override
    public ReturnVO getRegistrationMail(String type, String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReturnVO retVo = null;
        String randomid = null;
        String emailid = null;
        String firstname = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(strRegmail);
            ps.setString(1, email);
            ps.setString(2, type);
            rs = ps.executeQuery();
            while (rs.next()) {
                randomid = rs.getString("random_number").trim();
                emailid = rs.getString("loginname").trim();
                firstname = rs.getString("first_Name").trim();
            }
            if (randomid != null && randomid.length() > 0) {
                MailFormatVO mailFormatObj = new MailFormatVO();
                retVo = new ReturnVO();
                mailFormatObj = getMailContent("Registration", "Insert", "C");
                retVo.setStrDisplayMsg(checkNull(getForwardMessage("Registration", "Insert", "c")));
                String msg = mailFormatObj.getStrMailContent();
                msg = msg.replaceAll("@customer", firstname);
                msg = msg.replaceAll("@client", mailFormatObj.getStrRegard());
                retVo.setStrMailMsg(msg);
                retVo.setStrSubject(mailFormatObj.getStrSubject());
                retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                retVo.setStrToMailId(emailid);
                retVo.setRandomid(randomid);
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception While Calling SaveToRegistration" + e.getMessage());
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
                System.out.println("[ECOM] EcomRegistrationBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return retVo;
    }

    @Override
    public ReturnVO saveToAdminRegistration(UserCreationVO regVo, String saveType, String cust_code, int user_count, String client_code) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psD = null;
        ResultSet rst = null;
        ReturnVO retVo = new ReturnVO();
        CryptoLibrary crypt = new CryptoLibrary();
        saveType = checkNull(saveType);
        String randomid1 = null, randomid2 = null, randomid3 = null, randomid = null;
        String controllocation = null, agencycode = null, straddress2 = null, strwebsite = null, strotherscity = null, strcompname = null, strcompphone = null, straddress = null, strstate = null, strcountry = null, strcity = null, strpostal = null, timezone = null;
        saveType = checkNull(saveType);
        Random generator = new Random();
        Gson gson = new Gson();
        try {
            randomid1 = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
            randomid2 = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
            randomid3 = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
            randomid = randomid1 + randomid2 + randomid3;
            Map hs = (Map) ecomvdsservice.getecomapdetails(cust_code);
            List<CustomerMasterVo> lst = gson.fromJson(gson.toJson((ArrayList) hs.get("customermaster")), new TypeToken<List<CustomerMasterVo>>() {
            }.getType());
            if (lst.size() > 0) {
                CustomerMasterVo rs = (CustomerMasterVo) lst.get(0);
                straddress2 = checkNull(rs.getAddress2());
                strwebsite = checkNull(rs.getWebsite());
                strotherscity = checkNull(rs.getOtherscity());
                strcompname = checkNull(rs.getCustname());
                strcompphone = checkNull(rs.getPhone());
                straddress = checkNull(rs.getAddress1());
                strstate = checkNull(rs.getState());
                strcountry = checkNull(rs.getCountryid());
                strcity = checkNull(rs.getCity());
                strpostal = checkNull(rs.getPobox());
                controllocation = checkNull(rs.getControllocation());
            }
            //
            Map hsp = (Map) ecomvdsservice.getEcomAgencyLocation(controllocation);
            //////
            List agencylst = (List) hsp.get("agencylocations");
            if (agencylst != null && agencylst.size() > 0) {
                Map rs = (Map) agencylst.get(0);
                agencycode = (String) rs.get("agencycode");
            }
            //
            //Insert into ecom_reg_Details
            String refno = regNoGenerate("REG", GeneralConf.REG_PREFIX);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if (regVo != null) {
                ///conEcomdb = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                ps = con.prepareStatement(INSERT_NEW_CUSTOMER_USERCREATION);
                ps.setString(1, strcompname);
                ps.setString(2, straddress);
                ps.setString(3, strcity);
                ps.setString(4, strpostal);
                ps.setString(5, strcountry);
                ps.setString(6, strstate);
                ps.setString(7, strwebsite);
                ps.setString(8, strcompphone);
                //userinformation
                ps.setString(9, regVo.getFirstname());
                ps.setString(10, regVo.getLastname());
                ps.setString(11, regVo.getCust_country_code() + "-" + regVo.getCust_phone_code() + "-" + regVo.getCust_phone_no());
                ps.setString(12, regVo.getMobile_code() + "-" + regVo.getMobile_no());
                ps.setString(13, regVo.getFax_country_code() + "-" + regVo.getFax_phone_code() + "-" + regVo.getFax_phone_no());
                ps.setString(14, regVo.getLoginname());   // Loginname act as Email
                ps.setString(15, EcomConsts.NEW_CUSTOMER_TYPE);//type-> N
                ps.setString(16, "Y"); // Isvalid-> X
                ps.setString(17, EcomConsts.NEW_CUSTOMER_STATUS); //  Status- N
                ps.setString(18, straddress2);
                ps.setString(19, regVo.getLoginname());
                ps.setString(20, randomid);
                ps.setString(21, regVo.getPreflanguage());
                ps.setString(22, crypt.encrypt(regVo.getPassword()));
                ps.setString(23, strotherscity);
                ps.setString(24, cust_code);
                ps.setString(25, refno);
                ps.setString(26, "S");//RegType-System
                ps.setString(27, controllocation);//controllocation
                ps.setString(28, agencycode);//agencycode
                ps.setString(29, regVo.getTimezone());
                int i = ps.executeUpdate();
            }

            String menuId = null;
            int menuCount = 0;
            String timeZone = null;
            menuId = getConfiguredMenu();
            if (checkNull(menuId).length() == 0) {
                ps = con.prepareStatement(GETMENUIDS);
                rst = ps.executeQuery();
                while (rst.next()) {
                    if (menuCount == 0) {
                        menuId = checkNull(rst.getString("menuid")).trim();
                    } else {
                        menuId = menuId + "_" + checkNull(rst.getString("menuid")).trim();
                    }
                    menuCount++;
                }
            }
            //
            //
            ps = con.prepareStatement(INSERT_USERDETAILS);
            ps.setString(1, regVo.getEmail());
            ps.setString(2, crypt.encrypt(regVo.getPassword()));
            ps.setString(3, "N");
            ps.setString(4, cust_code);
            ps.setString(5, "A");
            ps.setString(6, menuId);
            ps.setString(7, timeZone);
            int k = ps.executeUpdate();
            //End 

            //insert into splprivilege
            ps = con.prepareStatement(INSERTPRIVILEGE);
            ps.setString(1, menuId);
            ps.setString(2, regVo.getEmail());
            ps.executeUpdate();
            // String res = saveToUserHistory(regVo.getLoginname(), EcomConsts.NEW_CUSTOMER_REG_HISTORY_REMARKS, EcomConsts.NEW_CUSTOMER_REG_HISTORY_TYPE, EcomConsts.NEW_CUSTOMER_REG_HISTORY_STATUS);

            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (psD != null) {
                    psD.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomRegistrationBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }

            // insert into Cusotmer Gate
            ArrayList cntlst = new ArrayList();
            CustomerContacts rm = new CustomerContacts();
            EcomCommonModel cm = new EcomCommonModel();
            rm.setCmpcode(client_code);
            rm.setCustcode(regVo.getCompanyId());
            rm.setContactname(regVo.getFirstname() + " " + regVo.getLastname());
            rm.setPhone(regVo.getCust_country_code() + "-" + regVo.getCust_phone_code() + "-" + regVo.getCust_phone_no());
            rm.setFax(regVo.getFax_country_code() + "-" + regVo.getFax_phone_code() + "-" + regVo.getFax_phone_no());
            rm.setEmail(regVo.getEmail());
            rm.setMobile(regVo.getMobile_code() + "-" + regVo.getMobile_no());
            rm.setCrusr("WEB");
            rm.setOnlineactive("A");
            rm.setWebid(regVo.getEmail());
            rm.setContacttype("online");
            rm.setStatus("A");
            cntlst.add(rm);
            cm.setCustomercontacts(cntlst);
            hs = (Map) ecomvdsservice.setecomnewcontactdetails(cm);
            //

            MailFormatVO mailFormatObj = new MailFormatVO();

            mailFormatObj = getMailContent("Registration", "Conmmail", "c");
            String msg = mailFormatObj.getStrMailContent();

            msg = msg.replaceAll("@customer", regVo.getFirstname());
            msg = msg.replaceAll("@userid", regVo.getLoginname());
            msg = msg.replaceAll("@password", regVo.getPassword());

            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());
            retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            retVo.setStrToMailId(regVo.getEmail());
            retVo.setStrName("Y");
        } catch (Exception e) {
            retVo.setStrName("E");
            System.out.println("[ECOM] EcomRegistrationBean Exception While Calling saveToAdminRegistration" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (psD != null) {
                    psD.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomRegistrationBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return retVo;
    }

  /*  private String regNoGenerate(String module, String prefix) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String regnum = null, prefixname = null;
        int bkgIndexNum = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BOOK_NO_GEN);
            ps.setString(1, module);
            rs = ps.executeQuery();
            while (rs.next()) {
                bkgIndexNum = rs.getInt("mdlindex");
                prefixname = rs.getString("mdlPrefix");
            }
            ps = con.prepareStatement(BOOK_NO_UPDATE);
            ps.setInt(1, bkgIndexNum + 1);
            ps.setString(2, module);
            ps.executeUpdate();
            regnum = prefixname + (bkgIndexNum + 1);
            System.out.println("[ECOM] EcomRegistrationBean  =NEW regnum=>>" + regnum);
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
                System.out.println("[ECOM] EcomRegistrationBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return regnum;
    }
*/

private String regNoGenerate(String module, String prefix1) {
    final String SQL =
        "UPDATE ecomMdlIndex WITH (ROWLOCK, UPDLOCK) " +
        "SET mdlIndex = mdlIndex + 1 " +
        "OUTPUT inserted.mdlPrefix, inserted.mdlIndex " +
        "WHERE mdlid = ?";

    try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection()) {
        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, module);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    con.rollback();
                    throw new IllegalStateException("Module not found in ecomMdlIndex: " + module);
                }

                String prefix = rs.getString(1);   // inserted.mdlPrefix
                long next    = rs.getLong(2);      // inserted.mdlIndex (already incremented)
                String bknum = prefix + next;

                con.commit();
                System.out.println("[ECOM] EcomRegistrationBean NEW regnum >> " + bknum);
                return bknum;
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ""; // or rethrow as unchecked, based on your convention
    }
}
    public ArrayList getuserDetails(String Usercode) throws RemoteException {
        ArrayList userArray = new ArrayList();
        String email = "";
        String webid = "";
        String contactname = "";
        String cust_code = "";
        String onlineactive = "";
        UserData userdate = null;
        List<CustomerContacts> custcontactlst = null;
        Gson gson = null;
        try {
            gson = new Gson();
            System.out.println("[ECOM] EcomRegistrationBean =getuserDetails===" + Usercode);
            Map hs = (Map) ecomvdsservice.getecomcontactdetailsforreg(Usercode);
            custcontactlst = gson.fromJson(gson.toJson((ArrayList) hs.get("customercontacts")), new TypeToken<List<CustomerContacts>>() {
            }.getType());
            if (custcontactlst != null && custcontactlst.size() > 0) {
                for (int i = 0; i < custcontactlst.size(); i++) {
                    CustomerContacts rs = (CustomerContacts) custcontactlst.get(i);
                    userdate = new UserData();
                    cust_code = checkNull(rs.getCustcode());
                    contactname = checkNull(rs.getContactname());
                    email = checkNull(rs.getWebid());
                    webid = checkNull(rs.getWebid());
                    System.out.println("[ECOM] EcomRegistrationBean ==cust_code" + cust_code + "==webid" + webid);
                    onlineactive = checkNull(rs.getOnlineactive());
                    if ("A".equalsIgnoreCase(onlineactive)) {
                        onlineactive = "Active";
                    } else if ("V".equalsIgnoreCase(onlineactive)) {
                        onlineactive = "Deactivate";
                    } else {
                        onlineactive = "";
                    }
                    userdate.setUsercode(cust_code);
                    userdate.setContantname(contactname);
                    userdate.setOnlineactive(onlineactive);
                    userdate.setLoginName(webid);
                    userdate.setEmail(email);
                    userArray.add(userdate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userArray;
    }

    @Override
    public ReturnVO deleteuserdetails(String loginname, String contactname) throws RemoteException {
        Map hs = null;
        ReturnVO retVo = null;
        try {
            retVo = new ReturnVO();
            ArrayList lst = new ArrayList();
            EcomCommonVo rm = new EcomCommonVo();
            EcomCommonModelVo cm = new EcomCommonModelVo();
            rm.setWebid(loginname);
            lst.add(rm);
            cm.setCustomerstatusupdate(lst);
            hs = (Map) ecomvdsservice.putecomuserdelete(cm);
            //
            MailFormatVO mailFormatObj = new MailFormatVO();

            mailFormatObj = getMailContent("Registration", "Deactivate", "C");
            String msg = mailFormatObj.getStrMailContent();

            msg = msg.replace("@userid", contactname);

            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());
            retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            retVo.setStrToMailId(loginname);
            retVo.setStrName("Successfully Deactivated");
            //
        } catch (Exception e) {
            e.printStackTrace();
            retVo.setStrName("Error while delete the records");
        }
        return retVo;
    }

    @Override
    public ReturnVO activeUserDetails(String loginname, String contactname) throws RemoteException {
        Map hs = null;
        ReturnVO retVo = null;
        try {
            retVo = new ReturnVO();
            ArrayList lst = new ArrayList();
            EcomCommonVo rm = new EcomCommonVo();
            EcomCommonModelVo cm = new EcomCommonModelVo();
            rm.setWebid(loginname);
            lst.add(rm);
            cm.setCustomerstatusupdate(lst);
            hs = (Map) ecomvdsservice.putecomuseractive(cm);
            //
            MailFormatVO mailFormatObj = new MailFormatVO();

            mailFormatObj = getMailContent("Registration", "Activate", "C");
            String msg = mailFormatObj.getStrMailContent();

            msg = msg.replace("@userid", contactname);

            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());
            retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            retVo.setStrToMailId(loginname);
            retVo.setStrName("Successfully Activated");
            //
        } catch (Exception e) {
            e.printStackTrace();
            retVo.setStrName("Error while delete the records");
        }
        return retVo;
    }

    @Override
    public String deleteRejectedusers(String loginname, String refid, String uid) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String Message = "Deleted";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(deleteRejecteduserfromEcom);
            ps.setString(1, uid);
            ps.setString(2, refid);
            ps.executeUpdate();
            ps = con.prepareStatement(deleteRejecteduserfromEcom1);
            ps.setString(1, loginname);
            ps.setString(2, refid);
            ps.executeUpdate();
            ps = con.prepareStatement(deletRejecteduserfromlrp);
            ps.setString(1, loginname);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Message = "Error while delete the records";
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
        return Message;
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

    @Override
    public MailFormatVO getMailContent(String module, String criteria, String usertype) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(MailContent, 1004, 1007);
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
    public String CheckUserId(String id) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "New";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(checkuserquery);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = "Exists";
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
            System.out.println("[ECOM] EcomRegistrationBean str==>" + str);
            return str;
        }
    }

    @Override
    public String CheckPassword(String login, String password) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CryptoLibrary crypto = new CryptoLibrary();
        String oldPass = crypto.encrypt(password);
        String str = "New";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CheckPasswordQry);
            ps.setString(1, login);
            ps.setString(2, oldPass);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = "Exists";
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
            System.out.println("[ECOM] EcomRegistrationBean str==>" + str);
            return str;
        }
    }

    public String checkState(String statecode) throws RemoteException {

        String str = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomcheckstate(statecode);
            //////
            List lst = (List) hs.get("statesmaster");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                str = (String) bkg.get("statecode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String checkcountry(String country) throws RemoteException {

        String str = "";
        if (country.contains("-")) {
            country = country.substring(0, country.indexOf("-")).trim();
        }
        try {
            Map hs = (Map) ecomvdsservice.getecomcheckcountry(country);
            //////
            List lst = (List) hs.get("country");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                str = (String) bkg.get("countrycode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getTerms(String module) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {

            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(TermsQry, 1004, 1007);
            ps.setString(1, module);
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
    public String getUserResDet(String username, String password, String npassword, String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String s = "";
        String newPass = "";
        String oldPass = "";
        try {
            CryptoLibrary crypto = new CryptoLibrary();
            oldPass = crypto.encrypt(password);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(strUserPwdQRY, 1004, 1007);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                try {
                    ps = con.prepareStatement(UserResDet);
                    ps.setString(1, username);
                    ps.setString(2, oldPass);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        try {
                            newPass = crypto.encrypt(npassword);
                            System.out.println("[ECOM] EcomRegistrationBean new password= " + newPass);
                            ps = con.prepareStatement(UserResDet2);
                            ps.setString(1, newPass);
                            ps.setString(2, username);
                            int a = ps.executeUpdate();
                            s = "Successfully Updated To database";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        s = "Invalid Old Password";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                s = "Invalid Username and Old Password";
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
        return s;
    }

    @Override
    public String getIncompletedRegUserResDet(String username, String password, String npassword) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String s = "";
        String newPass = "";
        String oldPass = "";
        try {
            CryptoLibrary crypto = new CryptoLibrary();
            oldPass = crypto.encrypt(password);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(INCOMPLETED_REG_CHECK_USER, 1004, 1007);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                try {
                    ps = con.prepareStatement(INCOMPLETED_REG_CHECK_PWD);
                    ps.setString(1, username);
                    ps.setString(2, oldPass);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        try {
                            newPass = crypto.encrypt(npassword);
                            System.out.println("[ECOM] EcomRegistrationBean incompleted reg new password updated for: " + username);
                            ps = con.prepareStatement(INCOMPLETED_REG_UPDATE_PWD);
                            ps.setString(1, newPass);
                            ps.setString(2, username);
                            ps.executeUpdate();
                            s = "Successfully Updated To database";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        s = "Invalid Old Password";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                s = "Invalid Username and Old Password";
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
        return s;
    }

    public String getControlLocation(String agencycode) throws RemoteException {

        String str = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomlocationwithagency(agencycode);
            //
            System.out.println("[ECOM] EcomRegistrationBean ######>" + hs);
            List lst = (List) hs.get("agency");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = checkNull((String) rs.get("location")).trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EcomRegistrationBean str==>" + str);
        return str;
    }

    @Override
    public ArrayList getAgencyLoginDet(String username, String password) throws RemoteException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        String logintype = "", loginname = "", userid = "";
        ArrayList arry = new ArrayList();
        try {

            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = conn.prepareStatement(AgencyLoginDet, 1004, 1007);
            pst.setString(1, username);
            //pst.setString(2, password);
            rst = pst.executeQuery();
            if (rst.next()) {
                String storedPassword = rst.getString("password");
                if (storedPassword != null && storedPassword.equals(password)) {
                    loginname = rst.getString("username");
                    logintype = rst.getString("usertype");
                    userid = rst.getString("userid");
                } else {
                    loginname = "";
                    logintype = "Invalid Username / Password";
                }

            } else {
                loginname = "";
                logintype = "Invalid Username / Password";
            }
            arry.add(loginname);
            arry.add(logintype);
            arry.add(userid);
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
        return arry;
    }

    @Override
    public ArrayList getAgencyLoginDetEncrypt(String username, String password, boolean isenabledEncrypt) throws RemoteException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        String logintype = "", loginname = "", userid = "";
        ArrayList arry = new ArrayList();
        try {

            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = conn.prepareStatement(AgencyLoginDet, 1004, 1007);
            pst.setString(1, username);
            //pst.setString(2, password);
            rst = pst.executeQuery();
            if (rst.next()) {
                String storedPassword = rst.getString("password");

                if (isenabledEncrypt) {
                    // System.out.println("storedPassword 11==> " + storedPassword);
                    // Hash the stored password with SHA-256
                    storedPassword = SHA256.encrypt(storedPassword);
                }
                System.out.println("{Ecom] getAgencyLoginDet storedPassword==> " + storedPassword + " password==> " + password);
                if (storedPassword != null && storedPassword.equals(password)) {
                    loginname = rst.getString("username");
                    logintype = rst.getString("usertype");
                    userid = rst.getString("userid");
                } else {
                    loginname = "";
                    logintype = "Invalid Username / Password";
                }

            } else {
                loginname = "";
                 logintype = getForwardMessage("E-COM", "LOGINMSG", "C");
                // logintype = "Invalid Username / Password";
            }
            arry.add(loginname);
            arry.add(logintype);
            arry.add(userid);
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
        return arry;
    }

    @Override
    public ArrayList getvendormodules(String columnValue) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String customerList = null;
        ArrayList column = new ArrayList();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(vendormodulesQry, 1004, 1007);
            rs = ps.executeQuery();
            while (rs.next()) {
                customerList = rs.getString(1);
                column.add(customerList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return column;
    }

    @Override
    public ArrayList getvendormoduleslist(String username) throws RemoteException {
        ArrayList mainMenus = new ArrayList();
        ArrayList Menus = new ArrayList();
        HashMap<String, ArrayList> submenu = new HashMap<String, ArrayList>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        RegisterMenuVO registerVo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(strMainAdminData, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuName(checkNull(rs.getString("mnuname")));
                registerVo.setCategory(checkNull(rs.getString("Category")));
                registerVo.setClassValue(checkNull(rs.getString("ClassValue")));
                registerVo.setRefNo(checkNull(rs.getString("RefNo")));
                registerVo.setLabelValue(checkNull(rs.getString("LabelValue")));
                registerVo.setAttrValue(checkNull(rs.getString("AttrValue")));
                registerVo.setMenuType(checkNull(rs.getString("MenuType")));
                registerVo.setParentMenu(checkNull(rs.getString("ParentMenu")));
                mainMenus.add(registerVo);
            }

            ps = con.prepareStatement(strSubAdminData, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuName(checkNull(rs.getString("mnuname")));
                registerVo.setCategory(checkNull(rs.getString("Category")));
                registerVo.setClassValue(checkNull(rs.getString("ClassValue")));
                registerVo.setLabelValue(checkNull(rs.getString("LabelValue")));
                registerVo.setRefNo(checkNull(rs.getString("RefNo")));
                registerVo.setAttrValue(checkNull(rs.getString("AttrValue")));
                registerVo.setMenuType(checkNull(rs.getString("MenuType")));
                registerVo.setParentMenu(checkNull(rs.getString("ParentMenu")));
                if (submenu != null && submenu.get(checkNull(rs.getString("ParentMenu"))) != null) {
                    submenu.get(checkNull(rs.getString("ParentMenu"))).add(registerVo);
                } else {
                    submenu.put(checkNull(rs.getString("ParentMenu")), new ArrayList());
                    submenu.get(checkNull(rs.getString("ParentMenu"))).add(registerVo);
                }
            }
            Menus.add(mainMenus);
            Menus.add(submenu);
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
        return Menus;
    }

    @Override
    public ArrayList getMenuDetails() throws RemoteException {
        ArrayList mainMenus = new ArrayList();
        ArrayList Menus = new ArrayList();
        HashMap<String, ArrayList> submenu = new HashMap<String, ArrayList>();
        HashMap<String, String> menuCategory = new HashMap<String, String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        RegisterMenuVO registerVo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(getMainMenuDetQRY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuId(rs.getInt("MenuId"));
                registerVo.setMenuName(checkNull(rs.getString("MenuName")));
                registerVo.setCategory(checkNull(rs.getString("Category")));
                registerVo.setClassValue(checkNull(rs.getString("ClassValue")));
                registerVo.setRefNo(checkNull(rs.getString("RefNo")));
                registerVo.setLabelValue(checkNull(rs.getString("LabelValue")));
                registerVo.setAttrValue(checkNull(rs.getString("AttrValue")));
                registerVo.setMenuType(checkNull(rs.getString("MenuType")));
                registerVo.setParentMenu(checkNull(rs.getString("ParentMenu")));
                mainMenus.add(registerVo);
                menuCategory.put(registerVo.getRefNo(), registerVo.getCategory());
            }

            ps = con.prepareStatement(getSubMenuDetQRY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuId(rs.getInt("MenuId"));
                registerVo.setMenuName(checkNull(rs.getString("MenuName")));
                registerVo.setCategory(checkNull(rs.getString("Category")));
                registerVo.setClassValue(checkNull(rs.getString("ClassValue")));
                registerVo.setLabelValue(checkNull(rs.getString("LabelValue")));
                registerVo.setRefNo(checkNull(rs.getString("RefNo")));
                registerVo.setAttrValue(checkNull(rs.getString("AttrValue")));
                registerVo.setMenuType(checkNull(rs.getString("MenuType")));
                registerVo.setParentMenu(checkNull(rs.getString("ParentMenu")));
                if (submenu != null && submenu.get(checkNull(rs.getString("ParentMenu"))) != null) {
                    submenu.get(checkNull(rs.getString("ParentMenu"))).add(registerVo);
                } else {
                    submenu.put(checkNull(rs.getString("ParentMenu")), new ArrayList());
                    submenu.get(checkNull(rs.getString("ParentMenu"))).add(registerVo);
                }
                menuCategory.put(registerVo.getRefNo(), registerVo.getCategory());
            }
            Menus.add(mainMenus);
            Menus.add(submenu);
            Menus.add(menuCategory);
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
        return Menus;
    }

    @Override
    public ArrayList getRegMenuDetails() throws RemoteException {
        ArrayList RegisteredMenus = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        RegisterMenuVO registerVo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(RegMenuDetailsQry, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            while (rs.next()) {
                registerVo = new RegisterMenuVO();
                registerVo.setMenuId(rs.getInt("MenuId"));
                registerVo.setMenuName(rs.getString("MenuName"));
                registerVo.setCategory(rs.getString("Category"));
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
    public UserData getActiveDetails(String username, String password, boolean isenabledEncrypt) throws RemoteException {
        UserData userDataObj = new UserData();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            System.out.println("[ECOM] EcomRegistrationBean ====>username" + username + "====>password" + password + "==>" + CHECK_USER_INPUT);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECK_USER_INPUT, 1004, 1007);
            ps.setString(1, username);
            //ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                CryptoLibrary crypt = new CryptoLibrary();
                if (isenabledEncrypt) {
                    password = crypt.decrypt(password);
                    storedPassword = crypt.decrypt(storedPassword);
                    //System.out.println("storedPassword 11==> " + storedPassword);
                    // Hash the stored password with SHA-256
                    storedPassword = SHA256.encrypt(storedPassword);
                }
                System.out.println("[Ecom] EcomRegistrationBean storedPassword==> " + storedPassword + " password==> " + password);
                if (storedPassword != null && storedPassword.equals(password)) {
                    userDataObj.setResData("VALID");
                    userDataObj.setCcMailid(rs.getString("ccmailid"));
                    userDataObj.setTimezone(rs.getString("preferredTimezone"));
                    userDataObj.setLastpwdup(rs.getString("lastpwdup"));//
                    userDataObj.setStatus(rs.getString("status"));
                } else {
                    System.out.println("Else storedPassword");
                    userDataObj.setResData("Invalid Username / Password");
                }
            } else {
                
                String errorMessage = getForwardMessage("E-COM", "LOGINMSG", "C");
                userDataObj.setResData("Account_activated_soon");
             
                System.out.println("Else storedPassword errorMessage==> " + errorMessage);
            }
            System.out.println("[ECOM] EcomRegistrationBean #>>GETActiveDetails<<<##" + userDataObj.getResData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDataObj;
    }

    @Override
    public UserData getUserDetailsFromCustomermaster(String username, String password) throws RemoteException {
        UserData userDataObj = new UserData();
        String strChkIncative = "N";
        List<CustomerContacts> custcontactlst = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecommcustomercontact(username);
            System.out.println("[ECOM] EcomRegistrationBean %%>>>hs" + hs);
            custcontactlst = gson.fromJson(gson.toJson((ArrayList) hs.get("customercontacts")), new TypeToken<List<CustomerContacts>>() {
            }.getType());
            if (custcontactlst.size() > 0) {
                CustomerContacts rs = (CustomerContacts) custcontactlst.get(0);
                userDataObj.setLoginName(checkNull(rs.getWebid()));
                userDataObj.setUserName(checkNull(rs.getContactname()));
                userDataObj.setEmail(checkNull(rs.getEmail()));
                userDataObj.setSystemType("C");   // need to confirm to remove
                userDataObj.setUsertype("C");     // need to confirm to remove
                userDataObj.setUsercode(checkNull(rs.getCustcode()));
                userDataObj.setPhone(checkNull(rs.getPhone()));
                userDataObj.setCountry(checkNull(rs.getCountryid()));
                userDataObj.setAdmin(checkNull(rs.getAdmin()));
                userDataObj.setCusttype(checkNull(rs.getCusttype()));
                userDataObj.setResData("VALID");
                userDataObj.setContantname(checkNull(rs.getCustname()));
                strChkIncative = checkNull(rs.getOnlineactive());
                userDataObj = getUserLangauge(userDataObj, checkNull(rs.getWebid()));
            } else {
                userDataObj.setResData("Invalid Username / Password");
            }
            if ("V".equalsIgnoreCase(strChkIncative)) {
                userDataObj.setResData("Webid is InActive,Please Contact Administrator");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EcomRegistrationBean %%%%>>>" + userDataObj.getResData());
        return userDataObj;
    }

    private UserData getUserLangauge(UserData userDataObj, String loginname) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(SELECT_USERLANGAUAGE_FROMECOMDB, 1004, 1007);
            ps.setString(1, loginname);
            rs = ps.executeQuery();
            while (rs.next()) {
                userDataObj.setUserpreflanguage(checkNull(rs.getString("prefLang")));  // need to confirm to remove
                userDataObj.setRoll(checkNull(rs.getString("userrole"))); // IsAdmin
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
        return userDataObj;
    }

    //
    @Override
    public String getalloweduser(String usercode) throws RemoteException {

        String value = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomallowedusercount(usercode);
            List lst = (List) hs.get("customermaster");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                value = (String) rs.get("allowedusers");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public String getAPNAME(String usercode) throws RemoteException {

        String returnvalue = "";
        Gson gson = new Gson();
        try {
            Map hs = (Map) ecomvdsservice.getecomapdetails(usercode);
            List<CustomerMasterVo> lst = gson.fromJson(gson.toJson((ArrayList) hs.get("customermaster")), new TypeToken<List<CustomerMasterVo>>() {
            }.getType());
            if (lst.size() > 0) {
                CustomerMasterVo rs = (CustomerMasterVo) lst.get(0);
                returnvalue = rs.getCustname();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    @Override
    public UserData getUserDetailsforAdmin(String usercode) throws RemoteException {
        UserData userDataObj = new UserData();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null, rs1 = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(strUserDataAdminQRY, 1004, 1007);
            ps.setString(1, usercode);
            rs = ps.executeQuery();
            if (rs.next()) {
                userDataObj.setLoginName(checkNull(rs.getString("loginname")));
                userDataObj.setFirstName(checkNull(rs.getString("First_Name")));
                userDataObj.setLastName(checkNull(rs.getString("last_name")));
                userDataObj.setUserName(checkNull(rs.getString("UserName")));
                userDataObj.setEmail(checkNull(rs.getString("user_Email")));
                userDataObj.setSystemType(checkNull(rs.getString("systemtype")));
                userDataObj.setUsertype(checkNull(rs.getString("userType")));
                userDataObj.setUsercode(checkNull(rs.getString("cust_code")));
                userDataObj.setResData("VALID");
                userDataObj.setUserpreflanguage(checkNull(rs.getString("preflang")));

                ps = con.prepareStatement("select emailid,ccmailid,preferredTimezone from ecomuserdetails where CustomerCode=?", 1004, 1007);
                ps.setString(1, usercode);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    userDataObj.setResData("VALID");
                    userDataObj.setCcMailid(rs1.getString("ccmailid"));
                    userDataObj.setTimezone(rs1.getString("preferredTimezone"));
                    System.out.println("[ECOM] EcomRegistrationBean preferredTimezone " + rs1.getString("preferredTimezone"));
                }
            } else {
                userDataObj.setResData("Invalid Username / Password");
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
        }
        return userDataObj;
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
            System.out.println("[ECOM] EcomRegistrationBean ==CustId====>>>" + CustId);
            conn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pst = conn.prepareStatement(CustMenuDetailsQry);
            pst.setString(1, CustId);
            rst = pst.executeQuery();
            while (rst.next()) {
                menuid = String.valueOf(rst.getString("menuid"));
                result = "(" + setDBConst(" ", menuid, "_") + ")";
                pst = conn.prepareStatement(CustMenuDetailsQry1);
                pst.setString(1, result);
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
            pst = conn.prepareStatement(CustMenuDetailsQry2);
            pst.setString(1, result);
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
    public CustomerProfile getuserProfile(String strloginame) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        CustomerProfile profileVO = new CustomerProfile();
        String strcompname = "";
        String strcustcode = "";
        String strusername = "";
        String stremail = "";
        String straddress = "";
        String strcity = "";
        String strpostal = "";
        String strstate = "";
        String stragency = "";
        String strcompphone = "";
        String struserphone = "";
        String strusermob = "";
        String struserfax = "";
        String strcountry = "";
        String strdeflang = "";
        String strpreflang = "";
        String address1 = "";
        String address2 = "", ccMailId = null, timeZone = null;
        List<CustomerContacts> custcontactlst = null;
        Gson gson = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETCCMAILID);
            ps.setString(1, strloginame);
            rst = ps.executeQuery();
            if (rst.next()) {
                ccMailId = rst.getString("ccmailid");
                timeZone = rst.getString("preferredTimezone");
            }
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecommcustomercontact(strloginame);
            System.out.println("[ECOM] EcomRegistrationBean $$$$getecommcustomercontact$>>>" + hs);
            custcontactlst = gson.fromJson(gson.toJson((ArrayList) hs.get("customercontacts")), new TypeToken<List<CustomerContacts>>() {
            }.getType());
            if (custcontactlst.size() > 0) {
                CustomerContacts rs = (CustomerContacts) custcontactlst.get(0);

                if (rs.getCustcode() != null) {
                    strcustcode = rs.getCustcode();
                } else {
                    strcustcode = "";
                }
                if (rs.getContactname() != null) {
                    strusername = rs.getContactname();
                } else {
                    strusername = "";
                }

                if (rs.getEmail() != null) {
                    stremail = rs.getEmail();
                } else {
                    stremail = "";
                }

                if (rs.getPhone() != null) {
                    struserphone = rs.getPhone();
                    if ("--".equalsIgnoreCase(struserphone)) {
                        struserphone = "-";
                    }
                } else {
                    struserphone = "";
                }

                if (rs.getMobile() != null) {
                    strusermob = rs.getMobile();
                } else {
                    strusermob = "";
                }

                if (rs.getFax() != null) {
                    struserfax = rs.getFax();
                    if ("--".equalsIgnoreCase(struserfax)) {
                        struserfax = "-";
                    }
                } else {
                    struserfax = "";
                }

                hs = (Map) ecomvdsservice.getecomapdetails(strcustcode);
                System.out.println("[ECOM] EcomRegistrationBean $$$$getecommcustomercontact$>>>" + hs);
                List<CustomerMasterVo> lst = gson.fromJson(gson.toJson((ArrayList) hs.get("customermaster")), new TypeToken<List<CustomerMasterVo>>() {
                }.getType());
                if (lst.size() > 0) {
                    CustomerMasterVo cus = (CustomerMasterVo) lst.get(0);
                    if (cus.getCustname() != null) {
                        strcompname = cus.getCustname();
                    } else {
                        strcompname = "";
                    }
                    if (cus.getPhone() != null) {
                        strcompphone = cus.getPhone();
                    } else {
                        strcompphone = "";
                    }

                    if (cus.getState() != null) {
                        strstate = cus.getState();
                    } else {
                        strstate = "";
                    }

                    if (cus.getCountryid() != null) {
                        strcountry = cus.getCountryid();
                    } else {
                        strcountry = "";
                    }
                    if (cus.getCity() != null) {
                        strcity = cus.getCity();
                    } else {
                        strcity = "";
                    }
                    if (cus.getPobox() != null) {
                        strpostal = cus.getPobox();
                    } else {
                        strpostal = "";
                    }
                    if (cus.getAddress1() != null) {
                        address1 = cus.getAddress1();
                    } else {
                        address1 = "";
                    }
                    if (cus.getAddress2() != null) {
                        address2 = cus.getAddress2();
                    } else {
                        address2 = "";
                    }
                }
                profileVO.setCustId(strcustcode);
                profileVO.setContactName(strusername);
                profileVO.setCustomerName(strcompname);
                profileVO.setAddr1(address1);
                profileVO.setCity(strcity);
                profileVO.setState(strstate);
                profileVO.setCountry(strcountry);
                profileVO.setPobox(strpostal);
                profileVO.setTeleNo(strcompphone);
                profileVO.setUserPhoneNo(struserphone);
                profileVO.setMobileNo(strusermob);
                profileVO.setFax(struserfax);
                profileVO.setEmailId(stremail);
                profileVO.setAgency(stragency);
                profileVO.setDefaultLamg(strdeflang);
                profileVO.setPreferedLang(strpreflang);
                profileVO.setAddr2(address2);
                profileVO.setCcMailId(checkNull(ccMailId));
                profileVO.setTimezone(checkNull(timeZone));
                System.out.println("[ECOM] EcomRegistrationBean ccmailinuserprofile " + profileVO.getCcMailId() + " timezoneinuserprofile " + profileVO.getTimezone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
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
        return profileVO;
    }

    @Override
    public ReturnVO getResetPasswordDetail(String strLoginName, String strEmail) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReturnVO retVo = null;
        String firstName = "";
        String outPut = "";
        String email = "";
        String passwordOG = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(ResetPasswordDetailQRY, 1004, 1007);
            ps.setString(1, strLoginName);
            rs = ps.executeQuery();
            while (rs.next()) {
                outPut = checkNull(rs.getString("password"));
                firstName = checkNull(rs.getString("first_name"));
                email = checkNull(rs.getString("user_email"));
            }
            if (outPut != null) {
                if (!outPut.toString().trim().equalsIgnoreCase("null")) {
                    if (!outPut.toString().trim().equalsIgnoreCase("")) {
                        CryptoLibrary crypto = new CryptoLibrary();
                        passwordOG = crypto.decrypt(outPut);
                    }
                }
            }
            MailFormatVO mailFormatObj = new MailFormatVO();
            retVo = new ReturnVO();
            retVo.setStrDisplayMsg(passwordOG);
            mailFormatObj = getMailContent("PasswordReset", "Update", "C");
            String msg = mailFormatObj.getStrMailContent();
            String clientName = mailFormatObj.getStrRegard();
            String support = mailFormatObj.getStrFromAddr();
            msg = msg.replace("@Firstname", firstName);
            msg = msg.replace("@client", clientName);
            msg = msg.replace("@password", passwordOG);
            msg = msg.replace("@support", support);
            msg = msg.replace("@loginname", strLoginName);
            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());
            retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            retVo.setStrToMailId(email);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retVo;
    }

    @Override
    public ReturnVO getResetPasswordDetailForIncompletedReg(String strLoginName) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReturnVO retVo = null;
        String firstName = "";
        String outPut = "";
        String email = "";
        String passwordOG = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_REG_DETAILS, 1004, 1007);
            ps.setString(1, strLoginName);
            rs = ps.executeQuery();
            while (rs.next()) {
                outPut = checkNull(rs.getString("password"));
                firstName = checkNull(rs.getString("first_name"));
                email = checkNull(rs.getString("user_email"));
            }
            if (outPut != null) {
                if (!outPut.toString().trim().equalsIgnoreCase("null")) {
                    if (!outPut.toString().trim().equalsIgnoreCase("")) {
                        CryptoLibrary crypto = new CryptoLibrary();
                        passwordOG = crypto.decrypt(outPut);
                    }
                }
            }
            MailFormatVO mailFormatObj = new MailFormatVO();
            retVo = new ReturnVO();
            retVo.setStrDisplayMsg(passwordOG);
            mailFormatObj = getMailContent("PasswordReset", "Update", "C");
            String msg = mailFormatObj.getStrMailContent();
            String clientName = mailFormatObj.getStrRegard();
            String support = mailFormatObj.getStrFromAddr();
            msg = msg.replace("@Firstname", firstName);
            msg = msg.replace("@client", clientName);
            msg = msg.replace("@password", passwordOG);
            msg = msg.replace("@support", support);
            msg = msg.replace("@loginname", strLoginName);
            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());
            retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            retVo.setStrToMailId(email);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retVo;
    }

    @Override
    public ReturnVO getForgotPasswordDetail(String strLoginName, String strEmail, String strFirstname, String strType, String temp_Password_For_ForgotPWD) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReturnVO retVo = null;
        String firstName = "";
        String outPut = "";
        String email = "";
        String saveType = "";
        String passwordOG = "";
        String randomPassword = "";
        String firstName2let = "";
        String newPass = "";
        String newPassDecrypt = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if ("forgotpassword".equalsIgnoreCase(strType)) {
                saveType = "forgotpassword";
                ps = con.prepareStatement(forgetPasswordQry, 1004, 1007);
                ps.setString(1, strLoginName);
                rs = ps.executeQuery();
                while (rs.next()) {
                    outPut = checkNull(rs.getString("password"));
                    firstName = checkNull(rs.getString("first_name"));
                    email = checkNull(rs.getString("user_email"));
                }
                if (outPut != null) {
                    if (!outPut.toString().trim().equalsIgnoreCase("null")) {
                        if (!outPut.toString().trim().equalsIgnoreCase("")) {
                            CryptoLibrary crypto = new CryptoLibrary();
                            passwordOG = crypto.decrypt(outPut);
                            randomPassword = String.valueOf(get5DigitRandomNumber());
                            if (firstName.length() >= 2) {
                                firstName2let = firstName.substring(0, 2);
                            } else {
                                firstName2let = checkNull(strLoginName).substring(0, 2);
                            }
                            String alphabet = "@#$&*^!";
                            Random rd = new Random();
                            firstName2let = firstName2let + randomPassword + alphabet.charAt(rd.nextInt(alphabet.length()));
                            firstName2let = firstName2let.substring(0, 1).toUpperCase() + firstName2let.substring(1, firstName2let.length());
                            newPass = crypto.encrypt(firstName2let);
                            newPassDecrypt = crypto.decrypt(newPass);
                            ps = con.prepareStatement(forgetPasswordUpdate, 1004, 1007);
                            ps.setString(1, newPass);
                            ps.setString(2, (("Y".equalsIgnoreCase(temp_Password_For_ForgotPWD)) ? "F" : "A"));
                            ps.setString(3, strLoginName);
                            ps.executeUpdate();
                        }
                    }
                }

                MailFormatVO mailFormatObj = new MailFormatVO();
                retVo = new ReturnVO();
                retVo.setStrDisplayMsg(newPass);
                retVo.setStrSaveType(saveType);

                mailFormatObj = getMailContent("ForgotPasswordNew", "Update", "C");

                String msg = mailFormatObj.getStrMailContent();
                String clientName = mailFormatObj.getStrRegard();
                String support = mailFormatObj.getStrFromAddr();
                msg = msg.replace("@Firstname", firstName);
                msg = msg.replace("@client", clientName);
                msg = msg.replace("@password", newPassDecrypt);
                msg = msg.replace("@support", support);
                retVo.setStrMailMsg(msg);
                retVo.setStrSubject(mailFormatObj.getStrSubject());
                retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                retVo.setStrToMailId(email);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retVo;
    }

    @Override
    public String checkAdminPassword(String username, String password) throws RemoteException {
        String result = "invalid";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String pwd = null;

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECK_ADMIN_PWD, 1004, 1007);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                pwd = checkNull(rs.getString("password"));
                if (pwd.length() > 0 && pwd.equals(password)) {
                    result = "valid";
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
        return result;
    }

    @Override
    public String resetAdminPassword(String username, String password, String oldPassword) throws RemoteException {
        String result = "fail";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String pwd = null;
        int i = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECK_ADMIN_PWD, 1004, 1007);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                pwd = checkNull(rs.getString("password"));
                if (pwd.length() > 0 && pwd.equals(oldPassword)) {
                    ps = con.prepareStatement(UPDATE_ADMIN_PWD, 1004, 1007);
                    ps.setString(1, password);
                    ps.setString(2, username);
                    i = ps.executeUpdate();
                }
            }
            if (i > 0) {
                result = "valid";
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
        return result;
    }

    public List getAdminLoginDetails(String username, String password) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdminCustomerVo admin = null;
        List<AdminCustomerVo> lstadmin = null;
        lstadmin = new ArrayList<AdminCustomerVo>();
        String valid = "N";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
//            ps = con.prepareStatement(strAdminLoginData, 1004, 1007);
//            ps.setString(1, username);
//            ps.setString(2, password);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                valid = "Y";
//            }
//            if (valid.equalsIgnoreCase("Y")) {
            ps = con.prepareStatement(AdmncustQry);
            rs = ps.executeQuery();
            while (rs.next()) {
                admin = new AdminCustomerVo();
                admin.setCustcode(rs.getString("cust_code"));
                admin.setCompname(rs.getString("comp_name"));
                lstadmin.add(admin);
            }
            //}
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
        return lstadmin;
    }

    @Override
    public String checkCompanyDetails(String custCode, String countryCode) throws RemoteException {
        String result = "Invalid";
        int allowedUserCount = 0, contactCount = 0;
        Map map = null;
        List list = null;
        try {
            map = (Map) regVDSService.getEcomCheckCompany(custCode, countryCode);
            System.out.println("[ECOM] EcomRegistrationBean checkcomapny map>>" + map);
            list = (List) map.get("customermaster");
            if (list != null && list.size() > 0) {
                map = (Map) list.get(0);
                allowedUserCount = Integer.parseInt((String) map.get("allowedusers"));
                result = "Valid";
                map = (Map) regVDSService.getEcomContactCount(custCode);
                System.out.println("[ECOM] EcomRegistrationBean >>check customer contact count>>>" + map);
                list = (List) map.get("customercontacts");
                if (list != null && list.size() > 0) {
                    map = (Map) list.get(0);
                    contactCount = Integer.parseInt((String) map.get("count"));
                }
                if (contactCount >= allowedUserCount) {
                    result = String.valueOf(allowedUserCount);
                }
            }
            System.out.println("[ECOM] EcomRegistrationBean >checkCompanyDetails>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String updateCCMailId(String mailId, String ccMailId) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Updated";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATECCMAILID, 1004, 1007);
            ps.setString(1, checkNull(ccMailId));
            ps.setString(2, mailId);
            ps.executeUpdate();
        } catch (Exception e) {
            result = "Not Updated";
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
    public String updateTimeZone(String mailId, String timeZone) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Updated";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATETIMEZONEVALUE, 1004, 1007);
            ps.setString(1, checkNull(timeZone));
            ps.setString(2, mailId);
            ps.executeUpdate();
        } catch (Exception e) {
            result = "Not Updated";
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

    public int get5DigitRandomNumber() {
        int upperLimit = 99999;
        int lowerLimit = 10000;

        int s = (int) (Math.random() * (upperLimit - lowerLimit + 1) + lowerLimit);
        if (s < lowerLimit) {
            s += lowerLimit;
        }
        return s;
    }

    public int getRandomNumber(int maxLimit) {
        return (int) (Math.random() * maxLimit);
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getAgencyMail(String agencycode) {
        String agencymail = "";
        Map hs = null;
        List lis = null;
        try {
            hs = (Map) regVDSService.getEcomRegAgencyMasterMail(agencycode);
            lis = (List) hs.get("agency");
            if (lis != null && lis.size() > 0) {
                hs = (Map) lis.get(0);
                agencymail = (String) hs.get("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agencymail;
    }

    @Override
    public ArrayList getTimeZone() throws RemoteException {
        ArrayList result = new ArrayList();
        List<DropDownVO> main = new ArrayList<>();
        DropDownVO ddVO = null;
        HashMap map = new HashMap();
        try {
            ddVO = new DropDownVO();
            ddVO.setCode("");
            ddVO.setName("Select TimeZone");
            main.add(ddVO);
            Map hs = (Map) regVDSService.getTimeZoneValues();
            //////
            List lst = (List) hs.get("usertimezone");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    ddVO = new DropDownVO();
                    map.put(rs.get("location"), rs.get("locationdisplay"));
                    ddVO.setCode((String) rs.get("location"));
                    ddVO.setName((String) rs.get("locationdisplay"));
                    main.add(ddVO);
                }
            }
            result.add(main);
            result.add(map);
            System.out.println("[ECOM] EcomRegistrationBean timezoneeeeeeeeeeemap" + result);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception Caught On gettimzoneDetails==>" + e);
        }
        return result;
    }

    @Override
    public RegistrationVO getCompanyDetails(String custcode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String details = "";
        String custcodeval = checkNull(custcode);
        String agencyname = "";
        String city = "";
        String statecode = "";
        String statedesc = "";
        String pincode = "";
        String phone = "";
        String areacode = "";
        String phone_countrycode = "";
        String country = "";
        String address1 = "";
        String address2 = "";
        String countrycode = "";
        String agencycode = "";
        String companydomain = "";
        String other_city = ""; // PRP- 3284
        String companyname = "";
        RegistrationVO regVo = new RegistrationVO();
        String status = "INVALID";
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement(GET_COMPY_DETAILS);
            ps.setString(1, custcodeval);

            rs = ps.executeQuery();
            while (rs.next()) {
                status = "VALID";
                companyname = checkNull(rs.getString("CustName"));
                agencyname = checkNull(rs.getString("agencyname"));
                address1 = checkNull(rs.getString("Address1"));
                address2 = checkNull(rs.getString("Address2"));
                pincode = checkNull(rs.getString("ZipCode"));
                city = checkNull(rs.getString("city")).toUpperCase();
                statecode = checkNull(rs.getString("statecode"));
                statedesc = checkNull(rs.getString("statedesc"));
                phone = checkNull(rs.getString("Phone"));
                country = checkNull(rs.getString("CountryName"));
                custcode = checkNull(rs.getString("CustCode"));
                statecode = checkNull(rs.getString("statecode"));
                countrycode = checkNull(rs.getString("countryid"));
                agencycode = checkNull(rs.getString("agencycode")); //Stagging bug for PRP-1751
                other_city = checkNull(rs.getString("otherscity")); // others as value in city drop down list PRP-3284
                companydomain = "";
                regVo.setHiddencustname(companyname);
                regVo.setCompanyId(custcode);
                regVo.setCountrycode(countrycode);
                regVo.setAgencycode(agencycode);
                regVo.setCompany_address(address1);
                regVo.setCompany_address1(address2);
                regVo.setCompany_postcode(pincode);
                regVo.setCity_desc(city);
                regVo.setCompany_state(statecode);
                regVo.setOther_city(other_city); // PRP-3284
                regVo.setCompany_domain(companydomain);
            }
            if (phone.length() > 0 && phone.contains("-")) {
                String p[] = phone.split("-");
                if (p.length == 3) {
                    phone_countrycode = p[0];
                    areacode = p[1];
                    phone = p[2];
                    phone_countrycode = checkNull(phone_countrycode).replace("+", "");
                }
            }
            regVo.setCompany_country_code(phone_countrycode);
            regVo.setCompany_phone_code(areacode);
            regVo.setCompany_phone_no(phone);
            address1 = address1.replace("~~", "");
            address2 = address2.replace("~~", "");
            details = custcode + "~~" + country + "~~" + countrycode + "~~" + agencyname + "~~" + agencycode + "~~" + address1 + "~~" + address2 + "~~"
                    + pincode + "~~" + city + "~~" + statedesc + "~~" + statecode + "~~" + phone_countrycode + "~~"
                    + areacode + "~~" + phone + "~~" + other_city + "~~" + companyname + "~~" + status + "~~" + companydomain;
            regVo.setCompanyDetails(details);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Error While Calling getCompanyDetails" + e.getMessage());
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
        return regVo;
    }

    private String getConfiguredMenu() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            System.out.println("[ECOM] EcomRegistrationBean Inside getConfiguredMenu");
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement("select pathvalue from globalconfigproduct where pathname='auto_mapping_menu'");
            rs = ps.executeQuery();
            if (rs.next()) {
                String menuid = checkNull(rs.getString("pathvalue"));
                System.out.println("[ECOM] EcomRegistrationBean menuid");
                if (menuid.length() > 0) {
                    String menurefid = "";
                    menurefid = "'" + menuid.replace(",", "','") + "'";
                    /*ArrayList<String> al = new ArrayList<String>(Arrays.asList(menuid.split(",")));
                    for (int i = 0; i < menuid.length(); i++) {
                        if (i == 0) {
                            menurefid = "'" + menurefid + "'";
                        } else {
                            menurefid = ",'" + menurefid + "'";
                        }
                    }*/
                    System.out.println("[ECOM] EcomRegistrationBean menurefid=> " + menurefid);
                    String sql = "select menuid from menuregistration where refno in(" + menurefid + ") and menustatus='A' and category='P'";
                    int menuCount = 0;
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (menuCount == 0) {
                            menuid = checkNull(rs.getString("menuid")).trim();
                        } else {
                            menuid = menuid + "_" + checkNull(rs.getString("menuid")).trim();
                        }
                        menuCount++;
                    }
                    result = menuid;
                    System.out.println("[ECOM] EcomRegistrationBean result=> " + result);
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
        return result;
    }

    public void updatelogindate(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATELOGINDATE, 1004, 1007);
            ps.setString(1, email);
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

    public void updatelogoutdate(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATELOGOUTDATE, 1004, 1007);
            ps.setString(1, email);
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

    public void updateWrgPasAtmp(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(UPDATEWRONGPASSATTEMPT, 1004, 1007);
            ps.setString(1, email);

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

    public void resetWrgPasAtmp(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(RESETWRONGPASSATTEMPT, 1004, 1007);
            ps.setString(1, email);

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

    public void lockWrgPasAcct(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(LOCKWRONGPASSACCOUNT, 1004, 1007);
            ps.setString(1, email);

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

    public UserData getWrgPasslogin(String email) throws RemoteException {
        UserData userDataObj = new UserData();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(GET_WRG_LOGIN_DETAILS);

            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                userDataObj.setWrgPsAtmpt(rs.getInt("WrgPassAtmpt"));
                userDataObj.setlStatus(rs.getString("LockedStatus"));
                userDataObj.setlTime(rs.getString("LockedTime"));
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
        return userDataObj;
    }

    public UserData getIncompletedReglogin(String username, String password, boolean isenabledEncrypt) throws RemoteException {
        UserData userDataObj = new UserData();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("[ECOM] EcomRegistrationBean ====>username" + username + "====>password" + password + "==>" + CHECK_USER_INPUT);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_REG_DETAILS, 1004, 1007);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                String isValid = rs.getString("isValid");
                if("M".equalsIgnoreCase(isValid) || "P".equalsIgnoreCase(isValid)) {
                String storedPassword = rs.getString("password");
                CryptoLibrary crypt = new CryptoLibrary();
                if (isenabledEncrypt) {
                    password = crypt.decrypt(password);
                    storedPassword = crypt.decrypt(storedPassword);
                    storedPassword = SHA256.encrypt(storedPassword);
                }
                System.out.println("[Ecom] EcomRegistrationBean storedPassword==> " + storedPassword + " password==> " + password);
                if (storedPassword != null && storedPassword.equals(password)) {
                    userDataObj.setResData("VALID");
                    userDataObj.setLoginName(rs.getString("User_Email"));
                    userDataObj.setTimezone(rs.getString("preferredTimezone"));
                    userDataObj.setStatus(rs.getString("status"));
                    userDataObj.setContantname(rs.getString("Title") + rs.getString("First_Name") + " " + rs.getString("Last_Name"));
                    userDataObj.setUserName(rs.getString("First_Name") + " " + rs.getString("Last_Name"));
                    userDataObj.setEmail(rs.getString("User_Email"));
                    userDataObj.setIsValid(isValid);
                    userDataObj.setPhone(rs.getString("User_Phone"));
                    userDataObj.setCountry(rs.getString("country"));
                    userDataObj = getUserLangauge(userDataObj, checkNull(rs.getString("User_Email")));
                } else {
                    System.out.println("Else storedPassword");
                    userDataObj.setResData("Invalid Username / Password");
                }
            }
        }
            System.out.println("[ECOM] EcomRegistrationBean #>>GETActiveDetails<<<##" + userDataObj.getResData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDataObj;
    }

    @Override
    public ArrayList<RegistrationVO> getCountryFileDetails(String country) throws RemoteException {
        System.out.println("[ECOM] EcomRegistrationBean getCountryFileDetails=================");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RegistrationVO> countryfileuploadlist = new ArrayList<>();

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_COUNTRY_FILE_DETAILS);
            ps.setString(1, country);
            rs = ps.executeQuery();

            while (rs.next()) {
                RegistrationVO regVo = new RegistrationVO();
                regVo.setFieldname(rs.getString("fieldname"));
                regVo.setMandatory(rs.getString("mandatory"));
                countryfileuploadlist.add(regVo);

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
        return countryfileuploadlist;

    }

    @Override
    public String getfileuploadsharepath() throws RemoteException {
        System.out.println("[ECOM] EcomRegistrationBean getfileuploadsharepath=================");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sharepath = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement(GET_FILE_UPLOAD_PATH);
            rs = ps.executeQuery();
            if (rs.next()) {
                sharepath = checkNull(rs.getString("value"));
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
        return sharepath;

    }

    @Override
    public ReturnVO getReqActivateActDeatails(String email) throws RemoteException {
        ReturnVO rvo = new ReturnVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String isValid = null, agencyCode = null, cust_code = null;
        int rowAffect = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(GETREQFORACTIVATEACCOUNTDETAILS);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                cust_code = rs.getString("Cust_Code");
                isValid = rs.getString("isValid");
                agencyCode = rs.getString("agencycode");
            }
            if ("E".equalsIgnoreCase(isValid)) {
                ps = con.prepareStatement(PUTREQFORACTIVATEACCOUNT, 1004, 1007);
                ps.setString(1, email);
                rowAffect = ps.executeUpdate();
                if (rowAffect > 0) {
                    MailFormatVO mailFormatObj = new MailFormatVO();
                    mailFormatObj = getMailContent("Registration", "Reactivate", "C");
                    String msg = mailFormatObj.getStrMailContent();
                    msg = msg.replace("@loginname", email);
                    msg = msg.replace("@custcode", cust_code);
                    rvo.setStrMailMsg(msg);
                    rvo.setStrSubject(mailFormatObj.getStrSubject());
                    rvo.setStrToMailId(checkNull(getAgencyMailCon(agencyCode)));
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
        return rvo;
    }

    @Override
    public String getCustCompanyName(String custcode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String details = "";
        String custcodeval = checkNull(custcode);
        String companyname = "", cityname = "";
        String status = "INVALID";
        System.out.println("[ECOM] EcomRegistrationBean custcode=>" + custcode);
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement(GET_COMPY_CITY_DETAIL);
            ps.setString(1, custcodeval);
            rs = ps.executeQuery();
            while (rs.next()) {
                status = "VALID";
                companyname = checkdetailName(rs.getString("CustName").toUpperCase());
                cityname = checkdetailName(rs.getString("city")).toUpperCase();
            }

            details = custcode + "~~" + status + "~~" + companyname + "~~" + cityname;
            System.out.println("[ECOM] EcomRegistrationBean details==>" + details);
        } catch (Exception e) {
//            System.out.println("[ECOM] EcomRegistrationBean Error While Calling getCompanyDetails" + e.getMessage());
            throw new RemoteException("Error processing getCustCompanyName", e);
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
        return details;

    }

    private String checkdetailName(String str) {
        try {

            str = (str != null && (!"".equalsIgnoreCase(str)) && !"-".equalsIgnoreCase(str)) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public ReturnVO getDuplicateRegMail(RegistrationVO regVo) throws RemoteException {
        ReturnVO retVo = new ReturnVO();
        try {
            MailFormatVO mailFormatObj = new MailFormatVO();

            retVo.setStrSaveType("N");
            if ("en".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessage(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE)));
            } else if ("ch".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessageLang(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE, "ch")));

            } else if ("ma".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessageLang(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE, "ma")));

            } else if ("es".equalsIgnoreCase(regVo.getLangType())) {
                retVo.setStrDisplayMsg(checkNull(getForwardMessageLang(EcomConsts.REG_MSG_NEWCUST_MODULE, EcomConsts.REG_MSG_NEWCUST_CRITERIA, EcomConsts.REG_MSG_NEWCUST_USERTYPE, "es")));

            }
            mailFormatObj = getMailContent(EcomConsts.REG_MSG_EXISTINGCUST_MODULE, EcomConsts.REG_MSG_EXISTINGCUST_CRITERIA, EcomConsts.REG_MSG_EXISTINGCUST_USERTYPE);

            String msg = mailFormatObj.getStrMailContent();
            //
            // msg = msg.replaceAll(EcomConsts.REG_MSG_NEWCUST_CUSTOMER, regVo.getFirstname());
            //  msg = msg.replaceAll(EcomConsts.REG_MSG_NEWCUST_CLIENTID, mailFormatObj.getStrRegard());
            retVo.setStrMailMsg(msg);
            retVo.setStrSubject(mailFormatObj.getStrSubject());

            retVo.setStrToMailId(regVo.getLoginname());

            //  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVo;
    }

    @Override
    public ReturnVO insertPasswordResetToken(String email, String token) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ReturnVO retVo = new ReturnVO();
        ResultSet rs = null;
        try {
            // First expire any existing tokens for this email
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(EXPIRE_PASSWORD_RESET_TOKEN);
            ps.setString(1, email);
            ps.executeUpdate();

            // Insert new token
            ps = con.prepareStatement(INSERT_PASSWORD_RESET_TOKEN);
            ps.setString(1, email);
            ps.setString(2, token);
            int result = ps.executeUpdate();

            if (result > 0) {
                retVo.setStrName("SUCCESS");
                retVo.setStrDisplayMsg("Password reset token created successfully");
                MailFormatVO mailFormatObj = new MailFormatVO();

                retVo.setStrSaveType("forgotpassword");
                String firstName = "Customer";
                mailFormatObj = getMailContent("ForgotPassword", "ResetURL", "C");
                ps = con.prepareStatement(forgetPasswordQry, 1004, 1007);
                ps.setString(1, email);
                rs = ps.executeQuery();
                while (rs.next()) {
                    firstName = checkNull(rs.getString("first_name"));
                }
                String msg = mailFormatObj.getStrMailContent();
                msg = msg.replace("@Firstname", firstName);
                retVo.setStrMailMsg(msg);
                retVo.setStrSubject(mailFormatObj.getStrSubject());
                retVo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                retVo.setStrToMailId(email);
            } else {
                retVo.setStrName("FAILED");
                retVo.setStrDisplayMsg("Failed to create password reset token");
            }

        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in insertPasswordResetToken: " + e.getMessage());
            e.printStackTrace();
            retVo.setStrName("ERROR");
            retVo.setStrDisplayMsg("Error creating password reset token: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in insertPasswordResetToken: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return retVo;
    }

    @Override
    public PasswordResetTokenVo validatePasswordResetToken(String token) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PasswordResetTokenVo retVo = new PasswordResetTokenVo();

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_PASSWORD_RESET_TOKEN);
            ps.setString(1, token);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Populate all database values
                retVo.setId(rs.getInt("id"));
                retVo.setEmailID(rs.getString("EmailID"));
                retVo.setToken(rs.getString("token"));
                retVo.setRequestDate(rs.getString("RequestDate"));
                retVo.setCompleteDate(rs.getString("CompleteDate"));
                retVo.setStatus(rs.getString("Status"));

                // Check if token is not expired (30 minutes)
                java.sql.Timestamp requestDate = rs.getTimestamp("RequestDate");
                java.util.Date currentDate = new java.util.Date();
                long diffInMinutes = (currentDate.getTime() - requestDate.getTime()) / (60 * 1000);

                if (diffInMinutes <= 30) {
                    retVo.setStrName("VALID");
                    retVo.setStrDisplayMsg("Token is valid");
                    retVo.setStrRefNo(String.valueOf(rs.getInt("id"))); // Store the token ID
                } else {
                    // Token expired, update status
                    ps = con.prepareStatement(UPDATE_PASSWORD_RESET_TOKEN);
                    ps.setString(1, "E"); // Expired
                    ps.setString(2, String.valueOf(rs.getInt("id")));
                    ps.executeUpdate();

                    retVo.setStrName("EXPIRED");
                    retVo.setStrDisplayMsg("Password reset token has expired");
                }
            } else {
                retVo.setStrName("INVALID");
                retVo.setStrDisplayMsg("Invalid password reset token");
            }

        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in getPasswordResetToken: " + e.getMessage());
            e.printStackTrace();
            retVo.setStrName("ERROR");
            retVo.setStrDisplayMsg("Error validating password reset token: " + e.getMessage());
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
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in getPasswordResetToken: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return retVo;
    }

    @Override
    public String updatePasswordWithToken(String emailID, String newPassword, String tokenId) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";

        try {
            System.out.println("[Ecom] updatePasswordWithToken==> " + tokenId);
            // Encrypt the new password
            CryptoLibrary crypto = new CryptoLibrary();
            String encryptedPassword = crypto.encrypt(newPassword);

            // Update the password in ecomuserdetails table
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(UPDATE_PASSWORD_WITH_TOKEN);
            ps.setString(1, encryptedPassword);
            ps.setString(2, emailID);
            int updateResult = ps.executeUpdate();

            if (updateResult > 0) {
                // Mark token as completed
                ps = con.prepareStatement(UPDATE_PASSWORD_RESET_TOKEN);
                ps.setString(1, "C"); // Completed
                ps.setString(2, tokenId);
                ps.executeUpdate();

                result = "success";
            } else {
                result = "Failed to update password";
            }

        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in updatePasswordWithToken: " + e.getMessage());
            e.printStackTrace();
            result = "Error updating password: " + e.getMessage();
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
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in updatePasswordWithToken: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }
    @Override
    public String getAccountStatusMessage(String email ,String gateAgencyMailId) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        String errorMessage = "" ;
        String agencyEmail = null,agencyCode = null,status = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ACCOUNT_STATUS_MESSAGE);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = checkNull(rs.getString("isValid"));
                agencyCode = checkNull(rs.getString("agencycode"));
                status = checkNull(rs.getString("status"));
                if ("N".equalsIgnoreCase(result) || "M".equalsIgnoreCase(result) || "I".equalsIgnoreCase(result) || "P".equalsIgnoreCase(result)) {
                    errorMessage = getForwardMessage("E-COM", "LOGINMSG", "C");
                }
            }
            if("Y".equalsIgnoreCase(gateAgencyMailId) && checkNull(errorMessage).length() > 0){
                agencyEmail = checkNull(getAgencyMailCon(agencyCode));
                if(agencyEmail != null && agencyEmail.length() > 0){   
                    errorMessage= errorMessage +" at "+agencyEmail;
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in getAccountStatus: " + e.getMessage());
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
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in getAccountStatus: " + e.getMessage());
                e.printStackTrace();
            }
            
        }
        return errorMessage;
    }

    public void lockWrgPasAcctMovetoPendwithAgency(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(LOCKWRONGPASSACCOUNT, 1004, 1007);
            ps.setString(1, email);
            ps.executeUpdate();

            ps2 = con.prepareStatement(ACCOUNTMOVETOPENDWITHAGENCY, 1004, 1007);
            ps2.setString(1, email); 
            ps2.executeUpdate();
            
            System.out.println("[ECOM] EcomRegistrationBean lockWrgPasAcctMovetoPendwithAgency==> email: " + email); 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
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
    public String getAccountlockedMessage(String email) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        String errorMessage = null ;
        String agencyEmail = null,agencyCode = null,status = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ACCOUNT_STATUS_MESSAGE);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = checkNull(rs.getString("isValid"));
                status = checkNull(rs.getString("status"));
                if("L".equalsIgnoreCase(status) && "P".equalsIgnoreCase(result)){
                    errorMessage = getForwardMessage("E-COM", "LOGINFAIL", "C");
                } else {
                    errorMessage = "Account is locked. Please try again later.";
                }
            }
           
        } catch (Exception e) {
            System.out.println("[ECOM] EcomRegistrationBean Exception in getAccountStatus: " + e.getMessage());
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
                System.out.println("[ECOM] EcomRegistrationBean Exception while closing connection in getAccountStatus: " + e.getMessage());
                e.printStackTrace();
            }
            
        }
        return errorMessage;
    }
}
