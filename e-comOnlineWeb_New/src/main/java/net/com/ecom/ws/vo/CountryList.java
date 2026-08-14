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
public class CountryList {

    @XmlElement(name = "MasterData")
    private List<CountryVO> countrylst = null;

    public List<CountryVO> getCountrylst() {
        return countrylst;
    }

    public void setCountrylst(List<CountryVO> countrylst) {
        this.countrylst = countrylst;
    }
}
