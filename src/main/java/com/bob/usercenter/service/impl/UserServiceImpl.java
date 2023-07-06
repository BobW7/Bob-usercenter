package com.bob.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.usercenter.common.ErrorCode;
import com.bob.usercenter.exception.BusinessException;
import com.bob.usercenter.service.UserService;
import com.bob.usercenter.model.User;
import com.bob.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bob.usercenter.constant.UserConstant.UESR_LOGIN_STATE;

/**
* @author SuperBob
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-06-08 21:19:43
*/
@Service
@Slf4j

public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "Bob";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"存在空参数");
        }
        if(userAccount.length()<4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户长度小于四位");
        }

        if(userPassword.length()<8||checkPassword.length()<8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于八位");
        }

        // 校验账户不能包含特殊字符
        String validPatter = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);

        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户中包含特殊字符");
        }

        //  账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);

        if(!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据库保存错误");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"账号或密码为空");
        }
        if(userAccount.length()<4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号过短");
        if(userPassword.length()<8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");
        // 校验账户不能包含特殊字符
        String validPatter = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户中包含特殊字符");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //  查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //  用户不存在
        if(user==null){
            log.info("user Log in failed,userAccount can't match userPassword");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户不存在");
        }
        //  3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //  4.记录用户的登录态
        request.getSession().setAttribute(UESR_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     *
     *用户脱敏
     * @param originuser
     * @return
     */
    @Override
    public User getSafetyUser(User originuser){
        if(originuser ==null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originuser.getId());
        safetyUser.setUserAccount(originuser.getUserAccount());
        safetyUser.setUsername(originuser.getUsername());
        safetyUser.setAvatarUrl(originuser.getAvatarUrl());
        safetyUser.setGender(originuser.getGender());
        safetyUser.setPhone(originuser.getPhone());
        safetyUser.setEmail(originuser.getEmail());
        safetyUser.setUserRole(originuser.getUserRole());
        safetyUser.setUserStatus(originuser.getUserStatus());
        safetyUser.setCreateTime(originuser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(UESR_LOGIN_STATE);
        return 1;
    }
}




