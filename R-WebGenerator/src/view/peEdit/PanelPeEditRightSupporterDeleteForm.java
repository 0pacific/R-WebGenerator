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

	// コンポーネント（NullPointerException回避のため、初期化してある）
	JTextArea textPaneTableSelect = new JTextArea();
	JTextPane textPaneTableNotSelected = new JTextPane();
	JComboBox comboBoxTableSelect = new JComboBox();
	JTextPane textPaneFieldLimitDesc = new JTextPane();
	ArrayList<JCheckBox> checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	JComboBox comboBoxDestPageSelect = new JComboBox();

	/*
	 * インスタンス参照変数は最後!
	 */
	private static PanelPeEditRightSupporterDeleteForm obj = new PanelPeEditRightSupporterDeleteForm();
	
	
	
	
	
	private PanelPeEditRightSupporterDeleteForm() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	public void locateForDeleteForm(PageElementDeleteForm deleteFormToEdit) {
		boolean japanese = GeneratorProperty.language.equals("Japanese");

		// 全コンポーネント除去
		panelRight.removeAll();

		// テキストエリア
		String msg1 = japanese ? "テーブルを選択" : "Select table";
		textPaneTableSelect = new JTextArea(msg1, 1, 30);
		textPaneTableSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textPaneTableSelect, 20, SpringLayout.NORTH, panelRight);
		springLayout.putConstraint(SpringLayout.WEST, textPaneTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneTableSelect);

		// コンボボックス（テーブル選択）初期化
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		String msg2 = japanese ? "---選択して下さい---" : "------Choose------";
		tableNames.add(0, msg2);
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		comboBoxTableSelect = new JComboBox(tableNameStrings);
		comboBoxTableSelect.addActionListener(this);
		comboBoxTableSelect.setActionCommand("テーブル選択コンボボックスの項目変更");
		// コンボボックス（テーブル選択）配置
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxTableSelect, 20, SpringLayout.SOUTH, textPaneTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxTableSelect);
		
		// 編集モードの場合 -> コンボボックス（テーブル選択）の項目を、編集対象のDeleteFormに現在設定されているものにしておく
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



		
		
		// テキストエリア初期化・配置
		String msg3 = japanese ? "更新成功後の遷移先を選択" : "Destination Web page :";
		JTextArea textPaneDestPageSelect = new JTextArea(msg3, 1, 30);
		textPaneDestPageSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH,	textPaneDestPageSelect, 20,	SpringLayout.SOUTH, comboBoxTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, textPaneDestPageSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneDestPageSelect);

		
		// コンボボックス（遷移先選択）初期化
		ArrayList<String> destPageNames = WebPageManager.getInstance().getPageNameArrayList();
		String msg4 = japanese ? "---遷移先を１つ選択---" : "------Choose------";
		destPageNames.add(0, msg4);

		destPageNames.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
			
		String[] destPageNameStrings = (String[])destPageNames.toArray(new String[0]);
		comboBoxDestPageSelect = new JComboBox(destPageNameStrings);
		
		// 追加モード -> コンボボックスを"---選択して下さい---"にしておく
		if(deleteFormToEdit==null) {
			comboBoxDestPageSelect.setSelectedIndex(0);
		}
		// 編集モード -> コンボボックス（遷移先ページ選択）の項目を、編集対象のDeleteFormに現在設定されているものにしておく
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

		// コンボボックス（遷移先Webページ選択）配置
		springLayout.putConstraint(SpringLayout.NORTH,	comboBoxDestPageSelect, 20,	SpringLayout.SOUTH, textPaneDestPageSelect);
		springLayout.putConstraint(SpringLayout.WEST,	comboBoxDestPageSelect, 20,	SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxDestPageSelect);
		
		

		
		
		// 編集モード
		if(deleteFormToEdit instanceof PageElementDeleteForm) {
			// 編集実行ボタン
			buttonEditExecute = new JButton("以上の内容に変更する");
			buttonEditExecute.addActionListener(this);
			buttonEditExecute.setActionCommand("編集実行");
			springLayout.putConstraint(SpringLayout.SOUTH,	buttonEditExecute,	-40,	SpringLayout.SOUTH,	panelRight);
			springLayout.putConstraint(SpringLayout.WEST,	buttonEditExecute,	20,		SpringLayout.WEST,	panelRight);
			panelRight.add(buttonEditExecute);
		}
		// 追加モード
		else {
			// 追加実行ボタン
			buttonAddExecute = new JButton(japanese?"ページ構成要素を追加":"Add Page Factor");
			buttonAddExecute.addActionListener(this);
			buttonAddExecute.setActionCommand("追加実行");
			springLayout.putConstraint(SpringLayout.SOUTH,	buttonAddExecute,	-40,	SpringLayout.SOUTH, panelRight);
			springLayout.putConstraint(SpringLayout.WEST,	buttonAddExecute,	20,		SpringLayout.WEST, panelRight);
			panelRight.add(buttonAddExecute);
		}

		MainFrame.repaintAndValidate();
	}

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("編集実行") || cmd.equals("追加実行")) {
			// コンボボックスの内容に応じてテーブル取得
			String tableName = (String)this.comboBoxTableSelect.getSelectedItem();
			if(tableName.equals("---選択して下さい---")) {
				// 何もしない
				return;
			}
			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			
			// 追加・編集対象のページ
			WebPage targetWebPage = Panel_PeEdit_Left.getInstance().getEditingPage();

			// 選択されている遷移先ページをWebPageインスタンスとして取得
			String selectedDestPageName = (String)this.comboBoxDestPageSelect.getSelectedItem();
			// DIALOG : 遷移先ページが選択されていない
			if(selectedDestPageName.equals("---選択して下さい---")) {
				// メッセージを出して終わり
				String msg1 = GeneratorProperty.japanese() ? "遷移先を選択して下さい" : "Select destination Web page";
				JOptionPane.showMessageDialog(MainFrame.getInstance(), msg1);
				return;
			}
			WebPage destPage = WebPageManager.getInstance().getPageByName(selectedDestPageName);
			
			if(cmd.equals("追加実行")) {
				// PageElementDeleteForm生成 -> ページへ追加
				PageElementDeleteForm td = new PageElementDeleteForm(targetWebPage, table, destPage);
				targetWebPage.addPageElement(td);
			}
			else if(cmd.equals("編集実行")) {
				// 編集対象のDeleteFormの表示テーブル・フィールド限定を、指定通りに変更
				PageElementDeleteForm editingTd = (PageElementDeleteForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingTd.table = table;
				editingTd.destPage = destPage;
			}

			// 右パネルの全コンポーネント除去、左パネルのコンポーネント再配置
			panelRight.removeAll();
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// メインフレーム再描画
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterDeleteForm getInstance() {
		return PanelPeEditRightSupporterDeleteForm.obj;
	}




	public static void updateInstance(PanelPeEditRightSupporterDeleteForm newObject) {
		PanelPeEditRightSupporterDeleteForm.obj = newObject;
	}
}
