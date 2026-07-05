import request from './request.js'

export function getBanners(limit = 5) {
  return request.get('/home/banners', { params: { limit } })
}

export function getHomeCategories(limit = 10) {
  return request.get('/home/categories', { params: { limit } })
}
