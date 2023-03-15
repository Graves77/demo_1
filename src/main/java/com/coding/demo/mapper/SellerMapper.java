package com.coding.demo.mapper;

import com.coding.demo.model.Position;
import com.coding.demo.model.Seller;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SellerMapper {
    /**
     *添加新的商家
     */
    @Insert("INSERT INTO seller(name,IdCard,introduction,location,prove) VALUES(#{name},#{IdCard},#{introduction},#{location},#{prove})")
    int saveSeller(@Param("name")String  name,@Param("IdCard")String IdCard,@Param("introduction")String introduction,
                   @Param("location")String location,@Param("prove")String prove);

    /**
     * 查找是否存在该商家
     */
    @Select("select * from seller where name=#{name}")
    Seller selectSeller(@Param("name")String name);

    /**
     *分页管理用
     */
    @Select("SELECT *   FROM seller   limit #{page},#{limit};")
    List<Seller> pageSeller(@Param("page") int page, @Param("limit") int limit);

    /**
     * 页面返回查找的信息（显示于页面上）
     */
    @Select("select seller.pictureUrl,seller.`name`  from seller where name like CONCAT('%',#{find},'%') limit #{first},#{second};")
    List<Seller> searchSeller(@Param("find")String find,@Param("first")int first,@Param("second")int second);

    /**
     * 商家照片
     * @param pictureUrl
     * @param id
     * @return
     */
    @Update("update seller set pictureUrl = #{pictureUrl}  where id = #{id}")
    int updateSellerImg(@Param("pictureUrl") String pictureUrl,@Param("id") Integer id);

    /**
     * 删除商家
     * @param name
     */
    @Delete("DELETE FROM seller WHERE `name`=#{name}")
    void deleteSeller(@Param("name")String name);

    @Select("SELECT * from seller WHERE `name`= #{name} AND  prove= #{prove}")
    Seller login(@Param("name")String name, @Param("prove")String prove);

    @Select("SELECT * from seller WHERE `name`= #{name}")
    Seller findSeller(@Param("name")String name);

    @Update("update seller set name = #{name}, introduction=#{introduction} , location=#{location}, prove=#{prove} where IdCard = #{IdCard}")
    void update(@Param("name") String name, @Param("IdCard") String IdCard, @Param("introduction") String introduction, @Param("location") String location, @Param("prove") String prove);

    @Select("SELECT * FROM seller ORDER BY RAND() LIMIT 10;")
    List<Seller> rand();

    @Insert("INSERT INTO position set name=#{name},latitude=#{latitude},longitude=#{longitude},title=#{title},remarks=#{remarks},phone=#{phone},flag=#{flag}")
    int addposition(@Param("name") String name, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("title") String title,
                    @Param("remarks") String remarks, @Param("phone") String phone, @Param("flag") Boolean flag);

    @Select("SELECT * FROM position WHERE `name` = #{name} or flag = 0")
    List<Position> showposition(String name);

    @Delete("DELETE FROM position WHERE `name`=#{name}")
    int deleteposition(String name);
}
