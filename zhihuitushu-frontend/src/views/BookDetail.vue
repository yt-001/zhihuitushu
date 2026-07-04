<template>
  <div class="book-detail-page">
    <header class="header">
      <div class="header-content">
        <div class="back-btn" @click="$router.back()">
          <ArrowLeft class="back-icon" />
          <span>返回</span>
        </div>
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
      <div class="book-info">
        <div class="book-cover-wrapper">
          <div class="book-cover">
            <img :src="book.cover" :alt="book.title" />
          </div>
          <div class="book-actions">
            <el-button type="primary" class="read-btn">
              <Notebook class="btn-icon" />
              立即阅读
            </el-button>
            <el-button class="add-btn" @click="addToLibrary">
              <Plus class="btn-icon" />
              加入书架
            </el-button>
          </div>
        </div>

        <div class="book-details">
          <h1 class="book-title">{{ book.title }}</h1>
          <p class="book-author">作者：{{ book.author }}</p>
          <div class="book-rating">
            <Star v-for="i in 5" :key="i" :class="{ filled: i <= Math.floor(book.rating) }" />
            <span class="rating-value">{{ book.rating }}</span>
            <span class="rating-count">({{ book.reviewCount }}人评价)</span>
          </div>
          <div class="book-meta">
            <span class="meta-item">{{ book.category }}</span>
            <span class="meta-item">{{ book.language }}</span>
            <span class="meta-item">{{ book.pages }}页</span>
            <span class="meta-item">{{ book.publisher }}</span>
          </div>
          <div class="book-intro">
            <h3 class="intro-title">内容简介</h3>
            <p class="intro-content">{{ book.intro }}</p>
          </div>
          <div class="book-tags">
            <span v-for="tag in book.tags" :key="tag" class="tag">{{ tag }}</span>
          </div>
        </div>
      </div>

      <div class="related-section">
        <h2 class="section-title">相关推荐</h2>
        <div class="related-books">
          <div class="related-book" v-for="item in relatedBooks" :key="item.id" @click="$router.push(`/book/${item.id}`)">
            <div class="related-cover">
              <img :src="item.cover" :alt="item.title" />
            </div>
            <h4 class="related-title">{{ item.title }}</h4>
            <p class="related-author">{{ item.author }}</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Notebook, ArrowLeft, Star, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const book = ref({
  id: 1,
  title: '百年孤独',
  author: '加西亚·马尔克斯',
  cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20magical%20realism%20One%20Hundred%20Years%20of%20Solitude&image_size=square',
  rating: 5.0,
  reviewCount: 12580,
  category: '文学小说',
  language: '中文',
  pages: 360,
  publisher: '南海出版公司',
  intro: '《百年孤独》是魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的传奇故事，以及加勒比海沿岸小镇马孔多的百年兴衰，反映了拉丁美洲一个世纪以来风云变幻的历史。作品融入神话传说、民间故事、宗教典故等神秘因素，巧妙地糅合了现实与虚幻，展现出一个瑰丽的想象世界，成为20世纪最重要的经典文学巨著之一。',
  tags: ['魔幻现实主义', '诺贝尔文学奖', '经典名著', '拉丁美洲'],
})

const relatedBooks = ref([
  { id: 2, title: '霍乱时期的爱情', author: '加西亚·马尔克斯', cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20love%20story&image_size=square' },
  { id: 3, title: '活着', author: '余华', cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20Chinese%20literature&image_size=square' },
  { id: 4, title: '三体', author: '刘慈欣', cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20science%20fiction&image_size=square' },
])

const addToLibrary = () => {
  ElMessage.success('已加入书架')
}
</script>

<style scoped>
.book-detail-page {
  min-height: 100vh;
  background: #FFFFFF;
}

.header {
  background: #FFFFFF;
  border-bottom: 1px solid #E2E8F0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
}

.back-btn:hover {
  color: #92400E;
}

.back-icon {
  width: 16px;
  height: 16px;
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

.book-info {
  display: flex;
  gap: 40px;
}

.book-cover-wrapper {
  width: 280px;
  flex-shrink: 0;
}

.book-cover {
  width: 100%;
  padding-top: 140%;
  background: #F1F5F9;
  border-radius: 12px;
  position: relative;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.book-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}

.book-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 24px;
}

.read-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.add-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #92400E;
  border-color: #92400E;
}

.btn-icon {
  margin-right: 8px;
}

.book-details {
  flex: 1;
}

.book-title {
  font-size: 32px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 12px;
}

.book-author {
  font-size: 18px;
  color: #64748B;
  margin-bottom: 16px;
}

.book-rating {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
}

.book-rating .Star {
  width: 20px;
  height: 20px;
  color: #E2E8F0;
}

.book-rating .Star.filled {
  color: #F59E0B;
}

.rating-value {
  font-size: 20px;
  font-weight: 600;
  color: #0F172A;
}

.rating-count {
  font-size: 14px;
  color: #64748B;
}

.book-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 24px;
}

.meta-item {
  font-size: 14px;
  color: #64748B;
  background: #F1F5F9;
  padding: 6px 12px;
  border-radius: 20px;
}

.book-intro {
  margin-bottom: 24px;
}

.intro-title {
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 12px;
}

.intro-content {
  font-size: 15px;
  color: #475569;
  line-height: 1.8;
  text-align: justify;
}

.book-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  font-size: 13px;
  color: #92400E;
  background: #FEF3C7;
  padding: 4px 12px;
  border-radius: 4px;
}

.related-section {
  margin-top: 60px;
}

.section-title {
  font-size: 24px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 24px;
}

.related-books {
  display: flex;
  gap: 24px;
  overflow-x: auto;
  padding-bottom: 12px;
}

.related-book {
  width: 150px;
  flex-shrink: 0;
  cursor: pointer;
}

.related-cover {
  width: 100%;
  padding-top: 140%;
  background: #F1F5F9;
  border-radius: 8px;
  position: relative;
  margin-bottom: 12px;
}

.related-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.related-title {
  font-size: 14px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.related-author {
  font-size: 12px;
  color: #64748B;
}

@media (max-width: 768px) {
  .book-info {
    flex-direction: column;
  }

  .book-cover-wrapper {
    width: 200px;
    margin: 0 auto;
  }

  .main-content {
    padding: 24px 16px;
  }

  .book-title {
    font-size: 24px;
  }
}
</style>