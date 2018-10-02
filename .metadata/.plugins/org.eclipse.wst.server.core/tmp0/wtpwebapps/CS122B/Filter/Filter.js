
function generateGenreCheckbox() {
	let genreString = "<tr>";
	for (let i = 0; i < genreList.length; i++) {
		genreString += "<td><label><input class='checkbox-inline' type='checkbox' name='genreSelection' value='" + 
			genreList[i] + "'> " + genreList[i] + "</label></td>";
		if ((i + 1) % 5 == 0) {
			genreString += "<tr></tr>";
		}
	}
	genreString += "</tr>";
	document.getElementById("genre-checkbox").innerHTML = genreString;
}

function submitBrowseForm(formSubmitEvent) {
	console.log("submit search form");
	
	formSubmitEvent.preventDefault();
	let urlQuery = $("form").serialize();
	console.log(urlQuery);
	
	document.getElementById("filter_form").reset();
	
	window.location.href = "../Movielist/Movielist.html?" + urlQuery;
}

function generateSelectElement() {
	let start = 2000;
	let end = new Date().getFullYear();
	let yearoptions = "<option selected='selected'>-</option>";
	for(let year = end; year >= start; year--){
		yearoptions += "<option>"+ year +"</option>";
	}
	document.getElementById("yearL").innerHTML = yearoptions;
	document.getElementById("yearR").innerHTML = yearoptions;
	
	let ratingoptionsL = "<option selected='selected'>-</option><option>none</option>";
	let ratingoptionsR = "<option selected='selected'>-</option>";
	for(let rating = 1.0; rating <= 10.0; rating += 0.1){
		ratingoptionsL += "<option>"+ rating.toFixed(1) +"</option>";
		ratingoptionsR += "<option>"+ rating.toFixed(1) +"</option>";
	}
	document.getElementById("ratingL").innerHTML = ratingoptionsL;
	document.getElementById("ratingR").innerHTML = ratingoptionsR;
}

$(function(){
	$(".filter").on("click", false);
	
	generateSelectElement();
	generateGenreCheckbox();
	
	$("form").submit(function(event) {
		submitBrowseForm(event);
	});
	
});