[TOC]



# 1、创建工程

这里采用idea社区版本的Spring Assistant快速构建

![image-20201126103727778](C:\Users\MSI -PC\AppData\Roaming\Typora\typora-user-images\image-20201126103727778.png)

构建完成后的项目目录

![image-20201126104014135](C:\Users\MSI -PC\AppData\Roaming\Typora\typora-user-images\image-20201126104014135.png)

 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.0</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.xxy</groupId>
	<artifactId>sbt</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>sbt</name>
	<description>Demo project for Spring Boot</description>

	<properties>
<!--		配置java版本-->
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
<!--		spring boot web启动器-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```

编写测试hello案列

在SbtApplication同级目录中新建controller文件夹，新建HelloController.class文件

```java
package com.xxy.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {
    @RequestMapping("hello")
    public String hello() {
        return "Hello World";
    }
}
```

在resources的application.properties中添加

```properties
server.port=8080
server.servlet.context-path=/sbt
```

启动项目

在浏览器输入http://127.0.0.1:8080/sbt/hello/hello 便可以看到"Hello world"输出了

# 2、spring-boot相关属性

属性文件：默认是application.properties或者application.yml

在application.properties中添加 jdbc属性

```properties
server.port=8080
server.servlet.context-path=/sbt
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://192.168.23.171:3306/xxy
jdbc.username=root
jdbc.password=123456
```

![image-20201126114018442](C:\Users\MSI -PC\AppData\Roaming\Typora\typora-user-images\image-20201126114018442.png)

在类上通过@ConfigurationProperties注解声明当前类为属性读取类

prefix="jdbc"读取属性文件中前缀为jdbc的值

新建JdbcConfig类

```java
package com.xxy.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcConfig {
    @Bean
    public DataSource dataSource(JdbcProperties jdbc) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbc.getUrl());
        dataSource.setDriverClassName(jdbc.getDriverClassName());
        dataSource.setUsername(jdbc.getUsername());
        dataSource.setPassword(jdbc.getPassword());
        return dataSource;
    }
}
```

更优雅方式的注入

我们直接把 @ConfigurationProperties(prefix = "jdbc") 声明在需要使用的 @Bean 的方法上，然后Spring Boot就会自动调用这个Bean（此处是DataSource）的set方法，然后完成注入。使用的前提是：该类必须有对应属 性的set方法！

```java
package com.xxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcConfig {
    @Bean
    @ConfigurationProperties(prefix = "jdbc")
    public DataSource dataSource(JdbcProperties jdbc) {
        return new DruidDataSource(); q q
    }
}

```

# 3、自动配置原理

## 3.1@SpringBootApplication

该注解包含

### 3.1.1@SpringBootConfiguration

在这个注解上面，又有一个 @Configuration 注解。这个注 解的作用就是声明当前类是一个配置类，然后Spring会自动扫描到添加了 @Configuration 的类，并且读取其中的配 置信息。而 @SpringBootConfiguration 是来声明当前类是SpringBoot应用的配置类，项目中只能有一个。所以一 般我们无需自己添加。

### 3.1.2@EnableAutoConfiguration

第二级的注解 @EnableAutoConfiguration ，告诉Spring Boot基于你所添加的依赖，去“猜测”你想要如何配 置Spring。比如我们引入了 spring-boot-starter-web ，而这个启动器中帮我们添加了 tomcat 、 SpringMVC 的依赖。此时自动配置就知道你是要开发一个web应用，所以就帮你完成了web及SpringMVC的默认配置了！

### 3.1.3@ComponentScan

配置组件扫描的指令。提供了类似与  标签的作用 通过basePackageClasses或者basePackages属性来指定要扫描的包。如果没有指定这些属性，那么将从声明 这个注解的类所在的包开始，扫描包及子包

# 4 SpringBoot实践

## lombok

@Data：自动提供getter和setter等方法

@Getter:自动提供getter

@Setter:自动提供setter

@Slf4j:自动提供log变量

```xml
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
</dependency>
```



## 静态资源目录

classpath:/static下

##  拦截器

新建拦截器类

```java
package com.xxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("这是拦截器的preHandle方法");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("这是拦截器的postHandle方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("这是拦截器的afterCompletion方法");
    }
}

```

定义配置类

```java
package com.xxy.config;

import com.xxy.interceptor.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 将拦截器注册到spring ioc容器
     */
    @Bean
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }

    /**
     * 重写该方法：往拦截器链添加自定义拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 通过registry添加myInterceptor拦截器
        registry.addInterceptor(myInterceptor()).addPathPatterns("/*");
    }
}

```

## 整合jdbc和事务

添加jdbc启动器和数据库驱动

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.49</version>
		</dependency>
```

## 整合连接池

在application.properties中添加

```properties
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.23.171:3306/xxy
spring.datasource.username=root
spring.datasource.password=123456
```

注释掉JdbcConfig类或者删除

## 整合mybatis

pom.xml添加依赖

```xml
<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>2.0.1</version>
		</dependency>
<!--		引入通用mapper-->
		<dependency>
			<groupId>tk.mybatis</groupId>
			<artifactId>mapper-spring-boot-starter</artifactId>
			<version>2.1.5</version>
		</dependency>
```

application.properties中添加如下配置

```properties
#mybatis
# 实体包别名包路径
mybatis.type-aliases-package=com.xxy.pojo
# 映射文件路径
# mybatis.mapper-locations=classpath:mappers/*
#控制台输出执行sql
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

配置mapper扫描

在每个Mapper接口类中添加@Mapper注解

或者在启动类上添加@MapperScan("com.xxy.mapper")

## 真实调用

user.java pojo

```java
package com.xxy.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "student")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Integer age;
}

```

UserController.java  controller

```java
package com.xxy.controller;

import com.xxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据id 获取用户
     * @param id
     * @return 用户的string
     */
    @RequestMapping("{id}")
    public String queryById(@PathVariable Long id) {
        return userService.queryById(id).toString();
    }
}

```

userService.java service

```java
package com.xxy.service;

import com.xxy.mapper.UserMapper;
import com.xxy.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User queryById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public void saveUser(User user) {
        log.info("新增用户:"+user.toString());
        userMapper.insertSelective(user);
    }
}

```

UserMapper.java mapper

```java
package com.xxy.mapper;

import com.xxy.pojo.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
}
```

浏览器调用

http://127.0.0.1:8080/sbt/user/1


