# Java Sec Code

对于学习Java漏洞代码来说，`Java Sec Code`是一个非常强大且友好的项目。

[英文文档](https://github.com/JoyChou93/java-sec-code/blob/master/README.md)

## 介绍

该项目也可以叫做Java Vulnerability Code(Java漏洞代码)。

每个漏洞类型代码默认存在安全漏洞（除非本身不存在漏洞），相关修复代码在注释里。具体可查看每个漏洞代码和注释。

[在线Demo](http://118.25.15.216:8080)

登录用户名密码：

```
admin/admin123
joychou/joychou123
```

## 漏洞代码

- [Actuators to RCE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/resources/logback-online.xml)
- [CommandInject](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CommandInject.java)
- [CORS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CORS.java)
- [CRLF Injection](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CRLFInjection.java)
- [CSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/security/WebSecurityConfig.java)
- [Deserialize](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Deserialize.java)
- [Fastjson](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Fastjson.java)
- [File Upload](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/FileUpload.java)
- [IP Forge](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/IPForge.java)
- [Java RMI](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/RMI/Server.java)
- [JSONP](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/jsonp/JSONP.java)
- [ooxmlXXE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/othervulns/ooxmlXXE.java)
- [PathTraversal](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/PathTraversal.java)
- [RCE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Rce.java)
- [SpEL](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SpEL.java)
- [SQL Injection](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SQLI.java)
- [SSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SSRF.java)
- [SSTI](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SSTI.java)
- [URL Redirect](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLRedirect.java)
- [URL whitelist Bypass](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLWhiteList.java)
- [xlsxStreamerXXE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/othervulns/xlsxStreamerXXE.java)
- [XSS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XSS.java)
- [XStream](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XStreamRce.java)
- [XXE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XXE.java)


## 漏洞说明

- [Actuators to RCE](https://github.com/JoyChou93/java-sec-code/wiki/Actuators-to-RCE)
- [CORS](https://github.com/JoyChou93/java-sec-code/wiki/CORS)
- [CSRF](https://github.com/JoyChou93/java-sec-code/wiki/CSRF)
- [Deserialize](https://github.com/JoyChou93/java-sec-code/wiki/Deserialize)
- [Fastjson](https://github.com/JoyChou93/java-sec-code/wiki/Fastjson)
- [Java RMI](https://github.com/JoyChou93/java-sec-code/wiki/Java-RMI)
- [JSONP](https://github.com/JoyChou93/java-sec-code/wiki/JSONP)
- [POI-OOXML XXE](https://github.com/JoyChou93/java-sec-code/wiki/Poi-ooxml-XXE)
- [SQLI](https://github.com/JoyChou93/java-sec-code/wiki/SQL-Inject)
- [SSRF](https://github.com/JoyChou93/java-sec-code/wiki/SSRF)
- [SSTI](https://github.com/JoyChou93/java-sec-code/wiki/SSTI)
- [URL whitelist Bypass](https://github.com/JoyChou93/java-sec-code/wiki/URL-whtielist-Bypass)
- [XXE](https://github.com/JoyChou93/java-sec-code/wiki/XXE)
- [Others](https://github.com/JoyChou93/java-sec-code/wiki/others)


## 如何运行

应用会用到mybatis自动注入，请提前运行mysql服务，并且配置mysql服务的数据库名称和用户名密码(除非是Docker环境)。

``` 
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/java_sec_code
spring.datasource.username=root
spring.datasource.password=woshishujukumima
```

- Docker
- IDEA
- Tomcat
- JAR

### Docker

开启应用：

``` 
docker-compose pull
docker-compose up
```

关闭应用：

```
docker-compose down
```

Docker环境：

- Java 1.8.0_102
- Mysql 8.0.17
- Tomcat 8.5.11

### IDEA

- `git clone https://github.com/JoyChou93/java-sec-code`
- 在IDEA中打开，直接点击run按钮即可运行。

例子：

```
http://localhost:8080/rce/exec?cmd=whoami
```
 
返回：

``` 
Viarus
```

### Tomcat

1. `git clone https://github.com/JoyChou93/java-sec-code & cd java-sec-code`
2. 生成war包 `mvn clean package`
3. 将target目录的war包，cp到Tomcat的webapps目录
4. 重启Tomcat应用


例子：

```
http://localhost:8080/java-sec-code-1.0.0/rce/exec?cmd=whoami
```
 
返回：

``` 
Viarus
```


### JAR包


先修改pom.xml里的配置，将war改成jar。

``` 
    <groupId>sec</groupId>
    <artifactId>java-sec-code</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>
```

再打包运行即可。

```
git clone https://github.com/JoyChou93/java-sec-code
cd java-sec-code
mvn clean package -DskipTests 
java -jar 打包后的jar包路径
```

## 认证

### 登录

[http://localhost:8080/login](http://localhost:8080/login)

如果未登录，访问任何页面都会重定向到login页面。用户名和密码如下。

```
admin/admin123
joychou/joychou123
```
### 登出

[http://localhost:8080/logout](http://localhost:8080/logout)

### 记住我

Tomcat默认JSESSION会话有效时间为30分钟，所以30分钟不操作会话将过期。为了解决这一问题，引入rememberMe功能，默认过期时间为2周。


## 贡献者

核心开发者： [JoyChou](https://github.com/JoyChou93).其他开发者：[lightless](https://github.com/lightless233),  [Anemone95](https://github.com/Anemone95)。欢迎各位提交PR。

## 捐赠

如果你喜欢这个项目，你可以捐款来支持我。 有了你的支持，我将能够更好地制作`Java sec code`项目。

### Alipay

扫描支付宝二维码支持`Java sec code`。

<img title="Alipay QRcode" src="https://aliyun-testaaa.oss-cn-shanghai.aliyuncs.com/alipay_qr.png" width="200">
