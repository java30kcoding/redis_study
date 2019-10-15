package cn.itlou.redis.basicopeation;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Set的操作
 *
 * @author yuanyl
 * @version V1.0
 * @since 2019/10/15 18:26
 **/
@Slf4j
public class SetOperation {

    static Jedis jedis;

    static {
        jedis = new Jedis("114.67.72.37", 6379);
        jedis.auth("redis123456");
    }

    public static void main(String[] args) {
        testSet();
    }

    private static void testSet(){
        String setName = "books";
        jedis.del(setName);
        Long saddResult = jedis.sadd(setName, "thinkinginjava");
        log.info("> sadd books thinkinginjava");
        log.info("(integer) 1");
        log.info("saddResult: {}", saddResult);
        //再次插入相同值
        Long saddResult1 = jedis.sadd(setName, "thinkinginjava");
        log.info("> sadd books thinkinginjava");
        log.info("(integer) 0");
        log.info("saddResult1: {}", saddResult1);
        Long saddResult2 = jedis.sadd(setName, "golang", "scala");
        log.info("> sadd books golang scala");
        log.info("(integer) 2");
        log.info("saddResult2: {}", saddResult2);
        //遍历Set内的所有值
        Set<String> smembersResult = jedis.smembers(setName);
        log.info("> smembers books");
        log.info("1) \"thinkinginjava\"\n" +
                 "2) \"scala\"\n" +
                 "3) \"golang\"");
        log.info("smembersResult: {}", smembersResult);
        //相当于contains("scala"); 如果set中没有该值为false, redis中为0
        Boolean sismemberResult = jedis.sismember(setName, "scala");
        log.info("> sismember books");
        log.info("(integer) 1");
        log.info("sismemberResult: {}", sismemberResult);
        //相当于size()
        Long scardResult = jedis.scard(setName);
        log.info("> scard books");
        log.info("(integer) 3");
        log.info("scardResult: {}", scardResult);
        //随机弹出一个
        String spopResult = jedis.spop(setName);
        log.info("> scard books");
        log.info("scala");
        log.info("spopResult: {}", spopResult);
    }

}
