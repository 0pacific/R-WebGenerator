package view.peEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.Transition;
import transition.TransitionManager;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;
import webPage.WebPageManager;

import javax.swing.*;
import javax.swing.border.*;

import debug.Debug;

import mainFrame.MainFrame;

import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementLoginForm;
import pageElement.PageElementObserver;
import pageElement.PageElementText;
import pageElement.PageElement_HyperLink;
import pageElement.PageElementDisplayArea;
import pageElement.PageElementTableDisplay;
import property.GeneratorProperty;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import java.io.Serializable;

/*
 * NOTICE : Singleton
 */
public class Panel_PeEdit_Right extends JPanel implements ActionListener,Serializable {
	boolean japanese = GeneratorProperty.japanese();
	
	// �e�[�u���\���̈�ǉ��E�ҏW�p
	public JComboBox tdCombo = new JComboBox();
	public JTextPane jtp_tdFieldLimitation;
	public ArrayList<JCheckBox> checkBoxArrayForTdFieldLimitation;	
	
	// �n�C�p�[�����N�ǉ��E�ҏW�p
	public JComboBox hlCombo_dest = new JComboBox();
	public JTextField hlTextField_name = new JTextField();

	// Create�t�H�[���p
	public JComboBox cfCombo;		// �e�[�u���I���R���{�{�b�N�X
	public JCheckBox multipleCb;	// �������R�[�h�쐬�\���ۂ��A��I������`�F�b�N�{�b�N�X
	public JComboBox cfDestCombo;	// �J�ڐ�y�[�W��I������R���{�{�b�N�X
	
	// �e�L�X�g�y�[�W�G�������g�p
	public JTextArea textAreaForPeText = new JTextArea();

	// ���O�C���t�H�[���p
	public JComboBox lfCombo = new JComboBox();
	public JComboBox lfCombo_dest = new JComboBox();
	
	// Page Element �ǉ��E�ҏW�̎��s�{�^���i�ǂ̃^�C�v�� Page Element �ł��g���j
	public JButton additionButton = new JButton(japanese?"�y�[�W�\���v�f��ǉ�":"Add Page Factor");
	public JButton editFinishButton = new JButton(japanese?"�ȏ�̒ʂ�ɕύX":"Modify Page Factor");
	
	// �p�l���̃T�C�Y
	public static final int panelWidth = MainFrame.frameWidth - Panel_PeEdit_Left.panelWidth;
	public static final int panelHeight = MainFrame.frameHeight - PanelPeEditBottom.panelHeight;

	// �p�l���̔w�i�F
	public static final Color BG_COLOR = Color.WHITE;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	public static Panel_PeEdit_Right obj = new Panel_PeEdit_Right();

	
	
	
	
	
	

	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private Panel_PeEdit_Right() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		setBackground(Panel_PeEdit_Right.BG_COLOR);

