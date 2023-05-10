package com.aybss.service.impl;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.LoginUser;
import com.aybss.domain.entity.Menu;
import com.aybss.domain.entity.User;
import com.aybss.mapper.UserMapper;
import com.aybss.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<com.aybss.domain.entity.User> wrapper = new LambdaQueryWrapper();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码不正确");
        }
        //如果是后台用户，还需要封装权限信息
        if(user.getType().equals(SystemConstants.USER_TYPE_ADMIN)){
            List<String> perms = menuService.getPermById(user.getId());
            return new LoginUser(user,perms);
        }
        return new LoginUser(user,null);
    }
}
