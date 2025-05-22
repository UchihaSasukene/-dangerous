-- 危化品数据
-- 添加多个危化品信息记录

-- 清空表（谨慎使用）
-- TRUNCATE TABLE chemical;

-- =====================================================
-- 危化品数据
-- =====================================================

-- 1. 硫酸
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (1, '硫酸', '酸类', '一级', '阴凉干燥处', 10.0, '强酸，具有强烈的腐蚀性');

-- 2. 氢氧化钠
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (2, '氢氧化钠', '碱类', '二级', '密封保存', 5.0, '强碱，具有强烈的腐蚀性');

-- 3. 乙醇
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (3, '乙醇', '醇类', '二级', '阴凉干燥处', 5.0, '易燃，有麻醉作用');

-- 4. 丙酮
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (4, '丙酮', '酮类', '二级', '阴凉干燥处', 5.0, '易燃易爆，有刺激性');

-- 5. 盐酸
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (5, '盐酸', '酸类', '二级', '阴凉干燥处', 5.0, '强酸，具有腐蚀性');

-- 6. 过氧化氢
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (6, '过氧化氢', '氧化剂', '二级', '避光保存', 5.0, '强氧化剂，有腐蚀性');

-- 7. 甲醇
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (7, '甲醇', '醇类', '一级', '阴凉干燥处', 5.0, '易燃易爆，有毒');

-- 8. 氯化钠
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (8, '氯化钠', '无机盐', '三级', '常温保存', 5.0, '低毒，刺激皮肤和眼睛');

-- 9. 乙酸乙酯
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (9, '乙酸乙酯', '酯类', '二级', '阴凉干燥处', 5.0, '易燃，有刺激性');

-- 10. 丙三醇
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (10, '丙三醇', '醇类', '三级', '常温保存', 5.0, '低毒，对眼睛有刺激');

-- 11. 碳酸钠
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (11, '碳酸钠', '无机盐', '三级', '干燥处', 5.0, '刺激皮肤和眼睛');

-- 12. 氯化钾
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (12, '氯化钾', '无机盐', '三级', '常温保存', 3.0, '低毒，刺激皮肤和眼睛');

-- 13. 溴化钠
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (13, '溴化钠', '无机盐', '二级', '避光保存', 1.0, '有毒，刺激皮肤和眼睛');

-- 14. 硝酸
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (14, '硝酸', '酸类', '一级', '阴凉干燥处', 2.0, '强酸，强氧化性，有腐蚀性');

-- 15. 四氯化碳
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (15, '四氯化碳', '有机溶剂', '一级', '阴凉干燥处', 1.0, '有毒，致癌物质');

-- 16. 氨水
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (16, '氨水', '碱类', '二级', '密封保存', 3.0, '具有刺激性和腐蚀性');

-- 17. 甲苯
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (17, '甲苯', '芳香烃', '一级', '阴凉干燥处', 2.0, '易燃易爆，有毒');

-- 18. 二氯甲烷
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (18, '二氯甲烷', '有机溶剂', '一级', '阴凉干燥处', 2.0, '有毒，可能致癌');

-- 19. 碘化钾
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (19, '碘化钾', '无机盐', '三级', '避光保存', 2.0, '低毒，刺激皮肤和眼睛');

-- 20. 氢氟酸
INSERT INTO chemical (id, name, category, danger_level, storage_condition, warning_threshold, description)
VALUES (20, '氢氟酸', '酸类', '一级', '专用容器保存', 1.0, '极强的腐蚀性，剧毒'); 