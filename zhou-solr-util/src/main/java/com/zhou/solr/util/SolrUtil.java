package com.zhou.solr.util;

import com.alibaba.fastjson.JSON;
import com.zhou.mapper.TbItemMapper;
import com.zhou.pojo.TbItem;
import com.zhou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description: solr util
 * @author: zhoulei
 * @createTime: 2020-04-14 13:26
 **/

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    public void importItemData(){
        TbItemExample example =  new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);
        for(TbItem item : itemList){
            Map map = JSON.parseObject(item.getSpec(), Map.class);
            Map newMap = new HashMap<>();
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                try {
                    newMap.put(URLEncoder.encode(key,"UTF-8"),map.get(key));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            item.setSpecMap(newMap);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }


    public void deleteAll(){
        solrTemplate.delete(new SimpleQuery("*:*"));
        solrTemplate.commit();
    }

    public void selectEmpty(){
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is("");
        query.addCriteria(criteria);
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query,TbItem.class);
        System.out.println(scoredPage.getTotalElements());
    }

    public static void main(String[] args) {
        // applicationContext*.xml 在jar包中，classpath*搜索jar
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.selectEmpty();
    }
}
