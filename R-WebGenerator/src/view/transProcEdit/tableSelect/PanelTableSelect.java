package view.transProcEdit.tableSelect;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.TransitionProcess;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;
import utility.*;

public class PanelTableSelect extends JPanel implements ActionListener {
	public static final int MODE_ADD_TABLE_READING = 0;
	public static final int MODE_EDIT_TABLE_READING = 1;
	public static final int MODE_ADD_CREATE_REFLECTION = 2;
	public static final int MODE_EDIT_CREATE_REFLECTION = 3;
	public static final int MODE_ADD_DELETE_REFLECTION = 4;
	public static final int MODE_EDIT_DELETE_REFLECTION = 5;
	public static final int MODE_SELECT_IA_REFLECTION_DT = 6;
	public static final int MODE_SELECT_IA_REFLECTION_AT = 7;
	public int mode;
	
	// �t���[����ł̈ʒu
	public static final int posX = 0;
	public static final int posY = 0;

	// �T�C�Y
	public static final int panelWidth = FrameTableSelect.frameWidth;
	public static final int panelHeight = FrameTableSelect.frameHeight;

	// �e�f�[�^�e�[�u���E�A�J�E���g�e�[�u����\���{�^��
	ArrayList<TableButton> buttons;
	
	// �S�{�^���̉��̃p�f�B���O
	public static final int btnPaddX = 20;

	// �ŏ��̃{�^����Y���W
	public static final int firBtnPosY = 20;

	// �{�^�����m�̊Ԋu
	public static final int btnDist = 20;

	// �{�^���̏c��
	public static final int btnHeight = 50;
	
	public JButton btnCancel;
	
	public SerializableSpringLayout springLayout;
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelTableSelect obj = new PanelTableSelect();
	
	
	
	
	private PanelTableSelect() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	public void locateButtons() {
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		TableManager tableManager = TableManager.getInstance();
		buttons = new ArrayList<TableButton>();

		// �f�[�^�e�[�u��
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			buttons.add(new TableButton(dt));
		}

