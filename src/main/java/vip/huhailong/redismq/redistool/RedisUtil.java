package vip.huhailong.redismq.redistool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Huhailong
 * @Description 添加stream消息
 * @Date 2021/3/10.
 */
@Slf4j
@Component
public class RedisUtil {
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * XADD 添加stream消息
     * @param key stream对应的key
     * @param message 要村粗的消息数据
     */
    public RecordId addStream(String key, Map<String,Object> message){
        RecordId add = redisTemplate.opsForStream().add(key, message);
        log.info("添加成功："+add);
        return add;
    }

    /**
     * 读取所有stream消息
     * @param key
     * @return
     */
    public List<MapRecord<String,Object,Object>> getAllStream(String key){
        return redisTemplate.opsForStream().range(key, Range.open("-","+"));
    }

}
