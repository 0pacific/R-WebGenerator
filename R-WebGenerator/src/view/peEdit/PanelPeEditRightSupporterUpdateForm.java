package view.peEdit;

import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import mainFrame.MainFrame;
import pageElement.*;
import property.GeneratorProperty;
import table.SuperTable;
import table.TableManager;
import webPage.WebPage;
import webPage.WebPageManager;
import utility.*;




public class PanelPeEditRightSupporterUpdateForm implements ActionListener,Serializable {
	Panel_PeEdit_Right panelRight;
	SerializableSpringLayout springLayout;

	// �ҏW����Update Form�i�ҏW���[�h�ł�PageElementUpdateForm�C���X�^���X���A�ǉ����[�h�ł�null���i�[�����j
	public PageElementUpdateForm updateFormToEdit;

	// �ǉ���ҏW�̂��߂̏����͂��n�܂��Ă���A�e�[�u���I���R���{�{�b�N�X����x���������ĂȂ���
	// �ǉ���ҏW���n�߂��u�Ԃ�true�ƂȂ�A�e�[�u���I���R���{�{�b�N�X���P��ł��������false�ɂȂ�
	// ���������u�ԂɃR���|�[�l���g�̍Ĕz�u���s���邪�A���̂Ƃ��ɕs�����������Ȃ��悤���̃C���X�^���X�ϐ���݂��Ă���
	public boolean comboBoxTableSelectedNotChangedAnyTime = true;
	
	// �R���|�[�l���g�iNullPointerException����̂��߁A���������Ă���j
	JTextArea textPaneTableSelect = new JTextArea();
	JTextPane textPaneTableNotSelected = new JTextPane();
	JComboBox comboBoxTableSelect = new JComboBox();
	ArrayList<JCheckBox> checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
	JTextPane textPaneDestPageSelect = new JTextPane();
	JComboBox comboBoxDestPageSelect = new JComboBox();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�!
	 */
	private static PanelPeEditRightSupporterUpdateForm obj = new PanelPeEditRightSupporterUpdateForm();
	
	
	
	
	
	private PanelPeEditRightSupporterUpdateForm() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ�͕ҏW����Update Form��n���A�ǉ��̏ꍇ��null��n��
	 */
	public void locateForUpdateForm(PageElementUpdateForm updateFormToEdit) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.updateFormToEdit = updateFormToEdit;
		this.comboBoxTableSelectedNotChangedAnyTime = true;
		
		// �S�R���|�[�l���g����
		panelRight.removeAll();

		// �e�L�X�g�y�C��������
		String msg1 = GeneratorProperty.japanese() ? "�e�[�u���ƃt�B�[���h�̑I��\n�@�@...�@�e�[�u����I�����A�ǂ̃t�B�[���h���X�V���邩�I�����ĉ������B" : "Select table and check fields to update.\nUnchecked field won't be updated despite accessibility.";
		textPaneTableSelect = new JTextArea(msg1, 2, 40);
		textPaneTableSelect.setEditable(false);
		// �e�L�X�g�y�C���z�u
		springLayout.putConstraint(SpringLayout.NORTH, textPaneTableSelect, 20, SpringLayout.NORTH, panelRight);
		springLayout.putConstraint(SpringLayout.WEST, textPaneTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneTableSelect);

		// �R���{�{�b�N�X�i�e�[�u���I���j������
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		tableNames.add(0, japanese?"---�I�����ĉ�����---":"------Select------");
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		comboBoxTableSelect = new JComboBox(tableNameStrings);
		

		// �ҏW���[�h�̏ꍇ -> �R���{�{�b�N�X�i�e�[�u���I���j�̍��ڂ��A�ҏW�Ώۂ�UpdateForm�Ɍ��ݐݒ肳��Ă�����̂ɂ��Ă���
		if(updateFormToEdit instanceof PageElementUpdateForm) {
			for(int i=0; i<comboBoxTableSelect.getItemCount(); i++) {
				String item = (String)comboBoxTableSelect.getItemAt(i);
				String focusedTdTableName = updateFormToEdit.getTable().getTableName();
				if(item.equals(focusedTdTableName)) {
					comboBoxTableSelect.setSelectedIndex(i);
					break;
				}
			}
		}
		// �ǉ����[�h�̏ꍇ -> �R���{�{�b�N�X�i�e�[�u���I���j�̍��ڂ��Ajapanese?"---�I�����ĉ�����---":"------Select------"�ɂ��Ă���
		else {
			comboBoxTableSelect.setSelectedIndex(0);
		}

