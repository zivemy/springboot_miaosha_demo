package com.zhaimy.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.zhaimy.service.OrderService;
import com.zhaimy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    //创建令牌桶实例
    private RateLimiter rateLimiter = RateLimiter.create(20);


    @RequestMapping("md5")
    public String getMd5(Integer id,Integer userid){

        String md5;
        try{
            md5 = orderService.getMd5(id,userid);
        }catch (Exception e){
            e.printStackTrace();
            return "获取MD5失败："+e.getMessage();
        }
        return "获取MD5信息为："+ md5;
    }

    /**
     * 秒杀方法
     * @param id 商品id
     * @return
     */
    @GetMapping("spike")
    public String spike(Integer id){
        System.out.println("秒杀商品的id："+id);
        //加入令牌桶的限流
        if (!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)){
            return "当前秒杀活动过于火爆，请重新尝试！";
        }
        try {
            //根据秒杀商品的ID去调用秒杀业务

            int orderId =  orderService.spike(id);
            return "秒杀成功,订单ID为：" + orderId;

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @GetMapping("spikemd5")
    public String spikemd5(Integer id,Integer userid,String md5){
        System.out.println("秒杀商品的id："+id);
        //加入令牌桶的限流
        if (!rateLimiter.tryAcquire(3, TimeUnit.SECONDS)){
            return "当前秒杀活动过于火爆，请重新尝试！";
        }
        try {
            //单用户调用接口的频率限制
            int count = userService.saveUserCount(userid);
            //进行调用次数的判断
            if (userService.getUserCount(userid)){
                return "重试次数过多请过一段时间之后再尝试";
            }

            //根据秒杀商品的ID去调用秒杀业务
            int orderId =  orderService.spikemd5(id,userid,md5);
            return "秒杀成功,订单ID为：" + orderId;

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
