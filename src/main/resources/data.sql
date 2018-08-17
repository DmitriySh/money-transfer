-- default data
INSERT INTO account (acc_number, amount)
 VALUES (1001, 1000),
        (1002, 2000),
        (1003, 3000);

INSERT INTO user (username, password)
 VALUES ('user', 'password'),
        ('admin', 'password');

INSERT INTO user_role (role_id, role)
 VALUES (1, 'ROLE_USER'),
        (2, 'ROLE_USER'),
        (2, 'ROLE_ADMIN');
