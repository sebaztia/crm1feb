CREATE TABLE IF NOT EXISTS `comments` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `message` varchar(2000) NOT NULL,
    `author` varchar(40) NOT NULL,
    `created_at` datetime DEFAULT NULL,
    `updated_at` datetime DEFAULT NULL,
    `client_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK5lpjg9gvqj3g9ukhyecxdf6ss` (`client_id`),
    CONSTRAINT `FK5lpjg9gvqj3g9ukhyecxdf6ss` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;