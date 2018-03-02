package com.ifroglab.lora;

import java.awt.*;
import java.awt.event.*;

/*
public class PreferenceDialog {

}
*/


class PreferenceDialog extends Dialog {
	private int tUITop=20;
   public PreferenceDialog(Frame parent,ifroglablora parareClass){
      super(parent, true);         
      
      
      //setBackground(Color.gray);
      setLayout(new BorderLayout());
      Panel panel = new Panel();
      setSize(600,500);
      //panel.setSize(600, 500);

      panel.setLayout(new FlowLayout());
      

     // Panel controlPanel = new Panel();
     // controlPanel.setLayout(new FlowLayout());
      
      
      
      
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            dispose();
         }
      });
      ui_Step1(panel,parareClass);
      add(panel);
      
      
      /*
    setLayout(new BorderLayout());
      
      Label headerLabel = new Label();
      headerLabel.setAlignment(Label.CENTER);
      Label statusLabel = new Label();        
      statusLabel.setAlignment(Label.CENTER);
      statusLabel.setSize(350,100);

      Panel controlPanel = new Panel();
      controlPanel.setLayout(new FlowLayout());
      

      controlPanel.add(headerLabel);
      controlPanel.add(controlPanel);
      controlPanel.add(statusLabel);
      controlPanel.setVisible(true); 
      

      add(controlPanel);
      */
      
      
      
      //setAlwaysOnTop(true);

     // validate();
      
      
      
	//	resize(H_SIZE, V_SIZE);
   }
/*
   public boolean action(Event evt, Object arg){
      if(arg.equals("Close")){
         dispose();
         return true;
      }
      return false;
   }
   */
/*
   public void paint(Graphics g){
      //g.setColor(Color.white);
      g.drawString("www.iFrogLab.com", 25,70 );
      g.drawString("Version 1.0", 60, 90);      
   }
  */ 
   
   
   public void ui_Step1(Panel mainFrame,ifroglablora p) {  
	   // Begin 01,                添加Step 1 的文字 "Step1"
	   Label l1 = new Label();
	   l1.setAlignment(Label.LEFT);
	   l1.setText(p.mStr[0][p.lan]);    //"Step1"
	   l1.setSize(mainFrame.size().width,30);
	   mainFrame.add(l1);  
	   //l1.setLocation(10, 20+tUITop);
	   // END 01
	   // Begin 02, 關閉按鈕
	   Button refreshButton = new Button(p.mStr[4][p.lan]);    //"reflash"
	   refreshButton.setBounds(mainFrame.size().width,100,mainFrame.size().width,30); 
	   mainFrame.add(refreshButton);
	   //mUIComPortName.setBounds(100,100, 150,75); 
	   //refreshButton.setLocation(410,25+tUITop+(refreshButton.size().height/2));
	   refreshButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  	dispose();
	    	  
  			//ui_Setp1_listPorts();                                  // 找出LoRa USB COM ports 設備
	      }
	   });
	   // END 02
	   
	   // Begin 5,                 添加Step 1 一條區分線
	   Label lineLabel = new Label();
	   lineLabel.setAlignment(Label.LEFT);
	   lineLabel.setText("");     // "Select LoRa device"
	   lineLabel.setSize(mainFrame.size().width,1);
	   lineLabel.setBackground(Color.lightGray );
	   mainFrame.add(lineLabel);  
	   lineLabel.setLocation(0, 75+tUITop);
	   // END 5
	   
	   
	   
	/*
	   // Begin 2,                 添加Step 1 的文字 "Select LoRa device"
	   Label l2 = new Label();
	   l2.setAlignment(Label.LEFT);
	   l2.setText(pcls.mStr[1][lan]);     // "Select LoRa device"
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
	   /*
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
	 */  
	}
	
}

