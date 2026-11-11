CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE mentions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    brand_id        UUID NOT NULL,
    source_id       VARCHAR(100) NOT NULL,
    external_id     VARCHAR(500),
    job_id          UUID,
    url             TEXT NOT NULL,
    title           TEXT,
    text            TEXT,
    author          VARCHAR(255),
    published_at    TIMESTAMPTZ,
    collected_at    TIMESTAMPTZ NOT NULL,
    sentiment       VARCHAR(20) NOT NULL DEFAULT 'NEUTRAL',
    sentiment_score REAL,
    model_version   VARCHAR(50),
    topic           VARCHAR(255),
    processed_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Уникальность для дедупликации: external_id (если есть) или нормализованный URL
CREATE UNIQUE INDEX uq_mentions_external_id
    ON mentions (brand_id, source_id, external_id)
    WHERE external_id IS NOT NULL;

CREATE UNIQUE INDEX uq_mentions_url
    ON mentions (brand_id, source_id, url)
    WHERE external_id IS NULL;

-- Индексы для фильтрации
CREATE INDEX idx_mentions_brand_published ON mentions (brand_id, published_at DESC);
CREATE INDEX idx_mentions_sentiment ON mentions (sentiment);
CREATE INDEX idx_mentions_source ON mentions (source_id);
CREATE INDEX idx_mentions_published_at ON mentions (published_at DESC);
CREATE INDEX idx_mentions_job_id ON mentions (job_id);

-- Триграммный индекс для полнотекстового поиска
CREATE INDEX idx_mentions_title_trgm ON mentions USING gin (title gin_trgm_ops);
CREATE INDEX idx_mentions_text_trgm ON mentions USING gin (text gin_trgm_ops);