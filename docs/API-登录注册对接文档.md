# 智慧图书 — 登录注册接口对接文档

## 1. 概述

### 1.1 认证方式

本项目采用 **JWT + HttpOnly Cookie** 的认证方案：

- 登录/注册成功后，后端生成 JWT，通过 `Set-Cookie: token=<jwt>; HttpOnly; Path=/; SameSite=Strict` 写入浏览器。
- 前端 axios 配置 `withCredentials: true`，后续请求会自动携带该 Cookie。
- 前端不保存真实 JWT，仅在 `localStorage` 中保存一个本地 UI 标记 `token='authenticated'` 和用户信息 `user`，用于路由守卫、页面展示和快速判断登录态。

### 1.2 基础信息

| 项 | 值 |
|---|---|
| 后端地址 | `http://localhost:8765`（由 `application.yml` 确定） |
| 接口前缀 | `/api/auth` |
| 前端代理 | `vite.config.js` 中 `/api` → `http://localhost:8765` |
| 数据库表 | `users` |
| 验证码存储 | Redis，Key 前缀 `verify_code:` / `verify_code_send:` |
| 邮件服务 | QQ 邮箱 SMTP（`smtp.qq.com:465`） |

### 1.3 核心文件

| 端 | 文件路径 |
|---|---|
| 后端 Controller | `zhihuitushu/src/main/java/com/yt/zhihuitushu/controller/AuthController.java` |
| 后端 Service | `zhihuitushu/src/main/java/com/yt/zhihuitushu/service/impl/UserServiceImpl.java` |
| 后端验证码 | `zhihuitushu/src/main/java/com/yt/zhihuitushu/service/impl/VerifyCodeServiceImpl.java` |
| 后端邮件服务 | `zhihuitushu/src/main/java/com/yt/zhihuitushu/service/EmailService.java` |
| 后端邮件实现 | `zhihuitushu/src/main/java/com/yt/zhihuitushu/service/impl/EmailServiceImpl.java` |
| 前端请求封装 | `zhihuitushu-frontend/src/api/auth.js` |
| 前端 axios | `zhihuitushu-frontend/src/api/request.js` |
| 前端状态 | `zhihuitushu-frontend/src/stores/auth.js` |
| 登录页 | `zhihuitushu-frontend/src/views/Login.vue` |
| 注册页 | `zhihuituitushu-frontend/src/views/Register.vue` |

---

## 2. 接口列表

### 2.1 发送验证码

- **Method**: `POST`
- **Path**: `/api/auth/send-code`
- **Content-Type**: `application/json`

#### 请求参数

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| account | string | 是 | 邮箱或手机号 |
| type | string | 是 | 当前前端传 `'email'`，后端不做枚举限制 |

```json
{
  "account": "hello@zhihuitushu.com",
  "type": "email"
}
```

#### 响应示例

```json
{
  "code": 200,
  "message": "验证码发送成功",
  "data": null
}
```

#### 业务规则

- 验证码为 6 位数字，有效期 5 分钟。
- 同一账号 60 秒内只能发送一次。
- 当 `type` 为 `email` 且 `account` 符合邮箱格式时，系统会通过 QQ 邮箱 SMTP 真实发送验证码邮件。
- 邮件发送失败时，已写入 Redis 的验证码会被清除，接口返回错误，避免用户收不到码但系统认为已发送。
- 本地测试时若未配置真实邮箱，仍可通过 Redis 查看验证码：`get verify_code:hello@zhihuitushu.com`。

#### 邮件配置说明

发送邮件依赖 `application.yml` 中的 `spring.mail` 配置：

```yaml
spring:
  mail:
    host: smtp.qq.com
    port: 465
    username: your_qq_email@qq.com
    password: your_qq_authorization_code
    protocol: smtp
    default-encoding: UTF-8
    properties:
      mail.smtp.auth: true
      mail.smtp.ssl.enable: true
      mail.smtp.starttls.enable: false
      mail.smtp.socketFactory.port: 465
      mail.smtp.socketFactory.class: javax.net.ssl.SSLSocketFactory
```

- `username` 替换为真实 QQ 邮箱地址。
- `password` 替换为 QQ 邮箱 SMTP 授权码（不是 QQ 登录密码）。
- 生产环境建议通过环境变量 `${MAIL_USERNAME}` / `${MAIL_PASSWORD}` 注入。

---

### 2.2 注册

- **Method**: `POST`
- **Path**: `/api/auth/register`
- **Content-Type**: `application/json`

#### 请求参数

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| account | string | 是 | 邮箱或手机号，注册后作为 username 并写入 email/phone |
| password | string | 是 | 至少 8 位，包含大小写字母和数字 |
| confirmPassword | string | 是 | 与 password 一致 |
| code | string | 是 | 6 位数字验证码 |

