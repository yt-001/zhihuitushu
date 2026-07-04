<template>
  <div class="books-page">
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <Notebook class="logo-icon" />
          <span class="logo-text">智慧图书</span>
        </div>
        <div class="search-bar">
          <el-input
            v-model="searchQuery"
            placeholder="搜索图书"
            prefix-icon="Search"
            @keyup.enter="handleSearch"
          />
        </div>
        <div class="header-actions">
          <router-link to="/login" class="login-link">登录</router-link>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="sidebar">
        <h3 class="sidebar-title">图书分类</h3>
        <div class="category-list">
          <div
            v-for="category in categories"
            :key="category.id"
            :class="['category-item', { active: selectedCategory === category.id }]"
            @click="selectedCategory = category.id"
          >
            <component :is="category.icon" class="category-icon" />
            <span>{{ category.name }}</span>
            <span class="category-count">{{ category.count }}</span>
          </div>
        </div>
      </div>

      <div class="content-area">
        <div class="filter-bar">
          <el-select v-model="sortBy" placeholder="排序" size="small">
            <el-option label="最新上架" value="newest" />
            <el-option label="最热阅读" value="hot" />
            <el-option label="评分最高" value="rating" />
          </el-select>
          <span class="result-count">共 {{ books.length }} 本图书</span>
        </div>

        <div class="books-grid">
          <div class="book-card" v-for="book in books" :key="book.id" @click="$router.push(`/book/${book.id}`)">
            <div class="book-cover">
              <img :src="book.cover" :alt="book.title" />
            </div>
            <h3 class="book-title">{{ book.title }}</h3>
            <p class="book-author">{{ book.author }}</p>
            <div class="book-meta">
              <span class="book-category">{{ book.category }}</span>
              <span class="book-rating">
                <Star class="rating-star" />
                {{ book.rating }}
              </span>
            </div>
          </div>
        </div>

        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="12"
            :total="100"
            layout="prev, pager, next"
          />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, markRaw } from 'vue'
import { Notebook, Search, Star, Reading, Collection, Document } from '@element-plus/icons-vue'

const searchQuery = ref('')
const selectedCategory = ref('all')
const sortBy = ref('hot')
const currentPage = ref(1)

const categories = ref([
  { id: 'all', name: '全部图书', count: 100000, icon: markRaw(Notebook) },
  { id: 'fiction', name: '文学小说', count: 25000, icon: markRaw(Collection) },
  { id: 'history', name: '历史传记', count: 15000, icon: markRaw(Reading) },
  { id: 'science', name: '科普读物', count: 20000, icon: markRaw(Document) },
  { id: 'selfhelp', name: '自我提升', count: 18000, icon: markRaw(Notebook) },
  { id: 'children', name: '少儿读物', count: 12000, icon: markRaw(Collection) },
])

const books = ref([
  { id: 1, title: '百年孤独', author: '加西亚·马尔克斯', category: '文学小说', rating: 5.0, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20magical%20realism&image_size=square' },
  { id: 2, title: '三体', author: '刘慈欣', category: '科普读物', rating: 5.0, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20science%20fiction&image_size=square' },
  { id: 3, title: '活着', author: '余华', category: '文学小说', rating: 5.0, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20Chinese%20literature&image_size=square' },
  { id: 4, title: '人类简史', author: '尤瓦尔·赫拉利', category: '历史传记', rating: 4.9, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20history&image_size=square' },
  { id: 5, title: '小王子', author: '圣埃克苏佩里', category: '少儿读物', rating: 4.9, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20little%20prince&image_size=square' },
  { id: 6, title: '穷查理宝典', author: '彼得·考夫曼', category: '自我提升', rating: 4.8, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20self%20improvement&image_size=square' },
  { id: 7, title: '明朝那些事儿', author: '当年明月', category: '历史传记', rating: 4.8, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20Chinese%20history&image_size=square' },
  { id: 8, title: '三体II', author: '刘慈欣', category: '科普读物', rating: 5.0, cover: 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=book%20cover%20space&image_size=square' },
])

const handleSearch = () => {
  console.log('搜索:', searchQuery.value)
}
</script>

<style scoped>
.books-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.header {
  background: #FFFFFF;
  border-bottom: 1px solid #E2E8F0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1440px;
  margin: 0 auto;
  padding: 16px 40px;
  display: flex;
  align-items: center;
  gap: 40px;
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

.search-bar {
  flex: 1;
  max-width: 500px;
}

.search-bar .el-input__wrapper {
  border-radius: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
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
  display: flex;
  max-width: 1440px;
  margin: 0 auto;
  padding: 32px 40px;
  gap: 32px;
}

.sidebar {
  width: 240px;
  flex-shrink: 0;
}

.sidebar-title {
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 16px;
}

.category-list {
  background: #FFFFFF;
  border-radius: 12px;
  padding: 8px;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.category-item:hover {
  background: #FFFBEB;
}

.category-item.active {
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
}

.category-icon {
  width: 20px;
  height: 20px;
  color: #92400E;
}

.category-item span {
  flex: 1;
  font-size: 14px;
  color: #334155;
}

.category-count {
  font-size: 12px;
  color: #94A3B8;
  background: #F1F5F9;
  padding: 2px 8px;
  border-radius: 12px;
}

.content-area {
  flex: 1;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.result-count {
  font-size: 14px;
  color: #64748B;
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
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.book-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.book-cover {
  width: 100%;
  padding-top: 140%;
  background: #F1F5F9;
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
  font-size: 15px;
  font-weight: 600;
  color: #0F172A;
  padding: 12px 12px 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-author {
  font-size: 13px;
  color: #64748B;
  padding: 0 12px 8px;
}

.book-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 12px 12px;
}

.book-category {
  font-size: 12px;
  color: #92400E;
  background: #FEF3C7;
  padding: 2px 8px;
  border-radius: 4px;
}

.book-rating {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #F59E0B;
}

.rating-star {
  width: 14px;
  height: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

@media (max-width: 1024px) {
  .books-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .sidebar {
    display: none;
  }

  .books-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .header-content {
    padding: 12px 16px;
    gap: 16px;
  }
}
</style>