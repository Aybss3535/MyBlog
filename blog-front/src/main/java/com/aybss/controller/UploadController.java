package com.aybss.controller;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.ResponseResult;
import com.aybss.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadAvatar(MultipartFile img){
        return uploadService.uploadImg(img, SystemConstants.IMAGE_DIRECTORY_AVATAR);
    }

}
