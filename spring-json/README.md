# Spring3.2.X集成JSON
## 配置关键依赖
```xml
<properties>
        <spring.version>3.2.18.RELEASE</spring.version>
        <json.version>2.5.0</json.version>
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
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${json.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>${json.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
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

    <!-- 自动配置ByteArrayHttpMessageConverter、StringHttpMessageConverter、
        ResourceHttpMessageConverter、SourceHttpMessageConverter、
        XmlAwareFormHttpMessageConverter、Jaxb2RootElementHttpMessageConverter、
        MappingJacksonHttpMessageConverter7个转换器
    -->
    <mvc:annotation-driven/>

    <context:component-scan base-package="com.allenayo.sj"/>

</beans>
```
## 新建JSON转换工具类
```java
public class JsonUtil {

    public static String toJson(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(o);
    }

    public static <T> T toBean(String xmlString, Class<T> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(xmlString, clazz);
    }
}
```
## 测试HTTP请求与响应中JSON数据自动转换
```java
@RequestMapping("book")
@Controller
public class BookController {

    @ResponseBody
    @RequestMapping(value = "getBook", method = RequestMethod.GET)
    public Book getUser() {
        List<Chapter> chapters = new ArrayList<>(Arrays.asList(
                new Chapter(1L, "chapter1", "chapter1", 8),
                new Chapter(2L, "chapter2", "chapter2", 8)));
        return new Book(1L, "book1", "allenAyo", "eng", 10.50, 50, chapters);
    }

    @ResponseBody
    @RequestMapping(value = "postBook", method = RequestMethod.POST)
    public Book postUser(@RequestBody Book book) {
        Optional.of(book).flatMap(b -> Optional.of(b.getChapters())).ifPresent(System.out::println);
        return book;
    }
}
```