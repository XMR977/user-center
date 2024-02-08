package com.xm.usercenter.service;

import com.xm.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2024-02-04 20:49:00
*/
public interface UserService extends IService<User> {
    /**
     *
     * @param
     *
     * @return return ID
     */

    Long userRegister(String userAccount, String userPassword, String checkPassword, String pandaCode);


    /**
     *
     * @param userAccount user account
     * @param userPassword user password
     * @return user info
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * handle user' info
     * @param originalUser
     * @return
     */
    User handleUser(User originalUser);

    /**
     * remove login status
     * @param httpServletRequest
     */
    Integer userLogout(HttpServletRequest httpServletRequest);
}
