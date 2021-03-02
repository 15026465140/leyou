package com.leyou.user.service;

import com.leyou.common.untils.CodecUtils;
import com.leyou.common.untils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AmqpTemplate  amqpTemplate;

    private static final String  KEY_PREFIX="user:verify";


    public Boolean checkUserData(String data, Integer type) {
        User user = new User();
        if (type == 1) {
            user.setUsername(data);
        } else if (type == 2) {
            user.setPhone(data);
        } else {
            return null;
        }
        return userMapper.selectCount(user)==0;
    }

    public void sendVerifyCode(String phone) {
       //校验手机号码是否合理及是否为空
        if (StringUtils.isBlank(phone)) {
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);

        //保存验证码到redis
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);

        stringRedisTemplate.opsForValue().set(KEY_PREFIX+phone,code, 30,TimeUnit.MINUTES);

        //发送手机号码及验证码到rabbit
        amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",msg);


    }
    //注册用户
    public Boolean register(User user, String code) {
        //获取redis保存的验证码
        String redisCode = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        if (!StringUtils.equals(redisCode, code)) {
            return false;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //加密加盐
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //新增用户到数据库
        user.setId(null);
        // 添加到数据库
        boolean b = this.userMapper.insertSelective(user) == 1;

        if(b){
            // 注册成功，删除redis中的记录
            this.stringRedisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return b;

    }
    //查询用户
    public User queryUser(String username, String password) {
        // 查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 校验用户名
        if (user == null) {
            return null;
        }
        // 校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
            return null;
        }
        // 用户名密码都正确
        return user;
    }
}
