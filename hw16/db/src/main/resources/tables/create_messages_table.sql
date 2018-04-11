CREATE TABLE IF NOT EXISTS `messages` (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  author_id BIGINT(20) NOT NULL,
  `time` DATETIME,
  `text` TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (author_id) REFERENCES `user`(id)
) CHARACTER SET utf8 COLLATE utf8_general_ci;
