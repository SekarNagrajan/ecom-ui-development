
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="net.ecom.vds.serviceVo.NoticeVO"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.Hashtable,net.ecom.common.GeneralConf,net.ecom.common.Validation,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    String clientcode = (String) session.getAttribute("clientcode");
%>
<style>
    /*    #datatablebynotice.table-bordered td {
            border-right: 0px !important;  Removes the right border 
        }*/
    table.table-bordered td {
        border-right: 0px !important;
    }
    #datatablebynotice thead {
        display: none;
    }
</style>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.notice" bundle="<%=clientcode%>"/></div>
<div class="details_search page_bottom_content">

    <form name="noticesummary" method="post"  id="nopticesummary" >
        <div class="table-responsive card card-body card-shadow " style="padding:39px">
            <div class="group-area">
                <div class="row" style="margin-bottom: 17px">
                    <div class="col-sm-12 col-md-6">
                        <div class="text-left">
                            <label style="display:flex;align-items: center" >Year&nbsp
                                <select id="yearFilter" class="custom-select custom-select-sm form-control form-control-sm" style="width: 99px" >

                                </select> 
                            </label>
                        </div>
                    </div>
                    <div class="col-sm-12 col-md-6">
                        <div class="text-right" >
                            <label style="display:ruby" >Search:
                                <input type="text" id="searchInput" class="form-control form-control-sm " style="width: 229px;" >
                            </label>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <table class="table table-striped table-bordered dataTable no-footer" id="datatablebynotice">
                        <thead>
                            <tr>
                                <th>title</th>
                                <th>crdate</th></tr>
                        </thead>
                        <tbody>
                            <%
//                                String userCode = (String) session.getAttribute("SESSION_USERCODE");
                                ArrayList<NoticeVO> notice = (ArrayList<NoticeVO>) session.getAttribute("noticeView");
                                String title = "", crDate = "", filename = "", year = "";
                                if (notice != null && !notice.isEmpty()) {
                                    for (NoticeVO vo : notice) {
                                        title = vo.getTitle() != null ? vo.getTitle() : "";
                                        crDate = vo.getCrdate() != null ? vo.getCrdate() : "";
                                        filename = vo.getFilename() != null ? vo.getFilename() : "";
//                                        year = String.valueOf(LocalDateTime.parse(crDate, DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")).getYear());

                                        System.out.println("[ECOM-WAR] Notice Summary title // crDate // filename: " + title + " // " + crDate + " // " + filename + year);
                            %>
                            <tr>
                                <td>
                                    <a href="javascript:void(0);" onclick="downloadPdf('<%=filename%>')">
                                        <span style="color: #276fbe;"><%=title%></span>
                                    </a>
                                </td>
                                <td><%=crDate%></td>
                            </tr>
                            <%
                                } // End of for loop
                            } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <!--<input type="hidden" id="usercode" name="usercode" value="<userCode>">-->
            <!--<input type="hidden" id="appversion" name="appversion">-->
    </form>
</div>
<script type="text/javascript" src="Design/app-assets/vendors/js/tables/datatable/datatables.min.js"></script>
<script>
                                        $(document).ready(function () {
                                            //Show the Year Drop Down 
                                            var currentYear = new Date().getFullYear();
                                            var yearFilter = $('#yearFilter');
                                            for (var i = 0; i < 3; i++) {
                                                var yearOption = "<option value=\"" + (currentYear - i) + "\">" + (currentYear - i) + "</option>";
                                                yearFilter.append(yearOption);
                                            }

                                            // Search or Filter based on Title .
                                            $('#searchInput').on('keyup', function () {
                                                let searchValue = $(this).val().toLowerCase();
                                                $('#datatablebynotice tbody tr').filter(function () {
                                                    $(this).toggle($(this).find('td:first-child').text().toLowerCase().indexOf(searchValue) > -1);
                                                });
                                            });

                                          //Year Filter Change event Function 
                                            $('#yearFilter').on('change', function () {
                                                let selectedYear = $(this).val();
                                                $.ajax({
                                                    type: "POST",
                                                    cache: false,
                                                    url: "NoticeAjax?year=" + selectedYear,
                                                    data: $(this).serializeArray(),
                                                    success: function (data) {
                                                        try {
                                                            var jsonArray = JSON.parse(data);
                                                        } catch (e) {
                                                            console.error("Failed to parse JSON:", e);
                                                        }

                                                        var tableBody = $("#datatablebynotice tbody");
                                                        tableBody.empty();

                                                        if (Array.isArray(jsonArray)) {
                                                            $.each(jsonArray, function (index, item) {
                                                                var row = $("<tr>");
                                                                row.append($("<td>").html(
                                                                        '<a href="javascript:void(0);" onclick="downloadPdf(\'' + item.filename + '\')">' +
                                                                        '<span style="color: #276fbe;">' + item.title + '</span>' +
                                                                        '</a>'
                                                                        ));
                                                                row.append($("<td>").text(item.crdate));
                                                                $('#datatablebynotice tbody').append(row);
                                                            });
                                                        } else {
                                                            console.error("Expected an array, but got:", jsonArray);
                                                        }
                                                    },
                                                    error: function (data, xhr) {
                                                        closeLoader();
                                                        if (xhr.status == 403) {
                                                            window.location.href = "SessionExpired.jsp";
                                                        }
                                                        //$("#mgtemp").html("Manage Template");
                                                    }
                                                });
                                            });
//                                            // dataTable Paging
                                            $('#datatablebynotice').DataTable({
                                                pagingType: 'simple_numbers',
                                                dom: 'tp',
                                                ordering: false,
                                            });

                                        });
                                        //Title ckic event with PDF Formate 
                                        function downloadPdf(filename) {
                                            $.ajax({
                                                url: "/ecomonline/downloadPdf",
                                                type: "GET",
                                                data: {
                                                    filename: filename
                                                },
                                                xhrFields: {
                                                    responseType: 'blob' // Set the response type to 'blob' to handle binary data like PDFs
                                                },
                                                success: function (data, status, xhr) {
                                                    var blob = new Blob([data], {type: 'application/pdf'});
                                                    var url = window.URL.createObjectURL(blob);
                                              window.open(url, '_blank');
                                                },
                                                error: function (xhr, status, error) {
                                                    alert("Error downloading PDF: " + error);
                                                }
                                            });
                                        }

</script>
<script type="text/javascript">
    // Get XmlHttpRequest object for auto-suggest functionality
    var searchReq = getXmlHttpRequestObject();
    function getXmlHttpRequestObject() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert("<bean:message key='ecom.msg.browsernotsupported' bundle='<%=clientcode%>'/>");
            return null;
        }
    }



</script>
