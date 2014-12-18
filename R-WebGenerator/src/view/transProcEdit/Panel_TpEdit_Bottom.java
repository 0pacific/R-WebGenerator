package view.transProcEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.TransitionProcess;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;
import view.transProcEdit.serviceSelect.PanelForServiceSelection;
import view.transProcEdit.tableSelect.FrameTableSelect;
import view.transProcEdit.tableSelect.PanelTableSelect;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;

import property.GeneratorProperty;

import mainFrame.MainFrame;
import utility.*;

/*
 * Singleton
 */
public class Panel_TpEdit_Bottom extends JPanel implements ActionListener,Serializable {
	private JComboBox tppTypeCombo;
	private static String[] tppTypes;
	public static String TPP_MENU_SERVICE;
	public static String TPP_MENU_TABLE_LOAD;
	public static String TPP_MENU_TABLE_UPDATE;
	public static String TPP_MENU_RECORD_CREATION;
	public static String TPP_MENU_RECORD_DELETION;
	public static String TPP_MENU_IA_ASSIGNMENT;
	private JButton additionButton;
	
	private JButton transEditButton;

	// �V���A���C�Y�{�^��
	ButtonSaveWork btnSaveWork;
	// �f�V���A���C�Y�{�^��
	ButtonLoadWork btnLoadWork;

	public SerializableSpringLayout springLayout;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static Panel_TpEdit_Bottom obj = new Panel_TpEdit_Bottom();


	
	
	

	private Panel_TpEdit_Bottom() {
		setBackground(Color.WHITE);

		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		boolean japanese = GeneratorProperty.japanese();
		Panel_TpEdit_Bottom.TPP_MENU_SERVICE = japanese ? "�T�[�r�X" : "Service";
		Panel_TpEdit_Bottom.TPP_MENU_TABLE_LOAD = japanese ? "�e�[�u���Ǐo����" : "Table Load";
		Panel_TpEdit_Bottom.TPP_MENU_TABLE_UPDATE = japanese ? "�e�[�u���X�V����" : "Table Update";
		Panel_TpEdit_Bottom.TPP_MENU_RECORD_CREATION = japanese ? "���R�[�h�쐬����" : "Record Creation";
		Panel_TpEdit_Bottom.TPP_MENU_RECORD_DELETION = japanese ? "���R�[�h�폜����" : "Record Deletion";
		Panel_TpEdit_Bottom.TPP_MENU_IA_ASSIGNMENT = japanese ? "�l�����A�T�C������" : "Individual Accessibility Assignment";
		Panel_TpEdit_Bottom.tppTypes = new String[]{
				TPP_MENU_SERVICE,
				TPP_MENU_TABLE_LOAD,
				TPP_MENU_TABLE_UPDATE, 
				TPP_MENU_RECORD_CREATION,
				TPP_MENU_RECORD_DELETION,
				TPP_MENU_IA_ASSIGNMENT
		};
		
		relocateComps();
	}

	

	
	
