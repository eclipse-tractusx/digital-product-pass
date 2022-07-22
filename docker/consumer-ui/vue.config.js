const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  publicPath: './',
  devServer: {
    allowedHosts: 'all',
	//proxy: 'localhost:9192'
    proxy: 'https://materialpass.int.demo.catena-x.net'
  }
})