		initComponentsForPeEdit();
	}
	
	
	
	
	
	public void initComponentsForPeEdit() {
		additionButton.addActionListener(this);
		editFinishButton.addActionListener(this);
	}

	

	
	
	
	
	
	
	

	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ��edit��true�ɂ��A�ǉ��̏ꍇ��false�ɂ���
	 */
	public void locateForResultTableDisplay(boolean edit) {
		// �S�R���|�[�l���g����
		removeAll();

		String text = japanese?"�\������\�f�[�^�̏o�͌��́A�J�ڃv���Z�X��`�̒��Ŏw�肵�ĉ������B":"Component outputing tabular data for displaying here has to be defined\nin transition process definition.";
		JTextArea textArea1 = new JTextArea(text, 2, 50);
		Slpc.put(springLayout, "N", textArea1, "N", this, 20);
		Slpc.put(springLayout, "W", textArea1, "W", this, 20);
		add(textArea1);

		
		// �ҏW���[�h��
		if(edit) {
			// �u�ȏ�̓��e�ɕύX����v�{�^��
			editFinishButton.setActionCommand("PageElement�ҏW - ���ʃe�[�u���\���̈�");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, textArea1);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, textArea1);
			add(editFinishButton);
		}
		// �ǉ����[�h��
		else {
			// �u�ȏ�̓��e�Ńy�[�W�\���v�f��ǉ�����v�{�^��
			additionButton.setActionCommand("PageElement�ǉ� - ���ʃe�[�u���\���̈�");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, textArea1);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, textArea1);
			add(additionButton);
		}
		
		
		MainFrame.getInstance().repaintAndValidate();
	}

	
	
	
	
	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ��edit��true�ɂ��A�ǉ��̏ꍇ��false�ɂ���
	 */
	public void locateForCreateForm(boolean edit) {
		boolean japanese = GeneratorProperty.japanese();
		// �S�R���|�[�l���g����
		removeAll();

		// �e�L�X�g�y�C���u�ǂ̃e�[�u���̃��R�[�h���쐬����̂��I�����ĉ������v
		String msg1 = GeneratorProperty.japanese() ? "�ǂ̃e�[�u���̃��R�[�h���쐬���܂���" : "Select table";
		JTextArea jtp_tblSelCre = new JTextArea(msg1, 1, 30);
		jtp_tblSelCre.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_tblSelCre, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_tblSelCre, 20, SpringLayout.WEST, this);
		add(jtp_tblSelCre);

		// �e�[�u����I������R���{�{�b�N�X
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		tableNames.add(0, japanese?"---�I�����ĉ�����---":"------Select------");
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		cfCombo = new JComboBox(tableNameStrings);
		Slpc.put(springLayout, "N", cfCombo, "S", jtp_tblSelCre, 20);
		Slpc.put(springLayout, "W", cfCombo, "W", jtp_tblSelCre, 0);
		add(cfCombo);

		
		// �ҏW���[�h�i�ǉ����[�h�ł͂Ȃ��j�̏ꍇ -> ���ݐݒ肳��Ă���e�[�u���̖��O���f�t�H���g�őI��
		if(edit) {
			// �t�H�[�J�X����Ă���PageElement���擾�i�ҏW���̂ݕK�v�j
			PageElementCreateForm focusedCf = (PageElementCreateForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();

			for(int i=0; i<cfCombo.getItemCount(); i++) {
				String item = (String)cfCombo.getItemAt(i);
				String focusedCfTableName = focusedCf.table.getTableName();
				if(item.equals(focusedCfTableName)) {
					cfCombo.setSelectedItem(cfCombo.getItemAt(i));
					break;
				}
			}
		}


		
		// ����e�L�X�g�y�C��
		String msg2 = GeneratorProperty.japanese() ? "���R�[�h�쐬�ɐ���������̑J�ڐ��I�����ĉ�����" : "Select destination page";
		JTextArea jtp_destSel = new JTextArea(msg2, 1, 30);
		jtp_destSel.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_destSel, 20, SpringLayout.SOUTH, cfCombo);
		springLayout.putConstraint(SpringLayout.WEST, jtp_destSel, 20, SpringLayout.WEST, this);
		add(jtp_destSel);

		
		
		// �J�ڐ�y�[�W��I������R���{�{�b�N�X
		ArrayList<String> pageFileNameArray = WebPageManager.getInstance().getPageNameArrayList();
		pageFileNameArray.add(0, japanese?"---�I�����ĉ�����---":"------Select------");
		pageFileNameArray.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
		String[] pageFileNames = (String[])pageFileNameArray.toArray(new String[0]);
		cfDestCombo = new JComboBox(pageFileNames);
		springLayout.putConstraint(SpringLayout.NORTH, cfDestCombo, 20, SpringLayout.SOUTH, jtp_destSel);
		springLayout.putConstraint(SpringLayout.WEST, cfDestCombo, 0, SpringLayout.WEST, jtp_tblSelCre);
		add(cfDestCombo);
		// �ҏW���[�h -> ���ɐݒ肳��Ă���J�ڐ�y�[�W���A�R���{�{�b�N�X�ł͗\�ߑI����Ԃɂ���
		if(edit) {
			// �t�H�[�J�X����Ă���PageElement���擾�i�ҏW���̂ݕK�v�j
			PageElementCreateForm focusedCf = (PageElementCreateForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();

			for(int i=0; i<cfDestCombo.getItemCount(); i++) {
				String item = (String)cfDestCombo.getItemAt(i);
				String editingCfDestPageName = focusedCf.destPage.pageFileName;
				if(item.equals(editingCfDestPageName)) {
					cfDestCombo.setSelectedItem(cfDestCombo.getItemAt(i));
					break;
				}
			}
		}
		
		
		// �ҏW���[�h��
		if(edit) {
			// �u�ȏ�̓��e�ɕύX����v�{�^��
			editFinishButton.setActionCommand("PageElement�ҏW - Create�t�H�[��");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, cfDestCombo);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, jtp_tblSelCre);
			add(editFinishButton);
		}
		// �ǉ����[�h��
		else {
			// �u�ȏ�̓��e�Ńy�[�W�\���v�f��ǉ�����v�{�^��
			additionButton.setActionCommand("PageElement�ǉ� - Create�t�H�[��");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, cfDestCombo);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, jtp_tblSelCre);
			add(additionButton);
		}
		/* �����܂� */

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}

	
	
	
	
	
	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ��edit��true�ɂ��A�ǉ��̏ꍇ��false�ɂ���
	 */
	public void locateForHyperLink(boolean edit) {
		Debug.debug_call(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean japanese = GeneratorProperty.language.equals("Japanese");
		
		// �S�R���|�[�l���g����
		removeAll();

		
		
		// �t�H�[�J�X����Ă���PageElement���擾�i�ҏW���̂ݕK�v�j
		PageElement_HyperLink focusedHl = null;
		if(edit) {
			focusedHl = (PageElement_HyperLink)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
		}

		
		
		// �J�ڐ�JTextPane
		String msg1 = japanese ? "�J�ڐ�̃y�[�W :" : "Destination Page :";
		JTextArea jtp_dest = new JTextArea(msg1, 1, 20);
		jtp_dest.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_dest, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_dest, 20, SpringLayout.WEST, this);
		add(jtp_dest);

		// �J�ڐ�y�[�W��I������R���{�{�b�N�X
		ArrayList<String> pageFileNameArray = WebPageManager.getInstance().getPageNameArrayList();
		if(japanese)
			pageFileNameArray.add(0, japanese?"---�I�����ĉ�����---":"------Select------");
		else
			pageFileNameArray.add(0, "------Select------");
		pageFileNameArray.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
		String[] pageFileNames = pageFileNameArray.toArray(new String[0]);
		hlCombo_dest = new JComboBox(pageFileNames);
		springLayout.putConstraint(SpringLayout.NORTH, hlCombo_dest, 5, SpringLayout.SOUTH, jtp_dest);
		springLayout.putConstraint(SpringLayout.WEST, hlCombo_dest, 0, SpringLayout.WEST, jtp_dest);
		add(hlCombo_dest);

		
		
		// �ҏW���[�h -> ���ɐݒ肳��Ă���J�ڐ�y�[�W���A�R���{�{�b�N�X�ł͗\�ߑI����Ԃɂ���
		if(edit) {
			for(int i=0; i<hlCombo_dest.getItemCount(); i++) {
				String item = (String)hlCombo_dest.getItemAt(i);
				String focusedHlDestPageName = focusedHl.getDestPage().pageFileName;
				if(item.equals(focusedHlDestPageName)) {
					hlCombo_dest.setSelectedItem(hlCombo_dest.getItemAt(i));
					break;
				}
			}
		}

		
		
		// ����e�L�X�g�y�C��
		JTextArea jtp_name = new JTextArea(1, 30);
		if(japanese)
			jtp_name.setText("Web�y�[�W��Ńn�C�p�[�����N��\�镶����i�� : �g�b�v�y�[�W�ցj");
		else
			jtp_name.setText("Text on the Web page :�iExample : Top Page�j");
		jtp_name.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_name, 40, SpringLayout.SOUTH, hlCombo_dest);
		springLayout.putConstraint(SpringLayout.WEST, jtp_name, 0, SpringLayout.WEST, jtp_dest);
		add(jtp_name);

		// �\��������JTextField
		if(edit) {
			hlTextField_name.setText(focusedHl.getText());
		}
		else {
			hlTextField_name.setText("");
		}
		hlTextField_name.setPreferredSize(new Dimension(200, 30));
		springLayout.putConstraint(SpringLayout.NORTH, hlTextField_name, 5, SpringLayout.SOUTH, jtp_name);
		springLayout.putConstraint(SpringLayout.WEST, hlTextField_name, 0, SpringLayout.WEST, jtp_dest);
		add(hlTextField_name);
		
		
		
		// �n�C�p�[�����N�ҏW��
		if(edit) {
			// �u�ȏ�̓��e�ɕύX����v�{�^��
			editFinishButton.setActionCommand("PageElement�ҏW - �n�C�p�[�����N");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, hlTextField_name);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, jtp_dest);
			add(editFinishButton);
		}
		// �n�C�p�[�����N�ǉ���
		else {
			// �u�ȏ�̓��e�Ńy�[�W�\���v�f��ǉ�����v�{�^��
			additionButton.setActionCommand("PageElement�ǉ� - �n�C�p�[�����N");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, hlTextField_name);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, jtp_dest);
			add(additionButton);
		}
		/* �����܂� */

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	
		Debug.debug_return(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
	
	
	
	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ��edit��true�ɂ��A�ǉ��̏ꍇ��false�ɂ���
	 */
	public void locateForText(boolean edit) {
		boolean japanese = GeneratorProperty.language.equals("Japanese");

		// �S�R���|�[�l���g����
		removeAll();

		// �e�L�X�g�G���A
		String msg1 = japanese ? "�e�L�X�g����͂��ĉ������B���s��Web�y�[�W��Ŕ��f����܂��B" : "Text :";
		JTextArea jtp_desc = new JTextArea(msg1, 1, 30);
		jtp_desc.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_desc, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_desc, 20, SpringLayout.WEST, this);
		add(jtp_desc);

		// �e�L�X�g�G���A
	    textAreaForPeText = new JTextArea(6, 30);
		springLayout.putConstraint(SpringLayout.NORTH, textAreaForPeText, 20, SpringLayout.SOUTH, jtp_desc);
		springLayout.putConstraint(SpringLayout.WEST, textAreaForPeText, 0, SpringLayout.WEST, jtp_desc);
		springLayout.putConstraint(SpringLayout.EAST, textAreaForPeText, 0, SpringLayout.EAST, jtp_desc);
		textAreaForPeText.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(textAreaForPeText);
	    
		// �t�H�[�J�X����Ă���PageElement���擾�i�ҏW���̂ݕK�v�j
		PageElementText focusedText = (PageElementText)Panel_PeEdit_Left.getInstance().getFocusedPageElement();

		// �ҏW���[�h -> ���ɐݒ肳��Ă���e�L�X�g���A�\�ߕ\��
		if(edit) {
			String currentText = focusedText.containingText;
			textAreaForPeText.setText(currentText);
		}

		// �ҏW��
		if(edit) {
			// �u�ȏ�̓��e�ɕύX����v�{�^��
			editFinishButton.setActionCommand("PageElement�ҏW - �e�L�X�g");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, textAreaForPeText);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, textAreaForPeText);
			add(editFinishButton);
		}
		// �ǉ���
		else {
			// �u�ȏ�̓��e�Ńy�[�W�\���v�f��ǉ�����v�{�^��
			additionButton.setActionCommand("PageElement�ǉ� - �e�L�X�g");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, textAreaForPeText);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, textAreaForPeText);
			add(additionButton);
		}

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}

	
	
	
	
	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ��edit��true�ɂ��A�ǉ��̏ꍇ��false�ɂ���
	 */
	public void locateForLoginForm(boolean edit) {
		// �S�R���|�[�l���g����
		removeAll();

		// �e�L�X�g�y�C���u�ǂ̃A�J�E���g�e�[�u���ɑΉ��������O�C���t�H�[���Ƃ��܂����H�v
		JTextPane jtp_tableSelect = new JTextPane();
		jtp_tableSelect.setText("�ǂ̃A�J�E���g�e�[�u���ɑΉ��������O�C���t�H�[���Ƃ��܂����H");
		jtp_tableSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_tableSelect, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_tableSelect, 20, SpringLayout.WEST, this);
		add(jtp_tableSelect);

		// �A�J�E���g�e�[�u����I������R���{�{�b�N�X
		String[] atNames = TableManager.getInstance().getAccountTableNames();
		lfCombo = new JComboBox(atNames);
		springLayout.putConstraint(SpringLayout.NORTH, lfCombo, 20, SpringLayout.SOUTH, jtp_tableSelect);
		springLayout.putConstraint(SpringLayout.WEST, lfCombo, 0, SpringLayout.WEST, jtp_tableSelect);
		add(lfCombo);

		
		// �e�L�X�g�y�C��
		JTextPane jtp_destSel = new JTextPane();
		jtp_destSel.setText("���O�C���ɐ��������ۂ̑J�ڐ��I�����ĉ�����");
		jtp_destSel.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_destSel, 20, SpringLayout.SOUTH, lfCombo);
		springLayout.putConstraint(SpringLayout.WEST, jtp_destSel, 20, SpringLayout.WEST, this);
		add(jtp_destSel);
		
		// �J�ڐ�y�[�W��I������R���{�{�b�N�X
		String[] pageFileNames = WebPageManager.getInstance().getPageNameArray();
		lfCombo_dest = new JComboBox(pageFileNames);
		springLayout.putConstraint(SpringLayout.NORTH, lfCombo_dest, 20, SpringLayout.SOUTH, jtp_destSel);
		springLayout.putConstraint(SpringLayout.WEST, lfCombo_dest, 0, SpringLayout.WEST, jtp_destSel);
		add(lfCombo_dest);

		
		// �t�H�[�J�X����Ă���PageElement���擾�i�ҏW���̂ݕK�v�j
		PageElementLoginForm focusedLoginForm = (PageElementLoginForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
		
		
		// �ҏW���[�h�̏ꍇ -> 
		if(edit) {
			// ���ݐݒ肳��Ă���A�J�E���g�e�[�u���̖��O���f�t�H���g�őI��
			for(int i=0; i<lfCombo.getItemCount(); i++) {
				String item = (String)lfCombo.getItemAt(i);
				String focusedLoginFormTableName = focusedLoginForm.accountTable.getTableName();
				if(item.equals(focusedLoginFormTableName)) {
					lfCombo.setSelectedItem(lfCombo.getItemAt(i));
					break;
				}
			}

			// ���ɐݒ肳��Ă���J�ڐ�y�[�W���A�R���{�{�b�N�X�ł͗\�ߑI����Ԃɂ���
			for(int i=0; i<lfCombo_dest.getItemCount(); i++) {
				String item = (String)lfCombo_dest.getItemAt(i);
				String editingLfDestPageName = focusedLoginForm.destWebPage.pageFileName;
				if(item.equals(editingLfDestPageName)) {
					lfCombo_dest.setSelectedItem(lfCombo_dest.getItemAt(i));
					break;
				}
			}
		}

		// �ҏW���[�h��
		if(edit) {
			// �u�ȏ�̓��e�ɕύX����v�{�^��
			editFinishButton.setActionCommand("PageElement�ҏW - ���O�C���t�H�[��");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, lfCombo_dest);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, lfCombo_dest);
			add(editFinishButton);
		}
		// �ǉ����[�h��
		else {
			// �u�ȏ�̓��e�Ńy�[�W�\���v�f��ǉ�����v�{�^��
			additionButton.setActionCommand("PageElement�ǉ� - ���O�C���t�H�[��");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, lfCombo_dest);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, lfCombo_dest);
			add(additionButton);
		}
		/* �����܂� */

		MainFrame.getInstance().repaintAndValidate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEvent�n���h��
	 * �y�[�W�G�������g�̒ǉ��E�ҏW�����s����
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		// ���݁A�y�[�W�\���v�f��ҏW���Ă���Web�y�[�W
		WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();

		if(cmd.equals("PageElement�ǉ� - ���ʃe�[�u���\���̈�")) {
			// �^�[�Q�b�g�̃y�[�W

			// PageElementDisplayArea���� -> �^�[�Q�b�g�̃y�[�W�֒ǉ�
			PageElementDisplayArea rtd = new PageElementDisplayArea(editingPage);
			editingPage.addPageElement(rtd);
		}
		else if(cmd.equals("PageElement�ҏW - ���ʃe�[�u���\���̈�")) {
			// �ҏW�Ώۂ�PageElementDisplayArea�擾
			PageElementDisplayArea editingRtd = (PageElementDisplayArea)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
		}
		else if(cmd.equals("PageElement�ǉ� - Create�t�H�[��")) {
			// �e�[�u���擾
			String tableName = (String)cfCombo.getSelectedItem();

			// DIALOG : �e�[�u�����I������Ă��Ȃ�
			if(tableName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "�e�[�u����I�����ĉ������B");
				return;
			}

			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			// �������R�[�h�쐬�\���ۂ���ݒ�
			boolean mult = false;
			/* �����ꂱ�����ɖ߂�
			boolean mult = multipleCb.isSelected();
			*/
			
			// �J�ڐ�y�[�W�̐ݒ�
			String destPageName = (String)cfDestCombo.getSelectedItem();

			// DIALOG : �J�ڐ悪�I������Ă��Ȃ�
			if(destPageName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "�J�ڐ��I�����ĉ������B");
				return;
			}
			
			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);
			
			// PageElementCreateForm�C���X�^���X���� -> �^�[�Q�b�g�̃y�[�W�֒ǉ�
			PageElementCreateForm cf = new PageElementCreateForm(editingPage, table, mult, destPage);
			editingPage.addPageElement(cf);
		}
		else if(cmd.equals("PageElement�ҏW - Create�t�H�[��")) {
			// �ҏW�Ώۂ�PageElementCreateForm�擾
			PageElementCreateForm editingCf = (PageElementCreateForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			// PageElementCreateForm�̑Ώۂ̃e�[�u�����A�V�����I�����ꂽ���̂֕ύX
			String tableName = (String)cfCombo.getSelectedItem();

			// DIALOG : �e�[�u�����I������Ă��Ȃ�
			if(tableName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "�e�[�u����I�����ĉ������B");
				return;
			}
			
			SuperTable table = TableManager.getInstance().getTableByName(tableName);
			editingCf.table = table;

			
			// �������R�[�h�쐬�\���ۂ����A�V�����w�肳�ꂽ�ʂ�ɕύX
			editingCf.multiple = false;
			/* �����ꂱ�����ɖ߂�
			editingCf.multiple = multipleCb.isSelected();
			*/

			
			/*
			 * �J�ڐ�y�[�W�̕ύX
			 */

			String destPageName = (String)cfDestCombo.getSelectedItem();

			// DIALOG : �J�ڐ悪�I������Ă��Ȃ�
			if(destPageName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "�J�ڐ��I�����ĉ������B");
				return;
			}

			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);
			editingCf.destPage = destPage;
		}
		else if(cmd.equals("PageElement�ǉ� - �n�C�p�[�����N")) {
			String destPageName = (String)hlCombo_dest.getSelectedItem();

			// DIALOG : �J�ڐ悪���I��
			if(destPageName.equals("---�I�����ĉ�����---") || destPageName.equals("------Select------")) {
				JOptionPane.showMessageDialog(this, "�J�ڐ��I�����ĉ������B");
				return;
			}
			
			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);

			
			
			String hlText = hlTextField_name.getText();

			PageElement_HyperLink hl = new PageElement_HyperLink(editingPage, destPage, hlText);
			editingPage.addPageElement(hl);
		}
		else if(cmd.equals("PageElement�ҏW - �n�C�p�[�����N")) {
			PageElement_HyperLink editingHl = (PageElement_HyperLink)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			String destPageName = (String)hlCombo_dest.getSelectedItem();

			// DIALOG : �J�ڐ悪���I��
			if(destPageName.equals("---�I�����ĉ�����---") || destPageName.equals("------Select------")) {
				JOptionPane.showMessageDialog(this, "�J�ڐ��I�����ĉ������B");
				return;
			}

			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);
			editingHl.setDestPage(destPage);
			
			String hlText = hlTextField_name.getText();
			editingHl.setText(hlText);
		}
		else if(cmd.equals("PageElement�ǉ� - �e�L�X�g")) {
			// �e�L�X�g��PageElement�Ƃ��Ēǉ�
			String text = textAreaForPeText.getText();
			PageElementText peText = new PageElementText(editingPage, text);
			editingPage.addPageElement(peText);
		}
		else if(cmd.equals("PageElement�ҏW - �e�L�X�g")) {
			PageElementText editingText = (PageElementText)Panel_PeEdit_Left.getInstance().getFocusedPageElement();

			// �e�L�X�g�G���A�̕�������擾���A�V����������Ƃ��ăe�L�X�g�y�[�W�G�������g�ɃZ�b�g
			String text = textAreaForPeText.getText();
			editingText.containingText = text;
		}
		else if(cmd.equals("PageElement�ǉ� - ���O�C���t�H�[��")) {
			// �A�J�E���g�e�[�u���擾
			String tableName = (String)lfCombo.getSelectedItem();
			AccountTable table = (AccountTable)(TableManager.getInstance().getTableByName(tableName));

			// �J�ڐ�Web�y�[�W�iWebPage�C���X�^���X�j���擾
			String destPageName = (String)lfCombo_dest.getSelectedItem();
			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);

			// PageElementLoginForm���� -> Web�y�[�W�֒ǉ�
			PageElementLoginForm loginForm = new PageElementLoginForm(editingPage, table, destPage);
			editingPage.addPageElement(loginForm);
		}
		else if(cmd.equals("PageElement�ҏW - ���O�C���t�H�[��")) {
			// �ҏW�Ώۂ�PageElementLoginForm�擾
			PageElementLoginForm editingLoginForm = (PageElementLoginForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			// PageElementLoginForm�̃A�J�E���g�e�[�u����I�𒆂̂��̂֕ύX
			String tableName = (String)lfCombo.getSelectedItem();
			AccountTable table = (AccountTable)(TableManager.getInstance().getTableByName(tableName));
			editingLoginForm.accountTable = table;

			// �J�ڐ�Web�y�[�W��I�𒆂̂��̂֕ύX
			String destPageName = (String)lfCombo_dest.getSelectedItem();
			WebPage newDestPage = WebPageManager.getInstance().getPageByName(destPageName);
			editingLoginForm.destWebPage = newDestPage;
		}

		// �S�R���|�[�l���g����  -> ���p�l���̃e�L�X�g�y�C���Ĕz�u
		removeAll();
		Panel_PeEdit_Left.getInstance().locateTextPanes();

		// �t���[���ĕ`��
		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}

	
	
	
	
	public void removeAllCompsPeEditRight() {
		removeAll();
	}

	
	

	
	public static Panel_PeEdit_Right getInstance() {
		return Panel_PeEdit_Right.obj;
	}





	/*
	 * NOTICE :
	 * ���e�X�g�p
	 */
	public void setHyperLinkDestPage(int jComboBoxIndex) {
		hlCombo_dest.setSelectedIndex(jComboBoxIndex);
	}

	/*
	 * NOTICE :
	 * ���e�X�g�p
	 */
	public void inputHyperLinkText(String text) {
		hlTextField_name.setText(text);
	}
	
	/*
	 * NOTICE :
	 * ���e�X�g�p
	 */
	public void clickAdditionButton() {
		additionButton.doClick();
	}
	
	
	public static void updateInstance(Panel_PeEdit_Right newObject) {
		Panel_PeEdit_Right.obj = newObject;
	}

}
