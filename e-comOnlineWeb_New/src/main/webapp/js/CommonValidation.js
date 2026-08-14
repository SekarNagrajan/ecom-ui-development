/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var unsafeWords = new Array('create','insert','read','exec','drop', 'proc', 'xp_cmdshell','ftp', '@rootkey', '@key','@value','char()','drop','shutdown','waitfor','delay','print','syscolumns','sysobject',  'dba', 'admin', 'root', 'dbo', 'systypes', 'sysdatabases');
$(document).ready(function () {
    $("input[type='text'],textarea").bind("focusout", function(){
        remUnsafeWords(this);
    });
    $("input[type='text'],textarea").on('keyup',function(event){
        var length = 0;
        var e = event || window.event;
        var ctrlKey = e.ctrlKey;
        var charCode = e.which ? e.which : e.keyCode;
        if(ctrlKey && charCode==65)
        {
            $('#demo').html("ctl key pressed");
        }
        else
        {
            if($('#demo').html()!="ctl key pressed"){
                //alert("next");
                var currId = $(this).attr('id');
                var str = $(this).val().split("");
                var len = str.length;
                var finalVar = "";
                //var myRegxp = /^[a-zA-Z0-9 `~!@#$%^&*()-_+={}:;'"|\/<>,.?|]+$/;
                var myRegxp=/^[\u0000-\u0080\u0091-\u00BF\u00D7\u00F7]/;
                var pos = getInputSelection(document.getElementById(currId)).start;
                for(var i=0;i<len;i++)
                {
                    if(myRegxp.test(str[i]))
                    {
                        finalVar = finalVar+str[i];
                    }
                    else if(((str[i].charCodeAt(0)>8194) && (str[i].charCodeAt(0)<8261))||(str[i].charCodeAt(0)==8364)||(str[i].charCodeAt(0)==710)||(str[i].charCodeAt(0)==732)||(str[i].charCodeAt(0)==8482)){
                        finalVar = finalVar+str[i];
                    }
                    else
                    {
                        length++;
                    }
                }
                $('#'+currId).val(finalVar);
                var input = document.getElementById(currId);
                if (input.createTextRange) {
                    var range = input.createTextRange();
                    range.move('character',(pos-length));
                    range.select();
                }
                else
                {
                    input.setSelectionRange((pos-length), (pos-length));
                }
            }
            $('#demo').html("ctl key NOT pressed");
        }
    });
});
function remUnsafeWords(ele) {
    // Skip validation if disableCommonValidation is set to 'Y'
    var disableValidation = $('#disableCommonValidation').val();
    if (disableValidation === 'Y') {
        return;
    }
    var val = $(ele).val();
    var valArr = val.split(" ");
    for (var i = 0; i < valArr.length; i++) {
        if ($.inArray(valArr[i], unsafeWords) > -1 || $.inArray(valArr[i].toLowerCase(), unsafeWords) > -1) {
            valArr[i] = "";
        }
    }
    valArr = $.grep(valArr, function (a) {
        return a != "";
    });
    $(ele).val(valArr.join(" "));
}



