<%@page import="net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%String clientcode = (String) session.getAttribute("clientcode");
    String loginTrackRefNo = (String) request.getAttribute("logintrackrefno");
    String loginTrackType = (String) request.getAttribute("tracktype");
    String refNoType = (String) request.getAttribute("refNoType");
    if ("bl".equalsIgnoreCase(refNoType)) {
        refNoType = "blnumbers";
    } else if ("booking".equalsIgnoreCase(refNoType)) {
        refNoType = "bookingnos";
    } else {
        refNoType = "containernos";
    }
    System.out.println("[ECOM-WAR] Tracking hai>>" + loginTrackType + "  loginTrackRefNo>>>" + loginTrackRefNo);
    String isViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
    long jsdate = new java.util.Date().getTime();
    String contextRoot = request.getContextPath();
    String siteKey = (String) getServletContext().getAttribute(GeneralConf.CaptchaSiteKey);
    String toDisplayGgleCaptcha = (String) getServletContext().getAttribute(GeneralConf.DisplayGoogleCaptcha);
    String carriercode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String enableLiveTracking = (String) getServletContext().getAttribute(GeneralConf.Enable_Live_Tracking);
    String mapboxAccessToken = (String) getServletContext().getAttribute(GeneralConf.MAPBOX_ACCESS_TOKEN);
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/tracking.png" /> <bean:message key="ecom.trck.head" bundle="<%=clientcode%>"/>  </div>
<div class="card card-body card-shadow page_bottom_content remove_print">
    <div class="container">
        <form id="track" method="post" action="TrackingDetails.do" class="form-horizontal" >

            <div class="row form-group">
                <div class="col-xs-12 col-sm-5">
                    <!--                        <input type="text" class="form-control inside_input" placeholder="Please enter a BL/Booking/Container No." />-->

                    <textarea id="txtNumbers"  cols="10" rows="3" class="form-control inputbox required validate-username" name="txtNumbers" placeholder="<bean:message key="tracking.msg.entdata" bundle="<%=clientcode%>"/>
                              <%if ("Yes".equalsIgnoreCase(isViaLogin) && (!("LSC".equalsIgnoreCase(carriercode) || "NMC".equalsIgnoreCase(carriercode)))) {%> 
                              <bean:message key="tracking.msg.entcont" bundle="<%=clientcode%>"/>
                              <%} else {%>
                              <bean:message key="tracking.msg.empty" bundle="<%=clientcode%>"/>
                              <%}%> <bean:message key="tracking.msg.entcontnum" bundle="<%=clientcode%>"/>" style="width: 99%;resize:none;height: 100%;" ></textarea>
                </div>
                <%if (!"Yes".equalsIgnoreCase(isViaLogin) && "Y".equalsIgnoreCase(toDisplayGgleCaptcha)) {%> 
                <div class="col-xs-12 col-sm-2">
                    <button class="btn btn-info btn_block_custom" onclick="checkvalidate();checkGoogleRecaptcha();checkRecaptcha();" autocomplete="off" id="searchbtn" > <bean:message key="ecom.Search" bundle="<%=clientcode%>"/> </button>
                </div>
                <%} else {%>
                <div class="col-xs-12 col-sm-2">
                    <button class="btn btn-info btn_block_custom" onclick="checkvalidate();" autocomplete="off" id="searchbtn" > <bean:message key="ecom.Search" bundle="<%=clientcode%>"/> </button>
                </div>
                <%}%>

                <div class="col-xs-12 col-sm-2">
                    <button class="btn btn-danger btn_block_custom_reset" onClick="$('#track label.error').remove();
                            $('#track input.error').css('border-color', '#ccc');
                            refreshpage();" id="resetbtn" >  <bean:message key="ecom.reset" bundle="<%=clientcode%>"/> </button>
                </div>
                <%if ("Y".equalsIgnoreCase(enableLiveTracking)) {%>
                <div class="col-xs-12 col-sm-2">
                    <button type="button" class="btn btn-success btn_block_custom livemap-search-btn" onclick="openLiveMapFromSearch();" id="livemapbtn"><i class="fa fa-map-marker"></i>&nbsp;Live Map&nbsp;</button>
                </div>
                <%}%>
