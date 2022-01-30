CREATE TABLE vocabulary
(
    id               BIGSERIAL NOT NULL,
    word             TEXT      NOT NULL,
    transcription_us TEXT      NOT NULL,
    transcription_uk TEXT      NOT NULL,
    sound_us_path    TEXT      NOT NULL,
    sound_uk_path    TEXT      NOT NULL,
    translates       TEXT      NOT NULL,
    PRIMARY KEY (id)
)
GO