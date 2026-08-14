package net.ecom.vo;

import java.io.Serializable;

public class BoundDefultVo
  implements Serializable
{
  private String pol = null;
  private String pod = null;
  private String serive = null;

  public String getPol()
  {
    return this.pol;
  }

  public void setPol(String pol)
  {
    this.pol = pol;
  }

  public String getPod()
  {
    return this.pod;
  }

  public void setPod(String pod)
  {
    this.pod = pod;
  }

  public String getSerive()
  {
    return this.serive;
  }

  public void setSerive(String serive)
  {
    this.serive = serive;
  }
}