```json
{
  "account": "hello@zhihuitushu.com",
  "password": "Hello1234",
  "confirmPassword": "Hello1234",
  "code": "123456"
}
```

#### 响应示例

```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "user": {
      "id": 1,
      "username": "hello@zhihuitushu.com",
      "avatar": null,
      "role": "user"
    }
  }
}
```

#### 业务规则

- 两次密码必须一致。
- 验证码必须正确且未过期。
- 账号不能已存在。
- 注册成功后自动写入 JWT Cookie（默认 2 小时有效），无需再调登录接口。
- 后端根据 `account` 自动识别邮箱/手机号，并写入对应字段。

---

### 2.3 登录

- **Method**: `POST`
- **Path**: `/api/auth/login`
- **Content-Type**: `application/json`

#### 请求参数

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| account | string | 是 | 邮箱、手机号或用户名 |
| password | string | 是 | 密码 |
| rememberMe | boolean | 否 | 是否记住登录，默认 `false` |

```json
{
  "account": "hello@zhihuitushu.com",
  "password": "Hello1234",
  "rememberMe": false
}
```

#### 响应示例

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "user": {
      "id": 1,
      "username": "hello@zhihuitushu.com",
      "avatar": null,
      "role": "user"
    }
  }
}
```

#### 业务规则

- 账号不存在时返回错误。
- 密码连续错误 3 次后账号锁定 5 分钟。
- 账号被禁用时返回错误。
- 登录成功后写入 JWT Cookie，默认 2 小时；`rememberMe=true` 时 7 天。

---

### 2.4 获取当前用户

- **Method**: `GET`
- **Path**: `/api/auth/me`

#### 响应示例

已登录：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "hello@zhihuitushu.com",
    "avatar": null,
    "role": "user"
  }
}
```

未登录：

```json
{
  "code": 401,
  "message": "未登录",
  "data": null
}
```

#### 用途

- 应用启动时调用，恢复登录态。
- 若返回 401，前端清除本地 `token` / `user` 标记。

---

### 2.5 退出登录

- **Method**: `POST`
- **Path**: `/api/auth/logout`

#### 响应示例

```json
{
  "code": 200,
  "message": "退出成功",
  "data": null
}
```

#### 业务规则

- 后端清除 `token` Cookie（Max-Age=0）。
- 前端同步清除 `localStorage` 中的 `token` 和 `user`。

---

## 3. 错误码与错误处理

### 3.1 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| code | 含义 |
|---|---|
| 200 | 成功 |
| 400 | 请求参数校验失败 |
| 401 | 未登录 / Token 无效 |
| 500 | 业务错误 / 服务器内部错误 |

### 3.2 常见错误信息

| 场景 | message |
|---|---|
| 账号不存在 | 账号不存在 |
| 密码错误 | 密码错误，还剩 N 次机会 |
| 账号锁定 | 账号已被锁定，请5分钟后再试 |
| 验证码错误 | 验证码错误 |
| 发送频率过高 | 发送频率过高，请稍后再试 |
| 账号已注册 | 该账号已注册，请直接登录 |
| 两次密码不一致 | 两次输入的密码不一致 |
| 账号被禁用 | 账号已被禁用 |

### 3.3 前端错误处理

- 表单提交前进行本地校验，实时清除错误状态。
- 后端返回错误时，统一显示在表单错误提示区域。
- 网络异常时显示“网络错误，请稍后重试”。

---

## 4. 前端对接说明

### 4.1 axios 配置

文件：`src/api/request.js`

```js
import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 后端使用 HttpOnly Cookie 下发 JWT，前端无需手动注入 Authorization
request.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
)

request.interceptors.response.use(
  (response) => {
    const { data } = response
    if (data.code !== 200) {
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    return data
  },
  (error) => {
    const message = error.response?.data?.message || error.message || '网络错误'
    return Promise.reject(new Error(message))
  }
)

export default request
```

### 4.2 认证状态

文件：`src/stores/auth.js`

- `token`：本地 UI 标记，值为 `'authenticated'`，仅用于快速判断登录态。
- `user`：当前用户信息，包含 `id`、`username`、`avatar`、`role`。
- `isLoggedIn`：基于 `token` 或 `user` 判断。
- `setToken` / `setUser` / `clearToken`：同步更新 `localStorage`。

### 4.3 接口封装

文件：`src/api/auth.js`

