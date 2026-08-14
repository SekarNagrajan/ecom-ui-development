package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class EcomCommonVo
  implements Serializable
{

  @SerializedName("webid")
  @Expose
  private String webid = null;

  public String getWebid()
  {
    return this.webid;
  }

  public void setWebid(String webid)
  {
    this.webid = webid;
  }
}