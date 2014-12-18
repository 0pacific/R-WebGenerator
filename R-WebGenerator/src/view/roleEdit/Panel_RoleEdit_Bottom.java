package view.roleEdit;

import gui.*;
import gui.arrow.*;

import role.GuestRole;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.*;
import tpp.*;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.tableList.PanelTableListAbove;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import property.GeneratorProperty;

import debug.Debug;

import mainFrame.MainFrame;

import java.awt.event.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.Serializable;

import utility.*;

/*
 * Singleton
 */
public class Panel_RoleEdit_Bottom extends JPanel implements ActionListener,Serializable {
	private JButton roleAddButton;

	public JButton btnRemoveRole;
	
	// �V�K���[��������͂���e�L�X�g�t�B�[���h�̉���
	public static int ROLE_NAME_TF_WIDTH = 100;
	
	// ����ʂւ̑J�ڃ{�^��
	private JButton transitionButton_diagram;
	private JButton transitionButton_tableList;
	private JButton transBtn_authEdit;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// �w�i�F
	public static Color BG_COLOR = Color.WHITE;

	// �V���A���C�Y�{�^��
	ButtonSaveWork btnSaveWork;
	// �f�V���A���C�Y�{�^��
	ButtonLoadWork btnLoadWork;

	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static Panel_RoleEdit_Bottom obj = new Panel_RoleEdit_Bottom();
	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private Panel_RoleEdit_Bottom() {
		// �p�l���̃��C�A�E�g
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	

	/*
	 * PURPOSE :
	 * �R���|�[�l���g�̍Ĕz�u
	 */
	public void locateComps() {
		boolean japanese = GeneratorProperty.japanese();
		
		// �S�R���|�[�l���g������
		removeAll();
		
		
		// ���[���쐬�{�^��
		String msg1 = japanese ? "���[�����쐬" : "Add Role";
		roleAddButton = new JButton(msg1);
		springLayout.putConstraint(SpringLayout.NORTH, roleAddButton, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, roleAddButton, 20, SpringLayout.WEST, this);
		add(roleAddButton);

		// ���[���폜�{�^��
		String msg2 = japanese ? "�I�𒆂̃��[�����폜" : "Delete Selected Role";
		btnRemoveRole = new JButton(msg2);
		Slpc.put(springLayout, "N", btnRemoveRole, "N", roleAddButton, 0);
		Slpc.put(springLayout, "W", btnRemoveRole, "E", roleAddButton, 30);
		add(btnRemoveRole);
		
		// �J�ڐ}��`��ʂւ̑J�ڃ{�^��
		String msg3 = japanese ? "Web�y�[�W��`��ʂ�" : "Go To Web Page Definition";
		transitionButton_diagram = new JButton(msg3);
		springLayout.putConstraint(SpringLayout.NORTH, transitionButton_diagram, 50, SpringLayout.SOUTH, roleAddButton);
		springLayout.putConstraint(SpringLayout.WEST, transitionButton_diagram, 20, SpringLayout.WEST, this);
		add(transitionButton_diagram);

		// �e�[�u�����X�g��ʂւ̑J�ڃ{�^��
		String msg4 = japanese ? "�e�[�u����`��ʂ�" : "Go To Table Definition";
		transitionButton_tableList = new JButton(msg4);
		springLayout.putConstraint(SpringLayout.NORTH, transitionButton_tableList, 0, SpringLayout.NORTH, transitionButton_diagram);
		springLayout.putConstraint(SpringLayout.WEST, transitionButton_tableList, 20, SpringLayout.EAST, transitionButton_diagram);
		add(transitionButton_tableList);

		// ������`��ʂւ̑J�ڃ{�^��
		String msg5 = japanese ? "�A�N�Z�X������`��ʂ�" : "Go To Accessibility Definition";
		transBtn_authEdit = new JButton(msg5);
		springLayout.putConstraint(SpringLayout.NORTH, transBtn_authEdit, 0, SpringLayout.NORTH, transitionButton_diagram);
		springLayout.putConstraint(SpringLayout.WEST, transBtn_authEdit, 20, SpringLayout.EAST, transitionButton_tableList);
		add(transBtn_authEdit);

		// �V���A���C�Y�{�^��
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", this, 20);
		Slpc.put(springLayout, "E", btnSaveWork, "E", this, -20);
		add(btnSaveWork);

		// �f�V���A���C�Y�{�^��
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", btnSaveWork, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		// �e�{�^���͂��̊֐������s�����x�ɐV������������Ă��邽�߁A����C�x���g���X�i�̒ǉ��E�R�}���h�ݒ���s��
		addListeners();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * ���[���ǉ��{�^���ƁA�e��ʂւ̑J�ڃ{�^���ɁAActionListener��ǉ����AActionCommand��ݒ肷��
	 */
	public void addListeners() {
		roleAddButton.addActionListener(this);
		roleAddButton.setActionCommand("���[���ǉ�");

		btnRemoveRole.addActionListener(this);
		btnRemoveRole.setActionCommand("���[���폜");
		
		transitionButton_diagram.addActionListener(this);
		transitionButton_diagram.setActionCommand("�J�ځF�y�[�W�J�ڒ�`���");

		transitionButton_tableList.addActionListener(this);
		transitionButton_tableList.setActionCommand("�J�ځF�e�[�u�����X�g���");
		
		transBtn_authEdit.addActionListener(this);
		transBtn_authEdit.setActionCommand("�J�ځF������`���");
	}
	
	
	
	
	
	public void paintComponent(Graphics g) {
		// �w�i�h��ׂ�
		g.setColor(Panel_RoleEdit_Bottom.BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �e�{�^���N���b�N���̃A�N�V����
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		// �u���[���̒ǉ��v�{�^���N���b�N -> ���[���ǉ��A���[���e�[�u���X�V
		if(cmd.equals("���[���ǉ�")) {
			while(true) {
				String msg1 = japanese ? "�쐬���郍�[���̖��̂���͂��ĉ������B" : "Enter Role Name";
				String newRoleName = JOptionPane.showInputDialog(this, msg1);
				if(newRoleName==null) {	// �L�����Z��
					return;
				}
				else if(newRoleName.equals("")) {	// ���
					String msg2 = japanese ? "1���ȏ�̖��̂���͂��ĉ������B" : "Role's name mustn't be blank.";
					JOptionPane.showMessageDialog(this, msg2);
				}
				else {
					NormalRole newNormalRole = new NormalRole(newRoleName);
					RoleManager.getInstance().addRole(newNormalRole);
					break;
				}
			}
		}
		else if(cmd.equals("���[���폜")) {
			// �I�𒆂̃��[���擾
			int selectedRoleIndex = Panel_RoleEdit_Above.getInstance().roleTable.getSelectedRow();
			Role selectedRole = RoleManager.getInstance().getRole(selectedRoleIndex);

			// �Q�X�g���[�����폜���悤�Ƃ��Ă��� -> �_��
			if(selectedRole instanceof GuestRole) {
				String msg3 = japanese ? "�Q�X�g���[���͍폜�ł��܂���" : "Guest Role can't be deleted";
				JOptionPane.showMessageDialog(this, msg3);
				return;
			}

			/*
			 * �I�����ꂽ�͔̂�Q�X�g���[���ł���
			 */

			NormalRole selectedNormalRole = (NormalRole)selectedRole;
			
			// ���̃��[�����폜����ƁA�A�J�E���g�e�[�u�����폜���邱�ƂɂȂ邩�ۂ��i���̃A�J�E���g�e�[�u���ł��̃��[���̃A�J�E���g�����Ǘ����Ă��Ȃ��ꍇ�����Ȃ�j
			boolean shouldDeleteAt = RoleManager.getInstance().shouldRemoveAtIfRoleRemoved(selectedNormalRole);
			
			// �A�J�E���g�e�[�u�����폜����邩�ۂ��ŁA�m�F���b�Z�[�W��ς���
			String confirmMessage = null;
			if(shouldDeleteAt) {
				confirmMessage =	japanese ?
									"�폜���Ă���낵���ł����H���̃��[���̃A�J�E���g�e�[�u���⃍�O�C���t�H�[�����폜���܂��B" :
									"Would you really like to delete the role? This role's account table and login form would also be deleted";
			}
			else {
				confirmMessage =	japanese ?
									"���̃��[�����폜���Ă���낵���ł����H" :
									"Would you really like to delete the role?";
			}

			// �m�F���b�Z�[�W
			int confirm = JOptionPane.showConfirmDialog(this, confirmMessage, "���[���̍폜", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {
				return;
			}

			/*
			 * ���[���폜������
			 */
			
			RoleManager.getInstance().removeNormalRole(selectedNormalRole);
		}
		// �u�y�[�W�J�ڒ�`��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF�y�[�W�J�ڒ�`���")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
		// �u�e�[�u���ҏW��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF�e�[�u�����X�g���")) {
			MainFrame.getInstance().shiftToTableList();
		}
		// �u������`��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF������`���")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		// �G���[
		else {
			JOptionPane.showMessageDialog(this, "�G���[���������܂����B");
			Debug.error("�s���ȃR�}���h�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		Panel_RoleEdit_Above.getInstance().refreshRoleTable();
		MainFrame.getInstance().repaintAndValidate();
	}

	
	
	
	
	public static Panel_RoleEdit_Bottom getInstance() {
		return Panel_RoleEdit_Bottom.obj;
	}
	
	
	
	public static void updateInstance(Panel_RoleEdit_Bottom newObject) {
		Panel_RoleEdit_Bottom.obj = newObject;
	}

}
