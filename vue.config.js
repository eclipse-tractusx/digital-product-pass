const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  publicPath: './',

  devServer: {
    allowedHosts: 'all',
    proxy: 'https://materialpass.int.demo.catena-x.net'
  },

  pluginOptions: {
    vuetify: {
      // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
    }
  }
});
