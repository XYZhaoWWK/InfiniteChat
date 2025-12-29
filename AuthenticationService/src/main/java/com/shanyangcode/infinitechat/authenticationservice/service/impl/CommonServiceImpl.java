package com.shanyangcode.infinitechat.authenticationservice.service.impl;

import com.shanyangcode.infinitechat.authenticationservice.constants.config.OSSConstant;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlResponse;
import com.shanyangcode.infinitechat.authenticationservice.service.CommonService;
import com.shanyangcode.infinitechat.authenticationservice.utils.OSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OSSUtils ossUtils;

    @Override
    public UploadUrlResponse uploadUrl(UploadUrlRequest uploadUrlRequest) throws Exception {
        String fileName = uploadUrlRequest.getFileName();
        String uploadUrl = ossUtils.uploadUrl(OSSConstant.BUCKET_NAME, fileName, OSSConstant.PICTURE_EXPIRE_TIME);
        String downUrl = ossUtils.downUrl(OSSConstant.BUCKET_NAME, fileName);
        UploadUrlResponse uploadUrlResponse = new UploadUrlResponse();
        uploadUrlResponse.setDownloadUrl(downUrl)
                .setUploadUrl(uploadUrl);
        return uploadUrlResponse;
    }
}
