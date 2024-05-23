/**
 * Tractus-X -  Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { visualizer } from 'rollup-plugin-visualizer';
import vuetify, { transformAssetUrls } from 'vite-plugin-vuetify';

// https://vitejs.dev/config/
export default defineConfig(async ({ command, mode }) => {
    return {
        base: '/passport',
        // https://vitejs.dev/config/shared-options.html#define
        define: { 'process.env': {} },
        optimizeDeps: {
            include: [
                'vue',
                'vue-router',
                'vuex',
                "crypto-js",
                "vue3-qrcode-reader"
            ]
        },
        plugins: [
            vue(
                {
                    template: {
                        // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin#image-loading
                        transformAssetUrls,
                    },
                }
            ),
            vuetify({ autoImport: true })
        ],
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url)),
                '~': fileURLToPath(new URL('./node_modules', import.meta.url))
            },
            extensions: ['.js', '.json', '.jsx', '.vue'],
        },
        server: {
            port: 8080,
            fs: {
                // Allow serving files from one level up to the project root
                allow: ['..'],
            },
        },
        build: {
            target: 'esnext',
            minify: 'esbuild',
            rollupOptions: {
                // @ts-ignore
                output: {
                    manualChunks: {
                        // Split external library from transpiled code.
                        vue: ['vue', 'vue-router', 'vuex', 'vuex', 'vuex-persistedstate'],
                        vuetify: [
                            'vuetify',
                            'vuetify/components',
                            'vuetify/directives',
                            'webfontloader',
                        ],
                        materialdesignicons: ['@mdi/font/css/materialdesignicons.css'],
                    },
                    plugins: [
                        mode === 'analyze'
                            ? // rollup-plugin-visualizer
                            // https://github.com/btd/rollup-plugin-visualizer
                            visualizer({
                                open: true,
                                filename: 'dist/stats.html',
                            })
                            : undefined,
                    ],
                },
            },
            esbuild: {
                // Drop console when production build.
                drop: command === 'serve' ? [] : ['console'],
            },
        }
    }
});
