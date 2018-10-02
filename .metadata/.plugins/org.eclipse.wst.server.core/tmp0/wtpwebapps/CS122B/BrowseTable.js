
const genreList = [
	"Action", "Adult", "Adventure", "Animation", "Biography", 
	"Comedy", "Crime", "Documentary", "Drama", "Family", 
	"Fantasy", "History", "Horror", "Music", "Musical", 
	"Mystery", "Reality-TV", "Romance", "Sci-Fi", "Sport",
	"Thriller", "War", "Western"
	];

function generateGenreTable() {
	let genreString = "";
	for (let i = 0; i < genreList.length; i++) {
		genreString += "<a class='dropdown-item genre_browse' href='../Movielist/Movielist.html?capital=&genre=" + 
			genreList[i] + "'>" + genreList[i] + "</a>";
	}
	document.getElementById("genre-table").innerHTML = genreString;
}


const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');

function generateTitleTable() {
	let titleString = "<ul class='list-inline text-left'>";	
	for (let i = 0; i < 10; i++) {
		titleString += "<li class='list-inline-item'><a class='dropdown-item title_browse' " + 
			"href='../Movielist/Movielist.html?capital=" + i + "&genre='>" + i + "</a></li>";
		if (i == 4) {
			titleString += "</ul><ul class='list-inline text-left'>";
		}
	}	
	titleString += "</ul><hr class='my-1'><ul class='list-inline text-left'>";
	
	for (let i = 0; i < 26; i++) {
		titleString += "<li class='list-inline-item'><a class='dropdown-item title_browse' " +
			"href='../Movielist/Movielist.html?capital=" + letters[i] + "&genre='>" + letters[i] + "</a></li>";
		if (i == 4 || i == 9 || i == 14 || i == 19 || i == 24) {
			titleString += "</ul><ul class='list-inline text-left'>"
		}
	}
	titleString += "</ul>";
	document.getElementById("title-table").innerHTML = titleString;
}

$(function(){
	generateGenreTable();
	generateTitleTable();
});