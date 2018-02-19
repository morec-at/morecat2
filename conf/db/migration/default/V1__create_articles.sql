CREATE TABLE articles (
  article_id             VARCHAR(36)  NOT NULL,
  title                  VARCHAR(255) NOT NULL,
  content                TEXT         NOT NULL,
  created_date_time      TIMESTAMP    NOT NULL,
  last_updated_date_time TIMESTAMP    NOT NULL,
  PRIMARY KEY (article_id)
)
  ENGINE = InnoDB;