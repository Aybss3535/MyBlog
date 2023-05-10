package com.aybss.service.impl;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.ChangeStatusDto;
import com.aybss.domain.dto.UserDto;
import com.aybss.domain.entity.Role;
import com.aybss.domain.entity.UserRole;
import com.aybss.domain.vo.PageVo;
import com.aybss.domain.vo.UserInfoVo;
import com.aybss.domain.vo.UserListVo;
import com.aybss.domain.vo.UserRoleVo;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.mapper.UserRoleMapper;
import com.aybss.service.RoleService;
import com.aybss.service.UserRoleService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.UserMapper;
import com.aybss.domain.entity.User;
import com.aybss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-12-15 11:11:36
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.eq(User::getId,userId);
        User user = userMapper.selectOne(wrapper);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        userMapper.updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        //对用户名、昵称和邮箱进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_NICKNAME);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_EMAIL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_PASSWORD);
        }
        //对用户名和邮箱进行唯一性判断
        if(fieldExist("user_name",user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(fieldExist("email",user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        //存入数据库中
        save(user);
        return null;
    }

    @Override
    public ResponseResult getAllUsers(Integer pageNum, Integer pageSize, UserDto userDto) {
        //分页查询
        Page<User> page = new Page<>(pageNum,pageSize);
        //按照查询条件模糊查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.like(StringUtils.hasText(userDto.getUserName()),User::getUserName,userDto.getUserName());
        wrapper.like(StringUtils.hasText(userDto.getPhonenumber()),User::getPhonenumber,userDto.getPhonenumber());
        wrapper.eq(StringUtils.hasText(userDto.getStatus()),User::getStatus,userDto.getStatus());
        //封装结果，返回
        page(page,wrapper);
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos,page.getTotal()));
    }

    @Override
    public ResponseResult getUserById(Long id) {
        //所有角色的列表
        List<Role> roles = roleService.list();
        //获取用户所关联的角色id列表
        List<Long> ids = null;
        if(id.equals(1L)){
            //如果是管理员的话，返回所有角色的Id
            ids = roles.stream().map(o -> o.getId()).collect(Collectors.toList());
        }else{
            //查询该用户具有的角色
            LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserRole::getUserId,id);
            List<UserRole> userRoles = userRoleService.list(wrapper);
            ids = userRoles.stream().map(o->o.getRoleId()).collect(Collectors.toList());
        }
        //用户信息
        User user = getById(id);
        UserListVo userListVo = BeanCopyUtils.copyBean(user, UserListVo.class);
        return ResponseResult.okResult(new UserRoleVo(ids,roles,userListVo));
    }

    @Override
    @Transactional
    public ResponseResult updateUser(User user) {
        //保存用户信息
        updateById(user);
        //保存用户关联的角色信息
        //先把用户关联的所有角色删除
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(wrapper);
        //再保存新的角色新
        List<Long> roleIds = user.getRoleIds();
        List<UserRole> userRoles = roleIds.stream().map(o -> new UserRole(user.getId(), o)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(List<Long> ids) {
        //根据id删除用户信息
        removeByIds(ids);
        //删除用户关联的角色信息
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRole::getUserId,ids);
        userRoleService.remove(wrapper);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult addUser(User user) {
        //对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //保存用户信息
        save(user);
        //保存用户关联的角色信息
        List<Long> roleIds = user.getRoleIds();
        List<UserRole> userRoles = roleIds.stream().map(o -> new UserRole(user.getId(), o)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(ChangeStatusDto changeStatusDto) {
        User user = new User();
        user.setStatus(changeStatusDto.getStatus());
        user.setId(changeStatusDto.getUserId());
        updateById(user);
        return ResponseResult.okResult();
    }


    private boolean fieldExist(String column,String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(column,userName);
        int i = count(wrapper);
        return i!=0;
    }
}

