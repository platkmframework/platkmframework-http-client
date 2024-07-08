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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.platkmframework.content.json.JsonUtil;
import org.platkmframework.httpclient.CustomHttpDelete;
import org.platkmframework.httpclient.error.ErrorInfo;
import org.platkmframework.httpclient.error.HttpClientAttemptError;
import org.platkmframework.httpclient.error.HttpClientError;
import org.platkmframework.httpclient.response.ResponseInfo;
import org.platkmframework.util.JsonException;

/**
 * description: util class to sync and async request
 */
public abstract class HttpClientService{ 
	
	/**
	 * description: mime tex list
	 */
	private List<String> mimeTextList;
	 
    
	/**
	 * description: constructor
	 */
    protected HttpClientService() {
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
    	
	}
    
    /**
     * description: async get request
     * @param url : url
     * @param param: parameters
     * @param mHeader: request header
     * @throws HttpClientError - request exception
     * @throws HttpClientAttemptError - request attempt error
     */
    protected void asynGet(String url, Map<String, String> param, Map<String, String> mHeader, FutureCallback<HttpResponse> futureCallback) throws HttpClientError, HttpClientAttemptError{
    	asyn(url, param, mHeader, futureCallback, new HttpGet());
    }
    
    /**
     * description: async put request
     * @param url : url
     * @param param :parameters
     * @param mHeader: header
     * @param body: body to send
     * @throws HttpClientError - request exception
     * @param futureCallback : future callback
     * @throws HttpClientAttemptError - request attempt error
     */
    protected void asynPost(String url, Map<String, String> param, Map<String, String> mHeader, Object body, FutureCallback<HttpResponse> futureCallback) throws HttpClientError, HttpClientAttemptError {
    	asynBody(url, param, mHeader, body, futureCallback, new HttpPost()); 
    }  
    
    /**
     * description: async put request
     * @param url : url
     * @param param :parameters
     * @param mHeader: header
     * @param body: body to send
     * @throws HttpClientError - request exception
     * @throws HttpClientAttemptError - request attempt error
     */
    protected void asynPut(String url, Map<String, String> param, Map<String, String> mHeader, Object body, FutureCallback<HttpResponse> futureCallback) throws HttpClientError, HttpClientAttemptError{
    	asynBody(url, param, mHeader, body, futureCallback, new HttpPut());
    }
    
    /**
     * 
     * @param url
     * @param param
     * @param mHeader
     * @param body
     * @param futureCallback
     * @throws HttpClientError
     * @throws HttpClientAttemptError
     */
    protected void asynDelete(String url, Map<String, String> param, Map<String, String> mHeader, Object body, FutureCallback<HttpResponse> futureCallback) throws HttpClientError, HttpClientAttemptError {
    	asynBody(url, param, mHeader, body, futureCallback, new CustomHttpDelete()); 
    }
    
    /**
     * description: base async call 
     * @param url : url
     * @param param :parameters
     * @param mHeader: header
     * @param body: body to send
     * @throws HttpClientError - request exception
     * @param futureCallback : future callback
     * @throws HttpClientAttemptError - request attempt error
     */
    private void asynBody(String url, Map<String, String> param, Map<String, String> mHeader, Object body, FutureCallback<HttpResponse> futureCallback, HttpEntityEnclosingRequestBase request) throws HttpClientError, HttpClientAttemptError {
		 
    	if(body != null){
    		if(body instanceof InputStream) {
    			InputStreamEntity requestEntity = new InputStreamEntity((InputStream)body); 
    			request.setEntity(requestEntity);
    		}else {
    			try {
					StringEntity requestEntity = new StringEntity( JsonUtil.objectToJson(body), ContentType.APPLICATION_JSON);
					request.setEntity(requestEntity);
    			} catch (UnsupportedCharsetException | JsonException e) {
					 throw new HttpClientAttemptError(e.getMessage());
				}
    		}
		}
    	asyn(url, param, mHeader, futureCallback, request); 
    }
 
     
    
    /**
     * description: async call 
     * @param url : url
     * @param param :parameters
     * @param mHeader: header
     * @param body: body to send
     * @throws HttpClientError - request exception
     * @param futureCallback : future callback
     * @throws HttpClientAttemptError - request attempt error
     */
    private void asyn(String url, Map<String, String> param, Map<String, String> mHeader, FutureCallback<HttpResponse> futureCallback, HttpRequestBase request) throws HttpClientError, HttpClientAttemptError{
    	 
		try {
			
			URI uri = _getUri(url, param);
			request.setURI(uri); 
			_setHeaders(request, mHeader); 
			List<HttpUriRequest> requests = new ArrayList<>();
			requests.add(request);
			
			if(futureCallback == null) {
				asyn(requests);
			}else {
				asyn(requests, futureCallback);
			}
			
		} catch (URISyntaxException | IOException e) {
			throw new HttpClientError("No se ha podido realizar el proceso, int�ntelo m�s tarde");
		} 
    }
    
