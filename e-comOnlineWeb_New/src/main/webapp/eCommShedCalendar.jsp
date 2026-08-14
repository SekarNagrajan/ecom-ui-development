<%-- 
    Document   : schedulebetweencalendar
    Created on : 4 Jul, 2024, 11:52:32 AM
    Author     : preethi.elumalai
--%>

<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.google.gson.Gson"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.*,net.ecom.vo.Route,net.ecom.common.Validation,net.ecom.vo.CutoffConfigVO,net.ecom.common.GeneralConf" errorPage="" %>
<link href="Design\assets\plugins\Calendar\main.min.css" rel="stylesheet">
<script src="Design\assets\plugins\Calendar\main.min.js"></script>
<!-- Custom Curelean theme CSS -->

<style>
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
    }
    #calendar {
        width: 100%;
        height: 100vh; /* Full viewport height */
        max-width: 100%;
        max-height: 100vh; /* Ensure the height doesn't exceed viewport height */
        overflow: hidden; /* To prevent any unwanted scrollbars */
    }
    .fc-event-title{
        color: blue;
    }
</style>
</head>
<body>
    <div id="calendar"></div>

    <%
        String bwlocpol = (String) session.getAttribute("bwlocpol");
        String bwlocpod = (String) session.getAttribute("bwlocpod");
        String bwlocfromdate = (String) session.getAttribute("bwlocdeptdate");
        String bwloctodate = (String) session.getAttribute("bwlocarrdate");
        String eventList = (String) session.getAttribute("jsonEventDataList");
    %>

    <script>
        function hideDivsExcept(exceptionId) {
            $('#export_button.table-responsive').hide();
            document.querySelectorAll('#locationSearchtable .details_panel').forEach(div => {
                if (div.id === exceptionId) {
                    div.style.display = 'block';
                } else {
                    div.style.display = 'none';
                }
            });
        }
        function convertToISO8601(dateString) {
            // Define a mapping of month names to numbers
            var monthMap = {
                'Jan': '01',
                'Feb': '02',
                'Mar': '03',
                'Apr': '04',
                'May': '05',
                'Jun': '06',
                'Jul': '07',
                'Aug': '08',
                'Sep': '09',
                'Oct': '10',
                'Nov': '11',
                'Dec': '12'
            };

            console.log('dateString', dateString);

            // Extract parts of the date string
            var parts = dateString.trim().split(/[-\s:]+/);
            var day = parts[0];
            var month = parts[1];
            var year = parts[2];
            var time = parts[3] + ':' + parts[4];

            console.log('[day, month, year, time]', [day, month, year, time]);

            // Check if the month is valid
            if (!monthMap[month]) {
                throw new Error('Invalid month: ' + month);
            }

            // Construct the ISO8601 date string
            var monthNumber = monthMap[month];
            var hour = time.split(':')[0];
            var minute = time.split(':')[1];
            var isoDateString = year + '-' + monthNumber + '-' + day + 'T00:00:00';


            console.log('isoDateString' + isoDateString);
            return isoDateString;
        }
        function convertdateToISO8601(dateString, isStartDate) {
            // Define a mapping of month names to numbers
            const monthMap = {
                'Jan': '01',
                'Feb': '02',
                'Mar': '03',
                'Apr': '04',
                'May': '05',
                'Jun': '06',
                'Jul': '07',
                'Aug': '08',
                'Sep': '09',
                'Oct': '10',
                'Nov': '11',
                'Dec': '12'
            };

            // Extract parts of the date string
            const parts = dateString.trim().split('-');
            const day = parts[0];
            const month = parts[1];
            const year = parts[2];

            // Construct the ISO8601 date string
            const monthNumber = monthMap[month];
            let isoDateString;
            if (isStartDate) {
                isoDateString = year + '-' + monthNumber + '-' + '01' + 'T00:00:00';
            } else {
                const lastDay = new Date(year, monthNumber, 0).getDate();
                isoDateString = year + '-' + monthNumber + '-' + lastDay + 'T00:00:00';
            }

            console.log('isoDateString>' + isoDateString + "dateString>>" + dateString);
            return isoDateString;
        }
        $(document).ready(function () {
            var calendarEl = document.getElementById('calendar');
            var eventDataList = <%=eventList%>;
            console.log(eventDataList);

            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                locale: 'en',
                fixedWeekCount: false,
                validRange: {
                    start: convertdateToISO8601('<%=bwlocfromdate%>', true),
                    end: convertdateToISO8601('<%=bwloctodate%>', false)
                },
                selectable: true,
                selectHelper: true,
                displayEventTime: false,
                themeSystem: 'bootstrap', // Disable time display
                events: eventDataList.map(item => {
                    let listtitle = item.event.title.split('~~');
                    let eventitle = listtitle[0];
                    let scheduleid = listtitle[1];
                    let tooltipdata = listtitle[2];
                    let eventStart = convertToISO8601(item.event.start);
                    // Assuming you want the same date for start and end
                    let eventEnd = eventStart;
                    return {
                        ...item.event,
                        title: eventitle,
                        classNames: ['highlighted-service'],
                        start: eventStart,
                        end: eventEnd,
                        extendedProps: {
                            Data: scheduleid,
                            ToolTipData: tooltipdata
                        }
                    };
                }),
                eventClick: function (info) {
                    console.log(info.event.extendedProps.Data);
                    var scheduledivid = info.event.extendedProps.Data;
                    console.log('>>>>>' + scheduledivid);
                    hideDivsExcept(scheduledivid);
                    $('#locationSearchtable').show();
                    $('html, body').animate({
                        scrollTop: $('#locationSearchtable').offset().top
                    },700); 
                },
                eventDidMount: function (info) {
                     let eventParts = info.event.extendedProps.ToolTipData;
                    $(info.el).tooltip({
                        title:eventParts, 
                        placement: 'top',
                        html: true,
                        container: 'body'
                    });
                }
            });
            calendar.render();
        });
    </script>
</body>
</html>
