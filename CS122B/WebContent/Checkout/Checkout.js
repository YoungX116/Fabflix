
function handlePaymentResult(resultData) {
	resultDataJson = JSON.parse(resultData);
	
	console.log("handle checkout response");
	//console.log(resultData);

	// if checkout success, then confirm
	if (resultDataJson["status"] == "success"){
		var msg = confirm("Confirm your payment!");
		if (msg == true) {
			alert("success!")
			$.ajax({
				  dataType: "json",
				  method: "GET",
				  data: "lastname=" + resultDataJson["lastname"] + "&firstname=" + resultDataJson["firstname"],
				  url: "/CS122B/Confirmation",
				  success: (resultData) => handleConfirmationResult(resultData)
			});
		} 
	} else {
		console.log("show error message");
		console.log(resultDataJson["message"]);
		alert("Wrong Information, please try again!");
	}
}

function handleConfirmationResult(resultData) {
	if (resultData["status"] == "success") {
		window.location.assign('../Confirmation/Confirmation.html');
	} else {
		console.log("show error message");
		console.log(resultDataString["message"]);
		alert("Server error, please try again!");
	}
}

function submitForm(formSubmitEvent) {
	console.log("submit form");
	
	// important: disable the default action of submitting the form
	//   which will cause the page to refresh
	//   see jQuery reference for details: https://api.jquery.com/submit/
	formSubmitEvent.preventDefault();
		
	$.post(
	"/CS122B/Checkout", 
	// serialize the login form to the data sent by POST request
	$("#checkout_form").serialize(),
	(resultData) => handlePaymentResult(resultData));
}

$("#checkout_form").submit((event) => submitForm(event));