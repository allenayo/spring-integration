# Spring3.2.X集成Jersey1.X
## 配置关键依赖
```xml
<properties>
        <spring.version>3.2.18.RELEASE</spring.version>
        <log4j.version>1.2.17</log4j.version>
        <jersey.version>1.19.1</jersey.version>
        <servlet-api.version>3.0-alpha-1</servlet-api.version>
    </properties>

    <dependencies>
        <!-- spring核心依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!-- spring web依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!-- jersey（REST框架）依赖 -->
        <dependency>
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-server</artifactId>
            <version>${jersey.version}</version>
        </dependency>
        <dependency> <!-- 上传文件 -->
            <groupId>com.sun.jersey.contribs</groupId>
            <artifactId>jersey-multipart</artifactId>
            <version>${jersey.version}</version>
        </dependency>
        <dependency> <!-- JavaBean转JSON -->
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-json</artifactId>
            <version>${jersey.version}</version>
        </dependency>

        <!-- spring + jersey-->
        <dependency>
            <groupId>com.sun.jersey.contribs</groupId>
            <artifactId>jersey-spring</artifactId>
            <version>${jersey.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-web</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-beans</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-context</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-asm</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- servlet容器依赖 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>${servlet-api.version}</version>
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
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">

    <!-- 扫描指定包下所有注解，自动注入 -->
    <context:component-scan base-package="com.allenayo.sj"/>

</beans>
```
## Web配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

    <display-name>Archetype Created Web Application</display-name>

    <context-param>
        <param-name>log4jConfigLocation</param-name>
        <param-value>classpath:log4j.properties</param-value>
    </context-param>

    <context-param>
        <param-name>log4jRefreshInterval</param-name>
        <param-value>10000</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>jersey-servlet</servlet-name>
        <servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
        <init-param>
            <param-name>com.sun.jersey.config.property.packages</param-name>
            <param-value>com.cjp.resource</param-value>
        </init-param>
        <init-param>
            <param-name>com.sun.jersey.api.json.POJOMappingFeature</param-name>
            <param-value>true</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>jersey-servlet</servlet-name>
        <url-pattern>/rest/*</url-pattern>
    </servlet-mapping>

</web-app>
```
## 实现上传文件
```java
/**
 * 上传文件
 */
@Path("file")
@Component
public class FileResource extends Observable {

    private static final Logger logger = Logger.getLogger(FileResource.class);

    private static final String BASE_DIR = "/data/files"; // 上传文件存储的根路径

    private static final String MODULE = "default"; // 上传文件所属的模块

    private static final String FILE_ACCESS_URL_PREFIX = ""; // 上传文件访问路径前缀

    @Path("upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA) // 指定接收MIME格式
    @Produces("application/json;charset=UTF-8") // 指定返回MIME格式
    public ResultVO upload(FormDataMultiPart formDataMultiPart) {
        // 上传文件存储的目录
        String dirPath = BASE_DIR + File.separator + MODULE + File.separator + DateUtil.getSysDate(DateUtil.YEAR_MONTH_DAY);
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        List<String> fileUrls = new ArrayList<>(); // 上传文件访问路径集合
        List<Resource> resources = new ArrayList<>(); // 上传文件集合

        List<BodyPart> bodyParts = formDataMultiPart.getBodyParts();
        for (BodyPart bodyPart : bodyParts) {
            // 上传文件的原名称
            String originalName = bodyPart.getContentDisposition().getFileName();
            // 上传文件的格式
            String ext = FileUtil.getExt(originalName);
            // 上传文件的新名称
            String newName = RandomUtil.uuid() + "." + ext;
            // 上传文件存储在服务器的地址
            String path = dir + File.separator + newName;
            // 上传文件访问路径
            String url = FILE_ACCESS_URL_PREFIX + newName;
            // 上传文件的实体
            BodyPartEntity entity = (BodyPartEntity) bodyPart.getEntity();
            try {
                // 将上传文件存储在服务器中指定的位置
                FileUtil.transferTo(entity.getInputStream(), path);
                fileUrls.add(url);
                resources.add(new Resource(newName, ext, new File(path).length(), path, url, DateUtil.getSysDateTime(), "system"));
                logger.info("文件上传成功，文件存储在：" + path);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultVO("1", "文件上传失败，原因: " + e.getMessage());
            }
        }

        if (fileUrls.size() == 0) {
            return new ResultVO("1", "上传文件为空！");
        }

        // 状态更新，通知所有观察对象
        this.setChanged();
        this.notifyObservers(resources);

        return new ResultVO("0", "文件上传成功！", fileUrls);
    }

}
```
## 实现根据注解判断是否拦截请求
### Web配置
```xml
<servlet>
        <servlet-name>jersey-servlet</servlet-name>
        <servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
        <init-param>
            <param-name>com.sun.jersey.config.property.packages</param-name>
            <param-value>com.cjp.resource</param-value>
        </init-param>
        <init-param>
            <param-name>com.sun.jersey.api.json.POJOMappingFeature</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>com.sun.jersey.spi.container.ResourceFilters</param-name>
            <param-value>com.allenayo.sj.config.JerseyResourceFilterFactory</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
```
### JerseyResourceFilterFactory
```java
public class JerseyResourceFilterFactory implements ResourceFilterFactory {

    @Override
    public List<ResourceFilter> create(AbstractMethod abstractMethod) {
        return Arrays.asList(this.createLogFilter(abstractMethod));
    }

    private ResourceFilter createLogFilter(final AbstractMethod abstractMethod) {
        return new ResourceFilter() {
            @Override
            public ContainerRequestFilter getRequestFilter() {
                return new LogFilter(abstractMethod);
            }

            @Override
            public ContainerResponseFilter getResponseFilter() {
                return null;
            }
        };
    }
}
```
### LogFilter
```java
public class LogFilter implements ContainerRequestFilter {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(LogFilter.class);

    private final AbstractMethod method;

    public LogFilter(AbstractMethod method) {
        this.method = method;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        Log logAnn = method.getAnnotation(Log.class);
        if (logAnn == null) return request;

        Optional.of(method.getMethod().getParameterTypes()).ifPresent(classes -> {
            Class<?> aClass = classes[0];
            Object entity = request.getEntity(aClass);
            logger.info(entity);
            try {
                request.setEntityInputStream(new ByteArrayInputStream(JsonUtil.toJson(entity).getBytes()));
            } catch (Exception e) {
            }
        });
        return request;
    }
}
```
### 接口配置注解拦截请求
```java
@Path("hello")
@Component
public class HelloResource {
    @Log
    @POST
    @Path("postResource")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultVO postResource(Resource resource) {
        System.out.println(resource);
        return new ResultVO("0", "success");
    }
}
```