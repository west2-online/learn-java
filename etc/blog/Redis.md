# Redis 入门

## 问题的抛出

**出现的问题：**

- 海量用户
- 高并发

**罪魁祸首——关系型数据库：**

- 性能瓶颈：磁盘IO性能低下
- 扩展瓶颈：数据关系复杂，扩展性差，不便于大规模集群

## **解决思路**

- 降低磁盘IO次数，越低越好 —— 内存存储
- 去除数据间的关系，越简单越好 —— 不存储关系，仅存储数据

## Nosql简介

NoSQL：
即`Not-OnlySQL`（泛指非关系型的数据库），作为关系型数据库的补充。

作用：
应用对于海量用户和海量数据前提吓得数据处理问题。

特征：

- 可扩容，可伸缩
- 大数据量下得高性能
- 灵活得数据模型
- 高可用

常见`Nosql`数据库：

- Redis
- memcache
- HBase
- MongoDB

## 解决方案（电商场景）

[![image-20221008185402254](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008185402254.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008185402254.png)

## Redis

概念：
`Redis(REmote DIctinary Server)`是用C语言开发的一个开源的高性能键值对`(key-value)`数据库
特征：

- 数据间没有必然的关联关系
- 内部采用单线程机制进行工作
- 高性能。官方提供测试数据，50个并发执行100000个请求，读的速度是110000次/s，写的速度是81000次/s。
- 多数据类型支持：`string（字符串类型）`、`list（列表类型）`、`hash（散列类型）`、`set（集合类型）`、`sorted_set（有序集合类型）`
- 持久化支持。可以进行数据灾难恢复

## Redis的应用

- 为热点数据加速查询（主要场景）、如热点商品、热点新闻、热点资讯、推广类等提高访问量信息等。
- 任务队列、如秒杀、抢购、购票等
- 即时信息查询，如各位排行榜、各类网站访问统计、公交到站信息、在线人数信息（聊天室、网站）、设备信号等
- 时效性信息控制，如验证码控制，投票控制等
- 分布式数据共享，如分布式集群构架中的`session`分离
- 消息队列
- 分布式锁

## Redis的基本操作

命令行模式工具使用思考

- 功能性命令
- 清除屏幕信息
- 帮助信息查阅
- 退出指令

### 信息添加

- 功能：设置key,value数据
- 命令

```
SHELL
set key value
```

- 范例

```
SHELL
set name itheima
```

### 信息查询

- 功能：根据key查询对应的value,如果不存在，返回空（null)
- 命令

```
SHELL
get key
```

- 范例

```
SHELL
get name
```

[![image-20221008185941452](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008185941452.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008185941452.png)

### 清除屏幕信息

- 功能：清除屏幕中的信息
- 命令

```
SHELL
clear
```

### 帮助命令

- 功能：获取命令帮助文档，获取组中所有命令信息名称
- 命令

```
SHELL
help 命令名称
help @组名
```

[![image-20221008190042511](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008190042511.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008190042511.png)

[![image-20221008190110484](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008190110484.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008190110484.png)

推出客户端命令行模式

- 功能：推出客户端
- 命令

```
SHELL
quit
exit
< ESC>(慎用)
```

# Redis 数据类型

## Redis数据类型 String

### 数据存储类型介绍

业务数据的特殊性

**作为缓存使用**

1. 原始业务功能设计
   秒杀
   618活动
   双十一活动
   排队购票
2. 运营平台监控到的突发高频访问数据
   突发市政要闻，被强势关注围观
3. 高频、复杂的统计数据
   在线人数
   投票排行榜

**附加功能**
系统功能优化或升级

- 单服务器升级集群
- `Session`管理
- `Token`管理

### Redis 数据类型（5种常用）

- string –> String
- hash –> Hashmap
- list –> LinkList
- set –> HashSet
- sorted_set –> TreeSet

### String

**redis 数据存储格式**

- redis自身是一个`Map`,其中所有的数据都是采用`key:value`的形式存储
- 数据类型指的是存储的数据的类型，也就是`value`部分的类型，`key`部分永远都是字符串

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70.png)

- **String 类型**
- 存储的数据：单个数据，最贱的数据存储类型，也是最常用的数据存储类型
- 存储数据的格式：一个存储空间保存一个数据
- 存储内容：通常使用字符串，如果字符串以整数的形式展示，可以作为数字操作使用

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/20200409191821527.png)](https://weishao-996.github.io/img/黑马程序员-Redis/20200409191821527.png)

### **String 类型数据的基本操作**

- 添加/修改数据

```
SHELL
set key value
```

- 获取数据

```
SHELL
get key
```

- 删除数据

```
SHELL
del key
```

- 添加/修改多个数据

```
SHELL
mset key1 valueq key2 value2 …
```

- 获取多个数据

```
SHELL
mget key1 key2 …
```

- 获取数据字符个数（字符串长度）

```
SHELL
strlen key
```

- 追加信息到原始信息后部（如果原始信息存在就追加，否则新建）

```
SHELL
append key value
```

[![image-20221008193222956](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008193222956.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008193222956.png)

### String类型数据的扩展操作

**业务场景**
大型企业级应用中，分表操作是基本操作，使用多张表存储同类型数据，但是对应的主键id必须保证统一性，不能重复。Oracle数据库具有sequence设定，可以解决该问题，但是MySQL数据库并不具有类似的机制，那么如何解决？

**解决方案**

- 设置数值数据增加指定范围的值

```
POWERSHELL
incr key
incrby key increment
incrbyfloat key increment
```

- 设置数值数据减少指定范围的值

```
SHELL
decr key
decrby key increment
```

[![image-20221008193637593](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008193637593.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008193637593.png)

[![image-20221008193707121](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008193707121.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008193707121.png)

**String作为数值操作**

`String`在`redis`内部存储默认就是一个字符串，当遇到增减类操作`incr`,`decr`时会转成数值型进行计算
`redis`所有的操作都是原子性的，采用单线程处理所有业务，命令是一个一个执行的，因此无需考虑并发带来的数据影响。
注意：按数值进行操作的数据，如果原始数据不能转成数值，或超过了`redis`数值上线范围，将会报错。`9223372036854775807` (java中long型数据最大值，`Long.MAX_VALUE`)

### String 数据时效性设置

**业务场景**

- 场景一：“最强女生”，启动海选投票，只能通过微信投票，每个微信号每4个小时只能投1票。
- 场景二：电商商家开启热门商品推荐，热门商品不能一直处于热门期，每种商品热门期维持3天，3天后自动取消热门
- 场景三：新闻网站会出现热点新闻，热点新闻最大的特征是对时效性，如何自动控制热点新闻的时效性

**解决方案**

- 设置数据具有指定的声明周期

```
POWERSHELL
setex key seconds value
psetex key milliseconds value
```

[![image-20221008194430071](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008194430071.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008194430071.png)

[![image-20221008195132656](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008195132656.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008195132656.png)

`psetex` 和 `setex` 作用一样，都是设置过期时间，区别是：`psetex` 时间单位是毫秒，`setex` 是秒。

Tips 2:

- `redis` 控制数据的生命周期，通过数据是否失效控制业务行为，适用于所有具有时效性限定控制的操作

### String 类型的注意事项

- 数据操作不成功的反馈与数据正常操作之间的差异
  - 1、表示运行结果是否成功
     `（integer)0–>false `失败
       `（integer)1–>true `成功
  - 2、表示运行结果值
     `（integer)3–>3 `3个
       `（integer)1–>1 `1个
- 数据未获取到`(nil）`等同于`null`
- 数据最大存储量`512MB`
- 数值计算最大范围（java中的long的最大值）

### String类型应用场景

**业务场景**
主页高频访问信息显示控制，例如新浪微博大V主页显示粉丝数与微博数量

[![image-20221008195627422](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008195627422.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008195627422.png)

### key的设置约定

数据库中的热点数据`key`命名惯例

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/20200413220259916.png)](https://weishao-996.github.io/img/黑马程序员-Redis/20200413220259916.png)

## Redis数据类型 Hash

### Hash类型

**存储的困惑**
对象类数据的存储如果具有较为频繁的更新需求操作会显得笨重

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652303781884.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652303781884.png)

### **hash类型**

- 新的存储需求：对一系列存储的数据进行编组，方便管理，典型应用存储对象信息
- 需要的内存结构：一个存储空间保存多少个键值对数据
- `hash`类型：底层使用哈希表结构实现数据存储

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652314848056.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652314848056.png)

### hash类型数据的基本操作

- 添加/修改数据

```
POWERSHELL
hset key field value
```

- 获取数据

```
SHELL
hget key field
hgetall key
```

- 删除数据

```
SHELL
hdel key field1 [field2]
```

- 添加/修改多个数据

```
SHELL
hmset key field1 value1 field2 calue2
```

- 获取多个数据

```
SHELL
hmget key field1 field2 …
```

- 获取哈希表中字段的数量

```
SHELL
hlen key
```

- 获取哈希表中是否存在指定的字段

```
SHELL
hexists key field
```

### hash类型数据扩展操作

- 获取哈希表中所有的字段名和字段值

```
SHELL
hkeys key
hvals key
```

- 设置指定字段的数值数据增加指定范围的值

```
SHELL
hincrby key field increment
hincrbyfloat key field increment
```

### hash类型数据操作的注意事项

- hash类型下的`value`只能存储字符串，不允许存储其他类型数据，不存在嵌套现象。如果数据未获取到，对应的值为`(nil)`
- 每个`hash`可以存储`232-1`个键值对
- `hash`类型十分贴近对象的数据存储形式，并且可以灵活添加删除对象属性。但`hash`设计初中不是为了存储大量对象而设计的，切记不可滥用，更不可以将`hash`作为对象列表使用
- `hgetall`操作可以获取全部属性，如果内部`field`过多，遍历整体数据效率就会很低，有可能成为数据访问瓶颈

### hash类型应用场景购物车

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652318807278.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652318807278.png)

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523190155910.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523190155910.png)

### Hash实现抢购

Hash应用场景

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523194297412.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523194297412.png)

**解决方案**

- 以商家`id`作为`key`
- 将参与抢购的商品`id`作为`field`
- 将参与抢购的商品数量作为对应的`value`
- 抢购时使用降至的方式控制产品数量

## Redis数据存储 List

### list类型

- 数据存储需求：存储多个数据，并对数据进入存储空间的顺序进行区分
- 需要的存储数据：一个存储空间保存多个数据，且通过数据可以体现进入顺序
- list类型：保存多个数据，底层使用双向链表存储结构实现

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523626646014.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523626646014.png)

### list类型数据基本操作

- **添加/修改数据**

```
POWERSHELL
lpush key value1 [value2] …
rpush key value1 [value2] …
```

- **获取数据**

```
SHELL
lrange key start stop
lindex key index
llen key
```

- **删除并移除数据**

```
SHELL
lpop key
rpop key
```

### **list 类型数组扩展操作**

- **规定时间内获取并移除数据**

```
PLAINTEXT
blpop key1 [key2] timeout
brpop key1 [key2] timeout
```

[![image-20221008215815344](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008215815344.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008215815344.png)
阻塞式获取，获取值如果还没有的时候可以等，如果有值就可以获取到。

### 业务场景

微信朋友圈点赞，要求按照点赞顺序显示点赞好友信息，如果取消点赞，移除对应好友信息

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523647532016.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166523647532016.png)

- **移除指定数据**

```
SHELL
lrem key count value
```

[![image-20221008220123068](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221008220123068.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221008220123068.png)

- `redis`应用于具有操作线后顺序的数据控制

### list类型数据操作注意事项

- `list `中保存的数据都是`string`类型的，数据总容量式由西安的，最多`232-1`个元素`（4294967295）`
- list具有索引的概念，但是操作数据时候通常以队列的形式进行入队出队操作，或以栈的形式进入栈出栈的操作
- 获取全部数据操作结束索引设置为`-1`
- `list` 可以对数据进行分页操作，通过第一页的信息来自`list`，第2页及更多的信息通过数据库的形式加载

### list类型应用场景

**业务场景**

- twitter、新浪微博、腾讯微博中个人用于的关注列表需要按照用户的关注顺序进行展示，粉丝列表需要将最近关注的粉丝列在前面

- 新闻、资讯类网站如何将最新的新闻或资讯按照发生的事件顺序展示

- 企业运营过程中，系统将产生出大量的运营数据，如何保障堕胎服务器操作日志的统一顺序输出？

  解决方案

- 依赖list的数据具有顺序的特征对信息进行管理

- 使用队列模型解决多路信息汇总合并的问题

- 使用栈模型解决最新消息的问题

## Redis数据类型 Set

- 新的存储需求：存储大量的数据，在查询方面提供更高的效率
- 休要的存储结构：能够保存大量的数据，搞笑的内部存储机制，便于查询
- set类型：与hash存储结构完全相同，仅存储键，不存储值（nil),并且值式不允许重复的

### set类型数据的基本操作

- 添加数据

```
SHELL
sadd key menber1 [member2]
```

- 获取全部数据

```
SHELL
smembers key
```

- 删除数据

```
SHELL
srem key member1 [member2]
```

- 获取集合数据总量

```
SHELL
scard key
```

- 判断集合中是否包含指定数据

```
SHELL
sismember key member
```

### set类型数据的扩展操作

**业务场景**
每位用户首次使用进入头条时候会设置3项爱好的内容，但是后期为了增加用户的活跃度，兴趣点，必须让用户对其他信息类别逐渐产生兴趣，增加客户留存度，如何实现？

**业务分析**

- 系统分析出各个分类的最新或最热点信息条目并组织成set集合
- 随机挑选其中部分信息
- 配合用户关注信息分类中的热点信息组织展示的全信息集合

**解决方案**

- 随机获取集合中指定数量的数据

```
SHELL
srandmember key [count]
```

- 随机获取集合中的某个数据并将该数据移出集合

```
SHELL
spop key [count]
```

- redis应用于随机推荐类信息检索，例如热点歌单推荐，热点新闻推荐，热点旅游线路，应用APP推荐，大V推荐等

