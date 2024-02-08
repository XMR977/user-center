package com.xm.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xm.usercenter.model.domain.User;
import com.xm.usercenter.mapper.UserMapper;
import com.xm.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xm.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-02-04 20:49:00
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final  String SALT = "xm";


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword, String pandaCode) {
        //1.validate
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,pandaCode)){
            //todo throw exception
            return -1L;
        }
        if(userAccount.length()<4){
            return -1L;
        }
        if(userPassword.length()<8){
            return -1L;
        }
        if(pandaCode.length()>5){
            return -1L;
        }


        //account can not contain special symbols
        String regex =  "^[a-zA-Z0-9]+$";
//        //compile the regular expression
//        Pattern pattern = Pattern.compile(regex);
//        //create a matcher object
//        Matcher matcher = pattern.matcher(userAccount);
//        //if account does not match regular expression return false;

        //method chaining
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);

        if(!matcher.matches()){
            return -1L;
        }

        if(!userPassword.equals(checkPassword)){
            return -1L;
        }

        //user account can not be existed
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            return -1L;
        }

        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("pandaCode", pandaCode);
        long count2 = userMapper.selectCount(queryWrapper2);
        if(count2>0){
            return -1L;
        }

        //2. encode

        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.insert data
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodePassword);
        user.setPandaCode(pandaCode);
        int save = userMapper.insert(user);
        if(save == 0){
            return -1L;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        //1.validate
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length()<4){
            return null;
        }
        if(userPassword.length()<8){
            return null;
        }


        //account can not contain special symbols
        String regex =  "^[a-zA-Z0-9]+$";
//        //compile the regular expression
//        Pattern pattern = Pattern.compile(regex);
//        //create a matcher object
//        Matcher matcher = pattern.matcher(userAccount);
//        //if account does not match regular expression return false;

        //method chaining
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);

        if(!matcher.matches()){
            return null;
        }

        //2. encode
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //Is user exist?
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed");
            return null;
        }

        //3.handled user info
        User handleUser = handleUser(user);

        //4.record user login status
        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE, handleUser);

        //return handled user info
        return handleUser;
    }

    public User handleUser(User originalUser){
        if(originalUser ==null){
            return null;
        }
        User handleUser = new User();
        handleUser.setId(originalUser.getId());
        handleUser.setPandaCode(originalUser.getPandaCode());
        handleUser.setUsername(originalUser.getUsername());
        handleUser.setUserAccount(originalUser.getUserAccount());
        handleUser.setAvatarUrl(originalUser.getAvatarUrl());
        handleUser.setGender(originalUser.getGender());
        handleUser.setRole(originalUser.getRole());
        handleUser.setPhone(originalUser.getPhone());
        handleUser.setEmail(originalUser.getEmail());
        handleUser.setUserStatus(originalUser.getUserStatus());
        handleUser.setCreateTime(originalUser.getCreateTime());
        return handleUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




