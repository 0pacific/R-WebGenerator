package view.transProcEdit.constEdit;

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





public class FrameConstEdit extends JFrame {
	// �t���[���ʒu
	public static final int framePosX = 400;
	public static final int framePosY = 300;
	
	// �t���[���T�C�Y
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	public PanelConstEdit panel;

	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static FrameConstEdit obj = new FrameConstEdit();

	
	
	

	private FrameConstEdit() {
		// �t���[���̈ʒu�ƃT�C�Y
		setBounds(FrameConstEdit.framePosX, FrameConstEdit.framePosY,
					FrameConstEdit.frameWidth, FrameConstEdit.frameHeight);

		// ���C�A�E�g�}�l�[�W���Ȃ�
		setLayout(null);
		
		// �p�l���̔z�u
		panel = PanelConstEdit.getInstance();
		panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		getContentPane().add(panel);
	}

	
	
	
	public static void repaintAndValidate() {
		FrameConstEdit.obj.repaint();
		FrameConstEdit.obj.validate();
	}
	




	public static FrameConstEdit getInstance() {
		return FrameConstEdit.obj;
	}




	public static void updateInstance(FrameConstEdit newObject) {
		FrameConstEdit.obj = newObject;
	}
}

