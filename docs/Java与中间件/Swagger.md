# Swagger
```http
https://swagger.io
```

## 关于Swagger的几个问题
```txt
Q:什么是Swagger？
A:Swagger是一款RESTFUL接口的文档在线自动生成+功能测试功能软件。

Q:为什么要用Swagger？
A:试试就知道了。

Q:Swagger2和Swagger3有何不同？
A:Swagger3对于Spring有更多官方的支持，主要体现在包是官方的
  目前（2023.07）来说，二者使用体验是一样的，但是Swagger2已经停止维护了
```

## Swagger基础配置

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.*.*"})  //改成自己要配置的包
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
### 版本2.0
```http
http://domain:port/{context-path}/swagger-ui.html
```

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<!--外接doc文档-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.9.6</version>
</dependency>
```

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(environment.acceptsProfiles(Profiles.of("dev","test")))   //根据环境判断是否启用swagger
                .apiInfo(apiInfo())
                .select()
            	.group()	//可以设置多个docket
            	//扫描包
                .apis(RequestHandlerSelectors.basePackage("com.superdog.cloudnote.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云笔记api")
                .description("springboot | swagger")
                .version("0.0.1")
                .build();
    }

}
```

```java
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //配置swagger静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
```

### 版本3.0(目前2023.6.12与springboot3.0不完全兼容)
```http
http://domain:port/{context-path}/swagger-ui/index.html
```

```xml
      <dependency>
          <groupId>io.springfox</groupId>
          <artifactId>springfox-boot-starter</artifactId>
          <version>3.0.0</version>
      </dependency>
```

```java
@EnableOpenApi
@Configuration
public class Swagger3Config {
    @Bean
    public Docket createRestApi(Environment environment) {
        //版本选择OAS3
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                //根据环境判断是否启用Swagger
                .enable(environment.acceptsProfiles(Profiles.of("dev","test")))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cloudnote"))//扫描的包路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("***")
                .description("** | Springboot | Swagger3")
//                .termsOfServiceUrl("你的项目地址")
                .version("1.0")
//                .contact(new Contact("联系人", "个人网站地址", "邮箱"))
                .build();
    }
}
```
```yml
spring:
  profiles:
  #此处配置springboot的工作环境，可以条件性的开启Swagger
    active: dev
```

**关于多包配置（尽量不要多包）**
```java
@Configuration
@EnableSwagger2
public class Swagger2Config {
    private static final String SPLITOR = ";";

    /**
     * 切割扫描的包生成Predicate<RequestHandler>
     *
     * @param basePackage
     * @return
     */
    public static Predicate<RequestHandler> scanBasePackage(final String basePackage) {
        if (StrUtil.isBlank(basePackage)) {
            throw new NullPointerException("basePackage不能为空，多个包扫描使用" + SPLITOR + "分隔");
        }
        String[] controllerPack = basePackage.split(SPLITOR);
        Predicate<RequestHandler> predicate = null;
        for (int i = controllerPack.length - 1; i >= 0; i--) {
            String strBasePackage = controllerPack[i];
            if (StrUtil.isNotBlank(strBasePackage)) {
                Predicate<RequestHandler> tempPredicate = RequestHandlerSelectors.basePackage(strBasePackage);
                predicate = predicate == null ? tempPredicate : Predicates.or(tempPredicate, predicate);
            }
        }
        if (predicate == null) {
            throw new NullPointerException("basePackage配置不正确，多个包扫描使用" + SPLITOR + "分隔");
        }
        return predicate;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(scanBasePackage("com.lczyfz;com.superdog.springboot"))//扫描的包路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("***")
                .description("Springboot基础开发工程swagger-api文档")
                .termsOfServiceUrl("https://github.com/***")
                .version("1.0")
                .contact(new Contact("Maple", "https://github.com/***", "example@gmail.com"))
                .build();
    }
}
```
```java
@ComponentScan(basePackages = {"com.superdog.springboot.*","com.lczyfz.*"})
//启动项配置
```


##Swagger注解
### @Api

