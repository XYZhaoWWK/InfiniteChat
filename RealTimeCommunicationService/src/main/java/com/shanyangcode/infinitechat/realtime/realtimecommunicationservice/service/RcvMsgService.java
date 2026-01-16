package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.service;

import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.data.ReceiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.data.ReceiveMessage.ReceiveMessageResponse;

public interface RcvMsgService {
    ReceiveMessageResponse receiveMessage(ReceiveMessageRequest request);
}