<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { TrendCharts } from '@element-plus/icons-vue'
import { getStatistics } from '@/api/admin.js'

const loading = ref(false)
const stats = ref({
  totalBooks: 12580,
  totalUsers: 32450,
  totalReadings: 156780,
  todayReadings: 1234,
  weeklyReadings: 8765,
  monthlyReadings: 45678
})

const trendData = ref([
  { date: '周一', count: 1200 },
  { date: '周二', count: 1450 },
  { date: '周三', count: 980 },
  { date: '周四', count: 1680 },
  { date: '周五', count: 2100 },
  { date: '周六', count: 2800 },
  { date: '周日', count: 2200 }
])

const popularBooks = ref([
  { title: '活着', count: 12580 },
  { title: '三体', count: 10234 },
  { title: '小王子', count: 9876 },
  { title: '围城', count: 8976 },
  { title: '明朝那些事儿', count: 8765 }
])

const activeUsers = ref([
  { username: '赵六', count: 234 },
  { username: '孙八', count: 178 },
  { username: '张三', count: 156 },
  { username: '李四', count: 89 },
  { username: '周九', count: 67 }
])

async function fetchData() {
  loading.value = true
  try {
    const res = await getStatistics()
    stats.value = res.data || stats.value
  } catch {}
  loading.value = false
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="statistics-page" v-loading="loading">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #10b981, #059669);"><el-icon :size="32"><TrendCharts /></el-icon></div>
        <div class="stat-info"><p class="stat-label">今日阅读</p><p class="stat-value">{{ stats.todayReadings.toLocaleString() }}</p></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #3b82f6, #1d4ed8);"><el-icon :size="32"><TrendCharts /></el-icon></div>
        <div class="stat-info"><p class="stat-label">本周阅读</p><p class="stat-value">{{ stats.weeklyReadings.toLocaleString() }}</p></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #f59e0b, #d97706);"><el-icon :size="32"><TrendCharts /></el-icon></div>
        <div class="stat-info"><p class="stat-label">本月阅读</p><p class="stat-value">{{ stats.monthlyReadings.toLocaleString() }}</p></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #8b5cf6, #7c3aed);"><el-icon :size="32"><TrendCharts /></el-icon></div>
        <div class="stat-info"><p class="stat-label">累计阅读</p><p class="stat-value">{{ stats.totalReadings.toLocaleString() }}</p></div>
      </div>
    </div>

    <div class="charts-grid">
      <div class="chart-card">
        <h3>本周阅读趋势</h3>
        <div class="chart-placeholder">
          <div class="bar-chart">
            <div v-for="item in trendData" :key="item.date" class="bar-item">
              <div class="bar" :style="{ height: (item.count / 3000 * 100) + '%' }"></div>
              <span class="label">{{ item.date }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="chart-card">
        <h3>热门图书排行</h3>
        <ul class="rank-list">
          <li v-for="(book, index) in popularBooks" :key="book.title" class="rank-item">
            <span class="rank" :class="{ top: index < 3 }">{{ index + 1 }}</span>
            <span class="name">{{ book.title }}</span>
            <span class="count">{{ book.count.toLocaleString() }} 次</span>
          </li>
        </ul>
      </div>

      <div class="chart-card">
        <h3>活跃用户排行</h3>
        <ul class="rank-list">
          <li v-for="(user, index) in activeUsers" :key="user.username" class="rank-item">
            <span class="rank" :class="{ top: index < 3 }">{{ index + 1 }}</span>
            <span class="name">{{ user.username }}</span>
            <span class="count">{{ user.count }} 次</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.statistics-page {
  min-height: 100%;
}

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
  margin: 0;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chart-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 20px 0;
}

.chart-placeholder {
  height: 200px;
}

.bar-chart {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  height: 100%;
  padding-bottom: 30px;
}

.bar-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bar {
  width: 30px;
  max-width: 60px;
  background: linear-gradient(180deg, #10b981, #059669);
  border-radius: 4px 4px 0 0;
  transition: height 0.3s;
}

.label {
  font-size: 12px;
  color: #6b7280;
  margin-top: 8px;
}

.rank-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.rank-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.rank-item:last-child {
  border-bottom: none;
}

.rank {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  background: #f0f0f0;
  border-radius: 6px;
}

.rank.top {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
}

.name {
  flex: 1;
  margin-left: 12px;
  font-weight: 500;
  color: #374151;
}

.count {
  color: #10b981;
  font-weight: 600;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
