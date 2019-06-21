package com.practice.FileManagerGoogleDrive.models;

import java.util.Map;

public class DriveInstance {

	private String name;
	private Map<String, String> providerData;
	private Map<String, String> configuration;
	private Map<String, String> element;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getProviderData() {
		return providerData;
	}

	public void setProviderData(Map<String, String> providerData) {
		this.providerData = providerData;
	}

	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	public Map<String, String> getElement() {
		return element;
	}

	public void setElement(Map<String, String> element) {
		this.element = element;
	}

}
