import { reactive, computed } from 'vue'

const cachedAdminUser = localStorage.getItem('adminUser')
const state = reactive({
  token: localStorage.getItem('adminToken') || '',
  admin: cachedAdminUser ? JSON.parse(cachedAdminUser) : null,
})

export const isAdminLoggedIn = computed(() => !!state.token && !!state.admin)

export function getAdminToken() {
  return state.token
}

export function setAdminToken(token) {
  state.token = token
  localStorage.setItem('adminToken', token)
}

export function getAdminUser() {
  return state.admin
}

export function setAdminUser(admin) {
  state.admin = admin
  if (admin) {
    localStorage.setItem('adminUser', JSON.stringify(admin))
  } else {
    localStorage.removeItem('adminUser')
  }
}

export function clearAdminAuth() {
  state.token = ''
  state.admin = null
  localStorage.removeItem('adminToken')
  localStorage.removeItem('adminUser')
}