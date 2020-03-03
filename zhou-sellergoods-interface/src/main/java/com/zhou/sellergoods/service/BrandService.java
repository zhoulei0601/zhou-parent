package com.zhou.sellergoods.service;

import com.zhou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @description: 品牌service
 * @author: zhoulei
 * @createTime: 2020-02-11 09:59
 **/
public interface BrandService {
    /**
      * @description 获取所有品牌
      * @params []
      * @return java.util.List<com.zhou.pojo.TbBrand>
      * @author zhoulei
      * @createtime 2020-02-11 10:03
      */
    List<TbBrand> findAll();

    /**
      * @description 品牌分页
      * @params [pageNum, pageSize]
      * @return entity.PageResult
      * @author zhoulei
      * @createtime 2020-02-11 15:14
      */
    PageResult findPage(int pageNum,int pageSize);

    /**
      * @description 增加品牌
      * @params [brand]
      * @return void
      * @author zhoulei
      * @createtime 2020-02-11 17:08
      */
    void save(TbBrand brand);

    /**
      * @description 根据主键查询品牌
      * @params [id]
      * @return com.zhou.pojo.TbBrand
      * @author zhoulei
      * @createtime 2020-02-11 22:36
      */
    TbBrand findOne(Long id);

    /**
      * @description 批量删除品牌
      * @params [ids]
      * @return int
      * @author zhoulei
      * @createtime 2020-02-11 22:59
      */
    int delete(Long[] ids);

    /**
     * @description 根据查询条件，品牌分页
     * @params [pageNum, pageSize]
     * @return entity.PageResult
     * @author zhoulei
     * @createtime 2020-02-11 15:14
     */
    PageResult findPage(TbBrand brand,int pageNum,int pageSize);

    /**
      * @description 品牌下拉列表
      * @params []
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
      * @author zhoulei
      * @createtime 2020-02-13 16:53
      */
    List<Map<String,String>> selectOptionList();
}
