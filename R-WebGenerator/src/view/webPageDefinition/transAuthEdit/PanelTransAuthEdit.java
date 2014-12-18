package view.webPageDefinition.transAuthEdit;

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
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import utility.*;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class PanelTransAuthEdit extends JPanel implements ActionListener{
	public static final int PANEL_HEIGHT = 800;
	
	public ArrayList<JCheckBox> checkBoxArrayRoles = new ArrayList<JCheckBox>();

	public JTextPane textPaneTransDesc;
	public JTextPane textPaneTransAuth;
	
	public JButton btnFinish = new JButton();
	public JButton btnCancel = new JButton();
	
	public SerializableSpringLayout springLayout;

	public static final Color BACKGROUND_COLOR = Color.white;

	public Transition editingTransition;
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelTransAuthEdit obj = new PanelTransAuthEdit();

	
	
	
	
	private PanelTransAuthEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(PanelTransAuthEdit.BACKGROUND_COLOR);
	}

	
	
	
	
	
	
	
	
	
	public void relocateCompsForTransAuthEdit(Transition transition) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.editingTransition = transition;
		
		removeAll();

		int PADD_LEFT = 20;

		// �e�L�X�g�y�C���i�J�ڂ̐����j
		textPaneTransDesc = new JTextPane();
		textPaneTransDesc.setEditable(false);
		String textTransDesc =	transition.startPage.pageFileName + " -> " + transition.endPage.pageFileName + "\n";
		PageElement triggerPe = transition.triggerPe;
		if(triggerPe instanceof PageElement_HyperLink) {
			PageElement_HyperLink triggerHyperLink = (PageElement_HyperLink)triggerPe;
			textTransDesc += japanese?
								"�n�C�p�[�����N�u" + triggerHyperLink.getText() + "�v�ɂ��J��":
								"Transition by Hyper Link '" + triggerHyperLink.getText() + "'";
		}
		else if(triggerPe instanceof PageElementCreateForm) {
			PageElementCreateForm triggerCreateForm = (PageElementCreateForm)triggerPe;
			textTransDesc += japanese?
								"�e�[�u���u" + triggerCreateForm.table.getTableName() + "�v�̃��R�[�h�쐬�t�H�[���ɂ��J��":
								"Transition by Creation Form of Table '" + triggerCreateForm.table.getTableName() + "'";
		}
		else if(triggerPe instanceof PageElementUpdateForm) {
			PageElementUpdateForm triggerUpdateForm = (PageElementUpdateForm)triggerPe;
			textTransDesc += japanese?
								"�e�[�u���u" + triggerUpdateForm.getTable().getTableName() + "�v�̍X�V�t�H�[���ɂ��J��":
								"Transition by Update Form of Table '" + triggerUpdateForm.getTable().getTableName() + "'";
		}
		else if(triggerPe instanceof PageElementLoginForm) {
			PageElementLoginForm triggerLoginForm = (PageElementLoginForm)triggerPe;
			textTransDesc += japanese?
							"�A�J�E���g�e�[�u���u" + triggerLoginForm.accountTable.getTableName() + "�v�̃��O�C���t�H�[���ɂ��J��":
							"Transition by Login Form of Account Table '" + triggerLoginForm.accountTable.getTableName() + "'";
		}
		else {
			Debug.error("�z��O�̑J�ڃg���K�[�y�[�W�G�������g�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		textPaneTransDesc.setText(textTransDesc);
		Slpc.put(springLayout, "N", textPaneTransDesc, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneTransDesc, "W", this, PADD_LEFT);
		add(textPaneTransDesc);

		
		textPaneTransAuth = new JTextPane();
		textPaneTransAuth.setEditable(false);
		textPaneTransAuth.setText(
			japanese?
			"��L�J�ڂ��ǂ̃��[���ɋ����܂����B\n�P�ȏ�`�F�b�N���ĉ������B\n���J�ڂ�������ƁA�J�ڃv���Z�X���̑S�Ă̏�����\n���쌠���Ɋ֌W�Ȃ����s����܂��B":
			"Check Roles which can transit."
		);
		textPaneTransAuth.setPreferredSize(new Dimension(FrameTransAuthEdit.frameWidth-(2*PADD_LEFT), 20));
		Slpc.put(springLayout, "N", textPaneTransAuth, "S", textPaneTransDesc, 20);
		Slpc.put(springLayout, "W", textPaneTransAuth, "W", this, PADD_LEFT);
		add(textPaneTransAuth);
		
		
		// �`�F�b�N�{�b�N�X�i�e���[���̖��O�j
		checkBoxArrayRoles = new ArrayList<JCheckBox>();
		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			TransitionAuth transAuth = AuthorityManager.getInstance().getTransAuth(role, transition);

			JCheckBox checkBoxRoleName = new JCheckBox(role.getRoleName());
			if(transAuth.transAuth) {
				checkBoxRoleName.setSelected(true);
			}

			// �`�F�b�N�{�b�N�X�z��ɒǉ�
			checkBoxArrayRoles.add(checkBoxRoleName);

			// �`�F�b�N�{�b�N�X�ʒu�����A�z�u
			Slpc.put(springLayout, "N", checkBoxRoleName, "S", textPaneTransAuth, 20+(i*30));
			Slpc.put(springLayout, "W", checkBoxRoleName, "W", this, PADD_LEFT);
			add(checkBoxRoleName);
		}

		
		// �ҏW�����{�^��
		btnFinish = new JButton(japanese?"�ҏW����":"Edit");
		btnFinish.addActionListener(this);
		btnFinish.setActionCommand("�ҏW����");
		Slpc.put(springLayout, "N", btnFinish, "S", textPaneTransAuth, 20+(checkBoxArrayRoles.size()*30));
		Slpc.put(springLayout, "W", btnFinish, "W", this, PADD_LEFT);
		add(btnFinish);
		
		// �L�����Z���{�^��
		btnCancel = new JButton(japanese?"�L�����Z��":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
		Slpc.put(springLayout, "N", btnCancel, "N", btnFinish, 0);
		Slpc.put(springLayout, "W", btnCancel, "E", btnFinish, 20);
		add(btnCancel);

		
		FrameTransAuthEdit.repaintAndValidate();
	}
	
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("�ҏW����")) {
			// �܂��A�P�ȏ�̃��[�����`�F�b�N���Ă��邩���ׂ�B�P�����ĂȂ��Ȃ�x�����ďI��
			int checkedRoleNum = 0;
			for(int i=0; i<this.checkBoxArrayRoles.size(); i++) {
				JCheckBox cb = checkBoxArrayRoles.get(i);
				if(cb.isSelected()) {
					checkedRoleNum++;
				}
			}
			if(checkedRoleNum==0) {
				JOptionPane.showMessageDialog(this, "�Œ�P�̃��[���ɑJ�ڂ������ĉ������B");
				return;
			}

		
			for(int i=0; i<this.checkBoxArrayRoles.size(); i++) {
				JCheckBox cb = checkBoxArrayRoles.get(i);
				Role role = RoleManager.getInstance().getRoleByName(cb.getText());
				TransitionAuth transAuth = AuthorityManager.getInstance().getTransAuth(role, this.editingTransition);

				transAuth.changeTransAuth(cb.isSelected());
			}
		}
		else if(cmd.equals("�L�����Z��")) {
			// �������Ȃ�
		}

		// ���̃t���[���������A���C���t���[�����g�p�\�ɂ���
		FrameTransAuthEdit.getInstance().setVisible(false);
		FrameTransAuthEdit.getInstance().setEnabled(false);
		FrameTransAuthEdit.getInstance().setFocusable(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();

		// �㕔�p�l���Ɖ����p�l���̃R���|�[�l���g�Ĕz�u
		PanelWebPageDefAbove.getInstance().locateWebPagePanels();
		PanelWebPageDefBottom.getInstance().locateCompsWebPageDefBottom();
		
		MainFrame.repaintAndValidate();
		FrameTransAuthEdit.repaintAndValidate();	
	}
	
	
	
	
	
	
	public static PanelTransAuthEdit getInstance() {
		return PanelTransAuthEdit.obj;
	}





	public static void updateInstance(PanelTransAuthEdit newObject) {
		PanelTransAuthEdit.obj = newObject;
	}
}
