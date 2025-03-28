const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  devServer:{
    client:{
      overlay:false
    }
  },
  transpileDependencies: true,
  chainWebpack: config => {
    config.plugin('define').tap(definitions => {
      Object.assign(definitions[0]['process.env'], {
        NODE_HOST: '"http://localhost:8888"',
      });
      return definitions;
    });
  }
})
