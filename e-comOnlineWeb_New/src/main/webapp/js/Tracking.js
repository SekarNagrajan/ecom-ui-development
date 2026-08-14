// JavaScript Document

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
var companyRequest = getXmlHttpRequestObject();
var searchReq = getXmlHttpRequestObject();
var searchRequest = getXmlHttpRequestObject();

function getXmlHttpRequestObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert(incompatibleforAjax);
    }
}

function validateEmailToCommon(value) {
    var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    return (regex.test(value)) ? true : false;
}


function validEmailCheckToCommon(idname) {
    var email = document.getElementById(idname).value;
    email = email.replace(",,", ",");
    email = email.replace(";,", ",");
    email = email.replace(";,", ",");
    email = email.replace(",;", ",");
    email = email.replace(/;/gi, ",");
    var result = email.replace(/\s/g, "").split(/,|;/);
    for (var i = 0; i < result.length; i++) {
        if (!validateEmailToCommon(result[i])) {
            return false;
        }
    }
    document.getElementById('email_text').value = result;
    return true;
}


function  EmailValidtion(field) {
    var emailId = trim(document.getElementById(field).value);
    if (document.getElementById('email_text').value.length == 0) {
        $('#mailreq').html(reqField);
        $('#mailreq').show();
        document.getElementById("email_text").focus();
        return false;
    } else if (emailId.length > 0) {
        if (!validEmailCheckToCommon(trim(field))) {
            $('#mailreq').html(invalidMailAlert);
            $('#mailreq').show();
            document.getElementById(field).focus();
            return false;
        }
    }
    return true;
}

