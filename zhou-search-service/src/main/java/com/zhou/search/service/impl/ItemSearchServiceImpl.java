package com.zhou.search.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.zhou.pojo.TbItem;
import com.zhou.pojo.TbItemCat;
import com.zhou.search.service.ItemSearchItemService;
import entity.SearchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @description: 搜索
 * @author: zhoulei
 * @createTime: 2020-04-14 13:13
 **/
@Service
public class ItemSearchServiceImpl implements ItemSearchItemService {
    @Autowired
    private SolrTemplate solrTemplate;

    //reids service
    @Autowired
    private RedisTemplate redisTemplate;

    /**
      * @description 搜索
      * @params [searchItem]
      * @return entity.SearchItemReturn
      * @author zhoulei
      * @createtime 2020-04-14 13:13
      */
    @Override
    public SearchItem search(SearchItem searchItem) {
        SearchItem searchItemReturn = new SearchItem();
        //1.查询高亮列表
        SearchItem returnItem = listData(searchItem);
        searchItemReturn.setItemList(returnItem.getItemList());
        searchItemReturn.setTotalRows(returnItem.getTotalRows());
        searchItemReturn.setTotalPages(returnItem.getTotalPages());
        searchItemReturn.setPageNo(returnItem.getPageNo());
        //2.查询商品分类
        List<String> categoryList = listCategoryGroup(searchItem);
        searchItemReturn.setCategoryList(categoryList);
        if(categoryList != null && !categoryList.isEmpty()){
            String category = searchItem.getCategory();
            if(StringUtils.isEmpty(category)){
                category = categoryList.get(0);
            }
            //3.查询品牌列表
            searchItemReturn.setBrandList(searchBrandList(category));
            //4.查询规格列表
            searchItemReturn.setSpecList(searchSpecList(category));
        }
        return searchItemReturn;
    }

    /**
      * @description 高亮查询
      * @params [searchItem]
      * @return java.util.List<com.zhou.pojo.TbItem>
      * @author zhoulei
      * @createtime 2020-04-14 22:32
      */
    private SearchItem listData(SearchItem searchItem){
        String keyWords = searchItem.getKeyWords();
        //关键字之间有空格会破坏分词
        if(StringUtils.isNotEmpty(keyWords)){
            searchItem.setKeyWords(keyWords.replaceAll(" ",""));
        }
        //关键字高亮
        HighlightQuery highlightQuery = new SimpleHighlightQuery();

        //设置高亮选项
        HighlightOptions options = new HighlightOptions().addField("item_title");
        options.setSimplePrefix("<em style='color:red'>");
        options.setSimplePostfix("</em>");
        highlightQuery.setHighlightOptions(options);

        //1.1 关键字查询
        Criteria criteria = new Criteria("item_keywords").is(keyWords);
        highlightQuery.addCriteria(criteria);
        //1.2 商品分类过滤
        if(StringUtils.isNotEmpty(searchItem.getCategory())){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria fiterCriteria = new Criteria("item_category").is(searchItem.getCategory());
            filterQuery.addCriteria(fiterCriteria);
            highlightQuery.addFilterQuery(filterQuery);
        }
        //1.3 品牌过滤
        if(StringUtils.isNotEmpty(searchItem.getBrand())){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchItem.getBrand());
            filterQuery.addCriteria(filterCriteria);
            highlightQuery.addFilterQuery(filterQuery);
        }
        //1.4 规格过滤
        if(searchItem.getSpec() != null){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Map specMap = searchItem.getSpec();
            Iterator iterator = specMap.keySet().iterator();
            while (iterator.hasNext()){
                Criteria filterCriteria = null;
                String key = (String) iterator.next();
                try {
                    filterCriteria = new Criteria("item_spec_" +
                            URLEncoder.encode(key,"UTF-8").replaceAll("%","_")).is(specMap.get(key));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                filterQuery.addCriteria(filterCriteria);
            }
            highlightQuery.addFilterQuery(filterQuery);
        }

        //1.5 价格过滤
        if(StringUtils.isNotEmpty(searchItem.getPrice())){
            String[] prices = searchItem.getPrice().split("-");
            if(!"0".equals(prices[0])){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                filterQuery.addCriteria(filterCriteria);
                highlightQuery.addFilterQuery(filterQuery);
            }
            if(!"*".equals(prices[1])){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                filterQuery.addCriteria(filterCriteria);
                highlightQuery.addFilterQuery(filterQuery);
            }
        }
        
        //1.6 分页
        Integer pageNo = StringUtils.isEmpty(searchItem.getPageNo()) ? 1 : Integer.valueOf(searchItem.getPageNo());
        Integer pageSize = StringUtils.isEmpty(searchItem.getPageSize()) ? 20 : Integer.valueOf(searchItem.getPageSize());
        highlightQuery.setOffset((pageNo - 1) * pageSize);//起始索引
        highlightQuery.setRows(pageSize);//每页记录数


        //1.7 排序
        String sortField = searchItem.getSortField();
        String sortValue = searchItem.getSort();
        if(StringUtils.isNotEmpty(sortField)){
            Sort sort = null;
            if("asc".equalsIgnoreCase(sortValue)){
                sort = new Sort(Sort.Direction.ASC,"item_" + sortField);
            }else{
                sort = new Sort(Sort.Direction.DESC,"item_" + sortField);
            }
            highlightQuery.addSort(sort);
        }
        

        //高亮页
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(highlightQuery,TbItem.class);

        //高亮集合入口
        List<HighlightEntry<TbItem>> entryList = highlightPage.getHighlighted();
        for(HighlightEntry<TbItem> entry : entryList){
            //高亮列表（高亮域个数） getSnipplets（每个域可存储多值）
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            TbItem item = entry.getEntity();
            if(highlightList != null && !highlightList.isEmpty() ){
                List<String> hightList = highlightList.get(0).getSnipplets();
                if(hightList != null && !hightList.isEmpty()){
                    item.setTitle(hightList.get(0));
                }
            }
        }
        SearchItem returnItem = new SearchItem();
        returnItem.setItemList(highlightPage.getContent());
        returnItem.setTotalPages(String.valueOf(highlightPage.getTotalPages()));//总页数
        returnItem.setTotalRows(String.valueOf(highlightPage.getTotalElements()));//总记录数
        returnItem.setPageNo(searchItem.getPageNo());
        return returnItem;
    }

