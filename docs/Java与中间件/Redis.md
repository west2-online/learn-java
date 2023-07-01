# Redis

[**Redis|官方网站**](https://redis.io/)



## 推荐教程

[Redis 教程 | 菜鸟教程 (runoob.com)](https://www.runoob.com/redis/redis-tutorial.html)

[超强、超详细Redis入门教程 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/411888708)



## Q&A

**Q1：什么是Redis？**

​	Redis是NOSQL的一种，采用键值对方式存储。NOSQL就是非关系数据库，基本上不是关系数据库都可以叫NOSQL。关于SQL数据库，请转移到[备忘录](docs/3-备忘录.md)。

**Q2：Redis可以持久化吗？**

​	可以，并且有其特殊的持久化配置

**Q3：我为什么选择Redis，不能只用Mysql吗？**

​	Redis基于内存实现存储功能，这代表Redis大大减少了持久化工作，相比于SQL数据库会操作更快但是选择了Redis也要考虑Redis的维护问题。

- 对于规模较小的但是持久化需求较高的或缓存命中率较低应用，只使用MYSQL是合理的
- 对于规模较小的但是缓存数据命中率较高的应用，建议使用应用配置的Cache
- 对于规模较大的应用且缓存数据命中率很高的，建议上手Redis

**Q4：Redis默认占用端口？**

​	6379



## Redis安装和配置

### Linux(Ubuntu)

下载安装

```shell
#下载安装Redis-server，安装后默认启动服务
> sudo apt install redis-server
#查看服务状态
> sudo systemctl status redis-server
#重启服务
> sudo systemctl restart redis-server
```

远程连接（一般来说没有必要开启远程连接，危险多多）

```shell
#文本编辑器打开配置
> sudo vim /etc/redis.conf
#定位到以bind 127.0.0.1 ::1开头的一行，并且取消它的注释：
bind 0.0.0.0 ::1
#保存
#重启服务
> sudo systemctl restart redis-server
#开放防火墙,允许从一个指定 IP 地址或者一个指定 IP 范围访问Redis
> sudo ufw allow proto tcp from ***.***.***.***/*** to any port 6379
```

连接测试

```shell
#使用本地或远程主机的redis-cli
> redis-cli -h <REDIS_IP_ADDRESS> ping
#成功响应
PONG
```

### Windows

**由于上手WSL具有一定的困难性，可以先上手非官方Redis，之后转向Linux。**

**非官方**：[Releases · tporadowski/redis (github.com)](https://github.com/tporadowski/redis/releases)

​	[Redis-Windows安装教程](https://blog.csdn.net/weixin_44893902/article/details/123087435)

​	非官方问题在于Redis版本更新的很慢，目前（2023.07）最新的更新停留在2022.02

**官方**：[官方|Install Redis on Windows | Redis](https://redis.io/docs/getting-started/installation/install-redis-on-windows/)

​	官方口吻：Redis在Windows上不受官方支持。要在 Windows 上安装 Redis，您首先需要启用 [WSL2](https://docs.microsoft.com/en-us/windows/wsl/install)（适用于 Linux 的 Windows 子系统）。

### MacOs

[官方|Install Redis on macOS](https://redis.io/docs/getting-started/installation/install-redis-on-mac-os/)



## Redis上手指南

### Redis连接测试

```shell
> ping
PONG
```

### Redis数据类型与命令

[官方文档|Redis数据类型](https://redis.io/docs/data-types/)

[Commands | Redis官方](https://redis.io/commands/)

**常用的数据类型**：String、List、Hash、Set（无序集合）、Sorted set（有序集合）、HyperLogLog（基数统计）

**常用命令**：INCR（自增）

Redis也增加了实用的地理类型 **geospatial**

```shell
> GEOADD locations:ca -122.27652 37.805186 station:1
(integer) 1
> GEOADD locations:ca -122.2674626 37.8062344 station:2
(integer) 1
> GEOADD locations:ca -122.2469854 37.8104049 station:3
(integer) 1

> GEOSEARCH locations:ca FROMLONLAT -122.2612767 37.7936847 BYRADIUS 5 km WITHDIST
1) 1) "station:1"
   2) "1.8523"
2) 1) "station:2"
   2) "1.4979"
3) 1) "station:3"
   2) "2.2441"
```

### Redis发布与订阅（pub&sub）

[Redis Pub/Sub | Redis](https://redis.io/docs/manual/pubsub/)

[Redis 发布订阅 | 菜鸟教程 (runoob.com)](https://www.runoob.com/redis/redis-pub-sub.html)

### Redis事务机制（Transactions）

[Transactions | Redis](https://redis.io/docs/manual/transactions/)

Redis事务机制与MYSQL事务机制不同，Redis的事务管理相当于批量执行命令，其中执行失败的命令不会影响其他命令的执行，也就是事务不会回滚

### Redis持久化的两种方式

[Redis的两种持久化方式 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/345725544)



## Redis In Java

**Java**

[Java guide | Redis](https://redis.io/docs/clients/java/)

**Springboot**

[SpringBoot教程(十四) | SpringBoot集成Redis(全网最全) - 掘金 (juejin.cn)](https://juejin.cn/post/7076244567569203208#heading-5)



## 思考问题

1. 我真的需要Redis吗，什么时候需要？
2. Redis为什么比SQL数据库要快？
3. Redis事务机制与Mysql的事务机制有什么不同？
4. Redis的持久化的两种方式是什么？并展开说说。
5. Redis在分布式场景为什么具有优势？Redis集群机制是什么？主从复制是什么？哨兵机制又是什么？
6. 如何设计Redis以应对在某些时间相当庞大的数据量？

