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

const fieldErrors = ref({
  account: '',
  password: '',
  confirmPassword: '',
  code: '',
  agree: '',
})

const EMAIL_RE = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/
const PHONE_RE = /^1[3-9]\d{9}$/
const CODE_RE = /^\d{6}$/
const PASSWORD_RE = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/

function validateAccount() {
  if (!account.value.trim()) {
    fieldErrors.value.account = '请输入账号'
    return false
  }
  if (!EMAIL_RE.test(account.value.trim()) && !PHONE_RE.test(account.value.trim())) {
    fieldErrors.value.account = '账号格式应为邮箱或手机号'
    return false
  }
  fieldErrors.value.account = ''
  return true
}

function validatePassword() {
  if (!password.value) {
    fieldErrors.value.password = '请输入密码'
    return false
  }
  if (!PASSWORD_RE.test(password.value)) {
    fieldErrors.value.password = '密码至少8位，且包含大小写字母和数字'
    return false
  }
  fieldErrors.value.password = ''
  return true
}

function validateConfirmPassword() {
  if (!confirmPassword.value) {
    fieldErrors.value.confirmPassword = '请确认密码'
    return false
  }
  if (confirmPassword.value !== password.value) {
    fieldErrors.value.confirmPassword = '两次输入的密码不一致'
    return false
  }
  fieldErrors.value.confirmPassword = ''
  return true
}

function validateCode() {
  if (!code.value) {
    fieldErrors.value.code = '请输入验证码'
    return false
  }
  if (!CODE_RE.test(code.value)) {
    fieldErrors.value.code = '验证码为6位数字'
    return false
  }
  fieldErrors.value.code = ''
  return true
}

function validateAgree() {
  if (!agree.value) {
    fieldErrors.value.agree = '请同意用户协议和隐私政策'
    return false
  }
  fieldErrors.value.agree = ''
  return true
}

function validateAll() {
  const results = [
    validateAccount(),
    validatePassword(),
    validateConfirmPassword(),
    validateCode(),
    validateAgree(),
  ]
  return results.every(Boolean)
}

