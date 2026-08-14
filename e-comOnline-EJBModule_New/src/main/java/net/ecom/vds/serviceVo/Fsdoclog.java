package net.ecom.vds.serviceVo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Fsdoclog implements Serializable {

    @SerializedName("doctype")
    @Expose
    private String doctype;

    @SerializedName("sysfilename")
    @Expose
    private String sysfilename;

    @SerializedName("refno")
    @Expose
    private String refno;

    @SerializedName("doccategory")
    @Expose
    private String doccategory;

    @SerializedName("docrefno")
    @Expose
    private String docrefno;

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("basepath")
    @Expose
    private String basepath;

    @SerializedName("docsize")
    @Expose
    private String docsize;

    @SerializedName("moduleid")
    @Expose
    private String moduleid;

    @SerializedName("uploaduser")
    @Expose
    private String uploaduser;

    @SerializedName("status")
    @Expose
    private String status;

    public String getDoctype() { return doctype; }
    public void setDoctype(String doctype) { this.doctype = doctype; }

    public String getSysfilename() { return sysfilename; }
    public void setSysfilename(String sysfilename) { this.sysfilename = sysfilename; }

    public String getRefno() { return refno; }
    public void setRefno(String refno) { this.refno = refno; }

    public String getDoccategory() { return doccategory; }
    public void setDoccategory(String doccategory) { this.doccategory = doccategory; }

    public String getDocrefno() { return docrefno; }
    public void setDocrefno(String docrefno) { this.docrefno = docrefno; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getBasepath() { return basepath; }
    public void setBasepath(String basepath) { this.basepath = basepath; }

    public String getDocsize() { return docsize; }
    public void setDocsize(String docsize) { this.docsize = docsize; }

    public String getModuleid() { return moduleid; }
    public void setModuleid(String moduleid) { this.moduleid = moduleid; }

    public String getUploaduser() { return uploaduser; }
    public void setUploaduser(String uploaduser) { this.uploaduser = uploaduser; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
