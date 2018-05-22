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

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableOAuth2Client
@Configuration
public class MyConfig {

  @Value("${oauth.resource:https://graph.facebook.com/me}")
  private String baseUrl;
  @Value("${oauth.authorize:https://www.facebook.com/dialog/oauth}")
  private String authorizeUrl;
  @Value("${oauth.token:https://graph.facebook.com/oauth/access_token}")
  private String tokenUrl;

  @Bean
  protected OAuth2ProtectedResourceDetails resource() {

    ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

    List scopes = new ArrayList<String>(2);
    scopes.add("write");
    scopes.add("read");
    resource.setAccessTokenUri(tokenUrl);
    resource.setClientId("restapp");
    resource.setClientSecret("restapp");
    resource.setGrantType("password");
    resource.setScope(scopes);

    resource.setUsername("**USERNAME**");
    resource.setPassword("**PASSWORD**");

    return resource;
  }

  @Bean
  public OAuth2RestTemplate restTemplate() {
    AccessTokenRequest atr = new DefaultAccessTokenRequest();

    return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
  }

}
