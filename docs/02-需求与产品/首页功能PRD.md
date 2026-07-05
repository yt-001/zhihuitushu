# 智慧图书 — 首页功能 PRD

## 1. Executive Summary

### Problem Statement
智慧图书作为一款移动端 H5 图书阅读应用，首页是用户进入 App 后的第一触点。当前首页尚未接入真实数据，无法展示推荐内容、Banner 轮播和分类入口，影响用户发现图书的效率。

### Proposed Solution
构建一个数据驱动的首页，包含顶部导航、Tab 切换（推荐/畅销/新书/分类）、Banner 轮播、分类快捷入口、编辑精选书籍列表，并与后端真实数据库（books / categories / banners）对接。

### Success Criteria
- 首页首屏加载时间 ≤ 1.5 秒（本地开发环境）。
- 首页 5 个核心模块（标题栏、Banner、分类、Tab、书籍列表）全部接入后端接口。
- 用户可以在首页完成「浏览 Banner → 切换 Tab → 点击书籍卡片」的完整动线。
- 分类入口和 Tab 筛选能正确过滤书籍数据。
- 页面在 375–430px 移动端宽度下无水平滚动、无布局错位。

---

## 2. User Experience & Functionality

### User Personas
- **普通读者**：希望在首页快速发现感兴趣的书籍，查看编辑推荐和热门榜单。
- **新用户**：通过分类入口和 Banner 了解平台内容，找到第一本想读的书。

### User Stories

#### Story 1：浏览首页推荐
- As a 读者
- I want 打开 App 后看到编辑推荐和热门书籍
- So that 我可以快速找到想读的书

**Acceptance Criteria**
- 首页默认显示「推荐」Tab 内容。
- 推荐书籍按综合评分和阅读量排序，最多展示 10 本。
- 每本书展示封面、书名、作者、评分/阅读量。

#### Story 2：切换 Tab 查看不同榜单
- As a 读者
- I want 点击「畅销」「新书」等 Tab
- So that 我可以按不同维度发现书籍

**Acceptance Criteria**
- 点击 Tab 后列表内容切换，无需整页刷新。
- 「畅销」按 `read_count` 降序排列。
- 「新书」按 `publish_date` 降序排列。
- 切换时显示加载状态或骨架屏。

#### Story 3：通过分类入口筛选
- As a 读者
- I want 点击「小说」「心理学」等分类图标
- So that 我可以按分类浏览书籍

**Acceptance Criteria**
- 首页展示 5 个常用分类入口（小说、心理学、商业、科普、艺术）。
- 点击分类后跳转到分类详情页或筛选对应分类的书籍。
- 分类入口从后端 `categories` 表读取，支持后台配置。

#### Story 4：浏览 Banner 轮播
- As a 读者
- I want 看到首页顶部的 Banner 轮播
- So that 我可以了解平台活动和重点推荐

**Acceptance Criteria**
- Banner 支持自动轮播和手动滑动。
- 每张 Banner 包含背景图、标题、副标题、标签、跳转链接。
- Banner 数据从后端 `banners` 表读取，支持后台配置上下线和排序。

#### Story 5：搜索与通知
- As a 读者
- I want 点击搜索或通知图标
- So that 我可以快速进入搜索页或查看通知

**Acceptance Criteria**
- 搜索图标点击进入搜索页（搜索页功能不在本次范围）。
- 通知图标显示未读数红点（本次仅做 UI 占位，真实通知后续实现）。

### Non-Goals
- 本次不实现搜索页的搜索功能和搜索结果。
- 本次不实现通知列表和未读数真实计算。
- 本次不实现个性化推荐算法，仅按字段排序。
- 本次不实现书籍详情页（点击书籍卡片可预留跳转）。
- 本次不实现后台管理端对 Banner / 分类 / 书籍的配置界面。

---

## 3. AI System Requirements

本次首页功能不涉及 AI 能力，无需 AI 系统要求。

---

## 4. Technical Specifications

### Architecture Overview

```
前端 Home.vue
  ├── 顶部标题栏（固定组件）
  ├── Banner 轮播 ← GET /api/home/banners
  ├── 分类快捷入口 ← GET /api/home/categories
  ├── Tab 切换（推荐/畅销/新书/分类）
  │     └── 书籍列表 ← GET /api/books?type=recommend|hot|new
  └── 底部导航栏（固定组件）
```

### Integration Points

| 数据源 | 表/接口 | 说明 |
|---|---|---|
| Banner | `banners` 表 | `GET /api/home/banners` |
| 分类 | `categories` 表 | `GET /api/home/categories` |
| 书籍 | `books` 表 | `GET /api/books?type=...&page=&size=` |
| 用户态 | JWT Cookie | 首页接口均允许匿名访问 |

### Database Schema Additions

当前数据库已有 `users`、`categories`、`books` 等表。本次新增 `banners` 表：

```sql
CREATE TABLE IF NOT EXISTS banners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'BannerID',
    title VARCHAR(100) NOT NULL COMMENT '主标题',
    subtitle VARCHAR(200) NULL COMMENT '副标题',
    tag VARCHAR(50) NULL COMMENT '标签（如科幻经典）',
    image_url VARCHAR(500) NOT NULL COMMENT '背景图URL',
    link_url VARCHAR(500) NULL COMMENT '跳转链接',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    status TINYINT DEFAULT 1 COMMENT '状态(0下架,1上架)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='首页Banner表';
```

### Security & Privacy
- 首页接口允许匿名访问，不强制要求登录。
- 返回的书籍列表仅包含已上架（`status=1`）的图书。
- Banner 和分类同样只返回已启用的数据。

---

## 5. Risks & Roadmap

### Phased Rollout

**MVP（本次实现）**
- 新增 `banners` 表并补充 Mock 数据。
- 后端提供 3 个接口：`/api/home/banners`、`/api/home/categories`、`/api/books`。
- 前端首页完成 5 个模块的展示和接口对接。

**v1.1（后续）**
- 接入真实搜索页。
- 通知未读数真实计算。
- 书籍详情页。

**v2.0（后续）**
- 后台管理端配置 Banner / 分类 / 书籍。
- 个性化推荐算法。

### Technical Risks
- 封面图使用第三方图片服务，可能存在加载慢或失效风险，后续应迁移到对象存储（COS）。
- 如果 `books` 表数据量大，推荐/畅销/新书接口需要分页和索引优化。
- Banner 轮播图尺寸不一致可能导致视觉错位，需要统一图片比例。

---

## 6. 接口清单（供开发使用）

| 接口 | Method | Path | 说明 |
|---|---|---|---|
| 获取 Banner 列表 | GET | `/api/home/banners` | 返回首页轮播图 |
| 获取分类入口 | GET | `/api/home/categories` | 返回首页展示的分类 |
| 获取书籍列表 | GET | `/api/books?type=&page=&size=` | type: recommend/hot/new |

---

*文档版本：v1.0*  
*更新日期：2026-07-05*
