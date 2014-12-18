package view.tableList;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import role.NormalRole;
import role.RoleManager;
import table.AccountTable;
import table.DataTable;
import table.TableManager;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.tableList.accountTableDef.FrameAccountTableDefinition;
import view.tableList.accountTableDef.PanelAccountTableDefinition;
import utility.*;

public class PanelTableListBottom extends JPanel implements ActionListener,Serializable {
	// �u�I�𒆂̃f�[�^�e�[�u����ҏW�v�{�^��
	private JButton dtEditButton;

	public SerializableSpringLayout springLayout;

	// �f�[�^�e�[�u���ǉ��{�^��
	public JButton btnDtAdd;

	// �u�I�𒆂̃A�J�E���g�e�[�u����ҏW�v�{�^��
	private JButton btnAtEdit;

	// �A�J�E���g�e�[�u�������̓e�L�X�g�t�B�[���h
	public JTextField tfAtName;
	
	// ���[���I���`�F�b�N�{�b�N�X
	public ArrayList<JCheckBox> cbArrayRoleSelect;
	
	// �A�J�E���g�e�[�u���ǉ��{�^��
	public JButton btnAtAdd;

	public static final Color backGroundColor = Color.white;
	
	// �V���A���C�Y�{�^��
	ButtonSaveWork btnSaveWork;
	// �f�V���A���C�Y�{�^��
	ButtonLoadWork btnLoadWork;

	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelTableListBottom obj = new PanelTableListBottom();

	
	
	
	
	
	
