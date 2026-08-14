/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.CustomerProfile;

/**
 *
 * @author deepa.p
 */
public class RemoveDuplicateValues {

    public List removeNotifyDuplicate(List ntList) {

        ArrayList removeList = new ArrayList();
        CustomerDetailsVO custVO = null;
        Set duplicateSet = new HashSet();
        String strResult = null;
        try {
            int listSize = ntList.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < listSize; i++) {

                custVO = (CustomerDetailsVO) ntList.get(i);
                sb.append(Validation.checkNull(custVO.getCustCode()).trim());
                strResult = sb.toString().trim();
                //
                if (duplicateSet.contains(strResult)) {
                    removeList.add(i);
                } else {
                    duplicateSet.add(strResult);
                }
                sb.delete(0, sb.length());

            }

            for (int z = removeList.size() - 1; z >= 0; z--) {
                int remove = (Integer) removeList.get(z);
                ntList.remove(remove);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ntList;
    }

    public List removeCustomerListDuplicate(List ntList) {

        ArrayList removeList = new ArrayList();
        CustomerProfile custVO = null;
        Set duplicateSet = new HashSet();
        String strResult = null;
        try {
            int listSize = ntList.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < listSize; i++) {

                custVO = (CustomerProfile) ntList.get(i);
                sb.append(Validation.checkNull(custVO.getCustId()).trim());
                strResult = sb.toString().trim();
                //
                if (duplicateSet.contains(strResult)) {
                    removeList.add(i);
                } else {
                    duplicateSet.add(strResult);
                }
                sb.delete(0, sb.length());

            }

            for (int z = removeList.size() - 1; z >= 0; z--) {
                int remove = (Integer) removeList.get(z);
                ntList.remove(remove);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ntList;
    }
}
