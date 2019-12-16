package blog.service.Impl;

import blog.config.SmsProperties;
import blog.dao.UserRepository;
import blog.pojo.User;
import blog.service.UserService;
import blog.util.MD5Utils;
import blog.util.NumberUtils;
import blog.util.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(SmsProperties.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendMessage sendMessage;
    @Autowired
    private SmsProperties smsProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify";

    // 检查用户名和密码
    @Override
    public User checkUser(String username, String password) {

        // 调用MD5加密,对用户输入的密码进行加密
        String code = MD5Utils.code(password);
        User user = userRepository.findByUsernameAndPassword(username, code);

        return user;
    }

    // 查询用户是否存在
    @Override
    public String checkUserName(String username) {
        return userRepository.findByUsername(username);
    }

    // 检查手机号是否存在
    @Override
    public String checkMobile(String mobile) {

        if ("15376764263".equals(mobile)){
            return mobile;
        } else {
            return null;
        }


    }

    // 发送短信
    @Override
    public String sendCode(String mobileNum) {

        if (!"".equals(mobileNum) && mobileNum!=null){
            String code = NumberUtils.generateCode(6);

            redisTemplate.opsForValue().set(KEY_PREFIX+mobileNum,code,5,TimeUnit.MINUTES );

            // 调用发送短信的方法
            String data = sendMessage.sendCode(mobileNum, code, smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate());

            return data;
        }

        return null;

    }
}
