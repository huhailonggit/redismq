package vip.huhailong.redismq.redistool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Slf4j
public class ListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {

    RedisUtil redisUtil;

    public ListenerMessage(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        try{
            log.info("接受到来自redis的消息");
            System.out.println("message id "+entries.getId().getValue());
            System.out.println("stream "+entries.getStream());
            System.out.println("body "+entries.getValue());
            redisUtil.delField(entries.getStream(),entries.getId().getValue());
        }catch (Exception e){
            log.error("error message:{}",e.getMessage());
        }
    }

}
