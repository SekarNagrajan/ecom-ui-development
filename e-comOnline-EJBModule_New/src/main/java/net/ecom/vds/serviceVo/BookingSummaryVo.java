/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.vds.serviceVo;

/**
 *
 * @author sasikumar.r
 */

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingSummaryVo implements Serializable{

	@SerializedName("booking")
	@Expose
	private List<Booking> booking = new ArrayList<Booking>();


	public List<Booking> getBooking() {
		return booking;
	}
	public void setBooking(List<Booking> booking) {
		this.booking = booking;
	}

}


