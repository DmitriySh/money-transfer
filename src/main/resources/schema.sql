CREATE TABLE IF NOT EXISTS account
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    acc_number   BIGINT                   NOT NULL,
    amount       DECIMAL(20, 2)                    DEFAULT 0,
    created_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    updated_time TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS account_audit
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_number  BIGINT,
    to_number    BIGINT,
    amount       DECIMAL(20, 2)           NOT NULL,
    description  VARCHAR(100)             NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    FOREIGN KEY (from_number) REFERENCES account (acc_number),
    FOREIGN KEY (to_number) REFERENCES account (acc_number)
);
