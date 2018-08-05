package chen.service;

import chen.common.ServerResponse;
import chen.entity.User;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
public interface UserService {

    ServerResponse login(String username, String password);

    List<User> getAllUsers();

    ServerResponse getUserById(Integer userId);

    ServerResponse updateUser(User user);

    ServerResponse register(User user);

    ServerResponse deleteUser(Integer userId);
}
