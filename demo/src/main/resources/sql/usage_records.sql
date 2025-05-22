-- 使用记录数据
-- 注意：以下数据与storage_outbound_records.sql中的入库和出库记录对应

-- 清空表（谨慎使用）
-- TRUNCATE TABLE usage_record;

-- =====================================================
-- 使用记录数据
-- =====================================================

-- 1. 硫酸使用记录（对应出库记录ID: 201）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (1, 1, '硫酸', 5.0, 'L', '2023-05-10 10:30:00', 5, '张三', '实验测试', '实验室研究', '用于pH值调节实验', '2023-05-10 10:30:00');

-- 2. 氢氧化钠使用记录（对应出库记录ID: 202）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (2, 2, '氢氧化钠', 3.0, 'kg', '2023-05-12 15:40:00', 6, '李四', '质量控制', '工艺改进', '用于废水处理实验', '2023-05-12 15:40:00');

-- 3. 乙醇使用记录（对应出库记录ID: 203）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (3, 3, '乙醇', 2.5, 'L', DATE_ADD(NOW(), INTERVAL 2 HOUR), 7, '王五', '产品消毒', '生产加工', '用于实验设备消毒', DATE_ADD(NOW(), INTERVAL 2 HOUR));

-- 4. 丙酮使用记录（对应出库记录ID: 204）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (4, 4, '丙酮', 1.8, 'L', DATE_SUB(NOW(), INTERVAL 12 HOUR), 5, '张三', '溶剂使用', '实验室研究', '用于有机物提取', DATE_SUB(NOW(), INTERVAL 12 HOUR));

-- 5. 氢氧化钠使用记录（对应出库记录ID: 205）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (5, 2, '氢氧化钠', 4.2, 'kg', DATE_FORMAT(NOW() ,'%Y-%m-01 14:30:00'), 7, '王五', '清洗剂', '设备维护', '用于管道清洗作业', DATE_FORMAT(NOW() ,'%Y-%m-01 14:30:00'));

-- 6. 盐酸使用记录（对应出库记录ID: 206）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (6, 5, '盐酸', 3.5, 'L', DATE_SUB(NOW(), INTERVAL 6 DAY), 6, '李四', '清洗实验器材', '设备维护', '用于金属器材酸洗', DATE_SUB(NOW(), INTERVAL 6 DAY));

-- 7. 过氧化氢使用记录（对应出库记录ID: 207）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (7, 6, '过氧化氢', 2.3, 'L', DATE_SUB(NOW(), INTERVAL 12 HOUR), 8, '赵六', '氧化反应', '实验室研究', '用于有机污染物降解', DATE_SUB(NOW(), INTERVAL 12 HOUR));

-- 8. 甲醇使用记录（对应出库记录ID: 208）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (8, 7, '甲醇', 1.5, 'L', DATE_ADD(NOW(), INTERVAL 1 HOUR), 9, '钱七', '色谱分析', '产品分析', '用于HPLC分析', DATE_ADD(NOW(), INTERVAL 1 HOUR));

-- 9. 氯化钠使用记录（对应出库记录ID: 209）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (9, 8, '氯化钠', 2.0, 'kg', DATE_ADD(NOW(), INTERVAL 3 HOUR), 7, '王五', '配制缓冲液', '实验室研究', '用于细胞培养实验', DATE_ADD(NOW(), INTERVAL 3 HOUR));

-- 10. 乙酸乙酯使用记录（对应出库记录ID: 210）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (10, 9, '乙酸乙酯', 3.2, 'L', DATE_SUB(NOW(), INTERVAL 2 DAY), 5, '张三', '有机提取', '实验室研究', '用于天然产物提取', DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 11. 丙三醇使用记录（对应出库记录ID: 211）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (11, 10, '丙三醇', 5.0, 'L', DATE_SUB(NOW(), INTERVAL 29 DAY), 10, '孙八', '保湿剂', '产品开发', '用于护肤品配方开发', DATE_SUB(NOW(), INTERVAL 29 DAY));

-- 12. 碳酸钠使用记录（对应出库记录ID: 212）
INSERT INTO usage_record (id, chemical_id, chemical_name, amount, unit, usage_time, user_id, user_name, purpose, usage_type, notes, create_time)
VALUES (12, 11, '碳酸钠', 2.7, 'kg', DATE_ADD(NOW(), INTERVAL 4 HOUR), 8, '赵六', 'pH调节', '水处理', '用于废水处理pH调节', DATE_ADD(NOW(), INTERVAL 4 HOUR)); 