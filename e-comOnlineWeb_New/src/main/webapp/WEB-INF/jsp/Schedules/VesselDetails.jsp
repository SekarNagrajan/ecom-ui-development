<%@page import="java.util.ArrayList,java.util.List,java.util.Hashtable,java.util.Set,java.util.Iterator,net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('#strusermobile').keyup(function () {
            if (this.value.match(/[^0-9]/g)) {
                this.value = this.value.replace(/[^0-9]/g, '');
            }
        });
        $('#userphone').keyup(function () {
            if (this.value.match(/[^0-9]/g)) {
                this.value = this.value.replace(/[^0-9]/g, '');
            }
        });

    });
</script>

<%
    ArrayList VesselDetails = (ArrayList) session.getAttribute("Vesseldetails");
    String client_code = (String) getServletContext().getAttribute("client_code");
    String VesselName = "", vesselcode = "", callsign = "", vesseloperator = "", vesselowner = "", vesseltype = "";
    String flag = "", portofregistry = "", built = "", imo = "", grosston = "", netton = "";
    String lengthoverall = "", teunominal = "";
    String pdfviewaccess = (String) getServletContext().getAttribute(GeneralConf.pdfviewaccess);
    if (VesselDetails != null && VesselDetails.size() > 0) {
        if (VesselDetails.get(0) != null && VesselDetails.get(0).toString().trim().length() > 0) {
            VesselName = VesselDetails.get(0).toString();
        } else {
            VesselName = "-";
        }
        if (VesselDetails.get(1) != null && VesselDetails.get(1).toString().trim().length() > 0) {
            vesselcode = VesselDetails.get(1).toString();
        } else {
            vesselcode = "-";
        }
        if (VesselDetails.get(2) != null && VesselDetails.get(2).toString().trim().length() > 0) {
            callsign = VesselDetails.get(2).toString();
        } else {
            callsign = "-";
        }
        if (VesselDetails.get(3) != null && VesselDetails.get(3).toString().trim().length() > 0) {
            vesseloperator = VesselDetails.get(3).toString();
        } else {
            vesseloperator = "-";
        }

        if (VesselDetails.get(4) != null && VesselDetails.get(4).toString().trim().length() > 0) {
            vesselowner = VesselDetails.get(4).toString();
        } else {
            vesselowner = "-";
        }

        if (VesselDetails.get(5) != null && VesselDetails.get(5).toString().trim().length() > 0) {
            vesseltype = VesselDetails.get(5).toString();
        } else {
            vesseltype = "-";
        }

        if (VesselDetails.get(6) != null && VesselDetails.get(6).toString().trim().length() > 0) {
            flag = VesselDetails.get(6).toString();
        } else {
            flag = "-";
        }

        if (VesselDetails.get(7) != null && VesselDetails.get(7).toString().trim().length() > 0) {
            portofregistry = VesselDetails.get(7).toString();
        } else {
            portofregistry = "-";
        }

        if (VesselDetails.get(8) != null && VesselDetails.get(8).toString().trim().length() > 0) {
            built = VesselDetails.get(8).toString();
        } else {
            built = "-";
        }

        if (VesselDetails.get(9) != null && VesselDetails.get(9).toString().trim().length() > 0) {
            imo = VesselDetails.get(9).toString();
        } else {
            imo = "-";
        }

        if (VesselDetails.get(10) != null && VesselDetails.get(10).toString().trim().length() > 0) {
            lengthoverall = VesselDetails.get(10).toString();
        } else {
            lengthoverall = "-";
        }

        if (VesselDetails.get(11) != null && VesselDetails.get(11).toString().trim().length() > 0) {
            teunominal = VesselDetails.get(11).toString();
        } else {
            teunominal = "-";
        }

        if (VesselDetails.get(12) != null && VesselDetails.get(12).toString().trim().length() > 0) {
            grosston = VesselDetails.get(12).toString();
        } else {
            grosston = "-";
        }

        if (VesselDetails.get(13) != null && VesselDetails.get(13).toString().trim().length() > 0) {
            netton = VesselDetails.get(13).toString();
        } else {
            netton = "-";
        }

    }
    String strUserName = (String) session.getAttribute("SESSION_LOGINNAME");
    String strUsercode = (String) session.getAttribute("SESSION_USERCODE");
    String clientcode = (String) session.getAttribute("clientcode");

%>
<div class="row min-height" id="vesselid" style="width:1024px">
    <div class="">
        <div class="card">
            <div class="card-header bg-primary text-white text-center"> Vessel Information </div>
            <div class="card-body">
                <form method="post" id="contactform" name="contactform" class="form-horizontal">
                    <div class="col-sm-12">
                        <div class="">
                            <%if (pdfviewaccess != null && pdfviewaccess.length() > 0 && "yes".equalsIgnoreCase(pdfviewaccess)) {%>
                            <div class="form-group row" id="downloadid">
                                <div class="col-sm-12"><div class="pull-right"><button type="button"  class="btn btn-primary btn-sm" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" onClick="return getvesselpdf()" id="mailbtn"> Download PDF</button>
                                    </div></div>
                            </div>
                            <%}%>
                            <div class="form-group row">
                                <label class="col-sm-3 ">Vessel Name</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="vesselname"><%=VesselName%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Vessel Code</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="vesselcode"><%=vesselcode%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Call Sign</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="country"><%=callsign%></label>
                            </div>



                            <div class="form-group row">
                                <label class="col-sm-3 ">Vessel Operator</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="agency"><%=vesseloperator%></label>
                            </div>

                            <%--
                                 <div class="form-group row">
                                <label class="col-sm-3 ">Vessel Owner</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="userphone"><%=vesselowner%></label>
                            </div>
                            --%>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Vessel Type</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="userphone"><%=vesseltype%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Flag</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 "  id="strusermobile"><%=flag%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Port Of Registry</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 "  id="strusermobile"><%=portofregistry%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Build</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="useremail"><%=built%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">IMO/Lloyd's Number</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="useremail"><%=imo%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Length Over All</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="useremail"><%=lengthoverall%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">TEUs Nominal</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="useremail"><%=teunominal%></label>
                            </div>

                            <div class="form-group row">
                                <label class="col-sm-3 ">Gross Tonnage</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="useremail"><%=grosston%></label>
                            </div>


                            <div class="form-group row">
                                <label class="col-sm-3 ">Net Tonnage</label>
                                <label class="col-sm-1 ">:</label>
                                <label class="col-sm-8 " id="useremail"><%=netton%></label>
                            </div>

                            <div class="form-group row">
                                <br />
                                The information contained in this page is for general information purposes only. The information is provided by <%=client_code%>  and whilst we endeavour to keep the information up-to-date and correct, we make no representations or warranties of any kind, express or implied, about the completeness, accuracy, reliability, suitability or availability with respect to the website or the information, products, services, or related graphics contained on the website for any purpose. Any reliance you place on such information is therefore strictly at your own risk. </div><div class="form-group">
                                In no event will we be liable for any loss or damage including without limitation, indirect or consequential loss or damage, or any loss or damage whatsoever arising from use of this information
                            </div>

                        </div></div>

                </form>
            </div>
        </div>

    </div>

</div>