function sendMail()
{
    if (EmailValidtion('email_text')) {
        $('#mailreq').hide();
        $('#mailbtn').html("<i class='fa fa-spinner fa-spin'></i> Sending...");
        if (searchReq.readyState == 4 || searchReq.readyState == 0)
        {
            /*var mailcontent = getTrackingcontent('mail');
             mailcontent = mailcontent.replace('@printbutton', '');
             mailcontent = mailcontent + '<style>html body{height:100%;background-color:#f5f7fa;direction:ltr}body{font-family:Lato,sans-serif!important;font-size:14px!important;background:#e4dede;background-size:cover}body{font-weight:500!important}body{margin:0;font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";font-size:1rem;font-weight:400;line-height:1.5;color:#212529;text-align:left;background-color:#fff}.p-r-30{padding-right:30px}.p-l-30{padding-left:30px}*{-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box}*,::after,::before{box-sizing:border-box}div{display:block}html body{height:100%;background-color:#f5f7fa;direction:ltr}body{font-family:Lato,sans-serif!important;font-size:14px!important;background:#e4dede;background-size:cover}.row{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;margin-right:-15px;margin-left:-15px}.card-header{padding:.75rem1.25rem;margin-bottom:0;background-color:rgba(0,0,0,.03);border-bottom:1pxsolidrgba(0,0,0,.125)}.fixed-top{position:fixed;top:0;right:0;left:0;z-index:1030}.card-header{border-bottom:none}.card-header:first-child{border-radius:calc(.25rem-1px) calc(.25rem-1px) 00}.m-t-0{margin-top:0!important}.page_header{font-size:17px!important;font-weight:700!important;background:#eaf7fd;border:none;box-shadow:-1px1px6px-1pxgrey;margin-bottom:30px;margin-top:56px;margin-left:0;margin-right:-30px;text-transform:uppercase;padding:7px15px;z-index:5;font-family:Montserrat,Georgia,"Times New Roman",Times,serif;color:#404e67!important}.header_icon_pages{width:50px;height:50px;margin-top:-3px}img{vertical-align:middle;border-style:none}.form-group{margin-bottom:20px}.table-responsive{display:block;width:100%;overflow-x:auto;-webkit-overflow-scrolling:touch}.m-t-70{margin-top:70px!important}.note-info{background-color:#d6d8d8;border-color:#2492a5!important;border-left:6pxsolid;border-radius:5px;padding-bottom:10px;margin-top:20px}.col,.col-1,.col-10,.col-11,.col-12,.col-2,.col-3,.col-4,.col-5,.col-6,.col-7,.col-8,.col-9,.col-auto,.col-lg,.col-lg-1,.col-lg-10,.col-lg-11,.col-lg-12,.col-lg-2,.col-lg-3,.col-lg-4,.col-lg-5,.col-lg-6,.col-lg-7,.col-lg-8,.col-lg-9,.col-lg-auto,.col-md,.col-md-1,.col-md-10,.col-md-11,.col-md-12,.col-md-2,.col-md-3,.col-md-4,.col-md-5,.col-md-6,.col-md-7,.col-md-8,.col-md-9,.col-md-auto,.col-sm,.col-sm-1,.col-sm-10,.col-sm-11,.col-sm-12,.col-sm-2,.col-sm-3,.col-sm-4,.col-sm-5,.col-sm-6,.col-sm-7,.col-sm-8,.col-sm-9,.col-sm-auto,.col-xl,.col-xl-1,.col-xl-10,.col-xl-11,.col-xl-12,.col-xl-2,.col-xl-3,.col-xl-4,.col-xl-5,.col-xl-6,.col-xl-7,.col-xl-8,.col-xl-9,.col-xl-auto{position:relative;width:100%;padding-right:15px;padding-left:15px}.Discla_note{color:#c9780a!important}.alert{position:relative}.alert-danger{color:#721c24;background-color:#f8d7da;border-color:#f5c6cb}.alert{position:relative;padding:.75rem1.25rem;margin-bottom:1rem;border:1pxsolidtransparent;border-radius:.25rem}label{font-size:14px!important;font-weight:600!important;margin-bottom:0}label{display:inline-block;margin-bottom:.5rem}table{border-collapse:collapse}.card{position:relative;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;min-width:0;word-wrap:break-word;background-color:#fff;background-clip:border-box;border:1pxsolidrgba(0,0,0,.125);border-radius:.25rem}.card-body{-ms-flex:11auto;flex:11auto;padding:1.25rem}.card{margin-bottom:1.875rem;border:none;border-radius:0;-webkit-box-shadow:10px40px0rgba(62,57,107,.07),2px9px0rgba(62,57,107,.06);box-shadow:10px40px0rgba(62,57,107,.07),2px9px0rgba(62,57,107,.06)}.card-shadow{border-radius:8px}.container{width:100%;padding-right:15px;padding-left:15px;margin-right:auto;margin-left:auto}.txt_tra_clr{color:#6bb8dc;font-size:15px;padding-left:20px;font-weight:700}.txt_tra_data{font-weight:700;font-size:18px;color:#000}.hori_trackig_line{margin-top:15px;margin-bottom:15px;border-bottom:3pxsolid#162749}.col-sm-5{-ms-flex:41.666667%;flex:41.666667%;max-width:41.666667%}.col-sm-2{-ms-flex:16.666667%;flex:16.666667%;max-width:16.666667%}.stepwizard{display:table;width:100%;position:relative;margin-bottom:50px;margin-top:20px}.stepwizard .stepwizard-row{display:block}.stepwizard .stepwizard-row:before{top:16px;bottom:0;position:absolute;content:" ";width:100%;height:1px;background-color:#e8ebf2;z-index:0;border-bottom-style:dashed;border-bottom-color:#ccc}.tracker_icon{width:50px;height:50px;margin-top:-4px;position:absolute;margin-left:-21px}img{vertical-align:middle;border-style:none}.txt_port_name{margin-top:40px;position:absolute;font-weight:700;margin-left:-30px}.track_ship_20{left:20%;position:absolute;margin-top:-10px}.stepwizard .stepwizard-row .stepwizard-step{display:block;position:relative}.txt_port_name_right{margin-top:40px;position:absolute;font-weight:700;white-space:nowrap;float:right;word-break:break-word;text-align:left;right:-20px}.track_ship_20{left:20%;position:absolute;margin-top:-10px}.table_even_odd{margin-top:40px}.table{width:100%;margin-bottom:1rem;color:#212529}table{border-collapse:collapse}.table thead th{border-right:0!important;font-size:12px!important}.table-borderless thead th{border-top:1pxsolid#e3ebf3}.table thead th{vertical-align:bottom;border-bottom:2pxsolid#e3ebf3;border-top:1pxsolid#e3ebf3}.table_even_odd tbody tr:nth-child(odd){background:#e3f4fa}.table td,.table th{padding:.75rem;vertical-align:top;border-top:1pxsolid#dee2e6}.table-borderless tbody+tbody,.table-borderless td,.table-borderless th,.table-borderless thead th{border:0}.table td,.table th{border-bottom:1pxsolid#e3ebf3;padding:.75rem2rem}.table td,.table th{padding:.75rem}.table thead th{vertical-align:bottom;border-bottom:2pxsolid#dee2e6}.table-borderless tbody+tbody,.table-borderless td,.table-borderless th,.table-borderless thead th{border:0}.table thead th{vertical-align:bottom;border-bottom:2pxsolid#e3ebf3;border-top:1pxsolid#e3ebf3}.table-borderless thead th{border-top:1pxsolid#e3ebf3}.table thead th{border-right:0!important;font-size:12px!important}img{display:none}table th{text-align:left;}</style>';*/
            var mailcontent = '<!DOCTYPE html><html><head><meta charset="UTF-8"><style type="text/css">.mail_header{text-align:center;text-decoration:underline}.ref_table tbody tr td,.ref_table thead tr th{width:33.33%;text-align:center}.ref_table tbody tr td{font-weight:700;font-size:18px;color:#000}.ref_table{width:100%;padding:10px;border-bottom:1px solid;border-bottom-color:#e0e0e0}.port_table thead tr th,.ref_table thead tr th{color:#808080ab;color:rgba(0,0,0,.54)}.port_table tbody tr td,.ref_table tbody tr td{font-size:18px}.ref_table tbody tr td{color:#03a9f4}.port_table tbody tr td{color:#34a853}.port_table{width:100%;border-bottom:1px solid;border-bottom-color:#ea4335;padding:10px}.port_table tbody tr td,.port_table thead tr th{width:20%;text-align:center}.track_table{width:100%;text-align:center;padding:10px}.track_table thead tr th{background:#51c1ac;color:#fff}.desc_table{color:#c9780a;font-size:14px;font-weight:700;background-color:#e5e8e8;border-color:#2492a5!important;border-left:6px solid;border-radius:5px;padding-bottom:10px;width:100%}.track_table tbody tr:nth-child(even){background:#e3f4fa}body{background:#f5f7fa}.bkg_split{margin-bottom:1.875rem;border:none;box-shadow:0 10px 40px 0 rgba(62,57,107,.07),0 2px 9px 0 rgba(62,57,107,.06);border-radius:8px;background:#fff;padding-top:10px;padding-bottom:20px}.container{padding-right:15px;padding-left:15px;margin-right:auto;margin-left:auto}</style></head><body>	<div class="container">	<h1 class="mail_header">Tracking Report</h1><div id="trackingmail">';
            $('.container_track_mail').each(function () {
                $('#hiddenmail').find('.origin').text($(this).find('.mail_origin').text());
                $('#hiddenmail').find('.delivery').text($(this).find('.mail_delivery').text());
                $('#hiddenmail').find('.book_no').text($(this).find('.mail_bkgno').text());
                $('#hiddenmail').find('.bl_no').text($(this).find('.mail_blno').text());
                $('#hiddenmail').find('.eta').text($(this).find('.mail_eta').text());

                var movedtls = '';
                //$(this).find('.trackingtable').find('tbody').find('tr').find('mailcontent').each(function () { 
                $('.mailcontent').each(function () {
                    movedtls += '<tr>';
                    movedtls += '<td>' + $(this).find('.mail_contno').text() + '</td>';
                    movedtls += '<td>' + $(this).find('.mail_latestact').text() + '</td>';
                    movedtls += '<td>' + $(this).find('.mail_actloc').text() + '</td>';
                    movedtls += '<td>' + $(this).find('.mail_conttype').text() + '</td>';
                    movedtls += '<td>' + $(this).find('.mail_actdate').text() + '</td>';
                    movedtls += '</tr>';
                }); 
                $('#hiddenmail').find('.track_table > tbody').html(movedtls);

                mailcontent = mailcontent + $('#hiddenmail').html();
                $('#hiddenmail').find('.track_table > tbody').html('');

            });
            mailcontent = mailcontent + '</div><table class="desc_table"><tbody><tr><td> Disclaimer:</td></tr><tr><td> These details are subject to change with or without prior intimation from our end.</td></tr>			<tr><td> The date(s) & time(s) represented on the tracking results above correspond to local time zone of the related port location.</td></tr><tr><td> Tracking information is dynamic in nature and generated basis current sailing schedule details available.</td></tr></tbody></table></div></body></html>';
            var id = document.getElementById('email_text').value;
            mailcontent = encodeURIComponent(mailcontent);
            var urlStr = "ecomAjax?type=sendMail&text = " + mailcontent + "&id=" + id;
            searchRequest.open("POST", "ecomAjax", true);
            searchRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            var params = "type=sendMail&text=" + mailcontent + "&id=" + id + "&date" + new Date();
            searchRequest.onreadystatechange = sentMail;
            searchRequest.send(params);
        }
    }
}

