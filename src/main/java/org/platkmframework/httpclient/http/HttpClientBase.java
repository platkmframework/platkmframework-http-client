/*******************************************************************************
 *   Copyright(c) 2023 the original author or authors.
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *        https://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *******************************************************************************/
package org.platkmframework.httpclient.http;
 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.platkmframework.httpclient.error.HttpClientError;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor; 
import com.fasterxml.jackson.databind.ObjectMapper;
 
/**
 * description: base class for custom http, sync and async calls
 */
public abstract class HttpClientBase extends HttpClientService{  
	
	/**
	 * description: cors rquest method constant
	 */
    public static final String  C_ACCESS_CONTROL_ALLOW_ORIGIN  = "Access-Control-Request-Method";  
    
	/**
	 * description: cors allow method constant
	 */
    public static final String  C_ACCESS_CONTRO_ALLOW_METHODS  = "Access-Control-Allow-Methods";
    
    /**
     * description: cors allow header
     */
    public static final String  C_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    
    /**
     * description: security authorization constant
     */
    public static final String  C_AUTHORIZATION                = "Authorization";  
    
    /**
     * description: cors request header constant
     */
    public static final String  C_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    
    /**
     * description: cors request method
     */
    public static final String  C_ACCESS_CONTROL_REQUEST_METHOD  = "Access-Control-Request-Method";                
                    
    /**
     * description: HTTP status ok constant
     */
    public static final int C_STATUS_OK = 200;
     
    /**
     * description: mime txt list
     */
	private List<String> mimeTextList;
	 
	/**
	 * description: mapper object
	 */
	ObjectMapper mapper;
    
	/**
	 * description: constructor
	 */
    public HttpClientBase() {
    	mimeTextList = new ArrayList<>();
    	 mimeTextList.add("text/css");
    	//mimeTextList.add("text/csv");
    	//mimeTextList.add("text/calendar");
    	 mimeTextList.add("text/html");
    	 mimeTextList.add("application/javascript");
    	 mimeTextList.add("application/json");
    	//mimeTextList.add("application/ld+json");
    	//mimeTextList.add("text/javascript");
    	mimeTextList.add("text/plain");
    	//mimeTextList.add("application/xhtml+xml"); 
    	
    	mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
	} 
 
	
    /**
     * description: default header info
     * @return default header info
     * @throws HttpClientError - http call exception
     */
	protected Map<String, String> _getDefaultHeader() throws HttpClientError {
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Access-Control-Request-Headers", getAccessControlRequestHeader());
		header.put("Access-Control-Request-Method", getAccessCotnrolRequestMethod());
		header.put("Authorization", "Bearer " +  getToken());
		header.put("prdid", getPrdId());
		header.put("acctcode", getAccountCode());
		 
		return header;
	}
 
	/**
	 * description: project id value
	 * @return current project id
	 * @throws HttpClientError -  http call exception
	 */
	public String getPrdId() throws HttpClientError{
		return null;
	} 
	
	/**
	 * description: cors request header value
	 * @return request header value
	 */
	public abstract String getAccessControlRequestHeader();
 
	/**
	 * description: cors request method
	 * @return request method value
	 */
	public abstract String getAccessCotnrolRequestMethod();
 
	/**
	 * description: project token
	 * @return token value
	 * @throws HttpClientError - HTTP request error
	 */
	public abstract String getToken() throws HttpClientError; 
	
	/**
	 * description: current account code
	 * @return account code
	 */
	protected abstract String getAccountCode();
	
	
	
 
}