package com.coding.demo.service;

import com.coding.demo.model.Seller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SellerService {


    /**
     * 申请成为商家
     * @param name
     * @param IdCard
     * @param introduction
     * @param location
     * @return 申请成功几个
     */
    int insertSeller(String name,String IdCard,String introduction,String location,String prove);

    /**
     * 查询是否存在该商家
     * @param name
     * @return
     */
    Seller selectSeller(String name);


    /**
     * 分页式管理
     * @param page
     * @param limit
     * @return
     */
    List<Seller> pageSeller(int page,int limit);

    /**
     * 查找对应的商家
     * @param find
     * @param page
     * @param limit
     * @return
     */
    List<Seller> searchSeller(String find,int page, int limit);

    /**
     * 更新照片
     * @param pictureUrl
     * @param id
     * @return
     */
    int  updateSellerImg(String pictureUrl, Integer id);

    /**
     * 删除商家
     * @param name
     */
    void deleteSeller(String  name);

    /**
     * 商家登录
     * @param name
     * @param prove
     * @return
     */
    Seller login(String name, String prove);

    Seller sellerDetail(String name);

    void update(String name,String IdCard,String introduction,String location,String prove);
}
