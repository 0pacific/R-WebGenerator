package view.tableList.accountTableDef;

import javax.swing.*;

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
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import utility.*;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class FrameAccountTableDefinition extends JFrame implements Serializable {
	public static final int frameWidth = 600;
	public static final int frameHeight = 700;

	public PanelAccountTableDefinition panel;

	/*
	 * インスタンス参照変数は最後！
	 */
	private static FrameAccountTableDefinition obj = new FrameAccountTableDefinition();
	
	

	
	private FrameAccountTableDefinition() {
		setLayout(new BorderLayout());
		
		setBounds(500, 270, FrameAccountTableDefinition.frameWidth, FrameAccountTableDefinition.frameHeight);

		// パネル初期化・サイズ調整
	    panel = PanelAccountTableDefinition.getInstance();
	    panel.setPreferredSize(new Dimension(this.getWidth()-20, PanelAccountTableDefinition.PANEL_HEIGHT));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(panel);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	public static void repaintAndValidate() {
		FrameAccountTableDefinition.obj.repaint();
		FrameAccountTableDefinition.obj.validate();
		FrameAccountTableDefinition.obj.panel.repaint();
		FrameAccountTableDefinition.obj.panel.validate();
	}

	
	
	
	
	public static FrameAccountTableDefinition getInstance() {
		return FrameAccountTableDefinition.obj;
	}



	public static void updateInstance(FrameAccountTableDefinition newObject) {
		FrameAccountTableDefinition.obj = newObject;
	}
}
