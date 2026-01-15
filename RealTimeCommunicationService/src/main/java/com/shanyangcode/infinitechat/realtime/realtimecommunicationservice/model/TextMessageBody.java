package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextMessageBody {

    private String content;

    private String replyId;
}
