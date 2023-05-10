package com.aybss.service.impl;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.MenuDto;
import com.aybss.domain.entity.RoleMenu;
import com.aybss.domain.vo.MenuTreeVo;
import com.aybss.domain.vo.RoleMenuTreeVo;
import com.aybss.domain.vo.RoutersVo;
import com.aybss.service.RoleMenuService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.MenuMapper;
import com.aybss.domain.entity.Menu;
import com.aybss.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-19 10:48:21
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> getPermById(Long id) {
        //如果是管理员的话，返回所有菜单类型为C或者F的
        if(id == 1){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //查询所有的目录和按钮
            wrapper.in(Menu::getMenuType, SystemConstants.MENU_TYPE_CATEGORY,SystemConstants.MENU_TYPE_BUTTON);
            //未被禁用的
            wrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        //如果不是管理员
        List<String> perms = menuMapper.getPermsByUserId(id);
        return perms;
    }

    @Override
    public ResponseResult<RoutersVo> getRouters() {
        //获取userId
        Long userId = SecurityUtils.getUserId();
        //如果用户是admin
        List<Menu> menus = null;
        if(SecurityUtils.isAdmin()){
            //返回所有的menu
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper();
            wrapper.in(Menu::getMenuType,SystemConstants.MENU_TYPE_CATEGORY,SystemConstants.MENU_TYPE_MENU);
            wrapper.eq(Menu::getStatus,SystemConstants.MENU_STATUS_NORMAL);
            wrapper.orderByAsc(Menu::getOrderNum);
            menus = list(wrapper);
        }else{
            //查询对应用户所具有的menu
            menus = menuMapper.getMenusByUserId(userId);
        }
        //对得到的menu生成树结构
        menus = buildMenuTree(menus, 0L);
        //封装结果返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @Override
    public ResponseResult getAllMenus(MenuDto menuDto) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(menuDto.getStatus()),Menu::getStatus,menuDto.getStatus());
        wrapper.like(StringUtils.hasText(menuDto.getMenuName()),Menu::getMenuName,menuDto.getMenuName());
        List<Menu> menus = list(wrapper);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        return ResponseResult.okResult(getById(id));
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        //先找出索引的menu
        List<Menu> menus = list();
        //把id及id下的menu都删除
        List<Long> ids = getTreeIds(menus,id);
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleMenuTreeById(Long roleId) {
        List<MenuTreeVo> menuTreeVos = getAllMenuTree();
        List<Long> checkedKeys = null;
        if(roleId.equals(1L)){
            //如果是管理员用户，返回所有的menus的Id
            checkedKeys = menuTreeVos.stream().map(o -> o.getId()).collect(Collectors.toList());
        }else{
            //否则，查询该用户关联的menus的Id
            LambdaQueryWrapper<RoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
            roleMenuWrapper.eq(RoleMenu::getRoleId,roleId);
            List<RoleMenu> roleMenus = roleMenuService.list(roleMenuWrapper);
            checkedKeys = roleMenus.stream().map(o->o.getMenuId()).collect(Collectors.toList());
        }
        return ResponseResult.okResult(new RoleMenuTreeVo(menuTreeVos,checkedKeys));
    }

    /**
     * 获取所有的menus，以树形状结构显示
     * @return
     */
    private List<MenuTreeVo> getAllMenuTree(){
        //查询所有的menus
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus,SystemConstants.MENU_STATUS_NORMAL);
        List<Menu> menus = list(wrapper);
        List<MenuTreeVo> menuTreeVos = menus.stream().map(menu -> BeanCopyUtils.copyBean(menu, MenuTreeVo.class).setLabel(menu.getMenuName())).collect(Collectors.toList());
        //构建成menuTree
        menuTreeVos = buildMenuVoTree(menuTreeVos, 0L);
        return menuTreeVos;
    }

    @Override
    public ResponseResult treeselect() {
        return ResponseResult.okResult(getAllMenuTree());
    }

    /**
     * 获取id及id下所有menus的id
     * @param menus
     * @param id
     * @return
     */
    private List<Long> getTreeIds(List<Menu> menus, Long id){
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        for(Menu menu:menus){
            if(menu.getParentId().equals(id)){
                ids.addAll(getTreeIds(menus,menu.getId()));
            }
        }
        return ids;
    }

    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId){
        return menus.stream().filter(o->o.getParentId().equals(parentId))
                .map(o->o.setChildren(buildMenuTree(menus,o.getId())))
                .collect(Collectors.toList());
    }

    private List<MenuTreeVo> buildMenuVoTree(List<MenuTreeVo> menus, Long parentId){
        return menus.stream().filter(o->o.getParentId().equals(parentId))
                .map(o->o.setChildren(buildMenuVoTree(menus,o.getId())))
                .collect(Collectors.toList());
    }
}

