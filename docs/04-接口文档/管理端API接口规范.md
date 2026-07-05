# 智慧图书阅读平台 — 管理端API接口规范

---

## 1 概述

### 1.1 文档说明

本文档定义了智慧图书阅读平台管理端的所有API接口规范，供前后端开发人员参考使用。

### 1.2 接口基础信息

| 项目 | 说明 |
|------|------|
| 基础路径 | /api/admin |
| 协议 | HTTP/HTTPS |
| 数据格式 | JSON |
| 字符编码 | UTF-8 |
| 认证方式 | JWT (HttpOnly Cookie) |

### 1.3 认证说明

- 管理员登录成功后，后端将JWT令牌存入名为 `admin_token` 的HttpOnly Cookie
- 后续请求自动携带Cookie，后端通过 `@AdminAuth` 注解验证管理员身份
- 未认证请求返回 `401 Unauthorized`

---

## 2 通用规范

### 2.1 请求格式

#### 请求头

```
Content-Type: application/json
Accept: application/json
Cookie: admin_token=<jwt_token>
```

#### 分页参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 当前页码 |
| pageSize | Integer | 否 | 10 | 每页条数(最大100) |

### 2.2 响应格式

#### 成功响应

```json
{
    "code": 200,
    "message": "success",
    "data": { }
}
```

#### 错误响应

```json
{
    "code": 400,
    "message": "错误描述",
    "data": null
}
```

### 2.3 状态码规范

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证/认证失败 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 2.4 时间格式

所有时间字段使用 ISO 8601 格式：`yyyy-MM-ddTHH:mm:ss`

---

## 3 管理员认证接口

### 3.1 管理员登录

**接口地址**：`POST /api/admin/auth/login`

**接口说明**：管理员使用用户名和密码登录，成功后设置Cookie并返回管理员信息。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**请求示例**：

```json
{
    "username": "admin",
    "password": "admin123"
}
```

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 管理员ID |
| username | String | 用户名 |
| nickname | String | 昵称 |
| avatar | String | 头像URL |
| email | String | 邮箱 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "admin",
        "nickname": "系统管理员",
        "avatar": "https://example.com/avatar.png",
        "email": "admin@example.com"
    }
}
```

**错误响应**：

```json
{
    "code": 401,
    "message": "用户名或密码错误",
    "data": null
}
```

```json
{
    "code": 403,
    "message": "账号已被禁用",
    "data": null
}
```

---

### 3.2 管理员登出

**接口地址**：`POST /api/admin/auth/logout`

**接口说明**：管理员退出登录，清除Cookie中的令牌。

**请求参数**：无

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": null
}
```

---

### 3.3 获取当前管理员信息

**接口地址**：`GET /api/admin/auth/info`

**接口说明**：获取当前登录管理员的详细信息。

