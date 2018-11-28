package org.yuanmeng.zwolfserver.server.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.yuanmeng.zwolfserver.server.handler.RequestDecoder;
import org.yuanmeng.zwolfserver.server.handler.RequestToTypeCodec;

/**
 * @author wangtonghe
 * @since 2018/10/5 10:17
 */
public class NettyHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
        channelPipeline.addLast(new RequestToTypeCodec());
        channelPipeline.addLast(new RequestDecoder());
    }
}
