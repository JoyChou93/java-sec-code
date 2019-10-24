USE `java_sec_code`;
CREATE TABLE IF NOT EXISTS `users`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `username` VARCHAR(255) NOT NULL,
   `password` VARCHAR(255) NOT NULL,
   PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `users` VALUES (1, 'admin', 'admin123');
INSERT INTO `users` VALUES (2, 'joychou', 'joychou123');
