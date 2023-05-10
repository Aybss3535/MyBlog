package com.aybss.service.impl;

import com.aybss.service.MenuService;
import com.aybss.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * 判断用户是否具有permission的权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        //如果当前登录用户是管理员，具有所有权限
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则，判断该用户是否具有该权限
        List<String> perms = SecurityUtils.getLoginUser().getPermissions();
        return perms.contains(permission);
    }

}
