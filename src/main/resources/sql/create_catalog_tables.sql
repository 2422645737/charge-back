-- 创建目录层级结构表
CREATE TABLE IF NOT EXISTS web.catalog (
    catalog_id BIGINT PRIMARY KEY,
    parent_id BIGINT REFERENCES web.catalog(catalog_id),
    name VARCHAR(100) NOT NULL,
    level INT NOT NULL,
    path VARCHAR(255) NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    invalid_flag VARCHAR(1) NOT NULL DEFAULT '0'
);

-- 创建文章与目录关联关系表
CREATE TABLE IF NOT EXISTS web.article_catalog (
    article_catalog_id BIGINT PRIMARY KEY,
    article_id BIGINT REFERENCES article(article_id),
    catalog_id BIGINT REFERENCES web.catalog(catalog_id),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_catalog_parent_id ON web.catalog(parent_id);
CREATE INDEX IF NOT EXISTS idx_catalog_level ON web.catalog(level);
CREATE INDEX IF NOT EXISTS idx_catalog_path ON web.catalog(path);
CREATE INDEX IF NOT EXISTS idx_article_catalog_article_id ON web.article_catalog(article_id);
CREATE INDEX IF NOT EXISTS idx_article_catalog_catalog_id ON web.article_catalog(catalog_id);

-- 插入默认顶级目录
INSERT INTO web.catalog (catalog_id, parent_id, name, level, path, create_time, update_time, invalid_flag)
VALUES (1, 0, '根目录', 1, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0')
ON CONFLICT (catalog_id) DO NOTHING;
