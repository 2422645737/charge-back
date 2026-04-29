# 待办模块部署与配置

## 数据库
- 使用 PostgreSQL
- 执行脚本：src/main/resources/sql/create_todo_tables.sql

## 应用配置
- MyBatis-Plus 扫描路径已包含 mapper/*/*.xml
- Swagger/Knife4j 自动生成文档，基础包：com.wanghui.shiyue

## 构建与运行
- 构建：mvn -DskipTests package
- 运行：java -jar target/shiyue-0.0.1-SNAPSHOT.jar

## 日志与审计
- 操作日志表：todo_log
- 记录操作类型、目标ID、内容、结果与时间
