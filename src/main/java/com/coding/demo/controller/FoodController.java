package com.coding.demo.controller;

import com.coding.demo.model.Food;
import com.coding.demo.model.JsonResult;
import com.coding.demo.service.Impl.FoodServiceImpl;
import com.coding.demo.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping("/seller/food")
@RestController
@ResponseBody
@Slf4j
public class FoodController {
    @Autowired
    private FoodServiceImpl foodService;
    @Autowired
    private RecommendService recommendService;

    @RequestMapping("/register")
    public JsonResult registerFood(String  name, int sellerID, String foodIntroduction, String species, double price){
        try{
            if(StringUtils.isEmpty(name)) return new JsonResult("食物名不能为空","400","fail");
            if(StringUtils.isEmpty(sellerID)) return new JsonResult("商家店铺号不能为空","400","fail");
            if(StringUtils.isEmpty(foodIntroduction)) return new JsonResult("食物介绍不能为空","400","fail");
            if(StringUtils.isEmpty(species)) return new JsonResult("种类不能为空","400","fail");
            if(StringUtils.isEmpty(price)) return new JsonResult("价格不能为空","400","fail");
            Food food=foodService.selectFood(name);
            if(food!=null){
                return new JsonResult("添加失败，该食物名称已经存在","400","fail");
            }
            if(foodService.InsertFood(name, sellerID, foodIntroduction, species, price)!=0){
                return new JsonResult("添加成功","201");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");
        }
        return new JsonResult("添加失败","400","fail");
    }

    @PostMapping("/loadImg")
    public JsonResult uploadImg(@RequestPart("file") MultipartFile file, @Param(value = "id") Integer id){
        try {
            if (file != null) {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                String upload_file_dir=uploadPathImg;//注意这里需要添加目录信息
                String destFileName =  uploadPathImg +fileName;
                //4.第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录（创建到了webapp下uploaded文件夹下）
                File upload_file_dir_file = new File(upload_file_dir);
                if (!upload_file_dir_file.exists())
                {
                    upload_file_dir_file.mkdirs();
                }
                //5.把浏览器上传的文件复制到希望的位置
                File targetFile = new File(upload_file_dir_file, fileName);
                file.transferTo(targetFile);
                //seller.setPictureUrl(fileName);
                foodService.updateFoodImg(fileName,id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JsonResult("添加成功","200");
    }
    @Value("/home/uploadedImg/")
    private String uploadPathImg;

    @PostMapping("/upload")
    public JsonResult upload(@RequestParam(value = "file")MultipartFile file, HttpServletRequest req, Integer id) throws IOException {
        try {
            if (file != null) {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                String upload_file_dir=uploadPathImg;//注意这里需要添加目录信息
                String destFileName =  uploadPathImg +fileName;
                //4.第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录（创建到了webapp下uploaded文件夹下）
                File upload_file_dir_file = new File(upload_file_dir);
                if (!upload_file_dir_file.exists())
                {
                    upload_file_dir_file.mkdirs();
                }
                //5.把浏览器上传的文件复制到希望的位置
                File targetFile = new File(upload_file_dir_file, fileName);
                file.transferTo(targetFile);
                //seller.setPictureUrl(fileName);
                foodService.updateFoodImg(fileName,id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JsonResult("添加成功","200");
    }

    @PostMapping("/show")
    public JsonResult listSeller(int page,int sellerID){
        //2022.8.24 新增或修改部分   by:Orall
        try{
            //2022.9.15 新增或修改部分   by:Orall（功能：协同过滤推荐算法中记录点击数据）
            int userId = 1;
            //判断当前有没有该用户的商店数据
            if( recommendService.isUserActive(userId,sellerID) == 0 ){        //没有数据则插入
                recommendService.insertUserActive(userId,sellerID);
            }else{          //已经存在则更新
                recommendService.updateUserActive(userId,sellerID);
            }

            List<Food> tmp=foodService.pageFood(page,8,sellerID);

            return new JsonResult(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");
        }
    }

    @PostMapping("/search")
    public JsonResult search( String find,int page) {
        //limit 为8
        try {
            List<Food> tmp = foodService.searchFood(find,page,8);
            return new JsonResult(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");
        }
    }

    @PostMapping("/delete")
    public JsonResult delete(String name){
        try{
            if(foodService.selectFood(name)==null)
            {
                return new JsonResult("删除失败，不存在该食物","400","失败");
            }
            else {
                foodService.deleteFood(name);
                return new JsonResult("删除成功","204");
            }
            } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");
        }

    }

    @PostMapping("/update")
    public JsonResult update(String name,int sellerID,String foodIntroduction,String species,double price){
        try {
            foodService.update(name,sellerID,foodIntroduction,species,price);
            return new JsonResult("修改成功","200","成功","成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");
        }
    }
}
