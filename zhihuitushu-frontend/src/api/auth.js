import request from './request.js'

export function sendCode(account, type = 'email') {
  return request.post('/auth/send-code', { account, type })
}

export function login(account, password, rememberMe = false) {
  return request.post('/auth/login', { account, password, rememberMe })
}

export function register(account, password, confirmPassword, code) {
  return request.post('/auth/register', { account, password, confirmPassword, code })
}

export function logout() {
  return request.post('/auth/logout')
}

export function getCurrentUser() {
  return request.get('/auth/me')
}
