# Spring上传文件
## 配置依赖
```xml
<properties>
	<spring.version>3.1.2.RELEASE</spring.version>
	<json.version>1.9.13</json.version>
</properties>

<dependencies>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-web</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-webmvc</artifactId>
		<version>${spring.version}</version>
	</dependency>

   	<dependency>
		<groupId>commons-fileupload</groupId>
		<artifactId>commons-fileupload</artifactId>
		<version>1.3.2</version>
	</dependency>

	<dependency>
		<groupId>log4j</groupId>
		<artifactId>log4j</artifactId>
		<version>1.2.17</version>
	</dependency>

	<dependency>
		<groupId>org.codehaus.jackson</groupId>
		<artifactId>jackson-core-asl</artifactId>
		<version>${json.version}</version>
	</dependency>
	<dependency>
		<groupId>org.codehaus.jackson</groupId>
		<artifactId>jackson-mapper-asl</artifactId>
		<version>${json.version}</version>
	</dependency>
	<dependency>
		<groupId>org.codehaus.jackson</groupId>
		<artifactId>jackson-core-lgpl</artifactId>
		<version>${json.version}</version>
	</dependency>
	<dependency>
		<groupId>org.codehaus.jackson</groupId>
		<artifactId>jackson-mapper-lgpl</artifactId>
		<version>${json.version}</version>
	</dependency>

	<dependency>
		<groupId>javax.servlet</groupId>
		<artifactId>servlet-api</artifactId>
		<version>3.0-alpha-1</version>
		<scope>provided</scope> <!-- 在编译和测试时有效，打包时不会加入 -->
	</dependency>
</dependencies>
```
## Spring配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 扫描指定包下所有注解，自动注入 -->
    <context:component-scan base-package="com.allenayo.su"/>

    <!-- 配置上传文件 -->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 默认编码 -->
        <property name="defaultEncoding" value="utf8"/>
        <!-- 文件大小的最大值 -->
        <property name="maxUploadSize" value="10485760000"/>
        <!-- 内存中的最大值 -->
        <property name="maxInMemorySize" value="40960"/>
    </bean>

    <mvc:annotation-driven/>
</beans>
```
## Web配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!-- 需求加载的配置文件 -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:application-context.xml</param-value>
    </context-param>

    <!-- SpringMVC servlet -->
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:application-context.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/rest/*</url-pattern>
    </servlet-mapping>

    <!-- 配置编码过滤器 -->
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <async-supported>true</async-supported>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- Spring监听器 -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- 防止Spring内存溢出监听器 -->
    <listener>
        <listener-class>org.springframework.web.util.IntrospectorCleanupListener</listener-class>
    </listener>
</web-app>
```
## FileController.java
```java
package com.allenayo.su.controller;

import com.allenayo.su.domain.Resource;
import com.allenayo.su.domain.ResultVO;
import com.allenayo.su.util.DateUtil;
import com.allenayo.su.util.FileUtil;
import com.allenayo.su.util.RandomUtil;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * 上传文件
 */
@RequestMapping("file")
@Component
public class FileController extends Observable {

    private static final Logger logger = Logger.getLogger(FileController.class);

    private static final String BASE_DIR = "/data/files"; // 上传文件存储的根路径

    private static final String MODULE = "default"; // 上传文件所属的模块

    private static final String FILE_ACCESS_URL_PREFIX = ""; // 上传文件访问路径前缀

    @RequestMapping(
            value = "upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, // 指定接收MIME格式
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" // 指定返回MIME格式
    )
    public ResultVO upload(HttpServletRequest request) {
        // 上传文件存储的目录
        String dirPath = BASE_DIR + File.separator + MODULE + File.separator + DateUtil.getSysDate(DateUtil.YEAR_MONTH_DAY);
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        List<String> fileUrls = new ArrayList<>(); // 上传文件访问路径集合
        List<Resource> resources = new ArrayList<>(); // 上传文件集合

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断request是否有文件上传，即是否有多部分请求
        if (!multipartResolver.isMultipart(request)) {
            return new ResultVO(0, "请求错误！");
        }

        // 转换成多部分请求
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 取得request中所有的文件名
        Iterator<String> iter = multipartRequest.getFileNames();
        while (iter.hasNext()) {
            // 取得上传文件
            MultipartFile file = multipartRequest.getFile(iter.next());
            if (file == null) continue;

            // 上传文件的原名称
            String originalName = file.getOriginalFilename();
            // 上传文件的格式
            String ext = FileUtil.getExt(originalName);
            // 上传文件的新名称
            String newName = RandomUtil.uuid() + "." + ext;
            // 上传文件存储在服务器的地址
            String path = dir + File.separator + newName;
            // 上传文件访问路径
            String url = FILE_ACCESS_URL_PREFIX + newName;
            try {
                // 将上传文件存储在服务器中指定的位置
                file.transferTo(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultVO(1, "文件上传失败，原因: " + e.getMessage());
            }
            fileUrls.add(url);
            resources.add(new Resource(newName, ext, file.getSize(), path, url, DateUtil.getSysDateTime(), "system"));
            logger.info("文件上传成功，文件存储在：" + path);
        }

        if (fileUrls.size() == 0) {
            return new ResultVO(0, "请求错误！");
        }

        // 状态更新，通知所有观察对象
        this.setChanged();
        this.notifyObservers(resources);

        return new ResultVO(0, "文件上传成功！", fileUrls);
    }
}
```