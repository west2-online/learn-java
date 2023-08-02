# RocketMQ简单入门

本文若有不当之处欢迎提出pr/issue

主要内容：

1. **[初识MQ](#1.初识MQ)**

2. **[RocketMQ简介](#2.RocketMQ简介)**

3. **[RocketMQ安装](#3.RocketMQ安装)**

4. **[RocketMQ快速入门](#4.RocketMQ快速入门)**

5. **[SpringBoot集成RocketMQ](#5.SpringBoot集成RocketMQ)**

6. **[最后](#6.最后)**



## 1.初识MQ

### 1.1.同步和异步通讯

微服务间通讯有同步和异步两种方式：

同步通讯：就像打电话，需要实时响应。

异步通讯：就像发邮件，不需要马上回复。

两种方式各有优劣，打电话可以立即得到响应，但是你却不能跟多个人同时通话。发送邮件可以同时与多个人收发邮件，但是往往响应会有延迟。



#### 1.1.1.同步通讯

Feign调用就属于同步方式，虽然调用可以实时得到结果，但存在下面的问题：

![image-20210717162004285](F:/java学习/1、微服务开发框架SpringCloud+RabbitMQ+Docker+Redis+搜索+分布式微服务全技术栈课程/实用篇/学习资料/day04-MQ/讲义/assets/image-20210717162004285.png)



**总结：**

同步调用的优点：

- 时效性较强，可以立即得到结果

同步调用的问题：

- 耦合度高
- 性能和吞吐能力下降
- 有额外的资源消耗
- 有级联失败问题（由于一个故障导致了连锁反应，使得系统中的其他组件或节点也相继失败）



#### 1.1.2.异步通讯

异步调用则可以避免上述问题：



我们以购买商品为例，用户支付后需要调用订单服务完成订单状态修改，调用物流服务，从仓库分配响应的库存并准备发货。

在事件模式中，支付服务是事件发布者（publisher），在支付完成后只需要发布一个支付成功的事件（event），事件中带上订单id。

订单服务和物流服务是事件订阅者（Consumer），订阅支付成功的事件，监听到事件后完成自己业务即可。



为了解除事件发布者与订阅者之间的耦合，两者并不是直接通信，而是有一个中间人（Broker）。发布者发布事件到Broker，不关心谁来订阅事件。订阅者从Broker订阅事件，不关心谁发来的消息。

![image-20230801111039523](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011110579.png)

Broker 是一个像数据总线一样的东西，所有的服务要接收数据和发送数据都发到这个总线上，这个总线就像协议一样，让服务间的通讯变得标准和可控。



好处：

- 吞吐量提升：无需等待订阅者处理完成，响应更快速

- 故障隔离：服务没有直接调用，不存在级联失败问题
- 调用间没有阻塞，不会造成无效的资源占用
- 耦合度极低，每个服务都可以灵活插拔，可替换
- 流量削峰：不管发布事件的流量波动多大，都由Broker接收，订阅者可以按照自己的速度去处理事件



缺点：

- 架构复杂了，业务没有明显的流程线，不好管理
- 需要依赖于Broker的可靠、安全、性能

好在现在开源软件或云平台上 Broker 的软件是非常成熟的，比较常见的一种就是我们今天要学习的MQ技术。



### 1.2.技术对比：

MQ，中文是消息队列（MessageQueue），字面来看就是存放消息的队列。也就是事件驱动架构中的Broker。

几种常见MQ的对比：

|            | **RabbitMQ**            | **ActiveMQ**                   | **RocketMQ** | **Kafka**  |
| ---------- | ----------------------- | ------------------------------ | ------------ | ---------- |
| 公司/社区  | Rabbit                  | Apache                         | 阿里         | Apache     |
| 开发语言   | Erlang                  | Java                           | Java         | Scala&Java |
| 协议支持   | AMQP，XMPP，SMTP，STOMP | OpenWire,STOMP，REST,XMPP,AMQP | 自定义协议   | 自定义协议 |
| 可用性     | 高                      | 一般                           | 高           | 高         |
| 单机吞吐量 | 一般                    | 差                             | 高           | 非常高     |
| 消息延迟   | 微秒级                  | 毫秒级                         | 毫秒级       | 毫秒以内   |
| 消息可靠性 | 高                      | 一般                           | 高           | 一般       |

追求可用性：Kafka、 RocketMQ 、RabbitMQ

追求可靠性：RabbitMQ、RocketMQ

追求吞吐能力：RocketMQ、Kafka

追求消息低延迟：RabbitMQ、Kafka

不同的消息队列系统在不同场景下有各自的优势和适用性。以下是各个消息队列系统在不同场合下的最佳选择：

1. Kafka：
   - 最佳场合：大规模数据处理、实时日志收集和分析、流式处理。
   - 优势：高吞吐量、低延迟、水平扩展能力强、长期消息存储，适合构建大规模的实时数据流处理平台，如实时日志收集和分析、事件流处理等。
2. RabbitMQ：
   - 最佳场合：传统的企业级应用、轻量级的消息传递场景。
   - 优势：简单易用、支持多种消息协议、适合点对点和发布/订阅模式，对于传统的企业应用和中小规模的消息传递需求，是一种可靠的选择。
3. ActiveMQ：
   - 最佳场合：中小规模的企业应用、Java生态系统中的集成需求。
   - 优势：Java开发环境友好、支持多种消息协议，适合与Java生态系统的其他组件集成，如Spring框架等。
4. RocketMQ：
   - 最佳场合：大规模的分布式系统、互联网应用、金融领域的消息处理。
   - 优势：高吞吐量、低延迟、丰富的消息存储模式，适用于处理大规模的消息传递场景，特别是在互联网和金融领域。

综合考虑以上因素，可以做如下简单**总结**：

- 如果需要处理大规模的实时数据流、日志收集和分析等高吞吐量场景，首选`Kafka`。
- 如果对于消息传递的简单性和易用性有较高要求，适合中小规模的企业应用和轻量级消息传递需求，可以选择`RabbitMQ`或`ActiveMQ`。
- 如果在大规模的分布式系统、互联网应用或金融领域需要处理消息传递，`RocketMQ`是一个较好的选择。

## 2.RocketMQ简介

官网： http://rocketmq.apache.org/

RocketMQ是阿里巴巴2016年**MQ中间件**，使用Java语言开发，RocketMQ 是一款开源的**分布式消息系统**，基于高可用分布式集群技术，提供低延时的、高可靠的消息发布与订阅服务。同时，广泛应用于多个领域，包括异步通信解耦、企业解决方案、金融支付、电信、电子商务、快递物流、广告营销、社交、即时通信、移动应用、手游、视频、物联网、车联网等。

RocketMQ的设计目标是支持大规模消息处理，具有高并发、高可用和容错能力。它在多个方面提供了强大的功能和特性：

1. 分布式架构：RocketMQ采用分布式架构，支持在多个节点之间进行消息的发送和接收，实现了水平扩展能力。
2. 高吞吐量：RocketMQ可以在大规模并发场景下实现高吞吐量的消息处理，适用于高并发的业务场景。
3. 低延迟：RocketMQ具有较低的消息传递延迟，适用于需要实时性的应用场景。
4. 消息可靠性：RocketMQ提供了多种消息存储模式，可以确保消息的可靠传递，包括同步刷盘和异步刷盘等方式。
5. 消息顺序性：RocketMQ支持消息的顺序传递，可以确保同一消息队列中的消息按照发送顺序被消费。
6. 支持多种消息模式：RocketMQ支持发布/订阅模式和点对点模式，可以根据业务需求选择合适的消息模式。
7. 灵活的部署方式：RocketMQ支持多种部署方式，可以在单机上运行，也可以搭建集群部署。
8. 丰富的监控和管理工具：RocketMQ提供了丰富的监控和管理工具，方便管理员对消息队列进行监控和管理。



### 核心概念

**Producer**：消息的发送者，生产者；举例：发件人。

**Consumer**：消息接收者，消费者；举例：收件人。

**Broker**：消息队列的中间服务器，负责存储消息并将消息传递给消费者；举例：快递。

**NameServer**：可以理解为是一个注册中心，主要是用来保存topic路由信息，管理Broker。在NameServer的集群中，NameServer与NameServer之间是没有任何通信的；举例：各个快递公司的管理机构相当于broker的注册中心，保留了broker的信息。

**Queue**：队列，消息存放的位置，一个Broker中可以有多个队列。

**Topic**：消息的逻辑分类，生产者发送消息到指定的Topic，消费者从指定的Topic订阅消息。一个Topic可以有多个Producer和多个Consumer。

**ProducerGroup**：生产者组 。

**ConsumerGroup**：消费者组，多个消费者组可以同时消费一个主题的消息。



### 工作流程

该部分转载自 [掘金文章]([RocketMQ保姆级教程 - 掘金 (juejin.cn)](https://juejin.cn/post/7134227366481494046#heading-4))

![img](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308012236257.webp)

通过这张图就可以很清楚的知道，RocketMQ大致的工作流程：

* `Broker`启动的时候，会往每台NameServer（因为NameServer之间不通信，所以每台都得注册）注册自己的信息，这些信息包括自己的ip和端口号，自己这台Broker有哪些topic等信息。

* `Producer`在启动之后会跟会NameServer建立连接，定期从NameServer中获取Broker的信息，当发送消息的时候，会根据消息需要发送到哪个topic去找对应的Broker地址，如果有的话，就向这台Broker发送请求；没有找到的话，就看根据是否允许自动创建topic来决定是否发送消息。

* `Broker`在接收到Producer的消息之后，会将消息存起来，持久化，如果有从节点的话，也会主动同步给从节点，实现数据的备份

* `Consumer`启动之后也会跟会NameServer建立连接，定期从NameServer中获取Broker和对应topic的信息，然后根据自己需要订阅的topic信息找到对应的Broker的地址，然后跟Broker建立连接，获取消息，进行消费





## 3.RocketMQ安装

本文档所涉及的是单机版的RocketMQ安装教程，能够满足基本的学习使用，属于入门级的教程，如果想要搭集群部署，可以参考其他资料，进行配置即可

#### 3.1.Windos下的安装

>**所需环境**
>
>Windows 64位系统
>JDK1.8(64位)
>Maven



进入[RocketMQ官网下载]([下载 | RocketMQ (apache.org)](https://rocketmq.apache.org/download/))

##### 1、选择**Binary 下载**

![image-20230801144754675](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011447766.png)

##### 2、将压缩包解压至自定路径

![image-20230801145153332](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011451403.png)

##### 3、配置系统中的环境变量

> 变量名：ROCKETMQ_HOME
>
> 变量值：（如图浏览目录选择指定bin-release文件夹路径）

![image-20230801145430167](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011454227.png)



##### 4.启动RocketMQ

在自己安装的RocketMQ的bin目录下执行cmd命令，输入下面命令，启动NameServer

> start mqnamesrv.cmd

![image-20230801150123283](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011501372.png)

若出现如上图所示的命令框，说明启动成功，**保留窗口切勿关闭**

**继续启动broker**

与上述同样的路径下呼出cmd，执行如下命令：

> start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable = true

![image-20230801150447169](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011504276.png)

**看到上述命令框弹出即完成对RocketMQ的启动。**

==注意：==

RocketMQ默认的虚拟机内存较大，启动如果因为内存不足报错可执行以下步骤：

用记事本打开bin目录下的 `runbroker.cmd`

![image-20230801152309835](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011523942.png)

> 1. `-Xms2g`：设置JVM初始堆内存大小为2GB。
> 2. `-Xmx2g`：设置JVM最大堆内存大小为2GB。
>
> 可修改为 -Xms256m -Xmx256m -Xmn128m



同理打开`runserver.cmd`

![image-20230801152644369](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011526463.png)

> 修改jvm参数为
>
> -Xms256m -Xmx256m -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m



##### 5.配置可视化页面

下载可视化插件源码

github下载地址：https://github.com/apache/rocketmq-dashboard

复制下载链接后使用git下载

可自建文件夹，进入后使用git bash下载

![image-20230801163401327](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011634391.png)

> git clone https://github.com/apache/rocketmq-dashboard.git

![image-20230801161727083](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011617144.png)



下载完成后，进入`application.yml`中查看配置

![image-20230801163618945](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011636007.png)

![image-20230801170930502](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011709586.png)

保存后进入到 ../rocketmq-dashboard目录下，鼠标右键进入git控制台

> 执行 mvn clean package -Dmaven.test.skip=true

将该文件打包成jar包，该jar包保存在 该目录的 target子目录下

![image-20230801164211692](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011642741.png)

打包完成！

![image-20230801165117196](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011651260.png)

在 target子目录下可找到对应的jar包

![image-20230801165150755](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011651823.png)

在该目录下打开cmd，输入指令==（请保证已经运行NameServer和broker）==：

> java -jar rocketmq-dashboard-1.0.1-SNAPSHOT.jar

![image-20230801170551977](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011705070.png)

成功执行jar包

然后在网页中访问 http://127.0.0.1:8080/#/ 即可进入rocketmq的图形化界面

![image-20230801170643300](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011706400.png)





#### 3.2.Linux下的安装

==请提前设置服务器的防火墙，放通9876和10909（默认的 RocketMQ Broker 端口号）端口==

进入[RocketMQ官网下载]([下载 | RocketMQ (apache.org)](https://rocketmq.apache.org/download/))

##### 1、选择**Binary 下载**

![image-20230801144754675](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011735189.png)



##### 2、在linux中创建RocketMQ文件夹

> mkdir RocketMQ

![image-20230801172901401](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011729451.png)



##### 3、将rocketmq-all-5.1.2-bin-release.zip压缩文件上传到linux服务器中

连接工具[XSHELL - NetSarang Website](https://www.xshell.com/zh/xshell/)

![image-20230801174800693](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011748741.png)

将压缩包上传到第2步创建的文件中

![image-20230801175003080](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011750131.png)

##### 4、解压zip包

> cd ./RocketMQ/
>
> unzip rocketmq-all-5.1.2-bin-release.zip

![image-20230801175140268](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011752656.png)

如果你的服务器没有unzip命令，则下载安装一个

> yum install unzip



##### 5、配置环境变量

> vim /etc/profile
>
> 在文件末尾添加
>
> export NAMESRV_ADDR=**服务器IP**:9876  



##### 6、修改脚本文件

修改目录/root/RocketMQ/rocketmq-all-5.1.2-bin-release/bin下的配置文件： `runserver.sh`、`runbroker.sh`

修改`runserver.sh `中原有内存配置

```sh
JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
```

修改runbroker.sh 中原有内存配置

```sh
JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m"
```

修改目录/root/RocketMQ/rocketmq-all-5.1.2-bin-release/conf/broker.conf文件

在最后添加上

```
namesrvAddr = (服务器ip):9876
autoCreateTopicEnable=true
brokerIP1 = (服务器ip)
```

![image-20230801191136957](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011911005.png)

##### 7、启动

进入/root/RocketMQ/rocketmq-all-5.1.2-bin-release

首先在安装目录下创建一个logs文件夹，用于存放日志

> mkdir logs

运行两条命令,启动NameServer和broker

>nohup sh bin/mqnamesrv > ./logs/namesrv.log &
>
>nohup sh bin/mqbroker -c conf/broker.conf > ./logs/broker.log &

![image-20230801192010772](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011920827.png)

运行后可在logs文件夹下看到两个日志文件

![image-20230801192127832](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011921911.png)

![image-20230801193014578](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011930651.png)



##### 8.配置可视化页面

前置步骤参考windows下的第5步[5.配置可视化页面](#5.配置可视化页面)

将jar包上传到服务器的/root/RocketMQ中

![image-20230801193449152](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011934222.png)

然后在RockerMQ中运行指令：

> nohup java -jar rocketmq-dashboard-1.0.1-SNAPSHOT.jar rocketmq.config.namesrvAddr=127.0.0.1:9876

命令拓展:--server.port指定运行的端口

--rocketmq.config.namesrvAddr=127.0.0.1:9876 指定namesrv地址

![image-20230801193916469](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011939517.png)

![image-20230801194920398](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011949490.png)

成功运行！

最后访问 **服务器ip:8080** 即可访问到图形化界面

![image-20230801195122389](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308011951502.png)





#### 3.3.docker安装（推荐）

##### 1、下载RockerMQ需要的镜像

> docker pull  rocketmqinc/rocketmq 
>
> docker pull  styletang/rocketmq-console-ng  

![image-20230801202211846](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308012022924.png)



##### 2、启动NameServer服务

创建NameServer数据存储路径

>  mkdir -p  /home/rocketmq/data/namesrv/logs /home/rocketmq/data/namesrv/store  

启动NameServer容器

> docker run -d --name rmqnamesrv -p 9876:9876 -v /home/rocketmq/data/namesrv/logs:/root/logs -v /home/rocketmq/data/namesrv/store:/root/store -e "MAX_POSSIBLE_HEAP=100000000"  rocketmqinc/rocketmq sh mqnamesrv


这是一个Docker命令，用于在Docker容器中运行RocketMQ Name Server（消息服务器）。让我们逐步解释这个命令：

```
bashCopy codedocker run -d \
--name rmqnamesrv \
-p 9876:9876 \
-v /home/rocketmq/data/namesrv/logs:/root/logs \
-v /home/rocketmq/data/namesrv/store:/root/store \
-e "MAX_POSSIBLE_HEAP=100000000" \
rocketmqinc/rocketmq sh mqnamesrv
```

解释：

- `docker run`: 这是Docker命令的基本部分，用于运行一个新的容器。
- `-d`: 这是一个选项，表示在后台（detached mode）运行容器。
- `--name rmqnamesrv`: 这是为容器指定一个名称，该名称为"rmqnamesrv"。
- `-p 9876:9876`: 这是端口映射的选项，将主机的端口9876映射到容器的端口9876。RocketMQ的Name Server默认监听端口是9876，通过这个映射，可以从主机的9876端口访问容器中运行的RocketMQ Name Server。
- `-v /home/rocketmq/data/namesrv/logs:/root/logs`: 这是用于将主机的`/home/rocketmq/data/namesrv/logs`目录映射到容器内的`/root/logs`目录。这样做的目的是将RocketMQ Name Server的日志文件存储在主机的目录中，方便查看和管理。
- `-v /home/rocketmq/data/namesrv/store:/root/store`: 这是用于将主机的`/home/rocketmq/data/namesrv/store`目录映射到容器内的`/root/store`目录。这样做的目的是将RocketMQ Name Server的存储文件存储在主机的目录中。
- `-e "MAX_POSSIBLE_HEAP=100000000"`: 这是用于设置环境变量的选项，设置了RocketMQ Name Server的最大堆内存大小为100,000,000字节，约为100MB。
- `rocketmqinc/rocketmq`: 这是指定要运行的Docker镜像的名称。在这里，它使用了RocketMQ官方提供的Docker镜像，名为`rocketmqinc/rocketmq`。
- `sh mqnamesrv`: 这是在容器中要运行的命令。在这里，它运行了RocketMQ Name Server的启动命令。



##### 3、启动Broker服务

创建Broker数据存储路径

> mkdir -p /home/rocketmq/data/broker/logs /home/rocketmq/data/broker/store

创建conf配置文件目录

> mkdir /home/rocketmq/conf

在配置文件目录下创建broker.conf配置文件

```
# 所属集群名称，如果节点较多可以配置多个
brokerClusterName = DefaultCluster
#broker名称，master和slave使用相同的名称，表明他们的主从关系
brokerName = broker-a
#0表示Master，大于0表示不同的slave
brokerId = 0
#表示几点做消息删除动作，默认是凌晨4点
deleteWhen = 04
#在磁盘上保留消息的时长，单位是小时
fileReservedTime = 48
#有三个值：SYNC_MASTER，ASYNC_MASTER，SLAVE；同步和异步表示Master和Slave之间同步数据的机制；
brokerRole = ASYNC_MASTER
#刷盘策略，取值为：ASYNC_FLUSH，SYNC_FLUSH表示同步刷盘和异步刷盘；SYNC_FLUSH消息写入磁盘后才返回成功状态，ASYNC_FLUSH不需要；
flushDiskType = ASYNC_FLUSH
# 设置broker节点所在服务器的ip地址
autoCreateTopicEnable=true
brokerIP1 = 你服务器外网ip
```

启动Broker容器 (==注意先开放10911和10909端口==)

> docker run -d --name rmqbroker --link rmqnamesrv:namesrv -p 10911:10911 -p 10909:10909 -v /home/rocketmq/data/broker/logs:/root/logs -v /home/rocketmq/data/broker/store:/root/store -v /home/rocketmq/conf/broker.conf:/opt/rocketmq/conf/broker.conf --privileged=true -e "NAMESRV_ADDR=namesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq sh mqbroker -c /opt/rocketmq/conf/broker.conf

解释：

- `docker run`: 这是Docker命令的基本部分，用于运行一个新的容器。
- `-d`: 这是一个选项，表示在后台（detached mode）运行容器。
- `--name rmqbroker`: 这是为容器指定一个名称，该名称为"rmqbroker"。
- `--link rmqnamesrv:namesrv`: 这是用于将已经运行的RocketMQ Name Server容器 "rmqnamesrv" 链接到当前运行的Broker容器。这样Broker容器就可以通过"namesrv"主机名访问Name Server。
- `-p 10911:10911`: 这是端口映射的选项，将主机的端口10911映射到容器的端口10911。RocketMQ的Broker默认监听端口是10911，通过这个映射，可以从主机的10911端口访问容器中运行的RocketMQ Broker。
- `-p 10909:10909`: 同上，将主机的端口10909映射到容器的端口10909。RocketMQ的Broker默认监听的另一个端口是10909，该端口用于向主节点发送心跳。
- `-v /home/rocketmq/data/broker/logs:/root/logs`: 这是用于将主机的`/home/rocketmq/data/broker/logs`目录映射到容器内的`/root/logs`目录。这样做的目的是将RocketMQ Broker的日志文件存储在主机的目录中，方便查看和管理。
- `-v /home/rocketmq/data/broker/store:/root/store`: 这是用于将主机的`/home/rocketmq/data/broker/store`目录映射到容器内的`/root/store`目录。这样做的目的是将RocketMQ Broker的存储文件存储在主机的目录中。
- `-v /home/rocketmq/conf/broker.conf:/opt/rocketmq/conf/broker.conf`: 这是用于将主机的`/home/rocketmq/conf/broker.conf`文件映射到容器内的`/opt/rocketmq/conf/broker.conf`文件。这个文件是RocketMQ Broker的配置文件，通过这个映射，可以将自定义的Broker配置应用到容器中。
- `--privileged=true`: 这是为容器添加特权模式，这样容器就可以获得更高的权限。
- `-e "NAMESRV_ADDR=namesrv:9876"`: 这是用于设置环境变量的选项，设置了RocketMQ Broker的Name Server地址为"namesrv:9876"。`NAMESRV_ADDR`是RocketMQ Broker连接Name Server的地址，这里设置为"namesrv:9876"表示通过名为"namesrv"的容器连接Name Server。
- `-e "MAX_POSSIBLE_HEAP=200000000"`: 这是用于设置环境变量的选项，设置了RocketMQ Broker的最大堆内存大小为200,000,000字节，约为200MB。
- `rocketmqinc/rocketmq`: 这是指定要运行的Docker镜像的名称。在这里，它使用了RocketMQ官方提供的Docker镜像，名为`rocketmqinc/rocketmq`。
- `sh mqbroker -c /opt/rocketmq/conf/broker.conf`: 这是在容器中要运行的命令。在这里，它运行了RocketMQ Broker的启动命令，通过`-c`参数指定了配置文件的路径。

启动控制台 (==注意先开放9999端口==)

> docker run -d --name rmqadmin -e "JAVA_OPTS=-Drocketmq.namesrv.addr=服务器的ip:9876 \
>
> -Dcom.rocketmq.sendMessageWithVIPChannel=false \
>
> -Duser.timezone='Asia/Shanghai'" -v /etc/localtime:/etc/localtime -p 9999:8080 styletang/rocketmq-console-ng

解释：

- `docker run`: 这是Docker命令的基本部分，用于运行一个新的容器。
- `-d`: 这是一个选项，表示在后台（detached mode）运行容器。
- `--name rmqadmin`: 这是为容器指定一个名称，该名称为"rmqadmin"。
- `-e "JAVA_OPTS=..."`: 这是用于设置Java虚拟机（JVM）运行时的参数。在这里，它设置了三个参数：
  - `-Drocketmq.namesrv.addr=服务器的ip:9876`：这是用于设置RocketMQ Name Server的地址。您需要将"服务器的ip"替换为实际的RocketMQ Name Server的IP地址，端口为9876。
  - `-Dcom.rocketmq.sendMessageWithVIPChannel=false`：这是用于设置RocketMQ消息发送时是否启用VIP通道的参数，将其设置为false表示禁用VIP通道。
  - `-Duser.timezone='Asia/Shanghai'`：这是用于设置容器时区的参数，将其设置为'Asia/Shanghai'表示使用上海时区。
- `-v /etc/localtime:/etc/localtime`: 这是用于将主机的时区配置映射到容器内，保持容器与主机的时区一致。
- `-p 9999:8080`: 这是端口映射的选项，将主机的端口9999映射到容器的端口8080。RocketMQ控制台使用8080端口，通过这个映射，可以从主机的9999端口访问容器中运行的RocketMQ控制台。
- `styletang/rocketmq-console-ng`: 这是指定要运行的Docker镜像的名称。在这里，它使用了RocketMQ控制台NG的Docker镜像，名为`styletang/rocketmq-console-ng`。



正常启动后的docker ps

![image-20230801214538318](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308012145429.png)

##### 4、访问控制台

http://你的服务器外网ip:9999/

![image-20230801214801893](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308012148095.png)





## 4.RocketMQ快速入门

[4.x文档 ](https://rocketmq.apache.org/zh/docs/4.x/) 下文基于该文档

[5.0文档](https://rocketmq.apache.org/zh/docs/)

通过该部分可以快速入门RocketMQ提供的多种发送消息的模式，例如同步消息，异步消息，顺序消息，延迟消息，事务消息等

### ==消息发送和监听的流程==

####  消息生产者

1.创建消息生产者`producer`，并制定生产者组名

2.指定`NameServer`地址

3.启动`producer`

4.创建消息对象，指定主题`Topic`、`Tag`和消息体等

5.发送消息

6.关闭生产者`producer`



#### 消息消费者

1.创建消费者`consumer`，制定消费者组名

2.指定`NameServe`r地址

3.创建监听订阅主题`Topic`和`Tag`等

4.处理消息

5.启动消费者`consumer`



了解了消息发送和监听的流程，我们可以开始简单的代码实现



### Start

创建一个空项目  `RocketMQ-study`

在空项目下创建一个新模板：

![image-20230801233051349](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308012330430.png)



#### 简单测试

![image-20230801233155749](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308012331826.png)

引入依赖：

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--原生api-->
        <dependency>
            <groupId>org.apache.rocketmq</groupId>
            <artifactId>rocketmq-client</artifactId>
            <version>5.1.2</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
```

测试一个简单的生产方法和消费方法：

```java
package com.wp.rocketmqdemo01.constant;

public interface MqConstant {
    /**
     * 生产者组名
     */
    String PRODUCER_GROUP = "test-producer-group";
    /**
     * 消费者组名
     */
    String CONSUMER_GROUP = "test-consumer-group";
    /**
     * 主题
     */
    String TOPIC = "test-topic";
    /**
     * NameServer地址
     */
    String NAME_SRV_ADDR = "ip:9876";
}
```

```java
package com.wp.rocketmqdemo01.demo;

import com.wp.rocketmqdemo01.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

@Slf4j
public class SimpleTest01 {

    /**
     * 生产者
     * @throws Exception
     */
    @Test
    public void SimpleTestProducer() throws Exception {
        // 创建一个生产者（制定一个组名
        DefaultMQProducer producer = new DefaultMQProducer(MqConstant.PRODUCER_GROUP);
        // 指定NameServer地址,连接到NameServer
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动生产者
        producer.start();
        // 创建一个消息
        Message message = new Message(MqConstant.TOPIC, MqConstant.TAG, "Hello RocketMQ".getBytes());
        // 发送消息
        SendResult sendResult = producer.send(message);
        log.info("消息发送结果：{}", sendResult);
        // 关闭生产者
        producer.shutdown();
    }

    /**
     * 消费者
     * @throws Exception
     */
    @Test
    public void SimpleTestConsumer() throws Exception {
        // 创建一个消费者（制定一个组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MqConstant.CONSUMER_GROUP);
        // 指定NameServer地址,连接到NameServer
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 订阅主题 *表示订阅所有
        consumer.subscribe(MqConstant.TOPIC, "*");
        // 注册消息监听器（一直监听，异步回调方法）
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 这个就是消费的方法 业务逻辑
                log.info("我是消费者，我正在消费消息");
                log.info(list.get(0).toString());
                for(MessageExt messageExt : list) {
                    log.info("消费消息：{}", new String(messageExt.getBody()));
                }
                log.info("消息上下文：{}", consumeConcurrentlyContext);
                // 返回消费状态
                // 如果消费成功，返回CONSUME_SUCCESS，消息会被消费掉，从mq出队
                // 如果消费失败，返回RECONSUME_LATER，消息会重新投递，mq不会出队
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者 这个start一定要写在registerMessageListener下面
        consumer.start();
        // 保证消费者不退出，挂起当前jvm
        System.in.read();
    }
}
```

分别测试后得到如下结果：

![image-20230802175627877](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308021756018.png)

![image-20230802175700920](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308021757101.png)

![image-20230802175717327](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308021757415.png)



![image-20230802175759752](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308021757819.png)

![image-20230802180033298](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308021800379.png)

![image-20230802180108488](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308021801705.png)

1. `Broker Offset`（代理器偏移量）： Broker Offset是指消息队列中的消息在Broker（消息代理器）上的偏移位置。**当生产者将消息发送到Broker时，每条消息都会被赋予一个唯一的偏移量，表示该消息在队列中的位置**。Broker Offset主要由消息代理器维护和管理，用于追踪消息的存储和处理情况。
2. `Consumer Offset`（消费者偏移量）： Consumer Offset是指消费者在消费消息时的位置偏移。**当消费者成功消费了一条消息后，会将自己的消费偏移量记录下来**，表示下次继续消费消息的起始位置。消费者需要定期更新Consumer Offset，以保证消息处理的准确性和可靠性。
3. `Diff Total`（差异总数）： **Diff Total是Broker Offset和Consumer Offset之间的差异总和**。也就是说，Diff Total表示消息队列中已经被生产者发送并存储在Broker上的消息数量，但尚未被消费者消费的消息数量。Diff Total可以用来监控消息队列的堆积情况，帮助发现消息处理速度跟不上消息产生速度的问题。

最简单的测试完成！



#### MQ的消费模式

可以大致分为两种：推（Push）模式和拉（Pull）模式

1. 推（Push）模式： 在推模式中，消息队列将消息直接推送给消费者。一旦有新的消息产生并发送到队列中，队列会立即将该消息推送给已注册的消费者。这样消费者就可以及时收到并处理消息。推模式适用于需要实时响应和高实时性的场景，比如即时通讯、实时推送等。
2. 拉（Pull）模式： 在拉模式中，消费者需要主动从消息队列中拉取消息。消费者需要周期性地向队列发起请求，查询是否有新的消息可供消费。如果队列中有新消息，队列会将这些消息返回给消费者，然后消费者再对这些消息进行处理。拉模式适用于不需要实时响应的场景，比如批量处理、数据同步等。

每种消费模式都有其适用的场景和优缺点。推模式能够及时将消息推送给消费者，实现了实时性和低延迟，但在高并发场景下可能会产生大量推送请求，增加系统压力。而拉模式需要消费者主动轮询消息队列，可以控制消费的速度，但可能会导致消息处理不及时，影响系统的实时性。

#### 发送同步消息

上面的快速入门就是发送同步消息，**发送过后就会有一个返回值(SEND_OK)**，也就是mq服务器接收到消息后返回的一个确认，这种方式非常安全，但是性能上并没有这么高，而且在mq集群中，也是要等到所有的从机都复制了消息以后才会返回，所以针对重要的消息可以选择这种方式

![img](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308022124039.jpg)



#### 发送异步消息

异步消息通常用在对响应时间敏感的业务场景，即发送端不能容忍长时间地等待Broker的响应。发送完以后会有一个异步消息通知

```java
    @Test
    public void syncProducer() throws Exception {
        // 创建默认的生产者
        DefaultMQProducer producer = new DefaultMQProducer(MqConstant.PRODUCER_GROUP+ "_sync");
        // 设置nameServer地址
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动实例
        producer.start();
        Message msg = new Message("TopicSyncTest", ("异步消息").getBytes());
        producer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功 (后执行)");
            }
            @Override
            public void onException(Throwable e) {
                log.info("发送失败 (后执行)");
            }
        });
        log.info("先执行");
        // 挂起jvm 因为回调是异步的不然测试不出来
        System.in.read();
        // 关闭实例
        producer.shutdown();
    }
```

#### 发送单向消息

这种方式主要用在不关心发送结果的场景，这种方式吞吐量很大，但是存在消息丢失的风险，例如日志信息的发送

```java
    // 发送单向消息
    producer.sendOneway(msg);
```



#### 发送延迟消息

消息放入mq后，过一段时间，才会被监听到，然后消费

比如下订单业务，提交了一个订单就可以发送一个延时消息，15min后去检查这个订单的状态，如果还是未付款就取消订单释放库存。

**DelayLevel**

```java
    Message msg = new Message("TopicTest", ("延迟消息").getBytes());
    // 给这个消息设定一个延迟等级
    // messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
    msg.setDelayTimeLevel(3);
    // 发送单向消息
    producer.send(msg);

```

==注意==

RocketMQ支持以下几个固定的延时等级，等级1就对应1s，以此类推，最高支持2h延迟

private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";

| 投递等级（delay level） | 延迟时间 | 投递等级（delay level） | 延迟时间 |
| ----------------------- | -------- | ----------------------- | -------- |
| 1                       | 1s       | 10                      | 6min     |
| 2                       | 5s       | 11                      | 7min     |
| 3                       | 10s      | 12                      | 8min     |
| 4                       | 30s      | 13                      | 9min     |
| 5                       | 1min     | 14                      | 10min    |
| 6                       | 2min     | 15                      | 20min    |
| 7                       | 3min     | 16                      | 30min    |
| 8                       | 4min     | 17                      | 1h       |
| 9                       | 5min     | 18                      | 2h       |

**修改延时级别**

RocketMQ的延迟等级可以进行修改，以满足自己的业务需求，可以修改/添加新的level。具体见[该文章]([RocketMQ进阶-延时消息 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/142441996))

同时5.0支持使用时间戳来设置延迟时间[定时/延时消息 | RocketMQ (apache.org)](https://rocketmq.apache.org/zh/docs/featureBehavior/02delaymessage)

#### 发送批量消息

可以一次性发送一组消息，那么这一组消息会被当做一个消息消费

```java
    List<Message> msgs = Arrays.asList(
            new Message("TopicTest", "我是一组消息的A消息".getBytes()),
            new Message("TopicTest", "我是一组消息的B消息".getBytes()),
            new Message("TopicTest", "我是一组消息的C消息".getBytes())

    );
    SendResult send = producer.send(msgs);

```



#### 发送顺序消息	

顺序消息是一种特殊类型的消息，可以保证按照发送的顺序进行消费，从而保证了消息的有序性。

在 RocketMQ 中，保证消息顺序发送的关键是要将相关的消息发送到同一个队列中，并且消费者按照队列的顺序来消费消息

以下是实现顺序消息的步骤：

1. 创建一个指定顺序的 MessageQueueSelector。 在发送消息时，你需要指定一个 MessageQueueSelector 来选择目标消息队列。该 Selector 将根据某种规则将相关的消息发送到同一个队列中，保证了消息的顺序性。
2. 设置 MessageQueueSelector 选择消息队列的逻辑。 在实现 MessageQueueSelector 接口的 select 方法中，你需要编写逻辑来选择目标队列。可以根据消息的某个属性或者业务关联来确定消息应该发送到哪个队列。
3. 发送消息时使用 MessageQueueSelector。 在发送消息时，使用 producer.send(msg, selector, orderId) 方法来指定消息发送的队列。其中，selector 参数即为你实现的 MessageQueueSelector 接口的实例，orderId 是一个标识消息顺序的参数。

```java
            String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
            for (int i = 0; i < 100; i++) {
                int orderId = i % 10;
                Message msg =
                    new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        // 当前主题有多少个队列
                        int queueNumber = mqs.size();
                        // 这个arg就是后面传入的 orderId
                        Integer id = (Integer) arg;
                        // 用这个值去%队列的个数得到一个队列
                        int index = id % queueNumber
                        // 返回选择的这个队列即可 ，那么相同的orderId 就会被放在相同的队列里 实现First In, First                           //Out了
                        return mqs.get(index);
                    }
                }, orderId);
```

**消费者的监听 MessageListenerOrderly如下**

```java
    // 注册一个消费监听 MessageListenerOrderly 是顺序消费 单线程消费
    consumer.registerMessageListener(new MessageListenerOrderly() {
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            MessageExt messageExt = msgs.get(0);
            System.out.println(new String(messageExt.getBody()));
            return ConsumeOrderlyStatus.SUCCESS;
        }
    });

```





## 5.SpringBoot集成RocketMQ



###  搭建rocketmq-producer（消息生产者）模块

![image-20230803004733801](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308030047989.png)



![image-20230803004824128](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308030048242.png)

#### 完整的依赖

```xml
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-configuration-processor</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
        
		<dependency>
			<groupId>org.apache.rocketmq</groupId>
			<artifactId>rocketmq-spring-boot-starter</artifactId>
			<version>2.1.1</version>
		</dependency>

	</dependencies>

```

#### 修改配置文件application.yml

```yaml
spring:
  application:
    name: rocketmq-producer
rocketmq:
  name-server: ip:9876     # rocketMq的nameServer地址
  producer:
    group: boot-test-producer-group        # 生产者组别
    send-message-timeout: 3000  # 消息发送的超时时间
    retry-times-when-send-async-failed: 2  # 异步消息发送失败重试次数
    max-message-size: 4194304       # 消息的最大长度

```

#### 添加测试类的内容：

```java
	/**
	 * 注入rocketMQTemplate，我们使用它来操作mq
	 */
	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	/**
	 * 测试发送简单的消息
	 * @throws Exception
	 */
	@Test
	public void testSimpleMsg() throws Exception {
		// boot-test是topic，我是一个简单的消息是消息内容
		SendResult sendResult = rocketMQTemplate.syncSend("boot-test", "我是一个简单的消息");
		// 拿到消息的发送状态
		log.info(String.valueOf(sendResult.getSendStatus()));
		// 拿到消息的id
		log.info(sendResult.getMsgId());
	}
```



#### 执行后，可得到：

![image-20230803013843291](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308030138389.png)

![image-20230803014006848](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308030140939.png)



### 同理创建消费者模块

#### 修改配置文件application.yml

```yaml
spring:
  application:
    name: rocketmq-consumer
rocketmq:
  name-server: 47.115.209.249:9876     # rocketMq?nameServer??
```

#### 添加测试类的内容：

```java
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 创建一个简单消息的监听
 * 1.类上添加注解@Component和@RocketMQMessageListener
 *      @RocketMQMessageListener(topic = "powernode", consumerGroup = "powernode-group")
 *      topic指定消费的主题，consumerGroup指定消费组,一个主题可以有多个消费者组,一个消息可以被多个不同的组的消费者都消费
 * 2.实现RocketMQListener接口，注意泛型的使用，可以为具体的类型，如果想拿到消息
 * 的其他参数可以写成MessageExt
 */
@Component
@RocketMQMessageListener(topic = "boot-test", consumerGroup = "boot-test-consumer-group",messageModel = MessageModel.CLUSTERING)
@Slf4j
public class SimpleMsgListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("接收到消息：{}",s);
    }
}
```

#### 执行后，可得到：

![image-20230803014921527](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308030149615.png)

![image-20230803014943326](https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202308030149401.png)





## 6.最后

从文章整体没有涉及太深入的一些机制和原理的讲解，因此仅作为入门学习
