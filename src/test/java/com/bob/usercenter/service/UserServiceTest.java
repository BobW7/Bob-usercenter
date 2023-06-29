package com.bob.usercenter.service;
import java.util.Date;

import com.bob.usercenter.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
* 用户服务测试
*
* @author SuperBob
*/
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;
    @Test
    public void testAddUser(){
        User user = new User();
        user.setUserAccount("Bos0");
        user.setUsername("Jayson Tatum");
        user.setAvatarUrl("D:\\IDEAProjectSource\\Bob-usercenter\\src\\main\\java\\images\\Jayson Tatum.jpeg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123456");
        user.setEmail("JaysonTatum@Celtics.com");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "Jayson";
        String userPassword = "";
        String checkPassword  ="123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验非空

        userAccount = "JT";
         result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验用户名不小于四位

        userAccount = "Jayson";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验密码不小于八位

        userAccount = "Jayson Tatum";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验账户不包含特殊字符

         checkPassword  ="123456jiojioj";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验密码和验证密码一致

        userAccount = "JaysonTatum";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验账户不能重复

        userAccount = "JaylenBrown";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //校验当所有条件都满足是否可以注册一位新用户


    }

}