package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.controller;

import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.common.Result;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.data.ReceiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.data.ReceiveMessage.ReceiveMessageResponse;
import com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.service.RcvMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/message")
@Slf4j
@RequiredArgsConstructor
public class RcvMsgController {
    @Autowired
    private RcvMsgService rcvMsgService;

    @PostMapping("/user")
    public Result<ReceiveMessageResponse> receiveMessage(@RequestBody ReceiveMessageRequest request){
        ReceiveMessageResponse response = rcvMsgService.receiveMessage(request);

        return Result.OK(response);
    }
}