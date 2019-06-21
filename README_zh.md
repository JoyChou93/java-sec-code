# Java Security Code

## 介绍

该项目也可以叫做Java Vulnerability Code(Java漏洞代码)。

每个漏洞类型代码默认存在安全漏洞（除非本身不存在漏洞），相关修复代码在注释里。具体可查看每个漏洞代码和注释。

## 漏洞代码

- [XXE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XXE.java)
- [SSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SSRF.java)
- [URL重定向](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLRedirect.java)
- [IP伪造](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/IPForge.java)
- [XSS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XSS.java)
- [CRLF注入](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CRLFInjection.java)
- [远程命令执行](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Rce.java)
- [反序列化](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Deserialize.java)
- [文件上传](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/FileUpload.java)
- [SQL注入](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SQLI.java)
- [URL白名单Bypass](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLWhiteList.java)
- [Java RMI](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/RMI/Server.java)
- [Fastjson](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Fastjson.java)
- [CORS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CORS.java)
- [JSONP](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/JSONP.java)
- [SPEL](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SPEL.java)
- [Actuators to RCE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/resources/logback.xml)
- [CSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/WebSecurityConfig.java)

## 漏洞说明

- [Java RMI](https://github.com/JoyChou93/java-sec-code/wiki/Java-RMI)
- [XXE](https://github.com/JoyChou93/java-sec-code/wiki/XXE)
- [SQLI](https://github.com/JoyChou93/java-sec-code/wiki/SQL-Inject)
- [Fastjson](https://github.com/JoyChou93/java-sec-code/wiki/Fastjson)
- [CORS](https://github.com/JoyChou93/java-sec-code/wiki/CORS)
- [CSRF](https://github.com/JoyChou93/java-sec-code/wiki/CSRF)
- [JSONP](https://github.com/JoyChou93/java-sec-code/wiki/JSONP)
- [Actuators to RCE](https://github.com/JoyChou93/java-sec-code/wiki/Actuators-to-RCE)
- [URL whitelist Bypass](https://github.com/JoyChou93/java-sec-code/wiki/URL-whtielist-Bypass)
- [Others](https://github.com/JoyChou93/java-sec-code/wiki/others)


## 如何运行


### Tomcat

1. 生成war包 `mvn clean package`
2. 将target目录的war包，cp到Tomcat的webapps目录
3. 重启Tomcat应用


```
http://localhost:8080/java-sec-code-1.0.0/rce/exec?cmd=whoami
```
 
返回

``` 
Viarus
```

### IDEA

直接点击run按钮即可运行。

```
http://localhost:8080/rce/exec?cmd=whoami
```
 
返回

``` 
Viarus
```



### Jar包


先修改pom.xml里的配置，将war改成jar。

``` 
    <groupId>sec</groupId>
    <artifactId>java-sec-code</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>
```

再打包运行即可。

```
mvn clean package -DskipTests 
java -jar 打包后的jar包路径
```