		// �A�J�E���g�e�[�u��
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			buttons.add(new TableButton(at));
		}


		// �e�{�^���̔z�u
		int PADD_SIDE = 40;
		int BTN_HEIGHT = 30;
		int BTN_SPACING = 10;
		for(int i=0; i<buttons.size(); i++) {
			TableButton btn = buttons.get(i);
			btn.addActionListener(this);

			// �ꉞ�AActionCommand�͑S�Ẵ{�^���œ��ꂷ��
			// �{�^���̔��ʂ̂��߂ɂ́Ae.getSource()�Ń{�^�����擾�����낢����
			btn.setActionCommand("�e�[�u���{�^���N���b�N");

			// �z�u
			btn.setPreferredSize(new Dimension(panelWidth-PADD_SIDE-PADD_SIDE, BTN_HEIGHT));
			Slpc.put(springLayout, "N", btn, "N", this, 20+(i*(BTN_HEIGHT+BTN_SPACING)));
			Slpc.put(springLayout, "W", btn, "W", this, PADD_SIDE);
			add(btn);
		}

		// �L�����Z���{�^���̔z�u
		btnCancel = new JButton(japanese?"�L�����Z��":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20+(buttons.size()*(BTN_HEIGHT+BTN_SPACING)));
		Slpc.put(springLayout, "W", btnCancel, "W", this, PADD_SIDE);
		add(btnCancel);
		
		FrameTableSelect.repaintAndValidate();
	}

	
	
	
	public void locateDataTableButtons() {
		removeAll();
		
		boolean japanese = GeneratorProperty.japanese();

		TableManager tableManager = TableManager.getInstance();
		buttons = new ArrayList<TableButton>();

		// �f�[�^�e�[�u���̂�
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			buttons.add(new TableButton(dt));
		}

		// �e�{�^���̔z�u
		int PADD_SIDE = 40;
		int BTN_HEIGHT = 30;
		int BTN_SPACING = 10;
		for(int i=0; i<buttons.size(); i++) {
			TableButton btn = buttons.get(i);
			btn.addActionListener(this);

			// �ꉞ�AActionCommand�͑S�Ẵ{�^���œ��ꂷ��
			// �{�^���̔��ʂ̂��߂ɂ́Ae.getSource()�Ń{�^�����擾�����낢����
			btn.setActionCommand("�e�[�u���{�^���N���b�N");

			// �z�u
			btn.setPreferredSize(new Dimension(this.panelWidth-(2*PADD_SIDE),BTN_HEIGHT));
			Slpc.put(springLayout, "N", btn, "N", this, 20+(i*(BTN_HEIGHT+BTN_SPACING)));
			Slpc.put(springLayout, "W", btn, "W", this, PADD_SIDE);
			add(btn);
		}

		// �L�����Z���{�^���̔z�u
		btnCancel = new JButton(japanese?"�L�����Z��":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20+(buttons.size()*(BTN_HEIGHT+BTN_SPACING)));
		Slpc.put(springLayout, "W", btnCancel, "W", this, PADD_SIDE);
		add(btnCancel);

		FrameTableSelect.repaintAndValidate();
	}
	

	
	
	public void locateAccountTableButtons() {
		removeAll();
		
		boolean japanese = GeneratorProperty.japanese();

		TableManager tableManager = TableManager.getInstance();
		buttons = new ArrayList<TableButton>();

		// �A�J�E���g�e�[�u���̂�
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			buttons.add(new TableButton(at));
		}

		// �e�{�^���̔z�u
		int PADD_SIDE = 40;
		int BTN_HEIGHT = 30;
		int BTN_SPACING = 10;
		for(int i=0; i<buttons.size(); i++) {
			TableButton btn = buttons.get(i);
			btn.addActionListener(this);

			// �ꉞ�AActionCommand�͑S�Ẵ{�^���œ��ꂷ��
			// �{�^���̔��ʂ̂��߂ɂ́Ae.getSource()�Ń{�^�����擾�����낢����
			btn.setActionCommand("�e�[�u���{�^���N���b�N");

			// �z�u
			btn.setPreferredSize(new Dimension(this.panelWidth-(2*PADD_SIDE),BTN_HEIGHT));
			Slpc.put(springLayout, "N", btn, "N", this, 20+(i*(BTN_HEIGHT+BTN_SPACING)));
			Slpc.put(springLayout, "W", btn, "W", this, PADD_SIDE);
			add(btn);
		}

		// �L�����Z���{�^���̔z�u
		btnCancel = new JButton(japanese?"�L�����Z��":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20+(buttons.size()*(BTN_HEIGHT+BTN_SPACING)));
		Slpc.put(springLayout, "W", btnCancel, "W", this, PADD_SIDE);
		add(btnCancel);

		FrameTableSelect.repaintAndValidate();
	}

	
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEvent�n���h��
	 */
	public void actionPerformed(ActionEvent e) {
		TransitionProcess editingTransProc = Panel_TpEdit_Above.getInstance().editingTp;
		String cmd = e.getActionCommand();
		
		// �e�[�u����\�������ꂩ�̃{�^�����N���b�N���ꂽ
		if(cmd.equals("�e�[�u���{�^���N���b�N")) {
			// �N���b�N���ꂽ�{�^�����擾
			TableButton btn = (TableButton)e.getSource();

			// �e�[�u�����[�f�B���O�ǉ����[�h�̏ꍇ
			if(mode==PanelTableSelect.MODE_ADD_TABLE_READING) {
				// �J�ڃv���Z�X��`��ʂ̍����p�l���ցA�e�[�u����`����
				// �i�������ł̓e�[�u�����[�f�B���O�̍쐬�E�J�ڃv���Z�X�ւ̒ǉ��E�p�l���ǉ����s���j
				Panel_TpEdit_Above.getInstance().addTableReadingAndTableReadingPanelByTable(btn.getTable());
			}
			// Create���t���N�V������TFD��ҏW���郂�[�h�̏ꍇ
			else if(mode==PanelTableSelect.MODE_ADD_CREATE_REFLECTION) {
				TppCreateReflection reflection = new TppCreateReflection(editingTransProc, btn.getTable());
				editingTransProc.addTpp(reflection);	// �Ƃ肠�����Ō����TPP�Ƃ��Ēǉ��B����Create���t���N�V������ҏW����x�ɁA�K�؂Ȉʒu�ֈړ�������

				// �쐬����Create���t���N�V������\���p�l�������p�l���̑J�ڃv���Z�X�}�֒ǉ�
				Panel_TpEdit_Above.getInstance().addCreateReflectionPanel(reflection, 100, 200);
			}
			// IA���t���N�V�����̃f�[�^�e�[�u���ǉ����[�h�̏ꍇ
			else if(mode==PanelTableSelect.MODE_SELECT_IA_REFLECTION_DT) {
				DataTable dataTable = (DataTable)btn.getTable();

				// �I�����ꂽ�f�[�^�e�[�u����IA���t���N�V�����ҏW�p�l���ɕ񍐂��A��͌������ł���Ă��炤
				PanelIaReflectionEdit.getInstance().informSelectedDataTable(dataTable);
			}
			// IA���t���N�V�����̃A�J�E���g�e�[�u���ǉ����[�h�̏ꍇ
			else if(mode==PanelTableSelect.MODE_SELECT_IA_REFLECTION_AT) {
				AccountTable accountTable = (AccountTable)btn.getTable();

				// �I�����ꂽ�A�J�E���g�e�[�u����IA���t���N�V�����ҏW�p�l���ɕ񍐂��A��͌������ł���Ă��炤
				PanelIaReflectionEdit.getInstance().informSelectedAccountTable(accountTable);
			}

			// TPP�p�l�����Ĕz�u
			Panel_TpEdit_Above.getInstance().locateTppPanels();
		}
		else if(cmd.equals("�L�����Z��")) {
			// �������Ȃ��i�t���[���s�������͉��̕��ł��j
		}
		else {
			Debug.error("�s����ActionCommand�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		MainFrame.repaintAndValidate();
		FrameTableSelect.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
		
		// �e�[�u���I���t���[����s�����A�W�F�l���[�^�t���[���𑀍�\��
		FrameTableSelect.getInstance().setVisible(false);
		FrameTableSelect.getInstance().setEnabled(false);
		FrameTableSelect.getInstance().setFocusable(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();
	}
	


	public static PanelTableSelect getInstance() {
		return PanelTableSelect.obj;
	}




	public static void updateInstance(PanelTableSelect newObject) {
		PanelTableSelect.obj = newObject;
	}
}
