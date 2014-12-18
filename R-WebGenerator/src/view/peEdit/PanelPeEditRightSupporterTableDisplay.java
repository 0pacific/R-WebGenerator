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
import pageElement.PageElementTableDisplay;
import property.GeneratorProperty;
import table.SuperTable;
import table.TableManager;
import webPage.WebPage;
import utility.*;





public class PanelPeEditRightSupporterTableDisplay implements ActionListener,Serializable {
	Panel_PeEdit_Right panelRight;
	SerializableSpringLayout springLayout;

	// �R���|�[�l���g�iNullPointerException����̂��߁A���������Ă���j
	JTextArea textPaneTableSelect = new JTextArea();
	JTextPane textPaneTableNotSelected = new JTextPane();
	JComboBox comboBoxTableSelect = new JComboBox();
	JTextPane textPaneFieldLimitDesc = new JTextPane();
	ArrayList<JCheckBox> checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�!
	 */
	private static PanelPeEditRightSupporterTableDisplay obj = new PanelPeEditRightSupporterTableDisplay();
	
	
	
	
	
	private PanelPeEditRightSupporterTableDisplay() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ�͕ҏW����TableDisplay��n���A�ǉ��̏ꍇ��null��n��
	 */
	public void locateForTableDisplay(PageElementTableDisplay tableDisplayToEdit) {
		boolean japanese = GeneratorProperty.language.equals("Japanese");

		// �S�R���|�[�l���g����
		panelRight.removeAll();

		// �e�L�X�g�G���A
		String msg1 = japanese ? "�\������e�[�u����I�����ĉ�����" : "Select table to display";
		textPaneTableSelect = new JTextArea(msg1, 1, 30);
		textPaneTableSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textPaneTableSelect, 20, SpringLayout.NORTH, panelRight);
		springLayout.putConstraint(SpringLayout.WEST, textPaneTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneTableSelect);

