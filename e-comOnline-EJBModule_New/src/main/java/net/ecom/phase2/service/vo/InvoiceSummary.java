/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.phase2.service.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.ecom.phase2.service.vo.InvoiceSummaryVO;

/**
 *
 * @author deepa.p
 */
@XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "InvoiceSummaryList")
public class InvoiceSummary {
    @XmlElement(name = "InvoiceSummary")
    private List<InvoiceSummaryVO> lstsummary;

    public List<InvoiceSummaryVO> getLstsummary() {
        return lstsummary;
    }

    public void setLstsummary(List<InvoiceSummaryVO> lstsummary) {
        this.lstsummary = lstsummary;
    }

}
