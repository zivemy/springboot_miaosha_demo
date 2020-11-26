package com.zhaimy.dao;

import com.zhaimy.entity.Order;
import com.zhaimy.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public interface OrderDao {
    //根据商品id查询库存信息
    Stock checkStock(Integer id);

    //根据商品id扣除库存
    int updateSale(Stock stock);

    //创建订单
    void createOrder(Order order);
}
