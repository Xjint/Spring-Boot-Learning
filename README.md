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
* 搭建”发起问题“页面

## 3
* 插入问题，并且当未登录或者标题/描述/标签为空时提示出错
* user表中增加avator_url，这是用户头像的url地址
* 使用lombok依赖，可以自动为pojo添加get和set方法

## 4
* 完善显示问题的页面
* 由于每次修改页面或者改动代码时，都要手动重启服务器，
因此引入“热部署”,引入spring-boot-devtools依赖，以后每次做了改动之后，不需要重启服务器，
只需rebuild项目就好啦。但是，rebuild项目也是得我们手动点击，
所以，在Idea中开启自动编译的选项，这样，我们只要改动了代码，IDE就会自动为我们编译了，我们不需要做任何额外的操作
* 实现分页功能

## 5
* 完成“我的问题”页面，展示我自己提的问题
* 添加拦截器 减少代码冗余

## 6
* 实现问题详情的功能，完成展示的页面
* 增强编辑功能，当问题不是我提出的时候，不显示编辑按钮
* 完成退出登陆功能

## 7
* 实现编辑问题的功能，点击编辑按钮时，跳转到发布问题的页面，但是里面的内容是自动填充的(就是这个要修改的问题)
* 使用mybatis generator插件，自动生成？？？(可能是sql语句),以后当有新表或者表变更时，不需要自己手动地去改mapper
* 添加检查异常的功能，使页面对用户更加友好，当访问不存在的问题时，页面显示”问题不存在“，当输入无效子地址时，显示”无效请求“

## 8
* 实现阅读数的功能，当查看一个问题时，其阅读数加1
* 实现评论功能
    * 首先创建一个评论的数据库表，mybatis generator这时就派上用场了，只需再generatorConfig.xml中简单配置一行，然后运行插件，就会自动生成dao层文件
    * 修改表中一些属性的数据类型，重构代码









---
问题1：不能设置每页显示的个数小于5？？？

问题2：我的问题页面，只有两页的问题，但是却显示了三页的标签，这个问题在主页显示所有问题的时候没有出现




    
    
