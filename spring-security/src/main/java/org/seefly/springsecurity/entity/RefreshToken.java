package org.seefly.springsecurity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefreshToken {

  /** null **/
  private String tokenId;
  /** null **/
  private String token;
  /** null **/
  private String authentication;

}
