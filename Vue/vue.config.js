const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({

    devServer: {
        allowedHosts:[
            'cl.xiaohai2006.cn',
        ],
        proxy: {
            '/api': {
              target: 'https://wifi.api.yhhrun.cn:86',
              changeOrigin: true,
              pathRewrite: { '^/api': '' },
            },
            '/proxy': {
              target: 'https://api.ilingku.com',
              changeOrigin: true,
              pathRewrite: { '^/proxy': '' },
            }
        },
        // proxy: {
        //     [process.env.VUE_APP_BASE_API]: {
        //       // target: `https://xhsa1.openapi.xiaohai2006.cn:1012`, // 目标服务器地址
        //       target: 'http://localhost:8080',
        //       changeOrigin: true,
        //       pathRewrite: {
        //         ['^' + process.env.VUE_APP_BASE_API]: ''
        //       }
        //     }
        // },
    },
    transpileDependencies: true,
  
})
