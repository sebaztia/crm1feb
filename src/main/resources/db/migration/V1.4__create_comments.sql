CREATE TABLE IF NOT EXISTS `comments` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `message` varchar(2000) NOT NULL,
    `author` varchar(20) NOT NULL,
    `created_at` datetime DEFAULT NULL,
    `updated_at` datetime DEFAULT NULL,
    `client_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK5lpjg9gvqj3g9ukhyecxdf6ss` (`client_id`),
    CONSTRAINT `FK5lpjg9gvqj3g9ukhyecxdf6ss` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    CREATE TABLE IF NOT EXISTS `client_status` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `status` varchar(30) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `client`
ADD COLUMN `status` VARCHAR(45) NULL AFTER `updated_at`,
ADD COLUMN `cleared` TINYINT NULL AFTER `status`;

INSERT INTO `client_status` VALUES (21,'To Call');
INSERT INTO `client_status` VALUES (22,'Will instructions taken');
INSERT INTO `client_status` VALUES (23,'Will instructions signed');
INSERT INTO `client_status` VALUES (24,'Will instructions written');
INSERT INTO `client_status` VALUES (25,'Will Signed');
INSERT INTO `client_status` VALUES (26,'Will Checked');
INSERT INTO `client_status` VALUES (27,'Will Stored');
