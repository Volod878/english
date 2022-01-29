CREATE TABLE translate
(
    id            BIGSERIAL NOT NULL,
    translate     TEXT      NOT NULL,
    vocabulary_id INT       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (vocabulary_id) REFERENCES vocabulary (id) ON DELETE CASCADE
)
GO