<style>
.livemap-search-btn {
    position: relative;
    font-weight: 600;
    letter-spacing: 0.4px;
    box-shadow: 0 2px 6px rgba(40,167,69,0.35);
    animation: livemap-glow 2.2s ease-in-out infinite;
}
.livemap-search-btn:hover {
    box-shadow: 0 4px 12px rgba(40,167,69,0.55);
    transform: translateY(-1px);
}

@keyframes livemap-glow {
    0%,100% { box-shadow: 0 2px 6px rgba(40,167,69,0.35); }
    50%      { box-shadow: 0 2px 14px rgba(40,167,69,0.70); }
}
</style>

            </div>
            <%if (!"Yes".equalsIgnoreCase(isViaLogin) && !"Y".equalsIgnoreCase(toDisplayGgleCaptcha)) {%> 
            <div class="row form-group">
                <div class="col-sm-5">
                    <input class="light_border m-t-5 form-control remove_autocomplete required valid" name="captcha" id="captcha" type="text" placeholder="<bean:message key="track.capt.char" bundle="<%=clientcode%>"/>" autocomplete="off" aria-invalid="false"><label id="captcha-error" class="error" for="captcha" style="display: none;"></label>
                </div>

                <div class="col-sm-3">
                    <img src="<%=contextRoot%>/CaptchaGeneration" id="captchaImage" alt="captcha"> 
                    <a href="javascript:void(0)" title="Reload captcha" class="refresh_cap" id="captcha" onclick="CreateNewCaptcha('/ecomonline');"> <i class="fa fa-refresh"></i> </a>
                </div>

                <div class="col-sm-2">

                </div>
            </div>
            <%}%>
            <%if (!"Yes".equalsIgnoreCase(isViaLogin) && "Y".equalsIgnoreCase(toDisplayGgleCaptcha)) {%> 
            <div class="form-group row">

                <div class="col-sm-5">
                    <div id="recaptiid" class="g-recaptcha" styleClass="form-control light_border required" data-sitekey="<%=siteKey%>"    required="true" data-expired-callback="recaptchaExpired" data-callback="verifyCaptcha"></div>
                    <div id="g-recaptcha-error"></div>  
                </div>
            </div>
            <%}%>
            <input type="hidden" id="print_text" name="print_text" />
            <input type="hidden" id="type" name="type" value="l"/>
        </form>
    </div>
</div>
<div id="trackingSearch">
</div>

