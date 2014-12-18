package view.peEdit;

import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import debug.Debug;

import mainFrame.MainFrame;
import pageElement.*;
import property.GeneratorProperty;
import table.SuperTable;
import table.TableManager;
import table.field.Field;
import webPage.WebPage;
import webPage.WebPageManager;
import utility.*;




public class PanelPeEditRightSupporterSaif implements ActionListener,Serializable {
	Panel_PeEdit_Right panelRight;
	SerializableSpringLayout springLayout;

	// 編集中のSaif（編集モードではPageElementSaifインスタンスが、追加モードではnullが格納される）
	public PageElementSaif SaifToEdit;

	// コンポーネント（NullPointerException回避のため、初期化してある）
	JTextPane textPaneSaifName = new JTextPane();
	JTextField textFieldSaifName = new JTextField();
	JTextPane textPaneSaifKind = new JTextPane();
	JComboBox comboBoxSaifKind = new JComboBox();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	
	/*
	 * インスタンス参照変数は最後!
	 */
	private static PanelPeEditRightSupporterSaif obj = new PanelPeEditRightSupporterSaif();
	
	
	
	
	
	private PanelPeEditRightSupporterSaif() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	/*
	 * NOTICE :
	 * 編集の場合は編集するSAIFを渡し、追加の場合はnullを渡す
	 */
	public void locateForSaif(PageElementSaif SaifToEdit) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.SaifToEdit = SaifToEdit;
		
		// 全コンポーネント除去
		panelRight.removeAll();

		int PADD_LEFT = 20;

		
		
		// テキストペイン
		textPaneSaifName = new JTextPane();
		textPaneSaifName.setEditable(false);
		textPaneSaifName.setText(japanese?"1. パラメータ名 :":"1. Parameter Name :");
		Slpc.put(springLayout, "N", textPaneSaifName, "N", panelRight, 20);
		Slpc.put(springLayout, "W", textPaneSaifName, "W", panelRight, PADD_LEFT);
		panelRight.add(textPaneSaifName);
		
		// テキストフィールド
		textFieldSaifName = new JTextField();
		Slpc.put(springLayout, "N", textFieldSaifName, "S", textPaneSaifName, 10);
		Slpc.put(springLayout, "W", textFieldSaifName, "W", panelRight, PADD_LEFT);
		Slpc.put(springLayout, "E", textFieldSaifName, "W", textFieldSaifName, 100);
		panelRight.add(textFieldSaifName);

		

		// テキストペイン
		textPaneSaifKind = new JTextPane();
		textPaneSaifKind.setEditable(false);
		textPaneSaifKind.setText(japanese?"パラメータのデータタイプ":"2. Parameter Data Type :");
		Slpc.put(springLayout, "N", textPaneSaifKind, "S", textFieldSaifName, 20);
		Slpc.put(springLayout, "W", textPaneSaifKind, "W", panelRight, PADD_LEFT);
		panelRight.add(textPaneSaifKind);

		
		// コンボボックス
		String[] safKinds = {
				japanese?"---選択して下さい---":"------Select------",
				Field.getDataTypeExpression(Field.DATATYPE_INT),
				Field.getDataTypeExpression(Field.DATATYPE_VARCHAR),
				Field.getDataTypeExpression(Field.DATATYPE_DATETIME),
				Field.getDataTypeExpression(Field.DATATYPE_DATE),
				Field.getDataTypeExpression(Field.DATATYPE_TIME)
		};
		comboBoxSaifKind = new JComboBox(safKinds);

		
		// 編集モードの場合 -> コンボボックス（テーブル選択）の項目を、編集対象のSaifに現在設定されているものにしておく
		if(SaifToEdit instanceof PageElementSaif) {
			for(int i=0; i<comboBoxSaifKind.getItemCount(); i++) {
				String item = (String)comboBoxSaifKind.getItemAt(i);
				String editingSaifKind = SaifToEdit.getSaifKind();
				if(item.equals(editingSaifKind)) {
					comboBoxSaifKind.setSelectedIndex(i);
					break;
				}
			}
		}
		// 追加モードの場合 -> コンボボックスの項目を、japanese?"---選択して下さい---":"------Select------"にしておく
		else {
			comboBoxSaifKind.setSelectedIndex(0);
		}

