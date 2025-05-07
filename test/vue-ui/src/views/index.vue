<template>
  <el-container>
    <!-- width的宽度跟collapse一样动态控制 -->
    <el-aside width="collapse">
      <div class="logo" v-show="open"><h3><i class="el-icon-eleme"></i>危化品信息管理系统</h3></div>
      <div class="logo" v-show="close"><h3><i class="el-icon-eleme"></i></h3></div>
      <!-- :collapse="isCollapse"  class="el-menu-vertical" 动态控制导航菜单的收起与展开  router：让index作为 path 进行路由跳转 -->
      <el-menu default-active="$route.path"
               router
               :default-openeds="openeds"
               :collapse="isCollapse"
               class="el-menu-vertical">
        <el-submenu index="1">
          <!-- 一级标题 -->
          <template slot="title">
            <i class="el-icon-s-tools"></i>
            <span slot="title">后台管理</span>
          </template>
          <!--  二级标题 -->
          <el-menu-item index="/console">
            <i class="el-icon-setting"></i>
            <span slot="title">控制台</span>
          </el-menu-item>
          <!-- 管理员可见菜单 -->
          <el-menu-item v-if="isAdmin" index="/man">
            <i class="el-icon-setting"></i>
            <span slot="title">员工管理</span>
          </el-menu-item>
          <el-menu-item index="/chemical">
            <i class="el-icon-setting"></i>
            <span slot="title">危化品管理</span>
          </el-menu-item>
          <el-menu-item index="/inventory">
            <i class="el-icon-setting"></i>
            <span slot="title">库存管理</span>
          </el-menu-item>
          <el-menu-item index="/storage">
            <i class="el-icon-setting"></i>
            <span slot="title">入库管理</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/outbound">
            <i class="el-icon-setting"></i>
            <span slot="title">出库管理</span>
          </el-menu-item>
          <el-menu-item index="/UseRecord">
            <i class="el-icon-setting"></i>
            <span slot="title">使用记录</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/warning">
            <i class="el-icon-setting"></i>
            <span slot="title">预警管理</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>

    </el-aside>
    <el-container>
      <el-header>
        <div class="trigger" @click="isShow">
          <!-- 点击展开收起导航和切换对应图标 -->
          <i class="el-icon-s-fold" v-show="open"></i>
          <i class="el-icon-s-unfold" v-show="close"></i>
        </div>
        <div class="user-info">
          <span>{{ userInfo.name }}</span>
          <span class="user-type">({{ userInfo.userType === 1 ? '管理员' : '普通用户' }})</span>
          <el-button type="text" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
      <el-footer>加纳</el-footer>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      openeds: ["1"],
      isCollapse: false, //导航栏默认为展开
      close: false, //第二个图标默认隐藏
      open: true, //默认显示第一个图标
      userInfo: {
        name: '',
        userType: 0
      }
    }
  },
  computed: {
    // 判断是否为管理员
    isAdmin() {
      return this.userInfo.userType === 1;
    }
  },
  created() {
    // 从sessionStorage获取用户信息
    const userStr = sessionStorage.getItem('user');
    if (userStr) {
      try {
        this.userInfo = JSON.parse(userStr);
      } catch (e) {
        console.error('解析用户信息失败', e);
      }
    }

    // 如果没有用户信息，跳转到登录页
    if (!this.userInfo || !this.userInfo.name) {
      this.$router.push('/login');
    }
  },
  methods: {
    isShow() {
      this.isCollapse = !this.isCollapse;
      this.open = !this.open;
      this.close = !this.close;
    },
    // 退出登录
    logout() {
      sessionStorage.removeItem('user');
      this.$router.push('/login');
      this.$message.success('已退出登录');
    }
  }
}
</script>

<style scoped>
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
  height: 100%;
  padding: 0 !important;
  display: flex;
  justify-content: space-between;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  height: 100vh;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
}

body > .el-container {
  margin-bottom: 40px;
}

.logo {
  height: 60px;
  line-height: 60px;
  background-color: antiquewhite;
  text-align: center;
}

.logo h3 {
  margin: 0;
  height: 60px;
}

.el-menu {
  border-right-width: 0;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 240px;
}

.trigger {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  width: 54px;
}

.trigger i {
  font-size: 20px;
}

.trigger:hover {
  background-color: rgb(203, 215, 230);
}

.user-info {
  margin-right: 20px;
  display: flex;
  align-items: center;
}

.user-type {
  margin: 0 10px;
  color: #606266;
}
</style>
