package com.zhaimy.service.impl;

import com.zhaimy.dao.OrderDao;
import com.zhaimy.dao.UserDao;
import com.zhaimy.entity.Order;
import com.zhaimy.entity.Stock;
import com.zhaimy.entity.User;
import com.zhaimy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int spike(Integer id) {

        //redis限时处理 校验redis中的秒杀商品是否超时
        if (!stringRedisTemplate.hasKey("spike"+id)){
            throw new RuntimeException("当前抢购商品活动已经结束！");
        }


        //1.根据商品id校验库存
        Stock stock = orderDao.checkStock(id);
        if (stock.getSale().equals(stock.getCount())){
            throw new RuntimeException("库存不足！");
        }else {
            //2.扣除库存
            //在sql层面去完成销量的+1和版本号的+1，并且根据商品的id和版本号同时查询更新的商品
            int updateSale = orderDao.updateSale(stock);
            if (updateSale==0){
                throw new RuntimeException("抢购失败,请重试！");
            }else {
                //3.创建订单
                Order order = new Order();
                order.setSid(stock.getId()).setName(stock.getName());
                orderDao.createOrder(order);
                return order.getId();
            }
        }
    }

    @Override
    public String getMd5(Integer id, Integer userid) {
        //验证用户合法性
        User user = userDao.findUserById(userid);
        if (user==null){
            throw new RuntimeException("用户信息不存在");
        }
        //验证商品合法性
        Stock stock = orderDao.checkStock(id);
        if (stock==null){
            throw new RuntimeException("商品信息不合法");
        }
        //生成md5签名，放入redis
        //生成hashKey
        String hashKey = "Key_"+userid+"_"+id;
        String md5 = DigestUtils.md5DigestAsHex((userid + id + "#sdfv#%gy").getBytes());
        stringRedisTemplate.opsForValue().set(hashKey,md5,3600, TimeUnit.SECONDS);

        return md5;
    }


    /**
     * 加上md5的抢购
     * @param id
     * @param userid
     * @param md5
     * @return
     */
    @Override
    public int spikemd5(Integer id, Integer userid, String md5) {
        //先验证签名
        String hashKey = "Key_"+userid+"_"+id;
        if (!md5.equals(stringRedisTemplate.opsForValue().get(hashKey))){
            throw new RuntimeException("当前请求数据不合法");
        }

        //redis限时处理 校验redis中的秒杀商品是否超时
        if (!stringRedisTemplate.hasKey("spike"+id)){
            throw new RuntimeException("当前抢购商品活动已经结束！");
        }

        //1.根据商品id校验库存
        Stock stock = orderDao.checkStock(id);
        if (stock.getSale().equals(stock.getCount())){
            throw new RuntimeException("库存不足！");
        }else {
            //2.扣除库存
            //在sql层面去完成销量的+1和版本号的+1，并且根据商品的id和版本号同时查询更新的商品
            int updateSale = orderDao.updateSale(stock);
            if (updateSale==0){
                throw new RuntimeException("抢购失败,请重试！");
            }else {
                //3.创建订单
                Order order = new Order();
                order.setSid(stock.getId()).setName(stock.getName());
                orderDao.createOrder(order);
                return order.getId();
            }
        }
    }
}
