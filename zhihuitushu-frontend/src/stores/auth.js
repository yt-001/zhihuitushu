import { reactive, computed } from 'vue'

const cachedUser = localStorage.getItem('user')
const state = reactive({
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
