package com.aybss.service.impl;

import com.aybss.domain.LoginUser;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.User;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.service.AdminLoginService;
import com.aybss.utils.JwtUtil;
import com.aybss.utils.RedisCache;
import com.aybss.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;


    @Override
    public ResponseResult login(User user) {
        //用户名判空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //校验
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){ //登录失败
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //登录成功，取出用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //创建token
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);
        //把用户信息放到Redis中
        redisCache.setCacheObject("adminLoginUser:"+userId,loginUser);
        //封装结果，返回
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("adminLoginUser:"+userId);
        return ResponseResult.okResult();
    }
}
