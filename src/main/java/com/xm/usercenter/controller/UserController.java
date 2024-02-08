package com.xm.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xm.usercenter.model.domain.User;
import com.xm.usercenter.model.domain.request.UserLoginRequest;
import com.xm.usercenter.model.domain.request.UserRegisterRequest;
import com.xm.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.xm.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.xm.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest request){
        if(request == null){
            return null;
        }
        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();
        String pandaCode = request.getPandaCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,pandaCode)){
            return null;
        }
        Long id = userService.userRegister(userAccount, userPassword, checkPassword, pandaCode);

        return id;
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);

        return user;
    }

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest httpServletRequest){
        if(httpServletRequest == null){
            return null;
        }

        Integer user = userService.userLogout(httpServletRequest);

        return user;
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest httpServletRequest){
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        Long userId = currentUser.getId();
        //todo valid user status
        User byId = userService.getById(userId);
        return userService.handleUser(byId);
    }
    @GetMapping("/search")
    public List<User> searchUser (String username, HttpServletRequest httpServletRequest){
        if(!isAdmin(httpServletRequest)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        return userService.list(queryWrapper);
    }

    @DeleteMapping
    public boolean deleteUser(@RequestBody Long id, HttpServletRequest httpServletRequest){
        if(!isAdmin(httpServletRequest)){
            return false;
        }
        if(id <=0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * is admin
     * @param httpServletRequest
     * @return
     */

    private boolean isAdmin(HttpServletRequest httpServletRequest){
        //verify role
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user.getRole() != ADMIN_ROLE || user ==null){
            return false;
        }
        return true;
    }

}