async function handleRegister() {
  if (!validateAll()) return

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await register(account.value.trim(), password.value, confirmPassword.value, code.value)
    const user = res.data?.user
    if (user) {
      setUser(user)
    }
    setToken('authenticated')
    router.replace('/')
  } catch (err) {
    errorMsg.value = err.message || '注册失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

async function handleSendCode() {
  if (!validateAccount()) return
  if (codeCountdown.value > 0) return

  codeLoading.value = true
  errorMsg.value = ''

  try {
    await sendCode(account.value.trim(), 'email')
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

function clearFieldError(field) {
  fieldErrors.value[field] = ''
}
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-center px-container-margin py-safe relative overflow-y-auto no-scrollbar">
    <!-- Decorative green blobs -->
    <div class="absolute top-10 left-1/2 -translate-x-1/2 w-56 h-56 bg-secondary-fixed-dim/45 -z-10 blur-3xl rounded-[60%_40%_30%_70%/60%_30%_70%_40%]" />
    <div class="absolute bottom-10 right-[-8%] w-40 h-40 bg-primary-fixed-dim/45 -z-10 blur-3xl rounded-[40%_60%_70%_30%/40%_50%_60%_50%]" />

    <header class="absolute top-0 left-0 right-0 flex items-center justify-between px-container-margin h-12 pt-safe z-20">
      <button class="touch-target text-primary hover:opacity-80" @click="goLogin">
        <span class="material-symbols-outlined">arrow_back</span>
      </button>
      <div class="text-display-lg font-extrabold text-on-surface">智慧图书</div>
      <span class="material-symbols-outlined text-primary text-2xl opacity-0">search</span>
    </header>

    <main class="w-full max-w-sm flex flex-col items-center py-4">
      <div class="glass-card w-full rounded-[20px] p-4 relative">
        <div class="absolute -top-12 left-1/2 -translate-x-1/2 w-20 h-20 z-20 bg-gradient-to-br from-secondary to-primary rounded-full flex items-center justify-center shadow-lg">
          <span class="material-symbols-outlined text-white text-[34px]">person_add</span>
        </div>

        <div class="mt-6">
          <h2 class="text-headline-md text-center mb-1">创建账号</h2>
          <p class="text-body-md text-on-surface-variant text-center mb-4">开启你的智慧阅读之旅</p>

          <form class="flex flex-col gap-3" @submit.prevent="handleRegister">
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
                  :class="{ 'border-error focus:border-error focus:ring-error': fieldErrors.account }"
                  @input="clearFieldError('account')"
                />
              </div>
              <p v-if="fieldErrors.account" class="text-label-sm text-error pl-3">{{ fieldErrors.account }}</p>
            </div>

            <div class="flex flex-col gap-1">
              <label class="text-label-md text-on-surface-variant">验证码</label>
              <div class="flex gap-2">
                <div class="relative flex-1">
                  <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline text-[18px]">pin</span>
                  <input
                    v-model="code"
                    type="text"
                    maxlength="6"
                    placeholder="123456"
                    class="w-full bg-surface-container-low/60 border border-outline-variant rounded-full py-2 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                    :class="{ 'border-error focus:border-error focus:ring-error': fieldErrors.code }"
                    @input="clearFieldError('code')"
                  />
                </div>
                <button
                  type="button"
                  :disabled="codeLoading || codeCountdown > 0"
                  class="px-3 rounded-full bg-primary-container text-on-primary-container text-label-md font-semibold hover:opacity-90 transition-opacity disabled:opacity-60 whitespace-nowrap"
                  @click="handleSendCode"
                >
                  {{ codeCountdown > 0 ? `${codeCountdown}s` : (codeLoading ? '发送中' : '获取') }}
                </button>
              </div>
              <p v-if="fieldErrors.code" class="text-label-sm text-error pl-3">{{ fieldErrors.code }}</p>
            </div>

            <div class="flex flex-col gap-1">
              <label class="text-label-md text-on-surface-variant">密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline text-[18px]">lock</span>
                <input
                  v-model="password"
                  type="password"
                  placeholder="••••••••"
                  autocomplete="new-password"
                  class="w-full bg-surface-container-low/60 border border-outline-variant rounded-full py-2 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                  :class="{ 'border-error focus:border-error focus:ring-error': fieldErrors.password }"
                  @input="clearFieldError('password')"
                />
              </div>
              <p v-if="fieldErrors.password" class="text-label-sm text-error pl-3">{{ fieldErrors.password }}</p>
            </div>

            <div class="flex flex-col gap-1">
              <label class="text-label-md text-on-surface-variant">确认密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-outline text-[18px]">lock</span>
                <input
                  v-model="confirmPassword"
                  type="password"
                  placeholder="••••••••"
                  autocomplete="new-password"
                  class="w-full bg-surface-container-low/60 border border-outline-variant rounded-full py-2 pl-10 pr-4 focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all text-on-surface"
                  :class="{ 'border-error focus:border-error focus:ring-error': fieldErrors.confirmPassword }"
                  @input="clearFieldError('confirmPassword')"
                />
              </div>
              <p v-if="fieldErrors.confirmPassword" class="text-label-sm text-error pl-3">{{ fieldErrors.confirmPassword }}</p>
            </div>

            <div v-if="errorMsg" class="text-center text-label-md text-error bg-error-container/50 rounded-lg py-1">{{ errorMsg }}</div>

            <label class="flex items-start gap-2 cursor-pointer">
              <input v-model="agree" type="checkbox" class="w-4 h-4 mt-0.5 rounded border-outline-variant text-primary focus:ring-primary" @change="clearFieldError('agree')" />
              <span class="text-label-md text-on-surface-variant leading-snug">我已阅读并同意《用户协议》和《隐私政策》</span>
            </label>
            <p v-if="fieldErrors.agree" class="text-label-sm text-error pl-3">{{ fieldErrors.agree }}</p>

            <button
              type="submit"
              :disabled="loading"
              class="w-full bg-primary text-on-primary rounded-full py-2.5 text-headline-sm hover:scale-[1.02] active:scale-95 transition-transform disabled:opacity-60 disabled:scale-100 shadow-[0_4px_14px_rgba(16,185,129,0.28)]"
            >
              {{ loading ? '注册中...' : '注 册' }}
            </button>
          </form>

          <div class="mt-4 text-center">
            <button type="button" class="text-label-md text-primary hover:opacity-80" @click="goLogin">
              已有账号？去登录
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
