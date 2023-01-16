package com.coding.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Food implements Serializable {
    int foodID;//标识主键
    String name;//菜名
    int sellerId;//用于连接
    String foodIntroduction;//食物介绍
    String species;//菜品种类
    double price;//价格
    String pictureUrl;
}
