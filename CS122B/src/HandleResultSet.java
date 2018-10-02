import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HandleResultSet {
	public static void parseRS(ResultSet rs, JsonArray movies) {
		Set<String> movieSet = new HashSet<>();
        Map<String, List<String>> starMap = new HashMap<>();
        Map<String, List<String>> genreMap = new HashMap<>();
        
        int number_of_rs = getRowCount(rs);
        System.out.println("number: " + number_of_rs);
        int count = 0;
        String last_id = "";
        String last_title = "";
        int last_year = 0;
        String last_director = "";
        float last_rating = 0;
        
        try {
        	while (rs.next()) {
            	count++;
            	//System.out.println(count);
            	
            	String movie_id = rs.getString(1);
                String movie_star = rs.getString(5) + "+" + rs.getString(8);
                String movie_genre = rs.getString(6);              
                
                if (!movieSet.contains(movie_id)) {
                	movieSet.add(movie_id);
                    starMap.put(movie_id, new ArrayList<>());
                    genreMap.put(movie_id, new ArrayList<>());
                    
                    if (last_id.length() != 0) {
                    	addSingleMovie(last_id, last_title, last_year, last_director, last_rating, starMap, genreMap, movies);
                    }
                    
                    last_id = movie_id;
                    last_title = rs.getString(2);
                    last_year = rs.getInt(3);
                    last_director = rs.getString(4);
                    last_rating = rs.getFloat(7);
                }
                
                if (!starMap.get(movie_id).contains(movie_star )) {
                    starMap.get(movie_id).add(movie_star);
                }
                if (!genreMap.get(movie_id).contains(movie_genre)) {
                    genreMap.get(movie_id).add(movie_genre);
                }   
                
                if (count == number_of_rs) {
                	addSingleMovie(last_id, last_title, last_year, last_director, last_rating, starMap, genreMap, movies);
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        
	}
	
	private static int getRowCount(ResultSet resultSet) {
	    if (resultSet == null) {
	        return 0;
	    }
	    try {
	        resultSet.last();
	        return resultSet.getRow();
	    } catch (SQLException exp) {
	        exp.printStackTrace();
	    } finally {
	        try {
	            resultSet.beforeFirst();
	        } catch (SQLException exp) {
	            exp.printStackTrace();
	        }
	    }
	    return 0;
	}
	
	private static void addSingleMovie(String id, String title, int year, String director, float rating, Map<String, List<String>> star, Map<String, List<String>> genre, JsonArray movies) {
		JsonObject single_movie = new JsonObject();
        single_movie.addProperty("movie_id", id);
        single_movie.addProperty("movie_title", title);
        single_movie.addProperty("movie_year", year);
        single_movie.addProperty("movie_director", director);
        single_movie.addProperty("movie_rating", rating);
        
        JsonArray stars = new JsonArray();
        Iterator<String> star_it = star.get(id).iterator();
        while (star_it.hasNext()) {
            stars.add(star_it.next());                          
        }
        single_movie.add("movie_stars", stars);
        
        JsonArray genres = new JsonArray();
        Iterator<String> genre_it = genre.get(id).iterator();
        while (genre_it.hasNext()) {
            genres.add(genre_it.next());
        }
        single_movie.add("movie_genres", genres);
        
        movies.add(single_movie);
	}
}