**请求参数**：无

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 管理员ID |
| username | String | 用户名 |
| nickname | String | 昵称 |
| avatar | String | 头像URL |
| email | String | 邮箱 |
| phone | String | 手机号 |
| lastLoginTime | String | 最后登录时间 |
| createdAt | String | 创建时间 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "admin",
        "nickname": "系统管理员",
        "avatar": "https://example.com/avatar.png",
        "email": "admin@example.com",
        "phone": "13800138000",
        "lastLoginTime": "2026-07-05T10:30:00",
        "createdAt": "2026-01-01T00:00:00"
    }
}
```

---

### 3.4 修改密码

**接口地址**：`PUT /api/admin/auth/password`

**接口说明**：管理员修改自己的登录密码。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| oldPassword | String | 是 | 原密码 |
| newPassword | String | 是 | 新密码(至少8位，包含大小写字母和数字) |
| confirmPassword | String | 是 | 确认新密码 |

**请求示例**：

```json
{
    "oldPassword": "admin123",
    "newPassword": "NewPass123",
    "confirmPassword": "NewPass123"
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "密码修改成功",
    "data": null
}
```

**错误响应**：

```json
{
    "code": 400,
    "message": "原密码错误",
    "data": null
}
```

```json
{
    "code": 400,
    "message": "两次输入的密码不一致",
    "data": null
}
```

---

## 4 图书管理接口

### 4.1 获取图书列表

**接口地址**：`GET /api/admin/books`

**接口说明**：分页查询图书列表，支持搜索和筛选。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 当前页码 |
| pageSize | Integer | 否 | 10 | 每页条数 |
| keyword | String | 否 | - | 搜索关键词(书名/作者/ISBN) |
| categoryId | Long | 否 | - | 分类ID |
| status | Integer | 否 | - | 状态(0下架/1上架) |
| sortBy | String | 否 | createdAt | 排序字段 |
| sortOrder | String | 否 | desc | 排序方式(asc/desc) |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| total | Long | 总记录数 |
| page | Integer | 当前页码 |
| pageSize | Integer | 每页条数 |
| list | Array | 图书列表 |

**图书对象**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 图书ID |
| title | String | 书名 |
| author | String | 作者 |
| cover | String | 封面URL |
| categoryId | Long | 分类ID |
| categoryName | String | 分类名称 |
| publisher | String | 出版社 |
| publishDate | String | 出版日期 |
| isbn | String | ISBN编号 |
| rating | Double | 评分 |
| readCount | Integer | 阅读次数 |
| status | Integer | 状态 |
| createdAt | String | 创建时间 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "total": 100,
        "page": 1,
        "pageSize": 10,
        "list": [
            {
                "id": 1,
                "title": "深入理解计算机系统",
                "author": "Randal E. Bryant",
                "cover": "https://example.com/cover/1.jpg",
                "categoryId": 1,
                "categoryName": "计算机",
                "publisher": "机械工业出版社",
                "publishDate": "2016-07-01",
                "isbn": "9787111544937",
                "rating": 9.5,
                "readCount": 1234,
                "status": 1,
                "createdAt": "2026-01-01T00:00:00"
            }
        ]
    }
}
```

---

### 4.2 获取图书详情

**接口地址**：`GET /api/admin/books/{id}`

