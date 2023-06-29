package com.bob.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author SuperBob
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final  long serialVersionUID = 2218546962356058650L;

    private String  userAccount;
    private String userPassword;
    private String checkPassword;
}
