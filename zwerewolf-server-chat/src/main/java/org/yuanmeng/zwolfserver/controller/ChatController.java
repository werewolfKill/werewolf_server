package org.yuanmeng.zwolfserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.yuanmeng.zwolfserver.annotation.RequestMapping;

/**
 * 聊天消息 org.yuanmeng.zwolfserver.sso.controller
 *
 * @author wangtonghe
 * @since 2018/10/5 20:52
 */
@Controller
public class ChatController {

    private Logger logger = LoggerFactory.getLogger(ChatController.class);


    @RequestMapping(value = "/chat/group")
    public void groupChat(String userId, String text) {



    }
}
