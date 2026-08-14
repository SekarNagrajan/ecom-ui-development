package net.ecom.AppTierCntrlBean;

import javax.ejb.Remote;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.RegistrationVO;
import java.rmi.RemoteException;
import java.util.ArrayList;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.UserData;
import java.util.List;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.TimeZone;
import net.ecom.vo.UserCreationVO;
import net.ecom.vo.PasswordResetTokenVo;

@Remote
public interface EcomRegistrationBeanRemote {

    public ReturnVO saveToRegistration(RegistrationVO regVo, String saveType, String companyName, String KYC_fileupload) throws RemoteException;

    public MailFormatVO getMailContent(String module, String criteria, String usertype) throws RemoteException;

    public String getForwardMessage(String module, String criteria, String usertype) throws RemoteException;

    public String CheckUserId(String id) throws RemoteException;

    public String getTerms(String module) throws RemoteException;

    public String checkState(String country) throws RemoteException;

    public String checkcountry(String country) throws RemoteException;

    public ReturnVO getForgotPasswordDetail(String strLoginName, String strEmail, String strFirstname, String strType, String temp_Password_For_ForgotPWD) throws RemoteException;

    public String getUserResDet(String username, String password, String npassword, String email) throws RemoteException;

    public String getIncompletedRegUserResDet(String username, String password, String npassword) throws RemoteException;

    public ReturnVO getResetPasswordDetail(String strLoginName, String strEmail) throws RemoteException;

    public ReturnVO getResetPasswordDetailForIncompletedReg(String strLoginName) throws RemoteException;

    public CustomerProfile getuserProfile(String strloginame) throws RemoteException;

    public ArrayList getAgencyLoginDet(String username, String password) throws RemoteException;

    public ArrayList getAgencyLoginDetEncrypt(String username, String password, boolean isenabledEncrypt) throws RemoteException;

    public ArrayList getvendormodules(String columnValue) throws RemoteException;

    public ArrayList getvendormoduleslist(String username) throws RemoteException;

    public ArrayList getRegMenuDetails() throws RemoteException;

    public UserData getActiveDetails(String username, String password, boolean isenabledEncrypt) throws RemoteException;

    public UserData getUserDetailsFromCustomermaster(String username, String password) throws RemoteException;

    public UserData getUserDetailsforAdmin(String usercode) throws RemoteException;

    public ArrayList getCustMenuDetails(String CustId, String Type) throws RemoteException;

    public String setDBConst(String ColumnName, String Value, String split) throws RemoteException;

    public List getAdminLoginDetails(String username, String password) throws RemoteException;

    public ArrayList getMenuDetails() throws RemoteException;

    public String CheckPassword(String login, String password) throws RemoteException;

    public ReturnVO deleteuserdetails(String loginname, String contactname) throws RemoteException;

    public String deleteRejectedusers(String loginname, String refid, String type) throws RemoteException;

    public ArrayList getuserDetails(String Usercode) throws RemoteException;

    public ReturnVO saveToAdminRegistration(UserCreationVO regVo, String saveType, String cust_code, int user_count, String client_code) throws RemoteException;

    public String getalloweduser(String usercode) throws RemoteException;

    public ReturnVO getRegistrationMail(String type, String email) throws RemoteException;

    public String resetAdminPassword(String username, String password, String oldPassword) throws RemoteException;

    public String checkAdminPassword(String username, String password) throws RemoteException;

    public String getAPNAME(String usercode) throws RemoteException;

    public ReturnVO activeUserDetails(String loginname, String contactname) throws RemoteException;

    public String checkCompanyDetails(String custCode, String countryCode) throws RemoteException;

    public String updateCCMailId(String mailId, String ccMailId) throws RemoteException;

    public String updateTimeZone(String mailId, String timeZone) throws RemoteException;

    public List<TimeZone> getTimeZone() throws RemoteException;

    public RegistrationVO getCompanyDetails(String custcode) throws RemoteException;

    public void updatelogindate(String email) throws RemoteException;

    public void updatelogoutdate(String email) throws RemoteException;

    public void updateWrgPasAtmp(String email) throws RemoteException;

    public void lockWrgPasAcct(String email) throws RemoteException;

    public void resetWrgPasAtmp(String email) throws RemoteException;

    public UserData getWrgPasslogin(String email) throws RemoteException;

    public ArrayList getCountryFileDetails(String country) throws RemoteException;

    public String getfileuploadsharepath() throws RemoteException;

    public ReturnVO validateRegUserbymail(String randomId, String client_code, String validateRegUserbymail, String disableAutoRegVerification, String enableRegAutoProcess, String Multiple_agencies_location) throws RemoteException;

    public ReturnVO getReqActivateActDeatails(String email) throws RemoteException;

    public String getCustCompanyName(String custcode) throws RemoteException;

    public ReturnVO getDuplicateRegMail(RegistrationVO regVo) throws RemoteException;

    // Password Reset Token Methods
    public ReturnVO insertPasswordResetToken(String email, String token) throws RemoteException;

    public PasswordResetTokenVo validatePasswordResetToken(String token) throws RemoteException;

    public String updatePasswordWithToken(String emailId, String newPassword, String tokenId) throws RemoteException;

    public String getAccountStatusMessage(String email ,String gateAgencyMailId) throws RemoteException;
    
    public void lockWrgPasAcctMovetoPendwithAgency(String email) throws RemoteException;

    public String getAccountlockedMessage(String email) throws RemoteException;
    
    public UserData getIncompletedReglogin(String email, String password, boolean isenabledEncrypt) throws RemoteException;

    public void executeVdsAutoRegCalls(String loginname, String custCode, String transferdoc, String regno) throws RemoteException;

}