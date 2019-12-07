package com.collect.api.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.collect.api.model.User;
import com.collect.api.service.IUserService;

@SuppressWarnings("deprecation")
@Component
public class AditionalInfoToken implements TokenEnhancer{

	@Autowired
	private IUserService userService;
	
	@Override 
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		User user = userService.findByUsername(authentication.getName());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("email", user.getEmail());
		data.put("enabled", user.getEnabled());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(data);
		
		return accessToken;
	}

}