```js
import request from './request.js'

export function sendCode(account, type = 'email') {
  return request.post('/auth/send-code', { account, type })
}

export function login(account, password, rememberMe = false) {
  return request.post('/auth/login', { account, password, rememberMe })
}

export function register(account, password, confirmPassword, code) {
  return request.post('/auth/register', { account, password, confirmPassword, code })
}

export function logout() {
  return request.post('/auth/logout')
}

export function getCurrentUser() {
  return request.get('/auth/me')
}
```

### 4.4 登录页与注册页校验

#### 登录页

- 账号必填。
- 密码必填。

#### 注册页

- 账号必填，且必须是邮箱或手机号格式。
- 密码至少 8 位，包含大小写字母和数字。
- 确认密码与密码一致。
- 验证码 6 位数字。
- 必须同意用户协议和隐私政策。

---

## 5. 安全说明

- 密码采用 `BCryptPasswordEncoder` 加密存储，不会明文保存。
- JWT 使用 `HMAC-SHA256` 签名，默认 2 小时过期，记住登录时 7 天。
- Cookie 设置为 `HttpOnly`，前端 JS 无法读取，降低 XSS 风险。
- Cookie 设置为 `SameSite=Strict`，防止 CSRF 跨站请求。
- 登录失败 3 次锁定 5 分钟，防止暴力破解。
- 验证码 5 分钟有效，发送间隔 60 秒，防止频繁刷接口。

---

## 6. 本地联调步骤

### 6.1 启动后端

```bash
cd zhihuitushu
mvn spring-boot:run
```

确认：
- 端口为 `8765`。
- Redis 已启动且连接正常。
- MySQL 已启动且数据库表已创建。

### 6.2 启动前端

```bash
cd zhihuitushu-frontend
npm run dev
```

前端默认运行在 `http://localhost:5173`，通过 Vite 代理 `/api` 到 `http://localhost:8765`。

### 6.3 测试注册

1. 打开 `http://localhost:5173/register`。
2. 输入邮箱账号，点击“获取”验证码。
3. 通过 Redis 查看验证码：`redis-cli get verify_code:<your-account>`。
4. 输入验证码、密码、确认密码，勾选协议，点击注册。
5. 注册成功后自动跳转首页。

### 6.4 测试登录

1. 打开 `http://localhost:5173/login`。
2. 输入刚才注册的账号和密码。
3. 登录成功后自动跳转首页或之前被拦截的页面。

### 6.5 验证登录态

1. 刷新页面，观察 `/api/auth/me` 是否返回当前用户信息。
2. 点击退出登录，观察 Cookie 是否被清除，页面是否跳回登录页。

---

## 7. 测试用例

| 用例 | 步骤 | 预期结果 |
|---|---|---|
| 注册成功 | 填写正确邮箱、验证码、密码，勾选协议 | 返回 200，自动写入 Cookie，跳转首页 |
| 注册-账号已存在 | 使用已注册邮箱再次注册 | 提示“该账号已注册，请直接登录” |
| 注册-验证码错误 | 输入错误验证码 | 提示“验证码错误” |
| 注册-两次密码不一致 | 密码和确认密码不同 | 前端提示“两次输入的密码不一致” |
| 注册-密码强度不足 | 输入 6 位纯数字密码 | 前端提示“密码至少8位，且包含大小写字母和数字” |
| 登录成功 | 输入正确账号密码 | 返回 200，写入 Cookie，跳转首页 |
| 登录-账号不存在 | 输入未注册账号 | 提示“账号不存在” |
| 登录-密码错误 | 输入错误密码 | 提示“密码错误，还剩 N 次机会” |
| 登录-账号锁定 | 连续错误 3 次 | 第 3 次提示“账号已被锁定，请5分钟后再试” |
| 登录态恢复 | 登录后刷新页面 | 调用 `/api/auth/me` 成功，保持登录态 |
| 退出登录 | 点击退出按钮 | Cookie 清除，本地状态清除，跳回登录页 |

---

## 8. 已知限制与后续优化

1. 忘记密码功能已预留按钮，但重置密码接口 `/api/auth/reset-password` 尚未在前端页面实现。
2. “记住我”功能后端已支持，但 UI 上尚未提供勾选框。
3. 用户协议/隐私政策文本目前仅为占位文案，未配置实际内容。
4. 生产环境部署时，CORS 的 `addAllowedOriginPattern("*")` 应收紧为具体域名。
5. 当前邮件服务使用 QQ 邮箱 SMTP，发送频率和每日上限受 QQ 邮箱策略限制；大规模用户场景建议接入专业邮件服务（如 SendGrid、Amazon SES 或企业邮箱）。
6. 邮件发送失败时仅做简单异常处理和日志记录，后续可增加重试机制、异步发送队列（如 RabbitMQ / Spring `@Async`）和邮件发送统计。

---

*文档版本：v1.0*  
*更新日期：2026-07-05*
