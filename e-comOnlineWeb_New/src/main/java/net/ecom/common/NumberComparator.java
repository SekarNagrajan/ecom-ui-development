/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.common;

/**
 *
 * @author udhaya
 */
import java.util.Comparator;
import net.ecom.vo.SIMasterVO;

public class NumberComparator implements Comparator<SIMasterVO> {
    @Override
	  public int compare(SIMasterVO o1, SIMasterVO o2) {
		  SIMasterVO s1 = (SIMasterVO)o1;
		  SIMasterVO s2 = (SIMasterVO)o2;
                  return (new Integer(s1.getStrSelect())).compareTo(new Integer(s2.getStrSelect()));
	         /* if (s1.getStrSelect() > s2.getStrSelect()){
                    return +1;
                  }else if (s1.getStrSelect() < s2.getStrSelect()){
                    return -1;
                  }else{
                    return 0;
                  }*/
	  }
}
