/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Key mapping
const keyMapping = {
    "0": "oldcontainerno",
    "1": "eqptype",
    "2": "soc",
    "3": "newcontainerno",
    "4": "carrierseal",
    "5": "shipperseal",
    "6": "commoditygroup",
    "7": "hscode",
    "8": "packagetype",
    "9": "quantity",
    "10": "cargowieght",
    "11": "cargowieghtunit",
    "12": "volume",
    "13": "volumeunit",
    "14": "marksno",
    "15": "description"
};
var smartImpObj;
var numericColumns = [9, 10, 12];
var dropdownPlaceholders = {
    //6: 'Search, type or select commodity',
    7: 'Search, type or select HS code',
    8: 'Search, type or select type of packaging',
    11: 'Search or select unit',
    13: 'Search or select unit'
};

(function injectSmartImportStyles() {
    if (document.getElementById('si-grid-styles')) return;
    var style = document.createElement('style');
    style.id = 'si-grid-styles';
    style.textContent =
        '#siblsmartimport td[data-readonly="true"] { background-color: #f0f0f0 !important; color: #666; }' +
        '#siblsmartimport tbody tr:nth-child(even) td:not([data-readonly="true"]) { background-color: #fafafa; }' +
        '#siblsmartimport tbody tr:nth-child(odd) td:not([data-readonly="true"]) { background-color: #ffffff; }' +
        '#siblsmartimport td { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }' +
        '#siblsmartimport td:hover { overflow: visible; white-space: normal; word-break: break-word; z-index: 1; position: relative; }' +
        '#siblsmartimportbox { max-height: 75vh; overflow: auto; }' +
        '#siblsmartimport thead td { position: sticky; top: 0; z-index: 2; background-color: #00bcd4; color: #fff; }' +
        '#siblsmartimport td.si-dropdown-placeholder::before { content: attr(data-placeholder); color: #aaa; font-style: italic; pointer-events: none; }' +
        '#siblsmartimport .jexcel_filter { margin-bottom: 0; }' +
        '#siblsmartimport .jexcel_filter > div { padding: 2px 4px; }';
    document.head.appendChild(style);
})();

function applyReadOnlyStyle(obj) {
    var readOnlyCols = [0, 1, 2, 6];
    var rows = obj.rows.length;
    for (var r = 0; r < rows; r++) {
        for (var i = 0; i < readOnlyCols.length; i++) {
            var cell = obj.getCellFromCoords(readOnlyCols[i], r);
            if (cell) {
                cell.setAttribute('data-readonly', 'true');
                cell.title = cell.innerText || '';
            }
        }
    }
}

function applyTooltips(obj) {
    var rows = obj.rows.length;
    var cols = obj.options.columns.length;
    for (var r = 0; r < rows; r++) {
        for (var c = 0; c < cols; c++) {
            var cell = obj.getCellFromCoords(c, r);
            if (cell) {
                cell.title = cell.innerText || '';
            }
        }
    }
}

function applyDropdownPlaceholders(obj) {
    var rows = obj.rows.length;
    for (var key in dropdownPlaceholders) {
        var colIndex = parseInt(key);
        for (var r = 0; r < rows; r++) {
            var cell = obj.getCellFromCoords(colIndex, r);
            if (cell) {
                var value = obj.getValueFromCoords(colIndex, r);
                if (!value || value.toString().trim() === '') {
                    cell.setAttribute('data-placeholder', dropdownPlaceholders[colIndex]);
                    cell.classList.add('si-dropdown-placeholder');
                } else {
                    cell.removeAttribute('data-placeholder');
                    cell.classList.remove('si-dropdown-placeholder');
                }
            }
        }
    }
}