	/*
	 * PURPOSE :
	 * �R���|�[�l���g�Ĕz�u
	 */
	public void relocateComps() {
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		tppTypeCombo = new JComboBox(Panel_TpEdit_Bottom.tppTypes);
		springLayout.putConstraint(SpringLayout.NORTH, tppTypeCombo, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tppTypeCombo, 10, SpringLayout.WEST, this);
		add(tppTypeCombo);

		additionButton = new JButton(japanese ? "��V���ɒǉ�����" : "Add");
		additionButton.addActionListener(this);
		additionButton.setActionCommand("TPP�ǉ�");
		springLayout.putConstraint(SpringLayout.NORTH, additionButton, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.EAST, tppTypeCombo);
		add(additionButton);

		transEditButton = new JButton(japanese ? "Web�y�[�W�E�J�ڌ�����`��ʂ�" : "GO TO Web Page Definition");
		transEditButton.addActionListener(this);
		transEditButton.setActionCommand("�J�� : �y�[�W�J�ڒ�`���");
		springLayout.putConstraint(SpringLayout.NORTH, transEditButton, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, transEditButton, 30, SpringLayout.EAST, additionButton);
		add(transEditButton);

		// �f�V���A���C�Y�{�^��
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "N", this, 10);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -10);
		add(btnLoadWork);
		
		// �V���A���C�Y�{�^��
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "S", btnLoadWork, 20);
		Slpc.put(springLayout, "W", btnSaveWork, "W", btnLoadWork, 0);
		add(btnSaveWork);

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		TransitionProcess editingTransProc = Panel_TpEdit_Above.getInstance().editingTp;
		String cmd = e.getActionCommand();
		
		// TPP�̒ǉ�
		if(cmd.equals("TPP�ǉ�")) {
			String tppType = (String)tppTypeCombo.getSelectedItem();

			if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_SERVICE)) {
				FrameForServiceSelection.getInstance().setVisible(true);
				PanelForServiceSelection.getInstance().locateComps();
				MainFrame.getInstance().setEnabled(false);
				FrameForServiceSelection.getInstance().requestFocus();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_TABLE_LOAD)) {
				FrameTableSelect.getInstance().setVisible(true);
				FrameTableSelect.getInstance().setEnabled(true);
				FrameTableSelect.getInstance().setFocusable(true);
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_ADD_TABLE_READING;
				PanelTableSelect.getInstance().locateButtons();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_RECORD_CREATION)) {
				FrameTableSelect.getInstance().setVisible(true);
				FrameTableSelect.getInstance().setEnabled(true);
				FrameTableSelect.getInstance().setFocusable(true);
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_ADD_CREATE_REFLECTION;
				PanelTableSelect.getInstance().locateButtons();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_RECORD_DELETION)) {
				FrameTableSelect.getInstance().setVisible(true);
				FrameTableSelect.getInstance().setEnabled(true);
				FrameTableSelect.getInstance().setFocusable(true);
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_ADD_DELETE_REFLECTION;
				PanelTableSelect.getInstance().locateButtons();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_IA_ASSIGNMENT)) {
				// IA���t���N�V�������쐬�A�ҏW���̑J�ڃv���Z�X�֍Ō��TPP�Ƃ��Ēǉ�
				TppIAReflection iaReflection = new TppIAReflection(editingTransProc);
				editingTransProc.addTpp(iaReflection);

				// IA���t���N�V�����p�l���̍쐬�����p�l���ֈ˗�
				Panel_TpEdit_Above.getInstance().addIaReflectionPanel(iaReflection, 150, 350);
			}
			// ���ȉ��A������  DB�ւ̔��f�Ƃ���
		}
		else if(cmd.equals("�J�� : �y�[�W�J�ڒ�`���")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
		else if(cmd.equals("���o�͑I�����[�h�L�����Z��")) {
			PanelServiceArgEdit.getInstance().finishTppSelection();
		}
		
		// TPP�p�l�����Ĕz�u
		Panel_TpEdit_Above.getInstance().locateTppPanels();

		MainFrame.repaintAndValidate();
	}
	
	
	

	public void locateIoSelectionCancelButton() {
		removeAll();
		
		JButton cancelBtn = new JButton(GeneratorProperty.japanese()?"�L�����Z��":"Cancel");
		Slpc.put(springLayout, "N", cancelBtn, "N", this, 20);
		Slpc.put(springLayout, "W", cancelBtn, "W", this, 20);
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("���o�͑I�����[�h�L�����Z��");
		add(cancelBtn);

		MainFrame.repaintAndValidate();
	}
	
	
	
	public static Panel_TpEdit_Bottom getInstance() {
		return Panel_TpEdit_Bottom.obj;
	}



	public static void updateInstance(Panel_TpEdit_Bottom newObject) {
		Panel_TpEdit_Bottom.obj = newObject;
	}
}
