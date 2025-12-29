package com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadUrlResponse {

    private String uploadUrl;
    private String downloadUrl;

}
