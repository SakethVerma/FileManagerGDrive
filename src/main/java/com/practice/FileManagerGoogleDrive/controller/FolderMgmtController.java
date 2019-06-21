package com.practice.FileManagerGoogleDrive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.practice.FileManagerGoogleDrive.models.ApplicationProperties;

@Controller
@RequestMapping("/folders")
public class FolderMgmtController {

	@Autowired
	ApplicationProperties properties;
	
	@RequestMapping(value = "/content", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getFolderContentsMeta(@RequestHeader(value = "Authorization") String authToken,
			@RequestParam(value = "path", defaultValue = "/") String path) throws Exception {
		final String uri = properties.getFolderCrudEndpoint() + "?path={path}";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		populateAuthorizationHeader(headers, properties.getUserToken(), properties.getOrganizationToken(), authToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> respEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, path);
		return respEntity.getBody();
	}
	
	private void populateAuthorizationHeader(HttpHeaders headers, String userToken, String organizationToken,
			String elementToken) {
		headers.set("Authorization",
				"User " + userToken + ", Organization " + organizationToken + ", Element " + elementToken);
	}
}
