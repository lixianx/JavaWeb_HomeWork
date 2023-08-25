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
![image](https://github.com/lixianx/my_webplatform/blob/main/images/7OI4UB5FUJ8%7DM17Q4%25_I0DO.png?raw=true)
### 3.输入项目地址，点击克隆
![image](https://github.com/lixianx/my_webplatform/blob/main/images/6O@GRPR\)N%7BY\)%5D7PNVZF%5BLUU.png?raw=true)
## 3.项目配置
### 1.配置web框架
![image](https://github.com/lixianx/my_webplatform/blob/main/images/FCTLU6JM@OIK%60M%7DKRU%5B_XII.png?raw=true)
### 2.打开项目结构添加依赖
![image](https://github.com/lixianx/my_webplatform/blob/main/images/9RD5A%5DDDV$K%7B2_Z2X~QRY%7DI.png?raw=true)
### 3.添加Tomcat目录下的JSP-api.jar和Servlet-api.jar及SDK
![image](https://github.com/lixianx/my_webplatform/blob/main/images/8~8G62$S%7DG@6LZW_WW\(2FGS.png?raw=true)
### 4.添加工件
![image](https://github.com/lixianx/my_webplatform/blob/main/images/%5D3@2B%604DT%25658%5BMMQB%5D%25XZR.png?raw=true)
### 5.添加本地Tomcat配置
![image](https://github.com/lixianx/my_webplatform/blob/5c97aec4e4f8b5f3321d8c0abfc6f38fdc16d26e/images/%25A%40\(66UP1R64L\)J%409AP7\)%7DT.png)
### 6.设置自己的Tomcat路径
![image](https://github.com/lixianx/my_webplatform/blob/main/images/BEA7WHY3U85K$4%7DZR~G5381.png?raw=true)
### 7.点击部署，添加工件
    可更改项目名
![image](https://github.com/lixianx/my_webplatform/blob/main/images/B%5DTUV@8%60$W@U3CN5RR73T%5BO.png?raw=true)
### 8.下载本仓库根目录下的tables.sql文件，登录Mysql，选择一个数据库输入以下代码建表(均为空表）
```
source tables.sql路径
```
### 9.找到src目录下的resource包里的两个配置文件，按要求更改
若未开通阿里云短信服务，可在user表中添加用户，直接登录
![image](https://github.com/lixianx/my_webplatform/blob/main/images/FB9O1K8X7%60ECH6%5B\)SH7NV~C.png?raw=true)
![image](https://github.com/lixianx/my_webplatform/blob/main/images/%60%7DL~%7BFS%5B2F1D49Z%60FHK%60%7DNT.png?raw=true)
### 点击调试，成功访问到主页
![image](https://github.com/lixianx/my_webplatform/blob/main/images/ATEZ7AO7%7BGWLR@P@AFT@S%256.png?raw=true)
