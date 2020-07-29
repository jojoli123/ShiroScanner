package com.core;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpRequest {
	public static boolean request(String url,String cookie){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Cookie","1=gfdsgdfsg;rememberMe="+cookie+";argash");
		RequestConfig requestConfig =  RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response=null;
		Header[] headerArray=null;
		try{
			response = client.execute(httpGet);
			headerArray=response.getHeaders("Set-Cookie");
		}catch(Exception e){
			response=null;
		}
		finally{
			try{
				response.close();
			}catch(Exception e){}
		}
		if(response==null)return false;
//		Header[] headerArray=response.getAllHeaders();
		if(headerArray.length==0)return false;
		if(headerArray[0].getValue().indexOf("rememberMe")!=-1)return true;
		return false;
	}

	public static void main(String[] args) throws Exception{
	}
}
