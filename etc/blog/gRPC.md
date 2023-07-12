## GRPC介绍

> [gRPC官网](https://grpc.io/)
>
> [gRPC 官方文档中文版_V1.0 (oschina.net)](http://doc.oschina.net/grpc)

### RPC协议介绍

RPC 全称为 Remote Procedure Call , 远程过程调用。这是一种协议，是用来屏蔽分布式计算中的各种调用细节，使你能够像调用本地方法一样，直接调用一个远程的函数。可以理解为一种规范。

下面简单说明一下客户端与服务端的沟通过程，以此来引出rpc的作用

**客户端 与 服务器 沟通的过程**: 

* 客户端 发送 数据 (字节流)
* 服务器 接受并解析。根据**约定**知道需要执行上面。把结果返回给客户

RPC: 

* 封装上述过程，使操作更加优化便捷
* 使用大家都认可的协议，使其规范化
* 做成一些框架，直接或间接产生利益

### gRPC框架介绍

![image-20230120131739507](https://gitee.com/sky-dog/note/raw/master/img/202301201331160.png)

​		而gRPC是一种开源的，高性能的通用RPC框架，能够跨平台支持多种语言。(隔壁Kitex目前还只能支持unix平台)

​		在gRPC中，我们称调用方为client，被调用方为server。跟其他RPC框架一样，gRPC也是基于"服务定义"的思想。简单的来说，就是我们通过某种方式来描述一个服务，这种描述方式是语言无关的。在这个"服务定义"的过程种，我们描述了我们提供的服务名是什么，有哪些方法开源被调用，有什么样的入参，有什么样的回参

​		而就是说，在定义好了这些服务、方法之后，**gRPC会频闭底层的细节，client只需要直接调用定义好的方法，就能够拿到预期返回的结果。**对于server端来说，只需要实现我们定义的方法。同样的，gRPC也会帮助我们屏蔽底层的细节，我们只需要实现所定义方法的具体逻辑即可。

​		你可以发现，在上面的描述过程种，所谓的"服务定义"，就跟定义接口是类似的。我更愿意理解为是一种"约定"，双方规定好接口，然后server实现接口，client调用接口，至于其他的细节就交给gRPC管理

​		此外，gRPC还是语言无关的，我们开源使用C++开发服务端，使用Golang、Java等作为客户端来调用服务器的接口。为了实现这一点，我们在"服务定义"和在编码解码过程中，同样应该做到语言无关。

![image-20230120133428187](https://gitee.com/sky-dog/note/raw/master/img/202301201334224.png)

### ProtoBuf介绍

​		因此，gRPC使用了Protocol Buffss(谷歌开发的一套成熟的数据结构序列化机制)

​		我们可以把他当成一个代码生成工具以及序列化工具。这个工具可以把我们定义的方法，转换成特定语言的代码。比如你定义了一种类型的参数，他会帮你转换成Golang种的结构体，你定义的方法，体会帮你转换成func函数。此外，在发送请求和接受响应的时候，这个工具还会完成对应的编码和解码工作，将你即将发送的数据编码成gRPC能够传输的形式，又或者将即将接受到的数据解码成编程语言所能理解的数据格式。

​		可能有些同学对序列化和反序列化的概念不太理解，下面简单解释一下这两个概念。

* 序列化: 将数据结构或对象转换成二进制串的过程
* 反序列化: 将在序列化过程中产生的二进制串转换成数据结构或对象的过程



​		而protobuf是谷歌开源的一种数据格式，适合高性能，对想要速度有要求的数据传输场景。因为protobuf是二进制数据格式，需要编码和解码。数据本身不具有可读性。因此只能反序列化之后得到真正可读的数据。

​		相比于其他数据结构，他有着这样几点优势:

* 序列化后体积相比Json和XML很小，更适合网络传输
* 支持跨平台多语言
* 消息格式升级和兼容性好
* 序列化和反序列化速度很快



## Proto

### 安装ProtoBuf

1、下载protocol buffers [Releases · protocolbuffers/protobuf (github.com)](https://github.com/protocolbuffers/protobuf/releases/)

* Protocol buffers，通常称为 Protobuf，是Google开发的一种协议，可以对结构数据进行序列化和反序列化操作，在网络通信中很有用
* 我们下载对应操作系统的zip文件，解压，并将bin目录配置到环境变量中即可
* 最后在cmd或bash中输入`protoc`命令查看是否安装并配置成功

2、安装`gRPC`核心库

~~~go
go get google.golang.org/grpc
~~~

3、安装protocol编译器。它开源生成各种不同语言的代码。因此，除了这个编译器，我们还需要配合各个语言的代码生成工具。对于Golang来说，这个工具是`protoc-gen-go`。

​		这里有个小坑，`github.com/golang/protobuf/protoc-gen-go`和`google.golang.org/protobuf/cmd/protoc-gen-go`是不同的！前者是旧版本，后者是Google接管的新版本，他们的API是不同的，用于生成的命令，生成的文件都是不一样的。由于目前的`gRPC-go`源码中的example是使用后者的生成方式，所以我们也采用google版本。

下面我们通过命令安装两个库(因为这些文件在安装grpc时以及下载了，这里只需要install即可)

~~~go
go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest
~~~

### Proto文件编写

下面简单写一个小demo，新建两个文件夹，分别作为客户端和服务端。

![image-20230120174308269](https://gitee.com/sky-dog/note/raw/master/img/202301201743327.png)

proto文件内容如下(可以当作模板记下来)

~~~protobuf
// 使用proto3语法
syntax = "proto3";

// 生成的go文件处于哪个目录哪个包中
// 这里声称在当前目录，service包中
option go_package = ".;service";

// 我们需要定义一个服务，在服务中需要有一个方法，这个方法可以接收客户端参数，返回服务端响应
// 其实很容易可以看出，我们定义了一个service，称为SayHello，这个服务有一个rpc方法，名为SayHello
// 这个方法会发送一个HelloRequest返回一个HelloResponse
service SayHello {
  rpc SayHello(HelloRequest) returns (HelloResponse) {}
}

// message关键字，可以理解为结构体
// 这个比较特别的是变量后的"赋值"(这里并不是赋值，而是定义这个变量在message中的位置)
message HelloRequest {
  string requestName = 1;
//  int64 age = 2;
}

message HelloResponse {
  string responseMsg = 1;
}
~~~

接下来可以通过protoc生成对应语言的代码，打开Terminal，进入proto目录，输入一些代码即可

两个命令(当作模板可以记下)

~~~protobuf
protoc --go_out=. hello.proto
protoc --go-grpc_out=. hello.proto

protoc -I internal/service/pb internal/service/pb/*.proto --go_out=.
protoc -I internal/service/pb internal/service/pb/*.proto --go-grpc_out=.
~~~

输入完可以发现在proto目录下生成了两个文件，我们使用时只需要重写或修改其中的我们定义的方法，加上业务逻辑即可



### Proto文件介绍

#### message

message: protobuf 中一个定义消息类型是通过关键字 message 指定的。**消息就是需要传输的数据格式的定义**

message 关键字类似于C++/Java中的class，C/Go中的struct

在消息中承载的数据分别对应每个字段，其中每个字段都有一个名字和一种类型

一个proto文件可以定义多个消息类型

#### 字段规则

* `required`: 消息体中必填字段。不设置会导致编码异常。**在protobuf2中使用，在protobuf3中被删去。**

* `optional`: 消息体中可选字段。**protobuf3中没有了reuired，optional等说明关键字，都默认为optional。**
* `repeated`: 消息体中可重复字段。重复的值的顺序会被保留，在go中重复的会被定义为切片。

#### 消息号

在消息体的定义中，**每个字段都必须要有一个唯一的标识号**，标识号是[1, 2^29-1]范围内的一个整数。

形式看上去与"赋值"类似。

#### 嵌套消息

可以在其他消息类型中定义、使用消息类型，在下面的例子中，person消息就定义在PersonInfo消息内

~~~protobuf
message PersonInfo{
	message Person{
		string name = 1;
		int32 height = 2;
		repeated int32 weight = 3;
	}
	repeated Person info = 1;
}
~~~

如果要在父消息外重用这个消息类型，需要使用PersonInfo.Person的形式来使用它，如:

~~~protobuf
message PersonMessage{
	PersonInfo.Person info = 1;
}
~~~

#### 服务定义

如果要将消息类型用在RPC系统，可以在.proto文件中定义一个RPC服务接口，protocol buffer编译器将会根据所选择的不同语言生成服务接口代码及存根。

~~~protobuf
service SearchService{
	# rpc 服务函数名 (参数) 返回 (返回参数)
	rpc Search(SearchRequest) returns (RequestResponse)
}
~~~

## 服务端编写

* 创建gRPC Server对象，你可以理解为Server端的抽象对象

* 将 server (其包含需要被调用的服务端接口) 注册到 gRPC Server 的内部注册中心

  这样可以在接受到请求时，通过内部的服务发现，发现该服务端接口并转接进行逻辑处理

* 创建 Listen，监听TCP端口

* gRPC Server 开始 lis.Accept，直到 Stop

下面给出以之前写的Hello服务为例，实现以下服务端的编写

~~~go
// 实现生成代码中未实现的服务
// hello Server
type server struct {
	pb.UnimplementedSayHelloServer
}

func (s *server) SayHello(ctx context.Context, req *pb.HelloRequest) (*pb.HelloResponse, error) {
	return &pb.HelloResponse{ResponseMsg: "hello" + req.RequestName}, nil
}
~~~

注册并开启rpc服务

~~~go
func main() {
	// 开启端口
	listen, err := net.Listen("tcp", ":9090")
	if err != nil {
		panic("port open failed")
	}
	// 创建grpc服务
	grpcServer := grpc.NewServer()
	// 在grpc服务端中注册我们自己编写的服务
	pb.RegisterSayHelloServer(grpcServer, &server{})
	// 启动服务
	err = grpcServer.Serve(listen)
	if err != nil {
		panic("service open failed")
	}
}
~~~



## 客户端编写

* 创建与给定目标（服务端）的连接交互
* 创建 server 的服务端对象
* 发送 RPC 请求，请求同步响应，得到回调后返回响应结果
* 输出响应结果

同样以刚刚的Hello服务为例，编写一下客户端侧的代码demo

~~~go
func main() {
	// 连接服务
	// 第二个参数为安全配置(这里新建了空的加密配置，即不进行安全加密)
	conn, err := grpc.Dial("127.0.0.1:9090", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		panic("fail to connect server")
	}
	// 关闭grpc连接，都记得关闭！
	defer conn.Close()
	// 与服务端建立连接
	client := pb.NewSayHelloClient(conn)
	resp, _ := client.SayHello(context.Background(),
		&pb.HelloRequest{
			RequestName: "二火",
		})
	fmt.Println(resp.GetResponseMsg())
}
~~~



我们分别启动服务端和客户端，可以看见:

![image-20230120203203783](https://gitee.com/sky-dog/note/raw/master/img/202301202032871.png)



## 认证-安全传输

### 介绍

gRPC 是一个典型的 C/S 模型，需要开发客户端和服务端，客户端与服务端需要达成协议，使用某一个确认的传输协议来传输数据，**gRPC通常默认是使用protobuf来作为传输协议**，当然也是可以使用其他自定义的。

![image-20230120203523973](https://gitee.com/sky-dog/note/raw/master/img/202301202035083.png)

​		那么，客户端与服务端要通信之前，客户端如何知道自己的数据是发给哪一个明确的服务端的呢？反过来，服务端是否也需要有一种方式来弄清楚自己的数据要返回给谁呢？

​		那么就不得不提到gRPC的认证

​		此处说到的认证，不是用户的身份认证，而是指多个server和多个client之间，如何识别对方是谁，并且可以安全地进行数据传输

* SSL / TLS 认证 (采用http2协议)
* 基于Token的认证方式 (基于安全链接)
* 不采用任何措施的连接，这是不安全的连接 (默认采用http1)
* 自定义的身份认证

客户端和服务端之间调用，我们可以通过加入证书的方式，实现调用的安全性

TLS (Transport Layer Security , 安全传输层)，TLS是建立在传输层TCP协议之上的协议，服务于应用层，它的前身是SSL (Secure Socket Layer , 安全套接字层)，它实现了将应用层的报文进行加密后再交由TCP进行传输的功能。

TLS协议主要解决如下三个网络安全问题。

* 保密(message privacy)，保密通过加密encryption实现，所有信息都加密传输，第三方无法嗅探
* 完整性(message integrity)，通过MAC校验机制，一旦被篡改，通信双方会立刻发现
* 认证(mutual authentication)，双方认证，双方都可以配备证书，防止身份被冒充

**生产环境可以购买证书或使用一些平台发放的免费证书**

下面解释一些有关证书的概念: 

* key: 服务器上的私钥文件，用于对发送给客户端数据的加密，以及对客户端接受到数据的解密。
* csr: 证书签名请求文件，用于提交给证书颁发机构(CA)，
* crt: 由证书办法机构(CA)签名后的证书，或是开发者自签名的证书，包含证书持有人的信息，持有者的公钥，以及签署者的签名等信息。
* pem: 是基于Base64编码的证书格式，扩展名包括pem、crt、cer

### SSL / TLS 认证

首先通过`openSSL`生成证书和私钥

* [openssl官网](https://www.openssl.org/source)下载 

  其他人做的[便捷版安装包](http://slproweb.com/products/Win32OpenSSL.html)

* 我们使用便捷版安装包，一直下一步即可
* 将bin目录配置到环境变量
* 命令行测试 openssl

~~~shell
# 1、生成私钥
openssl genrsa -out server.key 2048

# 2、生成证书 全部回车、可以不填
openssl req -new -x509 -key server.key -out server.crt -days 36500
# 国家名称
Country Name (2 letter code) [AU]:CN
# 省名称
State or Province Name (full name) [Some-State]:Fujian
# 城市名称
Locality Name (eg, city) []:Fuzhou
# 公司组织名称
Organization Name (eg, company) [Internet widgits Pty Ltd]: ByteDance
# 部门名称
Organizational Unit Name (eg, section) []:go
# 服务器or网站名称
Organizational Unit Name (e.g. server FQDN or YOUR name) []:dousheng
# 邮件
Email Address []:362664609@qq.com

# 3、生成csr
openssl req -new -key server.key -out server.csr
~~~

~~~shell
#更改openssl.cnf (Linux是openssl.cfg)
#1)从openssl/bin下将openssl.cnf复制到项目目录中
#2)找到 [ CA_default ], 打开 copy_extensions = copy (解除注释即可)
#3)找到 [ req ], 打开 req_extensions = v3_req # The extensions to add to a certificate request
#4)找到[ v3_req ], 添加 subjectAltName = @alt_names
#5)添加新的标签 [ alt_names ], 和标签字段
DNS.1 = *.skydog.ltd
~~~

~~~shell
# 生成证书私钥 test.key
openssl genpkey -algorithm RSA -out test.key

# 通过私钥test.key生成证书请求文件test.csr(注意cfg和cnf)
openssl req -new -nodes -key test.key -out test.csr -days 3650 -subj "/C=cn/OU=myorg/0=mycomp/CN=myname" -config ./openssl.cnf -extensions v3_req
#test.csr是上面生成的证书请求文件。ca.crt/server.key是CA证书文件和key，用来对test.csr进行签名认证。这两个文件在第一部分生成。

# 生成SAN证书 (pem)
openssl x509 -req -days 365 -in test.csr -out test.pem -CA server.crt -CAkey server.key -CAcreateserial -extfile ./openssl.cnf -extensions v3_req
~~~



#### 代码中添加认证

##### 服务端

~~~//
// TSL认证
// 两个参数分别是 cretFile, keyFile (自签名文件, 私钥文件)
creds, _ := credentials.NewServerTLSFromFile("F:\\Project\\GoProjects\\grpc-study\\key\\test.pem",
                                             "F:\\Project\\GoProjects\\grpc-study\\key\\test.key")
// 并在创建gRPC服务时，加入认证
grpcServer := grpc.NewServer(grpc.Creds(creds))
~~~

##### 客户端

~~~go
creds, _ := credentials.NewClientTLSFromFile("F:\\Project\\GoProjects\\grpc-study\\key\\test.pem",
                                             "*.skydog.ltd")
// 并在连接服务时请求认证
conn, err := grpc.Dial("127.0.0.1:9090", grpc.WithTransportCredentials(creds))
~~~

#### 

### Token认证

我们先看一个gRPC提供给我们的一个接口，这个接口中有两个方法，接口位于credentials包下，这个接口需要客户端来实现

~~~go
type PerPRCCredentials interface {
    // 获取元数据信息，也就是客户端提供的kv键值对，context用于控制超时与取消，uri是请求入口的uri
    GetRequestMetadata(ctx context.Context, uri ...string) (map[string] string, error)
    // 是否需要基于TLS认证进行安全传输，进入过返回值是true，则必须要上TLS验证，返回值是false则不用
    RequireTransportSecurity() bool
}
~~~

#### 客户端代码

定义并实现自己的Token解析类

~~~go
type PerPRCCredentials interface {
	// 获取元数据信息，也就是客户端提供的kv键值对，context用于控制超时与取消，uri是请求入口的uri
	GetRequestMetadata(ctx context.Context, uri ...string) (map[string]string, error)
	// 是否需要基于TLS认证进行安全传输，进入过返回值是true，则必须要上TLS验证，返回值是false则不用
	RequireTransportSecurity() bool
}

type ClientTokenAuth struct {
}

func (c ClientTokenAuth) GetRequestMetadata(ctx context.Context, uri ...string) (map[string]string, error) {
	return map[string]string{
		"appId":  "SkyDog",
		"appKey": "114514",
	}, nil
}
func (c ClientTokenAuth) RequireTransportSecurity() bool {
	return false
}
~~~

在客户端中添加自定义安全配置

~~~go
var opts []grpc.DialOption
opts = append(opts, grpc.WithTransportCredentials(insecure.NewCredentials()))
opts = append(opts, grpc.WithPerRPCCredentials(new(ClientTokenAuth)))
conn, err := grpc.Dial("127.0.0.1:9090", opts...)
~~~

#### 服务端

在服务端侧做好token校验工作

~~~go
// 在业务中获取元数据信息
md, ok := metadata.FromIncomingContext(ctx)
if !ok {
    return nil, errors.New("token not found")
}
var appId string
var appKey string
if v, ok := md["appId"]; ok {
    appId = v[0]
}
if v, ok := md["appKey"]; ok {
    appKey = v[0]
}
// 用户 对应-> appId
if appId != "SkyDog" || appKey != "114514" {
    return nil, errors.New("token invalid")
}
~~~































