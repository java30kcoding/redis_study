package cn.itlou.redis.basicopeation;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * List的操作
 *
 * @author yuanyl
 * @version V1.0
 * @since 2019/10/10 16:22
 **/
@Slf4j
public class ListOperation {

    static Jedis jedis;

    static {
        jedis = new Jedis("114.67.72.37", 6379);
        jedis.auth("redis123456");
    }

    public static void main(String[] args) {
//        testListLikeQueue();
        testListLikeStack();
    }

    /**
     * 右进左出: Queue
     */
    private static void testListLikeQueue(){
        jedis.del("list01");
        Long listResult = jedis.rpush("list01", "MR.A", "MR.B", "MR.C");
        log.info("> rpush list01 MR.A MR.B MR.C");
        log.info("(integer) 3");
        log.info("listResult: {}", listResult);
        //list长度
        Long listLength = jedis.llen("list01");
        log.info("list01.length: {}", listLength);
        //如果超过list长度，返回nil
        for (int i = 0; i < listLength; i++) {
            String lpopResult = jedis.lpop("list01");
            log.info("lpopResult: {}", lpopResult);
        }
    }

    /**
     * 右进右出: Stack
     */
    private static void testListLikeStack(){
        jedis.del("list01");
        Long listResult = jedis.rpush("list01", "MR.A", "MR.B", "MR.C");
        log.info("> rpush list01 MR.A MR.B MR.C");
        log.info("(integer) 3");
        log.info("listResult: {}", listResult);
        Long listLength = jedis.llen("list01");
        log.info("list01.length: {}", listLength);
        for (int i = 0; i < listLength; i++) {
            String rpopResult = jedis.rpop("list01");
            log.info("lpopResult: {}", rpopResult);
        }
    }

    /**
     * list的慢操作
     */
    private static void testListSlowOperation(){



    }

}
