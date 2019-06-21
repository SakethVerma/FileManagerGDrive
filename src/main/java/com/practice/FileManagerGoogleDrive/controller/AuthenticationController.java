package com.practice.FileManagerGoogleDrive.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.practice.FileManagerGoogleDrive.models.ApplicationProperties;
import com.practice.FileManagerGoogleDrive.models.DriveInstance;

@Controller
public class AuthenticationController {

	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	ApplicationProperties properties;

	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String oauth2Callback(HttpServletRequest request) throws Exception {
		String oAuthCode = request.getParameter("code");

		JsonElement backendResponse = createInstanceAndReturnResponse(oAuthCode, properties.getOauthCallbackUrl());

		JsonObject result = new JsonObject();
		result.add("token", backendResponse.getAsJsonObject().get("token"));
		result.add("status", new JsonPrimitive("success"));
		return result.toString();
	}

	@RequestMapping(value = "/callback/ui", method = RequestMethod.GET, produces = "application/json")
	public String oauth2CallbackUI(HttpServletRequest request) throws Exception {
		String oAuthCode = request.getParameter("code");

		JsonElement backendResponse = createInstanceAndReturnResponse(oAuthCode, properties.getOauthCallbackUrlUi());

		return "redirect:/home.html?token=" + backendResponse.getAsJsonObject().get("token").getAsString();
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String googleOAuth2Login(@RequestParam(value = "source", required = false) String source)
			throws Exception {
		String url = UriComponentsBuilder.fromUriString(properties.getInstanceLoginEndpoint())
				.queryParam("apiKey", properties.getOauthApiKey())
				.queryParam("apiSecret", properties.getOauthApiSecret())
				.queryParam("callbackUrl", "ui".equalsIgnoreCase(source) ? properties.getOauthCallbackUrlUi()
						: properties.getOauthCallbackUrl())
				.build().toUri().toString();
		RestTemplate restTemplate = new RestTemplate();
		JsonElement backendResponse = new JsonParser().parse(restTemplate.getForObject(url, String.class));

		JsonObject result = new JsonObject();
		result.add("oauthRedirect", backendResponse.getAsJsonObject().get("oauthUrl"));
		result.add("status", new JsonPrimitive("success"));

		return result.toString();

	}

	private void populateAuthorizationHeader(HttpHeaders headers, String userToken, String organizationToken) {
		headers.set("Authorization", "User " + userToken + ", Organization " + organizationToken);
	}

	private JsonElement createInstanceAndReturnResponse(String oAuthCode, String callbackURL) {
		final String uri = properties.getInstanceCrudEndpoint();

		DriveInstance instance = new DriveInstance();
		populateInstanceModel(oAuthCode, instance, callbackURL);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		populateAuthorizationHeader(headers, properties.getUserToken(), properties.getOrganizationToken());
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<DriveInstance> entity = new HttpEntity<>(instance, headers);

		ResponseEntity<String> respEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		JsonElement backendResponse = new JsonParser().parse(respEntity.getBody());
		return backendResponse;
	}
	
	private void populateInstanceModel(String oAuthCode, DriveInstance instance, String callbackURL) {
		instance.setName("Test" + (Math.random() * 1000) + 1);
		Map<String, String> providerData = new HashMap<>();
		Map<String, String> configurationData = new HashMap<>();
		Map<String, String> elementData = new HashMap<>();
		
		providerData.put("code", oAuthCode);
		
		configurationData.put("oauth.callback.url", callbackURL);
		configurationData.put("oauth.api.key", properties.getOauthApiKey());
		configurationData.put("oauth.api.secret", properties.getOauthApiSecret());
		
		elementData.put("key", "googledrive");
		
		instance.setConfiguration(configurationData);
		instance.setProviderData(providerData);
		instance.setElement(elementData);
	}
}