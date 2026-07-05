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

const accountError = ref('')
const passwordError = ref('')

function validate() {
  accountError.value = ''
  passwordError.value = ''
  let valid = true

  if (!account.value.trim()) {
    accountError.value = '请输入账号'
    valid = false
  }

  if (!password.value) {
    passwordError.value = '请输入密码'
    valid = false
  }

  return valid
}

async function handleLogin() {
  if (!validate()) return

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await login(account.value.trim(), password.value, false)
    const user = res.data?.user
    if (user) {
      setUser(user)
    }
    // 后端把 JWT 写入 HttpOnly Cookie；本地只保留一个 UI 登录标记
    setToken('authenticated')

    const redirect = route.query.redirect || '/'
    router.replace(redirect)
  } catch (err) {
    errorMsg.value = err.message || '登录失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

function goRegister() {
  router.push({ name: 'register' })
}
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-center px-container-margin py-safe relative overflow-hidden">
    <!-- Decorative green blobs -->
    <div class="absolute top-10 left-1/2 -translate-x-1/2 w-56 h-56 bg-primary-fixed-dim/50 -z-10 blur-3xl rounded-[60%_40%_30%_70%/60%_30%_70%_40%]" />
    <div class="absolute bottom-10 right-[-8%] w-40 h-40 bg-tertiary-fixed-dim/40 -z-10 blur-3xl rounded-[40%_60%_70%_30%/40%_50%_60%_50%]" />

    <header class="absolute top-0 left-0 right-0 flex items-center justify-between px-container-margin h-12 pt-safe z-20">
      <span class="material-symbols-outlined text-primary text-2xl opacity-0">arrow_back</span>
      <div class="text-display-lg font-extrabold text-on-surface">智慧图书</div>
      <span class="material-symbols-outlined text-primary text-2xl opacity-0">search</span>
    </header>

    <main class="w-full max-w-sm flex flex-col items-center">
      <div class="glass-card w-full rounded-[20px] p-4 relative">
        <!-- Decorative illustration -->
        <div class="absolute -top-12 left-1/2 -translate-x-1/2 w-20 h-20 z-20 bg-gradient-to-br from-primary to-secondary rounded-full flex items-center justify-center shadow-lg">
          <span class="material-symbols-outlined text-white text-[34px]">menu_book</span>
        </div>

        <div class="mt-6">
          <h2 class="text-headline-md text-center mb-1">欢迎回来</h2>
          <p class="text-body-md text-on-surface-variant text-center mb-4">登录后继续探索智慧图书</p>

          <form class="flex flex-col gap-3" @submit.prevent="handleLogin">
            <div class="flex flex-col gap-1">
              <label class="text-label-md text-on-surface-variant">账号 / 邮箱 / 手机号</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline text-[18px]">mail</span>
                <input
                  v-model="account"
                  type="text"
                  placeholder="hello@zhihuitushu.com"
                  autocomplete="username"
                  class="w-full bg-surface-container-low/60 border border-outline-variant rounded-full py-2 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                  :class="{ 'border-error focus:border-error focus:ring-error': accountError }"
                  @input="accountError = ''"
                />
              </div>
              <p v-if="accountError" class="text-label-sm text-error pl-3">{{ accountError }}</p>
            </div>

            <div class="flex flex-col gap-1">
              <label class="text-label-md text-on-surface-variant">密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline text-[18px]">lock</span>
                <input
                  v-model="password"
                  type="password"
                  placeholder="••••••••"
                  autocomplete="current-password"
                  class="w-full bg-surface-container-low/60 border border-outline-variant rounded-full py-2 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                  :class="{ 'border-error focus:border-error focus:ring-error': passwordError }"
                  @input="passwordError = ''"
                />
              </div>
              <p v-if="passwordError" class="text-label-sm text-error pl-3">{{ passwordError }}</p>
            </div>

            <div v-if="errorMsg" class="text-center text-label-md text-error bg-error-container/50 rounded-lg py-1">{{ errorMsg }}</div>

            <div class="flex justify-end">
              <button type="button" class="text-label-md text-primary hover:opacity-80">忘记密码？</button>
            </div>

            <button
              type="submit"
              :disabled="loading"
              class="w-full bg-primary text-on-primary rounded-full py-2.5 text-headline-sm hover:scale-[1.02] active:scale-95 transition-transform disabled:opacity-60 disabled:scale-100 shadow-[0_4px_14px_rgba(16,185,129,0.28)]"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </button>
          </form>

          <div class="mt-5 pt-4 border-t border-outline-variant/30 text-center">
            <p class="text-label-md text-on-surface-variant mb-2">还没有账号？</p>
            <button
              type="button"
              class="w-full bg-surface-container-low text-on-surface border border-outline-variant rounded-full py-2.5 text-headline-sm hover:scale-[1.02] active:scale-95 transition-transform"
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
