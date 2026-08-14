/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
//
function getlocationlist(countrycode,time){

    if(countrycode!="")
        {
    document.getElementById('agency').options.length = 1;
    document.getElementById('agency').options[0] = new Option("Loading.... ","");
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {
        var url = "UserProfileAjax?type=location&countrycode="+countrycode+"&ldate="+time;
        searchReq.open("GET",url,true);
        searchReq.onreadystatechange = setLocation;
        searchReq.send(null);
    }
		}
}
function setLocation(){
    if( searchReq.readyState == 4 ){
        if( searchReq.status == 200 ){
            var response = searchReq.responseText;
            var ports = response.split("~~");
            if( ports[0] == "valid" ){
                var portlist = ports[1].split("##");
                if( portlist[0] == "noports" ){
                    document.getElementById('agency').options.length = 1;
                    document.getElementById('agency').options[0] = new Option("No Location","");
                }else{
                    document.getElementById('agency').options.length = ports.length-1;
                    document.getElementById('agency').options[0] = new Option("Select Location","");
                    for( var i=1;i<ports.length-1;i++ ){
                        portname = ports[i].split("##");
                        document.getElementById('agency').options[i] = new Option(portname[0],portname[1]);
                    }
                }
            }
            else if( ports[0] == "invalid" ){

                alert(excep);
            }
        }
    }
}


function getlocationlist1(countrycode,time){

    if(countrycode!="")
        {
    document.getElementById('agencyname').options.length = 1;
    document.getElementById('agencyname').options[0] = new Option("Loading.... ","");
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {
        var url = "UserProfileAjax?type=location&countrycode="+countrycode+"&ldate="+time;
        searchReq.open("GET",url,true);
        searchReq.onreadystatechange = setLocation1;
        searchReq.send(null);
    }
		}
}
function setLocation1(){
    if( searchReq.readyState == 4 ){
        if( searchReq.status == 200 ){
            var response = searchReq.responseText;
            var ports = response.split("~~");
            if( ports[0] == "valid" ){
                var portlist = ports[1].split("##");
                if( portlist[0] == "noports" ){
                    document.getElementById('agencyname').options.length = 1;
                    document.getElementById('agencyname').options[0] = new Option("No Location","");
                }else{
                    document.getElementById('agencyname').options.length = ports.length-1;
                    document.getElementById('agencyname').options[0] = new Option("Select Location","");
                    for( var i=1;i<ports.length-1;i++ ){
                        portname = ports[i].split("##");
                        document.getElementById('agencyname').options[i] = new Option(portname[0],portname[1]);
                    }
                }
            }
            else if( ports[0] == "invalid" ){
                alert(excep);
            }
        }
    }
}


function getAgencyMail(agencycode,time){
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {
        var url = "UserProfileAjax?type=agencymail&agencycode="+agencycode+"&ldate="+time;
        searchReq.open("GET",url,true);
        searchReq.onreadystatechange = setAgencyMail;
        searchReq.send(null);
    }
}
function setAgencyMail(){
    if( searchReq.readyState == 4 ){
        if( searchReq.status == 200 ){
            var response = searchReq.responseText;
            var agencymail=trim(response);
	     	document.getElementById('agencymail').value=agencymail;
        }
    }
}

function validateEmailToCommon(value) {
    var regex=/^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    return (regex.test(value)) ? true : false;
}

function validEmailCheckToCommon(idname) {
	var email=document.getElementById(idname).value;
	email=email.replace(",,",",");
        email=email.replace(";,",",");
        email=email.replace(";,",",");
        email=email.replace(",;",",");
        email = email.replace(/;/gi,",");
        var result = email.replace(/\s/g, "").split(/,|;/);
    for(var i = 0;i < result.length;i++) {
        if(!validateEmailToCommon(result[i])) {
            return false;
        }
    }
	document.getElementById('useremail').value=result;
    return true;
}

function setCityDesc(id){
    document.getElementById('city_desc').value="";
    var city = document.getElementById(id).value;
    document.getElementById('city_desc').value = city;
}


function  EmailValidtion(field){
    var emailId=trim(document.getElementById(field).value);
	 if(document.getElementById('useremail').value.length ==0){
        alert(emailReq);
		setTimeout(function(){
               document.getElementById("useremail").focus();
        }, 10);

        return false;
    }
    else if(emailId.length>0){
        if(!validEmailCheckToCommon(trim(field))){
            alert(invalidMailAlert);
			setTimeout(function(){
                document.getElementById(field).focus();
        }, 10);
            return false;
        }
    }
	 return true;
}

