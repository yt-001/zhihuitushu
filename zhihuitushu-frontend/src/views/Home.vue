<template>
  <div class="home-page">
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <Notebook class="logo-icon" />
          <span class="logo-text">智慧图书</span>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-item active">首页</router-link>
          <router-link to="/books" class="nav-item">图书</router-link>
          <router-link to="/library" class="nav-item">我的书架</router-link>
          <router-link to="/profile" class="nav-item">个人中心</router-link>
        </nav>
        <div class="header-actions">
          <el-button class="login-btn" @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" class="register-btn" @click="$router.push('/register')">注册</el-button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <section class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">开启智慧阅读之旅</h1>
          <p class="hero-subtitle">AI智能推荐，让阅读更高效。海量图书，随时随地畅享阅读。</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" class="primary-btn" @click="$router.push('/register')">
              立即开始
            </el-button>
            <el-button size="large" class="secondary-btn" @click="$router.push('/books')">
              浏览图书
            </el-button>
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-value">10万+</span>
              <span class="stat-label">正版图书</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">100万+</span>
              <span class="stat-label">注册用户</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">98%</span>
              <span class="stat-label">用户好评</span>
            </div>
          </div>
        </div>
        <div class="hero-image">
          <div class="book-stack">
            <div class="book book-1"></div>
            <div class="book book-2"></div>
            <div class="book book-3"></div>
          </div>
        </div>
      </section>

      <section class="features-section">
        <h2 class="section-title">核心功能</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <Light class="feature-icon" />
            </div>
            <h3 class="feature-title">AI智能推荐</h3>
            <p class="feature-description">基于您的阅读偏好，精准推荐优质图书，让每一次阅读都有收获。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <Headset class="feature-icon" />
            </div>
            <h3 class="feature-title">语音阅读</h3>
            <p class="feature-description">支持多种语音音色，随时随地听书，解放您的双眼。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <View class="feature-icon" />
            </div>
            <h3 class="feature-title">无障碍阅读</h3>
            <p class="feature-description">大字体、高对比度、语音导航，关爱每一位读者。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <Cloudy class="feature-icon" />
            </div>
            <h3 class="feature-title">云端同步</h3>
            <p class="feature-description">阅读进度自动同步，多设备无缝切换，随时随地继续阅读。</p>
          </div>
        </div>
      </section>

      <section class="books-section">
        <div class="section-header">
          <h2 class="section-title">热门推荐</h2>
          <router-link to="/books" class="view-all">查看全部 →</router-link>
        </div>
        <div class="books-grid">
          <div class="book-card" v-for="book in books" :key="book.id" @click="$router.push(`/book/${book.id}`)">
            <div class="book-cover">
              <img :src="book.cover" :alt="book.title" />
            </div>
            <h3 class="book-title">{{ book.title }}</h3>
            <p class="book-author">{{ book.author }}</p>
            <div class="book-rating">
              <Star v-for="i in 5" :key="i" :class="{ filled: i <= book.rating }" />
              <span class="rating-text">{{ book.rating }}</span>
            </div>
          </div>
        </div>
      </section>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <div class="footer-logo">
          <Notebook class="logo-icon" />
          <span>智慧图书阅读平台</span>
        </div>
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">用户协议</a>
          <a href="#">隐私政策</a>
          <a href="#">联系我们</a>
        </div>
        <p class="footer-copyright">© 2026 智慧图书阅读平台. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Notebook, Star, Headset, View, Cloudy } from '@element-plus/icons-vue'

const books = ref([
  {
    id: 1,
    title: '百年孤独',
    author: '加西亚·马尔克斯',
    cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20of%20One%20Hundred%20Years%20of%20Solitude%20magical%20realism&image_size=square',
    rating: 5,
  },
  {
    id: 2,
    title: '三体',
    author: '刘慈欣',
    cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20of%20The%20Three%20Body%20Problem%20science%20fiction&image_size=square',
    rating: 5,
  },
  {
    id: 3,
    title: '活着',
    author: '余华',
    cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20of%20To%20Live%20Chinese%20literature&image_size=square',
    rating: 5,
  },
  {
    id: 4,
    title: '人类简史',
    author: '尤瓦尔·赫拉利',
    cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20of%20Sapiens%20history%20book&image_size=square',
    rating: 4,
  },
])
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #FFFFFF;
}

