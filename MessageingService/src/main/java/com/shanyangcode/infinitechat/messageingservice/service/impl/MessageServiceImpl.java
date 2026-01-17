package com.shanyangcode.infinitechat.messageingservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.messageingservice.common.ServiceException;
import com.shanyangcode.infinitechat.messageingservice.constants.SessionType;
import com.shanyangcode.infinitechat.messageingservice.data.sendMsg.SendMsgRequest;
import com.shanyangcode.infinitechat.messageingservice.data.sendMsg.SendMsgResponse;
import com.shanyangcode.infinitechat.messageingservice.mapper.FriendMapper;
import com.shanyangcode.infinitechat.messageingservice.mapper.MessageMapper;
import com.shanyangcode.infinitechat.messageingservice.model.Friend;
import com.shanyangcode.infinitechat.messageingservice.model.Message;
import com.shanyangcode.infinitechat.messageingservice.model.User;
import com.shanyangcode.infinitechat.messageingservice.service.MessageService;
import com.shanyangcode.infinitechat.messageingservice.service.UserService;
import com.shanyangcode.infinitechat.messageingservice.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private static final int STATUS_ACTIVE = 1;

    private final UserService userService;

    private final FriendMapper friendMapper;

    private final DiscoveryClient discoveryClient;

    private final UserSessionService userSessionService;

    public MessageServiceImpl(UserService userService,
                              FriendMapper friendMapper,
                              DiscoveryClient discoveryClient, UserSessionService userSessionService){
        this.userService = userService;
        this.friendMapper = friendMapper;
        this.discoveryClient = discoveryClient;
        this.userSessionService = userSessionService;
    }

    @Override
    public SendMsgResponse sendMessage(SendMsgRequest request) {
//        校验好友关系

//        判断是单聊还是群聊，群聊获取群成员名单
        List<Long> receiveUserIds = getReceiveUserIds(request);
//        构建消息

//        根据redis查询接受者netty服务在哪

//        发送消息

        return null;
    }

    private void validateSender(Long sendUserId) {
        User senderUser = userService.getById(sendUserId);
        log.info("发送者状态: {}", sendUserId);
        if (senderUser == null || senderUser.getStatus() != STATUS_ACTIVE) {
            throw new ServiceException("发送者状态异常");
        }
    }

    private List<Long> getReceiveUserIds(SendMsgRequest sendMsgRequest) {
        List<Long> receiveUserIds = new ArrayList<>();
        int sessionType = sendMsgRequest.getSessionType();

        if (sessionType == SessionType.SINGLE.getValue()) {
            Long receiveUserId = sendMsgRequest.getReceiveUserId();
            receiveUserIds.add(receiveUserId);
            validateSingleSession(sendMsgRequest.getSendUserId(), receiveUserId);
        } else {
            receiveUserIds.addAll(userSessionService.getUserIdsBySessionId(sendMsgRequest.getSessionId()));
            log.info("群聊接收者列表: {}", receiveUserIds);
            boolean removed = receiveUserIds.remove(sendMsgRequest.getSendUserId());
            if (removed) {
                log.info("移除发送者后的接收者列表: {}", receiveUserIds);
            } else {
                throw new ServiceException("发送者不在群聊内");
            }
        }

        return receiveUserIds;
    }

    private void validateSingleSession(Long sendUserId, Long receiveUserId) {
        User receiverUser = userService.getById(receiveUserId);
        if (receiverUser == null || receiverUser.getStatus() != STATUS_ACTIVE) {
            throw new ServiceException("接收者 " + receiveUserId + " 状态异常");
        }

        Friend friend = friendMapper.selectFriendship(sendUserId, receiveUserId);
        log.info("发送者ID: {}, 接收者ID: {}", sendUserId, receiveUserId);
        if (friend == null || friend.getStatus() != STATUS_ACTIVE) {
            throw new ServiceException("发送者 " + sendUserId + " 与接收者 " + receiveUserId + " 不是好友关系");
        }
    }
}
