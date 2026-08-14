package net.ecom.AppTierCntrlBean;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.Remote;
import net.ecom.vo.MaskPortVO;

@Remote 
public interface EcomMaskBeanRemote {
    public List<MaskPortVO> getmaskportlist() throws RemoteException;
   

}