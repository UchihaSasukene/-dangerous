<template>
  <div class="warning-container">
    <!-- 预警统计 -->
    <el-row :gutter="20" class="statistics-container">
      <el-col :span="6">
        <el-card class="statistics-card warning-card" shadow="hover">
          <div slot="header">
            <span>库存预警</span>
          </div>
          <div class="statistics-value">{{ statistics.stockWarning || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card danger-card" shadow="hover">
          <div slot="header">
            <span>库存不足</span>
          </div>
          <div class="statistics-value">{{ statistics.stockLow || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card info-card" shadow="hover">
          <div slot="header">
            <span>超储预警</span>
          </div>
          <div class="statistics-value">{{ statistics.stockHigh || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card primary-card" shadow="hover">
          <div slot="header">
            <span>待处理预警</span>
          </div>
          <div class="statistics-value">{{ statistics.unhandled || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="化学品">
          <el-input v-model="searchForm.chemicalName" placeholder="请输入化学品名称"></el-input>
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="searchForm.warningType" placeholder="请选择预警类型" clearable>
            <el-option label="库存预警" value="stock"></el-option>
            <el-option label="库存不足" value="low"></el-option>
            <el-option label="超储预警" value="high"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警等级">
          <el-select v-model="searchForm.warningLevel" placeholder="请选择预警等级" clearable>
            <el-option label="一般" value="normal"></el-option>
            <el-option label="严重" value="serious"></el-option>
            <el-option label="紧急" value="urgent"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.status" placeholder="请选择处理状态" clearable>
            <el-option label="未处理" value="unprocessed"></el-option>
            <el-option label="处理中" value="processing"></el-option>
            <el-option label="已处理" value="processed"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警时间">
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
          <el-button type="success" @click="checkAndGenerateWarnings">检查预警</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警列表 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      @selection-change="handleSelectionChange"
      style="width: 100%; margin-top: 20px;">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        prop="chemical.name"
        label="化学品名称"
        width="150">
      </el-table-column>
      <el-table-column
        prop="warningType"
        label="预警类型"
        width="120">
        <template slot-scope="scope">
          <el-tag :type="getWarningTypeTag(scope.row.warningType)">
            {{ getWarningTypeText(scope.row.warningType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="warningLevel"
        label="预警等级"
        width="120">
        <template slot-scope="scope">
          <el-tag :type="getWarningLevelTag(scope.row.warningLevel)">
            {{ getWarningLevelText(scope.row.warningLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="warningContent"
        label="预警内容"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="warningTime"
        label="预警时间"
        width="180">
      </el-table-column>
      <el-table-column
        prop="status"
        label="处理状态"
        width="120">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button @click="handleDetail(scope.row)" type="text" size="small">详情</el-button>
          <el-button 
            v-if="scope.row.status === 'unprocessed' || scope.row.status === '未处理'"
            @click="handleProcess(scope.row)" 
            type="text" 
            size="small">处理</el-button>
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

    <!-- 批量操作栏 -->
    <div class="batch-operation" v-if="multipleSelection.length > 0">
      <el-card shadow="hover">
        <div class="batch-operation-content">
          <span>已选择 {{ multipleSelection.length }} 条记录</span>
          <div class="batch-buttons">
            <el-button 
              size="small" 
              type="primary" 
              @click="handleBatchProcess"
              :disabled="!hasUnprocessedWarnings">批量处理</el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleBatchDelete">批量删除</el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 预警详情对话框 -->
    <el-dialog title="预警详情" :visible.sync="detailDialogVisible" width="50%">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预警ID">{{ currentDetail.id }}</el-descriptions-item>
        <el-descriptions-item label="化学品名称">{{ currentDetail.chemical && currentDetail.chemical.name }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">{{ currentDetail.warningType }}</el-descriptions-item>
        <el-descriptions-item label="预警等级">
          <el-tag :type="getWarningLevelTag(currentDetail.warningLevel)">
            {{ getWarningLevelText(currentDetail.warningLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警时间">{{ currentDetail.warningTime }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getStatusTag(currentDetail.status)">
            {{ getStatusText(currentDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentDetail.handler }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ currentDetail.handleTime }}</el-descriptions-item>
        <el-descriptions-item label="处理结果">{{ currentDetail.handleResult }}</el-descriptions-item>
      </el-descriptions>
      <div class="warning-content">
        <h4>预警内容：</h4>
        <p>{{ currentDetail.warningContent }}</p>
      </div>
    </el-dialog>

    <!-- 预警处理对话框 -->
    <el-dialog title="预警处理" :visible.sync="processDialogVisible" width="40%">
      <el-form :model="processForm" :rules="processRules" ref="processForm" label-width="100px">
        <el-form-item label="处理结果" prop="handleResult">
          <el-input type="textarea" v-model="processForm.handleResult"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="processDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitProcess">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑预警对话框 -->
    <el-dialog :title="form.id ? '编辑预警' : '新增预警'" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="化学品" prop="chemicalName">
          <el-input v-model="form.chemicalName" placeholder="请输入化学品名称"></el-input>
        </el-form-item>
        <el-form-item label="预警类型" prop="warningType">
          <el-select v-model="form.warningType" placeholder="请选择预警类型" style="width: 100%;">
            <el-option label="库存预警" value="stock"></el-option>
            <el-option label="库存不足" value="low"></el-option>
            <el-option label="超储预警" value="high"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警等级" prop="warningLevel">
          <el-select v-model="form.warningLevel" placeholder="请选择预警等级" style="width: 100%;">
            <el-option label="一般" value="normal"></el-option>
            <el-option label="严重" value="serious"></el-option>
            <el-option label="紧急" value="urgent"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警内容" prop="warningContent">
          <el-input type="textarea" v-model="form.warningContent" rows="3"></el-input>
        </el-form-item>
        <el-form-item label="预警时间" prop="warningTime">
          <el-date-picker
            v-model="form.warningTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%;">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 批量处理对话框 -->
    <el-dialog title="批量处理预警" :visible.sync="batchProcessDialogVisible" width="40%">
      <p>您选择了 {{ multipleSelection.length }} 条预警记录，其中 {{ unprocessedCount }} 条未处理。</p>
      <el-form :model="batchProcessForm" :rules="processRules" ref="batchProcessForm" label-width="100px">
        <el-form-item label="处理结果" prop="handleResult">
          <el-input type="textarea" v-model="batchProcessForm.handleResult"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchProcessDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitBatchProcess">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Warning',
  data() {
    return {
      // 查询条件
      searchForm: {
        chemicalName: '',
        warningType: '',
        warningLevel: '',
        status: '',
        timeRange: []
      },
      // 化学品选项
      chemicalOptions: [],
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
      // 多选数据
      multipleSelection: [],
      // 批量处理对话框
      batchProcessDialogVisible: false,
      batchProcessForm: {
        handleResult: ''
      },
      // 统计数据
      statistics: {
        stockWarning: 0,
        stockLow: 0,
        stockHigh: 0,
        unhandled: 0
      },
      // 详情对话框
      detailDialogVisible: false,
      currentDetail: {},
      // 处理对话框
      processDialogVisible: false,
      processForm: {
        handleResult: ''
      },
      processRules: {
        handleResult: [
          { required: true, message: '请输入处理结果', trigger: 'blur' }
        ]
      },
      // 新增/编辑对话框
      dialogVisible: false,
      form: {
        id: null,
        chemicalId: null,
        warningType: '',
        warningLevel: '',
        warningContent: '',
        warningTime: '',
        status: 'unprocessed'
      },
      rules: {
        chemicalId: [
          { required: true, message: '请选择化学品', trigger: 'change' }
        ],
        warningType: [
          { required: true, message: '请选择预警类型', trigger: 'change' }
        ],
        warningLevel: [
          { required: true, message: '请选择预警等级', trigger: 'change' }
        ],
        warningContent: [
          { required: true, message: '请输入预警内容', trigger: 'blur' }
        ],
        warningTime: [
          { required: true, message: '请选择预警时间', trigger: 'change' }
        ]
      }
    }
  },
  // 页面加载时使用的方法
  created() {
    this.fetchData()
    this.fetchStatistics()
    this.fetchChemicals()
  },
  computed: {
    // 获取未处理预警数量
    unprocessedCount() {
      return this.multipleSelection.filter(warning => 
        warning.status === 'unprocessed' || warning.status === '未处理'
      ).length
    },
    
    // 检查是否有未处理预警
    hasUnprocessedWarnings() {
      return this.unprocessedCount > 0
    }
  },
  methods: {
    // 查询预警信息
    async fetchData() {
      this.loading = true;
      try {
        const { startTime, endTime } = this.getTimeRange();
        
        console.log('查询参数:', {
          chemicalName: this.searchForm.chemicalName,
          warningType: this.searchForm.warningType,
          warningLevel: this.searchForm.warningLevel,
          status: this.searchForm.status,
          startTime,
          endTime,
          page: this.pagination.currentPage,
          size: this.pagination.pageSize
        });
        
        const response = await this.$http.get('/warning/list', {
          params: {
            chemicalName: this.searchForm.chemicalName || null,
            warningType: this.searchForm.warningType || null,
            warningLevel: this.searchForm.warningLevel || null,
            status: this.searchForm.status || null,
            startTime: startTime || null,
            endTime: endTime || null,
            page: this.pagination.currentPage,
            size: this.pagination.pageSize
          }
        });
        
        if (response.data && response.data.code === 200 && response.data.data) {
          console.log('预警 API 返回数据:', response.data.data);
          
          // 处理API返回的数据
          if (Array.isArray(response.data.data)) {
            // 直接返回的预警记录数组
            this.tableData = response.data.data;
            this.pagination.total = response.data.data.length;
          } else if (response.data.data.records && Array.isArray(response.data.data.records)) {
            // 分页格式
            this.tableData = response.data.data.records;
            this.pagination.total = response.data.data.total || 0;
            this.pagination.currentPage = response.data.data.current || this.pagination.currentPage;
            this.pagination.pageSize = response.data.data.size || this.pagination.pageSize;
          } else {
            this.$message.error('无法识别的数据格式');
            this.tableData = [];
            this.pagination.total = 0;
          }
          
          // 根据表格数据更新本地统计
          this.updateLocalStatistics();
          
          if (this.tableData.length === 0) {
            this.$message.info('未找到符合条件的预警记录');
          }
        } else {
          this.$message.error(response.data.message || '获取预警记录失败');
          this.tableData = [];
          this.pagination.total = 0;
        }
      } catch (error) {
        console.error('获取预警信息错误:', error);
        this.$message.error('获取预警信息失败: ' + (error.message || '服务器错误'));
        this.tableData = [];
        this.pagination.total = 0;
      } finally {
        this.loading = false;
      }
    },
    
    // 获取时间范围
    getTimeRange() {
      const range = this.searchForm.timeRange;
      if (!range || !range.length || range.length < 2) {
        return {
          startTime: null,
          endTime: null
        };
      }
      
      // 格式化日期为yyyy-MM-dd格式
      const formatDate = (date) => {
        if (!date) return null;
        const d = new Date(date);
        const year = d.getFullYear();
        const month = ('0' + (d.getMonth() + 1)).slice(-2);
        const day = ('0' + d.getDate()).slice(-2);
        return `${year}-${month}-${day}`;
      };
      
      return {
        startTime: formatDate(range[0]),
        endTime: formatDate(range[1])
      };
    },
    
    // 获取统计数据
    async fetchStatistics() {
      try {
        const response = await this.$http.get('/warning/statistics');
        console.log('统计数据响应:', response.data);
        
        if (response.data && response.data.code === 200 && response.data.data) {
          this.statistics = {
            stockWarning: response.data.data.stockWarning || 0,
            stockLow: response.data.data.stockLow || 0,
            stockHigh: response.data.data.stockHigh || 0,
            unhandled: response.data.data.unhandled || 0
          };
        } else {
          console.error('获取统计数据失败:', response.data.message);
          // 如果从服务器获取失败，使用本地计算
          this.updateLocalStatistics();
        }
      } catch (error) {
        console.error('获取统计数据异常:', error);
        // 如果出现异常，使用本地计算
        this.updateLocalStatistics();
      }
    },
    
    // 从本地表格数据计算统计数据
    updateLocalStatistics() {
      // 计算不同类型的预警数量
      let stockWarning = 0;
      let stockLow = 0;
      let stockHigh = 0;
      let unhandled = 0;
      
      for (const warning of this.tableData) {
        if (warning.warningType === 'stock') {
          stockWarning++;
        } else if (warning.warningType === 'low') {
          stockLow++;
        } else if (warning.warningType === 'high') {
          stockHigh++;
        }
        
        if (warning.status === 'unprocessed' || warning.status === '未处理') {
          unhandled++;
        }
      }
      
      this.statistics = {
        stockWarning,
        stockLow,
        stockHigh,
        unhandled
      };
    },
    
    // 获取化学品列表
    async fetchChemicals() {
      try {
        const response = await this.$http.get('/chemical/list');
        console.log('化学品列表响应:', response.data);
        
        if (response.data && response.data.code === 200) {
          if (response.data.data && response.data.data.list) {
            this.chemicalOptions = response.data.data.list;
          } else {
            this.chemicalOptions = response.data.data || [];
          }
        } else {
          this.$message.error(response.data.message || '获取化学品列表失败');
        }
      } catch (error) {
        console.error('获取化学品列表失败:', error);
        this.$message.error('获取化学品列表失败: ' + (error.message || error));
      }
    },
    // 获取预警类型标签
    getWarningTypeTag(type) {
      const types = {
        'stock': 'warning',
        'low': 'danger',
        'high': 'info'
      }
      return types[type] || ''
    },
    // 获取预警类型文本
    getWarningTypeText(type) {
      const texts = {
        'stock': '库存预警',
        'low': '库存不足',
        'high': '超储预警'
      }
      return texts[type] || type
    },
    // 获取预警等级标签
    getWarningLevelTag(level) {
      const levels = {
        'normal': 'info',
        'serious': 'warning',
        'urgent': 'danger'
      }
      return levels[level] || ''
    },
    // 获取预警等级文本
    getWarningLevelText(level) {
      const texts = {
        'normal': '一般',
        'serious': '严重',
        'urgent': '紧急'
      }
      return texts[level] || level
    },
    // 获取状态标签
    getStatusTag(status) {
      const statuses = {
        'unprocessed': 'danger',
        'processing': 'warning',
        'processed': 'success'
      }
      return statuses[status] || ''
    },
    // 获取状态文本
    getStatusText(status) {
      const texts = {
        'unprocessed': '未处理',
        'processing': '处理中',
        'processed': '已处理'
      }
      return texts[status] || status
    },
    // 处理查询
    handleSearch() {
      this.fetchData();
    },
    // 重置查询条件
    resetSearch() {
      this.searchForm = {
        chemicalName: '',
        warningType: '',
        warningLevel: '',
        status: '',
        timeRange: []
      };
      this.handleSearch();
    },
    // 处理详情
    handleDetail(row) {
      this.currentDetail = row
      this.detailDialogVisible = true
    },
    // 处理预警
    handleProcess(row) {
      this.currentDetail = row
      this.processForm = {
        handleResult: ''
      }
      this.processDialogVisible = true
    },
    // 提交处理
    submitProcess() {
      this.$refs.processForm.validate(async valid => {
        if (valid) {
          this.loading = true;
          try {
            const username = this.$store.getters.name || '系统管理员';
            
            console.log('提交预警处理，ID:', this.currentDetail.id, '处理人:', username, '处理结果:', this.processForm.handleResult);
            
            // 确保ID存在且为整数
            const warningId = parseInt(this.currentDetail.id);
            if (!warningId) {
              this.$message.error('预警ID无效');
              this.loading = false;
              return;
            }
            const response = await this.$http.post(`/warning/handle/${warningId}`, null, {
              params: {
                handler: username,
                handleResult: this.processForm.handleResult
              }
            });
            
            console.log('处理结果响应:', response.data);
            
            if (response.data && (response.data.code === 200 || response.data === true)) {
              this.$message.success('处理成功');
              this.processDialogVisible = false;
              this.fetchData();
              this.fetchStatistics();
            } else {
              this.$message.error((response.data && response.data.message) || '处理失败，请检查预警状态是否已被修改');
            }
          } catch (error) {
            console.error('处理失败:', error);
            this.$message.error('处理失败: ' + (error.message || error));
          } finally {
            this.loading = false;
          }
        }
      });
    },
    // 检查并生成预警
    async checkAndGenerateWarnings() {
      this.loading = true;
      try {
        console.log('开始检查预警...');
        const response = await this.$http.post('/warning/check');
        console.log('预警检查结果:', response.data);
        
        if (response.data && response.data.code === 200) {
          if (response.data.data && response.data.data > 0) {
            this.$message.success(`预警检查完成，发现${response.data.data}个新预警`);
          } else {
            this.$message.success('预警检查完成，未发现新预警');
          }
        } else {
          this.$message.info(response.data && response.data.message || '预警检查完成，未发现新预警');
        }
        
        // 刷新数据和统计
        await this.fetchData();
        await this.fetchStatistics();
      } catch (error) {
        console.error('预警检查出错:', error);
        this.$message.error('预警检查失败: ' + (error.message || '服务器错误'));
      } finally {
        this.loading = false;
      }
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
    
    // 处理新增
    handleAdd() {
      this.form = {
        id: null,
        chemicalId: null,
        warningType: '',
        warningLevel: '',
        warningContent: '',
        warningTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
        status: 'unprocessed'
      }
      this.dialogVisible = true
      // 清除之前的验证结果
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    
    // 处理提交表单
    async handleSubmit() {
      this.$refs.form.validate(function(valid) {
        if (valid) {
          this.submitLoading = true;
          
          try {
            // 构建请求参数
            const params = {
              chemicalId: this.form.chemicalId,
              warningType: this.form.warningType,
              warningLevel: this.form.warningLevel,
              warningContent: this.form.warningContent,
              warningTime: this.form.warningTime,
              handleStatus: this.form.status || 'unprocessed'
            };
            
            // 更新时需要传递ID
            if (this.form.id) {
              params.id = this.form.id;
            }
            
            // 发送请求
            const url = this.form.id ? `/warning/update/${this.form.id}` : '/warning/add';
            const method = this.form.id ? 'put' : 'post';
            
            this.$http[method](url, params).then(function(response) {
              if (response.data && response.data.code === 200) {
                this.$message.success(this.form.id ? '更新成功' : '添加成功');
                this.closeDialog();
                this.fetchData();
                this.fetchStatistics();
              } else {
                this.$message.error((response.data && response.data.message) || (this.form.id ? '更新失败' : '添加失败'));
              }
              this.submitLoading = false;
            }.bind(this)).catch(function(error) {
              console.error(this.form.id ? '更新预警失败:' : '添加预警失败:', error);
              this.$message.error((this.form.id ? '更新失败: ' : '添加失败: ') + (error.message || '服务器错误'));
              this.submitLoading = false;
            }.bind(this));
          } catch (error) {
            console.error(this.form.id ? '更新预警失败:' : '添加预警失败:', error);
            this.$message.error((this.form.id ? '更新失败: ' : '添加失败: ') + (error.message || '服务器错误'));
            this.submitLoading = false;
          }
        }
      }.bind(this));
    },
    // 处理表格多选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    
    // 批量处理
    handleBatchProcess() {
      if (this.unprocessedCount === 0) {
        this.$message.warning('没有可处理的预警记录')
        return
      }
      
      this.batchProcessForm = {
        handleResult: ''
      }
      this.batchProcessDialogVisible = true
      // 清除之前的验证结果
      this.$nextTick(() => {
        if (this.$refs.batchProcessForm) {
          this.$refs.batchProcessForm.clearValidate()
        }
      })
    },
    
    // 提交批量处理
    async submitBatchProcess() {
      this.$refs.batchProcessForm.validate(async valid => {
        if (valid) {
          this.loading = true;
          try {
            const username = this.$store.getters.name || '系统管理员';
            const unprocessedWarnings = this.multipleSelection.filter(warning => 
              warning.status === 'unprocessed' || warning.status === '未处理'
            );
            
            console.log('批量处理', unprocessedWarnings.length, '条预警记录');
            
            if (unprocessedWarnings.length === 0) {
              this.$message.warning('没有可处理的预警记录');
              this.loading = false;
              this.batchProcessDialogVisible = false;
              return;
            }
            
            // 创建处理任务队列
            const tasks = unprocessedWarnings.map(warning => {
              const warningId = parseInt(warning.id);
              console.log('处理预警ID:', warningId);
              return this.$http.post(`/warning/handle/${warningId}`, null, {
                params: {
                  handler: username,
                  handleResult: this.batchProcessForm.handleResult
                }
              });
            });
            
            // 并行处理所有任务
            const results = await Promise.allSettled(tasks);
            
            // 统计成功和失败数量
            const success = results.filter(r => r.status === 'fulfilled' && 
              (r.value.data === true || r.value.data.code === 200)).length;
            const failed = results.length - success;
            
            if (success > 0) {
              this.$message.success(`批量处理完成: ${success}条成功` + (failed > 0 ? `, ${failed}条失败` : ''));
            } else {
              this.$message.error(`批量处理失败: ${failed}条失败`);
            }
            
            this.batchProcessDialogVisible = false;
            this.fetchData();
            this.fetchStatistics();
          } catch (error) {
            console.error('批量处理失败:', error);
            this.$message.error('批量处理失败: ' + (error.message || error));
          } finally {
            this.loading = false;
          }
        }
      });
    },
    
    // 批量删除
    handleBatchDelete() {
      this.$confirm('确认删除选中的 ' + this.multipleSelection.length + ' 条预警记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.loading = true;
        try {
          // 创建删除任务队列
          const tasks = this.multipleSelection.map(function(warning) {
            return this.$http.delete('/warning/delete/' + warning.id);
          }.bind(this));
          
          // 并行处理所有任务
          const results = await Promise.allSettled(tasks);
          
          // 统计成功和失败数量
          const success = results.filter(function(r) { return r.status === 'fulfilled'; }).length;
          const failed = results.length - success;
          
          this.$message.success('批量删除完成: ' + success + '条成功, ' + failed + '条失败');
          this.fetchData();
          this.fetchStatistics();
        } catch (error) {
          console.error('批量删除失败:', error);
          this.$message.error('批量删除失败: ' + (error.message || error));
        } finally {
          this.loading = false;
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    
    // 编辑预警
    handleEdit(row) {
      this.form = Object.assign({}, row);
      this.dialogVisible = true;
      this.dialogTitle = '编辑预警';
      this.isEdit = true;
    },

    // 关闭对话框
    closeDialog() {
      this.dialogVisible = false;
      this.$refs.form.resetFields();
    },

    // 删除预警
    handleDelete(row) {
      this.$confirm('确认删除该预警记录?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        this.$http.delete('/warning/delete/' + row.id)
          .then(function(response) {
            if (response.data && response.data.code === 200) {
              this.$message.success('删除成功');
              this.fetchData();
              this.fetchStatistics();
            } else {
              this.$message.error((response.data && response.data.message) || '删除失败');
            }
          }.bind(this))
          .catch(function(error) {
            console.error('删除预警失败:', error);
            this.$message.error('删除失败: ' + (error.message || '服务器错误'));
          }.bind(this));
      }.bind(this)).catch(function() {
        this.$message.info('已取消删除');
      }.bind(this));
    }
  }
}
</script>

<style scoped>
.warning-container {
  padding: 20px;
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
}

.warning-card .statistics-value {
  color: #E6A23C;
}

.danger-card .statistics-value {
  color: #F56C6C;
}

.info-card .statistics-value {
  color: #909399;
}

.primary-card .statistics-value {
  color: #409EFF;
}

.search-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.warning-content {
  margin-top: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.warning-content h4 {
  margin: 0 0 10px 0;
  color: #606266;
}

.warning-content p {
  margin: 0;
  color: #303133;
  line-height: 1.5;
}

.batch-operation {
  margin: 15px 0;
}

.batch-operation-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.batch-buttons {
  display: flex;
  gap: 10px;
}
</style> 