/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ecom.vo;
import java.io.Serializable;
/**
 *
 * @author udhaya
 */
public class MenuBean implements Serializable{
    private String uname="";
    private String[] id=null;
    private String[] pid=null;
    private String[] text=null;

    public MenuBean(){
    }

    public void setUname(String uname)
    {
        this.uname=uname;
    }
    public String getUname()
    {
        return uname;
    }
    public void setId(String[] id)
    {
        this.id=id;
    }
    public String[] getId()
    {
      return id;
    }
    public void setPid(String[] pid)
    {
        this.pid=pid;
    }
    public String[] getPid()
    {
        return pid;
    }
    public void setText(String[] text)
    {
        this.text=text;
    }
    public String[] getText()
    {
        return text;
    }
    /** Creates a new instance of MenuBean */
}
