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
            //check用于验证key和对应消息是否一直
            log.info("stream name :{}, body:{}, check:{}",entries.getStream(),entries.getValue(),(entries.getStream().equals(entries.getValue().get("name"))));
            redisUtil.delField(entries.getStream(),entries.getId().getValue());
        }catch (Exception e){
            log.error("error message:{}",e.getMessage());
        }
    }

}
