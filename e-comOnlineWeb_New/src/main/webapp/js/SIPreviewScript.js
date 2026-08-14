/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function setSIPreviewDetails() {
    if ($('#bltype').length > 0) {
        $('#preview_bltype').text($('#bltype').val());
    }
    if ($('#freightoption').length > 0) {
        $('#preview_freightoption').text($('#freightoption').val());
    }
    if ($('#ebook_EnsDocType').length > 0) {
    const docTypeValue = $('#ebook_EnsDocType').val();
    const docTypeLabel = docTypeValue == 'C'? 'Consolidated' : docTypeValue == 'S'  ? 'Straight B/L' : '';
    $('#preview_ensdocTpe').text(docTypeLabel);
    }
    if ($('#t2lfiling').length > 0) {
        $('#preview_t2lfiling').text($("#t2lfiling").prop("checked") ? 'Yes' : 'No');
    }
    if ($('#nvocc').length > 0) {
        $('#preview_nvocc').text($("#nvocc").prop("checked") ? 'Yes' : 'No');
    }
    if ($('#orgTelType').length > 0 && $('#orgTelType').val()) {
        $('#preview_orgTelType').text($("#orgTelType").val()=='O' ? 'Original' : 'Telex');
    }
    //set customer details preview
    /**$('#shippername_label').val($('#ebook_shippername').val());
     $('#shipperadress_label').val($('#ebook_shipperaddress').val());
     $('#shipperadress1_label').val($('#ebook_shipperaddress1').val());
     $('#shippercountry_label').val($('#ebook_shippercountry_name').val());
     $('#shippercity_label').val($('#ebook_shippercity').val());
     $('#shipperphone_label').val($('#ebook_shipperphone').val());
     $('#shipperfax_label').val($('#ebook_shipperfax').val());
     $('#shipperemail_label').val($('#ebook_shipperemail').val());**/
    $('#shipperheadername_label').val($('#ebook_shippernameheader').val());
    $('#shipperheader_label').val($('#headershipper').val());

    /**$('#consigneename_label').val($('#ebook_consigneename').val());
     $('#consigneeadress_label').val($('#ebook_consigneeaddress').val());
     $('#consigneeadress1_label').val($('#ebook_consigneeaddress1').val());
     $('#consigneecountry_label').val($('#ebook_consigneecountry_name').val());
     $('#consigneecity_label').val($('#ebook_consigneecity').val());
     $('#consigneephone_label').val($('#ebook_consigneephone').val());
     $('#consigneefax_label').val($('#ebook_consigneefax').val());
     $('#consigneeemail_label').val($('#ebook_consigneeemail').val());**/
    $('#consigneeheadername_label').val($('#ebook_consigneenameheader').val());
    $('#consigneeheader_label').val($('#headerconsignee').val());


    /**$('#notifyname_label').val($('#ebook_notifyname').val());
     $('#notifyadress_label').val($('#ebook_notifyaddress').val());
     $('#notifyadress1_label').val($('#ebook_notifyaddress1').val());
     $('#notifycountry_label').val($('#ebook_notifycountry_name').val());
     $('#notifycity_label').val($('#ebook_notifycity').val());
     $('#notifyphone_label').val($('#ebook_notifyphone').val());
     $('#notifyfax_label').val($('#ebook_notifyfax').val());
     $('#notifyemail_label').val($('#ebook_notifyemail').val());**/
    $('#notifyheadername_label').val($('#ebook_notifynameheader').val());
    $('#notifyheader_label').val($('#headernotify').val());


    //
    if ((($('#nt_id2').length > 0)) && ($('#ebook_notify2name').val().length > 0 || $('#ebook_notify2address').val().length > 0 || $('#ebook_notify2country_name').val().length > 0 || $('#ebook_notify2city').val().length > 0 || $('#ebook_notify2phone').val().length > 0 || ($('#ebook_notify2fax').length > 0 && $('#ebook_notify2fax').val().length > 0) || $('#ebook_notify2email').val().length > 0))
    {
        $('#nt_id2').show()
        $('#notifyname_label2').val($('#ebook_notify2name').val());
        $('#notifyadress_label2').val($('#ebook_notify2address').val());
        $('#notifyadress1_label2').val($('#ebook_notify2address1').val());
        $('#notifycountry_label2').val($('#ebook_notify2country_name').val());
        $('#notifycity_label2').val($('#ebook_notify2city').val());
        $('#notifyphone_label2').val($('#ebook_notify2phone').val());
        $('#notifyfax_label2').val($('#ebook_notify2fax').val());
        $('#notifyemail_label2').val($('#ebook_notify2email').val());
        $('#notifystate_label2').val($('#ebook_notify2state').val());
        if ($('#ebook_notify2eori') && $('#notify2typeperson')) {
            $('#notifyeori_label2').val($('#ebook_notify2eori').val());
            $('#notifypersontype_label2').val($('#notify2typeperson').val());
        }
        if ($('#ebook_notify2zipcode').length > 0) {
            $('#notifyzipcode_label2').val($('#ebook_notify2zipcode').val());
        }
    } else
    {
        $('#nt_id2').hide()
    }
    //

    //
    if ((($('#nt_id3').length > 0)) && ($('#ebook_notify3name').val().length > 0 || $('#ebook_notify3address').val().length > 0 || $('#ebook_notify3country_name').val().length > 0 || $('#ebook_notify3city').val().length > 0 || $('#ebook_notify3phone').val().length > 0 || ($('#ebook_notify3fax').length > 0 && $('#ebook_notify3fax').val().length > 0) || $('#ebook_notify3email').val().length > 0))
    {
        $('#nt_id3').show()
        $('#notifyname_label3').val($('#ebook_notify3name').val());
        $('#notifyadress_label3').val($('#ebook_notify3address').val());
        $('#notifyadress1_label3').val($('#ebook_notify3address1').val());
        $('#notifycountry_label3').val($('#ebook_notify3country_name').val());
        $('#notifycity_label3').val($('#ebook_notify3city').val());
        $('#notifyphone_label3').val($('#ebook_notify3phone').val());
        $('#notifyfax_label3').val($('#ebook_notify3fax').val());
        $('#notifyemail_label3').val($('#ebook_notify3email').val());
        $('#notifystate_label3').val($('#ebook_notify3state').val());
        if ($('#ebook_notify3eori') && $('#notify3typeperson')) {
            $('#notifyeori_label3').val($('#ebook_notify3eori').val());
            $('#notifypersontype_label3').val($('#notify3typeperson').val());
        }
        if ($('#ebook_notify3zipcode').length > 0) {
            $('#notifyzipcode_label3').val($('#ebook_notify3zipcode').val());
        }
    } else
    {
        $('#nt_id3').hide()
    }
    //FW
    if ((($('#fw_id').length > 0)) && ($('#ebook_forwardername').val().length > 0 || $('#ebook_forwarderaddress').val().length > 0 || $('#ebook_forwardercountry_name').val().length > 0 || $('#ebook_forwardercity').val().length > 0 || $('#ebook_forwarderphone').val().length > 0 || ($('#ebook_forwarderfax').length > 0 && $('#ebook_forwarderfax').val().length > 0) || $('#ebook_forwarderemail').val().length > 0))
    {
        $('#fw_id').show()
        $('#forwardername_label').val($('#ebook_forwardername').val());
        $('#forwarderadress_label').val($('#ebook_forwarderaddress').val());
        $('#forwarderadress1_label').val($('#ebook_forwarderaddress1').val());
        $('#forwardercountry_label').val($('#ebook_forwardercountry_name').val());
        $('#forwardercity_label').val($('#ebook_forwardercity').val());
        $('#forwarderphone_label').val($('#ebook_forwarderphone').val());
        $('#forwarderfax_label').val($('#ebook_forwarderfax').val());
        $('#forwarderemail_label').val($('#ebook_forwarderemail').val());
        $('#forwarderstate_label').val($('#ebook_forwarderstate').val());
        if ($('#ebook_forwardereori') && $('#forwardertypeperson')) {
            $('#forwardereori_label').val($('#ebook_forwardereori').val());
            $('#forwarderpersontype_label').val($('#forwardertypeperson').val());
        }
        if ($('#ebook_forwarderzipcode').length > 0) {
            $('#forwarderzipcode_label').val($('#ebook_forwarderzipcode').val());
        }
    } else
    {
        $('#fw_id').hide()
    }

updateCustomerPreview();


      $('#pla_free_txt_label').val($('#pla_free_txt').val());
    $('#pol_free_txt_label').val($('#pol_free_txt').val());
    $('#pod_free_txt_label').val($('#pod_free_txt').val());
    $('#pld_free_txt_label').val($('#pld_free_txt').val());
    $('#vslvoyage_free_txt_label').val($('#vslvoyage_free_txt').val());

//Header information

    $('#hid_pla_free_txt_label').val($('#hid_pla_free_txt').val());
    $('#hid_pol_free_txt_label').val($('#hid_pol_free_txt').val());
    $('#hid_pod_free_txt_label').val($('#hid_pod_free_txt').val());
    $('#hid_pld_free_txt_label').val($('#hid_pld_free_txt').val());

    $('#pla_free_txt_label').val($('#pla_free_txt').val());
    $('#pol_free_txt_label').val($('#pol_free_txt').val());
    $('#pod_free_txt_label').val($('#pod_free_txt').val());
    $('#pld_free_txt_label').val($('#pld_free_txt').val());
    $('#vslvoyage_free_txt_label').val($('#vslvoyage_free_txt').val());

    //set routing details preview
    $('#siroutingdetailspreview').html($('#siroutingdetails').html());

    //set cargo details preview
    var i = 1, j = 1, l = 0, k, m = 2, n = 0, ll = 1;
    var tableval = "";
    var arr;
    var comdtyid = "";
    $("input[name='select']").each(function () {
        var contno = this.value;

        $('#sitablepreview>tbody').html("")

        if (this.checked) {
            i++;
            tableval = tableval + "<tr><td>";
            tableval = tableval + ll + "</td><td>"
            if ($('#containerno' + contno + '_display').length > 0) {
                $('#sicontno_lable').text($('#containerno' + contno + '_display').val());
            } else {
                $('#sicontno_lable').text($('#containerno' + contno).val());
            }
            if ($('#soc' + contno).val() == 'Y') {
                $('#sisoc_lable').text('Yes');
            } else {
                $('#sisoc_lable').text('No');
            }
            if ($("#oogflag_lable").length > 0) {
                if ($('#' + contno + 'oogcheckbox').prop('checked') == true) {
                    $('#oogflag_lable').text('Yes');
                } else {
                    $('#oogflag_lable').text('No');
                }
            }
            $('#sieqptyow_lable').text($('#eqptypeval' + contno).text());
            $('#sicarrierseal_lable').text($('#carrierseal' + contno + '1').val());
            $('#sishipperseal_lable').text($('#shipperseal' + contno + '1').val());
            $('#sitareweight_lable').text($('#tareweight' + contno).val());
            if ($('#EnableReeferDetails').val() == 'Y') {
                $('#sinor_lable').text($('#' + contno + '\\#nor\\#').val());
                $('#sisettemp_lable').text($('#' + contno + '\\#settemp\\#').val());
                $('#sitempunit_lable').text($('#' + contno + '\\#tempunit\\#').val());
                $('#sivent_lable').text($('#' + contno + '\\#ventilation\\#').val());
                $('#SIPreviewTemp').show();
            }
            // $('#sigrossweight_lable').text($('#grossweight'+ contno).val());
            tableval = tableval + $('#premainrowsi').html();
            tableval = tableval + "</td><td>";

            var insured_lbl = '';
            if ($("#insurance" + contno + j + "_available").length > 0 && $("#insurance" + contno + j + "_available").val() == 'Y') {
                insured_lbl = '<span class="fa fa-check-circle-o badge badge-pill badge-success preview_insured">Insured</span>';
            }

            $('#sicmdtylabel').html($('#commodity' + contno + j).val() + " " + insured_lbl);
            $('#siqtylabel').text($('#qty' + contno + j).val());
            $('#sivollabel').text($('#volume' + contno + j).val() + " " + $("#vunit" + contno + j + " option:selected").text());
            $('#sihscodelabel').text($('#hscode' + contno + j).val());
            //$('#sivolunitlabel').text($("#vunit" + contno + j + " option:selected").text());
            $('#sipacklabel').text($('#package' + contno + j).val());
            console.log('contno==>'+contno);
            $('#sibuyerlabel').text($('#byname'+ contno + j).val());
            $('#sisellerlabel').text($('#slname'+ contno + j).val());
            $('#sihblnolabel').text($('#cmdty_hblno'+ contno + j).val());
            $('#siwtlabel').text($('#weight' + contno + j).val() + " " + $('#unit' + contno + j + " option:selected").text());
            //  $('#siwtunitlabel').text($('#unit'+contno+j+" option:selected").text());
            $('#sinetweight_lable').text($('#netweight' + contno + j).val());
            $('#sihscodelabel').text($('#hscode' + contno + j).val());
            $('#simarkslabel').text($('#mark' + contno + j).val());
            $('#sidesclabel').text($('#descp' + contno + j).val());
            if ($("#oogflag_lable").length > 0) {
                if ($('#' + contno + 'oogcheckbox').prop('checked') == true) {
                    $('#OOGpreviewblock').show();
                    $('#sioogunitlabel').text($('#' + contno + '\\#itemlengthUnit\\#' + j).val());
                    $('#sioogleftlabel').text($('#' + contno + '\\#itemleft\\#' + j).val());
                    $('#sioogrightlabel').text($('#' + contno + '\\#itemright\\#' + j).val());
                    $('#sioogforwardlabel').text($('#' + contno + '\\#itemforward\\#' + j).val());
                    $('#sioogaltlabel').text($('#' + contno + '\\#itemaft\\#' + j).val());
                    $('#sioogtoplabel').text($('#' + contno + '\\#itemtop\\#' + j).val());
                } else {
                    $('#OOGpreviewblock').hide();
                }
            }
            tableval = tableval + $('#precargosi').html();
            var elements = document.getElementsByName(contno + "#newcmdty#");
            if (elements.length > 1) {
                for (var i = 1; i < elements.length; i++) {
                    var elementid = elements[i].getAttribute('id');
                    elementid = elementid.replace('newcmdty' + contno, '');
                    tableval = tableval + "</td></tr><tr><td></td><td></td><td>";
                    $('#sicmdtylabel').text($('#commodity' + contno + elementid).val());
                    $('#siqtylabel').text($('#qty' + contno + elementid).val());
                    $('#sibuyerlabel').text($('#byname'+ contno + elementid).val());
                    $('#sisellerlabel').text($('#slname'+ contno + elementid).val());
                    $('#sihblnolabel').text($('#cmdty_hblno'+ contno +elementid).val());
                    $('#sivollabel').text($('#volume' + contno + elementid).val());
                    $('#sihscodelabel').text($('#hscode' + contno + elementid).val());
                    $('#sivolunitlabel').text($("#vunit" + contno + elementid + " option:selected").text());
                    $('#sipacklabel').text($('#package' + contno + elementid).val());
                    $('#siwtlabel').text($('#weight' + contno + elementid).val() + " " + $('#uncait' + contno + elementid + " option:selected").text());
                    //   $('#siwtunitlabel').text();
                    $('#sinetweight_lable').text($('#netweight' + contno + elementid).val());
                    $('#simarkslabel').text($('#mark' + contno + elementid).val());
                    $('#sidesclabel').text($('#descp' + contno + elementid).val());
                    if ($("#oogflag_lable").length > 0) {
                        if ($('#' + contno + 'oogcheckbox').prop('checked') == true) {
                            $('#OOGpreviewblock').show();
                            $('#sioogunitlabel').text($('#' + contno + '\\#itemlengthUnit\\#' + elementid).val());
                            $('#sioogleftlabel').text($('#' + contno + '\\#itemleft\\#' + elementid).val());
                            $('#sioogrightlabel').text($('#' + contno + '\\#itemright\\#' + elementid).val());
                            $('#sioogforwardlabel').text($('#' + contno + '\\#itemforward\\#' + elementid).val());
                            $('#sioogaltlabel').text($('#' + contno + '\\#itemaft\\#' + elementid).val());
                            $('#sioogtoplabel').text($('#' + contno + '\\#itemtop\\#' + elementid).val());
                        } else {
                            $('#OOGpreviewblock').hide();
                        }
                    }
                    tableval = tableval + $('#precargosi').html();
                    tableval = tableval + "</td></tr>";
                }
            } else {
                tableval = tableval + "</td></tr>";
            }

            ll = ll + 1;
        }
    });

    $('#sitablepreview>tbody').html(tableval);

    if ($("#previewdatatablebycharge").length > 0) {
        $('#datatablebychargetbl').find('.prepaidcollect').each(function () {
            $(this).find('span').html($(this).find('select').find('option:selected').text());
        });
        $('#datatablebychargetbl').find('.custtypeid').each(function () {
            $(this).find('span').html($(this).find('select').find('option:selected').text());
        });
        $("#previewdatatablebycharge").html($("#datatablebychargetbl").html());
        $('#previewdatatablebycharge').find('.prepaidcollect').each(function () {
            $(this).find('span').removeClass('d-none');
            $(this).find('select').remove();
        });
        $('#previewdatatablebycharge').find('.custtypeid').each(function () {
            $(this).find('span').removeClass('d-none');
            $(this).find('select').remove();
        });
        $("#previewdatatablebycharge tbody tr td:first-child").remove();
        $("#previewdatatablebycharge thead tr th:first-child").remove();
    }
    if ($("#releasetype").length > 0) {
        $("#releasetypelbl").text($("#releasetype").val());
    }
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
    if ("#GENS_TAB") {
        $("#euCustZone_label").val(($('input[name="euCustZone"]:checked').val() == "Y" ? "Yes" : "No"));
        $("#blTypeENS_label").val($("#blTypeENS").val());
        $("#ensFillingType_label").val($("#ensFillingType").val());
        $("#paymentMethod_label").val($("#paymentMethod").val());

        if ($("#ensFillingType").val() == 'Single Filing') {
            $('#buyername_label').val($('#ebook_buyername').val());
            $('#buyeradress_label').val($('#ebook_buyeraddress').val());
            $('#buyeradress1_label').val($('#ebook_buyeraddress1').val());
            $('#buyercountry_label').val($('#ebook_buyercountry_name').val());
            $('#buyercity_label').val($('#ebook_buyercity').val());
            $('#buyerphone_label').val($('#ebook_buyerphone').val());
            $('#buyerfax_label').val($('#ebook_buyerfax').val());
            $('#buyeremail_label').val($('#ebook_buyeremail').val());
            $('#buyerstate_label').val($('#ebook_buyerstate').val());
            if ($('#ebook_buyereori') && $('#buyertypeperson')) {
                $('#buyereori_label').val($('#ebook_buyereori').val());
                $('#buyerpersontype_label').val($('#buyertypeperson').val());

            }
            $('#sellername_label').val($('#ebook_sellername').val());
            $('#selleradress_label').val($('#ebook_selleraddress').val());
            $('#selleradress1_label').val($('#ebook_selleraddress1').val());
            $('#sellercountry_label').val($('#ebook_sellercountry_name').val());
            $('#sellercity_label').val($('#ebook_sellercity').val());
            $('#sellerphone_label').val($('#ebook_sellerphone').val());
            $('#sellerfax_label').val($('#ebook_sellerfax').val());
            $('#selleremail_label').val($('#ebook_selleremail').val());
            $('#sellerstate_label').val($('#ebook_sellerstate').val());
            if ($('#ebook_sellereori') && $('#sellertypeperson')) {
                $('#sellereori_label').val($('#ebook_sellereori').val());
                $('#sellerpersontype_label').val($('#sellertypeperson').val());
            }
            if ($('#ebook_buyerzipcode').length > 0) {
                $('#buyerzipcode_label').val($('#ebook_buyerzipcode').val());
            }
            if ($('#ebook_sellerzipcode').length > 0) {
                $('#sellerzipcode_label').val($('#ebook_sellerzipcode').val());
            }
            $("#buyerseller_preview").show();
            $("#supplementarydeclarant_preview").hide();
        } else if ($("#ensFillingType").val() == 'Multiple Filing') {
            $('#declarantName_label').val($('#declarantName').val());
            $('#declarantAddress_label').val($('#declarantAddress').val());
            $('#declarantAddress1_label').val($('#declarantAddress1').val());
            $('#declarantCity_label').val($('#declarantCity').val());
            $('#declarantCountry_label').val($('#declarantCountry').val());
            $('#declarantState_label').val($('#declarantState').val());
            $('#declarantTelephone_label').val($('#declarantTelephone').val());
            $('#declarantEORI_label').val($('#declarantEORI').val());
            $('#declarantEmail_label').val($('#declarantEmail').val());
            $('#declarantZipcode_label').val($('#declarantZipcode').val());
            $('#declarantFillingType_label').val(($('input[name="declarantFillingType"]:checked').val()));
            
            $("#supplementarydeclarant_preview").show();
            $("#buyerseller_preview").hide();
        }
    }

}
  function updateCustomerPreview() {
   
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
        'border-top': '1px solid #ccc',
        'padding-block': '26px'
    });
}
    // Populate each label with values
    clone.find('#CustType_Sellername_label').val($('#ebook_Sellername' + rowid).val());
    clone.find('#CustType_Selleradress_label').val($('#ebook_Selleraddress' + rowid).val());
    clone.find('#CustType_Selleradress1_label').val($('#ebook_Selleraddress1' + rowid).val());
    clone.find('#CustType_Sellercity_label').val($('#ebook_Sellercity_desc' + rowid).val());
    clone.find('#CustType_Sellercountry_label').val($('#ebook_Sellercountry_name' + rowid ).val());
    clone.find('#CustType_Sellerphone_label').val($('#ebook_Sellerphone' + rowid).val());
