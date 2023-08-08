# grpc SpringBoot实践

##  grpc简介

gRPC 是google提供的一个高性能、开源和通用的 RPC 框架，在gRPC里客户端应用可以像调用本地对象一样直接调用另一台不同的机器上服务端应用的方法，使得您能够更容易地创建分布式应用和服务

Cloud Native兴起，云端服务架构不同语言的集成能力也越来越标准化和简单化，不同语言组件间的通讯也需要统一和标准化，而这个标准的通讯协议只能是目前流行的rest或grpc，而rest则侧重于外部通讯，内部通讯首选grpc，此文主要介绍spring boot下grpc使用。

使用grpc有几点优势：

![image-20230717142920909](https://gitee.com/sky-dog/note/raw/master/img/202307171429993.png)

这边我们以golang和java为例，进行一个简单的实践；

通过本文可以学会：

* 如何定于服务接口
* 如何生成服务器和客户端代码
* 如何继承SpringBoot和golang实现一个简单的服务端和客户端
  * 这边我们用Springboot作为服务端(网关层)对外提供http服务
  * 使用golang作为客户端接收springboot请求对数据库进行操作



## 实践目标

demo需求: 一个简单的todolist服务

**golang实现task模块**，对用户表实现增删改查；**springboot实现user模块**；**springboot实现网关模块**，对外部提供http服务；二者内部使用proto通信，以grpc作为**RPC(远程函数调用)**框架。

### demo结构

~~~shell
.
├─grpc-demo-api
│  └─src
│      └─main
│         ├─java
│         └─resources
├─grpc-demo-gateway
│  └─src
│      └─main
│         ├─java
│         └─resources
├─grpc-demo-task
│  └─src
│      └─main
│         ├─java
│         └─resources
└─grpc-demo-user
    └─src
~~~

### 准备名为grpc-demo的普通maven项目

在父项目的pom中管理好spring boot、mybatis-plus和**grpc及proto**的相关依赖

~~~xml
    <dependencyManagement>
        <dependencies>
            <!-- grpc相关依赖 -->
            <dependency>
                <groupId>com.google.protobuf</groupId>d
                <artifactId>protobuf-java-util</artifactId>
                <version>${protobuf.version}</version>
            </dependency>
            <dependency>
                <groupId>org.apache.tomcat</groupId>
                <artifactId>annotations-api</artifactId>
                <version>6.0.53</version>
                <scope>provided</scope> <!-- not needed at runtime -->
            </dependency>
            <dependency>
                <groupId>com.google.errorprone</groupId>
                <artifactId>error_prone_annotations</artifactId>
                <version>2.3.4</version> <!-- prefer to use 2.3.3 or later -->
            </dependency>
            <dependency>
                <groupId>net.devh</groupId>
                <artifactId>grpc-server-spring-boot-starter</artifactId>
                <version>2.10.1.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>io.grpc</groupId>
                <artifactId>grpc-bom</artifactId>
                <version>${grpc.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>net.devh</groupId>
                <artifactId>grpc-client-spring-boot-starter</artifactId>
                <version>2.10.1.RELEASE</version>
            </dependency>
            <!--web依赖-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!--单元测试-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
            <!--lombok-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
            <!--mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <scope>runtime</scope>
            </dependency>
            <!-- mybatis-plus -->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.2.2</version>
            </dependency>
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>3.5.1</version>
            </dependency>
            <!-- swagger -->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>2.7.0</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>2.7.0</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
~~~

在**grpc-demo-api**(存放proto源文件)中的pom添加依赖proto相关依赖和编译插件

~~~xml
    <build>
        <!-- os系统信息插件, protobuf-maven-plugin需要获取系统信息下载相应的protobuf程序 -->
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.6.2</version>
            </extension>
        </extensions>
        <plugins>

            <!-- grpc代码生成插件 -->
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.6.1</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:${protoc.version}:exe:${os.detected.classifier}
                    </protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
                    </pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- jar版本冲突检测插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-enforcer-plugin</artifactId>
                <version>1.4.1</version>
                <executions>
                    <execution>
                        <id>enforce</id>
                        <goals>
                            <goal>enforce</goal>
                        </goals>
                        <configuration>
                            <rules>
                                <requireUpperBoundDeps/>
                            </rules>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
~~~

载入成功后，能够在maven管理项看见proto插件，编译后可以在target目录下看见编译后的protobuf文件

<img src="https://gitee.com/sky-dog/note/raw/master/img/202307231644529.png" alt="image-20230723164435458" style="zoom:67%;" /><img src="https://gitee.com/sky-dog/note/raw/master/img/202307241022148.png" alt="image-20230724102253074" style="zoom:80%;" />

接下来，简单介绍一下protobuf下生成的文件的作用：

* **grpc-java**
  * 生成的是grpc基于java语言特性生成的服务类，封装了服务注册、服务发现等功能，提供了在proto文件中定义的服务模板
  * 实现服务就只需要**继承grpc-java下对应的服务类**(如图示中的UserServiceGrpc)并重写对应的方法，即可完成服务的编写
* **java**
  * 这个目录下的文件主要是实体定义、通信相关等
  * model
    * 主要是生成了在proto中定义的实体及其构造类
  * service
    * 主要是用作grpc的服务注册和通信相关

### 实现server端

#### 用户模块使用依赖

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>grpc-demo</artifactId>
        <groupId>com.west2</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>grpc-demo-user</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.west2</groupId>
            <artifactId>grpc-demo-api</artifactId>
            <version>1.0</version>
        </dependency>

        <!-- grpc相关依赖 -->
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java-util</artifactId>
        </dependency>

        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty-shaded</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
        </dependency>

        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
        </dependency>

        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-testing</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.google.errorprone</groupId>
            <artifactId>error_prone_annotations</artifactId>
        </dependency>

        <!-- grpc server和spring-boot集成框架 -->
        <dependency>
            <groupId>net.devh</groupId>
            <artifactId>grpc-server-spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- grpc client和spring-boot集成框架 -->
        <dependency>
            <groupId>net.devh</groupId>
            <artifactId>grpc-client-spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- mybatis-plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>

        <!-- mysql 连接 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.7</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>

    </dependencies>

</project>
~~~

#### 配置文件

~~~yml
#net.devh.boot.grpc.server.config.GrpcServerProperties
grpc:
  server:
    port: 9091
    in-process-name: grpc-demo-user
  client:
    #允许客户端在同一应用程序内使用以下配置连接到服务器
    inProcess:
      address: in-process:grpc-demo-user
spring:
  application:
    name: grpc-demo-user
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/todolist?serverTimezone=GMT%2b8
    username: root
    password: 9738faq
~~~



#### 实现样例

user模块作为rpc的服务端，为网关模块(客户端)提供用户相关的服务

只要继承生成的对应服务类，并重写方法即可实现rpc通信。

以下是一个example：

```java
/**
 * 用户服务实现类
 * @author 天狗
 */
@Slf4j
@GrpcService
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

    @Resource
    private UserMapper userMapper;

    @Override
    public void getById(UserIdRequest request, StreamObserver<UserResponse> responseObserver) {
        log.info("start getById");
        User user = userMapper.selectById(request.getId());

        // 构造rpc响应
        com.west2.demo.grpc.user.model.User.Builder respUser = com.west2.demo.grpc.user.model.User.newBuilder()
                .setId(user.getId()).setUsername(user.getUsername()).setPassword(user.getPassword());
        UserResponse resp = UserResponse.newBuilder().setUser(respUser).setCode(200).build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
        log.info("end getById");
    }
    
    @Override
    public void add(UserSaveRequest request, StreamObserver<UserResponse> responseObserver) {
        log.info("start add");
        String id = request.getId();
        User user = new User();
        if (StringUtils.isEmpty(id)) {
            id = IdWorker.getIdStr();
        }
        user.setId(id);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        if (0 < userMapper.insert(user)) {
            log.info("insert success");
            user = userMapper.selectById(id);
            // 构造rpc响应
            com.west2.demo.grpc.user.model.User.Builder respUser = com.west2.demo.grpc.user.model.User.newBuilder()
                    .setId(user.getId())
                    .setUsername(user.getUsername())
                    .setPassword(user.getPassword());
            UserResponse resp = UserResponse.newBuilder().setUser(respUser).setCode(200).build();
            responseObserver.onNext(resp);
        }
        responseObserver.onCompleted();
        log.info("end add");
    }

}
```

### 实现client端

#### 网关模块使用依赖

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>grpc-demo</artifactId>
        <groupId>com.west2</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>grpc-demo-gateway</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.west2</groupId>
            <artifactId>grpc-demo-api</artifactId>
            <version>1.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.2.6.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <!-- grpc client和spring-boot集成框架 -->
        <dependency>
            <groupId>net.devh</groupId>
            <artifactId>grpc-client-spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>

</project>
~~~

#### 配置文件

~~~yml
#net.devh.boot.grpc.server.config.GrpcServerProperties
#grpc相关配置
grpc:
  client:
    userDemoClient:	# 与@GrpcClient注解相对应
      #禁用传输层安全(https://yidongnan.github.io/grpc-spring-boot-starter/zh-CN/client/security.html)
      negotiationType: PLAINTEXT
      #grpc服务地址配置(https://yidongnan.github.io/grpc-spring-boot-starter/zh-CN/client/configuration.html#configuration-via-properties)
      address: static://127.0.0.1:9091
server:
  port: 9092
spring:
  profiles:
    active: dev
  application:
    name: grpc-demo-client
~~~



#### 实现样例

网关模块作为rpc服务的客户端，接收各个服务端(如上面的user模块)传递的数据，并通过web服务以http协议将数据表达给访问网关的用户。

在这里(rpc客户端)我们只需要通过框架提供的注解 (`@GrpcClient("YourClientName")`这里的`YourClientName`由你自己在yml中配置的相对于即可) ，直接注入传递数据的对象，通过对象进行通信。

下面是一个Example：

~~~java
package com.west2.controller;

import com.alibaba.fastjson.JSONObject;
import com.west2.demo.grpc.user.model.UserResponse;
import com.west2.demo.grpc.user.model.UserSaveRequest;
import com.west2.demo.grpc.user.service.UserServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试写的UserController
 * @author 天狗
 */
@RestController
@Slf4j
public class UserController {

    @GrpcClient("userDemoClient")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    @GetMapping(path = "/hello")
    public String helloGrpc(@RequestParam(value = "name", defaultValue = "world") String name) {
        log.info("开始访问/hello路由");
        UserSaveRequest req = UserSaveRequest.newBuilder().setUsername("testuser").setPassword("114514").build();
        UserResponse resp;
        try {
            resp = blockingStub.add(req);
        } catch (StatusRuntimeException e) {
            log.error("RPC Failed: " + e.getMessage(), e);
            throw e;
        }
        log.error("testAdd结果: " + resp.getUser());
        return JSONObject.toJSONString(resp.toString());
    }

}
~~~





## TODO

有关golang使用grpc并跨语言通信还没做...

以后有时间再来补罢！

## Tips

完整样例项目见github仓库：https://github.com/SkyDDDog/grpc-demo.git

## 引用

> 官网grpc基础示例https://grpc.io/docs/languages/java/basics/