# Java sec code


[中文文档](https://github.com/JoyChou93/java-sec-code/blob/master/README.md)

## Introduce

This project can also be called Java vulnerability code. 

Each vulnerability type code has a security vulnerability by default unless there is no vulnerability. The relevant fix code is in the comments or code. Specifically, you can view each vulnerability code and comments.

## Vulnerability Code

Sort by letter.

- [Actuators to RCE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/resources/logback.xml)
- [CORS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CORS.java)
- [CRLF Injection](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CRLFInjection.java)
- [CSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/WebSecurityConfig.java)
- [Deserialize](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Deserialize.java)
- [Fastjson](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Fastjson.java)
- [File Upload](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/FileUpload.java)
- [IP Forge](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/IPForge.java)
- [Java RMI](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/RMI/Server.java)
- [JSONP](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/JSONP.java)
- [RCE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Rce.java)
- [SPEL](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SPEL.java)
- [SQL Injection](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SQLI.java)
- [SSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SSRF.java)
- [URL Redirect](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLRedirect.java)
- [URL whitelist Bypass](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLWhiteList.java)
- [XSS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XSS.java)
- [XXE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XXE.java)


## Vulnerability Description

- [Actuators to RCE](https://github.com/JoyChou93/java-sec-code/wiki/Actuators-to-RCE)
- [CORS](https://github.com/JoyChou93/java-sec-code/wiki/CORS)
- [CSRF](https://github.com/JoyChou93/java-sec-code/wiki/CSRF)
- [Fastjson](https://github.com/JoyChou93/java-sec-code/wiki/Fastjson)
- [Java RMI](https://github.com/JoyChou93/java-sec-code/wiki/Java-RMI)
- [JSONP](https://github.com/JoyChou93/java-sec-code/wiki/JSONP)
- [SQLI](https://github.com/JoyChou93/java-sec-code/wiki/SQL-Inject)
- [URL whitelist Bypass](https://github.com/JoyChou93/java-sec-code/wiki/URL-whtielist-Bypass)
- [XXE](https://github.com/JoyChou93/java-sec-code/wiki/XXE)
- [Others](https://github.com/JoyChou93/java-sec-code/wiki/others)

## How to run

- Tomcat
- IDEA
- JAR

### Tomcat

- Exclude tomcat in pom.xml.

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    ```

- Build war package by `mvn clean package`.
- Copy war package to tomcat webapps directory.
- Start tomcat application.

Example:

```
http://localhost:8080/java-sec-code-1.0.0/rce/exec?cmd=whoami
```

return:

```
Viarus
```

### IDEA

Click `run` button.

Example:

```
http://localhost:8080/rce/exec?cmd=whoami
```

return:

```
Viarus
```

### Jar

- Change `war` to `jar` in `pom.xml`.

    ```xml
    <groupId>sec</groupId>
    <artifactId>java-sec-code</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>
    ```

- Build package and run.

    ```
    mvn clean package -DskipTests 
    java -jar target/java-sec-code-1.0.0.jar
    ```


