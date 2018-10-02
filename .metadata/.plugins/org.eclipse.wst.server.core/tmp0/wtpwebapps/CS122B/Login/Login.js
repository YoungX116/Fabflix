function handleLoginResult(resultDataString) {
	resultDataJson = JSON.parse(resultDataString);
	
	console.log("handle login response");
	console.log(resultDataJson);
	console.log(resultDataJson["status"]);
	
	// if login success, redirect to index.html page
	if (resultDataJson["status"] == "success") {
		window.location.assign("/CS122B/Home/Home.html");
	} 
	else if (resultDataJson["status"] == "error_user"){
		console.log("show error message");
		console.log(resultDataJson["message"]); 
		$(".error_user").text(resultDataJson["message"]);
	}
	else {
		console.log("show error message");
		console.log(resultDataJson["message"]);
		$(".error_pwd").text(resultDataJson["message"]);
	}
}

function submitLoginForm(formSubmitEvent) {
	console.log("submit login form");
	
	// disable the default action of submitting the form
	// which will cause the page to refresh
	formSubmitEvent.preventDefault();
	
	$.post(
	"/CS122B/Login",
	// serialize the login form to the data sent by POST request
	$("form").serialize(),
	(resultDataString) => handleLoginResult(resultDataString));
}

function validateForm() {
    var x = document.forms["login_form"]["email"].value;
    var y = document.forms["login_form"]["password"].value;
    var email_invalid = false;
    var pwd_invalid = false;
    if (x == "" || !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(x)) {
    	$(".error_user").text("Please enter a valid email!");
    	email_invalid = true;
    } 
    if (y == "") {
    	$(".error_pwd").text("Please enter a valid password!");
    	pwd_invalid = true;
    } 
    if (email_invalid || pwd_invalid) return false;
    else return true;
}


//bind the submit action of the form to a handler function
$(function(){
	$("form").submit(
		(formSubmitEvent) => submitLoginForm(formSubmitEvent));
	
	$("#email").focus(function(){
		$(".error_user").html("<br>");
	});
	
	$("#pwd").focus(function(){
		$(".error_pwd").html("<br>");
	});
});