<script type="text/javascript" src="js/Tracking.js?<%=jsdate%>"></script>
<script type="text/javascript">
                        var contextRoot = "<%=contextRoot%>";
                        var enableLiveTracking = "<%=enableLiveTracking%>";
                        var mapboxAccessToken = "<%=mapboxAccessToken%>";

                        var lastLiveMapSearch = "";
                        function openLiveMapFromSearch() {
                            var inputVal = trim($('#txtNumbers').val());
                            if (inputVal.length < 3) {
                                alert("Please enter a valid Booking, BL or Container number.");
                                return;
                            }

                            if (isViaLogin != 'YES' && isViaLogin != 'Yes') {
                                if (enableGgleCaptcha == 'Y') {
                                    if (!checkRecaptcha()) {
                                        checkGoogleRecaptcha();
                                        return;
                                    }
                                } else {
                                    if (!$('#captcha').valid()) {
                                        return;
                                    }
                                }
                            }

                            var inputChanged = (inputVal !== lastLiveMapSearch);
                            if (!inputChanged) {
                                var existingContainers = extractContainerNumbers();
                                if (existingContainers.length > 0) {
                                    LiveMap.openLiveMap(existingContainers.join(","), mapboxAccessToken);
                                    return;
                                }
                            }

                            var recaptresponse = "";
                            if (enableGgleCaptcha == 'Y' && isViaLogin != 'YES') {
                                recaptresponse = grecaptcha.getResponse();
                            }

                            openLoader();
                            var m_array = inputVal.split(/\s+/);
                            var MdlNo = m_array.join(",");
                            lastLiveMapSearch = inputVal;
                            $.ajax({
                                type: "POST",
                                url: "TrackingDetails.do?Mdl=l&MdlNo=" + escape(MdlNo) + "&ldate=" + Date.parse(new Date()) + "&gcaptresp=" + recaptresponse + "&tmp=",
                                success: function (response) {
                                    $("#trackingSearch").html(response);
                                    closeLoader();
                                    if (enableGgleCaptcha == 'Y' && isViaLogin != 'YES') {
                                        grecaptcha.reset();
                                    }
                                    var containers = extractContainerNumbers();
                                    if (containers.length > 0) {
                                        LiveMap.openLiveMap(containers.join(","), mapboxAccessToken);
                                    } else {
                                        alert("No container numbers found for the given search.");
                                    }
                                },
                                error: function (xhr) {
                                    closeLoader();
                                    if (xhr.status == 403) {
                                        window.location.href = "SessionExpired.jsp";
                                    } else {
                                        alert("Unable to retrieve tracking details.");
                                    }
                                }
                            });
                        }

                        function extractContainerNumbers() {
                            var containers = [];
                            $('#trackingSearch #datatablebytrack tbody tr[data-contno]').each(function () {
                                var contNo = $(this).attr('data-contno');
                                if (contNo && contNo.length > 0 && $.inArray(contNo, containers) === -1) {
                                    containers.push(contNo);
                                }
                            });
                            return containers;
                        }
                        var enableGgleCaptcha = "<%=toDisplayGgleCaptcha%>";
                        var remotevalidate = contextRoot + "/CaptchaValidation";
                        var capchavalidate = contextRoot + "/CaptchaGeneration?";
                        var isViaLogin = '<%=isViaLogin%>';

                        function trim(value) {
                            return LTrim(RTrim(value));
                        }
                        function LTrim(value) {
                            var re = /\s*((\S+\s*)*)/;
                            return value.replace(re, "$1");
                        }
                        // Removes ending whitespaces
                        function RTrim(value) {
                            var re = /((\s*\S+)*)\s*/;
                            return value.replace(re, "$1");
                        }

                        $(document).ready(function () {
                            $("#track").validate({
                                rules: {
                                    txtNumbers:
                                            {
                                                minlength: 3
                                            },
                                    captcha: {
                                        required: true,
                                        remote: remotevalidate
                                    }
                                },
                                messages: {
                                    txtNumbers:
                                            {
                                                minlength: "<bean:message key="ecom.msg.tracking.minchar" bundle="<%=clientcode%>"/>"
                                            },
                                    captcha: {
                                        required: "<bean:message key="ecom.msg.entcapchacode" bundle="<%=clientcode%>"/>",
                                        remote: "<bean:message key="ecom.msg.incorrectcaptcha" bundle="<%=clientcode%>"/>"
                                    }
                                },
                                submitHandler: function (form) {
                                    // $("#searchbtn").button('loading');
                                    console.log('inside submitHandler  ');
                                    $('#resetfunc').val('Y');
                                    var j = 0;
                                    var type = "";
                                    type = document.getElementById("type").value;
                                    var MdlNo = $('#txtNumbers').val();
                                    var m_array = trim(document.getElementById("txtNumbers").value).split(/\s+/);
                                    var con_array = new Array();
                                    for (var i = 0; i < m_array.length; i++) {
                                        con_array.push(m_array[i]);
                                    }
                                    MdlNo = con_array;

                                    if (enableGgleCaptcha == 'Y' && (isViaLogin != 'YES')) {
                                        if (checkRecaptcha()) {
                                            var recaptresponse = grecaptcha.getResponse();
                                            openLoader();
                                            $.ajax({
                                                type: "POST",
                                                url: "TrackingDetails.do?Mdl=" + type + "&MdlNo=" + escape(MdlNo) + "&ldate=" + Date.parse(new Date()) + "&gcaptresp=" + recaptresponse + "&tmp=",
                                                success: function (response) {
                                                    $("#trackingSearch").html(response);
                                                    closeLoader();
                                                    grecaptcha.reset();
                                                    //  $("#searchbtn").button('reset');
                                                },
                                                error: function (xhr) {
                                                    if (xhr.status == 403) {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        openLoader();
                                        $.ajax({
                                            type: "POST",
                                            url: "TrackingDetails.do?Mdl=" + type + "&MdlNo=" + escape(MdlNo) + "&ldate=" + Date.parse(new Date()) + "&gcaptresp=" + recaptresponse + "&tmp=",
                                            success: function (response) {
                                                $("#trackingSearch").html(response);
                                                closeLoader();
                                                //  $("#searchbtn").button('reset');
                                            },
                                            error: function (xhr) {
                                                if (xhr.status == 403) {
                                                    window.location.href = "SessionExpired.jsp";
                                                }
                                            }
                                        });
                                    }
                                    return false;
                                }
                            });
                        });
                        $("#newsearch").click(function () {
                            //      callLoading();
                            openLoader();
                            location.href = "Accesslogin.do?type=tracking";
                        });
                        function CreateNewCaptcha(contextRoot)
                        {
                            var d = new Date();
                            $("#captchaImage").attr('src', capchavalidate + d.getTime());
                            $("#captcha").val('');
                        }
                        function refreshpage()
                        {
                            $('#txtNumbers').val('');
                            $('#reqval').css("display", "none");
                            $('#trackingSearch').html("");
                            $('input:radio[name="type"][value="c"]').prop('checked', true);
                            $("#captcha").val('');
                            $("#txtNumbers").focus();
                        }

                        function ContainerInfoNew(containerNo, Mdlno, tableId, tdid, linkid) {
                            openLoader();
                            $.ajax({
                                type: "POST",
                                cache: false,
                                url: "ecomAjax?type=containermovement&containerNo=" + trim(containerNo) + "&bookNo=" + trim(Mdlno) + "&date=" + Date.parse(new Date()) + "&tmp=",
                                data: $(this).serializeArray(),
                                success: function (data) {
                                    closeLoader();
                                    $.fancybox.open(data, {
                                        showCloseButton: false,
                                        closeClick: false,
                                        maxWidth: 1200,
                                        maxHeight: 600,
                                        fitToView: true,
                                        async: false,
                                        width: '100%',
                                        height: '100%',
                                        autoSize: true,
                                        scrollOutside: true,
                                        //closeBtn   : false,
                                        hideOnOverlayClick: false,
                                        overlayShow: false,
                                        helpers: {
                                            overlay: {closeClick: false}
                                        },
                                        afterClose: function () {
                                            //  $("#" + linkid).html("More Details");
                                        },
                                        keys: {
                                            close: null
                                        }

                                    });
                                },
                                error: function (data, xhr) {
                                    if (xhr.status == 403) {
                                        window.location.href = "SessionExpired.jsp";
                                    }
                                    //$("#" + linkid).html("More Details");
                                    //$('#routehref').html("<i class='fa fa-search'></i>");
                                    alert("! Error");
                                }
                            });
                        }

                        function checkvalidate()
                        {

                        }
                        function checkGoogleRecaptcha() {

                            var response = grecaptcha.getResponse();
                            if (response.length == 0) {
                                document.getElementById('g-recaptcha-error').innerHTML = '<span style="color:red;"><bean:message key="ecom.gglecaptcha.err" bundle="<%=clientcode%>"/></span>';
                                return false;
                            }
                            return true;
                        }

                        function verifyCaptcha() {
                            document.getElementById('g-recaptcha-error').innerHTML = '';
                        }
                        function recaptchaExpired() {
                            alert("<bean:message key="ecom.gglecaptcha.expired" bundle="<%=clientcode%>"/>");
                        }
                        function checkRecaptcha() {
                            var captchaval = $('#g-recaptcha-response').val();

                            if ($('#g-recaptcha-response').length == 0 || captchaval == null || captchaval == 'null' || captchaval == '' || captchaval.length == 0) {
                                return false;
                            }
                            return true;

                        }

                        function removevalidate()
                        {
                            //$('#reqval').css("display","none");
                            $('#trackingSearch').html("");
                            $('#txtNumbers').val('');
                        }

//                        $(document).ready(function () {
//                            setTimeout(function () {
//                                $('#txtNumbers').val('');
//                            }, 10);
//                        });

                        $(document).ready(function () {

                            $("#txtNumbers").focus();
    <%
        if ("logintracking".equalsIgnoreCase(loginTrackType)) {
    %>
                            $('#txtNumbers').val('<%=loginTrackRefNo%>');
                            $("#txtNumbers").attr("value", '<%=loginTrackRefNo%>');
                            callTrackingForLogin('<%=refNoType%>', '<%=loginTrackRefNo%>');
    <%                        }
    %>
                            var placeholder_text = '<bean:message key="tracking.msg.entdata" bundle="<%=clientcode%>"/>';
    <%if ("Yes".equalsIgnoreCase(isViaLogin) && (!("LSC".equalsIgnoreCase(carriercode) || "NMC".equalsIgnoreCase(carriercode)))) {%>
                            placeholder_text = placeholder_text + '<bean:message key="tracking.msg.entcont" bundle="<%=clientcode%>"/>';
    <%}%>
                            placeholder_text = placeholder_text + ' <bean:message key="tracking.msg.entcontnum" bundle="<%=clientcode%>"/>';

                            $("#txtNumbers").attr("placeholder", placeholder_text);

                        });



                        function trackingGraph(bookno) {
                            openLoader();
                            $.ajax({
                                type: "POST",
                                cache: false,
                                //async : false,
                                url: "TrackingAjax?bookno=" + encodeURIComponent(bookno),
                                data: $(this).serializeArray(),
                                success: function (data) {
                                    closeLoader();
                                    $.fancybox(data, {
                                        showCloseButton: false,
                                        closeClick: false,
                                        maxWidth: 1200,
                                        maxHeight: 600,
                                        fitToView: true,
                                        width: '100%',
                                        height: '100%',
                                        autoSize: true,
                                        scrollOutside: true,
                                        hideOnOverlayClick: false,
                                        helpers: {
                                            overlay: {
                                                closeClick: false
                                            }
                                        },
                                        keys: {
                                            close: null
                                        },
                                        afterClose: function () {
                                        },
                                        afterShow: function () {

                                        }

                                    });
                                },
                                error: function (data, xhr) {
                                    if (xhr.status == 403) {
                                        window.location.href = "SessionExpired.jsp";
                                    }
                                }
                            });
                        }
                        function callTrackingForLogin(type, value) {
                            // $("#searchbtn").button('loading');
                            $('#resetfunc').val('Y');
                            var j = 0;
                            var MdlNo = value;
                            var m_array = trim(value).split(/\s+/);
                            var con_array = new Array();
                            for (var i = 0; i < m_array.length; i++) {
                                con_array.push(m_array[i]);
                            }
                            MdlNo = con_array;
                            openLoader();
                            $.ajax({
                                type: "POST",
                                url: "TrackingDetails.do?Mdl=" + type + "&MdlNo=" + escape(MdlNo) + "&ldate=" + Date.parse(new Date()) + "&tmp=" + "&trackingviahome=Y",
                                success: function (response) {
                                    $("#trackingSearch").html(response);
                                    closeLoader();
                                    //  $("#searchbtn").button('reset');
                                },
                                error: function (xhr) {
                                    if (xhr.status == 403) {
                                        window.location.href = "SessionExpired.jsp";
                                    }
                                }
                            });
                        }

</script>




