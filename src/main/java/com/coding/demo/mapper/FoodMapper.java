package com.coding.demo.mapper;

import com.coding.demo.model.Food;
import com.coding.demo.model.Seller;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FoodMapper {
    @Insert("INSERT INTO food(name,sellerID,foodIntroduction,species,price) VALUES(#{name},#{sellerID},#{foodIntroduction},#{species},#{price})")
    int saveFood(@Param("name") String  name, @Param("sellerID") int sellerID,
                 @Param("foodIntroduction") String foodIntroduction, @Param("species") String species, double price);

    @Select("SELECT * FROM food where name=#{name}")
    Food selectFood(@Param("name")String name);

    @Update("update food set pictureUrl = #{pictureUrl}  where id = #{id}")
    int updateFoodImg(@Param("pictureUrl") String pictureUrl,@Param("id") Integer id);

    /**
     *分页管理用
     */
    @Select("SELECT *   FROM food Where sellerID = #{sellerID}  limit #{page},#{limit};")
    List<Food> pageFood(@Param("page") int page, @Param("limit") int limit,@Param("sellerID")int sellerID);

    /**
     * 页面返回查找的信息（显示于页面上）
     */
    @Select("select food.pictureUrl,food.`name`  from food where name like CONCAT('%',#{find},'%') limit #{first},#{second};")
    List<Food> searchFood(@Param("find")String find,@Param("first")int first,@Param("second")int second);

    /**
     * 删除食物
     * @param name
     */
    @Delete("DELETE FROM food WHERE `name`=#{name}")
    void deleteFood(String name);

    @Update("update food set sellerID = #{sellerID}, foodIntroduction=#{foodIntroduction},  species=#{species}, price=#{price} where name = #{name}")
    void update(@Param("name") String name, @Param("sellerID") int sellerID, @Param("foodIntroduction") String foodIntroduction, @Param("species") String species, @Param("price") double price);
}
