-- 库存数据
-- 添加多个危化品库存记录

-- 清空表（谨慎使用）
-- TRUNCATE TABLE inventory;

-- =====================================================
-- 库存数据
-- =====================================================

-- 1. 硫酸的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (1, 1, '危化品仓库A区-01', 15.0, 'L', NOW(), NOW(), NOW());

-- 2. 氢氧化钠的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (2, 2, '危化品仓库A区-02', 7.8, 'kg', NOW(), NOW(), NOW());

-- 3. 乙醇的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (3, 3, '危化品仓库B区-01', 7.5, 'L', NOW(), NOW(), NOW());

-- 4. 丙酮的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (4, 4, '危化品仓库B区-02', 6.2, 'L', NOW(), NOW(), NOW());

-- 5. 盐酸的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (5, 5, '危化品仓库A区-03', 8.5, 'L', NOW(), NOW(), NOW());

-- 6. 过氧化氢的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (6, 6, '危化品仓库C区-01', 7.7, 'L', NOW(), NOW(), NOW());

-- 7. 甲醇的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (7, 7, '危化品仓库C区-02', 8.5, 'L', NOW(), NOW(), NOW());

-- 8. 氯化钠的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (8, 8, '普通仓库D区-01', 8.0, 'kg', NOW(), NOW(), NOW());

-- 9. 乙酸乙酯的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (9, 9, '危化品仓库B区-03', 6.8, 'L', NOW(), NOW(), NOW());

-- 10. 丙三醇的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (10, 10, '普通仓库D区-02', 10.0, 'L', NOW(), NOW(), NOW());

-- 11. 碳酸钠的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (11, 11, '普通仓库D区-03', 7.3, 'kg', NOW(), NOW(), NOW());

-- 12. 氯化钾的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (12, 12, '普通仓库D区-04', 5.0, 'kg', NOW(), NOW(), NOW());

-- 13. 溴化钠的库存记录（库存量低于预警阈值）
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (13, 13, '危化品仓库A区-04', 0.8, 'kg', NOW(), NOW(), NOW());

-- 14. 硝酸的库存记录（库存量低于预警阈值）
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (14, 14, '危化品仓库A区-05', 1.2, 'L', NOW(), NOW(), NOW());

-- 15. 四氯化碳的库存记录（库存量低于预警阈值）
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (15, 15, '危化品仓库C区-03', 0.5, 'L', NOW(), NOW(), NOW());

-- 16. 氨水的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (16, 16, '危化品仓库C区-04', 4.5, 'L', NOW(), NOW(), NOW());

-- 17. 甲苯的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (17, 17, '危化品仓库B区-04', 3.8, 'L', NOW(), NOW(), NOW());

-- 18. 二氯甲烷的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (18, 18, '危化品仓库B区-05', 2.7, 'L', NOW(), NOW(), NOW());

-- 19. 碘化钾的库存记录
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (19, 19, '普通仓库D区-05', 3.2, 'kg', NOW(), NOW(), NOW());

-- 20. 氢氟酸的库存记录（库存量低于预警阈值）
INSERT INTO inventory (id, chemical_id, location, current_amount, unit, last_check_time, create_time, update_time)
VALUES (20, 20, '危化品仓库A区-06', 0.3, 'L', NOW(), NOW(), NOW()); 