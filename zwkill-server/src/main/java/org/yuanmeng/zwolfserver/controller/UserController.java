package org.yuanmeng.zwolfserver.controller;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.yuanmeng.zwolfserver.annotation.RequestMapping;
import org.yuanmeng.zwolfserver.entity.RequestInfo;
import org.yuanmeng.zwolfserver.entity.ResponseBody;
import org.yuanmeng.zwolfserver.entity.UserChannel;
import org.yuanmeng.zwolfserver.data.UserChannelTable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangtonghe
 * @since 2018/10/5 16:00
 */
@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private static AtomicInteger autoUserId = new AtomicInteger(0);

    /**
     * 用户登录，验证密码，将用户写入在线列表
     *
     * @param name        用户名
     * @param password    密码
     * @param requestInfo 请求信息
     */
    @RequestMapping(value = "/data/login")
    public void login(String name, String password, RequestInfo requestInfo) {

        logger.info("login name={},password={}", name, password);
        // 模拟验证账号密码过程，通过返回userId
        int newUserId = verifyPwd(name, password);
        Channel channel = requestInfo.getChannel();

        UserChannel userChannel = new UserChannel();
        userChannel.setChannel(channel);
        userChannel.setUserName(name);
        userChannel.setUserId(newUserId);
        // 保存新用户
        UserChannelTable.addUser(newUserId, userChannel);
        ResponseBody responseBody = new ResponseBody(0, requestInfo.getTargetUrl(), null);
        channel.writeAndFlush(responseBody);
        System.out.println("当前在线用户人数为: " + UserChannelTable.getTotal());
    }

    private int verifyPwd(String name, String password) {
        return getNextUserId();

    }

    private static int getNextUserId() {
        return autoUserId.incrementAndGet();

    }

}
