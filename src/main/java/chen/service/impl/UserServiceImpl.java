package chen.service.impl;

import chen.common.ServerResponse;
import chen.dao.UserDtoMapper;
import chen.dto.UserDto;
import chen.entity.User;
import chen.service.UserService;
import chen.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bystander
 * @date 2018/8/1
 */
@Service("/userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDtoMapper userMapper;

    @Override
    public ServerResponse<UserDto> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = new User(username, md5Password);
        UserDto userDto = userMapper.getUserByUser(user);
        if (userDto == null){
            return ServerResponse.createByErrorMessage("用户名或密码错误!");
        }
        return ServerResponse.createBySuccess("登录成功", userDto);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public ServerResponse getUserById(Integer userId) {
        UserDto userDto = userMapper.selectByPrimaryKey(userId);
        if (userDto == null){
            return ServerResponse.createByErrorMessage("该用户不存在");
        }
        return ServerResponse.createBySuccess(userDto);
    }

    @Override
    public ServerResponse updateUser(User user) {
        int resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("更新用户失败");
        }
        return ServerResponse.createBySuccessMessage("更新用户成功");
    }

    @Override
    public ServerResponse register(User user){
        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("新建用户失败");
        }
        return ServerResponse.createBySuccessMessage("新建用户成功");
    }

    @Override
    public ServerResponse deleteUser(Integer userId) {
        int resultCount = userMapper.deleteByPrimaryKey(userId);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("删除用户失败");
        }
        return ServerResponse.createBySuccessMessage("删除用户成功");
    }
}
