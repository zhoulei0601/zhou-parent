package com.zhou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhou.pojo.TbBrand;
import com.zhou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description: 品牌controller
 * @author: zhoulei
 * @createTime: 2020-02-11 10:12
 **/
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    /**
      * @description 获取所有品牌
      * @params []
      * @return java.util.List<com.zhou.pojo.TbBrand>
      * @author zhoulei
      * @createtime 2020-02-11 10:17
      */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    /**
      * @description 品牌分页
      * @params [pageNum, pageSize]
      * @return entity.PageResult
      * @author zhoulei
      * @createtime 2020-02-11 15:32
      */
    @RequestMapping("/findPage")
    public PageResult findPage(int pageNum,int pageSize){
        return brandService.findPage(pageNum,pageSize);
    }

    /**
      * @description 增加
      * @params [brand]
      * @return entity.Result
      * @author zhoulei
      * @createtime 2020-02-11 17:10
      */
    @RequestMapping("/save")
    public Result save(@RequestBody TbBrand brand){
        try {
            brandService.save(brand);
            return new Result(true,"保存成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"保存失败！");
        }
    }

    /**
      * @description 根据主键查询品牌
      * @params [id]
      * @return com.zhou.pojo.TbBrand
      * @author zhoulei
      * @createtime 2020-02-11 22:38
      */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    /**
      * @description 批量删除品牌
      * @params [ids]
      * @return entity.Result
      * @author zhoulei
      * @createtime 2020-02-11 23:02
      */
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try{
            brandService.delete(ids);
            return new Result(true,"删除成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败！");
        }
    }

    /**
      * @description 根据查询条件 分页
      * @params \
      * @return entity.PageResult
      * @author zhoulei
      * @createtime 2020-02-11 23:41
      */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand brand,int pageNum,int pageSize){
        return brandService.findPage(brand,pageNum,pageSize);
    }

    /***
      * @description 品牌下拉
      * @params []
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
      * @author zhoulei
      * @createtime 2020-02-13 22:18
      */
    @RequestMapping("/selectOptionList")
    public List<Map<String,String>> selectOptionList(){
        return brandService.selectOptionList();
    }

}
