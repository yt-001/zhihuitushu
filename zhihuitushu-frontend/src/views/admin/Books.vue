<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Plus, Edit, Delete, View, Refresh,
  Download, Upload, Filter, Picture
} from '@element-plus/icons-vue'
import {
  getBookList, createBook, updateBook, deleteBook, batchDeleteBooks,
  getBookById, updateBookStatus, getCategoryList, uploadCover
} from '@/api/admin.js'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchQuery = ref('')
const categoryFilter = ref(null)
const statusFilter = ref(null)

// 分类列表（从API获取）
const categories = ref([])

// 状态选项
const statusOptions = [
  { label: '全部', value: null },
  { label: '上架', value: 1 },
  { label: '下架', value: 0 }
]

// 排序配置
const sortField = ref('createdAt')
const sortOrder = ref('descending')

// 选中行
const selectedRows = ref([])

// 弹窗相关
const dialogVisible = ref(false)
const dialogTitle = ref('添加图书')
const dialogForm = reactive({
  id: null,
  title: '',
  author: '',
  isbn: '',
  categoryId: null,
  publisher: '',
  publishDate: '',
  language: '中文',
  pages: null,
  description: '',
  cover: '',
  status: 1
})
const formRef = ref(null)
const submitLoading = ref(false)
const coverUploadLoading = ref(false)

// 详情弹窗
const detailDialogVisible = ref(false)
const currentBook = ref(null)

// 表单验证规则
const formRules = {
  title: [
    { required: true, message: '请输入书名', trigger: 'blur' },
    { min: 1, max: 200, message: '书名长度在 1 到 200 个字符', trigger: 'blur' }
  ],
  author: [
    { required: true, message: '请输入作者', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 统计
const activeCount = computed(() => tableData.value.filter(b => b.status === 1).length)
const inactiveCount = computed(() => tableData.value.filter(b => b.status === 0).length)

// 获取分类列表
async function fetchCategories() {
  try {
    const res = await getCategoryList()
    categories.value = res.data || []
  } catch (err) {
    console.error('获取分类列表失败:', err)
    ElMessage.error('获取分类列表失败')
  }
}

// 获取图书列表
async function fetchBooks() {
  loading.value = true
  try {
    const res = await getBookList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      categoryId: categoryFilter.value,
      status: statusFilter.value,
      sortBy: sortField.value,
      sortOrder: sortOrder.value === 'descending' ? 'desc' : 'asc'
    })
    tableData.value = res.data?.records || res.data?.list || []
    total.value = res.data?.total || 0
  } catch (err) {
    console.error('获取图书列表失败:', err)
    ElMessage.error(err.message || '获取图书列表失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  currentPage.value = 1
  fetchBooks()
}

// 重置搜索
function handleReset() {
  searchQuery.value = ''
  categoryFilter.value = null
  statusFilter.value = null
  currentPage.value = 1
  fetchBooks()
}

// 选择变化
function handleSelectionChange(rows) {
  selectedRows.value = rows
}

// 排序变化
function handleSortChange({ prop, order }) {
  sortField.value = prop || 'createdAt'
  sortOrder.value = order || 'descending'
  fetchBooks()
}

// 分页变化
function handlePageChange(page) {
  currentPage.value = page
  fetchBooks()
}

function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
  fetchBooks()
}

// 重置弹窗表单
function resetDialogForm() {
  Object.assign(dialogForm, {
    id: null,
    title: '',
    author: '',
    isbn: '',
    categoryId: null,
    publisher: '',
    publishDate: '',
    language: '中文',
    pages: null,
    description: '',
    cover: '',
    status: 1
  })
}

// 添加图书
function handleAdd() {
  dialogTitle.value = '添加图书'
  resetDialogForm()
  dialogVisible.value = true
}

// 编辑图书
function handleEdit(row) {
  dialogTitle.value = '编辑图书'
  resetDialogForm()
  Object.assign(dialogForm, {
    id: row.id,
    title: row.title,
    author: row.author,
    isbn: row.isbn || '',
    categoryId: row.categoryId,
    publisher: row.publisher || '',
    publishDate: row.publishDate || '',
    language: row.language || '中文',
    pages: row.pages || null,
    description: row.description || '',
    cover: row.cover || '',
    status: row.status
  })
  dialogVisible.value = true
}

// 查看详情
async function handleView(row) {
  try {
    const res = await getBookById(row.id)
    currentBook.value = res.data
    detailDialogVisible.value = true
  } catch (err) {
    console.error('获取图书详情失败:', err)
    ElMessage.error(err.message || '获取图书详情失败')
  }
}

// 切换状态
async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const statusText = newStatus === 1 ? '上架' : '下架'

  try {
    await ElMessageBox.confirm(`确定要${statusText}《${row.title}》吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await updateBookStatus(row.id, newStatus)
    ElMessage.success(`${statusText}成功`)
    row.status = newStatus
  } catch (err) {
    if (err !== 'cancel') {
      console.error('更新状态失败:', err)
      ElMessage.error(err.message || '操作失败')
    }
  }
}

// 删除图书
async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除《${row.title}》吗？删除后将无法恢复。`, '删除确认', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'error'
    })

    await deleteBook(row.id)
    ElMessage.success('删除成功')
    fetchBooks()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('删除失败:', err)
      ElMessage.error(err.message || '删除失败')
    }
  }
}

