


package com.ifroglab.lora;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
//import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

//import com.fazecast.jSerialComm.SerialPort;

//import gnu.io.*;
//An AWT program inherits from the top-level container java.awt.Frame
public class ifroglablora extends Frame {
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// LoRa 相關變數
	private loralib mloralib;
	private ArrayList<String> mLoRaSerialPortString; //找到的LoRa Com Port 位置
	private String mDeviceID;   // 現在開啟的LoRa設備名稱
	public byte Freq1=0x01;
	public byte Freq2=0x65;
	public byte Freq3=0x6c;
	public byte Power=0x3;  //{ TXRX,0x01,0x65,0x6c,0x3); 
	
	// UI
	private  Choice mUIComPortName; //Step 1  Com 設備列表
	private  int tUITop=30;
	private  TextArea recevieText;  //顯示接收得資料
	private  TextArea sendText;     //顯示送出的資料
	private  Choice mChoiceRecevieDisplay;   // 目標顯示，是要文字

	private Frame mainFrame;       // GUI Frame
	private Label labelMessage;    // Declare a Label component 
	private Label labelLoRaStatus;    // Declare a Label component 
	//private TextField tfCount; // Declare a TextField component 
	//private Button btnCount;   // Declare a Button component
	private Label mLabelStatus;  // "設備狀態" 
	private Label mLabelID;   // MAC ID:
	private Label mLabelFireware;    // Fireware Version
	private Choice ChoiceKind;
	// private String ButtonLogFileName;  
	
	 
	
	// 程式專用

