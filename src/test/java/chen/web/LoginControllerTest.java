package chen.web;

import chen.common.ServerResponse;
import chen.service.UserService;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author bystander
 * @date 2018/8/1
 */
public class LoginControllerTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void TestLogin(){
        String username = "bystander";
        String password = "CBC19948178";
        ServerResponse res = userService.login(username, password);
        System.out.println(res.isSuccess());
    }
}
