/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;

/**
 *
 * @author gobinath.t
 */
public class CargoProtectVo implements Serializable {
    
    int id;
    String tier;
    String maxcoverage;
    String pershipment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getMaxcoverage() {
        return maxcoverage;
    }

    public void setMaxcoverage(String maxcoverage) {
        this.maxcoverage = maxcoverage;
    }

    public String getPershipment() {
        return pershipment;
    }

    public void setPershipment(String pershipment) {
        this.pershipment = pershipment;
    }
    
    
}
