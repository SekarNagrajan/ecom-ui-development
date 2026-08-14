/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.si.vo;

/**
 *
 * @author vijay
 */
public class Name implements java.io.Serializable{
   private String strCode;
   private String strName;

    private String polname;
    private String podname;
    
    private String description;


   // Added by Mahalakshmi.r on 28th June, 2013
   private String strEmailid;

   public Name(){

   }

    /**
     * @return the strCode
     */
    public String getStrCode() {
        return strCode;
    }

    /**
     * @param strCode the strCode to set
     */
    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    /**
     * @return the strName
     */
    public String getStrName() {
        return strName;
    }

    /**
     * @param strName the strName to set
     */
    public void setStrName(String strName) {
        this.strName = strName;
    }

    /**
     * @return the strEmailid
     */
    public String getStrEmailid() {
        return strEmailid;
    }

    /**
     * @param strEmailid the strEmailid to set
     */
    public void setStrEmailid(String strEmailid) {
        this.strEmailid = strEmailid;
    }

    /**
     * @return the polname
     */
    public String getPolname() {
        return polname;
    }

    /**
     * @param polname the polname to set
     */
    public void setPolname(String polname) {
        this.polname = polname;
    }

    /**
     * @return the podname
     */
    public String getPodname() {
        return podname;
    }

    /**
     * @param podname the podname to set
     */
    public void setPodname(String podname) {
        this.podname = podname;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    

    
}
