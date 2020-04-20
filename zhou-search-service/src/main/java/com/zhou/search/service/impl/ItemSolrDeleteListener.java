package com.zhou.search.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @description: solr索引库更新 监听
 * @author: zhoulei
 * @createTime: 2020-04-20 11:10
 **/
@Component
public class ItemSolrDeleteListener implements MessageListener {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("solr删除：" + JSON.toJSONString(ids));
            Query query = new SimpleQuery();
            Criteria criteria = new Criteria("item_goodsId").in(ids);
            query.addCriteria(criteria);
            solrTemplate.delete(query);
            solrTemplate.commit();
            System.out.println("删除成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
