# 文章数据迁移方案

## 1. 迁移背景

在现有文章管理系统基础上，新增了目录层级功能，需要将现有文章数据与新的目录结构进行关联，确保数据的完整性和一致性。

## 2. 迁移策略

### 2.1 目录结构准备

1. 首先创建根目录（如果尚未创建）
2. 根据现有文章的分类结构，创建对应的目录层级
3. 确保目录结构的合理性和可维护性

### 2.2 文章数据迁移

1. **单目录关联**：将文章的`catalog_id`字段值作为主目录关联
2. **多目录关联**：支持文章关联到多个目录
3. **数据验证**：确保迁移过程中数据的完整性

## 3. 迁移步骤

### 3.1 准备阶段

1. 执行目录结构创建SQL语句，创建目录表和文章目录关联表
2. 检查现有文章数据，统计需要迁移的文章数量

### 3.2 执行阶段

1. 创建根目录
2. 根据现有分类创建对应目录
3. 执行文章目录关联迁移
4. 验证迁移结果

### 3.3 验证阶段

1. 检查文章目录关联数据
2. 验证文章查询功能是否正常
3. 确认数据完整性

## 4. 迁移SQL语句

### 4.1 创建目录结构

```sql
-- 创建根目录
INSERT INTO web.catalog (catalog_id, parent_id, name, level, path, invalid_flag, create_time) 
VALUES (1, 0, '根目录', 1, '/1', '0', NOW()) ON DUPLICATE KEY UPDATE name = '根目录';

-- 根据现有分类创建目录
INSERT INTO web.catalog (catalog_id, parent_id, name, level, path, invalid_flag, create_time) 
SELECT 
    class_id + 10000, -- 避免ID冲突
    1, -- 父目录为根目录
    class_name, -- 目录名称
    2, -- 目录层级
    CONCAT('/1/', class_id + 10000), -- 目录路径
    '0', -- 有效
    NOW() -- 创建时间
FROM class WHERE invalid_flag = '0' ON DUPLICATE KEY UPDATE name = class_name;
```

### 4.2 迁移文章目录关联

```sql
-- 迁移文章目录关联（基于现有catalog_id字段）
INSERT INTO web.article_catalog (article_catalog_id, article_id, catalog_id) 
SELECT 
    (article_id * 10000 + IFNULL(catalog_id, 0)), -- 生成唯一ID
    article_id, 
    IFNULL(catalog_id, 1) -- 如果catalog_id为空，关联到根目录
FROM article WHERE invalid_flag = '0' 
ON DUPLICATE KEY UPDATE catalog_id = IFNULL(catalog_id, 1);

-- 迁移文章目录关联（基于分类创建的目录）
INSERT INTO web.article_catalog (article_catalog_id, article_id, catalog_id) 
SELECT 
    (article_id * 10000 + class_id + 20000), -- 生成唯一ID
    article_id, 
    class_id + 10000 -- 关联到对应分类的目录
FROM article WHERE invalid_flag = '0' AND class_id IS NOT NULL 
ON DUPLICATE KEY UPDATE catalog_id = class_id + 10000;
```

### 4.3 数据验证

```sql
-- 验证文章目录关联数据
SELECT 
    COUNT(*) AS total_articles, 
    COUNT(DISTINCT article_id) AS unique_articles, 
    COUNT(*) AS total_relations 
FROM web.article_catalog;

-- 检查是否所有文章都有关联目录
SELECT 
    COUNT(*) AS articles_without_catalog 
FROM article 
WHERE invalid_flag = '0' 
AND article_id NOT IN (SELECT article_id FROM web.article_catalog);
```

## 5. 迁移注意事项

1. **数据备份**：在执行迁移操作前，务必对现有数据进行备份
2. **ID冲突**：确保迁移过程中不会产生ID冲突，特别是目录ID和关联表ID
3. **性能优化**：对于大量数据的迁移，建议分批执行，避免数据库压力过大
4. **错误处理**：迁移过程中可能出现的错误，需要有相应的处理机制
5. **回滚方案**：准备迁移失败的回滚方案，确保系统可以恢复到迁移前的状态

## 6. 迁移后验证

1. **功能验证**：验证文章的CRUD操作是否正常
2. **查询验证**：验证按目录、分类、标签的多维度查询是否正常
3. **数据一致性**：验证文章数据与目录关联的一致性
4. **性能验证**：验证系统性能是否满足要求

## 7. 总结

本迁移方案旨在确保现有文章数据能够平滑过渡到新的目录结构中，同时保持数据的完整性和一致性。通过合理的迁移策略和步骤，可以最小化迁移风险，确保系统的稳定运行。