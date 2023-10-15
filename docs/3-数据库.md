# Java 第三轮考核

## 参考资料

* [MySQL[8.0] 解压版安装教程](https://blog.csdn.net/tyler880/article/details/109106093)
  * **推荐使用8.0版本**，当然5.7也是主流版本
  * 解压版相比安装版小很多，也可以使用安装版。具体安装教程可以百度。~~(安装版就无脑next就可以了罢)~~
* SQL教程
  * [SQL教程 - 廖雪峰的官方网站 (liaoxuefeng.com)](https://www.liaoxuefeng.com/wiki/1177760294764384)，这个讲的没有很详细但是看着入门挺快
  * [狂神说MySQL](https://www.bilibili.com/video/BV1NJ411J79W/?share_source=copy_web&vd_source=7d2fd3963c594f890889ebd454ef8d1c)(个人很喜欢的一个up，学起来比较轻松入门快)
* 持久层部分 (使用Java对数据库进行操作)
  * **JDBC**(最基础最底层的连接数据库方式)
    *  [JDBC优质博客](https://blog.csdn.net/jungle_rao/article/details/81274720)
    *  上面狂神说MySQL视频最后部分
  * Mybatis (扩展提升)
    * [狂神说Mybatis](https://www.bilibili.com/video/BV1NE411Q7Nx/?share_source=copy_web&vd_source=7d2fd3963c594f890889ebd454ef8d1c)
    * 到这类框架学习掌握一定技巧之后其实可以尝试直接查看官方文档学习
  * 其他框架
    * 自行百度
* [数据库设计三大范式](https://www.cnblogs.com/knowledgesea/p/3667395.html)

## 知识点

* 数据库 (Mysql)
* Sqlyog/Navicate 等图形化工具的使用
* 数据库设计范式
* jdbc/持久层框架
* Bonus
  * Maven环境搭建
  * Json数据结构
  * 第三方接口调用
  * **Spring** 框架

## 任务

假设工作室近期需要置办一系列搭建，邀请各位编写一个简单的订单管理系统(用来记账)

* 至少需要记录以下信息:

  * 商品: 商品编号、商品名、商品价格
  * 订单: 订单编号、**商品信息**(考虑如何合理存储关联信息)、下单时间、订单价格

* 需要将信息保存至**数据库**

* 编写JDBC工具类，功能包括但不限于：

  * 处理数据库连接
  * 执行增删查改操作

  * 解决**SQL注入问题**
  * 添加**事务管理**
  * 包含异常处理和资源释放

* **使用编写的JDBC工具类实现商品和订单信息的增删改查、更新，商品和订单排序（价格、下单时间）等功能**

  * 在创建订单时，实施数据验证，确保订单信息的完整性和准确性。例如，检查商品是否存在，价格是否合法等等。
  * 如果想要删除已经存在在订单中的商品，你要怎么处理？
  * 避免使用SELECT *
 
* **完整的测试**

* 可以自己设计新功能，有需要也可以自行增加字段。

* 在代码中加入必要的注释，说明每个方法的作用和功能，以及关键代码的解释。

* 使用合适的代码风格和命名规范，确保代码的可读性和可维护性。

* 编写README.md文档，内容可以是你的学习记录与心得，方便我们了解你们的学习状况


## Bonus

### 基础任务进阶需求

* 实现分页查询。
* 使用其他连接池，如Druid。
* 使用mybatis等orm框架。
* **使用spring框架暴露http路由（日后常用）**

> 第四轮的难度有一定的提升，请在**保证第三轮作业的质量**的前提下，去提前看看第四轮

### 第三方Api调用

> 你觉得福州天气变化无常<img src="https://gitee.com/poldroc/typora-drawing-bed01/raw/master/imgs/202310140145992.jpeg" alt="img" style="zoom: 40%;" />，想制作一个天气查询系统查询福州和其他部分城市的天气

* 使用和风天气提供的免费 API 来完成任务

  * [和风开发者平台官网 (qweather.com)](https://dev.qweather.com/)

  * [和风天气WebApi使用教程](https://www.cnblogs.com/6543x1/p/15684812.html)

  * 使用的api

    * 城市查询 API，用于查询城市的 城市ID（id）、纬度（lat）、经度（lon）等城市信息

      https://geoapi.qweather.com/v2/city/lookup?key=这里填你的key&location=要查询的城市 名字

    * 三日天气查询 api ，用于查询某地今日、明日、后日的天气信息

      https://devapi.qweather.com/v7/weather/3d?key=这里填你的key&location=要查询的城市 的id

  * 以上 api 返回信息均为 json 格式，请选择合适的 json 工具进行处理并保存信息到数据库中。

* 在Java中通过上述api获取信息，并保存到数据库中，数据库的结构请自行设计，注意合理性。

* 至少需要以下三个城市的基本信息以及它们对应的天气信息：北京市、上海市、福州市。如果你有 其他想要的城市也可以加入到数据库中

* 至少需要以下三个城市的基本信息以及它们对应的天气信息：北京市、上海市、福州市。如果你有 其他想要的城市也可以加入到数据库中

* 城市的天气信息，需要：当日日期（fxDate），当日最高气温（tempMax）、当日最低气温 （tempMin）、白天天气情况（textDay），同时还需要记录这个天气属于哪个城市，以便于直接 查询某个城市的天气信息。注意明天和后天的天气信息也要保存到数据库中。

  * 注意：假设你在1月1日查询了福州市的未来三日（1号、2号、3号）天气信息, 又在1月2日查 询了福州市三日天气信息（2号、3号、4号），新查询的2号、3号信息要覆盖之前记录在数据 库中的信息，不要重复记录。

* 数据库的结构、细节请自行设计。

* 控制台中提供从数据库查询数据（比如查询福州市三日的天气信息）、以及更新数据的方法， 可以自行设计。功能设计较好可以加分

### Maven管理项目依赖

可以先了解有关maven的使用，后面会用上

## 提示

* 数据库推荐使用 **mysql** (也可使用oracle database、sql server等等)
* 数据库连接可以从 **JDBC** 写起，也可以使用框架，如 mybatis、hibernate、spring data、jpa
* 推荐使用数据库可视化工具，如 SQLyog、Navicate 
* 了解数据库三大范式，多考虑如何将表设计的更加合理，减少冗余



* 有关第三方Api调用

  * 天气查询api免费用户每日只能请求1000次，注意不要过度请求

  * 请合理设计数据库，减少冗余，注意表之间的联系

  * api请求可以使用第三方包如` HttpClient` ，或者Java原生的 `HttpURLConnection`

  * 本作业建议使用maven管理项目的包。可以自行百度所需包的依赖并引入。

    （注意版本问题，有时 候太老的版本可能不兼容，可以在 [[Maven Repository (mvnrepository.com)])](https://mvnrepository.com/)上搜索你的需要包，点进去看最新版本是啥，引入对应包即可（可以直接从 版本号点进去，里面写好了依赖，可以直接copy到你的 pom.xml 中））

  * Java中直接获取数据会乱码，这是因为原api使用了gzip进行压缩，需要进行如下处理: 

    ~~~java
    //获取请求结果的inputStream，根据你选择的工具决定，以下是几个常见http工具获取结果的
    inputstream方法
    //HTTPURLConnection: httpURLConnection.getInputStream()
    //Commons-httpclient: getMethod.getResponseBodyAsStream()
    //org.apache.http.client.HttpClient: response.getEntity().getContent()
    GZIPInputStream gzipInputStream =new GZIPInputStream(//这里填通过http工具请求
    api获取的inputstream);
    StringBuilder res=new StringBuilder();
    String line;
    BufferedReader br = new BufferedReader(new
    InputStreamReader(gzipInputStream, StandardCharsets.UTF_8));
    while ((line = br.readLine()) != null) {
    res.append(line);
    }
    System.out.println(res);//res就是请求的结果，注意res为StringBuilder
    ~~~

    
