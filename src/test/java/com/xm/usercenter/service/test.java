package com.xm.usercenter.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class test {

    @Test
    void  vatest() {
        String userAccount = "Abcdefg ";
        String regex = "^[a-zA-Z0-9]+$";
//        //compile the regular expression
//        Pattern pattern = Pattern.compile(regex);
//        //create a matcher object
//        Matcher matcher = pattern.matcher(userAccount);
//        //if account does not match regular expression return false;

        //method chaining
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);

        if(matcher.matches())

        {
            System.out.println("valid");
        }else

        {
            System.out.println("invalid");
        }
    }


}
