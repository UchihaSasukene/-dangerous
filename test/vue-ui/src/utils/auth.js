// 从localStorage获取token
export function getToken() {
  return localStorage.getItem('token') || ''
}

// 设置token到localStorage
export function setToken(token) {
  return localStorage.setItem('token', token)
}

// 移除token
export function removeToken() {
  return localStorage.removeItem('token')
} 