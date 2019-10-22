package cn.itlou.redis.basicopeation;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * ZSet的操作
 *
 * @author yuanyl
 * @version V1.0
 * @since 2019/10/15 18:37
 **/
@Slf4j
public class ZSetOperation {

    static Jedis jedis;

    static {
        jedis = new Jedis("*.*.*.*", 6379);
        jedis.auth("**********");
    }

    public static void main(String[] args) {
        testZSet();
    }

    private static void testZSet(){
        String zsetKey = "zsetTest";
        jedis.del(zsetKey);
        Long zaddResult = jedis.zadd(zsetKey, 9.0, "english");
        log.info("> zadd zsetTest 9.0 english");
        log.info("(integer) 1");
        log.info("zaddResult: {}", zaddResult);
        jedis.zadd(zsetKey, 8.9, "chinese");
        jedis.zadd(zsetKey, 8.8, "korean");
        //正序显示所有zset内容
        Set<String> zrangeResult = jedis.zrange(zsetKey, 0, -1);
        log.info("> zrange zsetTest 0 -1");
        log.info("1) \"korean\"\n" +
                "2) \"chinese\"\n" +
                "3) \"english\"");
        log.info("zrangeResult: {}", zrangeResult);
        //逆序显示所有zset内容
        Set<String> zrevrangeResult = jedis.zrevrange(zsetKey, 0, -1);
        log.info("> zrevrange zsetTest 0 -1");
        log.info("1) \"english\"\n" +
                "2) \"chinese\"\n" +
                "3) \"korean\"");
        log.info("zrevrangeResult: {}", zrevrangeResult);
        //相当于size()
        Long zcardResult = jedis.zcard(zsetKey);
        log.info("> zcard zsetTest");
        log.info("(integer) 3");
        log.info("zcardResult: {}", zcardResult);
        //获取指定value的score
        Double zscoreResult = jedis.zscore(zsetKey, "chinese");
        log.info("> zscore zsetTest chinese");
        //score内部采用double存储，所以存在小数点精度问题，但通过jedis会解决这个问题
        log.info("8.9000000000000004");
        log.info("zscoreResult: {}", zscoreResult);
        //获取排名，从0开始
        Long zrankResult = jedis.zrank(zsetKey, "chinese");
        log.info("> zrank zsetTest chinese");
        log.info("1");
        log.info("zrankResult: {}", zrankResult);
        //根据score区间遍历
        Set<String> zrangeByScoreResult = jedis.zrangeByScore(zsetKey, 0, 8.9);
        log.info("> zrangeByScore zsetTest 0 8.9");
        log.info("1) \"korean\"\n" +
                "2) \"chinese\"\n");
        log.info("zrangeByScoreResult: {}", zrangeByScoreResult);
        //根据score区间遍历并显示score
        Set<Tuple> zrangeByScoreWithScoresResult = jedis.zrangeByScoreWithScores(zsetKey, 8.8, 8.9);
        log.info("> zrangeByScoreWithScores zsetTest 8.8 8.9");
        log.info("1) \"korean\"\n" +
                "2) \"8.8000000000000007\"\n" +
                "3) \"chinese\"\n" +
                "4) \"8.9000000000000004\"");
        log.info("zrangeByScoreWithScoresResult: {}", zrangeByScoreWithScoresResult);
        //移除一个或多个元素
        Long zremResult = jedis.zrem(zsetKey, "korean");
        log.info("> zrem zsetTest korean");
        log.info("(integer) 1");
        log.info("zremResult: {}", zremResult);
        Set<String> zrangeResult1 = jedis.zrange(zsetKey, 0, -1);
        log.info("zrangeResult1: {}", zrangeResult1);
    }

}
