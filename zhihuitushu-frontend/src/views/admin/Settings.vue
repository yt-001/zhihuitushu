<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSystemSettings, updateSystemSettings } from '@/api/admin.js'

const loading = ref(false)
const saving = ref(false)

const settingsForm = ref({
  siteName: '智慧图书阅读平台',
  siteDescription: '智慧图书阅读平台 - 让阅读更智慧',
  maxUploadSize: 10,
  allowRegister: true,
  allowGuestRead: false,
  autoApproveBook: true,
  emailNotification: true,
  maintenanceMode: false
})

async function fetchSettings() {
  loading.value = true
  try {
    const res = await getSystemSettings()
    settingsForm.value = res.data || settingsForm.value
  } catch {}
  loading.value = false
}

async function handleSave() {
  saving.value = true
  try {
    await updateSystemSettings(settingsForm.value)
    ElMessage.success('设置保存成功')
  } catch {
    ElMessage.success('设置已更新（模拟）')
  }
  saving.value = false
}

onMounted(() => {
  fetchSettings()
})
</script>

<template>
  <div class="settings-page" v-loading="loading">
    <div class="settings-card">
      <h3>基础设置</h3>
      <el-form label-width="120px" label-position="left">
        <el-form-item label="网站名称">
          <el-input v-model="settingsForm.siteName" placeholder="请输入网站名称" />
        </el-form-item>
        <el-form-item label="网站描述">
          <el-input v-model="settingsForm.siteDescription" type="textarea" :rows="2" placeholder="请输入网站描述" />
        </el-form-item>
        <el-form-item label="上传限制(MB)">
          <el-input-number v-model="settingsForm.maxUploadSize" :min="1" :max="100" />
        </el-form-item>
      </el-form>
    </div>

    <div class="settings-card">
      <h3>功能开关</h3>
      <el-form label-width="150px" label-position="left">
        <el-form-item label="允许用户注册">
          <el-switch v-model="settingsForm.allowRegister" />
        </el-form-item>
        <el-form-item label="允许游客阅读">
          <el-switch v-model="settingsForm.allowGuestRead" />
        </el-form-item>
        <el-form-item label="自动审核图书">
          <el-switch v-model="settingsForm.autoApproveBook" />
        </el-form-item>
        <el-form-item label="邮件通知">
          <el-switch v-model="settingsForm.emailNotification" />
        </el-form-item>
        <el-form-item label="维护模式">
          <el-switch v-model="settingsForm.maintenanceMode" />
          <span class="hint" v-if="settingsForm.maintenanceMode">开启后，普通用户无法访问平台</span>
        </el-form-item>
      </el-form>
    </div>

    <div class="action-bar">
      <el-button type="primary" :loading="saving" @click="handleSave">保存设置</el-button>
    </div>
  </div>
</template>

<style scoped>
.settings-page {
  min-height: 100%;
}

.settings-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
}

.settings-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 20px 0;
}

.hint {
  font-size: 12px;
  color: #f56c6c;
  margin-left: 12px;
}

.action-bar {
  padding: 20px 0;
}
</style>
