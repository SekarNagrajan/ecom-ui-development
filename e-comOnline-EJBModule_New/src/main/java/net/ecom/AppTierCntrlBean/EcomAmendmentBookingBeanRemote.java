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
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.ReturnVO;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomAmendmentBookingBeanRemote {

    public AmendmentBookingFormBean getEcomBookingDetails(String bookNo) throws RemoteException;

    public List getEcomBookingCargoDetails(String bookNo) throws RemoteException;

    public AmendmentBookingFormBean getLRPBookingMasterDetails(String bookNo) throws RemoteException;

    public ArrayList getLRPBookingRoutingDetails(String bookNo) throws RemoteException;

    public ArrayList getLRPBookingViewCargoDetails(String bookNo, String type, String ecomBookNo,String clearTemperatureWhileCopy) throws RemoteException;

    public ArrayList getEcomRoutingDetailsfrombookingleg(String bookNo) throws RemoteException;

    public ReturnVO saveAmendmentBooking(AmendmentBookingFormBean eBookingMasterData, ArrayList routingList, List<AmendmentBookingFormBean> cargoList, String client_code, FileDetailsVO fls, String custTypeConfig, String SaveType, String ApEmailID, String bkg_type, String custCountry, Map<String, List<AmendmentBookingFormBean>> CustomerMap) throws RemoteException;

    public String checkSICreation(String bookNo) throws RemoteException;

    public String getEcomCustMappingAgency(String custCode) throws RemoteException;

    public List getLRPBookingHaulageDetails(String bookNo, String type, HashMap cargonomap) throws RemoteException;

    public CutoffConfigVO getLRPBookingConfDetails(String bookno) throws RemoteException;

    public HashMap<String, List<AmendmentBookingFormBean>> getLRPBookingCutomerDetails(String bookNo) throws RemoteException;

    public HashMap<String, List<AmendmentBookingFormBean>> getEcomCutomerDetails(String bookNo) throws RemoteException;

    public ENSDeclarantVo getLRPBookingENSTabDetails(String lrpBookNo) throws RemoteException;
}
