<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { login } from '@/api/auth.js'
import { setToken, setUser } from '@/stores/auth.js'

const router = useRouter()
const route = useRoute()

const account = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')

async function handleLogin() {
  if (!account.value || !password.value) {
    errorMsg.value = '请输入账号和密码'
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await login(account.value, password.value, false)
    const user = res.data?.user
    if (user) {
      setUser(user)
    }
    // token is stored in HttpOnly cookie by backend; keep a local flag for UI state
    setToken('authenticated')

    const redirect = route.query.redirect || '/'
    router.replace(redirect)
  } catch (err) {
    errorMsg.value = err.message || '登录失败'
  } finally {
    loading.value = false
  }
}

function goRegister() {
  router.push({ name: 'register' })
}
</script>

<template>
  <div class="min-h-screen px-container-margin pt-safe pb-24 relative overflow-x-hidden">
    <!-- Decorative blobs -->
    <div class="absolute top-10 left-1/2 -translate-x-1/2 w-64 h-64 bg-primary-fixed-dim/40 -z-10 blur-3xl rounded-[60%_40%_30%_70%/60%_30%_70%_40%]" />
    <div class="absolute bottom-10 right-0 w-48 h-48 bg-tertiary-fixed-dim/40 -z-10 blur-3xl rounded-[40%_60%_70%_30%/40%_50%_60%_50%]" />

    <header class="flex items-center justify-between w-full h-14">
      <span class="material-symbols-outlined text-primary text-2xl opacity-0">arrow_back</span>
      <div class="text-display-lg font-extrabold text-on-surface">智慧图书</div>
      <span class="material-symbols-outlined text-primary text-2xl opacity-0">search</span>
    </header>

    <main class="pt-12 relative z-10 flex flex-col items-center">
      <div class="glass-card w-full max-w-sm rounded-[24px] p-6 relative">
        <!-- Decorative illustration -->
        <div class="absolute -top-16 left-1/2 -translate-x-1/2 w-28 h-28 z-20 bg-gradient-to-br from-primary-container to-tertiary-container rounded-full flex items-center justify-center shadow-lg">
          <span class="material-symbols-outlined text-white text-5xl">menu_book</span>
        </div>

        <div class="mt-10">
          <h2 class="text-headline-md text-center mb-2">欢迎回来</h2>
          <p class="text-body-md text-on-surface-variant text-center mb-8">登录后继续探索智慧图书</p>

          <form class="flex flex-col gap-5" @submit.prevent="handleLogin">
            <div class="flex flex-col gap-2">
              <label class="text-label-md text-on-surface-variant">账号 / 邮箱 / 手机号</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline">mail</span>
                <input
                  v-model="account"
                  type="text"
                  placeholder="hello@zhihuitushu.com"
                  autocomplete="username"
                  class="w-full bg-surface-container-lowest/60 border border-outline-variant rounded-full py-3 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                />
              </div>
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-label-md text-on-surface-variant">密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline">lock</span>
                <input
                  v-model="password"
                  type="password"
                  placeholder="••••••••"
                  autocomplete="current-password"
                  class="w-full bg-surface-container-lowest/60 border border-outline-variant rounded-full py-3 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                />
              </div>
            </div>

            <div v-if="errorMsg" class="text-center text-label-md text-error">{{ errorMsg }}</div>

            <div class="flex justify-end">
              <button type="button" class="text-label-md text-primary hover:opacity-80">忘记密码？</button>
            </div>

            <button
              type="submit"
              :disabled="loading"
              class="w-full bg-primary text-on-primary rounded-full py-3 text-headline-sm hover:scale-[1.02] active:scale-95 transition-transform disabled:opacity-60 disabled:scale-100"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </button>
          </form>

          <div class="mt-8 pt-6 border-t border-outline-variant/30 text-center">
            <p class="text-label-md text-on-surface-variant mb-4">还没有账号？</p>
            <button
              type="button"
              class="w-full bg-surface-container-low text-on-surface border border-outline-variant rounded-full py-3 text-headline-sm hover:scale-[1.02] active:scale-95 transition-transform"
              @click="goRegister"
            >
              立即注册
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
