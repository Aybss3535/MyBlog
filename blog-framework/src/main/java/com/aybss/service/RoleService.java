package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.RoleDto;
import com.aybss.domain.dto.RoleMenuDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-12-19 10:50:30
 */
public interface RoleService extends IService<Role> {

    List<String> getRolesByUserId(Long userId);

    ResponseResult getAllRoles(Integer pageNum, Integer pageSize, RoleDto roleDto);

    ResponseResult getRoleById(Integer id);

    ResponseResult updateRole(RoleMenuDto roleMenuDto);

    ResponseResult addRole(RoleMenuDto roleMenuDto);

    ResponseResult deleteRole(List<Long> ids);

    ResponseResult listAllRole();

}