	// 網址
	//private String mDomainName="http://localhost/ICBlock/iot/";//"http://www.ifroglab.com/iot/";
	private String mDomainName="http://www.ifroglab.com/iot/";
	public String mURL[]= {
			  mDomainName+"AjaxPerson.php?action=login",  // 確認是否有帳號
			  mDomainName+"AjaxPerson.php?action=create&groupid=0&level=0&FBID=0",  // 建立帳號
			  mDomainName+"AjaxIoTProjects.php?action=selectPersonalId&IoT=1",  // 確認是否有專案
			  mDomainName+"AjaxIoTProjects.php?action=create&IoT=1",  // 建立專案
			  mDomainName+"AjaxIoTWidgets.php?action=List",  // 確認是否有IoTWidgets
			  mDomainName+"AjaxIoTWidgets.php?action=create&IoT=1",  // 建立專案  //5
			  mDomainName+"AjaxIoTWidgets.php?action=ListDB",  // 建立專案
			  mDomainName+"page_IoT_index.php?device=LoRa&IoT=1&share=1&projectid=",  // 把開專案web
			  // AjaxIoTTable.php?action=upload&Key=data&Value=33&apikey=60-010000D5
			  mDomainName+"AjaxIoTTable.php?action=upload",  // 上傳資料
	          };
	// 多國語言
	public String mStr[][]={
			  {"Step1:","步驟 1:"},
			  {"Select LoRa Com Port","iFrogLab LoRa 設備"},
			  {"Reload","搜尋LoRa設備"},
			  {"Searching:","搜尋中LoRa"},
			  {"Done","完成"},
			  {"Step2:","步驟 2:"},  //5
			  {"Setup device","設定設備"},  
			  {"Stauts: ","設備狀態:"},  
			  {"No LoRa Device","沒有LoRa設備"},  
			  {"MAC Address:","MAC 地址："},
			  {"Fireware Version:","韌體版本:"}, //10
			  {"Broadcast","廣播Broadcast"},
			  {"Node (Client)","節點端 Node"},
			  {"Gateway (Server)","伺服器端 Gateway"},
			  {"Preferences","更多設定"},
			  {"Step3:","步驟 3:"},  //15
			  {"",""},
			  {"",""},
			  {"",""},
			  {"Send data","傳送的資料"}, 
			  {"Send","送出"},  //20
			  {"LoRa Device Selected: ","LoRa設備被選取:"}, 
			  {"LoRa Device Setup to: ","LoRa設備被設定為:"}, 
			  {"Open LoRa Device: ","LoRa設備連接中:"}, 
			  {"Open LoRa","啟動LoRa"},
			  {"Close LoRa","關閉LoRa"}, //25
			  {"Turn On LoRa ID ","使用中LoRa設備編號"},
			  {"LoRa Turn Close","LoRa未啟動"},
			  {"",""},
			  {"",""},
			  {"",""},  //30
			  {"Clear","清除"},  
			  {"Receive data ","收到的資料"},  
			  {"Text (UTF-8):","UTF-8  文字"},
			  {"Hex (00 to ff) for E.g 01,aa","16進位數字 (00 到 ff) 例如：01,aa"}, 
			  {"Decimal (0 to 255) E.g: 1,122,255","10進位 (0-255)  例如: 1,122,255 "}, //35
			  {"File:","檔案"}, 
			  {"",""},
			  {"",""},
			  {"",""},
			  {"English","English"}, //40
			  {"Traditional Chinese","繁體中文"}, 
			  {"Send a lot of data","送大量資料"}, 
			  {" isn't iFrogLab LoRa Device"," 不是iFrogLab LoRa 設備"},  
			  {"iFrogLab LoRa Device worning, now.","iFrogLab LoRa 工作中"}, 
			  {"Upload to Dashboard","上傳到儀表板"}, //45  
			  {"Open Dashboard","打開儀表板"}, 
			  {"save data to","儲存資料到"},   
			  {"http://127.0.0.1/index.php?data=","http://127.0.0.1/index.php?data="}, 
			  {"LoRa Setup:","設定LoRa設備:"},  
			  {"frequency MHz(137.00~1020.00)default 915","頻率MHz(137.00 ~ 1020.00,內定915.00:"},  //50
			  {"Power( default 5dBm):","功率 (內定5dBm):"},  
			  {"When Receive Data:","當接收到資料:"},     
			  {"Upload to HTTP (URL+data)","上傳到網路HTTP+data"}, 
			  {"Upload to MQTT","上傳到MQTT"},    
			  {"Data can be spread to the Internet, Dashboard etc.,see \"Preferences\".","接收到資料時還可以傳到網路、Dashboard、MQTT，請在「更多設定」選取"},    //55
			  {"Application Setup","軟體設定"},    
			  {"Language:","語言:"},       
			  {"Need to re-start application for the new settings.","需重新開啟應用程式，才會執行新的設定"},    
	};
	// 程式用到的變數
	private ifroglablora ifroglabloraClass; 
	public int lan=0;
	public String StringLogFileName="iFrogLab.csv";
	public String TextFieldUrl;
	public String checkBoxSaveLog;
	public String checkBoxUpload;
	public String checkBoxDashboard;
	public String Frequency;
	public String PersonId;
	public String ProjectId;
	public String APIKEY;
	
	
	
	
	@SuppressWarnings("deprecation")
	public ifroglablora () {
	   data_init(); 
	   if(mloralib==null)  mloralib=new loralib();
	   // 內定值設定
	   mDeviceID="";
	   ifroglabloraClass=this;
	   // Begin 01, 設定主畫面UI
	   mainFrame= new Frame();  
	   mainFrame = new Frame("iFrogLab LoRa Application");
	   mainFrame.addWindowListener(new WindowAdapter() {         // 關閉按鈕
	       public void windowClosing(WindowEvent windowEvent){
	    	   if(mloralib!=null){  mloralib.FunLora_close(); }
	          System.exit(0);
	       }        
	   });
	   mainFrame.setSize(600,490+60);  
	   mainFrame.setMinimumSize(new Dimension(600,490+60));
	   mainFrame.setMaximumSize(new Dimension(600,490+60));
	   mainFrame.setResizable(false);
	   
	   mainFrame.setLayout(null);  
	   ui_Step1(mainFrame);
	   ui_Step2(mainFrame);
	   ui_Step3(mainFrame);
	   mainFrame.setVisible(true);  
	   mainFrame.addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {  }
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
	   // END 01
	   showMenu(); 											    // 顯示下拉式選單
	   data_ifroglab_dashboard();
	}
	// 連接LoRa COM ports 設備
	private void ui_Setp1_OpenComPort(){ 					   	// 連接 LoRa USB COM port 設備
	    if(mLoRaSerialPortString!=null && mLoRaSerialPortString.size()>0){  // 如果有設備的話
			// String data = mStr[22][lan]+ mUIComPortName.getSelectedItem(); //mUIComPortName.getItem(mUIComPortName.getSelectedIndex());
			labelMessage.setText(mStr[23][lan]+ mUIComPortName.getSelectedItem());	 //顯示開設備名稱　LoRa USB COM port
			mloralib.SerialPort_setSerialPort(mUIComPortName.getSelectedItem());     //告訴LoRa Lib要打開的設備LORA Com Port 
	    }
	}
	private void data_init() {
		// BEGIN 01 設定語言
		String t1=FunPreferencesLoad("Language","0");
		if(t1!="") { 	lan=Integer.parseInt(t1); }
		// END   01 設定語言
		// BEGIN 02 設定Log 檔案名稱
		StringLogFileName=FunPreferencesLoad("StringLogFileName","iFrogLab.csv");
	    // END   02 設定Log 檔案名稱
		// BEGIN 03 設定 網路位置
		TextFieldUrl=FunPreferencesLoad("TextFieldUrl","http://127.0.0.1/index.php?data=");
	    // END   03 設定 網路位置
		checkBoxSaveLog=FunPreferencesLoad("checkBoxSaveLog","false");
		checkBoxUpload=FunPreferencesLoad("checkBoxUpload","false");
		checkBoxDashboard=FunPreferencesLoad("checkBoxDashboard","true");

		// BEGIN 05 取得和轉換 頻率
		Frequency=FunPreferencesLoad("Frequency","915.00");
		float tFloatFrequency = Float.parseFloat(Frequency);
		tFloatFrequency=tFloatFrequency*100;
		int tIntFrequency=(int)tFloatFrequency;
		//String tStrHexFrequency=Integer.toHexString(tIntFrequency);
		int tFreq1=tIntFrequency/(256*256);
		int tFreq2=(tIntFrequency-tFreq1*(256*256))/256;
		int tFreq3=(tIntFrequency)%256;
		Freq1=(byte)tFreq1;
		Freq2=(byte)tFreq2;
		Freq3=(byte)tFreq3;
		// END 05 取得和轉換 頻率
		// BEGIN 05 取得和轉換 頻率
		String tPower=FunPreferencesLoad("Power","3");
		Power = (byte)Integer.parseInt(tPower);
		// END 05 取得和轉換 頻率
		ProjectId=FunPreferencesLoad("ProjectId","60");
		mDeviceID=FunPreferencesLoad("mDeviceID","010000D5");
		APIKEY=FunPreferencesLoad("APIKEY","");
	}
	// 找出LoRa USB COM ports 設備
	private void ui_Setp1_listPorts(){ 					   	    // 找出LoRa USB COM ports 設備
		 labelMessage.setText(mStr[3][lan]); //"Searching"
	     	// 找設備，並且顯示在combo中
     	 try{
		    mLoRaSerialPortString=mloralib.serial_allPorts();

	    	mUIComPortName.removeAll();                          // 清除下拉式選單
		    if(mLoRaSerialPortString.size()>0){
		       int i=0;
		       while (i<mLoRaSerialPortString.size()){
		    	   mUIComPortName.add(mLoRaSerialPortString.get(i).toString());            //把COM Port 加上
		    	   i++;
		       }
		    }else{
		 	   mUIComPortName.add(mStr[1][lan]);             //"No Com Port Devices"
		    }
		    ui_Setp1_OpenComPort(); 					      	// 連接 LoRa USB COM port 設備
         }catch ( Exception e1 ){
            e1.printStackTrace();
         }
	}
	public interface Callback {
		  // abstract methods
		  public void OnSuccess(String response);
		  public void OnError(int status_code, String message);
		 }
	public String FunHTTPPost(String urlString,String PostData) {

	    StringBuilder sb = new StringBuilder();
		//String data3 = "";
		String Data = PostData; //"data=Hello+World!";
	    try {
		    URL url = new URL(urlString); //"http://localhost:8084/WebListenerServer/webListener");
		    HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
		    con.setDoOutput(true);
		    con.getOutputStream().write(Data.getBytes("UTF-8"));
		    con.getInputStream();
		    
		    int HttpResult = con.getResponseCode();
	        if (HttpResult == HttpURLConnection.HTTP_OK) {
	            BufferedReader br = new BufferedReader(new InputStreamReader(
	            		con.getInputStream(), "utf-8"));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            br.close();
	            //System.out.println( sb.toString());
	            return sb.toString();
	        } else {
	        	   //  System.out.println(" "  + con.getResponseMessage());
	        }
		     
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	private String FunRandomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	public String FunHTTP(String urlString) {
		//String urlString =urlString
				URL url;
				URLConnection conn;
				InputStream is;
				String data3 = "";
				try {
					url = new URL(urlString);
					try {
						    conn = url.openConnection();
   					        is = conn.getInputStream();
   					        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
   					        StringBuffer sb = new StringBuffer();
   					        String line = "";
   					        while ((line = br.readLine()) != null) {
   					            sb.append(line);
   					        }
   					        data3 = sb.toString();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return data3;
	}
    // Dashboard Login 
	public String data_ifroglab_dashboard_01_login(String mDeviceID) {                 // 打開Dashboard  帳戶
		   // BEGIN 1,確認是否有Dashboard  帳戶
		   if(mDeviceID.length()>0) {
			   String t1=FunHTTPPost(mURL[0],"email="+mDeviceID+"&password="+mDeviceID);
			   System.out.println(" URL: " +t1);
			   try {
				   JSONParser parser = new JSONParser();
			       JSONObject obj2 = (JSONObject)parser.parse(t1);
			       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
			       // if(obj2.get("Result").toString().compareTo("OK")==0 && obj2.get("TotalRecordCount").toString().compareTo("0")==0) {
			       if(obj2.get("Result").toString().equals("OK") && obj2.get("TotalRecordCount").equals("0") ) {  //有反應，但是沒有該用戶
			    	   		return "0";
			       }else if(obj2.get("Result").toString().equals("OK") && obj2.get("TotalRecordCount").equals("1") )   {   //有反應，也有該用戶
			    	        // URL: {"Result":"OK","TotalRecordCount":"1","debbug":"SELECT * FROM people WHERE email='010000D5' and password='010000D5';","Records":[{"0":"113","PersonId":"113","1":"010000D5","Name":"010000D5","2":"0","Age":"0","3":"2018-03-06 18:07:01","RecordDate":"2018-03-06 18:07:01","4":"0000-00-00","BirthDate":"0000-00-00","5":"","idNumber":"","6":"","Phone":"","7":"","EmergencyPhone":"","8":"","EmergencyPeople":"","9":"","deviceID":"","10":"","Memo":"","11":"0","groupid":"0","12":"0","Gender":"0","13":"","image":"","14":"010000D5","email":"010000D5","15":"010000D5","password":"010000D5","16":"0","level":"0","17":"0","CreatedPersonId":"0","18":"0","FBID":"0"}]}
			           JSONArray array = (JSONArray)obj2.get("Records");
			           JSONObject obj3 = (JSONObject)array.get(0);
			           return obj3.get("PersonId").toString();    	   			    	 
			       }
			   } catch (ParseException e) {
				   e.printStackTrace();
			   }
			}
			// END 1, 如果要上傳到 Dashboard
		   return "";
	}
    // Dashboard Create Account 
	public String data_ifroglab_dashboard_02_CreateAccount(String mDeviceID) {                 // 打開Dashboard  帳戶
		   // BEGIN 1,確認是否有Dashboard  帳戶
		   if(mDeviceID.length()>0) {
			   String t1=FunHTTPPost(mURL[1],"Name="+mDeviceID+"&email="+mDeviceID+
					                         "&password="+mDeviceID+ "&BirthDate="+
					                         "&idNumber="+"&Phone="+
					                		 "&EmergencyPhone="+ "&EmergencyPeople="+
					                		 "&deviceID="+ "&Memo="	   );
			   System.out.println(" URL: " +t1);
			   // URL: {"Result":"OK","Record":{"0":"107","PersonId":"107","1":"010000D5","Name":"010000D5","2":"0","Age":"0","3":"2018-03-06 17:51:08","RecordDate":"2018-03-06 17:51:08","4":"0000-00-00","BirthDate":"0000-00-00","5":"","idNumber":"","6":"","Phone":"","7":"","EmergencyPhone":"","8":"","EmergencyPeople":"","9":"","deviceID":"","10":"","Memo":"","11":"0","groupid":"0","12":"0","Gender":"0","13":"","image":"","14":" 010000D5","email":" 010000D5","15":" 010000D5","password":" 010000D5","16":"0","level":"0","17":"0","CreatedPersonId":"0","18":"0","FBID":"0"}}
			   try {
				   JSONParser parser = new JSONParser();
			       JSONObject obj2 = (JSONObject)parser.parse(t1);
			       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
			       if(obj2.get("Result").toString().equals("OK")) {  //有反應，但是沒有該用戶
			    	   		JSONObject obj3 = (JSONObject)obj2.get("Record");
			    	   		String t3=obj3.get("PersonId").toString();
			    	   		return t3;
			       }
			   } catch (ParseException e) {
				   e.printStackTrace();
			   }
			}
			// END 1, 如果要上傳到 Dashboard
		   return "";
	}
	 //List Project
	public String data_ifroglab_dashboard_03_ListProject(String mDeviceID,String PersonalId) {                 // 打開Dashboard  帳戶
		   // BEGIN 1,確認是否有Dashboard  帳戶
		   if(mDeviceID.length()>0) {
			   String t1=FunHTTPPost(mURL[2], "PersonalId="+PersonalId );
			   //{"Result":"OK","TotalRecordCount":"0","Records":[]}
			   System.out.println(" URL: " +t1);
			   try {
				   JSONParser parser = new JSONParser();
			       JSONObject obj2 = (JSONObject)parser.parse(t1);
			       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
			       if(obj2.get("Result").toString().equals("OK") && obj2.get("TotalRecordCount").equals("0") ) {  //有反應，但是沒有該用戶
			    	   		return "0";
			       }else if(obj2.get("Result").toString().equals("OK")) {   //有反應，也有該用戶 
			    	       //   {"Result":"OK","TotalRecordCount":"1","Records":[{"0":"60","id":"60","1":"010000D5","projectName":"010000D5","2":"","imagefilename":"","3":"010000D5","description":"010000D5","4":"010000D5","instructions":"010000D5","5":"113","PersonalId":"113","6":"1","shared":"1","7":"2018-03-06 23:49:04","RecordDate":"2018-03-06 23:49:04","8":"","block":"","9":"","device":"","10":"1","IoT":"1"}]}
			    	       JSONArray array = (JSONArray)obj2.get("Records");
			           JSONObject obj3 = (JSONObject)array.get(0);
			           return obj3.get("id").toString();    	   			    	 
			       }
			   } catch (ParseException e) {
				   e.printStackTrace();
			   }
			}
			// END 1, 如果要上傳到 Dashboard
		   return "";
	}
	 //Create Project 
		public String data_ifroglab_dashboard_04_CreateProject(String mDeviceID,String PersonalId) {                 // 打開Dashboard  帳戶
			   if(mDeviceID.length()>0) {
				   String t1=FunHTTPPost(mURL[3], "projectName="+mDeviceID+"&imagefilename2="+
	                         "&description="+mDeviceID+"&instructions="+mDeviceID+
	                         "&PersonalId="+PersonalId+"&shared=1"+
	                		 "&block="+"&device=" );
				   //{"Result":"OK","debug":"INSERT INTO IoTProjects(projectName, imagefilename,description,instructions,PersonalId,shared,block,device,IoT,RecordDate) VALUES('010000D5','','010000D5','010000D5',113,1,'','',1,now());","sql":"INSERT INTO IoTProjects(projectName, imagefilename,description,instructions,PersonalId,shared,block,device,IoT,RecordDate) VALUES('010000D5','','010000D5','010000D5',113,1,'','',1,now());",
				    // "Record":{"0":"60","id":"60","1":"010000D5","projectName":"010000D5","2":"","imagefilename":"","3":"010000D5","description":"010000D5","4":"010000D5","instructions":"010000D5","5":"113","PersonalId":"113","6":"1","shared":"1","7":"2018-03-06 23:49:04","RecordDate":"2018-03-06 23:49:04","8":"","block":"","9":"","device":"","10":"1","IoT":"1"}}
				   System.out.println(" URL: " +t1);
				   try {
					   JSONParser parser = new JSONParser();
				       JSONObject obj2 = (JSONObject)parser.parse(t1);
				       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
				       // if(obj2.get("Result").toString().compareTo("OK")==0 && obj2.get("TotalRecordCount").toString().compareTo("0")==0) {
				       if(obj2.get("Result").toString().equals("OK") && obj2.get("TotalRecordCount").equals("0") ) {  //有反應，但是沒有該用戶
				    	   		return "0";
				       }else if(obj2.get("Result").toString().equals("OK") && obj2.get("TotalRecordCount").equals("1") )   {   //有反應，也有該用戶
				    	        // URL: {"Result":"OK","TotalRecordCount":"1","debbug":"SELECT * FROM people WHERE email='010000D5' and password='010000D5';","Records":[{"0":"113","PersonId":"113","1":"010000D5","Name":"010000D5","2":"0","Age":"0","3":"2018-03-06 18:07:01","RecordDate":"2018-03-06 18:07:01","4":"0000-00-00","BirthDate":"0000-00-00","5":"","idNumber":"","6":"","Phone":"","7":"","EmergencyPhone":"","8":"","EmergencyPeople":"","9":"","deviceID":"","10":"","Memo":"","11":"0","groupid":"0","12":"0","Gender":"0","13":"","image":"","14":"010000D5","email":"010000D5","15":"010000D5","password":"010000D5","16":"0","level":"0","17":"0","CreatedPersonId":"0","18":"0","FBID":"0"}]}
				           JSONArray array = (JSONArray)obj2.get("Records");
				           JSONObject obj3 = (JSONObject)array.get(0);
				           return obj3.get("PersonId").toString();    	   			    	 
				       }
				   } catch (ParseException e) {
					   e.printStackTrace();
				   }
				}
			   return "";
		}
		 //List IoTWidgets
		public String data_ifroglab_dashboard_05_ListIoTWidgets(String mDeviceID,String PersonalId,String IoTProjectsId) {                 // 打開Dashboard  帳戶
			   // BEGIN 1,確認是否有Dashboard  帳戶
			   if(mDeviceID.length()>0) {
				   String t1=FunHTTPPost(mURL[4], "IoTProjectsId="+IoTProjectsId );
				   //{"Result":"OK","TotalRecordCount":0,"maxid":0,"Records":[]}
				   System.out.println(" URL: " +t1);
				   try {
					   JSONParser parser = new JSONParser();
				       JSONObject obj2 = (JSONObject)parser.parse(t1);
				       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
				       if(obj2.get("Result").toString().equals("OK")) {  //&& obj2.get("TotalRecordCount").equals("0") ) {  //有反應，但是沒有該用戶
				    	   	   // 		return "0";
				    	   	   // }else if(obj2.get("Result").toString().equals("OK")) {   //有反應，也有該用戶 
				    	       //   {"Result":"OK","TotalRecordCount":"1","Records":[{"0":"60","id":"60","1":"010000D5","projectName":"010000D5","2":"","imagefilename":"","3":"010000D5","description":"010000D5","4":"010000D5","instructions":"010000D5","5":"113","PersonalId":"113","6":"1","shared":"1","7":"2018-03-06 23:49:04","RecordDate":"2018-03-06 23:49:04","8":"","block":"","9":"","device":"","10":"1","IoT":"1"}]}
				    	       return obj2.get("maxid").toString(); 	   			    	 
				       }
				   } catch (ParseException e) {
					   e.printStackTrace();
				   }
				}
				// END 1, 如果要上傳到 Dashboard
			   return "";
		}
		 //Create IoTWidgets 
			public String data_ifroglab_dashboard_06_CreateIoTWidgets(String mDeviceID,String PersonId,String IoTProjectsId,String maxid,String APIKEY,String iData,String iDatatype) {                 // 打開Dashboard  帳戶
				   if(mDeviceID.length()>0) {
					   String t1=FunHTTPPost(mURL[5], "Data="+iData+"&OwnerId="+PersonId+
		                         "&Datatype="+iDatatype+"&IoTProjectsId="+IoTProjectsId+
		                         "&IoTWidgetID="+maxid+"&title=iFrogLabLoRa"+
		                		 "&APIKEY="+APIKEY);
					   //{"Result":"OK","sql":"INSERT INTO IoTWidgets( Data,OwnerId,Datatype,IoTProjectsId,IoTWidgetID,title,APIKEY) VALUES('ifroglablora',113,'database',60,1,'iFrogLabLoRa','ifroglablora');",
					   // "TotalRecordCount":1,"Records":[{"0":"51","id":"51","1":"","KeyName":"","2":"ifroglablora","Data":"ifroglablora","3":"113","OwnerId":"113","4":"database","Datatype":"database","5":"60","IoTProjectsId":"60","6":"1","IoTWidgetID":"1","7":"iFrogLabLoRa","title":"iFrogLabLoRa","8":"ifroglablora","APIKEY":"ifroglablora"}]}

					   System.out.println(" URL: " +t1);
					   try {
						   JSONParser parser = new JSONParser();
					       JSONObject obj2 = (JSONObject)parser.parse(t1);
					       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
					       // if(obj2.get("Result").toString().compareTo("OK")==0 && obj2.get("TotalRecordCount").toString().compareTo("0")==0) {
					       //if(obj2.get("Result").toString().equals("OK") && obj2.get("TotalRecordCount").equals("0") ) {  //有反應，但是沒有該用戶
					    	   //		return "0";
					       //}else 
					    	   if(obj2.get("Result").toString().equals("OK"))   {   //有反應，也有該用戶
					    	        // URL: {"Result":"OK","TotalRecordCount":"1","debbug":"SELECT * FROM people WHERE email='010000D5' and password='010000D5';","Records":[{"0":"113","PersonId":"113","1":"010000D5","Name":"010000D5","2":"0","Age":"0","3":"2018-03-06 18:07:01","RecordDate":"2018-03-06 18:07:01","4":"0000-00-00","BirthDate":"0000-00-00","5":"","idNumber":"","6":"","Phone":"","7":"","EmergencyPhone":"","8":"","EmergencyPeople":"","9":"","deviceID":"","10":"","Memo":"","11":"0","groupid":"0","12":"0","Gender":"0","13":"","image":"","14":"010000D5","email":"010000D5","15":"010000D5","password":"010000D5","16":"0","level":"0","17":"0","CreatedPersonId":"0","18":"0","FBID":"0"}]}
					           JSONArray array = (JSONArray)obj2.get("Records");
					           JSONObject obj3 = (JSONObject)array.get(0);
					           return obj3.get("id").toString();    	   			    	 
					       }
					   } catch (ParseException e) {
						   e.printStackTrace();
					   }
					}
				   return "";
			}
			 //List IoTWidgets
			public String data_ifroglab_dashboard_07_ListIoTWidgetsDB(String mDeviceID,String PersonalId,String IoTProjectsId) {                 // 打開Dashboard  帳戶
				   // BEGIN 1,確認是否有Dashboard  帳戶
				   if(mDeviceID.length()>0) {
					   String t1=FunHTTPPost(mURL[6], "IoTProjectsId="+IoTProjectsId+"&Datatype='database'" );
					   // {"Result":"OK","debug":"SELECT * FROM IoTWidgets WHERE IoTProjectsId=60  and Datatype='database'   ORDER BY id ASC    ",
					   // "TotalRecordCount":1,"maxid":"1","Records":[{"id":"54","KeyName":"","Data":"20180307115042950010000D5","OwnerId":"113","Datatype":"database","IoTProjectsId":"60","IoTWidgetID":"1","title":"iFrogLabLoRa","APIKEY":"20180307115042950010"}]}
					   System.out.println(" URL: " +t1);
					   try {
						   JSONParser parser = new JSONParser();
					       JSONObject obj2 = (JSONObject)parser.parse(t1);
					       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
					       if(obj2.get("Result").toString().equals("OK")) { 
					    	       String t2=obj2.get("TotalRecordCount").toString();
					    	       if(t2.length()>0) {
					    	    	   		int tintTotalRecordCount = Integer.parseInt(t2);
					    	    	   		if (tintTotalRecordCount==0) return ""; 	  
						    	        JSONArray array = (JSONArray)obj2.get("Records");
							        JSONObject obj3 = (JSONObject)array.get(0);
							        return obj3.get("id").toString();    
					    	       }
					       }
					   } catch (ParseException e) {
						   e.printStackTrace();
					   }
					}
					// END 1, 如果要上傳到 Dashboard
				   return "";
			}
			 //List IoTWidgets
			public String data_ifroglab_dashboard_08_GetAPIKEY(String mDeviceID,String PersonalId,String IoTProjectsId) {                 // 打開Dashboard  帳戶
				   // BEGIN 1,確認是否有Dashboard  帳戶
				   if(mDeviceID.length()>0) {
					   String t1=FunHTTPPost(mURL[6], "IoTProjectsId="+IoTProjectsId+"&Datatype='database'" );
					   // {"Result":"OK","debug":"SELECT * FROM IoTWidgets WHERE IoTProjectsId=60  and Datatype='database'   ORDER BY id ASC    ",
					   // "TotalRecordCount":1,"maxid":"1","Records":[{"id":"54","KeyName":"","Data":"20180307115042950010000D5","OwnerId":"113","Datatype":"database","IoTProjectsId":"60","IoTWidgetID":"1","title":"iFrogLabLoRa","APIKEY":"20180307115042950010"}]}
					   System.out.println(" URL: " +t1);
					   try {
						   JSONParser parser = new JSONParser();
					       JSONObject obj2 = (JSONObject)parser.parse(t1);
					       System.out.println(obj2.get("Result"));  System.out.println(obj2.get("TotalRecordCount"));  
					       if(obj2.get("Result").toString().equals("OK")) { 
					    	       String t2=obj2.get("TotalRecordCount").toString();
					    	       if(t2.length()>0) {
					    	    	   		int tintTotalRecordCount = Integer.parseInt(t2);
					    	    	   		if (tintTotalRecordCount==0) return ""; 	  
						    	        JSONArray array = (JSONArray)obj2.get("Records");
							        JSONObject obj3 = (JSONObject)array.get(0);
							        return obj3.get("APIKEY").toString();    
					    	       }
					       }
					   } catch (ParseException e) {
						   e.printStackTrace();
					   }
					}
					// END 1, 如果要上傳到 Dashboard
				   return "";
			}
	public void data_ifroglab_dashboard() {                 // 打開Dashboard  帳戶
		if(mDeviceID.length()==0) {
			mDeviceID="010000D5";
		}
	    System.out.println(" mDeviceID: " +mDeviceID);
	    PersonId=data_ifroglab_dashboard_01_login(mDeviceID);
	    if(PersonId.length()==0) {
	    		return;
	    }else {
		   if(PersonId=="0") {  //有反應，但是沒有該用戶
			   // BEGIN 2,建立該Dashboard  帳戶
			   PersonId=data_ifroglab_dashboard_02_CreateAccount(mDeviceID);
			   if(PersonId.length()==0) {
				   return ;
			   }
			   // BEGIN 2,建立該Dashboard  帳戶
		   }
	    }
	    if(PersonId!="0") { // 建立project
	    		ProjectId=data_ifroglab_dashboard_03_ListProject(mDeviceID,PersonId);
	    		if(ProjectId=="0") {
	    			ProjectId=data_ifroglab_dashboard_04_CreateProject(mDeviceID,PersonId);
	    		}
	    		
	    		String IoTWidgetsId=data_ifroglab_dashboard_07_ListIoTWidgetsDB( mDeviceID, PersonId,ProjectId);
	    		if(IoTWidgetsId.length()==0) {
	    			String maxid=data_ifroglab_dashboard_05_ListIoTWidgets( mDeviceID, PersonId,ProjectId);
	    			int intmaxid = Integer.parseInt(maxid);
	    			maxid=Integer.toString( intmaxid+1);
	    			//String date = new SimpleDateFormat("yyyMMddHHmmssSSS").format(new Date());
	    			String date = new SimpleDateFormat("yyyMMddHH").format(new Date());
	    			APIKEY=date+FunRandomString(5);  //+mDeviceID;
	    			APIKEY=ProjectId+"-"+mDeviceID;
	    			IoTWidgetsId =data_ifroglab_dashboard_06_CreateIoTWidgets(mDeviceID,PersonId,ProjectId,maxid,APIKEY,APIKEY,"database");                // 打開Dashboard  帳戶
	    			System.out.println(IoTWidgetsId);
	    			/*
	    			//Begin 10 建立Google Chart
	    			String tData1="%7B%5E02%5Echarttype%5E02%5E%3A%5E02%5EArea%20Chart%5E02%5E%2C%5E02%5Edatasource%5E02%5E%3A%5E02%5EDatabaseRealTime%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5E201803140341310ZiL5%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5Edata%5E02%5E%3A%5E02%5E%257B%255E02%255EsleepTime%255E02%255E%253A%255E02%255E10%255E02%255E%252C%255E02%255Etimer%255E02%255E%253Afalse%252C%255E02%255Eapikey%255E02%255E%253A%255E02%255E201803140341310ZiL5%255E02%255E%252C%255E02%255EbashboardDatabaselist%255E02%255E%253A%255E02%255Ebyte0%255E02%255E%252C%255E02%255EDatabaseFieldlist%255E02%255E%253A%255E02%255E201803140341310ZiL5%255E02%255E%252C%255E02%255EData%255E02%255E%253A%255E02%255E%25255B%25255B%25255E01%25255EDatetime%25255E01%25255E%25252C%25255E01%25255Ebyte0%25255E01%25255E%25255D%25252C%25255B%25255E01%25255E2018-03-13%25252023%25253A33%25253A57%25255E01%25255E%25252C1%25255D%25255D%255E02%255E%257D%5E02%5E%7D";
	    			         //"%7B%5E02%5Echarttype%5E02%5E%3A%5E02%5EArea%20Chart%5E02%5E%2C%5E02%5Edatasource%5E02%5E%3A%5E02%5EDatabaseRealTime%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5E201803140333017jLvo%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5Edata%5E02%5E%3A%5E02%5E%257B%255E02%255EsleepTime%255E02%255E%253A%255E02%255E10%255E02%255E%252C%255E02%255Etimer%255E02%255E%253Afalse%252C%255E02%255Eapikey%255E02%255E%253A%255E02%255E201803140333017jLvo%255E02%255E%252C%255E02%255EbashboardDatabaselist%255E02%255E%253A%255E02%255Ebyte0%255E02%255E%252C%255E02%255EDatabaseFieldlist%255E02%255E%253A%255E02%255E201803140333017jLvo%255E02%255E%252C%255E02%255EData%255E02%255E%253A%255E02%255E%25255B%25255B%25255E01%25255EDatetime%25255E01%25255E%25252C%25255E01%25255Ebyte0%25255E01%25255E%25255D%25252C%25255B%25255E01%25255E2018-03-13%25252023%25253A33%25253A57%25255E01%25255E%25252C1%25255D%25255D%255E02%255E%257D%5E02%5E%7D";
	    			//String tData1="%7B%5E02%5Echarttype%5E02%5E%3A%5E02%5EArea%20Chart%5E02%5E%2C%5E02%5Edatasource%5E02%5E%3A%5E02%5EDatabaseRealTime%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5E161-010000D5%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5Edata%5E02%5E%3A%5E02%5E%257B%255E02%255EsleepTime%255E02%255E%253A%255E02%255E10%255E02%255E%252C%255E02%255Etimer%255E02%255E%253Afalse%252C%255E02%255Eapikey%255E02%255E%253A%255E02%255E161-010000D5%255E02%255E%252C%255E02%255EbashboardDatabaselist%255E02%255E%253A%255E02%255Ebyte0%255E02%255E%252C%255E02%255EDatabaseFieldlist%255E02%255E%253A%255E02%255E161-010000D5%255E02%255E%252C%255E02%255EData%255E02%255E%253A%255E02%255E%25255B%25255B%25255E01%25255EDatetime%25255E01%25255E%25252C%25255E01%25255Ebyte0%25255E01%25255E%25255D%25252C%25255B%25255E01%25255E2018-03-13%25252022%25253A05%25253A28%25255E01%25255E%25252C60%25255D%25252C%25255B%25255E01%25255E22%25253A05%25253A38%25255E01%25255E%25252C61%25255D%25252C%25255B%25255E01%25255E22%25253A05%25253A49%25255E01%25255E%25252C62%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A00%25255E01%25255E%25252C63%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A10%25255E01%25255E%25252C64%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A21%25255E01%25255E%25252C65%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A32%25255E01%25255E%25252C66%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A42%25255E01%25255E%25252C67%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A53%25255E01%25255E%25252C68%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A04%25255E01%25255E%25252C69%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A14%25255E01%25255E%25252C70%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A25%25255E01%25255E%25252C71%25255D%25252C%25255B%25255E01%25255E22%25253A08%25253A43%25255E01%25255E%25252C74%25255D%25252C%25255B%25255E01%25255E22%25253A08%25253A53%25255E01%25255E%25252C75%25255D%25252C%25255B%25255E01%25255E22%25253A09%25253A04%25255E01%25255E%25252C76%25255D%25252C%25255B%25255E01%25255E22%25253A09%25253A14%25255E01%25255E%25252C77%25255D%25255D%255E02%255E%257D%5E02%5E%7D";String tData1="%7B%5E02%5Echarttype%5E02%5E%3A%5E02%5EArea%20Chart%5E02%5E%2C%5E02%5Edatasource%5E02%5E%3A%5E02%5EDatabaseRealTime%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5E161-010000D5%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5Edata%5E02%5E%3A%5E02%5E%257B%255E02%255EsleepTime%255E02%255E%253A%255E02%255E10%255E02%255E%252C%255E02%255Etimer%255E02%255E%253Afalse%252C%255E02%255Eapikey%255E02%255E%253A%255E02%255E161-010000D5%255E02%255E%252C%255E02%255EbashboardDatabaselist%255E02%255E%253A%255E02%255Ebyte0%255E02%255E%252C%255E02%255EDatabaseFieldlist%255E02%255E%253A%255E02%255E161-010000D5%255E02%255E%252C%255E02%255EData%255E02%255E%253A%255E02%255E%25255B%25255B%25255E01%25255EDatetime%25255E01%25255E%25252C%25255E01%25255Ebyte0%25255E01%25255E%25255D%25252C%25255B%25255E01%25255E2018-03-13%25252022%25253A05%25253A28%25255E01%25255E%25252C60%25255D%25252C%25255B%25255E01%25255E22%25253A05%25253A38%25255E01%25255E%25252C61%25255D%25252C%25255B%25255E01%25255E22%25253A05%25253A49%25255E01%25255E%25252C62%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A00%25255E01%25255E%25252C63%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A10%25255E01%25255E%25252C64%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A21%25255E01%25255E%25252C65%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A32%25255E01%25255E%25252C66%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A42%25255E01%25255E%25252C67%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A53%25255E01%25255E%25252C68%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A04%25255E01%25255E%25252C69%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A14%25255E01%25255E%25252C70%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A25%25255E01%25255E%25252C71%25255D%25252C%25255B%25255E01%25255E22%25253A08%25253A43%25255E01%25255E%25252C74%25255D%25252C%25255B%25255E01%25255E22%25253A08%25253A53%25255E01%25255E%25252C75%25255D%25252C%25255B%25255E01%25255E22%25253A09%25253A04%25255E01%25255E%25252C76%25255D%25252C%25255B%25255E01%25255E22%25253A09%25253A14%25255E01%25255E%25252C77%25255D%25255D%255E02%255E%257D%5E02%5E%7D";
	    			tData1="%7B%5E02%5Echarttype%5E02%5E%3A%5E02%5EArea%20Chart%5E02%5E%2C%5E02%5Edatasource%5E02%5E%3A%5E02%5EDatabaseRealTime%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5E20180314040924h7Rjd%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5Edata%5E02%5E%3A%5E02%5E%257B%255E02%255EsleepTime%255E02%255E%253A%255E02%255E10%255E02%255E%252C%255E02%255Etimer%255E02%255E%253Afalse%252C%255E02%255Eapikey%255E02%255E%253A%255E02%255E20180314040924h7Rjd%255E02%255E%252C%255E02%255EbashboardDatabaselist%255E02%255E%253A%255E02%255E20180314040924h7Rjd%255E02%255E%252C%255E02%255EDatabaseFieldlist%255E02%255E%253A%255E02%255Ebyte0%255E02%255E%252C%255E02%255EData%255E02%255E%253A%255E02%255E%25255B%25255B%25255E01%25255EDatetime%25255E01%25255E%25252C%25255E01%25255Ebyte0%25255E01%25255E%25255D%25252C%25255B%25255E01%25255E2018-03-14%25252005%25253A09%25253A40%25255E01%25255E%25252C1%25255D%25255D%255E02%255E%257D%5E02%5E%7D";
	    			

	    			String tData2=tData1.replace("20180314040924h7Rjd",APIKEY);
	    			maxid=maxid+1;
	    			IoTWidgetsId =data_ifroglab_dashboard_06_CreateIoTWidgets(mDeviceID,PersonId,ProjectId,maxid,"",tData2,"chart");                // 打開Dashboard  帳戶
	    			System.out.println(IoTWidgetsId);
	    			*/
// {"charttype":"Area Chart","datasource":"DatabaseRealTime","bashboardDatabaselist":"201803140333017jLvo","DatabaseFieldlist":"byte0","DatabaseFieldlist":"byte0","data":"{"sleepTime":"10","timer":false,"apikey":"201803140333017jLvo","bashboardDatabaselist":"byte0","DatabaseFieldlist":"201803140333017jLvo","Data":"%5B%5B%5E01%5EDatetime%5E01%5E%2C%5E01%5Ebyte0%5E01%5E%5D%2C%5B%5E01%5E2018-03-13%2023%3A33%3A57%5E01%5E%2C1%5D%5D"}"}
// {"charttype":"Area Chart","datasource":"DatabaseRealTime","bashboardDatabaselist":"201803140341310ZiL5","DatabaseFieldlist":"byte0","DatabaseFieldlist":"byte0","data":"%7B%5E02%5EsleepTime%5E02%5E%3A%5E02%5E10%5E02%5E%2C%5E02%5Etimer%5E02%5E%3Afalse%2C%5E02%5Eapikey%5E02%5E%3A%5E02%5E201803140341310ZiL5%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5E201803140341310ZiL5%5E02%5E%2C%5E02%5EData%5E02%5E%3A%5E02%5E%255B%255B%255E01%255EDatetime%255E01%255E%252C%255E01%255Ebyte0%255E01%255E%255D%252C%255B%255E01%255E2018-03-13%252023%253A33%253A57%255E01%255E%252C1%255D%255D%5E02%5E%7D"}
	    			/*
	    			{"charttype":"Area Chart","datasource":"DatabaseRealTime","bashboardDatabaselist":"60-010000D5","DatabaseFieldlist":"byte0","DatabaseFieldlist":"byte0",
	    				"data":"{"sleepTime":"10","timer":false,"apikey":"60-010000D5","bashboardDatabaselist":"byte0","DatabaseFieldlist":"60-010000D5","
	    				+ ""Data":"%5B%5B%5E01%5EDatetime%5E01%5E%2C%5E01%5Ebyte0%5E01%5E%5D%2C%5B%5E01%5E2018-03-14%2005%3A01%3A25%5E01%5E%2C1%5D%5D"}"}
	    			*/
	    			
	    			//(243, '', '%7B%5E02%5Echarttype%5E02%5E%3A%5E02%5EArea%20Chart%5E02%5E%2C%5E02%5Edatasource%5E02%5E%3A%5E02%5EDatabaseRealTime%5E02%5E%2C%5E02%5EbashboardDatabaselist%5E02%5E%3A%5E02%5E161-010000D5%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5EDatabaseFieldlist%5E02%5E%3A%5E02%5Ebyte0%5E02%5E%2C%5E02%5Edata%5E02%5E%3A%5E02%5E%257B%255E02%255EsleepTime%255E02%255E%253A%255E02%255E10%255E02%255E%252C%255E02%255Etimer%255E02%255E%253Afalse%252C%255E02%255Eapikey%255E02%255E%253A%255E02%255E161-010000D5%255E02%255E%252C%255E02%255EbashboardDatabaselist%255E02%255E%253A%255E02%255Ebyte0%255E02%255E%252C%255E02%255EDatabaseFieldlist%255E02%255E%253A%255E02%255E161-010000D5%255E02%255E%252C%255E02%255EData%255E02%255E%253A%255E02%255E%25255B%25255B%25255E01%25255EDatetime%25255E01%25255E%25252C%25255E01%25255Ebyte0%25255E01%25255E%25255D%25252C%25255B%25255E01%25255E2018-03-13%25252022%25253A05%25253A28%25255E01%25255E%25252C60%25255D%25252C%25255B%25255E01%25255E22%25253A05%25253A38%25255E01%25255E%25252C61%25255D%25252C%25255B%25255E01%25255E22%25253A05%25253A49%25255E01%25255E%25252C62%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A00%25255E01%25255E%25252C63%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A10%25255E01%25255E%25252C64%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A21%25255E01%25255E%25252C65%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A32%25255E01%25255E%25252C66%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A42%25255E01%25255E%25252C67%25255D%25252C%25255B%25255E01%25255E22%25253A06%25253A53%25255E01%25255E%25252C68%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A04%25255E01%25255E%25252C69%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A14%25255E01%25255E%25252C70%25255D%25252C%25255B%25255E01%25255E22%25253A07%25253A25%25255E01%25255E%25252C71%25255D%25252C%25255B%25255E01%25255E22%25253A08%25253A43%25255E01%25255E%25252C74%25255D%25252C%25255B%25255E01%25255E22%25253A08%25253A53%25255E01%25255E%25252C75%25255D%25252C%25255B%25255E01%25255E22%25253A09%25253A04%25255E01%25255E%25252C76%25255D%25252C%25255B%25255E01%25255E22%25253A09%25253A14%25255E01%25255E%25252C77%25255D%25255D%255E02%255E%257D%5E02%5E%7D', 0, 'chart', 161, 2, 'Name', ''),

	    			
	    			//End   10 建立Google Chart
	    		}
	    		APIKEY=data_ifroglab_dashboard_08_GetAPIKEY(mDeviceID, PersonId,ProjectId);

	    		FunPreferencesSave("APIKEY",APIKEY);
	    		FunPreferencesSave("ProjectId",ProjectId);
	    		FunPreferencesSave("mDeviceID",mDeviceID);
	    }
	}
	public void ui_Step1(Frame mainFrame) {  
	   // Begin 01,                添加Step 1 的文字 "Step1"
	   Label l1 = new Label();
	   l1.setAlignment(Label.LEFT);
	   l1.setText(mStr[0][lan]);    //"Step1"
	   l1.setSize(80,30);
	   mainFrame.add(l1);  
	   l1.setLocation(10, 20+tUITop);
	   // END 01
	
	   // Begin 2,                 添加Step 1 的文字 "Select LoRa device"
	   Label l2 = new Label();
	   l2.setAlignment(Label.LEFT);
	   l2.setText(mStr[1][lan]);     // "Select LoRa device"
	   l2.setSize(140,30);
	   mainFrame.add(l2);  
	   l2.setLocation(10, 40+tUITop);
	   // END 2
	   
	   // Begin 03,               添加Step 1  Com 設備列表 Choice, combo box
	   mUIComPortName=new Choice();  
	   mUIComPortName.setBounds(100,100, 250,30);  
	   mUIComPortName.add(mStr[1][lan]);                   //"No Com Port Devices"
	   mainFrame.add(mUIComPortName);  
	   mUIComPortName.setLocation(155, 25+tUITop+(mUIComPortName.size().height/2));
	   mUIComPortName.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				// 顯示選取Com port 設備：
				String data = mStr[21][lan]+ mUIComPortName.getItem(mUIComPortName.getSelectedIndex());
				labelMessage.setText(data);	
				ui_Setp1_OpenComPort();                    //連接LoRa COM ports 設備
			}
	   });
	
	   // END 02
	   
	   // Begin 03, 添加Step 1  更新的按鈕 
	   Button refreshButton = new Button(mStr[2][lan]);    //"reflash"
	   refreshButton.setBounds(100,100, 130,30); 
	   mainFrame.add(refreshButton);
	   //mUIComPortName.setBounds(100,100, 150,75); 
	   refreshButton.setLocation(410,25+tUITop+(refreshButton.size().height/2));
	   refreshButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
  			ui_Setp1_listPorts();                                  // 找出LoRa USB COM ports 設備
	      }
	   });
	   // END 03
	   // Begin 04,                 添加Step 1 的文字 "狀態ˋ"
	   labelMessage = new Label();
	   labelMessage.setAlignment(Label.LEFT);
	   labelMessage.setText("Status:");     // "Select LoRa device"
	   labelMessage.setSize(mainFrame.size().width-250,25);
	   labelMessage.setBackground(Color.lightGray );
	   mainFrame.add(labelMessage);  
	   int y= mainFrame.size().height-25;
	   if(mloralib.OS.startsWith("Windows")==true)  { 
		   y=442;
	   }
	   labelMessage.setLocation(0,y);// mainFrame.size().height-25);
	   // END 4   
	    // Begin 05,                 添加Step 1 的文字 "LoRa 是否打開ˋ"
	   labelLoRaStatus = new Label();
	   labelLoRaStatus.setAlignment(Label.RIGHT);
	   labelLoRaStatus.setText(mStr[27][lan]);     //" LoRa關閉中"
	   labelLoRaStatus.setSize(250,25);
	   labelLoRaStatus.setBackground(Color.lightGray );
	   mainFrame.add(labelLoRaStatus);  
	   y= mainFrame.size().height-25;
	   if(mloralib.OS.startsWith("Windows")==true)  { 
		   y=442;
	   }
	   labelLoRaStatus.setLocation(mainFrame.size().width-250, y); //mainFrame.size().height-25);
	   // END 5   
	   // Begin 5,                 添加Step 1 一條區分線
	   Label lineLabel = new Label();
	   lineLabel.setAlignment(Label.LEFT);
	   lineLabel.setText("");     // "Select LoRa device"
	   lineLabel.setSize(mainFrame.size().width,1);
	   lineLabel.setBackground(Color.lightGray );
	   mainFrame.add(lineLabel);  
	   lineLabel.setLocation(0, 75+tUITop);
	   // END 5
	   // 基本設定
	   ui_Setp1_listPorts();                                  // 找出LoRa USB COM ports 設備
	   
	}
	
		@SuppressWarnings("deprecation")
		public void ui_Step2(Frame mainFrame) {
			  int y=60;
		      // Begin 01,                添加Step 2 的文字 "Step1"
		      Label l1 = new Label();
		      l1.setAlignment(Label.LEFT);
		      l1.setText(mStr[5][lan]);    //"Step1"
		      l1.setSize(90,25);
		      mainFrame.add(l1);  
		      l1.setLocation(10, y+20+tUITop);
		      // END 01
	
		      // Begin 2,                 添加Step 2 的文字 "Select LoRa device"
		      Label l2 = new Label();
		      l2.setAlignment(Label.LEFT);
		      l2.setText(mStr[6][lan]);     // "Seup LoRa device"
		      l2.setSize(100,30);
		      mainFrame.add(l2);  
		      l2.setLocation(10, y+40+tUITop);
		      // END 2
		      // Begin 3,                 添加Step 2 的文字 "設備狀態"
		      mLabelStatus= new Label();
		      mLabelStatus.setAlignment(Label.LEFT);
		      mLabelStatus.setText(mStr[7][lan]+mStr[8][lan]);     // "設備狀態"
		      mLabelStatus.setSize(400,25);
		      mainFrame.add(mLabelStatus);  
		      mLabelStatus.setLocation(130, y+20+tUITop);
		      // END 3
		      // Begin 4,                 添加Step 2 的文字 MAC ID:
		      mLabelID = new Label();
		      mLabelID.setAlignment(Label.LEFT);
		      mLabelID.setText(mStr[9][lan]); //"MAC Address:");     // MAC ID:
		      mLabelID.setSize(400,25);
		      mainFrame.add(mLabelID);  
		      mLabelID.setLocation(300, y+40+tUITop);
		      // END 4
		      // Begin 5,                 添加Step 2 的文字 Fireware Version
		      mLabelFireware = new Label();
		      mLabelFireware.setAlignment(Label.LEFT);
		      mLabelFireware.setText(mStr[10][lan]); //"MAC Address:");     // Fireware Version
		      mLabelFireware.setSize(400,25);
		      mainFrame.add(mLabelFireware);  
		      mLabelFireware.setLocation(130, y+40+tUITop);
		      // END 5
		      // Begin 06,               添加Step 2  Com 設備列表
		      final Choice LoRaModeChoice=new Choice();  
		      LoRaModeChoice.setBounds(100,100, 170,30);  
		      LoRaModeChoice.add(mStr[11][lan]);             
		      LoRaModeChoice.add(mStr[12][lan]);             
		      LoRaModeChoice.add(mStr[13][lan]); 
		      mainFrame.add(LoRaModeChoice);  
		      LoRaModeChoice.setLocation(125, y+50+tUITop+(LoRaModeChoice.size().height/2));
		      LoRaModeChoice.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
		            String data =mStr[22][lan]
		            + LoRaModeChoice.getItem(LoRaModeChoice.getSelectedIndex());
		            labelMessage.setText(data);	
				}
		      });
		      // END 06
		     
		      // Begin 08, 添加Step 2  啟動LoRa的按鈕 
		      final Button startButton = new Button(mStr[24][lan]);    //"reflash"
		      startButton.setBounds(100,100, 115,30); 
		      mainFrame.add(startButton);
		      startButton.setLocation(290,y+50+(startButton.size().height/2+tUITop));
		      startButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {        // 當按下[步驟2；啟動LoRa 按鍵] 
		        	 if(mThreadRecevieText!=null) {   
		        	 //if(mDeviceID.length()>0){                 // 如果已經開啟LoRa 設備的話
			        	 labelMessage.setText(mStr[25][lan]); //  按鈕顯示「開啟LoRa」　
			        	 labelLoRaStatus.setText(mStr[27][lan]); //LoRa未開啟
			        	 startButton.setLabel(mStr[24][lan]);
			        //	 mloralib.serial_serialEvent_Close();
			        	 mDeviceID="";
			        	 TreadStop();					  	  // 關閉　LoRa　Reciver 資料
			        	 mloralib.FunLora_close();
  			         mLabelStatus.setText(mStr[7][lan]+mStr[8][lan]);     // "設備狀態"
				     mLabelID.setText(mStr[9][lan]);                      // MAC ID:
					 mLabelFireware.setText(mStr[10][lan]);               // Fireware Version
		        	 }else{                                   // 如果沒有開啟LoRa 設備的話
			        	 labelMessage.setText(mStr[24][lan]); // 按鈕顯示「關閉LoRa」
			        	 mDeviceID=mloralib.GetDeviceID();
			        	 if(mDeviceID.length()>0){
			        		 TreadStop();					  	  // 關閉　LoRa　Reciver 資料
			        		 startButton.setLabel(mStr[25][lan]);  // 成功的話，就改變按鈕的文字為「關閉LoRa」
			        		 String StringID=Integer.toString(mloralib.GetFirmwareVersion());
				        	 labelLoRaStatus.setText(mStr[26][lan]+mDeviceID+","+mStr[10][lan]+":"+StringID ); //LoRa使用中
					         mLabelStatus.setText(mStr[26][lan]+mDeviceID+","+mStr[10][lan]+":"+StringID);     // "設備狀態"
							 mLabelID.setText(mStr[9][lan]+mDeviceID);                      // MAC ID:
							 mLabelFireware.setText(mStr[10][lan]+StringID);               // Fireware Version
							 mloralib.ReadMode(Freq1, Freq2, Freq3, Power);  //{ TXRX,0x01,0x65,0x6c,0x3);             // 設定LoRa為讀取模式	 
			        		 TreadStart();			    		  	  // 打開　LoRa　Reciver 資料
				        	 labelMessage.setText(mStr[44][lan]); // 按鈕顯示「LoRa工作中」
				      	 data_ifroglab_dashboard();           // 打開Dashboard  帳戶
			        	 }else {
			        		 String tMsg= mUIComPortName.getItem(mUIComPortName.getSelectedIndex())+mStr[43][lan];
			        		 labelMessage.setText(tMsg); // 這不是iFrogLab LoRa 設備
			                 MsgDialog aboutDialog = new MsgDialog(mainFrame,tMsg,500,100 );
			                 aboutDialog.setVisible(true);
			        	 }
		        	 }
		         }
		      });
		      // END 08
		      // Begin 07, 添加Step 2  「更多設定」的按鈕 
		      Button preferencesButton = new Button(mStr[14][lan]);    //"Preference"
		      preferencesButton.setBounds(100,100, 130,30); 
		      mainFrame.add(preferencesButton);
		      preferencesButton.setLocation(410,y+50+tUITop+(preferencesButton.size().height/2));
		      preferencesButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 	labelMessage.setText(mStr[12][lan]);  
		        	 	preference mpreference=new preference(ifroglabloraClass);		                
		         }
		      });
		      // END 07
		      
		      
		      /*
		      // Begin 10 添加Step 2  LoRa的送資料的測試按鈕 
		      final Button sendAlotOfDatastartButton = new Button(mStr[42][lan]);    //"reflash"
		      sendAlotOfDatastartButton.setBounds(30,70, 115,30); 
		      mainFrame.add(sendAlotOfDatastartButton);
		      sendAlotOfDatastartButton.setLocation(504,y+50+(startButton.size().height/2+tUITop));
		      sendAlotOfDatastartButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {        // 當按下[步驟2；啟動LoRa 按鍵] 
		        	 	//TreadStop();					  	  // 關閉　LoRa　Reciver 資料
		        	 	mloralib.WriteMode(  Freq1, Freq2, Freq3, Power);  //{ TXRX,0x01,0x65,0x6c,0x3);             // 設定LoRa為讀取模式
					byte tCounter=0;
		        	 	for(int i=0;i<=5;i++) {
	    		        	   try {
		    		        	 byte[] data={tCounter,2,3,4};
		    		        	 mloralib.FunLora_5_write16bytesArray(data);
		    		        	 tCounter++;
						 TimeUnit.SECONDS.sleep(1);
					   } catch (InterruptedException e1) {
						 e1.printStackTrace();
					   }
					}
		         }
		      });
		      // END 08
		      */
		    
		      
		      
		      // Begin 9,                 添加Step 1 一條區分線
		      Label lineLabel = new Label();
		      lineLabel.setAlignment(Label.LEFT);
		      lineLabel.setText("");     // "Select LoRa device"
		      lineLabel.setSize(mainFrame.size().width,1);
		      lineLabel.setBackground(Color.lightGray );
		      mainFrame.add(lineLabel);  
		      lineLabel.setLocation(0, y+100+tUITop);
		      // END 9
		      
		      
		      
		}
		public void ui_Step3(Frame mainFrame) {
			 int y=140;
		     // Begin 01,                添加Step 3 的文字 "Step3"
		     Label l1 = new Label();
		     l1.setAlignment(Label.LEFT);
		     l1.setText(mStr[15][lan]);    //"Step3"
		     l1.setSize(100,25);
		     mainFrame.add(l1);  
		     l1.setLocation(10, y+30+tUITop);
		      // END 01
		      // Begin 2,                 添加Step 3 的文字 "傳送的資料"
		      Label l2 = new Label();
		      l2.setAlignment(Label.LEFT);
		      l2.setText(mStr[19][lan]);     // "傳送的資料"
		      l2.setSize(100,30);
		      mainFrame.add(l2);  
		      l2.setLocation(10, y+50+tUITop);
		      // END 2
		      
		      // Begin 03,               添加Step 3  Text (ASCII)  , Hex  ,File
		      ChoiceKind=new Choice();  
		      ChoiceKind.setBounds(100,100, 180,30);  
		      ChoiceKind.add(mStr[33][lan]);             
		      ChoiceKind.add(mStr[34][lan]);             
		      ChoiceKind.add(mStr[35][lan]);             
		      //ChoiceKind.add(mStr[36][lan]); 
		      mainFrame.add(ChoiceKind);  
		      ChoiceKind.setLocation(125, y+35+(ChoiceKind.size().height/2+tUITop));
		      ChoiceKind.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
		            String data = "Setup: " 
		            + ChoiceKind.getItem(ChoiceKind.getSelectedIndex());
		            labelMessage.setText(data);	
				}
		      });
		      // END 03
		      // Begin 04, 添加Step 3 「送出」的按鈕 
		      Button buttonSend = new Button(mStr[20][lan]);    
		      buttonSend.setBounds(100,100, 120,30); 
		      mainFrame.add(buttonSend);
		      buttonSend.setLocation(310,y+35+(buttonSend.size().height/2+tUITop));
		      buttonSend.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        		 TreadStop();
		        		 labelMessage.setText(mStr[3][lan]); 
		        		 mloralib.WriteMode(Freq1, Freq2, Freq3, Power); 
		        	     String tSendTextString=sendText.getText();
		        	     if(ChoiceKind.getSelectedIndex()==0) {   // 送文字
			        	     int len=tSendTextString.length();
	     	    	 		 if(len>16) len=16;
		        	    	 	byte[] data = tSendTextString.getBytes(StandardCharsets.UTF_8); //UTF-8
		        	    	 	mloralib.FunLora_5_write16bytesArray(data,len);
		        	    	 	try {
								TimeUnit.MILLISECONDS.sleep(10);
		        	    	 	} catch (InterruptedException e1) {
								e1.printStackTrace();
						}
		        	     }else if(ChoiceKind.getSelectedIndex()==1) {   // 送HEX
			        	    	 try {
			        	    	 	String[] parts = tSendTextString.split(",");
			        	    	 	int len1=parts.length;
					        	     int len=tSendTextString.length();
			     	    	 		 if(len1>16) len1=16;
			        	    	 	byte[] data= new byte[len1];//= {0,0,0,0,0, 0,0,0,0,0,  0,0,0,0,0,  0,0,0,0,0};
			        	    	 	for(int i=0;i<len1;i++) {
			        	    	 		data[i]=(byte)Long.parseLong(parts[i], 16);
			        	    	 	}
			        	    	 	mloralib.FunLora_5_write16bytesArray(data,len1);
			        	    	 }
			        	    	 catch (NumberFormatException n){
			        	     } 
		        	     }else if(ChoiceKind.getSelectedIndex()==2) {   // 送OCX
		        	    	 try {
		        	    	 	String[] parts = tSendTextString.split(",");
		        	    	 	int len1=parts.length;
				        	     int len=tSendTextString.length();
		     	    	 		 if(len1>16) len1=16;
		        	    	 	byte[] data= new byte[len1];
		        	    	 	for(int i=0;i<len1;i++) {
		        	    	 		data[i]=(byte)Long.parseLong(parts[i], 10);
		        	    	 	}
		        	    	 	mloralib.FunLora_5_write16bytesArray(data,len1);
		        	    	 }
		        	    	 catch (NumberFormatException n){
		        	     }    	 
		        	     }
		        	     mloralib.ReadMode(Freq1, Freq2, Freq3, Power); 
		        	     TreadStart();
		         }
		      });
		      // END 04
		     // Begin 04,                添加Step 3 的文字 "Step1"
		     // Label l2 = new Label();
		     sendText = new TextArea("",5,30);		     
		     sendText.setText(mStr[19][lan]);    //"Step3"
		     sendText.setSize(580,80);
		     mainFrame.add(sendText);  
		     sendText.setLocation(10, y+80+tUITop);
		      // END 04
		     // Begin 03,               添加Step 3  Text (ASCII)  , Hex  ,File
		     mChoiceRecevieDisplay=new Choice();  
		     mChoiceRecevieDisplay.setBounds(100,100, 180,30);  
		     mChoiceRecevieDisplay.add(mStr[33][lan]);             
		     mChoiceRecevieDisplay.add(mStr[34][lan]);             
		     mChoiceRecevieDisplay.add(mStr[35][lan]);             
		     // mChoiceRecevieDisplay.add(mStr[36][lan]); 
		      mainFrame.add(mChoiceRecevieDisplay);  
		      mChoiceRecevieDisplay.setLocation(125, y+160+tUITop);
		      mChoiceRecevieDisplay.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
		            String data = "Recevie: " 
		            + mChoiceRecevieDisplay.getItem(mChoiceRecevieDisplay.getSelectedIndex());
		            labelMessage.setText(data);	
		            recevieText.setText("");
		            TreadStop();
		            TreadStart();
				}
		      });
		      // END 03
		      
		      // Begin 04, 添加Step 3 「清除」的按鈕 
		      Button buttonClear = new Button(mStr[31][lan]);    
		      buttonClear.setBounds(100,100, 120,30); 
		      mainFrame.add(buttonClear);
		      buttonClear.setLocation(310,y+160+tUITop);
		      buttonClear.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 labelMessage.setText(mStr[31][lan]); 
		   		     recevieText.setText("");    // 「清除」 添加Step 3 顯示 接收的資料
		        	    
		         }
		      });
		      // END 04
		      
		      // Begin 2,  添加Step 3 「接收資料」的文字 
		      Label l3 = new Label();
		      l3.setAlignment(Label.LEFT);
		      l3.setText(mStr[32][lan]);     // "Select LoRa device"
		      l3.setSize(100,30);
		      mainFrame.add(l3);  
		      l3.setLocation(10, y+160+tUITop);
		      // END 2
		     // Begin 05,                添加Step 3 顯示 接收的資料
		     recevieText = new TextArea("",5,30);
		     recevieText.setText(mStr[55][lan]);    
		     recevieText.setSize(580,80);
		     mainFrame.add(recevieText);  
		     recevieText.setLocation(10, y+190+tUITop);
		     // END 05	     
		     /*
		     // Begin 2,  添加Step 3 「接收資料」的文字 
		     Label l4 = new Label();
		     l4.setAlignment(Label.LEFT);
		     l4.setText(mStr[55][lan]);     // "Select LoRa device"
		     l4.setSize(580,30);
		     mainFrame.add(l4);  
		     l4.setLocation(10, y+190+75+tUITop);
		     // END 2
		     */
		     
		     /*
  			 // Begin 06, 上傳到網路的選項 
		     checkBoxUpload = new Checkbox();
		     checkBoxUpload.setLabel(mStr[45][lan]);
		     checkBoxUpload.setSize(170,30);
  			 mainFrame.add(checkBoxUpload);
  			 checkBoxUpload.setLocation(10, y+190+75+tUITop);
  			 // END 06	
  			 // Begin 07, 打開儀表版的按鈕 
  		     Button ButtonIoT = new Button(mStr[46][lan]);    //"reflash"
  		     ButtonIoT.setBounds(100,100, 140,30); 
  		     ButtonIoT.setLocation(320+130, y+190+75+tUITop);
  		     mainFrame.add(ButtonIoT);
  		     //mUIComPortName.setBounds(100,100, 150,75); 
  		     //ButtonIoT.setLocation(300,y+190+tUITop);
  		     ButtonIoT.addActionListener(new ActionListener() {
  		        public void actionPerformed(ActionEvent e) {
  	  			  ui_Setp1_listPorts();                                  // 找出LoRa USB COM ports 設備
  		        }
  		     });
  		     // END 07
		     // Begin 08, 網址 
  		     TextFieldUrl=new TextField(mStr[48][lan]);  
  		     TextFieldUrl.setBounds(280,150, 270,30);  
  		     mainFrame.add(TextFieldUrl);
  		     TextFieldUrl.setLocation(180, y+190+75+tUITop);
   		     // END 08

			 // Begin 06, 上傳到網路的選項 
		     checkBoxSaveLog = new Checkbox();
		     checkBoxSaveLog.setLabel(mStr[47][lan]);
		     checkBoxSaveLog.setSize(110,30);
			 mainFrame.add(checkBoxSaveLog);
			 checkBoxSaveLog.setLocation(10, y+190+75+30+tUITop);
			 // END 06	
			 		 
			 // Begin 07, 選檔案按鈕
			 StringLogFileName=FunPreferencesLoad("StringLogFileName","iFrogLab.csv");
			 File f = new File(StringLogFileName);
			 //System.out.println(f.getName());
			 ButtonLogFileName = new Button(f.getName());   
			 ButtonLogFileName.setBounds(100,100, 140,30); 
	  		 mainFrame.add(ButtonLogFileName);
			 ButtonLogFileName.setLocation(120,y+190+75+30+tUITop);
	  		 ButtonLogFileName.addActionListener(new ActionListener() {
	  		     public void actionPerformed(ActionEvent e) {
	  	  			 //ui_Setp1_listPorts();                                  // 找出LoRa USB COM ports 設備
	  				 // Begin 08, 選檔案
					 JFileChooser fileChooser = new JFileChooser();
					 fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
					 //int result = fileChooser.showOpenDialog(this); //parent);  //Show up the dialog
					 int result = fileChooser.showSaveDialog(null);
					 if (result == JFileChooser.APPROVE_OPTION) {
						    // user selects a file
						    File selectedFile = fileChooser.getSelectedFile();
						    StringLogFileName=selectedFile.getAbsolutePath();
						    String LogFileName2=selectedFile.getName();
						    System.out.println("Selected file: " +StringLogFileName );
						    FunPreferencesSave("StringLogFileName",StringLogFileName);
						    ButtonLogFileName.setLabel(LogFileName2);
					 }
					 // END 08, 選檔案
	  		    }
	  		  });
			  // END 07	
			   */
		}
		   //02 begin  menu
		   // 下拉式選
		   private void showMenu(){
			   
		      //create a menu bar
		      final MenuBar menuBar = new MenuBar();

		      //create menus
		      Menu fileMenu = new Menu("File");
		      Menu editMenu = new Menu("Edit"); 
		      final Menu aboutMenu = new Menu("iFrogLab");
		      
		      //create menu items
		      MenuItem newMenuItem = new MenuItem("New",new MenuShortcut(KeyEvent.VK_N));
		      newMenuItem.setActionCommand("New");
		      MenuItem openMenuItem = new MenuItem("Open");
		      openMenuItem.setActionCommand("Open");
		      MenuItem saveMenuItem = new MenuItem("Save");
		      saveMenuItem.setActionCommand("Save");
		      MenuItem exitMenuItem = new MenuItem("Exit");
		      exitMenuItem.setActionCommand("Exit");
		      MenuItem cutMenuItem = new MenuItem("Cut");
		      cutMenuItem.setActionCommand("Cut");
		      MenuItem copyMenuItem = new MenuItem("Copy");
		      copyMenuItem.setActionCommand("Copy");
		      MenuItem pasteMenuItem = new MenuItem("Paste");
		      pasteMenuItem.setActionCommand("Paste");
		      MenuItem aboutMenuItem = new MenuItem("About");
		      aboutMenuItem.setActionCommand("about iFrogLab");
		      MenuItemListener menuItemListener = new MenuItemListener();
		      newMenuItem.addActionListener(menuItemListener);
		      openMenuItem.addActionListener(menuItemListener);
		      saveMenuItem.addActionListener(menuItemListener);
		      exitMenuItem.addActionListener(menuItemListener);
		      cutMenuItem.addActionListener(menuItemListener);
		      copyMenuItem.addActionListener(menuItemListener);
		      pasteMenuItem.addActionListener(menuItemListener);
		      aboutMenu.addActionListener(menuItemListener);
	          
		      final CheckboxMenuItem showWindowMenu = 
		         new CheckboxMenuItem("Show About", true);
		      showWindowMenu.addItemListener(new ItemListener() {
		         public void itemStateChanged(ItemEvent e) {
		            if(showWindowMenu.getState()){
		               menuBar.add(aboutMenu);
		            }else{
		               menuBar.remove(aboutMenu);
		            }
		         }
		      });

		      //add menu items to menus
		      fileMenu.add(newMenuItem);
		      fileMenu.add(openMenuItem);
		      fileMenu.add(saveMenuItem);
		      fileMenu.addSeparator();
		      fileMenu.add(showWindowMenu);
		      fileMenu.addSeparator();
		      fileMenu.add(exitMenuItem);

		      editMenu.add(cutMenuItem);
		      editMenu.add(copyMenuItem);
		      editMenu.add(pasteMenuItem);
		      
		      aboutMenu.add(aboutMenuItem);
		      menuBar.add(aboutMenu);

		      //add menubar to the frame
		      
		      mainFrame.setMenuBar(menuBar);
		      mainFrame.setVisible(true);  
		      
		   }

		   class MenuItemListener implements ActionListener {
		      public void actionPerformed(ActionEvent e) {            
		    	  labelMessage.setText(e.getActionCommand() 
		            + " MenuItem clicked.");
		    	  if (Desktop.isDesktopSupported()) {
		    		    try {
							Desktop.getDesktop().browse(new URI("http://www.ifroglab.com/"));
						} catch (IOException e1) {e1.printStackTrace();
						} catch (URISyntaxException e1) {	e1.printStackTrace();}
		    	  }
		    	  
		    	  
		    	  
		      }    
		   }
		   //02 end menu		
	// The entry main() method
		   
	
	private ThreadRecevieText mThreadRecevieText;	
	@SuppressWarnings("deprecation")
	public void TreadStop(){
		if(mThreadRecevieText!=null){
			mThreadRecevieText.stop();
			mThreadRecevieText=null;
		}
	}
	@SuppressWarnings("deprecation")
	public void TreadStart(){
		if(mThreadRecevieText!=null){
			mThreadRecevieText.stop();
			mThreadRecevieText=null;
		}
		
		mThreadRecevieText = new ThreadRecevieText(
				  labelMessage,
				// checkBoxUpload,
				//  TextFieldUrl,checkBoxSaveLog,
				  recevieText,mloralib,mChoiceRecevieDisplay,ifroglabloraClass);
		mThreadRecevieText.start();
	
	}
	public static void main(String[] args) {
	   // Invoke the constructor to setup the GUI, by allocating an instance
		ifroglablora app = new ifroglablora();
	      // or simply "new AWTCounter();" for an anonymous instance   
	}
	

	/////////////////////////////////////////
