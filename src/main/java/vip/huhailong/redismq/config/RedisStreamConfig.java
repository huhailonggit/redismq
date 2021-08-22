package vip.huhailong.redismq.config;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import vip.huhailong.redismq.redistool.ListenerMessage;

import java.time.Duration;

/**
 * @author Huhailong
 * @Description
 * @Date 2021/3/12.
 */
@Configuration
public class RedisStreamConfig {

    @Autowired
    private ListenerMessage streamListener;

    @Bean
    public Subscription subscription(RedisConnectionFactory factory){
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        var listenerContainer = StreamMessageListenerContainer.create(factory,options);
        var subscription = listenerContainer.receiveAutoAck(Consumer.from("mygroup","huhailong"),
                StreamOffset.create("mystream", ReadOffset.lastConsumed()),streamListener);
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

        var listenerContainer = StreamMessageListenerContainer.create(factory,options);
        var subscription = listenerContainer.receiveAutoAck(Consumer.from("mygroup","huhailong"),
                StreamOffset.create("mystream2", ReadOffset.lastConsumed()),streamListener);
        listenerContainer.start();
        return subscription;
    }
}
