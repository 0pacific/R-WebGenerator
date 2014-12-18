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
import pageElement.PageElementDeleteForm;
import pageElement.PageElementUpdateForm;
import property.GeneratorProperty;
import table.SuperTable;
import table.TableManager;
import webPage.WebPage;
import webPage.WebPageManager;
import utility.*;





public class PanelPeEditRightSupporterDeleteForm implements ActionListener,Serializable {
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
	JComboBox comboBoxDestPageSelect = new JComboBox();

	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�!
	 */
	private static PanelPeEditRightSupporterDeleteForm obj = new PanelPeEditRightSupporterDeleteForm();
	
	
	
	
	
	private PanelPeEditRightSupporterDeleteForm() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	public void locateForDeleteForm(PageElementDeleteForm deleteFormToEdit) {
		boolean japanese = GeneratorProperty.language.equals("Japanese");

		// �S�R���|�[�l���g����
		panelRight.removeAll();

		// �e�L�X�g�G���A
		String msg1 = japanese ? "�e�[�u����I��" : "Select table";
		textPaneTableSelect = new JTextArea(msg1, 1, 30);
		textPaneTableSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textPaneTableSelect, 20, SpringLayout.NORTH, panelRight);
		springLayout.putConstraint(SpringLayout.WEST, textPaneTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneTableSelect);

		// �R���{�{�b�N�X�i�e�[�u���I���j������
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		String msg2 = japanese ? "---�I�����ĉ�����---" : "------Choose------";
		tableNames.add(0, msg2);
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		comboBoxTableSelect = new JComboBox(tableNameStrings);
		comboBoxTableSelect.addActionListener(this);
		comboBoxTableSelect.setActionCommand("�e�[�u���I���R���{�{�b�N�X�̍��ڕύX");
		// �R���{�{�b�N�X�i�e�[�u���I���j�z�u
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxTableSelect, 20, SpringLayout.SOUTH, textPaneTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxTableSelect);
		
		// �ҏW���[�h�̏ꍇ -> �R���{�{�b�N�X�i�e�[�u���I���j�̍��ڂ��A�ҏW�Ώۂ�DeleteForm�Ɍ��ݐݒ肳��Ă�����̂ɂ��Ă���
		if(deleteFormToEdit instanceof PageElementDeleteForm) {
			for(int i=0; i<comboBoxTableSelect.getItemCount(); i++) {
				String item = (String)comboBoxTableSelect.getItemAt(i);
				String focusedTdTableName = deleteFormToEdit.getTable().getTableName();
				if(item.equals(focusedTdTableName)) {
					comboBoxTableSelect.setSelectedItem(comboBoxTableSelect.getItemAt(i));
					break;
				}
			}
		}



		
		
		// �e�L�X�g�G���A�������E�z�u
		String msg3 = japanese ? "�X�V������̑J�ڐ��I��" : "Destination Web page :";
		JTextArea textPaneDestPageSelect = new JTextArea(msg3, 1, 30);
		textPaneDestPageSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH,	textPaneDestPageSelect, 20,	SpringLayout.SOUTH, comboBoxTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, textPaneDestPageSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneDestPageSelect);

		
		// �R���{�{�b�N�X�i�J�ڐ�I���j������
		ArrayList<String> destPageNames = WebPageManager.getInstance().getPageNameArrayList();
		String msg4 = japanese ? "---�J�ڐ���P�I��---" : "------Choose------";
		destPageNames.add(0, msg4);

		destPageNames.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
			
		String[] destPageNameStrings = (String[])destPageNames.toArray(new String[0]);
		comboBoxDestPageSelect = new JComboBox(destPageNameStrings);
		
		// �ǉ����[�h -> �R���{�{�b�N�X��"---�I�����ĉ�����---"�ɂ��Ă���
		if(deleteFormToEdit==null) {
			comboBoxDestPageSelect.setSelectedIndex(0);
		}
		// �ҏW���[�h -> �R���{�{�b�N�X�i�J�ڐ�y�[�W�I���j�̍��ڂ��A�ҏW�Ώۂ�DeleteForm�Ɍ��ݐݒ肳��Ă�����̂ɂ��Ă���
		if((deleteFormToEdit instanceof PageElementDeleteForm)) {
			for(int i=0; i<comboBoxDestPageSelect.getItemCount(); i++) {
				String item = (String)comboBoxDestPageSelect.getItemAt(i);
				String currentDestPageName = deleteFormToEdit.destPage.pageFileName;
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
		if(deleteFormToEdit instanceof PageElementDeleteForm) {
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

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("�ҏW���s") || cmd.equals("�ǉ����s")) {
			// �R���{�{�b�N�X�̓��e�ɉ����ăe�[�u���擾
			String tableName = (String)this.comboBoxTableSelect.getSelectedItem();
			if(tableName.equals("---�I�����ĉ�����---")) {
				// �������Ȃ�
				return;
			}
			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			
			// �ǉ��E�ҏW�Ώۂ̃y�[�W
			WebPage targetWebPage = Panel_PeEdit_Left.getInstance().getEditingPage();

			// �I������Ă���J�ڐ�y�[�W��WebPage�C���X�^���X�Ƃ��Ď擾
			String selectedDestPageName = (String)this.comboBoxDestPageSelect.getSelectedItem();
			// DIALOG : �J�ڐ�y�[�W���I������Ă��Ȃ�
			if(selectedDestPageName.equals("---�I�����ĉ�����---")) {
				// ���b�Z�[�W���o���ďI���
				String msg1 = GeneratorProperty.japanese() ? "�J�ڐ��I�����ĉ�����" : "Select destination Web page";
				JOptionPane.showMessageDialog(MainFrame.getInstance(), msg1);
				return;
			}
			WebPage destPage = WebPageManager.getInstance().getPageByName(selectedDestPageName);
			
			if(cmd.equals("�ǉ����s")) {
				// PageElementDeleteForm���� -> �y�[�W�֒ǉ�
				PageElementDeleteForm td = new PageElementDeleteForm(targetWebPage, table, destPage);
				targetWebPage.addPageElement(td);
			}
			else if(cmd.equals("�ҏW���s")) {
				// �ҏW�Ώۂ�DeleteForm�̕\���e�[�u���E�t�B�[���h������A�w��ʂ�ɕύX
				PageElementDeleteForm editingTd = (PageElementDeleteForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingTd.table = table;
				editingTd.destPage = destPage;
			}

			// �E�p�l���̑S�R���|�[�l���g�����A���p�l���̃R���|�[�l���g�Ĕz�u
			panelRight.removeAll();
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// ���C���t���[���ĕ`��
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterDeleteForm getInstance() {
		return PanelPeEditRightSupporterDeleteForm.obj;
	}




	public static void updateInstance(PanelPeEditRightSupporterDeleteForm newObject) {
		PanelPeEditRightSupporterDeleteForm.obj = newObject;
	}
}
