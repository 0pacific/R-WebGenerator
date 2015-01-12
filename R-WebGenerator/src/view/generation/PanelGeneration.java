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
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;

import account.AccountManager;
import property.GeneratorProperty;
import mainFrame.MainFrame;
import debug.Debug;
import executer.generation.GenerationExecuter;
import utility.*;
import utility.serialize.Serializer;





public class PanelGeneration extends JPanel implements ActionListener,Serializable {
	public SerializableSpringLayout springLayout;

	// ���������s����{�^��
	JButton generateButton;
	
	//�Ǘ��җp�f�[�^�̏o��
	JButton adminDataGenerateButton;

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
		boolean japanese = GeneratorProperty.japanese();
		
		// �����{�^��
		generateButton = new JButton(japanese?"Web�A�v���P�[�V�����𐶐�":"Generate Web Application");
		generateButton.addActionListener(this);
		generateButton.setActionCommand("�������s");
		
		// �Ǘ��җp�f�[�^���o��
		adminDataGenerateButton = new JButton(japanese? "�Ǘ��җp�f�[�^���o��" : "Generate the Data Admin Uses");
		adminDataGenerateButton.addActionListener(this);
		adminDataGenerateButton.setActionCommand("�Ǘ��җp�f�[�^����");

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
		
		springLayout.putConstraint(SpringLayout.NORTH, adminDataGenerateButton, 20, SpringLayout.SOUTH, generateButton);
		springLayout.putConstraint(SpringLayout.WEST, adminDataGenerateButton, 0, SpringLayout.WEST, generateButton);
		add(adminDataGenerateButton);

		springLayout.putConstraint(SpringLayout.NORTH, transBtnWebPageDef, 20, SpringLayout.SOUTH, adminDataGenerateButton);
		springLayout.putConstraint(SpringLayout.WEST, transBtnWebPageDef, 0, SpringLayout.WEST, adminDataGenerateButton);
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
		else if(cmd.equals("�Ǘ��җp�f�[�^����")){
			Debug.out("���������݂܂�", getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName());

			boolean result = adminDataGeneration();
			if(result){
				String msg =
						japanese ?
								"�Ǘ��җp�f�[�^�𐶐����܂����B\n"
								:
									"Generation has been finished successfully.\n";
									JOptionPane.showMessageDialog(this, msg, japanese?"��������":"Success", JOptionPane.PLAIN_MESSAGE);



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
	
	
	
	public boolean adminDataGeneration(){
		String filePath = "src/generatedFiles/adminsData.txt";

		//�����f�[�^�̃��[�h���Ǘ��җp�ɕύX
		AccountManager.getInstance().setAdmins();
		// �����̃t�@�C�������݂��Ȃ����m�F����B���݂���Ȃ�㏑�����邩�₢�����A�L�����Z�����ꂽ��Ȃɂ����Ȃ�
		File confirmFile = new File(filePath);
		if(confirmFile.exists() && confirmFile.isFile()) {
			int confirm = JOptionPane.showConfirmDialog(this, filePath+"�͊��ɑ��݂��܂��B�㏑�����܂����H", "�t�@�C���̏㏑���ۑ�", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {
				//�ҏW���[�h�����ɖ߂�
				AccountManager.getInstance().setDeveloping();
				return false;
			}
		}

		Serializer serializer = new Serializer();
		serializer.executeSerialization(filePath);

		//�ҏW���[�h�����ɖ߂�
		AccountManager.getInstance().setDeveloping();
		//JOptionPane.showMessageDialog(this, filePath+"�ɍ�Ƃ�ۑ����܂����B");
		return true;
	}

	
	
	
	
	
	
	public static PanelGeneration getInstance() {
		return PanelGeneration.obj;
	}


	public static void updateInstance(PanelGeneration newObject) {
		PanelGeneration.obj = newObject;
	}
}
