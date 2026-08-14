/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vo;

import java.io.Serializable;



/**
 *
 * @author preethi
 */
public class EcomBookingScheduleVo implements Serializable {
    private String ebook_pol_eta;
    private String ebook_vesselcode;
    private String ebook_pol_etd;
    private String ebook_voyage;
    private String ebook_bound_leg;
//    private String servicecode;
    private String ebook_origin_country;
    private String ebook_delivery_country;

  

    public String getEbook_pol_eta() {
        return ebook_pol_eta;
    }

    public void setEbook_pol_eta(String ebook_pol_eta) {
        this.ebook_pol_eta = ebook_pol_eta;
    }

    public String getEbook_vesselcode() {
        return ebook_vesselcode;
    }

    public void setEbook_vesselcode(String ebook_vesselcode) {
        this.ebook_vesselcode = ebook_vesselcode;
    }

    public String getEbook_pol_etd() {
        return ebook_pol_etd;
    }

    public void setEbook_pol_etd(String ebook_pol_etd) {
        this.ebook_pol_etd = ebook_pol_etd;
    }

    public String getEbook_voyage() {
        return ebook_voyage;
    }

    public void setEbook_voyage(String ebook_voyage) {
        this.ebook_voyage = ebook_voyage;
    }

    public String getEbook_bound_leg() {
        return ebook_bound_leg;
    }

    public void setEbook_bound_leg(String ebook_bound_leg) {
        this.ebook_bound_leg = ebook_bound_leg;
    }

    public String getEbook_origin_country() {
        return ebook_origin_country;
    }

    public void setEbook_origin_country(String ebook_origin_country) {
        this.ebook_origin_country = ebook_origin_country;
    }

    public String getEbook_delivery_country() {
        return ebook_delivery_country;
    }

    public void setEbook_delivery_country(String ebook_delivery_country) {
        this.ebook_delivery_country = ebook_delivery_country;
    }

  
}

