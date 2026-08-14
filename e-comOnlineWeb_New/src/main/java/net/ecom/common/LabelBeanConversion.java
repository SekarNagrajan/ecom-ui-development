/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.ArrayList;
import java.util.List;
import net.ecom.vo.DropDownVO;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author deepa.p
 */
public class LabelBeanConversion {

    public static ArrayList labelListConversion(List<DropDownVO> ddList) {
        ArrayList labelBeanList = new ArrayList();
        DropDownVO ddVO = null;
        try {
            if (ddList != null) {
                for (int i = 0; i < ddList.size(); i++) {
                    ddVO = ddList.get(i);
                    labelBeanList.add(new LabelValueBean(ddVO.getName(), ddVO.getCode()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return labelBeanList;
    }
}
