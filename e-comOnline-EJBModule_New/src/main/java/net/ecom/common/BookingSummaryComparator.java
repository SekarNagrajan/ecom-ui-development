/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.Comparator;
import net.ecom.vo.EbookingViewAll;

public class BookingSummaryComparator implements Comparator<EbookingViewAll> {

    @Override
    public int compare(EbookingViewAll o1, EbookingViewAll o2) {
        String strDt1 = ((EbookingViewAll) o1).getCrdate();
        String strDt2 = ((EbookingViewAll) o2).getCrdate();
        return strDt2.compareTo(strDt1);
    }
}
