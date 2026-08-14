/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function () {

    var customDateDDMMMYYYYToOrd = function (date) {
        "use strict"; //let's avoid tom-foolery in this function
        // Convert to a number YYYYMMDD which we can use to order
        var datearr = date.split(' ');
        var dateParts = datearr[0].split(/-/);
        var result = (dateParts[2] * 10000) + ($.inArray(dateParts[1].toUpperCase(), ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"]) * 100) + (dateParts[0] * 1);
        if (datearr.length > 1)
        {
            var timearr = datearr[1].split(':');
            if (timearr.length > 1) {
                result = result + timearr[0] + timearr[1];
            }
        }
        return (result * 1);
    };

// This will help DataTables magic detect the "dd-MMM-yyyy" format; Unshift
// so that it's the first data type (so it takes priority over existing)
    jQuery.fn.dataTableExt.aTypes.unshift(
            function (sData) {
                "use strict"; //let's avoid tom-foolery in this function
                sData = sData.split(' ')[0];
                if (/^([0-2]?\d|3[0-1])-(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)-\d{4}/i.test(sData)) {
                    return 'date-dd-mmm-yyyy';
                }
                return null;
            }
    );

// define the sorts
    jQuery.fn.dataTableExt.oSort['date-dd-mmm-yyyy-asc'] = function (a, b) {
        "use strict"; //let's avoid tom-foolery in this function
        var ordA = customDateDDMMMYYYYToOrd(a),
                ordB = customDateDDMMMYYYYToOrd(b);
        return (ordA < ordB) ? -1 : ((ordA > ordB) ? 1 : 0);
    };

    jQuery.fn.dataTableExt.oSort['date-dd-mmm-yyyy-desc'] = function (a, b) {
        "use strict"; //let's avoid tom-foolery in this function
        var ordA = customDateDDMMMYYYYToOrd(a),
                ordB = customDateDDMMMYYYYToOrd(b);
        return (ordA < ordB) ? 1 : ((ordA > ordB) ? -1 : 0);
    };

})();