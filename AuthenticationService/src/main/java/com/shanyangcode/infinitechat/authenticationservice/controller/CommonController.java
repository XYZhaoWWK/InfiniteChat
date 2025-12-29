package com.shanyangcode.infinitechat.authenticationservice.controller;


import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlResponse;
import com.shanyangcode.infinitechat.authenticationservice.service.CommonService;
import com.shanyangcode.infinitechat.authenticationservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("api/v1/user/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/sms")
    public Result<String> mail(@RequestParam("targetEmail") String targetEmail){
        emailService.sendCode(targetEmail);
        return Result.OK(targetEmail);
    }

    @GetMapping("/uploadUrl")
    public Result<UploadUrlResponse> getUploadUrl(@Valid UploadUrlRequest uploadUrlRequest) throws Exception {
        UploadUrlResponse response = commonService.uploadUrl(uploadUrlRequest);
        return Result.OK(response);
    }

}
