# 待办模块 API 文档（含 JSON 样例）

## 基础信息
- 路由前缀：/todo
- 约定：所有接口均使用 POST 请求方式；返回统一使用 ResponseResult
- 时间格式：yyyy-MM-dd HH:mm:ss（东八区）
- 优先级：3=高，2=中，1=低

---

## 1. 查询待办 /todo/query
- 方法：POST
- 入参：TodoQueryParam（请求体）

请求体样例
```json
{
  "completed": false,
  "priority": 3,
  "startTime": "2026-02-01 00:00:00",
  "endTime": "2026-02-28 23:59:59",
  "orderByPriority": true
}
```

响应体样例（data 为 List<TodoDTO>）
```json
{
  "success": true,
  "code": "0000",
  "message": "成功",
  "data": [
    {
      "todoId": 10001,
      "title": "年度 OKR 拆解",
      "remark": "围绕Q1目标细化",
      "dueTime": "2026-02-28 18:00:00",
      "priority": 3,
      "completed": false,
      "createTime": "2026-02-20 10:00:00",
      "updateTime": "2026-02-21 09:35:00",
      "subTasks": [
        {
          "subTaskId": 20001,
          "todoId": 10001,
          "parentId": null,
          "name": "确定关键结果",
          "completed": false,
          "createTime": "2026-02-20 10:05:00",
          "updateTime": "2026-02-21 09:10:00",
          "children": [
            {
              "subTaskId": 20002,
              "todoId": 10001,
              "parentId": 20001,
              "name": "量化 KR1 指标",
              "completed": false,
              "createTime": "2026-02-20 10:06:00",
              "updateTime": "2026-02-21 09:10:00",
              "children": []
            }
          ]
        }
      ]
    }
  ],
  "traceId": null,
  "exceptionName": null
}
```

---

## 2. 新增待办 /todo/add
- 方法：POST
- 入参：TodoDTO（请求体，不传 todoId）
- 约束：title 必填，remark ≤ 500 字符，dueTime 必填，priority ∈ {1,2,3}

请求体样例
```json
{
  "title": "阅读《深入理解JVM》",
  "remark": "第2版，完成第3-5章",
  "dueTime": "2026-03-05 21:00:00",
  "priority": 2,
  "completed": false,
  "subTasks": [
    { "name": "第3章 类加载机制", "completed": false },
    { "name": "第4章 内存模型", "completed": false }
  ]
}
```

响应体样例（返回创建后的 TodoDTO，包含生成的 todoId/subTaskId）
```json
{
  "success": true,
  "code": "0000",
  "message": "成功",
  "data": {
    "todoId": 10111,
    "title": "阅读《深入理解JVM》",
    "remark": "第2版，完成第3-5章",
    "dueTime": "2026-03-05 21:00:00",
    "priority": 2,
    "completed": false,
    "createTime": "2026-02-23 12:10:00",
    "updateTime": "2026-02-23 12:10:00",
    "subTasks": [
      { "subTaskId": 30101, "todoId": 10111, "parentId": null, "name": "第3章 类加载机制", "completed": false, "children": [] },
      { "subTaskId": 30102, "todoId": 10111, "parentId": null, "name": "第4章 内存模型", "completed": false, "children": [] }
    ]
  },
  "traceId": null,
  "exceptionName": null
}
```

---

## 3. 修改待办 /todo/update
- 方法：POST
- 入参：TodoDTO（请求体，包含 todoId；允许更新所有字段与子项目）

请求体样例
```json
{
  "todoId": 10111,
  "title": "阅读《深入理解JVM》（修订）",
  "remark": "优先完成第3章与第5章",
  "dueTime": "2026-03-06 20:30:00",
  "priority": 3,
  "completed": false,
  "subTasks": [
    { "subTaskId": 30101, "todoId": 10111, "name": "第3章 类加载机制（精读）", "completed": false },
    { "name": "第5章 垃圾回收", "completed": false }
  ]
}
```

响应体样例
```json
{
  "success": true,
  "code": "0000",
  "message": "成功",
  "data": {
    "todoId": 10111,
    "title": "阅读《深入理解JVM》（修订）",
    "remark": "优先完成第3章与第5章",
    "dueTime": "2026-03-06 20:30:00",
    "priority": 3,
    "completed": false,
    "createTime": "2026-02-23 12:10:00",
    "updateTime": "2026-02-23 13:20:00",
    "subTasks": [
      { "subTaskId": 30101, "todoId": 10111, "parentId": null, "name": "第3章 类加载机制（精读）", "completed": false, "children": [] },
      { "subTaskId": 30103, "todoId": 10111, "parentId": null, "name": "第5章 垃圾回收", "completed": false, "children": [] }
    ]
  }
}
```

---

## 4. 删除待办 /todo/delete
- 方法：POST
- 入参：todoId（请求参数 query/form）

请求参数 JSON 结构（用于说明）
```json
{ "todoId": 10111 }
```

响应体样例
```json
{ "success": true, "code": "0000", "message": "成功", "data": true }
```

---

## 5. 批量删除 /todo/deleteBatch
- 方法：POST
- 入参：List<Long>（请求体）

请求体样例
```json
[10111, 10112, 10113]
```

响应体样例
```json
{ "success": true, "code": "0000", "message": "成功", "data": true }
```

---

## 6. 子项目查询 /todo/sub/list
- 方法：POST
- 入参：todoId（请求参数 query/form）

请求参数 JSON 结构（用于说明）
```json
{ "todoId": 10001 }
```

响应体样例（data 为 List<SubTaskDTO>，树形）
```json
{
  "success": true,
  "code": "0000",
  "message": "成功",
  "data": [
    {
      "subTaskId": 20001,
      "todoId": 10001,
      "parentId": null,
      "name": "确定关键结果",
      "completed": false,
      "children": [
        { "subTaskId": 20002, "todoId": 10001, "parentId": 20001, "name": "量化 KR1 指标", "completed": false, "children": [] }
      ]
    }
  ]
}
```

---

## 7. 新增子项目 /todo/sub/add
- 方法：POST
- 入参：SubTaskDTO（请求体，不传 subTaskId）

请求体样例
```json
{
  "todoId": 10001,
  "parentId": 20001,
  "name": "为 KR1 设置里程碑",
  "completed": false
}
```

响应体样例
```json
{
  "success": true,
  "code": "0000",
  "message": "成功",
  "data": {
    "subTaskId": 20005,
    "todoId": 10001,
    "parentId": 20001,
    "name": "为 KR1 设置里程碑",
    "completed": false,
    "children": []
  }
}
```

---

## 8. 更新子项目 /todo/sub/update
- 方法：POST
- 入参：SubTaskDTO（请求体，包含 subTaskId）

请求体样例
```json
{
  "subTaskId": 20005,
  "todoId": 10001,
  "parentId": 20001,
  "name": "为 KR1 设置里程碑（细化）",
  "completed": true
}
```

响应体样例
```json
{
  "success": true,
  "code": "0000",
  "message": "成功",
  "data": {
    "subTaskId": 20005,
    "todoId": 10001,
    "parentId": 20001,
    "name": "为 KR1 设置里程碑（细化）",
    "completed": true,
    "children": []
  }
}
```

---

## 9. 删除子项目 /todo/sub/delete
- 方法：POST
- 入参：subTaskId（请求参数 query/form）

请求参数 JSON 结构（用于说明）
```json
{ "subTaskId": 20005 }
```

响应体样例
```json
{ "success": true, "code": "0000", "message": "成功", "data": true }
```
