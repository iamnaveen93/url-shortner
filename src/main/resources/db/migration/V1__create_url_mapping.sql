CREATE TABLE url_mapping
(
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    long_url    VARCHAR(2048) NOT NULL,
    short_code  VARCHAR(10)   NOT NULL,
    expiry_time DATETIME      NOT NULL,
    created_at  DATETIME      NOT NULL,
    updated_at  DATETIME      NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_url_mapping_short_code UNIQUE (short_code)
);