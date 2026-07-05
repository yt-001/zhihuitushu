<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  House, User, Document, Setting, TrendCharts, DataAnalysis
} from '@element-plus/icons-vue'
import { getStatistics, getPopularBooks } from '@/api/admin.js'

const router = useRouter()

// 统计数据
const stats = ref({
  totalBooks: 0,
  totalUsers: 0,
  totalReadings: 0,
  todayReadings: 0,
  newUsersToday: 0,
  activeUsers: 0
})

const popularBooks = ref([])
const loading = ref(false)

// 模拟数据
const mockStats = {
  totalBooks: 12580,
  totalUsers: 32450,
  totalReadings: 156780,
  todayReadings: 1234,
  newUsersToday: 89,
  activeUsers: 2456
}

const mockPopularBooks = [
  { id: 1, title: '活着', author: '余华', readCount: 12580, cover: 'https://picsum.photos/seed/book1/60/80' },
  { id: 2, title: '三体', author: '刘慈欣', readCount: 10234, cover: 'https://picsum.photos/seed/book2/60/80' },
  { id: 3, title: '围城', author: '钱钟书', readCount: 8976, cover: 'https://picsum.photos/seed/book3/60/80' },
  { id: 4, title: '红楼梦', author: '曹雪芹', readCount: 7654, cover: 'https://picsum.photos/seed/book4/60/80' },
  { id: 5, title: '百年孤独', author: '加西亚·马尔克斯', readCount: 6543, cover: 'https://picsum.photos/seed/book5/60/80' },
]

async function fetchStatistics() {
  loading.value = true
  try {
    // 尝试获取真实数据，失败则使用模拟数据
    const res = await getStatistics()
    stats.value = res.data || mockStats
  } catch {
    stats.value = mockStats
  }

  try {
    const res = await getPopularBooks(5)
    popularBooks.value = res.data || mockPopularBooks
  } catch {
    popularBooks.value = mockPopularBooks
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStatistics()
})
</script>

