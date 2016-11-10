## disconf demo project


## 案例
* 包括 百度、滴滴出行、银联、网易、拉勾网、苏宁易购、顺丰科技 等互联网公司正在使用!
* 「disconf」在「2015 年度新增开源软件排名 TOP 100(OSC开源中国提供)」中排名第16强。

## 特性
* 支持配置项和配置文件
* 实时推模式
* 客户端支持xml和注解形式
* 数据持久化，支持本地文件容灾

## 与Diamond比较
—— | 淘宝Diamond[2]  | Disconf | 比较
----|------|------|----
数据持久性 | 存储在mysql上 | 存储在mysql上 | 都持久化到数据库里，都易于管理
推拉模型  | 拉模型，每隔15s拉一次全量数据 | 基于Zookeeper的推模型，实时推送  | disconf基于分布式的Zookeeper来实时推送，不断是在稳定性、实效性、易用性上均优于diamond
配置读写  | 支持实例对配置读写。支持某台实例写配置数据，并广播到其它实例上  | 只支持实例对配置读。通过在disconf-web上更新配置到达到广播写到所有应用实例  | 从目前的应用场景来看，实例对配置的写需求不是那么明显。disconf支持的中心化广播方案可能会与人性思考更加相似。
容灾  | 多级容灾模式，配置数据会dump在本地，避免中心服务挂机时无法使用  | 多级容灾模式，优先读取本地配置文件。 |  双方均支持在中心服务挂机时配置实例仍然可以使用
配置数据模型  | 只支持KV结构的数据，非配置文件模式 | 支持传统的配置文件模式（配置文件），亦支持KV结构数据(配置项) | 使用配置文件的编程方式可能与程序员的编程习惯更为相似，更易于接受和使用。
编程模型  | 需要将配置文件拆成多个配置项，没有明显的编程模型  | 在使用配置文件的基础上，提供了注解式和基于XML的两种编程模型  | 无
并发性  | 多条配置要同时生效时，无法解决并发同时生效的问题  |  基于注解式的配置，可以解决并发性问题  | 无

## 架构设计

* 集群模式

![架构设计](http://ww1.sinaimg.cn/bmiddle/60c9620fgw1ehi7wwkdtoj20nw0fz0uh.jpg)

* 模块设计

![模块设计](http://ww1.sinaimg.cn/bmiddle/60c9620fjw1eqi7cnhjp0j20e4097wfq.jpg)

* 配置获取流程

![配置加载流程](http://ww1.sinaimg.cn/bmiddle/60c9620fjw1eqj9zzgc7yj20b20pn41v.jpg)

## 部署步骤

* 安装依赖软件
   安装Mysql（>=5）
   安装Tomcat（apache-tomcat-7.0.50）
   安装Nginx（nginx/1.5.3）
   安装 zookeeeper （zookeeper-3.3.0）
   安装 Redis （2.4.5）

* Git clone 代码
    (https://github.com/knightliao/disconf.git)[https://github.com/knightliao/disconf.git]

* 设置disconf配置目录和war输出目录，拷贝修改数据库，zk，redis及app配置：
    jdbc-mysql.properties (数据库配置) 
    redis-config.properties (Redis配置，主要用于web登录使用) 
    zoo.properties (Zookeeper配置
    application.properties (应用配置）

* 构建部署包
```
ONLINE_CONFIG_PATH=/home/work/dsp/disconf-rd/online-resources WAR_ROOT_PATH=/home/work/dsp/disconf-rd/war 
export ONLINE_CONFIG_PATH 
export WAR_ROOT_PATH 
cd disconf-web 
sh deploy/deploy.sh
```

* 初始化数据库：可以参考 sql/readme.md 来进行数据库的初始化。注意顺序执行
```
0-init_table.sql
1-init_data.sql
201512/20151225.sql
20160701/20160701.sql
```

* 配置tomcat的context path：
```
<Context path="" docBase="/home/work/dsp/disconf-rd/war"></Context>
```

* 配置nginx
  ```
    upstream disconf {
        server 127.0.0.1:8080;
    }   
    
    server {
        listen 8991;
        server_name localhost;
        access_log /Users/fanchao/nginx/log/disconf/access.log;
        error_log /Users/fanchao/nginx/log/disconf/error.log;
    
    
        location / { 
            root /Users/fanchao/dsp/disconf-web/war/html;
            if ($query_string) {
                 expires max;
            }   
        }   
    
        location ~ ^/(api|export) {
            proxy_pass_header Server;
            proxy_set_header Host $http_host;
            proxy_redirect off;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Scheme $scheme;
            proxy_pass http://disconf;
        }   
     } 
```

## 客户端使用
* 引入POM依赖
```
<dependency>
      <groupId>com.baidu.disconf</groupId>
      <artifactId>disconf-client</artifactId>
      <version>2.6.36</version>
</dependency>
```

* 第一步：添加Disconf的支持。在applicationContext.xml里添加Bean定义：
```
<!-- 使用disconf必须添加以下配置 -->
<bean id="disconfMgrBean" class="com.baidu.disconf.client.DisconfMgrBean"
      destroy-method="destroy">
    <property name="scanPackage" value="com.example.disconf.demo"/>
</bean>
<bean id="disconfMgrBean2" class="com.baidu.disconf.client.DisconfMgrBeanSecond"
      init-method="init" destroy-method="destroy">
</bean>
```

* 增加包扫描路径和aop支持
```
<context:component-scan base-package="com.example"/>
<aop:aspectj-autoproxy proxy-target-class="true"/>
```

* 配置应用环境和应用标示
在web console管理后台，进行应用和环境注册

* 添加 disconf.properties
在应用程序中，添加disconf的配置，服务器地址，环境、版本和应用信息

* 在单例的spring  bean中使用注解，获取远程配置中心的配置
参考代码工程，[https://github.com/yfcchilly/disconf-demo](https://github.com/yfcchilly/disconf-demo)

## 参考文档
[http://disconf.readthedocs.io/zh_CN/latest/tutorial-client/index.html](http://disconf.readthedocs.io/zh_CN/latest/tutorial-client/index.html)