```java
/**
*	作用在类，接口，枚举类上
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Api {
    /**
     *	value即tags
     */
    String value() default "";

    /**
     * API文档显示的标记
     */
    String[] tags() default "";

    /**
     * 不用
     */
    @Deprecated String description() default "";

    /**
     * 不用
     */
    @Deprecated String basePath() default "";

    /**
     * 不用
     */
    @Deprecated int position() default 0;

    /**
     * 指定返回的内容类型，即仅当请求头的 Accept 类型中包含该指定类型才返回，例如:application/json
     */
    String produces() default "";

    /**
     * 指定返回的内容类型，即仅当请求头的 Accept 类型中包含该指定类型才返回，例如:application/json
     */
    String consumes() default "";

    /**
     * 标识当前的请求支持的协议，例如：http、https、ws
     */
    String protocols() default "";

    /**
     * 对应于操作对象的“security”字段。
     * 接收此资源下操作的授权（安全要求）列表。这可能会被特定操作覆盖。
     */
    Authorization[] authorizations() default @Authorization(value = "");

    /**
     * 隐藏此资源下的操作
     */
    boolean hidden() default false;
}
```

### @ApiIgnore

```java
/**
* 作用在类，方法，参数
* 用于过滤或者说屏蔽部分资源
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface ApiIgnore {
    
  String value() default "";
    
}

```

### @ApiOperation

```java
/**
* 作用在方法，类
*/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {
    /**
     * value即tags
     */
    String value();

    /**
     * 对应于操作的“注释”字段。
     */
    String notes() default "";

    /**
     * 对应操作的标记
     */
    String[] tags() default "";

    /**
     * 操作的响应类型
     * 在JAX-RS应用程序中，方法的返回类型将自动使用，除非它是javax.ws.RS.core.Response。在这种情况下，操作返回类型将默认为“void”，因为实际的响应类型      *未知。
     * 如果使用的值是表示基元（Integer，Long，…）的类，则将使用相应的基元类型。
     */
    Class<?> response() default Void.class;

    /**
     * 说明包装的容器，默认情况下，有效值为 List、Set、Map；在定义通用 Response 后，一般用不上
     */
    String responseContainer() default "";

    /**
     * Specifies a reference to the response type. The specified reference can be either local or remote and
     * will be used as-is, and will override any specified response() class.
     */

    String responseReference() default "";

    /**
     * 与所使用的HTTP方法的“方法”字段相对应。
	 *如果没有说明，在JAX-RS应用程序中，将扫描并使用以下JAX-RS注释：@GET、@HEAD、@POST、@PUT、@DELETE和@OPTIONS。请注意，即使不是JAX-RS规范的一部      *分，如果您创建并使用@PATCH注释，它也将被解析和使用。如果设置了httpMethod属性，它将覆盖JAX-RS注释。
	 *对于Servlet，必须手动指定HTTP方法。
	 *可接受的值为“GET”、“HEAD”、“POST”、“PUT”、“DELETE”、“OPTIONS”和“PATCH”。
     */
    String httpMethod() default "";

    /**
     * Not used in 1.5.X, kept for legacy support.
     */
    @Deprecated int position() default 0;

    /**
     * Corresponds to the `operationId` field.
     * <p>
     * The operationId is used by third-party tools to uniquely identify this operation. In Swagger 2.0, this is
     * no longer mandatory and if not provided will remain empty.
     */
    String nickname() default "";

    /**
     * 参考@Api
     */
    String produces() default "";

    /**
     * 参考@Api
     */
    String consumes() default "";

    /**
     * 参考@Api
     */
    String protocols() default "";

    /**
     * Corresponds to the `security` field of the Operation Object.
     * <p>
     * Takes in a list of the authorizations (security requirements) for this operation.
     *
     * @return an array of authorizations required by the server, or a single, empty authorization value if not set.
     * @see Authorization
     */
    Authorization[] authorizations() default @Authorization(value = "");

    /**
     * 参考@Api
     */
    boolean hidden() default false;

    /**
     * 响应旁边提供的可能的标头列表。
     */
    ResponseHeader[] responseHeaders() default @ResponseHeader(name = "", response = Void.class);

    /**
     * 响应的HTTP状态代码。
	 *该值应该是一个正式的HTTP状态代码定义。
     */
    int code() default 200;

    /**
     * @return an optional array of extensions
     */

    Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));

    /**
     * 解析操作和类型时忽略JsonView注释。为了向后兼容性
     */
    boolean ignoreJsonView() default false;
```

