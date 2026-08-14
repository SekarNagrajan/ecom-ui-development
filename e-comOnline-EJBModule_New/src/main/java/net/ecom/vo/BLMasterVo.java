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
public class BLMasterVo implements Serializable{

     private String strCntNo="";
  private int strSelect=0;
  private String strType="";

    /**
     * @return the strCntNo
     */
    public String getStrCntNo() {
         return Validation.checkNull(strCntNo);
    }

    /**
     * @param strCntNo the strCntNo to set
     */
    public void setStrCntNo(String strCntNo) {
        this.strCntNo = strCntNo;
    }

    /**
     * @return the strSelect
     */
    public int getStrSelect() {
        return strSelect;
    }

    /**
     * @param strSelect the strSelect to set
     */
    public void setStrSelect(int strSelect) {
        this.strSelect = strSelect;
    }

    /**
     * @return the strType
     */
    public String getStrType() {
      return Validation.checkNull(strType);
    }

    /**
     * @param strType the strType to set
     */
    public void setStrType(String strType) {
        this.strType = strType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.strCntNo != null ? this.strCntNo.hashCode() : 0);
        hash = 11 * hash + (this.strType != null ? this.strType.hashCode() : 0);
        return hash;
    }
 public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BLMasterVo other = (BLMasterVo) obj;
        if ((this.strCntNo == null) ? (other.strCntNo != null) : !this.strCntNo.equals(other.strCntNo)) {
            return false;
        }
        if ((this.strType == null) ? (other.strType != null) : !this.strType.equals(other.strType)) {
            return false;
        }
        return true;
    }

}
