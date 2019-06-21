package com.practice.FileManagerGoogleDrive.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ApplicationProperties {
	private String oauthCallbackUrl;
	private String oauthApiKey;
	private String oauthApiSecret;
	private String oauthCallbackUrlUi;
	private String userToken;
	private String organizationToken;
	private String instanceLoginEndpoint;
	private String instanceCrudEndpoint;
	private String fileCrudEndpoint;

	public String getOauthCallbackUrl() {
		return oauthCallbackUrl;
	}

	public void setOauthCallbackUrl(String oauthCallbackUrl) {
		this.oauthCallbackUrl = oauthCallbackUrl;
	}

	public String getOauthApiKey() {
		return oauthApiKey;
	}

	public void setOauthApiKey(String oauthApiKey) {
		this.oauthApiKey = oauthApiKey;
	}

	public String getOauthApiSecret() {
		return oauthApiSecret;
	}

	public void setOauthApiSecret(String oauthApiSecret) {
		this.oauthApiSecret = oauthApiSecret;
	}

	public String getOauthCallbackUrlUi() {
		return oauthCallbackUrlUi;
	}

	public void setOauthCallbackUrlUi(String oauthCallbackUrlUi) {
		this.oauthCallbackUrlUi = oauthCallbackUrlUi;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getOrganizationToken() {
		return organizationToken;
	}

	public void setOrganizationToken(String organizationToken) {
		this.organizationToken = organizationToken;
	}

	public String getInstanceLoginEndpoint() {
		return instanceLoginEndpoint;
	}

	public void setInstanceLoginEndpoint(String instanceLoginEndpoint) {
		this.instanceLoginEndpoint = instanceLoginEndpoint;
	}

	public String getInstanceCrudEndpoint() {
		return instanceCrudEndpoint;
	}

	public void setInstanceCrudEndpoint(String instanceCrudEndpoint) {
		this.instanceCrudEndpoint = instanceCrudEndpoint;
	}

	public String getFileCrudEndpoint() {
		return fileCrudEndpoint;
	}

	public void setFileCrudEndpoint(String fileCrudEndpoint) {
		this.fileCrudEndpoint = fileCrudEndpoint;
	}

	public String getFolderCrudEndpoint() {
		return folderCrudEndpoint;
	}

	public void setFolderCrudEndpoint(String folderCrudEndpoint) {
		this.folderCrudEndpoint = folderCrudEndpoint;
	}

	private String folderCrudEndpoint;
}