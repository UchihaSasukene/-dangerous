<template>
  <div class="man-container">
    <!-- 搜索区域 - 扩展搜索功能 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="请选择性别" clearable style="width: 120px">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="searchForm.department" placeholder="请输入部门" clearable></el-input>
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="searchForm.position" placeholder="请输入职位" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="el-icon-search">搜索</el-button>
          <el-button @click="resetSearch" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="openAddDialog" icon="el-icon-plus">添加人员</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <div class="table-operations" style="margin-bottom: 15px">
        <el-button type="danger" icon="el-icon-delete" :disabled="selectedRows.length === 0" @click="batchDelete">批量删除</el-button>
        <el-button type="primary" icon="el-icon-download" @click="exportData">导出数据</el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="id"
          label="ID"
          width="80">
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="120">
        </el-table-column>
        <el-table-column
          prop="gender"
          label="性别"
          width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.gender === '男' ? 'primary' : 'success'">
              {{ scope.row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          width="120">
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          width="180">
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
          label="操作"
          width="360">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="deleteMan(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>

    <!-- 对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
            <el-option label="其他" value="MAN,What can I say"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="form.department"></el-input>
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "man",
  data() {
    return {
      title: '',
      currentRow: null,
      dialogFormVisible: false,
      form: {
        id: null,
        name: '',
        gender: '',
        phone: '',
        email: '',
        department: '',
        position: ''
      },
      tableData: [],
      total: 0,
      currentPage: 1,
      pageSize: 5,
      loading: false,
      dialogVisible: false,
      dialogTitle: '',
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 1, max: 50, message: '姓名长度必须在1-50个字符之间', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[1-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        department: [
          { required: true, message: '请输入部门', trigger: 'blur' },
          { max: 50, message: '部门名称不能超过50个字符', trigger: 'blur' }
        ],
        position: [
          { required: true, message: '请输入职位', trigger: 'blur' },
          { max: 50, message: '职位名称不能超过50个字符', trigger: 'blur' }
        ]
      },
      searchForm: {
        name: '',
        gender: '',
        phone: '',
        email: '',
        department: '',
        position: ''
      },
      selectedRows: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
      this.form = {
        id: null,
        name: '',
        gender: '',
        phone: '',
        email: '',
        department: '',
        position: ''
      };
    },

    openAddDialog() {
      this.dialogTitle = '添加人员';
      this.resetForm();
      this.dialogVisible = true;
    },

    openEditDialog(row) {
      this.dialogTitle = '编辑人员';
      this.form = {
        id: row.id,
        name: row.name,
        gender: row.gender,
        phone: row.phone,
        email: row.email,
        department: row.department,
        position: row.position
      };
      this.dialogVisible = true;
    },

    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.form.id ? '/man/update' : '/man/add';
          const successMsg = this.form.id ? '更新成功' : '添加成功';
          
          // 确保所有字段都是字符串类型
          const formData = {
            id: this.form.id,
            name: this.form.name,
            gender: this.form.gender,
            phone: String(this.form.phone),   //确保是字符串类型
            email: this.form.email,
            department: this.form.department,
            position: this.form.position
          };
          
          // 根据是否有id选择不同的HTTP方法
          const method = this.form.id ? 'put' : 'post';
          
          this.$http[method](url, formData).then(response => {
            if (response.data && response.data.code === 200) {
              this.$message.success(successMsg);
              this.dialogVisible = false;
              this.getList();
              this.resetForm();
            } else {
              this.$message.error(response.data.message || '操作失败');
            }
          }).catch(error => {
            console.error('操作失败:', error);
            this.$message.error('操作失败：' + error.message);
          });
        } else {
          console.log('表单验证失败');
          return false;
        }
      });
    },

    deleteMan(id) {
      this.$confirm('确定要删除该人员吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.get(`/man/delete?id=${id}`).then(response => {
          if (response.data && response.data.code === 200) {
            this.$message.success('删除成功');
            this.getList();
          } else {
            this.$message.error(response.data.message || '删除失败');
          }
        }).catch(error => {
          console.error('删除失败:', error);
          this.$message.error('删除失败：' + error.message);
        });
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },

    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.getList()
    },

    handleSearch() {
      this.currentPage = 1
      this.getList()
    },

    resetSearch() {
      this.searchForm = {
        name: '',
        gender: '',
        department: '',
        position: ''
      }
      this.currentPage = 1
      this.getList()
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    batchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请至少选择一条记录');
        return;
      }
      
      const ids = this.selectedRows.map(row => row.id);
      
      this.$confirm(`确定要删除选中的 ${ids.length} 条记录吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/man/batchDelete', { ids }).then(response => {
          if (response.data && response.data.code === 200) {
            this.$message.success(response.data.message);
            this.getList();
            this.selectedRows = [];
          } else {
            this.$message.error(response.data.message || '批量删除失败');
          }
        }).catch(error => {
          console.error('批量删除失败:', error);
          this.$message.error('批量删除失败：' + error.message);
        });
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    exportData() {
      this.$confirm('确定要导出当前筛选条件下的所有数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const params = {
          name: this.searchForm.name || null,
          phone: this.searchForm.phone || null,
          gender: this.searchForm.gender || null,
          email: this.searchForm.email || null,
          department: this.searchForm.department || null,
          position: this.searchForm.position || null
        }
        
        Object.keys(params).forEach(key => {
          if (params[key] === null || params[key] === '') {
            delete params[key]
          }
        })
        
        const queryString = Object.keys(params)
          .map(key => `${key}=${encodeURIComponent(params[key])}`)
          .join('&')
        
        // 添加时间戳防止缓存
        const timestamp = new Date().getTime()
        const url = `/man/export?${queryString}${queryString ? '&' : ''}t=${timestamp}`
        
        const link = document.createElement('a')
        link.href = url
        link.target = '_blank'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        
        this.$message.success('导出请求已发送，请等待下载')
        console.log('导出URL:', url)
      }).catch(() => {
        this.$message.info('已取消导出')
      })
    },

    getList() {
      this.loading = true;
      const params = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.searchForm.name || undefined,
        gender: this.searchForm.gender || undefined,
        department: this.searchForm.department || undefined,
        position: this.searchForm.position || undefined
      };

      this.$http.get('/man/list', { params }).then(response => {
        this.loading = false;
        if (response.data && response.data.code === 200) {
          const { list, total } = response.data.data;
          this.tableData = list;
          this.total = total;
        } else {
          this.$message.error(response.data.message || '获取数据失败');
          this.tableData = [];
          this.total = 0;
        }
      }).catch(error => {
        this.loading = false;
        console.error('获取数据失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.tableData = [];
        this.total = 0;
      });
    }
  },
  mounted() {
    this.getList();
  }
}
</script>

<style scoped>
.man-container {
  padding: 20px;
}
.search-card {
  margin-bottom: 20px;
}
.table-card {
  margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
