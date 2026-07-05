import request from './request.js'

// 管理员登录
export function adminLogin(username, password) {
  return request.post('/admin/login', { username, password })
}

// 获取管理员信息
export function getAdminInfo() {
  return request.get('/admin/info')
}

// 管理员登出
export function adminLogout() {
  return request.post('/admin/logout')
}

// ==================== 图书管理 ====================

/**
 * 上传图书封面
 * @param {File} file - 图片文件
 */
export function uploadCover(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload/cover', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 分页查询图书列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 当前页码（默认1）
 * @param {number} params.pageSize - 每页条数（默认10）
 * @param {string} params.keyword - 搜索关键词（书名/作者/ISBN）
 * @param {number} params.categoryId - 分类ID
 * @param {number} params.status - 状态（0下架/1上架）
 * @param {string} params.sortBy - 排序字段
 * @param {string} params.sortOrder - 排序方式（asc/desc）
 */
export function getBookList(params) {
  return request.get('/admin/books', { params })
}

/**
 * 查询单个图书详情
 * @param {number} id - 图书ID
 */
export function getBookDetail(id) {
  return request.get(`/admin/books/${id}`)
}

/**
 * 创建图书
 * @param {Object} data - 图书数据
 * @param {string} data.title - 书名（必填）
 * @param {string} data.author - 作者（必填）
 * @param {number} data.categoryId - 分类ID（必填）
 * @param {string} data.cover - 封面URL
 * @param {string} data.publisher - 出版社
 * @param {string} data.publishDate - 出版日期（yyyy-MM-dd）
 * @param {string} data.language - 语言
 * @param {number} data.pages - 页数
 * @param {string} data.isbn - ISBN编号
 * @param {string} data.description - 图书简介
 * @param {number} data.status - 状态（0下架/1上架，默认1）
 */
export function createBook(data) {
  return request.post('/admin/books', data)
}

// 添加图书（兼容旧方法名）
export function addBook(data) {
  return createBook(data)
}

/**
 * 更新图书信息
 * @param {number} id - 图书ID
 * @param {Object} data - 更新数据（字段同创建图书）
 */
export function updateBook(id, data) {
  return request.put(`/admin/books/${id}`, data)
}

/**
 * 删除单个图书
 * @param {number} id - 图书ID
 */
export function deleteBook(id) {
  return request.delete(`/admin/books/${id}`)
}

/**
 * 批量删除图书
 * @param {Array<number>} ids - 图书ID列表
 */
export function batchDeleteBooks(ids) {
  return request.delete('/admin/books/batch', { data: { ids } })
}

/**
 * 更新图书状态（上架/下架）
 * @param {number} id - 图书ID
 * @param {number} status - 状态（0下架/1上架）
 */
export function updateBookStatus(id, status) {
  return request.put(`/admin/books/${id}/status`, { status })
}

// 查询单个书籍（别名方法）
export function getBookById(id) {
  return getBookDetail(id)
}

// ==================== 分类管理 ====================

/**
 * 获取分类列表
 * @param {boolean} tree - 是否返回树形结构
 */
export function getCategoryList(tree = false) {
  return request.get('/admin/categories', { params: { tree } })
}

/**
 * 新增分类
 * @param {Object} data - 分类数据
 * @param {string} data.name - 分类名称（必填）
 * @param {number} data.parentId - 父分类ID（默认0）
 * @param {string} data.icon - 分类图标
 * @param {number} data.sortOrder - 排序值
 * @param {number} data.status - 状态（0禁用/1启用）
 */
export function createCategory(data) {
  return request.post('/admin/categories', data)
}

/**
 * 更新分类
 * @param {number} id - 分类ID
 * @param {Object} data - 更新数据
 */
export function updateCategory(id, data) {
  return request.put(`/admin/categories/${id}`, data)
}

/**
 * 删除分类
 * @param {number} id - 分类ID
 */
export function deleteCategory(id) {
  return request.delete(`/admin/categories/${id}`)
}

/**
 * 更新分类状态
 * @param {number} id - 分类ID
 * @param {number} status - 状态（0禁用/1启用）
 */
export function updateCategoryStatus(id, status) {
  return request.put(`/admin/categories/${id}/status`, { status })
}

// ==================== 用户管理 ====================

// 获取用户列表
export function getUserList(params) {
  return request.get('/admin/users', { params })
}

// 获取用户详情
export function getUserDetail(id) {
  return request.get(`/admin/users/${id}`)
}

// 更新用户状态
export function updateUserStatus(id, status) {
  return request.put(`/admin/users/${id}/status`, { status })
}

// 删除用户
export function deleteUser(id) {
  return request.delete(`/admin/users/${id}`)
}

// ==================== 数据统计 ====================

// 获取统计数据概览
export function getStatistics() {
  return request.get('/admin/statistics')
}

// 获取阅读趋势数据
export function getReadingTrend(params) {
  return request.get('/admin/statistics/reading-trend', { params })
}

// 获取热门图书排行
export function getPopularBooks(limit = 10) {
  return request.get('/admin/statistics/popular-books', { params: { limit } })
}

// 获取活跃用户排行
export function getActiveUsers(limit = 10) {
  return request.get('/admin/statistics/active-users', { params: { limit } })
}

// ==================== 系统设置 ====================

// 获取系统设置
export function getSystemSettings() {
  return request.get('/admin/settings')
}

// 更新系统设置
export function updateSystemSettings(data) {
  return request.put('/admin/settings', data)
}