/////////////////////////////////////////
	
		public void FunPreferencesSave(String PREF_NAME,String newValue) {
			Preferences prefs = Preferences.userNodeForPackage(com.ifroglab.lora.ifroglablora.class);
			prefs.put(PREF_NAME, newValue);
		}
		public String FunPreferencesLoad(String PREF_NAME,String defaultValue) {
			Preferences prefs = Preferences.userNodeForPackage(com.ifroglab.lora.ifroglablora.class);
			String propertyValue = prefs.get(PREF_NAME,defaultValue); // "a string"
			return propertyValue;
		}
	class ThreadRecevieText extends Thread
	{ 
	  public ThreadRecevieText(
			  Label ilabelMessage,
			  // Checkbox icheckBoxUpload,
			  // TextField iTextFieldUrl,Checkbox icheckBoxSaveLog,
			  TextArea iComRrecevieText,loralib iloralib,Choice iChoiceRecevieDisplay,ifroglablora imLo)
	  { 
		  delaytime=500;
		  mlabelMessage=ilabelMessage;
		  mRrecevieText = iComRrecevieText;
	      generator = new Random();
	      threadloralib=iloralib;
          mloralib.ReadMode(Freq1, Freq2, Freq3, Power); 
          mChoiceRecevieDisplay=iChoiceRecevieDisplay; //.getSelectedIndex();
          mLo=imLo;
	  }

	  public void run()
	  { 
	   	  int counter=0;
	   try
	   {
	     while (!interrupted())
	     { 
			long tCounter=mloralib.FunLora_7_counter_long();           //   DOTO: 韌體還沒有做
			if(tCounter!=mCounter) {
			   delaytime=10;
  		       System.out.println("tCounter="+Long.toString(tCounter));   
 		       mCounter=tCounter;
					  byte[] data=mloralib.FunLora_6_readPureData();
				    	  if(data!=null && data.length>4 &&  data[0]==(byte)0xc1 &&    data[1]==(byte)0x86 ){
				    		int len=(int)data[2]-2;  //18
				    		if(data.length==len+6 && len>0){ //15 22  /// 8+1  3 
					    		byte[] data2 = new byte[len];
					    		for(int i2=0;i2<len;i2++){
					    			byte t1=data[3+i2];
					    			data2[i2]=t1;
					    		}
				    			//if(oldData==null || Arrays.equals(oldData, data2)==false) {
						    	 	String  tRecHex="";
						    	 	try {
							    	 	if(mChoiceRecevieDisplay.getSelectedIndex()==0) {   //文字
							    	 		tRecHex = new String(data2, "UTF-8");
							    	 	}else if(mChoiceRecevieDisplay.getSelectedIndex()==1) {   //16進位數字
								    	 	tRecHex=mloralib.FunBytesToHexString(data2,',');    //FunBytesToHex(data2);
							    	 	}else if(mChoiceRecevieDisplay.getSelectedIndex()==2) {   //10進位數字
							    	 		tRecHex=mloralib.FunBytesToString(data2,',');
							    	 	}
						    	 	}catch (UnsupportedEncodingException e) {}
						    	    System.out.println("收到資料COM Port<-"+tRecHex);   
						    	    //if(oldData!=null) {
					    	    			recevieText.setText(tRecHex+"\n"+recevieText.getText());  //避免顯示上次USB 記憶體上的舊資料
					    	    			// BEGIN 13, 如果要上傳到 URL
						    	    	    if(mLo.checkBoxUpload=="true") {
					    	    				if(TextFieldUrl.length()>5) {
					    	    					String data3=FunHTTP(TextFieldUrl+tRecHex);
				    	    					    mlabelMessage.setText(data3); 
					    	    				}
					    	    			}
					    	    			// END 13, 如果要上傳到 URL
					    	    			
					    	    			// BEGIN 12, 如果要上傳到  Dashboard
						    	    	    String t1aa=mLo.checkBoxDashboard;
						    	    	    if(mLo.checkBoxDashboard.equals("true")) {
						    	    	    		String thttpURLResult="";
					    	    	    	    		String thttpURL=mLo.mURL[8]+"&Key=data&Value="+tRecHex+"&apikey="+APIKEY;
									    	if(mChoiceRecevieDisplay.getSelectedIndex()==0) {   //文字 不改，直接丟
									    		thttpURL=mLo.mURL[8]+"&Key=String&Value="+tRecHex+"&apikey="+APIKEY;
									    		thttpURLResult=FunHTTP(thttpURL);
									    	}else if(mChoiceRecevieDisplay.getSelectedIndex()==1 ||    //16進位數字
									    			mChoiceRecevieDisplay.getSelectedIndex()==2 ) {     //10進位數字
									    		for(int i=0;i<len;i++) {	
									    			int t1=(int) (data2[i]& 0xFF);
									    			String t2 = Integer.toString(t1);
			    	    	    	    				    		thttpURL=mLo.mURL[8]+"&Key=byte"+Integer.toString(i)+"&Value="+t2+"&apikey="+APIKEY;
			    	    	    	    				    		thttpURLResult=FunHTTP(thttpURL);
									    		}
									    	}
				    	    					mlabelMessage.setText(thttpURLResult); 
					    	    			}
					    	    			// END 12, 如果要上傳到 Dashboard
					    	    			// BEGIN 11,ifroglab.csv 儲存檔案
					    	    			if(mLo.checkBoxSaveLog.equals("true")) {
					    	    				try	{
					    	    					if(mLo.StringLogFileName.length()>0) {
					    	    						String filename= mLo.StringLogFileName; 
					    	    				    		FileWriter fw = new FileWriter(filename,true);   //the true will append the new data
					    	    				    		fw.write(tRecHex+"\n");                         //appends the string to the file
					    	    				    		fw.close();
					    	    					}
					    	    				}catch(IOException ioe){
					    	    				    System.err.println("IOException: " + ioe.getMessage());
					    	    				}
					    	    			}					    	    			
						    	  //  }
			    	    			    // END 11,ifroglab.csv 儲存檔案
							    	oldData=data2.clone();
				    			//}
				    		}
				    	  }
			}
		    //sleep(500);
		    sleep(delaytime);
		    if(delaytime<500)   delaytime=delaytime+2;
		    // begon 001  debug 1
		   /*
		    byte[] data_d2= {1,2,3};
		    data_d2[0]=(byte) counter;
		    data_d2[1]=(byte) (counter+100);
		    data_d2[2]=(byte) (counter+200);
    			String thttpURLdebug; //=mLo.mURL[8]+"&Key=data&Value="+tRecHex+"&apikey="+APIKEY;
    			for(int i=0;i<data_d2.length;i++) {
    			    int t1=(int) (data_d2[i]& 0xFF);
    				String t2 = Integer.toString(t1);
    				thttpURLdebug=mLo.mURL[8]+"&Key=byte"+Integer.toString(i)+"&Value="+t2+"&apikey="+APIKEY;
    				FunHTTP(thttpURLdebug);
    			}
			counter=counter+1;
		    sleep(1000*10);
		    */
		    // end 001 debug 1
	     }
	   }
	   catch (InterruptedException exception) {}
	  }
	  private Random generator;
	  private TextArea mRrecevieText;
	  private loralib threadloralib;
	  private byte[] oldData;
	  private long mCounter;
	  // private int mChoiceRecevieDisplayIndex;
	  private Choice mChoiceRecevieDisplay;
	  private int delaytime;
	//  private Checkbox mcheckBoxSaveLog;// = new Checkbox();
	//  private Checkbox mcheckBoxUpload;// = new Checkbox();
    //	private TextField mTextFieldUrl;//=new TextField
	  private Label mlabelMessage;
	  private ifroglablora mLo;
	    		  
	    		  
	}	
}
