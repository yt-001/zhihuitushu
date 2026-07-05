<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  House, User, Document, Setting, SwitchButton,
  DataAnalysis, Bell, Fold, Expand, Tickets
} from '@element-plus/icons-vue'
import { getAdminUser, clearAdminAuth } from '@/stores/admin.js'

const router = useRouter()
const route = useRoute()
const adminUser = ref(getAdminUser())
const isCollapse = ref(false)

// 菜单项配置
const menuItems = [
  { path: '/admin/dashboard', title: '首页概览', icon: House },
  { path: '/admin/books', title: '图书管理', icon: Document },
  { path: '/admin/users', title: '用户管理', icon: User },
  { path: '/admin/statistics', title: '数据统计', icon: DataAnalysis },
  { path: '/admin/settings', title: '系统设置', icon: Setting },
]

// 当前激活的菜单路径
const activeMenu = computed(() => route.path)

// 面包屑导航
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched.map(item => ({
    title: item.meta.title.replace('管理后台 - ', ''),
    path: item.path
  }))
})

// 当前页面标题
const pageTitle = computed(() => {
  const lastBreadcrumb = breadcrumbs.value[breadcrumbs.value.length - 1]
  return lastBreadcrumb?.title || '管理后台'
})

// 菜单选择处理
function handleMenuSelect(path) {
  if (route.path !== path) {
    router.push(path)
  }
}

// 退出登录
async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    clearAdminAuth()
    ElMessage.success('已退出登录')
    router.push('/admin/login')
  } catch {
    // 取消退出
  }
}

// 切换侧边栏折叠
function toggleSidebar() {
  isCollapse.value = !isCollapse.value
}
</script>

<template>
  <div class="admin-layout">
    <!-- 左侧导航栏 -->
    <aside class="sidebar" :class="{ collapsed: isCollapse }">
      <div class="sidebar-header">
        <div class="logo">
          <el-icon :size="28"><Tickets /></el-icon>
          <span v-show="!isCollapse" class="logo-text">智慧图书</span>
        </div>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapse"
        :collapse-transition="false"
        @select="handleMenuSelect"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>

      <div class="sidebar-footer">
        <el-button
          :icon="SwitchButton"
          text
          @click="handleLogout"
          class="logout-btn"
        >
          <span v-show="!isCollapse">退出登录</span>
        </el-button>
      </div>
    </aside>

    <!-- 右侧内容区域 -->
    <div class="main-container" :class="{ expanded: isCollapse }">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="header-left">
          <el-button
            text
            :icon="isCollapse ? Expand : Fold"
            @click="toggleSidebar"
            class="collapse-btn"
          />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
              v-for="(item, index) in breadcrumbs"
              :key="item.path"
            >
              <span v-if="index === breadcrumbs.length - 1">{{ item.title }}</span>
              <router-link v-else :to="item.path">{{ item.title }}</router-link>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="3" class="notification-badge" :max="99">
            <el-button :icon="Bell" circle class="notification-btn" />
          </el-badge>
          <el-dropdown trigger="click">
            <div class="user-info">
              <el-avatar :size="36" style="background-color: #10b981">
                {{ adminUser?.username?.charAt(0)?.toUpperCase() || 'A' }}
              </el-avatar>
              <span class="username">{{ adminUser?.username || '管理员' }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/admin/settings')">
                  <el-icon><Setting /></el-icon>
                  <span>系统设置</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 主内容区域 -->
      <main class="content-area">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* 侧边栏样式 */
.sidebar {
  width: 220px;
  background: linear-gradient(180deg, #047857 0%, #10b981 100%);
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 100;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  cursor: pointer;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  white-space: nowrap;
  letter-spacing: 1px;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
  background: transparent;
  --el-menu-bg-color: transparent;
  --el-menu-text-color: rgba(255, 255, 255, 0.9);
  --el-menu-hover-bg-color: rgba(255, 255, 255, 0.15);
  --el-menu-active-bg-color: rgba(255, 255, 255, 0.25);
  --el-menu-active-color: white;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 220px;
}

:deep(.el-menu-item) {
  height: 56px;
  line-height: 56px;
  margin: 4px 8px;
  border-radius: 8px;
}

:deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.25) !important;
  font-weight: 600;
}

:deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.15) !important;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.15);
}

.logout-btn {
  width: 100%;
  color: rgba(255, 255, 255, 0.9) !important;
  justify-content: flex-start;
}

.logout-btn:hover {
  color: white !important;
  background: rgba(255, 255, 255, 0.15) !important;
}

/* 主内容区域 */
.main-container {
  flex: 1;
  margin-left: 220px;
  min-width: 0;
  transition: margin-left 0.3s ease;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-container.expanded {
  margin-left: 64px;
}

/* 顶部导航栏 */
.top-header {
  height: 64px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 20px;
  color: #374151;
}

.collapse-btn:hover {
  color: #10b981;
}

:deep(.el-breadcrumb) {
  font-size: 14px;
}

:deep(.el-breadcrumb__inner a),
:deep(.el-breadcrumb__inner span) {
  color: #6b7280;
  font-weight: 500;
}

:deep(.el-breadcrumb__inner a:hover) {
  color: #10b981;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #111827;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.notification-btn {
  color: #6b7280;
}

.notification-btn:hover {
  color: #10b981;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background: #f5f5f5;
}

.username {
  font-size: 14px;
  color: #374151;
  font-weight: 500;
}

/* 内容区域 */
.content-area {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  min-width: 0;
  background: #f0f2f5;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    width: 220px;
  }

  .sidebar.collapsed {
    transform: translateX(0);
    width: 220px;
  }

  .main-container,
  .main-container.expanded {
    margin-left: 0;
  }

  .username {
    display: none;
  }

  .notification-badge {
    display: none;
  }
}
</style>