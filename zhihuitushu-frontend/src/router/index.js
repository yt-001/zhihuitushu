import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/Home.vue'),
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/auth/Login.vue'),
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/auth/Register.vue'),
  },
  {
    path: '/reset-password',
    name: 'resetPassword',
    component: () => import('../views/auth/ResetPassword.vue'),
  },
  {
    path: '/books',
    name: 'books',
    component: () => import('../views/Books.vue'),
  },
  {
    path: '/book/:id',
    name: 'bookDetail',
    component: () => import('../views/BookDetail.vue'),
  },
  {
    path: '/library',
    name: 'library',
    component: () => import('../views/Library.vue'),
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('../views/Profile.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = document.cookie.includes('token')
  if (to.name !== 'login' && to.name !== 'register' && to.name !== 'resetPassword' && !token) {
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router