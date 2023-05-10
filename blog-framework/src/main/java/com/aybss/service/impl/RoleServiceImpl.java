package com.aybss.service.impl;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.RoleDto;
import com.aybss.domain.dto.RoleMenuDto;
import com.aybss.domain.entity.RoleMenu;
import com.aybss.domain.vo.PageVo;
import com.aybss.domain.vo.RoleVo;
import com.aybss.service.RoleMenuService;
import com.aybss.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.RoleMapper;
import com.aybss.domain.entity.Role;
import com.aybss.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-12-19 10:50:30
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> getRolesByUserId(Long userId) {
        //如果是管理员，角色信息中有"admin"
        if(userId == 1){
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            return roles;
        }
        //否则，从数据库中查询对应用户的角色
        List<String> roles = getBaseMapper().getRolesByUserId(userId);
        return roles;
    }

    @Override
    public ResponseResult getAllRoles(Integer pageNum, Integer pageSize, RoleDto roleDto) {
        Page<Role> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleDto.getRoleName()),Role::getRoleName,roleDto.getRoleName());
        wrapper.eq(StringUtils.hasText(roleDto.getStatus()),Role::getStatus,roleDto.getStatus());
        page(page,wrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos,page.getTotal()));
    }

    @Override
    public ResponseResult getRoleById(Integer id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(RoleMenuDto roleMenuDto) {
        //修改角色属性
        Role role = BeanCopyUtils.copyBean(roleMenuDto, Role.class);
        updateById(role);
        //修改角色关联的menus
        //先删除这个roleId的所有menus
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,roleMenuDto.getId());
        roleMenuService.remove(wrapper);
        //再新增menus
        List<Long> menuIds = roleMenuDto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds.stream().map(o -> new RoleMenu(roleMenuDto.getId(), o)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleMenuDto roleMenuDto) {
        //添加角色信息
        Role role = BeanCopyUtils.copyBean(roleMenuDto, Role.class);
        save(role);
        //添加角色的menus
        List<Long> menuIds = roleMenuDto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds.stream().map(o -> new RoleMenu(role.getId(), o)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        //返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(List<Long> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> roles = list();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        return ResponseResult.okResult(roleVos);
    }
}

