package view.transProcEdit.rightSubPanels;

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
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.portTrans.PortTransManager;
import transition.*;
import utility.*;
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
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;
import view.webPageDefinition.*;
import webPage.*;





public class PanelCreateReflectionEdit extends JPanel implements ActionListener {
	public TppCreateReflection reflection;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	/*
	 * ���C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelCreateReflectionEdit obj = new PanelCreateReflectionEdit();
	
	
	
	
	private PanelCreateReflectionEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Color.WHITE);
	}

	
	
	
	
	public void locateCompsForCreateReflectionEdit(TppCreateReflection createReflection) {
		boolean japanese = GeneratorProperty.japanese();
		this.reflection = createReflection;
		
		removeAll();

		int PADD_LEFT = 20;
		
		// �e�L�X�g�G���A
		JTextPane textPaneTitle = new JTextPane();
		textPaneTitle.setFont(new Font("Serif", Font.PLAIN, 17));
		textPaneTitle.setText(japanese ? "���R�[�h�쐬�����̒�`" : "Record Creation Configuration");
		Slpc.put(springLayout, "N", textPaneTitle, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneTitle, "W", this, PADD_LEFT);
		add(textPaneTitle);

		// �e�L�X�g�y�C���i�e�[�u�����j
		JTextPane textPaneTableName = new JTextPane();
		textPaneTableName.setFont(new Font("Serif", Font.PLAIN, 14));
		textPaneTableName.setText(japanese ? "1. �e�[�u��\n\n    " + reflection.table.getTableName() : "1. Table\n\n    " + reflection.table.getTableName());
		Slpc.put(springLayout, "N", textPaneTableName, "S", textPaneTitle, 40);
		Slpc.put(springLayout, "W", textPaneTableName, "W", this, PADD_LEFT);
		add(textPaneTableName);

		/*
		 * ���������F�e�[�u����ύX����ҏW�{�^�����u������
		 */

		JTextPane textPaneTfd = new JTextPane();
		textPaneTfd.setFont(new Font("Serif", Font.PLAIN, 14));
		String text = japanese ? "2. �\�f�[�^ : " : "2. Tabular Data : ";
		// TFD����`�ς��ۂ����`�F�b�N�i��`�ςȂ�TFD�o�͌^TPP���A����`�Ȃ�null���Ԃ�͂��ł���j
		TransitionProcessPart inputTpp = reflection.transProc.portTransManager.getInputTppToTppPortIfExists(reflection.getTfdInputPort());
		if(inputTpp==null)
			text += japanese ? "����`" : "Undefined";
		else if(inputTpp instanceof TfdOutputer)
			text += japanese ? "��`��" : "Defined";
		else {
			Debug.error("Create���t���N�V�����ɓn��TFD�Ƃ��āATFD�o�͌^�łȂ�TPP����`����Ă��܂��B");
			Debug.informError();
			return;
		}
		textPaneTfd.setText(text);
		Slpc.put(springLayout, "N", textPaneTfd, "S", textPaneTableName, 40);
		Slpc.put(springLayout, "W", textPaneTfd, "W", this, PADD_LEFT);
		add(textPaneTfd);

		
		
		JButton btnTfdEdit = new JButton(GeneratorProperty.japanese()?"���}����o�͌���I��":"Select in Diagram");
		btnTfdEdit.addActionListener(this);
		btnTfdEdit.setActionCommand("TFD�ҏW�{�^���N���b�N");
		Slpc.put(springLayout, "N", btnTfdEdit, "S", textPaneTfd, 10);
		Slpc.put(springLayout, "W", btnTfdEdit, "W", this, PADD_LEFT+20);
		add(btnTfdEdit);
		
		
		// �폜�{�^��
		JButton buttonRemoveCreateReflection = new JButton(japanese?"�폜":"Delete");
		buttonRemoveCreateReflection.addActionListener(this);
		buttonRemoveCreateReflection.setActionCommand("Create���t���N�V�����폜");
		Slpc.put(springLayout, "N", buttonRemoveCreateReflection, "S", btnTfdEdit, 40);
		Slpc.put(springLayout, "W", buttonRemoveCreateReflection, "W", this, PADD_LEFT);
		add(buttonRemoveCreateReflection);
		
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		if(cmd.equals("TFD�ҏW�{�^���N���b�N")) {
			this.locateCancelButton();
			Panel_TpEdit_Bottom.getInstance().removeAll();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD;

			
		}
		else if(cmd.equals("TFD�I���̃L�����Z��")) {
			this.locateCompsForCreateReflectionEdit(reflection);
			Panel_TpEdit_Bottom.getInstance().relocateComps();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		}
		else if(cmd.equals("Create���t���N�V�����폜")) {
			int confirm = JOptionPane.showConfirmDialog(this, japanese?"���R�[�h�쐬�������폜���܂����H":"Delete this create reflection?", japanese?"���R�[�h�쐬�����̍폜":"Deletion of create reflection", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}

			/*
			 * Create���t���N�V�������폜���邱�Ƃ�����
			 */
			
			reflection.removeFromTransProc();
		}
		else {
			JOptionPane.showMessageDialog(this, "�G���[���������܂����B");
			Debug.error("�z��O�̃A�N�V�����R�}���h�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}

		Panel_TpEdit_Above.getInstance().locateTppPanels();

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}

	
	

	/*
	 * PURPOSE :
	 * Panel_TpEdit_Left�ɂ����ā@TFD�o�͌^TPP���I�����ꂽ�Ƃ��ɍs������
	 */
	public void informSelectedTfdForCreateReflection(TfdOutputer outerTpp) {
		DataPort outPort = ((TransitionProcessPart)outerTpp).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;
		
		// �ҏW���̑J�ڃv���Z�X�ɁA�u�I�����ꂽTPP�̏o��->Create���t���N�V�����̓���TPP�|�[�g�i�P��������j�v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		portTransManager.addPortTrans(outPort, this.reflection.getTfdInputPort());

		// �����p�l���̃��[�h��ʏ�ɖ߂��A�{�p�l���≺���p�l�����Ĕz�u����
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		this.locateCompsForCreateReflectionEdit(this.reflection);
		Panel_TpEdit_Bottom.getInstance().relocateComps();

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * �S�R���|�[�l���g���������ATFD�I�𒆂̏�Ԃ��������L�����Z���{�^���݂̂�z�u
	 * TFD�I�����[�h�Ɉڂ�Ƃ��Ɏ��s
	 */
	public void locateCancelButton() {
		removeAll();
		
		JButton btnCancel = new JButton(GeneratorProperty.japanese()?"�L�����Z��":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("TFD�I���̃L�����Z��");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20);
		Slpc.put(springLayout, "W", btnCancel, "W", this, 20);
		add(btnCancel);

		MainFrame.repaintAndValidate();
	}
	




	public static PanelCreateReflectionEdit getInstance() {
		return PanelCreateReflectionEdit.obj;
	}
	
	
	
	
	
	public static void updateInstance(PanelCreateReflectionEdit newObject) {
		PanelCreateReflectionEdit.obj = newObject;
	}

}
