package vip.huhailong.redismq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.web.bind.annotation.*;
import vip.huhailong.redismq.redistool.RedisUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Huhailong
 * @Description
 * @Date 2021/3/10.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private RedisUtil redisUtil;

    @Autowired
    public TestController(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    @PostMapping("/addStream")
    public String addStream(@RequestBody Map<String,Object> map){
        String key = (String) map.get("key");
        Map<String,Object> message = (Map<String, Object>) map.get("message");
        return redisUtil.addStream(key, message).getValue();
    }

    @GetMapping("/getStream")
    public List<MapRecord<String,Object,Object>> getStream(String key){
        return redisUtil.getAllStream(key);
    }

    @GetMapping("/getStream1")
    public void getStream1(String key){
        redisUtil.getStream(key);
    }

    @GetMapping("/groupRead")
    public void getStreamByGroup(String key, String groupName, String consumerName){
        redisUtil.getStreamByGroup(key,groupName,consumerName);
    }
}
