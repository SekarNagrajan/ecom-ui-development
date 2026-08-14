/*
 * PortVO.java
 *
 * Created on February 15, 2008, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author udhaya
 */
public class PortVO implements Serializable {
 private String portCode=null;
 private String portName=null;
 private String searchImg="";
 private String contractImg="";
 private String carriageImg="";
 private StringBuffer sb=new StringBuffer("");
 StringBuffer sv=new StringBuffer("");
 StringBuffer ss=new StringBuffer("");
    /** Creates a new instance of PortVO */
    public PortVO() {
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }
    public void setSearchImg(String searchImg){
        this.searchImg=searchImg;
    }
    public String getSearchImg(){
      //String firstNameAlias=firstName.replaceAll(" ","%20");
        sb.append("<a  style=\"cursor:pointer;\"");
        sb.append(" onclick=\"setInTransitInfo('"+portCode+"','InTransit');\">");
        sb.append("<img src=\"images/Pick16.gif\" align=\"absmiddle\">");
        sb.append("</a>");
        return sb.toString();
    }  
    public void setContractImg(String contractImg){
        this.contractImg=contractImg;
    }
    public String getContractImg(){
        sv.append("<a  style=\"cursor:pointer;\"");
        sv.append(" onclick=\"setTypeOfMovementInfo('"+portCode+"',escape('"+portName+"'),'typeofMovementcode');\">");
        sv.append("<img src=\"images/Pick16.gif\" align=\"absmiddle\">");
        sv.append("</a>");
        return sv.toString();
    }
    public void setCarriageImg(String carriageImg){
        this.carriageImg=carriageImg;
    }
    public String getCarriageImg(){
        ss.append("<a  style=\"cursor:pointer;\"");
        ss.append(" onclick=\"setCarriageTermInfo('"+portCode+"',escape('"+portName+"'),'typeofMovementcode');\">");
        ss.append("<img src=\"images/Pick16.gif\" align=\"absmiddle\">");
        ss.append("</a>");
        return ss.toString();
    }
}
