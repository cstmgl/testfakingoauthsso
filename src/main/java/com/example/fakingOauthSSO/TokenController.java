/*
 * Copyright Avaloq Licence AG. All rights reserved.
 * Avaloq Licence AG
 * Schwerzistrasse 6 | CH-8807 Freienbach | Switzerland
 *
 * This software is the confidential and proprietary information of Avaloq Evolution AG.
 * You shall not disclose whole or parts of it and shall use it only in accordance with the terms of the
 * license agreement you entered into with Avaloq Evolution AG.
 */

package com.example.fakingOauthSSO;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

  @Autowired
  OAuth2RestTemplate oauthRestTemplate;

  @RequestMapping(path = "/api/token", method = RequestMethod.GET)
  public AuthenticationInfo getAuthenticationInfo() {
    OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
        .getContext()
        .getAuthentication();

    HashMap<String, String> userDetails = (HashMap<String, String>) authentication
        .getUserAuthentication()
        .getDetails();

    return new AuthenticationInfo(oauthRestTemplate.getAccessToken(), userDetails);
  }

  class AuthenticationInfo {

    private OAuth2AccessToken token;
    private HashMap<String, String> userDetails;

    public AuthenticationInfo(OAuth2AccessToken token, HashMap<String, String> userDetails) {
      this.token = token;
      this.userDetails = userDetails;
    }

    public String getToken() {
      return this.token.getValue();
    }

    public String getUsername() {
      return this.userDetails.get("username");
    }
    public String getEmail() {
      return this.userDetails.get("email");
    }
    public String getName() {
      return this.userDetails.get("name");
    }

  }


}