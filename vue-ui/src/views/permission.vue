<template>
  <div class="permission-container">
    <!-- 操作按钮 -->
    <div class="operation-container">
      <el-button type="primary" @click="handleAddUser">新增用户</el-button>
      <el-button type="success" @click="handleAddRole">新增角色</el-button>
    </div>

    <!-- 用户管理表格 -->
    <el-card class="table-card">
      <div slot="header">
        <span>用户管理</span>
        <el-input
          v-model="userSearchKeyword"
          placeholder="请输入用户名或姓名搜索"
          class="search-input"
          @input="handleUserSearch">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
      <el-table
        v-loading="userLoading"
        :data="userTableData"
        border
        style="width: 100%">
        <el-table-column
          prop="username"
          label="用户名"
          width="120">
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="120">
        </el-table-column>
        <el-table-column
          prop="department"
          label="部门"
          width="120">
        </el-table-column>
        <el-table-column
          prop="position"
          label="职位"
          width="120">
        </el-table-column>
        <el-table-column
          prop="role"
          label="角色"
          width="120">
          <template slot-scope="scope">
            <el-tag :type="getRoleType(scope.row.role)">{{ getRoleText(scope.row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(val) => handleStatusChange(scope.row, val)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="200">
          <template slot-scope="scope">
            <el-button @click="handleEditUser(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="handleResetPassword(scope.row)" type="text" size="small">重置密码</el-button>
            <el-button @click="handleDeleteUser(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色管理表格 -->
    <el-card class="table-card">
      <div slot="header">
        <span>角色管理</span>
      </div>
      <el-table
        v-loading="roleLoading"
        :data="roleTableData"
        border
        style="width: 100%">
        <el-table-column
          prop="roleName"
          label="角色名称"
          width="150">
        </el-table-column>
        <el-table-column
          prop="description"
          label="角色描述">
        </el-table-column>
        <el-table-column
          prop="permissions"
          label="权限">
          <template slot-scope="scope">
            <el-tag
              v-for="perm in scope.row.permissions"
              :key="perm"
              size="small"
              style="margin-right: 5px">
              {{ getPermissionText(perm) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="150">
          <template slot-scope="scope">
            <el-button @click="handleEditRole(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="handleDeleteRole(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户表单对话框 -->
    <el-dialog :title="userDialogTitle" :visible.sync="userDialogVisible" width="40%">
      <el-form :model="userForm" :rules="userRules" ref="userForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="userForm.id !== ''"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="userForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="userForm.department"></el-input>
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="userForm.position"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色">
            <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitUserForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 角色表单对话框 -->
    <el-dialog :title="roleDialogTitle" :visible.sync="roleDialogVisible" width="40%">
      <el-form :model="roleForm" :rules="roleRules" ref="roleForm" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input type="textarea" v-model="roleForm.description"></el-input>
        </el-form-item>
        <el-form-item label="权限" prop="permissions">
          <el-checkbox-group v-model="roleForm.permissions">
            <el-checkbox label="chemical">危化品管理</el-checkbox>
            <el-checkbox label="storage">入库管理</el-checkbox>
            <el-checkbox label="outbound">出库管理</el-checkbox>
            <el-checkbox label="inventory">库存监控</el-checkbox>
            <el-checkbox label="warning">安全预警</el-checkbox>
            <el-checkbox label="record">使用记录</el-checkbox>
            <el-checkbox label="permission">权限管理</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRoleForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Permission',
  data() {
    return {
      // 用户搜索关键词
      userSearchKeyword: '',
      // 用户表格数据
      userTableData: [],
      // 角色表格数据
      roleTableData: [],
      // 加载状态
      userLoading: false,
      roleLoading: false,
      // 用户对话框
      userDialogVisible: false,
      userDialogTitle: '',
      userForm: {
        id: '',
        username: '',
        name: '',
        gender: '男',
        phone: '',
        email: '',
        department: '',
        position: '',
        role: ''
      },
      userRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      },
      // 角色对话框
      roleDialogVisible: false,
      roleDialogTitle: '',
      roleForm: {
        id: '',
        roleName: '',
        description: '',
        permissions: []
      },
      roleRules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ],
        permissions: [
          { type: 'array', required: true, message: '请至少选择一个权限', trigger: 'change' }
        ]
      },
      // 角色选项
      roleOptions: [
        { value: 'admin', label: '管理员' },
        { value: 'operator', label: '操作员' },
        { value: 'user', label: '普通用户' }
      ]
    }
  },
  created() {
    this.fetchUsers()
    this.fetchRoles()
  },
  methods: {
    // 获取用户列表
    async fetchUsers() {
      this.userLoading = true
      try {
        const response = await this.$http.get('/man/list')
        this.userTableData = response.data
      } catch (error) {
        this.$message.error('获取用户列表失败')
      }
      this.userLoading = false
    },
    // 获取角色列表
    async fetchRoles() {
      this.roleLoading = true
      try {
        const response = await this.$http.get('/role/list')
        this.roleTableData = response.data
      } catch (error) {
        this.$message.error('获取角色列表失败')
      }
      this.roleLoading = false
    },
    // 获取角色类型
    getRoleType(role) {
      const types = {
        'admin': 'danger',
        'operator': 'warning',
        'user': 'info'
      }
      return types[role] || ''
    },
    // 获取角色文本
    getRoleText(role) {
      const texts = {
        'admin': '管理员',
        'operator': '操作员',
        'user': '普通用户'
      }
      return texts[role] || role
    },
    // 获取权限文本
    getPermissionText(permission) {
      const texts = {
        'chemical': '危化品管理',
        'storage': '入库管理',
        'outbound': '出库管理',
        'inventory': '库存监控',
        'warning': '安全预警',
        'record': '使用记录',
        'permission': '权限管理'
      }
      return texts[permission] || permission
    },
    // 处理用户搜索
    handleUserSearch() {
      // 实现本地搜索
      if (!this.userSearchKeyword) {
        this.fetchUsers()
        return
      }
      const keyword = this.userSearchKeyword.toLowerCase()
      const filteredData = this.userTableData.filter(item => {
        return item.username.toLowerCase().includes(keyword) ||
               item.name.toLowerCase().includes(keyword)
      })
      this.userTableData = filteredData
    },
    // 处理添加用户
    handleAddUser() {
      this.userDialogTitle = '新增用户'
      this.userForm = {
        id: '',
        username: '',
        name: '',
        gender: '男',
        phone: '',
        email: '',
        department: '',
        position: '',
        role: ''
      }
      this.userDialogVisible = true
    },
    // 处理编辑用户
    handleEditUser(row) {
      this.userDialogTitle = '编辑用户'
      this.userForm = { ...row }
      this.userDialogVisible = true
    },
    // 处理重置密码
    async handleResetPassword(row) {
      try {
        await this.$confirm('确认重置该用户的密码？', '提示', {
          type: 'warning'
        })
        await this.$http.post(`/employee/${row.id}/reset-password`)
        this.$message.success('密码重置成功')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('密码重置失败')
        }
      }
    },
    // 处理删除用户
    async handleDeleteUser(row) {
      try {
        await this.$confirm('确认删除该用户？', '提示', {
          type: 'warning'
        })
        await this.$http.delete(`/employee/${row.id}`)
        this.$message.success('删除成功')
        this.fetchUsers()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    // 处理状态变更
    async handleStatusChange(row, value) {
      try {
        await this.$http.put(`/employee/${row.id}/status`, { status: value })
        this.$message.success('状态更新成功')
      } catch (error) {
        this.$message.error('状态更新失败')
        row.status = !value // 恢复状态
      }
    },
    // 提交用户表单
    submitUserForm() {
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.userForm.id) {
              await this.$http.put(`/employee/${this.userForm.id}`, this.userForm)
            } else {
              await this.$http.post('/employee', this.userForm)
            }
            this.$message.success('保存成功')
            this.userDialogVisible = false
            this.fetchUsers()
          } catch (error) {
            this.$message.error('保存失败')
          }
        }
      })
    },
    // 处理添加角色
    handleAddRole() {
      this.roleDialogTitle = '新增角色'
      this.roleForm = {
        id: '',
        roleName: '',
        description: '',
        permissions: []
      }
      this.roleDialogVisible = true
    },
    // 处理编辑角色
    handleEditRole(row) {
      this.roleDialogTitle = '编辑角色'
      this.roleForm = { ...row }
      this.roleDialogVisible = true
    },
    // 处理删除角色
    async handleDeleteRole(row) {
      try {
        await this.$confirm('确认删除该角色？', '提示', {
          type: 'warning'
        })
        await this.$http.delete(`/role/${row.id}`)
        this.$message.success('删除成功')
        this.fetchRoles()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    // 提交角色表单
    submitRoleForm() {
      this.$refs.roleForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.roleForm.id) {
              await this.$http.put(`/role/${this.roleForm.id}`, this.roleForm)
            } else {
              await this.$http.post('/role', this.roleForm)
            }
            this.$message.success('保存成功')
            this.roleDialogVisible = false
            this.fetchRoles()
          } catch (error) {
            this.$message.error('保存失败')
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.permission-container {
  padding: 20px;
}

.operation-container {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.search-input {
  float: right;
  width: 200px;
}

.el-tag {
  margin-right: 5px;
}

.el-checkbox-group {
  display: flex;
  flex-direction: column;
}

.el-checkbox {
  margin-bottom: 10px;
}
</style>
