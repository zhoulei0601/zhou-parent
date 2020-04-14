package com.zhou.search.service;

import entity.SearchItem;

/**
 * @description: TbItem search
 * @author: zhoulei
 * @createTime: 2020-04-14 13:07
 **/
public interface ItemSearchItemService {

    /**
      * @description 搜索
      * @params [searchItem]
      * @return entity.SearchItemReturn
      * @author zhoulei
      * @createtime 2020-04-14 13:12
      */
    SearchItem search(SearchItem searchItem);
}