.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #F1F5F9;
}

.header-content {
  max-width: 1440px;
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

.nav {
  display: flex;
  gap: 32px;
}

.nav-item {
  font-size: 15px;
  color: #64748B;
  text-decoration: none;
  transition: color 0.2s;
}

.nav-item:hover,
.nav-item.active {
  color: #92400E;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.login-btn {
  color: #92400E;
  border-color: #92400E;
}

.register-btn {
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.main-content {
  padding: 0 40px;
}

.hero-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 80px 0;
  max-width: 1440px;
  margin: 0 auto;
}

.hero-content {
  flex: 1;
  padding-right: 60px;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  color: #0F172A;
  margin-bottom: 16px;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 18px;
  color: #64748B;
  margin-bottom: 32px;
  line-height: 1.6;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 48px;
}

.primary-btn {
  width: 160px;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.secondary-btn {
  width: 160px;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #92400E;
  border-color: #92400E;
}

.hero-stats {
  display: flex;
  gap: 48px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #0F172A;
}

.stat-label {
  font-size: 14px;
  color: #64748B;
}

.hero-image {
  flex: 1;
  display: flex;
  justify-content: center;
}

.book-stack {
  position: relative;
  width: 300px;
  height: 400px;
}

.book {
  position: absolute;
  width: 200px;
  height: 280px;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

.book-1 {
  background: linear-gradient(135deg, #92400E 0%, #B45309 100%);
  transform: rotate(-15deg);
  top: 60px;
  left: 20px;
}

.book-2 {
  background: linear-gradient(135deg, #D97706 0%, #F59E0B 100%);
  transform: rotate(-5deg);
  top: 40px;
  left: 60px;
}

.book-3 {
  background: linear-gradient(135deg, #78716C 0%, #A8A29E 100%);
  transform: rotate(5deg);
  top: 80px;
  left: 100px;
}

.features-section {
  padding: 80px 0;
  background: #FFFBEB;
}

.features-section .section-title {
  text-align: center;
  font-size: 32px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 48px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  max-width: 1440px;
  margin: 0 auto;
}

.feature-card {
  background: #FFFFFF;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.feature-icon-wrapper {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.feature-icon {
  width: 32px;
  height: 32px;
  color: #92400E;
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 8px;
}

.feature-description {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
}

.books-section {
  padding: 80px 0;
  max-width: 1440px;
  margin: 0 auto;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #0F172A;
}

.view-all {
  font-size: 14px;
  color: #92400E;
  text-decoration: none;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.book-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.book-card:hover {
  transform: translateY(-8px);
}

.book-cover {
  width: 100%;
  padding-top: 140%;
  background: #F1F5F9;
  border-radius: 8px;
  margin-bottom: 12px;
  overflow: hidden;
  position: relative;
}

.book-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.book-title {
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-author {
  font-size: 14px;
  color: #64748B;
  margin-bottom: 8px;
}

.book-rating {
  display: flex;
  align-items: center;
  gap: 4px;
}

.book-rating .Star {
  width: 16px;
  height: 16px;
  color: #E2E8F0;
}

.book-rating .Star.filled {
  color: #F59E0B;
}

.rating-text {
  font-size: 14px;
  color: #64748B;
  margin-left: 4px;
}

.footer {
  background: #0F172A;
  color: #94A3B8;
  padding: 48px 40px;
}

.footer-content {
  max-width: 1440px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #FFFFFF;
}

.footer-logo .logo-icon {
  color: #D97706;
}

.footer-links {
  display: flex;
  gap: 24px;
}

.footer-links a {
  font-size: 14px;
  color: #94A3B8;
  text-decoration: none;
}

.footer-links a:hover {
  color: #FFFFFF;
}

.footer-copyright {
  font-size: 14px;
}

@media (max-width: 1024px) {
  .features-grid,
  .books-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .hero-section {
    flex-direction: column;
    gap: 48px;
  }

  .hero-content {
    padding-right: 0;
    text-align: center;
  }

  .hero-actions {
    justify-content: center;
  }

  .hero-stats {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 16px 20px;
  }

  .nav {
    display: none;
  }

  .hero-title {
    font-size: 32px;
  }

  .features-grid,
  .books-grid {
    grid-template-columns: 1fr;
  }
}
</style>