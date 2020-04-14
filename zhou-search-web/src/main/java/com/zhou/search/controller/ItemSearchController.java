package com.zhou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhou.search.service.ItemSearchItemService;
import entity.SearchItem;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: search controller
 * @author: zhoulei
 * @createTime: 2020-04-14 15:14
 **/
@RestController
@RequestMapping("/search")
public class ItemSearchController {

    @Reference(timeout = 3000)
    private ItemSearchItemService itemSearchItemService;

    /**
      * @description 搜索
      * @params [searchItem]
      * @return entity.SearchItemReturn
      * @author zhoulei
      * @createtime 2020-04-14 15:17
      */
    @RequestMapping("/keyword")
    public SearchItem search(@RequestBody SearchItem searchItem){
        return itemSearchItemService.search(searchItem);
    }
}