var hiddenusername="";
function sendcontactmail()
{
    var messageText="";
    var name="";
    var compname="";
    var country="";
    var agency="";
     var userphone="";
    var usermobile="";
    var useremail=""
    var preftime="";
    var agencymail="";
    hiddenusername=document.getElementById('hiddenusername').value;

  if(hiddenusername!="null")
      {
     name=document.getElementById('username').innerHTML;
     compname=document.getElementById('compname').innerHTML;
     country=document.getElementById('country').innerHTML;
     agency=document.getElementById('agency').innerHTML;
     userphone=document.getElementById('userphone').innerHTML;
     usermobile=document.getElementById('strusermobile').innerHTML;
     useremail=document.getElementById('useremail').innerHTML;
     preftime=document.getElementById('preftime').value;
    agencymail=trim(document.getElementById('agencymail').value);
      }
       else
          {

     name=document.getElementById('username').value;
     compname=document.getElementById('compname').value;
     country=document.getElementById('country').value;
     agency=document.getElementById('agency').value;
     userphone=document.getElementById('userphone').value;
     usermobile=document.getElementById('strusermobile').value;
     useremail=document.getElementById('useremail').value;
     preftime=document.getElementById('preftime').value;
     agencymail=trim(document.getElementById('agencymail').value);

		var letters = /^[0-9]+$/;

    if(name=="")
    {
        alert(entName);
		setTimeout(function(){
              document.getElementById('username').focus();
        }, 10);
        return false;
    }
    if(compname=="")
    {
        alert(entCmpnyName);
		setTimeout(function(){
             document.getElementById('compname').focus();
        }, 10);
        return false;
    }

	 if(country=="")
    {
        alert(selectCountry);
		setTimeout(function(){
              document.getElementById('country').focus();
        }, 10);

        return false;
    }
    if(agency=="")
    {
        alert(selectAgency);
		setTimeout(function(){
             document.getElementById('agency').focus();
        }, 10);

        return false;
    }

	 if(userphone=="")
    {
        alert(entPhoneNum);
		 setTimeout(function(){
            document.getElementById('userphone').focus();
        }, 10);
        return false;
    }

	 if(useremail=="")
    {
        alert(entUserEmail);
		 setTimeout(function(){
            document.getElementById('useremail').focus();
        }, 10);

        return false;
    }



if(!userphone.match(letters))
{
alert(validPhoneNum);
 setTimeout(function(){
            document.getElementById('userphone').focus();
        }, 10);
return false;
}

if(usermobile!="")
{
if(!usermobile.match(letters))
{
alert(validMobNum);
setTimeout(function(){
            document.getElementById('strusermobile').focus();
        }, 10);
return false;
}
}

country= document.getElementById('country').options[document.getElementById('country').selectedIndex].text;

          }


	if(EmailValidtion('useremail'))
	{


    if(agencymail=="")
    {
        alert(agencyMailNotExists);
        return false;
    }



    if (searchReq.readyState == 4 || searchReq.readyState == 0) {
       
        messageText +="<div><table border=\"0\" width=\"100%\" ><tr><td style='color:#000000'><tr><td>Dear Team,<br><br></td></tr>";
        messageText +="<tr><td colspan=\"2\">"+ "The following details are submitted through e-Commerce application."+"</td></tr>";
        messageText +="<tr><td></td></tr>";
        messageText +="<tr><td>Name</td><td>"+name+"</td></tr>";
        messageText +="<tr><td>Company Name</td><td>"+ compname+"</td></tr>";
        messageText +="<tr><td>Country</td><td>"+country +"</td></tr>";
        messageText +="<tr><td>User Phone</td><td>"+ userphone +"</td></tr>";
        messageText +="<tr><td>User Mobile</td><td>"+usermobile+"</td></tr>";
        messageText +="<tr><td>User Email</td><td>"+useremail +"</td></tr>";
        messageText +="<tr><td>Preferred Time</td><td>"+preftime +"</td></tr>";
        messageText +="</table></div><br><div><table border=\"0\" width=\"100%\" ><tr><td style='color:#000000'><tr><td style='color:#000000'>Regards,</td></tr><tr><td style='color:#000000'>eService Team</td></tr></table></div></body></html>";
        messageText=encodeURIComponent(messageText);
        var urlStr="ecomAjax?type=contactusmail&text = "+messageText+"&id="+agencymail;
        searchRequest.open("POST", "ecomAjax", true);
        searchRequest.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        var params = "type=contactusmail&text=" +messageText+"&id="+agencymail+"&date"+new Date();
        searchRequest.onreadystatechange = sentcontactMail;
        searchRequest.send(params);
    }

	}
}


function getCitylist(time){
    var countrycode = document.getElementById('country').value;
	if(countrycode!=null)
	{
    document.getElementById('city').options[0] = new Option("Loading.... ","");
    if (searchReq.readyState == 4 || searchReq.readyState == 0)
    {

        var url = "UserProfileAjax?type=city&countrycode="+countrycode+"&ldate="+time;
        searchReq.open("GET",url,true);
        searchReq.onreadystatechange = setCityList;
        searchReq.send(null);
    }
	}

}
function setCityList(){
    if( searchReq.readyState == 4 ){
        if( searchReq.status == 200 ){
            var response = searchReq.responseText;
            document.getElementById('city').options[0] = new Option("Loading.... ","");
            var ports = response.split("~~");
            if( ports[0] == "valid" ){
                var portlist = ports[1].split("##");
                if( portlist[0] == "nocity" ){
                    document.getElementById('city').options.length = 1;
                    document.getElementById('city').options[0] = new Option("No City","");
                }else{
                        document.getElementById('city').options.length = ports.length-1;
                        document.getElementById('city').options[0] = new Option("Select City","");
                        for( var i=1;i<ports.length-1;i++ ){
                            portname = ports[i].split("##");
                            document.getElementById('city').options[i] = new Option(portname[0],portname[1]);

                        }

                }

            }else if( ports[0] == "invalid" ){

                alert(excep);
            }
        }
    }

}








