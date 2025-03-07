use oshiel_db

# テーブルのダンプ article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `article_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `url` varchar(100) NOT NULL,
  `description` varchar(300) NOT NULL,
  `urlToImage` varchar(100) NOT NULL,
  `publishedAt` varchar(20) NOT NULL,
  `createdate` datetime NOT NULL,
  `updatedate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# テーブルのダンプ favorite_article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `favorite_article`;

CREATE TABLE `favorite_article` (
  `favorite_article_id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `oshiel_id` int(11) NOT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `evaluation` varchar(2) DEFAULT NULL,
  `createdate` datetime NOT NULL,
  `updatedate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`favorite_article_id`,`article_id`,`oshiel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# テーブルのダンプ member
# ------------------------------------------------------------

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
  `oshiel_id` int(11) NOT NULL AUTO_INCREMENT,
  `slack_id` int(11) DEFAULT NULL,
  `slack_token` varchar(100) NOT NULL,
  `slack_expire` datetime NOT NULL,
  `notification_flag` tinyint(4) DEFAULT NULL,
  `notification_time` datetime DEFAULT NULL,
  `createdate` datetime NOT NULL,
  `updatedate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`oshiel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# テーブルのダンプ notification_article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `notification_article`;

CREATE TABLE `notification_article` (
  `notification_article_id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `oshiel_id` int(11) NOT NULL,
  `createdate` datetime NOT NULL,
  `updatedate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_article_id`,`article_id`,`oshiel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# テーブルのダンプ topic
# ------------------------------------------------------------

DROP TABLE IF EXISTS `topic`;

CREATE TABLE `topic` (
  `oshiel_id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL,
  `topic_detail` varchar(100) NOT NULL,
  `createdate` datetime NOT NULL,
  `updatedate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`oshiel_id`,`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;c
