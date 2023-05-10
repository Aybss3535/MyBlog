package com.aybss.controller;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.RoleDto;
import com.aybss.domain.dto.RoleMenuDto;
import com.aybss.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getAllRoles(Integer pageNum, Integer pageSize, RoleDto roleDto){
        return roleService.getAllRoles(pageNum, pageSize, roleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id") Integer id){
        return roleService.getRoleById(id);
    }

    @PutMapping()
    public ResponseResult updateRole(@RequestBody RoleMenuDto roleMenuDto){
        return roleService.updateRole(roleMenuDto);
    }

    @PostMapping()
    public ResponseResult addRole(@RequestBody RoleMenuDto roleMenuDto){
        return roleService.addRole(roleMenuDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") List<Long> ids){
        return roleService.deleteRole(ids);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
