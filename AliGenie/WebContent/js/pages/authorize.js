$("document").ready(function(){
	$("#Button_Auth").click(function(){
		$("#client_id").val(request("client_id"));
		$("#response_type").val(request("response_type"));
		$("#redirect_uri").val(request("redirect_uri"));
		$("#Form1")[0].submit();
	});
});





