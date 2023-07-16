## Mybatis

环境:

* JDK1.8
* Mysql 8.0.27
* Maven 3.8.4
* IDEA

回顾:

* JDBC
* Mysql
* JavaSE
* Maven
* Junit

SSM框架：配置文件的。[官网啥都有.jpg](http://www.mybatis.cn/archives/789.html)



## 1、简介

### 1.1、什么是Mybatis

![image-20220204130557048](https://gitee.com/sky-dog/note/raw/master/img/202204291918196.png)

* MyBatis 是一款优秀的**持久层框架**
* 它支持自定义 SQL、存储过程以及高级映射
* MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

* MyBatis本是apache的一个开源项目iBatis，2010年这个项目由apache software foundation迁移到了google code，并且改名为MyBatis。2013年11月迁移到[**Github**](https://github.com/search?q=mybatis)。



如何获得Mybatis？

* Maven仓库

~~~xml
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.9</version>
</dependency>

~~~

* [Releases · mybatis/mybatis-3 · GitHub](https://github.com/mybatis/mybatis-3/releases)
* [mybatis – MyBatis 3 | 中文文档](https://mybatis.org/mybatis-3/zh/index.html)



### 1.2、持久化

数据持久化

* 4持久化就是将程序的数据在持久状态和瞬时状态转化的过程

* 内存：断电丢失数据
* 数据库(jdbc)，io文件持久化



### 1.3、持久层

Dao层，Service层，Controller层.....

* 完成持久化工作的代码块
* 层是界限十分明显的



1.4、为什么需要Mybatis

* 帮助程序猿将数据存到数据库

* 方便
* 传统的JDBC代码太麻烦
* 优点
  * 简单易学：本身就很小且简单。没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件
  * 灵活：sql写在xml里，便于统一管理和优化。通过sql语句可以满足操作数据库的所有需求
  * 解除sql与程序代码的耦合：通过提供DAO层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护，更易单元测试
  * 提供映射标签，支持对象与数据库的orm字段关系映射
  * 提供对象关系映射标签，支持对象关系组建维护
  * 提供xml标签，支持编写动态sql



## 2、第一个Mybatis程序

思路：搭建环境-->导入Mybatis-->编写代码-->调试

* 项目pom.xml
* MybatisUtills
* mybatis-config.xml
* 实体类
* Mapper接口
* 接口对应Mapper.xml
* 对应Test

### 2.1、搭建环境

搭建数据库

~~~sql
CREATE DATABASE `mybatis`;

USE `mybatis`;

CREATE TABLE `user`(
  `id` INT(20) NOT NULL PRIMARY KEY,
  `name` VARCHAR(30) DEFAULT NULL,
  `pwd` VARCHAR(30) DEFAULT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `user`(`id`,`name`,`pwd`) VALUES
(1,'炎东','123456'),
(2,'张三','123456'),
(3,'李四','123456')
~~~

新建普通maven项目

删除src目录，创建父工程

### 2.2、创建一个模块

* 编写mybatis核心配置文件

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--configuration核心配置文件-->
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSl=true&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="9738faq"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="org/mybatis/example/BlogMapper.xml"/>
    </mappers>
</configuration>
~~~

* 编写工具类

~~~java
package com.lyd.Utills;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtills {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //使用Mybatis第一步:获取sqlSessionFactory对象
            String resource = "org/mybatis/example/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }

}
~~~

### 2.3、编写代码

* 实体类

~~~java
package com.lyd.pojo;

public class User {
    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
~~~

* Dao接口

~~~java
public interface UserDao {
    public List<User> getUserList();
}
~~~



* 接口实现类 由原来的UserDaoImpl转变为一个Mapper配置文件

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.lyd.dao.UserDao">
    <select id="getUserList" resultType="com.lyd.pojo.User">
        select * from mybatis.user
    </select>
</mapper>
~~~

### 2.4、测试

Junit测试

~~~java
package com.lyd.dao;

import com.lyd.pojo.User;
import com.lyd.utills.MybatisUtills;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {
    @Test
    public void test(){
        //第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtills.getSqlSession();
        
//        //方式一：getMapper
//        List<User> userList = sqlSession.getMapper(UserDao.class).getUserList();
//        for (User user: userList) {
//            System.out.println(user);
//        }
        
        //方式二：selectOne||selectList (不推荐使用)
        List<User> userList = sqlSession.selectList("com.lyd.dao.UserDao.getUserList");
        for (User user: userList) {
            System.out.println(user);
        }

        //关闭sqlSession
        sqlSession.close();
    }
}
~~~



#### 可能会遇到的问题

* ##### 配置文件没有注册

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSl=true&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="9738faq"/>
            </dataSource>
        </environment>
    </environments>
    <!--记得注册-->
    <mappers>
        <mapper resource="com/lyd/dao/UserMapper.xml"/>
    </mappers>
</configuration>
~~~

* ##### 绑定接口错误

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.lyd.dao.UserDao">
    <select id="getUserList" resultType="com.lyd.pojo.User">
        select * from mybatis.user
    </select>
</mapper>
~~~

* 方法名不对
* 返回类型不对



* maven中文编码问题

maven中文编码导致xml中不允许存在中文注释

否则出现**java.lang.ExceptionInInitializerError**

解决方法：

在项目pom.xml中配置

~~~xml
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
    </properties>
~~~



* maven导出资源问题

maven项目中找不到xml文件

解决方法：

在项目pom.xml中配置

~~~xml
    <build>
        <resources>
            <!--这个是资源目录的文件，一般我们资源目录的文件是可以正常被导出来的，但是以防万一还是写上 -->
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <!--这个是java中的文件-->
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
~~~



![image-20220204185838498](https://gitee.com/sky-dog/note/raw/master/img/202204291918247.png)



## 3、CRUD增删改查



### 3.1、namespace

namespace中的包名要和 Dao/mapper 接口的包名一致

### 3.2、select

选择，查询语句

* id：就是对应的namespace中的方法名
* resultType：Sql语句执行的返回值
* parameterType：参数类型

1.编写接口

~~~java
//根据ID查询用户
User getUserById(int id);
~~~

2.编写对应mapper中的sql语句

~~~xml
<select id="getUserById" parameterType="int" resultType="com.lyd.pojo.User">
    select * from mybatis.user where id = #{id}
</select>
~~~

3.测试(**增删改**需要提交事务！)

~~~java
@Test
public void getUserById(){
    SqlSession sqlSession = MybatisUtills.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    User user = mapper.getUserById(1);

    System.out.println(user);

    sqlSession.close();
}
~~~

### 3.3、add

~~~xml
<!--对象中的属性可以直接取出来-->
<insert id="addUser" parameterType="com.lyd.pojo.User">
    insert into mybatis.user (id,name,pwd) values (#{id},#{name},#{pwd})
</insert>
~~~

### 3.4、update

~~~xml
<update id="updateUser" parameterType="com.lyd.pojo.User">
    update mybatis.user set name=#{name},pwd=#{pwd} where id = #{id}
</update>
~~~

### 3.5、delete

~~~xml
<delete id="deleteUser" parameterType="int">
    delete from mybatis.user where id=#{id}
</delete>
~~~



**注意**：增删改需要**提交事务**！！！



## 4、配置解析

### 4.1、核心配置文件

* mybatis-config.xml
* Mybatis的配置文件包含了会深深影响Mybatis行为的设置和属性信息
* configuration（配置）
  - [properties（属性）](https://mybatis.org/mybatis-3/zh/configuration.html#properties)
  - [settings（设置）](https://mybatis.org/mybatis-3/zh/configuration.html#settings)
  - [typeAliases（类型别名）](https://mybatis.org/mybatis-3/zh/configuration.html#typeAliases)
  - [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
  - [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
  - [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
  - environments（环境配置）
    - environment（环境变量）
      - transactionManager（事务管理器）
      - dataSource（数据源）
  - [databaseIdProvider（数据库厂商标识）](https://mybatis.org/mybatis-3/zh/configuration.html#databaseIdProvider)
  - [mappers（映射器）](https://mybatis.org/mybatis-3/zh/configuration.html#mappers)



### 4.2、环境配置（environments）

MyBatis 可以配置成适应多种环境

**不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**

学会使用配置多套运行环境！

Mybatis默认的事务管理器就是JDBC，连接池：POOLED

### 4.3、属性（properties）

我们可以通过properties属性来实现引用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。**[db.properties]**

编写一个配置文件db.properties

~~~properties
drive=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSl=true&useUnicode=true&characterEncoding=UTF-8
username=root
password=9738faq
~~~

在核心配置中引入

~~~xml
    <!--引入外部配置文件-->
    <properties resource="db.properties">
        <property name="username" value="root"/>
        <property name="password" value="111111"/>
    </properties>
~~~

* 可以直接引入外部文件
* 可以在期中增加一些属性配置
* 如果两个文件有同一个字段，优先使用外部配置文件的



### 4.4、类型别名（typeAliases）

* 类型别名可为 Java 类型设置一个缩写名字
* 仅用于 XML 配置，意在降低冗余的全限定类名书写

在核心配置中引入

~~~xml
<!--引入外部配置文件-->
    <properties resource="db.properties">
        <property name="username" value="root"/>
        <property name="password" value="111111"/>
    </properties>
~~~

也可以指定一个包名，Mybatis会在包名下面搜索需要的Java Bean

扫描实体类的包，它的默认别名九尾这个类的 类名(建议首字母小写)



在实体类少的时候，建议使用第一种

在实体类躲的时候，建议使用第二种

第一种可以DIY别名； 第二种不行，非要改，加注解



### 4.5、设置（settings）

这是Mybatis种极为重要的调整设置，它们会改变Mybatis的运行时行为

![image-20220205000019583](https://gitee.com/sky-dog/note/raw/master/img/202204291919518.png)

![image-20220205000154509](https://gitee.com/sky-dog/note/raw/master/img/202204291919357.png)

### 4.6、其他配置

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
  - [mybatis-generator-core](https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core)
  - [mybatis-plus](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus)
  - 通用mapper

### 4.7、映射器（mappers）

MapperRegistry：注册绑定我们的Mapper文件

方式一： **（推荐使用！）**

~~~xml
<!-- 使用相对于类路径的资源引用 -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>
~~~

方式二：使用class文件绑定注册

~~~xml
<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>
~~~

注意点：

* 接口和Mapper配置文件必须同名
* 接口和Mapper配置文件必须在同一个包下

方式三：

~~~xml
<!-- 将包内的映射器接口实现全部注册为映射器 -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
~~~

注意点：

* 接口和Mapper配置文件必须同名
* 接口和Mapper配置文件必须在同一个包下



### 4.8、作用域（Scope）和生命周期

![image-20220205001553995](https://gitee.com/sky-dog/note/raw/master/img/202204291919472.png)

不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的并发问题。

#### SqlSessionFactoryBuilder

* 一旦创建了 SqlSessionFactory，就不再需要它了
* 局部变量

#### SqlSessionFactory

* 说白了可以想象为：数据库连接池
* SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例
* SqlSessionFactory 的最佳作用域是应用作用域
* 最简单的就是使用单例模式或者静态单例模式

#### SqlSession

* 连接到连接池的一个请求
* SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域
* 用完之后需要赶紧关闭，否则资源浪费

![image-20220205002056188](https://gitee.com/sky-dog/note/raw/master/img/202204291919646.png)

这里的每一个Mapper都代表一个具体业务



## 5、解决属性名与字段名不一致的问题

### 5.1、问题

~~~java
public class User {
    private int id;
    private String name;
    private String password;
~~~

~~~sql
select * from mybatis.user where id = #{id}
#类型处理器
select id,name,pwd from mybatis.user where id = #{id}
~~~

解决方法：

* 起别名

~~~xml
	<select id="getUserList" resultType="User">
        select id,name,pwd as password from mybatis.user where id = #{id}
    </select>
~~~



### 5.2、resultMap

结果集映射

~~~
id 		name 	pwd
id 		name 	password
~~~

~~~xml
    <resultMap id="UserMap" type="User">
        <!--column数据库种的字段，property实体类种的属性-->
        <result column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="pwd" property="password"/>
    </resultMap>
~~~

* `resultMap` 元素是 MyBatis 中最重要最强大的元素
* ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了

* 这就是 `ResultMap` 的优秀之处——你完全可以不用显式地配置它们。 虽然上面的例子不用显式配置 `ResultMap`

* 如果这个世界总是这么简单就好了。



![image-20220205004254292](https://gitee.com/sky-dog/note/raw/master/img/202204291919551.png)





## 6、日志

### 6.1、日志工厂

如果一个数据库操作，出现了异常，我们需要排错，日志就是最好的助手！

曾经：sout、debug

现在：日志工厂

![image-20220205113738141](https://gitee.com/sky-dog/note/raw/master/img/202204291920490.png)

* SLF4J 
*  LOG4J  (**掌握**)

(目前，Apache Log4j 已经发布了新版本来修复该漏洞，请受影响的用户将 Apache Log4j2 的所有相关应用程序升级至最新的 Log4j-2.15.0-rc2 版本)

*  LOG4J2 
*  JDK_LOGGING 
*  COMMONS_LOGGING 
*  STDOUT_LOGGING (**掌握**)
*  NO_LOGGING

在Mybatis种具体使用哪一个日志实现，在设置中设定



标准日志工厂

~~~xml
    <settings>
<!--        标准日志工厂实现-->
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
~~~



### 6.2、log4j实现

什么是log4j

* 通过使用Log4j，我们可以控制日志信息输送的目的地是控制台、文件、GUI组件，甚至是套接口服务器、NT的事件记录器、UNIXSyslog守护进程等
* 我们也可以控制每一条日志的输出格式
* 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程
* 这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码



#### 1.先导入log4j的包

~~~xml
<!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.17.1</version>
</dependency>

~~~

~~~xml
<!-- 引入log4j依赖，可以查看报错信息和sql语句，值的接收与返回等，记得将它的配置文件log4j.properties导入-->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
~~~



#### 2.配置log4j.properties

~~~properties
#将等级为DEBUG的日志信息输出导console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold = DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n

#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File = ./log/lyd.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
~~~

#### 3.配置log4j为日志的实现

~~~xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
~~~

#### 4.直接测试



#### 简单使用

* 在要使用Log4j的类中，导入包， import org.apache.logj.Logger;
* 日志对象，参数为当前类的class

~~~java
static Logger logger = Logger.getLogger(UserDaoTest.class);
~~~

* 日志级别

~~~log
[INFO][22-02-05][com.lyd.dao.UserDaoTest]info:进入了testLog4j
[DEBUG][22-02-05][com.lyd.dao.UserDaoTest]debug:进入了testLog4j
[ERROR][22-02-05][com.lyd.dao.UserDaoTest]error:进入了testLog4j
~~~



## 7、分页

**为什么要分页？**

减少数据处理量



### 7.1、使用limit分页

~~~sql
select * from user limit startIndex,pageSize
~~~



### 7.2、使用Mybatis实现分页，核心sql

* 接口

~~~java
//分页
List<User> getUserByLimit(Map<String,Integer> map);
~~~

* Mapper.xml

~~~xml
<select id="getUserByLimit" parameterType="map" resultMap="UserMap">
    select * from mybatis.user limit #{startIndex},#{pageSize}
</select>
~~~

* 测试

~~~java
@Test
public void getUserByLimit(){
    SqlSession sqlSession = MybatisUtills.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    HashMap<String, Integer> map = new HashMap<>();
    map.put("startIndex",0);
    map.put("pageSize",2);
    List<User> userList = mapper.getUserByLimit(map);
    for (User user:userList) {
        System.out.println(user);
    }
    sqlSession.close();
}
~~~

### 7.3、RowBounds分页

不再使用SQL实现分页(不推荐)

* 接口

~~~java
List<User> getUserByRowBounds();
~~~

* mapper.xml

~~~xml
<select id="getUserByRowBounds" resultMap="UserMap">
    select * from mybatis.user
</select>
~~~

* 测试

~~~java
@Test
public void getUserByRowBounds(){
    SqlSession sqlSession = MybatisUtills.getSqlSession();
    RowBounds rowBounds = new RowBounds(1,2);
    //通过Java代码层面实现分页
    List<User> userList = sqlSession.selectList("com.lyd.dao.UserMapper.getUserByRowBounds",null,rowBounds);
    for (User user:userList) {
        System.out.println(user);
    }
    sqlSession.close();
}
~~~



### 7.4、分页插件

![image-20220205172552370](https://gitee.com/sky-dog/note/raw/master/img/202204291920496.png)

了解即可，要用再去看文档:(



## 8、使用注解开发

### 8.1、面向接口编程



### 8.2、实现注解开发

* 注解在接口上实现

~~~java
@Select("select * from user")
List<User> getUsers();
~~~

* 需要在核心配置文件中绑定接口

~~~xml
<mappers>
    <mapper class="com.lyd.dao.UserMapper"/>
</mappers>
~~~

* 测试

~~~java
@Test
public void test(){
    SqlSession sqlSession = MybatisUtills.getSqlSession();
    //底层主要应用反射
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> users = mapper.getUsers();
    for (User user:users) {
        System.out.println(user);
    }
    sqlSession.close();
}
~~~



本质：反射机制实现

底层：动态代理

![image-20220205174946924](https://gitee.com/sky-dog/note/raw/master/img/202204291920271.png)

**Mybatis详细的执行流程**



### 8.3、CRUD

#### 设置自动提交事务

~~~java
public static SqlSession getSqlSession(){
    return sqlSessionFactory.openSession(true);
}
~~~

#### 编写接口，增加注解

~~~java
public interface UserMapper {

    @Select("select * from user")
    List<User> getUsers();

    //方法传入多个参数时，所有参数前一定要加上@Param
    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") int id);
    
    @Insert("insert into user(id,name,pwd) values (#{id},#{name},#{pwd})")
    int addUser(User user);

    @Update("update user set name=#{name},pwd=#{pwd} where id = #{id}")
    int updateUser(User user);
    
    @Delete("delete from user where id=#{uid}")
    int deleteUser(@Param("uid") int id);
}
~~~

#### 测试类

懒得写了



**注意**：我们必须要将接口注册绑定到我们的核心配置文件中

~~~xml
<mappers>
    <mapper class="com.lyd.dao.UserMapper"/>
</mappers>
~~~



#### 关于@Param注解

* 基本类型的 **参数** 或 **String** 类型，需要加上

* 引用类型不需要加
* 如果只有一个基本类型可以忽略，但是建议都加上
* 我们在SQL中引用的就是这里在@Param中设定的属性名



**\#{}和${}的区别** 

* \#{}可以防止sql注入



## 9、Lombok

~~~English
Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
~~~



使用步骤：

* 在IDEA中安装Lombok插件(现版本自带)
* 导入jar包

~~~xml
<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.22</version>
</dependency>
~~~

* 在实体类上加注解即可

~~~java
@Getter and @Setter
@FieldNameConstants
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
experimental @var
@UtilityClass
~~~



**说明：**

~~~
@Data == 无参构造+get+set+tostring+hashcode+equals
@AllArgsConstructor
@NoArgsConstructor
~~~



## 10、多对一处理

* 多个学生，对应一个老师
* 对于学生而言，关联，多个学啥，关联一个老师[**多对一**]
* 对于老师而言，集合，一个老师，有很多学生[**一对多**]



### 测试环境搭建

1.导入lombok

2.新建实体类 Teacher , Student

3.建立Mapper接口

4.建立Mapper.xml

5.在核心配置文件中绑定注册mapper接口或者文件

6.测试查询是否能够成功

复杂情况例子 建表sql

~~~sql
CREATE TABLE `teacher` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师'); 

CREATE TABLE `student` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  `tid` INT(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');
~~~



### 按照查询结果嵌套处理

~~~xml
<select id="getStudent" resultMap="StudentTeacher">
    select * from student
</select>
<resultMap id="StudentTeacher" type="Student">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <!--复杂的属性需要单独处理 对象: association 集合: collection-->
    <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
</resultMap>
<select id="getTeacher" resultType="Teacher">
    select * from teacher where id = #{id}
</select>
~~~



### 按照结果嵌套处理

~~~xml
<select id="getStudent2" resultMap="StudentTeacher2">
    select s.id sid,s.name sname,t.name tname
    from student s, teacher t
    where s.tid = t.id;
</select>
<resultMap id="StudentTeacher2" type="Student">
    <result property="id" column="sid"/>
    <result property="name" column="sname"/>
    <association property="teacher" javaType="Teacher">
        <result property="name" column="tname"/>
    </association>
</resultMap>
~~~



多对一查询方式

* 子查询
* 联表查询



## 11、一对多处理

比如：一个老师有多个学生

对于老师，就是一对多的关系



### 环境搭建，和刚才一样

实体类的修改

~~~java
package com.lyd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private int id;
    private String name;
    private int tid;
}
~~~

~~~java
package com.lyd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private int id;
    private String name;
    //一个老师拥有多个学生
    private List<Student> students;
}

~~~



### 按照结果嵌套处理

~~~xml
    <!--按结果嵌套查询-->
    <select id="getTeacher" resultMap="TeacherStudent">
        select s.id sid,s.name sname,t.name tname,t.id tid
        from student s,teacher t
        where s.tid = t.id and t.id = #{tid}
    </select>
    <resultMap id="TeacherStudent" type="Teacher">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
        <!--复杂的属性需要单独处理 对象: association 集合: collection
        javaType="" 指定属性的类型
        集合中的泛型信息，用ofType获取
        -->
        <collection property="students" ofType="Student">
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
            <result property="tid" column="tid"/>
        </collection>
    </resultMap>
~~~

### 按照查询嵌套处理

~~~xml
    <select id="getTeacher2" resultMap="TeacherStudent2">
        select * from teacher where id = #{tid}
    </select>
    <resultMap id="TeacherStudent2" type="Teacher">
        <collection property="students" javaType="ArrayList" ofType="Student" select="getStudentByTeacherId" column="id"/>
    </resultMap>
    <select id="getStudentByTeacherId" resultType="Student">
        select * from student where tid = #{tid}
    </select>
~~~



### 小结

* 关联 - association [多对一]
* 集合 - collection [一对多]
* javaType & ofType
  * JavaType 用来指定实体类中的属性的类型
  * ofType 用来指定映射到List或者集合中的pojo类型，泛型的约束类型



注意点：

* 保证SQL的可读性，尽量保证通俗易懂
* 注意一对多和多对一，属性名和字段的问题
* 如果问题不好排查，可以用日志，建议Log4j



面试高频

* Mysql引擎
* InnoDB底层原理
* 索引
* 索引优化



## 12、动态SQL

什么是动态SQL：动态SQL就是根据不同条件生成不同的SQL语句

利用动态 SQL，可以彻底摆脱这种痛苦

~~~
如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

if
choose (when, otherwise)
trim (where, set)
foreach
~~~



### 搭建环境

~~~sql
CREATE TABLE `blog`(
`id` VARCHAR(50) NOT NULL COMMENT '博客id',
`title` VARCHAR(100) NOT NULL COMMENT '博客标题',
`author` VARCHAR(30) NOT NULL COMMENT '博客作者',
`create_time` DATETIME NOT NULL COMMENT '创建时间',
`views` INT(30) NOT NULL COMMENT '浏览量'
)ENGINE=INNODB DEFAULT CHARSET=utf8
~~~



创建一个基础工程

* 导包
* 编写配置文件
* 编写实体类

~~~java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    private String id;
    private String title;
    private String auther;
    private Date createTime;
    private int views;
}
~~~

* 编写实体类对应Mapper接口和Mapper.xml



### IF

~~~xml
<select id="queryBlogIF" parameterType="map" resultType="Blog">
    select * from blog where 1=1
    <if test="title != null">
        and title = #{title}
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</select>
~~~



### choose、when、otherwise

~~~xml
<select id="queryBlogChoose" parameterType="map" resultType="Blog">
    select * from blog
    <where>
        <choose>
            <when test="title!=null">
                title like #{title}
            </when>
            <when test="author!=null">
                and author like #{author}
            </when>
            <otherwise>
                and views = #{views}
            </otherwise>
        </choose>
    </where>
</select>
~~~



### trim(where、set)

~~~xml
<select id="queryBlogIF" parameterType="map" resultType="Blog">
    select * from blog 
    <where>
        <if test="title != null">
            title = #{title}
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </where>
</select>
~~~

where set可以动态修改 ， and之类的东西



可以用trim定制where、set的功能



所谓的动态SQL，本质还是SQL语句，只是我们可以在SQL层面，去执行一个逻辑代码



### Foreach

~~~sql
select * from user where 1=1 and (id=1 or id=2 or id=3)
~~~

![image-20220206031013629](https://gitee.com/sky-dog/note/raw/master/img/202204291920622.png)

~~~xml
    <select id="queryBlogForeach" parameterType="map" resultType="Blog">
        select * from blog
        <where>
            <foreach collection="ids" item="id" open="and (" close=")" separator="or">
                id = #{id}
            </foreach>
        </where>
    </select>
~~~

~~~java
    @Test
    public void queryBlogForeach(){
        SqlSession sqlSession = MybatisUtills.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map = new HashMap();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        map.put("ids",ids);
        List<Blog> blogs = mapper.queryBlogForeach(map);
        for (Blog blog:blogs) {
            System.out.println(blog);
        }
        sqlSession.close();
    }
~~~



### SQL片段

有的时候可能会将一些公共部分抽取出来，方便复用

* 使用SQL标签抽取公共部分

~~~xml
    <sql id="if-title-author">
        <if test="title!=null">
            title = #{title},
        </if>
        <if test="author!=null">
            author = #{author}
        </if>
    </sql>
~~~

* 在需要使用的地方使用include标签引用即可

~~~sql
    <select id="queryBlogIF" parameterType="map" resultType="Blog">
        select * from blog
        <where>
            <include refid="if-title-author"></include>
        </where>
    </select>
~~~

注意事项：

* 最好基于单表来定义SQL片段
* 不要存在where标签
* 一般只用if、set



动态SQL就是在拼接SQL语句，我们只要保证SQL的正确性，按照SQL的格式，去排列组合就可以了

建议：

* 先在Mysql中写出完整的SQL，再去对应的修改成为动态SQL，实现通用！	





## 13、缓存

### 13.1、简介

* 什么是缓存
  * 存在内存中的临时数据
  * 将用户经常查询的数据放在缓存(内存)中，用户去查询数据就不用从磁盘上(关系型数据库数据文件)查询，从缓存中查询， 从而提高查询效率，解决了高并发系统的性能问题
* 为什么使用缓存
  * 减少数据库的交互次数，减少系统开销，提高系统效率
* 什么样的数据能使用缓存
  * 经常查询且不经常改变的数据



### 13.2、Mybatis缓存

* MyBatis包含一个非常强大的缓存特性，可以非常方便地定制和配置缓存。缓存可以极大的提升查询效率
* Mybatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**
  * 默认情况下，只有一级缓存开启(SqlSession级别的缓存，也成为本地缓存)
  * 二级缓存需要手动开启和配置，是基于namespace级别的缓存
  * 为了提高扩展性，Mybatis定义了缓存接口的Cache。我们可以通过实现Cache接口来自定义二级缓存



### 13.3、一级缓存

* 一级缓存也叫本地缓存
  * 与数据库同一时会话期间查询到的数据
  * 以后可以再次调用里面的数据



* 映射语句文件中的所有 select 语句的结果将会被缓存。
* 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。
* 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。
* 缓存不会定时进行刷新（也就是说，没有刷新间隔）。
* 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。
* 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。



缓存失效的情况

- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存
- sqlSession.clearcache()方法，手动清理缓存



一级缓存就像一个map



### 13.4、二级缓存

* 二级缓存也叫全局缓存，一级缓存作用域太低，所以诞生了二级缓存
* 基于namespace级别的缓存，一个名称空间，对应一个二级缓存
* 工作机制
  * 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中
  * 如果当前会话关闭了，这个会话对应的一级缓存就被清理了，但会被保存到二级缓存中
  * 新的会话查询信息，就可以从二级缓存中获取内容
  * 不同的mapper查出的数据会放在自己对应的缓存(map)中

步骤：

* 开启全局缓存

~~~xml
<!--是否开启全局缓存-->
<setting name="cacheEnabled" value="true"/>
~~~

* 在要使用二级缓存的Mapper.xml中开启

~~~xml
<!--在当前Mapper.xml中使用二级缓存-->
<cache/>
~~~

​		也可以自定义缓存属性

~~~xml
<!--在当前Mapper.xml中使用二级缓存-->
<cache
       eviction="FIFO"
       flushInterval="60000"
       size="512"
       readOnly="true"/>
~~~

* 测试

  1.问题：需要将实体类**序列化**

  ~~~java
  Caused by: java.io.NotSerializableException: com.lyd.pojo.User
  ~~~

  解决方法:

  ~~~java
  public class User implements Serializable {
      private int id;
      private String name;
      private String pwd;
  }
  ~~~

  

小结：  

* 只要开启了二级缓存，在同一个Mapper下就有效
* 所有的数据都会先存放在一级缓存中
* 只有当会话提交，或者关闭的时候，才会提交到二级缓存中



### 13.5、缓存原理 

![image-20220206135959222](https://gitee.com/sky-dog/note/raw/master/img/202204291920966.png)



### 13.6、自定义缓存 - ehcache

Ehcache是一种广泛使用的开源Java分布式缓存，主要面向通用缓存

使用步骤：

* 导包

~~~xml
<!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
<dependency>
    <groupId>org.mybatis.caches</groupId>
    <artifactId>mybatis-ehcache</artifactId>
    <version>1.2.1</version>
</dependency>

~~~

* 在Mapper.xml中配置ehcache覆盖原有缓存方案

~~~xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
~~~

* 配置ehcache配置文件 ehcache.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
         updateCheck="false">

    <diskStore path="./tmpdir/Tmp_EhCache"/>

    <defaultCache
            eternal="false"
            maxElementsInMemory="10000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="259200"
            memoryStoreEvictionPolicy="LRU"/>

    <cache
            name="cloud_user"
            eternal="false"
            maxElementsInMemory="5000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="1800"
            memoryStoreEvictionPolicy="LRU"/>
</ehcache>
~~~

不用了解太深

以后用 **Redies** ！！！！



































