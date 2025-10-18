-- ---------------------------------------------------------------------------
-- HEROIS
-- data_nascimento: TIMESTAMPTZ para casar com Instant no Java
-- ---------------------------------------------------------------------------
CREATE TABLE herois (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(120) NOT NULL,
    nome_heroi       VARCHAR(120) NOT NULL,
    data_nascimento  TIMESTAMPTZ NULL,
    altura           REAL NOT NULL,
    peso             REAL NOT NULL
);

-- índice útil para buscas por nome_heroi
CREATE INDEX ix_herois_nome_heroi ON herois (nome_heroi);

-- ---------------------------------------------------------------------------
-- SUPERPODERES
-- superpoder com unicidade lógica
-- ---------------------------------------------------------------------------
CREATE TABLE superpoderes (
    id          BIGSERIAL PRIMARY KEY,
    superpoder  VARCHAR(50)  NOT NULL,
    descricao   VARCHAR(250) NULL
);

CREATE UNIQUE INDEX ux_superpoderes_superpoder ON superpoderes (superpoder);

-- ---------------------------------------------------------------------------
-- Tabela de relacionamento (associação explícita)
-- - chave substituta (id)
-- - unicidade (heroi_id, superpoder_id)
-- - FKs com ON DELETE CASCADE para manter integridade
-- - created_at para auditoria básica (opcional)
-- ---------------------------------------------------------------------------
CREATE TABLE herois_superpoderes (
    id             BIGSERIAL PRIMARY KEY,
    heroi_id       BIGINT NOT NULL,
    superpoder_id  BIGINT NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_hs_heroi
        FOREIGN KEY (heroi_id) REFERENCES herois (id) ON DELETE CASCADE,
    CONSTRAINT fk_hs_superpoder
        FOREIGN KEY (superpoder_id) REFERENCES superpoderes (id) ON DELETE CASCADE,
    CONSTRAINT ux_heroi_superpoder UNIQUE (heroi_id, superpoder_id)
);

-- índices nos FKs ajudam nos JOINs
CREATE INDEX ix_hs_heroi_id ON herois_superpoderes (heroi_id);
CREATE INDEX ix_hs_superpoder_id ON herois_superpoderes (superpoder_id);