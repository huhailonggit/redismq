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
import java.util.HashMap;
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
    public Subscription subscription(RedisConnectionFactory factory){
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
        initStream(redisStreamNames[0],groups[0]);
        var listenerContainer = StreamMessageListenerContainer.create(factory,options);
        var subscription = listenerContainer.receiveAutoAck(Consumer.from(groups[0],this.getClass().getName()),
                StreamOffset.create(redisStreamNames[0], ReadOffset.lastConsumed()),streamListener);
        listenerContainer.start();
        return subscription;
    }

    @Bean
    public Subscription subscription2(RedisConnectionFactory factory){
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
        initStream(redisStreamNames[1],groups[0]);
        var listenerContainer = StreamMessageListenerContainer.create(factory,options);
        var subscription = listenerContainer.receiveAutoAck(Consumer.from(groups[0],this.getClass().getName()),
                StreamOffset.create(redisStreamNames[1], ReadOffset.lastConsumed()),streamListener);
        listenerContainer.start();
        return subscription;
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
