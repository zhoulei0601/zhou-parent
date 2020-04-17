package com.zhou.page.service;

/**
 * @description: 商品服务 静态模型html
 * @author: zhoulei
 * @createTime: 2020-04-16 15:53
 **/
public interface ItemPageService {

    /**
      * @description 商品 静态模型
      * @params [goodsId]
      * @return boolean
      * @author zhoulei
      * @createtime 2020-04-16 15:55
      */
    boolean genItemHtml(Long[] goodsId);
}
