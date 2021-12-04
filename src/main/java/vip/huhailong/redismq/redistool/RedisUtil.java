package vip.huhailong.redismq.redistool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

/**
 * @author Huhailong
 * @Description 添加stream消息
 * @Date 2021/3/10.
 */
@Slf4j
@Component
public class RedisUtil {
    private final RedisTemplate<String,String> redisTemplate;

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

    public void addGroup(String key, String groupName){
        redisTemplate.opsForStream().createGroup(key,groupName);
    }

    public void delField(String key, String fieldId){
        redisTemplate.opsForStream().delete(key,fieldId);
    }

    /**
     * 读取所有stream消息
     * @param key
     * @return
     */
    public List<MapRecord<String,Object,Object>> getAllStream(String key){
        List<MapRecord<String, Object, Object>> range = redisTemplate.opsForStream().range(key, Range.open("-", "+"));
        if(range == null){
            return null;
        }
        for(MapRecord<String,Object,Object> mapRecord : range){
            redisTemplate.opsForStream().delete(key,mapRecord.getId());
        }
        return range;
    }

    public void getStream(String key){

        List<MapRecord<String, Object, Object>> read = redisTemplate.opsForStream().read(StreamReadOptions.empty().block(Duration.ofMillis(1000*30)).count(2), StreamOffset.latest(key));
        System.out.println(read);
    }

}
