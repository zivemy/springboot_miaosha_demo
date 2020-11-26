package com.zhaimy.service;

public interface UserService {
    //向redis中写入用户访问次数
    int saveUserCount(Integer userid);
    //判断单位时间内调用次数
    boolean getUserCount(Integer userid);
}
