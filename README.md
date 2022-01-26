## 基于SpringBoot + Redis Stream 实现消息队列
__添加redis依赖__
~~~
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
~~~
- JDK —— Version 1.8
- Maven —— Version 3.6.3
- SpringBoot —— Version 2.4.3

### 使用说明
该项目为redis stream 作为消息队列的一个简单demo，大家可以根据自己需求进行修改，代码中主要的
，运行该demo时只需要将application.yml中的配置信息改为自己的即可。
