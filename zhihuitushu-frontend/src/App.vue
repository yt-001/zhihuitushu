<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getCurrentUser } from '@/api/auth.js'
import { setUser, clearToken } from '@/stores/auth.js'
import BottomNavBar from './components/BottomNavBar.vue'

const route = useRoute()
const hideNavRoutes = ['login', 'register']

onMounted(async () => {
  try {
    const res = await getCurrentUser()
    if (res.data) {
      setUser(res.data)
    }
  } catch (e) {
    clearToken()
  }
})
</script>

<template>
  <div class="mobile-container relative min-h-full overflow-x-hidden">
    <!-- Soft green background blobs -->
    <div class="fixed inset-0 z-[-1] pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[60%] h-[50%] bg-primary-container opacity-25 blur-[100px] rounded-full mix-blend-multiply" />
      <div class="absolute bottom-[-10%] right-[-10%] w-[70%] h-[60%] bg-tertiary-container opacity-20 blur-[120px] rounded-full mix-blend-multiply" />
      <div class="absolute top-[30%] right-[10%] w-[40%] h-[40%] bg-secondary-container opacity-20 blur-[90px] rounded-full mix-blend-overlay" />
    </div>

    <main :class="hideNavRoutes.includes(route.name) ? 'pb-0' : 'pb-20'">
      <RouterView v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </RouterView>
    </main>

    <BottomNavBar v-if="!hideNavRoutes.includes(route.name)" />
  </div>
</template>