**接口说明**：获取指定图书的详细信息。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 图书ID |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 图书ID |
| title | String | 书名 |
| author | String | 作者 |
| cover | String | 封面URL |
| categoryId | Long | 分类ID |
| categoryName | String | 分类名称 |
| publisher | String | 出版社 |
| publishDate | String | 出版日期 |
| language | String | 语言 |
| pages | Integer | 页数 |
| isbn | String | ISBN编号 |
| description | String | 图书简介 |
| rating | Double | 评分 |
| reviewCount | Integer | 评论数量 |
| readCount | Integer | 阅读次数 |
| downloadCount | Integer | 下载次数 |
| status | Integer | 状态 |
| createdAt | String | 创建时间 |
| updatedAt | String | 更新时间 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "title": "深入理解计算机系统",
        "author": "Randal E. Bryant",
        "cover": "https://example.com/cover/1.jpg",
        "categoryId": 1,
        "categoryName": "计算机",
        "publisher": "机械工业出版社",
        "publishDate": "2016-07-01",
        "language": "中文",
        "pages": 737,
        "isbn": "9787111544937",
        "description": "本书从程序员的视角...",
        "rating": 9.5,
        "reviewCount": 100,
        "readCount": 1234,
        "downloadCount": 567,
        "status": 1,
        "createdAt": "2026-01-01T00:00:00",
        "updatedAt": "2026-07-01T00:00:00"
    }
}
```

---

### 4.3 新增图书

**接口地址**：`POST /api/admin/books`

**接口说明**：添加新的图书信息。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 书名(最长200字符) |
| author | String | 是 | 作者(最长100字符) |
| cover | String | 否 | 封面URL |
| categoryId | Long | 是 | 分类ID |
| publisher | String | 否 | 出版社 |
| publishDate | String | 否 | 出版日期(yyyy-MM-dd) |
| language | String | 否 | 语言(默认中文) |
| pages | Integer | 否 | 页数 |
| isbn | String | 否 | ISBN编号(唯一) |
| description | String | 否 | 图书简介 |
| status | Integer | 是 | 状态(0下架/1上架) |

**请求示例**：

```json
{
    "title": "深入理解计算机系统",
    "author": "Randal E. Bryant",
    "cover": "https://example.com/cover/1.jpg",
    "categoryId": 1,
    "publisher": "机械工业出版社",
    "publishDate": "2016-07-01",
    "language": "中文",
    "pages": 737,
    "isbn": "9787111544937",
    "description": "本书从程序员的视角...",
    "status": 1
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "添加成功",
    "data": {
        "id": 1
    }
}
```

---

### 4.4 更新图书

**接口地址**：`PUT /api/admin/books/{id}`

**接口说明**：更新指定图书的信息。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 图书ID |

**请求参数**：同新增图书

**请求示例**：

```json
{
    "title": "深入理解计算机系统（第3版）",
    "author": "Randal E. Bryant",
    "cover": "https://example.com/cover/1.jpg",
    "categoryId": 1,
    "publisher": "机械工业出版社",
    "publishDate": "2016-07-01",
    "language": "中文",
    "pages": 737,
    "isbn": "9787111544937",
    "description": "本书从程序员的视角...",
    "status": 1
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "更新成功",
    "data": null
}
```

---

### 4.5 删除图书

**接口地址**：`DELETE /api/admin/books/{id}`

**接口说明**：删除指定图书（软删除或物理删除）。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 图书ID |

**响应示例**：

```json
{
    "code": 200,
    "message": "删除成功",
    "data": null
}
```

---

### 4.6 批量删除图书

**接口地址**：`DELETE /api/admin/books/batch`

**接口说明**：批量删除图书。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| ids | Array[Long] | 是 | 图书ID列表 |

**请求示例**：

```json
{
    "ids": [1, 2, 3, 4, 5]
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "批量删除成功",
    "data": {
        "successCount": 5,
        "failCount": 0
    }
}
```

---

### 4.7 更新图书状态

**接口地址**：`PUT /api/admin/books/{id}/status`

**接口说明**：更新图书上下架状态。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 图书ID |

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | Integer | 是 | 状态(0下架/1上架) |

**请求示例**：

```json
{
    "status": 0
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "状态更新成功",
    "data": null
}
```

---

## 5 分类管理接口

### 5.1 获取分类列表

**接口地址**：`GET /api/admin/categories`

**接口说明**：获取所有分类列表，支持树形结构返回。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| tree | Boolean | 否 | false | 是否返回树形结构 |

**响应参数（平铺列表）**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 分类ID |
| name | String | 分类名称 |
| parentId | Long | 父分类ID |
| icon | String | 分类图标 |
| sortOrder | Integer | 排序值 |
| status | Integer | 状态 |
| bookCount | Integer | 关联图书数量 |
| createdAt | String | 创建时间 |

**响应示例（平铺列表）**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": 1,
            "name": "计算机",
            "parentId": 0,
            "icon": "computer",
            "sortOrder": 1,
            "status": 1,
            "bookCount": 100,
            "createdAt": "2026-01-01T00:00:00"
        },
        {
            "id": 2,
            "name": "编程语言",
            "parentId": 1,
            "icon": "code",
            "sortOrder": 1,
            "status": 1,
            "bookCount": 50,
            "createdAt": "2026-01-01T00:00:00"
        }
    ]
}
```

