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
- ✅ docker容器部署
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

### 1. clone项目
```shell
git clone https://github.com/Tonytan123/bankManageSystem.git
cd bankManageSystem
```
### 2. 构建jar包
```shell
mvn clean package
mvn install
```
构建成功后，jar包位于路径/bankManageSystem/target/bankManageSystem-0.0.1-SNAPSHOT.jar

### 3. 构建Docker镜像
   在bankManageSystem目录下执行下面的命令
```shell
chmod +x start.sh
./start.sh
```
### 4. 访问服务api 
启动成功之后，通过http://localhost:8080 访问api

----

## 🧪 测试
执行下面命令，执行所有测试任务
```shell
mvn test
```
-----

## 📖 API 文档
### 1. 创建银行账号
- PATH： /bank/account/manage/v1/createBankAccount
- METHOD：  POST
- Content： Application/JSON
- RequestBody
```json
{
    "accountHolderName":"tucker", #用户名
    "contactNumber":"123456789", #联系电话号码
    "idCard": "1234567890", #身份证号
    "bankCardNumber" : "123456788890", #银行卡号
    "userId":"1111111", # 用户id
    "emailAddress": "", # 邮箱地址
    "balance" : "1.22", #账户余额
    "description" : "新增账户" #简要描述
}
```
- Response
```json
{
    "code": 200,
    "data": {
        "id": 1,
        "userUid": "1111111",
        "idCard": "1234567890",
        "accountHolderName": "tucker",
        "contactNumber": "123456789",
        "bankCardNumber": "123456788890",
        "balance": 1.22,
        "status": "ACTIVE",
        "description": "新增账户",
        "emailAddress": "",
        "createdAt": "2025-09-14T15:07:34.6885039",
        "updatedAt": "2025-09-14T15:07:34.6885039"
    },
    "message": "ok",
    "description": ""
}
```

### 2. 更新银行账号
- PATH： /bank/account/manage/v1/updateBankAccount
- METHOD：  POST
- Content： Application/JSON
- RequestBody
```json
{
    "accountHolderName":"tucker", #用户名
    "contactNumber":"123456789", #联系电话号码
    "idCard": "1234567890", #身份证号
    "bankCardNumber" : "123456788890", #银行卡号
    "userId":"1111111", # 用户id
    "emailAddress": "", # 邮箱地址
    "balance" : "1.22", #账户余额
    "description" : "新增账户", #简要描述
    "status": "ACTIVE" # 银行卡状态
}
```
- Response
```json
{
    "code": 200,
    "data": null,
    "message": "ok",
    "description": "update success"
}
```

### 3. 删除银行账号
- PATH： /bank/account/manage/v1/deleteBankAccount
- METHOD：  POST
- Content： Application/JSON
- RequestBody
```json
{
    "bankCardNumber" : "123456788890" #银行卡号
}
```
- Response
```json
{
    "code": 200,
    "data": null,
    "message": "ok",
    "description": "delete success"
}
```

### 3. 列出用户银行账号分页
- PATH： /bank/account/manage/v1/getBankAccountPage/userId/{userId}?pageNo=#{pageNo}&&pageSize=#{pageSize}
- METHOD：  GET
- Content： Application/JSON
- Response
```json
{
    "code": 200,
    "data": {
        "page": 1, # 当前所在页数
        "pageSize": 10, #当前页数包含记录数量
        "totalPage": 1, #总共多少页
        "total": 1, #记录总数
        "data": [
            {
                "accountHolderName": "tucker",
                "contactNumber": "123456789",
                "idCard": "1234567890",
                "emailAddress": "",
                "balance": 1.22,
                "description": "新增账户",
                "bankCardNumber": "123456788890",
                "userId": "1111111"
            }
        ],
        "first": false,
        "last": true
    },
    "message": "ok",
    "description": ""
}
```

### 4. 列出用户银行账号详情
- PATH： /bank/account/manage/v1/getBankAccountDetail/bankCardNumber/{bankCardNumber}
- METHOD：  GET
- Content： Application/JSON
- Response
```json
    "code": 200,
    "data": {
        "id": 1,
        "userUid": "1111111",
        "idCard": "1234567890",
        "accountHolderName": "tucker",
        "contactNumber": "123456789",
        "bankCardNumber": "123456788890",
        "balance": 1.22,
        "status": "ACTIVE",
        "description": "新增账户",
        "emailAddress": "",
        "createdAt": "2025-09-14T15:07:34.6885039",
        "updatedAt": "2025-09-14T15:07:34.6885039"
    },
    "message": "ok",
    "description": ""
```

### 5. 不同账号进行相互转账
- PATH： /bank/account/manage/v1/bankTransfer
- METHOD：  POST
- Content： Application/JSON
- RequestBody
```json
{
    "sendAccountHolderName":"tucker", # 转出方
    "sendBankCardNumber":"123456788890", #转出方银行账号
    "amount":"1", # 转出额度
    "receiveAccountHolderName":"tony", # 转入方
    "receiveBankCardNumber":"123456788899" # 转入方银行账号
}
```
- Response
```json
{
    "code": 200,
    "data": {
        "id": 1,
        "userUid": "1111111",
        "idCard": "1234567890",
        "accountHolderName": "tucker",
        "contactNumber": "123456789",
        "bankCardNumber": "123456788890",
        "balance": 1.22,
        "status": "ACTIVE",
        "description": "新增账户",
        "emailAddress": "",
        "createdAt": "2025-09-14T15:07:34.6885039",
        "updatedAt": "2025-09-14T15:07:34.6885039"
    },
    "message": "ok",
    "description": ""
}
```
