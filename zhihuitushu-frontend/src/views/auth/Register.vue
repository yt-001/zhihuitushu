<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-left">
        <div class="logo-section">
          <div class="logo">
            <Notebook class="logo-icon" />
          </div>
          <h1 class="title">智慧图书阅读平台</h1>
          <p class="subtitle">开启您的智慧阅读之旅</p>
        </div>
        <div class="feature-list">
          <div class="feature-item">
            <Lightning class="feature-icon" />
            <span>AI智能推荐</span>
          </div>
          <div class="feature-item">
            <Headset class="feature-icon" />
            <span>语音阅读</span>
          </div>
          <div class="feature-item">
            <View class="feature-icon" />
            <span>无障碍阅读</span>
          </div>
        </div>
      </div>

      <div class="register-right">
        <div class="register-card">
          <h2 class="card-title">创建账号</h2>
          <p class="card-subtitle">开始您的阅读之旅</p>

          <el-form ref="registerForm" :model="form" :rules="rules" class="register-form">
            <el-form-item prop="account">
              <el-input
                v-model="form.account"
                placeholder="手机号或邮箱"
                size="large"
                prefix-icon="User"
              />
            </el-form-item>

            <el-form-item prop="code">
              <div class="code-input-wrapper">
                <el-input
                  v-model="form.code"
                  placeholder="验证码"
                  size="large"
                  prefix-icon="Message"
                />
                <el-button
                  type="primary"
                  size="large"
                  class="code-btn"
                  :disabled="countdown > 0"
                  @click="sendCode"
                >
                  {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
                </el-button>
              </div>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="密码（至少8位，包含大小写字母和数字）"
                size="large"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="确认密码"
                size="large"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <el-checkbox v-model="form.agreed">
                我已阅读并同意
                <a href="#" class="agreement-link">《用户协议》</a>
                和
                <a href="#" class="agreement-link">《隐私政策》</a>
              </el-checkbox>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="register-btn"
                :loading="loading"
                @click="handleRegister"
              >
                注册
              </el-button>
            </el-form-item>
          </el-form>

          <div class="login-link">
            <span>已有账号？</span>
            <router-link to="/login">立即登录</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Notebook, Lightning, Headset, View } from '@element-plus/icons-vue'

const router = useRouter()
const registerForm = ref(null)
const loading = ref(false)
const countdown = ref(0)

const form = reactive({
  account: '',
  code: '',
  password: '',
  confirmPassword: '',
  agreed: false,
})

const rules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const phoneRegex = /^1[3-9]\d{9}$/
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        if (value && !phoneRegex.test(value) && !emailRegex.test(value)) {
          callback(new Error('请输入有效的手机号或邮箱'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度至少8位', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/
        if (value && !regex.test(value)) {
          callback(new Error('密码需包含大小写字母和数字'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
  agreed: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请同意用户协议和隐私政策'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
}

const sendCode = async () => {
  const valid = await registerForm.value.validateField('account')
  if (!valid) return

  try {
    const response = await fetch('/api/auth/send-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ account: form.account, type: 'register' }),
    })
    const data = await response.json()
    if (data.code === 200) {
      ElMessage.success('验证码发送成功')
      countdown.value = 60
      const timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    } else {
      ElMessage.error(data.message || '发送失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  }
}

const handleRegister = async () => {
  const valid = await registerForm.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const response = await fetch('/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
      credentials: 'include',
    })
    const data = await response.json()
    if (data.code === 200) {
      ElMessage.success('注册成功')
      router.push('/')
    } else {
      ElMessage.error(data.message || '注册失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #FFFBEB 0%, #FEF3C7 50%, #FDE68A 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-container {
  display: flex;
  width: 100%;
  max-width: 900px;
  height: 600px;
  background: #FFFFFF;
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  overflow: hidden;
}

.register-left {
  flex: 1;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  padding: 60px 40px;
  color: #FFFFFF;
  display: flex;
  flex-direction: column;
}

.logo-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.logo {
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
}

.logo-icon {
  width: 50px;
  height: 50px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 16px;
  opacity: 0.8;
}

.feature-list {
  display: flex;
  gap: 30px;
  justify-content: center;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.feature-icon {
  width: 32px;
  height: 32px;
  opacity: 0.9;
}

.register-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.register-card {
  width: 100%;
}

.card-title {
  font-size: 24px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 4px;
}

.card-subtitle {
  font-size: 14px;
  color: #64748B;
  margin-bottom: 32px;
}

.register-form {
  margin-bottom: 24px;
}

.code-input-wrapper {
  display: flex;
  gap: 12px;
}

.code-input-wrapper .el-input {
  flex: 1;
}

.code-btn {
  width: 140px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.code-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #78350F 0%, #B45309 100%);
}

.code-btn:disabled {
  background: #CBD5E1;
}

.agreement-link {
  color: #92400E;
  text-decoration: none;
}

.agreement-link:hover {
  text-decoration: underline;
}

.register-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.register-btn:hover {
  background: linear-gradient(135deg, #78350F 0%, #B45309 100%);
}

.login-link {
  display: flex;
  justify-content: center;
  gap: 8px;
  font-size: 14px;
  color: #64748B;
}

.login-link a {
  color: #92400E;
  font-weight: 600;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-container {
    flex-direction: column;
    height: auto;
  }

  .register-left {
    padding: 40px 30px;
  }

  .register-right {
    padding: 40px 30px;
  }

  .feature-list {
    gap: 20px;
  }
}
</style>