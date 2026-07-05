import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '@/stores/auth.js'
import Home from '@/views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
  ],
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '智慧图书'

  if (to.meta.requiresAuth && !isLoggedIn.value) {
    next({ name: 'login', query: { redirect: to.fullPath } })
  } else if (to.meta.guestOnly && isLoggedIn.value) {
    next({ name: 'home' })
  } else {
    next()
  }
})

export default router