function openSmartImportBox() {

    /* $.fancybox.open(
     '<div id="siblsmartimportbox"><div id="siblsmartimport"></div><div style="float: right;"><button type="button" class="btn btn-primary btn-sm" onclick="getSmartImportData()" autocomplete="off" id="smartimpupdate"> Update</button></div></div>',
     {
     padding: 0,
     showCloseButton: false,
     closeClick: false,
     maxWidth: "100%",
     maxHeight: "100%",
     fitToView: false,
     width: "100%",
     height: "100%",
     autoSize: false,
     scrollOutside: false,
     closeBtn: true,
     hideOnOverlayClick: false,
     overlayShow: true, // Enable overlay display
     helpers: {
     overlay: {closeClick: false}, // Disable click on overlay
     },
     afterLoad: function () {
     // Disable scrolling
     $('body').css('overflow', 'hidden');
     },
     afterClose: function () {
     // Restore scrolling after modal is closed
     $('body').css('overflow', '');
     },
     }
     );*/

    loadJExcel();
    // smartImport();
    $('#smartImportModal').modal('show');
}
function getValueById(id) {
    // console.log('params--->' + id)
    let element = document.getElementById(id);
    //  console.log("Looking for element with ID:" + element);
    return element ? element.value || "" : "";//element.textContent ||
}
function loadJExcel() {
// Call the destroy function
    //smartImpObj.destroy();
    destroyJexcel(smartImpObj, 'siblsmartimport');
    //
    //jexcel(document.getElementById('smartimport'), {});
    let containers = document.querySelectorAll('[id^="containernosicontainerid"]:not([id$="-error"])');
    let totalContainers = containers.length;
    console.log('Total number of containers:', totalContainers);
    let smartImportData = [];
    for (let i = 1; i <= totalContainers; i++) {
        //  console.log('Outerloop--->i Value--->' + i + 'conatiner--->' + totalContainers);
        let containerId = getValueById("containernosicontainerid" + i);
        let eqpType = getValueById("sicontainerid" + i + "#eqpType#");
        let soc = getValueById("socsicontainerid" + i);
        let carrierSeal = getValueById("carriersealsicontainerid" + i + "1");
        let shipperSeal = getValueById("shippersealsicontainerid" + i + "1");

        let commodities = document.querySelectorAll(`#sicontainerid${i} [id^="commoditysicontainerid${i}"]:not([id$="-error"])`);
        let totalCommodities = commodities.length;
        console.log('Innerloop--->j Value---> commodities--->' + totalCommodities);
        for (let j = 1; j <= totalCommodities; j++) {
            // console.log('Innerloop--->j Value--->' + j + 'commodities--->' + totalCommodities);
            let cmdtygrp = getValueById("commoditysicontainerid" + i + j);
            let hscode = getValueById("hscodesicontainerid" + i + j);
            let packageInfo = getValueById("packagesicontainerid" + i + j);
            let quantity = getValueById("qtysicontainerid" + i + j);
            let cargoWt = getValueById("weightsicontainerid" + i + j);
            let cargoWtUnt = getValueById("unitsicontainerid" + i + j);
            let volume = getValueById("volumesicontainerid" + i + j);
            let volumeUnt = getValueById("vunitsicontainerid" + i + j);
            let marksNo = getValueById("marksicontainerid" + i + j);
            let desc = getValueById("descpsicontainerid" + i + j);
//
            cmdtygrp = extractCode(cmdtygrp);
            hscode = extractCode(hscode);
            packageInfo = extractCode(packageInfo);

//
            let row = [
                containerId,
                eqpType,
                soc,
                containerId,
                carrierSeal,
                shipperSeal,

                cmdtygrp,
                hscode,
                packageInfo,
                quantity,
                cargoWt,
                cargoWtUnt,
                volume,
                volumeUnt,
                marksNo,
                desc

            ];
            smartImportData.push(row);
        }
    }

    console.log('SmartImportData--->Final--->Data--->' + smartImportData);


      var packagesgrpMapped = packagesJson.map(function (item) {
     return {
     id: item.packagecode,
     name: item.packagename
     };
     });
     /*
    var packagesgrpMapped = packagesJson.map(function (item) {
        return {
            id: item.packagename,
            name: item.packagename
        };
    });*/
    var cmdgrpMapped = cmdgrp.map(function (item) {
        return {
            id: item.commoditycode,
            name: item.commodityname
        };
    });

    var allHScodeee = allHScode.map(function (item) {
        return  {
            id: item.commoditycode,
            name: '(' + item.commoditycode + ') ' + item.commodityname
        };
    });
    //console.log('smartImportData.length--->', smartImportData.length);
    var impdata = smartImportData.length > 0 ? smartImportData : [];
    //  console.log('impdata--->', impdata);

    let isProgrammaticChange = false; // Flag to prevent recursive calls

// Dropdown filter function
    let HScode = [];
    dropdownFilter = function (instance, cell, c, r, source) {
        console.log('Inside --->dropdownFilter', )
        const selectedCommodity = instance.jexcel.getValueFromCoords(c - 1, r);
        // console.log('selectedCommodity--->', selectedCommodity)
        const openCommodity = openCommodityJsonArray;

        let filtered = [];
        if (openCommodity.includes(selectedCommodity)) {
            //     console.log('openCommodity.includes(selectedCommodity)----->', openCommodity.includes(selectedCommodity));
            filtered = commodityJson.filter(item => item.parentcode !== "0");
            HScode = filtered.map(function (item) {
                return {
                    id: item.commoditycode,
                    name: '(' + item.commoditycode + ') ' + item.commodityname
                };
            });
            //   console.log('HSCODE=====>', HScode)
            return HScode;
        } else if (!openCommodity.includes(selectedCommodity)) {
            //   console.log('openCommodity.includes(selectedCommodity)ELSE----->', openCommodity.includes(selectedCommodity))
            filtered = commodityJson.filter(item => item.grpcode == selectedCommodity);
            HScode = filtered.map(function (item) {
                return {
                    id: item.commoditycode,
                    name: '(' + item.commoditycode + ') ' + item.commodityname
                };
            })
            return HScode;
        }
        console.log('HScode=====>', HScode)
        return HScode;
    };

// Spreadsheet initialization
    smartImpObj = jexcel(document.getElementById('siblsmartimport'), {
        data: impdata,
        columnResize: true,
        allowInsertColumn: false,
        allowDeleteColumn: false,
        allowRenameColumn: false,
        search: true,
        wordWrap: false,
        columns: [
            {
                type: 'text',
                title: 'Container No',
                width: '130',
                class: 'sticky-column',
                readOnly: true
            },
            {
                type: 'text',
                title: 'Type',
                width: '50',
                readOnly: true,
                class: 'sticky-column',
            },
            {type: 'text', title: 'SOC', readOnly: true, width: '40'},
            {
                type: 'text',
                title: 'Actual Container No',
                width: '140',
                class: 'sticky-column',
            },
            {type: 'text', title: 'Carrier Seal', width: '100'},
            {type: 'text', title: 'Shipper Seal', width: '100'},
            {
                type: 'autocomplete',
                title: 'Commodity',
                width: '300',
                source: cmdgrpMapped,
                readOnly: true,
            },
            {
                type: 'autocomplete',
                title: 'HS Code and Name',
                width: '300',
                source: allHScodeee,
                filter: dropdownFilter,

            },
            {type: 'autocomplete', title: 'Type', width: '300', source: packagesgrpMapped},
            {type: 'numeric', title: 'Quantity', width: '80', decimal: '.'},
            {type: 'numeric', title: 'Cargo Weight', width: '100', decimal: '.'},
            {type: 'dropdown', title: 'Unit', width: '80', source: [{id: 'K', name: 'Kilos'}]},
            {type: 'numeric', title: 'Volume', width: '60', decimal: '.'},
            {type: 'dropdown', title: 'Unit', width: '100', source: [{id: 'M', name: 'Cubic Meter'}, {id: 'F', name: 'Cubic Feet'}]},

            {type: 'text', title: 'Marks No', width: '300'},
            {type: 'text', title: 'Commodity Description', width: '600'}
        ],
        contextMenu: function (obj, x, y, e) {
            var items = [];
            if (x == null) {
                if (obj.options.allowInsertRow == true) {
                    items.push({
                        title: obj.options.text.insertANewRowBefore,
                        onclick: function () {
                            const rowData = obj.getRowData(y);

                            const columnsToReplicate = [0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 15, 16, 17];

                            const newRowData = new Array(obj.options.columns.length).fill('');

                            columnsToReplicate.forEach(function (colIndex) {
                                newRowData[colIndex] = rowData[colIndex];
                            });

                            obj.insertRow(newRowData, parseInt(y), 1);
                        }
                    });

                    items.push({
                        title: obj.options.text.insertANewRowAfter,
                        onclick: function () {
                            const rowData = obj.getRowData(y);

                            // Replicate specific columns (e.g., Category and Food)
                            const columnsToReplicate = [0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 15, 16, 17]; // Category column (1) and Food column (2)

                            // Create a new row data array with empty values
                            const newRowData = new Array(obj.options.columns.length).fill('');

                            // Copy data for specific columns
                            columnsToReplicate.forEach(function (colIndex) {
                                newRowData[colIndex] = rowData[colIndex]; // Replicate Category and Food data
                            });

                            // Insert the new row after the current row (y)
                            obj.insertRow(newRowData, parseInt(y), 0);
                        }
                    });
                }
                if (obj.options.allowDeleteRow == true) {
                    items.push({
                        title: obj.options.text.deleteSelectedRows,
                        onclick: function () {
                            obj.deleteRow(obj.getSelectedRows().length ? undefined : parseInt(y));
                        }
                    });
                }

                if (x) {
                    if (obj.options.allowComments == true) {
                        items.push({type: 'line'});

                        var title = obj.records[y][x].getAttribute('title') || '';

                        items.push({
                            title: title ? obj.options.text.editComments : obj.options.text.addComments,
                            onclick: function () {
                                obj.setComments([x, y], prompt(obj.options.text.comments, title));
                            }
                        });

                        if (title) {
                            items.push({
                                title: obj.options.text.clearComments,
                                onclick: function () {
                                    obj.setComments([x, y], '');
                                }
                            });
                        }
                    }
                }
            }
            items.push({type: 'line'});
            return items;
        },
        onload: function (instance) {
            applyReadOnlyStyle(instance.jexcel);
            applyTooltips(instance.jexcel);
            applyDropdownPlaceholders(instance.jexcel);
        },
        oninsertrow: function (instance) {
            applyReadOnlyStyle(instance.jexcel);
            applyDropdownPlaceholders(instance.jexcel);
        },
        onselection: function (instance, x1, y1, x2, y2) {
            var totalCols = instance.jexcel.options.columns.length;
            if (x1 === 0 && x2 === totalCols - 1) {
                return;
            }
            var cell = instance.jexcel.getCellFromCoords(x2, y2);
            if (cell) {
                cell.scrollIntoView({block: 'nearest', inline: 'nearest'});
            }
        },
        onchange: function (instance, cell, c, r, value) {
            var td = instance.jexcel.getCellFromCoords(c, r);
            if (td) td.title = td.innerText || '';

            var colInt = parseInt(c);
            if (dropdownPlaceholders.hasOwnProperty(colInt) && td) {
                if (!value || value.toString().trim() === '') {
                    td.setAttribute('data-placeholder', dropdownPlaceholders[colInt]);
                    td.classList.add('si-dropdown-placeholder');
                } else {
                    td.removeAttribute('data-placeholder');
                    td.classList.remove('si-dropdown-placeholder');
                }
            }

            if (isProgrammaticChange)
                return;

            if (numericColumns.indexOf(Number(c)) !== -1 && value !== '') {
                var cleaned = value.toString().replace(/[^0-9.]/g, '');
                if (cleaned !== value.toString()) {
                    isProgrammaticChange = true;
                    try {
                        var cellName = jexcel.getColumnNameFromId([c, r]);
                        instance.jexcel.setValue(cellName, cleaned);
                    } finally {
                        isProgrammaticChange = false;
                    }
                    return;
                }
            }
            const firstColumnValue = instance.jexcel.getValueFromCoords(0, r); // Get value from the first column (column index 0)
            //console.log('firstColumnValue=>' + firstColumnValue);
            if (!firstColumnValue.trim()) { // If empty or only whitespace
                //console.log('First column is empty. Removing row:', r);
                instance.jexcel.deleteRow(r); // Delete the row
                return; // Exit further processing
            }

            if (c == 6) {
                isProgrammaticChange = true;
                const hsCodeCellElement = instance.jexcel.getCellFromCoords(Number(c) + 1, r);
                // console.log('hsCodeCellElement--->', hsCodeCellElement)
                const commodityValue1 = instance.jexcel.getCellFromCoords(c, r);
                const displayedCommodityValue = commodityValue1 ? commodityValue1.innerText.trim() : '';
                //  console.log('displayedCommodityValue--->', displayedCommodityValue);
                //   console.log('commodityValue1--->', commodityValue1)
                isProgrammaticChange = false;
                if (displayedCommodityValue) {
                    const hsCodeCell1 = jexcel.getColumnNameFromId([Number(c) + 1, r]);
                    //    console.log('hsCodeCell1-->', hsCodeCell1)
                    instance.jexcel.setValue(hsCodeCell1, '');
                }

                if (!hsCodeCellElement) {
                    isProgrammaticChange = false;
                    return; // Exit if the cell is undefined
                }
                try {
                    const foodCell = jexcel.getColumnNameFromId([Number(c) + 1, r]);
                    instance.jexcel.setValue(foodCell, ''); // Clear food cell
                    const hsCodeCellElement = instance.jexcel.getCellFromCoords(Number(c) + 1, r);

                    if (hsCodeCellElement) {
                        hsCodeCellElement.style.backgroundColor = ''; // Reset background color
                    }
                } finally {
                    isProgrammaticChange = false;
                }
            }

            if (c == 7) {
                const categoryValue = instance.jexcel.getValueFromCoords(c - 1, r);
                const allowedHScode = dropdownFilter(instance, cell, c, r, HScode);
                // console.log("allowedHScode=> " + JSON.stringify(allowedHScode));
                // Handle invalid food
                // console.log("value==? " + value)
                const isValueIncluded = allowedHScode.some(item => item.id === value);
                //  console.log('allowedHScode.includes(value)---->', isValueIncluded);
                if (!isValueIncluded) {
                    //      console.log('!allowedHScode.includes');
                    isProgrammaticChange = true;
                    try {
                        const hsCodeCellElement = instance.jexcel.getCellFromCoords(c, r);
                        if (hsCodeCellElement) {
                            hsCodeCellElement.style.backgroundColor = 'red'; // Highlight cell in red
                        }
                        instance.jexcel.setValue(cell, ''); // Clear invalid value
                    } finally {
                        isProgrammaticChange = false;
                    }
                } else {
                    //   console.log('allowedHScode.includes');
                    const hsCodeCellElement = instance.jexcel.getCellFromCoords(c, r);
                    if (hsCodeCellElement) {
                        hsCodeCellElement.style.backgroundColor = ''; // Reset background color for valid value
                    }
                }
            }
        },
        oneditionstart: function (instance, cell, x, y) {
            var colIndex = parseInt(x);
            if (dropdownPlaceholders.hasOwnProperty(colIndex)) {
                cell.classList.remove('si-dropdown-placeholder');
                setTimeout(function () {
                    var input = cell.querySelector('input');
                    if (input) {
                        input.setAttribute('placeholder', dropdownPlaceholders[colIndex]);
                    }
                }, 50);
            }
        },
        oneditionend: function (instance, cell, x, y) {
            var colIndex = parseInt(x);
            if (dropdownPlaceholders.hasOwnProperty(colIndex)) {
                var value = instance.jexcel.getValueFromCoords(colIndex, parseInt(y));
                if (!value || value.toString().trim() === '') {
                    cell.setAttribute('data-placeholder', dropdownPlaceholders[colIndex]);
                    cell.classList.add('si-dropdown-placeholder');
                }
            }
        }
    });
}
function getSmartImportData() {
    openLoader();
    
    // Use setTimeout to allow the browser to render the loader before processing
    setTimeout(function() {
        const jsonData = smartImpObj.getJson(); // Retrieve data in JSON format
        //console.log("jsonData=> "+jsonData);
        const restructuredData = restructureData(jsonData, keyMapping);
        //console.log("restructuredData=> "+restructuredData);
        const groupedData = groupBy(restructuredData, 'oldcontainerno');
        console.log("groupedData=> " + JSON.stringify(groupedData));

        /*let containers = document.querySelectorAll('[id^="containernosicontainerid"]');
         let totalContainers = containers.length;*/
        let containers = document.querySelectorAll('[id^="containernosicontainerid"]:not([id$="-error"])');
        let totalContainers = containers.length;

        let smartimplen = Object.keys(groupedData).length;
        console.log('Total number of containers in Screen:', totalContainers);
        console.log('Total number of containers in Smart Import :', smartimplen);
        if (totalContainers == smartimplen) {
            for (let i = 1; i <= totalContainers; i++) {
                // console.log('Outerloop--->i Value--->' + i + 'conatiner--->' + totalContainers);
                let oldcontainer = getValueById("containernosicontainerid" + i);
                if (groupedData.hasOwnProperty(oldcontainer)) {
                    //      console.log(`Group: ${oldcontainer}`);
                    const firstItem = groupedData[oldcontainer]?.[0];
                            if (firstItem) {
                        $("#containernosicontainerid" + i).val(firstItem["newcontainerno"]).trigger('blur').trigger('change');
                        $("#containernosicontainerid" + i).valid();
                        $('#eqptypevalsicontainerid' + i).parents('.box_bg').find('.container_code').html(firstItem["newcontainerno"]);
                        $("#carriersealsicontainerid" + i + "1").val(firstItem["carrierseal"]);
                        $("#shippersealsicontainerid" + i + "1").val(firstItem["shipperseal"]);

                        // Iterate over the array of items in the current group
                        let commodities = document.querySelectorAll(`#sicontainerid${i} [id^="commoditysicontainerid${i}"]:not([id$="-error"])`);
                        let screencount = commodities.length;
                        let simpcount = groupedData[oldcontainer].length;

                        if (screencount > simpcount) {
                            const mismatchCount = screencount - simpcount; // Calculate the number of mismatches

                            for (let i1 = 0; i1 < mismatchCount; i1++) {
                                // Trigger the action for each mismatch
                                $(`#containernosicontainerid${i}`).parents(".container-body-inner").find(".btn_remove_si:last").click().toggle('click');
                                $(`#containernosicontainerid${i}`).parents(".container-body-inner").find(".si_cmdty_remove_btn:last").click().toggle('click');
                            }
                        }
                        if (simpcount > screencount) {
                            const mismatchCount1 = simpcount - screencount; // Calculate the number of mismatches

                            for (let i2 = 0; i2 < mismatchCount1; i2++) {
                                // Trigger the action for each mismatch
                                $(`#containernosicontainerid${i}`).parents(".container-body-inner").find(".btn_add_si:first").click().toggle('click');
                            }
                        }

                    }
                }

                commodities = document.querySelectorAll(`#sicontainerid${i} [id^="commoditysicontainerid${i}"]`);
                let totalCommodities = commodities.length;
                console.log("totalCommodities==> " + totalCommodities);
                let simpcmdtyarr = groupedData[oldcontainer] || [];
                for (let j = 1; j <= totalCommodities; j++) {
                    let cmdtyobj = simpcmdtyarr[j - 1] || {};
                    //   console.log('Innerloop--->j Value--->' + j + 'cmdtyobj--->' + JSON.stringify(cmdtyobj));

                    // $("#hscodesicontainerid" + i + j).val(cmdtyobj["hscode"]);
                    // console.log("hscode==> " + cmdtyobj["hscode"]);
                    var hscodeobj = allHScode.filter(item => item.commoditycode === cmdtyobj["hscode"]);
                    //   console.log("hscodeobj==> " + hscodeobj);
                    //  console.log("hscodeobj1==> " + JSON.stringify(hscodeobj));
                    /*let hscodenameval = hscodeobj.map(function (item) {
                     return item.commodityname + " ~~ " + item.commoditycode;
                     });*/
                    let hscodenameval = hscodeobj.length > 0
                            ? hscodeobj[0].commodityname + " ~~ " + hscodeobj[0].commoditycode
                            : "";
                    // console.log("hscodenameval==> " + hscodenameval);
                    //
                    /*
                     $("#hidhscodevalsicontainerid" + i + j).val(hscodenameval);
                     $("#hiddenhsidsicontainerid" + i + j).val(hscodenameval);
                     $("#hscodesicontainerid" + i + j).val(hscodenameval);//.trigger('blur').trigger('change');
                     $("#hscodesicontainerid" + i + j).typeahead('val',hscodenameval);
                     $("#hscodesicontainerid" + i + j).trigger('blur').trigger('change');//.val(hscodenameval)
                     */
                    //
                    /*
                    // TODO: Remove this delay - Added for testing purpose
                    var testDelay = 2000; // 2 seconds delay for testing
                    var startTime = new Date().getTime();
                    while (new Date().getTime() < startTime + testDelay) {
                        // Blocking delay for testing
                    }
                    console.log("Testing delay completed for container " + i + ", commodity " + j);
                    */
                   
                    $("#hscodesicontainerid" + i + j).val(hscodenameval).trigger('blur').trigger('change');
                    $("#hscodesicontainerid" + i + j).typeahead('val', hscodenameval);
                    $("#hidhscodevalsicontainerid" + i + j).val(hscodenameval);

                    var packvalobj = packagesJson.filter(item => item.packagecode === cmdtyobj["packagetype"]);//packagename
                    let packcodenameval = packvalobj.map(function (item) {
                        return item.packagename + " ~~ " + item.packagecode;
                    });
                    $("#packagesicontainerid" + i + j).val(packcodenameval);
                    $("#packagesicontainerid" + i + j).typeahead('val', packcodenameval);
                    $("#newpackidsicontainerid" + i + j).val(packcodenameval);
                    //
                    $("#qtysicontainerid" + i + j).val(cmdtyobj["quantity"]).trigger('blur').trigger('change');
                    $("#weightsicontainerid" + i + j).val(cmdtyobj["cargowieght"]).trigger('blur').trigger('change');
                    $("#unitsicontainerid" + i + j).val(cmdtyobj["cargowieghtunit"]);
                    $("#volumesicontainerid" + i + j).val(cmdtyobj["volume"]);
                    $("#vunitsicontainerid" + i + j).val(cmdtyobj["volumeunit"]);
                    $("#marksicontainerid" + i + j).val(cmdtyobj["marksno"]);
                    $("#descpsicontainerid" + i + j).val(cmdtyobj["description"]);

                }
            }
            //$.fancybox.close();

            $('#smartImportModal').modal('hide');
        } else {
            alert("The container count does not match between SI(" + totalContainers + ") and Smart Import(" + smartimplen + ").");
        }
        // Call the destroy function
        //smartImpObj.destroy();
        //destroyJexcel(smartImpObj, 'siblsmartimport');
        
        // Wait for all AJAX calls to complete before closing the loader
        setTimeout(function() {
            if ($.active > 0) {
                $(document).one('ajaxStop', function() {
                    closeLoader();
                });
            } else {
                closeLoader();
            }
        }, 100);
    }, 100); // Small delay to allow loader to render
}

// Function to destroy the Jexcel instance
function destroyJexcel(instance, containerId) {
    // Remove the table from the DOM
    var container = document.getElementById(containerId);
    if (container && container.jexcel) {
        container.jexcel = null; // Clear the jexcel reference
        container.innerHTML = ''; // Clear the inner HTML
    }

    // Optionally clear the instance variable
    instance = null;
}


function extractCode(inputString) {
    if (!inputString || typeof inputString !== 'string' || inputString.trim() === '') {
        return "";
    }
    const parts = inputString.split('~~');

    const code = parts.length > 1 ? parts[1].trim() : "";
    return code;
}
// Function to restructure JSON
function restructureData(data, mapping) {
    return data.map(item => {
        const newItem = {};
        for (const [oldKey, newKey] of Object.entries(mapping)) {
            newItem[newKey] = item[oldKey] || ""; // Assign value or empty string if not present
        }
        return newItem;
    });
}

// Function to group by a key
function groupBy(data, key) {
    return data.reduce((acc, item) => {
        const groupKey = item[key];
        if (!acc[groupKey]) {
            acc[groupKey] = [];
        }
        acc[groupKey].push(item);
        return acc;
    }, {});
}