### @ApiImplicitParams

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiImplicitParams {
    /**
     * A list of {@link ApiImplicitParam}s available to the API operation.
     */
    ApiImplicitParam[] value();
}
```

### @ApiImplicitParam

```java
//作用在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiImplicitParam {
    /**
     * 参数的名称。
	 *为了获得正确的Swagger功能，在基于paramType（）命名参数时，请遵循以下规则：
	 *如果paramType为“path”，则名称应为路径中的关联节。
	 *对于所有其他情况，名称应该是应用程序期望接受的参数名称
     */
    String name() default "";

    /**
     * 参数的简要说明。
     */
    String value() default "";

    /**
     * 描述参数的默认值。
     */
    String defaultValue() default "";

    /**
     * 限制此参数的可接受值。
	 *有三种方法可以描述允许值：
	 *若要设置值列表，请提供逗号分隔的列表。例如：first, second, third
	 *若要设置值的范围，请以“range”开头，方括号括起的值包括最小值和最大值，或圆括号表示排除的最小值和最高值。例如：range[1, 5], range(1, 5), range[1, 5)。
	 *要设置最小/最大值，请使用与范围相同的格式，但使用"infinity" or "-infinity"作为第二个值。例如，range[1，infinity]表示该参数的最小允许值为1。
     */
    String allowableValues() default "";

    /**
     * 指定是否需要该参数。
     */
    boolean required() default false;

    /**
     * 允许从API文档中筛选参数。
     */
    String access() default "";

    /**
     * 指定参数是否可以通过多次出现来接受多个值。
     */
    boolean allowMultiple() default false;

    /**
     * 参数的数据类型。
	 *这可以是类名，也可以是基元。
     */
    String dataType() default "";

    /**
     * 参数的类。
     */
    Class<?> dataTypeClass() default Void.class;

    /**
     * 参数的参数类型。
     */
    String paramType() default "";

    /**
     * 非实体类型参数的单个示例
     */
    String example() default "";

    /**
     * 参数示例。仅适用于BodyParameters
     */
    Example examples() default @Example(value = @ExampleProperty(mediaType = "", value = ""));

    /**
     * 添加覆盖检测到的类型的功能
     */
    String type() default "";

    /**
     * 添加了提供自定义格式的功能
     */
    String format() default "";

    /**
     * 添加将格式设置为空的功能
     */
    boolean allowEmptyValue() default false;

    /**
     * 增加了被指定为只读的能力。
     */
    boolean readOnly() default false;

    /**
     * 添加了用“array”类型覆盖collectionFormat的功能
     */
    String collectionFormat() default "";
