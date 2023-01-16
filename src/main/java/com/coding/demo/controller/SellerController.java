package com.coding.demo.controller;

import com.coding.demo.mapper.SellerMapper;
import com.coding.demo.model.JsonResult;
import com.coding.demo.model.Seller;
import com.coding.demo.service.Impl.SellerServiceImpl;
import com.coding.demo.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/seller")
public class SellerController {
    @Resource
    SellerMapper sellerMapper;

    @Autowired
    private SellerServiceImpl sellerService;
    @PostMapping("/register")
    public JsonResult register(String  name,String IdCard,String introduction,String location,String prove){
        try{
            if(name.isEmpty()) return new JsonResult("用户名不能为空","400","fail");
            if(IdCard.isEmpty()) return new JsonResult("身份证不能为空","400","fail");
            if(introduction.isEmpty()) return new JsonResult("介绍不能为空","400","fail");
            if(location.isEmpty()) return new JsonResult("位置不能为空","400","fail");
            if(prove.isEmpty()) return new JsonResult("证明不能为空","400","fail");
            Seller seller= sellerService.selectSeller(name);
            if(seller !=null)return new JsonResult("该用户已经存在","400","fail");

            if(sellerService.insertSeller(name, IdCard, introduction, location,prove)==1) {
                return new JsonResult("申请成功","201");
            }
            } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器内部出错","500","fail");
        }
        return new JsonResult("申请失败","400","fail");
    }

    @PostMapping("/login")
    public JsonResult login(String name,String prove){
        Seller seller = sellerService.login(name,prove);
        if(seller==null){
            return new JsonResult("登录失败","400","错误");
        }
        else return new JsonResult("登录成功","200");
    }

    @PostMapping("/show")
    public JsonResult listSeller(int page){
            try{

                List<Seller> tmp=sellerService.pageSeller(page,8);
                return new JsonResult(tmp);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonResult("服务器内部错误","500","错误");
            }
    }
    @PostMapping("/search")
    public JsonResult search( String find,int page) {
        //limit 为8
        try {
            List<Seller> tmp = sellerService.searchSeller(find,page,8);
            return new JsonResult(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器内部错误","500","错误");
        }
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
                sellerService.updateSellerImg(fileName,id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JsonResult("添加成功","200");
    }

    @PostMapping("/delete")
    public JsonResult delete(String name){
        try{
            if(sellerService.selectSeller(name)==null)
            {
                return new JsonResult("删除失败，不存在该用户","400","失败");
            }
            else {
                sellerService.deleteSeller(name);
                return new JsonResult("删除成功","204");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");
        }

    }

    @PostMapping("/detail")
    public JsonResult detail(String name){
        Seller seller = sellerService.sellerDetail(name);
        return new JsonResult(seller,"200","成功");
    }

    @PostMapping("/update")
    public JsonResult update(String name,String IdCard,String introduction,String location,String prove){
        try {
            sellerService.update(name,IdCard,introduction,location,prove);
            return new JsonResult("修改成功","200");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult("服务器异常","500","失败");

        }

    }
}
