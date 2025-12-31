package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.websocket;

import cn.hutool.json.JSONUtil;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.constants.MessageTypeEnum;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.model.MessageDTO;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class MessageInboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务端收到消息：{}", msg.text());
        MessageDTO messageDTO = JSONUtil.toBean(msg.text(), MessageDTO.class);
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.of(messageDTO.getType());
        switch (messageTypeEnum){
            case ACK:

            case LOG_OUT:

            case HEART_BEAT:

            default:
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("websocket has build");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        处理心跳
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    log.error("读空闲超时......关闭连接...{}, 用户ID{}", ctx.channel().remoteAddress(), ChannelManager.getChannelUser(ctx.channel()));
                    offline(ctx);
                case WRITER_IDLE:
                    log.error("写空闲超时");
                case ALL_IDLE:
                    log.error("读写空闲超时");
            }
        }
//        处理握手，协议升级
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            String token = NettyUtils.getAttr(ctx.channel(), NettyUtils.TOKEN);
            String userUuid = NettyUtils.getAttr(ctx.channel(), NettyUtils.UID);

            if(!validateToken(userUuid, token)){
                log.info("Token invalid");
                ctx.close();
                return;
            }

// 存储用户的管道信息
            Channel channel = ChannelManager.getChannelByUserId(userUuid);
            if (channel != null) {
                ChannelManager.removeUserChannel(userUuid);
                ChannelManager.removeChannelUser(channel);
                channel.close();
            }

            // 在将新的 channel 放入到其中
            ChannelManager.addUserChannel(userUuid, ctx.channel());
            ChannelManager.addChannelUser(userUuid, ctx.channel());
            log.info("客户连接成功， 用户ID：{}",userUuid + "管道地址： " + ctx.channel().remoteAddress());
        }
    }

    public void offline(ChannelHandlerContext ctx){
        String userUuid = ChannelManager.getChannelUser(ctx.channel());
        try{
            ChannelManager.removeChannelUser(ctx.channel());
            if (userUuid != null) {
                ChannelManager.removeUserChannel(userUuid);
                log.info("客户端关闭连接UserID: {}, 客户端地址为:{}", userUuid, ctx.channel().remoteAddress());
            }
        } catch (Exception e){
            log.error("处理退出登录异常", e);
        } finally {
            if(ctx.channel() != null){
                ctx.channel().close();
            }
        }
    }

    private boolean validateToken(String userUuid, String token){
        Claims claims = JwtUtil.parse(token);
        String userId = claims.getSubject();
        if(userId == null || !userId.equals(userUuid)){
            return false;
        }
        return true;
    }

}
