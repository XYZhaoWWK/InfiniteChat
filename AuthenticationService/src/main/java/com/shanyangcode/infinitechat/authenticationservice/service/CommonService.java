package com.shanyangcode.infinitechat.authenticationservice.service;

import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlResponse;

public interface CommonService {

    UploadUrlResponse uploadUrl(UploadUrlRequest uploadUrlRequest) throws Exception;

}
