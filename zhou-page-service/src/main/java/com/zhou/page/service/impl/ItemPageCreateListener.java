package com.zhou.page.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @description: page Listener
 * @author: zhoulei
 * @createTime: 2020-04-20 12:54
 **/
@Component
public class ItemPageCreateListener implements MessageListener {
    @Autowired
    private ItemPageServiceImpl itemPageService;
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("topic-create:" + JSON.toJSONString(ids));
            itemPageService.genItemHtml(ids);
            System.out.println("商品明细页面创建成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
