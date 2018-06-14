# Java Security Code


- [XMLInject](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XMLInjection.java)
- [SSRF](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/SSRF.java)
- [URLRedirect](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/URLRedirect.java)
- [IPForge](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/IPForge.java)
- [XSS](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/XSS.java)
- [CRLFInjection](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/CRLFInjection.java)
- [RCE](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/Rce.java)
- [Deserialize](https://github.com/JoyChou93/java-sec-code/blob/master/src/main/java/org/joychou/controller/deserialize.java)

## Usage


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

如果想在IDEA中直接运运行，需要进行两个操作:

1. 覆盖适配IDEA的pom.xml `mv pom.xml.idea pom.xml`
2. 覆盖适配IDEA的Application.java文件 `mv Application.java.idea Application.java`