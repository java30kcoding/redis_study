package cn.itlou.redis.basicopeation;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Hash的操作
 *
 * @author yuanyl
 * @version V1.0
 * @since 2019/10/11 13:45
 **/
@Slf4j
public class HashOperation {

    static Jedis jedis;

    static {
        jedis = new Jedis("114.67.72.37", 6379);
        jedis.auth("redis123456");
    }

    public static void main(String[] args) {
        testHash();
    }

    private static void testHash(){
        String hashName = "language";
        jedis.del(hashName);
        //hset 同一个key，返回结果为0，但value会变
        Long hsetResult = jedis.hset(hashName, "java", "gc");
        log.info("> hset language key value");
        log.info("(integer) 1");
        log.info("hsetResult: {}", hsetResult);
        jedis.hset(hashName, "scala", "FP");
        jedis.hset(hashName, "go", "coroutine");
        //获取所有的hash，key，value顺序不定
        Map<String, String> hgetAllResult = jedis.hgetAll(hashName);
        log.info("> hgetall language");
        log.info("1) \"java\"\n" +
                 "2) \"gc\"\n" +
                 "3) \"scala\"\n" +
                 "4) \"FP\"\n" +
                 "5) \"go\"\n" +
                 "6) \"coroutine\"");
        log.info("hgetAllResult: {}", hgetAllResult);
        Long hlenResult = jedis.hlen(hashName);
        log.info("> hlen language");
        log.info("(integer) 3");
        log.info("hlenResult: {}", hlenResult);
        String hgetResult = jedis.hget(hashName, "scala");
        log.info("> hget language scala");
        log.info("FP");
        log.info("hgetResult: {}", hgetResult);
        HashMap<String, String> languageMap = Maps.newHashMap();
        languageMap.put("python", "easy");
        languageMap.put("kotlin", "good");
        //设置多个key - value
        jedis.hmset(hashName, languageMap);
        Map<String, String> hgetAllResultAfter = jedis.hgetAll(hashName);
        log.info("hgetAllResultAfter: {}", hgetAllResultAfter);
        //hash也可以对数字value进行计算
        jedis.hset(hashName, "age", "10");
        Long age = jedis.hincrBy(hashName, "age", 10);
        log.info("age: {}", age);
    }

}
