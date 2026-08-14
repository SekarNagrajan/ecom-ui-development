/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.rmi.RemoteException;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author deepa.p
 */
@Remote
public interface EcomV1MasterBeanRemote {

    public Map<String, Map<String, String>> getEcomV1MasterCache() throws RemoteException;
}
