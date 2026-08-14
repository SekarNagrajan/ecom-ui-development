package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EcomCommonModelVo
  implements Serializable
{

  @SerializedName("customercontacts")
  @Expose
  private List<CustomerContacts> customerstatusupdate = new ArrayList();

  public List<CustomerContacts> getCustomerstatusupdate()
  {
    return this.customerstatusupdate;
  }

  public void setCustomerstatusupdate(List<CustomerContacts> customerstatusupdate)
  {
    this.customerstatusupdate = customerstatusupdate;
  }
}