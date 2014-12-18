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
import tpp.portTrans.PortTrans;
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
import view.transProcEdit.tableSelect.FrameTableSelect;
import view.transProcEdit.tableSelect.PanelTableSelect;
import view.webPageDefinition.*;
import webPage.*;





public class PanelIaReflectionEdit extends JPanel implements ActionListener {
	public TppIAReflection reflection;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	/*
	 * ���C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelIaReflectionEdit obj = new PanelIaReflectionEdit();
	
	
	
	
	private PanelIaReflectionEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Color.WHITE);
	}

	
	
	
	
	public void locateCompsForIaReflectionEdit(TppIAReflection iaReflection) {
		this.reflection = iaReflection;
		
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();

		int PADD_LEFT = 20;
		
		// �e�L�X�g�y�C��
		JTextPane textPaneTitle = new JTextPane();
		textPaneTitle.setText(japanese ? "�l�����A�T�C�������̒�`" : "Individual Accessibility Assignment");
		Slpc.put(springLayout, "N", textPaneTitle, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneTitle, "W", this, PADD_LEFT);
		add(textPaneTitle);

		// �e�L�X�g�y�C���i�f�[�^�e�[�u�����j
		JTextPane textPaneDtName = new JTextPane();
		if(reflection.dataTable==null) {
			textPaneDtName.setText(japanese ? "�I�f�[�^�e�[�u���F����`" : "! Data Table : Undefined");
		}
		else {
			textPaneDtName.setText((japanese ? "�f�[�^�e�[�u���F" : "Data Table : ") + reflection.dataTable.getTableName());
		}
		Slpc.put(springLayout, "N", textPaneDtName, "S", textPaneTitle, 40);
		Slpc.put(springLayout, "W", textPaneDtName, "W", this, PADD_LEFT);
		add(textPaneDtName);

		// �f�[�^�e�[�u����I�����邽�߂̃{�^��
		JButton btnDtSelect = new JButton(japanese ? "�f�[�^�e�[�u����I��" : "Select Data Table");
		btnDtSelect.addActionListener(this);
		btnDtSelect.setActionCommand("�f�[�^�e�[�u���I��");
		Slpc.put(springLayout, "N", btnDtSelect, "S", textPaneDtName, 10);
		Slpc.put(springLayout, "W", btnDtSelect, "W", this, PADD_LEFT);
		add(btnDtSelect);
		
		JTextPane textPaneDtTfd = new JTextPane();
		textPaneDtTfd.setText(japanese ? "�f�[�^�e�[�u���\�f�[�^" : "Tabular Form Data of Data Table");
		Slpc.put(springLayout, "N", textPaneDtTfd, "S", btnDtSelect, 20);
		Slpc.put(springLayout, "W", textPaneDtTfd, "W", this, PADD_LEFT);
		add(textPaneDtTfd);

		JButton btnDtTfdEdit = new JButton(japanese ? "���}����I��" : "Select in Diagram");
		btnDtTfdEdit.addActionListener(this);
		btnDtTfdEdit.setActionCommand("�f�[�^�e�[�u��TFD�ҏW�{�^���N���b�N");
		Slpc.put(springLayout, "N", btnDtTfdEdit, "S", textPaneDtTfd, 20);
		Slpc.put(springLayout, "W", btnDtTfdEdit, "W", this, PADD_LEFT);
		add(btnDtTfdEdit);

		// �����f�[�^�e�[�u��������`�Ȃ�΁A�f�[�^�e�[�u��TFD��I���ł��Ȃ��悤�Ƀ{�^���𖳌���
		if(reflection.dataTable==null) {
			btnDtTfdEdit.setEnabled(false);
		}
		
		// �f�[�^�e�[�u��TFD����`�ς��ۂ��������e�L�X�g�y�C��
		JTextPane textPaneDtTfdDefined = new JTextPane();
		// �f�[�^�e�[�u��TFD����`�ς��ۂ����`�F�b�N�i��`�ςȂ�e�[�u�����[�f�B���OorCreate�t�H�[�����t���N�V����orCreate���t���N�V�������A����`�Ȃ�null���Ԃ�͂��ł���j
		TransitionProcessPart inputTpp = reflection.transProc.portTransManager.getInputTppToTppPortIfExists(reflection.getDataTableTfdInputPort());
		if(inputTpp==null) {
			textPaneDtTfdDefined.setText(japanese ? "�I����`" : "!Undefined");
		}
		else if(inputTpp instanceof TppTableReading || inputTpp instanceof TppCreateReflection || inputTpp instanceof TppCreateFormReflection) {
			textPaneDtTfdDefined.setText(japanese ? "��`��" : "Defined");
		}
		else {
			Debug.error("Ia���t���N�V�����ɓn���f�[�^�e�[�u��TFD�Ƃ��āA�e�[�u�����[�f�B���O�ł�Create���t���N�V�����ł�Create�t�H�[�����t���N�V�����ł��Ȃ�TPP����`����Ă��܂��B");
			return;
		}
		Slpc.put(springLayout, "N", textPaneDtTfdDefined, "N", btnDtTfdEdit, 0);
		Slpc.put(springLayout, "W", textPaneDtTfdDefined, "E", btnDtTfdEdit, 20);
		add(textPaneDtTfdDefined);

		
		
		// �e�L�X�g�y�C���i�A�J�E���g�e�[�u�����j
		JTextPane textPaneAtName = new JTextPane();
		if(reflection.accountTable==null) {
			textPaneAtName.setText(japanese ? "�I�A�J�E���g�e�[�u���F����`" : "! Account Table : Undefined");
		}
		else {
			textPaneAtName.setText((japanese ? "�A�J�E���g�e�[�u���F" : "Account Table : ") + reflection.accountTable.getTableName());
		}
		Slpc.put(springLayout, "N", textPaneAtName, "S", textPaneDtTfdDefined, 20);
		Slpc.put(springLayout, "W", textPaneAtName, "W", this, PADD_LEFT);
		add(textPaneAtName);

		// �A�J�E���g�e�[�u����I�����邽�߂̃{�^��
		JButton btnAtSelect = new JButton(japanese ? "�A�J�E���g�e�[�u����I��" : "Select Account Table");
		btnAtSelect.addActionListener(this);
		btnAtSelect.setActionCommand("�A�J�E���g�e�[�u���I��");
		Slpc.put(springLayout, "N", btnAtSelect, "S", textPaneAtName, 10);
		Slpc.put(springLayout, "W", btnAtSelect, "W", this, PADD_LEFT);
		add(btnAtSelect);
		
		JTextPane textPaneAtTfd = new JTextPane();
		textPaneAtTfd.setText(japanese ? "�A�J�E���g�e�[�u���\�f�[�^" : "Tabular Form Data of Account Table");
		Slpc.put(springLayout, "N", textPaneAtTfd, "S", btnAtSelect, 20);
		Slpc.put(springLayout, "W", textPaneAtTfd, "W", this, PADD_LEFT);
		add(textPaneAtTfd);

		JButton btnAtTfdEdit = new JButton(japanese ? "���}����I��" : "Select in Diagram");
		btnAtTfdEdit.addActionListener(this);
		btnAtTfdEdit.setActionCommand("�A�J�E���g�e�[�u��TFD�ҏW�{�^���N���b�N");
		Slpc.put(springLayout, "N", btnAtTfdEdit, "S", textPaneAtTfd, 20);
		Slpc.put(springLayout, "W", btnAtTfdEdit, "W", this, PADD_LEFT);
		add(btnAtTfdEdit);

		// �����A�J�E���g�e�[�u��������`�Ȃ�΁A�A�J�E���g�e�[�u��TFD��I���ł��Ȃ��悤�Ƀ{�^���𖳌���
		if(reflection.accountTable==null) {
			btnAtTfdEdit.setEnabled(false);
		}
		
		// �A�J�E���g�e�[�u��TFD����`�ς��ۂ��������e�L�X�g�y�C��
		JTextPane textPaneAtTfdDefined = new JTextPane();
		// �A�J�E���g�e�[�u��TFD����`�ς��ۂ����`�F�b�N�i��`�ςȂ�e�[�u�����[�f�B���OorCreate�t�H�[�����t���N�V����orCreate���t���N�V�������A����`�Ȃ�null���Ԃ�͂��ł���j
		TransitionProcessPart inputAtTfd = reflection.transProc.portTransManager.getInputTppToTppPortIfExists(reflection.getAccountTableTfdInputPort());
		if(inputAtTfd==null) {
			textPaneAtTfdDefined.setText(japanese ? "�I����`" : "!Undefined");
		}
		else if(inputAtTfd instanceof TppTableReading || inputAtTfd instanceof TppCreateReflection || inputAtTfd instanceof TppCreateFormReflection) {
			textPaneAtTfdDefined.setText(japanese ? "��`��" : "Defined");
		}
		else {
			Debug.error("Ia���t���N�V�����ɓn���A�J�E���g�e�[�u��TFD�Ƃ��āA�e�[�u�����[�f�B���O�ł�Create���t���N�V�����ł�Create�t�H�[�����t���N�V�����ł��Ȃ�TPP����`����Ă��܂��B");
			return;
		}
		Slpc.put(springLayout, "N", textPaneAtTfdDefined, "N", btnAtTfdEdit, 0);
		Slpc.put(springLayout, "W", textPaneAtTfdDefined, "E", btnAtTfdEdit, 20);
		add(textPaneAtTfdDefined);

		
		
		// �폜�{�^��
		JButton buttonRemoveIaReflection = new JButton(japanese ? "�폜" : "Delete");
		buttonRemoveIaReflection.addActionListener(this);
		buttonRemoveIaReflection.setActionCommand("IA���t���N�V�����폜");
		Slpc.put(springLayout, "N", buttonRemoveIaReflection, "S", btnAtTfdEdit, 40);
		Slpc.put(springLayout, "W", buttonRemoveIaReflection, "W", this, PADD_LEFT);
		add(buttonRemoveIaReflection);
		
		
		
		
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("�f�[�^�e�[�u���I��")) {
			FrameTableSelect.getInstance().setVisible(true);
			FrameTableSelect.getInstance().setEnabled(true);
			FrameTableSelect.getInstance().setFocusable(true);
			MainFrame.getInstance().setEnabled(false);
			MainFrame.getInstance().setFocusable(false);

			PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_SELECT_IA_REFLECTION_DT;
			PanelTableSelect.getInstance().locateDataTableButtons();
		}
		else if(cmd.equals("�A�J�E���g�e�[�u���I��")) {
			FrameTableSelect.getInstance().setVisible(true);
			FrameTableSelect.getInstance().setEnabled(true);
			FrameTableSelect.getInstance().setFocusable(true);
			MainFrame.getInstance().setEnabled(false);
			MainFrame.getInstance().setFocusable(false);

			PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_SELECT_IA_REFLECTION_AT;
			PanelTableSelect.getInstance().locateAccountTableButtons();
		}
		else if(cmd.equals("�f�[�^�e�[�u��TFD�ҏW�{�^���N���b�N")) {
			this.locateCancelButton();
			Panel_TpEdit_Bottom.getInstance().removeAll();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD;
		}
		else if(cmd.equals("�A�J�E���g�e�[�u��TFD�ҏW�{�^���N���b�N")) {
			this.locateCancelButton();
			Panel_TpEdit_Bottom.getInstance().removeAll();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD;
		}
		else if(cmd.equals("TFD�I���̃L�����Z��")) {
			this.locateCompsForIaReflectionEdit(reflection);
			Panel_TpEdit_Bottom.getInstance().relocateComps();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		}
		else if(cmd.equals("IA���t���N�V�����폜")) {
			int confirm = JOptionPane.showConfirmDialog(this, "�l�����A�T�C���������폜���܂��B��낵���ł����H", "�l�����A�T�C�������̍폜", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}

			/*
			 * IA���t���N�V�������폜���邱�Ƃ�����
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
	}

	
	

	public void informSelectedDataTable(DataTable dt) {
		// �f�[�^�e�[�u�����i�āj�ݒ�
		reflection.dataTable = dt;

		// �������f�[�^�e�[�u��TFD����`���Ă���̂ł���΁A����͔j������i���̃f�[�^�e�[�u��TFD����IA���t���N�V������0�ԓ���TPP�|�[�g�ւ̃|�[�g�g�����X�~�b�V�������폜����j
		boolean remove = reflection.removeDataTableTfdInputPortTransIfExists();

		this.locateCompsForIaReflectionEdit(reflection);
		MainFrame.repaintAndValidate();
	}
	public void informSelectedAccountTable(AccountTable at) {
		// �A�J�E���g�e�[�u�����i�āj�ݒ�
		reflection.accountTable = at;

		// �������A�J�E���g�e�[�u��TFD����`���Ă���̂ł���΁A����͔j������i���̃A�J�E���g�e�[�u��TFD����IA���t���N�V������1�ԓ���TPP�|�[�g�ւ̃|�[�g�g�����X�~�b�V�������폜����j
		boolean remove = reflection.removeAccountTableTfdInputPortTransIfExists();

		this.locateCompsForIaReflectionEdit(reflection);
		MainFrame.repaintAndValidate();
	}
	public void informSelectedDtTfd(TfdOutputer outerTpp) {
		DataPort outPort = ((TransitionProcessPart)outerTpp).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;
		
		// �������f�[�^�e�[�u��TFD����`���Ă������̂ł���΁A����͔j������i���̃f�[�^�e�[�u��TFD����IA���t���N�V�����̃f�[�^�e�[�u��TFD���̓|�[�g�ւ̃|�[�g�g�����X�~�b�V�����폜�j
		boolean remove = reflection.removeDataTableTfdInputPortTransIfExists();

		// �ҏW���̑J�ڃv���Z�X�ɁA�u�I�����ꂽTPP�̏o��->Ia���t���N�V�����̃f�[�^�e�[�u��TFD����TPP�|�[�g�v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		portTransManager.addPortTrans(outPort, this.reflection.getDataTableTfdInputPort());

		// �����p�l���̃��[�h��ʏ�ɖ߂��A�{�p�l���≺���p�l�����Ĕz�u����
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		this.locateCompsForIaReflectionEdit(this.reflection);
		Panel_TpEdit_Bottom.getInstance().relocateComps();

		MainFrame.repaintAndValidate();
	}
	public void informSelectedAtTfd(TfdOutputer outerTpp) {
		DataPort outPort = ((TransitionProcessPart)outerTpp).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;
		
		// �������A�J�E���g�e�[�u��TFD����`���Ă������̂ł���΁A����͔j������i���̃A�J�E���g�e�[�u��TFD����IA���t���N�V�����̃A�J�E���g�e�[�u��TFD���̓|�[�g�ւ̃|�[�g�g�����X�~�b�V�����폜�j
		boolean remove = reflection.removeAccountTableTfdInputPortTransIfExists();

		// �ҏW���̑J�ڃv���Z�X�ɁA�u�I�����ꂽTPP�̏o��->Ia���t���N�V�����̃f�[�^�e�[�u��TFD����TPP�|�[�g�v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		portTransManager.addPortTrans(outPort, this.reflection.getAccountTableTfdInputPort());

		// �����p�l���̃��[�h��ʏ�ɖ߂��A�{�p�l���≺���p�l�����Ĕz�u����
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		this.locateCompsForIaReflectionEdit(this.reflection);
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
		Slpc.put(springLayout, "N", btnCancel, "N", this, 150);
		Slpc.put(springLayout, "W", btnCancel, "W", this, 20);
		add(btnCancel);

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	




	public static PanelIaReflectionEdit getInstance() {
		return PanelIaReflectionEdit.obj;
	}




	public static void updateInstance(PanelIaReflectionEdit newObject) {
		PanelIaReflectionEdit.obj = newObject;
	}
}
