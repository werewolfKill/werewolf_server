package org.yuanmeng.zwolfserver.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author wangtonghe
 * @since 2018/10/4 22:57
 */
@Component
public class WolfNettyServer {


    private Logger logger = LoggerFactory.getLogger(WolfNettyServer.class);

    @Value("${server.port}")
    private int port;

    private EventLoopGroup parentGroup = new NioEventLoopGroup(1);
    private EventLoopGroup childGroup = new NioEventLoopGroup();

    private final static int DEFAULT_PORT = 9000;


    @PostConstruct
    public void run() throws InterruptedException {
        try {
            startServer();
        } catch (InterruptedException e) {
            logger.error("server error", e);
            throw e;
        }
    }

    private void startServer() throws InterruptedException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new NettyHandlerInitializer());
        ChannelFuture cf = serverBootstrap.bind(port <= 0 ? DEFAULT_PORT : port).sync();
        if (cf.isSuccess()) {
            logger.info("the server is start ...");
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        try {
            parentGroup.shutdownGracefully().sync();
            childGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            logger.error("stop server fail...", e);
            throw e;
        }
        logger.info("the server is shut down ...");
    }
}
