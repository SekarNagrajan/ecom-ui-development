function trim( value ) {
    return LTrim(RTrim(value));
}
function LTrim( value ) {
    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");
}
// Removes ending whitespaces
function RTrim( value ) {
    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");
}
var companyRequest=getXmlHttpRequestObject();
var searchReq=getXmlHttpRequestObject();
var searchRequest=getXmlHttpRequestObject();

function getXmlHttpRequestObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else if(window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        alert(incompatibleforAjax);
    }
}

function getCustomer(){

var customercode=trim(document.getElementById("customercode").value);
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {
        var url = "PortListAjax?type=checkcustomer&q="+encodeURIComponent(customercode);
        searchReq.open("GET",url,true);
        searchReq.onreadystatechange = setCustomer;
        searchReq.send(null);
    }
}
function setCustomer(){
    if( searchReq.readyState == 4 ){
        if( searchReq.status == 200 ){
            var response = searchReq.responseText;
			if(response=="false")
			{
				alert(invalidCust);
				return false;
			}
                        else
                        {
                           alert("Customer Selected");
			   location.reload(true);
			    // location.href="Accesslogin.do?type=port";
                           return false;
                        }
    }
	}
}

