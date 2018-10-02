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