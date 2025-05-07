<template>
  <div class="outbound-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="化学品">
          <el-input v-model="searchForm.chemicalName" placeholder="请输入化学品名称"></el-input>
        </el-form-item>
        <el-form-item label="领用人">
          <el-input v-model="searchForm.recipient" placeholder="请输入领用人"></el-input>
        </el-form-item>
        <el-form-item label="出库时间">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <div class="operation-container">
      <el-button type="success" @click="handleBatchImport">批量导入</el-button>
      <el-button type="warning" @click="handleExport">导出数据</el-button>
    </div>

    <!-- 数据统计 -->
    <el-row :gutter="20" class="statistics-container">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div slot="header">
            <span>本月出库总量</span>
          </div>
          <div class="statistics-value">{{ statistics.monthlyTotal || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div slot="header">
            <span>本月出库次数</span>
          </div>
          <div class="statistics-value">{{ statistics.monthlyCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div slot="header">
            <span>今日出库总量</span>
          </div>
          <div class="statistics-value">{{ statistics.dailyTotal || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div slot="header">
            <span>今日出库次数</span>
          </div>
          <div class="statistics-value">{{ statistics.dailyCount || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        prop="chemical.name"
        label="化学品名称"
        width="150">
      </el-table-column>
      <el-table-column
        prop="amount"
        label="出库数量"
        width="120">
      </el-table-column>
      <el-table-column
        prop="unit"
        label="单位"
        width="80">
      </el-table-column>
      <el-table-column
        prop="batchNo"
        label="批次号"
        width="120">
      </el-table-column>
      <el-table-column
        prop="recipient"
        label="领用人"
        width="120">
      </el-table-column>
      <el-table-column
        prop="purpose"
        label="用途"
        width="150">
      </el-table-column>
      <el-table-column
        prop="outboundTime"
        label="出库时间"
        width="180">
      </el-table-column>
      <el-table-column
        prop="operator.name"
        label="操作员"
        width="120">
      </el-table-column>
      <el-table-column
        prop="notes"
        label="备注">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="180">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="text" size="small">删除</el-button>
          <el-button @click="handleDetail(scope.row)" type="text" size="small">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="化学品" prop="chemicalId">
          <el-select 
            v-model="form.chemicalId" 
            filterable 
            placeholder="请选择化学品"
            @change="handleChemicalChange">
            <el-option
              v-for="item in chemicalOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.category }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="当前库存" v-if="currentStock !== null">
          <span>{{ currentStock }} {{ form.unit }}</span>
        </el-form-item>
        <el-form-item label="出库数量" prop="amount">
          <el-input-number 
            v-model="form.amount" 
            :min="0" 
            :max="currentStock || 0"
            :precision="2">
          </el-input-number>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="form.unit" placeholder="请选择单位">
            <el-option label="kg" value="kg"></el-option>
            <el-option label="L" value="L"></el-option>
            <el-option label="g" value="g"></el-option>
            <el-option label="ml" value="ml"></el-option>
            <el-option label="桶" value="桶"></el-option>
            <el-option label="瓶" value="瓶"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="批次号" prop="batchNo">
          <el-input v-model="form.batchNo"></el-input>
        </el-form-item>
        <el-form-item label="领用人" prop="recipient">
          <el-input v-model="form.recipient"></el-input>
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="form.purpose"></el-input>
        </el-form-item>
        <el-form-item label="出库时间" prop="outboundTime">
          <el-date-picker
            v-model="form.outboundTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input type="textarea" v-model="form.notes"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入" :visible.sync="importDialogVisible" width="30%">
      <el-upload
        class="upload-demo"
        drag
        action="/outbound/import"
        :headers="uploadHeaders"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        accept=".xlsx,.xls">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传excel文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  name: 'Outbound',
  data() {
    return {
      // 查询条件
      searchForm: {
        chemicalName: '',
        recipient: '',
        timeRange: []
      },
      // 化学品选项
      chemicalOptions: [],
      // 当前选中化学品的库存
      currentStock: null,
      // 表格数据
      tableData: [],
      // 加载状态
      loading: false,
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 5,
        total: 0
      },
      // 统计数据
      statistics: {
        monthlyTotal: 0,
        monthlyCount: 0,
        dailyTotal: 0,
        dailyCount: 0
      },
      // 对话框显示状态
      dialogVisible: false,
      importDialogVisible: false,
      // 对话框标题
      dialogTitle: '新增出库记录',
      // 表单数据
      form: {
        chemicalId: '',
        chemicalName: '',
        amount: 0,
        unit: '',
        batchNo: '',
        recipient: '',
        purpose: '',
        outboundTime: '',
        notes: '',
        operatorId: 1
      },
      // 表单验证规则
      rules: {
        chemicalId: [
          { required: true, message: '请选择化学品', trigger: 'change' }
        ],
        amount: [
          { required: true, message: '请输入出库数量', trigger: 'blur' },
          { validator: this.validateAmount, trigger: 'blur' }
        ],
        unit: [
          { required: true, message: '请选择单位', trigger: 'change' }
        ],
        batchNo: [
          { required: true, message: '请输入批次号', trigger: 'blur' }
        ],
        recipient: [
          { required: true, message: '请输入领用人', trigger: 'blur' }
        ],
        purpose: [
          { required: true, message: '请输入用途', trigger: 'blur' }
        ],
        outboundTime: [
          { required: true, message: '请选择出库时间', trigger: 'change' }
        ]
      },
      // 上传请求头
      uploadHeaders: {
        'Authorization': 'Bearer ' + getToken()
      }
    }
  },
  created() {
    this.fetchChemicals()
    this.fetchStatistics()
    this.fetchData() //获取真正的数据内容
  },
  methods: {
    // 获取时间范围
    getTimeRange() {
      const range = this.searchForm.timeRange
      if (!range || !range.length || range.length < 2) {
        return {
          startTime: null,
          endTime: null
        }
      }
      
      // 格式化日期为yyyy-MM-dd格式
      const formatDate = (date) => {
        if (!date) return null
        const d = new Date(date)
        const year = d.getFullYear()
        const month = ('0' + (d.getMonth() + 1)).slice(-2)
        const day = ('0' + d.getDate()).slice(-2)
        return `${year}-${month}-${day}`
      }
      
      return {
        startTime: formatDate(range[0]),
        endTime: formatDate(range[1])
      }
    },
    
    // 获取表格数据
    async fetchData() {
      this.loading = true
      try {
        const { startTime, endTime } = this.getTimeRange()
        
        console.log('发送出库记录查询请求参数:', {
          chemicalName: this.searchForm.chemicalName || null,
          recipient: this.searchForm.recipient || null,
          startTime,
          endTime,
          page: this.pagination.currentPage,
          size: this.pagination.pageSize
        })
        
        const response = await this.$http.get('/outbound/list', {
          params: {
            chemicalName: this.searchForm.chemicalName || null,
            recipient: this.searchForm.recipient || null,
            startTime: startTime || null,
            endTime: endTime || null,
            page: this.pagination.currentPage,
            size: this.pagination.pageSize
          }
        })
        
        console.log('出库记录接收到的响应:', response.data)
        
        if (response.data && response.data.code === 200) {
          // 处理数据，判断返回的是否是分页对象
          if (response.data.data && response.data.data.records) {
            this.tableData = response.data.data.records || []
            this.pagination.total = response.data.data.total || 0
            
            if (this.tableData.length === 0) {
              this.$message.info('未找到符合条件的出库记录')
            } else {
              console.log('处理后的数据:', this.tableData)
            }
          } else {
            // 如果直接返回的是数组
            this.tableData = response.data.data || []
            this.pagination.total = response.data.data ? response.data.data.length : 0
            
            if (this.tableData.length === 0) {
              this.$message.info('未找到符合条件的出库记录')
            }
          }
        } else {
          this.$message.error(response.data.message || '获取出库记录失败')
        }
      } catch (error) {
        console.error('获取出库记录失败:', error)
        this.$message.error('获取出库记录失败：' + (error.message || error))
      }
      this.loading = false
    },
    
    // 获取化学品列表
    async fetchChemicals() {
      try {
        const response = await this.$http.get('/chemical/list')
        console.log('化学品列表响应:', response.data)
        
        if (response.data && response.data.code === 200) {
          this.chemicalOptions = response.data.data || []
        } else {
          this.$message.error(response.data.message || '获取化学品列表失败')
        }
      } catch (error) {
        console.error('获取化学品列表失败:', error)
        this.$message.error('获取化学品列表失败：' + (error.message || error))
      }
    },
    
    // 获取统计数据
    async fetchStatistics() {
      try {
        const response = await this.$http.get('/outbound/statistics')
        console.log('出库统计数据响应:', response.data)
        
        if (response.data && response.data.code === 200) {
          this.statistics = response.data.data || {
            monthlyTotal: 0,
            monthlyCount: 0,
            dailyTotal: 0,
            dailyCount: 0
          }
        } else {
          console.error('获取统计数据失败:', response.data.message)
          this.$message.error(response.data.message || '获取统计数据失败')
          this.resetStatistics()
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
        this.$message.error('获取统计数据失败：' + (error.message || error))
        this.resetStatistics()
      }
    },
    
    // 重置统计数据
    resetStatistics() {
      this.statistics = {
        monthlyTotal: 0,
        monthlyCount: 0,
        dailyTotal: 0,
        dailyCount: 0
      }
    },
    // 获取当前库存
    async fetchCurrentStock(chemicalId) {
      try {
        const response = await this.$http.get(`/inventory/getTotalAmount`, {
          params: { chemicalId: chemicalId }
        })
        if (response.data && response.data.code === 200) {
          this.currentStock = response.data.data || 0
        } else {
          this.$message.error(response.data.message || '获取当前库存失败')
          this.currentStock = 0
        }
      } catch (error) {
        console.error('获取当前库存失败:', error)
        this.$message.error('获取当前库存失败')
        this.currentStock = 0
      }
    },
    // 处理化学品选择变化
    handleChemicalChange(value) {
      console.log('选择化学品变更:', value)
      if (value) {
        // 获取当前库存
        this.fetchCurrentStock(value)
        
        // 根据选中的化学品ID更新化学品名称
        const selected = this.chemicalOptions.find(item => item.id === value)
        if (selected) {
          this.form.chemicalName = selected.name
          // 可以设置默认单位，如果化学品有默认单位的话
          if (!this.form.unit) {
            this.form.unit = selected.defaultUnit || 'kg'
          }
          console.log('更新化学品名称为:', selected.name)
        }
      } else {
        this.currentStock = null
        this.form.chemicalName = ''
      }
    },
    // 验证出库数量
    validateAmount(rule, value, callback) {
      if (value > this.currentStock) {
        callback(new Error('出库数量不能大于当前库存'))
      } else {
        callback()
      }
    },
    // 处理查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    // 重置查询
    resetSearch() {
      this.searchForm = {
        chemicalName: '',
        recipient: '',
        timeRange: []
      }
      this.handleSearch()
    },
    // 处理新增
    handleAdd() {
      this.dialogTitle = '新增出库记录'
      // 创建当前时间的格式化字符串
      const now = new Date();
      const formattedDateTime = now.toISOString().substr(0, 19).replace('T', ' ');
      
      // 生成批次号
      const dateStr = now.getFullYear().toString().substr(-2) + 
                    ('0' + (now.getMonth() + 1)).slice(-2) + 
                    ('0' + now.getDate()).slice(-2);
      const batchNo = 'O' + dateStr + now.getTime().toString().substr(-4);
      
      this.form = {
        chemicalId: '',
        chemicalName: '',
        amount: 0,
        unit: '',
        batchNo: batchNo,
        recipient: '',
        purpose: '',
        outboundTime: formattedDateTime,
        notes: '',
        operatorId: 1
      }
      this.currentStock = null
      this.dialogVisible = true
    },
    // 处理编辑
    handleEdit(row) {
      this.dialogTitle = '编辑出库记录'
      // 复制行数据，确保不直接修改表格数据
      this.form = { 
        ...row,
        // 使用化学品名称代替ID
        chemicalId: row.chemical ? row.chemical.id : null,
        chemicalName: row.chemical ? row.chemical.name : ''
      }
      this.fetchCurrentStock(row.chemical ? row.chemical.id : null)
      this.dialogVisible = true
    },
    // 处理删除
    handleDelete(row) {
      this.$confirm('确认删除该出库记录?', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/outbound/delete/${row.id}`)
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    // 处理详情
    handleDetail(row) {
      // 实现查看详情的逻辑
      this.$alert(`
        <div>
          <p><strong>ID:</strong> ${row.id}</p>
          <p><strong>化学品:</strong> ${row.chemical ? row.chemical.name : row.chemicalName}</p>
          <p><strong>数量:</strong> ${row.amount} ${row.unit}</p>
          <p><strong>批次号:</strong> ${row.batchNo}</p>
          <p><strong>领用人:</strong> ${row.recipient}</p>
          <p><strong>用途:</strong> ${row.purpose}</p>
          <p><strong>出库时间:</strong> ${row.outboundTime}</p>
          <p><strong>操作员:</strong> ${row.operator ? row.operator.name : '未知'}</p>
          <p><strong>备注:</strong> ${row.notes || '无'}</p>
        </div>
      `, '出库记录详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '关闭'
      });
      console.log('查看详情:', row)
    },
    // 处理提交
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            if (this.form.id) {
              await this.$http.put(`/outbound/update/${this.form.id}`, this.form)
            } else {
              await this.$http.post('/outbound/add', this.form)
            }
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error(error.response && error.response.data && error.response.data.message || '保存失败')
          }
        }
      })
    },
    // 处理批量导入
    handleBatchImport() {
      this.importDialogVisible = true
    },
    // 处理导出
    handleExport() {
      const { startTime, endTime } = this.getTimeRange()
      
      // 构建查询参数（只添加用户输入的条件）
      const params = new URLSearchParams()
      if (this.searchForm.chemicalName) params.append('chemicalName', this.searchForm.chemicalName)
      if (this.searchForm.recipient) params.append('recipient', this.searchForm.recipient)
      if (startTime) params.append('startTime', startTime)
      if (endTime) params.append('endTime', endTime)
      
      // 显示导出开始提示
      this.$message.info('开始导出数据，请稍候...')
      
      // 使用原生方式处理下载
      const downloadLink = document.createElement('a')
      let url = '/outbound/export';
      if (params.toString()) {
        url += '?' + params.toString();
      }
      downloadLink.href = url;
      
      // 添加时间戳防止缓存
      if (url.includes('?')) {
        downloadLink.href += '&_t=' + new Date().getTime();
      } else {
        downloadLink.href += '?_t=' + new Date().getTime();
      }
      
      document.body.appendChild(downloadLink)
      downloadLink.click()
      document.body.removeChild(downloadLink)
    },
    // 处理导入成功
    handleImportSuccess(response) {
      this.$message.success('导入成功')
      this.fetchData()
      this.fetchStatistics()
    },
    // 处理导入失败
    handleImportError() {
      this.$message.error('导入失败')
    },
    // 下载模板
    downloadTemplate() {
      window.location.href = '/outbound/template'
    },
    // 处理分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchData()
    },
    // 处理页码变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },
  }
}
</script>

<style scoped>
.outbound-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.operation-container {
  margin-bottom: 20px;
}

.statistics-container {
  margin-bottom: 20px;
}

.statistics-card {
  text-align: center;
}

.statistics-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 