    /**
     * 
     * @param requests
     * @throws HttpClientAttemptError
     * @throws HttpClientError
     */
    private void asyn(List<HttpUriRequest> requests) throws HttpClientAttemptError, HttpClientError{
    	
    	//final CountDownLatch latch = new CountDownLatch(100);
    	try {
			asyn(requests, new FutureCallback<HttpResponse>() {

						@Override
						public void completed(HttpResponse result) {
							// latch.countDown(); 
						}

						@Override
						public void failed(Exception ex) {
							// latch.countDown(); 
						}

						@Override
						public void cancelled() {
						//	latch.countDown(); 
						}
					});
		} catch ( IOException e) {
			throw new HttpClientAttemptError(e.getMessage());
		}
    }
    
    /**
     * description: async request and future
     * @param requests: http request
     * @param futureCallback: call back function
     * @throws HttpClientError - http error
     * @throws IOException - i0 error
     */
    private void asyn(List<HttpUriRequest> requests, FutureCallback<HttpResponse> futureCallback) throws HttpClientError, IOException{
    	
    	CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            // Start the client
            httpclient.start(); 
            for (int i = 0; i < requests.size(); i++) { 
                httpclient.execute(requests.get(i), futureCallback);
            } 
        } catch (Exception e) {
			throw new HttpClientError(e.getMessage());
		}
    	
    }

    /**
     * description: sync get
     * @param url: url
     * @param param: parameter
     * @param mHeader : request
     * @return requested value
     * @throws HttpClientAttemptError - request error
     * @throws HttpClientError -   request error
     */
    protected ResponseInfo get(String url, Map<String, String> param, Map<String, String> mHeader) throws HttpClientAttemptError, HttpClientError{
		 
		try{ 
			
			URI uri = _getUri(url, param);
			HttpGet request = new HttpGet(uri);  
			_setHeaders(request, mHeader); 
			
			HttpClient client = HttpClientBuilder.create().build(); 
			
			HttpResponse response = client.execute(request); 
			 
			validateResponse(response);
				
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setHeader(response.getAllHeaders());
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
				responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			} 
			String mmt = getMimeType(response);
			responseInfo.setMimeType(mmt);
			
			if(response.getEntity() != null) {
				responseInfo.setJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
			}
			return responseInfo;
			
			/**if(!isAttachment(response)){  
				if(StringUtils.isBlank(mmt) || mimeTextList.contains(getMimeType(response))){
					responseInfo.setText(true);
					responseInfo.setData(  IOUtils.toString(response.getEntity().getContent(), "UTF-8")); 
				}else {
					responseInfo.setData(JsonUtil. response.getEntity().getContent());
				}
			}else { 
				responseInfo.setAttachment(true);
				responseInfo.setData(response.getEntity().getContent()); 
				responseInfo.setFileName(getFileName(response)); 
			}
			 
			return responseInfo;
			*/
		} catch (IOException | URISyntaxException | UnsupportedOperationException | JsonException e) 
		{ 
			throw new HttpClientAttemptError(e.getMessage()); 
		}  
	}
	


	/**
	 * description: custom description:
	 * @param url : url
	 * @param param: parameters 
	 * @param mHeader: header
	 * @param body: body to send 
	 * @return response info
	 * @throws HttpClientError - client error
	 * @throws HttpClientAttemptError - attempt error
	 * 
	 */
	protected ResponseInfo post(String url, Map<String, String> param, Map<String, String> mHeader, Object body) throws HttpClientError, HttpClientAttemptError
	{
		  
		try{
			
			URI uri = _getUri(url, param);
			HttpPost request = new HttpPost(uri); 
			_setHeaders(request, mHeader);
			
			if(body != null){
				if(body instanceof InputStream) {
					InputStreamEntity requestEntity = new InputStreamEntity((InputStream)body); 
					request.setEntity(requestEntity);
				}else {
					StringEntity requestEntity = new StringEntity( JsonUtil.objectToJson(body), ContentType.APPLICATION_JSON);
					request.setEntity(requestEntity);
				}
			}
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(request);
			validateResponse(response);
			
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setHeader(response.getAllHeaders());
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
				responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			} 
			String mmt = getMimeType(response);
			responseInfo.setMimeType(mmt);
			
			if(response.getEntity() != null) {
				responseInfo.setJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
			}
			return responseInfo;
			
			/**responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
			} 
			responseInfo.setData(IOUtils.toString(response.getEntity().getContent(), "UTF-8")); 
		 
			return responseInfo;
			*/
			
		} catch (URISyntaxException | IOException | UnsupportedCharsetException | JsonException e) 
		{  
			throw new HttpClientAttemptError(e.getMessage()); 
		}   
	}
 	 
	
	/**
	 * description: sync put:
	 * @param url : url
	 * @param param: parameters 
	 * @param mHeader: header
	 * @return : httpEntity:  entity  
	 * @throws HttpClientError - client error
	 * @throws HttpClientAttemptError - attempt error
	 */
	protected ResponseInfo put(String url, Map<String, String> param, Map<String, String> mHeader, Object body) throws HttpClientError, HttpClientAttemptError
	{
		 
		try 
		{
			URI uri = _getUri(url, param);
			HttpPut request = new HttpPut(uri);  
			_setHeaders(request, mHeader);
			
			if(body != null){
				if(body instanceof InputStream) {
					InputStreamEntity requestEntity = new InputStreamEntity((InputStream)body); 
					request.setEntity(requestEntity);
				}else {
					StringEntity requestEntity = new StringEntity( JsonUtil.objectToJson(body), ContentType.APPLICATION_JSON);
					request.setEntity(requestEntity);
				}
			}
			HttpClient client = HttpClientBuilder.create().build(); 
			HttpResponse response = client.execute(request);
			validateResponse(response);
			
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setHeader(response.getAllHeaders());
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
				responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			} 
			String mmt = getMimeType(response);
			responseInfo.setMimeType(mmt);
			
			if(response.getEntity() != null) {
				responseInfo.setJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
			}
			return responseInfo;
		 
			/**responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
			} 
			responseInfo.setData(IOUtils.toString(response.getEntity().getContent(), "UTF-8")); 
		 
			return responseInfo;
		 */
			
		} catch (URISyntaxException | IOException | UnsupportedCharsetException | JsonException e) 
		{
			throw new HttpClientAttemptError(e.getMessage());  
		}   
	}	
	
	
	/**
	 * description: sync delete:
	 * @param url : url
	 * @param param: parameters 
	 * @param mHeader: header 
	 * @throws HttpClientError - client error
	 * @throws HttpClientAttemptError - attempt error
	 */
	protected ResponseInfo delete(String url, Map<String, String> param, Map<String, String> mHeader, Object body) throws HttpClientError, HttpClientAttemptError
	{   
		HttpResponse response = null;
		try{
			
			URI uri = _getUri(url, param);
			CustomHttpDelete request = new CustomHttpDelete(uri); 
			_setHeaders(request, mHeader);
			
			if(body != null){
				if(body instanceof InputStream) {
					InputStreamEntity requestEntity = new InputStreamEntity((InputStream)body); 
					request.setEntity(requestEntity);
				}else {
					StringEntity requestEntity = new StringEntity( JsonUtil.objectToJson(body), ContentType.APPLICATION_JSON);
					request.setEntity(requestEntity);
				}
			}
			HttpClient client = HttpClientBuilder.create().build(); 
			response = client.execute(request);
			validateResponse(response);
			
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setHeader(response.getAllHeaders());
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
				responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			} 
			String mmt = getMimeType(response);
			responseInfo.setMimeType(mmt);
			
			if(response.getEntity() != null) {
				responseInfo.setJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
			}
			return responseInfo;
			
			/**ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
			} 
			responseInfo.setData(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));  
			return responseInfo;
			*/
		} catch (URISyntaxException | IOException | UnsupportedCharsetException | JsonException e) 
		{
			if(response != null) {
				throw new HttpClientAttemptError(e.getMessage(), Integer.valueOf(response.getStatusLine().getStatusCode()));
			}else {
				throw new HttpClientAttemptError(e.getMessage());
			}
		}  
 
	}	
	
	/**
	 * description: sync json put:
	 * @param url : url
	 * @param param: parameters 
	 * @param mHeader: header
	 * @return : httpEntity:  entity 
	 * inputStream: body to send
	 * @param filName: file name
	 * @throws HttpClientError - client error
	 * @throws HttpClientAttemptError - attempt error
	 */
	protected ResponseInfo postMultiPart(String url, Map<String, String> param, Map<String, String> mHeader, InputStream inputStream, String fileName) throws HttpClientError,HttpClientAttemptError {

		HttpResponse response = null;
		try {
			URI uri = _getUri(url, param);
			HttpPost post = new HttpPost(uri);   
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		 
			_setHeaders(post, mHeader);
			
			builder.addBinaryBody("upstream", inputStream, ContentType.DEFAULT_BINARY, fileName); 
			  
			HttpEntity entity = builder.build();
			post.setEntity(entity);
			HttpClient client = HttpClientBuilder.create().build(); 
			
			//ResponseInfo responseInfo = new ResponseInfo();
		
			response = client.execute(post); 
			validateResponse(response);
			
			/**responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
			} 
			responseInfo.setData(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));  
			return responseInfo;
			*/
			
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setHeader(response.getAllHeaders());
			if(response.getStatusLine() != null) {
				responseInfo.setReasonPhrase(response.getStatusLine().getReasonPhrase());
				responseInfo.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));
			} 
			String mmt = getMimeType(response);
			responseInfo.setMimeType(mmt);
			
			if(response.getEntity() != null) {
				responseInfo.setJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
			}
			return responseInfo;
			
		} catch (URISyntaxException |IOException | UnsupportedOperationException | JsonException e) {
			if(response != null) {
				throw new HttpClientAttemptError(e.getMessage(), Integer.valueOf(response.getStatusLine().getStatusCode()));
			}else {
				throw new HttpClientAttemptError(e.getMessage());
			}
		}
	}
	
	 
	/**
	 * description: set headers info
	 * @param request: request base
	 * @param mHeader: header
	 */
	private void _setHeaders(org.apache.http.client.methods.HttpRequestBase request, Map<String, String> mHeader){
		 
		for (Map.Entry<String, String> entry : mHeader.entrySet())
			request.setHeader(entry.getKey(),  entry.getValue());
		
	}

	/**
	 * description: request url
	 * @param url: url
	 * @param param: parameters
	 * @return URI
	 * @throws URISyntaxException - uri exception
	 */
	private URI _getUri(String url, Map<String, String> param) throws URISyntaxException{
		URIBuilder builder = new URIBuilder(url);
		
		for (Map.Entry<String, String> entry : param.entrySet())
			builder.setParameter(entry.getKey(),  entry.getValue());
		  
		return builder.build();
		
	}
 
	/**
	 * description: get file name from header
	 * @param response: http response
	 * @return: file name
	 
	private String getFileName(HttpResponse response) {
		 Header[] contentDispositionHeader = response.getHeaders("Content-disposition");
		 if(contentDispositionHeader!= null && contentDispositionHeader.length>0) {
			 Pattern p = Pattern.compile(".*filename=\"(.*)\"");
			 Matcher m = p.matcher(contentDispositionHeader[0].getValue());
			 m.matches();
			 return m.group(1);
		 }
		 return "";
	}
	*/
	/**
	 * description: check if attached file exists
	 * @param response: http response
	 * @return true false
	 
	private boolean isAttachment(HttpResponse response) {
		Header[] contentDispositionHeader = response.getHeaders("Content-disposition");  
		if(contentDispositionHeader != null && contentDispositionHeader.length>0) {
			return contentDispositionHeader[0].getValue().contains("attachment");
		}
		
		return false;
	}
	 */
 
	/**
	 * description: check for mime types
	 * @param response: mime types
	 * @return
	 */
	protected String getMimeType(HttpResponse response) {
		Header contentType = response.getEntity().getContentType();
		if(contentType != null && StringUtils.isNotBlank(contentType.getValue())) {
			return contentType.getValue().split(";")[0].trim(); 
		}
		return "";
	}
 
	/**
	 * description: validate reponse
	 * @param response: http resonse
	 * @throws HttpClientError - htpp client error
	 * @throws IOException 
	 * @throws JsonException 
	 * @throws UnsupportedOperationException 
	 */
	protected void validateResponse(HttpResponse response) throws HttpClientError, UnsupportedOperationException, JsonException, IOException{ 
		if(response.getStatusLine() != null && response.getStatusLine().getStatusCode()>= 400) { 
			if(response.getEntity() != null) {
				ErrorInfo errorInfo = JsonUtil.jsonToObject(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), ErrorInfo.class);
				throw new HttpClientError(errorInfo.getMessage(), errorInfo.getStatus()); 
			}else 
				throw new HttpClientError(response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode()); 
		}   
	}
 
 
}