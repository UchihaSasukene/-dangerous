module.exports = {
  devServer: {
    proxy: {
      '/': {
        target: 'http://localhost:9090',
        changeOrigin: true
      }
    }
  }
} 