```

### @ApiParam

作用在参数上，可以参考上面的@ApiImplicitParam

### @ApiResponses

类似@ApiImplicitParams，是@ApiResponse的集合

### @ApiResponse

```java
//作用在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponse {
    /**
     * 响应的HTTP状态代码。
     */
    int code();

    /**
     * 响应附带的可读信息。
     */
    String message();

    /**
     * 用于描述消息有效负载的可选响应类。
	 * 对应于响应消息对象的“schema”字段。
     */
    Class<?> response() default Void.class;

    /**
     * Specifies a reference to the response type. The specified reference can be either local or remote and
     * will be used as-is, and will override any specified response() class.
     */

    String reference() default "";

    /**
     * 响应旁边提供的可能的标头列表。
     */
    ResponseHeader[] responseHeaders() default @ResponseHeader(name = "", response = Void.class);

    /**
     * 声明一个包装响应的容器。
     * 有效值为“List”、“Set”或“Map”。任何其他值都将被忽略。
     */
    String responseContainer() default "";

    /**
     * 响应示例
     */
    Example examples() default @Example(value = @ExampleProperty(value = "", mediaType = ""));
}
```

### @ApiModel

```java
//作用在实体类上
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiModel {
    /**
     * 提供模型的替代名称。
	 * 默认情况下，将使用类名。
     */
    String value() default "";

    /**
     * 提供类的详细说明。
     */
    String description() default "";

    /**
     * 为模型提供一个父类，以允许描述继承。
     */
    Class<?> parent() default Void.class;

    /**
     * 支持model 继承和多态性。
	 * 这是用作鉴别器的字段的名称。基于该字段，可以断言需要使用哪个子类型。
     */
    String discriminator() default "";

    /**
     * 从该模型继承的子类型的数组。
     */
    Class<?>[] subTypes() default {};

    /**
     * 指定对相应类型定义的引用，覆盖指定的任何其他元数据
     */

    String reference() default "";
}
```

### @ApiModelProperty

```java
//一般作用在实体类的属性上
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelProperty {
    /**
     * 此属性的简要说明。
     */
    String value() default "";

    /**
     * 允许重写属性的名称。
     */
    String name() default "";

    /**
     * 参考@ApiImplicitParam
     */
    String allowableValues() default "";

    /**
     * Allows for filtering a property from the API documentation. See io.swagger.core.filter.SwaggerSpecFilter.
     */
    String access() default "";

    /**
     * Currently not in use.
     */
    String notes() default "";

    /**
     * 参数的数据类型。
     */
    String dataType() default "";

    /**
     * 指定是否需要该参数。
     */
    boolean required() default false;

    /**
     * 允许显式排序模型中的特性。
     */
    int position() default 0;

    /**
     * 允许在Swagger模型定义中隐藏模型特性。
     */
    boolean hidden() default false;

    /**
     * 属性的示例值。
     */
    String example() default "";

    /**
     * 弃用
     */
    @Deprecated
    boolean readOnly() default false;

    /**
     * 允许指定模型属性的访问模式（AccessMode.READ_ONLY、READ_WRITE）
     */
    AccessMode accessMode() default AccessMode.AUTO;


    /**
     * Specifies a reference to the corresponding type definition, overrides any other metadata specified
     */

    String reference() default "";

    /**
     * 允许传递空值
     */
    boolean allowEmptyValue() default false;

    /**
     * @return an optional array of extensions
     */
    Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));

    enum AccessMode {
        AUTO,
        READ_ONLY,
        READ_WRITE;
    }
}
```

## Swagger示例代码
```java
@Api(tags = "考试管理系统试题接口")
@RestController
@RequestMapping("/${apiPath}/es/element")
public class EsElementController extends BaseController {

    @ApiOperation(value = "添加试题", notes = "Request-DateTime样例：2018-09-29 11:26:20")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Access-Token",
                    value = "访问token",
                    paramType = "header",
                    dataType = "string",
                    required = true),
            @ApiImplicitParam(
                    name = "Request-DateTime",
                    value = "发起请求时间",
                    paramType = "header",
                    dataType = "date",
                    required = true,
                    defaultValue = "2023-01-01 00:00:00"),
            @ApiImplicitParam(
                    name = "username",
                    value = "用户名",
                    paramType = "query",
                    dataType = "string",
                    required = true,
                    defaultValue = "test"),
            @ApiImplicitParam(
                    name = "password",
                    value = "密码",
                    paramType = "query",
                    dataType = "string",
                    required = true,
                    defaultValue = "123456")
    })
    @PostMapping
    public CommonResult add(@Validated(Create.class) @RequestBody EsElementVO esElementVO, BindingResult bindingResult,
                            @RequestParam String username, @RequestParam String password) {

        //业务操作
    }
}
```
```java
@ApiModel(value = "EsElement", description = "EsElement")
public class EsElement {

    @ApiModelProperty(value = "出题人")
    private String owner;
}
```

