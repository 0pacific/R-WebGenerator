package view.webPageDefinition;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.Transition;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.transAuthEdit.FrameTransAuthEdit;
import view.webPageDefinition.transAuthEdit.PanelTransAuthEdit;
import webPage.WebPage;
import webPage.WebPageManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.FlowLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import property.GeneratorProperty;

import mainFrame.MainFrame;
import utility.*;

import debug.Debug;





/*
 * Singleton
 */
public class PanelWebPageDefBottom extends JPanel implements ActionListener {
	// �p�l���T�C�Y
	public static final int panelWidth = MainFrame.frameWidth;
	public static final int panelHeight = MainFrame.frameHeight - PanelWebPageDefAbove.panelHeight;

	// �w�i�F
	public static Color backGroundColor = Color.DARK_GRAY;

	// �y�[�W�ǉ��p�R���|�[�l���g
	private JButton pageAddButton;
	
	public JButton btnLogoutDestPage;

	public JButton btnDeletePage;
	
	// PageElement�ҏW��ʂւ̑J�ڃ{�^��
	private JButton peEditButton;

	// TransitionProcess�ҏW��ʂւ̑J�ڃ{�^��
	private JButton tpEditButton;

	public JButton btnEditTransAuth;
	
	// ����ʂւ̑J�ڃ{�^��
	private JButton transitionButton_roleEdit;
	private JButton transitionButton_tableList;
	public JButton transitionButton_generate;
	public JButton btnEditTableAuth;

	// �V���A���C�Y�{�^��
	ButtonSaveWork btnSaveWork;
	// �f�V���A���C�Y�{�^��
	ButtonLoadWork btnLoadWork;
	
	public SerializableSpringLayout springLayout;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static PanelWebPageDefBottom obj = new PanelWebPageDefBottom();
	
	
	
