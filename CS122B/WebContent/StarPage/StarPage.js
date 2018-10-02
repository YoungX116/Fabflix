function handleStarResult(resultData) {
	console.log("handleStarResult");
	
	let starCardBodyElement = document.getElementById("starinfobody");
	
	let	result = '<div class="row">' + 
			'<div class="col-sm"></div>' +
			'<div class="col-sm-9 jumbotron py-3 rounded-0">' +	
			'<div class="media mb-2">' +
			'<div class="align-self-center mr-3" id="image"></div>' + 
			'<div class="media-body">' + 
			'<h4>' + resultData[0]["star_name"] + '</h4>';
	
	if (resultData[0]["birth_year"] != 0) {
		result += '<p class="mt-3"><strong>Year of Birth:</strong> ' +  resultData[0]["birth_year"] + '</p>';
	}
	
	result += '<p class="mb-0"><strong>Movies:</strong> ';
	let length = resultData.length;
	for (let i = 0; i < length - 1; i++) {
		result += '<a href="../MoviePage/MoviePage.html?movie_id=' + resultData[i]["movie_id"] + '">' + 
		resultData[i]["movie_title"] + ', ' + '</a>';
	}
	result += '<a href="../MoviePage/MoviePage.html?movie_id=' + resultData[length - 1]["movie_id"] + '">' + 
	resultData[length - 1]["movie_title"] + '</a>';
	
	result += '</div></div></div><div class="col-sm"></div></div>';
	
	starCardBodyElement.innerHTML = result;
}

var image = "";
const API_KEY = 'f749e73212f30dc7424e9641180a9324';

function getStarInfo(star_name) {
	let imageUrl = "http://image.tmdb.org/t/p/w780";
//	$.ajax({
//		dataType: "json",
//		method: "GET",
//		data: "api_key=" + API_KEY,
//		url: "https://api.themoviedb.org/3/configuration",
//		success: function(json) {
//			imageUrl += json.images.base_url + "w780";
//		}
//	});
	
	$.ajax({
		dataType: "json",
		method: "GET",
		data: "api_key=" + API_KEY + "&query=" + star_name,
		url: "https://api.themoviedb.org/3/search/person",
		success: function(json) {
			if (json.total_results >= 1 && json.results[0].name.toLowerCase() == star_name.toLowerCase() &&
					json.results[0].profile_path != null) {			
				imageUrl += json.results[0].profile_path;
				image += '<img src="' + imageUrl + '" class="img-fluid" alt="Responsive image" style="width:80px"/>';
				
				document.getElementById("image").innerHTML = image;	
				
			}
			else {
				document.getElementById("image").innerHTML = 
					'<img src="./none.jpg" class="img-fluid" alt="Responsive image" style="width:80px"/>';
			}
			console.log(image);
		}
	});
}


$(function(){
	$.ajax({
		dataType: "json",
		method: "GET",
		data: "star_id=" + window.location.search.split("=")[1],
		url: "/CS122B/StarPage",
		success: function(resultData) {
			handleStarResult(resultData);
			getStarInfo(resultData[0]["star_name"]);
		}
	});
	console.log("success submitted");
	console.log("");
});
