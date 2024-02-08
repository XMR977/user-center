package com.xm.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author xm
 */
@Data
public class UserLoginRequest implements Serializable {

   private String       userAccount;
   private String      userPassword;

}
