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
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithOAuth2AuthenticationSecurityContextFactory implements
    WithSecurityContextFactory<WithOAuth2Authentication> {

  @Override
  public SecurityContext createSecurityContext(final WithOAuth2Authentication oauth) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Set<String> scope = new HashSet<>();
    scope.add(oauth.scope());
    OAuth2Request request = new OAuth2Request(null, oauth.clientId(), null, true, scope, null, null, null, null);
    TestingAuthenticationToken token = new TestingAuthenticationToken(oauth.username(), null, "read");
    HashMap<String, String> details = new HashMap<>();
    details.put("username", oauth.username());
    details.put("email", oauth.email());
    details.put("name", oauth.name());
    token.setDetails(details);
    token.setAuthenticated(true);
    Authentication auth = new OAuth2Authentication(request, token);
    auth.setAuthenticated(true);
    context.setAuthentication(auth);
    return context;
  }
}