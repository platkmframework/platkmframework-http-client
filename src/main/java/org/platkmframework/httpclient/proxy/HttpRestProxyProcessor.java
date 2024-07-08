package org.platkmframework.httpclient.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.platkmframework.annotation.ClassMethod;
import org.platkmframework.annotation.HttpRequestMethod;
import org.platkmframework.annotation.HttpRest;
import org.platkmframework.annotation.HttpRestAsyn;
import org.platkmframework.annotation.RequestBody;
import org.platkmframework.annotation.RequestParam;
import org.platkmframework.content.project.ProjectContent;
import org.platkmframework.httpclient.RestInfo;
import org.platkmframework.httpclient.error.HttpClientAttemptError;
import org.platkmframework.httpclient.error.HttpClientError;
import org.platkmframework.httpclient.http.HttpClientProcessor;
import org.platkmframework.proxy.ProxyProcesorException;
import org.platkmframework.proxy.ProxyProcessor;

public class HttpRestProxyProcessor implements ProxyProcessor{
	
	private static Logger logger = LoggerFactory.getLogger(HttpRestProxyProcessor.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object run(Object proxy, Class<?> classInterface, Method method, Object[] args) throws ProxyProcesorException {
		
		if(!method.isAnnotationPresent(ClassMethod.class))
			throw new ProxyProcesorException("La notacion ClassMethod no está presente en el ecabezado del método");
		
		ClassMethod classMethod = method.getAnnotation(ClassMethod.class);
		String methodPath = classMethod.name();
		HttpRequestMethod httpMethod = classMethod.method();
		
		Object body = null;
		RestInfo restInfo = null;
		Map<String, Object> mapParam = new HashMap<>();
		Parameter parameter;
		FutureCallback<HttpResponse> futureCallBack = null;
		
		for (int i = 0; i < method.getParameters().length; i++){
			parameter = method.getParameters()[i];
			if(parameter.isAnnotationPresent(RequestParam.class)){
				mapParam.put(((RequestParam)parameter.getAnnotation(RequestParam.class)).name(), args[i]);
			}else if(parameter.isAnnotationPresent(RequestBody.class)){
				body = args[i]; 
			}else if (RestInfo.class.getName().equals(parameter.getType().getName())){
				restInfo = RestInfo.class.cast(args[i]); 
			}else if (parameter.getType().getName().equals(FutureCallback.class.getName())) {
				futureCallBack = FutureCallback.class.cast(args[i]); 
			}
		}
		
		if(restInfo == null) {
			String restConfigurationId = classInterface.getAnnotation(HttpRest.class).configuration();
			if(StringUtils.isBlank(restConfigurationId))  
				throw new ProxyProcesorException("No se encontró configuración para procesar el request");
			
			restInfo = (RestInfo)  ProjectContent.instance().get(restConfigurationId);
			if(restInfo == null) 
				throw new ProxyProcesorException("No se encontró configuración para procesar el request");
		}
		
		String url = restInfo.getUrl() + "/" + methodPath;
		Map<String, String> mapQueryParam = new HashMap<>();
		
		for(Map.Entry<String, Object> entry : mapParam.entrySet()){
		    String key   = entry.getKey();
		    Object value = entry.getValue();
		    if(url.contains("{" + key + "}")) {
		    	url = url.replace("{" + key + "}", value.toString());
		    }else {
		    	mapQueryParam.put(key, value.toString()); 
		    }
		} 
		
		try
		{
			return HttpClientProcessor.instance().process(url, mapQueryParam, body,httpMethod, method.isAnnotationPresent(HttpRestAsyn.class), futureCallBack, restInfo, method.getReturnType());
		
		} catch (HttpClientError e) {
			throw new ProxyProcesorException(e.getStatus(),e.getMessage());
		} catch (HttpClientAttemptError e) {
			logger.error(e.getMessage() + "url->" +url);
			throw new ProxyProcesorException("No se a podido realizar la operación");
		}
	}

}
