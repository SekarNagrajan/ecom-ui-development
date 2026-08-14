/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remote;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.ReturnVO;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomV1AmendmentBookingBeanRemote {

    public AmendmentBookingFormBean getV1LRPBookingMasterDetails(String bookNo) throws RemoteException;

    public ArrayList getV1LRPBookingRoutingDetails(String bookNo) throws RemoteException;

    public ArrayList getV1LRPBookingViewCargoDetails(String bookNo,String type) throws RemoteException;
    
    public ReturnVO saveAmendmentBooking(AmendmentBookingFormBean eBookingMasterData, ArrayList routingList, List<AmendmentBookingFormBean> cargoList, String client_code) throws RemoteException;
}
