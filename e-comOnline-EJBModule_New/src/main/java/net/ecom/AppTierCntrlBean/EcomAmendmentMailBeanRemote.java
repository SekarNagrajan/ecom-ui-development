/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import net.ecom.vo.BokingDetailds;

/**
 *
 * @author preethi.elumalai
 */
@Remote
public interface EcomAmendmentMailBeanRemote {

    public BokingDetailds getBookingDetails(Map bookChace, String booktype, String Type) throws RemoteException;

    public String generateSheduledetails(String bookNo, String EDIReadId, Map PORTLIST) throws ParseException, RemoteException;
}
