package view.webPageDefinition.transAuthEdit;

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





public class FrameTransAuthEdit extends JFrame {
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	public PanelTransAuthEdit panel;

	/*
	 * インスタンス参照変数は最後！
	 */
	private static FrameTransAuthEdit obj = new FrameTransAuthEdit();
	
	

	
	private FrameTransAuthEdit() {
		setLayout(new BorderLayout());
		
		setBounds(500, 300, FrameTransAuthEdit.frameWidth, FrameTransAuthEdit.frameHeight);

		// パネル初期化・サイズ調整
	    panel = PanelTransAuthEdit.getInstance();
	    panel.setPreferredSize(new Dimension(this.getWidth()-20, PanelTransAuthEdit.PANEL_HEIGHT));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(panel);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	public static void repaintAndValidate() {
		FrameTransAuthEdit.obj.repaint();
		FrameTransAuthEdit.obj.validate();
	}

	
	
	
	
	public static FrameTransAuthEdit getInstance() {
		return FrameTransAuthEdit.obj;
	}





	public static void updateInstance(FrameTransAuthEdit newObject) {
		FrameTransAuthEdit.obj = newObject;
	}
}
