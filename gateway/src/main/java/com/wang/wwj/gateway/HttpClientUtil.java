package com.wang.wwj.gateway;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Component
public class HttpClientUtil {
	public static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	
	public String getMethodNoParam(String url) throws IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		String entity = null;
		try {
			
			response = client.execute(httpGet);
			System.out.println("response 的状态码： "+response.getStatusLine().getStatusCode());
			
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				entity = EntityUtils.toString(response.getEntity(), "UTF-8");
				logger.info(" response 返回参数： "+ entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("http请求出错，请重新发送请求");
			return "httpClient请求出错";
		} finally {
			if (response != null) {
				response.close();
			}
			client.close();
		}
		return entity;
	}

	
	public String getMethodParam(String url, Map<String, Object> map) throws URISyntaxException {
		
		CloseableHttpClient client = HttpClients.createDefault();

		StringBuffer sb = new StringBuffer();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			String value = map.get(key).toString();
			sb.append("&"+key+"="+value);
		}
		String param = sb.substring(0).replaceFirst("&","");
		
		HttpGet httpGet = new HttpGet(url+"?"+param);
		CloseableHttpResponse response = null;
		String entity = null;
		try {
			response = client.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				entity = EntityUtils.toString(response.getEntity());
				System.out.println(entity);
			}
		} catch (IOException e) {

			return "请求出错";
		}
		return entity;
	}

	
	public String postMethod(String url, Map<String, Object> map) {
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost(url);
		
		CloseableHttpResponse response = null;
		
		List<NameValuePair> list = new ArrayList<>();
		for (String keys : map.keySet()) {
			list.add(new BasicNameValuePair(keys, String.valueOf(map.get(keys))));
		}
		String entitys = null;
		try {
			
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list,"utf-8");
			httpPost.setEntity(urlEncodedFormEntity);
			
			response = client.execute(httpPost);

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				entitys =EntityUtils.toString(response.getEntity());
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entitys;
	}



}
