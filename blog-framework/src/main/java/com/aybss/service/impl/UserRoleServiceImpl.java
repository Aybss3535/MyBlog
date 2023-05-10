package com.aybss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.UserRoleMapper;
import com.aybss.domain.entity.UserRole;
import com.aybss.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-12-20 16:38:55
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

