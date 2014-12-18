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

	// コンポーネント（NullPointerException回避のため、初期化してある）
	JTextArea textPaneTableSelect = new JTextArea();
	JTextPane textPaneTableNotSelected = new JTextPane();
	JComboBox comboBoxTableSelect = new JComboBox();
	JTextPane textPaneFieldLimitDesc = new JTextPane();
	ArrayList<JCheckBox> checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	
	/*
	 * インスタンス参照変数は最後!
	 */
	private static PanelPeEditRightSupporterTableDisplay obj = new PanelPeEditRightSupporterTableDisplay();
	
	
	
	
	
	private PanelPeEditRightSupporterTableDisplay() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	/*
	 * NOTICE :
	 * 編集の場合は編集するTableDisplayを渡し、追加の場合はnullを渡す
	 */
	public void locateForTableDisplay(PageElementTableDisplay tableDisplayToEdit) {
		boolean japanese = GeneratorProperty.language.equals("Japanese");

		// 全コンポーネント除去
		panelRight.removeAll();

		// テキストエリア
		String msg1 = japanese ? "表示するテーブルを選択して下さい" : "Select table to display";
		textPaneTableSelect = new JTextArea(msg1, 1, 30);
		textPaneTableSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textPaneTableSelect, 20, SpringLayout.NORTH, panelRight);
		springLayout.putConstraint(SpringLayout.WEST, textPaneTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneTableSelect);

		// コンボボックス（テーブル選択）初期化
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		tableNames.add(0, japanese?"---選択して下さい---":"------Select------");
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		comboBoxTableSelect = new JComboBox(tableNameStrings);
		comboBoxTableSelect.addActionListener(this);
		comboBoxTableSelect.setActionCommand("テーブル選択コンボボックスの項目変更");
		// コンボボックス（テーブル選択）配置
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxTableSelect, 20, SpringLayout.SOUTH, textPaneTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxTableSelect);
		
		// 編集モードの場合 -> コンボボックス（テーブル選択）の項目を、編集対象のTableDisplayに現在設定されているものにしておく
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


		
		// コンボボックスの状態に応じて分岐
		// 編集時ならテーブルの各フィールド分のチェックボックスが置かれる（チェックはまだ）
		// 追加時なら、テーブルを選択しろとのメッセージが表示されるだけ
		relocateSinceCheckBoxArrayFieldLimit();

		
		// 編集時 -> チェックボックスをあらかじめチェック 状態・未チェック状態にする
		if(tableDisplayToEdit instanceof PageElementTableDisplay) {
			ArrayList<Integer> limitedOffsets = tableDisplayToEdit.getLimitedOffsets();

			// まず全部未チェックに
			for(int i=0; i<checkBoxArrayFieldLimit.size(); i++) {
				checkBoxArrayFieldLimit.get(i).setSelected(false);
			}

			// フィールド限定で選び抜かれているものを全部チェック
			for(int i=0; i<limitedOffsets.size(); i++) {
				this.checkBoxArrayFieldLimit.get(limitedOffsets.get(i)).setSelected(true);
			}
		}
		

		
		
		// 編集モード
		if(tableDisplayToEdit instanceof PageElementTableDisplay) {
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

	
	
	
	/*
	 * コンボボックス（テーブル選択）の内容を見て、フィールド限定のチェックボックス配列を配置し直す
	 */
	public void relocateSinceCheckBoxArrayFieldLimit() {
		boolean japanese = GeneratorProperty.japanese();
		
		// 今チェックボックスが置かれているなら全部除去しておく
		for(int i=0; i<this.checkBoxArrayFieldLimit.size(); i++) {
			JCheckBox cb = this.checkBoxArrayFieldLimit.get(i);
			panelRight.remove(cb);
		}
		
		// コンボボックスで選択されているテーブル取得（未選択かも）
		String tableName = (String)this.comboBoxTableSelect.getSelectedItem();

		// テーブル未選択 -> テキストペインを配置して終了
		if(tableName.equals(japanese?"---選択して下さい---":"------Select------")) {
			String msg1 = GeneratorProperty.japanese() ? "テーブルが未選択です。" : "Table is unselected.";
			textPaneTableNotSelected = new JTextPane();
			textPaneTableNotSelected.setText(msg1);
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneTableNotSelected,	20,		SpringLayout.SOUTH,	comboBoxTableSelect);
			springLayout.putConstraint(SpringLayout.WEST,	textPaneTableNotSelected,	20,		SpringLayout.WEST,	panelRight);
			panelRight.add(textPaneTableNotSelected);
			return;
		}

		SuperTable table = TableManager.getInstance().getTableByName(tableName);

		// テーブル未選択の場合のテキストペインを除去
		panelRight.remove(textPaneTableNotSelected);


		// テキストペイン
		String msg2 = GeneratorProperty.japanese() ? "権限に関わらず表示しないフィールドは、チェックを外して下さい。" : "If you don't display some fields' independently of accessibility, uncheck them.";
		textPaneFieldLimitDesc = new JTextPane();
		textPaneFieldLimitDesc.setText(msg2);
		Slpc.put(springLayout, "N", textPaneFieldLimitDesc, "S", comboBoxTableSelect, 20);
		Slpc.put(springLayout, "W", textPaneFieldLimitDesc, "W", panelRight, 20);
		panelRight.add(textPaneFieldLimitDesc);
		
		// チェックボックス配列（フィールド限定）
		checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
		for(int i=0; i<table.getFieldNum(); i++) {
			JCheckBox cb = new JCheckBox();
			cb.setText(table.getField(i).getFieldName());

			checkBoxArrayFieldLimit.add(cb);

			// このチェックボックスを配置
			springLayout.putConstraint(SpringLayout.NORTH,	cb,	(30*i)+20,	SpringLayout.SOUTH,	textPaneFieldLimitDesc);
			springLayout.putConstraint(SpringLayout.WEST,	cb,	20,			SpringLayout.WEST,	panelRight);
			panelRight.add(cb);

			// チェックしておく（デフォルトでは「表示」なので）
			// これは追加時のみ意味を持ち、編集時の場合はこの後既存の値に合わせてチェック・チェックアウトされる
			cb.setSelected(true);
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		if(cmd.equals("テーブル選択コンボボックスの項目変更")) {
			this.relocateSinceCheckBoxArrayFieldLimit();
		}
		else if(cmd.equals("編集実行") || cmd.equals("追加実行")) {
			// コンボボックスの内容に応じてテーブル取得
			String tableName = (String)this.comboBoxTableSelect.getSelectedItem();
			if(tableName.equals(japanese?"---選択して下さい---":"------Select------")) {
				// 何もしない
				return;
			}
			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			
			// 追加・編集対象のページ
			WebPage targetWebPage = Panel_PeEdit_Left.getInstance().getEditingPage();

			
			// フィールド限定のチェックボックスを見ていき、チェックされているフィールドのオフセットを集める
			// 一度IntegerのArrayListを作り、後でint配列にする
			ArrayList<Integer> offsetArrayList = new ArrayList<Integer>();
			for(int i=0; i<table.getFieldNum(); i++) {
				JCheckBox checkBox = this.checkBoxArrayFieldLimit.get(i);
				if(checkBox.isSelected()) {
					offsetArrayList.add(new Integer(i));
				}
			}
			if(offsetArrayList.size()==0) {
				// フィールド限定でフィールドが１つも選択されていない
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "表示するフィールドは１つ以上チェックして下さい。");
				return;
			}
			int[] offsetArray = new int[offsetArrayList.size()];
			for(int i=0; i<offsetArrayList.size(); i++) {
				offsetArray[i] = offsetArrayList.get(i);
			}


			if(cmd.equals("追加実行")) {
				// PageElementTableDisplay生成 -> ページへ追加
				PageElementTableDisplay td = new PageElementTableDisplay(targetWebPage, table, offsetArray);
				targetWebPage.addPageElement(td);
			}
			else if(cmd.equals("編集実行")) {
				// 編集対象のTableDisplayの表示テーブル・フィールド限定を、指定通りに変更
				PageElementTableDisplay editingTd = (PageElementTableDisplay)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingTd.setTable(table);
				editingTd.setLimitedOffsets(offsetArray);
			}

			// 右パネルの全コンポーネント除去、左パネルのコンポーネント再配置
			panelRight.removeAll();
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// メインフレーム再描画
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterTableDisplay getInstance() {
		return PanelPeEditRightSupporterTableDisplay.obj;
	}




	public static void updateInstance(PanelPeEditRightSupporterTableDisplay newObject) {
		PanelPeEditRightSupporterTableDisplay.obj = newObject;
	}
}
