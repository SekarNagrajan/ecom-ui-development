/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.phase2.service.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author deepa.p
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CRONumbersList")
public class CRONumbers {

    @XmlElement(name = "BookNo")
    private List<String> bookNo=null;
    @XmlElement(name = "releaseNo")
    private List<String> releaseNo=null;

    public List<String> getReleaseNo() {
        return releaseNo;
    }

    public void setReleaseNo(List<String> releaseNo) {
        this.releaseNo = releaseNo;
    }

    public List<String> getBookNo() {
        return bookNo;
    }

    public void setBookNo(List<String> bookNo) {
        this.bookNo = bookNo;
    }

}
