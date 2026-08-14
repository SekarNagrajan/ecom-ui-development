/*
 * Formdto.java
 *
 * Created on January 31, 2007, 5:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author muthu
 */
public class Formdto implements Serializable {
    
    /** Creates a new instance of Formdto */
    public Formdto() {
    }
    
    
  private String countrycode ;
  private String countryname;
  private String deptcode;
  private String deptname;
  private String carriercode;
  private String citycode;
  private String cityname;
  private String usertype;
  private String usertypedesc;
  
  
  
  
  public void setCountrycode(String countrycode ){
      this.countrycode = countrycode;
  }
  public String getCountrycode(){
      return countrycode;
  }    
  public void setCountryname(String countryname ){
      this.countryname = countryname;
  }
  public String getCountryname(){
      return countryname;
  }    
  public void  setDeptcode(String deptcode){
      this.deptcode = deptcode;
  }
  public String getDeptcode(){
      return deptcode;
  }
  public void  setDeptname(String deptname){
      this.deptname = deptname;
  }
  public String getDeptname(){
      return deptname;
  }
  public void  setCarriercode(String carriercode){
      this.carriercode = carriercode;
  }
  public String getCarriercode(){
      return carriercode;
  }
   public void  setCitycode(String citycode){
      this.citycode = citycode;
  }
  public String getCitycode(){
      return citycode;
  }
  public void  setCityname(String cityname){
      this.cityname = cityname;
  }
  public String getCityname(){
      return cityname;
  }
public String getUsertype(){
    return usertype ;
}
 public void setUsertype(String usertype){
    this.usertype = usertype ;
 } 
 public String getUsertypedesc(){
    return usertypedesc ;
}
 public void setUsertypedesc(String usertypedesc){
    this.usertypedesc = usertypedesc ;
 }  
 
}