**响应示例（树形结构）**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": 1,
            "name": "计算机",
            "parentId": 0,
            "icon": "computer",
            "sortOrder": 1,
            "status": 1,
            "bookCount": 50,
            "children": [
                {
                    "id": 2,
                    "name": "编程语言",
                    "parentId": 1,
                    "icon": "code",
                    "sortOrder": 1,
                    "status": 1,
                    "bookCount": 30
                }
            ]
        }
    ]
}
```

---

### 5.2 新增分类

**接口地址**：`POST /api/admin/categories`

**接口说明**：添加新的图书分类。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 分类名称(最长50字符，唯一) |
| parentId | Long | 否 | 父分类ID(默认0为顶级) |
| icon | String | 否 | 分类图标 |
| sortOrder | Integer | 否 | 排序值(默认0) |
| status | Integer | 是 | 状态(0禁用/1启用) |

**请求示例**：

```json
{
    "name": "人工智能",
    "parentId": 1,
    "icon": "ai",
    "sortOrder": 1,
    "status": 1
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "添加成功",
    "data": {
        "id": 10
    }
}
```

---

### 5.3 更新分类

**接口地址**：`PUT /api/admin/categories/{id}`

**接口说明**：更新指定分类信息。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 分类ID |

**请求参数**：同新增分类

**响应示例**：

```json
{
    "code": 200,
    "message": "更新成功",
    "data": null
}
```

---

### 5.4 删除分类

**接口地址**：`DELETE /api/admin/categories/{id}`

**接口说明**：删除指定分类。如果分类下有图书或子分类，则返回错误。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 分类ID |

**响应示例**：

```json
{
    "code": 200,
    "message": "删除成功",
    "data": null
}
```

**错误响应**：

```json
{
    "code": 400,
    "message": "该分类下存在图书，无法删除",
    "data": null
}
```

---

### 5.5 更新分类状态

**接口地址**：`PUT /api/admin/categories/{id}/status`

**接口说明**：启用或禁用分类。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | Integer | 是 | 状态(0禁用/1启用) |

**响应示例**：

```json
{
    "code": 200,
    "message": "状态更新成功",
    "data": null
}
```

---

## 6 用户管理接口

### 6.1 获取用户列表

**接口地址**：`GET /api/admin/users`

**接口说明**：分页查询用户列表，支持搜索和筛选。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 当前页码 |
| pageSize | Integer | 否 | 10 | 每页条数 |
| keyword | String | 否 | - | 搜索关键词(用户名/邮箱/昵称) |
| status | Integer | 否 | - | 状态(0禁用/1启用) |
| sortBy | String | 否 | createdAt | 排序字段 |
| sortOrder | String | 否 | desc | 排序方式(asc/desc) |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| total | Long | 总记录数 |
| page | Integer | 当前页码 |
| pageSize | Integer | 每页条数 |
| list | Array | 用户列表 |

**用户对象**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户ID |
| username | String | 用户名 |
| email | String | 邮箱 |
| nickname | String | 昵称 |
| avatar | String | 头像URL |
| status | Integer | 状态 |
| bookCount | Integer | 书架图书数量 |
| readTime | Integer | 累计阅读时长(分钟) |
| createdAt | String | 注册时间 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "total": 1000,
        "page": 1,
        "pageSize": 10,
        "list": [
            {
                "id": 1,
                "username": "user001",
                "email": "user001@example.com",
                "nickname": "阅读达人",
                "avatar": "https://example.com/avatar/1.jpg",
                "status": 1,
                "bookCount": 15,
                "readTime": 1200,
                "createdAt": "2026-01-01T00:00:00"
            }
        ]
    }
}
```

---

### 6.2 获取用户详情

**接口地址**：`GET /api/admin/users/{id}`

**接口说明**：获取指定用户的详细信息。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户ID |
| username | String | 用户名 |
| email | String | 邮箱 |
| phone | String | 手机号 |
| nickname | String | 昵称 |
| avatar | String | 头像URL |
| status | Integer | 状态 |
| role | String | 角色 |
| failedAttempts | Integer | 登录失败次数 |
| lockedAt | String | 锁定时间 |
| lastLoginTime | String | 最后登录时间 |
| createdAt | String | 注册时间 |
| updatedAt | String | 更新时间 |
| statistics | Object | 用户统计数据 |

**用户统计数据**：

