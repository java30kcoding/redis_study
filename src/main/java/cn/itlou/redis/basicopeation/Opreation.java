package cn.itlou.redis.basicopeation;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 常用数据结构的操作
 *
 * @author yuanyl
 * @version V1.0
 * @since 2019/10/10 15:43
 **/
@Slf4j
public class Opreation {

    static Jedis jedis;

    static {
        jedis = new Jedis("114.67.72.37", 6379);
        jedis.auth("redis123456");
    }

    public static void main(String[] args) {
//        testString();
//        testExpire();
        testInteger();
    }

    /**
     * 测试String操作
     */
    private static void testString(){
        log.info("**************常用命令***************");
        String setResult = jedis.set("test01", "yyl");
        log.info("> set test01 yyl");
        log.info("OK");
        log.info("setResult: {}", setResult);
        String getResult = jedis.get("test01");
        log.info("> get test01");
        log.info("yyl");
        log.info("getResult: {}", getResult);
        Boolean existsResult = jedis.exists("test01");
        log.info("> exists test01");
        log.info("(integer) 1");
        log.info("existsResult: {}", existsResult);
        Long delResult = jedis.del("test01");
        log.info("> del test01");
        log.info("(integer) 1");
        log.info("delResult: {}", delResult);

        log.info("**************批量操作***************");
        String msetResult = jedis.mset("test02", "wow", "test03", "thanks");
        log.info("> mset test02 wow test03 thanks");
        log.info("OK");
        log.info("msetResult: {}", msetResult);
        //get多个String返回值是List
        List<String> mgetResult = jedis.mget("test02", "test03");
        log.info("> mget test02 test03");
        log.info("1) \"wow\"\n" +
                 "2) \"thanks\"");
        log.info("mgetResult: {}", mgetResult);
    }

    /**
     * 测试过期命令
     */
    private static void testExpire(){
        String setResult = jedis.set("expire01", "value");
        //设置expire01这个key，过期时间为5s
        Long expire01Result = jedis.expire("expire01", 5);
        //返回值是操作成功数量
        log.info("expireResult: {}", expire01Result);
        Long expire01Time = jedis.ttl("expire01");
        log.info("剩余时间: {}s", expire01Time);
        //相当于上面的set + expire
        String expire02Result = jedis.setex("expire02", 5, "value");
        log.info("expire02Result: {}", expire02Result);
        Long expire02Time = jedis.ttl("expire02");
        log.info("剩余时间: {}s", expire02Time);
        //如果没有key，setnx才会成功
        Long setnxResult = jedis.setnx("expire02", "wow");
        log.info("setnxResult: {}", setnxResult);
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long setnxResultAfter = jedis.setnx("expire02", "wow");
        log.info("setnxResultAfter: {}", setnxResultAfter);
    }

    private static void testInteger(){
        jedis.set("age", "24");
        log.info("age: {}", jedis.get("age"));
        Long ageIncr = jedis.incr("age");
        log.info("ageIncr: {}", ageIncr);
        Long ageIncr5 = jedis.incrBy("age", 5L);
        log.info("ageIncr5: {}", ageIncr5);
        Long ageDecr5 = jedis.incrBy("age", -5L);
        log.info("ageDecr5: {}", ageDecr5);
        //该值为redis中的最大值
        jedis.set("maxInRedis", "9223372036854775807");
        log.info("maxInRedis: {}", jedis.get("maxInRedis"));
        //超出最大值会报错 ERR increment or decrement would overflow
        Long maxIncr = jedis.incr("maxInRedis");
        log.info("maxIncr: {}", maxIncr);
    }

}
