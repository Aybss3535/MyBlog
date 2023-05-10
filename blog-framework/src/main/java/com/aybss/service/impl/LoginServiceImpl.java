package com.aybss.service.impl;

import com.aybss.domain.LoginUser;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.User;
import com.aybss.domain.vo.UserInfoVo;
import com.aybss.domain.vo.UserLoginVo;
import com.aybss.service.LoginService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.JwtUtil;
import com.aybss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //调用AuthenticationManager的方法验证用户名和密码
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码不正确");
        }
        //用户名和密码正确，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);
        //将用户数据存入Redis
        redisCache.setCacheObject("loginUser:"+userId,loginUser);
        //封装数据，返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        UserLoginVo userLoginVo = new UserLoginVo(token,userInfoVo);
        return ResponseResult.okResult(userLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //从redis中删除userId对应数据
        redisCache.deleteObject("loginUser:"+userId);
        return ResponseResult.okResult();
    }
}
