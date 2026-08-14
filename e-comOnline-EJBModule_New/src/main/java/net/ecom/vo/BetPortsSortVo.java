/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author vijay.j
 */
public class BetPortsSortVo implements Comparator<BetPortsSortVo>{
   private String depDate;
   private List lstRow;

    @Override
    public int compare(BetPortsSortVo obj1, BetPortsSortVo obj2) {
        return obj1.getDepDate().compareTo(obj2.getDepDate());
    }

    /**
     * @return the depDate
     */
    public String getDepDate() {
        return depDate;
    }

    /**
     * @param depDate the depDate to set
     */
    public void setDepDate(String depDate) {
        this.depDate = depDate;
    }

    /**
     * @return the lstRow
     */
    public List getLstRow() {
        return lstRow;
    }

    /**
     * @param lstRow the lstRow to set
     */
    public void setLstRow(List lstRow) {
        this.lstRow = lstRow;
    }
   
}
