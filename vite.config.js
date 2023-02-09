import { fileURLToPath, URL } from 'url';
import vuetify from 'vite-plugin-vuetify';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// https://vitejs.dev/config/
export default defineConfig({
    cssCodeSplit: false,
    optimizeDeps: {
        include: [
            'vue',
            'vue-router',
            'vuex',
            "crypto-js",
            "vue3-qrcode-reader"
            // etc.
        ]
    },
    plugins: [vue(),
    vuetify({ autoImport: true })
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        port: 8080
    }
})
