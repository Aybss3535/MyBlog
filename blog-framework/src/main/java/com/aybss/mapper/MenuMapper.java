package com.aybss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aybss.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-19 10:48:21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getPermsByUserId(Long userId);

    List<Menu> getMenusByUserId(Long userId);
}

