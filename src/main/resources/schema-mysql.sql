# noinspection SqlNoDataSourceInspectionForFile

CREATE DATABASE IF NOT EXISTS `movies`;

USE `movies`;

DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `movie`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `movie` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `budget` double NOT NULL,
  `cast` varchar(255) DEFAULT NULL,
  `director` varchar(255) DEFAULT NULL,
  `duration` int NOT NULL,
  `genre` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `plot` text DEFAULT NULL,
  `poster_url` varchar(255) DEFAULT NULL,
  `rating` int NOT NULL,
  `year` int NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` text DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rating` int DEFAULT 0,
  `movie_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `movie_review_fk` (`movie_id`),
  CONSTRAINT `movie_review_fk` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`)
);

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
