import request from './request.js'

export function getBooks(type = 'recommend', limit = 10) {
  return request.get('/books', { params: { type, limit } })
}
