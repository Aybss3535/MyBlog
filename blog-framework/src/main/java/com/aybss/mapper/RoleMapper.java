package com.aybss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aybss.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-19 10:50:30
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getRolesByUserId(Long userId);
}

