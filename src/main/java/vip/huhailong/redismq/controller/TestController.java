package vip.huhailong.redismq.controller;

import it.sauronsoftware.jave.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.web.bind.annotation.*;
import vip.huhailong.redismq.redistool.RedisUtil;
import vip.huhailong.redismq.util.Image2Binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
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

    @GetMapping("/test2")
    public void testVideo(String path) throws IOException {
        File source = File.createTempFile("temp",".tmp");
        System.out.println(source.getCanonicalPath());
        Image2Binary.toBDFile(path,source.getCanonicalPath());
        Encoder encoder = new Encoder();
        FileChannel fc= null;
        String size = "";
        try {
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(source);
            long ls = m.getDuration();
            System.out.println("此视频时长为:"+(ls)/1000+"秒！");
            //视频帧宽高
            System.out.println("此视频高度为:"+m.getVideo().getSize().getHeight());
            System.out.println("此视频宽度为:"+m.getVideo().getSize().getWidth());
            System.out.println("此视频格式为:"+m.getFormat());
            FileInputStream fis = new FileInputStream(source);
            fc= fis.getChannel();
            BigDecimal fileSize = new BigDecimal(fc.size());
            size = fileSize.divide(new BigDecimal(1024), 0, RoundingMode.HALF_UP) + "KB";
            System.out.println("此视频大小为"+size);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=fc){
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
