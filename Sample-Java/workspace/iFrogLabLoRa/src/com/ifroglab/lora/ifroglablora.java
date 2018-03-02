


package com.ifroglab.lora;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

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
	private byte Freq1=0x01;
	private byte Freq2=0x65;
	private byte Freq3=0x6c;
	private byte Power=0x3;  //{ TXRX,0x01,0x65,0x6c,0x3); 
	//private long mCounter;

	
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
	
	private TextField TextFieldUrl;
	private Checkbox checkBoxSaveLog;
	private Checkbox checkBoxUpload;
	private Button ButtonLogFileName;  
	
	 
	
	// 程式專用
	
	// 多國語言
	public String mStr[][]={
			  {"Step1:","步驟 1:"},
			  {"Select LoRa Com Port","iFrogLab LoRa 設備"},
			  {"Reload","尋找此機器LoRa設備"},
			  {"Searching:","搜尋中LoRa"},
			  {"Done","完成"},
			  {"Step2:","步驟 2:"},  //5
			  {"Setup LoRa device","設定LoRa設備"},  
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
			  {"Hex (0x00 to 0xff) for E.g 0x01,0x0a","16進位數字 (0x00 to 0xff) 例如：0x01,0x0a"}, 
			  {"Decimal (0-255) E.g:1,2,255 　","10進位 (0-255)  例如: 1,2,255 "}, //35
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
			  {"http://www.ifroglab.org/iot/AjaxIoTTable.php?action=upload&Key=Temperature&apikey=20180226203605JXpDA&Value=","http://www.ifroglab.org/iot/AjaxIoTTable.php?action=upload&Key=Temperature&apikey=20180226203605JXpDA&Value="}, 
	};
	public int lan=0;
	// 程式用到的變數
	private String StringLogFileName="iFrogLab.csv";
	private ifroglablora ifroglabloraClass; 
	
	@SuppressWarnings("deprecation")
	public ifroglablora () {
		
		
		//String t1= Integer.toString(ChoiceLanguage.getSelectedIndex() );   // ChoiceLanguage.getSelectedIndex()   //ChoiceLanguage.getItem(ChoiceLanguage.getSelectedIndex());	
		String t1=FunPreferencesLoad("Language","0");
		if(t1!="") {
			lan=Integer.parseInt(t1);
		}
		

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
	}
	// 連接LoRa COM ports 設備
	private void ui_Setp1_OpenComPort(){ 					   	// 連接 LoRa USB COM port 設備
	    if(mLoRaSerialPortString!=null && mLoRaSerialPortString.size()>0){  // 如果有設備的話
			// String data = mStr[22][lan]+ mUIComPortName.getSelectedItem(); //mUIComPortName.getItem(mUIComPortName.getSelectedIndex());
			labelMessage.setText(mStr[23][lan]+ mUIComPortName.getSelectedItem());	 //顯示開設備名稱　LoRa USB COM port
			mloralib.SerialPort_setSerialPort(mUIComPortName.getSelectedItem());     //告訴LoRa Lib要打開的設備LORA Com Port 
	    }
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
		 	   mUIComPortName.add(mStr[1][lan]);                 //"No Com Port Devices"
		    }
		    ui_Setp1_OpenComPort(); 					      	// 連接 LoRa USB COM port 設備
         }
         catch ( Exception e1 ){
            e1.printStackTrace();
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
	   l2.setSize(115,30);
	   mainFrame.add(l2);  
	   l2.setLocation(10, 40+tUITop);
	   // END 2
	   
	   // Begin 03,               添加Step 1  Com 設備列表 Choice, combo box
	   mUIComPortName=new Choice();  
	   mUIComPortName.setBounds(100,100, 280,30);  
	   mUIComPortName.add(mStr[1][lan]);                   //"No Com Port Devices"
	   mainFrame.add(mUIComPortName);  
	   mUIComPortName.setLocation(125, 25+tUITop+(mUIComPortName.size().height/2));
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
	   refreshButton.setBounds(100,100, 140,30); 
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
		      l2.setText(mStr[6][lan]);     // "Select LoRa device"
		      l2.setSize(80,30);
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
		        	 if(mDeviceID.length()>0){                 // 如果已經開啟LoRa 設備的話
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
			        	 }else {
			        		 String tMsg= mUIComPortName.getItem(mUIComPortName.getSelectedIndex())+mStr[43][lan];
			        		 labelMessage.setText(tMsg); // 這不是iFrogLab LoRa 設備

			                 MsgDialog aboutDialog = new MsgDialog(mainFrame,tMsg,240,150 );
			                 aboutDialog.setVisible(true);
			        	 }
		        	 }
		         }
		      });
		      // END 08
		      // Begin 07, 添加Step 2  「更多設定」的按鈕 
		      Button preferencesButton = new Button(mStr[14][lan]);    //"Preference"
		      preferencesButton.setBounds(100,100, 125,30); 
		      mainFrame.add(preferencesButton);
		      preferencesButton.setLocation(495,y+50+tUITop+(preferencesButton.size().height/2));
		      preferencesButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 	labelMessage.setText(mStr[12][lan]);  

		        	 	PreferenceDialog preferenceDialog = new PreferenceDialog(mainFrame,ifroglabloraClass);
		        	 	preferenceDialog.setVisible(true);
		                
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
		      
		      // Begin 06,               添加Step 2  改變語言
		      final Choice ChoiceLanguage=new Choice();  
		      ChoiceLanguage.setBounds(100,100,100,30);  
		      ChoiceLanguage.add("English");             
		      ChoiceLanguage.add("Chinese");       
		      mainFrame.add(ChoiceLanguage);  
		      ChoiceLanguage.setLocation(400,y+50+tUITop+(ChoiceLanguage.size().height/2));
		      ChoiceLanguage.select(lan);
		      ChoiceLanguage.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
					String t1= Integer.toString(ChoiceLanguage.getSelectedIndex() );   // ChoiceLanguage.getSelectedIndex()   //ChoiceLanguage.getItem(ChoiceLanguage.getSelectedIndex());	
					FunPreferencesSave("Language",t1 );
					System.exit(0);
				}
		      });
		      // END 06
		      
		      
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
		      final Choice LoRaModeChoice=new Choice();  
		      LoRaModeChoice.setBounds(100,100, 180,30);  
		      LoRaModeChoice.add(mStr[33][lan]);             
		      LoRaModeChoice.add(mStr[34][lan]);             
		      LoRaModeChoice.add(mStr[35][lan]);             
		      LoRaModeChoice.add(mStr[36][lan]); 
		      mainFrame.add(LoRaModeChoice);  
		      LoRaModeChoice.setLocation(125, y+35+(LoRaModeChoice.size().height/2+tUITop));
		      LoRaModeChoice.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent e) {
		            String data = "Setup: " 
		            + LoRaModeChoice.getItem(LoRaModeChoice.getSelectedIndex());
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
		        	     byte[] data = tSendTextString.getBytes(StandardCharsets.UTF_8); //UTF-8
		        	     mloralib.FunLora_5_write16bytesArray(data,5);
		        	     try {
								TimeUnit.MILLISECONDS.sleep(10);
						 } catch (InterruptedException e1) {
								e1.printStackTrace();
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
		     mChoiceRecevieDisplay.add(mStr[36][lan]); 
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
		     recevieText.setText("");    
		     recevieText.setSize(580,80);
		     mainFrame.add(recevieText);  
		     recevieText.setLocation(10, y+190+tUITop);
		     // END 05	     
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
		
		mThreadRecevieText = new ThreadRecevieText( labelMessage,checkBoxUpload,
				  TextFieldUrl,checkBoxSaveLog,
				  recevieText,mloralib,mChoiceRecevieDisplay);
		mThreadRecevieText.start();
	
	}
	public static void main(String[] args) {
	   // Invoke the constructor to setup the GUI, by allocating an instance
		ifroglablora app = new ifroglablora();
	      // or simply "new AWTCounter();" for an anonymous instance   
	}
	

	/////////////////////////////////////////
/////////////////////////////////////////
	class MsgDialog extends Dialog {
	      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public MsgDialog(Frame parent,String iMsg,int iWidth,int iHight){
	         super(parent, true);         
	         //setBackground(Color.gray);
	         mMsg=iMsg;
	         mWidth=iWidth;
	         mHight=iHight;
	         setLayout(new BorderLayout());
	         Panel panel = new Panel();
	         panel.add(new Button("Close"));
	         add("South", panel);
	         setSize(iWidth,iHight);

	         addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent windowEvent){
	               dispose();
	            }
	         });
	      }

	      public boolean action(Event evt, Object arg){
	         if(arg.equals("Close")){
	            dispose();
	            return true;
	         }
	         return false;
	      }

	      public void paint(Graphics g){
	        // g.setColor(Color.white);
	         g.drawString(mMsg, 20,mHight/2 );
	      }
	      private String mMsg;
	      private int mWidth;
	      private int mHight;
	      
	   }
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
	  public ThreadRecevieText(Label ilabelMessage,Checkbox icheckBoxUpload,
			  TextField iTextFieldUrl,Checkbox icheckBoxSaveLog,
			  TextArea iComRrecevieText,loralib iloralib,Choice iChoiceRecevieDisplay)
	  { 
		  delaytime=500;
		  mlabelMessage=ilabelMessage;
		  mRrecevieText = iComRrecevieText;
		  mcheckBoxSaveLog=icheckBoxSaveLog; 
		  mcheckBoxUpload=icheckBoxUpload; 
		  mTextFieldUrl=iTextFieldUrl; 
	      generator = new Random();
	      threadloralib=iloralib;
          mloralib.ReadMode(Freq1, Freq2, Freq3, Power); 
          //String t1=iChoiceRecevieDisplay.getItem(iChoiceRecevieDisplay.getSelectedIndex());
          mChoiceRecevieDisplayIndex=iChoiceRecevieDisplay.getSelectedIndex();
	  }

	  public void run()
	  { 
	   	  
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
				    			if(oldData==null || Arrays.equals(oldData, data2)==false) {
						    	 	String tRecHex="";
						    	 	try {
							    	 	if(mChoiceRecevieDisplayIndex==0) {   //文字
							    	 		tRecHex = new String(data2, "UTF-8");
							    	 	}else if(mChoiceRecevieDisplayIndex==1) {   //16進位數字
								    	 		tRecHex=mloralib.FunBytesToHex(data2,',');    //FunBytesToHex(data2);
							    	 	}else if(mChoiceRecevieDisplayIndex==2) {   //10進位數字
							    	 		tRecHex=mloralib.FunBytesToHex(data2);
							    	 	}
						    	 	}catch (UnsupportedEncodingException e) {}
						    	    System.out.println("收到資料COM Port<-"+tRecHex);   
						    	    if(oldData!=null) {
					    	    			recevieText.setText(tRecHex+"\n"+recevieText.getText());  //避免顯示上次USB 記憶體上的舊資料
					    	    			// BEGIN 12, 如果要上傳到 Dashboard
					    	    			if(mcheckBoxUpload.getState()==true) {
					    	    				if(mTextFieldUrl.getText().length()>5) {
					    	    					String urlString = mTextFieldUrl.getText(); //"http://wherever.com/someAction?param1=value1&param2=value2....";
					    	    					URL url;
					    	    					URLConnection conn;
					    	    					InputStream is;
					    	    					String data3 = "";
													try {
														url = new URL(urlString+tRecHex);
														try {
															conn = url.openConnection();
							    	    					        is = conn.getInputStream();
							    	    					        //InputStream iStream = httpEntity.getContent();
							    	    					        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
							    	    					        StringBuffer sb = new StringBuffer();
							    	    					        String line = "";

							    	    					        while ((line = br.readLine()) != null) {
							    	    					            sb.append(line);
							    	    					        }
							    	    					        data3 = sb.toString();
							    	    					        System.out.println(data3);
							    	    					        mlabelMessage.setText(data3); 
							    	    					        
							    	    					        
							    	    					        
							    	    					        
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													} catch (MalformedURLException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
					    	    					// Do what you want with that stream
					    	    					
					    	    				}
					    	    			}
					    	    			// END 12, 如果要上傳到 Dashboard
					    	    			// BEGIN 11,ifroglab.csv 儲存檔案
					    	    			if(mcheckBoxSaveLog.getState()==true) {
					    	    				try	{
					    	    					if(StringLogFileName.length()>0) {
					    	    						String filename= StringLogFileName; 
					    	    				    		FileWriter fw = new FileWriter(filename,true);   //the true will append the new data
					    	    				    		fw.write(tRecHex+"\n");                         //appends the string to the file
					    	    				    		fw.close();
					    	    					}
					    	    				}catch(IOException ioe){
					    	    				    System.err.println("IOException: " + ioe.getMessage());
					    	    				}
					    	    			}					    	    			
						    	    }
			    	    			    // END 11,ifroglab.csv 儲存檔案
							    	oldData=data2.clone();
				    			}
				    		}
				    	  }
			}
		    //sleep(500);
		    sleep(delaytime);
		    if(delaytime<500)   delaytime=delaytime+2;
	     }
	   }
	   catch (InterruptedException exception) {}
	  }
	  private Random generator;
	  private TextArea mRrecevieText;
	  private loralib threadloralib;
	  private byte[] oldData;
	  private long mCounter;
	  private int mChoiceRecevieDisplayIndex;
	  private int delaytime;
	  private Checkbox mcheckBoxSaveLog;// = new Checkbox();
	  private Checkbox mcheckBoxUpload;// = new Checkbox();
	  private TextField mTextFieldUrl;//=new TextField
	  private Label mlabelMessage;
	    		  
	    		  
	}	
}
