# Swagger
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.*.*"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

## 版本2.0
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

## 版本3.0(目前2023.6.12与springboot3.0不完全兼容)
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
