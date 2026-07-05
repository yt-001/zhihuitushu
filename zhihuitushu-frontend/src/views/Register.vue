<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register, sendCode } from '@/api/auth.js'
import { setToken, setUser } from '@/stores/auth.js'

const router = useRouter()

const account = ref('')
const password = ref('')
const confirmPassword = ref('')
const code = ref('')
const agree = ref(false)
const loading = ref(false)
const codeLoading = ref(false)
const codeCountdown = ref(0)
const errorMsg = ref('')

async function handleRegister() {
  if (!account.value || !password.value || !confirmPassword.value || !code.value) {
    errorMsg.value = '请填写完整信息'
    return
  }
  if (password.value !== confirmPassword.value) {
    errorMsg.value = '两次输入的密码不一致'
    return
  }
  if (!agree.value) {
    errorMsg.value = '请同意用户协议和隐私政策'
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await register(account.value, password.value, confirmPassword.value, code.value)
    const user = res.data?.user
    if (user) {
      setUser(user)
    }
    setToken('authenticated')
    router.replace('/')
  } catch (err) {
    errorMsg.value = err.message || '注册失败'
  } finally {
    loading.value = false
  }
}

async function handleSendCode() {
  if (!account.value) {
    errorMsg.value = '请先输入账号'
    return
  }
  if (codeCountdown.value > 0) return

  codeLoading.value = true
  errorMsg.value = ''

  try {
    await sendCode(account.value, 'email')
    codeCountdown.value = 60
    const timer = setInterval(() => {
      codeCountdown.value--
      if (codeCountdown.value <= 0) clearInterval(timer)
    }, 1000)
  } catch (err) {
    errorMsg.value = err.message || '验证码发送失败'
  } finally {
    codeLoading.value = false
  }
}

function goLogin() {
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="min-h-screen px-container-margin pt-safe pb-24 relative overflow-x-hidden">
    <!-- Decorative blobs -->
    <div class="absolute top-10 left-1/2 -translate-x-1/2 w-64 h-64 bg-secondary-fixed-dim/40 -z-10 blur-3xl rounded-[60%_40%_30%_70%/60%_30%_70%_40%]" />
    <div class="absolute bottom-10 right-0 w-48 h-48 bg-primary-fixed-dim/40 -z-10 blur-3xl rounded-[40%_60%_70%_30%/40%_50%_60%_50%]" />

    <header class="flex items-center justify-between w-full h-14">
      <button class="touch-target text-primary hover:opacity-80" @click="goLogin">
        <span class="material-symbols-outlined">arrow_back</span>
      </button>
      <div class="text-display-lg font-extrabold text-on-surface">智慧图书</div>
      <span class="material-symbols-outlined text-primary text-2xl opacity-0">search</span>
    </header>

    <main class="pt-12 relative z-10 flex flex-col items-center">
      <div class="glass-card w-full max-w-sm rounded-[24px] p-6 relative">
        <div class="absolute -top-16 left-1/2 -translate-x-1/2 w-28 h-28 z-20 bg-gradient-to-br from-secondary-container to-primary-container rounded-full flex items-center justify-center shadow-lg">
          <span class="material-symbols-outlined text-white text-5xl">person_add</span>
        </div>

        <div class="mt-10">
          <h2 class="text-headline-md text-center mb-2">创建账号</h2>
          <p class="text-body-md text-on-surface-variant text-center mb-8">开启你的智慧阅读之旅</p>

          <form class="flex flex-col gap-5" @submit.prevent="handleRegister">
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
              <label class="text-label-md text-on-surface-variant">验证码</label>
              <div class="flex gap-2">
                <div class="relative flex-1">
                  <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline">pin</span>
                  <input
                    v-model="code"
                    type="text"
                    placeholder="123456"
                    class="w-full bg-surface-container-lowest/60 border border-outline-variant rounded-full py-3 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                  />
                </div>
                <button
                  type="button"
                  :disabled="codeLoading || codeCountdown > 0"
                  class="px-4 rounded-full bg-primary-container text-on-primary-container text-label-md font-semibold hover:opacity-90 transition-opacity disabled:opacity-60"
                  @click="handleSendCode"
                >
                  {{ codeCountdown > 0 ? `${codeCountdown}s` : (codeLoading ? '发送中' : '获取') }}
                </button>
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
                  autocomplete="new-password"
                  class="w-full bg-surface-container-lowest/60 border border-outline-variant rounded-full py-3 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                />
              </div>
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-label-md text-on-surface-variant">确认密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline">lock</span>
                <input
                  v-model="confirmPassword"
                  type="password"
                  placeholder="••••••••"
                  autocomplete="new-password"
                  class="w-full bg-surface-container-lowest/60 border border-outline-variant rounded-full py-3 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                />
              </div>
            </div>

            <div v-if="errorMsg" class="text-center text-label-md text-error">{{ errorMsg }}</div>

            <label class="flex items-start gap-2 cursor-pointer">
              <input v-model="agree" type="checkbox" class="w-5 h-5 mt-0.5 rounded border-outline-variant text-primary focus:ring-primary" />
              <span class="text-label-md text-on-surface-variant leading-snug">我已阅读并同意《用户协议》和《隐私政策》</span>
            </label>

            <button
              type="submit"
              :disabled="loading"
              class="w-full bg-primary text-on-primary rounded-full py-3 text-headline-sm hover:scale-[1.02] active:scale-95 transition-transform disabled:opacity-60 disabled:scale-100"
            >
              {{ loading ? '注册中...' : '注 册' }}
            </button>
          </form>

          <div class="mt-6 text-center">
            <button type="button" class="text-label-md text-primary hover:opacity-80" @click="goLogin">
              已有账号？去登录
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
