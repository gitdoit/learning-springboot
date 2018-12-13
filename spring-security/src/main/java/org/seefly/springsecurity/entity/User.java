package org.seefly.springsecurity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

  /** null **/
  private String username;
  /** null **/
  private String password;
  /** null **/
  private String email;
  /** null **/
  private Integer activated;
  /** null **/
  private String activationkey;
  /** null **/
  private String resetpasswordkey;

}