| 参数 | 类型 | 说明 |
|------|------|------|
| bookCount | Integer | 书架图书数 |
| readingCount | Integer | 阅读中图书数 |
| completedCount | Integer | 已完成图书数 |
| totalReadTime | Integer | 累计阅读时长(分钟) |
| commentCount | Integer | 评论数 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "user001",
        "email": "user001@example.com",
        "phone": "13800138000",
        "nickname": "阅读达人",
        "avatar": "https://example.com/avatar/1.jpg",
        "status": 1,
        "role": "user",
        "failedAttempts": 0,
        "lockedAt": null,
        "lastLoginTime": "2026-07-05T10:00:00",
        "createdAt": "2026-01-01T00:00:00",
        "updatedAt": "2026-07-05T10:00:00",
        "statistics": {
            "bookCount": 15,
            "readingCount": 3,
            "completedCount": 5,
            "totalReadTime": 1200,
            "commentCount": 10
        }
    }
}
```

---

### 6.3 更新用户状态

**接口地址**：`PUT /api/admin/users/{id}/status`

**接口说明**：启用或禁用用户账号。

**路径参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | Integer | 是 | 状态(0禁用/1启用) |
| reason | String | 否 | 禁用原因 |

**请求示例**：

```json
{
    "status": 0,
    "reason": "违规操作"
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "状态更新成功",
    "data": null
}
```

---

## 7 数据统计接口

### 7.1 获取概览数据

**接口地址**：`GET /api/admin/stats/overview`

**接口说明**：获取平台整体统计数据。

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| userCount | Long | 用户总数 |
| todayNewUsers | Long | 今日新增用户 |
| bookCount | Long | 图书总数 |
| todayReadCount | Long | 今日阅读量 |
| categoryCount | Long | 分类数量 |
| commentCount | Long | 评论总数 |
| userGrowthRate | Double | 用户增长率(%) |
| readGrowthRate | Double | 阅读增长率(%) |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "userCount": 10000,
        "todayNewUsers": 50,
        "bookCount": 5000,
        "todayReadCount": 1200,
        "categoryCount": 20,
        "commentCount": 3000,
        "userGrowthRate": 5.5,
        "readGrowthRate": 12.3
    }
}
```

---

### 7.2 获取用户增长趋势

**接口地址**：`GET /api/admin/stats/users/trend`

**接口说明**：获取近N天用户增长趋势数据。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| days | Integer | 否 | 30 | 天数(最大90) |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| date | String | 日期 |
| count | Long | 新增用户数 |
| total | Long | 累计用户数 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "date": "2026-06-05",
            "count": 30,
            "total": 9500
        },
        {
            "date": "2026-06-06",
            "count": 45,
            "total": 9545
        }
    ]
}
```

---

### 7.3 获取阅读趋势

**接口地址**：`GET /api/admin/stats/reading/trend`

**接口说明**：获取近N天阅读趋势数据。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| days | Integer | 否 | 30 | 天数(最大90) |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| date | String | 日期 |
| readCount | Long | 阅读次数 |
| readTime | Long | 阅读时长(分钟) |
| uniqueReaders | Long | 阅读用户数 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "date": "2026-06-05",
            "readCount": 1200,
            "readTime": 36000,
            "uniqueReaders": 350
        },
        {
            "date": "2026-06-06",
            "readCount": 1500,
            "readTime": 45000,
            "uniqueReaders": 420
        }
    ]
}
```

---

### 7.4 获取热门图书

**接口地址**：`GET /api/admin/stats/books/top`

**接口说明**：获取热门图书排行榜。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| limit | Integer | 否 | 10 | 返回数量(最大50) |
| type | String | 否 | read | 排行类型(read/rating/download) |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| rank | Integer | 排名 |
| id | Long | 图书ID |
| title | String | 书名 |
| author | String | 作者 |
| cover | String | 封面 |
| categoryName | String | 分类名称 |
| readCount | Long | 阅读次数 |
| rating | Double | 评分 |
| downloadCount | Long | 下载次数 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "rank": 1,
            "id": 1,
            "title": "深入理解计算机系统",
            "author": "Randal E. Bryant",
            "cover": "https://example.com/cover/1.jpg",
            "categoryName": "计算机",
            "readCount": 5000,
            "rating": 9.5,
            "downloadCount": 1200
        }
    ]
}
```

---

### 7.5 获取活跃用户

**接口地址**：`GET /api/admin/stats/users/top`

**接口说明**：获取活跃用户排行榜。

**请求参数**：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| limit | Integer | 否 | 10 | 返回数量(最大50) |
| type | String | 否 | readTime | 排行类型(readTime/bookCount) |

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| rank | Integer | 排名 |
| id | Long | 用户ID |
| username | String | 用户名 |
| nickname | String | 昵称 |
| avatar | String | 头像 |
| readTime | Long | 阅读时长(分钟) |
| bookCount | Integer | 书架图书数 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "rank": 1,
            "id": 1,
            "username": "user001",
            "nickname": "阅读达人",
            "avatar": "https://example.com/avatar/1.jpg",
            "readTime": 5000,
            "bookCount": 30
        }
    ]
}
```

---

