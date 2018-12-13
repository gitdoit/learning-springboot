package org.seefly.springsecurity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccessToken  {

  /** null **/
  private String tokenId;
  /** null **/
  private String token;
  /** null **/
  private String authenticationId;
  /** null **/
  private String userName;
  /** null **/
  private String clientId;
  /** null **/
  private String authentication;
  /** null **/
  private String refreshToken;

}
