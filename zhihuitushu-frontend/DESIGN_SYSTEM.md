# Melody Flow 设计规范文档

## 目录

1. [设计风格概述](#设计风格概述)
2. [颜色规范](#颜色规范)
3. [排版规范](#排版规范)
4. [间距规范](#间距规范)
5. [视觉效果规范](#视觉效果规范)
6. [组件规范](#组件规范)
7. [响应式设计规范](#响应式设计规范)
8. [最佳实践和注意事项](#最佳实践和注意事项)

---

## 设计风格概述

Melody Flow 是一款音乐流媒体应用，采用现代、清新的设计语言。设计风格特点：

- **毛玻璃拟态风格**：采用半透明背景 + 模糊效果，营造轻盈、现代的视觉体验
- **有机形态设计**：使用不规则圆角和流动的渐变背景，增添趣味性和活力
- **Material Design 3 色彩体系**：遵循 M3 设计规范，使用完整的色彩语义系统
- **移动优先设计**：以移动端体验为核心，优化触控交互和屏幕空间利用

---

## 颜色规范

### 主色调

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| Primary | `primary` | `#b3272e` | 主要操作按钮、激活状态、强调元素 |
| Primary Container | `primary-container` | `#ff5e5e` | 主色容器背景、标签背景 |
| On Primary | `on-primary` | `#ffffff` | 主色背景上的文本和图标 |
| On Primary Container | `on-primary-container` | `#63000c` | 主色容器背景上的文本 |

### 次要色调

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| Secondary | `secondary` | `#825500` | 次要操作、图标颜色 |
| Secondary Container | `secondary-container` | `#fdb64a` | 次色容器背景（金橙色） |
| On Secondary Container | `on-secondary-container` | `#704800` | 次色容器背景上的文本 |

### 三级色调

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| Tertiary | `tertiary` | `#006495` | 第三强调色（蓝色） |
| Tertiary Container | `tertiary-container` | `#249de2` | 三级色容器背景 |
| On Tertiary Container | `on-tertiary-container` | `#00314c` | 三级色容器背景上的文本 |

### 表面色系统

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| Background | `background` | `#f7f9ff` | 页面背景色 |
| Surface | `surface` | `#f7f9ff` | 基础表面色 |
| Surface Bright | `surface-bright` | `#f7f9ff` | 高亮表面 |
| Surface Dim | `surface-dim` | `#cadcf0` | 暗淡表面 |
| Surface Container | `surface-container` | `#e2efff` | 容器背景色 |
| Surface Container Low | `surface-container-low` | `#ecf4ff` | 低层级容器 |
| Surface Container High | `surface-container-high` | `#d8eafe` | 高层级容器 |
| Surface Container Highest | `surface-container-highest` | `#d2e4f8` | 最高层级容器 |
| Surface Container Lowest | `surface-container-lowest` | `#ffffff` | 最低层级容器 |

### 文本与图标色

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| On Background | `on-background` | `#0b1d2b` | 背景上的文本 |
| On Surface | `on-surface` | `#0b1d2b` | 表面上的主要文本 |
| On Surface Variant | `on-surface-variant` | `#5a413f` | 表面上的次要文本 |
| Outline | `outline` | `#8e706e` | 边框、分隔线 |
| Outline Variant | `outline-variant` | `#e2bebc` | 浅色边框 |

### 错误状态色

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| Error | `error` | `#ba1a1a` | 错误状态、警告 |
| Error Container | `error-container` | `#ffdad6` | 错误容器背景 |
| On Error | `on-error` | `#ffffff` | 错误背景上的文本 |
| On Error Container | `on-error-container` | `#93000a` | 错误容器上的文本 |

### 反色系统

| 颜色名称 | 变量名 | 色值 | 用途 |
|---------|--------|------|------|
| Inverse Surface | `inverse-surface` | `#213241` | 反转表面（深色模式用） |
| Inverse On Surface | `inverse-on-surface` | `#e7f2ff` | 反转表面上的文本 |
| Inverse Primary | `inverse-primary` | `#ffb3af` | 反转主色 |

---

## 排版规范

### 字体家族

所有文本统一使用 **Plus Jakarta Sans** 字体家族。

```css
font-family: 'Plus Jakarta Sans', sans-serif;
```

### 字体尺寸系统

| 类型 | 变量名 | 字号 | 行高 | 字重 | 字间距 | 用途 |
|------|--------|------|------|------|--------|------|
| Display Large | `display-lg` | 24px | 32px | 800 (Extrabold) | -0.02em | 页面标题、品牌名称 |
| Display Large Mobile | `display-lg-mobile` | 20px | 28px | 800 (Extrabold) | -0.02em | 移动端页面标题 |
| Headline Medium | `headline-md` | 20px | 28px | 700 (Bold) | - | 区块标题 |
| Headline Small | `headline-sm` | 18px | 24px | 700 (Bold) | - | 小节标题、卡片标题 |
| Body Large | `body-lg` | 16px | 24px | 500 (Medium) | - | 强调正文 |
| Body Medium | `body-md` | 14px | 20px | 400 (Regular) | - | 常规正文、描述文本 |
| Label Medium | `label-md` | 12px | 16px | 600 (Semibold) | 0.01em | 标签、按钮文本 |
| Label Small | `label-sm` | 10px | 14px | 500 (Medium) | - | 小标签、辅助信息 |

### 使用示例

```html
<!-- 页面标题 -->
<h1 class="font-display-lg text-display-lg">Melody Flow</h1>

<!-- 区块标题 -->
<h2 class="font-headline-md text-headline-md">Staff Picks</h2>

<!-- 正文 -->
<p class="font-body-md text-body-md">Describe the vibe...</p>

<!-- 标签 -->
<span class="font-label-sm text-label-sm">New Release</span>
```

---

## 间距规范

### 语义化间距变量

| 变量名 | 值 | 用途 |
|--------|-----|------|
| `container-margin` | 20px | 页面容器左右边距 |
| `card-padding` | 16px | 卡片内边距 |
| `stack-gap-lg` | 24px | 大型垂直堆叠间距（section 之间） |
| `stack-gap-md` | 16px | 中等垂直堆叠间距（元素组之间） |
| `stack-gap-sm` | 8px | 小型垂直堆叠间距（紧密元素之间） |
| `inline-gap` | 12px | 水平排列元素间距 |

### 使用原则

```html
<!-- 页面主容器 -->
<main class="px-container-margin">
  <!-- Section 之间 -->
  <section class="flex flex-col gap-stack-gap-lg">
    <!-- 元素组之间 -->
    <div class="flex flex-col gap-stack-gap-md">
      <!-- 紧密元素 -->
      <div class="flex gap-stack-gap-sm">...</div>
    </div>
  </section>
</main>
```

### 圆角系统

| 变量名 | 值 | 用途 |
|--------|-----|------|
| `rounded-lg` | 8px (0.5rem) | 小型元素（按钮、标签） |
| `rounded-xl` | 12px (0.75rem) | 中型元素（输入框、小卡片） |
| `rounded-[16px]` | 16px | 中大型卡片 |
| `rounded-[20px]` | 20px | 大型卡片 |
| `rounded-[24px]` | 24px | 超大卡片、模态框 |
| `rounded-full` | 9999px | 完全圆形（按钮、头像） |

---

## 视觉效果规范

### 毛玻璃效果

项目中定义了两种毛玻璃效果：

#### 1. Glass Panel（面板效果）

```css
.glass-panel {
  background: rgba(255, 255, 255, 0.45);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}
```

**用途**：轻量级毛玻璃，用于底部导航栏、播放栏

#### 2. Glass Card（卡片效果）

```css
.glass-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
}
```

**用途**：卡片容器，带有轻微阴影

### 渐变背景

#### 页面渐变背景

```css
background: linear-gradient(135deg, #fff7d6 0%, #e0f2fe 100%);
```

从暖黄到淡蓝的对角渐变，营造温暖、清新的氛围。

#### 有机形态背景

```html
<div class="absolute inset-0 z-[-1] pointer-events-none overflow-hidden">
  <!-- 次色容器光晕 -->
  <div class="absolute top-[-10%] left-[-10%] w-[60%] h-[50%] 
              bg-secondary-container opacity-20 blur-[100px] 
              rounded-full mix-blend-multiply"></div>
  <!-- 三级色容器光晕 -->
  <div class="absolute bottom-[-10%] right-[-10%] w-[70%] h-[60%] 
              bg-tertiary-container opacity-20 blur-[120px] 
              rounded-full mix-blend-multiply"></div>
  <!-- 主色容器光晕 -->
  <div class="absolute top-[30%] right-[10%] w-[40%] h-[40%] 
              bg-primary-container opacity-15 blur-[90px] 
              rounded-full mix-blend-overlay"></div>
</div>
```

使用大量模糊的彩色光球，创造出流动、有机的背景氛围。

### 有机形态动画

```css
.wobbly-blob {
  animation: wobble 10s ease-in-out infinite alternate;
}

@keyframes wobble {
  0% { border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%; transform: rotate(0deg); }
  50% { border-radius: 30% 60% 70% 40% / 50% 60% 30% 60%; transform: rotate(180deg); }
  100% { border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%; transform: rotate(360deg); }
}
```

### 唱片效果

```css
.vinyl-peak {
  position: absolute;
  right: -10px;
  top: 50%;
  transform: translateY(-50%);
  width: 80%;
  height: 90%;
  background: radial-gradient(circle, 
    #1a1a1a 30%, #333 40%, #1a1a1a 50%, #333 60%, #1a1a1a 70%);
  border-radius: 50%;
  z-index: -1;
  box-shadow: -5px 0 15px rgba(0,0,0,0.2);
}

.media-card:hover .vinyl-peak {
  transform: translateY(-50%) translateX(15px) rotate(45deg);
}
```

模拟黑胶唱片从卡片滑出的效果，hover 时触发动画。

### 阴影系统

| 阴影类型 | CSS | 用途 |
|---------|-----|------|
| 轻柔阴影 | `shadow-[0_10px_30px_rgba(0,0,0,0.05)]` | 卡片、面板 |
| 中等阴影 | `shadow-md` | 按钮、激活状态 |
| 主色阴影 | `shadow-[0_4px_14px_rgba(255,94,94,0.3)]` | 主色按钮 |
| 深色阴影 | `shadow-[0_10px_20px_rgba(179,39,46,0.3)]` | 播放按钮 |

---

## 组件规范

### 1. TopAppBar（顶部导航栏）

**设计特点**：
- 固定定位（`fixed top-0`），透明背景
- 层级 `z-40`
- 左侧：搜索/返回按钮（主色 primary）
- 中间：Logo 或标题（display-lg 字号）
- 右侧：通知按钮（主色 primary）
- 支持标签导航切换

**样式规范**：
```html
<header class="fixed top-0 w-full z-40 bg-transparent flex flex-col 
               gap-stack-gap-sm px-container-margin pt-4">
  <!-- 左侧图标 -->
  <button class="text-primary hover:opacity-80 transition-opacity 
                 scale-105 active:scale-95 transition-transform">
    <span class="material-symbols-outlined">search</span>
  </button>
  
  <!-- 标题 -->
  <h1 class="font-display-lg text-display-lg font-extrabold text-on-surface">
    Melody Flow
  </h1>
  
  <!-- 激活标签 -->
  <button class="text-on-primary bg-primary rounded-full">
    Tab Label
  </button>
  
  <!-- 非激活标签 -->
  <button class="text-on-surface-variant hover:opacity-80">
    Tab Label
  </button>
</header>
```

### 2. BottomNavBar（底部导航栏）

**设计特点**：
- 固定定位（`fixed bottom-0`），毛玻璃背景
- 层级 `z-50`
- 圆角顶部（`rounded-t-xl`）
- 安全区域适配（`pb-safe`）
- 当前页面图标填充，放大 1.1 倍
- 底部小圆点指示当前页面

**样式规范**：
```html
<nav class="fixed bottom-0 left-0 w-full z-50 flex justify-around 
            items-center px-4 py-3 pb-safe 
            bg-surface-lowest/60 backdrop-blur-xl 
            border-t border-white/30 rounded-t-xl shadow-lg">
  <!-- 导航项 -->
  <button class="flex flex-col items-center gap-1 
                 text-primary scale-110">
    <span class="material-symbols-outlined text-[24px]" 
          style="font-variation-settings: 'FILL' 1;">
      home
    </span>
    <span class="font-label-sm text-label-sm">Home</span>
    <div class="absolute -bottom-2 w-1.5 h-1.5 bg-primary rounded-full"></div>
  </button>
</nav>
```

### 3. GlassCard（玻璃卡片）

**设计特点**：
- 可配置圆角（sm/md/lg/xl/full）
- 可配置内边距（none/sm/md/lg/card）
- 支持 hover 效果（缩放 1.02 倍）
- 可选阴影效果

**Props**：
- `radius`: 圆角大小（默认 'xl'）
- `padding`: 内边距（默认 'card'）
- `hover`: 是否启用 hover 效果（默认 false）
- `shadow`: 是否显示阴影（默认 false）

**样式映射**：
```
radius: sm → rounded-lg (8px)
        md → rounded-xl (12px)
        lg → rounded-[20px] (20px)
        xl → rounded-[24px] (24px)
        full → rounded-full

padding: none → 无
         sm → p-2 (8px)
         md → p-3 (12px)
         lg → p-4 (16px)
         card → p-card-padding (16px)
```

### 4. NowPlayingBar（播放栏）

**设计特点**：
- 固定定位在底部导航栏上方（`bottom-[76px]`）
- 毛玻璃背景（`bg-white/70`）
- 左侧：专辑封面（圆形，带边框）
- 中间：歌曲信息（可截断）
- 右侧：操作按钮（喜欢、播放/暂停、下一首）

**样式规范**：
```html
<div class="fixed bottom-[76px] left-4 right-4 z-40">
  <div class="glass-panel rounded-[24px] p-2 flex items-center gap-3 
              shadow-[0_10px_30px_rgba(0,0,0,0.05)] bg-white/70">
    <!-- 专辑封面 -->
    <div class="w-12 h-12 rounded-full overflow-hidden shrink-0 
                border-2 border-white shadow-sm">
      <img src="..." class="w-full h-full object-cover" />
    </div>
    
    <!-- 歌曲信息 -->
    <div class="flex-1 overflow-hidden">
      <div class="font-body-md text-body-md font-bold text-on-surface truncate">
        Song Title
      </div>
      <div class="font-label-sm text-label-sm text-on-surface-variant truncate">
        Artist Name
      </div>
    </div>
    
    <!-- 播放按钮 -->
    <button class="w-10 h-10 rounded-full bg-primary text-white 
                   flex items-center justify-center shadow-md 
                   scale-105 hover:scale-110 transition-transform">
      <span class="material-symbols-outlined">play_arrow</span>
    </button>
  </div>
</div>
```

### 5. 按钮系统

#### 主要按钮（Primary Button）
```html
<button class="bg-primary text-on-primary rounded-full py-3 px-6 
               font-headline-sm text-headline-sm
               hover:scale-[1.02] active:scale-95 transition-transform
               shadow-[0_4px_14px_rgba(255,94,94,0.3)]">
  Primary Action
</button>
```

#### 次要按钮（Secondary Button）
```html
<button class="bg-primary-container text-on-primary-container 
               rounded-full py-3 px-6
               font-headline-sm text-headline-sm
               hover:opacity-90 active:scale-95 transition-all
               shadow-[0_4px_14px_rgba(255,94,94,0.3)]">
  Secondary Action
</button>
```

#### 图标按钮（Icon Button）
```html
<button class="w-10 h-10 rounded-full border border-primary text-primary 
               flex items-center justify-center 
               hover:bg-primary hover:text-white transition-colors">
  <span class="material-symbols-outlined">play_arrow</span>
</button>
```

#### 标签按钮（Chip Button）
```html
<button class="px-3 py-1.5 rounded-full font-label-md text-label-md
               :class="[
                 active ? 'bg-primary text-on-primary shadow-md scale-105' 
                         : 'glass-card text-on-surface-variant hover:bg-white/40'
               ]">
  Chip Label
</button>
```

### 6. 输入框系统

#### 文本输入框
```html
<div class="relative">
  <!-- 图标 -->
  <span class="material-symbols-outlined absolute left-3 top-1/2 
               -translate-y-1/2 text-outline">mail</span>
  
  <!-- 输入框 -->
  <input 
    class="w-full bg-surface-container-lowest/60 
           border border-outline-variant rounded-full 
           py-3 pl-10 pr-4 
           focus:outline-none focus:border-primary 
           focus:ring-1 focus:ring-primary transition-all 
           text-on-surface font-body-lg"
    placeholder="placeholder text"
    type="email"
  />
</div>
```

#### 文本域
```html
<textarea 
  class="w-full bg-white/60 border border-white/40 rounded-xl p-3 
         font-body-md text-body-md text-on-surface 
         focus:outline-none focus:ring-2 focus:ring-primary/50 
         resize-none h-24"
  placeholder="placeholder text"
></textarea>
```

### 7. 卡片系统

#### 水平卡片（Media Card）
```html
<div class="glass-panel rounded-[20px] p-4 flex gap-4 items-center 
            cursor-pointer hover:bg-white/50 transition-colors">
  <!-- 封面 -->
  <div class="w-20 h-20 rounded-xl overflow-hidden shrink-0 relative">
    <img src="..." class="w-full h-full object-cover" />
  </div>
  
  <!-- 内容 -->
  <div class="flex-1">
    <h4 class="font-body-lg text-body-lg font-bold text-on-surface mb-1">
      Title
    </h4>
    <p class="font-label-md text-label-md text-on-surface-variant mb-2">
      Subtitle
    </p>
  </div>
  
  <!-- 操作 -->
  <button class="w-10 h-10 rounded-full border border-primary text-primary">
    <span class="material-symbols-outlined">play_arrow</span>
  </button>
</div>
```

#### 垂直卡片（Playlist Card）
```html
<div class="min-w-[280px] glass-panel rounded-xl p-card-padding 
            flex flex-col gap-stack-gap-sm 
            hover:scale-[1.02] transition-transform 
            shadow-[0_10px_30px_rgba(0,0,0,0.05)] cursor-pointer">
  <!-- 封面 -->
  <div class="w-full aspect-[4/3] rounded-lg overflow-hidden relative">
    <img src="..." class="w-full h-full object-cover" />
  </div>
  
  <!-- 内容 -->
  <div class="flex flex-col mt-2">
    <h3 class="font-body-lg text-body-lg text-on-surface truncate">Title</h3>
    <p class="font-body-md text-body-md text-on-surface-variant truncate">
      Artist
    </p>
  </div>
</div>
```

---

## 响应式设计规范

### 移动端设计原则

1. **固定导航结构**
   - 顶部导航栏固定（`sticky top-0`）
   - 底部导航栏固定（`fixed bottom-0`）
   - 内容区域预留底部空间（`pb-24` 或 `pb-[140px]`）

2. **安全区域适配**
   ```css
   .pb-safe {
     padding-bottom: calc(12px + env(safe-area-inset-bottom));
   }
   ```

3. **横向滚动容器**
   ```html
   <div class="flex overflow-x-auto no-scrollbar gap-inline-gap 
               px-container-margin snap-x snap-mandatory pb-4">
     <!-- 使用 snap-center 实现滑动吸附 -->
     <div class="snap-center shrink-0 min-w-[280px]">...</div>
   </div>
   ```

4. **隐藏滚动条**
   ```css
   .no-scrollbar::-webkit-scrollbar {
     display: none;
   }
   .no-scrollbar {
     -ms-overflow-style: none;
     scrollbar-width: none;
   }
   ```

5. **触控优化**
   - 最小触控区域：44x44px
   - 按钮内边距：`py-3`（12px 垂直）
   - 激活状态缩放：`active:scale-95`
   - Hover 状态缩放：`hover:scale-[1.02]`

### 内容层级结构

```
页面容器 (min-h-screen, pb-24)
├── 背景层 (absolute, z-[-1])
│   ├── 有机形态光球
│   └── 渐变背景
├── 顶部导航 (fixed, z-40)
├── 主内容区 (px-container-margin, gap-stack-gap-lg)
│   ├── Section 1
│   │   ├── 标题
│   │   └── 内容网格/列表
│   └── Section 2
├── 播放栏 (fixed, z-40)
└── 底部导航 (fixed, z-50)
```

---

## 最佳实践和注意事项

### 1. 颜色使用规范

✅ **正确做法**：
- 使用语义化颜色变量（`text-on-surface` 而非 `text-gray-900`）
- 遵循 Material Design 3 的颜色层级关系
- 保持颜色对比度符合可访问性标准

❌ **避免**：
- 硬编码颜色值（如 `text-gray-500`）
- 混用不同色彩系统的变量
- 在主色背景上使用低对比度文本

### 2. 排版使用规范

✅ **正确做法**：
```html
<h1 class="font-display-lg text-display-lg">Title</h1>
<p class="font-body-md text-body-md">Description</p>
<span class="font-label-sm text-label-sm">Tag</span>
```

❌ **避免**：
```html
<!-- 不要混用 font- 和 text- 类 -->
<h1 class="font-display-lg text-headline-md">Title</h1>
<!-- 不要使用硬编码的字号 -->
<p class="text-[14px]">Description</p>
```

### 3. 间距使用规范

✅ **正确做法**：
- 使用语义化间距变量（`px-container-margin`）
- 遵循间距层级（sm < md < lg）
- 保持一致的间距节奏

❌ **避免**：
```html
<!-- 不要使用随意的间距值 -->
<div class="gap-3 px-5 py-2">...</div>
<!-- 应该使用 -->
<div class="gap-stack-gap-sm px-container-margin py-card-padding">...</div>
```

### 4. 毛玻璃效果使用

✅ **正确做法**：
- 在有背景内容的区域使用毛玻璃
- 保持适当的透明度（0.45-0.7）
- 添加半透明边框增强效果

❌ **避免**：
- 在纯色背景上使用毛玻璃（无效果）
- 过度使用毛玻璃影响性能
- 在小元素上使用过度模糊

### 5. 动画和过渡

✅ **正确做法**：
```html
<!-- 统一过渡时长 -->
<button class="transition-all duration-300 ease-out">...</button>
<!-- Hover 效果 -->
<div class="hover:scale-[1.02] active:scale-95 transition-transform">...</div>
```

❌ **避免**：
```html
<!-- 不要使用过长的过渡时间 -->
<button class="transition-all duration-1000">...</button>
<!-- 不要禁用过渡 -->
<button class="transition-none">...</button>
```

### 6. 图标使用

图标统一使用 Material Symbols Outlined 字体图标：

```html
<span class="material-symbols-outlined">home</span>

<!-- 填充图标 -->
<span class="material-symbols-outlined" 
      style="font-variation-settings: 'FILL' 1;">
  home
</span>

<!-- 自定义大小 -->
<span class="material-symbols-outlined text-[24px]">home</span>
```

### 7. 图片处理

✅ **正确做法**：
```html
<!-- 使用 object-cover 保持比例 -->
<img src="..." class="w-full h-full object-cover" alt="..." />
<!-- 使用 aspect-ratio 控制比例 -->
<div class="w-full aspect-[4/3]">
  <img src="..." class="w-full h-full object-cover" />
</div>
```

### 8. 无障碍性

✅ **正确做法**：
- 为按钮添加 `aria-label`
- 保持足够的颜色对比度（4.5:1 以上）
- 为表单元素添加 `label`
- 支持键盘导航

```html
<button aria-label="Search" class="...">
  <span class="material-symbols-outlined">search</span>
</button>
```

### 9. 性能优化

✅ **推荐做法**：
- 使用 CSS 变量和 Tailwind 类减少重复样式
- 合理使用 `will-change` 优化动画
- 延迟加载非关键资源
- 使用 `loading="lazy"` 懒加载图片

### 10. 代码组织

**推荐的 Vue 组件结构**：
```vue
<template>
  <!-- HTML 结构 -->
</template>

<script setup>
// 逻辑代码
</script>

<style scoped>
/* 组件特定样式（仅在无法用 Tailwind 表达时使用） */
</style>
```

---

## 总结

Melody Flow 设计系统遵循以下核心原则：

1. **一致性**：使用统一的颜色、排版、间距变量
2. **层次感**：通过毛玻璃、阴影、渐变创建视觉层次
3. **流畅性**：有机形态和动画增强用户体验
4. **移动优先**：优化触控交互和移动端布局
5. **可访问性**：保持足够的对比度和触控区域

遵循本设计规范可以确保整个应用的视觉一致性和用户体验质量。在进行后续页面重构时，请严格遵循上述规范，并参考已有页面的实现方式。