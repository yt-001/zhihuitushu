<template>
  <div class="reset-page">
    <div class="reset-container">
      <div class="reset-card">
        <div class="back-link" @click="$router.go(-1)">
          <ArrowLeft class="back-icon" />
          <span>返回</span>
        </div>

        <div class="logo-section">
          <div class="logo">
            <Key class="logo-icon" />
          </div>
          <h1 class="title">找回密码</h1>
          <p class="subtitle">请通过手机号或邮箱找回您的密码</p>
        </div>

        <el-form ref="resetForm" :model="form" :rules="rules" class="reset-form">
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

          <el-form-item prop="newPassword">
            <el-input
              v-model="form.newPassword"
              type="password"
              placeholder="新密码（至少8位，包含大小写字母和数字）"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="reset-btn"
              :loading="loading"
              @click="handleReset"
            >
              确认修改
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-link">
          <router-link to="/login">返回登录页面</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Key } from '@element-plus/icons-vue'

const router = useRouter()
const resetForm = ref(null)
const loading = ref(false)
const countdown = ref(0)

const form = reactive({
  account: '',
  code: '',
  newPassword: '',
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
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
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
}

const sendCode = async () => {
  const valid = await resetForm.value.validateField('account')
  if (!valid) return

  try {
    const response = await fetch('/api/auth/send-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ account: form.account, type: 'reset' }),
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

const handleReset = async () => {
  const valid = await resetForm.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const response = await fetch('/api/auth/reset-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
    })
    const data = await response.json()
    if (data.code === 200) {
      ElMessage.success('密码修改成功，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error(data.message || '修改失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.reset-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #FFFBEB 0%, #FEF3C7 50%, #FDE68A 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.reset-container {
  width: 100%;
  max-width: 420px;
}

.reset-card {
  background: #FFFFFF;
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  padding: 48px 40px;
}

.back-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  margin-bottom: 24px;
}

.back-link:hover {
  color: #92400E;
}

.back-icon {
  width: 16px;
  height: 16px;
}

.logo-section {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.logo-icon {
  width: 40px;
  height: 40px;
  color: #FFFFFF;
}

.title {
  font-size: 24px;
  font-weight: 700;
  color: #0F172A;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #64748B;
}

.reset-form {
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

.reset-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #92400E 0%, #D97706 100%);
  border: none;
}

.reset-btn:hover {
  background: linear-gradient(135deg, #78350F 0%, #B45309 100%);
}

.login-link {
  text-align: center;
}

.login-link a {
  color: #92400E;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .reset-card {
    padding: 40px 24px;
  }
}
</style>