    /**
      * @description 根据搜索条件，分组查询商品分类
      * @params [searchItem]
      * @return java.util.List<java.lang.String>
      * @author zhoulei
      * @createtime 2020-04-14 22:49
      */
    private List<String> listCategoryGroup(SearchItem searchItem){
        String keyWords = searchItem.getKeyWords();
        Query query = new SimpleQuery();

        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //设置查询条件
        Criteria criteria = new Criteria("item_keywords").is(keyWords);
        query.addCriteria(criteria);

        //分组页
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query,TbItem.class);
        //根据分组域 获取结果集
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
        //分组结果入口页
        Page<GroupEntry<TbItem>>  groupEntries = groupResult.getGroupEntries();
        List<GroupEntry<TbItem>> contentList = groupEntries.getContent();

        List<String> groupValueList = new ArrayList<>();
        for(GroupEntry<TbItem>  entry : contentList){
            groupValueList.add(entry.getGroupValue());
        }

        return groupValueList;

    }

    /**
      * @description 根据商品分类查询 品牌列表
      * @params [category]
      * @return java.util.List<java.util.Map>
      * @author zhoulei
      * @createtime 2020-04-15 10:25
      */
    private List<Map> searchBrandList(String category){
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        return brandList;
    }

    /**
      * @description 根据商品分类 查询规格列表
      * @params [category]
      * @return java.util.List<java.util.Map>
      * @author zhoulei
      * @createtime 2020-04-15 10:32
      */
    private List<Map> searchSpecList(String category){
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
        return specList;
    }

    /**
      * @description 更新索引库
      * @params [list]
      * @return void
      * @author zhoulei
      * @createtime 2020-04-15 17:59
      */
    @Override
    public void importItemCat(List<TbItem> list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    /**
      * @description 删除商品索引库
      * @params [ids]
      * @return void
      * @author zhoulei
      * @createtime 2020-04-15 17:59
      */
    @Override
    public void deleteItemCat(Long[] ids) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(ids);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

}