	private PanelWebPageDefBottom() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		locateCompsWebPageDefBottom();
	}

	
	
	
	
	public void locateCompsWebPageDefBottom() {
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		int PADD_LEFT = 20;
		
		// �y�[�W�ǉ��̃{�^��
		pageAddButton = japanese ? new JButton("Web�y�[�W���쐬") : new JButton("Add Web Page");
		Slpc.put(springLayout, "N", pageAddButton, "N", this, 20);
		Slpc.put(springLayout, "W", pageAddButton, "W", this, PADD_LEFT);
		add(pageAddButton);	
		pageAddButton.addActionListener(this);
		pageAddButton.setActionCommand("�y�[�W�ǉ��{�^���N���b�N");

		// �y�[�W�폜�{�^��
		btnDeletePage = japanese ? new JButton("�I�𒆂�Web�y�[�W���폜") : new JButton("Delete Web Page");
		Slpc.put(springLayout, "N", btnDeletePage, "N", pageAddButton, 0);
		Slpc.put(springLayout, "W", btnDeletePage, "E", pageAddButton, 20);
		add(btnDeletePage);	
		btnDeletePage.addActionListener(this);
		btnDeletePage.setActionCommand("�y�[�W�폜");
		
		
		// ���O�A�E�g�J�ڐ�y�[�W��`�{�^��
		btnLogoutDestPage = japanese ? new JButton("���O�A�E�g���̑J�ڐ�Ƃ���") : new JButton("Set as Logout Destination");
		Slpc.put(springLayout, "N", btnLogoutDestPage, "N", btnDeletePage, 0);
		Slpc.put(springLayout, "W", btnLogoutDestPage, "E", btnDeletePage, 20);
		add(btnLogoutDestPage);	
		btnLogoutDestPage.addActionListener(this);
		btnLogoutDestPage.setActionCommand("���O�A�E�g�J�ڐ��`");
		
		// Page Element �ҏW�{�^��
		peEditButton = japanese ? new JButton("�y�[�W�\���v�f�̕ҏW") : new JButton("Edit Page Factors");
		Slpc.put(springLayout, "N", peEditButton, "S", pageAddButton, 20);
		Slpc.put(springLayout, "W", peEditButton, "W", this, PADD_LEFT);
		add(peEditButton);
		peEditButton.addActionListener(this);
		peEditButton.setActionCommand("�J�ځFPageElement�ҏW���");

		// TransitionProcess�ҏW��ʂւ̑J�ڃ{�^��
		tpEditButton = japanese ? new JButton("�J�ڃv���Z�X�̕ҏW") : new JButton("Edit Transition Process");
		Slpc.put(springLayout, "N", tpEditButton, "N", peEditButton, 0);
		Slpc.put(springLayout, "W", tpEditButton, "E", peEditButton, 40);
		add(tpEditButton);
		tpEditButton.addActionListener(this);
		tpEditButton.setActionCommand("�J�ځFTransitionProcess�ҏW���");

		// �J�ڌ����ҏW�{�^��
		btnEditTransAuth = japanese ? new JButton("�J�ڌ����̕ҏW") : new JButton("Edit Transition Authority");
		Slpc.put(springLayout, "N", btnEditTransAuth, "N", tpEditButton, 0);
		Slpc.put(springLayout, "W", btnEditTransAuth, "E", tpEditButton, 20);
		add(btnEditTransAuth);
		btnEditTransAuth.addActionListener(this);
		btnEditTransAuth.setActionCommand("�J�ځF�J�ڌ����ҏW���");
		
		// �e�[�u�����X�g��ʂւ̑J�ڃ{�^��
		transitionButton_tableList = japanese ? new JButton("�e�[�u����`��ʂ�") : new JButton("Go To Table Definition");
		Slpc.put(springLayout, "N", transitionButton_tableList, "S", peEditButton, 20);
		Slpc.put(springLayout, "W", transitionButton_tableList, "W", this, PADD_LEFT);
		add(transitionButton_tableList);
		transitionButton_tableList.addActionListener(this);
		transitionButton_tableList.setActionCommand("�J�ځF�e�[�u���ҏW���");

		// ���[���ҏW��ʂւ̑J�ڃ{�^��
		transitionButton_roleEdit = japanese ? new JButton("���[���ҏW��ʂ�") : new JButton("Go To Role Definition");
		Slpc.put(springLayout, "N", transitionButton_roleEdit, "N", transitionButton_tableList, 0);
		Slpc.put(springLayout, "W", transitionButton_roleEdit, "E", transitionButton_tableList, 20);
		add(transitionButton_roleEdit);
		transitionButton_roleEdit.addActionListener(this);
		transitionButton_roleEdit.setActionCommand("�J�ځF���[���ҏW���");

		// �e�[�u�������ҏW�{�^��
		btnEditTableAuth = japanese ? new JButton("���쌠���ҏW��ʂ�") : new JButton("Go To Accessibility Definition");
		Slpc.put(springLayout, "N", btnEditTableAuth, "N", transitionButton_roleEdit, 0);
		Slpc.put(springLayout, "W", btnEditTableAuth, "E", transitionButton_roleEdit, 20);
		add(btnEditTableAuth);
		btnEditTableAuth.addActionListener(this);
		btnEditTableAuth.setActionCommand("�J�ځF���쌠���ҏW���");
		
		// ������ʂւ̑J�ڃ{�^��
		transitionButton_generate = japanese ? new JButton("������ʂ�") : new JButton("Go To Generation");
		Slpc.put(springLayout, "N", transitionButton_generate, "N", btnEditTableAuth, 0);
		Slpc.put(springLayout, "W", transitionButton_generate, "E", btnEditTableAuth, 20);
		add(transitionButton_generate);
		transitionButton_generate.addActionListener(this);
		transitionButton_generate.setActionCommand("�J�ځF�������");

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

		if(PanelWebPageDefAbove.getInstance().webPagePanelFocused()) {
			informPageFocus();
		}
		else {
			informPageUnfocus();
		}

		if(PanelWebPageDefAbove.getInstance().transtionIsFocused()) {
			informTransitionFocus();
		}
		else {
			informTransitionUnfocus();
		}
	}
	
	
	
	
	public void paintComponent(Graphics g) {
		// �w�i�h��ׂ�
		g.setColor(PanelWebPageDefBottom.backGroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �e�{�^���N���b�N���̃A�N�V����
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		if(cmd.equals("�y�[�W�ǉ��{�^���N���b�N")) {
			while(true) {
				String msg1 = japanese ? "Web�y�[�W���𔼊p�p�����œ���\n\"index\"�Ɠ��͂����\"index.php\"�Ƃ���Web�y�[�W�ɂȂ�܂�" : "Enter Web page's name.\nIf you enter \"index\", Web page \"index.php\" will be created.";
				String newPageName = JOptionPane.showInputDialog(this, msg1);
				if(newPageName==null) {	// ���͎�����
					return;
				}
				else if(newPageName.equals("")) {
					String msg2 = japanese ? "���������͂��ĉ�����" : "Web page's name can't be blank.";
					JOptionPane.showMessageDialog(this, msg2);
				}
				else if(WebPageManager.getInstance().getPageByName(newPageName+".php") instanceof WebPage) {
					String msg3 = japanese ? newPageName+".php�͊��ɑ��݂��܂��B�Ⴄ���O�ɂ��ĉ������B" : newPageName+".php already exists.";
					JOptionPane.showMessageDialog(this, msg3);
				}
				else {
					WebPageManager.getInstance().addNewPage(newPageName + ".php");
					Debug.out("WebPageManager�C���X�^���X�Ƀy�[�W�ǉ� : " + newPageName + ".php");
					break;
				}
			}

			// �y�[�W���ǉ����ꂽ

			WebPageManager.getInstance().debug();
		}
		else if(cmd.equals("�y�[�W�폜")) {
			WebPage targetPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
			if(targetPage==null) {
				Debug.error("�y�[�W�폜�{�^����������܂������A�y�[�W���t�H�[�J�X����Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			String webPageName = targetPage.pageFileName;
			String msg4 = japanese ? "Web�y�[�W\"" + webPageName + "\"���폜���܂����H\n�y�[�W�\���v�f��J�ځA�J�ڃv���Z�X���S�č폜����܂��B" : "Delete "+webPageName+"?";
			int confirm = JOptionPane.showConfirmDialog(this, msg4, "Web�y�[�W�̍폜", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}

			WebPageManager.getInstance().removeWebPage(targetPage);
			PanelWebPageDefAbove.getInstance().focusedWebPagePanel = null;
		}
		
		else if(cmd.equals("���O�A�E�g�J�ڐ��`")) {
			WebPage focusedPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
			if(focusedPage==null) {
				Debug.error("�㕔�p�l���Ńt�H�[�J�X����Ă���y�[�W������͂��Ȃ̂ł����Anull�̂悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			String msg5 = japanese ? "���O�A�E�g���ɂ́A"+focusedPage.pageFileName+"�֖߂�悤�ɂ��܂��B��낵���ł����H" : "Users will go to "+focusedPage.pageFileName+" when they logout. OK?";
			int confirm = JOptionPane.showConfirmDialog(this, msg5, japanese?"���O�A�E�g���J�ڐ�̒�`":"Logout Destination Page", JOptionPane.OK_CANCEL_OPTION);
			
			if(confirm==JOptionPane.OK_OPTION) {
				WebPageManager.getInstance().setLogoutDestPage(focusedPage);
			}
			// �L�����Z��
			else if(confirm==JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		
		// �u������ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF�������")) {
			MainFrame.getInstance().shiftToGenerate();
		}

		// �u���[���ҏW��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF���[���ҏW���")) {
			MainFrame.getInstance().shiftToRoleEdit();
		}
		// �u���쌠���ҏW��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF���쌠���ҏW���")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		// �u�e�[�u���ҏW��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځF�e�[�u���ҏW���")) {
			MainFrame.getInstance().shiftToTableList();
		}

		// �u�y�[�W�\���v�f�ҏW��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځFPageElement�ҏW���")) {
			WebPage targetPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
			if(targetPage==null) {
				Debug.error("Panel_Transition_Bottom actionPerformed() : �y�[�W�\���v�f��ҏW���悤�Ƃ���Page�I�u�W�F�N�g��null�ł�");
				return;
			}
			MainFrame.getInstance().shiftToPeEdit(targetPage);
		}
		
		// �u�J�ڃv���Z�X�ҏW��ʂցv�{�^���N���b�N -> �ړ�
		else if(cmd.equals("�J�ځFTransitionProcess�ҏW���")) {
			Transition targetTrans = PanelWebPageDefAbove.getInstance().getFocusedTransition();
			if(targetTrans==null) {
				Debug.error("�J�ڃv���Z�X��ҏW���悤�Ƃ���Transition�C���X�^���X��null�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			MainFrame.getInstance().shiftToTpEdit(targetTrans);
		}
		else if(cmd.equals("�J�ځF�J�ڌ����ҏW���")) {
			Transition targetTrans = PanelWebPageDefAbove.getInstance().getFocusedTransition();
			if(targetTrans==null) {
				Debug.error("�J�ڌ�����ҏW���悤�Ƃ���Transition�C���X�^���X��null�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}

			MainFrame.getInstance().setEnabled(false);
			MainFrame.getInstance().setFocusable(false);
			FrameTransAuthEdit.getInstance().setVisible(true);
			FrameTransAuthEdit.getInstance().setEnabled(true);
			FrameTransAuthEdit.getInstance().setFocusable(true);
			FrameTransAuthEdit.getInstance().requestFocus();

			PanelTransAuthEdit.getInstance().relocateCompsForTransAuthEdit(targetTrans);
			FrameTransAuthEdit.repaintAndValidate();
		}

		// �e�y�[�W�̍Ĕz�u
		PanelWebPageDefAbove.getInstance().locateWebPagePanels();

		// �����p�l���̃R���|�[�l���g�Ĕz�u
		locateCompsWebPageDefBottom();
		
		// �t���[���̃��y�C���g
		MainFrame.repaintAndValidate();
	}

	
	
	
	
	public void informPageFocus() {
		peEditButton.setEnabled(true);
		btnDeletePage.setEnabled(true);
		
		// ���O�A�E�g�J�ڐ�łȂ�Web�y�[�W���t�H�[�J�X���ꂽ�Ȃ�A���O�A�E�g�J�ڐ��`�{�^����L���ɂ���
		WebPage focusedPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
		if(focusedPage!=WebPageManager.getInstance().getLogoutDestPage()) {
			btnLogoutDestPage.setEnabled(true);
		}
		else {	// ���O�A�E�g�J�ڐ�Web�y�[�W���t�H�[�J�X���ꂽ�ꍇ
			btnLogoutDestPage.setEnabled(false);
		}
	}

	public void informPageUnfocus() {
		peEditButton.setEnabled(false);
		btnDeletePage.setEnabled(false);
		btnLogoutDestPage.setEnabled(false);
	}
	
	public void informTransitionFocus() {
		tpEditButton.setEnabled(true);
		btnEditTransAuth.setEnabled(true);
	}
	
	public void informTransitionUnfocus() {
		tpEditButton.setEnabled(false);
		btnEditTransAuth.setEnabled(false);
	}
	
	
	public static PanelWebPageDefBottom getInstance() {
		return PanelWebPageDefBottom.obj;
	}




	public static void updateInstance(PanelWebPageDefBottom newObject) {
		PanelWebPageDefBottom.obj = newObject;
	}
}
