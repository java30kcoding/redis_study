package cn.itlou.redis.example;

import cn.itlou.redis.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("single") // 设置profile
public class SingleExampleServiceTest {

    @Autowired
    SingleExampleService exampleService;

    // ------- spring redistemplate功能演示
    @Test
    public void setTest() {
        exampleService.setByCache("tony", "hahhhhh");
        exampleService.setByCache("a", "1");
        exampleService.setByCache("foo", "bar");
    }

    @Test
    public void getTest() throws Exception {
        User user = exampleService.findUser("tony");
        System.out.println(user);
    }


}