function getInputSelection(el) {
    var x = 0;
    var userAgt = navigator.userAgent;
    var browserName  = navigator.appName;
    var fullVersion  = ''+parseFloat(navigator.appVersion);
    var majorVersion = parseInt(navigator.appVersion,10);
    var nameOffset,verOffset,ix;
    var start = 0, end = 0, normalizedValue, range,
    textInputRange, len, endRange;
    if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
        start = el.selectionStart;
        end = el.selectionEnd;
    } else {
        //alert("else")
        range = document.selection.createRange();

        var leng = 0;
        if (range && range.parentElement() == el) {
            len = el.value.length;
            normalizedValue = el.value.replace(/\r\n/g, "\n");
            //alert("normalizedvalue==>>"+normalizedValue);
            // Create a working TextRange that lives only in the input
            textInputRange = el.createTextRange();
            textInputRange.moveToBookmark(range.getBookmark());

            // Check if the start and end of the selection are at the very end
            // of the input, since moveStart/moveEnd doesn't return what we want
            // in those cases
            endRange = el.createTextRange();
            endRange.collapse(false);
            //alert("sssssss===>"+textInputRange.compareEndPoints("StartToEnd", endRange));
            if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
                //alert("R");
                start = end = len;
            } else {
                //alert("A");
                start = -textInputRange.moveStart("character", -len);
                start += normalizedValue.slice(0, start).split("\n").length - 1;

                if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
                    //alert("M");
                    end = len;
                } else {
                    //alert("E");
                    end = -textInputRange.moveEnd("character", -len);
                    end += normalizedValue.slice(0, end).split("\n").length - 1;
                }
            }
        }
    }
    if ((verOffset=userAgt.indexOf("MSIE"))!=-1) {
        browserName = "Microsoft Internet Explorer";
        fullVersion = (userAgt.substring(verOffset+5));
    }
    if ((ix=fullVersion.indexOf(";"))!=-1)
        fullVersion=(fullVersion.substring(0,ix));

    if ((ix=fullVersion.indexOf(" "))!=-1)
        fullVersion=fullVersion.substring(0,ix);
    majorVersion = parseInt(''+fullVersion,10);
    if (isNaN(majorVersion)) {
        fullVersion  = ''+parseFloat(navigator.appVersion);
        majorVersion = parseInt(navigator.appVersion,10);
    }
    if((browserName=="Microsoft Internet Explorer") && (parseFloat(fullVersion) < 9))
    {
        var str = el.value.substring(0,start);
        var leng;
        if(str.lastIndexOf('\n')!=-1)
        {
            leng = str.split('\n');
            x = leng.length-1;
        }
        start = start - x ;
    }
    return {
        start: start,
        end: end
    };
}

function checkvalue(elementRef, value1, fieldval) {
    document.getElementById(fieldval).value = value1.replace(/[^0-9.0-9]/g, '');
    value1 = document.getElementById(fieldval).value;
    var keyCodeEntered = (elementRef.which) ? elementRef.which : elementRef.keyCode;
    if (keyCodeEntered == 8 || keyCodeEntered == 37 || keyCodeEntered == 39 || keyCodeEntered == 46) {
        return true;
    }
    var val = value1;
    var afterdot;
    var beforedot;
    var bdot;
    if (val.indexOf(".") != -1)
    {
        afterdot = value1.split(".").pop();
        beforedot = isNaN(parseInt(value1, 10)) ? '0' : parseInt(value1, 10); //parseInt(value1, 10);
        bdot = beforedot.toString();
        if (afterdot.length > 2)
        {
            // var aftercon = afterdot.substring(0, afterdot.length-1);
            var aftercon = afterdot.slice(0, 3);
            var conval = bdot + "." + aftercon;
            document.getElementById(fieldval).value = conval;
            return false;
        }
    }
    else
    {
        if (val.length > 9 && keyCodeEntered != 190)
        {
            var newStr = val.slice(0, 8);
            document.getElementById(fieldval).value = newStr;
            return false;
        }
    }

}
function ValDigit(elementRef, val) {
    var keyCodeEntered = (elementRef.which) ? elementRef.which : elementRef.keyCode;
    if ((verOffset = nAgt.indexOf("Chrome")) != -1 || (verOffset = nAgt.indexOf("MSIE")) != -1 || (verOffset = nAgt.indexOf("Safari")) != -1)
    {
        if (keyCodeEntered == 46)
        {
            keyCodeEntered = 0;
        }
    }
    else
    {
        if (keyCodeEntered == 46)
        {
            keyCodeEntered = elementRef.keyCode;
        }
    }
    if (keyCodeEntered == 8 || keyCodeEntered == 9 || keyCodeEntered == 37 || keyCodeEntered == 39 || keyCodeEntered == 46 || (keyCodeEntered >= 48) && (keyCodeEntered <= 57)) {
        return true;
    }
    else if (keyCodeEntered == 0) {
        if ((val.value) && (val.value.indexOf('.') >= 0))
            return false;
        else
            return true;
    }
    return false;
}