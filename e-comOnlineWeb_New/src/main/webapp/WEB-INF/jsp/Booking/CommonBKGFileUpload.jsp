<%-- 
    Document   : CommonBKGFileUpload
    Created on : Sep 18, 2020, 1:07:32 AM
    Author     : gobinath.t
--%>

<%@page contentType="text/html"   pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page import="java.util.Locale,java.util.ArrayList,java.util.LinkedHashMap,org.apache.struts.util.MessageResources,org.apache.struts.action.ActionForm" %>
<%@page import="net.ecom.formbeans.EbookingValidatorFormBeanNew,java.util.Date,net.ecom.vo.Route,net.ecom.common.Validation,net.ecom.common.GeneralConf,java.util.List,java.util.Map,net.ecom.vo.TransportServiceVO,net.ecom.vo.CustomerDetailsVO,net.ecom.vo.CutoffConfigVO,java.util.Hashtable,java.util.Set,net.ecom.vo.BookingHaulagedetailsVo,net.ecom.common.utils.DateConversion_New"%>
<%
    String bookingType = ((String) session.getAttribute("type"));
    String clientcode = (String) session.getAttribute("clientcode");
    ArrayList filenamelist = (ArrayList) request.getAttribute("FILENAMELIST");
    String EnableFileBackendValidation = (String) getServletContext().getAttribute(GeneralConf.EnableFileBackendValidation);
    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
    String fileExt = (String) getServletContext().getAttribute(GeneralConf.EcomFileExtension);
    System.out.println("[ECOM-WAR] CommonBKGFileUpload fileExt -->"+fileExt);
%>

<%
    if (!"templateedit".equalsIgnoreCase(bookingType)) {
%>
<h6><i class="step-icon fa_upload"></i>  <bean:message key="Booking.head.file" bundle="<%=clientcode%>"/> </h6>

<fieldset>

    <div class="inner txt_box_book txt_upload">
        <div class='col-sm-12 row'>
            <div class="col-sm-4">
                <div class="row">
                    <div class="col-xs-12 col-sm-12">
                        <div class="txt_note file_disclaimer_color"><bean:message key="ecom.bkg.filenote" bundle="<%=clientcode%>"/>:</div> 
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-12 ">
                        <%if("Y".equalsIgnoreCase(EnableFileBackendValidation)){%>
                        <div class="file_disclaimer_color"><bean:message key="ebooking.msg.fileonlyext" bundle="<%=clientcode%>"/> <%=fileExt%> files only accepted. </div>
                        <%}else{%>
                        <div class="file_disclaimer_color"><bean:message key="ebooking.msg.fileext" bundle="<%=clientcode%>"/></div>
                        <%}%>
                        <div class="file_disclaimer_color"> <bean:message key="ebooking.msg.filesizelimit" bundle="<%=clientcode%>"/><%=fileSize%>MB.</div>
                    </div>
                </div>                          
            </div>
            <div class='col-sm-8'>
                <div class="form-group">
                    <div class="row">               
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.vgm' bundle="<%=clientcode%>"/></span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path1" styleId="file_path1" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                           <!-- <label id="filename1_lbl" style="display:none"> <bean1:message key='ebooking.filename.fname' bundle="<%=clientcode%>"/>: <span id="filename1_span" class="file_nmae">  </span> </label>
                            <html1:hidden  styleId="filename1"  property="filename1"/>-->

                        </div>
                        <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('vgm')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                    </div>
                </div>

                <div class="form-group d-none" id="vgm1">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.vgm' bundle="<%=clientcode%>"/>  1 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path6" styleId="file_path6" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                            <!--<label id="filename1_lbl" style="display:none"> <bean1:message key='ebooking.filename.fname' bundle="<%=clientcode%>"/>: <span id="filename1_span" class="file_nmae">  </span> </label>
                            <html1:hidden  styleId="filename1"  property="filename1"/>-->

                        </div>

                    </div>
                </div>
                <div class="form-group d-none" id="vgm2">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.vgm' bundle="<%=clientcode%>"/> 2 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path7" styleId="file_path7" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.dg' bundle="<%=clientcode%>"/> </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path2" styleId="file_path2"  styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('dg')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>
                    </div>
                </div>
                <div class="form-group d-none" id="dg1">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.dg' bundle="<%=clientcode%>"/> 1 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path8" styleId="file_path8"  styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group d-none" id="dg2">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.dg' bundle="<%=clientcode%>"/> 2 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path9" styleId="file_path9"  styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others1' bundle="<%=clientcode%>"/>  </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path3" styleId="file_path3" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>

                        </div>
                        <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('others1')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                    </div>
                </div>

                <div class="form-group d-none" id="others11">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others1' bundle="<%=clientcode%>"/> 1 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path10" styleId="file_path10" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>

                <div class="form-group d-none" id="others12">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others1' bundle="<%=clientcode%>"/> 2 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path11" styleId="file_path11" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>


                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others2' bundle="<%=clientcode%>"/> </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path4" styleId="file_path4" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>

                        </div>
                        <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('others2')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                    </div>
                </div>
                <div class="form-group d-none" id="others21">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others2' bundle="<%=clientcode%>"/> 1 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path12" styleId="file_path12" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>

                <div class="form-group d-none" id="others22">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others2' bundle="<%=clientcode%>"/> 2 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path13" styleId="file_path13" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others3' bundle="<%=clientcode%>"/> </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path5" styleId="file_path5" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>

                            </div>

                        </div>
                        <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('others3')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                    </div>
                </div>

                <div class="form-group d-none" id="others31">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others3' bundle="<%=clientcode%>"/> 1 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path14" styleId="file_path14" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>

                            </div>

                        </div>

                    </div>
                </div>

                <div class="form-group d-none" id="others32">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">

                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text-fileupload"><bean:message key='ecom.file.others3' bundle="<%=clientcode%>"/> 2 </span>
                                </div>
                                <div class="custom-file">
                                    <html:file property="file_path15" styleId="file_path15" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                    <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>

                            </div>

                        </div>

                    </div>
                </div>

                <%if (filenamelist != null && filenamelist.size() > 0) {%>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th scope="col">FileName</th>
                                        <th scope="col" width='10'>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%for (int i = 0; i < filenamelist.size(); i++) {%>
                                    <tr>
                                        <th><%=filenamelist.get(i)%></th>
                                        <td><a href="javascript:void(0);" onclick="downloadfile('<%=filenamelist.get(i)%>')" class='text-success' ><i class="fa fa-download"  aria-hidden="true"></i></a>
                                            <a href="javascript:void(0);" onclick="deletefile('<%=filenamelist.get(i)%>', this)" class='text-danger'><i class="fa fa-trash-o"  aria-hidden="true"></i></a></td>
                                    </tr>
                                    <%}%>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
                <%}%>
            </div>
        </div>
    </div>
</fieldset>	
<%}%>