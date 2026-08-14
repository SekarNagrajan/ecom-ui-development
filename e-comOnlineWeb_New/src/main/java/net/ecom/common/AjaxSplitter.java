package net.ecom.common;

import java.util.ArrayList;
import java.util.List;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.Route;

public class AjaxSplitter {

    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();

    public List getMatchedData(String str, List listObj, int listSize) {

        List listMatchedObj = new ArrayList();
        str = str.toLowerCase();
        String strListObj = "";
        for (int i = 0; i < listSize; i++) {
            strListObj = listObj.get(i).toString().toLowerCase();
            if (strListObj.contains(str)) {
                strListObj = strListObj.toUpperCase();
                strListObj = Validation.checkNull(strListObj);
                listMatchedObj.add(strListObj);
            }
        }
        return listMatchedObj;
    }

    public ArrayList FindLordporttype(ArrayList arr) {
        String pol_str = null, pod_str = null;
        ArrayList arr_lst = new ArrayList();
        Route eBookingrouting = new Route();
        if (arr != null && arr.size() > 0) {
            int arraySize = arr.size();
            for (int j = 0; j < arraySize; j++) {
                try {
                    eBookingrouting = (Route) arr.get(j);
                    String lordporttype = bookingObj.getPortType(eBookingrouting.getLoadport());
                    String disporttype = bookingObj.getPortType(eBookingrouting.getDisport());
                    if (!"I".equalsIgnoreCase(lordporttype)) {
                        pol_str = pol_str != null ? pol_str : eBookingrouting.getLoadport();
                    }
                    if (!"I".equalsIgnoreCase(disporttype)) {
                        pod_str = eBookingrouting.getDisport();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        arr_lst.add(pol_str);
        arr_lst.add(pod_str);
        return arr_lst;
    }
}