<template>
  <div class="dashboard-page" v-loading="loading">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">首页概览</h1>
      <p class="page-description">欢迎使用智慧图书管理后台，查看平台数据概览</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card books">
        <div class="stat-icon">
          <el-icon :size="32"><Document /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">图书总数</p>
          <p class="stat-value">{{ stats.totalBooks?.toLocaleString() || '0' }}</p>
          <p class="stat-trend up">
            <span>↑ 5.2%</span>
            <span>较上周</span>
          </p>
        </div>
      </div>

      <div class="stat-card users">
        <div class="stat-icon">
          <el-icon :size="32"><User /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">用户总数</p>
          <p class="stat-value">{{ stats.totalUsers?.toLocaleString() || '0' }}</p>
          <p class="stat-trend up">
            <span>↑ {{ stats.newUsersToday }}人</span>
            <span>今日新增</span>
          </p>
        </div>
      </div>

      <div class="stat-card readings">
        <div class="stat-icon">
          <el-icon :size="32"><TrendCharts /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">阅读次数</p>
          <p class="stat-value">{{ stats.totalReadings?.toLocaleString() || '0' }}</p>
          <p class="stat-trend up">
            <span>↑ {{ stats.todayReadings?.toLocaleString() || '0' }}次</span>
            <span>今日阅读</span>
          </p>
        </div>
      </div>

      <div class="stat-card active">
        <div class="stat-icon">
          <el-icon :size="32"><DataAnalysis /></el-icon>
        </div>
        <div class="stat-info">
          <p class="stat-label">活跃用户</p>
          <p class="stat-value">{{ stats.activeUsers?.toLocaleString() || '0' }}</p>
          <p class="stat-trend up">
            <span>↑ 12%</span>
            <span>本周活跃</span>
          </p>
        </div>
      </div>
    </div>

    <!-- 数据详情区域 -->
    <div class="data-section">
      <!-- 热门图书 -->
      <div class="data-card">
        <div class="card-header">
          <h3>热门图书排行</h3>
          <el-button text type="primary" @click="router.push('/admin/books')">
            查看全部
          </el-button>
        </div>
        <el-table :data="popularBooks" stripe style="width: 100%">
          <el-table-column type="index" label="#" width="50" />
          <el-table-column label="封面" width="80">
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
                    <el-icon><Document /></el-icon>
                  </div>
                </template>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="书名" />
          <el-table-column prop="author" label="作者" />
          <el-table-column prop="readCount" label="阅读次数">
            <template #default="{ row }">
              <span class="read-count">{{ row.readCount?.toLocaleString() || '0' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 快捷操作 -->
      <div class="data-card quick-actions">
        <div class="card-header">
          <h3>快捷操作</h3>
        </div>
        <div class="actions-grid">
          <div class="action-item" @click="router.push('/admin/books')">
            <div class="action-icon books">
              <el-icon :size="28"><Document /></el-icon>
            </div>
            <span class="action-label">添加图书</span>
          </div>
          <div class="action-item" @click="router.push('/admin/users')">
            <div class="action-icon users">
              <el-icon :size="28"><User /></el-icon>
            </div>
            <span class="action-label">用户管理</span>
          </div>
          <div class="action-item" @click="router.push('/admin/statistics')">
            <div class="action-icon stats">
              <el-icon :size="28"><DataAnalysis /></el-icon>
            </div>
            <span class="action-label">数据报表</span>
          </div>
          <div class="action-item" @click="router.push('/admin/settings')">
            <div class="action-icon settings">
              <el-icon :size="28"><Setting /></el-icon>
            </div>
            <span class="action-label">系统设置</span>
          </div>
        </div>

        <!-- 今日动态 -->
        <div class="today-dynamic">
          <h4>今日动态</h4>
          <div class="dynamic-list">
            <div class="dynamic-item">
              <el-icon class="dynamic-icon" color="#10b981"><User /></el-icon>
              <span class="dynamic-text">新增用户 {{ stats.newUsersToday }} 人</span>
            </div>
            <div class="dynamic-item">
              <el-icon class="dynamic-icon" color="#3b82f6"><Document /></el-icon>
              <span class="dynamic-text">新增图书 15 本</span>
            </div>
            <div class="dynamic-item">
              <el-icon class="dynamic-icon" color="#f59e0b"><TrendCharts /></el-icon>
              <span class="dynamic-text">阅读次数 {{ stats.todayReadings?.toLocaleString() || '0' }} 次</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 系统公告 -->
    <div class="announcement-card">
      <div class="card-header">
        <h3>系统公告</h3>
      </div>
      <div class="announcement-list">
        <div class="announcement-item">
          <el-tag type="warning" size="small">重要</el-tag>
          <div class="announcement-content">
            <h5>系统维护通知</h5>
            <p>系统将于本周日凌晨2:00-4:00进行例行维护，届时可能无法访问。</p>
            <span class="announcement-time">2024-01-15</span>
          </div>
        </div>
        <div class="announcement-item">
          <el-tag type="success" size="small">新功能</el-tag>
          <div class="announcement-content">
            <h5>AI读书助手上线</h5>
            <p>新增AI读书助手功能，支持智能推荐和阅读辅导。</p>
            <span class="announcement-time">2024-01-12</span>
          </div>
        </div>
        <div class="announcement-item">
          <el-tag size="small">通知</el-tag>
          <div class="announcement-content">
            <h5>春节放假安排</h5>
            <p>春节期间客服服务时间有所调整，请提前安排相关工作。</p>
            <span class="announcement-time">2024-01-10</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard-page {
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

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s, box-shadow 0.2s;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100px;
  background: currentColor;
  opacity: 0.05;
  border-radius: 50%;
  transform: translate(30%, -30%);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-card.books .stat-icon {
  background: linear-gradient(135deg, #10b981, #059669);
}

.stat-card.users .stat-icon {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
}

.stat-card.readings .stat-icon {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.stat-card.active .stat-icon {
  background: linear-gradient(135deg, #8b5cf6, #7c3aed);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 4px 0;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 6px 0;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  gap: 6px;
  align-items: center;
}

.stat-trend.up {
  color: #10b981;
}

.stat-trend.down {
  color: #ef4444;
}

/* 数据卡片区域 */
.data-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.data-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.card-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #111827;
  margin: 0;
}

.book-cover {
  width: 40px;
  height: 56px;
  object-fit: cover;
  border-radius: 4px;
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

/* 快捷操作 */
.quick-actions {
  height: fit-content;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  gap: 8px;
}

.action-item:hover {
  background: #ecfdf5;
  transform: translateY(-2px);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.action-icon.books {
  background: linear-gradient(135deg, #10b981, #059669);
}

.action-icon.users {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
}

.action-icon.stats {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.action-icon.settings {
  background: linear-gradient(135deg, #8b5cf6, #7c3aed);
}

.action-label {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

/* 今日动态 */
.today-dynamic {
  border-top: 1px solid #e5e7eb;
  padding-top: 16px;
}

.today-dynamic h4 {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 12px 0;
}

.dynamic-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.dynamic-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #f9fafb;
  border-radius: 8px;
}

.dynamic-icon {
  font-size: 16px;
}

.dynamic-text {
  font-size: 13px;
  color: #6b7280;
}

/* 系统公告 */
.announcement-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.announcement-item {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.announcement-item:hover {
  background: #f3f4f6;
}

.announcement-content {
  flex: 1;
}

.announcement-content h5 {
  font-size: 14px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 6px 0;
}

.announcement-content p {
  font-size: 13px;
  color: #6b7280;
  margin: 0 0 6px 0;
  line-height: 1.4;
}

.announcement-time {
  font-size: 12px;
  color: #9ca3af;
}

/* 响应式 */
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .data-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>