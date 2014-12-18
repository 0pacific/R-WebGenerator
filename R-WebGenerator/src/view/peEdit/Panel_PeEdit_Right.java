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
	
	// テーブル表示領域追加・編集用
	public JComboBox tdCombo = new JComboBox();
	public JTextPane jtp_tdFieldLimitation;
	public ArrayList<JCheckBox> checkBoxArrayForTdFieldLimitation;	
	
	// ハイパーリンク追加・編集用
	public JComboBox hlCombo_dest = new JComboBox();
	public JTextField hlTextField_name = new JTextField();

	// Createフォーム用
	public JComboBox cfCombo;		// テーブル選択コンボボックス
	public JCheckBox multipleCb;	// 複数レコード作成可能か否か、を選択するチェックボックス
	public JComboBox cfDestCombo;	// 遷移先ページを選択するコンボボックス
	
	// テキストページエレメント用
	public JTextArea textAreaForPeText = new JTextArea();

	// ログインフォーム用
	public JComboBox lfCombo = new JComboBox();
	public JComboBox lfCombo_dest = new JComboBox();
	
	// Page Element 追加・編集の実行ボタン（どのタイプの Page Element でも使う）
	public JButton additionButton = new JButton(japanese?"ページ構成要素を追加":"Add Page Factor");
	public JButton editFinishButton = new JButton(japanese?"以上の通りに変更":"Modify Page Factor");
	
	// パネルのサイズ
	public static final int panelWidth = MainFrame.frameWidth - Panel_PeEdit_Left.panelWidth;
	public static final int panelHeight = MainFrame.frameHeight - PanelPeEditBottom.panelHeight;

	// パネルの背景色
	public static final Color BG_COLOR = Color.WHITE;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	
	/*
	 * インスタンス参照変数は最後！
	 */
	public static Panel_PeEdit_Right obj = new Panel_PeEdit_Right();

	
	
	
	
	
	

	/*
	 * PURPOSE :
	 * コンストラクタ
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
	 * 編集の場合はeditをtrueにし、追加の場合はfalseにする
	 */
	public void locateForResultTableDisplay(boolean edit) {
		// 全コンポーネント除去
		removeAll();

		String text = japanese?"表示する表データの出力元は、遷移プロセス定義の中で指定して下さい。":"Component outputing tabular data for displaying here has to be defined\nin transition process definition.";
		JTextArea textArea1 = new JTextArea(text, 2, 50);
		Slpc.put(springLayout, "N", textArea1, "N", this, 20);
		Slpc.put(springLayout, "W", textArea1, "W", this, 20);
		add(textArea1);

		
		// 編集モード時
		if(edit) {
			// 「以上の内容に変更する」ボタン
			editFinishButton.setActionCommand("PageElement編集 - 結果テーブル表示領域");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, textArea1);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, textArea1);
			add(editFinishButton);
		}
		// 追加モード時
		else {
			// 「以上の内容でページ構成要素を追加する」ボタン
			additionButton.setActionCommand("PageElement追加 - 結果テーブル表示領域");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, textArea1);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, textArea1);
			add(additionButton);
		}
		
		
		MainFrame.getInstance().repaintAndValidate();
	}

	
	
	
	
	/*
	 * NOTICE :
	 * 編集の場合はeditをtrueにし、追加の場合はfalseにする
	 */
	public void locateForCreateForm(boolean edit) {
		boolean japanese = GeneratorProperty.japanese();
		// 全コンポーネント除去
		removeAll();

		// テキストペイン「どのテーブルのレコードを作成するのか選択して下さい」
		String msg1 = GeneratorProperty.japanese() ? "どのテーブルのレコードを作成しますか" : "Select table";
		JTextArea jtp_tblSelCre = new JTextArea(msg1, 1, 30);
		jtp_tblSelCre.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_tblSelCre, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_tblSelCre, 20, SpringLayout.WEST, this);
		add(jtp_tblSelCre);

		// テーブルを選択するコンボボックス
		ArrayList<String> tableNames = TableManager.getInstance().getTableNames();
		tableNames.add(0, japanese?"---選択して下さい---":"------Select------");
		String[] tableNameStrings = (String[])tableNames.toArray(new String[0]);
		cfCombo = new JComboBox(tableNameStrings);
		Slpc.put(springLayout, "N", cfCombo, "S", jtp_tblSelCre, 20);
		Slpc.put(springLayout, "W", cfCombo, "W", jtp_tblSelCre, 0);
		add(cfCombo);

		
		// 編集モード（追加モードではなく）の場合 -> 現在設定されているテーブルの名前をデフォルトで選択
		if(edit) {
			// フォーカスされているPageElementを取得（編集時のみ必要）
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


		
		// 解説テキストペイン
		String msg2 = GeneratorProperty.japanese() ? "レコード作成に成功した後の遷移先を選択して下さい" : "Select destination page";
		JTextArea jtp_destSel = new JTextArea(msg2, 1, 30);
		jtp_destSel.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_destSel, 20, SpringLayout.SOUTH, cfCombo);
		springLayout.putConstraint(SpringLayout.WEST, jtp_destSel, 20, SpringLayout.WEST, this);
		add(jtp_destSel);

		
		
		// 遷移先ページを選択するコンボボックス
		ArrayList<String> pageFileNameArray = WebPageManager.getInstance().getPageNameArrayList();
		pageFileNameArray.add(0, japanese?"---選択して下さい---":"------Select------");
		pageFileNameArray.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
		String[] pageFileNames = (String[])pageFileNameArray.toArray(new String[0]);
		cfDestCombo = new JComboBox(pageFileNames);
		springLayout.putConstraint(SpringLayout.NORTH, cfDestCombo, 20, SpringLayout.SOUTH, jtp_destSel);
		springLayout.putConstraint(SpringLayout.WEST, cfDestCombo, 0, SpringLayout.WEST, jtp_tblSelCre);
		add(cfDestCombo);
		// 編集モード -> 既に設定されている遷移先ページを、コンボボックスでは予め選択状態にする
		if(edit) {
			// フォーカスされているPageElementを取得（編集時のみ必要）
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
		
		
		// 編集モード時
		if(edit) {
			// 「以上の内容に変更する」ボタン
			editFinishButton.setActionCommand("PageElement編集 - Createフォーム");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, cfDestCombo);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, jtp_tblSelCre);
			add(editFinishButton);
		}
		// 追加モード時
		else {
			// 「以上の内容でページ構成要素を追加する」ボタン
			additionButton.setActionCommand("PageElement追加 - Createフォーム");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, cfDestCombo);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, jtp_tblSelCre);
			add(additionButton);
		}
		/* ここまで */

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}

	
	
	
	
	
	/*
	 * NOTICE :
	 * 編集の場合はeditをtrueにし、追加の場合はfalseにする
	 */
	public void locateForHyperLink(boolean edit) {
		Debug.debug_call(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean japanese = GeneratorProperty.language.equals("Japanese");
		
		// 全コンポーネント除去
		removeAll();

		
		
		// フォーカスされているPageElementを取得（編集時のみ必要）
		PageElement_HyperLink focusedHl = null;
		if(edit) {
			focusedHl = (PageElement_HyperLink)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
		}

		
		
		// 遷移先JTextPane
		String msg1 = japanese ? "遷移先のページ :" : "Destination Page :";
		JTextArea jtp_dest = new JTextArea(msg1, 1, 20);
		jtp_dest.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_dest, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_dest, 20, SpringLayout.WEST, this);
		add(jtp_dest);

		// 遷移先ページを選択するコンボボックス
		ArrayList<String> pageFileNameArray = WebPageManager.getInstance().getPageNameArrayList();
		if(japanese)
			pageFileNameArray.add(0, japanese?"---選択して下さい---":"------Select------");
		else
			pageFileNameArray.add(0, "------Select------");
		pageFileNameArray.remove(Panel_PeEdit_Left.getInstance().getEditingPage().pageFileName);
		String[] pageFileNames = pageFileNameArray.toArray(new String[0]);
		hlCombo_dest = new JComboBox(pageFileNames);
		springLayout.putConstraint(SpringLayout.NORTH, hlCombo_dest, 5, SpringLayout.SOUTH, jtp_dest);
		springLayout.putConstraint(SpringLayout.WEST, hlCombo_dest, 0, SpringLayout.WEST, jtp_dest);
		add(hlCombo_dest);

		
		
		// 編集モード -> 既に設定されている遷移先ページを、コンボボックスでは予め選択状態にする
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

		
		
		// 解説テキストペイン
		JTextArea jtp_name = new JTextArea(1, 30);
		if(japanese)
			jtp_name.setText("Webページ上でハイパーリンクを貼る文字列（例 : トップページへ）");
		else
			jtp_name.setText("Text on the Web page :（Example : Top Page）");
		jtp_name.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_name, 40, SpringLayout.SOUTH, hlCombo_dest);
		springLayout.putConstraint(SpringLayout.WEST, jtp_name, 0, SpringLayout.WEST, jtp_dest);
		add(jtp_name);

		// 表示文字列JTextField
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
		
		
		
		// ハイパーリンク編集時
		if(edit) {
			// 「以上の内容に変更する」ボタン
			editFinishButton.setActionCommand("PageElement編集 - ハイパーリンク");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, hlTextField_name);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, jtp_dest);
			add(editFinishButton);
		}
		// ハイパーリンク追加時
		else {
			// 「以上の内容でページ構成要素を追加する」ボタン
			additionButton.setActionCommand("PageElement追加 - ハイパーリンク");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, hlTextField_name);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, jtp_dest);
			add(additionButton);
		}
		/* ここまで */

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	
		Debug.debug_return(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
	
	
	
	/*
	 * NOTICE :
	 * 編集の場合はeditをtrueにし、追加の場合はfalseにする
	 */
	public void locateForText(boolean edit) {
		boolean japanese = GeneratorProperty.language.equals("Japanese");

		// 全コンポーネント除去
		removeAll();

		// テキストエリア
		String msg1 = japanese ? "テキストを入力して下さい。改行もWebページ上で反映されます。" : "Text :";
		JTextArea jtp_desc = new JTextArea(msg1, 1, 30);
		jtp_desc.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_desc, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_desc, 20, SpringLayout.WEST, this);
		add(jtp_desc);

		// テキストエリア
	    textAreaForPeText = new JTextArea(6, 30);
		springLayout.putConstraint(SpringLayout.NORTH, textAreaForPeText, 20, SpringLayout.SOUTH, jtp_desc);
		springLayout.putConstraint(SpringLayout.WEST, textAreaForPeText, 0, SpringLayout.WEST, jtp_desc);
		springLayout.putConstraint(SpringLayout.EAST, textAreaForPeText, 0, SpringLayout.EAST, jtp_desc);
		textAreaForPeText.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(textAreaForPeText);
	    
		// フォーカスされているPageElementを取得（編集時のみ必要）
		PageElementText focusedText = (PageElementText)Panel_PeEdit_Left.getInstance().getFocusedPageElement();

		// 編集モード -> 既に設定されているテキストを、予め表示
		if(edit) {
			String currentText = focusedText.containingText;
			textAreaForPeText.setText(currentText);
		}

		// 編集時
		if(edit) {
			// 「以上の内容に変更する」ボタン
			editFinishButton.setActionCommand("PageElement編集 - テキスト");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, textAreaForPeText);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, textAreaForPeText);
			add(editFinishButton);
		}
		// 追加時
		else {
			// 「以上の内容でページ構成要素を追加する」ボタン
			additionButton.setActionCommand("PageElement追加 - テキスト");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, textAreaForPeText);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, textAreaForPeText);
			add(additionButton);
		}

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}

	
	
	
	
	/*
	 * NOTICE :
	 * 編集の場合はeditをtrueにし、追加の場合はfalseにする
	 */
	public void locateForLoginForm(boolean edit) {
		// 全コンポーネント除去
		removeAll();

		// テキストペイン「どのアカウントテーブルに対応したログインフォームとしますか？」
		JTextPane jtp_tableSelect = new JTextPane();
		jtp_tableSelect.setText("どのアカウントテーブルに対応したログインフォームとしますか？");
		jtp_tableSelect.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_tableSelect, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, jtp_tableSelect, 20, SpringLayout.WEST, this);
		add(jtp_tableSelect);

		// アカウントテーブルを選択するコンボボックス
		String[] atNames = TableManager.getInstance().getAccountTableNames();
		lfCombo = new JComboBox(atNames);
		springLayout.putConstraint(SpringLayout.NORTH, lfCombo, 20, SpringLayout.SOUTH, jtp_tableSelect);
		springLayout.putConstraint(SpringLayout.WEST, lfCombo, 0, SpringLayout.WEST, jtp_tableSelect);
		add(lfCombo);

		
		// テキストペイン
		JTextPane jtp_destSel = new JTextPane();
		jtp_destSel.setText("ログインに成功した際の遷移先を選択して下さい");
		jtp_destSel.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, jtp_destSel, 20, SpringLayout.SOUTH, lfCombo);
		springLayout.putConstraint(SpringLayout.WEST, jtp_destSel, 20, SpringLayout.WEST, this);
		add(jtp_destSel);
		
		// 遷移先ページを選択するコンボボックス
		String[] pageFileNames = WebPageManager.getInstance().getPageNameArray();
		lfCombo_dest = new JComboBox(pageFileNames);
		springLayout.putConstraint(SpringLayout.NORTH, lfCombo_dest, 20, SpringLayout.SOUTH, jtp_destSel);
		springLayout.putConstraint(SpringLayout.WEST, lfCombo_dest, 0, SpringLayout.WEST, jtp_destSel);
		add(lfCombo_dest);

		
		// フォーカスされているPageElementを取得（編集時のみ必要）
		PageElementLoginForm focusedLoginForm = (PageElementLoginForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
		
		
		// 編集モードの場合 -> 
		if(edit) {
			// 現在設定されているアカウントテーブルの名前をデフォルトで選択
			for(int i=0; i<lfCombo.getItemCount(); i++) {
				String item = (String)lfCombo.getItemAt(i);
				String focusedLoginFormTableName = focusedLoginForm.accountTable.getTableName();
				if(item.equals(focusedLoginFormTableName)) {
					lfCombo.setSelectedItem(lfCombo.getItemAt(i));
					break;
				}
			}

			// 既に設定されている遷移先ページを、コンボボックスでは予め選択状態にする
			for(int i=0; i<lfCombo_dest.getItemCount(); i++) {
				String item = (String)lfCombo_dest.getItemAt(i);
				String editingLfDestPageName = focusedLoginForm.destWebPage.pageFileName;
				if(item.equals(editingLfDestPageName)) {
					lfCombo_dest.setSelectedItem(lfCombo_dest.getItemAt(i));
					break;
				}
			}
		}

		// 編集モード時
		if(edit) {
			// 「以上の内容に変更する」ボタン
			editFinishButton.setActionCommand("PageElement編集 - ログインフォーム");
			springLayout.putConstraint(SpringLayout.NORTH, editFinishButton, 40, SpringLayout.SOUTH, lfCombo_dest);
			springLayout.putConstraint(SpringLayout.WEST, editFinishButton, 0, SpringLayout.WEST, lfCombo_dest);
			add(editFinishButton);
		}
		// 追加モード時
		else {
			// 「以上の内容でページ構成要素を追加する」ボタン
			additionButton.setActionCommand("PageElement追加 - ログインフォーム");
			springLayout.putConstraint(SpringLayout.NORTH, additionButton, 40, SpringLayout.SOUTH, lfCombo_dest);
			springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.WEST, lfCombo_dest);
			add(additionButton);
		}
		/* ここまで */

		MainFrame.getInstance().repaintAndValidate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEventハンドラ
	 * ページエレメントの追加・編集を実行する
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		// 現在、ページ構成要素を編集しているWebページ
		WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();

		if(cmd.equals("PageElement追加 - 結果テーブル表示領域")) {
			// ターゲットのページ

			// PageElementDisplayArea生成 -> ターゲットのページへ追加
			PageElementDisplayArea rtd = new PageElementDisplayArea(editingPage);
			editingPage.addPageElement(rtd);
		}
		else if(cmd.equals("PageElement編集 - 結果テーブル表示領域")) {
			// 編集対象のPageElementDisplayArea取得
			PageElementDisplayArea editingRtd = (PageElementDisplayArea)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
		}
		else if(cmd.equals("PageElement追加 - Createフォーム")) {
			// テーブル取得
			String tableName = (String)cfCombo.getSelectedItem();

			// DIALOG : テーブルが選択されていない
			if(tableName.equals(japanese?"---選択して下さい---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "テーブルを選択して下さい。");
				return;
			}

			SuperTable table = TableManager.getInstance().getTableByName(tableName);

			// 複数レコード作成可能か否かを設定
			boolean mult = false;
			/* いずれこっちに戻す
			boolean mult = multipleCb.isSelected();
			*/
			
			// 遷移先ページの設定
			String destPageName = (String)cfDestCombo.getSelectedItem();

			// DIALOG : 遷移先が選択されていない
			if(destPageName.equals(japanese?"---選択して下さい---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "遷移先を選択して下さい。");
				return;
			}
			
			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);
			
			// PageElementCreateFormインスタンス生成 -> ターゲットのページへ追加
			PageElementCreateForm cf = new PageElementCreateForm(editingPage, table, mult, destPage);
			editingPage.addPageElement(cf);
		}
		else if(cmd.equals("PageElement編集 - Createフォーム")) {
			// 編集対象のPageElementCreateForm取得
			PageElementCreateForm editingCf = (PageElementCreateForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			// PageElementCreateFormの対象のテーブルを、新しく選択されたものへ変更
			String tableName = (String)cfCombo.getSelectedItem();

			// DIALOG : テーブルが選択されていない
			if(tableName.equals(japanese?"---選択して下さい---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "テーブルを選択して下さい。");
				return;
			}
			
			SuperTable table = TableManager.getInstance().getTableByName(tableName);
			editingCf.table = table;

			
			// 複数レコード作成可能か否かを、新しく指定された通りに変更
			editingCf.multiple = false;
			/* いずれこっちに戻す
			editingCf.multiple = multipleCb.isSelected();
			*/

			
			/*
			 * 遷移先ページの変更
			 */

			String destPageName = (String)cfDestCombo.getSelectedItem();

			// DIALOG : 遷移先が選択されていない
			if(destPageName.equals(japanese?"---選択して下さい---":"------Select------")) {
				JOptionPane.showMessageDialog(this, "遷移先を選択して下さい。");
				return;
			}

			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);
			editingCf.destPage = destPage;
		}
		else if(cmd.equals("PageElement追加 - ハイパーリンク")) {
			String destPageName = (String)hlCombo_dest.getSelectedItem();

			// DIALOG : 遷移先が未選択
			if(destPageName.equals("---選択して下さい---") || destPageName.equals("------Select------")) {
				JOptionPane.showMessageDialog(this, "遷移先を選択して下さい。");
				return;
			}
			
			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);

			
			
			String hlText = hlTextField_name.getText();

			PageElement_HyperLink hl = new PageElement_HyperLink(editingPage, destPage, hlText);
			editingPage.addPageElement(hl);
		}
		else if(cmd.equals("PageElement編集 - ハイパーリンク")) {
			PageElement_HyperLink editingHl = (PageElement_HyperLink)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			String destPageName = (String)hlCombo_dest.getSelectedItem();

			// DIALOG : 遷移先が未選択
			if(destPageName.equals("---選択して下さい---") || destPageName.equals("------Select------")) {
				JOptionPane.showMessageDialog(this, "遷移先を選択して下さい。");
				return;
			}

			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);
			editingHl.setDestPage(destPage);
			
			String hlText = hlTextField_name.getText();
			editingHl.setText(hlText);
		}
		else if(cmd.equals("PageElement追加 - テキスト")) {
			// テキストをPageElementとして追加
			String text = textAreaForPeText.getText();
			PageElementText peText = new PageElementText(editingPage, text);
			editingPage.addPageElement(peText);
		}
		else if(cmd.equals("PageElement編集 - テキスト")) {
			PageElementText editingText = (PageElementText)Panel_PeEdit_Left.getInstance().getFocusedPageElement();

			// テキストエリアの文字列を取得し、新しい文字列としてテキストページエレメントにセット
			String text = textAreaForPeText.getText();
			editingText.containingText = text;
		}
		else if(cmd.equals("PageElement追加 - ログインフォーム")) {
			// アカウントテーブル取得
			String tableName = (String)lfCombo.getSelectedItem();
			AccountTable table = (AccountTable)(TableManager.getInstance().getTableByName(tableName));

			// 遷移先Webページ（WebPageインスタンス）を取得
			String destPageName = (String)lfCombo_dest.getSelectedItem();
			WebPage destPage = WebPageManager.getInstance().getPageByName(destPageName);

			// PageElementLoginForm生成 -> Webページへ追加
			PageElementLoginForm loginForm = new PageElementLoginForm(editingPage, table, destPage);
			editingPage.addPageElement(loginForm);
		}
		else if(cmd.equals("PageElement編集 - ログインフォーム")) {
			// 編集対象のPageElementLoginForm取得
			PageElementLoginForm editingLoginForm = (PageElementLoginForm)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			// PageElementLoginFormのアカウントテーブルを選択中のものへ変更
			String tableName = (String)lfCombo.getSelectedItem();
			AccountTable table = (AccountTable)(TableManager.getInstance().getTableByName(tableName));
			editingLoginForm.accountTable = table;

			// 遷移先Webページを選択中のものへ変更
			String destPageName = (String)lfCombo_dest.getSelectedItem();
			WebPage newDestPage = WebPageManager.getInstance().getPageByName(destPageName);
			editingLoginForm.destWebPage = newDestPage;
		}

		// 全コンポーネント除去  -> 左パネルのテキストペイン再配置
		removeAll();
		Panel_PeEdit_Left.getInstance().locateTextPanes();

		// フレーム再描画
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
	 * ◆テスト用
	 */
	public void setHyperLinkDestPage(int jComboBoxIndex) {
		hlCombo_dest.setSelectedIndex(jComboBoxIndex);
	}

	/*
	 * NOTICE :
	 * ◆テスト用
	 */
	public void inputHyperLinkText(String text) {
		hlTextField_name.setText(text);
	}
	
	/*
	 * NOTICE :
	 * ◆テスト用
	 */
	public void clickAdditionButton() {
		additionButton.doClick();
	}
	
	
	public static void updateInstance(Panel_PeEdit_Right newObject) {
		Panel_PeEdit_Right.obj = newObject;
	}

}