function sentMail() {
    if (searchRequest.readyState == 4) {
        $('#mailbtn').html('<i class="fa fa-envelope-o"></i> Send Mail');
        var response = searchRequest.responseText;
        document.getElementById('email_text').value = "";
        $('#myModal').modal('hide');
        if (response.toUpperCase() == "INVALID") {
            alert(mailNotDelivered);

        } else {
            alert(trckgsent);
            return false;
        }
    }
}

function makePrint(logo)
{
    var trackPrint = window.open("about:blank");
    var printcontent = getTrackingcontent('print');
    printcontent = printcontent.replace('@printbutton', '<div class="form-group" id="printbtn"><div class="row "><div class="col-sm-12"><div class="col-sm-1 offset-md-5"><button onclick="doPrint();" class="btn btn-info btn-sm"><span class="fa fa-print"> Print</span></button></div></div></div></div><script>function doPrint(){document.getElementById("printbtn").style.display = "none";window.print();document.getElementById("printbtn").style.display = "block";}</script>');
    trackPrint.document.writeln(printcontent);
    trackPrint.document.close();

}

function closeMail()
{
    document.getElementById('email_text').value = "";
}

function getTrackingcontent(type)
{
    if (!$('#hiddenprintormail').find('.bodydiv').find('.page_header').is('.page_header')) {

        $('#hiddenprintormail').find('.bodydiv').html($(document).find('.app-content').html());
        $('#hiddenprintormail').find('.bodydiv').append();
        $('#hiddenprintormail').find('.remove_print').remove();
        $('#hiddenprintormail').find('script').remove();
        $('#hiddenprintormail').find('.page_header').addClass('m-t-0');
        $('#hiddenprintormail').find('.note-info').addClass('m-t-70');
        $('#hiddenprintormail').find('.page_scrol_data').css('opacity', '1');
        $('#hiddenprintormail').find('.header_icon_pages').css('width', '50px').css('height', '50px');
        $("#hiddenprintormail thead tr th:last-child").remove();
        $("#hiddenprintormail tbody tr td:last-child").remove();
        $('#hiddenprintormail').find('#trackingSearch').addClass('m-t-80');
        /*if(type=='mail'){
         $('#hiddenprintormail').find('img').each(function(){
         $(this).replaceWith('<span class="'+$(this).attr('class')+'" style="background-image:url('+$(this).attr('src')+');background-repeat: no-repeat;"></span>')
         });
         }*/
    }
    var trackingcontent = '<!DOCTYPE html><html lang="en"><head>' + $('#hiddenprintormail').find('.headdiv').html() + '</head><body><div class="p-l-30 p-r-30">' + $('#hiddenprintormail').find('.bodydiv').html() + ' @printbutton </div></body></html>';
    $('#hiddenprintormail').find('.bodydiv').html('');
    return trackingcontent;
}