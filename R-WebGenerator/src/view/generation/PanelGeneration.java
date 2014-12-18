package view.generation;

import gui.*;
import gui.arrow.*;

import role.Role;
import role.RoleManager;
import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.*;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import executer.generation.GenerationExecuter;
import utility.*;





public class PanelGeneration extends JPanel implements ActionListener,Serializable {
	public SerializableSpringLayout springLayout;

	// ���������s����{�^��
	JButton generateButton;

	// ����ʂւ̑J�ڃ{�^��
	private JButton transBtnWebPageDef;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static PanelGeneration obj = new PanelGeneration();

	private PanelGeneration() {
		setBackground(Color.WHITE);

		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		initButtons();
		locateComps();
	}

	
	
	
	public void initButtons() {
		// �����{�^��
		generateButton = new JButton(GeneratorProperty.japanese()?"Web�A�v���P�[�V�����𐶐�":"Generate Web Application");
		generateButton.addActionListener(this);
		generateButton.setActionCommand("�������s");

		// Web�y�[�W�E�J�ڌ�����`��ʂւ̑J�ڃ{�^��
		transBtnWebPageDef = new JButton(GeneratorProperty.japanese()?"Web�y�[�W�E�J�ڌ�����`��ʂ�":"Go To Web Page Definition");
		transBtnWebPageDef.addActionListener(this);
		transBtnWebPageDef.setActionCommand("�J�ځFWeb�y�[�W��`���");
	}
	
	
	
	
	public void locateComps() {
		removeAll();
		
		springLayout.putConstraint(SpringLayout.NORTH, generateButton, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, generateButton, 20, SpringLayout.WEST, this);
		add(generateButton);

		springLayout.putConstraint(SpringLayout.NORTH, transBtnWebPageDef, 20, SpringLayout.SOUTH, generateButton);
		springLayout.putConstraint(SpringLayout.WEST, transBtnWebPageDef, 0, SpringLayout.WEST, generateButton);
		add(transBtnWebPageDef);
	}
	

	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		// ����
		if(cmd.equals("�������s")) {
			Debug.out("���������݂܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

			GenerationExecuter genExe = GenerationExecuter.getInstance();
			boolean result = genExe.execute();
			if(result) {
				String message =
					japanese ?
					"�����Web�A�v���P�[�V�����̐������������܂����B\n" +
					"R-WebGenerator/src/generatedFiles/application/�ɃA�v���P�[�V�����A\n" +
					"R-WebGenerator/src/generatedFiles/sql/�Ƀf�[�^�x�[�X�̃_���v������܂��B\n" +
					"���ꂼ��Apache�ւ̔z�u�AMySQL���̃f�[�^�x�[�X'hoge'�ւ̓K�p���s���ĉ������B"
					:
					"Generation has been finished successfully.\n" +
					"Application is on R-WebGenerator/src/generatedFiles/application/ and\n" +
					"Dump file of database is on R-WebGenerator/src/generatedFiles/sql/ .\n" +
					"Respectively, put in Apache, apply for MySQL database 'hoge'.";
				JOptionPane.showMessageDialog(this, message, japanese?"��������":"Success", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				String message = japanese ? "�����𒆒f���܂����B" : "Generation has been suspended.";
				JOptionPane.showMessageDialog(this, message, japanese?"�������f":"Suspend", JOptionPane.PLAIN_MESSAGE);
			}
		}
		else if(cmd.equals("�J�ځFWeb�y�[�W��`���")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
	}
	
	
	
	
	public static PanelGeneration getInstance() {
		return PanelGeneration.obj;
	}


	public static void updateInstance(PanelGeneration newObject) {
		PanelGeneration.obj = newObject;
	}
}
