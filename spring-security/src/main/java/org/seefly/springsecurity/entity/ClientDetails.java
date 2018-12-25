package org.seefly.springsecurity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@ToString
public class ClientDetails {

  /** null **/
  private Integer id;
  /** null **/
  private String clientId;
  /** null **/
  private String resourceIds;
  /** null **/
  private String clientSecret;
  /** null **/
  private Set<String> scope;
  /** null **/
  private Collection<GrantedAuthority> authorities = Collections.emptyList();
  /** null **/
  private Set<String> authorizedGrantTypes;
  /** null **/
  private Set<String> webServerRedirectUri;
  /** null **/
  private Integer accessTokenValidity;
  /** null **/
  private Integer refreshTokenValidity;
  /** null **/
  private String additionalInformation;
  /** null **/
  private String autoapprove;

  public void setScope(String scope) {
    this.scope = StringUtils.commaDelimitedListToSet(scope);
  }

  public void setAuthorities(String authorities) {
    if(!StringUtils.isEmpty(authorities)){
      this.authorities = new ArrayList<>();
      String[] strings = StringUtils.commaDelimitedListToStringArray(authorities);
      for (String auth : strings) {
        this.authorities.add(new SimpleGrantedAuthority(auth));
      }
    }
  }

  public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
    this.authorizedGrantTypes = StringUtils.commaDelimitedListToSet(authorizedGrantTypes);
  }

  public void setWebServerRedirectUri(String webServerRedirectUri){
    this.webServerRedirectUri = StringUtils.commaDelimitedListToSet(webServerRedirectUri);
  }
}
