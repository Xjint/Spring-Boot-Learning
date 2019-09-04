# Spring Boot实战

## 1
* 初始化仓库，关联本地仓库，用git管理源代码

* 使用bootstrap框架，快速搭建一个前端UI

* 关联Github，用Github账号登陆网站
    * fastjson okhttp依赖
    * 将一些配置文件写在application.properties文件中，使代码结构变得灵活稳健
    * 实现登陆跳转功能。当未登陆时，右上角显示为"登陆"，登陆成功后，显示为我的名字

* 使用H2数据库，h2数据库使用简单，导入maven依赖即可使用
    * springboot内置有数据源 

## 2
* 使用h2总是报用户名或密码错误，遂改用MySQL
* 持久化登陆状态，将token作为cookie，这样用户只要登陆过一次，就会在数据库中存入其数据，浏览器会保存其cookie,下次就会自动登陆
* 搭建“发起问题”页面



    
    
