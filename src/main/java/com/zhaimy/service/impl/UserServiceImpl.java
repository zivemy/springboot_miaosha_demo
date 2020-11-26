package com.zhaimy.service.impl;

import com.zhaimy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int saveUserCount(Integer userid) {
        //根据不同的用户生成调用次数的key
        String limitKey = "LIMIT"+"_"+userid;
        //获取redis中指定key的调用次数
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        int limit = -1;
        if (limitNum==null){
            //第一次放进去的时候初始化
            stringRedisTemplate.opsForValue().set(limitKey,"0",3600, TimeUnit.SECONDS);
        }else {
            limit = Integer.parseInt(limitNum)+1;
            stringRedisTemplate.opsForValue().set(limitKey,String.valueOf(limit),3600, TimeUnit.SECONDS);
        }
        return limit;//返回调用次数
    }

    @Override
    public boolean getUserCount(Integer userid) {
        //根据不同的用户生成调用次数的key
        String limitKey = "LIMIT"+"_"+userid;
        //获取redis中指定key的调用次数
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);


        if (limitNum==null){
            //为空直接抛弃说明key异常
            return true;
        }
        return Integer.parseInt(limitNum)>10;//false代表没有超过限制次数
    }
}
