package com.zhou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhou.content.service.ContentService;
import com.zhou.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 广告 controller
 * @author: zhoulei
 * @createTime: 2020-04-13 08:57
 **/
@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    /**
      * @description 根据categoryId 查询内容列表
      * @params [categoryId]
      * @return java.util.List<com.zhou.pojo.TbContent>
      * @author zhoulei
      * @createtime 2020-04-13 10:00
      */
    @RequestMapping("/listContentByCategoryId")
    public List<TbContent> listContentByCategoryId(Long categoryId) {
        return contentService.listContentByCategoryId(categoryId);
    }

}
