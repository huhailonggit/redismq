package vip.huhailong.redismq.redistool;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Configuration
public class ListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        System.out.println("message id "+entries.getId());
        System.out.println("stream "+entries.getStream());
        System.out.println("body "+entries.getValue());
    }
}
