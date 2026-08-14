/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;
import java.io.Serializable;
import java.util.ArrayList;
/**
 *
 * @author durkgadevi
 */
public class BLContainerValueVO implements Serializable{

    private BLSealVO blSealObj=null;
        private ArrayList<BLContainerDetailVo> cntDeatilsObj=new ArrayList<BLContainerDetailVo>();

    /**
     * @return the blSealObj
     */
    public BLSealVO getBlSealObj() {
        return blSealObj;
    }

    /**
     * @param blSealObj the blSealObj to set
     */
    public void setBlSealObj(BLSealVO blSealObj) {
        this.blSealObj = blSealObj;
    }

    /**
     * @return the cntDeatilsObj
     */
    public ArrayList<BLContainerDetailVo> getCntDeatilsObj() {
        return cntDeatilsObj;
    }

    /**
     * @param cntDeatilsObj the cntDeatilsObj to set
     */
    public void setCntDeatilsObj(ArrayList<BLContainerDetailVo> cntDeatilsObj) {
        this.cntDeatilsObj = cntDeatilsObj;
    }


}
