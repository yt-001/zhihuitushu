<script setup>
import { useRouter } from 'vue-router'
import { House } from '@element-plus/icons-vue'

const router = useRouter()

function goHome() {
  router.push('/')
}

function goAdmin() {
  router.push('/admin/login')
}
</script>

<template>
  <div class="not-found">
    <div class="content">
      <div class="error-code">404</div>
      <h1>页面未找到</h1>
      <p>抱歉，您访问的页面不存在或已被移除</p>
      <div class="actions">
        <el-button type="primary" :icon="House" @click="goHome">返回首页</el-button>
        <el-button @click="goAdmin">管理后台</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5fbf8 0%, #d1fae5 50%, #ecfdf5 100%);
}

.content {
  text-align: center;
  padding: 40px;
}

.error-code {
  font-size: 120px;
  font-weight: 800;
  color: #10b981;
  line-height: 1;
  margin-bottom: 20px;
  text-shadow: 0 4px 20px rgba(16, 185, 129, 0.3);
}

h1 {
  font-size: 32px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 12px 0;
}

p {
  font-size: 16px;
  color: #6b7280;
  margin: 0 0 32px 0;
}

.actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}
</style>