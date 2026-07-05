import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '@/stores/auth.js'
import { isAdminLoggedIn } from '@/stores/admin.js'
import Home from '@/views/Home.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 用户端路由
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: { title: '智慧图书', requiresAuth: false },
    },
    {
      path: '/explore',
      name: 'explore',
      component: () => import('@/views/Explore.vue'),
      meta: { title: '发现', requiresAuth: false },
    },
    {
      path: '/ai',
      name: 'ai',
      component: () => import('@/views/AiAssistant.vue'),
      meta: { title: 'AI 读书助手', requiresAuth: true },
    },
    {
      path: '/library',
      name: 'library',
      component: () => import('@/views/Library.vue'),
      meta: { title: '我的书架', requiresAuth: true },
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/Profile.vue'),
      meta: { title: '个人中心', requiresAuth: true },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue'),
      meta: { title: '登录', requiresAuth: false, guestOnly: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue'),
      meta: { title: '注册', requiresAuth: false, guestOnly: true },
    },

    // 管理端路由 - 使用嵌套路由和统一布局
    {
      path: '/admin/login',
      name: 'admin-login',
      component: () => import('@/views/admin/Login.vue'),
      meta: { title: '管理员登录', requiresAuth: false, adminGuestOnly: true },
    },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAdminAuth: true },
      children: [
        {
          path: '',
          redirect: '/admin/dashboard',
        },
        {
          path: 'dashboard',
          name: 'admin-dashboard',
          component: () => import('@/views/admin/Dashboard.vue'),
          meta: { title: '管理后台 - 首页概览' },
        },
        {
          path: 'books',
          name: 'admin-books',
          component: () => import('@/views/admin/Books.vue'),
          meta: { title: '管理后台 - 图书管理' },
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('@/views/admin/Users.vue'),
          meta: { title: '管理后台 - 用户管理' },
        },
        {
          path: 'statistics',
          name: 'admin-statistics',
          component: () => import('@/views/admin/Statistics.vue'),
          meta: { title: '管理后台 - 数据统计' },
        },
        {
          path: 'settings',
          name: 'admin-settings',
          component: () => import('@/views/admin/Settings.vue'),
          meta: { title: '管理后台 - 系统设置' },
        },
      ],
    },

    // 404
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFound.vue'),
      meta: { title: '页面未找到' },
    },
  ],
})

router.beforeEach((to) => {
  document.title = to.meta.title || '智慧图书'

  // 判断是否为管理端路由
  const isAdminRoute = to.path.startsWith('/admin')

  // === 系统隔离守卫 ===
  // 如果用户端已登录，访问管理端路由时强制跳转到用户端首页
  if (isLoggedIn.value && isAdminRoute) {
    return { name: 'home' }
  }

  // 如果管理端已登录，访问用户端路由时强制跳转到管理端首页
  if (isAdminLoggedIn.value && !isAdminRoute) {
    return { name: 'admin-dashboard' }
  }

  // === 用户端路由守卫 ===
  if (to.meta.requiresAuth && !isLoggedIn.value) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  if (to.meta.guestOnly && isLoggedIn.value) {
    return { name: 'home' }
  }

  // === 管理端路由守卫 ===
  if (to.meta.requiresAdminAuth && !isAdminLoggedIn.value) {
    return { name: 'admin-login', query: { redirect: to.fullPath } }
  }

  if (to.meta.adminGuestOnly && isAdminLoggedIn.value) {
    return { name: 'admin-dashboard' }
  }
})

export default router