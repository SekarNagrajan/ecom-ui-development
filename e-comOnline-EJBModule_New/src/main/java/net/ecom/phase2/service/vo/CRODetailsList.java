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
@XmlRootElement(name = "CRODetailsList")
public class CRODetailsList {
    @XmlElement(name = "CRODetails")
    private List<CRODetailsVO> croDetailsList;

    public List<CRODetailsVO> getCroDetailsList() {
        return croDetailsList;
    }

    public void setCroDetailsList(List<CRODetailsVO> croDetailsList) {
        this.croDetailsList = croDetailsList;
    }


}
