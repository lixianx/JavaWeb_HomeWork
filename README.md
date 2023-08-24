# 1.项目主体结构
```
JavaWeb_HomeWork
├─src
│  ├─com
│  │  └─oa
│  │      └─learn
│  │          ├─bean        封装类
│  │          ├─filter      过滤器
│  │          ├─servlet     servlet
│  │          └─utils       封装工具类
│  └─resources              数据库和阿里云短信服务的配置
└─web
    ├─images                图标
    └─WEB-INF         
        └─lib               依赖
```

                

# 2.项目运行方法
## 1.环境配置
JDK 17、Mysql 8.0、Tomcat 10.0
## 2.克隆项目
### 1.复制仓库地址
https://github.com/lixianx/JavaWeb_HomeWork.git
### 2.打开Idea，选择从CSV获取
### 3.输入项目地址，点击克隆
## 3.项目配置
### 1.配置web框架
### 2.打开项目结构添加依赖
### 3.添加Tomcat目录下的JSP-api.jar和Servlet-api.jar及SDK
### 4.添加工件
### 5.添加本地Tomcat配置
### 6.设置自己的Tomcat路径
### 7.点击部署，添加工件
    可更改项目名
### 8.下载本仓库根目录下的tables.sql文件，登录Mysql，选择一个数据库输入以下代码建表(均为空表）
```
source tables.sql路径
```
### 9.找到src目录下的resource包里的两个配置文件，按要求更改
若未开通阿里云短信服务，可直接在user表中添加用户，直接登录
### 点击调试，成功访问到主页
