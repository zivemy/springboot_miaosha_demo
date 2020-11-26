package com.zhaimy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)//允许set方法链式调用
public class Order {

    private Integer id;
    private Integer sid;
    private String name;
    private Date createTime;
}
