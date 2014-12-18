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

	// 編集中のUpdate Form（編集モードではPageElementUpdateFormインスタンスが、追加モードではnullが格納される）
	public PageElementUpdateForm updateFormToEdit;

	// 追加や編集のための情報入力が始まってから、テーブル選択コンボボックスが一度もいじられてないか
	// 追加や編集を始めた瞬間にtrueとなり、テーブル選択コンボボックスを１回でもいじるとfalseになる
	// いじった瞬間にコンポーネントの再配置が行われるが、そのときに不整合が生じないようこのインスタンス変数を設けている
	public boolean comboBoxTableSelectedNotChangedAnyTime = true;
	
	// コンポーネント（NullPointerException回避のため、初期化してある）
	JTextArea textPaneTableSelect = new JTextArea();
	JTextPane textPaneTableNotSelected = new JTextPane();
	JComboBox comboBoxTableSelect = new JComboBox();
	ArrayList<JCheckBox> checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
	JTextPane textPaneDestPageSelect = new JTextPane();
	JComboBox comboBoxDestPageSelect = new JComboBox();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	
	/*
	 * インスタンス参照変数は最後!
	 */
	private static PanelPeEditRightSupporterUpdateForm obj = new PanelPeEditRightSupporterUpdateForm();
	
	
	
	
	
	private PanelPeEditRightSupporterUpdateForm() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	/*
	 * NOTICE :
	 * 編集の場合は編集するUpdate Formを渡し、追加の場合はnullを渡す
	 */
	public void locateForUpdateForm(PageElementUpdateForm updateFormToEdit) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.updateFormToEdit = updateFormToEdit;
		this.comboBoxTableSelectedNotChangedAnyTime = true;
		
		// 全コンポーネント除去
		panelRight.removeAll();

		// テキストペイン初期化
		String msg1 = GeneratorProperty.japanese() ? "テーブルとフィールドの選択\n　　...　テーブルを選択し、どのフィールドを更新するか選択して下さい。" : "Select table and check fields to update.\nUnchecked field won't be updated despite accessibility.";
		textPaneTableSelect = new JTextArea(msg1, 2, 40);
		textPaneTableSelect.setEditable(false);
		// テキストペイン配置
		springLayout.putConstraint(SpringLayout.NORTH, textPaneTableSelect, 20, SpringLayout.NORTH, panelRight);
		springLayout.putConstraint(SpringLayout.WEST, textPaneTableSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneTableSelect);

		// コンボボックス（テーブル選択）初期化
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		tableNames.add(0, japanese?"---選択して下さい---":"------Select------");
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		comboBoxTableSelect = new JComboBox(tableNameStrings);
		

		// 編集モードの場合 -> コンボボックス（テーブル選択）の項目を、編集対象のUpdateFormに現在設定されているものにしておく
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
		// 追加モードの場合 -> コンボボックス（テーブル選択）の項目を、japanese?"---選択して下さい---":"------Select------"にしておく
		else {
			comboBoxTableSelect.setSelectedIndex(0);
		}

		// 上でコンボボックスを自動でいじっているので、ここまで着てからActionListenerを追加せねばならない
		// （上のいじりによってactionPerformed()が呼び出され、comboBoxTableSelectedNotChangedAnyTimeがfalseになると困る）
		comboBoxTableSelect.addActionListener(this);
		comboBoxTableSelect.setActionCommand("テーブル選択コンボボックスの項目変更");

		
		// コンボボックス（テーブル選択）配置
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxTableSelect, 20, SpringLayout.SOUTH, textPaneTableSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxTableSelect, 0, SpringLayout.WEST, textPaneTableSelect);
		panelRight.add(comboBoxTableSelect);

		
		// コンボボックスの状態に応じて分岐
		// 編集時ならテーブルの各フィールド分のチェックボックスが置かれる
		// 追加時なら、テーブルを選択しろとのメッセージが表示されるだけ
		relocateSinceCheckBoxArrayFieldLimit();

		
		MainFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * コンボボックス（テーブル選択）の内容を見て、フィールド限定のチェックボックス配列以降を再配置
	 */
	public void relocateSinceCheckBoxArrayFieldLimit() {
		boolean japanese = GeneratorProperty.japanese();
		
		// チェックボックス配列、テーブル未選択のテキストペイン、遷移先ページ選択のコンボボックス、追加ボタン、編集ボタンを一度除去
		for(int i=0; i<this.checkBoxArrayFieldLimit.size(); i++) {
			JCheckBox cb = this.checkBoxArrayFieldLimit.get(i);
			panelRight.remove(cb);
		}
		panelRight.remove(textPaneTableNotSelected);
		panelRight.remove(textPaneDestPageSelect);
		panelRight.remove(this.comboBoxDestPageSelect);
		panelRight.remove(this.buttonAddExecute);
		panelRight.remove(this.buttonEditExecute);
		

		
		// コンボボックスで選択されているテーブル取得（未選択かも）
		String tableName = (String)this.comboBoxTableSelect.getSelectedItem();

		// テーブル未選択 -> テキストペインを配置
		if(tableName.equals(japanese?"---選択して下さい---":"------Select------")) {
			textPaneTableNotSelected = new JTextPane();
			textPaneTableNotSelected.setText(japanese?"テーブルが未選択です。":"Table is unselected.");
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneTableNotSelected,	20,		SpringLayout.SOUTH,	comboBoxTableSelect);
			springLayout.putConstraint(SpringLayout.WEST,	textPaneTableNotSelected,	20,		SpringLayout.WEST,	panelRight);
			panelRight.add(textPaneTableNotSelected);
		}
		// テーブルが選択されている -> フィールド限定のチェックボックス配列を配置
		else {
			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			// チェックボックス配列
			checkBoxArrayFieldLimit = new ArrayList<JCheckBox>();
			for(int i=0; i<table.getFieldNum(); i++) {
				JCheckBox cb = new JCheckBox();
				cb.setText(table.getField(i).getFieldName());

				checkBoxArrayFieldLimit.add(cb);

				// このチェックボックスを配置
				springLayout.putConstraint(SpringLayout.NORTH,	cb,	(30*i)+20,	SpringLayout.SOUTH,	comboBoxTableSelect);
				springLayout.putConstraint(SpringLayout.WEST,	cb,	20,			SpringLayout.WEST,	panelRight);
				panelRight.add(cb);
			}
		}

		// 追加モードの場合 -> フィールド限定の全チェックボックスを予めチェック
		if(updateFormToEdit==null) {
			for(int i=0; i<checkBoxArrayFieldLimit.size(); i++) {
				checkBoxArrayFieldLimit.get(i).setSelected(true);
			}
		}
		// 編集モードで、かつテーブル選択コンボボックスがまだ一度もいじられていない場合 -> チェックボックスをあらかじめチェック 
		else if((updateFormToEdit instanceof PageElementUpdateForm) && this.comboBoxTableSelectedNotChangedAnyTime) {
			int[] limitedOffsets = updateFormToEdit.getLimitedOffsets();
			for(int i=0; i<limitedOffsets.length; i++) {
				this.checkBoxArrayFieldLimit.get(limitedOffsets[i]).setSelected(true);
			}
		}
		// 編集モードで、テーブルを変更した瞬間である場合 -> フィールド限定の全チェックボックスを予めチェック
		else if((updateFormToEdit instanceof PageElementUpdateForm) && !this.comboBoxTableSelectedNotChangedAnyTime) {
			for(int i=0; i<checkBoxArrayFieldLimit.size(); i++) {
				checkBoxArrayFieldLimit.get(i).setSelected(true);
			}
		}


		// テキストペイン初期化・配置
		textPaneDestPageSelect = new JTextPane();
		textPaneDestPageSelect.setEditable(false);
		textPaneDestPageSelect.setText(japanese?"２．アップデート後の遷移先ページ":"Destination Page After Update");
		if(tableName.equals(japanese?"---選択して下さい---":"------Select------")) {	// テーブル未選択
			// テーブル未選択を示すテキストペインの下に配置
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneDestPageSelect, 40,	SpringLayout.SOUTH, textPaneTableNotSelected);
		}
		else {	// テーブルが選択されている
			// 最後のチェックボックスの下に配置
			springLayout.putConstraint(SpringLayout.NORTH,	textPaneDestPageSelect, 20,	SpringLayout.SOUTH, checkBoxArrayFieldLimit.get(checkBoxArrayFieldLimit.size()-1));
		}
		springLayout.putConstraint(SpringLayout.WEST, textPaneDestPageSelect, 20, SpringLayout.WEST, panelRight);
		panelRight.add(textPaneDestPageSelect);

		
		// コンボボックス（遷移先Webページ選択）初期化（まだテーブル選択コンボボックスが一度もいじられていない場合のみ）
		if(this.comboBoxTableSelectedNotChangedAnyTime) {
			ArrayList<String> destPageNames = WebPageManager.getInstance().getPageNameArrayList();
			destPageNames.add(0, japanese?"---選択して下さい---":"------Select------");

			// ページ構成要素を編集しているWebページ自体は抜いておく（遷移先にできないように）
			destPageNames.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
			
			String[] destPageNameStrings = (String[])destPageNames.toArray(new String[0]);
			comboBoxDestPageSelect = new JComboBox(destPageNameStrings);
		}
		
		// 追加モードで、かつテーブル選択コンボボックスがまだ一度もいじられていない場合 -> コンボボックス（遷移先ページ選択）の項目を、"---選択して下さい---"にしておく
		if(updateFormToEdit==null && this.comboBoxTableSelectedNotChangedAnyTime) {
			comboBoxDestPageSelect.setSelectedIndex(0);
		}
		// 編集モードで、かつテーブル選択コンボボックスがまだ一度もいじられていない場合 -> コンボボックス（遷移先ページ選択）の項目を、編集対象のUpdateFormに現在設定されているものにしておく
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

		// コンボボックス（遷移先Webページ選択）配置
		springLayout.putConstraint(SpringLayout.NORTH,	comboBoxDestPageSelect, 20,	SpringLayout.SOUTH, textPaneDestPageSelect);
		springLayout.putConstraint(SpringLayout.WEST,	comboBoxDestPageSelect, 20,	SpringLayout.WEST, panelRight);
		panelRight.add(comboBoxDestPageSelect);

		
		// 編集モード
		if(updateFormToEdit instanceof PageElementUpdateForm) {
			// 編集実行ボタン
			buttonEditExecute = new JButton("以上の内容に変更する");
			buttonEditExecute.addActionListener(this);
			buttonEditExecute.setActionCommand("編集実行");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonEditExecute,	50,	SpringLayout.SOUTH,	comboBoxDestPageSelect);
			springLayout.putConstraint(SpringLayout.WEST,	buttonEditExecute,	20,	SpringLayout.WEST,	panelRight);
			panelRight.add(buttonEditExecute);
		}
		// 追加モード
		else {
			// 追加実行ボタン
			buttonAddExecute = new JButton(japanese?"ページ構成要素を追加":"Add Page Factor");
			buttonAddExecute.addActionListener(this);
			buttonAddExecute.setActionCommand("追加実行");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonAddExecute,	50,	SpringLayout.SOUTH, comboBoxDestPageSelect);
			springLayout.putConstraint(SpringLayout.WEST,	buttonAddExecute,	20,	SpringLayout.WEST, panelRight);
			panelRight.add(buttonAddExecute);
		}

		
		// メインフレーム再描画
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();
		
		if(cmd.equals("テーブル選択コンボボックスの項目変更")) {
			// コンボボックス（テーブル選択）が一度でもいじられたということを記憶
			this.comboBoxTableSelectedNotChangedAnyTime = false;

			this.relocateSinceCheckBoxArrayFieldLimit();
		}
		else if(cmd.equals("編集実行") || cmd.equals("追加実行")) {
			// コンボボックスの内容に応じてテーブル取得
			String tableName = (String)this.comboBoxTableSelect.getSelectedItem();
			if(tableName.equals("---選択して下さい---")) {
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
			// DIALOG : フィールド限定でフィールドが１つも選択されていない
			if(offsetArrayList.size()==0) {
				// メッセージを出して終わり
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "更新可能なフィールドは１つ以上選択して下さい。");
				return;
			}
			int[] offsetArray = new int[offsetArrayList.size()];
			for(int i=0; i<offsetArrayList.size(); i++) {
				offsetArray[i] = offsetArrayList.get(i);
			}


			// 選択されている遷移先ページをWebPageインスタンスとして取得
			String selectedDestPageName = (String)this.comboBoxDestPageSelect.getSelectedItem();
			// DIALOG : 遷移先ページが選択されていない
			if(selectedDestPageName.equals("---選択して下さい---")) {
				// メッセージを出して終わり
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "遷移先ページを選択して下さい。");
				return;
			}
			WebPage destPage = WebPageManager.getInstance().getPageByName(selectedDestPageName);
			
	
			// 追加・編集の実行
			if(cmd.equals("追加実行")) {
				// PageElementUpdateForm生成 -> ページへ追加
				PageElementUpdateForm updateForm = new PageElementUpdateForm(targetWebPage, table, offsetArray, destPage);
				targetWebPage.addPageElement(updateForm);
			}
			else if(cmd.equals("編集実行")) {
				// 編集対象のUpdateFormの表示テーブル・フィールド限定を、指定通りに変更
				PageElementUpdateForm editingUpdateForm = (PageElementUpdateForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingUpdateForm.setTable(table);
				editingUpdateForm.setLimitedOffsets(offsetArray);
				editingUpdateForm.setDestPage(destPage);

				// ページエレメント編集に付随する処理を行う
				PageElementObserver.getInstance().informPeEdition(editingUpdateForm);
			}

			// 右パネルの全コンポーネント除去
			panelRight.removeAll();
			// 左パネルのコンポーネント再配置
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// メインフレーム再描画
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterUpdateForm getInstance() {
		return PanelPeEditRightSupporterUpdateForm.obj;
	}
	
	
	public static void updateInstance(PanelPeEditRightSupporterUpdateForm newObject) {
		PanelPeEditRightSupporterUpdateForm.obj = newObject;
	}

}
