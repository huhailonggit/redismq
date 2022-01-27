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
### 测试方法
启动程序后分别访问一下测试接口
~~~
http://localhost:8080/test/sendTest/mystream1
http://localhost:8080/test/sendTest/mystream2
~~~
控制台打印
~~~
2022-01-27 10:18:48.298  INFO 10552 --- [nio-8080-exec-5] v.huhailong.redismq.redistool.RedisUtil  : 添加成功：1643249866013-0
2022-01-27 10:18:48.302  INFO 10552 --- [cTaskExecutor-1] v.h.redismq.redistool.ListenerMessage    : 接受到来自redis的消息
message id 1643249866013-0
stream mystream1
body {test=hello redismq, send time=2022-01-27 10:18:48}
2022-01-27 10:18:54.080  INFO 10552 --- [nio-8080-exec-7] v.huhailong.redismq.redistool.RedisUtil  : 添加成功：1643249871795-0
2022-01-27 10:18:54.080  INFO 10552 --- [cTaskExecutor-2] v.h.redismq.redistool.ListenerMessage    : 接受到来自redis的消息
message id 1643249871795-0
stream mystream2
body {test=hello redismq, send time=2022-01-27 10:18:54}
~~~