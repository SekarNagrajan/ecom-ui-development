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

public class RoutingVo implements Serializable{

	@SerializedName("routedetail")
	@Expose
	private List<Routedetail> routedetail = new ArrayList<Routedetail>();


	public List<Routedetail> getRoutedetail() {
		return routedetail;
	}
	public void setRoutedetail(List<Routedetail> routedetail) {
		this.routedetail = routedetail;
	}

}