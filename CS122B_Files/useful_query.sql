

mysql -u mytestuser -p



SELECT email, password FROM customers WHERE email='kwhite@ics185.edu' OR password='book';



SELECT m.*, sim.starId, s.name, g.name, r.rating
FROM (SELECT * FROM movies WHERE genre = 'Drama') AS m
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



create table `movies_star_extend`
(
	`id` varchar(10) not null,
	`title` varchar(100) not null,
	`year` integer not null,
	`director` varchar(100) not null,
	`starId` varchar(10) not null,
	`star` varchar(100) not null
);



create table `movies_genre_extend`
(
	`id` varchar(10) not null,
	`title` varchar(100) not null,
	`year` integer not null,
	`director` varchar(100) not null,
	`genreId` integer not null,
	`genre` varchar(32) not null 
);



INSERT INTO movies_star_extend
SELECT m.*, sim.starId, s.name
FROM movies AS m 
LEFT JOIN stars_in_movies AS sim
ON sim.movieId = m.id
LEFT JOIN stars AS s
ON sim.starId = s.id



INSERT INTO movies_genre_extend
SELECT m.*, gim.genreId, g.name
FROM movies AS m 
LEFT JOIN genres_in_movies AS gim
ON gim.movieId = m.id
LEFT JOIN genres AS g
ON gim.genreId = g.id



SELECT m.*, mse.starId, mse.star, mge.genre, r.rating
FROM (SELECT * FROM movies WHERE title like 'B%') AS m
LEFT JOIN movies_star_extend AS mse
ON mse.id = m.id
LEFT JOIN movies_genre_extend AS mge
ON mge.id = m.id
LEFT JOIN ratings AS r
ON r.movieId = m.id;




create table `movies_extend`
(
	`id` varchar(10) not null,
	`title` varchar(100) not null,
	`year` integer not null,
	`director` varchar(100) not null,
	`starId` varchar(10) not null,
	`star` varchar(100) not null,
	`genreId` integer not null,
	`genre` varchar(32) not null,
	`rating` float(2)
);



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


SELECT me.*
FROM movies_extend AS me
WHERE me.genre = 'Drama';


SELECT me.id, me.title, me.year, me.director, me.star, me.genre, COALESCE(ROUND(me.rating, 1), 0)
FROM movies_extend AS me
WHERE me.title like 'A%';


SELECT me.id, me.title, me.year, me.director, me.star, me.genre, COALESCE(ROUND(me.rating, 1), 0)
FROM movies_extend AS me, (SELECT DISTINCT id FROM movies_genre_extend WHERE genre='Action') AS tmp0,
(SELECT DISTINCT id FROM movies_genre_extend WHERE genre='Comedy') AS tmp1,
(SELECT DISTINCT id FROM movies_genre_extend WHERE genre='Romance') AS tmp2
WHERE tmp0.id = tmp1.id 
AND tmp1.id = tmp2.id 
AND tmp2.id = me.id
ORDER BY me.title;


SELECT me.id, me.title, me.year, me.director, me.star, me.genre, COALESCE(ROUND(me.rating, 1), 0)
FROM movies_extend AS me
WHERE me.rating IS NULL;

SELECT * FROM (SELECT * FROM movies_star_extend AS mse WHERE mse.starId = "nm2933757") AS tmp1
LEFT JOIN (SELECT id, IFNULL(birthyear, 0) FROM stars) AS tmp2
ON tmp1.starId = tmp2.id;

DELETE FROM sales WHERE saleDate='2018-09-23';

ALTER TABLE movies_extend ENGINE = MyISAM;
ALTER TABLE movies ENGINE = MyISAM;
ALTER TABLE stars ENGINE = MyISAM;

ALTER TABLE movies_extend ENGINE = INNODB;
ALTER TABLE movies ENGINE = INNODB;
ALTER TABLE stars ENGINE = INNODB;

CREATE FULLTEXT INDEX titleIndex ON movies_extend(title);

CREATE FULLTEXT INDEX titleIndex ON movies(title);

CREATE FULLTEXT INDEX nameIndex ON stars(name);

SELECT * FROM movies WHERE MATCH(title) AGAINST ('+wonder* +woman* ' IN BOOLEAN MODE);
SELECT * FROM movies WHERE MATCH(title) AGAINST ('+wonder*' IN BOOLEAN MODE);

wget http://apache.mirrors.pair.com/tomcat/tomcat-8/v8.5.34/bin/apache-tomcat-8.5.34.tar.gz
tar xvf apache-tomcat-8.5.34.tar.gz -C /home/ubuntu/tomcat/ --strip-components=1

<Proxy "balancer://TomcatTest_balancer">
    BalancerMember "http://172.31.6.240:8080/TomcatTest/"
    BalancerMember "http://172.31.13.216:8080/TomcatTest/"
</Proxy>

http://52.53.159.54/TomcatTest/servlet/TomcatTest


Header add Set-Cookie "ROUTEID=.%{BALANCER_WORKER_ROUTE}e; path=/" env=BALANCER_ROUTE_CHANGED

<Proxy "balancer://Session_balancer">
    BalancerMember "http://172.31.6.240:8080/Session" route=1
    BalancerMember "http://172.31.13.216:8080/Session" route=2
ProxySet stickysession=ROUTEID
</Proxy> 

http://52.53.159.54/Session/servlet/ShowSession?myname=Michael




Header add Set-Cookie "ROUTEID=.%{BALANCER_WORKER_ROUTE}e; path=/" env=BALANCER_ROUTE_CHANGED

<Proxy "balancer://CS122B_balancer">
    BalancerMember "http://172.31.12.107.240:8080/CS122B" route=1
    BalancerMember "http://172.31.15.17:8080/CS122B" route=2
ProxySet stickysession=ROUTEID
</Proxy> 

ProxyPass /CS122B balancer://CS122B_balancer
ProxyPassReverse /CS122B balancer://CS122B_balancer

ProxySet "balancer://CS122B_balancer" timeout=15


GRANT ALL ON *.* TO 'repl'@'%';


Header add Set-Cookie "ROUTEID=.%{BALANCER_WORKER_ROUTE}e; path=/" env=BALANCER_ROUTE_CHANGED

<Proxy "balancer://CS122B_balancer">
    BalancerMember "http://54.183.118.166:8080/CS122B" route=1
    BalancerMember "http://54.153.56.43:8080/CS122B" route=2
ProxySet stickysession=ROUTEID
</Proxy> 