	private PanelTableListBottom() {
		// �p�l���̃��C�A�E�g
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	
	public void locateCompsPanelTableListBottom() {
		boolean japanese = GeneratorProperty.japanese();		
		
		removeAll();
		
		// �u�I�𒆂̃f�[�^�e�[�u����ҏW�v�{�^��
		dtEditButton = japanese ? new JButton("�I�𒆂̃f�[�^�e�[�u����ҏW") : new JButton("Edit Data Table");
		dtEditButton.addActionListener(this);
		dtEditButton.setActionCommand("�f�[�^�e�[�u���ҏW");
		springLayout.putConstraint(SpringLayout.NORTH, dtEditButton, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, dtEditButton, 20, SpringLayout.WEST, this);
		add(dtEditButton);

		// �f�[�^�e�[�u���ǉ��{�^��
		btnDtAdd = japanese ? new JButton("�V���ȃf�[�^�e�[�u�����쐬") : new JButton("Create Data Table");
		springLayout.putConstraint(SpringLayout.NORTH, btnDtAdd, 30, SpringLayout.SOUTH, dtEditButton);
		springLayout.putConstraint(SpringLayout.WEST, btnDtAdd, 0, SpringLayout.WEST, dtEditButton);
		btnDtAdd.addActionListener(this);
		btnDtAdd.setActionCommand("�f�[�^�e�[�u���ǉ�");
		add(btnDtAdd);

		RoleManager roleManager = RoleManager.getInstance();

		// �u�I�𒆂̃A�J�E���g�e�[�u����ҏW�v�{�^��
		btnAtEdit = japanese ? new JButton("�I�𒆂̃A�J�E���g�e�[�u����ҏW") : new JButton("Edit Account Table");
		btnAtEdit.addActionListener(this);
		btnAtEdit.setActionCommand("�A�J�E���g�e�[�u���ҏW");
		springLayout.putConstraint(SpringLayout.NORTH, btnAtEdit, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnAtEdit, 100, SpringLayout.EAST, dtEditButton);
		add(btnAtEdit);
		if(TableManager.getInstance().getAccountTableNum()==0) {
			// �܂��A�J�E���g�e�[�u�����P���Ȃ��ꍇ�̓N���b�N�ł��Ȃ�
			btnAtEdit.setEnabled(false);
		}
		
		// �A�J�E���g�e�[�u���������Ȃ��m�[�}�����[�����c���Ă���ꍇ -> �A�J�E���g�e�[�u���ǉ��{�^���\��
		btnAtAdd = japanese ? new JButton("�V���ȃA�J�E���g�e�[�u�����쐬") : new JButton("Create Account Table");
		Slpc.put(springLayout, "N", btnAtAdd, "S", btnAtEdit, 30);
		Slpc.put(springLayout, "W", btnAtAdd, "W", btnAtEdit, 0);
		btnAtAdd.addActionListener(this);
		btnAtAdd.setActionCommand("�A�J�E���g�e�[�u���ǉ�");
		add(btnAtAdd);

		// �S�Ẵm�[�}�����[�����A�J�E���g�e�[�u���������Ă��� -> �A�J�E���g�e�[�u����V�����ǉ��ł��Ȃ����Ƃ�\�����A�A�J�E���g�e�[�u���ǉ��{�^���𖳌���
		if(!roleManager.someNormalRolesHaveNoAt()) {
			Debug.out("�A�J�E���g�e�[�u���������Ȃ��m�[�}�����[���́A���ݑ��݂��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			
			// �e�L�X�g�y�C��
			JTextPane textPaneAllNormalRoleAtDefined = new JTextPane();
			textPaneAllNormalRoleAtDefined.setText(japanese ? "�S�Ẵ��[���i�Q�X�g���[�������j�ɑ΂�\n�A�J�E���g�e�[�u������`����Ă��܂��B" : "All roles (except Guest) have account tables.");
			Slpc.put(springLayout, "N", textPaneAllNormalRoleAtDefined, "S", btnAtAdd, 20);
			Slpc.put(springLayout, "W", textPaneAllNormalRoleAtDefined, "W", btnAtAdd, 0);
			add(textPaneAllNormalRoleAtDefined);

			// �A�J�E���g�e�[�u���ǉ��{�^��������
			btnAtAdd.setEnabled(false);
		}

		// �ړ��{�^���F�y�[�W�J�ڒ�`���
		JButton btnGoTrans = japanese ? new JButton("Web�y�[�W�E�J�ڌ�����`��ʂ�") : new JButton("Go To Web Page Definition");
		btnGoTrans.addActionListener(this);
		btnGoTrans.setActionCommand("�J�� : �y�[�W�J�ڒ�`���");
		springLayout.putConstraint(SpringLayout.NORTH, btnGoTrans, 300, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnGoTrans, 20, SpringLayout.WEST, this);
		add(btnGoTrans);

		// �ړ��{�^���F���[����`���
		JButton btnGoRoleEdit = japanese ? new JButton("���[����`��ʂ�") : new JButton("Go To Role Definition");
		springLayout.putConstraint(SpringLayout.SOUTH, btnGoRoleEdit, 0, SpringLayout.SOUTH, btnGoTrans);
		springLayout.putConstraint(SpringLayout.WEST, btnGoRoleEdit, 20, SpringLayout.EAST, btnGoTrans);
		btnGoRoleEdit.addActionListener(this);
		btnGoRoleEdit.setActionCommand("�J�� : ���[����`���");
		add(btnGoRoleEdit);

		// �ړ��{�^���F������`���
		JButton btnGoAuthEdit = japanese ? new JButton("�A�N�Z�X������`��ʂ�") : new JButton("Go To Accessibility Definition");
		springLayout.putConstraint(SpringLayout.SOUTH, btnGoAuthEdit, 0, SpringLayout.SOUTH, btnGoRoleEdit);
		springLayout.putConstraint(SpringLayout.WEST, btnGoAuthEdit, 20, SpringLayout.EAST, btnGoRoleEdit);
		btnGoAuthEdit.addActionListener(this);
		btnGoAuthEdit.setActionCommand("�J�� : ������`���");
		add(btnGoAuthEdit);
		
		// �V���A���C�Y�{�^��
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", this, 300);
		Slpc.put(springLayout, "E", btnSaveWork, "E", this, -20);
		add(btnSaveWork);

		// �f�V���A���C�Y�{�^��
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", btnSaveWork, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		// �u�I�𒆂̃f�[�^�e�[�u����ҏW�v�{�^���N���b�N -> �ړ�
		if(cmd.equals("�f�[�^�e�[�u���ҏW")) {
			// �I�𒆂̃f�[�^�e�[�u���擾
			int selectedDtIndex = PanelTableListAbove.getInstance().dataTableList.getSelectedRow();
			DataTable selectedDt = TableManager.getInstance().getDataTable(selectedDtIndex);

			// �ړ�
			MainFrame.getInstance().shiftToFieldList(selectedDt);
		}
		// �f�[�^�e�[�u���ǉ��{�^���N���b�N
		else if(cmd.equals("�f�[�^�e�[�u���ǉ�")) {
			while(true) {
				String newDataTableName = JOptionPane.showInputDialog(this, japanese?"�f�[�^�e�[�u���̖��̂���͂��ĉ������B":"Input data table's name.");
				if(newDataTableName==null) {	// �L�����Z��
					return;
				}
				else if(newDataTableName.equals("")) {	// ���
					JOptionPane.showMessageDialog(this, "���������͂��ĉ������B");
				}
				else {
					DataTable newTable = new DataTable(newDataTableName);
					TableManager.getInstance().addDataTable(newTable);
					break;
				}
			}
		}
		// �A�J�E���g�e�[�u���ǉ��{�^���N���b�N
		else if(cmd.equals("�A�J�E���g�e�[�u���ǉ�")) {
			FrameAccountTableDefinition frameAtd = FrameAccountTableDefinition.getInstance();
			frameAtd.setVisible(true);
			frameAtd.setEnabled(true);
			PanelAccountTableDefinition.getInstance().relocateCompsForAccountTableDefinition(true);
			
			MainFrame mainFrame = MainFrame.getInstance();
			mainFrame.setEnabled(false);
		}
		// �u�I�𒆂̃A�J�E���g�e�[�u����ҏW�v�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�A�J�E���g�e�[�u���ҏW")) {
			// �I�𒆂̃A�J�E���g�e�[�u���擾
			int selectedAtIndex = PanelTableListAbove.getInstance().accountTableList.getSelectedRow();
			AccountTable selectedAt = TableManager.getInstance().getAccountTable(selectedAtIndex);

			// �ړ�
			MainFrame.getInstance().shiftToFieldList(selectedAt);
		}
		else if(cmd.equals("�J�� : �y�[�W�J�ڒ�`���")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
		else if(cmd.equals("�J�� : ���[����`���")) {
			MainFrame.getInstance().shiftToRoleEdit();
		}
		else if(cmd.equals("�J�� : ������`���")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		else {
			Debug.error("�s���ȃR�}���h�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		PanelTableListAbove.getInstance().refreshDataTableList();
		PanelTableListAbove.getInstance().refreshAccountTableList();
		PanelTableListAbove.getInstance().locateComps();

		PanelTableListBottom.getInstance().locateCompsPanelTableListBottom();

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}


	
	
	public static PanelTableListBottom getInstance() {
		return PanelTableListBottom.obj;
	}



	public static void updateInstance(PanelTableListBottom newObject) {
		PanelTableListBottom.obj = newObject;
	}
}
