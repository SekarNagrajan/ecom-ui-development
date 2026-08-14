/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var wsocket;

function connect() {
    console.log('entered web socket');
    var username = $("#logincustcode").val();
    if (username) {
        console.log(username);
        //checkNotification(username);
        console.log(username);
        console.log(getNotifyRootUri() + "/EcomNotifyService/sendEcomNotification/username=" + username + "");
        wsocket = new WebSocket(getNotifyRootUri() + "/EcomNotifyService/sendEcomNotification/username=" + username + "");
        wsocket.onmessage = onNotifyMessage;
        wsocket.onclose = function () {
            // connection closed, discard old websocket and create a new one in 5s
            console.log('Websocket closed');
            wsocket = null
            setTimeout(connect, 2000);
        }
        console.log('----------' + wsocket.onmessage);
    }
}

function getNotifyRootUri() {

    var wsnotifyurl = document.location.hostname === "" ? "localhost" : document.location.hostname;
    /*var urlSplit = wsnotifyurl.split(".");
    if (wsnotifyurl.indexOf('solverminds') > 0) {
        wsnotifyurl = wsnotifyurl.replace(urlSplit[0], 'lrpv2');
        console.log(wsnotifyurl);
    } else if (wsnotifyurl.indexOf('emiratesline') > 0) {
        wsnotifyurl = wsnotifyurl.replace(urlSplit[0], 'erp');
        console.log(wsnotifyurl);
    }*/
    return (document.location.protocol === "https:" ? "wss://" : "ws://") + (wsnotifyurl) + (document.location.port === "" ? "" : (":" + document.location.port));
}


function onNotifyMessage(evt) {
    console.log('evt.data-----------');
    onSendMsg(evt.data);
    var msg = evt.data.split("@@@@")[0];
    var msgid = evt.data.split("@@@@")[1];
    loadNewNotificationMessage(msg,msgid);

}
window.addEventListener("load", connect, false);


function onSendMsg(data) {
    var msg = data;
    //
    var body = msg.split("@@@@")[0];
    var head = "Ecommerce";
    //var body = msg;
    console.log('onSendMsg');
    Push.create(head, {
        body: body,
        timeout: 50000,
        onClick: function () {
            console.log(this);
        }
    });
}

function loadNewNotificationMessage(data,msgid) {
    var currentnotifycount = parseInt($('#notificationcount').text());
    $('#notificationcount').text(currentnotifycount + 1);
    var formData = "messagedata=" + data+"&msgid="+msgid;
    $.ajax({
        type: "POST",
        cache: false,
        url: "UserProfileAjax?type=putNotificationMessage",
        async: false,
        data: formData,
        success: function (data) {
           // console.log(data);
            $('#EcomNotificationBox').prepend(data);
        },
        error: function (xhr) {
            if (xhr.status == 403) {
                window.location.href = "SessionExpired.jsp";
            }
        }
    });

}

function onNotificationMessageViewAction(msgid) {
    if (msgid != null && msgid.length > 0 && $('#' + msgid).is('.active')) {
        $.ajax({
            type: "POST",
            cache: false,
            url: "UserProfileAjax?type=putnotificationviewdetails&msgid=" + msgid,
            async: false,
            success: function (data) {
                console.log(data);
                $('#' + msgid).removeClass('active');
                $('#' + msgid).removeAttr('onclick');
                var currentnotifycount = parseInt($('#notificationcount').text());
                $('#notificationcount').text(currentnotifycount - 1);
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
}


$(function () {
    $(".EcomNotification").on('click', function () {
        console.log("Notification icon button click");
        //$('.helpline_btn').addClass('d-none');
        if ($(".notification_box").is('.open')) {
            $('.notification_box').removeClass('open');
        } else {
            $('.notification_box').addClass('open');
        }
    });
    $(".notificationbox_close").on('click', function () {
        console.log("Notification Close button click");
        //$('.helpline_btn').removeClass('d-none');
        $('.notification_box').removeClass('open');
    });
});