		// �R���{�{�b�N�X�i�e�[�u���I���j������
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		tableNames.add(0, japanese?"---�I�����ĉ�����---":"------Select------");
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		comboBoxTableSelect = new JComboBox(tableNameStrings);
		comboBoxTableSelect.addActionListener(this);
		comboBoxTableSelect.setActionCommand("�e�[�u���I���R���{�{�b�N�X�̍��ڕύX");
		// �R���{�{�b�N�X�i�e�[�u���I���j�z�u
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxTableSelect, 20, SpringLayout.SOUTH, textPaneTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxTableSelect);
		
		// �ҏW���[�h�̏ꍇ -> �R���{�{�b�N�X�i�e�[�u���I���j�̍��ڂ��A�ҏW�Ώۂ�TableDisplay�Ɍ��ݐݒ肳��Ă�����̂ɂ��Ă���
		if(tableDisplayToEdit instanceof PageElementTableDisplay) {
			for(int i=0; i<comboBoxTableSelect.getItemCount(); i++) {
				String item = (String)comboBoxTableSelect.getItemAt(i);
				String focusedTdTableName = tableDisplayToEdit.getTable().getTableName();
				if(item.equals(focusedTdTableName)) {
					comboBoxTableSelect.setSelectedItem(comboBoxTableSelect.getItemAt(i));
					break;
				}
			}
		}


		
		// �R���{�{�b�N�X�̏�Ԃɉ����ĕ���
		// �ҏW���Ȃ�e�[�u���̊e�t�B�[���h���̃`�F�b�N�{�b�N�X���u�����i�`�F�b�N�͂܂��j
		// �ǉ����Ȃ�A�e�[�u����I������Ƃ̃��b�Z�[�W���\������邾��
		relocateSinceCheckBoxArrayFieldLimit();

		
		// �ҏW�� -> �`�F�b�N�{�b�N�X�����炩���߃`�F�b�N ��ԁE���`�F�b�N��Ԃɂ���
		if(tableDisplayToEdit instanceof PageElementTableDisplay) {
			ArrayList<Integer> limitedOffsets = tableDisplayToEdit.getLimitedOffsets();

			// �܂��S�����`�F�b�N��
			for(int i=0; i<checkBoxArrayFieldLimit.size(); i++) {
				checkBoxArrayFieldLimit.get(i).setSelected(false);
			}

			// �t�B�[���h����őI�є�����Ă�����̂�S���`�F�b�N
			for(int i=0; i<limitedOffsets.size(); i++) {
				this.checkBoxArrayFieldLimit.get(limitedOffsets.get(i)).setSelected(true);
			}
		}
		

		
		
		// �ҏW���[�h
		if(tableDisplayToEdit instanceof PageElementTableDisplay) {
			// �ҏW���s�{�^��
			buttonEditExecute = new JButton("�ȏ�̓��e�ɕύX����");
			buttonEditExecute.addActionListener(this);
			buttonEditExecute.setActionCommand("�ҏW���s");
			springLayout.putConstraint(SpringLayout.SOUTH,	buttonEditExecute,	-40,	SpringLayout.SOUTH,	panelRight);
			springLayout.putConstraint(SpringLayout.WEST,	buttonEditExecute,	20,		SpringLayout.WEST,	panelRight);
			panelRight.add(buttonEditExecute);
		}
		// �ǉ����[�h
		else {
			// �ǉ����s�{�^��
			buttonAddExecute = new JButton(japanese?"�y�[�W�\���v�f��ǉ�":"Add Page Factor");
			buttonAddExecute.addActionListener(this);
			buttonAddExecute.setActionCommand("�ǉ����s");
			springLayout.putConstraint(SpringLayout.SOUTH,	buttonAddExecute,	-40,	SpringLayout.SOUTH, panelRight);
			springLayout.putConstraint(SpringLayout.WEST,	buttonAddExecute,	20,		SpringLayout.WEST, panelRight);
			panelRight.add(buttonAddExecute);
		}

		MainFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * �R���{�{�b�N�X�i�e�[�u���I���j�̓��e�����āA�t�B�[���h����̃`�F�b�N�{�b�N�X�z���z�u������
	 */
	public void relocateSinceCheckBoxArrayFieldLimit() {
		boolean japanese = GeneratorProperty.japanese();
		
		// ���`�F�b�N�{�b�N�X���u����Ă���Ȃ�S���������Ă���
		for(int i=0; i<this.checkBoxArrayFieldLimit.size(); i++) {
			JCheckBox cb = this.checkBoxArrayFieldLimit.get(i);
			panelRight.remove(cb);
		}
		
		// �R���{�{�b�N�X�őI������Ă���e�[�u���擾�i���I�������j
		String tableName = (String)this.comboBoxTableSelect.getSelectedItem();

		// �e�[�u�����I�� -> �e�L�X�g�y�C����z�u���ďI��
		if(tableName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
			String msg1 = GeneratorProperty.japanese() ? "�e�[�u�������I���ł��B" : "Table is unselected.";
			textPaneTableNotSelected = new JTextPane();
			textPaneTableNotSelected.setText(msg1);
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneTableNotSelected,	20,		SpringLayout.SOUTH,	comboBoxTableSelect);
			springLayout.putConstraint(SpringLayout.WEST,	textPaneTableNotSelected,	20,		SpringLayout.WEST,	panelRight);
			panelRight.add(textPaneTableNotSelected);
			return;
		}

		SuperTable table = TableManager.getInstance().getTableByName(tableName);

		// �e�[�u�����I���̏ꍇ�̃e�L�X�g�y�C��������
		panelRight.remove(textPaneTableNotSelected);


		// �e�L�X�g�y�C��
		String msg2 = GeneratorProperty.japanese() ? "�����Ɋւ�炸�\�����Ȃ��t�B�[���h�́A�`�F�b�N���O���ĉ������B" : "If you don't display some fields' independently of accessibility, uncheck them.";
		textPaneFieldLimitDesc = new JTextPane();
		textPaneFieldLimitDesc.setText(msg2);
		Slpc.put(springLayout, "N", textPaneFieldLimitDesc, "S", comboBoxTableSelect, 20);
		Slpc.put(springLayout, "W", textPaneFieldLimitDesc, "W", panelRight, 20);
		panelRight.add(textPaneFieldLimitDesc);
		
		// �`�F�b�N�{�b�N�X�z��i�t�B�[���h����j
		checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
		for(int i=0; i<table.getFieldNum(); i++) {
			JCheckBox cb = new JCheckBox();
			cb.setText(table.getField(i).getFieldName());

			checkBoxArrayFieldLimit.add(cb);

			// ���̃`�F�b�N�{�b�N�X��z�u
			springLayout.putConstraint(SpringLayout.NORTH,	cb,	(30*i)+20,	SpringLayout.SOUTH,	textPaneFieldLimitDesc);
			springLayout.putConstraint(SpringLayout.WEST,	cb,	20,			SpringLayout.WEST,	panelRight);
			panelRight.add(cb);

			// �`�F�b�N���Ă����i�f�t�H���g�ł́u�\���v�Ȃ̂Łj
			// ����͒ǉ����݈̂Ӗ��������A�ҏW���̏ꍇ�͂��̌�����̒l�ɍ��킹�ă`�F�b�N�E�`�F�b�N�A�E�g�����
			cb.setSelected(true);
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		if(cmd.equals("�e�[�u���I���R���{�{�b�N�X�̍��ڕύX")) {
			this.relocateSinceCheckBoxArrayFieldLimit();
		}
		else if(cmd.equals("�ҏW���s") || cmd.equals("�ǉ����s")) {
			// �R���{�{�b�N�X�̓��e�ɉ����ăe�[�u���擾
			String tableName = (String)this.comboBoxTableSelect.getSelectedItem();
			if(tableName.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
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
			if(offsetArrayList.size()==0) {
				// �t�B�[���h����Ńt�B�[���h���P���I������Ă��Ȃ�
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "�\������t�B�[���h�͂P�ȏ�`�F�b�N���ĉ������B");
				return;
			}
			int[] offsetArray = new int[offsetArrayList.size()];
			for(int i=0; i<offsetArrayList.size(); i++) {
				offsetArray[i] = offsetArrayList.get(i);
			}


			if(cmd.equals("�ǉ����s")) {
				// PageElementTableDisplay���� -> �y�[�W�֒ǉ�
				PageElementTableDisplay td = new PageElementTableDisplay(targetWebPage, table, offsetArray);
				targetWebPage.addPageElement(td);
			}
			else if(cmd.equals("�ҏW���s")) {
				// �ҏW�Ώۂ�TableDisplay�̕\���e�[�u���E�t�B�[���h������A�w��ʂ�ɕύX
				PageElementTableDisplay editingTd = (PageElementTableDisplay)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingTd.setTable(table);
				editingTd.setLimitedOffsets(offsetArray);
			}

			// �E�p�l���̑S�R���|�[�l���g�����A���p�l���̃R���|�[�l���g�Ĕz�u
			panelRight.removeAll();
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// ���C���t���[���ĕ`��
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterTableDisplay getInstance() {
		return PanelPeEditRightSupporterTableDisplay.obj;
	}




	public static void updateInstance(PanelPeEditRightSupporterTableDisplay newObject) {
		PanelPeEditRightSupporterTableDisplay.obj = newObject;
	}
}
