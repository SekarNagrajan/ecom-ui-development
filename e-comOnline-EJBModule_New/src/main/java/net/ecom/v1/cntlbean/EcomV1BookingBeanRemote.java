/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.vo.ReturnVO;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomV1BookingBeanRemote {

    public ArrayList getV1LRPBookingViewCargoDetails(String bookNo) throws RemoteException;

    public ArrayList getV1LRPBookingViewDetails(String bookNo) throws RemoteException;

    public ArrayList getV1LRPBookingRoutingDetails(String bookNo) throws RemoteException;
    
    public ReturnVO saveToBooking(EbookingValidatorFormBeanNew eBookingMasterData, ArrayList all, String custcode, String custname, String bookno, String savetype, ArrayList voidCargoListObj, ArrayList voidComListObj, String eqpQuantity, String client_code, String slottype) throws RemoteException;
    
    public List getV1eBookingCustomerInfo(String columnName, String constraints, String columnValue, String custCode) throws RemoteException;
    
    public List getV1MoreCustomerDetails(String custCode, String moduleType) throws RemoteException;
    
    public String cancelEcomV1Booking(String ecomBookNo, String lrpBookNo) throws RemoteException;
}
