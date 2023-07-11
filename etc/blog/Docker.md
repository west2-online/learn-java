## Docker

![image-20230627231147837](https://gitee.com/sky-dog/note/raw/master/img/202306272311949.png)

## 前言

* Docker概述
* Docker安装
* Docker命令
  * 镜像命令
  * 容器命令
  * 操作命令
  * etc.....
* Docker镜像
* 容器数据卷
* DockerFile
* Docker网络原理
* Idea整合Docker
* Docker Compose(集群)
* Docker Swarm
* CI\CD Jenkins



## 概述

### Docker是什么

Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的[镜像](https://baike.baidu.com/item/镜像/1574)中，然后发布到任何流行的 [Linux](https://baike.baidu.com/item/Linux)或[Windows](https://baike.baidu.com/item/Windows/165458)操作系统的机器上，也可以实现[虚拟化](https://baike.baidu.com/item/虚拟化/547949)。容器是完全使用[沙箱](https://baike.baidu.com/item/沙箱/393318)机制，相互之间不会有任何接口。<img src="https://gitee.com/sky-dog/note/raw/master/img/202207191257809.jpeg" alt="Docker" style="zoom:25%;" />



### Docker为什么出现

一款产品以前会有两个环境：开发环境，应用环境

由于环境配置不同、版本不同等问题

可能会出现换了环境之后无法运行项目的问题

而环境配置很麻烦，如果没有Docker，每一台及其都需要部署环境

Docker：发布项目 (jar + 环境)

~~之前服务器被黑了之后重新配置环境真的痛苦，~~有了Docker就不会出现这样的问题

如果没有Docker，跨平台

Windows -> Linux 容易出现问题

有了docker：

java -> jar -> 镜像(带有环境的项目) -> Docker仓库 -> 拉取镜像即可直接运行

Docker思想：集装箱思想

核心思想：**隔离**

每个集装箱都是相互隔离的



### Docker的历史

2010年，几个年轻人再美国成立了一家公司dotCloud

做一些pass的云计算服务和LXC有关的容器技术

他们将容器化技术命名为Docker

刚诞生的时候没那么火，后来在Docker开源后，逐渐引起了一些开发者的注意，发现了docker的优点

2014.4.9，Docker1.0发布

docker和vm比起来十分轻巧

docker容器技术也是一种虚拟化技术 

> 聊聊docker

Docker是基于Go语言开发的开源项目

学习资料：

* [官网](https://www.docker.com/)
* [超级详细的官方文档](https://docs.docker.com/)
* [Docker Hub 仓库](https://hub.docker.com/) (类似Git)



### Docker能做啥

> 对比虚拟机技术

缺点：

* 资源占用多
* 冗余步骤多
* 启动慢



> 容器化技术

* 容器化技术不是模拟一个完整的操作系统

* 容器内的应用直接运行在 宿主机 的内核中，容器是没有自己的内核的，也没有硬件，十分轻便
* 每个容器间相互隔离，每个容器内部有一个属于自己的文件系统，互不影响



> DevOps(开发+运维)

**更快速的交付和部署**

传统：一堆帮助文档，安装程序

Docker：打包镜像发布测试，一键运行

**更简便的升级和扩缩容**

使用Docker后，我们部署应用就和搭积木一样

**更简单的系统运维**

容器化后，我们开发和测试环境高度一致



## Docker安装

### Docker的基本组成

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207191834622.jpeg" alt="Docker架构图"  />

**镜像(image)：**

docker镜像好比一个模板，开源通过这个模板来创建容器服务，tomcat镜像 \=\=\=> run ===> tomcat01容器（提供服务器），通过这个镜像开源创建多个容器

**容器(container)：**

Docker利用容器技术，独立运行一个或一个组应用，通过镜像来创建。

启动，停止，删除，基本命令

目前就可以把这个容器理解为一个建议的linux系统

**仓库(repository)：**

仓库就是存放镜像的地方

仓库分为公有仓库和私有仓库

Docker Hub是国外的，需要配置镜像加速



### 安装Docker

> 环境准备

1、需要一点linux基础

2、CentOs 7

3、使用SSH工具连接远程服务器

> 环境查看

~~~shell
# 系统内核 3.10以上
[root@Lear /]# uname -r
3.10.0-1062.18.1.el7.x86_64
~~~

~~~shell
# 系统系统版本
[root@Lear /]# cat /etc/os-release 
NAME="CentOS Linux"
VERSION="7 (Core)"
ID="centos"
ID_LIKE="rhel fedora"
VERSION_ID="7"
PRETTY_NAME="CentOS Linux 7 (Core)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:centos:centos:7"
HOME_URL="https://www.centos.org/"
BUG_REPORT_URL="https://bugs.centos.org/"

CENTOS_MANTISBT_PROJECT="CentOS-7"
CENTOS_MANTISBT_PROJECT_VERSION="7"
REDHAT_SUPPORT_PRODUCT="centos"
REDHAT_SUPPORT_PRODUCT_VERSION="7"
~~~

> 安装

帮助文档

~~~shell
# 1、卸载
yum remove docker \
		   docker-client \
		   docker-client-latest \
		   docker-common \
		   docker-latest \
		   docker-latest-logrotate \
		   docker-logrotate \
		   docker-engine

# 2、需要的安装包
yum install -y yum-utils

# 3、设置镜像仓库 (阿里云镜像)
yum-config-manager \
	--add-repo \
	http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 更新yum软件包索引
yum makecache fast

# 4、安装docker
yum install docker-ce docker-ce-cli containerd.io

# 5、启动docker
systemctl start docker
# 6、测试是否启动成功
docker version
~~~

~~~shell
# 7、hello-world
docker run hello-world
~~~

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207191938342.png" alt="image-20220719193840268" style="zoom:67%;" />

~~~shell
# 8、查看一下下载的这个 hello-world镜像
[root@Lear /]# docker images
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    feb5d9fea6a5   9 months ago   13.3kB
~~~



卸载docker

~~~shell
# 卸载依赖
yum remove docker-ce docker-ce-cli containerd.io
# 删除资源
rm -rf /var/lib/docker
~~~



> 默认工作路径

/var/lib/docker



### 阿里云镜像加速

#### 1、登录阿里云找到`容器镜像服务`



#### 2、找到镜像加速地址

![image-20220719194507243](https://gitee.com/sky-dog/note/raw/master/img/202207191945356.png)

#### 3、配置使用

~~~shell
sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://cikesdgr.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload

sudo systemctl restart docker
~~~



### 回顾hello-world流程

> run的运行流程图

![image-20220719195046922](https://gitee.com/sky-dog/note/raw/master/img/202207191950028.png)



### 底层原理

Docker是一个Client - Server 结构的系统，Docker的守护进程（服务）运行在宿主机上。通过Socket从客户端访问

DockerServer接受到Docker-Client的指令，就会执行这个命令

![image-20220719200703298](https://gitee.com/sky-dog/note/raw/master/img/202207192007376.png)

> Docker为什么比VM快

1、Docker有比虚拟机更少的抽象层

2、Docker 利用的是宿主机的内核，VM需要 Guest OS

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207192007681.png" alt="image-20220719200749619" style="zoom: 80%;" />

新建容器时，Docker不需要像VM一样重新加载一个操作系统

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207192010643.png" alt="image-20220719201006555" style="zoom:67%;" />





## Docker 常用命令

### 帮助命令

~~~shell
docker version		 # 显示docker版本信息
docker info			 # 显示docker的系统信息(包括镜像和容器信息)
docker 命令 --help	# 万能命令
~~~

帮助文档：[Docker run reference | Docker Documentation](https://docs.docker.com/engine/reference/run/)



### 镜像命令

**docker images**  查看所有本地主机上的镜像

~~~shell
[root@Lear /]# docker images
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    feb5d9fea6a5   9 months ago   13.3kB

# 名词解释
REPOSITORY	镜像的仓库源
TAG			镜像的标签
IAMGE ID	镜像的ID
CREATED		镜像的创建时间
SIZE		镜像的大小

# 可选参数
  -a, --all		# 列出所有镜像
  -q, --quite	# 只显示镜像的id
~~~



**docker search** 搜索镜像

~~~shell
[root@Lear /]# docker search mysql
NAME                           DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
mysql                          MySQL is a widely used, open-source relation…   12891     [OK]       
mariadb                        MariaDB Server is a high performing open sou…   4943      [OK]       
percona                        Percona Server is a fork of the MySQL relati…   582       [OK]       
phpmyadmin                     phpMyAdmin - A web interface for MySQL and M…   577       [OK]       
bitnami/mysql                  Bitnami MySQL Docker Image                      72                   [OK]

# 可选参数
  --filter=STARS=3000		# 搜索出STARS大于3000的镜像

~~~



**docker pull** 下载镜像

~~~shell
# 下载镜像 docker pull 镜像名[:tag]
[root@Lear /]# docker pull mysql
Using default tag: latest			# 不写tag默认使用latest
latest: Pulling from library/mysql
72a69066d2fe: Pull complete 		# 分层下载，docker image的核心 联合文件系统
93619dbc5b36: Pull complete 
99da31dd6142: Pull complete 
626033c43d70: Pull complete 
37d5d7efb64e: Pull complete 
ac563158d721: Pull complete 
d2ba16033dad: Pull complete 
688ba7d5c01a: Pull complete 
00e060b6d11d: Pull complete 
1c04857f594f: Pull complete 
4d7cfa90e6ea: Pull complete 
e0431212d27d: Pull complete 
Digest: sha256:e9027fe4d91c0153429607251656806cc784e914937271037f7738bd5b8e7709	# 签名信息
Status: Downloaded newer image for mysql:latest	
docker.io/library/mysql:latest	# 真实地址

# 拉取指定版本镜像
[root@Lear /]# docker pull mysql:5.7
5.7: Pulling from library/mysql
72a69066d2fe: Already exists 
93619dbc5b36: Already exists 
99da31dd6142: Already exists 
626033c43d70: Already exists 
37d5d7efb64e: Already exists 
ac563158d721: Already exists 
d2ba16033dad: Already exists 
0ceb82207cd7: Pull complete 
37f2405cae96: Pull complete 
e2482e017e53: Pull complete 
70deed891d42: Pull complete 
Digest: sha256:f2ad209efe9c67104167fc609cca6973c8422939491c9345270175a300419f94
Status: Downloaded newer image for mysql:5.7
docker.io/library/mysql:5.7
~~~



**docker rmi** 删除镜像

~~~shell
[root@Lear /]# docker rmi -f 容器id [容器id 容器id 容器id .....]		#删除指定镜像
[root@Lear /]# docker rmi -f $(docker images -aq)					 #删除所有镜像
~~~





### 容器命令

tips：有了镜像才可以创建容器

> 准备工作

下载个centos来测试学习

~~~shell
docker pull centos
~~~

新建容器并启动

~~~shell
docker run [可选参数] image

# 参数说明
--name="Name"		# 容器名字
-d					# 以后台方式运行
-it					# 使用交互方式运行(可进入容器查看)
-p					# 指定容器端口 -p 8080:8080
	-p ip:主机端口:容器端口
	-p 主机端口:容器端口	# 常用！
	-p 容器端口
	容器端口
-P					# 随机指定端口

# 测试	启动并进入容器
[root@Lear /]# docker run -it centos /bin/bash
[root@73efe585932d /]# ls		# 查看容器内的centos，很多版本，内部命令不完善
bin  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var

# 从容器中退回主机
[root@73efe585932d /]# exit
exit
[root@Lear /]# 
~~~

**列出所有运行中的容器**

~~~shell
# docker ps 命令
-a		# 列出当前正在运行的容器+历史运行过的容器
-n=?	# 显示最近创建的容器(显示个数)
-q		# 只显示容器id

[root@Lear /]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
[root@Lear /]# docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS                      PORTS     NAMES
73efe585932d   centos         "/bin/bash"   2 minutes ago   Exited (0) 45 seconds ago             magical_neumann
963913bef5b5   feb5d9fea6a5   "/hello"      20 hours ago    Exited (0) 20 hours ago               vigorous_elbakyan

~~~

**退出容器**

~~~shell
exit	# 停止容器并推出
ctrl + P + Q	# 退出但容器不停止运行
~~~

**删除容器**

~~~shell
docker rm 容器id				   # 删除指定容器
docker rm -f $(docker ps -aq)	# 删除所有容器
docker ps -a -q|xargs docker rm	# 删除所有的容器
~~~



**启动和停止容器**

~~~shell
docker start 容器id		# 启动容器
docker restart 容器id		# 重启容器
docker stop 容器id		# 停止当前正在运行的容器
docker kill 容器id		# 强制停止当前容器
~~~



### 常用其他命令

**后台启动容器**

~~~shell
# 命令 docker run -d 容器名

# 问题:docker ps 发现容器已经停止了

# 常见的坑:docker发现没有应用,就会自动停止
# nginx: 容器启动后，发现自己没有提供服务，就会立刻停止
~~~

**查看日志**

~~~shell
docker logs -f -t --tail 显示行数 容器id
~~~

**查看容器进程信息**

~~~shell
docker top 容器id
~~~

**查看镜像元数据**

~~~shell
docker inspect 容器id
~~~



**进入正在运行的容器！**

~~~shell
docker exec -it 容器id /bin/bash
进入容器后开启一个新的终端

docker attach 容器id /bin/bash
进入正在执行的命令行
~~~

**从容器拷贝文件**

~~~shell
docker cp 容器id:容器内路径 目的主机路径
~~~



### 小结

![image-20220720162815150](https://gitee.com/sky-dog/note/raw/master/img/202207201628318.png)



### 练习

> 部署nginx

~~~shell

# 1、搜索nginx镜像
docker search nginx
# 或去dockerhub上搜索(建议)
# 2、拉去nginx镜像
docker pull nginx
docker images	# 查看镜像
# 3、启动容器
docker run -d --name nginx01 -p 8000:80		# 后台启动、容器命名为nginx01、外网访问8000映射到容器内网80

~~~

端口映射图解

<img src="C:/Users/%E5%A4%A9%E7%8B%97/AppData/Roaming/Typora/typora-user-images/image-20220723161641406.png" alt="image-20220723161641406" style="zoom: 50%;" />



思考：

如何在宿主机修改容器内部配置、启动应用等？





## Docker镜像详解

### 镜像是什么

镜像是一种轻量级、可执行的独立软件包，用来打包软件运行环境和基于运行环境开发的软件，它包含运行某个软件所需的所有内容，包括代码、运行时、库、环境变量、配置文件。

所有的应用直接打包为镜像，即可直接跑起来

如何获取镜像：

* 远程仓库拉取
* 朋友拷贝来
* 自己制作DockerFile



### Docker镜像加载原理

> UnionFS(联合文件系统)

联合文件系统（UnionFS）是一种分层、轻量级并且高性能的文件系统，它支持对文件系统的修改作为一次提交来一层层的叠加，同时可以将不同目录挂载到同一个虚拟文件系统下。联合文件系统是 Docker 镜像的基础。镜像可以通过分层来进行继承，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像。
特性：一次同时加载多个文件系统，但从外面看起来只能看到一个文件系统。联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录。

![image-20220723172606514](https://gitee.com/sky-dog/note/raw/master/img/202207231747751.png)

> 镜像加载原理

Docker的镜像实际由一层一层的文件系统组成：

bootfs（boot file system）主要包含bootloader和kernel。bootloader主要是引导加载kernel，完成后整个内核就都在内存中了。此时内存的使用权已由bootfs转交给内核，系统卸载bootfs。可以被不同的Linux发行版公用。
rootfs（root file system），包含典型Linux系统中的/dev，/proc，/bin，/etc等标准目录和文件。rootfs就是各种不同操作系统发行版（Ubuntu，Centos等）。因为底层直接用Host的kernel，rootfs只包含最基本的命令，工具和程序就可以了。

### 分层理解

所有的Docker镜像都起始于一个基础镜像层，当进行修改或增加新的内容时，就会在当前镜像层之上，创建新的容器层。
容器在启动时会在镜像最外层上建立一层可读写的容器层（R/W），而镜像层是只读的（R/O）。

![image-20220723174705516](https://gitee.com/sky-dog/note/raw/master/img/202207231747588.png)

![image-20220723173708823](https://gitee.com/sky-dog/note/raw/master/img/202207231737928.png)



### commit镜像

~~~shell
docker commit 	# 提交容器为一个新的副本
# 和git类似
docker commit -m="提交的描述信息" -a="作者" 容器id 目标镜像名:[TAG]
~~~

可以将自己diy的容器打包为一个镜像





### 练习

在本地制作一个centos+jdk镜像

~~(自己做个jdk17的镜像用来开我的世界服务器)~~







## 容器数据卷

### 什么是容器数据卷

如果数据都在容器中，删除容器 -> 数据丢失

==需求:数据持久化==

容器数据卷：即一种容器间数据共享技术

简单来说，就是将容器内的目录挂载在宿主机上

也就是容器的持久化和同步操作，容器间也可以数据共享

<img src="C:/Users/%E5%A4%A9%E7%8B%97/AppData/Roaming/Typora/typora-user-images/image-20220724124255538.png" alt="image-20220724124255538" style="zoom:50%;" />



### 使用数据卷

> 直接使用命令挂载		-v

~~~shell
docker run -it -v 主机目录:容器内目录

# 测试
docker run -it -v /home/ceshi:/home centos /bin/bash
# 查看是否挂载成功
docker inspect 容器id
~~~



### 具名挂载/匿名挂载

~~~shell
# 匿名挂载
-v 容器内路径
docker run -d -P --name nginx01 -v /etc/nginx nginx

# 查看所有的 volume 情况
docker volume ls

# 具名挂载
docker run -d -P --name nginx02 -v juming-nginx:/etc/nginx nginx

~~~

所有的docker容器内的卷，在没有指定目录的情况下都是在`/var/lib/docker/volumes/xxxx/_data`

通过具名挂载可以方便的找到一个卷，大多数使用**具名挂载**

~~~shell
# 如何确定是具名挂载还是匿名挂载，还是指定路径挂载

~~~







### 练习：Mysql同步数据

~~~shell
# 获取镜像
docker pull mysql:5.7

# 运行容器(需要数据挂载)
# 启动mysql需要配置密码！
# 官方测试: docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag

# 启动参数
-d 后台运行
-p 端口映射
-v 卷挂载
-e 环境配置
--name 容器名
docker run -d -p 3310:3306 -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name mysql01 mysql:5.7
~~~













## DockerFIle

### DockerFile介绍

DockerFIle 就是构建 docker 镜像的构建文件！本质是一个命令脚本，用于生成镜像。



构建步骤:

* 编写一个 dockerfile 文件
* docker build 构建成为一个镜像
* docker run 运行镜像
* docker push 发布镜像 (DockerHub、阿里云镜像仓库)



查看centos官方镜像学习

<img src="C:/Users/%E5%A4%A9%E7%8B%97/AppData/Roaming/Typora/typora-user-images/image-20220728160841386.png" alt="image-20220728160841386" style="zoom:50%;" />

dockerhub上的镜像90%都源自 scratch

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207281609703.png" alt="image-20220728160909567" style="zoom:50%;" />

官方镜像都是基础包，缺少很多功能，我们通常构建自己的镜像使用

### DockerFile构建过程

**基础知识:**

1、每个保留关键字(指令)都必须是大写字母

2、从上到下顺序执行

3、`#`表示注释

4、每一个指令都会创建提交一个新的镜像层并提交

<img src="C:/Users/%E5%A4%A9%E7%8B%97/AppData/Roaming/Typora/typora-user-images/image-20220728161639633.png" alt="image-20220728161639633" style="zoom:67%;" />

dockerfile是面向开发的，以后要发布项目，做镜像，就需要编写dockerfile

docker镜像逐渐成为企业交付的标准，必须要掌握

步骤：开发、部署、上线、运维。。。

DockerFile : 构建文件，定义了一切的步骤，源代码

DockerImages : 通过 DockerFile 构建生成的镜像 (最终要发布和运行的产品)

Docker容器 : 容器就是镜像运行起来提供服务的



### DockerFile指令

~~~shell
FROM			# 基础镜像 (一切从这里开始构建) (一般用centos)
MAINTAINER		# 镜像是谁写的 (姓名+邮箱)
RUN				# 镜像构建时需要运行的命令
ADD				# 步骤：添加内容(比如说jdk压缩包)
WORKDIR			# 镜像工作目录
VOLUME			# 挂载目录
EXPOSE			# 暴露端口
CMD				# 指定容器启动时运行的命令 (只有最后一个会生效，可被替代)
ENTRYPOINT		# 指定容器启动时运行的命令 (可以追加命令)
ONBUILD			# 当构建一个被继承 DockerFile 这个时候会执行 ONBUILD 的命令
COPY			# 类似ADD命令 将文件拷贝到镜像中
ENV				# 构建时设置环境变量

~~~

![image-20220728162227803](https://gitee.com/sky-dog/note/raw/master/img/202207281622971.png)



### 实战测试

> 自己的centos

~~~shell
# 1、编写配置文件

FROM centos
MAINTAINER lear<362664609@qq.com>

ENV MYPATH /usr/local
WORKDIR $MYPATH

RUN yum -y install vim
RUN yum -y install net-tools

EXPOSE 80

CMD echo $MYPATH
CMD echo "---build end---"
CMD /bin/bash


# 2、通过dockerfile文件构建镜像
# 命令 docker build -f dockerfile文件路径 -t 镜像名:[tag] .

~~~












### 使用DockerFile 挂载目录

~~~dockerfile
# 创建一个dockerfile文件	名字可以随机(建议dockerfile)
# 注意:文件中指令均为大写
FROM centos

VOLUME ["volume01","volume02"]		# 匿名挂载

CMD echo "---end---"
CMD /bin/bash
~~~

~~~shell
docker build -f dockerfile1 -t myCentos .
~~~



![image-20220727190103012](https://gitee.com/sky-dog/note/raw/master/img/202207271901219.png)



~~~shell
docker run -it --name docker02 --volume-from docker01 kuangshen/centos:1.0
# --volume-from 用于继承另一个容器的挂载属性
# 即将目录挂载到宿主机的同一个路径下
~~~



### 发布自己的镜像

> [Docker Hub](https://hub.docker.com/)

0. 注册

1. 登录

2. 在服务器上提交镜像

   ~~~shell
   [root@Lear ~]# docker login --help
   
   Usage:  docker login [OPTIONS] [SERVER]
   
   Log in to a Docker registry.
   If no server is specified, the default is defined by the daemon.
   
   Options:
     -p, --password string   Password
         --password-stdin    Take the password from stdin
     -u, --username string   Username
   ~~~

   

3. 登录后, docker push

![image-20220731111307307](https://gitee.com/sky-dog/note/raw/master/img/202207311113440.png)

分层push提交



> 阿里云镜像提交

1. 登录阿里云

2. 容器镜像服务

3. 创建命名空间

   ![image-20220731112211413](https://gitee.com/sky-dog/note/raw/master/img/202207311122494.png)

4. 创建容器镜像

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207311123000.png" alt="image-20220731112302887" style="zoom:50%;" />

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207311124072.png" alt="image-20220731112405998" style="zoom:50%;" />

5. 阿里云官方文档说明

   ![image-20220731112805083](https://gitee.com/sky-dog/note/raw/master/img/202207311128214.png)

### 小结



<img src="https://gitee.com/sky-dog/note/raw/master/img/202207311132570.png" alt="image-20220731113115201" style="zoom: 50%;" />



## Docker 网络

### 理解 Docker0

![image-20220731115345610](https://gitee.com/sky-dog/note/raw/master/img/202207311153670.png)



~~~shell
# 运行tomcat容器测试
docker run -d -P --name tomcat01 tomcat

# 先给容器安装网络相关包
apt update && apt install -y iproute2
apt-get install inetutils-ping
# 再使用ip addr查看容器内网络信息
[root@Lear /]# docker exec -it tomcat01 ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
10: eth0@if11: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever

# 使用宿主机测试ping容器
[root@Lear /]# ping 172.17.0.2
PING 172.17.0.2 (172.17.0.2) 56(84) bytes of data.
64 bytes from 172.17.0.2: icmp_seq=1 ttl=64 time=0.058 ms
64 bytes from 172.17.0.2: icmp_seq=2 ttl=64 time=0.071 ms
64 bytes from 172.17.0.2: icmp_seq=3 ttl=64 time=0.074 ms

# 通过linux服务器可以 ping 通容器内部
~~~

> 原理

1、我们每启动一个docker容器，docker就会给docker容器分配一个ip，我们只要安装了docker，就会有一个网卡 docker0

桥接模式，使用技术：evth-pair



在宿主机中再次使用 ip addr 查看，发现多了一对网卡 (11: veth3908f46@if10)

![image-20220731132551565](https://gitee.com/sky-dog/note/raw/master/img/202207311325642.png)

~~~shell
# 发现容器带来的网卡是一对一对的
# evth-pair 就是一对虚拟设备接口，成对出现，一端连着协议，一端彼此相连
# 正因为这个特性，将 evth-pair 作为桥梁，连接各种虚拟网络设备
# OpenStac , Docker 容器间连接 , OVS连接 , 都是使用 evth-pair 技术

容器间可以相互ping通

~~~



<img src="https://gitee.com/sky-dog/note/raw/master/img/202207311505001.png" alt="image-20220731150512889" style="zoom:50%;" />

<img src="https://gitee.com/sky-dog/note/raw/master/img/202207311510365.png" alt="image-20220731151039267" style="zoom:50%;" />



Docker 使用Linux的桥接，宿主机中是一个Docker容器的网桥，即Docker0

Docker中所有的网络接口都是虚拟的。虚拟的转发效率高！

只要停止或删除容器，对应 veth-pair 就没了

<img src="C:/Users/%E5%A4%A9%E7%8B%97/AppData/Roaming/Typora/typora-user-images/image-20220731214642516.png" alt="image-20220731214642516" style="zoom: 67%;" />



### --link

每次启动容器 ip 地址都会发生变化

如何通过容器名来访问容器网络

~~~shell
# 无法通过容器名直接访问
[root@Lear rollup]# docker exec -it tomcat02 ping tomcat01
ping: unknown host
~~~

~~~shell
# 测试 --link
[root@Lear rollup]# docker run -d -P --name tomcat03 --link tomcat02 tomcat

# 测试ping
# 正向可以ping通
[root@Lear rollup]# docker exec -it tomcat03 ping tomcat02
PING tomcat02 (172.17.0.3) 56(84) bytes of data.
64 bytes from tomcat02 (172.17.0.3): icmp_seq=1 ttl=64 time=0.123 ms
64 bytes from tomcat02 (172.17.0.3): icmp_seq=2 ttl=64 time=0.093 ms
64 bytes from tomcat02 (172.17.0.3): icmp_seq=3 ttl=64 time=0.094 ms
# 反向无法ping通
[root@Lear rollup]# docker exec -it tomcat02 ping tomcat03
ping: unknown host
~~~

~~~shell
# 查看网络
[root@Lear rollup]# docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
d4469cc409f7   bridge    bridge    local
e5a5ec26c3f0   host      host      local
02d693215add   none      null      local
[root@Lear rollup]# docker network inspect d4469cc409f7
~~~

![image-20220731221556159](https://gitee.com/sky-dog/note/raw/master/img/202207312215266.png)

原理

使用 --link 时，在hosts文件中配置了域名解析，直接将tomcat02解析为对应的ip地址

能够使用 容器名 或 容器id，来访问容器网络

~~~shell
[root@Lear rollup]# docker exec -it tomcat03 cat /etc/hosts
127.0.0.1	localhost
::1	localhost ip6-localhost ip6-loopback
fe00::0	ip6-localnet
ff00::0	ip6-mcastprefix
ff02::1	ip6-allnodes
ff02::2	ip6-allrouters
172.17.0.3	tomcat02 cf77a4d0a845
172.17.0.4	b41942f2cf97
~~~



现在玩 Docker 已经不建议使用 --link 了

docker0 问题 : 无法使用容器名访问

我们可以使用自定义网络



### 自定义网络

> 查看docker所有网络

![image-20220801142024347](https://gitee.com/sky-dog/note/raw/master/img/202208011420461.png)

**网络模式**

bridge : 桥接 docker0 (默认)

none : 不配置网络

host : 与宿主机共享网络

container : 容器网络联通 (少用，局限性大)



**测试**

~~~shell
# 我们直接启动的命令 --net bridge 就是docker0
docker run -d -P --name tomcat01 [-net bridge] tomcat

# docker0 特点: 域名不可访问(只能使用--link)

# 自定义一个网络
# --driver bridge
# -subnet 192.168.0.0/16		网络支持范围: 192.168.0.2 ~ 192.168.255.255
# --gateway 192.168.0.1			路由网关
[root@Lear /]# docker network create --driver bridge --subnet 192.168.0.0/16 --gateway 192.168.0.1 mynet
d7f0ea06cca6a0e19a9e51344deb7c51a66553703be47686d65c346d35ef19f3
[root@Lear /]# docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
d4469cc409f7   bridge    bridge    local
e5a5ec26c3f0   host      host      local
d7f0ea06cca6   mynet     bridge    local
02d693215add   none      null      local
~~~

<img src="C:/Users/%E5%A4%A9%E7%8B%97/AppData/Roaming/Typora/typora-user-images/image-20220803141230732.png" alt="image-20220803141230732" style="zoom:80%;" />

~~~shell
# 启动容器测试
9e512bd57a2851d815f8fadee0e0ce197505e397621e3b6c4cc044da6d151868
[root@Lear /]# docker run -d -P --name tomcat-net-02 --net mynet tomcat
709ca79a1764e35cdcbbf7226d7b968ae93b96825785c87fbd4896bfb7598dc8
[root@Lear /]# docker network inspect mynet
[
    {
        "Name": "mynet",
        "Id": "d7f0ea06cca6a0e19a9e51344deb7c51a66553703be47686d65c346d35ef19f3",
        "Created": "2022-08-03T14:09:33.135986794+08:00",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": {},
            "Config": [
                {
                    "Subnet": "192.168.0.0/16",
                    "Gateway": "192.168.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "709ca79a1764e35cdcbbf7226d7b968ae93b96825785c87fbd4896bfb7598dc8": {
                "Name": "tomcat-net-02",
                "EndpointID": "0d2cf16c67b4104cd6c874602577b7abde9cfec21099632d512909798d0f5a02",
                "MacAddress": "02:42:c0:a8:00:03",
                "IPv4Address": "192.168.0.3/16",
                "IPv6Address": ""
            },
            "9e512bd57a2851d815f8fadee0e0ce197505e397621e3b6c4cc044da6d151868": {
                "Name": "tomcat-net-01",
                "EndpointID": "16a8e57b686989075a06588409571335c2763b887b4712897a83cca6cdf917ca",
                "MacAddress": "02:42:c0:a8:00:02",
                "IPv4Address": "192.168.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {},
        "Labels": {}
    }
]
~~~

~~~shell
# 测试ping通
[root@Lear /]# docker exec -it tomcat-net-01 ping 192.168.03
PING 192.168.03 (192.168.0.3) 56(84) bytes of data.
64 bytes from 192.168.0.3: icmp_seq=1 ttl=64 time=0.122 ms
64 bytes from 192.168.0.3: icmp_seq=2 ttl=64 time=0.097 ms
64 bytes from 192.168.0.3: icmp_seq=3 ttl=64 time=0.090 ms
^C
--- 192.168.03 ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2000ms
rtt min/avg/max/mdev = 0.090/0.103/0.122/0.013 ms
[root@Lear /]# docker exec -it tomcat-net-01 ping tomcat-net-02
PING tomcat-net-02 (192.168.0.3) 56(84) bytes of data.
64 bytes from tomcat-net-02.mynet (192.168.0.3): icmp_seq=1 ttl=64 time=0.075 ms
64 bytes from tomcat-net-02.mynet (192.168.0.3): icmp_seq=2 ttl=64 time=0.098 ms
64 bytes from tomcat-net-02.mynet (192.168.0.3): icmp_seq=3 ttl=64 time=0.064 ms
^C
--- tomcat-net-02 ping statistics ---
~~~

**推荐这么使用网络**

自定义的网络docker已经帮我们维护好了对应关系



好处：

* redis - 不同集群使用不同网络，保证集群安全和健康
* mysql- 不同集群使用不同网络，保证集群安全和健康

![image-20220803181719805](https://gitee.com/sky-dog/note/raw/master/img/202208031817064.png)

### 网络连通

![image-20220803182135746](https://gitee.com/sky-dog/note/raw/master/img/202208031821854.png)

![image-20220809180730300](https://gitee.com/sky-dog/note/raw/master/img/202208091807498.png)

~~~shell
docker network connect mynet tomcat01
# 直接将tomcat的网络配置进mynet了
# 一个容器 两个ip
~~~



### 实战

> redis 集群

![image-20220809190050918](https://gitee.com/sky-dog/note/raw/master/img/202208091900106.png)



~~~shell
# 创建网卡
docker network create redis --subnet 172.38.0.0/16

# 通过脚本创建六个redis配置
for port in $(seq 1 6); \
do \
mkdir -p /mydata/redis/node-${port}/conf
touch /mydata/redis/node-${port}/conf/redisonf
cat << EOF >/mydata/redis/node-${port}/conf/redisconf
port 6379
bind 0.0.0.0
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip 172.38.0.1${port}
cluster-announce-port 6379
cluster-announce-bus-port 16379
appendonly yes
EOF
done

# 启动六个redis
for port in $(seq 1 6); \
do \
docker run -p 637${port}:6379 -p 1637${port}:16379 --name redis-${port} \
-v /mydata/redis/node-${port}/data:/data \
-v /mydata/redis/node-${port}/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.38.0.1${port} redis:5.0.9-alpine3.11 redis-server /etc/redis/redis.conf; \
done

# 例:启动一个redis
docker run -p 6371:6379 -p 16371:16379 --name redis-1 \
-v /mydata/redis/node-1/data:/data \
-v /mydata/redis/node-1/conf/redis.conf:/etc/redis/redis.conf \
-d --net redis --ip 172.38.0.11 redis:5.0.9-alpine3.11 redis-server /etc/redis/redis.conf; \

# 进入其中一个容器
# ps:redis镜像中没有/bin/bash
docker exec -it redis-1 /bin/sh

# 创建集群
redis-cli --cluster create 172.38.0.11:6379 172.38.0.12:6379 172.38.0.13:6379 172.38.0.14:6379 172.38.0.15:6379 172.38.0.16:6379 --cluster-replicas 1

# 测试集群
redis-cli -c 
cluster info
~~~



> SpringBoot 微服务打包镜像

1. 构建SpringBoot项目
2. 打包应用
3. 编写dockerfile
4. 构建镜像
5. 发布运行



~~~dockerfile
FROM java:8

COPY *.jar /app.jar

CMD ["--server.port=9091"]

EXPOSE 9091

ENTRYPOINT ["java","-jar",]

~~~





















## 进阶

### Docker Compose



#### 简介

(自动化脚本)

DockerFile build run 手动操作单个容器

如果多个微服务相互依赖就会很麻烦

DOcker Compose 可以高效的管理、定义多个容器



> 官方介绍

* 定义、运行多个容器
* YAML 配置文件
* single commond. 命令有哪些

Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services. Then, with a single command, you create and start all the services from your configuration. To learn more about all the features of Compose, see [the list of features](https://docs.docker.com/compose/#features).

所有环境都能够使用 compose

Compose works in all environments: production, staging, development, testing, as well as CI workflows. You can learn more about each case in [Common Use Cases](https://docs.docker.com/compose/#common-use-cases).

**三步骤:**

Using Compose is basically a three-step process:

1. Define your app’s environment with a `Dockerfile` so it can be reproduced anywhere.
   * DockerFile 保证项目在任何环境可以运行
2. Define the services that make up your app in `docker-compose.yml` so they can be run together in an isolated environment.
   * services 是什么
   * `docker-compose.yml 怎么写
3. Run `docker compose up` and the [Docker compose command](https://docs.docker.com/compose/#compose-v2-and-the-new-docker-compose-command) starts and runs your entire app. You can alternatively run `docker-compose up` using Compose standalone(`docker-compose` binary).
   * 启动项目



作用 : 批量容器编排



> 理解

Compose 是 Docker 官方的开源项目，需要独立安装

`Dockerfile` 让程序在任何地方运行

Yaml文件 例:

~~~yaml
version: "3.9"  # optional since v1.27.0
services:
  web:
    build: .
    ports:
      - "8000:5000"
    volumes:
      - .:/code
      - logvolume01:/var/log
    depends_on:
      - redis
  redis:
    image: redis
volumes:
  logvolume01: {}
~~~

Compose : 重要概念

* 服务 services , 容器/应用 (web、redis、mysql ...)
* 项目 project , 一组关联的容器



#### 安装

~~~shell
# 第一步 下载
# 官方地址 (可能很慢)
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 这个可能快点
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.5/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

# 第二步 授权
sudo chmod +x /usr/local/bin/docker-compose

~~~

![安装成功](https://gitee.com/sky-dog/note/raw/master/img/202208111726995.png)

#### 体验测试





















### Docker Swam

(配置集群)















### CI/CD jenkins





### Docker Stack





### Docker Secret







### Docker Config







### k8s























































































