// 批量删除
async function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的图书')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 本图书吗？删除后将无法恢复。`,
      '批量删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }
    )

    const ids = selectedRows.value.map(row => row.id)
    await batchDeleteBooks(ids)
    ElMessage.success(`成功删除 ${selectedRows.value.length} 本图书`)
    selectedRows.value = []
    fetchBooks()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('批量删除失败:', err)
      ElMessage.error(err.message || '批量删除失败')
    }
  }
}

// 批量上架
async function handleBatchActive() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要上架的图书')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要上架选中的 ${selectedRows.value.length} 本图书吗？`,
      '批量上架确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    const promises = selectedRows.value.map(row => updateBookStatus(row.id, 1))
    await Promise.all(promises)
    ElMessage.success(`成功上架 ${selectedRows.value.length} 本图书`)
    selectedRows.value = []
    fetchBooks()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('批量上架失败:', err)
      ElMessage.error(err.message || '批量上架失败')
    }
  }
}

// 批量下架
async function handleBatchInactive() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要下架的图书')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要下架选中的 ${selectedRows.value.length} 本图书吗？`,
      '批量下架确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const promises = selectedRows.value.map(row => updateBookStatus(row.id, 0))
    await Promise.all(promises)
    ElMessage.success(`成功下架 ${selectedRows.value.length} 本图书`)
    selectedRows.value = []
    fetchBooks()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('批量下架失败:', err)
      ElMessage.error(err.message || '批量下架失败')
    }
  }
}

