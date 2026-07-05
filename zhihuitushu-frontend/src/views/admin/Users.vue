<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getUserList, updateUserStatus, deleteUser } from '@/api/admin.js'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchQuery = ref('')
const statusFilter = ref('')

// 模拟用户数据
const mockUsers = [
  { id: 1, username: '张三', email: 'zhangsan@example.com', phone: '13800138001', status: 'active', readingCount: 156, createdAt: '2024-01-15' },
  { id: 2, username: '李四', email: 'lisi@example.com', phone: '13800138002', status: 'active', readingCount: 89, createdAt: '2024-01-12' },
  { id: 3, username: '王五', email: 'wangwu@example.com', phone: '13800138003', status: 'inactive', readingCount: 45, createdAt: '2024-01-10' },
  { id: 4, username: '赵六', email: 'zhaoliu@example.com', phone: '13800138004', status: 'active', readingCount: 234, createdAt: '2024-01-08' },
  { id: 5, username: '钱七', email: 'qianqi@example.com', phone: '13800138005', status: 'banned', readingCount: 12, createdAt: '2024-01-05' },
  { id: 6, username: '孙八', email: 'sunba@example.com', phone: '13800138006', status: 'active', readingCount: 178, createdAt: '2024-01-03' },
  { id: 7, username: '周九', email: 'zhoujiu@example.com', phone: '13800138007', status: 'active', readingCount: 67, createdAt: '2024-01-01' },
  { id: 8, username: '吴十', email: 'wushi@example.com', phone: '13800138008', status: 'inactive', readingCount: 23, createdAt: '2023-12-28' },
]

async function fetchUsers() {
  loading.value = true
  try {
    const res = await getUserList({
      page: currentPage.value,
      pageSize: pageSize.value,
      search: searchQuery.value,
      status: statusFilter.value
    })
    tableData.value = res.data?.list || mockUsers
    total.value = res.data?.total || mockUsers.length
  } catch {
    tableData.value = mockUsers
    total.value = mockUsers.length
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  currentPage.value = 1
  fetchUsers()
}

function handleReset() {
  searchQuery.value = ''
  statusFilter.value = ''
  currentPage.value = 1
  fetchUsers()
}

function handlePageChange(page) {
  currentPage.value = page
  fetchUsers()
}

function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
  fetchUsers()
}

async function handleStatusChange(row, status) {
  try {
    await updateUserStatus(row.id, status)
    row.status = status
    ElMessage.success('状态更新成功')
  } catch {
    ElMessage.error('状态更新失败')
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除用户 "${row.username}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<template>
  <div class="users-page">
    <div class="toolbar">
      <div class="search-area">
        <el-input v-model="searchQuery" placeholder="搜索用户名、邮箱、手机号" :prefix-icon="Search" clearable style="width: 280px" @keyup.enter="handleSearch" />
        <el-select v-model="statusFilter" placeholder="选择状态" clearable style="width: 140px" @change="handleSearch">
          <el-option label="全部" value="" />
          <el-option label="正常" value="active" />
          <el-option label="未激活" value="inactive" />
          <el-option label="已封禁" value="banned" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
    </div>

    <div class="table-container">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : row.status === 'banned' ? 'danger' : 'info'" size="small">
              {{ row.status === 'active' ? '正常' : row.status === 'banned' ? '已封禁' : '未激活' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="readingCount" label="阅读数" width="80">
          <template #default="{ row }">
            <span class="read-count">{{ row.readingCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="110" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" size="small" @click="handleStatusChange(row, row.status === 'active' ? 'inactive' : 'active')">
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.users-page {
  min-height: 100%;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 16px;
}

.search-area {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.read-count {
  color: #10b981;
  font-weight: 600;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-area {
    width: 100%;
  }
}
</style>
