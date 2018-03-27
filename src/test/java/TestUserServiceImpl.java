import com.hyb.redis.User;
import com.hyb.redis.UserService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by HuangYibo on 2018/3/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring*.xml"})
public class TestUserServiceImpl {
    /**
     * 用户接口
     */
    @Autowired
    private UserService userService;

    @Test
    public void testAdd(){
        User user = new User();
        user.setUserId("000001");
        user.setEmpCode("1234");
        user.setEmpName("HuangYibo");
        user.setRole("Soft Development Engineer");
        user.setTitle("DEV");
        boolean isTrue =userService.addUser(user);
        Assert.assertTrue(isTrue);
    }

    @Test
    public void testQueryById(){
        User user = new User();
        user.setUserId("000001");
        System.out.println(userService.queryUserByUserId(user));
    }
}
