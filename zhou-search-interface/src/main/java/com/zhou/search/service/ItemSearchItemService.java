package com.zhou.search.service;

import com.zhou.pojo.TbItem;
import entity.SearchItem;

import java.util.List;

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

    /**
      * @description 更新索引数据
      * @params [list]
      * @return void
      * @author zhoulei
      * @createtime 2020-04-15 17:58
      */
    void importItemCat(List<TbItem> list);

    /**
      * @description 删除索引库
      * @params [ids]
      * @return void
      * @author zhoulei
      * @createtime 2020-04-15 17:58
      */
    void deleteItemCat(Long[] ids);
}
