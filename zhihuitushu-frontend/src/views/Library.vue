<template>
  <div class="library-page">
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <Notebook class="logo-icon" />
          <span class="logo-text">智慧图书</span>
        </div>
        <div class="header-actions">
          <router-link to="/login" class="login-link">登录</router-link>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="page-header">
        <h1 class="page-title">我的书架</h1>
        <div class="filter-tabs">
          <span :class="['tab', { active: activeTab === 'reading' }]" @click="activeTab = 'reading'">
            <Notebook class="tab-icon" />
            阅读中
          </span>
          <span :class="['tab', { active: activeTab === 'finished' }]" @click="activeTab = 'finished'">
            <CircleCheck class="tab-icon" />
            已完成
          </span>
          <span :class="['tab', { active: activeTab === 'wishlist' }]" @click="activeTab = 'wishlist'">
            <StarFilled class="tab-icon" />
            想读
          </span>
        </div>
      </div>

      <div class="books-list" v-if="activeTab === 'reading'">
        <div v-if="readingBooks.length === 0" class="empty-state">
          <Notebook class="empty-icon" />
          <p class="empty-text">暂无阅读中的图书</p>
          <el-button type="primary" @click="$router.push('/books')">去发现图书</el-button>
        </div>
        <div class="book-item" v-for="book in readingBooks" :key="book.id">
          <div class="book-cover">
            <img :src="book.cover" :alt="book.title" />
            <div class="progress-ring">
              <svg viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="45" fill="none" stroke="#E2E8F0" stroke-width="4" />
                <circle cx="50" cy="50" r="45" fill="none" stroke="#92400E" stroke-width="4" :stroke-dasharray="circumference" :stroke-dashoffset="getProgressOffset(book.progress)" stroke-linecap="round" />
              </svg>
              <span class="progress-text">{{ book.progress }}%</span>
            </div>
          </div>
          <div class="book-info">
            <h3 class="book-title">{{ book.title }}</h3>
            <p class="book-author">{{ book.author }}</p>
            <p class="book-progress">已阅读 {{ book.readPages }}/{{ book.totalPages }} 页</p>
            <p class="book-time">上次阅读：{{ book.lastReadTime }}</p>
          </div>
          <div class="book-actions">
            <el-button type="primary" size="small" @click="continueReading(book)">继续阅读</el-button>
            <el-button size="small" @click="removeBook(book)">移出书架</el-button>
          </div>
        </div>
      </div>

      <div class="books-grid" v-else>
        <div v-if="finishedBooks.length === 0" class="empty-state">
          <CircleCheck class="empty-icon" />
          <p class="empty-text">暂无已完成的图书</p>
        </div>
        <div class="book-card" v-for="book in finishedBooks" :key="book.id">
          <div class="book-cover">
            <img :src="book.cover" :alt="book.title" />
            <div class="finished-badge">已完成</div>
          </div>
          <h3 class="book-title">{{ book.title }}</h3>
          <p class="book-author">{{ book.author }}</p>
          <div class="read-time">阅读时长：{{ book.readTime }}</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Notebook, CircleCheck, StarFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('reading')
const circumference = 2 * Math.PI * 45

const readingBooks = ref([
  {
    id: 1,
    title: '百年孤独',
    author: '加西亚·马尔克斯',
    cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20magical%20realism&image_size=square',
    progress: 65,
    readPages: 234,
    totalPages: 360,
    lastReadTime: '2小时前',
  },
  {
    id: 2,
    title: '三体',
    author: '刘慈欣',
    cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20science%20fiction&image_size=square',
    progress: 30,
    readPages: 120,
    totalPages: 400,
    lastReadTime: '1天前',
  },
])

const finishedBooks = ref([
  { id: 3, title: '小王子', author: '圣埃克苏佩里', cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20little%20prince&image_size=square', readTime: '3小时' },
  { id: 4, title: '活着', author: '余华', cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20Chinese%20literature&image_size=square', readTime: '5小时' },
])

const getProgressOffset = (progress) => {
  return circumference - (progress / 100) * circumference
}

const continueReading = (book) => {
  ElMessage.success(`继续阅读《${book.title}》`)
}

const removeBook = (book) => {
  readingBooks.value = readingBooks.value.filter((b) => b.id !== book.id)
  ElMessage.success('已移出书架')
}
</script>

<style scoped>
.library-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.header {
  background: #FFFFFF;
  border-bottom: 1px solid #E2E8F0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.logo-icon {
  width: 32px;
  height: 32px;
  color: #92400E;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #0F172A;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.login-link {
  font-size: 14px;
  color: #92400E;
  text-decoration: none;
  font-weight: 500;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
}

.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 20px;
}

.filter-tabs {
  display: flex;
  gap: 8px;
  background: #FFFFFF;
  padding: 4px;
  border-radius: 12px;
}

.tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  color: #64748B;
  cursor: pointer;
  transition: all 0.2s;
}

.tab.active {
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  color: #FFFFFF;
}

.tab-icon {
  width: 16px;
  height: 16px;
}

.book-item {
  display: flex;
  align-items: center;
  gap: 20px;
  background: #FFFFFF;
  padding: 24px;
  border-radius: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.book-item .book-cover {
  width: 100px;
  height: 140px;
  position: relative;
  flex-shrink: 0;
}

.book-item .book-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.progress-ring {
  position: absolute;
  bottom: -8px;
  right: -8px;
  width: 48px;
  height: 48px;
  background: #FFFFFF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.progress-ring svg {
  width: 40px;
  height: 40px;
  transform: rotate(-90deg);
}

.progress-text {
  position: absolute;
  font-size: 10px;
  font-weight: 600;
  color: #92400E;
}

.book-info {
  flex: 1;
}

.book-item .book-title {
  font-size: 18px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 4px;
}

.book-item .book-author {
  font-size: 14px;
  color: #64748B;
  margin-bottom: 8px;
}

.book-progress,
.book-time {
  font-size: 13px;
  color: #94A3B8;
  margin-bottom: 4px;
}

.book-actions {
  display: flex;
  gap: 12px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80px 0;
}

.empty-icon {
  width: 80px;
  height: 80px;
  color: #CBD5E1;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
  color: #64748B;
  margin-bottom: 24px;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.book-card {
  background: #FFFFFF;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.book-card .book-cover {
  width: 100%;
  padding-top: 140%;
  background: #F1F5F9;
  position: relative;
}

.book-card .book-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.finished-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #10B981;
  color: #FFFFFF;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
}

.book-card .book-title {
  font-size: 15px;
  font-weight: 600;
  color: #0F172A;
  padding: 12px 12px 4px;
}

.book-card .book-author {
  font-size: 13px;
  color: #64748B;
  padding: 0 12px 8px;
}

.read-time {
  font-size: 12px;
  color: #94A3B8;
  padding: 0 12px 12px;
}

@media (max-width: 768px) {
  .books-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .book-item {
    flex-direction: column;
    text-align: center;
  }

  .book-actions {
    width: 100%;
    justify-content: center;
  }
}
</style>