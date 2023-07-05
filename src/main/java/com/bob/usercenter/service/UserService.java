package com.bob.usercenter.service;

import com.bob.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author SuperBob
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-06-08 21:19:43
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验码
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     *用户登录
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originuser
     * @return
     */
    User getSafetyUser(User originuser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
