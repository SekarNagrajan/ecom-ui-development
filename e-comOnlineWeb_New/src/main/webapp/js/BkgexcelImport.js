/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function getXmlHttpRequestObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        alert(incompatibleforAjax);
    }
}
var searchReq = getXmlHttpRequestObject();

function checkEmpty(strval)
{
    if (strval == "no~records" || strval == undefined || strval == "undefined")
    {
        strval = "";
    }
    return strval;
}


function ImportData()
{
    var appconsize = "1";
    // $('.preview').each(function() {
    //     appconsize=$(this).attr('id');
    //   });
    var rowid = [];
    var row = "";
    $('.containerclass').each(function () {
        row = $(this).attr('id');
        row = row.replace("containerNo", "");
        rowid.push(row);
    });


    // console.log("::::"+rowid);

    appconsize = document.getElementById('ebookconsize').value;
    var importexcel = document.getElementById('importexcel').value;
    var extn = importexcel.substring(importexcel.lastIndexOf("."), importexcel.length).toLowerCase();
    if (extn == "")
    {
        alert(selfile);
        document.getElementById('importexcel').value = "";
        document.getElementById('importexcel').focus();
        return false;
    }

    if (extn != ".xls")
    {
        alert(invfileformat);
        document.getElementById('importexcel').value = "";
        document.getElementById('importexcel').focus();
        return false;
    } else
    {
        openLoader();
        var totresponse;
        var response;
        var errresponse;
        var error;
        var countresponse;
        var overallresponse;
        var counttotresponse;
        var msg_response;
        var message_response;
        var flag;
        var i = 0;
        var data;
        var checkcontianerordering = true;
        var sno = "", eqpno = "", socs = "", eqpsize = "", cmty = "", volme = "", volumeunit = "", eqptype = "";
        var pkg = "", cargoweight = "", weightunit = "", packageqty = "";
        var hazardous = "", hscodes = "", eqpstatus = "", unit = "", left = "", right = "", forward = "", alt = "", top = "";
        var norval = "", presettemp = "", mintemp = "", maxtemp = "", tempunit = "", shippingname = "";
        var marinepollutant = "", unnos = "", flashpoint = "", flashpointunit = "", othcommodity = "", dgclass = "";

        // fieldid

        var eqpType = "", eqpSize = "", eqpQty = "", soc = "", hzd = "", commodity = "", packagename = "", hscode = "";
        var packageQty = "", eqpStatus = "", tareWt = "", cargoWt = "", grossWt = "", volume = "", cargoWtUnit = "";
        var grossWtUnit = "", volUnit = "", othersCmdty = "", marinePollut = "", unno = "", flashPoint = "", flashUnit = "", commodityothers = "";
        var shippingName = "", dgClass = "", presetTemp = "", minTemp = "", maxTemp = "", tempUnit = "", nor = "";
        var dimUnit = "", owLeft = "", owRight = "", oh = "", olAft = "", olForward = "", hzdDiv = "", refDiv = "", oogDiv = "", eqptypeval = "";
        var commodityval = "", packageval = "", refclass = "", oogclass = "", hzdclass = "";
        var hidden_oog = "", hidden_lcl = "", hidden_hzd = "", hidden_rfr = "", hidden_soc = "";
        var commcheck = "", commodityclass = "", comid = "", conid = "", othercommodity = "", otherhscode = "", oogval = "";
        var cargoweightcheck = "", volumeweightcheck = "";
        var commodityport = "", packagenameport = "", hiddencomid = "", hiddenpackid = "", containerNo = "";
        var unwantedCharacter = "";
        var commgroup = "", groupcode = "", ventval = "", ventid = "", humidityid = "", humidityflagid = "", drainsid = "", co2id = "", o2id = "", humidityval = "", drainsval = "", co2val = "", o2val = "";

        //

        if (searchReq.readyState == 4 || searchReq.readyState == 0)
        {

            data = new FormData();
            data.append('importexcel', $('#importexcel')[0].files[0]);
            var url = "ExcelImportBooking";
            searchReq.open("POST", url, true);
            searchReq.send(data);

            searchReq.onreadystatechange = function ()
            {
                //
                var concont = 0;
                if (searchReq.readyState == 4)
                {
                    closeLoader();
                    overallresponse = trim(searchReq.responseText);

                    if (overallresponse == "true")
                    {
                        alert(invxltemp);
                        return false;
                    }

                    if (overallresponse.indexOf("**~~~~~~~~~~~**") !== -1) {
                        msg_response = overallresponse.split("**~~~~~~~~~~~**");
                        overallresponse = msg_response[0];
                        message_response = msg_response[1];
                    }

                    if (overallresponse.indexOf("~*~~~~~~~~~~~*~") !== -1) {
                        counttotresponse = overallresponse.split("~*~~~~~~~~~~~*~");
                        totresponse = counttotresponse[0];
                        countresponse = counttotresponse[1];
                        concont = countresponse.replace(/~/g, "");
                        concont = trim(concont);
                    } else
                    {
                        totresponse = overallresponse;
                    }
                    if (totresponse.indexOf("~~~~~~~~~~") !== -1) {
                        errresponse = totresponse.split("~~~~~~~~~~");
                        response = errresponse[0];
                        error = errresponse[1];
                    } else
                    {
                        response = totresponse;
                    }



                    if (parseInt(concont) > parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(morecontcountbkg);
                        return false;
                    }
                    if (parseInt(concont) < parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(lesscontcountbkg);
                        return false;
                    }

                    if (parseInt(rowid.length) > parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(rowcountbkg);
                        return false;
                    }

                    if (parseInt(rowid.length) < parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(rowcountbkg);
                        return false;
                    } else if (error != null && error != 'undefined')
                    {
                        $("#myImportModal").modal("hide");
                        setTimeout(function () {
                            $("#myModalImportErrors").modal("show");
                        }, 500);
                        error = error.replace(/~/g, "");
                        // alert(error);
                        $('#importerr').html(error);
                        $('#datatableexcel').DataTable();
                        return false;
                    } else if (response != null && response != "") {

                        if (message_response != 'undefined' && message_response != undefined && message_response != null)
                        {
                            $("#myModalImportMsg").modal("show");
                            message_response = message_response.replace(/~/g, "");
                            $('#importmsg').html(message_response);
                            $('#datatableexcel1').DataTable();
                        }

                        var res = response.split("~~~~");
                        var i = 0;
                        var j = 1;

                        for (i = 0; i < res.length - 1; i++)
                        {
                            j = rowid[i];
                            // console.log("j:::::::"+j);
                            //   if(parseInt(j)>1)
                            //    {
                            //       addamendmentcargo();
                            //    }
                            //variable assing
                            refclass = "refclass" + j;
                            oogclass = "oogclass" + j;
                            hzdclass = "hzdclass" + j;
                            cargoweightcheck = "cargoweightcheck" + j;
                            volumeweightcheck = "volumeweightcheck" + j;
                            eqpType = "eqpType" + j;
                            eqpSize = "eqpSize" + j;
                            eqpQty = "eqpQty" + j;
                            soc = "soc" + j;
                            hzd = "hzd" + j;
                            containerNo = "containerNo" + j;
                            commodityclass = "commodityclass" + j;
                            commodity = "commodity" + j;
                            hiddencomid = "hiddencomid" + j;
                            hiddenpackid = "hiddenpackid" + j;
                            commodityport = "commodity" + j + "_port";
                            packagename = "packagename" + j;
                            packagenameport = "packagename" + j + "_port";
                            hscode = "hscode" + j;
                            packageQty = "packageQty" + j;
                            eqpStatus = "eqpStatus" + j;
                            tareWt = "tareWt" + j;
                            cargoWt = "cargoWt" + j;
                            grossWt = "grossWt" + j;
                            volume = "volume" + j;
                            comid = "comid" + j;
                            conid = "conid" + j;
                            othercommodity = "othercommodity" + j;
                            otherhscode = "otherhscode" + j;
                            cargoWtUnit = "cargoWtUnit" + j;
                            grossWtUnit = "grossWtUnit" + j;
                            volUnit = "volUnit" + j;
                            othersCmdty = "othersCmdty" + j;
                            commodityothers = "commodityothers" + j;
                            //
                            marinePollut = "marinePollut" + j;
                            unno = "unno" + j;
                            flashPoint = "flashPoint" + j;
                            flashUnit = "flashUnit" + j;
                            shippingName = "shippingName" + j;
                            dgClass = "dgClass" + j;
                            presetTemp = "presetTemp" + j;
                            minTemp = "minTemp" + j;
                            maxTemp = "maxTemp" + j;
                            ventid = "ventilation" + j;
                            humidityflagid = "humidityflag" + j;
                            humidityid = "humidity" + j;
                            drainsid = "drains" + j;
                            co2id = "co2" + j;
                            o2id = "o2" + j;
                            tempUnit = "tempUnit" + j;
                            nor = "nor" + j;
                            owLeft = "owLeft" + j;
                            dimUnit = "dimUnit" + j;
                            owRight = "owRight" + j;
                            oh = "oh" + j;
                            olAft = "olAft" + j;
                            olForward = "olForward" + j;
                            hzdDiv = "hzdDiv" + j;
                            refDiv = "refDiv" + j;
                            oogDiv = "oogDiv" + j;
                            hidden_oog = "hidden_oog" + j;
                            hidden_hzd = "hidden_hzd" + j;
                            hidden_rfr = "hidden_rfr" + j;
                            hidden_soc = "hidden_soc" + j;
                            //
                            var reqresonse = res[i];
                            var req = reqresonse.split("~~~");
                            if (req[0] == unwantedCharacter) {
                                req = req.slice(1);
                            }
                            sno = checkEmpty(req[0]);
                            eqpno = checkEmpty(req[1]);
                            socs = checkEmpty(req[2]);
                            eqpsize = checkEmpty(req[3]);
                            eqptype = checkEmpty(req[4]);
                            hazardous = checkEmpty(req[5]);
                            othcommodity = checkEmpty(req[6]);
                            cmty = checkEmpty(req[7]);
                            console.log("check commodity>>" + req[7]);
                            hscodes = checkEmpty(req[8]);
                            volme = checkEmpty(req[9]);
                            volumeunit = checkEmpty(req[10]);
                            pkg = checkEmpty(req[11]);
                            cargoweight = checkEmpty(req[12]);
                            weightunit = checkEmpty(req[13]);
                            packageqty = checkEmpty(req[14]);
                            console.log("eqptype==>1 " + eqptype);
                            if (eqptype == 'O')
                            {
                                unit = checkEmpty(req[15]);
                                left = checkEmpty(req[16]);
                                right = checkEmpty(req[17]);
                                forward = checkEmpty(req[18]);
                                alt = checkEmpty(req[19]);
                                top = checkEmpty(req[20]);
                                console.log("req.length==>"+req.length);
                                console.log("OOG_Plugin==>"+$('#OOG_Plugin').val());
                                if (req.length > 34 && $('#OOG_Plugin').val() == 'Y') {
                                    oogval = checkEmpty(req[34]);
                                    console.log("oogval==>"+oogval);
                                }
                            }
                            if (eqptype == 'R')
                            {
                                norval = checkEmpty(req[21]);
                                presettemp = checkEmpty(req[22]);
                                mintemp = checkEmpty(req[23]);
                                maxtemp = checkEmpty(req[24]);
                                tempunit = checkEmpty(req[25]);
                                ventval = checkEmpty(req[33]);
                                if (req.length > 34) {
                                    humidityval = checkEmpty(req[34]);
                                    drainsval = checkEmpty(req[35]);
                                    co2val = checkEmpty(req[36]);
                                    o2val = checkEmpty(req[37]);
                                }
                            }
                            //
                            if (hazardous == 'Y') {
                                //cargoselectmethod_hzd(hazardous, j);
                                //setDGValues(j);
                                marinepollutant = checkEmpty(req[26]);
                                unnos = checkEmpty(req[27]);
                                dgclass = checkEmpty(req[28]);
                                shippingname = checkEmpty(req[29]);
                                flashpoint = checkEmpty(req[30]);
                                flashpointunit = checkEmpty(req[31]);
                            }
                            eqpstatus = checkEmpty(req[32]);
                            //

                            cargoselectmethod(eqptype, j);
                            getEqpList_new(eqptype, eqpSize);
                            document.getElementById(eqpType).value = eqptype;
                            document.getElementById(eqpSize).value = eqpsize;
                            if (othcommodity == 'Y')
                            {
                                document.getElementById(othersCmdty).checked = true;
                                checkcommodity(j, othersCmdty, othersCmdty);
                                document.getElementById(commodityothers).value = 'Y';
                                document.getElementById(othercommodity).value = cmty;
                                document.getElementById(otherhscode).value = hscodes;

                            } else
                            {
                                //document.getElementById(othersCmdty).checked = false;
                                //checkcommodity(j, othersCmdty, othersCmdty);
                                document.getElementById(commodityothers).value = 'N';
                                document.getElementById(commodity).value = cmty;
                                $('#' + commodity).typeahead('destroy');
                                commgroup = "";
                                commgroup = cmty.split("~~");
                                if (commgroup.length > 1) {
                                    groupcode = commgroup[1];
                                    groupcode = trim(groupcode);
                                }
                                //    console.log(commodity+'::::::::'+hiddencomid+":::::::"+groupcode);
                                commodityload(commodity, hiddencomid, groupcode);
                            }

                            setDGValues(j);
                            document.getElementById(containerNo).value = eqpno;
                            document.getElementById(volume).value = volme;
                            document.getElementById(volUnit).value = volumeunit;
                            document.getElementById(packagename).value = pkg;
                            document.getElementById(cargoWt).value = cargoweight;
                            document.getElementById(cargoWtUnit).value = weightunit;
                            document.getElementById(packageQty).value = packageqty;
                            //document.getElementById(hzd).value = hazardous;
                            if (hazardous == 'Y') {
                                $('#' + hzd).prop('checked', true).trigger('change');
                            } else {
                                $('#' + hzd).prop('checked', false).trigger('change');
                            }
                            document.getElementById(hscode).value = hscodes;
                            document.getElementById(eqpStatus).value = eqpstatus;
                            //document.getElementById(soc).value = socs;
                            if (socs == 'Y') {
                                $('#' + soc).prop('checked', true).trigger('change');
                            } else {
                                $('#' + soc).prop('checked', false).trigger('change');
                            }
                            getTareweightAmend(eqpsize, j);
                            document.getElementById(containerNo).value = eqpno;
                            cargoweightcalc(j);
                            //	changeCargoWT(j,weightunit,'amen');

                            if (pkg != "" && pkg != null)
                            {
                                //  console.log(packagename + "::::::::" + hiddenpackid);
                                $('#' + packagename).typeahead('destroy');
                                packageload(packagename, hiddenpackid);
                            }

                            checkBkgContNo(containerNo, soc, j);
                            // checkContainerValidate(containerNo, soc, j);

                            /*if (socs == 'Y') {
                             enableEqpStatus(eqpStatus, socs);
                             checkBkgContNo(containerNo, soc, j);
                             }*/
                            console.log("eqptype==>" + eqptype);
                            if (eqptype == 'O')
                            {
                                document.getElementById(dimUnit).value = unit;
                                document.getElementById(owLeft).value = left;
                                document.getElementById(owRight).value = right;
                                document.getElementById(olForward).value = forward;
                                document.getElementById(olAft).value = alt;
                                document.getElementById(oh).value = top;
                                console.log("oogval==>" + oogval);
                                if ($('#OOG_Plugin').val() == 'N') {
                                    $('#'+hidden_oog).val("Y");
                                    $('#'+oogDiv).show();
                                    $('.'+oogclass).addClass('required');
                                } else {
                                if ("Y" == oogval) {
                                    $('#'+hidden_oog).val("Y");
                                    $('.'+oogclass).addClass('required');
                                    $('#'+oogDiv).show();
                                    $('#oogflag' + j).attr('disabled', false).prop('checked', true);
                                } else {
                                     $('#oogflag' + j).attr('disabled', false).prop('checked', false).trigger('change');
                                }
                            }
                            }
                            if (eqptype == 'R')
                            {
                                document.getElementById(nor).value = norval;
                                if (norval == 'Y')
                                {
                                    setTempSetEnble(j);
                                } else
                                {
                                    document.getElementById(presetTemp).value = presettemp;
                                    if ($("#" + minTemp).length > 0) {
                                        document.getElementById(minTemp).value = mintemp;
                                    }
                                    if ($("#" + maxTemp).length > 0) {
                                        document.getElementById(maxTemp).value = maxtemp;
                                    }
                                    document.getElementById(tempUnit).value = tempunit;
                                    document.getElementById(ventid).value = ventval;
                                    if (humidityval != null && humidityval != 'null' && humidityval.trim().length > 0) {
                                        $("#" + humidityflagid).prop("checked", true).trigger('onchange');
                                        document.getElementById(humidityid).value = humidityval;
                                    } else {
                                        $("#" + humidityflagid).prop("checked", false).trigger('onchange');
                                    }
                                    if (drainsval != null && drainsval != 'null' && drainsval.trim().length > 0) {
                                        document.getElementById(drainsid).value = drainsval;
                                    }
                                    if (co2val != null && co2val != 'null' && co2val.trim().length > 0) {
                                        document.getElementById(co2id).value = co2val;
                                    }
                                    if (o2val != null && o2val != 'null' && o2val.trim().length > 0) {
                                        document.getElementById(o2id).value = o2val;
                                    }
                                }
                            }
                            //
                            if (hazardous == 'Y') {
                                //cargoselectmethod_hzd(hazardous, j);
                                //setDGValues(j);
                                document.getElementById(marinePollut).value = marinepollutant;
                                document.getElementById(unno).value = unnos;
                                document.getElementById(dgClass).value = dgclass;
                                document.getElementById(shippingName).value = shippingname;
                                document.getElementById(hiddencomid).value = shippingname;
                                document.getElementById(flashPoint).value = flashpoint;
                                document.getElementById(flashUnit).value = flashpointunit;
                                dgload(unno, hiddencomid, dgClass, shippingName);
                            } else
                            {
                                //cargoselectmethod_hzd(hazardous, j);
                                //setDGValues(j);
                                document.getElementById(marinePollut).value = "";
                                document.getElementById(unno).value = "";
                                document.getElementById(dgClass).value = "";
                                document.getElementById(shippingName).value = "";
                                document.getElementById(hiddencomid).value = "";
                                document.getElementById(flashPoint).value = "";
                            }
                            j++;
                        }
                        document.getElementById('importexcel').value = "";
                        $("#myImportModal").modal("hide");
                        return true;
                    } else
                    {
                        alert(entmandatoryfields);
                        document.getElementById('importexcel').value = "";
                        $("#myImportModal").modal("hide");
                        return true;
                    }
                }
            }
        }
    }
}



