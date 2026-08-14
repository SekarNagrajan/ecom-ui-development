/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.com.ecom.ws.vo;

/**
 *
 * @author sasikumar.r
 */
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MasterList")
public class GeneralVOList {

    @XmlElement(name = "MasterData")
    private List<GeneralVO> portvo = null;

    public List<GeneralVO> getPortvo() {
        return portvo;
    }

    public void setPortvo(List<GeneralVO> portvo) {
        this.portvo = portvo;
    }
}
