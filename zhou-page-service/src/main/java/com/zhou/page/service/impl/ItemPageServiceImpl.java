package com.zhou.page.service.impl;

import com.zhou.mapper.TbGoodsDescMapper;
import com.zhou.mapper.TbGoodsMapper;
import com.zhou.mapper.TbItemCatMapper;
import com.zhou.mapper.TbItemMapper;
import com.zhou.page.service.ItemPageService;
import com.zhou.pojo.TbGoods;
import com.zhou.pojo.TbGoodsDesc;
import com.zhou.pojo.TbItem;
import com.zhou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 商品 静态模型实现类
 * @author: zhoulei
 * @createTime: 2020-04-16 15:55
 **/
@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private FreeMarkerConfig freemarkerConfig;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Value("${pagedir}")
    private String pageDir;


    /**
      * @description 商品明细模型
      * @params [goodsId]
      * @return boolean
      * @author zhoulei
      * @createtime 2020-04-16 15:56
      */
    @Override
    public boolean genItemHtml(Long[] goodsIds) {
        Configuration configuration = freemarkerConfig.getConfiguration();
        Writer writer = null;
        File file = new File(pageDir);
        if(!file.exists()){
            file.mkdir();
        }
        try {
            Template template = configuration.getTemplate("item.ftl");
            for(Long goodsId : goodsIds){
                TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
                TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
                Map<String,Object> dataModel = new HashMap<>();
                dataModel.put("goods",goods);
                dataModel.put("goodsDesc",goodsDesc);
                //查询商品分类名称
                String catName1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
                String catName2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
                String catName3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
                dataModel.put("catName1",catName1);
                dataModel.put("catName2",catName2);
                dataModel.put("catName3",catName3);
                //查询商品SKU
                TbItemExample example = new TbItemExample();
                TbItemExample.Criteria criteria = example.createCriteria();
                criteria.andGoodsIdEqualTo(goodsId);
                criteria.andStatusEqualTo("1");//审核
                example.setOrderByClause("is_default desc");
                List<TbItem> itemList = itemMapper.selectByExample(example);
                dataModel.put("itemList",itemList);

                writer = new FileWriter(new File(pageDir + goods.getId() + ".html"));
                template.process(dataModel,writer);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
      * @description 删除商品明细页
      * @params [ids]
      * @return void
      * @author zhoulei
      * @createtime 2020-04-20 13:10
      */
    public void deleteItemPage(Long[] ids){
        for(Long id : ids){
            File file = new File(pageDir + id + ".html");
            if(file != null){
                file.delete();
            }
        }
    }
}
