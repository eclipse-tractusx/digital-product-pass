const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  publicPath: './',
  devServer: {
    allowedHosts: 'all',
    proxy: 'https://materialpass.int.demo.catena-x.net'
  }
});
