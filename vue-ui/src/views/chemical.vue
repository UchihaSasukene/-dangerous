<template>
  <el-card class="box-card">
    <!-- 搜索栏 -->
    <el-row :gutter="20" class="search-row">
      <el-col :span="6">
        <el-input
          placeholder="请输入危化品名称"
          v-model="searchForm.name"
          clearable>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-select v-model="searchForm.category" placeholder="危化品类别" clearable>
          <el-option label="易燃品" value="易燃品"></el-option>
          <el-option label="爆炸品" value="爆炸品"></el-option>
          <el-option label="腐蚀品" value="腐蚀品"></el-option>
          <el-option label="毒害品" value="毒害品"></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-col>
    </el-row>

    <!-- 工具栏 -->
    <el-row class="tool-row">
      <el-button size="mini" type="primary" @click="add">新增危化品</el-button>
    </el-row>

    <!-- 表格 -->
    <el-table
      :data="tableData"
      style="width: 100%">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        property="id"
        label="ID"
        width="80"
        align="center">
      </el-table-column>
      <el-table-column
        property="name"
        label="危化品名称"
        width="120"
        align="center">
      </el-table-column>
      <el-table-column
        property="category"
        label="类别"
        width="100"
        align="center">
      </el-table-column>
      <el-table-column
        property="dangerLevel"
        label="危险等级"
        width="100"
        align="center">
      </el-table-column>
      <el-table-column
        property="storageCondition"
        label="存储条件"
        width="150"
        align="center">
      </el-table-column>
      <el-table-column
        property="warningThreshold"
        label="预警阈值(kg)"
        width="120"
        align="center">
      </el-table-column>
      <el-table-column
        property="description"
        label="描述"
        align="center">
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="edit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="remove(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible" width="40%">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="危化品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入危化品名称"></el-input>
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="form.category" placeholder="请选择类别">
            <el-option label="易燃品" value="易燃品"></el-option>
            <el-option label="爆炸品" value="爆炸品"></el-option>
            <el-option label="腐蚀品" value="腐蚀品"></el-option>
            <el-option label="毒害品" value="毒害品"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="危险等级" prop="dangerLevel">
          <el-select v-model="form.dangerLevel" placeholder="请选择危险等级">
            <el-option label="一级" value="一级"></el-option>
            <el-option label="二级" value="二级"></el-option>
            <el-option label="三级" value="三级"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="存储条件" prop="storageCondition">
          <el-input v-model="form.storageCondition" placeholder="请输入存储条件"></el-input>
        </el-form-item>
        <el-form-item label="预警阈值" prop="warningThreshold">
          <el-input-number v-model="form.warningThreshold" :min="0" :precision="2" :step="0.1"></el-input-number>
          <span class="unit">kg</span>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm()">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  name: 'chemical',
  data() {
    return {
      // 搜索表单
      searchForm: {
        name: '',
        category: ''
      },
      // 表格数据
      tableData: [],
      // 分页
      currentPage: 1,
      pageSize: 5,
      total: 0,
      // 对话框
      title: '',
      dialogFormVisible: false,
      form: {
        id: null,
        name: '',
        category: '',
        dangerLevel: '',
        storageCondition: '',
        warningThreshold: 0,
        description: ''
      },
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入危化品名称', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择类别', trigger: 'change' }
        ],
        dangerLevel: [
          { required: true, message: '请选择危险等级', trigger: 'change' }
        ],
        storageCondition: [
          { required: true, message: '请输入存储条件', trigger: 'blur' }
        ],
        warningThreshold: [
          { required: true, message: '请输入预警阈值', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 搜索
    search() {
      this.getList()
    },
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        name: '',
        category: ''
      }
      this.getList()
    },
    // 新增
    add() {
      this.reset()
      this.dialogFormVisible = true
      this.title = '新增危化品'
    },
    // 编辑
    edit(index, row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.title = '编辑危化品'
    },
    // 删除
    remove(index, row) {
      this.$confirm('确认删除该危化品信息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/chemical/delete/${row.id}`)
          .then((response) => {
            if (response.data && response.data.code === 200) {
              this.$message({
                message: '删除成功!',
                type: 'success'
              });
              this.getList();
            } else {
              this.$message.error(response.data.message || '删除失败');
            }
          }).catch((error) => {
            this.$message.error('删除失败：' + error.message);
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 提交
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.form.id ? '/chemical/edit' : '/chemical/add';
          const successMsg = this.form.id ? '更新成功' : '添加成功';
          
          // 确保所有字段都是正确的类型
          const formData = {
            id: this.form.id,
            name: this.form.name,
            category: this.form.category,
            dangerLevel: this.form.dangerLevel,
            storageCondition: this.form.storageCondition,
            warningThreshold: Number(this.form.warningThreshold),
            description: this.form.description
          };
          
          // 根据是否有id选择不同的HTTP方法
          const method = this.form.id ? 'put' : 'post';
          
          this.$http[method](url, formData).then(response => {
            if (response.data && response.data.code === 200) {
              this.$message.success(successMsg);
              this.dialogFormVisible = false;
              this.getList();
              this.resetForm();
            } else {
              this.$message.error(response.data.message || '操作失败');
            }
          }).catch(error => {
            console.error('操作失败:', error);
          });
        } else {
          console.log('表单验证失败');
          return false;
        }
      });
    },
    // 获取列表
    getList() {
      this.$http({
        method: 'get',
        url: '/chemical/list',
        params: {
          page: this.currentPage,
          size: this.pageSize,
          name: this.searchForm.name,
          category: this.searchForm.category
        }
      }).then((response) => {
        if (response.data && response.data.data) {
          this.tableData = response.data.data.list
          this.total = response.data.data.total
        }
      }).catch((error) => {
        console.error('请求失败:', error)
        this.$message.error('获取数据失败：' + error.message)
      })
    },
    // 重置表单
    reset() {
      this.form = {
        id: null,
        name: '',
        category: '',
        dangerLevel: '',
        storageCondition: '',
        warningThreshold: 0,
        description: ''
      }
      if (this.$refs['form']) {
        this.$refs['form'].resetFields()
      }
    },
    // 分页
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    }
  },
  mounted() {
    this.getList()
  }
}
</script>

<style scoped>
.search-row {
  margin-bottom: 20px;
}
.tool-row {
  margin-bottom: 20px;
}
.unit {
  margin-left: 10px;
}
</style>