		Slpc.put(springLayout, "N", comboBoxSaifKind, "S", textPaneSaifKind, 20);
		Slpc.put(springLayout, "W", comboBoxSaifKind, "W", panelRight, PADD_LEFT);
		panelRight.add(comboBoxSaifKind);
		


		// 編集モード
		if(SaifToEdit instanceof PageElementSaif) {
			// 編集実行ボタン
			buttonEditExecute = new JButton("以上の内容に変更する");
			buttonEditExecute.addActionListener(this);
			buttonEditExecute.setActionCommand("編集実行");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonEditExecute,	50,	SpringLayout.SOUTH,	comboBoxSaifKind);
			springLayout.putConstraint(SpringLayout.WEST,	buttonEditExecute,	20,	SpringLayout.WEST,	panelRight);
			panelRight.add(buttonEditExecute);
		}
		// 追加モード
		else {
			// 追加実行ボタン
			buttonAddExecute = new JButton(japanese?"ページ構成要素を追加":"Add Page Factor");
			buttonAddExecute.addActionListener(this);
			buttonAddExecute.setActionCommand("追加実行");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonAddExecute,	50,	SpringLayout.SOUTH, comboBoxSaifKind);
			springLayout.putConstraint(SpringLayout.WEST,	buttonAddExecute,	20,	SpringLayout.WEST, panelRight);
			panelRight.add(buttonAddExecute);
		}

		
		
		MainFrame.repaintAndValidate();
	}

	
	
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();
		WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();

		
		
		if(cmd.equals("編集実行") || cmd.equals("追加実行")) {
			// SAIFの名前取得
			String saifName = this.textFieldSaifName.getText();
			if(saifName.equals("")) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), japanese?"パラメータ名を入力して下さい。":"Parameter name is blank.");
				return;
			}
			
			
			// SAIFのデータタイプ取得
			String saifKindExp = (String)comboBoxSaifKind.getSelectedItem();
			if(saifKindExp.equals(japanese?"---選択して下さい---":"------Select------")) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), japanese?"データタイプが未選択です":"Data type is not selected.");
				return;
			}
			String saifKind = null;
			if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_INT))) {
				saifKind = PageElementSaif.KIND_INT;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_VARCHAR))) {
				saifKind = PageElementSaif.KIND_VARCHAR;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_DATETIME))) {
				saifKind = PageElementSaif.KIND_DATETIME;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_DATE))) {
				saifKind = PageElementSaif.KIND_DATE;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_TIME))) {
				saifKind = PageElementSaif.KIND_TIME;
			}
			else {
				Debug.informError();
				Debug.error("想定外の値です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			
			
	
			// 追加・編集の実行
			if(cmd.equals("追加実行")) {
				// PageElementSaif生成 -> ページへ追加
				PageElementSaif Saif = new PageElementSaif(editingPage, saifName, saifKind);
				editingPage.addPageElement(Saif);
			}
			else if(cmd.equals("編集実行")) {
				// 編集対象のSaifを、指定通りに変更
				PageElementSaif editingSaif = (PageElementSaif)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingSaif.saifName = saifName;
				editingSaif.saifKind = saifKind;

				// ページエレメント編集に付随する処理を行う
				PageElementObserver.getInstance().informPeEdition(editingSaif);
			}

			// 右パネルの全コンポーネント除去
			panelRight.removeAll();
			// 左パネルのコンポーネント再配置
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// メインフレーム再描画
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterSaif getInstance() {
		return PanelPeEditRightSupporterSaif.obj;
	}
	
	
	public static void updateInstance(PanelPeEditRightSupporterSaif newObject) {
		PanelPeEditRightSupporterSaif.obj = newObject;
	}

}
