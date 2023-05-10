package com.aybss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.RoleMenuMapper;
import com.aybss.domain.entity.RoleMenu;
import com.aybss.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-20 15:04:13
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

