package com.xm.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xm.usercenter.common.BaseResponse;
import com.xm.usercenter.common.ErrorCode;
import com.xm.usercenter.common.ResultUtils;
import com.xm.usercenter.exception.BusinessException;
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
import java.util.stream.Collectors;

import static com.xm.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.xm.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest request){
        if(request == null){
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();
        String pandaCode = request.getPandaCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,pandaCode)){
            return ResultUtils.error(ErrorCode.PARAMS_NULL);
        }
        Long id = userService.userRegister(userAccount, userPassword, checkPassword, pandaCode);

//        return new BaseResponse<>(0,id,"Ok");
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);

        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest httpServletRequest){
        if(httpServletRequest == null){
            return null;
        }

        Integer user = userService.userLogout(httpServletRequest);

        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest httpServletRequest){
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        Long userId = currentUser.getId();
        //todo valid user status
        User byId = userService.getById(userId);
        User handleUser = userService.handleUser(byId);
        return ResultUtils.success(handleUser);
    }
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser (String username, HttpServletRequest httpServletRequest){
        if(!isAdmin(httpServletRequest)){
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        List<User> userlist = userService.list(queryWrapper);
        List<User> list = userlist.stream().map(user -> userService.handleUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @DeleteMapping
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest httpServletRequest){
        if(!isAdmin(httpServletRequest)){
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }
        if(id <=0){
            return ResultUtils.error(ErrorCode.NO_LOGIN);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
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
