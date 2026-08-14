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

function checkedData()
{
    var noncheckid = "";
    $('[name=select]:not(:checked)').each(function () {
        noncheckid = $(this).attr("id");
        document.getElementById(noncheckid).checked = true;
    });
    return true;
}


function ImportData()
{
    var importexcel = document.getElementById('importexcel').value;
    var extn = importexcel.substring(importexcel.lastIndexOf("."), importexcel.length).toLowerCase();
    var comdityflag = true;
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
        var allVals = [];
        $('[name=select]:checked').each(function () {
            allVals.push($(this).val());
        });
        $('[name=select]:not(:checked)').each(function () {
            allVals.push($(this).val());
        });
        var eqpsizeallvalues = $(".eqpclassall").map(function () {
            return trim(this.innerHTML);
        }).get();
        var commoditysrnval = [];
        var comflag = "";
        $('.checkcommodityvalue').each(function () {
            if ($(this).val() == null || $(this).val() == '') {
                comdityflag = false;
            }
            comflag = jQuery.inArray($(this).val(), commoditysrnval);
            if (comflag != null && comflag != undefined && comflag != 'undefined' && comflag != "" && comflag == -1)
            {
                commoditysrnval.push($(this).val());
            }
        });

        var appconsize = allVals.length;
        var selsize = allVals.length - 1;
        var totresponse;
        var response;
        var errresponse;
        var error;
        var countresponse;
        var overallresponse;
        var counttotresponse;
        //
        var cmdyallresponse;
        //
        var flag;
        var i = 0;
        var data;
        var checkcontianerordering = true;
        var commgroup = "";
        var cargowtresponse = "";
        var cwtresponse = "";
        if (searchReq.readyState == 4 || searchReq.readyState == 0)
        {

            data = new FormData();
            data.append('importexcel', $('#importexcel')[0].files[0]);

            var url = "ExcelImportData";
            searchReq.open("POST", url, true);
            searchReq.send(data);

            searchReq.onreadystatechange = function ()
            {
                //
                var sno = "", contno = "", quantity = "", commodity = "", hiddenpackid = "", hidpackage = "", hscode = "", hiddenhsid = "";
                var soc = "", eqpsize = "", carrierseal = "", shipperseal = "";
                var pack = "", volume = "", vunit = "", cargowt = "", weightunit = "", marks = "", descrip = "", rateeqpid = "", nor = "", eqptype = '';
                var oog = "", oogunit = "", left = "", right = "", forward = "", top = "", alt = "", itemforwardname = "", itemtopname = "", itemrightname = "", itemaftname = "", itemlengthUnitname = "", itemleftname = "", oogid = "", oogname = "", oogclass = "";
                var containerNo = "sicontainerid";
                var carshipcont = "sicontainerid";
                var checkbox = "";
                var containerNos = "", cno = "", sid = "", eqp = "", hidcty = "", hidhscodeval = "";
                ;
                var cseal = "", sseal = "", qty = "", ctysize = "", pkgtype = "", hscodeid = "", twt = "", groupcode = "";
                var vol = "", volunit = "", cgwt = "", wunit = "", mark = "", desp = "", socid = "";
                var eqptypeval = "", dummycontno = "", reqclass = "", qtyclass = "", weigthclass = "", tareweight = "";
                var volumeclass = "", select = "select";
                var arr = [];
                var foundPresent = false;
                var keyvalue = new Object();
                var keyvalue1 = new Object();
                var keyvalue2 = new Object();
                var keyvalue3 = new Object();
                var keyvalue4 = new Object();
                var pos = 1;
                var concont = 0;
                var thisval = 0;
                var posn = 0;
                var containerarray = new Array();
                var incclassarray = new Array();
                var keyobj;
                var incclass = "incclass";
                var incobj = "", classobj = "";
                var containerinc = 1;
                var containernoforseal = "";
                var contaarraa = new Array();
                var eqpsizeimportval = "";
                var eqpsizeimportvalue = "";
                var cmtyimportvalue = "";
                //
                if (searchReq.readyState == 4)
                {
                    closeLoader();
                    overallresponse = trim(searchReq.responseText);
                    //	console.log(":::::>>>>"+overallresponse)
                    if (overallresponse == "true")
                    {
                        alert(invxltemp);
                        return false;
                    }
                    if (overallresponse.indexOf("***~~~~~~~~~~~***") !== -1) {
                        cmdyallresponse = overallresponse.split("***~~~~~~~~~~~***");
                        overallresponse = cmdyallresponse[0];
                        cmtyimportvalue = cmdyallresponse[1];
                        //  cmtyimportvalue = cmtyimportvalue.replace(/~/g, "");
                        cmtyimportvalue = trim(cmtyimportvalue);
                    }
                    if (overallresponse.indexOf("**~~~~~~~~~~~**") !== -1) {
                        eqpsizeimportval = overallresponse.split("**~~~~~~~~~~~**");
                        overallresponse = eqpsizeimportval[0];
                        eqpsizeimportvalue = eqpsizeimportval[1];
                        eqpsizeimportvalue = eqpsizeimportvalue.replace(/~/g, "");
                        eqpsizeimportvalue = trim(eqpsizeimportvalue);
                    }
                    if (overallresponse.indexOf("~*~~~~~~~~~~~*~") !== -1) {
                        counttotresponse = overallresponse.split("~*~~~~~~~~~~~*~");
                        totresponse = counttotresponse[0];
                        countresponse = counttotresponse[1];
                        cargowtresponse = counttotresponse[2];
                        concont = countresponse.replace(/~/g, "");
                        concont = trim(concont);
                        //	console.log(":::>>>>>>>>>cwtresponse::::::::>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+cwtresponse);
                        if (cargowtresponse != null && cargowtresponse != 'undefined' && cargowtresponse != undefined && cargowtresponse != "") {
                            cwtresponse = cargowtresponse.replace(/~/g, "");
                            cwtresponse = trim(cwtresponse);
                        }

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
                        alert(morecontcountbl);
                        return false;
                    }
                    if (parseInt(concont) < parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(lesscontcountbl);
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
                    } else if (cwtresponse != null && cwtresponse != 'undefined' && cwtresponse != undefined && cwtresponse != "")
                    {
                        $("#myImportModal").modal("hide");
                        setTimeout(function () {
                            $("#myModalImportErrors").modal("show");
                        }, 500);
                        error = cwtresponse.replace(/~/g, "");
                        $('#importerr').html(error);
                        $('#datatableexcel1').DataTable();
                        return false;
                    } else if (response != null && response != "") {
                        var dup = response.split("~~~~");
                        var duparr = new Array();
                        var dupcont = "";
                        var precheck = "", cuurcheck = "";
                        var dupboolean = false;
                        var dupival = 0;
                        //console.log("dup len>>"+dup.length);
                        for (var dupinc = 0; dupinc < dup.length - 1; dupinc++)
                        {
                            dupboolean = false;
                            var dupres = dup[dupinc];
                            dupres = dupres.split("~~~");
                            dupcont = checkEmpty(dupres[1]);
                            for (var ari = 0; ari < duparr.length; ari++) {
                                //console.log("check>>"+duparr[ari]+">>11"+dupcont);
                                if (duparr[ari] == dupcont) {
                                    dupboolean = true;
                                    break;
                                }
                            }


                            if (!dupboolean) {
                                duparr[dupival] = dupcont;
                                dupival++;
                                precheck = dupcont;
                            } else if (dupcont != precheck) {
                                alert(contorderbl);
                                return false;
                            }
                        }

                    }
                    // for Equipment substitution
                    //		console.log("yes:::::::::::::::::::::::::>>>>>"+eqpsizeallvalues+":::::"+eqpsizeimportvalue);
                    /* if (eqpsizeallvalues != null && eqpsizeallvalues != 'undefined' && eqpsizeallvalues != undefined && eqpsizeimportvalue != null && eqpsizeimportvalue != 'undefined' && eqpsizeimportvalue != undefined)
                     {
                     //  console.log("yes::::::::>>>>>" + eqpsizeallvalues + ":::::" + eqpsizeimportvalue);
                     if (eqpsizeallvalues != eqpsizeimportvalue) {
                     alert(eqpsize);
                     return false;
                     }
                     
                     }*/
                    if (!comdityflag) {
                        //  console.log("comdityflag:::::::::::::::::::::::"+comdityflag);
                        alert(updatecommbl);
                        return false;
                    }
                    if (cmtyimportvalue != null && cmtyimportvalue != 'undefined' && cmtyimportvalue != undefined && commoditysrnval != null && commoditysrnval != 'undefined' && commoditysrnval != undefined)
                    {
                        //   console.log("yes::::::::>>>>>" + eqpsizeallvalues + ":::::" + eqpsizeimportvalue);
                        cmtyimportvalue = cmtyimportvalue.toString().toUpperCase().split("~~")[1].trim();
                        commoditysrnval = commoditysrnval.toString().toUpperCase().split("~~")[1].trim();
                        console.log("yes::::::::>>>>>" + cmtyimportvalue + ":::::" + commoditysrnval);
                        if (cmtyimportvalue != commoditysrnval) {
                            alert(commmismatchbl)
                            return false;
                        }
                    }
                    if (checkcontianerordering)
                    {
                        var req1 = response.split("~~~~");
                        var i = 0;
                        var j = 1;
                        var k = 0;
                        var inc = 0;
                        var aa = 0;
                        var m = 1;
                        var sel = 0;
                        var dummyobjinc = 1;
                        var incarrinc = 0, containernoincrement;
                        var cur = "", prev = "";
                        var iscontChanged = false;
                        var checkcncount = 1, checkcommoditycount = 1;
                        //console.log(req1.length-1);
                        for (i = 0; i < req1.length - 1; i++)
                        {
                            var reqresonse = req1[i];
                            var req = reqresonse.split("~~~");
                            sno = checkEmpty(req[0]);
                            contno = checkEmpty(req[1]);
                            soc = checkEmpty(req[2]);
                            eqpsize = checkEmpty(req[3]);
                            carrierseal = checkEmpty(req[4]);
                            shipperseal = checkEmpty(req[5]);
                            quantity = checkEmpty(req[6]);
                            commodity = checkEmpty(req[7]);
                            hscode = checkEmpty(req[8]);
                            pack = checkEmpty(req[9]);
                            volume = checkEmpty(req[10]);
                            vunit = checkEmpty(req[11]);
                            cargowt = checkEmpty(req[12]);
                            weightunit = checkEmpty(req[13]);
                            marks = checkEmpty(req[14]);
                            descrip = checkEmpty(req[15]);
                            rateeqpid = checkEmpty(req[16]);
                            eqptype = checkEmpty(req[17]);
                            nor = checkEmpty(req[18]);
                            oog = checkEmpty(req[19]);
                            oogunit = checkEmpty(req[20]);
                            left = checkEmpty(req[21]);
                            right = checkEmpty(req[22]);
                            forward = checkEmpty(req[23]);
                            top = checkEmpty(req[24]);
                            alt = checkEmpty(req[25]);

                            keyobj = {
                                "containernokey": contno,
                                "containernovalue": contno
                            };
                            contaarraa[i] = contno;
                            containerarray[i] = keyobj;
                            var dummyobj, dummyobj1;
                            var booleancheck = true;
                            //console.log("contno>>"+contno);
                            if (i == 0) {
                                incobj = "incclass1";
                                classobj = {
                                    "incclasskey": contno,
                                    "incclassvalue": incobj
                                };
                                incclassarray[incarrinc] = classobj;
                            } else {
                                //console.log("incclassarray length>>"+incclassarray.length);
                                if (incclassarray != null && incclassarray.length > 0) {
                                    for (var iii = 0; iii < incclassarray.length; iii++) {
                                        dummyobj = incclassarray[iii];
                                        if (dummyobj.incclasskey == contno) {
                                            //console.log("contno>>"+contno+"incclassvalue>>"+dummyobj.incclassvalue);
                                            booleancheck = false;
                                        } else {

                                        }
                                    }
                                    if (booleancheck) {
                                        //console.log("entered in else>>"+contno);
                                        dummyobjinc++;
                                        incarrinc++;
                                        dummyobj1 = "incclass" + dummyobjinc;
                                        dummyobj1 = {
                                            "incclasskey": contno,
                                            "incclassvalue": dummyobj1
                                        };
                                        incclassarray[incarrinc] = dummyobj1;
                                    }
                                }
                            }
                            //

                            if (i == 0) {
                                prev = contno;
                            }
                            cur = contno;
                            iscontChanged = (cur == prev) ? false : true;

                            if (iscontChanged) {
                                checkcommoditycount = 1;
                                checkcncount++;
                                prev = cur;

                            }
                            containerNo = "sicontainerid" + checkcncount;
                            containerNos = "sicontainerid" + checkcncount + checkcommoditycount;
                            var checkcommoditycount_1 = checkcommoditycount;
                            checkcommoditycount++;

                            cno = "containerno" + containerNo;
                            sid = containerNo + "#socID#";
                            eqp = "eqptypeval" + containerNo;
                            eqptypeval = containerNo + "#eqpType#";
                            checkbox = "selectsoc" + containerNo;
                            tareweight = "tareweight" + containerNo;
                            socid = "soc" + containerNo;
                            //
                            cseal = "carrierseal" + containerNo + "1";
                            sseal = "shipperseal" + containerNo + "1";
                            qty = "qty" + containerNos;
                            //    console.log("qty id before>>"+qty);
                            ctysize = "commodity" + containerNos;
                            hscodeid = "hscode" + containerNos;
                            pkgtype = "package" + containerNos;
                            vol = "volume" + containerNos;
                            volunit = "vunit" + containerNos;
                            cgwt = "weight" + containerNos;
                            wunit = "unit" + containerNos;
                            mark = "mark" + containerNos;
                            desp = "descp" + containerNos;
                            hidcty = "hiddencomid" + containerNos;
                            hiddenhsid = "hiddenhsid" + containerNos;
                            hiddenpackid = "hiddenpackid" + containerNos;
                            hidpackage = "hidpackage" + containerNos;
                            twt = 'twt' + containerNos;
                            hidhscodeval = "hidhscodeval" + containerNos;
                            //
                            var cdId = "cid" + containerNos;
                            var comdyId_new = "newcmdty" + containerNos;
                            var cmdId = 'commodity' + containerNos;
                            var hiddencomid = 'hiddencomid' + containerNos;
                            var qtyId = "qty" + containerNos;
                            var pkgId = "package" + containerNos;
                            var wtId = "weight" + containerNos;
                            var utId = "unit" + containerNos;
                            var volId = "volume" + containerNos;
                            var vutId = "vunit" + containerNos;
                            var mrkId = "mark" + containerNos;
                            var desId = "descp" + containerNos;
                            var qtyId_new = "newqtyid" + containerNos;
                            var wtId_new = "newwtid" + containerNos;
                            var volId_new = "newvolid" + containerNos;
                            var packId_new = "newpackid" + containerNos;
                            itemforwardname = containerNo + "#itemforward#" + checkcommoditycount_1;
                            itemtopname = containerNo + "#itemtop#" + checkcommoditycount_1;
                            itemrightname = containerNo + "#itemright#" + checkcommoditycount_1;
                            itemaftname = containerNo + "#itemaft#" + checkcommoditycount_1;
                            itemlengthUnitname = containerNo + "#itemlengthUnit#" + checkcommoditycount_1;
                            itemleftname = containerNo + "#itemleft#" + checkcommoditycount_1;
                            oogid = containerNo + "oogcheckbox";
                            oogname = containerNo + "#oog#";
                            oogclass = "oogclass" + containerNo;

                            //
                            //console.log("un>>>>"+$("#sitable").find("#"+qty).val());
                            // console.log();
                            if ($("#is_SI_restriction").val() == 'Y') {
                                if (document.getElementById(cno) != null) {
                                    document.getElementById(cno).value = contno;
                                    document.getElementById(cseal).value = carrierseal;
                                    document.getElementById(sseal).value = shipperseal;
                                }

                                document.getElementById(mrkId).value = marks;
                                document.getElementById(desId).value = descrip;
                            } else {
                                if ($("#sitable").find("#" + qty).val() == undefined || $("#sitable").find("#" + qty).val() == 'undefined') {
                                    //console.log("entereed in addRow_New");
                                    var addint = document.getElementById(cseal);
                                    var printin = addint.parentNode.parentNode.parentNode.parentNode.rowIndex;

                                    //var rowCount = checkcncount;
                                    var rowCount = printin;
                                    var inccls;
                                    posn = rowCount;
                                    //deepa.p
                                    if (incclassarray != null && incclassarray.length > 0) {
                                        for (var jjj = 0; jjj < incclassarray.length; jjj++) {
                                            dummyobj = incclassarray[jjj];
                                            if (dummyobj.incclasskey == contno) {
                                                inccls = dummyobj.incclassvalue;
                                            }
                                        }
                                    }
                                    select = "select" + (checkcncount - 1);
                                    addRow_New('sitable', containerNo, posn, select, inccls, "1", 'excel');
                                    //  addRow_New('sitable',containerNo,containerNo,cseal,ctysize,sseal,cgwt,qty,pkgtype,vol,wunit,'1','0',posn,'1',reqclass,select,qtyclass,weigthclass,volumeclass,inccls);
                                } else {

                                }
                                if ($("#" + qty).length == 0) {
                                    $("#" + cno).parents(".container-body-inner").find(".btn_add_si:first").click().toggle('click');
                                    console.log("add New Commodity!!")
                                }
                                if (document.getElementById(cno) != null) {
                                    document.getElementById(cno).value = contno;
                                    $('#eqptypeval' + containerNo).parents('.box_bg').find('.container_code').html(contno);
                                    if (soc == "y" || soc == "yes" || soc == "Yes" || soc == "YES")
                                    {
                                        document.getElementById(checkbox).checked = true;
                                    }
                                    document.getElementById(eqp).innerHTML = eqpsize;
                                    document.getElementById(eqptypeval).value = eqpsize;
                                    document.getElementById(cseal).value = carrierseal;
                                    document.getElementById(sseal).value = shipperseal;
                                }
                                //	console.log("qty:::::"+qty);
                                document.getElementById(qty).value = quantity;
                                document.getElementById(ctysize).value = commodity;
                                document.getElementById(hscodeid).value = hscode;
                                document.getElementById(hidhscodeval).value = hscode.replace("\"", "&quot;");
                                document.getElementById(pkgtype).value = pack;
                                document.getElementById(vol).value = volume;
                                document.getElementById(volunit).value = vunit;
                                document.getElementById(cgwt).value = cargowt;
                                document.getElementById(wunit).value = weightunit;
                                document.getElementById(mark).value = marks;
                                document.getElementById(desp).value = descrip;
                                //
                                document.getElementById(qtyId_new).value = quantity;
                                document.getElementById(comdyId_new).value = commodity;
                                document.getElementById(packId_new).value = pack;
                                document.getElementById(volId_new).value = volume;
                                document.getElementById(vutId).value = vunit;
                                document.getElementById(wtId_new).value = cargowt;
                                document.getElementById(utId).value = weightunit;
                                document.getElementById(mrkId).value = marks;
                                document.getElementById(desId).value = descrip;
                                //
                                var rat_eqp = document.getElementById(containerNo + "rateeqptypeid").value;
                                console.log('old-rate eqp id=> ' + rat_eqp);
                                if (rat_eqp == null || rat_eqp == 'null' || rat_eqp == '' || typeof rat_eqp == "undefined" || rat_eqp.trim().length == 0) {
                                    document.getElementById(containerNo + "rateeqptypeid").value = rateeqpid;
                                    console.log('excel rateeqptypeid updated.....')
                                }
                                //document.getElementById(containerNo + "#eqpType#").value = splitval[3];
                                // $('#eqptypeval' + containerNo).html(splitval[3]);
                                if (eqptype != null && eqptype != 'null' && eqptype != '' && eqptype.trim().length > 0) {
                                    document.getElementById(containerNo + "#typeID#").value = eqptype;
                                }
                                if (nor != null && nor != 'null' && nor != '' && nor.trim().length > 0) {
                                    document.getElementById(containerNo + "#nor#").value = nor;
                                }
                                //
                                setnewvaluesComdty(comdyId_new, cmdId);
                                setnewvalues(qtyId_new, qtyId);
                                setnewvalues(volId_new, volId);
                                setnewvalues(packId_new, pkgId);
                                setnewvalues(wtId_new, wtId);
                                $('#' + ctysize).typeahead('destroy');
                                $('#' + hscodeid).typeahead('destroy');
                                $('#' + pkgtype).typeahead('destroy');
                                commgroup = "";
                                commgroup = commodity.split("~~");
                                groupcode = commgroup[1];
                                groupcode = trim(groupcode);

                                commodityload(ctysize, hidcty, hscodeid, hiddenhsid, groupcode);
                                packageload(pkgtype, hidpackage);
                                checkBLContNo(cno, socid, tareweight, wunit, eqpsize);
                                calcBLTareWeight(document.getElementById(wunit).value, tareweight, twt, cgwt, '');
                                calculateGrossWeight(containerNo, containerNos);
                                //  checkSIeqpsize(cno,socid,eqptypeval,eqp);
                                //alert(qty);
                                //alert($("#sitable").find("#"+qty).val());
                                if ("Y" == $('#oogplugin').val() || "y" == $('#oogplugin').val()) {
                                    $.each(EquipmentJson, function (i, obj) {
                                        if (eqpsize == obj.equipSize && ('O' == obj.equipType || 'o' == obj.equipType)) {
                                            $('#' + oogid).attr('disabled', false);
                                            if ("Y" == oog || "y" == oog) {

                                                console.log("OOG...................................");
                                                console.log(top + ',' + right + ',' + alt + "," + oogunit + ',' + left + ',' + forward);
                                                console.log("itemtopname........" + itemtopname);
                                                $('#' + oogid).prop('checked', true);//.trigger('change');
                                                $("#" + containerNo + "\\#oog\\#").val("Y");
                                                //document.getElementById(containerNo + "#oog#").value = 'Y';
                                                $('#' + oogid).val('Y');
                                                $("." + oogclass).addClass('required');
                                                $("div#oogblock" + containerNo).show();
                                                // oogEnable($('#' + oogid), containerNo, oogclass);
                                                $('#' + itemtopname).val(top);
                                                $('#' + itemrightname).val(right);
                                                $('#' + itemaftname).val(alt);
                                                $('#' + itemlengthUnitname).val(oogunit);
                                                $('#' + itemleftname).val(left);
                                                $('#' + itemforwardname).val(forward);
                                                document.getElementById(itemtopname).value = top;
                                                document.getElementById(itemrightname).value = right;
                                                document.getElementById(itemaftname).value = alt;
                                                document.getElementById(itemlengthUnitname).value = oogunit;
                                                document.getElementById(itemleftname).value = left;
                                                document.getElementById(itemforwardname).value = forward;
                                            } else {
                                                $('#' + oogid).prop('checked', false).trigger('change');
                                                // oogEnable($('#' + oogid), containerNo, oogclass);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        loadAccordionHeading();
                        document.getElementById('importexcel').value = "";
                        $("#myImportModal").modal("hide");
                        checkedData();
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
function ImportDataCustomize()
{

    var importexcel = document.getElementById('importexcel').value;
    var extn = importexcel.substring(importexcel.lastIndexOf("."), importexcel.length).toLowerCase();
    var comdityflag = true;
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
        var allVals = [];
        $('[name=select]:checked').each(function () {
            allVals.push($(this).val());
        });
        $('[name=select]:not(:checked)').each(function () {
            allVals.push($(this).val());
        });

        var commoditysrnval = [];
        //
        var appconsize = allVals.length;
        var totresponse;
        var response;
        var errresponse;
        var error;
        var countresponse;
        var overallresponse;
        var counttotresponse;
        //
        var cmdyallresponse;
        //
        var flag;
        var i = 0;
        var data;
        var checkcontianerordering = true;
        var commgroup = "";
        var cargowtresponse = "";
        var cwtresponse = "";
        if (searchReq.readyState == 4 || searchReq.readyState == 0)
        {

            data = new FormData();
            data.append('importexcel', $('#importexcel')[0].files[0]);

            var url = "SICustomizeExcelImportData";
            searchReq.open("POST", url, true);
            searchReq.send(data);

            searchReq.onreadystatechange = function ()
            {
                //~*~~~~~~~~~~~*~
                var sno = "", contno = "", quantity = "", commodity = "", hidpackage = "", hscode = "", hiddenhsid = "";
                var soc = "", eqpsize = "", carrierseal = "", shipperseal = "";
                var pack = "", volume = "", vunit = "", cargowt = "", weightunit = "", marks = "", descrip = "", rateeqpid = "", nor = "", eqptype = '';
                var containerNo = "sicontainerid";
                var checkbox = "";
                var containerNos = "", cno = "", sid = "", eqp = "", hidcty = "", hidhscodeval = "";
                var cseal = "", sseal = "", qty = "", ctysize = "", pkgtype = "", hscodeid = "", twt = "", groupcode = "";
                var vol = "", volunit = "", cgwt = "", wunit = "", mark = "", desp = "", socid = "";
                var eqptypeval = "", tareweight = "";
                var select = "select";
                var concont = 0;
                var posn = 0;
                var containerarray = new Array();
                var incclassarray = new Array();
                var keyobj;
                var incobj = "", classobj = "";
                var contaarraa = new Array();
                var eqpsizeimportval = "";
                var eqpsizeimportvalue = "";
                var cmtyimportvalue = "";
                //
                if (searchReq.readyState == 4)
                {
                    closeLoader();
                    overallresponse = trim(searchReq.responseText);
                    if (overallresponse == "true")
                    {
                        alert(invxltemp);
                        return false;
                    }

                    if (overallresponse.indexOf("***~~~~~~~~~~~***") !== -1) {
                        cmdyallresponse = overallresponse.split("***~~~~~~~~~~~***");
                        overallresponse = cmdyallresponse[0];
                        cmtyimportvalue = cmdyallresponse[1];
                        cmtyimportvalue = trim(cmtyimportvalue);
                    }

                    if (overallresponse.indexOf("**~~~~~~~~~~~**") !== -1) {
                        eqpsizeimportval = overallresponse.split("**~~~~~~~~~~~**");
                        overallresponse = eqpsizeimportval[0];
                        eqpsizeimportvalue = eqpsizeimportval[1];
                        eqpsizeimportvalue = eqpsizeimportvalue.replace(/~/g, "");
                        eqpsizeimportvalue = trim(eqpsizeimportvalue);
                    }

                    if (overallresponse.indexOf("~*~~~~~~~~~~~*~") !== -1) {
                        counttotresponse = overallresponse.split("~*~~~~~~~~~~~*~");
                        totresponse = counttotresponse[0];
                        countresponse = counttotresponse[1];
                        cargowtresponse = counttotresponse[2];
                        concont = countresponse.replace(/~/g, "");
                        concont = trim(concont);
                        if (cargowtresponse != null && cargowtresponse != 'undefined' && cargowtresponse != undefined && cargowtresponse != "") {
                            cwtresponse = cargowtresponse.replace(/~/g, "");
                            cwtresponse = trim(cwtresponse);
                        }
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
                        alert(morecontcountsi);
                        return false;
                    }
                    if (parseInt(concont) < parseInt(appconsize))
                    {
                        alert(lesscontcountsi);
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
                    } else if (cwtresponse != null && cwtresponse != 'undefined' && cwtresponse != undefined && cwtresponse != "")
                    {
                        $("#myImportModal").modal("hide");
                        setTimeout(function () {
                            $("#myModalImportErrors").modal("show");
                        }, 500);
                        error = cwtresponse.replace(/~/g, "");
                        $('#importerr').html(error);
                        $('#datatableexcel1').DataTable();
                        return false;
                    } else if (response != null && response != "") {
                        var dup = response.split("~~~~");
                        var duparr = new Array();
                        var dupcont = "";
                        var precheck = "", cuurcheck = "";
                        var dupboolean = false;
                        var dupival = 0;

                        for (var dupinc = 0; dupinc < dup.length - 1; dupinc++)
                        {
                            dupboolean = false;
                            var dupres = dup[dupinc];
                            dupres = dupres.split("~~~");
                            dupcont = checkEmpty(dupres[1]);
                            for (var ari = 0; ari < duparr.length; ari++) {
                                if (duparr[ari] == dupcont) {
                                    dupboolean = true;
                                    break;
                                }
                            }


                            if (!dupboolean) {
                                duparr[dupival] = dupcont;
                                dupival++;
                                precheck = dupcont;
                            } else if (dupcont != precheck) {
                                alert(contorderbl);
                                return false;
                            }
                        }

                    }

                    if (!comdityflag) {
                        alert(updatecommbl);
                        return false;
                    }

                    if (cmtyimportvalue != null && cmtyimportvalue != 'undefined' && cmtyimportvalue != undefined && commoditysrnval != null && commoditysrnval != 'undefined' && commoditysrnval != undefined)
                    {
                        cmtyimportvalue = cmtyimportvalue.toString().toUpperCase().split("~~")[1].trim();
                        commoditysrnval = commoditysrnval.toString().toUpperCase().split("~~")[1].trim();
                        console.log("yes::::::::>>>>>" + cmtyimportvalue + ":::::" + commoditysrnval);
                        if (cmtyimportvalue != commoditysrnval) {
                            alert(commmismatchbl)
                            return false;
                        }
                    }

                    if (checkcontianerordering)
                    {
                        var req1 = response.split("~~~~");
                        var i = 0;
                        var j = 1;
                        var k = 0;
                        var inc = 0;
                        var aa = 0;
                        var m = 1;
                        var sel = 0;
                        var dummyobjinc = 1;
                        var incarrinc = 0, containernoincrement;
                        var cur = "", prev = "";
                        var iscontChanged = false;
                        var checkcncount = 1, checkcommoditycount = 1;
                        //console.log(req1.length-1);
                        for (i = 0; i < req1.length - 1; i++)
                        {
                            var reqresonse = req1[i];
                            var req = reqresonse.split("~~~");
                            sno = checkEmpty(req[0]);
                            contno = checkEmpty(req[1]);
                            soc = checkEmpty(req[2]);
                            eqpsize = checkEmpty(req[3]);
                            carrierseal = checkEmpty(req[4]);
                            shipperseal = checkEmpty(req[5]);
                            quantity = checkEmpty(req[6]);
                            pack = checkEmpty(req[7]);
                            cargowt = checkEmpty(req[8]);
                            marks = checkEmpty(req[9]);
                            descrip = checkEmpty(req[10]);
                            rateeqpid = checkEmpty(req[11]);
                            eqptype = checkEmpty(req[12]);
                            nor = checkEmpty(req[13]);

                            keyobj = {
                                "containernokey": contno,
                                "containernovalue": contno
                            };
                            contaarraa[i] = contno;
                            containerarray[i] = keyobj;
                            var dummyobj, dummyobj1;
                            var booleancheck = true;
                            //console.log("contno>>"+contno);
                            if (i == 0) {
                                incobj = "incclass1";
                                classobj = {
                                    "incclasskey": contno,
                                    "incclassvalue": incobj
                                };
                                incclassarray[incarrinc] = classobj;
                            } else {
                                if (incclassarray != null && incclassarray.length > 0) {
                                    for (var iii = 0; iii < incclassarray.length; iii++) {
                                        dummyobj = incclassarray[iii];
                                        if (dummyobj.incclasskey == contno) {
                                            booleancheck = false;
                                        } else {

                                        }
                                    }
                                    if (booleancheck) {
                                        dummyobjinc++;
                                        incarrinc++;
                                        dummyobj1 = "incclass" + dummyobjinc;
                                        dummyobj1 = {
                                            "incclasskey": contno,
                                            "incclassvalue": dummyobj1
                                        };
                                        incclassarray[incarrinc] = dummyobj1;
                                    }
                                }
                            }
                            //

                            if (i == 0) {
                                prev = contno;
                            }
                            cur = contno;
                            iscontChanged = (cur == prev) ? false : true;

                            if (iscontChanged) {
                                checkcommoditycount = 1;
                                checkcncount++;
                                prev = cur;

                            }
                            containerNo = "sicontainerid" + checkcncount;
                            containerNos = "sicontainerid" + checkcncount + checkcommoditycount;
                            checkcommoditycount++;

                            cno = "containerno" + containerNo;
                            sid = containerNo + "#socID#";
                            eqp = "eqptypeval" + containerNo;
                            eqptypeval = containerNo + "#eqpType#";
                            checkbox = "selectsoc" + containerNo;
                            tareweight = "tareweight" + containerNo;
                            socid = "soc" + containerNo;
                            //
                            cseal = "carrierseal" + containerNo + "1";
                            sseal = "shipperseal" + containerNo + "1";
                            qty = "qty" + containerNos;
                            pkgtype = "package" + containerNos;
                            cgwt = "weight" + containerNos;
                            mark = "mark" + containerNos;
                            desp = "descp" + containerNos;
                            hidcty = "hiddencomid" + containerNos;
                            hiddenhsid = "hiddenhsid" + containerNos;
                            hiddenpackid = "hiddenpackid" + containerNos;
                            hidpackage = "hidpackage" + containerNos;
                            twt = 'twt' + containerNos;
                            wunit = "unit" + containerNos;
                            //
                            var cdId = "cid" + containerNos;
                            var qtyId = "qty" + containerNos;
                            var pkgId = "package" + containerNos;
                            var wtId = "weight" + containerNos;
                            var mrkId = "mark" + containerNos;
                            var desId = "descp" + containerNos;
                            var qtyId_new = "newqtyid" + containerNos;
                            var wtId_new = "newwtid" + containerNos;
                            var packId_new = "newpackid" + containerNos;
                            //
                            /*  if ($("#is_SI_restriction").val() == 'Y') {
                             if (document.getElementById(cno) != null) {
                             document.getElementById(cno).value = contno;
                             document.getElementById(cseal).value = carrierseal;
                             document.getElementById(sseal).value = shipperseal;
                             }
                             
                             document.getElementById(mrkId).value = marks;
                             document.getElementById(desId).value = descrip;
                             } else {*/
                            if ($("#sitable").find("#" + qty).val() == undefined || $("#sitable").find("#" + qty).val() == 'undefined') {
                                //    console.log("entereed in addRow_New");
                                var addint = document.getElementById(cseal);
                                var printin = addint.parentNode.parentNode.parentNode.parentNode.rowIndex;

                                //var rowCount = checkcncount;
                                var rowCount = printin;
                                var inccls;
                                posn = rowCount;
                                //deepa.p
                                if (incclassarray != null && incclassarray.length > 0) {
                                    for (var jjj = 0; jjj < incclassarray.length; jjj++) {
                                        dummyobj = incclassarray[jjj];
                                        if (dummyobj.incclasskey == contno) {
                                            inccls = dummyobj.incclassvalue;
                                        }
                                    }
                                }
                                select = "select" + (checkcncount - 1);
                                //  console.log("posn>>" + posn);
                                addRow_New('sitable', containerNo, posn, select, inccls, "1", 'excel');
                                //addRow_New('sitable',containerNo,containerNo,cseal,ctysize,sseal,cgwt,qty,pkgtype,vol,wunit,'1','0',posn,'1',reqclass,select,qtyclass,weigthclass,volumeclass,inccls);
                            } else {

                            }
                           /* if ($("#" + qty).length == 0) {
                                $("#" + cno).parents(".container-body-inner").find(".btn_add_si:first").click().toggle('click');
                                console.log("add New Commodity!!")
                            }*/
                            if (document.getElementById(cno) != null) {
                                document.getElementById(cno).value = contno;
                                $('#eqptypeval' + containerNo).parents('.box_bg').find('.container_code').html(contno);
                                if (soc == "y" || soc == "yes" || soc == "Yes" || soc == "YES")
                                {
                                    document.getElementById(checkbox).checked = true;
                                }
                                document.getElementById(socid).value = soc;
                                document.getElementById(eqp).innerHTML = eqpsize;
                                document.getElementById(eqptypeval).value = eqpsize;
                                document.getElementById(cseal).value = carrierseal;
                                document.getElementById(sseal).value = shipperseal;
                            }
                            //   console.log("cgwt:::::" + cgwt);
                            //   console.log("quantity:::::" + qty);
                            document.getElementById(qty).value = quantity;
                            document.getElementById(pkgtype).value = pack;
                            document.getElementById(cgwt).value = cargowt;
                            document.getElementById(mark).value = marks;
                            document.getElementById(desp).value = descrip;
                            //
                            document.getElementById(qtyId_new).value = quantity;
                            document.getElementById(packId_new).value = pack;
                            document.getElementById(wtId_new).value = cargowt;
                            document.getElementById(mrkId).value = marks;
                            document.getElementById(desId).value = descrip;
                            //
                            var rat_eqp = document.getElementById(containerNo + "rateeqptypeid").value;
                            console.log('old-rate eqp id=> ' + rat_eqp);
                            if (rat_eqp == null || rat_eqp == 'null' || rat_eqp == '' || typeof rat_eqp == "undefined" || rat_eqp.trim().length == 0) {
                                document.getElementById(containerNo + "rateeqptypeid").value = rateeqpid;
                                console.log('excel rateeqptypeid updated.....')
                            }
                            if (eqptype != null && eqptype != 'null' && eqptype != '' && eqptype.trim().length > 0) {
                                document.getElementById(containerNo + "#typeID#").value = eqptype;
                            }
                            if (nor != null && nor != 'null' && nor != '' && nor.trim().length > 0) {
                                document.getElementById(containerNo + "#nor#").value = nor;
                            }
                            //
                            setnewvalues(qtyId_new, qtyId);
                            setnewvalues(packId_new, pkgId);
                            setnewvalues(wtId_new, wtId);
                            $('#' + pkgtype).typeahead('destroy');
                            packageload(pkgtype, hidpackage);
                            checkSIContNo(cno, socid, tareweight, wunit, eqpsize);
                            calcSITareWeight(document.getElementById(wunit).value, tareweight, twt, cgwt, '');
                            calculateGrossWeight(containerNo, containerNos);
                            //}
                        }
                        document.getElementById('importexcel').value = "";
                        $("#myImportModal").modal("hide");
                        checkedData();
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