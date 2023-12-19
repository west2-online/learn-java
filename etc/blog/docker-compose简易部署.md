## docker compose 简易编排部署

以springboot+mysql+redis作为示例参考

ps:

1. 以下操作均为**简易版**部署，具体开发以需求为准，文档仅供参考
2. 本文档仅留作记录（传承的艺术防止忘记命令），故具体操作细节没有补充，**使用前请保证自身能流畅使用docker**

***

### 文件前期准备

1. docker 安装

2. docker compose安装

3. 项目本体（springboot + redis + mysql）

4. maven打包项目为jar

#### maven打包要点：

1. 注意mysql和redis连接地址需要更改为底下docker-compose.yml文件中的对应容器名称（container_name）

   这边不知道为什么的需要复习docker network相关知识，docker compose启动容器会默认创建新network并将所有编排容器连接到此网络中使得能够相互访问，（也可以自己在yml里面自定义网络，更安全可控）

2. 注意服务端需要暴露的端口号



### 编写docerfile构建springboot镜像

部署步骤：

	1. 例如创建feige文件夹，mkdir feige 
	1. cd feige
	1. mkdir feigeapp

feige文件夹中要有docker-compose.yml 和 feigeapp 文件夹

feigeapp文件夹中编写dockerfile并将jar包放在此文件夹中

```do
# Docker image for springboot file run
# VERSION 0.0.1
# Author: 
# 基础镜像使用java
FROM openjdk:8
# 作者
MAINTAINER feige
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp 
# 将jar包添加到容器中并更名为xx.jar
ADD demo.jar test.jar 
# 运行jar包
RUN bash -c 'touch /test.jar'
# 指定docker容器启动时执行的命令，格式：
# ENTRYPOINT ["executable", "param1","param2"...]
# 例：指定docker容器启动时运行jar包
# ENTRYPOINT ["java", "-jar","/mall-tiny-docker-file.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test.jar"]

```

执行docker build . -t {命名}:{tag}

例：docker build . -t feige-demo:v1.0

到此镜像打包完成，可以自行选择上传阿里云或者docker云社区

### docker-compose编排部署所有容器

docker-compose.yml内容（大致）

``` dockerfile
#compose版本跟docker版本的对应关系
version: "3"

services:
 #指定服务名称
 mysql:
   #指定服务使用的镜像
   image: mysql:8.0.31
   #指定容器名称
   container_name: feigemysql
   restart: always
   #指定服务运行的端口
   ports :
     - "3306:3306"
   #指定容器的环境变量
   environment:
	   #数据库密码
     - MYSQL_ROOT_PASSWORD=123456
       #创建的库
     - MYSQL_DATABASE=feigemysql
       #允许多IP连接数据库
     - MYSQL_ROOT_HOST=%  
    
 #指定服务名称
 redis:
   image: redis
   #指定容器名称
   container_name: feigeredis
   ports:
     - "6379:6379"
   #设置redis密码
   command:
      --requirepass "xxxxx" 
   #这一行使得只有container内的root有真正的root权限
   #privileged: true
   
 #指定服务名称
 feigeapp:
# 镜像名:版本
image: feige-demo:v1
   container_name: feige-demo
   #restart: always
   #指定服务运行的端口
   ports:
     - "8080:8080"
   #启动时，要覆盖的环境变量配置
   environment:
  #数据库IP
     - DATABASE_HOST=mysql
     #数据库用户名
     - DATABASE_USER=root
     #数据库密码
     - DATABASE_PASSWORD=123456
     #初始化的数据库
     - DATABASE_NAME=feigemysql
     #数据库端口
     - DATABASE_PORT=3306
     #redis的IP
     - REDIS_HOST=redis
     #redis的端口
     - REDIS_PORT=6379
   #依赖的服务
   depends_on:
     #服务名称
     - mysql
     - redis
```

ps:

1. 示例中mysql没有使用volume挂载，挂载时需要宿主机文件以及容器中指定文件两个目录（注意目录必须存在且路径正确）否则无法启动
2.  当指定了**外部配置文件**与外部存储路径时（没有指定的话，不需要挂载mysql-files），也需要指定 /var/lib/mysql-files的外部目录，否则导致闪退问题
2.  端口号映射关系： {宿主机映射端口}:{docker容器端口}



### 一些常用命令（用于debug）

```do
#获取容器日志
docker logs {container_id}

#查看容器中进程信息ps
docker top {container_id}

#进入当前容器后开启一个新终端，可以在里面操作
docker exec 

#管理网络
docker network {--help}
```

