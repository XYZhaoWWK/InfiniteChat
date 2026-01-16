package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.service.impl;

import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.data.ReceiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.data.ReceiveMessage.ReceiveMessageResponse;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.service.RcvMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@Slf4j
public class RcvMsgServiceImpl implements RcvMsgService {

    @Autowired
    private NettyMessageService nettyMessageService;
    @Override
    public ReceiveMessageResponse receiveMessage(@Valid ReceiveMessageRequest request) {
        nettyMessageService.sendMessageToUser(request);

        return new ReceiveMessageResponse();
    }
}