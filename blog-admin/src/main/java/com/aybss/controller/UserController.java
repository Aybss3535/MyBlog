package com.aybss.controller;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.ChangeStatusDto;
import com.aybss.domain.dto.UserDto;
import com.aybss.domain.entity.User;
import com.aybss.domain.entity.UserRole;
import com.aybss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getAllUsers(Integer pageNum, Integer pageSize, UserDto userDto) {
        return userService.getAllUsers(pageNum, pageSize, userDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping()
    public ResponseResult updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") List<Long> ids){
        return userService.deleteUser(ids);
    }

    @PostMapping()
    public ResponseResult addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeStatusDto changeStatusDto){
        return userService.changeStatus(changeStatusDto);
    }
}
