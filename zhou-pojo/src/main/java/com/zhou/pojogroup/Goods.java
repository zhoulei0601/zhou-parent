package com.zhou.pojogroup;

import com.zhou.pojo.TbGoods;
import com.zhou.pojo.TbGoodsDesc;
import com.zhou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 商品组合类
 * @author: zhoulei
 * @createTime: 2020-03-02 13:35
 **/
public class Goods implements Serializable {
    private static final long serialVersionUID = -539499522455978894L;

    private TbGoods goods;//商品
    private TbGoodsDesc goodsDesc;//商品描述-
    private List<TbItem> itemList;//商品SKU

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
