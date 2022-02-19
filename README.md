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

测试多多个key多接受能力借口

~~~

curl -X GET http://localhost:8080/test/moreTest/10  //后面多10可以更改

~~~

__注意：如果插入redis多数量很大，建议将插入放到集合后分批批量插入，例如100万条数据可以分为100次插入，每次1万条，这样可以减少I/O操作，加快速度__

#### List分割方法

~~~java
public class ListUtil {

    /**
     * 集合分割工具类
     * @param max 分割后集合最大数量
     * @param list 要分割多集合
     * @return 返回分割后多集合
     */
    public static List<List<String>> cutApart(int max, List<String> list){
        List<List<String>> resultList = new ArrayList<>();
        Stream.iterate(0, n->n+1).limit(countStep(list.size(), max)).forEach(i->{
            resultList.add(list.stream().skip(i*max).limit(max).collect(Collectors.toList()));
        });
        return resultList;
    }

    private static Integer countStep(int size, int max){
        return (size + max - 1) / max;
    }
}
~~~