package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class CustomerAdvisoryVo implements Serializable{

    private int keyid;
    private String advisoryname;
    private String description;
    private String status;
    private String created_date;

    public String getAdvisoryname() {
        return advisoryname;
    }

    public void setAdvisoryname(String advisoryname) {
        this.advisoryname = advisoryname;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
