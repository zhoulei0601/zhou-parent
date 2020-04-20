package com.zhou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * @description: solr索引库更新 监听
 * @author: zhoulei
 * @createTime: 2020-04-20 11:10
 **/
@Component
public class ItemSolrImportListener implements MessageListener {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String itemListJson = textMessage.getText();
            System.out.println("收到solr导入：" + itemListJson);
            List<TbItem> itemList = JSON.parseArray(itemListJson, TbItem.class);
            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();
            System.out.println("导入成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
