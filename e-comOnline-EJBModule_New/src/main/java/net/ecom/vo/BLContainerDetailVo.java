/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;
import java.io.Serializable;
import net.ecom.common.Validation;
/**
 *
 * @author durkgadevi
 */
public class BLContainerDetailVo implements Serializable{

    private String strCommodity="";
       private String strWgt="";
       private String strQty="";
       private String strUnit="";
       private String strCid ="";

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.getStrCid() != null ? this.getStrCid().hashCode() : 0);
        return hash;
    }

      public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BLContainerDetailVo other = (BLContainerDetailVo) obj;
        if ((this.getStrCid() == null) ? (other.getStrCid() != null) : !this.strCid.equals(other.strCid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the strCommodity
     */
    public String getStrCommodity() {
        return strCommodity;
    }

    /**
     * @param strCommodity the strCommodity to set
     */
    public void setStrCommodity(String strCommodity) {
        this.strCommodity = strCommodity;
    }

    /**
     * @return the strWgt
     */
    public String getStrWgt() {
        return strWgt;
    }

    /**
     * @param strWgt the strWgt to set
     */
    public void setStrWgt(String strWgt) {
        this.strWgt = strWgt;
    }

    /**
     * @return the strQty
     */
    public String getStrQty() {
        return strQty;
    }

    /**
     * @param strQty the strQty to set
     */
    public void setStrQty(String strQty) {
        this.strQty = strQty;
    }

    /**
     * @return the strUnit
     */
    public String getStrUnit() {
        return strUnit;
    }

    /**
     * @param strUnit the strUnit to set
     */
    public void setStrUnit(String strUnit) {
        this.strUnit = strUnit;
    }

    /**
     * @return the strCid
     */
    public String getStrCid() {
        return strCid;
    }

    /**
     * @param strCid the strCid to set
     */
    public void setStrCid(String strCid) {
        this.strCid = strCid;
    }
       }


