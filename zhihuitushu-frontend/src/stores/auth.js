import { reactive, computed } from 'vue'

const cachedUser = localStorage.getItem('user')
const state = reactive({
  // token 不是后端 JWT，而是一个本地 UI 登录标记。
  // 后端认证通过 HttpOnly Cookie 中的 token 完成，axios 会自动携带。
  token: localStorage.getItem('token') || '',
  user: cachedUser ? JSON.parse(cachedUser) : null,
})

export const isLoggedIn = computed(() => !!state.token || !!state.user)

export function getToken() {
  return state.token
}

export function setToken(token) {
  state.token = token
  localStorage.setItem('token', token)
}

export function setUser(user) {
  state.user = user
  if (user) {
    localStorage.setItem('user', JSON.stringify(user))
  } else {
    localStorage.removeItem('user')
  }
}

export function getUser() {
  return state.user
}

export function clearToken() {
  state.token = ''
  state.user = null
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}
