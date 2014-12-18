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
import property.GeneratorProperty;
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
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.webPageDefinition.*;
import webPage.*;



public class Panel_TransProcEdit_Sub extends JPanel {
	public SerializableSpringLayout springLayout;
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	public static Panel_TransProcEdit_Sub obj = new Panel_TransProcEdit_Sub();

	
	// �T�[�r�X�p�l���N���b�N���ɕ\������T�[�r�X�����ꗗ�p�l���ƁA������i�[����X�N���[���y�C��
	private PanelServiceArgEdit panelCsArgs = PanelServiceArgEdit.getInstance();
	private JScrollPane scrollPane_serviceEdit;



	private Panel_TransProcEdit_Sub() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

	
		setBackground(Color.WHITE);
	}


	
	
	
	/*
	 * PURPOSE :
	 * �T�[�r�X�̊e�����̐����A�ҏW�{�^����\��
	 * 
	 * NOTICE :
	 * �o�͂Ƃ��̕ҏW�{�^�����\�����邩������Ȃ�
	 */
	public void showCsArgs(Service service) {
		// �S�R���|�[�l���g����
		removeAll();

		// �T�[�r�X�����I���p�l�����ރX�N���[���y�C���i���ł��z�u�ł���悤setBounds()���Ă����j
		panelCsArgs.setPreferredSize(new Dimension(this.getWidth()-20, 1000));
		scrollPane_serviceEdit = new JScrollPane();
		scrollPane_serviceEdit.getViewport().add(panelCsArgs);
		scrollPane_serviceEdit.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane_serviceEdit, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane_serviceEdit, 0, SpringLayout.WEST, this);
		add(scrollPane_serviceEdit);
		
		// �T�[�r�X�����ꗗ�p�l���̃R���|�[�l���g�z�u
		panelCsArgs.relocate(service);


		boolean japanese = GeneratorProperty.japanese();
		Frame_TransProcEdit_Sub.getInstance().setTitle(japanese ? "�ҏW - �T�[�r�X" : "Configuration - Service");

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	
	
	
	
	
	public void displayPanelCreateReflectionEdit(TppCreateReflection reflection) {
		// �S�R���|�[�l���g����
		removeAll();

		PanelCreateReflectionEdit panel = PanelCreateReflectionEdit.getInstance();
		panel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		Slpc.put(springLayout, "N", panel, "N", this, 0);
		Slpc.put(springLayout, "W", panel, "W", this, 0);
		add(panel);

		panel.locateCompsForCreateReflectionEdit(reflection);

		boolean japanese = GeneratorProperty.japanese();
		Frame_TransProcEdit_Sub.getInstance().setTitle(japanese ? "�ҏW - ���R�[�h�쐬����" : "Configuration - Record Creation");
		
		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	
	
	
	public void displayPanelIaReflectionEdit(TppIAReflection reflection) {
		// �S�R���|�[�l���g����
		removeAll();

		// �X�N���[���y�C���̒��Ɏ�荞��IA���t���N�V�����ҏW�p�l���̏c��
		int PANEL_HEIGHT = 750;
		
		PanelIaReflectionEdit panel = PanelIaReflectionEdit.getInstance();
		panel.locateCompsForIaReflectionEdit(reflection);
		panel.setPreferredSize(new Dimension(this.getWidth()-20, PANEL_HEIGHT));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(panel);
		scrollPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		Slpc.put(springLayout, "N", scrollPane, "N", this, 0);
		Slpc.put(springLayout, "W", scrollPane, "W", this, 0);
		add(scrollPane);

		boolean japanese = GeneratorProperty.japanese();
		Frame_TransProcEdit_Sub.getInstance().setTitle(japanese ? "�ҏW - �l�����A�T�C������" : "Configuration - Individual Accessibility Assignment");

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	


	public static Panel_TransProcEdit_Sub getInstance() {
		return Panel_TransProcEdit_Sub.obj;
	}

}