### set类型数据的扩展操作

**解决方案**

- 求两个集合的交、并、差集

```
SHELL
sinter key1 [key2]
sunion key1 [key2]
sdiff key1 [key2]
```

- 求两个集合的交、并、差集并存储到指定集合中

```
SHELL
sinterstore destination key1 [key2]
sunionstore destination key1 [key2]
sdiffstore destination key1 [key2]
```

- 将指定数据从原始集合移动到目标集合中

```
SHELL
smove source destination member
```

- `redis`应用于同类信息的关联搜索，二度关联搜索，深度关联搜索
- 显示共同关注（一度）
- 显示共同好友（一度）
- 由用户A出发，获取到好友用户B的好友信息列表（一度）
- 由用户A出发，获取到好友用户B的购物清单列表（二度）
- 由用户A出发，获取到好友用户B的游戏充值列表（二度）

### Set类型数据操作的注意事项

- `set`类型不允许数据重复，如果添加的数据在`set`中已经存在，将只保留一份
- `set`虽然与`hash`的存储结构相同，但是无法启用`hash`中存储值的空间

**业务场景**

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652943755982.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652943755982.png)

**解决方案**

- 依赖set集合数据不重复的特征，依赖set集合hash存储结构特征完成数据过滤与快速查询
- 根据用户id获取用户所有角色
- 根据用户所有角色获取用户所有操作权限放入set集合
- 根据用户所有觉得获取用户所有数据全选放入set集合

**校验工作**：redis提供基础数据还是提供校验结果
Tips 10:

- redis应用于同类型不重复数据的合并操作

### set类型应用场景

**业务场景**

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/20200420110014423.png)](https://weishao-996.github.io/img/黑马程序员-Redis/20200420110014423.png)

**解决方案**

- 利用set集合的数据去重特征，记录各种访问数据
- 建立string类型数据，利用incr统计日访问量（PV)
- 建立set模型，记录不同cookie数量（UV)
- 建立set模型，记录不同IP数量（IP)

**业务场景（黑白名单）**

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652944831775.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652944831775.png)

**解决方案**

- 基于经营战略设定问题用户发现、鉴别规则
- 周期性更行满足规则的用户黑名单，加入set集合
- 用户行为信息达到后与黑名单进行比比对，确认行为去向
- 黑名单过滤IP地址：应用于开放游客访问权限的信息源
- 黑名单过滤设备信息：应用于限定访问设备的信息源
- 黑名单过滤用户：应用于基于访问权限的信息源

Tips 12:

- redis应用于基于黑名单与白名单设定的服务控制

## Redis数据类型 sorted_set

- 新的存储需求：根据排序有利于数据的有效显示，需要提供一种可以根据自身特征进行排序的方式
- 需要的存储结构：新的存储模型，可以保存可排序的数据
- sorted_set类型：在set的存储结构基础上添加可排序字段

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652961455767.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652961455767.png)

### sorted_set类型数据的基本操作

- 添加数据

```
SHELL
zadd key score1 member1 [score2 member2]
```

- 获取全部数据

```
SHELL
zrange key start stop [WITHSCORES]
zrevrange key start stop [WITHSCORES]
```

- 删除数据

```
SHELL
zrem key member [member …]
```

- 按条件获取数据

```
SHELL
zrangebyscore key min max [WITHSCORES] [LIMIT]
zrevrangebyscore key max min [WITHSCORES]
```

- 条件删除

```
SHELL
zremrangebyrank key start stop
zremrangebyscore key min max
```

**注意**：

- `min`与`max`用于限定搜索查询的条件
- `start`与`stop`用于限定查询范围，作用于索引，表示开始和结束索引
- `offset`与`count`用于限定查询范围，作用于查询结果，表示`开始位置`和`数据总量`
- 获取集合数据总量

```
SHELL
zcard key
zcount key min max
```

- 集合交、并操作

```
SHELL
zinterstore destination numkeys key [key …]
zunionstore destination numkeys key [key …]
```

计算给定的一个或多个有序集的交集，其中给定 `key` 的数量必须以 `numkeys` 参数指定，并将该交集(结果集)储存到 `destination` 。

### sorted_set 类型数据的扩展操作

**业务场景**

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652965719209.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-16652965719209.png)

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529658190511.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529658190511.png)

**解决方案**

- 获取数据对应的索引（排名）

```
SHELL
zrank key member
zrevrank key member
```

- score 值获取与修改

```
PLAINTEXT
zscore key member
zincrby key increment member
```

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529756430013.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529756430013.png)

Tips 13:

- `redis `应用于计数器组合排序功能对应的排名

### sorted_set 类型数据操作的注意事项

- score 保存的数据存储空间是`64`位，如果是整数范围是
- score保存的数据也可以是一个双精度的`double`值，基于双精度浮点数的特征，可能会丢失精度，使用时侯要慎重
- sorted_set底层存储还是基于set结构的，因此数据不能重复，如果重复添加相同的数据，`score`值将被反复覆盖，保留最后一次修改的结果

**业务场景**

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/20200420131157293.png)](https://weishao-996.github.io/img/黑马程序员-Redis/20200420131157293.png)

解决方案

- 对于基于时间线限定的任务处理，将处理时间记录位`score`值，利用排序功能区分处理的先后顺序
- 记录下一个要处理的事件，当到期后处理对应的任务，移除`redis`中的记录，并记录下一个要处理的时间
- 当新任务加入时，判定并更新当前下一个要处理的任务时间
- 为提升`sorted_set`的性能，通常将任务根据特征存储成若干个`sorted_set`.例如1小时内，1天内，年度等，操作时逐渐提升，将即将操作的若干个任务纳入到1小时内处理队列中
- 获取当前系统时间

```
SHELL
time
```

Tips 14

- `redis`应用于定时任务执行顺序管理或任务过期管理

### **业务场景**

任务/消息权重设定应用

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/20200420132321704.png)](https://weishao-996.github.io/img/黑马程序员-Redis/20200420132321704.png)

**解决方案**

对于带有权重的任务，优先处理权重高的任务，采用`score`记录权重即可

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529806574817.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529806574817.png)

