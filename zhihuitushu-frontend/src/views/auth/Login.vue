<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-left">
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

      <div class="login-right">
        <div class="login-card">
          <h2 class="card-title">欢迎回来</h2>
          <p class="card-subtitle">请登录您的账号</p>

          <el-form ref="loginForm" :model="form" :rules="rules" class="login-form">
            <el-form-item prop="account">
              <el-input
                v-model="form.account"
                placeholder="手机号或邮箱"
                size="large"
                prefix-icon="User"
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="密码"
                size="large"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <div class="form-row">
                <el-checkbox v-model="form.rememberMe">记住我</el-checkbox>
                <router-link to="/reset-password" class="forgot-link">忘记密码？</router-link>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="login-btn"
                :loading="loading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>

          <div class="divider">
            <span>或</span>
          </div>

          <div class="register-link">
            <span>还没有账号？</span>
            <router-link to="/register">立即注册</router-link>
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
const loginForm = ref(null)
const loading = ref(false)

const form = reactive({
  account: '',
  password: '',
  rememberMe: false,
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
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度至少8位', trigger: 'blur' },
  ],
}

const handleLogin = async () => {
  const valid = await loginForm.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
      credentials: 'include',
    })
    const data = await response.json()
    if (data.code === 200) {
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(data.message || '登录失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #FFFBEB 0%, #FEF3C7 50%, #FDE68A 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  display: flex;
  width: 100%;
  max-width: 900px;
  height: 500px;
  background: #FFFFFF;
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  overflow: hidden;
}

.login-left {
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

.login-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-card {
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

.login-form {
  margin-bottom: 24px;
}

.form-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-link {
  font-size: 14px;
  color: #92400E;
  text-decoration: none;
}

.forgot-link:hover {
  text-decoration: underline;
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.login-btn:hover {
  background: linear-gradient(135deg, #78350F 0%, #B45309 100%);
}

.divider {
  display: flex;
  align-items: center;
  margin: 20px 0;
  color: #94A3B8;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #E2E8F0;
}

.divider span {
  padding: 0 16px;
  font-size: 14px;
}

.register-link {
  display: flex;
  justify-content: center;
  gap: 8px;
  font-size: 14px;
  color: #64748B;
}

.register-link a {
  color: #92400E;
  font-weight: 600;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    height: auto;
  }

  .login-left {
    padding: 40px 30px;
  }

  .login-right {
    padding: 40px 30px;
  }

  .feature-list {
    gap: 20px;
  }
}
</style>