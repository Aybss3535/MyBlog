package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.MenuDto;
import com.aybss.domain.vo.RoutersVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-12-19 10:48:21
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermById(Long id);

    ResponseResult<RoutersVo> getRouters();

    ResponseResult getAllMenus(MenuDto menuDto);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult addMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    ResponseResult getRoleMenuTreeById(Long roleId);

    ResponseResult treeselect();
}

