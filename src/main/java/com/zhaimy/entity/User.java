package com.zhaimy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)//允许set方法链式调用
public class User {
    private Integer id;
    private String name;
    private String password;

}
