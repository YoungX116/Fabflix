INSERT INTO movies_star_extend
SELECT m.*, sim.starId, s.name
FROM movies AS m 
LEFT JOIN stars_in_movies AS sim
ON sim.movieId = m.id
LEFT JOIN stars AS s
ON sim.starId = s.id;

INSERT INTO movies_genre_extend
SELECT m.*, gim.genreId, g.name
FROM movies AS m 
LEFT JOIN genres_in_movies AS gim
ON gim.movieId = m.id
LEFT JOIN genres AS g
ON gim.genreId = g.id;

INSERT INTO movies_extend
SELECT m.*, sim.starId, s.name, gim.genreId, g.name, r.rating
FROM movies AS m
LEFT JOIN stars_in_movies AS sim
ON sim.movieId = m.id
LEFT JOIN stars AS s
ON sim.starId = s.id
LEFT JOIN genres_in_movies AS gim
ON gim.movieId = m.id
LEFT JOIN genres AS g
ON gim.genreId = g.id
LEFT JOIN ratings AS r
ON r.movieId = m.id;