package vip.huhailong.redismq.redistool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Slf4j
@Component
public class ListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        log.info("接受到来自redis的消息");
        System.out.println("message id "+entries.getId().getValue());
        System.out.println("stream "+entries.getStream());
        System.out.println("body "+entries.getValue());
        try{
            //异常中断测试
            System.out.println(1/0);
        }catch (Exception e){
            log.error("error message:{}",e.getMessage());
        }
        redisUtil.delField("mystream",entries.getId().getValue());
    }

}
