package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class VideoHelpVo implements Serializable {

    private String logintype;    
    private String menuname;
    private String link;
    private String status;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
