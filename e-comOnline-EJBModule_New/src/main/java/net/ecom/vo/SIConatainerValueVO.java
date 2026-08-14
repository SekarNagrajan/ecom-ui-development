/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author udhaya
 */
public class SIConatainerValueVO  implements Serializable{
        private SISealVO siSealObj=null;
        private ArrayList<SIContainerDetailVO> cntDeatilsObj=new ArrayList<SIContainerDetailVO>();

    /**
     * @return the siSealObj
     */
    public SISealVO getSiSealObj() {
        return siSealObj;
    }

    /**
     * @param siSealObj the siSealObj to set
     */
    public void setSiSealObj(SISealVO siSealObj) {
        this.siSealObj = siSealObj;
    }

    /**
     * @return the cntDeatilsObj
     */
    public ArrayList<SIContainerDetailVO> getCntDeatilsObj() {
        return cntDeatilsObj;
    }

    /**
     * @param cntDeatilsObj the cntDeatilsObj to set
     */
    public void setCntDeatilsObj(ArrayList<SIContainerDetailVO> cntDeatilsObj) {
        this.cntDeatilsObj = cntDeatilsObj;
    }

   
        
}
