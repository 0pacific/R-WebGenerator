package view.transProcEdit.tableSelect;

import java.awt.FlowLayout;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;
import view.transProcEdit.serviceSelect.PanelForServiceSelection;

import javax.swing.*;

import mainFrame.MainFrame;

public class FrameTableSelect extends JFrame {
	// フレーム位置
	public static final int framePosX = 500;
	public static final int framePosY = 300;
	
	// フレームサイズ
	public static final int frameWidth = 300;
	public static final int frameHeight = 400;

	// パネル
	private PanelTableSelect panel;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static FrameTableSelect obj = new FrameTableSelect();

	
	
	
	
	private FrameTableSelect() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    setLayout(null);

	    setBounds(FrameTableSelect.framePosX, FrameTableSelect.framePosY,
					FrameTableSelect.frameWidth, FrameTableSelect.frameHeight);

	    PanelTableSelect panel = PanelTableSelect.getInstance();
		panel.setBounds(PanelTableSelect.posX, PanelTableSelect.posY, PanelTableSelect.panelWidth, PanelTableSelect.panelHeight);
		getContentPane().add(panel);
	}
	
	
	
	
	
	public static void repaintAndValidate() {
		FrameTableSelect.obj.repaint();
		FrameTableSelect.obj.validate();
	}
	
	
	
	public static FrameTableSelect getInstance() {
		return FrameTableSelect.obj;
	}
	
	
	
	public static void updateInstance(FrameTableSelect newObject) {
		FrameTableSelect.obj = newObject;
	}

}
