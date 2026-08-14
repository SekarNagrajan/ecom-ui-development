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
public class DashboardVO implements Serializable{
    private String status;
    private String countval;
    private String book_no;

    public String getBook_no() {
        return book_no;
    }

    public void setBook_no(String book_no) {
        this.book_no = book_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountval() {
        return countval;
    }

    public void setCountval(String countval) {
        this.countval = countval;
    }
}
