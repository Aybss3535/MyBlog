package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
