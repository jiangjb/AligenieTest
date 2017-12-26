$("document").ready(function(){
	
	$("#Button_Auth").click(getAccessToken);
});

function getAccessToken(){
	var data={};
	var grant_type=request("grant_type");
	if(grant_type.length==0)grant_type="authorization_code";
	data.grant_type=grant_type;
	data.client_id="c1ebe466-1cdc-4bd3-ab69-77c3561b9dee";
	data.client_secret="d8346ea2-6017-43ed-ad68-19c0f971738b";
	data.code=request("code");
	data.redirect_uri="http%3A%2f%2flocalhost%3A8080/AliGenie/pages/callback.html";
    $.ajax({
    	type: "POST",
        url: "../token" ,
        data: data,
        dataType: 'json',
        async: true,
        success: function (response, status, xhr) {
        	alert(response.access_token);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest);
        }
    });
}




