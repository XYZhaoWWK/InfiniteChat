package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.websocket;

import cn.hutool.json.JSONUtil;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.constants.MessageTypeEnum;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.constants.UserConstants;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.excption.MessageTypeException;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.model.AckData;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.model.LogOutData;

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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.InetAddress;

@Slf4j
@Sharable
@AllArgsConstructor
public class MessageInboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private StringRedisTemplate redisTemplate;

//    客户端发一条聊天json时触发这个
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务端收到消息：{}", msg.text());
        MessageDTO messageDTO = JSONUtil.toBean(msg.text(), MessageDTO.class);
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.of(messageDTO.getType());
        switch (messageTypeEnum){
            case ACK:
                processACK(messageDTO);
            case LOG_OUT:
                processLogOut(ctx, messageDTO);
            case HEART_BEAT:
                processHeartBeat(ctx, messageDTO);
            default:
                processIllegal(messageDTO);
        }

    }
    private void processACK(MessageDTO msg){
        // 处理客户端成功返回的数据
        AckData ackData = JSONUtil.toBean(msg.getData().toString(), AckData.class);
        log.info("ackData:{}",ackData);
        log.info("推送消息成功！");
    }

    private void processLogOut(ChannelHandlerContext ctx, MessageDTO msg){
        LogOutData logOutData = JSONUtil.toBean(msg.getData().toString(), LogOutData.class);
        Integer userUuid = logOutData.getUserUuid();
        log.info("请求断开用户{}的连接...",userUuid);
        offline(ctx);
        log.info("断开连接成功！");
    }

    private void processHeartBeat(ChannelHandlerContext ctx, MessageDTO msg){
        log.info("收到心跳包");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(MessageTypeEnum.HEART_BEAT.getCode());
        TextWebSocketFrame frame = new TextWebSocketFrame(JSONUtil.toJsonStr(messageDTO));
        ctx.channel().writeAndFlush(frame);
    }

    private void processIllegal(MessageDTO msg){
        throw new MessageTypeException("不支持的消息格式！");
    }


    //    客户端tcp连接成功时触发这个
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("websocket has build");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        offline(ctx);

        super.channelInactive(ctx);
    }

//    完成协议升级以及心跳断了时触发这个
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        处理心跳
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    log.error("读空闲超时......关闭连接...{}, 用户ID{}", ctx.channel().remoteAddress(), ChannelManager.getChannelUser(ctx.channel()));
                    offline(ctx);
                    break;
                case WRITER_IDLE:
                    log.error("写空闲超时");
                    break;
                case ALL_IDLE:
                    log.error("读写空闲超时");
                    break;
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
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            redisTemplate.opsForValue().set(UserConstants.USER_SESSION + userUuid, hostAddress);


// 存储用户的管道信息
            Channel channel = ChannelManager.getChannelByUserId(userUuid);
//            如果已经有连接了就顶下线
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
            redisTemplate.opsForValue().getAndDelete(UserConstants.USER_SESSION + userUuid);
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("捕获到异常：", cause);

        try {
            offline(ctx);
        }catch (Exception e){
            log.error("关闭管道失败", e);
        }
    }

}
