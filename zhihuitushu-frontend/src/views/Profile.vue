<template>
  <div class="profile-page">
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
      <div class="profile-card">
        <div class="profile-header">
          <div class="avatar-wrapper">
            <div class="avatar">
              <User class="avatar-icon" />
            </div>
            <div class="edit-avatar">
              <Camera class="camera-icon" />
            </div>
          </div>
          <div class="profile-info">
            <h2 class="username">用户名</h2>
            <p class="email">未绑定邮箱</p>
            <div class="profile-stats">
              <div class="stat">
                <span class="stat-value">0</span>
                <span class="stat-label">已读</span>
              </div>
              <div class="stat">
                <span class="stat-value">0</span>
                <span class="stat-label">在读</span>
              </div>
              <div class="stat">
                <span class="stat-value">0</span>
                <span class="stat-label">想读</span>
              </div>
            </div>
          </div>
        </div>

        <div class="menu-section">
          <h3 class="section-title">我的阅读</h3>
          <div class="menu-list">
            <div class="menu-item" @click="$router.push('/library')">
              <BookOpen class="menu-icon" />
              <span class="menu-text">我的书架</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Clock class="menu-icon" />
              <span class="menu-text">阅读记录</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Star class="menu-icon" />
              <span class="menu-text">我的书评</span>
              <ArrowRight class="menu-arrow" />
            </div>
          </div>
        </div>

        <div class="menu-section">
          <h3 class="section-title">账户设置</h3>
          <div class="menu-list">
            <div class="menu-item">
              <User class="menu-icon" />
              <span class="menu-text">编辑资料</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Lock class="menu-icon" />
              <span class="menu-text">修改密码</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Bell class="menu-icon" />
              <span class="menu-text">消息通知</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <MapLocation class="menu-icon" />
              <span class="menu-text">语言设置</span>
              <ArrowRight class="menu-arrow" />
            </div>
          </div>
        </div>

        <div class="menu-section">
          <h3 class="section-title">关于</h3>
          <div class="menu-list">
            <div class="menu-item">
              <Help class="menu-icon" />
              <span class="menu-text">帮助中心</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Files class="menu-icon" />
              <span class="menu-text">用户协议</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Key class="menu-icon" />
              <span class="menu-text">隐私政策</span>
              <ArrowRight class="menu-arrow" />
            </div>
            <div class="menu-item">
              <Setting class="menu-icon" />
              <span class="menu-text">关于我们</span>
              <ArrowRight class="menu-arrow" />
            </div>
          </div>
        </div>

        <div class="logout-section">
          <el-button class="logout-btn" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { Notebook, User, Camera, Clock, Star, Lock, Bell, MapLocation, Help, Files, Key, Setting, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const handleLogout = () => {
  document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
  ElMessage.success('已退出登录')
  window.location.href = '/login'
}
</script>

<style scoped>
.profile-page {
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
  max-width: 480px;
  margin: 0 auto;
  padding: 40px;
}

.profile-card {
  background: #FFFFFF;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 32px;
  border-bottom: 1px solid #F1F5F9;
}

.avatar-wrapper {
  position: relative;
}

.avatar {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  width: 40px;
  height: 40px;
  color: #FFFFFF;
}

.edit-avatar {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 28px;
  height: 28px;
  background: #FFFFFF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.camera-icon {
  width: 14px;
  height: 14px;
  color: #92400E;
}

.profile-info {
  flex: 1;
}

.username {
  font-size: 20px;
  font-weight: 600;
  color: #0F172A;
  margin-bottom: 4px;
}

.email {
  font-size: 14px;
  color: #64748B;
  margin-bottom: 16px;
}

.profile-stats {
  display: flex;
  gap: 32px;
}

.stat {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #0F172A;
}

.stat-label {
  font-size: 12px;
  color: #94A3B8;
}

.menu-section {
  padding: 20px 32px;
  border-bottom: 1px solid #F1F5F9;
}

.section-title {
  font-size: 12px;
  font-weight: 600;
  color: #94A3B8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 12px;
}

.menu-list {
  display: flex;
  flex-direction: column;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 14px 0;
  cursor: pointer;
  transition: background 0.2s;
}

.menu-item:hover {
  background: #F8FAFC;
  margin: 0 -32px;
  padding-left: 32px;
  padding-right: 32px;
}

.menu-icon {
  width: 20px;
  height: 20px;
  color: #92400E;
}

.menu-text {
  flex: 1;
  font-size: 15px;
  color: #334155;
}

.menu-arrow {
  width: 16px;
  height: 16px;
  color: #CBD5E1;
}

.logout-section {
  padding: 24px 32px;
}

.logout-btn {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-size: 15px;
  color: #DC2626;
  border-color: #DC2626;
}

@media (max-width: 480px) {
  .main-content {
    padding: 20px 16px;
  }

  .profile-header {
    padding: 24px;
  }

  .profile-stats {
    gap: 20px;
  }

  .menu-section {
    padding: 16px 20px;
  }
}
</style>