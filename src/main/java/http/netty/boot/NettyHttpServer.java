package http.netty.boot;

import factory.InitBean;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import http.netty.handle.HttpChannelInitService;

/**
 * Created by zhuran on 2019/1/25 0025
 */
public class NettyHttpServer {
    private static final int port = 8090;
    private static final String ip = "127.0.0.1";

    public static void start() {
        InitBean initBean = new InitBean();
        initBean.initBeans();
        EventLoopGroup boot = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boot, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new HttpChannelInitService(initBean.getModels(), initBean.getBeanContainerMap()))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boot.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
