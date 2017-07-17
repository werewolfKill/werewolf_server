package com.vector.im.im;

import com.vector.im.handler.ByteToPacketCodec;
import com.vector.im.handler.LoginChannelHandler;
import com.vector.im.handler.PacketChannelHandler;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 一个线程ServiceSocket,可以在一个新线程开启一个服务
 *
 * author: vector.huang
 * date：2016/4/18 1:31
 */
public class ThreadServerSocket extends Thread{

    private int port;//绑定的端口
    private  ChannelFuture channelFuture;//连接好的channel
    private OnChannelActiveListener listener;//当channel 可以用的时候回调

    public ThreadServerSocket(int port){
        this.port = port;
    }

    public void run() {
        try {
            startSocket();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopSocket(){
        if(channelFuture == null||channelFuture.channel() == null){
            channelFuture = null;
            System.out.println("服务器未开启...");
            return;
        }

        if(channelFuture.channel().isOpen()){
            channelFuture.channel().close();
            System.out.println("服务器关闭中...");
        }

    }

    public void startSocket() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(boss,worker);

            boot.channel(NioServerSocketChannel.class);
            boot.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024,0,4,-4,0,false));
                    ch.pipeline().addLast(new ByteToPacketCodec());
                    ch.pipeline().addLast(new LoginChannelHandler(listener));
                    ch.pipeline().addLast(new PacketChannelHandler());
                }
            });

            boot.option(ChannelOption.SO_BACKLOG,128);
            boot.childOption(ChannelOption.SO_KEEPALIVE,true);
            channelFuture = boot.bind(port).sync();
            System.out.println("服务器"+port+"开启成功...");
            channelFuture.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
            channelFuture = null;
            System.out.println("服务器关闭成功...");
        }
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setOnChannelActiveListener(OnChannelActiveListener listener){
        this.listener = listener;
    }

    public interface OnChannelActiveListener{
        void onChannelActive(ChannelHandlerContext ctx);
    }
}
