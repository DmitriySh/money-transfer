DROP TABLE IF EXISTS account;
CREATE TABLE IF NOT EXISTS account
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  acc_number BIGINT NOT NULL,
  amount DECIMAL(20, 2) DEFAULT 0,
  last_update TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp
);

DROP TABLE IF EXISTS log;
CREATE TABLE IF NOT EXISTS log
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  from_number BIGINT,
  to_number BIGINT,
  amount DECIMAL(20, 2) NOT NULL,
  description VARCHAR(100) NOT NULL,
  create_date TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
  FOREIGN KEY (from_number) REFERENCES account(acc_number),
  FOREIGN KEY (to_number) REFERENCES account(acc_number)
);

DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username varchar(100) NOT NULL,
  password varchar(100) NOT NULL,
  create_date TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
);

DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_id BIGINT NOT NULL,
  role VARCHAR(100),
  UNIQUE INDEX (role_id, role),
  FOREIGN KEY (role_id) REFERENCES user(id) ON DELETE CASCADE
);
