package view.adminAuthEdit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import mainFrame.MainFrame;
import debug.Debug;
import property.GeneratorProperty;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.roleEdit.Panel_RoleEdit_Above;
import view.roleEdit.Panel_RoleEdit_Bottom;
//�y�ǉ��z
public class PanelAdminAuthEditBottom extends JPanel implements ActionListener,Serializable{
	
	// ����ʂւ̑J�ڃ{�^��
		private JButton transitionButton_diagram;
		private JButton transitionButton_tableList;
		private JButton transBtn_authEdit;
		private JButton transBtn_roleEdit;
		
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
		private static PanelAdminAuthEditBottom obj = new PanelAdminAuthEditBottom();

		private PanelAdminAuthEditBottom(){
			springLayout = new SerializableSpringLayout();
			setLayout(springLayout);
		}
		
		//�R���|�[�l���g�̍Ĕz�u
		public void locateComps(){
			boolean japanese = GeneratorProperty.japanese();
			
			
			// �J�ڐ}��`��ʂւ̑J�ڃ{�^��
			String msg3 = japanese ? "Web�y�[�W��`��ʂ�" : "Go To Web Page Definition";
			transitionButton_diagram = new JButton(msg3);
			springLayout.putConstraint(SpringLayout.NORTH, transitionButton_diagram, 50, SpringLayout.SOUTH, this);
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

			// ���[����`��ʂւ̑J�ڃ{�^��
			String msg6 = japanese ? "���[����`��ʂ�" : "Go To Role Definition";
			transBtn_roleEdit = new JButton(msg6);
			springLayout.putConstraint(SpringLayout.NORTH, transBtn_roleEdit, 0, SpringLayout.NORTH, transitionButton_diagram);
			springLayout.putConstraint(SpringLayout.WEST, transBtn_roleEdit, 20, SpringLayout.EAST, transitionButton_tableList);
			add(transBtn_roleEdit);
			
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
		
		
	private void addListeners() {
		
		transitionButton_diagram.addActionListener(this);
		transitionButton_diagram.setActionCommand("�J�ځF�y�[�W�J�ڒ�`���");

		transitionButton_tableList.addActionListener(this);
		transitionButton_tableList.setActionCommand("�J�ځF�e�[�u�����X�g���");
		
		transBtn_authEdit.addActionListener(this);
		transBtn_authEdit.setActionCommand("�J�ځF������`���");
		
		transBtn_roleEdit.addActionListener(this);
		transBtn_roleEdit.setActionCommand("�J�ځF���[����`���");
			
		}

	public void paintComponent(Graphics g) {
		// �w�i�h��ׂ�
		g.setColor(Panel_RoleEdit_Bottom.BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	
	public void actionPerformed(ActionEvent e) {

		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		// �u�y�[�W�J�ڒ�`��ʂցv�{�^���N���b�N -> �ړ�
		if(cmd.equals("�J�ځF�y�[�W�J�ڒ�`���")) {
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
		else if(cmd.equals("�J�ځF���[����`���")) {
			MainFrame.getInstance().shiftToRoleEdit();
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

}
