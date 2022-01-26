package vip.huhailong.redismq.config;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import vip.huhailong.redismq.redistool.ListenerMessage;
import vip.huhailong.redismq.redistool.RedisUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huhailong
 * @Description
 * @Date 2021/3/12.
 */
@Slf4j
@Configuration
public class RedisStreamConfig {

    private final ListenerMessage streamListener;
    private final RedisUtil redisUtil;

    @Value("${redis-stream.names}")
    private String[]redisStreamNames;
    @Value("${redis-stream.groups}")
    private String[]groups;

    @Autowired
    public RedisStreamConfig(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
        this.streamListener = new ListenerMessage(redisUtil);
    }

    @Bean
    public List<Subscription> subscription(RedisConnectionFactory factory){
        List<Subscription> resultList = new ArrayList<>();
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
        for (String redisStreamName : redisStreamNames) {
            initStream(redisStreamName,groups[0]);
            var listenerContainer = StreamMessageListenerContainer.create(factory,options);
            Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from(groups[0], this.getClass().getName()),
                    StreamOffset.create(redisStreamName, ReadOffset.lastConsumed()), streamListener);
            resultList.add(subscription);
            listenerContainer.start();
        }
        return resultList;
    }


    private void initStream(String key, String group){
        boolean hasKey = redisUtil.hasKey(key);
        if(!hasKey){
            Map<String,Object> map = new HashMap<>();
            map.put("field","value");
            RecordId recordId = redisUtil.addStream(key, map);
            redisUtil.addGroup(key,group);
            //将初始化的值删除掉
            redisUtil.delField(key,recordId.getValue());
            log.info("stream:{}-group:{} initialize success",key,group);
        }
    }
}
