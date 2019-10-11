package cn.itlou.redis.basicopeation;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

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
//        testListLikeStack();
        testListSlowOperation();
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
        String listName = "list02";
        jedis.del(listName);
        jedis.rpush(listName, "java", "scala", "go");
        Long llen = jedis.llen(listName);
        log.info("list02的长度: {}", llen);
        //注意为 O(n)的操作
        String lindexResult = jedis.lindex(listName, 1);
        log.info("> lindex list02 1");
        log.info("scala");
        log.info("lindexResult: {}", lindexResult);
        //遍历list；O(n)
        List<String> lrangeResult = jedis.lrange(listName, 0, -1);
        log.info("> lrange list02 0 -1");
        log.info("1) \"java\"\n" +
                 "2) \"scala\"\n" +
                 "3) \"go\"");
        log.info("lrangeResult: {}", lrangeResult);
        //ltrim截取
        String ltrimResult = jedis.ltrim(listName, 1, -1);
        log.info("> ltrim list02 1 -1");
        log.info("OK");
        log.info("ltrimResult: {}", ltrimResult);
        List<String> afterTrim = jedis.lrange(listName, 0, -1);
        log.info("ltrim后的数据: {}", afterTrim);
        //ltrim 1 0 是清空整个列表
        jedis.ltrim(listName, 1, 0);
        List<String> afterTrimAllList = jedis.lrange(listName, 0, -1);
        log.info("ltrim所有列表后的数据: {}", afterTrimAllList);
    }

}
