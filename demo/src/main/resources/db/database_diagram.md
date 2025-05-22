# 危化品管理系统数据库关系图

```
                                  +-------------------+
                                  |     chemical      |
                                  +-------------------+
                                  | PK id             |
                                  | name              |
                                  | category          |
                                  | danger_level      |
                                  | storage_condition |
                                  | warning_threshold |
                                  | description       |
                                  +-------------------+
                                     |        |
                                     |        |
                 +------------------+          +----------------+
                 |                                              |
                 |                                              |
    +-------------------+                           +-------------------+
    |     inventory     |                           |   warning_record  |
    +-------------------+                           +-------------------+
    | PK id             |                           | PK id             |
    | FK chemical_id    |                           | FK chemical_id    |
    | current_amount    |                           | warning_type      |
    | unit              |                           | warning_level     |
    | location          |                           | warning_content   |
    | last_check_time   |                           | status            |
    | create_time       |                           | warning_time      |
    +-------------------+                           | handle_time       |
                                                    | handler           |
                                                    | handle_result     |
                                                    | create_time       |
                                                    +-------------------+
                                                    
                                                    
                 +-------------------+
                 |   usage_record    |
                 +-------------------+
                 | PK id             |
                 | FK chemical_id    |<--------------+
                 | FK user_id        |<----+         |
                 | amount            |     |         |
                 | unit              |     |         |
                 | usage_time        |     |         |
                 | usage_purpose     |     |         |
                 | notes             |     |         |
                 | create_time       |     |         |
                 +-------------------+     |         |
                                           |         |
                                           |         |
                 +-------------------+     |         |
                 |        man        |     |         |
                 +-------------------+     |         |
                 | PK id             |-----+         |
                 | name              |               |
                 | gender            |               |
                 | phone             |               |
                 | email             |               |
                 | department        |               |
                 | position          |               |
                 | create_time       |               |
                 +-------------------+               |
                                                     |
                                                     |
                                     +-------------------+
                                     |     chemical      |
                                     +-------------------+
                                     | PK id             |---------------+
                                     | name              |
                                     | category          |
                                     | danger_level      |
                                     | storage_condition |
                                     | warning_threshold |
                                     | description       |
                                     +-------------------+
```

## 表间关系

1. **chemical (1) ---> (N) inventory**
   - 一种化学品可以有多个库存记录
   - 外键: `inventory.chemical_id` 引用 `chemical.id`

2. **chemical (1) ---> (N) warning_record**
   - 一种化学品可以有多个预警记录
   - 外键: `warning_record.chemical_id` 引用 `chemical.id`

3. **chemical (1) ---> (N) usage_record**
   - 一种化学品可以有多个使用记录
   - 外键: `usage_record.chemical_id` 引用 `chemical.id`

4. **man (1) ---> (N) usage_record**
   - 一个人员可以有多个使用记录
   - 外键: `usage_record.user_id` 引用 `man.id`

## 业务流程

1. **库存管理流程**:
   - 化学品入库 -> 更新库存记录 -> 检查库存量 -> 触发预警(如果需要)
   - 化学品出库 -> 更新库存记录 -> 检查库存量 -> 触发预警(如果需要)
   - 定期库存盘点 -> 更新库存记录 -> 检查库存量 -> 触发预警(如果需要)

2. **预警处理流程**:
   - 系统生成预警记录 -> 管理员查看预警 -> 处理预警 -> 更新预警状态

3. **使用记录流程**:
   - 用户申请使用化学品 -> 记录使用信息 -> 更新库存 -> 检查库存量 -> 触发预警(如果需要) 