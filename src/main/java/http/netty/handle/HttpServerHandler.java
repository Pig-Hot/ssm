package http.netty.handle;

import exception.RequestException;
import http.model.HttpControllerModel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import http.netty.coder.ByteBufToBytes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by zhuran on 2019/1/25 0025
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private ByteBufToBytes reader;
    private List<HttpControllerModel> models;
    public Map<String, Object> beanContainerMap;
    private String uri;

    public HttpServerHandler(List<HttpControllerModel> models, Map<String, Object> beanContainerMap) {
        this.models = models;
        this.beanContainerMap = beanContainerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if (HttpUtil.isContentLengthSet(request)) {
                reader = new ByteBufToBytes((int) HttpUtil.getContentLength(request));
                uri = request.uri();
            }
            if (!checkRequest(request)) {
                String resultStr = "本服务器只支持json格式,请求格式错误";
                writeAndFlushResponse(ctx, resultStr);
                throw new RequestException("本服务器只支持json格式,请求格式错误");
            }
        }
        if (msg instanceof HttpContent) {
            for (HttpControllerModel model : models) {
                if (model.getPath().equals(uri)) {
                    HttpContent httpContent = (HttpContent) msg;
                    ByteBuf content = httpContent.content();
                    reader.reading(content);
                    content.release();
                    if (reader.isEnd()) {
                        String resultStr = new String(reader.readFull());
                        Object object = beanContainerMap.get(model.getClassName());
                        Method method = object.getClass().getMethod(model.getMethodName(), String.class);
                        try {
                            method.invoke(object, resultStr);
                            writeAndFlushResponse(ctx, resultStr);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            writeAndFlushResponse(ctx, e.toString());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void writeAndFlushResponse(ChannelHandlerContext ctx, String resultStr) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(resultStr.getBytes()));
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
    }

    private boolean checkRequest(HttpRequest httpRequest) {
        return httpRequest.headers().get("Content-Type").equals("application/json");
    }

}