### 7.6 获取分类分布

**接口地址**：`GET /api/admin/stats/categories/distribution`

**接口说明**：获取各分类图书数量分布。

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 分类ID |
| name | String | 分类名称 |
| bookCount | Long | 图书数量 |
| percentage | Double | 占比(%) |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": 1,
            "name": "计算机",
            "bookCount": 500,
            "percentage": 25.0
        },
        {
            "id": 2,
            "name": "文学",
            "bookCount": 400,
            "percentage": 20.0
        }
    ]
}
```

---

## 8 系统设置接口

### 8.1 获取管理员信息

**接口地址**：`GET /api/admin/settings/info`

**接口说明**：获取当前管理员的详细信息。

**响应参数**：同 3.3 获取当前管理员信息

---

### 8.2 更新管理员信息

**接口地址**：`PUT /api/admin/settings/info`

**接口说明**：更新管理员个人信息（不包括密码）。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| nickname | String | 否 | 昵称 |
| avatar | String | 否 | 头像URL |
| email | String | 否 | 邮箱 |
| phone | String | 否 | 手机号 |

**请求示例**：

```json
{
    "nickname": "超级管理员",
    "avatar": "https://example.com/avatar/admin.jpg",
    "email": "admin@example.com",
    "phone": "13800138000"
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "更新成功",
    "data": null
}
```

---

### 8.3 获取系统配置

**接口地址**：`GET /api/admin/settings/config`

**接口说明**：获取系统配置列表。

**响应参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| configKey | String | 配置键 |
| configValue | String | 配置值 |
| configType | String | 配置类型 |
| description | String | 配置说明 |

**响应示例**：

```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "configKey": "site_name",
            "configValue": "智慧图书阅读平台",
            "configType": "string",
            "description": "网站名称"
        },
        {
            "configKey": "site_logo",
            "configValue": "https://example.com/logo.png",
            "configType": "string",
            "description": "网站Logo"
        },
        {
            "configKey": "max_upload_size",
            "configValue": "10485760",
            "configType": "number",
            "description": "最大上传文件大小(字节)"
        }
    ]
}
```

---

### 8.4 更新系统配置

**接口地址**：`PUT /api/admin/settings/config`

**接口说明**：批量更新系统配置。

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| configs | Array | 是 | 配置列表 |

**configs数组元素**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| configKey | String | 是 | 配置键 |
| configValue | String | 是 | 配置值 |

**请求示例**：

```json
{
    "configs": [
        {
            "configKey": "site_name",
            "configValue": "智慧图书阅读平台"
        },
        {
            "configKey": "site_logo",
            "configValue": "https://example.com/logo.png"
        }
    ]
}
```

**响应示例**：

```json
{
    "code": 200,
    "message": "配置更新成功",
    "data": null
}
```

---

## 9 错误码说明

### 9.1 通用错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证/认证失败 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 409 | 资源冲突 |
| 500 | 服务器内部错误 |

### 9.2 业务错误码

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户名或密码错误 |
| 1002 | 账号已被禁用 |
| 1003 | 账号已被锁定 |
| 2001 | 图书不存在 |
| 2002 | 图书ISBN已存在 |
| 3001 | 分类不存在 |
| 3002 | 分类名称已存在 |
| 3003 | 分类下存在图书，无法删除 |
| 4001 | 用户不存在 |

---

## 10 附录

### 10.1 请求示例（cURL）

```bash
# 管理员登录
curl -X POST 'http://localhost:8080/api/admin/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}'

# 获取图书列表
curl -X GET 'http://localhost:8080/api/admin/books?page=1&pageSize=10' \
  -H 'Cookie: admin_token=<your_token>'

# 新增图书
curl -X POST 'http://localhost:8080/api/admin/books' \
  -H 'Content-Type: application/json' \
  -H 'Cookie: admin_token=<your_token>' \
  -d '{
    "title": "深入理解计算机系统",
    "author": "Randal E. Bryant",
    "categoryId": 1,
    "status": 1
  }'
```

### 10.2 接口版本记录

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0 | 2026-07-05 | 初始版本 |

---

*本文档版本：v1.0*
*创建日期：2026-07-05*
*维护团队：智慧图书阅读平台开发组*