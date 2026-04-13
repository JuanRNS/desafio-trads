CREATE TABLE user_acess (
                            id BIGINT NOT NULL PRIMARY KEY,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            role VARCHAR(255) NOT NULL CHECK (role IN ('USER', 'ADMIN', 'MANAGER'))
);

CREATE TABLE usuarios (
                          id BIGINT NOT NULL PRIMARY KEY,
                          email VARCHAR(255) NOT NULL,
                          nome VARCHAR(255) NOT NULL,
                          papel VARCHAR(255) NOT NULL
);

CREATE TABLE pipelines (
                           id BIGINT NOT NULL PRIMARY KEY,
                           descricao VARCHAR(255),
                           nome VARCHAR(255) NOT NULL,
                           sort INTEGER NOT NULL
);

CREATE TABLE stages (
                        id BIGINT NOT NULL PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL,
                        semantics VARCHAR(10) NOT NULL,
                        sort INTEGER NOT NULL,
                        status_id VARCHAR(255) NOT NULL UNIQUE,
                        pipeline_id BIGINT NOT NULL,
                        FOREIGN KEY (pipeline_id) REFERENCES pipelines (id)
);

CREATE TABLE negocios (
                          id BIGINT NOT NULL PRIMARY KEY,
                          closed BOOLEAN NOT NULL,
                          closedate DATE,
                          comments VARCHAR(255),
                          currency_id VARCHAR(10) NOT NULL,
                          custom_fields TEXT,
                          date_create TIMESTAMP NOT NULL,
                          date_modify TIMESTAMP NOT NULL,
                          moved_time TIMESTAMP NOT NULL,
                          opportunity NUMERIC(19, 2) NOT NULL,
                          probability INTEGER NOT NULL,
                          stage_semantics VARCHAR(10) NOT NULL,
                          titulo VARCHAR(255) NOT NULL,
                          utm_campaign VARCHAR(255),
                          utm_medium VARCHAR(255),
                          utm_source VARCHAR(255),
                          assigned_user_id BIGINT NOT NULL,
                          created_user_id BIGINT NOT NULL,
                          pipeline_id BIGINT NOT NULL,
                          stage_id VARCHAR(255) NOT NULL,
                          CONSTRAINT check_closed CHECK ((closed = 0 AND closedate IS NULL) OR (closed = 1 AND closedate IS NOT NULL)),
                          FOREIGN KEY (pipeline_id) REFERENCES pipelines (id),
                          FOREIGN KEY (stage_id) REFERENCES stages (status_id)
);

CREATE TABLE movimentacoes (
                               id BIGINT NOT NULL PRIMARY KEY,
                               created_time TIMESTAMP NOT NULL,
                               type_id INTEGER NOT NULL,
                               id_negocio BIGINT NOT NULL,
                               pipeline_id BIGINT NOT NULL,
                               stage_id VARCHAR(255) NOT NULL,
                               FOREIGN KEY (id_negocio) REFERENCES negocios (id),
                               FOREIGN KEY (pipeline_id) REFERENCES pipelines (id)
);