package view.transProcEdit.subFrame;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.portTrans.*;
import tpp.service.*;
import transition.*;
import utility.*;
import utility.serialize.Serializer;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;


public class Frame_TransProcEdit_Sub extends JFrame {
	public static final int frameWidth = 350;
	public static final int frameHeight = 600;
	public SerializableSpringLayout springLayout = new SerializableSpringLayout();

	public Panel_TransProcEdit_Sub basePanel;
	
	/*
	 * インスタンスは最後！
	 */
	private static Frame_TransProcEdit_Sub obj = new Frame_TransProcEdit_Sub();





	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private Frame_TransProcEdit_Sub() {
		setBounds(900, 380, Frame_TransProcEdit_Sub.frameWidth, Frame_TransProcEdit_Sub.frameHeight);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	    
	    // レイアウト
	    springLayout = new SerializableSpringLayout();
	    getContentPane().setLayout(springLayout);
	    
	    
	    // Generator Panel 初期化・追加
	    basePanel = Panel_TransProcEdit_Sub.getInstance();
	    basePanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
	    Slpc.put(springLayout, "N", basePanel, "N", getContentPane(), 0);
		Slpc.put(springLayout, "W", basePanel, "W", getContentPane(), 0);
		getContentPane().add(basePanel);
	}




	public static void repaintAndValidate() {
		Frame_TransProcEdit_Sub.obj.repaint();
		Frame_TransProcEdit_Sub.obj.validate();
	}

	public static Frame_TransProcEdit_Sub getInstance() {
		return Frame_TransProcEdit_Sub.obj;
	}
}
