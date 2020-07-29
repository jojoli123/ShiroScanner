package com.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class DNSLog {
//	private CloseableHttpClient client;
//	private CookieStore cookieStore;
	String cookie;
	
	public DNSLog(){
//		cookieStore = new BasicCookieStore();
//        client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}
	
	public String getDomain2()throws Exception{
		Random rd=new Random();
		URL url=new URL("http","www.dnslog.cn","/getdomain.php?t="+rd.nextInt(900000)+10000);
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Cookie", "");
		connection.connect();
		this.cookie=connection.getHeaderField("Set-Cookie");
		InputStream is=connection.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String resBody="";
		String tmps=null;
		while((tmps=br.readLine())!=null){
			resBody+=tmps;
		}
		
//		System.out.println(cookie);
//		System.out.println(resBody);
		br.close();
		return resBody.trim();
	
	}
	
	public String getRecode2()throws Exception{
		URL url=new URL("http","www.dnslog.cn","/getrecords.php");
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		System.out.println(this.cookie);
		connection.setRequestProperty("Cookie", this.cookie);
		connection.connect();
		InputStream is=connection.getInputStream();
//		System.out.println(cookie);
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String resBody="";
		String tmps=null;
		while((tmps=br.readLine())!=null){
			resBody+=tmps;
		}
//		System.out.println(resBody);
		br.close();
		return resBody;
	}
	
//	public String getDomain(){
//		Random rd=new Random();
//		HttpGet httpGet = new HttpGet("http://www.dnslog.cn/getdomain.php?t="+rd.nextInt(900000)+10000);
//		RequestConfig requestConfig =  RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
//		httpGet.setConfig(requestConfig);
//		String response=null;
//		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		try{
////			response = client.execute(httpGet,responseHandler);
////			System.out.println(cookieStore.getCookies().get(0).getValue());
//		}catch(Exception e){
//			response=null;
//		}
//		if(response==null)return null;
//		else{
//			return response;
//		}
//	}
	
//	public String getRecode(){
//		HttpClient client=HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("http://www.dnslog.cn/getrecords.php");
////		String cookie=cookieStore.getCookies().get(0).getName()+'='+cookieStore.getCookies().get(0).getValue()+";";
////		System.out.println(cookie);
//		RequestConfig requestConfig =  RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
//		httpGet.setConfig(requestConfig);
//		httpGet.setHeader("Cookie", cookie);
//		String response=null;
//		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		try{
//			response = client.execute(httpGet,responseHandler);
//		}catch(Exception e){
//			response=null;
//		}
//		if(response==null)return null;
//		else{
//			return response;
//		}
//	}
	
	public static void main(String[] args) {
		try{
			DNSLog log=new DNSLog();
			String x=log.getDomain2().trim();
			System.out.println(x);
			Runtime.getRuntime().exec("ping xxgsfgdsfghsdhx."+x);
			Thread.sleep(1000);
			String y=log.getRecode2();
			System.out.println(y);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
