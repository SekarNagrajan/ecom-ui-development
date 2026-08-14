/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author deepa.p
 */
public class EcomRegCommonModel implements Serializable {

    @SerializedName("customercontacts")
    @Expose
    private List<CustomerContacts> customercontacts = new ArrayList();

    public List<CustomerContacts> getCustomercontacts() {
        return customercontacts;
    }

    public void setCustomercontacts(List<CustomerContacts> customercontacts) {
        this.customercontacts = customercontacts;
    }
}
