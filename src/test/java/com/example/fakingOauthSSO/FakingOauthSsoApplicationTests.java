package com.example.fakingOauthSSO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FakingOauthSsoApplication.class)
public class FakingOauthSsoApplicationTests {

  @Autowired
  WebApplicationContext wac;

  MockMvc mvc;

  @MockBean
  OAuth2RestTemplate template;

  @Before
  public void setup() {
    mvc = webAppContextSetup(wac).build();
    when(template.getOAuth2ClientContext()).thenReturn(new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
    when(template.getAccessToken()).thenReturn(new DefaultOAuth2AccessToken("my-fake-token"));
  }


  @Test
  @WithOAuth2Authentication
  public void testGetAuthenticationInfo() throws Exception {

    mvc.perform(get("/api/token")
        .sessionAttr("scopedTarget.oauth2ClientContext", getOauth2ClientContext()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.token").value("my-fake-token"))
        .andExpect(jsonPath("$.username").value("test"))
        .andExpect(jsonPath("$.name").value("test user"))
        .andExpect(jsonPath("$.email").value("test@test.org"));
  }

  @Test
  @WithOAuth2Authentication(username = "john", name = "john", email = "john@email.com")
  public void testGetAuthenticationInfo2() throws Exception {

    mvc.perform(get("/api/token")
        .sessionAttr("scopedTarget.oauth2ClientContext", getOauth2ClientContext()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.token").value("my-fake-token"))
        .andExpect(jsonPath("$.username").value("john"))
        .andExpect(jsonPath("$.name").value("john"))
        .andExpect(jsonPath("$.email").value("john@email.com"));
  }


  private OAuth2ClientContext getOauth2ClientContext() {
    OAuth2ClientContext mockClient = mock(OAuth2ClientContext.class);
    when(mockClient.getAccessToken()).thenReturn(new DefaultOAuth2AccessToken("my-fake-token"));
    return mockClient;
  }

}