[![img](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529807350419.png)](https://weishao-996.github.io/img/黑马程序员-Redis/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FtcW0zMw==,size_16,color_FFFFFF,t_70-166529807350419.png)

- Tips 15:
- `redis`应用于即时任务/消息队列执行管理

# Redis 通用指令

## key通用指令

### **key 特征**

key是一个字符串，通过`key`获取`redis`中保存的数据

**key应该设计哪些操作？**

- 对于key自身状态的相关操作，例如：删除，判定存在，获取类型等
- 对于key有效性控制相关操作，例如：有效期设定，判定是否有效，有效状态的切换等
- 对于key快速查询操作，例如：按指定策略查询key
- ……

### key 基本操作

#### **删除指定key**

```
SHELL
del key
POWERSHELL
127.0.0.1:6379> del string
(integer) 1
127.0.0.1:6379> get string
(nil)
```

#### **获取key是否存在**

```
POWERSHELL
exists key
POWERSHELL
127.0.0.1:6379> exists list1
(integer) 1
```

#### **获取key的类型**

```
SHELL
type key
POWERSHELL
127.0.0.1:6379> type list1
list
```

### key 扩展操作（时效性控制）

#### **为指定key设置有效期**

```
SHELL
expire key seconds
pexpire key milliseconds
expireat key timestamp
pexpireat key milliseconds-timestamp
POWERSHELL
127.0.0.1:6379> expire name 10
(integer) 1
127.0.0.1:6379> get name
"weiwshao"
127.0.0.1:6379> get name
(nil)
POWERSHELL
127.0.0.1:6379> expireat name 1665310700
(integer) 1
127.0.0.1:6379> get name
"weishao"
127.0.0.1:6379> time
1) "1665310693"
2) "637763"
127.0.0.1:6379> get name
"weishao"
127.0.0.1:6379> get name
(nil)
```

`EXPIREAT` 的作用和 `EXPIRE` 类似，都用于为 `key` 设置生存时间。不同在于 `EXPIREAT` 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。

#### **获取key的有效时间**

```
POWERSHELL
ttl key
pttl key
POWERSHELL
127.0.0.1:6379> set name weishao
OK
127.0.0.1:6379> ttl name
(integer) -1
127.0.0.1:6379> expire name 10
(integer) 1
127.0.0.1:6379> ttl name
(integer) 7
127.0.0.1:6379> ttl name
(integer) 3
127.0.0.1:6379>
POWERSHELL
127.0.0.1:6379> pttl name
(integer) -2
127.0.0.1:6379> set name weishao
OK
127.0.0.1:6379> pttl name
(integer) -1
127.0.0.1:6379> expire name 10
(integer) 1
127.0.0.1:6379> pttl name
(integer) 7061
127.0.0.1:6379> pttl name
(integer) -2
127.0.0.1:6379>
```

- 当 `key` 不存在时，返回 `-2` 。
- 当 `key` 存在但没有设置剩余生存时间时，返回 `-1` 。
- 否则，以毫秒为单位，返回 `key` 的剩余生存时间。

#### **切换key从时效性转换为永久性**

```
SHELL
persist key
POWERSHELL
127.0.0.1:6379> expire name 10
(integer) 1
127.0.0.1:6379> ttl name
(integer) 7
127.0.0.1:6379> persist name
(integer) 1
127.0.0.1:6379> ttl name
(integer) -1
```

### key 扩展操作（查询模式）

#### **查询key**

```
SHELL
keys pattern
```

**查询模式规则**

```
SHELL
* 匹配任意数量的任意符号 ? 配合一个任意符号 [] 匹配一个指定符号
keys * 查询所有
keys it* 查询所有以it开头
keys *heima 查询所有以heima结尾
keys ??heima 查询所有前面两个字符任意，后面以heima结尾
keys user:? 查询所有以user:开头，最后一个字符任意
keys u[st]er:1 查询所有以u开头，以er:1结尾，中间包含一个字母，s或t
```

#### **key 其他操作**

##### 为key改名

```
PLAINTEXT
rename key newkey
renamenx key newkey
POWERSHELL
127.0.0.1:6379> set name weishao
OK
127.0.0.1:6379> rename name name2
OK
127.0.0.1:6379>
127.0.0.1:6379> get name2
"weishao"
POWERSHELL
127.0.0.1:6379> get name2
"weishao"
127.0.0.1:6379> set name3 123
OK
127.0.0.1:6379> renamenx name2 name3
(integer) 0
```

当且仅当 `newkey` 不存在时，将 `key` 改名为 `newkey` 。

当 `key` 不存在时，返回一个错误。

修改成功时，返回 `1` ； 如果 `newkey` 已经存在，返回 `0` 。

##### 对所有key排序

```
POWERSHELL
sort
PLAINTEXT
127.0.0.1:6379> lrange list2 0 -1
1) "8"
2) "6"
3) "2"
4) "4"
5) "3"
6) "6"
127.0.0.1:6379> sort list2
1) "2"
2) "3"
3) "4"
4) "6"
5) "6"
6) "8"
```

##### 其他key通用操作

```
POWERSHELL
help @generic
```

## 数据库通用指令

### **数据库**

#### **key 的重复问题**

- `key`是由程序员定义的
- `redis`在使用过程中，伴随着操作数据量的增加，会出现大量的数据以及对应的`key`
- 数据不区分种类、类别混杂在一起，极易出现重复或冲突

#### **解决方案**

- redis为每个服务提供有16个数据库，编号从0到15
- 每个数据库之间的数据相互独立

[![image-20221009204434585](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221009204434585.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221009204434585.png)

### db 基本操作

#### 切换数据库

```
POWERSHELL
select index
POWERSHELL
127.0.0.1:6379> select 1
OK
127.0.0.1:6379[1]>
```

#### 其他操作

```
POWERSHELL
quit
ping
echo message
POWERSHELL
127.0.0.1:6379> ping
PONG
```

使用客户端向 Redis 服务器发送一个 `PING` ，如果服务器运作正常的话，会返回一个 `PONG` 。通常用于测试与服务器的连接是否仍然生效，或者用于测量延迟值。

```
PLAINTEXT
127.0.0.1:6379> echo 123
"123"
127.0.0.1:6379>
```

打印一个特定的信息 `message` ，测试时使用。

### db 相关操作

#### 数据移动

```
PLAINTEXT
move key db
POWERSHELL
127.0.0.1:6379> move name2 1
(integer) 1
127.0.0.1:6379> exists name2
(integer) 0
127.0.0.1:6379> select 1
OK
127.0.0.1:6379[1]> keys *
1) "name2"
```

#### 数据清除

```
POWERSHELL
dbsize
flushdb
flushall
POWERSHELL
127.0.0.1:6379[1]> dbsize
(integer) 1
```

返回当前数据库的 key 的数量。

```
POWERSHELL
127.0.0.1:6379> dbsize
(integer) 16
127.0.0.1:6379> flushdb
OK
127.0.0.1:6379> dbsize
(integer) 0
```

清空当前数据库中的所有 key。此命令从不失败。

```
POWERSHELL
127.0.0.1:6379> dbsize
(integer) 0
127.0.0.1:6379> select 1
OK
127.0.0.1:6379[1]> dbsize
(integer) 1
127.0.0.1:6379[1]> select 0
OK
127.0.0.1:6379> flushall
OK
127.0.0.1:6379> select 1
OK
127.0.0.1:6379[1]> dbsize
(integer) 0
```

清空整个 `Redis` 服务器的数据(删除所有数据库的所有` key` )。此命令从不失败。

# Jedis

## Jedis简介

### 编程语言与redis

```
Java`语言连接`redis`服务 `Jedis
```

[![image-20221009210540279](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221009210540279.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221009210540279.png)

- Java语言连接redis服务
  - Jedis
  - SpringData Redis
  - Lettuce
- C 、C++ 、C# 、Erlang、Lua 、Objective-C 、Perl 、PHP 、Python 、Ruby 、Scala
- 可视化连接redis客户端
  - Redis Desktop Manager
  - Redis Client
  - Redis Studi

## HelloWorld（Jedis版）

### 准备工作

- **jar包导入**

下载地址：https://mvnrepository.com/artifact/redis.clients/jedis

- **基于maven**

```
XML
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
```

### 客户端连接redis

#### **连接redis**

```
JAVA
Jedis jedis = new Jedis("localhost", 6379);
```

#### **操作redis**

```
JAVA
jedis.set("name", "itheima");
jedis.get("name");
```

#### **关闭redis连接**

```
JAVA
jedis.close();
```

### Jedis读写redis数据

#### **案例：服务调用次数控制**

人工智能领域的语义识别与自动对话将是未来服务业机器人应答呼叫体系中的重要技术，百度自研用户评 价语义识别服务，免费开放给企业试用，同时训练百度自己的模型。现对试用用户的使用行为进行限速， 限制每个用户每分钟最多发起10次调用

- **案例要求**
  - ① 设定A、B、C三个用户
  - ② A用户限制10次/分调用，B用户限制30次/分调用，C用户不限制

#### **案例：需求分析**

- ① 设定一个服务方法，用于模拟实际业务调用的服务，内部采用打印模拟调用
- ② 在业务调用前服务调用控制单元，内部使用redis进行控制，参照之前的方案
- ③ 对调用超限使用异常进行控制，异常处理设定为打印提示信息
- ④ 主程序启动3个线程，分别表示3种不同用户的调用

#### **案例：实现步骤**

1. **设定业务方法**

```
JAVA
void business(String id,long num){
 System.out.println("用户"+id+"发起业务调用，当前第"+num+"次");
}
```

1. **设定多线类，模拟用户调用**

```
JAVA
public void run(){
     while(true){
     	jd.service(id);
     	//模拟调用间隔，设定为1.x秒
         try{
             Random r = new Random();
             Thread.sleep(1000+ r.nextInt(200));
         }catch (InterruptedException e){
             e.printStackTrace();;
         }
     }
}
```

1. **设计redis控制方案**

```
JAVA
void service(String id){
     Jedis jedis = new Jedis("localhost", 6379);
     String value = jedis.get("compid:" + id);
     //判定是否具有调用计数控制，利用异常进行控制处理
     if(value == null) {
         //没有控制，创建控制计数器
         jedis.setex("compid:" + id, 20, ""+(Long.MAX_VALUE-10));
         }else{
         //有控制，自增，并调用业务
         try{
         	Long val = jedis.incr("compid:"+id);
         	business(id,10+val-Long.MAX_VALUE);
         }catch (JedisDataException e){
         //调用次数溢出，弹出提示
         System.out.println("用户："+id+"使用次数已达到上限，请稍后再试，或升级VIP会员");
         return;
     }finally{
     	jedis.close();
     	}
     }
}
```

1. **设计启动主程序**

```
JAVA
public static void main(String[] args) {
     MyThread t1 = new MyThread("初级用户");
     t1.start();
}
```

后续1：对业务控制方案进行改造，设定不同用户等级的判定

后续2：将不同用户等级对应的信息、限制次数等设定到redis中，使用hash保存

## 基于连接池获取连接

JedisPool：Jedis提供的连接池技术

- `poolConfig`:连接池配置对象
- `host`:redis服务地址
- `port`:redis服务端口号

```
JAVA
public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port) {
 this(poolConfig, host, port, 2000, (String)null, 0, (String)null);
}
```

### 封装连接参数

- **jedis.properties**

```
PROPERTIES
jedis.host=localhost
jedis.port=6379
jedis.maxTotal=30
jedis.maxIdle=10
```

### 加载配置信息

- 静态代码块初始化资源

```
JAVA
static{
 //读取配置文件 获得参数值
 ResourceBundle rb = ResourceBundle.getBundle("jedis");
 host = rb.getString("jedis.host");
 port = Integer.parseInt(rb.getString("jedis.port"));
 maxTotal = Integer.parseInt(rb.getString("jedis.maxTotal"));
 maxIdle = Integer.parseInt(rb.getString("jedis.maxIdle"));
 poolConfig = new JedisPoolConfig();
 poolConfig.setMaxTotal(maxTotal);
 poolConfig.setMaxIdle(maxIdle);
 jedisPool = new JedisPool(poolConfig,host,port);
}
```

### 获取连接

- 对外访问接口，提供`jedis`连接对象，连接从连接池获取

```
JAVA
public static Jedis getJedis(){
     Jedis jedis = jedisPool.getResource();
     return jedis;
}
```

## 可视化客户端

[![image-20221010115536757](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010115536757.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010115536757.png)

# Redis 安装

## 基于Linux环境安装Redis

- ### **下载安装包**

```
SHELL
wget http://download.redis.io/releases/redis-?.?.?.tar.gz
```

[![image-20221010120957375](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010120957375.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010120957375.png)

- **解压**

```
SHELL
tar –xvf 文件名.tar.gz
```

[![image-20221010121604452](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010121604452.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010121604452.png)

- **编译**

```
SHELL
make
```

[![image-20221010122116412](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010122116412.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010122116412.png)

- **安装**

```
SHELL
make install [destdir=/目录]
```

[![image-20221010122310424](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010122310424.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010122310424.png)

## 指定端口启动服务

```
SHELL
redis-server –-port 6380
```

[![image-20221010123927631](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010123927631.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010123927631.png)

[![image-20221010124244936](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010124244936.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010124244936.png)

## 指定配置文件启动

**删除配置文件 注释信息和无用空白并保存至新的配置文件`redis-6379`文件中**

```
SHELL
cat redis.conf | grep -v "#" | grep -v "^$" > redis-6379.conf
```

[![image-20221010125149349](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010125149349.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010125149349.png)

**新建日志保存位置，并获取路径**

[![image-20221010130449881](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010130449881.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010130449881.png)

**修改配置`redis-6379`文件保存必要配置**

**基本配置**

- daemonize yes
  - 以守护进程方式启动，使用本启动方式，redis将以服务的形式存在，日志将不再打印到命令窗口中
- port 6
  - 设定当前服务启动端口号
- dir “/自定义目录/redis/data“
  - 设定当前服务文件保存位置，包含日志文件、持久化文件（后面详细讲解）等
- logfile 6***.log
  - 设定日志文件名，便于查阅

[![image-20221010130013766](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010130013766.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010130013766.png)

**指定配置文件`redis-6379.conf`启动**

```
SHELL
redis-server redis-6379.conf 
```

[![image-20221010130712193](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010130712193.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010130712193.png)

**查看redis是否启动完成**

```
SHELL
ps -ef | grep redis-
```

[![image-20221010130827016](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010130827016.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010130827016.png)

## 配置文件启动目录管理

**创建配置目录，并将配置文件放入目录内**

```
PLAINTEXT
[root@hecs-33111 redis-7.0.5]# mkdir conf
[root@hecs-33111 redis-7.0.5]# mv redis-6379.conf conf
```

**以指定目录下的配置及文件启动redis**

```
SHELL
[root@hecs-33111 redis-7.0.5]# redis-server conf/redis-6379.conf
```

**同时启动两个不同端口的redis**

- **复制配置文件并修改**

```
PLAINTEXT
cp redis-6379.conf redis-6380.conf
```

[![image-20221010164820275](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010164820275.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010164820275.png)

- **同时启动两个服务**

```
SHELL
[root@hecs-33111 redis-7.0.5]# redis-server conf/redis-6380.conf
[root@hecs-33111 redis-7.0.5]# redis-server conf/redis-6379.conf
```

[![image-20221010165103534](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010165103534.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010165103534.png)

# Redis 持久化

**什么是持久化**

利用永久性存储介质将数据进行保存，在特定的时间将保存的数据进行恢复的工作机制称为持久化。

**为什么要进行持久化**

防止数据的意外丢失，确保数据安全性

**持久化过程保存什么**

- 将当前数据状态进行保存，快照形式，存储数据结果，存储格式简单，关注点在数据
- 将数据的操作过程进行保存，日志形式，存储操作过程，存储格式复杂，关注点在数据的操作过程

## RDB

### RDB启动方式 —— save指令

**命令**

```
SHELL
save
```

作用 手动执行一次保存操作

**演示**

```
POWERSHELL
[root@hecs-33111 redis-7.0.5]# redis-cli -p 6379
127.0.0.1:6379> 
127.0.0.1:6379> 
127.0.0.1:6379> 
127.0.0.1:6379> save
OK
```

[![image-20221010171723018](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010171723018.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010171723018.png)

- **添加数据观察rdb文件变化**

```
POWERSHELL
127.0.0.1:6379> set name weishao
OK
127.0.0.1:6379> save
OK
```

[![image-20221010172032344](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010172032344.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010172032344.png)

#### RDB启动方式 —— save指令相关配置

- dbfilename dump.rdb
  - 说明：设置本地数据库文件名，默认值为 dump.rdb
  - 经验：通常设置为`dump-端口号.rdb`
- dir
  - 说明：设置存储.rdb文件的路径
  - 经验：通常设置成存储空间较大的目录中，目录名称`data`
- rdbcompression yes
  - 说明：设置存储至本地数据库时是否压缩数据，默认为` yes`，采用 `LZF` 压缩
  - 经验：通常默认为开启状态，如果设置为no，可以节省 CPU 运行时间，但会使存储的文件变大（巨大）
- rdbchecksum yes
  - 说明：设置是否进行`RDB`文件格式校验，该校验过程在写文件和读文件过程均进行
  - 经验：通常默认为开启状态，如果设置为`no`，可以节约读写性过程约`10%`时间消耗，但是存储一定的数据损坏风险

**演示**

- **修改配置文件**

[![image-20221010211312492](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010211312492.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010211312492.png)

- **添加数据并save,观察rdb文件是否生成**

```
SHELL
127.0.0.1:6379> set name weishao
OK
127.0.0.1:6379> save
OK
```

[![image-20221010211455930](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010211455930.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010211455930.png)

**数据恢复演示**

- **关闭redis进程**

[![image-20221010211925344](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010211925344.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010211925344.png)

[![image-20221010212001633](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010212001633.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010212001633.png)

- **启动redis，观察是否有数据**

```
PLAINTEXT
[root@hecs-33111 redis-7.0.5]# redis-server conf/redis-6379.conf 
```

[![image-20221010212159279](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010212159279.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010212159279.png)

- **关闭前的数据存在，持久化生效**

#### RDB启动方式 —— save指令工作原理

* save指令执行会阻塞当前redis服务器，直到RDB过程完成为止，有可能造成长时间时间阻塞，线上环境不建议使用

**RDB启动方式**

**数据量过大，单线程执行方式造成效率过低如何处理？**

后台执行

- 谁：redis操作者（用户）发起指令；redis服务器控制指令执行
- 什么时间：即时（发起）；合理的时间（执行）
- 干什么事情：保存数据

### RDB启动方式 —— bgsave指令

- 命令

```
SHELL
bgsave
```

- 作用

 手动启动后台保存操作，但不是立即执行

**演示**

```
SHELL
127.0.0.1:6379> keys *
1) "name"
127.0.0.1:6379> set age 25
OK
127.0.0.1:6379> get age
"25"
127.0.0.1:6379> bgsave
Background saving started
```

[![image-20221010215027206](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221010215027206.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221010215027206.png)

#### RDB启动方式 —— bgsave指令工作原理

* bgsave是针对save阻塞问题的优化，redis内部所有涉及到rdb操作都采用bgsave方式，save命令可以放弃使用

#### RDB启动方式 —— bgsave指令相关配置

- dbfilename dump.rdb
- dir
- rdbcompression yes
- rdbchecksum yes
- **stop-writes-on-bgsave-error yes**
  - 说明：后台存储过程中如果出现错误现象，是否停止保存操作
  - 经验：通常默认为开启状态

**RDB启动方式**

反复执行保存指令，忘记了怎么办？不知道数据产生了多少变化，何时保存？

**自动执行**

- 谁：redis服务器发起指令（基于条件）
- 什么时间：满足条件
- 干什么事情：保存数据

### RDB启动方式 ——save配置

- **配置**

```
SHELL
save second changes
```

- **作用**

满足限定时间范围内`key`的变化数量达到指定数量即进行持久化

- **参数**
  - second：监控时间范围
  - changes：监控key的变化量
- **位置**
  - 在conf文件中进行配置
- **范例**

```
SHELL
save 900 1
save 300 10
save 60 10000
```

**演示**

修改配置文件，并重新以配置文件启动

[![image-20221011141609019](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221011141609019.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221011141609019.png)

添加数据观察`rdb`文件变化

[![image-20221011142127323](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221011142127323.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221011142127323.png)

[![image-20221011142306626](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221011142306626.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221011142306626.png)

#### RDB启动方式 ——save配置原理

* save配置要根据实际业务情况进行设置，频度过高或者过低都会出现性能问题，结果可能是灾难性的
* save配置中队伍second与changes设置通常具有互补对应关系，尽量不要设置成包含性关系
* save配置启动后执行的是bgsave操作

### RDB三种启动方式对比

| 方式           | save指令 | bgsave指令 |
| -------------- | -------- | ---------- |
| 读写           | 同步     | 异步       |
| 阻塞客户端指令 | 是       | 否         |
| 额外内存消耗   | 否       | 是         |
| 启动新进程     | 否       | 是         |

### RDB特殊启动形式

- **全量复制**

 在主从复制中详细讲解

- **服务器运行过程中重启**

```
SHELL
debug reload
```

- **关闭服务器时指定保存数据**

```
SHELL
shutdown save
```

默认情况下执行`shutdown`命令时，自动执行 `bgsave`(如果没有开启`AOF`持久化功能)

### RDB优点与缺点

#### RDB优点

- `RDB`是一个紧凑压缩的二进制文件，存储效率较高
- `RDB`内部存储的是`redis`在某个`时间点`的数据快照，非常适合用于数据备份，全量复制等场景
- `RDB`恢复数据的速度要比`AOF`快很多
- 应用：服务器中每`X`小时执行`bgsave`备份，并将`RDB`文件拷贝到远程机器中，用于灾难恢复。

#### RDB缺点

- `RDB`方式无论是执行指令还是利用配置，无法做到实时持久化，具有较大的可能性丢失数据
- `bgsave`指令每次运行要执行`fork`操作创建子进程，要牺牲掉一些性能
- `Redis`的众多版本中未进行`RDB`文件格式的版本统一，有可能出现各版本服务之间数据格式无法兼容现象

## AOF

### RDB存储的弊端

- 存储数据量较大，效率较低 基于快照思想，每次读写都是全部数据，当数据量巨大时，效率非常低
- 大数据量下的`IO`性能较低
- 基于`fork`创建子进程，内存产生额外消耗
- 宕机带来的数据丢失风险

#### 解决思路

- 不写全数据，仅记录部分数据
- 降低区分数据是否改变的难度，改记录数据为记录操作过程
- 对所有操作均进行记录，排除丢失数据的风险

### AOF概念

- `AOF`(append only file)持久化：以独立日志的方式记录每次写命令，重启时再重新执行`AOF`文件中命令 达到恢复数据的目的。与`RDB`相比可以简单描述为改记录数据为记录数据产生的过程
- `AOF`的主要作用是解决了数据持久化的实时性，目前已经是`Redis`持久化的主流方式

### AOF写数据过程

* set->缓存->aof写命令刷新缓存区->将命令同步到aof文件中

### AOF写数据三种策略(appendfsync)

**always(每次）**

 每次写入操作均同步到`AOF`文件中，`数据零误差，性能较低`

**everysec（每秒）**

 每秒将缓冲区中的指令同步到`AOF`文件中，数据`准确性较高，性能较高`

 在系统突然宕机的情况下丢失`1`秒内的数据

**no（系统控制）**

 由操作系统控制每次同步到`AOF`文件的周期，整体过程`不可控`

### AOF相关配置

**配置**

```
SHELL
appendfilename filename
```

**作用**

```
AOF`持久化文件名，默认文件名未`appendonly.aof`，建议配置为`appendonly-端口号.aof
```

**配置**

```
SHELL
dir
```

**作用**

 `AOF`持久化文件保存路径，与`RDB`持久化文件保持一致即可

**演示**

**修改配置文件，并重启服务**

[![image-20221011164116974](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221011164116974.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221011164116974.png)

**添加数据**

```
SHELL
127.0.0.1:6379> set age 55
OK
```

[![image-20221012100201807](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221012100201807.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221012100201807.png)



### AOF重写

随着命令不断写入`AOF`，文件会越来越大，为了解决这个问题，`Redis`引入了`AOF`重写机制压缩文件体积。`AOF`文件重 写是将`Redis`进程内的数据转化为写命令同步到新`AOF`文件的过程。简单说就是将对同一个数据的若干个条命令执行结 果转化成最终结果数据对应的指令进行记录。

#### AOF重写作用

- 降低磁盘占用量，提高磁盘利用率
- 提高持久化效率，降低持久化写时间，提高IO性能
- 降低数据恢复用时，提高数据恢复效

#### AOF重写规则

- 进程内已超时的数据不再写入文件
- 忽略无效指令，重写时使用进程内数据直接生成，这样新的AOF文件只保留最终数据的写入命令 如`del key1、 hdel key2、srem key3、set key4 111、set key4 222`等
- 对同一数据的多条写命令合并为一条命令 如`lpush list1 a、lpush list1 b、 lpush list1 c` 可以转化为：`lpush list1 a b c`。 为防止数据量过大造成客户端缓冲区溢出，对`list、set、hash、zset`等类型，每条指令最多写入`64`个元素

#### 重写方式

- 手动重写

```
SHELL
bgrewriteaof
```

- 自动重写

```
SHELL
auto-aof-rewrite-min-size size
auto-aof-rewrite-percentage percentage
```

#### 演示

- 添加数据，观察aof文件变化

```
SHELL
127.0.0.1:6379> set name 111
OK
127.0.0.1:6379> set name 222
OK
```

[![image-20221012100448914](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221012100448914.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221012100448914.png)

- 手动重写

```
POWERSHELL
127.0.0.1:6379> BGREWRITEAOF
Background append only file rewriting started
```

[![image-20221012100544751](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221012100544751.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221012100544751.png)

#### AOF手动重写 —— bgrewriteaof指令工作原理

* bgrewiteaof->发送指令->调用fork函数生成子进程->创建rdb文件->重写aof文件
* 返回消息

#### AOF自动重写方式

- 自动重写触发条件设置

```
SHELL
auto-aof-rewrite-min-size size
auto-aof-rewrite-percentage percent
```

- 自动重写触发比对参数（ 运行指令info Persistence获取具体信息 ）

```
SHELL
aof_current_size
aof_base_size
```

- 自动重写触发条件

[![image-20221012102721225](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221012102721225.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221012102721225.png)

## RDB VS AOF

| 持久化方式   | RDB                | AOF                |
| ------------ | ------------------ | ------------------ |
| 占用存储空间 | 小（数据集：压缩） | 大（指令级：重写） |
| 存储速度     | 慢                 | 快                 |
| 恢复速度     | 快                 | 慢                 |
| 数据安全性   | 会丢失数据         | 依据策略决定       |
| 资源消耗     | 高/重量级          | 低/轻量级          |
| 启动优先级   | 低                 | 高                 |

## RDB与AOF的选择之惑

- 对数据非常敏感，建议使用默认的AOF持久化方案
  -  `AOF`持久化策略使用`everysecond`，每秒钟`fsync`一次。该策略`redis`仍可以保持很好的处理性能，当出 现问题时，最多丢失`0-1`秒内的数据。
  -  注意：由于`AOF`文件存储体积较大，且恢复速度较慢
- 数据呈现阶段有效性，建议使用RDB持久化方案
  -  数据可以良好的做到阶段内无丢失（该阶段是开发者或运维人员手工维护的），且恢复速度较快，阶段 点数据恢复通常采用`RDB`方案
  -  注意：利用`RDB`实现紧凑的数据持久化会使`Redis`降的很低，慎重总结：
- 综合比对
  -  `RDB`与`AOF`的选择实际上是在做一种权衡，每种都有利有弊
  -  如不能承受数分钟以内的数据丢失，对业务数据非常敏感，选用`AOF`
  -  如能承受数分钟以内的数据丢失，且追求大数据集的恢复速度，选用`RDB`
  -  灾难恢复选用`RDB`
  -  双保险策略，同时开启 `RDB` 和` AOF`，重启后，`Redis`优先使用 `AOF` 来恢复数据，降低丢失数据的量

## 持久化应用场景

* 抢购，限购类，限量发放优惠券，激活码等业务的数据存储设计
* 具有先后操作顺序的数据控制
* 最新消息展示
* 基于黑名单与白名单设定的服务控制
* 计数器组合排序功能对应的排名

# Redis 事务

**什么是事务**

Redis执行指令过程中，多条连续执行的指令被干扰，打断，插队

redis事务就是一个命令执行的队列，将一系列预定义命令包装成一个整体（一个队列）。当执行时，一次性 按照添加顺序依次执行，中间不会被打断或者干扰。

一个队列中，一次性、顺序性、排他性的执行一系列命令

[![image-20221012161628529](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221012161628529.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221012161628529.png)

## 事务基本操作

- **开启事务**

```
SHELL
multi
```

- **作用**

 设定事务的开启位置，此指令执行后，后续的所有指令均加入到事务中

- **执行事务**

```
SHELL
exec
```

**注意：加入事务的命令暂时进入到任务队列中，并没有立即执行，只有执行`exec`命令才开始执行**

**演示**

```
POWERSHELL
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set name 111
QUEUED
127.0.0.1:6379> get name 
QUEUED
127.0.0.1:6379> set name 222
QUEUED
127.0.0.1:6379> get name
QUEUED
127.0.0.1:6379> EXEC
1) OK
2) "111"
3) OK
4) "222"
```

**事务定义过程中发现出了问题，怎么办？**

取消事务

```
SHELL
discard
```

作用

 终止当前事务的定义，发生在`multi`之后，`exec`之前

**演示**

```
POWERSHELL
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set name 111
QUEUED
127.0.0.1:6379> DISCARD
OK
127.0.0.1:6379> EXEC
(error) ERR EXEC without MULTI
127.0.0.1:6379> 
```

## 事务的工作流程

<img src="https://emptystudy-1320078852.cos.ap-nanjing.myqcloud.com/%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%8E%A5%E6%94%B6%E6%8C%87%E4%BB%A4.png" style="zoom:50%;" />

## 事务的注意事项

**定义事务的过程中，命令格式输入错误怎么办？**

- **语法错误**

 指命令书写格式有误

- **处理结果**

 如果定义的事务中所包含的命令存在语法错误，整体事务中所有命令均不会执行。包括那些语法正 确的命令。

**演示**

```
POWERSHELL
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> SET name 111
QUEUED
127.0.0.1:6379> TES name 222
(error) ERR unknown command 'TES'
127.0.0.1:6379> EXEC
(error) EXECABORT Transaction discarded because of previous errors.
```

**定义事务的过程中，命令执行出现错误怎么办？**

- **运行错误**

 指命令格式正确，但是无法正确的执行。例如对`list`进行`incr`操作

- **处理结果**

 能够正确运行的命令会执行，运行错误的命令不会被执行

**注意：已经执行完毕的命令对应的数据不会自动回滚，需要程序员自己在代码中实现回滚。**

**演示**

```
POWERSHELL
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set name 111
QUEUED
127.0.0.1:6379> get name 
QUEUED
127.0.0.1:6379> set name 222
QUEUED
127.0.0.1:6379> get name
QUEUED
127.0.0.1:6379> lpush name 1 2 3
QUEUED
127.0.0.1:6379> EXEC
1) OK
2) "111"
3) OK
4) "222"
5) (error) WRONGTYPE Operation against a key holding the wrong kind of value
```

## 手动进行事务回滚

- 记录操作过程中被影响的数据之前的状态
  -  单数据：string
  -  多数据：hash、list、set、zset
- 设置指令恢复所有的被修改的项
  -  单数据：直接set（注意周边属性，例如时效）
  -  多数据：修改对应值或整体克隆复制

# Redis 锁

## 基于特定条件的事务执行——锁

**业务场景**

天猫双11热卖过程中，对已经售罄的货物追加补货，4个业务员都有权限进行补货。补货的操作可能是一系 列的操作，牵扯到多个连续操作，如何保障不会重复操作？

**业务分析**

- 多个客户端有可能同时操作同一组数据，并且该数据一旦被操作修改后，将不适用于继续操作
- 在操作之前锁定要操作的数据，一旦发生变化，终止当前操作

**解决方案**

- 对 `key` 添加监视锁，在执行`exec`前如果`key`发生了变化，终止事务执行

```
SHELL
watch key1 [key2……]
```

- 取消对所有 key 的监视

```
SHELL
unwatch
```

Tips 18： `redis `应用基于状态控制的批量任务执行

**演示1.监控时 锁未被修改**

```
POWERSHELL
127.0.0.1:6379> FLUSHDB
OK
127.0.0.1:6379> 
127.0.0.1:6379> keys *
(empty list or set)
127.0.0.1:6379> 
127.0.0.1:6379> set name 123
OK
127.0.0.1:6379> set age 321
OK
127.0.0.1:6379> watch name
OK
127.0.0.1:6379> get name
"123"
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set aa bb
QUEUED
127.0.0.1:6379> get aa
QUEUED
127.0.0.1:6379> EXEC
1) OK
2) "bb"
```

**演示2.监控时 锁被修改**

**客户端1**

```
POWERSHELL
127.0.0.1:6379> watch name age
OK
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set aa cc
QUEUED
127.0.0.1:6379> get aa
QUEUED
```

**客户端2**

```
POWERSHELL
[root@hecs-33111 ~]# redis-cli -p 6379
127.0.0.1:6379> 
127.0.0.1:6379> 
127.0.0.1:6379> 
127.0.0.1:6379> set name 111
OK
```

**客户端1**

```
POWERSHELL
127.0.0.1:6379> 
127.0.0.1:6379> watch name age
OK
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> set aa cc
QUEUED
127.0.0.1:6379> get aa
QUEUED
127.0.0.1:6379> EXEC
(nil)
```

**演示3.事务必须在监视内部开启**

```
POWERSHELL
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> WATCH age
(error) ERR WATCH inside MULTI is not allowed
```

**演示4.取消锁**

```
POWERSHELL
127.0.0.1:6379> WATCH name
OK
127.0.0.1:6379> get name
"111"
127.0.0.1:6379> UNWATCH
OK
```

## 基于特定条件的事务执行——分布式锁

**业务场景**

天猫双11热卖过程中，对已经售罄的货物追加补货，且补货完成。客户购买热情高涨，3秒内将所有商品购 买完毕。本次补货已经将库存全部清空，如何避免最后一件商品不被多人同时购买？【超卖问题】

**业务分析**

- 使用`watch`监控一个`key`有没有改变已经不能解决问题，此处要监控的是具体数据
- 虽然`redis`是单线程的，但是多个客户端对同一数据同时进行操作时，如何避免不被同时修改？

**解决方案**

- 使用 setnx 设置一个公共锁

```
SHELL
setnx lock-key value
```

利用`setnx`命令的返回值特征，有值则返回设置失败，无值则返回设置成功

-  对于返回设置成功的，拥有控制权，进行下一步的具体业务操作
-  对于返回设置失败的，不具有控制权，排队或等待
  操作完毕通过del操作释放锁

注意：上述解决方案是一种设计概念，依赖规范保障，具有风险性

Tips 19： `redis` 应用基于分布式锁对应的场景控制

**演示**

**客户端1加锁并修改目标变量**

```
POWERSHELL
127.0.0.1:6379> set num 10
OK
127.0.0.1:6379> 
127.0.0.1:6379> SETNX lock-num 1
(integer) 1
127.0.0.1:6379> INCRBY num -1
(integer) 9
```

**客户端2也进行加锁，加锁失败**

```
POWERSHELL
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
```

**客户端1删除锁**

```
POWERSHELL
127.0.0.1:6379> set num 10
OK
127.0.0.1:6379> 
127.0.0.1:6379> SETNX lock-num 1
(integer) 1
127.0.0.1:6379> INCRBY num -1
(integer) 9
127.0.0.1:6379> DEL lock-num
(integer) 1
```

**客户端2加锁成功**

```
POWERSHELL
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 1
```

## 基于特定条件的事务执行——分布式锁改良

**业务场景**

依赖分布式锁的机制，某个用户操作时对应客户端宕机，且此时已经获取到锁。如何解决？

**业务分析**

- 由于锁操作由用户控制加锁解锁，必定会存在加锁后未解锁的风险
- 需要解锁操作不能仅依赖用户控制，系统级别要给出对应的保底处理方案

**解决方案**

- 使用 `expire` 为锁`key`添加时间限定，到时不释放，放弃锁

```
SHELL
expire lock-key second
pexpire lock-key milliseconds
```

由于操作通常都是微秒或毫秒级，因此该锁定时间不宜设置过大。具体时间需要业务测试后确认。

- 例如：持有锁的操作最长执行时间`127ms`，最短执行时间`7ms`。
- 测试百万次最长执行时间对应命令的最大耗时，测试百万次网络延迟平均耗时
- 锁时间设定推荐：最大耗时`*120%+平均网络延迟*110%`
- 如果`业务最大耗时<<网络平均延迟`，通常为`2个数量级`，取其中单个耗时较长即可

**演示**

**客户端1，加锁并设置过期时间**

```
POWERSHELL
127.0.0.1:6379> SETNX lock-num 1
(integer) 1
127.0.0.1:6379> EXPIRE lock-num 20
(integer) 1
```

**客户端2，尝试加锁**

```
POWERSHELL
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 0
127.0.0.1:6379> SETNX lock-num 1
(integer) 1
```

# Redis 删除策略

## Redis中的数据特征

- `Redis`是一种内存级数据库，所有数据均存放在内存中，内存中的数据可以通过`TTL`指令获取其状态
  - `XX` ：具有时效性的数据
  - `-1` ：永久有效的数据
  - `-2` ：已经过期的数据 或 被删除的数据 或 未定义的数据

**过期的数据真的删除了吗？**

## 数据删除策略

1. **定时删除**
2. **惰性删除**
3. **定期删除**

### 数据删除策略的目标

在内存占用与`CPU`占用之间寻找一种平衡，顾此失彼都会造成整体`redis`性能的下降，甚至引发`服务器宕机`或`内存泄露`

### 定时删除

- 创建一个定时器，当`key`设置有过期时间，且过期时间到达时，由定时器任务立即执行对键的删除操作
- 优点：节约内存，到时就删除，快速释放掉不必要的内存占用
- 缺点：`CPU`压力很大，无论`CPU`此时负载量多高，均占用`CPU`，会影响`redis`服务器响应时间和指令吞吐量
- 总结：用处理器性能换取存储空间（拿时间换空间）

### 惰性删除

- 数据到达过期时间，不做处理。等下次访问该数据时
  - 如果未过期，返回数据
  - 发现已过期，删除，返回不存在
- 优点：节约CPU性能，发现必须删除的时候才删除
- 缺点：内存压力很大，出现长期占用内存的数据
- 总结：用存储空间换取处理器性能 （拿时间换空间）

### 定期删除

**两种方案都走极端，有没有折中方案？**

- 周期性轮询`redis`库中的时效性数据，采用随机抽取的策略，利用过期数据占比的方式控制删除频度
- 特点1：`CPU`性能占用设置有峰值，检测频度可自定义设置
- 特点2：内存压力不是很大，长期占用内存的冷数据会被持续清理
- 总结：周期性抽查存储空间 `expireIfNeeded()` （随机抽查，重点抽查）

### 删除策略比对

[![image-20221013151547838](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221013151547838.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221013151547838.png)

## 逐出算法

### 新数据进入检测

当新数据进入redis时，如果内存不足怎么办？

- `Redis`使用内存存储数据，在执行每一个命令前，会调用`freeMemoryIfNeeded()`检测内存是否充足。如 果内存不满足新加入数据的最低存储要求，redis要临时删除一些数据为当前指令清理存储空间。清理数据 的策略称为逐出算法。
- 注意：逐出数据的过程不是`100%`能够清理出足够的可使用的内存空间，如果不成功则反复执行。当对所 有数据尝试完毕后，如果不能达到内存清理的要求，将出现错误信息。

[![image-20221014131638733](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221014131638733.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221014131638733.png)

### 影响数据逐出的相关配置

- 最大可使用内存

```
PLAINTEXT
maxmemory
```

占用物理内存的比例，默认值为0，表示不限制。生产环境中根据需求设定，通常设置在50%以上。

- 每次选取待删除数据的个数

```
PLAINTEXT
maxmemory-samples
```

选取数据时并不会全库扫描，导致严重的性能消耗，降低读写性能。因此采用随机获取数据的方式作为待检测删除数据

- 删除策略

```
SHELL
maxmemory-policy
```

达到最大内存后的，对被挑选出来的数据进行删除的策略

 检测易失数据（可能会过期的数据集`server.db[i].expires` ）

① `volatile-lru`：挑选最近最少使用的数据淘汰

② `volatile-lfu`：挑选最近使用次数最少的数据淘汰

③ `volatile-ttl`：挑选将要过期的数据淘汰

④ `volatile-random`：任意选择数据淘汰

[![image-20221014132248229](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221014132248229.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221014132248229.png)

 检测全库数据（所有数据集`server.db[i].dict `）

⑤ `allkeys-lru`：挑选最近最少使用的数据淘汰

⑥ `allkeys-lfu`：挑选最近使用次数最少的数据淘汰

⑦ `allkeys-random`：任意选择数据淘汰

 放弃数据驱逐

⑧ `no-enviction`（驱逐）：禁止驱逐数据（`redis4.0`中默认策略），会引发错误`OOM（Out Of Memory）`

**命令配置**

```
POWERSHELL
maxmemory-policy volatile-lru
```

### 数据逐出策略配置依据

使用`INFO`命令输出监控信息，查询缓存 `hit` 和 `miss` 的次数，根据业务需求调优`Redis`配置

```
POWERSHELL
127.0.0.1:6379> INFO
```

[![image-20221014133100016](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221014133100016.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221014133100016.png)

# Redis 核心配置

## 服务器端设定

- 设置服务器以守护进程的方式运行

```
POWERSHELL
daemonize yes|no
```

- 绑定主机地址

```
POWERSHELL
bind 127.0.0.1
```

- 设置服务器端口号

```
POWERSHELL
port 6379
```

- 设置数据库数量

```
POWERSHELL
databases 16
```

## 日志配置

- 设置服务器以指定日志记录级别

```
POWERSHELL
loglevel debug|verbose|notice|warning
```

- 日志记录文件名

```
SHELL
logfile 端口号.log
```

注意：日志级别开发期设置为`verbose`即可，生产环境中配置为`notice`，简化日志输出量，降低写日志IO的频度

## 客户端配置

- 设置同一时间最大客户端连接数，默认无限制。当客户端连接到达上限，Redis会关闭新的连接

```
POWERSHELL
maxclients 0
```

- 客户端闲置等待最大时长，达到最大值后关闭连接。如需关闭该功能，设置为 0

```
POWERSHELL
timeout 300
```

## 多服务器快捷配置

导入并加载指定配置文件信息，用于快速创建`redis`公共配置较多的`redis`实例配置文件，便于维护

```
SHELL
include /path/server-端口号.conf
```

# Redis 高级数据类型

## Bitmaps

### Bitmaps类型的基础操作

- 获取指定key对应偏移量上的bit值

```
POWERSHELL
getbit key offset
```

- 设置指定key对应偏移量上的bit值，value只能是1或0

```
SHELL
setbit key offset value
```

**演示**

```
POWERSHELL
127.0.0.1:6379> SETBIT bits 0 1
(integer) 0
127.0.0.1:6379> GETBIT bits 0
(integer) 1
127.0.0.1:6379> GETBIT bits 10
(integer) 0
```

### Bitmaps类型的扩展操作

**业务场景**

**电影网站**

- 统计每天某一部电影是否被点播
- 统计每天有多少部电影被点播
- 统计每周/月/年有多少部电影被点播
- 统计年度哪部电影没有被点播

**业务分析**

[![image-20221014140142843](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221014140142843.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221014140142843.png)

- 对指定key按位进行交、并、非、异或操作，并将结果保存到destKey中

```
PLAINTEXT
bitop op destKey key1 [key2...]
```

- and：交
- or：并
- not：非
- xor：异或
- 统计指定key中1的数量

```
SHELL
bitcount key [start end]
```

Tips 21： `redis` 应用于信息状态统计

**演示**

```
POWERSHELL
127.0.0.1:6379> SETBIT 20880808 0 1
(integer) 0
127.0.0.1:6379> SETBIT 20880808 4 1
(integer) 0
127.0.0.1:6379> SETBIT 20880808 8 1
(integer) 0
127.0.0.1:6379> SETBIT 20880809 0 1
(integer) 0
127.0.0.1:6379> SETBIT 20880809 5 1
(integer) 0
127.0.0.1:6379> SETBIT 20880809 8 1
(integer) 0
127.0.0.1:6379> BITCOUNT 20880808
(integer) 3
127.0.0.1:6379> BITCOUNT 20880809
(integer) 3
127.0.0.1:6379> SETBIT  20880808 6 1
(integer) 0
127.0.0.1:6379> BITCOUNT 20880808
(integer) 4
127.0.0.1:6379> BITOP or 08-09 20880808 20880809
(integer) 2
127.0.0.1:6379> BITCOUNT 08-09
(integer) 5
```

## HyperLogLog

### 统计独立UV

- 原始方案：set
  - 存储每个用户的id（字符串）
- 改进方案：Bitmaps
  - 存储每个用户状态（bit）
- 全新的方案：Hyperloglog

### 基数

- 基数是数据集去重后元素个数
- `HyperLogLog` 是用来做基数统计的，运用了`LogLog`的算法

### HyperLogLog类型的基本操作

- 添加数据

```
SHELL
pfadd key element [element ...]
```

- 统计数据

```
SHELL
pfcount key [key ...]
```

- 合并数据

```
SHELL
pfmerge destkey sourcekey [sourcekey...]
```

Tips 22： redis 应用于独立信息统计

```
SHELL
127.0.0.1:6379> PFADD hll 001
(integer) 1
127.0.0.1:6379> PFADD hll 001
(integer) 0
127.0.0.1:6379> PFADD hll 001
(integer) 0
127.0.0.1:6379> PFADD hll 001
(integer) 0
127.0.0.1:6379> PFADD hll 001
(integer) 0
127.0.0.1:6379> PFADD hll 002
(integer) 1
127.0.0.1:6379> GET hll
"HYLL\x01\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x80L\x9f\x84R\xbf\x80`\x9d"
127.0.0.1:6379> PFCOUNT hll
(integer) 2
SHELL
redis> PFADD  nosql  "Redis"  "MongoDB"  "Memcached"
(integer) 1
redis> PFADD  RDBMS  "MySQL" "MSSQL" "PostgreSQL"
(integer) 1
redis> PFMERGE  databases  nosql  RDBMS
OK
redis> PFCOUNT  databases
(integer) 6
```

### 相关说明

- 用于进行基数统计，不是集合，不保存数据，只记录数量而不是具体数据
- 核心是基数估算算法，最终数值**存在一定误差**
- 误差范围：基数估计的结果是一个带有 `0.81%` 标准错误的近似值
- 耗空间极小，每个`hyperloglog key`占用了`12K`的内存用于标记基数
- `pfadd`命令不是一次性分配`12K`内存使用，会随着基数的增加内存逐渐增大
- `Pfmerge`命令合并后占用的存储空间为`12K`，无论合并之前数据量多少

## GEO

### GEO类型的基本操作

- **添加坐标点**

```
SHELL
geoadd key longitude latitude member [longitude latitude member ...]
```

- **获取坐标点**

```
SHELL
geopos key member [member ...]
```

- **计算坐标点距离**

```
SHELL
geodist key member1 member2 [unit]
```

**演示**

```
POWERSHELL
127.0.0.1:6379> GEOADD geos 1 1 a
(integer) 1
127.0.0.1:6379> GEOADD geos 2 2 b
(integer) 1
127.0.0.1:6379> GEOPOS geos a
1) 1) "0.99999994039535522"
   2) "0.99999945914297683"
127.0.0.1:6379> GEODIST geos a b
"157270.0561"
127.0.0.1:6379> GEODIST geos a b m
"157270.0561"
127.0.0.1:6379> GEODIST geos a b km
"157.2701"
```

- **根据坐标范围内的数据**

```
SHELL
georadius key longitude latitude radius m|km|ft|mi [withcoord] [withdist] [withhash] [count count]
```

- **根据点求范围内数据**

```
SHELL
georadiusbymember key member radius m|km|ft|mi [withcoord] [withdist] [withhash] [count count]
```

- **计算经纬度**

```
SHELL
geohash key member [member ...]
```

**演示**

```
POWERSHELL
127.0.0.1:6379> GEODIST geos a b
"157270.0561"
127.0.0.1:6379> GEODIST geos a b m
"157270.0561"
127.0.0.1:6379> GEODIST geos a b km
"157.2701"
127.0.0.1:6379> GEOADD geos 1 1 1,1
(integer) 1
127.0.0.1:6379> GEOADD geos 1 2 1,2
(integer) 1
127.0.0.1:6379> GEOADD geos 1 3 1,3
(integer) 1
127.0.0.1:6379> GEOADD geos 2 1 2,1
(integer) 1
127.0.0.1:6379> GEOADD geos 2 2 2,2
(integer) 1
127.0.0.1:6379> GEOADD geos 2 3 2,3
(integer) 1
127.0.0.1:6379> GEOADD geos 3 1 3,1
(integer) 1
127.0.0.1:6379> GEOADD geos 3 2 3,2
(integer) 1
127.0.0.1:6379> GEOADD geos 3 3 3,3
(integer) 1
127.0.0.1:6379> GEOADD geos 5 5 5,5
(integer) 1
127.0.0.1:6379> GEORADIUSBYMEMBER geos 2,2 180 km
 1) "1,1"
 2) "a"
 3) "2,1"
 4) "1,2"
 5) "2,2"
 6) "b"
 7) "3,1"
 8) "3,2"
 9) "1,3"
10) "2,3"
11) "3,3"
127.0.0.1:6379> GEORADIUSBYMEMBER geos 2,2 120 km
1) "1,2"
2) "2,2"
3) "b"
4) "2,3"
5) "2,1"
6) "3,2"
127.0.0.1:6379> GEORADIUSBYMEMBER geos 2,2 1800 km
 1) "1,1"
 2) "a"
 3) "2,1"
 4) "1,2"
 5) "2,2"
 6) "b"
 7) "3,1"
 8) "3,2"
 9) "1,3"
10) "2,3"
11) "3,3"
12) "5,5"
POWERSHELL
127.0.0.1:6379> GEORADIUS geos 1.5 1.5 90 km
1) "1,2"
2) "2,2"
3) "b"
4) "1,1"
5) "a"
6) "2,1"
POWERSHELL
127.0.0.1:6379> GEOHASH geos 2,2
1) "s037ms06g70"
```

Tips 23： redis 应用于地理位置计算

# Redis 主从复制

## 主从复制简介

**互联网“三高”架构**

- **高并发**
- **高性能**
- **高可用**

### **你的“Redis”是否高可用**

单机`redis`的风险与问题

- 问题1.机器故障
  - 现象：硬盘故障、系统崩溃
  - 本质：数据丢失，很可能对业务造成灾难性打击
  - 结论：基本上会放弃使用`redis`.
- 问题2.容量瓶颈
  - 现象：内存不足，从`16G`升级到`64G`，从`64G`升级到`128G`，无限升级内存
  - 本质：穷，硬件条件跟不上
  - 结论：放弃使用`redis`
- 结论：
  -  为了避免单点`Redis`服务器故障，准备多台服务器，互相连通。将数据复制多个副本保存在不同的服 务器上，**连接在一起**，并保证数据是**同步**的。即使有其中一台服务器宕机，其他服务器依然可以继续 提供服务，实现`Redis`的高可用，同时实现数据**冗余备份**。

### 多台服务器连接方案

- 提供数据方：master
  - 主服务器，主节点，主库
  - 主客户端
- 接收数据方：slave
  - 从服务器，从节点，从库
  - 从客户端
- 需要解决的问题：
  - 数据同步
- 核心工作：
  - master的数据复制到slave中

### 主从复制

主从复制即将`master`中的数据即时、有效的复制到`slave`中

特征：一个`master`可以拥有多个`slave`，一个`slave`只对应一个`master`

职责：

- master

  :

  - 写数据
  - 执行写操作时，将出现变化的数据自动同步到`slave`
  - 读数据（可忽略）

- slave

  :

  - 读数据
  - 写数据（禁止）

### 主从复制的作用

- 读写分离：`master`写、`slave`读，提高服务器的读写负载能力
- 负载均衡：基于主从结构，配合读写分离，由`slave`分担`master`负载，并根据需求的变化，改变`slave`的数 量，通过多个从节点分担数据读取负载，大大提高`Redis`服务器并发量与数据吞吐量
- 故障恢复：当`master`出现问题时，由`slave`提供服务，实现快速的故障恢复
- 数据冗余：实现数据热备份，是持久化之外的一种数据冗余方式
- 高可用基石：基于主从复制，构建哨兵模式与集群，实现`Redis`的高可用方案

## 主从复制工作流程

- 主从复制过程大体可以分为3个阶段
  - **建立连接阶段（即准备阶段）**
  - **数据同步阶段**
  - **命令传播阶段**

### 阶段一：建立连接阶段

建立`slave`到`master`的连接，使`master`能够识别`slave`，并保存`slave`端口号

#### 建立连接阶段工作流程

1. 设置master地址端口，保存master信息
2. 建立socket连接
3. 发送ping命令
4. 身份验证
5. 发送slave端口信息

#### 主从连接（slave连接master）

- **方式一：客户端发送命令**

```
POWERSHELL
slaveof <masterip> <masterport>
```

- **方式二：启动服务器参数**

```
POWERSHELL
redis-server -slaveof <masterip> <masterport>
```

- **方式三：服务器配置**

```
POWERSHELL
slaveof <masterip> <masterport>
```

- `slave`系统信息
  - master_link_down_since_seconds
  - masterhost
  - masterport
- `master`系统信息
  - slave_listening_port(多个)

**演示**

- **修改配置文件关闭守护进程和日志**

**redis-6379.conf**

```
PROPERTIES
port 6379
daemonize no
#logfile "6379.log"
dir /root/redis-4.0.0/data
dbfilename dump-6379.rdb
rdbcompression yes
rdbchecksum yes
save 10 2
appendonly yes
appendfsync always
appendfilename appendonly-6379.aof
bind 127.0.0.1
databases 16
```

**redis-6380.conf**

```
PROPERTIES
port 6380
daemonize no
#logfile "6380.log"
dir /root/redis-4.0.0/data
```

- **启动多个redis服务**

[![image-20221017202520833](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017202520833.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017202520833.png)

[![image-20221017202628618](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017202628618.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017202628618.png)

- **slave服务器连接master服务器**

```
SHELL
[root@hecs-33111 ~]# redis-cli -p 6380
127.0.0.1:6380> 
127.0.0.1:6380> 
127.0.0.1:6380> SLAVEOF 127.0.0.1 6379
OK
```

[![image-20221017203447084](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017203447084.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017203447084.png)

**观察服务器连接信息**

[![image-20221017203710563](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017203710563.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017203710563.png)

[![image-20221017203741205](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017203741205.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017203741205.png)

**同步效果测试**

[![image-20221017204007153](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017204007153.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017204007153.png)

[![image-20221017204029344](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017204029344.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017204029344.png)

**第二种连接方式(启动时连接)**

```
SHELL
[root@hecs-33111 redis-4.0.0]# redis-server conf/redis-6380.conf --slaveof 127.0.0.1 6379
```

**第三种连接方式（配置文件连接）**

```
PROPERTIES
port 6380
daemonize no
#logfile "6380.log"
dir /root/redis-4.0.0/data
slaveof 127.0.0.1 6379
```

**查看信息**

```
PLAINTEXT
info
```

[![image-20221017204901267](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017204901267.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017204901267.png)

[![image-20221017204924813](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017204924813.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017204924813.png)

#### 主从断开连接

- 客户端发送命令

```
SHELL
slaveof no one
```

说明：

-  `slave`断开连接后，不会删除已有数据，只是不再接受`master`发送的数据

#### 授权访问

- `master`客户端发送命令设置密码

```
SHELL
requirepass <password>
```

- `master`配置文件设置密码

```
SHELL
config set requirepass <password>
config get requirepass
```

- `slave`客户端发送命令设置密码

```
SHELL
auth <password>
```

- slave配置文件设置密码

```
SHELL
masterauth <password>
```

- slave启动服务器设置密码

```
SHELL
redis-server –a <password>
```

### 阶段二：数据同步阶段工作流程

- 在`slave`初次连接`master`后，复制`master`中的所有数据到`slave`
- 将`slave`的数据库状态更新成`master`当前的数据库状态

数据同步阶段工作流程

1. 请求同步数据
2. 创建RDB同步数据
3. 恢复RDB同步数据
4. 请求部分同步数据
5. 恢复部分同步数据

#### 数据同步阶段master说明

1. 如果`master`数据量巨大，数据同步阶段应避开流量高峰期，避免造成`master`阻塞，影响业务正常执行
2. 复制缓冲区大小设定不合理，会导致数据溢出。如进行全量复制周期太长，进行部分复制时发现数据已 经存在丢失的情况，必须进行第二次全量复制，致使`slave`陷入死循环状态。

```
PROPERTIES
repl-backlog-size 1mb
```

1. `master`单机内存占用主机内存的比例不应过大，建议使用`50%-70%`的内存，留下`30%-50%`的内存用于执 行`bgsave`命令和创建复制缓冲区

#### 数据同步阶段slave说明

1. 为避免`slave`进行全量复制、部分复制时服务器响应阻塞或数据不同步，建议关闭此期间的对外服务

```
SHELL
slave-serve-stale-data yes|no
```

1. 数据同步阶段，`master`发送给`slave`信息可以理解`master`是`slave`的一个客户端，主动向`slave`发送 命令
2. 多个`slave`同时对`master`请求数据同步，`master`发送的`RDB`文件增多，会对带宽造成巨大冲击，如果 `master`带宽不足，因此数据同步需要根据业务需求，适量错峰
3. `slave`过多时，建议调整拓扑结构，由一主多从结构变为树状结构，中间的节点既是`master`，也是 `slave`。注意使用树状结构时，由于层级深度，导致深度越高的`slave`与最顶层`master`间数据同步延迟 较大，数据一致性变差，应谨慎选择

### 阶段三：命令传播阶段

- 当`master`数据库状态被修改后，导致主从服务器数据库状态不一致，此时需要让主从数据同步到一致的 状态，同步的动作称为命令传播
- `master`将接收到的数据变更命令发送给`slave`，`slave`接收命令后执行命令
- 主从复制过程大体可以分为3个阶段
  - **建立连接阶段（即准备阶段）**
  - **数据同步阶段**
  - **命令传播阶段**

#### 命令传播阶段的部分复制

* 命令传播截断出现断网现象
  * 网络闪断闪连	忽略
  * 短时间网络中断     部分复制
  * 长时间网络中断      全量复制
* 部分复制的三个核心要素
  * 服务器的运行id（run id)
  * 主从服务器的复制积压缓冲区
  * 主从服务器的复制偏移量

#### 服务器运行ID（runid）

- 概念：服务器运行ID是每一台服务器每次运行的身份识别码，一台服务器多次运行可以生成多个运行id
- 组成：运行id由40位字符组成，是一个随机的十六进制字符 例如：fdc9ff13b9bbaab28db42b3d50f852bb5e3fcdce
- 作用：运行id被用于在服务器间进行传输，识别身份 如果想两次操作均对同一台服务器进行，必须每次操作携带对应的运行id，用于对方识别
- 实现方式：运行id在每台服务器启动时自动生成的，master在首次连接slave时，会将自己的运行ID发 送给slave，slave保存此ID，通过info Server命令，可以查看节点的runid

[![image-20221017214743888](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221017214743888.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221017214743888.png)

#### 复制缓冲区

概念：复制缓冲区，又名复制积压缓冲区，是一个先进先出`（FIFO）`的队列，用于存储服务器执行过的命 令，每次传播命令，`master`都会将传播的命令记录下来，并存储在复制缓冲区

#### 复制缓冲区内部工作原理

* 组成
  * 偏移量
  * 字节值
* 工作原理
  * 通过offset区别不同的slave当前数据传播的差异
  * master记录已发送的信息对应的offset
  * slave记录已接受的信息对应的offset

- 概念：复制缓冲区，又名复制积压缓冲区，是一个先进先出

  ```
  （FIFO）
  ```

  的队列，用于存储服务器执行过的命 令，每次传播命令，

  ```
  master
  ```

  都会将传播的命令记录下来，并存储在复制缓冲区

  - 复制缓冲区默认数据存储空间大小是`1M`，由于存储空间大小是固定的，当入队元素的数量大于队 列长度时，最先入队的元素会被弹出，而新元素会被放入队列

- 由来：每台服务器启动时，如果开启有`AOF`或被连接成为`master`节点，即创建复制缓冲区

- 作用：用于保存`master`收到的所有指令（仅影响数据变更的指令，例如`set，select`）

- 数据来源：当`master`接收到主客户端的指令时，除了将指令执行，会将该指令存储到缓冲区中

#### 主从服务器复制偏移量（offset）

- 概念：一个数字，描述复制缓冲区中的指令字节位置
- 分类：
  - `master`复制偏移量：记录发送给所有`slave`的指令字节对应的位置（多个）
  - `slave`复制偏移量：记录`slave`接收`master`发送过来的指令字节对应的位置（一个）
- 数据来源：
  - `master`端：发送一次记录一次
  - `slave`端：接收一次记录一次
- 作用：同步信息，比对`master`与`slave`的差异，当`slave`断线后，恢复数据使用

#### 数据同步+命令传播阶段工作流程

（ps: 图片转自黑马）

[![image-20221018165949891](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018165949891.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018165949891.png)

#### 心跳机制

- 进入命令传播阶段候，`master`与`slave`间需要进行信息交换，使用心跳机制进行维护，实现双方连接保持在线

- ```
  master
  ```

  心跳：

  - 指令：PING
  - 周期：由`repl-ping-slave-period`决定，默认10秒
  - 作用：判断`slave`是否在线
  - 查询：INFO replication 获取slave最后一次连接时间间隔，`lag`项维持在0或1视为正常

- slave心跳任务

  - 指令：REPLCONF ACK {offset}
  - 周期：1秒
  - 作用1：汇报`slave`自己的复制偏移量，获取最新的数据变更指令
  - 作用2：判断`master`是否在线

#### 心跳阶段注意事项

- 当`slave`多数掉线，或延迟过高时，`master`为保障数据稳定性，将拒绝所有信息同步操作

```
PROPERTIES
min-slaves-to-write 2
min-slaves-max-lag 8
```

`slave`数量少于2个，或者所有`slave`的延迟都大于等于10秒时，强制关闭`master`写功能，停止数据同步

- `slave`数量由`slave`发送`REPLCONF ACK`命令做确认
- `slave`延迟由`slave`发送`REPLCONF ACK`命令做确认

#### 主从复制工作流程（完整）

(ps: 图片转自黑马)

[![image-20221018171711366](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018171711366.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018171711366.png)

## 主从复制常见问题

### 频繁的全量复制（1）

伴随着系统的运行，`master`的数据量会越来越大，一旦`master`重启，`runid`将发生变化，会导致全部`slave`的 全量复制操作

内部优化调整方案：

1. `master`内部创建`master_replid`变量，使用`runid`相同的策略生成，长度`41`位，并发送给所有slave

2. 在

   ```
   master
   ```

   关闭时执行命令

    

   ```
   shutdown save
   ```

   ，进行

   ```
   RDB
   ```

   持久化,将

   ```
   runid
   ```

   与

   ```
   offset
   ```

   保存到RDB文件中

   - repl-id repl-offset
   - 通过`redis-check-rdb`命令可以查看该信息

3. ```
   master
   ```

   重启后加载

   ```
   RDB
   ```

   文件，恢复数据

   - 重启后，将

     ```
     RDB
     ```

     文件中保存的

     ```
     repl-id
     ```

     与

     ```
     repl-offset
     ```

     加载到内存中

     - master_repl_id = repl master_repl_offset = repl-offset
     - 通过`info`命令可以查看该信息

作用： 本机保存上次`runid`，重启后恢复该值，使所有`slave`认为还是之前的`master`

### 频繁的全量复制（2）

- 问题现象
  - 网络环境不佳，出现网络中断，`slave`不提供服务
- 问题原因
  - 复制缓冲区过小，断网后`slave`的`offset`越界，触发全量复制
- 最终结果
  - `slave`反复进行全量复制
- 解决方案
  - 修改复制缓冲区大小

```
PROPERTIES
repl-backlog-size
```

- 建议设置如下

1. 测算从`master`到`slave`的重连平均时长`second`
2. 获取`master`平均每秒产生写命令数据总量`write_size_per_second`
3. 最优复制缓冲区空间 = `2 * second * write_size_per_second`

### 频繁的网络中断（1）

- 问题现象
  - `master`的`CPU`占用过高 或 `slave`频繁断开连接
- 问题原因
  - `slave`每1秒发送`REPLCONF ACK`命令到`master`
  - 当`slave`接到了慢查询时（`keys *` ，`hgetall`等），会大量占用`CPU`性能
  - `master`每1秒调用复制定时函数r`eplicationCron()`，比对`slave`发现长时间没有进行响应
- 最终结果
  - `master`各种资源（输出缓冲区、带宽、连接等）被严重占用
- 解决方案
  - 通过设置合理的超时时间，确认是否释放`slave`

```
PROPERTIES
repl-timeout
```

该参数定义了超时时间的阈值（默认`60秒`），超过该值，释放`slave`

### 频繁的网络中断（2）

- 问题现象
  - `slave`与`master`连接断开
- 问题原因
  - `master`发送`ping`指令频度较低
  - `master`设定超时时间较短
  - `ping`指令在网络中存在丢包
- 解决方案
  - 提高`ping`指令发送的频度

```
PROPERTIES
repl-ping-slave-period
```

超时时间`repl-time`的时间至少是`ping`指令频度的5到10倍，否则`slave`很容易判定超时

### 数据不一致

- 问题现象
  - 多个`slave`获取相同数据不同步
- 问题原因
  - 网络信息不同步，数据发送有延迟
- 解决方案
  - 优化主从间的网络环境，通常放置在同一个机房部署，如使用阿里云等云服务器时要注意此现象
  - 监控主从节点延迟（通过`offset`）判断，如果`slave`延迟过大，暂时屏蔽程序对该`slave`的数据访问

```
PROPERTIES
slave-serve-stale-data yes|no
```

开启后仅响应`info`、`slaveof`等少数命令（慎用，除非对数据一致性要求很高）

# **Redis 哨兵模式**

## 哨兵简介

* 主机”宕机“
  * 关闭master和所有slave
  * 找一个slave作为master
  * 修改其他slave配置，连接新的主
  * 启动新的master与slave
  * 全量复制*N+部分复制*N
* 关闭期间的数据服务谁来接？
* 找一个主？怎么找？
* 修改配置后，原始的主恢复了怎么办

**哨兵**

哨兵(sentinel) 是一个分布式系统，用于对主从结构中的每台服务器进行**监控**，当出现故障时通过投票机制**选择**新的 `master`并将所有`slave`连接到新的`master`。

**哨兵的作用**

- 监控
  - 不断的检查`master`和`slave`是否正常运行。
  - `master`存活检测、`master`与`slave`运行情况检测
- 通知（提醒）
  - 当被监控的服务器出现问题时，向其他（哨兵间，客户端）发送通知。
- 自动故障转移
  - 断开`master`与`slave`连接，选取一个`slave`作为`master`，将其他`slave`连接到新的`master`，并告知客户端新的服 务器地址

注意： 哨兵也是一台`redis`服务器，只是不提供数据服务 通常哨兵配置数量为单数

## 配置哨兵

- 配置一拖二的主从结构
- 配置三个哨兵（配置相同，端口不同）
  - 参看`sentinel.conf`
- 启动哨兵

```
PROPERTIES
redis-sentinel sentinel-端口号.conf
```

**演示**

[![image-20221018211141094](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018211141094.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018211141094.png)

**观察文件信息**

```
PROPERTIES
[root@hecs-33111 redis-4.0.0]# cat sentinel.conf | grep -v "#" | grep -v "^$"
port 26379
dir /tmp
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
```

**将配置文件拷贝一份至指定文件夹下**

```
SHELL
cat sentinel.conf | grep -v "#" | grep -v "^$" > ./conf/sentinel-26379.conf
```

[![image-20221018211919137](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018211919137.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018211919137.png)

**修改配置文件**

[![image-20221018212228270](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018212228270.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018212228270.png)

**再拷贝两份哨兵的配置文件**

```
SHELL
[root@hecs-33111 conf]# sed 's/26379/26380/g' sentinel-26379.conf > sentinel-26380.conf 
[root@hecs-33111 conf]# sed 's/26379/26381/g' sentinel-26379.conf > sentinel-26381.conf 
[root@hecs-33111 conf]# cat sentinel-26380.conf 
port 26380
dir /root/redis-4.0.0/data
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
```

**再拷贝一份slave的配置**

```
SHELL
[root@hecs-33111 conf]# sed 's/6380/6381/g' redis-6380.conf > redis-6381.conf
[root@hecs-33111 conf]# cat redis-6381.conf 
port 6381
daemonize no
#logfile "6381.log"
dir /root/redis-4.0.0/data
slaveof 127.0.0.1 6379
```

**清空data文件夹中的所有信息**

```
SHELL
[root@hecs-33111 data]# rm -rf *
```

**启动master**

[![image-20221018213515776](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018213515776.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018213515776.png)

**启动两个slave**

[![image-20221018213813008](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018213813008.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018213813008.png)

[![image-20221018213903729](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018213903729.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018213903729.png)

**启动哨兵1**

```
SHELL
redis-sentinel  conf/sentinel-26379.conf 
```

[![image-20221018214102909](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018214102909.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018214102909.png)

**客户端连接哨兵1观察信息**

```
SHELL
[root@hecs-33111 /]# redis-cli -p 26379
127.0.0.1:26379> info
```

[![image-20221018214557474](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018214557474.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018214557474.png)

**观察配置文件 已经发生变化**

[![image-20221018214924564](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018214924564.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018214924564.png)

**启动哨兵2**

[![image-20221018215154820](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018215154820.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018215154820.png)

**观察到哨兵1的相关信息**

**观察哨兵1的日志信息可以观察到哨兵2的信息**

[![image-20221018215320585](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018215320585.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018215320585.png)

**观察现有两个哨兵的配置文件信息**

[![image-20221018215520080](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018215520080.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018215520080.png)

**启动哨兵3**

[![image-20221018215630899](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018215630899.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018215630899.png)

**观察到识别出两个哨兵信息**

**观察哨兵1的配置文件 可以查看到新增哨兵信息**

[![image-20221018215802938](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221018215802938.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221018215802938.png)

**验证master与两个slave的连通性**

```
SHELL
127.0.0.1:6379> set name weishao
OK
SHELL
127.0.0.1:6380> get name
"weishao"
SHELL
127.0.0.1:6381> get name
"weishao"
```

**CTRL+C使得master宕机**

[![image-20221019151305171](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019151305171.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019151305171.png)

**观察哨兵1的信息**

[![image-20221019151729961](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019151729961.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019151729961.png)

[![image-20221019151925079](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019151925079.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019151925079.png)

## 工作原理

### **主从切换**

- 哨兵在进行主从切换过程中经历三个阶段
  - 监控
  - 通知
  - 故障转移

### 阶段一：监控阶段

* 用于同步各个节点的状态消息
  * 获取各个sentinel的状态（是否在线）
  * 获取master的状态
    * master属性
      * runid
      * role:master
    * 各个slave的详细信息
  * 获取所有slave的状态（根据master中的slave信息）
    * slave属性
      * runid
      * role: slave
      * master_host,master_port
      * offset
      * ......



(PS:一下图片均转自黑马)

[![image-20221019202524994](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019202524994.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019202524994.png)

### 阶段二：通知阶段

[![image-20221019202725003](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019202725003.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019202725003.png)

### 阶段三：故障转移阶段

[![image-20221019203036948](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019203036948.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019203036948.png)

**选择领头的sentine进行清理，选举过程**

[![image-20221019203813171](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019203813171.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019203813171.png)

**领头的sentinel选择新的slave作为master**

[![image-20221019204608998](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019204608998.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019204608998.png)

### 总结

- 监控
  - 同步信息
- 通知
  - 保持联通
- 故障转移
  - 发现问题
  - 竞选负责人
  - 优选新master
  - 新`master`上任，其他`slave`切换`master`，原`master`作为`slave`故障回复后连接

# Redis 集群

## 集群简介

**现状问题**

**业务发展过程中遇到的峰值瓶颈**

- `redis`提供的服务`OPS`可以达到10万/秒，当前业务OPS已经达到10万/秒
- 内存单机容量达到`256G`，当前业务需求内存容量`1T`
- 使用集群的方式可以快速解决上述问题

**集群架构**

集群就是使用网络将若干台计算机联通起来，并提供统一的管理方式，使其对外呈现单机的服务效果

**集群作用**

- 分散单台服务器的访问压力，实现负载均衡
- 分散单台服务器的存储压力，实现可扩展性
- 降低单台服务器宕机带来的业务灾难

## Redis集群结构设计

### 数据存储设计

* 通过算法设计，计算出ket应该保存的位置
* 将所有的存储空间切割成16384份，每台主机保存一部分，每份代表一个储存空间，不是key的保存空间
* 将key按照计算出的结果放到对应的存储空间

[![image-20221019211257396](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019211257396.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019211257396.png)

### 集群内部通讯设计（转自黑马）

[![image-20221019211557357](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019211557357.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019211557357.png)

## cluster集群搭建

### 搭建方式

- 原生安装（单条命令）
  - 配置服务器（3主3从）
  - 建立通信（Meet）
  - 分槽（Slot）
  - 搭建主从（master-slave）
- 工具安装（批处理）

### Cluster配置

- 添加节点

  ```
  PROPERTIES
  cluster-enabled yes|no
  ```

- cluster配置文件名，该文件属于自动生成，仅用于快速查找文件并查询文件内容

  ```
  PROPERTIES
  cluster-config-file <filename>
  ```

- 节点服务响应超时时间，用于判定该节点是否下线或切换为从节点

  ```
  PROPERTIES
  cluster-node-timeout <milliseconds>
  ```

- master连接的slave最小数量

  ```
  PROPERTIES
  cluster-migration-barrier <count>
  ```

### Cluster节点操作命令

- 查看集群节点信息

  ```
  SHELL
  cluster nodes
  ```

- 进入一个从节点 redis，切换其主节点

  ```
  SHELL
  cluster replicate <master-id>
  ```

- 发现一个新节点，新增主节点

  ```
  SHELL
  cluster meet ip:port
  ```

- 忽略一个没有solt的节点

  ```
  SHELL
  cluster forget <id>
  ```

- 手动故障转移

  ```
  SHELL
  cluster failover
  ```

### redis-trib命令

- 添加节点

  ```
  SHELL
  redis-trib.rb add-node
  ```

- 删除节点

  ```
  SHELL
  redis-trib.rb del-node
  ```

- 重新分片

  ```
  SHELL
  redis-trib.rb reshard
  ```

### **演示**

#### 搭建

**清空data**

```
SHELL
[root@hecs-33111 data]# rm -rf *
```

**配置redis-6379集群的配置**

[![image-20221019212827549](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019212827549.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019212827549.png)

**复制配置文件为6份**

```
SHELL
[root@hecs-33111 conf]# sed 's/6379/6380/g' redis-6379.conf >redis-6380.conf
[root@hecs-33111 conf]# sed 's/6379/6381/g' redis-6379.conf >redis-6381.conf
[root@hecs-33111 conf]# sed 's/6379/6382/g' redis-6379.conf >redis-6382.conf
[root@hecs-33111 conf]# sed 's/6379/6383/g' redis-6379.conf >redis-6383.conf
[root@hecs-33111 conf]# sed 's/6379/6384/g' redis-6379.conf >redis-6384.conf
[root@hecs-33111 conf]# sed 's/6379/6385/g' redis-6379.conf >redis-6385.conf
```

**根据配置文件依次启动redis-server**

```
SHELL
[root@hecs-33111 redis-4.0.0]# redis-server conf/redis-6379.conf 
```

[![image-20221019213943319](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019213943319.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019213943319.png)

**观察服务已经全部启动**

[![image-20221019214204996](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019214204996.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019214204996.png)

**观察启动命令**

[![image-20221019215041069](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019215041069.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019215041069.png)

执行此命令需要安装`ruby`和`ruby gem`

**安装ruby**

```
SHELL
sudo yum install ruby
```

**安装ruby gem**

```
PLAINTEXT
yum install rubygems -y
```

**查看ruby和ruby gem安装版本**

```
SHELL
[root@hecs-33111 src]# ruby -v
ruby 2.5.9p229 (2021-04-05 revision 67939) [x86_64-linux]
[root@hecs-33111 src]# gem -v
2.7.6.3
```

**启动集群**

```
SHELL
[root@hecs-33111 src]# ./redis-trib.rb create --replicas 1 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384
```

**报错**

[![image-20221019221124005](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019221124005.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019221124005.png)

**解决方案**

```
SHELL
[root@hecs-33111 src]# gem install redis
```

[![image-20221019221255502](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221019221255502.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221019221255502.png)

**观察到data目录下已经生成了一些配置文件**

[![image-20221020103923017](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020103923017.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020103923017.png)

**观察6379的节点信息**

[![image-20221020104041099](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020104041099.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020104041099.png)

**覆盖配置**

[![image-20221020104442095](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020104442095.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020104442095.png)

**节点开始加入**

[![image-20221020104543307](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020104543307.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020104543307.png)

**观察6379配置文件变化**

[![image-20221020104914459](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020104914459.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020104914459.png)

**观察6379此时的日志信息**

[![image-20221020105340354](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020105340354.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020105340354.png)

#### **设置与获取数据**

**连接redis集群**

```
SHELL
[root@hecs-33111 src]# redis-cli -c
127.0.0.1:6379> 
```

**设置数据**

**根据key计算出槽位，并且重定向至槽位**

[![image-20221020105938279](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020105938279.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020105938279.png)

**连接集群内指定位置的redis服务器**

```
SHELL
redis-cli -c -p 6382
SHELL
[root@hecs-33111 src]# redis-cli -c -p 6382
127.0.0.1:6382> 
127.0.0.1:6382> 
127.0.0.1:6382> 
127.0.0.1:6382> get name
-> Redirected to slot [5798] located at 127.0.0.1:6380
"itheima"
```

#### **主从下线与主从切换**

**关闭slave1 观察master1日志信息**

[![image-20221020110725445](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020110725445.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020110725445.png)

**其余redis服务器显示的信息**

[![image-20221020110909594](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020110909594.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020110909594.png)

**将slave1恢复连接观察master1的信息**

[![image-20221020111001703](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020111001703.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020111001703.png)

**关闭master1，观察slave1日志信息**

[![image-20221020111147479](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020111147479.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020111147479.png)

**slave1替代master1成为新的master**

[![image-20221020111637321](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020111637321.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020111637321.png)

**查看集群节点信息**

```
SHELL
127.0.0.1:6380> CLUSTER NODES
```

[![image-20221020112004426](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020112004426.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020112004426.png)

**恢复6379，观察日志信息**

[![image-20221020113758577](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020113758577.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020113758577.png)

# Redis 企业级解决方案

## 缓存预热

**“宕机”**

服务器启动后迅速宕机

**问题排查**

1. 请求数量较高
2. 主从之间数据吞吐量较大，数据同步操作频度较高

**解决方案**

**前置准备工作**：

1. 日常例行统计数据访问记录，统计访问频度较高的热点数据

2. 利用LRU数据删除策略，构建数据留存队列 例如：storm与kafka配合

   **准备工作**：

   1. 将统计结果中的数据分类，根据级别，redis优先加载级别较高的热点数据

   2. 利用分布式多服务器同时进行数据读取，提速数据加载过程

   3. 热点数据主从同时预热

      **实施**：

      1. 使用脚本程序固定触发数据预热过程
      2. 如果条件允许，使用了CDN（内容分发网络），效果会更好

**总结**

缓存预热就是系统启动前，提前将相关的缓存数据直接加载到缓存系统。避免在用户请求的时候，先查询数据库，然后再将数据缓 存的问题！用户直接查询事先被预热的缓存数据！

## 缓存雪崩

**数据库服务器崩溃（1）**

1. 系统平稳运行过程中，忽然数据库连接量激增
2. 应用服务器无法及时处理请求
3. 大量408，500错误页面出现
4. 客户反复刷新页面获取数据
5. 数据库崩溃
6. 应用服务器崩溃
7. 重启应用服务器无效
8. Redis服务器崩溃
9. Redis集群崩溃
10. 重启数据库后再次被瞬间流量放倒

**问题排查**

1. 在一个较短的时间内，缓存中较多的key集中过期
2. 此周期内请求访问过期的数据，redis未命中，redis向数据库获取数据
3. 数据库同时接收到大量的请求无法及时处理
4. Redis大量请求被积压，开始出现超时现象
5. 数据库流量激增，数据库崩溃
6. 重启后仍然面对缓存中无数据可用
7. Redis服务器资源被严重占用，Redis服务器崩溃
8. Redis集群呈现崩塌，集群瓦解
9. 应用服务器无法及时得到数据响应请求，来自客户端的请求数量越来越多，应用服务器崩溃
10. 应用服务器，redis，数据库全部重启，效果不理想

**问题分析**

- 短时间范围内
- 大量key集中过期

**解决方案（道）**

1. **更多的页面静态化处理**

2. **构建多级缓存架构**

    Nginx缓存+redis缓存+ehcache缓存

3. **检测Mysql严重耗时业务进行优化**

    对数据库的瓶颈排查：例如超时查询、耗时较高事务等

4. **灾难预警机制**

    监控redis服务器性能指标

   - CPU占用、CPU使用率
   - 内存容量

   - 查询平均响应时间

   - 线程数

5. **限流、降级**

 短时间范围内牺牲一些客户体验，限制一部分请求访问，降低应用服务器压力，待业务低速运转后再逐步放开访问

**解决方案（术）**

1. **LRU与LFU切换**

2. **数据有效期策略调整**

   根据业务数据有效期进行分类错峰，A类90分钟，B类80分钟，C类70分钟

   过期时间使用固定时间+随机值的形式，稀释集中到期的key的数量

3. **超热数据使用永久key**

4. **定期维护（自动+人工）**

   对即将过期数据做访问量分析，确认是否延时，配合访问量统计，做热点数据的延时

5. **加锁 慎用！**

**总结**

缓存雪崩就是瞬间过期数据量太大，导致对数据库服务器造成压力。如能够有效避免过期时间集中，可以有效解决雪崩现象的出现 （约40%），配合其他策略一起使用，并监控服务器的运行数据，根据运行记录做快速调整。

## 缓存击穿

**数据库服务器崩溃（2）**

1. 系统平稳运行过程中
2. 数据库连接量瞬间激增
3. Redis服务器无大量key过期
4. Redis内存平稳，无波动
5. Redis服务器CPU正常
6. 数据库崩溃

**问题排查**

1. Redis中某个key过期，该key访问量巨大
2. 多个数据请求从服务器直接压到Redis后，均未命中
3. Redis在短时间内发起了大量对数据库中同一数据的访问

**问题分析**

- 单个key高热数据
- key过期

**解决方案（术）**

1. **预先设定**

    以电商为例，每个商家根据店铺等级，指定若干款主打商品，在购物节期间，加大此类信息 key的过期时长

    注意：购物节不仅仅指当天，以及后续若干天，访问峰值呈现逐渐降低的趋势

2. **现场调整**

    监控访问量，对自然流量激增的数据延长过期时间或设置为永久性key

3. **后台刷新数据**

    启动定时任务，高峰期来临之前，刷新数据有效期，确保不丢失

4. **二级缓存**

    设置不同的失效时间，保障不会被同时淘汰就行

5. **加锁**

    分布式锁，防止被击穿，但是要注意也是性能瓶颈，慎重！

**总结**

缓存击穿就是单个高热数据过期的瞬间，数据访问量较大，未命中redis后，发起了大量对同一数据的数据库访问，导致对数据库服务器造成压力。应对策略应该在业务数据分析与预防方面进行，配合运行监控测试与即时调整策略，毕竟单个key的过期监控难度较高，配合雪崩处理策略即可。

## 缓存穿透

**数据库服务器崩溃（3）**

1. 系统平稳运行过程中
2. 应用服务器流量随时间增量较大
3. Redis服务器命中率随时间逐步降低
4. Redis内存平稳，内存无压力
5. Redis服务器CPU占用激增
6. 数据库服务器压力激增
7. 数据库崩溃

**问题排查**

1. Redis中大面积出现未命中
2. 出现非正常URL访问

**问题分析**

- 获取的数据在数据库中也不存在，数据库查询未得到对应数据
- Redis获取到null数据未进行持久化，直接返回
- 下次此类数据到达重复上述过程
- 出现黑客攻击服务器

**解决方案（术）**

1. 缓存null

   * 对查询结果为null的数据进行缓存（长期使用，定期清理），设定短时限，例如30-60秒，最高5分钟

2. 白名单策略

   * 提前预热各种分类数据id对应的bitmaps,id作为bitmaps的offset,相当于设置了数据白名单，当加载正常数据时，放行，加载异常数据时直接拦截（效率偏低）
   * 使用布隆过滤器（有关布隆过滤器的命中问题当前状况可忽略）

3. 实施监控

   实时监控redis命中率（业务正常范围时，通常会有一个波动值）与null数据的占比

   * 非活动时段波动：通常检测3-5倍，超过5倍纳入重点排查对象
   * 活动时段波动：通常检测10-50倍，超过50倍纳入重点排查对象

   根据倍数不同启动不同的排查流程，然后使用黑名单进行防控（运营）

4. key加密

   问题出现后，临时启动防灾业务key,对key进行业务层传输加密服务，设定校验程序，过来的key校验

   例如每天随机分配60个加密串，挑选2到3个，混淆到页面数据id中，发现key不满足规则，驳回数据访问

**总结**

缓存击穿访问了不存在的数据，跳过了合法数据的redis数据缓存阶段，每次访问数据库，导致对数据库服务器造成压力。通常此类 数据的出现量是一个较低的值，当出现此类情况以毒攻毒，并及时报警。应对策略应该在临时预案防范方面多做文章。

无论是黑名单还是白名单，都是对整体系统的压力，警报解除后尽快移除。

## 性能指标监控

**监控指标**

* 性能指标：Performance
* 内存指标：Memory
* 基本活动指标：Basic activity
* 持久性指标：Persistence
* 错误指标：Error



**Performance**

| Name                      | Description              |
| ------------------------- | ------------------------ |
| latency                   | Redis响应一个请求的时间  |
| instantaneous_ops_per_sec | 平均每秒处理请求总数     |
| hit rate (calculated)     | 缓存命中率（计算出来的） |

**Memory**

| Name                    | Description                                   |
| ----------------------- | --------------------------------------------- |
| used_memory             | 已使用内存                                    |
| mem_fragmentation_ratio | 内存碎片率                                    |
| evicted_keys            | 由于最大内存限制被移除的key的数量             |
| blocked_clients         | 由于BLPOP,BRPOP or BRPOPLPUSH而被阻塞的客户端 |

**Basic activity**

| Name                      | Description                |
| ------------------------- | -------------------------- |
| connected_clients         | 客户连接数                 |
| connected_slaves          | Slave数量                  |
| master_last_io_second_ago | 最近一次主从交互之后的秒数 |
| keyspace                  | 数据库中key值总数          |

**Persistence**

| Name                        | Description                        |
| --------------------------- | ---------------------------------- |
| rdb_last_save_time          | 最后一次持久化保存到磁盘的时间戳   |
| rdb_changes_since_last_save | 自最后一次持久化以来数据库的更改数 |

**Error**

| Name                           | Description                           |
| ------------------------------ | ------------------------------------- |
| rejected_connections           | 由于达到maxclient限制而被拒绝的连接数 |
| keyspace_misses                | key值查找失败（没有命中）次数         |
| master_link_down_since_seconds | 主从断开的持续时间（单位为秒）        |

**监控方式**

* 工具
  * Cloud Insight Redis
  * Prometheus
  * Redis-stat
  * Redis-faina
  * RedisLive
  * zabbix
* 命令
  * benchmark
  * redis cli
    * monitor
    * showlog

**benchmark**

- 命令

```
SHELL
redis-benchmark [-h ] [-p ] [-c ] [-n <requests]> [-k ]
```

- 范例1

```
SHELL
redis-benchmark
```

说明：50个连接，10000次请求对应的性能

- 范例2

```
SHELL
redis-benchmark -c 100 -n 5000
```

说明：100个连接，5000次请求对应的性能

**演示**

[![image-20221020170658042](https://weishao-996.github.io/img/%E9%BB%91%E9%A9%AC%E7%A8%8B%E5%BA%8F%E5%91%98-Redis/image-20221020170658042.png)](https://weishao-996.github.io/img/黑马程序员-Redis/image-20221020170658042.png)



| 序号 | 选项  | 描述                                     | 127.0.0.1 |
| ---- | ----- | ---------------------------------------- | --------- |
| 1    | -h    | 指定服务器主机名                         | 6379      |
| 2    | -p    | 指定服务器端口                           |           |
| 3    | -s    | 指定服务器socket                         | 50        |
| 4    | -c    | 指定并发连接数                           | 10000     |
| 5    | -n    | 指定请求书                               | 2         |
| 6    | -d    | 以字节的形式指定SET/GET值的数据大小      | 1         |
| 7    | -k    | 1=keep alive 0=reconnect                 |           |
| 8    | -r    | SET/GET/INCR 使用随机key，SADD使用随机值 | 1         |
| 9    | -P    | 通过管道传输<numreq>请求                 |           |
| 10   | -q    | 强制退出redis,仅显示query/sec值          |           |
| 11   | --csv | 以CSV格式输出                            |           |
| 12   | -l    | 生成循环，永久执行测试                   |           |
| 13   | -t    | 仅运行以逗号分隔的测试命令列表           |           |
| 14   | -l    | ldle模式，仅打开N个idle连接并等待        |           |

**monitor**

- 命令

```
PLAINTEXT
monitor
```

打印服务器调试信息

**showlong**

- 命令

```
SHELL
slowlog [operator]
```

- get ：获取慢查询日志
- len ：获取慢查询日志条目数
- reset ：重置慢查询日志
- 相关配置

```
SHELL
slowlog-log-slower-than 1000 #设置慢查询的时间下线，单位：微妙
slowlog-max-len 100 #设置慢查询命令对应的日志显示长度，单位：命令数
```