package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
