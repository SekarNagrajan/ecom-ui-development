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
@XmlRootElement(name = "DDTariffSummaryList")
public class DDTariffSummaryList {
    @XmlElement(name = "DDTariffSummary")
    private List<DDTariffSummaryVO> ddTariffListSummary;

    public List<DDTariffSummaryVO> getDdTariffListSummary() {
        return ddTariffListSummary;
    }

    public void setDdTariffListSummary(List<DDTariffSummaryVO> ddTariffListSummary) {
        this.ddTariffListSummary = ddTariffListSummary;
    }

}
