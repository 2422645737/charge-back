# 目录层级结构设计文档

## 1. 设计背景

为了实现文章的树状目录层级展示，需要设计目录层级结构表和文章与目录的关联关系表，确保与现有文章表、类别表、标签表的兼容性。

## 2. 设计目标

- 支持无限层级的目录嵌套
- 提供文章与目录的关联管理功能
- 确保与现有数据模型的兼容性
- 优化查询性能，支持目录层级的快速遍历

## 3. 数据库表结构设计

### 3.1 catalog 表（目录层级结构表）

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| catalog_id | BIGINT | PRIMARY KEY | 目录ID |
| parent_id | BIGINT | FOREIGN KEY (REFERENCES catalog(catalog_id)) | 父目录ID，顶级目录为0 |
| name | VARCHAR(100) | NOT NULL | 目录名称 |
| level | INT | NOT NULL | 目录层级，从1开始 |
| path | VARCHAR(255) | NOT NULL | 目录路径，如 "1/2/3" |
| create_time | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| invalid_flag | VARCHAR(1) | NOT NULL DEFAULT '0' | 作废标识，0-有效，1-作废 |

### 3.2 article_catalog 表（文章与目录关联关系表）

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| article_catalog_id | BIGINT | PRIMARY KEY | 关联ID |
| article_id | BIGINT | FOREIGN KEY (REFERENCES article(article_id)) | 文章ID |
| catalog_id | BIGINT | FOREIGN KEY (REFERENCES catalog(catalog_id)) | 目录ID |
| create_time | TIMESTAMP | NOT NULL DEFAULT CURRENT_TIMESTAMP | 创建时间 |

## 4. 设计说明

### 4.1 catalog 表设计说明

- **层级实现**：使用 parent_id 自引用实现目录层级关系，顶级目录的 parent_id 为 0
- **性能优化**：
  - level 字段记录目录层级，避免递归查询
  - path 字段记录目录路径，支持快速定位和范围查询
- **数据完整性**：
  - 所有字段都有明确的约束
  - 时间戳字段使用默认值和自动更新
  - 作废标识用于逻辑删除

### 4.2 article_catalog 表设计说明

- **关联关系**：实现文章与目录的多对多关联
- **兼容性**：与现有 article 表的 catalog_id 字段并存，支持两种关联方式
  - catalog_id 字段用于记录文章的主目录
  - article_catalog 表用于支持文章属于多个目录的场景

## 5. 与现有表的关系

### 5.1 与 article 表的关系

- article 表中的 catalog_id 字段与 catalog 表的 catalog_id 字段关联，用于记录文章的主目录
- article_catalog 表作为中间表，实现文章与目录的多对多关联

### 5.2 与其他表的关系

- 与 class 表（类别表）无直接关联，文章的类别信息仍通过 article 表的 class_id 字段记录
- 与 tag 表（标签表）无直接关联，文章的标签信息仍通过 article_tag 表记录

## 6. 查询优化策略

1. **目录层级查询**：利用 path 字段和 level 字段，避免递归查询
2. **文章按目录查询**：通过 article_catalog 表的索引，支持快速查询指定目录下的文章
3. **多维度筛选**：结合目录、类别、标签的关联关系，支持复合条件查询

## 7. 数据迁移策略

1. **现有文章数据迁移**：
   - 对于已有 catalog_id 的文章，保持不变
   - 对于 catalog_id 为 null 的文章，可选择默认目录或创建新目录

2. **目录结构初始化**：
   - 创建默认顶级目录
   - 根据业务需求，初始化常用的目录结构

## 8. 技术实现要点

1. **目录层级管理**：
   - 实现目录的增删改查
   - 支持目录的移动和重命名
   - 维护目录的 level 和 path 字段

2. **文章与目录关联**：
   - 支持文章的目录分配和修改
   - 维护文章的主目录和多目录关联

3. **前端展示**：
   - 实现目录树状结构的展示
   - 支持目录的展开和折叠
   - 提供文章按目录的导航功能
