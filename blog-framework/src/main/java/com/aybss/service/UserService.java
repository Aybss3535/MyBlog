package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.ChangeStatusDto;
import com.aybss.domain.dto.UserDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.User;

import java.util.List;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-12-15 11:11:36
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getAllUsers(Integer pageNum, Integer pageSize, UserDto userDto);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(User user);

    ResponseResult deleteUser(List<Long> ids);

    ResponseResult addUser(User user);

    ResponseResult changeStatus(ChangeStatusDto changeStatusDto);
}