//    clone.find('#CustType_Sellerfax_label').val($('#ebook_Sellerfax' + rowid).val());
    clone.find('#CustType_Selleremail_label').val($('#ebook_Selleremail' + rowid).val());
    clone.find('#CustType_Sellerstate_label').val($('#ebook_Sellerstate' + rowid).val());


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
    var rowid = $(this).attr('id'); 
    const buyerInput = $('#ebook_Buyername' + rowid);
if (buyerInput.length > 0 && buyerInput.val().length > 0){
  console.log('prepare template'+rowid)
    
    var clone = $('#customerTypePreviewTemplate').clone().removeAttr('id').show();

if (rowid !== "1") {
    clone.css({
        'border-top':  '1px solid #ccc',
        'padding-block': '26px'
    });
}
    // Populate each label with values
    clone.find('#CustType_Buyername_label').val($('#ebook_Buyername' + rowid).val());
    clone.find('#CustType_Buyeradress_label').val($('#ebook_Buyeraddress' + rowid).val());
    clone.find('#CustType_Buyeradress1_label').val($('#ebook_Buyeraddress1' + rowid).val());
    clone.find('#CustType_Buyercity_label').val($('#ebook_Buyercity_desc' + rowid).val());
    clone.find('#CustType_Buyercountry_label').val($('#ebook_Buyercountry_name' + rowid).val());
    clone.find('#CustType_Buyerphone_label').val($('#ebook_Buyerphone' + rowid).val());
//    clone.find('#CustType_Buyerfax_label').val($('#ebook_Buyerfax' + rowid).val());
    clone.find('#CustType_Buyeremail_label').val($('#ebook_Buyeremail' + rowid).val());
    console.log('email==>'+$('#ebook_Buyeremail' + rowid).val());
    clone.find('#CustType_Buyerstate_label').val($('#ebook_Buyerstate' + rowid).val());


    $('#buyerTypePreviewContainer').append(clone);
}
     
});
}else{
    $('#byd_hd').hide();
}
}

