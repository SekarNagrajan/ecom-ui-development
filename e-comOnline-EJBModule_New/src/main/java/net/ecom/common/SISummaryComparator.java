/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.common;

import java.util.Comparator;
import net.ecom.vo.SIsummaryVO;

public class SISummaryComparator implements Comparator<SIsummaryVO>{
    @Override
    public int compare(SIsummaryVO o1, SIsummaryVO o2) {
        String strDt1=((SIsummaryVO)o1).getDepdate();
        String strDt2=((SIsummaryVO)o2).getDepdate();
        return strDt2.compareTo(strDt1);
    }

}
