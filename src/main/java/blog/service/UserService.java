package blog.service;

import blog.pojo.User;

public interface UserService {

    // 检查用户名和密码
    User checkUser(String username,String password);

    // 根据姓名查询用户是否存在
    String checkUserName(String username);

    // 查询手机号是否存在
    String checkMobile(String mobile);

    // 手机号发送短信
    String sendCode(String mobileNum);
}
