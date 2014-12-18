package view.transProcEdit.constArrayEdit;

import javax.swing.*;

import mainFrame.MainFrame;

import authority.AuthorityManager;

import java.awt.*;
import java.util.*;
import java.io.*;

import debug.Debug;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;
import executer.generation.mysql.*;





public class FrameConstArrayEdit extends JFrame implements Serializable {
	// フレーム位置
	public static final int framePosX = 400;
	public static final int framePosY = 300;
	
	// フレームサイズ
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	public PanelConstArrayEdit panel;

	/*
	 * インスタンスは最後！
	 */
	private static FrameConstArrayEdit obj = new FrameConstArrayEdit();

	
	
	

	private FrameConstArrayEdit() {
		// フレームの位置とサイズ
		setBounds(FrameConstArrayEdit.framePosX, FrameConstArrayEdit.framePosY,
					FrameConstArrayEdit.frameWidth, FrameConstArrayEdit.frameHeight);

		// レイアウトマネージャなし
		setLayout(null);
		
		// パネルの配置
		panel = PanelConstArrayEdit.getInstance();
		panel.setBounds(PanelConstArrayEdit.posX, PanelConstArrayEdit.posY, PanelConstArrayEdit.panelWidth, PanelConstArrayEdit.panelHeight);
		getContentPane().add(panel);
	}

	
	
	
	public static void repaintAndValidate() {
		FrameConstArrayEdit.obj.repaint();
		FrameConstArrayEdit.obj.validate();
	}




	public static FrameConstArrayEdit getInstance() {
		return FrameConstArrayEdit.obj;
	}




	public static void updateInstance(FrameConstArrayEdit newObject) {
		FrameConstArrayEdit.obj = newObject;
	}
}
