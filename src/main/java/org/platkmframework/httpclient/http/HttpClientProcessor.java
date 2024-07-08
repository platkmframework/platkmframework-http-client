/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.httpclient.http;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.platkmframework.annotation.HttpRequestMethod;
import org.platkmframework.content.json.JsonUtil;
import org.platkmframework.httpclient.RestInfo;
import org.platkmframework.httpclient.error.HttpClientAttemptError;
import org.platkmframework.httpclient.error.HttpClientError;
import org.platkmframework.httpclient.response.ResponseInfo;
import org.platkmframework.util.JsonException;

import com.google.gson.reflect.TypeToken;

/**
 * description: base class for custom http, sync and async calls
 * @param <E>
 */
public final class HttpClientProcessor extends HttpClientService{  
	
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
	//private List<String> mimeTextList;
	 
	/**
	 * description: mapper object
	 */
	//ObjectMapper mapper;
    
	/**
	 * description: constructor
	 */
    
    private static HttpClientProcessor httpClientProcessor;
    
    private HttpClientProcessor() {
    	super();
    /**	mimeTextList = new ArrayList<>();
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
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);*/
	} 
    
    public static HttpClientProcessor instance() {
    	if(httpClientProcessor == null) httpClientProcessor = new HttpClientProcessor();
    	return httpClientProcessor;
    }
 

	public Object process(String url, Map<String, String> mapQueryParam, Object body, HttpRequestMethod httpMethod,
			boolean isAnyc, FutureCallback<HttpResponse> futureCallBack, RestInfo restInfo, Class<?> returnType) throws HttpClientError, HttpClientAttemptError {
    	
	
		try {
			if(isAnyc) {
	    		
	    		if(httpMethod.name().equals(HttpRequestMethod.GET.name())) {
	    			
	    			asynGet(url, mapQueryParam, restInfo.getHeader(), futureCallBack);
	    			
	    		}else if(httpMethod.name().equals(HttpRequestMethod.POST.name())) {
	    			
	    			asynPost(url, mapQueryParam, restInfo.getHeader(), body != null? new  ByteArrayInputStream(JsonUtil.objectToJson(restInfo).getBytes()):null, futureCallBack);
	    			
	    		}else if(httpMethod.name().equals(HttpRequestMethod.PUT.name())) {
	    			
	    			asynPut(url, mapQueryParam, restInfo.getHeader(), body != null? new  ByteArrayInputStream(JsonUtil.objectToJson(restInfo).getBytes()):null, futureCallBack);
	    			
	    		}else if(httpMethod.name().equals(HttpRequestMethod.DELETE.name())) {
	    			
	    			asynDelete(url, mapQueryParam, restInfo.getHeader(), body != null? new  ByteArrayInputStream(JsonUtil.objectToJson(restInfo).getBytes()):null, futureCallBack);
	    		} 		
	    		
	    	}else { 
	    		
	    		ResponseInfo responseInfo = null;
	    		if(httpMethod.name().equals(HttpRequestMethod.GET.name())) {
	    			
	    			responseInfo = get(url, mapQueryParam, restInfo.getHeader());
	    			
	    		}else if(httpMethod.name().equals(HttpRequestMethod.POST.name())) {
	    			
	    			responseInfo = post(url, mapQueryParam, restInfo.getHeader(), body);
	    			
	    		}else if(httpMethod.name().equals(HttpRequestMethod.PUT.name())) {
	    			
	    			responseInfo = put(url, mapQueryParam, restInfo.getHeader(), body);
	    			
	    		}else if(httpMethod.name().equals(HttpRequestMethod.DELETE.name())) {
	    			responseInfo = delete(url, mapQueryParam, restInfo.getHeader(), body);
	    		}
	    		
	    		if(responseInfo == null) return null;
	    		
	    		if(returnType.equals(Void.TYPE)) return null;
	    		
	    		if(returnType.getName().equals(ResponseInfo.class.getName())) return responseInfo;
	    			
	    		return processResult(responseInfo, returnType);
	    		 
	    	}
		}catch(JsonException e) {
			throw new HttpClientAttemptError(e.getMessage());
		}
		return null;
	}
	
	private <E> E processResult(ResponseInfo responseInfo, Class<E> returnClassType) throws UnsupportedOperationException, JsonException {
		return JsonUtil.jsonToObjectTypeReference(responseInfo.getJson(), TypeToken.get(returnClassType)); 			
	}
 
}