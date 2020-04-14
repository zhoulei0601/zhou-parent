package com.zhou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhou.pojo.TbItem;
import com.zhou.search.service.ItemSearchItemService;
import entity.SearchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;


/**
 * @description: 搜索
 * @author: zhoulei
 * @createTime: 2020-04-14 13:13
 **/
@Service
public class ItemSearchServiceImpl implements ItemSearchItemService {
    @Autowired
    private SolrTemplate solrTemplate;
    /**
      * @description 搜索
      * @params [searchItem]
      * @return entity.SearchItemReturn
      * @author zhoulei
      * @createtime 2020-04-14 13:13
      */
    @Override
    public SearchItem search(SearchItem searchItem) {
        String keyWords = searchItem.getKeyWords();
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(keyWords);
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        SearchItem searchItemReturn = new SearchItem();
        searchItemReturn.setItemList(scoredPage.getContent());
        return searchItemReturn;
    }
}
