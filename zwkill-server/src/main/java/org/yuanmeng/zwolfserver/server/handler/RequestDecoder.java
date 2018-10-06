package org.yuanmeng.zwolfserver.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yuanmeng.zwolfserver.bean.MsgDispatcherBean;
import org.yuanmeng.zwolfserver.entity.RequestBody;

import javax.annotation.Resource;

/**
 * @author wangtonghe
 * @since 2018/10/5 15:12
 */
@Component
public class RequestDecoder extends SimpleChannelInboundHandler<RequestBody> {

    @Autowired
    private MsgDispatcherBean msgDispatcherBean;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody msg) throws Exception {

        new MsgDispatcherBean().dispatcher(msg, ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
