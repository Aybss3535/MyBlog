package com.aybss.controller;

import com.aybss.domain.LoginUser;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.Menu;
import com.aybss.domain.entity.User;
import com.aybss.domain.vo.AdminUserInfoVo;
import com.aybss.domain.vo.RoutersVo;
import com.aybss.domain.vo.UserInfoVo;
import com.aybss.service.AdminLoginService;
import com.aybss.service.MenuService;
import com.aybss.service.RoleService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return adminLoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取UserId
        Long userId = SecurityUtils.getUserId();
        //根据UserId查询permissions
        List<String> permissions = menuService.getPermById(userId);
        //根据UserId查询roles
        List<String> roles = roleService.getRolesByUserId(userId);
        //查询用户信息
        User user = SecurityUtils.getLoginUser().getUser();
        //封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        return menuService.getRouters();
    }

}
