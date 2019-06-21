package com.practice.FileManagerGoogleDrive.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.practice.FileManagerGoogleDrive.models.ApplicationProperties;

@Controller
@RequestMapping("/files")
public class FileMgmtController {

	@Autowired
	ApplicationProperties properties;

	@RequestMapping(value = "/content", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<byte[]> getFileContent(@RequestHeader(value = "Authorization") String authToken,
			@RequestParam("path") String path) throws Exception {
		return getFileContents(authToken, path);
	}

	@RequestMapping(value = "/ui/content", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<byte[]> getFileContentForUI(
			@RequestParam(value = "Authorization") String authToken, @RequestParam("path") String path)
			throws Exception {
		return getFileContents(authToken, path);
	}

	@RequestMapping(value = "/content", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String uploadFileContent(@RequestHeader(value = "Authorization") String authToken,
			@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws Exception {
		final String uri = properties.getFileCrudEndpoint() + "?path={path}";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		populateAuthorizationHeader(headers, properties.getUserToken(), properties.getOrganizationToken(), authToken);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("file", file.getResource());
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(bodyMap, headers);

		return restTemplate.exchange(uri, HttpMethod.POST, entity, String.class, path).getBody();
	}

	private void populateAuthorizationHeader(HttpHeaders headers, String userToken, String organizationToken,
			String elementToken) {
		headers.set("Authorization",
				"User " + userToken + ", Organization " + organizationToken + ", Element " + elementToken);
	}

	private ResponseEntity<byte[]> getFileContents(String authToken, String path) {
		final String uri = properties.getFileCrudEndpoint() + "?path={path}";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		populateAuthorizationHeader(headers, properties.getUserToken(), properties.getOrganizationToken(), authToken);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<?> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class, path);
	}
}
