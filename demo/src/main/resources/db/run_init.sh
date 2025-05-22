#!/bin/bash
echo "正在初始化数据库..."

# 设置MySQL连接参数
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=123456
MYSQL_DB=vueone

# 执行SQL脚本
mysql -h $MYSQL_HOST -P $MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DB < init_tables.sql

echo "数据库初始化完成！" 