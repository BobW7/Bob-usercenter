package com.bob.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final  long serialVersionUID = 3103639152933415400L;

    private String  userAccount;
    private String userPassword;
}
