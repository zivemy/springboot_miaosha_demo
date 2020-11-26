package com.zhaimy.dao;

import com.zhaimy.entity.Order;
import com.zhaimy.entity.Stock;
import com.zhaimy.entity.User;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
    //根据商品id查询库存信息
    User findUserById(Integer id);

}
