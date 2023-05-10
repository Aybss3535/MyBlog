package com.aybss.controller;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.MenuDto;
import com.aybss.domain.entity.Menu;
import com.aybss.service.MenuService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @GetMapping("/list")
    public ResponseResult getAllMenus(MenuDto menuDto){
        return menuService.getAllMenus(menuDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id") Long id){
        return menuService.getMenuById(id);
    }

    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable("id") Long id){
        return menuService.deleteMenu(id);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getRoleMenuTreeById(@PathVariable("id") Long roleId){
        return menuService.getRoleMenuTreeById(roleId);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }

}
