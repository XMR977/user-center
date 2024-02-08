package com.xm.usercenter.service;

import com.xm.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("pandaxm");
        user.setUserAccount("xm");
        user.setAvatarUrl("www.com");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123456");
        user.setEmail("@gmail");


        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void testRegister(){
        String ac = "xmr888";
        String password = "12345678";
        String sp = "12345678";
        String xc = "12345";
        Long l = userService.userRegister(ac, password, sp,xc);

        Assertions.assertTrue(l>0);

    }

}