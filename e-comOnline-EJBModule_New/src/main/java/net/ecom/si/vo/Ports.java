/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.si.vo;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author salomi.a
 */
public class Ports implements Serializable{

	@SerializedName("portcode")
	@Expose
	private String portcode;


	public String getPortcode() {
		return portcode;
	}
	public void setPortcode(String portcode) {
		this.portcode = portcode;
	}

}

