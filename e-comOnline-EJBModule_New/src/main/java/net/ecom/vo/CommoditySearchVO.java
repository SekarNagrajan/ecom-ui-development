/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author deepa.p
 */
public class CommoditySearchVO implements Serializable{
    private String parentComdtyCode;
    private String chileComdtyCode;
    private String parenComdtyName;
    private String chileComdtyName;

    public String getParentComdtyCode() {
        return parentComdtyCode;
    }

    public void setParentComdtyCode(String parentComdtyCode) {
        this.parentComdtyCode = parentComdtyCode;
    }

    public String getChileComdtyCode() {
        return chileComdtyCode;
    }

    public void setChileComdtyCode(String chileComdtyCode) {
        this.chileComdtyCode = chileComdtyCode;
    }

    public String getParenComdtyName() {
        return parenComdtyName;
    }

    public void setParenComdtyName(String parenComdtyName) {
        this.parenComdtyName = parenComdtyName;
    }

    public String getChileComdtyName() {
        return chileComdtyName;
    }

    public void setChileComdtyName(String chileComdtyName) {
        this.chileComdtyName = chileComdtyName;
    }
    
}