// 封面上传
function beforeCoverUpload(rawFile) {
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp', 'image/bmp']
  if (!allowedTypes.includes(rawFile.type)) {
    ElMessage.error('仅支持 jpg、png、gif、webp、bmp 格式的图片')
    return false
  }
  if (rawFile.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return true
}

async function handleCoverUpload(options) {
  coverUploadLoading.value = true
  try {
    const res = await uploadCover(options.file)
    dialogForm.cover = res.data.url
    ElMessage.success('封面上传成功')
    return res
  } catch (err) {
    ElMessage.error(err.message || '封面上传失败')
    return Promise.reject(err)
  } finally {
    coverUploadLoading.value = false
  }
}

function handleCoverRemove() {
  dialogForm.cover = ''
}

// 提交表单
async function handleSubmit() {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
  } catch {
    ElMessage.warning('请完善表单信息')
    return
  }

  submitLoading.value = true
  try {
    if (dialogForm.id) {
      await updateBook(dialogForm.id, dialogForm)
      ElMessage.success('更新成功')
    } else {
      await createBook(dialogForm)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchBooks()
  } catch (err) {
    console.error('操作失败:', err)
    ElMessage.error(err.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 导出数据
function handleExport() {
  ElMessage.success('导出功能开发中...')
}

// 导入数据
function handleImport() {
  ElMessage.success('导入功能开发中...')
}

// 初始化加载
onMounted(() => {
  fetchCategories()
  fetchBooks()
})
</script>

<template>
  <div class="books-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">图书管理</h1>
      <p class="page-description">管理平台所有图书信息，支持增删改查操作</p>
    </div>

    <!-- 搜索和操作栏 -->
    <div class="toolbar-card">
      <div class="toolbar">
        <div class="search-area">
          <el-input
            v-model="searchQuery"
            placeholder="搜索书名、作者、ISBN"
            :prefix-icon="Search"
            clearable
            class="search-input"
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          />
          <el-select
            v-model="categoryFilter"
            placeholder="分类"
            clearable
            class="filter-select"
            @change="handleSearch"
          >
            <el-option label="全部分类" :value="null" />
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
          <el-select
            v-model="statusFilter"
            placeholder="状态"
            clearable
            class="filter-select"
            @change="handleSearch"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </div>
        <div class="action-area">
          <el-button
            v-if="selectedRows.length > 0"
            type="success"
            plain
            @click="handleBatchActive"
          >
            批量上架
          </el-button>
          <el-button
            v-if="selectedRows.length > 0"
            type="warning"
            plain
            @click="handleBatchInactive"
          >
            批量下架
          </el-button>
          <el-button
            v-if="selectedRows.length > 0"
            type="danger"
            :icon="Delete"
            @click="handleBatchDelete"
          >
            批量删除 ({{ selectedRows.length }})
          </el-button>
          <el-button :icon="Download" @click="handleExport">导出</el-button>
          <el-button :icon="Upload" @click="handleImport">导入</el-button>
          <el-button type="primary" :icon="Plus" @click="handleAdd">添加图书</el-button>
        </div>
      </div>
    </div>

    <!-- 数据统计卡片 -->
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-icon total">
          <el-icon :size="24"><View /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">图书总数</p>
          <p class="stat-value">{{ total }}</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon active">
          <el-icon :size="24"><View /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">上架图书</p>
          <p class="stat-value">{{ activeCount }}</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon inactive">
          <el-icon :size="24"><View /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">下架图书</p>
          <p class="stat-value">{{ inactiveCount }}</p>
        </div>
      </div>
    </div>

    <!-- 表格 -->
    <div class="table-container">
      <div class="table-wrapper">
        <el-table
          :data="tableData"
          v-loading="loading"
          stripe
          highlight-current-row
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
          style="width: 100%; min-width: 1000px"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="封面" width="72" align="center">
            <template #default="{ row }">
              <el-image
                :src="row.cover"
                :alt="row.title"
                class="book-cover"
                fit="cover"
                lazy
              >
                <template #error>
                  <div class="cover-error">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="书名" min-width="180" show-overflow-tooltip sortable="custom" />
          <el-table-column prop="author" label="作者" min-width="120" show-overflow-tooltip />
          <el-table-column prop="isbn" label="ISBN" min-width="140" show-overflow-tooltip />
          <el-table-column prop="categoryName" label="分类" width="96" align="center">
            <template #default="{ row }">
              <el-tag size="small" type="info">{{ row.categoryName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template #default="{ row }">
              <el-tag
                :type="row.status === 1 ? 'success' : 'info'"
                size="small"
                effect="dark"
              >
                {{ row.status === 1 ? '上架' : '下架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="readCount" label="阅读次数" width="100" align="center" sortable="custom">
            <template #default="{ row }">
              <span class="read-count">{{ row.readCount?.toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="添加时间" width="120" align="center" sortable="custom" />
          <el-table-column label="操作" width="220" fixed="right" align="center">
            <template #default="{ row }">
              <el-button text type="primary" size="small" :icon="View" @click="handleView(row)">
                查看
              </el-button>
              <el-button text type="primary" size="small" :icon="Edit" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button
                :type="row.status === 1 ? 'warning' : 'success'"
                text
                size="small"
                @click="handleToggleStatus(row)"
              >
                {{ row.status === 1 ? '下架' : '上架' }}
              </el-button>
              <el-button text type="danger" size="small" :icon="Delete" @click="handleDelete(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="760px"
      class="book-dialog"
      destroy-on-close
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="dialogForm"
        :rules="formRules"
        label-width="80px"
        label-position="right"
      >
        <el-row :gutter="24">
          <!-- 左侧：基本信息 -->
          <el-col :xs="24" :md="14">
            <div class="dialog-section">
              <h3 class="dialog-section-title">基本信息</h3>
              <el-form-item label="书名" prop="title">
                <el-input v-model="dialogForm.title" placeholder="请输入书名" maxlength="200" show-word-limit />
              </el-form-item>
              <el-form-item label="作者" prop="author">
                <el-input v-model="dialogForm.author" placeholder="请输入作者" maxlength="100" show-word-limit />
              </el-form-item>
              <el-form-item label="ISBN" prop="isbn">
                <el-input v-model="dialogForm.isbn" placeholder="例如：978-7-111-23456-7" />
              </el-form-item>
              <el-form-item label="分类" prop="categoryId">
                <el-select v-model="dialogForm.categoryId" placeholder="请选择分类" style="width: 100%">
                  <el-option
                    v-for="cat in categories"
                    :key="cat.id"
                    :label="cat.name"
                    :value="cat.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="简介" prop="description">
                <el-input
                  v-model="dialogForm.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入图书简介"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </div>

            <div class="dialog-section">
              <h3 class="dialog-section-title">出版信息</h3>
              <el-form-item label="出版社" prop="publisher">
                <el-input v-model="dialogForm.publisher" placeholder="请输入出版社" />
              </el-form-item>
              <el-row :gutter="16">
                <el-col :span="12">
                  <el-form-item label="出版日期" prop="publishDate">
                    <el-date-picker
                      v-model="dialogForm.publishDate"
                      type="date"
                      placeholder="选择出版日期"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="页数" prop="pages">
                    <el-input-number v-model="dialogForm.pages" :min="1" placeholder="页数" style="width: 100%" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="语言" prop="language">
                <el-input v-model="dialogForm.language" placeholder="请输入语言" />
              </el-form-item>
            </div>
          </el-col>

          <!-- 右侧：封面与状态 -->
          <el-col :xs="24" :md="10">
            <div class="dialog-section">
              <h3 class="dialog-section-title">封面与状态</h3>
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="dialogForm.status">
                  <el-radio :value="1">上架</el-radio>
                  <el-radio :value="0">下架</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="封面上传">
                <div class="cover-uploader-area">
                  <el-upload
                    accept="image/*"
                    :show-file-list="false"
                    :http-request="handleCoverUpload"
                    :before-upload="beforeCoverUpload"
                    :disabled="coverUploadLoading"
                    class="cover-uploader"
                  >
                    <div v-if="dialogForm.cover" class="cover-preview">
                      <el-image :src="dialogForm.cover" fit="cover" class="cover-preview-image" />
                      <div class="cover-actions">
                        <el-button
                          type="danger"
                          link
                          size="small"
                          :icon="Delete"
                          @click.stop="handleCoverRemove"
                        >
                          移除
                        </el-button>
                      </div>
                    </div>
                    <div v-else class="cover-placeholder">
                      <el-icon :size="28" class="upload-icon"><Plus /></el-icon>
                      <span class="upload-text">点击或拖拽上传封面</span>
                      <span class="upload-tip">支持 jpg / png / gif / webp，最大 5MB</span>
                    </div>
                  </el-upload>
                </div>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="图书详情"
      width="720px"
      class="book-dialog"
      destroy-on-close
    >
      <div v-if="currentBook" class="book-detail">
        <div class="detail-header">
          <el-image
            :src="currentBook.cover"
            :alt="currentBook.title"
            class="detail-cover"
            fit="cover"
          >
            <template #error>
              <div class="cover-error-large">
                <el-icon :size="48"><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          <div class="detail-info">
            <h2 class="detail-title">{{ currentBook.title }}</h2>
            <p class="detail-author">作者：{{ currentBook.author }}</p>
            <div class="detail-meta">
              <el-tag type="info">{{ currentBook.categoryName }}</el-tag>
              <el-tag :type="currentBook.status === 1 ? 'success' : 'info'">
                {{ currentBook.status === 1 ? '上架中' : '已下架' }}
              </el-tag>
            </div>
            <p class="detail-isbn">ISBN：{{ currentBook.isbn }}</p>
          </div>
        </div>
        <el-divider />
        <div class="detail-stats">
          <div class="stat-item">
            <span class="stat-label">阅读次数</span>
            <span class="stat-number">{{ currentBook.readCount?.toLocaleString() }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">添加时间</span>
            <span class="stat-text">{{ currentBook.createdAt }}</span>
          </div>
        </div>
        <el-divider />
        <div class="detail-description">
          <h4>图书简介</h4>
          <p>{{ currentBook.description || '暂无简介' }}</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEdit(currentBook); detailDialogVisible = false">编辑</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.books-page {
  min-height: 100%;
}

/* 页面标题 */
.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 8px 0;
}

.page-description {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

/* 工具栏卡片 */
.toolbar-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.search-area {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  flex: 1;
  min-width: 0;
}

.search-input {
  width: 280px;
  max-width: 100%;
}

.filter-select {
  width: 130px;
}

.action-area {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

/* 统计卡片 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
}

.stat-icon.active {
  background: linear-gradient(135deg, #10b981, #059669);
}

.stat-icon.inactive {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
  margin: 0 0 4px 0;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #111827;
  margin: 0;
}

/* 表格容器 */
.table-container {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  min-width: 0;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
}

.book-cover {
  width: 40px;
  height: 56px;
  border-radius: 4px;
  display: block;
}

.cover-error {
  width: 40px;
  height: 56px;
  background: #f3f4f6;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
}

.read-count {
  color: #10b981;
  font-weight: 600;
}

/* 分页 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 弹窗 - 分栏布局 */
.book-dialog :deep(.el-dialog__body) {
  padding-top: 10px;
  padding-bottom: 10px;
}

.dialog-section {
  background: #f9fafb;
  border-radius: 10px;
  padding: 16px 20px;
  margin-bottom: 16px;
}

.dialog-section-title {
  font-size: 15px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 16px 0;
  padding-left: 10px;
  border-left: 3px solid #10b981;
}

.dialog-section :deep(.el-form-item) {
  margin-bottom: 16px;
}

/* 封面上传 */
.cover-uploader-area {
  width: 100%;
}

.cover-uploader :deep(.el-upload) {
  width: 100%;
}

.cover-placeholder {
  width: 100%;
  height: 160px;
  border: 2px dashed #d1d5db;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #6b7280;
  cursor: pointer;
  transition: border-color 0.2s, background-color 0.2s;
}

.cover-placeholder:hover {
  border-color: #10b981;
  background-color: #f0fdf4;
}

.upload-icon {
  color: #10b981;
}

.upload-text {
  font-size: 14px;
  font-weight: 500;
}

.upload-tip {
  font-size: 12px;
  color: #9ca3af;
}

.cover-preview {
  position: relative;
  width: 100%;
  height: 160px;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid #e5e7eb;
}

.cover-preview-image {
  width: 100%;
  height: 100%;
  display: block;
}

.cover-actions {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.cover-preview:hover .cover-actions {
  opacity: 1;
}

.cover-actions :deep(.el-button) {
  color: white;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 图书详情 */
.book-detail {
  padding: 10px 0;
}

.detail-header {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.detail-cover {
  width: 120px;
  height: 160px;
  border-radius: 8px;
  flex-shrink: 0;
  object-fit: cover;
}

.cover-error-large {
  width: 120px;
  height: 160px;
  background: #f3f4f6;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
}

.detail-info {
  flex: 1;
  min-width: 200px;
}

.detail-title {
  font-size: 20px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 8px 0;
}

.detail-author {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 12px 0;
}

.detail-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.detail-isbn {
  font-size: 13px;
  color: #9ca3af;
  margin: 0;
}

.detail-stats {
  display: flex;
  gap: 40px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-item .stat-label {
  font-size: 12px;
  color: #9ca3af;
}

.stat-number {
  font-size: 18px;
  font-weight: 700;
  color: #10b981;
}

.stat-text {
  font-size: 14px;
  color: #374151;
}

.detail-description h4 {
  font-size: 14px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 8px 0;
}

.detail-description p {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0;
}

/* 响应式 */
@media (max-width: 1024px) {
  .toolbar {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-area,
  .action-area {
    width: 100%;
  }

  .action-area {
    justify-content: flex-start;
  }
}

@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }

  .search-input,
  .filter-select {
    width: 100%;
  }

  .detail-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .detail-meta {
    justify-content: center;
  }

  .book-dialog :deep(.el-dialog) {
    width: 92vw !important;
  }

  .dialog-section {
    padding: 12px 14px;
  }
}
</style>
