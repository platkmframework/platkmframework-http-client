package org.platkmframework.httpclient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RestInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7288856423066417291L;
	
	private String url;
	private Map<String, String> header;
	
	public RestInfo() {
		super();
		header = new HashMap<>();
	}

	public static RestInfo create() {
		return new RestInfo();
	}
	
	public RestInfo url(String url) {
		this.url = url;
		return this;
	}
	
	public RestInfo header(String key, String value) {
		this.header.put(key, value);
		return this;
	}
	
	public String getUrl() {
		return url;
	}
	
	public Map<String, String> getHeader() {
		return header;
	}

}
