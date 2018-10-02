
function handleResult(resultData) {
	console.log("handleCartResult: populating cartinfo table from resultData");
	
//	if (resultData != "" && resultData != null) {
//		console.log("not null");	
//	} 
	
	var cartTableBodyElement = document.getElementById("cartinfobody");
	var carttable = "<h5 class='my-3'>Shopping Cart</h5>" + 
				"<div class='table-responsive'><table class='table table-hover table-striped'><thead class='thead-dark'><tr class='d-flex'>" +
				"<th class='col-3 col-sm-3 col-md-4'>Movie</th><th class='col-3 col-sm-3 col-md-4'>Quantity</th><th class='col-6 col-sm-6 col-md-4'>Add, Subtract or Delete</th>" + 
				"</tr></thead><tbody id='shopping_cart'>";
	if (resultData.length > 0) { 
				
		for (var i = 0; i < resultData.length; i++) {
			carttable += "<tr class='d-flex mb-0 pb-0'>";
			carttable += "<th class='col-3 col-sm-3 col-md-4'>" + resultData[i]["movie"] + "</th>";
			carttable += "<th class='col-3 col-sm-3 col-md-4'>" + resultData[i]["quantity"] + "</th>";
			carttable += "<th class='col-6 col-sm-6 col-md-4'><div class='btn-group'>";
			carttable += "<button class='btn mx-1' style='background-color:transparent;' onclick='operation(this)' id='" +
					resultData[i]["movie"] + "+" + "add" + "'>" +
					"<i class='fa fa-plus-square'></i></button>";
			
			carttable += "<button class='btn mx-1' style='background-color:transparent;' onclick='operation(this)' id='" +
					resultData[i]["movie"] + "+" + "substract" + "'>" +
					"<i class='fa fa-minus-square'></i></button>";
			
			carttable += "<button class='btn mx-1' style='background-color:transparent;' onclick='operation(this)' id='" +
					resultData[i]["movie"] + "+" + "delete" + "'>" +
					"<i class='fa fa-trash'></i></button>";
			
			carttable += "</div></th></tr>";
		}
		carttable += "</tbody></table></div>";
		carttable += "<button class='btn checkout rounded-0' onclick=\"window.location.href='../Checkout/Checkout.html'\">Check Out</button>";
	}
	else {
		carttable += "<tr class='d-flex mb-0 pb-0'><th class='col-3 col-sm-3 col-md-4 empty_msg'>Empty!</th></tr>";	
	}
	carttable += "</tbody></table></div>";
	cartTableBodyElement.innerHTML = carttable;
	
}


function operation(item) {
	jQuery.ajax({
		dataType: "json",
		method: "GET",
		data: "item=" + item.id.split("+")[0] + "&process=" + item.id.split("+")[1], 
		url: "/CS122B/CartProcessing",
		success: (resultData) => handleResult(resultData)
	}); 
	//window.location.replace("../Cart/Cart.html");
}


$(function(){
	$(".cart").on("click", false);
	
	jQuery.ajax({
		  dataType: "json",
		  method: "GET",
		  url: "/CS122B/ShowCart",
		  success: (resultData) => handleResult(resultData)
	});
	console.log("success submitted");
	console.log("");
});