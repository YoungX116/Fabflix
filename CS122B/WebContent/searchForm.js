var searchCache = {};

/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	// TODO: if you want to check past query results first, you can do it here
	if (query in searchCache) { 
		handleLookupAjaxSuccess(searchCache[query], query, doneCallback)
		console.log("query exists in cache")
		console.log("search result from cache")
	}	
	else {
		console.log("new query")
		console.log("search result from servlet")
	// sending the HTTP GET request to the Java Servlet with the query data
		$.ajax({
			"method": "GET",
			// generate the request url from the query.
			// escape the query string to avoid errors caused by special characters 
			"url": "/CS122B/SearchSuggestion?query=" + escape(query),
			"success": function(data) {
				// pass the data, query, and doneCallback function into the success handler
				handleLookupAjaxSuccess(data, query, doneCallback) 
			},
			"error": function(errorData) {
				console.log("lookup ajax error")
				console.log(errorData)
			}	
		})
	}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	
	// TODO: if you want to cache the result into a global variable you can do it here
	searchCache[query] = data
	
	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	// the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"])
	var category = suggestion["data"]["category"];
	var url = "/CS122B/" + category + "Page/" +  
	          category + "Page.html?id=" +
	          suggestion["data"]["ID"];
	console.log(url)
	window.location.href = url; 
}


/*
 * This statement binds the autocomplete library with the input box element and 
 * sets necessary parameters of the library.
 * 
 */
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    // Minimum number of characters required to trigger autosuggest
    minChars: 3,
    
    showNoSuggestionNotice : true,
    noSuggestionNotice: "Sorry, no results!",
    // there are some other parameters that you might want to use to satisfy all the requirements
});


function handleSearchResult(resultData) {
	console.log("handle search response");
	console.log(resultData);

	if (resultData != "" && resultData != null) {
		var title = document.getElementById("autocomplete").value;
		var url = "/CS122B/Movielist/Movielist.html?query=" + title;
		window.location.href = url;		
	} else {
		console.log("show error message");
	}
}


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
	
	$.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "/CS122B/NormalSearch?query=" + escape(query) + "&sorting=rating&order=DESC",
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleSearchResult(data)
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})	
}


function EnterPress(e) {   
    var e = e || window.event;   
    if (e.keyCode == 13) {   
	    console.log("press the enter key");	  
		console.log($('#autocomplete').val());
		e.preventDefault();		
		handleNormalSearch($('#autocomplete').val());   
		
    }   
} 

function EnterClick(e) {
	e.preventDefault();		
	handleNormalSearch($('#autocomplete').val()); 
}