		// ��ŃR���{�{�b�N�X�������ł������Ă���̂ŁA�����܂Œ��Ă���ActionListener��ǉ����˂΂Ȃ�Ȃ�
		// �i��̂�����ɂ����actionPerformed()���Ăяo����AcomboBoxTableSelectedNotChangedAnyTime��false�ɂȂ�ƍ���j
		comboBoxTableSelect.addActionListener(this);
		comboBoxTableSelect.setActionCommand("�e�[�u���I���R���{�{�b�N�X�̍��ڕύX");

		
		// �R���{�{�b�N�X�i�e�[�u���I���j�z�u
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxTableSelect, 20, SpringLayout.SOUTH, textPaneTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxTableSelect, 0, SpringLayout.WEST, textPaneTableSelect);
		panelRight.add(comboBoxTableSelect);

		
		// �R���{�{�b�N�X�̏�Ԃɉ����ĕ���
		// �ҏW���Ȃ�e�[�u���̊e�t�B�[���h���̃`�F�b�N�{�b�N�X���u�����
		// �ǉ����Ȃ�A�e�[�u����I������Ƃ̃��b�Z�[�W���\������邾��
		relocateSinceCheckBoxArrayFieldLimit();

		
		MainFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * �R���{�{�b�N�X�i�e�[�u���I���j�̓��e�����āA�t�B�[���h����̃`�F�b�N�{�b�N�X�z��ȍ~���Ĕz�u
	 */
	public void relocateSinceCheckBoxArrayFieldLimit() {
		boolean japanese = GeneratorProperty.japanese();
		
		// �`�F�b�N�{�b�N�X�z��A�e�[�u�����I���̃e�L�X�g�y�C���A�J�ڐ�y�[�W�I���̃R���{�{�b�N�X�A�ǉ��{�^���A�ҏW�{�^������x����
		for(int i=0; i<this.checkBoxArrayFieldLimit.size(); i++) {
			JCheckBox cb = this.checkBoxArrayFieldLimit.get(i);
			panelRight.remove(cb);
		}
		panelRight.remove(textPaneTableNotSelected);
		panelRight.remove(textPaneDestPageSelect);
		panelRight.remove(this.comboBoxDestPageSelect);
		panelRight.remove(this.buttonAddExecute);
		panelRight.remove(this.buttonEditExecute);
		

		
		// �R���{�{�b�N�X�őI������Ă���e�[�u���擾�i���I�������j
		String tableName = (String)this.comboBoxTableSelect.getSelectedItem();

		// �e�[�u�����I�� -> �e�L�X�g�y�C����z�u
		if(tableName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
			textPaneTableNotSelected = new JTextPane();
			textPaneTableNotSelected.setText(japanese?"�e�[�u�������I���ł��B":"Table is unselected.");
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneTableNotSelected,	20,		SpringLayout.SOUTH,	comboBoxTableSelect);
			springLayout.putConstraint(SpringLayout.WEST,	textPaneTableNotSelected,	20,		SpringLayout.WEST,	panelRight);
			panelRight.add(textPaneTableNotSelected);
		}
		// �e�[�u�����I������Ă��� -> �t�B�[���h����̃`�F�b�N�{�b�N�X�z���z�u
		else {
			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			// �`�F�b�N�{�b�N�X�z��
			checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
			for(int i=0; i<table.getFieldNum(); i++) {
				JCheckBox cb = new JCheckBox();
				cb.setText(table.getField(i).getFieldName());

				checkBoxArrayFieldLimit.add(cb);

				// ���̃`�F�b�N�{�b�N�X��z�u
				springLayout.putConstraint(SpringLayout.NORTH,	cb,	(30*i)+20,	SpringLayout.SOUTH,	comboBoxTableSelect);
				springLayout.putConstraint(SpringLayout.WEST,	cb,	20,			SpringLayout.WEST,	panelRight);
				panelRight.add(cb);
			}
		}

		// �ǉ����[�h�̏ꍇ -> �t�B�[���h����̑S�`�F�b�N�{�b�N�X��\�߃`�F�b�N
		if(updateFormToEdit==null) {
			for(int i=0; i<checkBoxArrayFieldLimit.size(); i++) {
				checkBoxArrayFieldLimit.get(i).setSelected(true);
			}
		}
		// �ҏW���[�h�ŁA���e�[�u���I���R���{�{�b�N�X���܂���x���������Ă��Ȃ��ꍇ -> �`�F�b�N�{�b�N�X�����炩���߃`�F�b�N 
		else if((updateFormToEdit instanceof PageElementUpdateForm) && this.comboBoxTableSelectedNotChangedAnyTime) {
			int[] limitedOffsets = updateFormToEdit.getLimitedOffsets();
			for(int i=0; i<limitedOffsets.length; i++) {
				this.checkBoxArrayFieldLimit.get(limitedOffsets[i]).setSelected(true);
			}
		}
		// �ҏW���[�h�ŁA�e�[�u����ύX�����u�Ԃł���ꍇ -> �t�B�[���h����̑S�`�F�b�N�{�b�N�X��\�߃`�F�b�N
		else if((updateFormToEdit instanceof PageElementUpdateForm) && !this.comboBoxTableSelectedNotChangedAnyTime) {
			for(int i=0; i<checkBoxArrayFieldLimit.size(); i++) {
				checkBoxArrayFieldLimit.get(i).setSelected(true);
			}
		}


		// �e�L�X�g�y�C���������E�z�u
		textPaneDestPageSelect = new JTextPane();
		textPaneDestPageSelect.setEditable(false);
		textPaneDestPageSelect.setText(japanese?"�Q�D�A�b�v�f�[�g��̑J�ڐ�y�[�W":"Destination Page After Update");
		if(tableName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {	// �e�[�u�����I��
			// �e�[�u�����I���������e�L�X�g�y�C���̉��ɔz�u
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneDestPageSelect, 40,	SpringLayout.SOUTH, textPaneTableNotSelected);
		}
		else {	// �e�[�u�����I������Ă���
			// �Ō�̃`�F�b�N�{�b�N�X�̉��ɔz�u
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneDestPageSelect, 20,	SpringLayout.SOUTH, checkBoxArrayFieldLimit.get(checkBoxArrayFieldLimit.size()-1));
		}
		springLayout.putConstraint(SpringLayout.WEST, textPaneDestPageSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneDestPageSelect);

		
		// �R���{�{�b�N�X�i�J�ڐ�Web�y�[�W�I���j�������i�܂��e�[�u���I���R���{�{�b�N�X����x���������Ă��Ȃ��ꍇ�̂݁j
		if(this.comboBoxTableSelectedNotChangedAnyTime) {
			ArrayList<String> destPageNames = WebPageManager.getInstance().getPageNameArrayList();
			destPageNames.add(0, japanese?"---�I�����ĉ�����---":"------Select------");

			// �y�[�W�\���v�f��ҏW���Ă���Web�y�[�W���͔̂����Ă����i�J�ڐ�ɂł��Ȃ��悤�Ɂj
			destPageNames.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
			
			String[] destPageNameStrings = (String[])destPageNames.toArray(new String[0]);
			comboBoxDestPageSelect = new JComboBox(destPageNameStrings);
		}
		
		// �ǉ����[�h�ŁA���e�[�u���I���R���{�{�b�N�X���܂���x���������Ă��Ȃ��ꍇ -> �R���{�{�b�N�X�i�J�ڐ�y�[�W�I���j�̍��ڂ��A"---�I�����ĉ�����---"�ɂ��Ă���
		if(updateFormToEdit==null && this.comboBoxTableSelectedNotChangedAnyTime) {
			comboBoxDestPageSelect.setSelectedIndex(0);
		}
		// �ҏW���[�h�ŁA���e�[�u���I���R���{�{�b�N�X���܂���x���������Ă��Ȃ��ꍇ -> �R���{�{�b�N�X�i�J�ڐ�y�[�W�I���j�̍��ڂ��A�ҏW�Ώۂ�UpdateForm�Ɍ��ݐݒ肳��Ă�����̂ɂ��Ă���
		if((updateFormToEdit instanceof PageElementUpdateForm) && this.comboBoxTableSelectedNotChangedAnyTime) {
			for(int i=0; i<comboBoxDestPageSelect.getItemCount(); i++) {
				String item = (String)comboBoxDestPageSelect.getItemAt(i);
				String currentDestPageName = updateFormToEdit.destPage.pageFileName;
				if(item.equals(currentDestPageName)) {
					comboBoxDestPageSelect.setSelectedIndex(i);
					break;
				}
			}
		}

		// �R���{�{�b�N�X�i�J�ڐ�Web�y�[�W�I���j�z�u
		springLayout.putConstraint(SpringLayout.NORTH,	comboBoxDestPageSelect, 20,	SpringLayout.SOUTH, textPaneDestPageSelect);
		springLayout.putConstraint(SpringLayout.WEST,	comboBoxDestPageSelect, 20,	SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxDestPageSelect);

		
		// �ҏW���[�h
		if(updateFormToEdit instanceof PageElementUpdateForm) {
			// �ҏW���s�{�^��
			buttonEditExecute = new JButton("�ȏ�̓��e�ɕύX����");
			buttonEditExecute.addActionListener(this);
			buttonEditExecute.setActionCommand("�ҏW���s");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonEditExecute,	50,	SpringLayout.SOUTH,	comboBoxDestPageSelect);
			springLayout.putConstraint(SpringLayout.WEST,	buttonEditExecute,	20,	SpringLayout.WEST,	panelRight);
			panelRight.add(buttonEditExecute);
		}
		// �ǉ����[�h
		else {
			// �ǉ����s�{�^��
			buttonAddExecute = new JButton(japanese?"�y�[�W�\���v�f��ǉ�":"Add Page Factor");
			buttonAddExecute.addActionListener(this);
			buttonAddExecute.setActionCommand("�ǉ����s");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonAddExecute,	50,	SpringLayout.SOUTH, comboBoxDestPageSelect);
			springLayout.putConstraint(SpringLayout.WEST,	buttonAddExecute,	20,	SpringLayout.WEST, panelRight);
			panelRight.add(buttonAddExecute);
		}

		
		// ���C���t���[���ĕ`��
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();
		
		if(cmd.equals("�e�[�u���I���R���{�{�b�N�X�̍��ڕύX")) {
			// �R���{�{�b�N�X�i�e�[�u���I���j����x�ł�������ꂽ�Ƃ������Ƃ��L��
			this.comboBoxTableSelectedNotChangedAnyTime = false;

			this.relocateSinceCheckBoxArrayFieldLimit();
		}
		else if(cmd.equals("�ҏW���s") || cmd.equals("�ǉ����s")) {
			// �R���{�{�b�N�X�̓��e�ɉ����ăe�[�u���擾
			String tableName = (String)this.comboBoxTableSelect.getSelectedItem();
			if(tableName.equals("---�I�����ĉ�����---")) {
				// �������Ȃ�
				return;
			}
			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			
			// �ǉ��E�ҏW�Ώۂ̃y�[�W
			WebPage targetWebPage = Panel_PeEdit_Left.getInstance().getEditingPage();

			
			// �t�B�[���h����̃`�F�b�N�{�b�N�X�����Ă����A�`�F�b�N����Ă���t�B�[���h�̃I�t�Z�b�g���W�߂�
			// ��xInteger��ArrayList�����A���int�z��ɂ���
			ArrayList<Integer> offsetArrayList = new ArrayList<Integer>();
			for(int i=0; i<table.getFieldNum(); i++) {
				JCheckBox checkBox = this.checkBoxArrayFieldLimit.get(i);
				if(checkBox.isSelected()) {
					offsetArrayList.add(new Integer(i));
				}
			}
			// DIALOG : �t�B�[���h����Ńt�B�[���h���P���I������Ă��Ȃ�
			if(offsetArrayList.size()==0) {
				// ���b�Z�[�W���o���ďI���
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "�X�V�\�ȃt�B�[���h�͂P�ȏ�I�����ĉ������B");
				return;
			}
			int[] offsetArray = new int[offsetArrayList.size()];
			for(int i=0; i<offsetArrayList.size(); i++) {
				offsetArray[i] = offsetArrayList.get(i);
			}


			// �I������Ă���J�ڐ�y�[�W��WebPage�C���X�^���X�Ƃ��Ď擾
			String selectedDestPageName = (String)this.comboBoxDestPageSelect.getSelectedItem();
			// DIALOG : �J�ڐ�y�[�W���I������Ă��Ȃ�
			if(selectedDestPageName.equals("---�I�����ĉ�����---")) {
				// ���b�Z�[�W���o���ďI���
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "�J�ڐ�y�[�W��I�����ĉ������B");
				return;
			}
			WebPage destPage = WebPageManager.getInstance().getPageByName(selectedDestPageName);
			
	
			// �ǉ��E�ҏW�̎��s
			if(cmd.equals("�ǉ����s")) {
				// PageElementUpdateForm���� -> �y�[�W�֒ǉ�
				PageElementUpdateForm updateForm = new PageElementUpdateForm(targetWebPage, table, offsetArray, destPage);
				targetWebPage.addPageElement(updateForm);
			}
			else if(cmd.equals("�ҏW���s")) {
				// �ҏW�Ώۂ�UpdateForm�̕\���e�[�u���E�t�B�[���h������A�w��ʂ�ɕύX
				PageElementUpdateForm editingUpdateForm = (PageElementUpdateForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingUpdateForm.setTable(table);
				editingUpdateForm.setLimitedOffsets(offsetArray);
				editingUpdateForm.setDestPage(destPage);

				// �y�[�W�G�������g�ҏW�ɕt�����鏈�����s��
				PageElementObserver.getInstance().informPeEdition(editingUpdateForm);
			}

			// �E�p�l���̑S�R���|�[�l���g����
			panelRight.removeAll();
			// ���p�l���̃R���|�[�l���g�Ĕz�u
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// ���C���t���[���ĕ`��
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterUpdateForm getInstance() {
		return PanelPeEditRightSupporterUpdateForm.obj;
	}
	
	
	public static void updateInstance(PanelPeEditRightSupporterUpdateForm newObject) {
		PanelPeEditRightSupporterUpdateForm.obj = newObject;
	}

}
