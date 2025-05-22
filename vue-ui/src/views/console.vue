<template>
  <div class="console-container">
    <el-card class="welcome-card">
      <div slot="header" class="welcome-header">
        <span>欢迎使用危化品信息管理系统</span>
      </div>
      <div class="welcome-content">
        <h2>您好，{{ userInfo.name }}！</h2>
        <p>您当前的身份是：<span class="user-role">{{ userInfo.userType === 1 ? '管理员' : '普通用户' }}</span></p>
        
        <div v-if="isAdmin" class="admin-info">
          <h3>管理员功能指南：</h3>
          <ul>
            <li>您可以管理系统中的所有员工信息</li>
            <li>您可以管理危化品的出入库记录</li>
            <li>您可以查看和处理安全预警信息</li>
            <li>您可以管理系统用户权限</li>
            <li>您拥有系统的全部操作权限</li>
          </ul>
        </div>
        
        <div v-else class="user-info">
          <h3>普通用户功能指南：</h3>
          <ul>
            <li>您可以查看危化品基本信息</li>
            <li>您可以查看库存信息</li>
            <li>您可以查看使用记录</li>
            <li>如需更多权限，请联系管理员</li>
          </ul>
        </div>
        
        <el-divider></el-divider>
        
        <div class="system-info">
          <h3>系统信息</h3>
          <p>当前版本：v0.8.0</p>
          <p>最后更新：2025-04-09</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "console",
  data() {
    return {
      userInfo: {
        name: '用户',
        userType: 0
      },
      currentDate: new Date().toLocaleDateString()
    }
  },
  computed: {
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
  }
}
</script>

<style scoped>
.console-container {
  padding: 20px;
}

.welcome-card {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.welcome-header {
  font-size: 18px;
  font-weight: bold;
}

.welcome-content {
  padding: 20px 0;
}

.user-role {
  font-weight: bold;
  color: #409EFF;
}

.admin-info, .user-info {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f8f8;
  border-radius: 4px;
}

.admin-info {
  border-left: 4px solid #67C23A;
}

.user-info {
  border-left: 4px solid #409EFF;
}

h3 {
  margin-top: 0;
  color: #303133;
}

ul {
  padding-left: 20px;
}

li {
  line-height: 1.8;
}

.system-info {
  color: #909399;
  font-size: 14px;
}
</style>
