package com.coding.demo.service.Impl;

import com.coding.demo.mapper.SellerMapper;
import com.coding.demo.model.JsonResult;
import com.coding.demo.model.Position;
import com.coding.demo.model.Seller;
import com.coding.demo.service.SellerService;
import com.coding.demo.utils.SensitiveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SensitiveUtils sensitiveUtils;

    @Override
    public int insertSeller(String name, String IdCard, String introduction, String location,String prove) {
        //2022.8.24 新增或修改部分   by:Orall
        return sellerMapper.saveSeller(sensitiveUtils.filter(name), IdCard, sensitiveUtils.filter(introduction), location,prove);

    }

    @Override
    public Seller selectSeller(String name) {
        return sellerMapper.selectSeller(name);
    }


    @Override
    public List<Seller> pageSeller(int page,int limit) {
        //为提升系统性能和用户体验
        //首先在Redis缓存中查询，如果有，直接使用；如果没有，去数据库查询并放入redis缓存
        List<Seller>  listSeller = (List<Seller>) redisTemplate.opsForValue().get("listSeller_"+page);
        if (null == listSeller) {
            //去数据库查询
            //{(page -1) * limit},#{limit};")
            int first = (page - 1) * limit;
            int second = limit;
            listSeller = sellerMapper.pageSeller(first, second);
            //只返回第一张图片
//            for( Seller t : listSeller ){
//                String picture = t.getPictureUrl();
//                int f = picture.indexOf("|");
//                if( f != -1 ) {
//                    t.setPictureUrl(picture.substring(0, f));
//                }
//            }
            //并放入redis缓存
            redisTemplate.opsForValue().set("listSeller_"+page, listSeller, 30, TimeUnit.SECONDS);
        }

        return listSeller;
    }

    @Override
    public List<Seller> searchSeller(String find, int page, int limit) {
        //为提升系统性能和用户体验
        //首先在Redis缓存中查询，如果有，直接使用；如果没有，去数据库查询并放入redis缓存
        List<Seller>  searchSeller = (List<Seller>) redisTemplate.opsForValue().get("searchSeller_"+find);

        if (null == searchSeller) {
            //去数据库查询
            int first = (page - 1) * limit;
            int second = limit;
            searchSeller = sellerMapper.searchSeller(find,first,second);
            //只返回第一张图片
            for( Seller t : searchSeller ){
                String picture = t.getPictureUrl();
                int f = picture.indexOf("|");
                if( f != -1 ) {
                    t.setPictureUrl(picture.substring(0, f));
                }
            }
            //并放入redis缓存
            redisTemplate.opsForValue().set("searchSeller_"+find, searchSeller, 30, TimeUnit.SECONDS);
        }
        return searchSeller;
    }

    @Override
    public int updateSellerImg(String pictureUrl, Integer id) {
        return sellerMapper.updateSellerImg(pictureUrl, id);
    }

    @Override
    public void deleteSeller(String name) {
        sellerMapper.deleteSeller(name);
    }

    @Override
    public Seller login(String name, String prove) {
        return  sellerMapper.login(name,prove);
    }

    @Override
    public Seller sellerDetail(String name) {
        return sellerMapper.findSeller(name);
    }

    @Override
    public void update(String name,String IdCard,String introduction,String location,String prove) {
        sellerMapper.update(name,IdCard,introduction,location,prove);
    }

    @Override
    public List<Seller> pageSellerRand() {
        return sellerMapper.rand();
    }

    @Override
    public int addposition(String name, double latitude, double longitude, String title, String remarks, String phone, Boolean flag) {
        return sellerMapper.addposition(name,latitude,longitude,title,remarks,phone,flag);
    }


    @Override
    public List<Position> showposition(String name) {
        return sellerMapper.showposition(name);
    }

    @Override
    public int deleteposition(String name) {
        return sellerMapper.deleteposition(name);
    }


}
