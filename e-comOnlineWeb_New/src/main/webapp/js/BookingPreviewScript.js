/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function setBkgPreviewDetails(bkgtype) {
    var tableval;
    //$('#bkgpreviewtable>tbody>').last().after("");
    var carriagejson = {"C": "Carrier", "M": "Merchant", "N": "N/A"};
    var ensjson = {"S": "Single", "P": "Partial", "N": "N/A"};
    var ensTypeJson = {"S": "Straight B/L", "C": "Consolidated"};
    $('#precarriage_label').text(carriagejson[$("#preCarriage").val()]);
    $('#oncarriage_label').text(carriagejson[$("#onCarriage").val()]);
    $('#ensfiling_label').text(ensjson[$("#ensfiling").val()]);
    $('#ensDocType_label').text(ensTypeJson[$("#ebook_EnsDocType").val()]);
    $('#t2lfiling_label').text($('#t2lfiling').val()=='Y' ? 'Yes' : 'No');
    $('#booking_ref_label').text($('#booking_refno').val());
    $('#booking_party_label').text($('#ebook_bookingparty_desc').val());
    $('#pickupplace_label').text($('#ebook_origin_country').val());
    $('#deliveryplace_label').text($('#ebook_delivery_country').val());
    $('#bookdate_label').text($('#ebook_date').val());
    $('#rateref_label').text($('#ebook_rate_reference').val());
    $('#agencyref_label').text($('#ebook_agency_reference').val());
    $('#oceanfreight_label').text($('#ebook_ocean_freight option:selected').text());
    if( $('#preferred_agency').length>0){
        var prefagency=preferredAgencyNameJson[$('#preferred_agency option:selected').val()] || $('#preferred_agency option:selected').val() ;
        $('#ebook_preferred_agency_label').text(prefagency);
    }
    //$('#carriercontact_label').text($('#carrierContact option:selected').text());
    if ($('#carrierContact').val() != '') {
        $('#carriercontact_label').text($('#carrierContact option:selected').text());
    } else {
        $('#carriercontact_label').text('');
    }
    if ($('#emptyPickup option:selected').text() != 'Select Empty pick up') {
        $('#emptypickup_label').text($('#emptyPickup option:selected').text());
    } else {
        $('#emptypickup_label').text('');
    }

    $('#emptypickupdate_label').text($('#emptyPickUpDate').val());
    if ($('#podintransit option:selected').text() != 'Select Place of Final Receipt') {
        $('#instransit_label').text($('#podintransit option:selected').text());
    } else {
        $('#instransit_label').text('');
    }

    $('#natcode_label').text($('#ebook_natcode').val());
    $('#haulercode_label').text($('#ebook_haulercode').val());
    $('#custpo_label').text($('#ebook_custpo').val());
    $('#reftype_label').text($('#ebook_reftype').val());
    $('#expref_label').text($('#ebook_exportref').val());
    $('#haulagetype_label').text(($('#ebook_haulagetype option:selected').text() == 'Select Haulage Type') ? '' : $('#ebook_haulagetype option:selected').text());
    $('#pickupdate_label').text($('#ebook_pickup_date').val());
    $('#dropdate_label').text($('#ebook_drop_date').val());
    $('#customer_reference_label').text($('#customerreference').val());
     if( $('#shipperReferenceType').length > 0){
    var shipperReferenceType = "";
    var value = $('#shipperReferenceType').val(); 
    if (Array.isArray(DPWShipperTypeListArr) && DPWShipperTypeListArr.length > 0) {
        for (var i = 0; i < DPWShipperTypeListArr.length; i++) { 
            // split on "-"
            var parts = DPWShipperTypeListArr[i].split("-");
            var code = parts[0].trim();       // "C"
    
            if (code === value) {
                shipperReferenceType = DPWShipperTypeListArr[i];
                break;
            }
        }
    }
    $('#shipperReferenceType_label').text($('#shipperReference' ).val().length > 0 ? shipperReferenceType : "");
    $('#shipperReference_label').text($('#shipperReference' ).val() || "");
    }
    $('#ACID_label').text($('#ACID').val());
    $('#previewRoute').html($('#routingdetails').html());
    $('#previewRoute').find('#routehref').remove();
    $('#previewRoute').find('#bkgshowDetails').attr('id', $('#previewRoute').find('#bkgshowDetails').attr('id') + '_preview');
    var transshowid = $('#previewRoute').find('#show_details').attr('onclick');
    if (typeof transshowid !== 'undefined') {
        transshowid = transshowid.replace('bkgshowDetails', 'bkgshowDetails_preview');
        $('#previewRoute').find('#show_details').attr('onclick', transshowid);
    }
    if ($('#routingdetails').html() == "")
    {
        $('#viewroute').hide();
    } else
    {
        $('#viewroute').show();
    }
    //customerdetails
    $('#bp_hd').show();
    $('#bookingname_label').text($('#ebook_bookingname').val());
    $('#bookingadress_label').text($('#ebook_bookingaddress').val());
    $('#bookingadress1_label').text($('#ebook_bookingaddress1').val());
    $('#bookingcountry_label').text($('#ebook_bookingcountry_name').val());
    $('#bookingcity_label').text($('#ebook_bookingcity_desc').val());
    $('#bookingphone_label').text($('#ebook_bookingphone').val());
    $('#bookingfax_label').text($('#ebook_bookingfax').val());
    $('#bookingemail_label').text($('#ebook_bookingemail').val());
    $('#bookingstate_label').text($('#ebook_bookingstate').val());
    if($('#ebook_bookingzipcode').length > 0){
        $('#bookingzipcode_label').text($('#ebook_bookingzipcode').val());
    }
    if($('#ebook_bookingeori').length > 0){
    $('#bookingeori_label').text($('#ebook_bookingeori').val());
    $('#bookingpersontype_label').text($('#bookingtypeperson').val());  
    }
    //

    if ($('#ebook_agreename').val().length > 0 || $('#ebook_agreeaddress').val().length > 0 || $('#ebook_agreeaddress1').val().length > 0 || $('#ebook_agreecountry_name').val().length > 0 || $('#ebook_agreecity_desc').val().length > 0 || ($('#ebook_agreefax').length > 0 && $('#ebook_agreefax').val().length > 0) || $('#ebook_agreeemail').val().length > 0 || $('#ebook_agreephone').val().length > 0)
    {
        $('#ap_hd').show();
        $('#agreename_label').text($('#ebook_agreename').val());
        $('#agreeadress_label').text($('#ebook_agreeaddress').val());
        $('#agreeadress1_label').text($('#ebook_agreeaddress1').val());
        $('#agreecountry_label').text($('#ebook_agreecountry_name').val());
        $('#agreecity_label').text($('#ebook_agreecity_desc').val());
        $('#agreephone_label').text($('#ebook_agreephone').val());
        $('#agreefax_label').text($('#ebook_agreefax').val());
        $('#agreeemail_label').text($('#ebook_agreeemail').val());
        $('#agreestate_label').text($('#ebook_agreestate').val());
        $('#agreeeori_label').text($('#ebook_agreeeori').val());
         console.log('ebooking agreeeori'+$('#ebook_agreeeori').val());
        $('#agreepersontype_label').text($('#agreetypeperson').val());
        if($('#ebook_agreezipcode').length > 0){
            $('#agreezipcode_label').text($('#ebook_agreezipcode').val());
        }
    } else
    {
        $('#ap_hd').hide();
    }
    //
    if ($('#ebook_shippername').val().length > 0 || $('#ebook_shipperaddress').val().length > 0 || $('#ebook_shipperaddress1').val().length > 0 || $('#ebook_shippercountry_name').val().length > 0 || $('#ebook_shippercity_desc').val().length > 0 || $('#ebook_shipperphone').val().length > 0 || ($('#ebook_shipperfax').length > 0 && $('#ebook_shipperfax').val().length > 0) || $('#ebook_shipperemail').val().length > 0)
    {
        $('#sh_hd').show();
        $('#shippername_label').text($('#ebook_shippername').val());
        $('#shipperadress_label').text($('#ebook_shipperaddress').val());
        $('#shipperadress1_label').text($('#ebook_shipperaddress1').val());
        $('#shippercountry_label').text($('#ebook_shippercountry_name').val());
        $('#shippercity_label').text($('#ebook_shippercity_desc').val());
        $('#shipperphone_label').text($('#ebook_shipperphone').val());
        $('#shipperfax_label').text($('#ebook_shipperfax').val());
        $('#shipperemail_label').text($('#ebook_shipperemail').val());
        $('#shipperstate_label').text($('#ebook_shipperstate').val());
        $('#shippereori_label').text($('#ebook_shippereori').val());
        $('#shipperpersontype_label').text($('#shippertypeperson').val());
        if($('#ebook_shipperzipcode').length > 0){
            $('#shipperzipcode_label').text($('#ebook_shipperzipcode').val());
        }
    } else
    {
        $('#sh_hd').hide();
    }
    //
    if ($('#ebook_consigneename').val().length > 0 || $('#ebook_consigneeaddress').val().length > 0 || $('#ebook_consigneeaddress1').val().length > 0 || $('#ebook_consigneecountry_name').val().length > 0 || $('#ebook_consigneecity_desc').val().length > 0 || $('#ebook_consigneephone').val().length > 0 || ($('#ebook_consigneefax').length > 0 && $('#ebook_consigneefax').val().length > 0) || $('#ebook_consigneeemail').val().length > 0)
    {
        $('#cn_hd').show();
        $('#consigneename_label').text($('#ebook_consigneename').val());
        $('#consigneeadress_label').text($('#ebook_consigneeaddress').val());
        $('#consigneeadress1_label').text($('#ebook_consigneeaddress1').val());
        $('#consigneecountry_label').text($('#ebook_consigneecountry_name').val());
        $('#consigneecity_label').text($('#ebook_consigneecity_desc').val());
        $('#consigneephone_label').text($('#ebook_consigneephone').val());
        $('#consigneefax_label').text($('#ebook_consigneefax').val());
        $('#consigneeemail_label').text($('#ebook_consigneeemail').val());
        $('#consigneestate_label').text($('#ebook_consigneestate').val());
        $('#consigneeeori_label').text($('#ebook_consigneeeori').val());
        $('#consigneepersontype_label').text($('#consigneetypeperson').val());
        if($('#ebook_consigneezipcode').length > 0){
            $('#consigneezipcode_label').text($('#ebook_consigneezipcode').val());
        }
    } else
    {
        $('#cn_hd').hide();
    }
    //
    if ($('#ebook_notifyname').val().length > 0 || $('#ebook_notifyaddress').val().length > 0 || $('#ebook_notifyaddress1').val().length > 0 || $('#ebook_notifycountry_name').val().length > 0 || $('#ebook_notifycity_desc').val().length > 0 || $('#ebook_notifyphone').val().length > 0 || ($('#ebook_notifyfax').length > 0 && $('#ebook_notifyfax').val().length > 0) || $('#ebook_notifyemail').val().length > 0)
    {
        $('#nt_hd').show()
        $('#notifyname_label').text($('#ebook_notifyname').val());
        $('#notifyadress_label').text($('#ebook_notifyaddress').val());
        $('#notifyadress1_label').text($('#ebook_notifyaddress1').val());
        $('#notifycountry_label').text($('#ebook_notifycountry_name').val());
        $('#notifycity_label').text($('#ebook_notifycity_desc').val());
        $('#notifyphone_label').text($('#ebook_notifyphone').val());
        $('#notifyfax_label').text($('#ebook_notifyfax').val());
        $('#notifyemail_label').text($('#ebook_notifyemail').val());
        $('#notifystate_label').text($('#ebook_notifystate').val());
        $('#notifyeori_label').text($('#ebook_notifyeori').val());
        $('#notifypersontype_label').text($('#notifytypeperson').val());
        if($('#ebook_notifyzipcode').length > 0){
            $('#notifyzipcode_label').text($('#ebook_notifyzipcode').val());
        }
    } else
    {
        $('#nt_hd').hide()
    }
    //
    if ((($('#nt1_hd').length > 0)) && ($('#ebook_notifyname_n1').val().length > 0 || $('#ebook_notifyaddress_n1').val().length > 0 || $('#ebook_notifyaddress1_n1').val().length > 0 || $('#ebook_notifycountry_name_n1').val().length > 0 || $('#ebook_notifycity_desc_n1').val().length > 0 || $('#ebook_notifyphone_n1').val().length > 0 || ($('#ebook_notifyfax_n1').length > 0 && $('#ebook_notifyfax_n1').val().length > 0) || $('#ebook_notifyemail_n1').val().length > 0))
    {
        $('#nt1_hd').show()
        $('#notifyname_label_n1').text($('#ebook_notifyname_n1').val());
        $('#notifyadress_label_n1').text($('#ebook_notifyaddress_n1').val());
        $('#notifyadress1_label_n1').text($('#ebook_notifyaddress1_n1').val());
        $('#notifycountry_label_n1').text($('#ebook_notifycountry_name_n1').val());
        $('#notifycity_label_n1').text($('#ebook_notifycity_desc_n1').val());
        $('#notifyphone_label_n1').text($('#ebook_notifyphone_n1').val());
        $('#notifyfax_label_n1').text($('#ebook_notifyfax_n1').val());
        $('#notifyemail_label_n1').text($('#ebook_notifyemail_n1').val());
        $('#notifystate_label_n1').text($('#ebook_notifystate_n1').val());
        $('#notifyeori_label_n1').text($('#ebook_notifyeori_n1').val());
        $('#notifypersontype_label_n1').text($('#notifytypeperson_n1').val());
        if($('#ebook_notifyzipcode_n1').length > 0){
            $('#notifyzipcode_label_n1').text($('#ebook_notifyzipcode_n1').val());
        }
    } else
    {
        $('#nt1_hd').hide()
    }
    //
    if ((($('#fw_hd').length > 0)) && ($('#ebook_forwardername').val().length > 0 || $('#ebook_forwarderaddress').val().length > 0 || $('#ebook_forwarderaddress1').val().length > 0 || $('#ebook_forwardercountry_name').val().length > 0 || $('#ebook_forwardercity_desc').val().length > 0 || ($('#ebook_forwarderfax').length > 0 && $('#ebook_forwarderfax').val().length > 0) || $('#ebook_forwarderemail').val().length > 0 || $('#ebook_forwarderphone').val().length > 0))
    {
        $('#fw_hd').show();
        $('#forwardername_label').text($('#ebook_forwardername').val());
        $('#forwarderadress_label').text($('#ebook_forwarderaddress').val());
        $('#forwarderadress1_label').text($('#ebook_forwarderaddress1').val());
        $('#forwardercountry_label').text($('#ebook_forwardercountry_name').val());
        $('#forwardercity_label').text($('#ebook_forwardercity_desc').val());
        $('#forwarderphone_label').text($('#ebook_forwarderphone').val());
        $('#forwarderfax_label').text($('#ebook_forwarderfax').val());
        $('#forwarderemail_label').text($('#ebook_forwarderemail').val());
        $('#forwarderstate_label').text($('#ebook_forwarderstate').val());
        $('#forwardereori_label').text($('#ebook_forwardereori').val());
        $('#forwarderpersontype_label').text($('#forwardertypeperson').val());
        if($('#ebook_forwarderzipcode').length > 0){
            $('#forwarderzipcode_label').text($('#ebook_forwarderzipcode').val());
        }
    } else
    {
        $('#fw_hd').hide();
    }
    //
    if ((($('#wh_id').length > 0)) && ($('#ebook_Warehousename').val().length > 0 || $('#ebook_Warehouseaddress').val().length > 0 || $('#ebook_Warehouseaddress1').val().length > 0 || $('#ebook_Warehousecountry_name').val().length > 0 || $('#ebook_Warehousecity_desc').val().length > 0 || ($('#ebook_Warehousefax').val().length > 0 && $('#ebook_Warehousefax').length > 0 || $('#ebook_Warehouseemail').val().length > 0) || $('#ebook_Warehousephone').val().length > 0))
    {
        $('#wh_hd').show();
        $('#Warehousename_label').text($('#ebook_Warehousename').val());
        $('#Warehouseadress_label').text($('#ebook_Warehouseaddress').val());
        $('#Warehouseadress1_label').text($('#ebook_Warehouseaddress1').val());
        $('#Warehousecountry_label').text($('#ebook_Warehousecountry_name').val());
        $('#Warehousecity_label').text($('#ebook_Warehousecity_desc').val());
        $('#Warehousephone_label').text($('#ebook_Warehousephone').val());
        $('#Warehousefax_label').text($('#ebook_Warehousefax').val());
        $('#Warehouseemail_label').text($('#ebook_Warehouseemail').val());
        $('#Warehousestate_label').text($('#ebook_Warehousestate').val());
        $('#Warehouseeori_label').text($('#ebook_Warehouseeori').val());
        $('#Warehousepersontype_label').text($('#Warehousetypeperson').val());
        if($('#ebook_Warehousezipcode').length > 0){
            $('#Warehousezipcode_label').text($('#ebook_Warehousezipcode').val());
        }
    } else
    {
        $('#wh_hd').hide();
    }
    if ((($('#nc_hd').length > 0) && $('#ebook_NamedCustomername').length > 0) && ($('#ebook_NamedCustomername').val().length > 0 || $('#ebook_NamedCustomeraddress').val().length > 0 || $('#ebook_NamedCustomeraddress1').val().length > 0 || $('#ebook_NamedCustomercountry_name').val().length > 0 || $('#ebook_NamedCustomercity_desc').val().length > 0 || ($('#ebook_NamedCustomerfax').val().length > 0 && $('#ebook_NamedCustomerfax').length > 0 || $('#ebook_NamedCustomeremail').val().length > 0) || $('#ebook_NamedCustomerphone').val().length > 0))
    {
        $('#nc_hd').show();
        $('#NamedCustomername_label').text($('#ebook_NamedCustomername').val());
        $('#NamedCustomeradress_label').text($('#ebook_NamedCustomeraddress').val());
        $('#NamedCustomeradress1_label').text($('#ebook_NamedCustomeraddress1').val());
        $('#NamedCustomercountry_label').text($('#ebook_NamedCustomercountry_name').val());
        $('#NamedCustomercity_label').text($('#ebook_NamedCustomercity_desc').val());
        $('#NamedCustomerphone_label').text($('#ebook_NamedCustomerphone').val());
        $('#NamedCustomerfax_label').text($('#ebook_NamedCustomerfax').val());
        $('#NamedCustomeremail_label').text($('#ebook_NamedCustomeremail').val());
        $('#NamedCustomerstate_label').text($('#ebook_NamedCustomerstate').val());
        $('#NamedCustomereori_label').text($('#ebook_NamedCustomereori').val());
        $('#NamedCustomerpersontype_label').text($('#NamedCustomertypeperson').val());
        if($('#ebook_NamedCustomerzipcode').length > 0){
            $('#NamedCustomerzipcode_label').text($('#ebook_NamedCustomerzipcode').val());
        }
    } else
    {
        $('#nc_hd').hide();
    }
if ((($('#sl_hd').length > 0) && $('#ebook_Sellername').length > 0) && ($('#ebook_Sellername').val().length > 0 || $('#ebook_Selleraddress').val().length > 0 || $('#ebook_Selleraddress1').val().length > 0 || $('#ebook_Sellercountry_name').val().length > 0 || $('#ebook_Sellercity_desc').val().length > 0 || ($('#ebook_Sellerfax').val().length > 0 && $('#ebook_Sellerfax').length > 0 || $('#ebook_Selleremail').val().length > 0) || $('#ebook_Sellerphone').val().length > 0))
    {
        $('#sl_hd').show();
        $('#Sellername_label').text($('#ebook_Sellername').val());
        $('#Selleradress_label').text($('#ebook_Selleraddress').val());
        $('#Selleradress1_label').text($('#ebook_Selleraddress1').val());
        $('#Sellercountry_label').text($('#ebook_Sellercountry_name').val());
        $('#Sellercity_label').text($('#ebook_Sellercity_desc').val());
        $('#Sellerphone_label').text($('#ebook_Sellerphone').val());
        $('#Sellerfax_label').text($('#ebook_Sellerfax').val());
        $('#Selleremail_label').text($('#ebook_Selleremail').val());
        $('#Sellerstate_label').text($('#ebook_Sellerstate').val());
        $('#Sellereori_label').text($('#ebook_Sellereori').val());
        $('#Sellerpersontype_label').text($('#Sellertypeperson').val());
        if($('#ebook_Sellerzipcode').length > 0){
            $('#Sellerzipcode_label').text($('#ebook_Sellerzipcode').val());
        }
    } else
    {
        $('#sl_hd').hide();
    }
if ((($('#by_hd').length > 0) && $('#ebook_Buyername').length > 0) && ($('#ebook_Buyername').val().length > 0 || $('#ebook_Buyeraddress').val().length > 0 || $('#ebook_Buyeraddress1').val().length > 0 || $('#ebook_Buyercountry_name').val().length > 0 || $('#ebook_Buyercity_desc').val().length > 0 || ($('#ebook_Buyerfax').val().length > 0 && $('#ebook_Buyerfax').length > 0 || $('#ebook_Buyeremail').val().length > 0) || $('#ebook_Buyerphone').val().length > 0))
    {
        $('#by_hd').show();
        $('#Buyername_label').text($('#ebook_Buyername').val());
        $('#Buyeradress_label').text($('#ebook_Buyeraddress').val());
        $('#Buyeradress1_label').text($('#ebook_Buyeraddress1').val());
        $('#Buyercountry_label').text($('#ebook_Buyercountry_name').val());
        $('#Buyercity_label').text($('#ebook_Buyercity_desc').val());
        $('#Buyerphone_label').text($('#ebook_Buyerphone').val());
//        $('#Buyerfax_label').text($('#ebook_Buyerfax').val());
        $('#Buyeremail_label').text($('#ebook_Buyeremail').val());
        $('#Buyerstate_label').text($('#ebook_Buyerstate').val());
        $('#Buyereori_label').text($('#ebook_Buyereori').val());
        $('#Buyerpersontype_label').text($('#Buyertypeperson').val());
        if($('#ebook_Buyerzipcode').length > 0){
            $('#Buyerzipcode_label').text($('#ebook_Buyerzipcode').val());
        }
    } else
    {
        $('#by_hd').hide();
    }
 
if ($('#SellerContainer').length > 0 && $('#SellerContainer').find('.previewCust #ebook_Sellername1').val().length > 0){
$('#sellerTypePreviewContainer').html("");
$('#sld_hd').show();
$('#SellerContainer').find('.previewCust').each(function (index) {
    var rowid = $(this).attr('id'); 
   const sellerInput = $('#ebook_Sellername' + rowid);
if (sellerInput.length > 0 && sellerInput.val().length > 0){
console.log('prepare template'+rowid)
  
    var clone = $('#sellerTypePreviewTemplate').clone().removeAttr('id').show();

if (rowid !== "1") {
    clone.css({
        'border-top': '2px solid #f2b92a',
        'padding-block': '26px'
    });
}
    // Populate each label with values
    clone.find('#CustType_Sellername_label').text($('#ebook_Sellername' + rowid).val());
    clone.find('#CustType_Selleradress_label').text($('#ebook_Selleraddress' + rowid).val());
    clone.find('#CustType_Selleradress1_label').text($('#ebook_Selleraddress1' + rowid).val());
    clone.find('#CustType_Sellercity_label').text($('#ebook_Sellercity_desc' + rowid).val());
    clone.find('#CustType_Sellercountry_label').text($('#ebook_Sellercountry_name' + rowid ).val());
    clone.find('#CustType_Sellerphone_label').text($('#ebook_Sellerphone' + rowid).val());
//    clone.find('#CustType_Sellerfax_label').text($('#ebook_Sellerfax' + rowid).val());
    clone.find('#CustType_Selleremail_label').text($('#ebook_Selleremail' + rowid).val());
    clone.find('#CustType_Sellerstate_label').text($('#ebook_Sellerstate' + rowid).val());


    // Append to container
    $('#sellerTypePreviewContainer').append(clone);
    
}
});
}else{
    $('#sld_hd').hide();
}
if ($('#BuyerContainer').length > 0 && $('#BuyerContainer').find('.previewCust #ebook_Buyername1').val().length > 0){
$('#buyerTypePreviewContainer').html("");

$('#byd_hd').show();
$('#BuyerContainer').find('.previewCust').each(function (index) {
    var rowid = $(this).attr('id'); // e.g., 1, 2, ...
 const buyerInput = $('#ebook_Buyername' + rowid);
if (buyerInput.length > 0 && buyerInput.val().length > 0){
    var clone = $('#customerTypePreviewTemplate').clone().removeAttr('id').show();

if (rowid !== "1") {
    clone.css({
        'border-top': '2px solid #f2b92a',
        'padding-block': '26px'
    });
}
    // Populate each label with values
    clone.find('#CustType_Buyername_label').text($('#ebook_Buyername' + rowid).val());
    clone.find('#CustType_Buyeradress_label').text($('#ebook_Buyeraddress' + rowid).val());
    clone.find('#CustType_Buyeradress1_label').text($('#ebook_Buyeraddress1' + rowid).val());
    clone.find('#CustType_Buyercity_label').text($('#ebook_Buyercity_desc' + rowid).val());
    clone.find('#CustType_Buyercountry_label').text($('#ebook_Buyercountry_name' + rowid).val());
    clone.find('#CustType_Buyerphone_label').text($('#ebook_Buyerphone' + rowid).val());
//    clone.find('#CustType_Buyerfax_label').text($('#ebook_Buyerfax' + rowid).val());
    clone.find('#CustType_Buyeremail_label').text($('#ebook_Buyeremail' + rowid).val());
    console.log('email==>'+$('#ebook_Buyeremail' + rowid).val());
    clone.find('#CustType_Buyerstate_label').text($('#ebook_Buyerstate' + rowid).val());



    $('#buyerTypePreviewContainer').append(clone);
} 
     
});
}else{
    $('#byd_hd').hide();
}


    var i = 1;
    var type, hzd, rowid;
    tableval = $('#bkgpreviewtable1>tbody').html();
    $('#bkgpreviewtable1>tbody').html("")
    tableval = "";
    $('.preview').each(function () {
        tableval = tableval + "<tr><td>";
        rowid = $(this).attr('id');
        $('#serialno').text(i);
        tableval = tableval + i + "</td><td>"
        $('#eqptype_lable').text(($("#eqpType" + rowid).val() == 'D') ? 'Dry' : ($("#eqpType" + rowid).val() == 'O' ? 'Open' : ($("#eqpType" + rowid).val() == 'R' ? 'Reefer' : '')));
       
            $('#eqpqty_lable').text($('#eqpQty' + rowid).val());
       
        if (bkgtype == 'amendment') {
            if ($('#containerNo' + rowid + '_display').length > 0) {
                $('#eqpqty_lable').text($('#containerNo' + rowid + '_display').val());
            } else {
                $('#eqpqty_lable').text($('#containerNo' + rowid).val());
            }
        }
        $('#eqpsize_lable').text($("#eqpSize" + rowid + " option:selected").text());
        $('#soc_lable').text($("#soc" + rowid).prop('checked') == true ? "Yes" : "No");
        $('#hzd_lable').text($("#hzd" + rowid).prop('checked') == true ? "Yes" : "No");
        if ($("#oog_lable").length > 0) {
            $('#oog_lable').text($("#oogflag" + rowid).prop('checked') == true ? "Yes" : "No");
        }
        var insured_lbl = '';
        if ($("#insurance" + rowid + "_available").length > 0 && $("#insurance" + rowid + "_available").val() == 'Y') {
            insured_lbl = '<span class="fa fa-check-circle-o badge badge-pill badge-success preview_insured">Insured</span>';
        }
        if ($('#othersCmdty' + rowid).is(':checked')) {
            $('#commodity_lable').html($('#othercommodity' + rowid).val() + " " + insured_lbl);
        } else {
            $('#commodity_lable').html($('#commodity' + rowid).val() + " " + insured_lbl);
        }
        $('#hscode_lable').text($('#hscode' + rowid).val());
        $('#package_lable').text($('#packagename' + rowid).val());
        $('#pkgqty_lable').text($('#packageQty' + rowid).val());
        $('#actcommoname_lable').text($('#actCommoName' + rowid).val());
        $('#cmdtyhblno_lable').text($('#cmdtyhblno' + rowid).val());
        $('#sellersName_lable').text($('#sellersName' + rowid).val());
        $('#buyersName_lable').text($('#buyersName' + rowid).val());
        $('#eqpstatus_lable').text($("#eqpStatus" + rowid + " option:selected").text());
        $('#tareweight_lable').text($('#tareWt' + rowid).val() + "  " + $("#cargoWtUnit" + rowid + " option:selected").text());
        $('#cargowt_lable').text($('#cargoWt' + rowid).val() + "  " + $("#cargoWtUnit" + rowid + " option:selected").text());

        $('#netwt_lable').text($('#netWt' + rowid).val() + "  " + $("#cargoWtUnit" + rowid + " option:selected").text());

        //$('#cargowtunit_lable').text($("#cargoWtUnit" + rowid + " option:selected").text());
        $('#grosswt_lable').text($('#grossWt' + rowid).val() + " " + $("#cargoWtUnit" + rowid + " option:selected").text());
        //$('#grosswtunit_lable').text($("#grossWtUnit" + rowid + " option:selected").text());
        $('#volume_lable').text($('#volume' + rowid).val() + "  " + $("#volUnit" + rowid + " option:selected").text());
        //$('#volunit_lable').text($("#volUnit" + rowid + " option:selected").text());
        tableval = tableval + $('#bkgrowid').html();
        //refer
        type = $("#eqpType" + rowid).val()
        hzd = $("#hzd" + rowid).val()
        if (type == 'R') {
            $('#settemp_lable').text($('#presetTemp' + rowid).val());
            $('#mintemp_lable').text($('#minTemp' + rowid).val());
            $('#maxtemp_lable').text($('#maxTemp' + rowid).val());
            $('#nor_lable').text($("#nor" + rowid + " option:selected").text());
            $('#tempunit_lable').text($("#tempUnit" + rowid + " option:selected").text());
            $('#humidity_lable').text($('#humidity' + rowid).val());
            $('#drains_lable').text($("#drains" + rowid + " option:selected").text());
            $('#co2_lable').text($('#co2' + rowid).val());
            $('#o2_lable').text($('#o2' + rowid).val());
            $('#vent_lable').text($('#ventilation' + rowid).val());
            tableval = tableval + $('#refdivpreview').html();
        }
        //hazardous - Multiple DG Details Support
        if (hzd == 'Y') {
            var dgCount = parseInt($('#dgCount' + rowid).val() || '1');
            var dgHtml = '<hr><br>';
            
            console.log('[ECOM] Preview DG: Container=' + rowid + ', DG Count=' + dgCount);
            
            // Loop through all DG entries for this container
            for (var dgIdx = 1; dgIdx <= dgCount; dgIdx++) {
                var marinePollutVal = $("#marinePollut" + rowid + "_" + dgIdx + " option:selected").text();
                var unnoVal = $("#unno" + rowid + "_" + dgIdx).val();
                var flashPointVal = $('#flashPoint' + rowid + "_" + dgIdx).val();
                var flashUnitVal = $("#flashUnit" + rowid + "_" + dgIdx + " option:selected").text();
                var dgClassVal = $('#dgClass' + rowid + "_" + dgIdx).val();
                var shippingNameVal = $('#shippingName' + rowid + "_" + dgIdx).val();
                
                // Skip empty entries
                if (!unnoVal || unnoVal.trim().length === 0) {
                    continue;
                }
                
                // Add separator for multiple entries
                if (dgIdx > 1) {
                    dgHtml += '<hr style="border-top: 1px dashed #ccc; margin: 15px 0;">';
                }
                
                // Add entry header for multiple DG
                if (dgCount > 1) {
                    dgHtml += '<div class="form-group row"><label class="col-sm-12" style="font-weight: bold; color: #333;">DG Details #' + dgIdx + '</label></div>';
                }
                
                // Add DG fields
                dgHtml += '<div class="form-group row form-group-sm">';
                dgHtml += '<label class="col-sm-2 ">Marine Pollutant</label><span class=""> : </span>';
                dgHtml += '<label class=" col-sm-2 labl_preview">' + marinePollutVal + '</label>';
                dgHtml += '<label class=" col-sm-1">UN No</label><span class=""> : </span>';
                dgHtml += '<label class=" col-sm-2 labl_preview">' + unnoVal + '</label>';
                dgHtml += '<label class=" col-sm-2">Flash Point</label><span class=""> : </span>';
                dgHtml += '<label class=" col-sm-2 labl_preview">' + flashPointVal + ' ' + flashUnitVal + '</label>';
                dgHtml += '</div>';
                
                dgHtml += '<div class="form-group row">';
                dgHtml += '<label class="col-sm-2 ">DG Class</label><span class=""> : </span>';
                dgHtml += '<label class=" col-sm-2 labl_preview">' + dgClassVal + '</label>';
                dgHtml += '<label class="col-sm-1">Shipping Name</label><span class=""> : </span>';
                dgHtml += '<label class=" col-sm-4 labl_preview">' + shippingNameVal + '</label>';
                dgHtml += '</div>';
                
                console.log('[ECOM] Preview DG Entry ' + dgIdx + ': UN=' + unnoVal + ', Class=' + dgClassVal);
            }
            
            tableval = tableval + dgHtml;
        }
        //oog
        if (type == 'O') {
            $('#dimunit_lable').text($("#dimUnit" + rowid + " option:selected").text());
            $('#olforward_lable').text($('#olForward' + rowid).val());
            $('#owleft_lable').text($('#owLeft' + rowid).val());
            $('#olaft_lable').text($('#olAft' + rowid).val());
            $('#owright_lable').text($('#owRight' + rowid).val());
            $('#oh_lable').text($('#oh' + rowid).val());
            tableval = tableval + $('#oogdivpreview').html();
        }
        tableval = tableval + "</td></tr>";
        i++;
    });
    $('#bkgpreviewtable1>tbody').html(tableval);

    //Haulage details preview
    $("#previewdatatablebyhaulage").html($("#datatablebyhaulage").html());
    $("#previewdatatablebyhaulage tbody tr td:first-child").remove();
    $("#previewdatatablebyhaulage thead tr th:first-child").remove();

    if ($('#cargoprotect_table_available_div').length > 0 && $("input[name='cargoprotect_check']:checked").length > 0) {
        $('#cargoprotect_table_div_preview').hide();
        $('#cargoprotect_table_div_preview').html('');

        $('#cargoprotect_table_div_preview').html($('#cargoprotect_table_available_div').html());

        $("#cargoprotect_table_div_preview table thead tr th:first-child").remove();
        $("#cargoprotect_table_div_preview table tbody tr").remove();


        $("#cargoprotect_table_div_preview table tbody").html('<tr>' + $("input[name='cargoprotect_check']:checked").parents('tr').html() + '</tr>');
        $("#cargoprotect_table_div_preview table tbody tr td:first-child").remove();
        $('#cargoprotect_table_div_preview').show();

    } else if ($('#cargoprotect_table').length > 0 && $('#cargoprotect_table_div').html().length > 0) {

        $('#cargoprotect_table_div_preview').hide();
        $('#cargoprotect_table_div_preview').html('');

        $('#cargoprotect_table_div_preview').html($('#cargoprotect_table_div').html());

        $("#cargoprotect_table_div_preview table thead tr th:last-child").remove();
        $("#cargoprotect_table_div_preview table tbody tr td:last-child").remove();
        $('#cargoprotect_table_div_preview').show();
    } else {
        $('#cargoprotect_table_div_preview').hide();
        $('#cargoprotect_table_div_preview').html('');
    }
    //ENS Tab
    console.log('euport=>'+$('#euport').val())
    if ($('#euport').val() == 'Y') {
        $("#ens_eu_cust_zone_lable").text(($('#euCustZoneY').prop("checked") ? "Yes" : "No"));
        $("#ens_bl_type_lable").text($("#blTypeENS").val());
        $("#ens_filling_type_lable").text($("#ensFillingType").val());
        $("#ens_payment_method_lable").text($("#paymentMethod").val());

        if ($("#ensFillingType").val() == 'Single Filing') {
            $('#buyername_label').text($('#ebook_buyername').val());
            $('#buyeradress_label').text($('#ebook_buyeraddress').val());
            $('#buyeradress1_label').text($('#ebook_buyeraddress1').val());
            $('#buyercountry_label').text($('#ebook_buyercountry_name').val());
            $('#buyercity_label').text($('#ebook_buyercity').val());
            $('#buyerphone_label').text($('#ebook_buyerphone').val());
            $('#buyerfax_label').text($('#ebook_buyerfax').val());
            $('#buyeremail_label').text($('#ebook_buyeremail').val());
            $('#buyerstate_label').text($('#ebook_buyerstate').val());
            if ($('#ebook_buyereori') && $('#buyertypeperson')) {
                $('#buyereori_label').text($('#ebook_buyereori').val());
                $('#buyerpersontype_label').text($('#buyertypeperson').val());

            }
            $('#sellername_label').text($('#ebook_sellername').val());
            $('#selleradress_label').text($('#ebook_selleraddress').val());
            $('#selleradress1_label').text($('#ebook_selleraddress1').val());
            $('#sellercountry_label').text($('#ebook_sellercountry_name').val());
            $('#sellercity_label').text($('#ebook_sellercity').val());
            $('#sellerphone_label').text($('#ebook_sellerphone').val());
            $('#sellerfax_label').text($('#ebook_sellerfax').val());
            $('#selleremail_label').text($('#ebook_selleremail').val());
            $('#sellerstate_label').text($('#ebook_sellerstate').val());
            if ($('#ebook_sellereori') && $('#sellertypeperson')) {
                $('#sellereori_label').text($('#ebook_sellereori').val());
                $('#sellerpersontype_label').text($('#sellertypeperson').val());
            }
            $("#buyerseller_preview").show();
            $("#supplementarydeclarant_preview").hide();
        } else if ($("#ensFillingType").val() == 'Multiple Filing') {
            $('#declarantName_label').text($('#declarantName').val());
            $('#declarantAddress_label').text($('#declarantAddress').val());
            $('#declarantAddress1_label').text($('#declarantAddress1').val());
            $('#declarantCity_label').text($('#declarantCity').val());
            $('#declarantCountry_label').text($('#declarantCountry').val());
            $('#declarantState_label').text($('#declarantState').val());
            $('#declarantTelephone_label').text($('#declarantTelephone').val());
            $('#declarantEORI_label').text($('#declarantEORI').val());
            $('#declarantEmail_label').text($('#declarantEmail').val());
            $('#declarantZipcode_label').text($('#declarantZipcode').val());
            console.log('declarantZipcode_label==>'+$('#declarantZipcode').val());
            $('#declarantFillingType_label').text(($('input[name="declarantFillingType"]:checked').val()));
            
            $("#supplementarydeclarant_preview").show();
            $("#buyerseller_preview").hide();
        }
    }

}