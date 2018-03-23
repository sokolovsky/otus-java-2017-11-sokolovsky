CREATE TABLE IF NOT EXISTS user (
  id       BIGINT(20) NOT NULL AUTO_INCREMENT,
  login    VARCHAR(255),
  password VARCHAR(50),
  age INTEGER(3),
  address_id BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (id)
) CHARACTER SET utf8 COLLATE utf8_general_ci;
