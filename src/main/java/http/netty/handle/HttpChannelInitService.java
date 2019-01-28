package http.netty.handle;

import http.model.HttpControllerModel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuran on 2019/1/25 0025
 */
public class HttpChannelInitService extends ChannelInitializer<SocketChannel> {

    private List<HttpControllerModel> models;
    private Map<String, Object> beanContainerMap;

    public HttpChannelInitService(List<HttpControllerModel> models,Map<String, Object> beanContainerMap){
        this.models = models;
        this.beanContainerMap = beanContainerMap;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast(new HttpRequestDecoder());
        sc.pipeline().addLast(new HttpResponseEncoder());
        sc.pipeline().addLast(new HttpServerHandler(models,beanContainerMap));
    }
}
