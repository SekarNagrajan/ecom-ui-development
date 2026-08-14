package net.ecom.AppTierCntrlBean;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.svm.lrp.nfr.query.cbean.VDSInvoker;

import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vo.MaskPortVO;


@Stateless(name = "EcomMaskBean")
@Remote(EcomMaskBeanRemote.class)

public class EcomMaskBean implements EcomMaskBeanRemote {
    @Inject
   
    VDSInvoker vds;
    EcomBKGVdsService ecomvdsservice;

    @PostConstruct
    public void inti() {
        ecomvdsservice = vds.create(EcomBKGVdsService.class);
       
    }
    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }
    
   @Override
    public List<MaskPortVO> getmaskportlist() throws RemoteException {
        Map hs = null;
        List li = null;
        List<MaskPortVO> maskPortList = new ArrayList<MaskPortVO>();
        
        try {
            hs = (Map) ecomvdsservice.getEcomMaskPort();
            
            if (hs != null) {
                li = (List) hs.get("maskport");
                
                if (li != null) {
                    for (int i = 0; i < li.size(); i++) {
                        hs = (Map) li.get(i);
                        String mport = checkNull((String) hs.get("mport"));
                        String port = checkNull((String) hs.get("port"));
                        String portName = checkNull((String) hs.get("mportname"));
                        String actualPortname = checkNull((String) hs.get("portname"));
                        String status = checkNull((String) hs.get("status"));
                        
                        MaskPortVO maskPortVO = new MaskPortVO();
                        maskPortVO.setPort(port);
                        maskPortVO.setMport(mport);
                        maskPortVO.setStatus(status);
                        maskPortVO.setPortname(actualPortname);
                        maskPortVO.setMportname(portName);
                        
                        maskPortList.add(maskPortVO);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("[ECOMEJB] EcomMaskBean getmaskportlist Exception:   " + e);
            e.printStackTrace();
        }
        
        return maskPortList;
    }

   
}
