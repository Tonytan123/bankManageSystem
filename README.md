# 银行账户管理系统
a simple application for managing bank accounts in a banking system

## 📌  项目概述
本项目为一个基于springboot 框架设计的轻量级银行账户管理系统，支持以下能力：
- ✅ 创建银行账户
- ✅ 删除银行账户
- ✅ 修改银行账户详情（例如，账户持有人姓名、联系信息）
- ✅ 列出所有银行账户（带分页）
- ✅ 查看特定银行账户的详情
- ✅ 在两个账户之间转账
- ✅ 错误处理和验证
- ✅ 单元测试&压力测试
-----
## 📦  技术栈使用
| 模块 |     工具     | 说明                     |
|------|--------------|------------------------------|
| 后端框架 | Spring Boot | 用于快速构建restful接口    | 
| 数据库| H2 Database     | 嵌入式数据库                     | 
| 缓存    | Caffeine     | 缓存实现                     | 
|项目构建 | Maven     | 项目构建                   |
| 容器化   | Docker     | 快速环境部署             |
| 单元测试| Mokito + Junit5    | 用于本地单元测试                 |
| 压力测试| ContiPerf | 用于本地压力测试                 |
------
## 🚀 快速开始

1. clone项目
   > git clone https://github.com/Tonytan123/bankManageSystem.git
   > cd bankManageSystem
2. 构建jar包
   > mvn clean package
   > mvn install
构建成功后，jar包位于target/bankManageSystem-0.0.1-SNAPSHOT.jar
3. 构建Docker镜像
   在bankManageSystem目录下执行下面的命令
   > chmod +x start.sh
   > ./start.sh
4. 访问服务api
 启动成功之后，通过http://localhost:8080 访问api
