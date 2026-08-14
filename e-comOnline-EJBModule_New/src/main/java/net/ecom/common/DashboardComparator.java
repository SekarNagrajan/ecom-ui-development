/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.Comparator;

/**
 *
 * @author deepa.p
 */
public class DashboardComparator implements Comparator<net.ecom.vds.serviceVo.DashboardVO> {

    @Override
    public int compare(net.ecom.vds.serviceVo.DashboardVO o1, net.ecom.vds.serviceVo.DashboardVO o2) {
        int strDt1 = Integer.parseInt(((net.ecom.vds.serviceVo.DashboardVO) o1).getCountVal());
        int strDt2 = Integer.parseInt(((net.ecom.vds.serviceVo.DashboardVO) o2).getCountVal());
        int result = 0;
        if (strDt1 < strDt2) {
            result = 1;
        } else if (strDt1 > strDt2) {
            result = -1;
        }
        return result;
    }
}
