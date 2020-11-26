package com.zhaimy.service;

public interface OrderService {
    //用来处理秒杀的方法,并返回订单id
    int spike(Integer id);

    String getMd5(Integer id, Integer userid);

    int spikemd5(Integer id, Integer userid, String md5);
}
