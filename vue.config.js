const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    allowedHosts: 'all',
    //proxy: 'http://localhost:9192'
    proxy: 'https://materialpass.int.demo.catena-x.net'
  }
})
