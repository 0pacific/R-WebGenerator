package view.peEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import debug.Debug;

import mainFrame.MainFrame;

import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementDeleteForm;
import pageElement.PageElementLoginForm;
import pageElement.PageElementSaif;
import pageElement.PageElementText;
import pageElement.PageElementUpdateForm;
import pageElement.PageElement_HyperLink;
import pageElement.PageElementDisplayArea;
import pageElement.PageElementTableDisplay;
import property.GeneratorProperty;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.Serializable;


/*
 * NOTICE : Singleton
 */
public class PanelPeEditBottom extends JPanel implements ActionListener,Serializable {
	public JButton additionButton;
	public JComboBox peTypeCombo;
	public JButton editButton;
	public JButton deleteButton;

	// 他画面への遷移ボタン
	public JButton transButton_transEdit;
	
	// パネルのサイズ
	public static final int panelWidth = MainFrame.frameWidth;
	public static final int panelHeight = 150;

	public SerializableSpringLayout springLayout;
	
	// シリアライズボタン
	ButtonSaveWork btnSaveWork;
	// デシリアライズボタン
	ButtonLoadWork btnLoadWork;

	// ■この配列に要素を付け足すときは、必ず最後尾に付け足すように！
	// ■途中に付け足したせいでエラーになりデバッグに時間を食った
	public static String[] peTypeList;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static PanelPeEditBottom obj = new PanelPeEditBottom();

	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private PanelPeEditBottom() {
		if(GeneratorProperty.japanese()) {
			PanelPeEditBottom.peTypeList = new String[]{"テキスト", "ハイパーリンク", "テーブル表示", "レコード作成フォーム", "更新フォーム", "レコード削除フォーム", "ログインフォーム", "サービス出力テーブル表示", "サービス引数入力フォーム"};
		}
		else {
			PanelPeEditBottom.peTypeList = new String[]{"Text", "Hyperlink", "Table Display", "Record Creation Form", "Record Deletion Form", "Update Form", "Login Form", "Service Output Table Display", "Service Argument Input Form"};
		}
		
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Color.DARK_GRAY);
	}
	
	
	
	public void locateComps() {
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		// コンボボックス : 追加するPage Elementのタイプを選択
		peTypeCombo = new JComboBox(PanelPeEditBottom.peTypeList);
		peTypeCombo.addActionListener(this);
		peTypeCombo.setActionCommand("Page Elementタイプ選択");
		Slpc.put(springLayout, "N", peTypeCombo, "N", this, 20);
		Slpc.put(springLayout, "W", peTypeCombo, "W", this, 20);
		add(peTypeCombo);

		
		
		// Page Element 追加ボタン
		additionButton = japanese ? new JButton("をページ構成要素として追加") : new JButton("Add Page Factor");
		additionButton.addActionListener(this);
		additionButton.setActionCommand("Page Element 追加");
		Slpc.put(springLayout, "N", additionButton, "N", peTypeCombo, 0);
		Slpc.put(springLayout, "W", additionButton, "E", peTypeCombo, 10);
		add(additionButton);

		
		
		// Page Element 編集ボタン
		editButton = japanese ? new JButton("選択中のページ構成要素を編集") : new JButton("Edit Page Factor");
		editButton.addActionListener(this);
		editButton.setActionCommand("Page Element 編集");
		editButton.setEnabled(false);
		Slpc.put(springLayout, "N", editButton, "S", peTypeCombo, 10);
		Slpc.put(springLayout, "W", editButton, "W", peTypeCombo, 0);
		add(editButton);

		// Page Element 削除ボタン
		deleteButton = japanese ? new JButton("選択中のページ構成要素を削除") : new JButton("Delete Page Factor");
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("Page Element 削除");
		deleteButton.setEnabled(false);
		Slpc.put(springLayout, "N", deleteButton, "N", editButton, 0);
		Slpc.put(springLayout, "W", deleteButton, "E", editButton, 10);
		add(deleteButton);
		
		// 遷移編集画面への遷移ボタン
		transButton_transEdit = japanese ? new JButton("Webページ・遷移権限定義画面へ") : new JButton("Go To Web Page Definition");
		transButton_transEdit.addActionListener(this);
		transButton_transEdit.setActionCommand("遷移 - ページ遷移定義画面");
		Slpc.put(springLayout, "N", transButton_transEdit, "N", this, 20);
		Slpc.put(springLayout, "E", transButton_transEdit, "E", this, -20);
		add(transButton_transEdit);

		// デシリアライズボタン
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", transButton_transEdit, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		// シリアライズボタン
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", btnLoadWork, 0);
		Slpc.put(springLayout, "E", btnSaveWork, "W", btnLoadWork, -20);
		add(btnSaveWork);

		MainFrame.getInstance().repaintAndValidate();
	}


	public void informPageElementFocus() {
		editButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}
	
	public void informPageElementUnfocus() {
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		// Page Element 追加
		if(cmd.equals("Page Element 追加")) {
			String peType = (String)peTypeCombo.getSelectedItem();

			// テーブル表示領域
			if(peType.equals("テーブル表示") || peType.equals("Table Display")) {
				PanelPeEditRightSupporterTableDisplay.getInstance().locateForTableDisplay(null);
			}
			// 結果テーブル表示
			else if(peType.equals("サービス出力テーブル表示") || peType.equals("Service Output Table Display")) {
				Debug.out("サービス出力テーブル表示の追加が指示されました");
				Panel_PeEdit_Right.getInstance().locateForResultTableDisplay(false);
			}
			// ハイパーリンク
			else if(peType.equals("ハイパーリンク") || peType.equals("Hyperlink")) {
				Panel_PeEdit_Right.getInstance().locateForHyperLink(false);
			}
			// Createフォーム
			else if(peType.equals("レコード作成フォーム") || peType.equals("Record Creation Form")) {
				Panel_PeEdit_Right.getInstance().locateForCreateForm(false);
			}
			// Deleteフォーム
			else if(peType.equals("レコード削除フォーム") || peType.equals("Record Deletion Form")) {
				PanelPeEditRightSupporterDeleteForm.getInstance().locateForDeleteForm(null);
			}
			// Updateフォーム
			else if(peType.equals("更新フォーム") || peType.equals("Update Form")) {
				PanelPeEditRightSupporterUpdateForm.getInstance().locateForUpdateForm(null);
			}
			// テキスト
			else if(peType.equals("テキスト") || peType.equals("Text")) {
				Panel_PeEdit_Right.getInstance().locateForText(false);
			}
			// ログインフォーム
			else if(peType.equals("ログインフォーム") || peType.equals("Login Form")) {
				Panel_PeEdit_Right.getInstance().locateForLoginForm(false);
			}
			// サービス引数入力フォーム
			else if(peType.equals("サービス引数入力フォーム") || peType.equals("Service Argument Input Form")) {
				PanelPeEditRightSupporterSaif.getInstance().locateForSaif(null);
			}
		}
		// Page Element 編集
		else if(cmd.equals("Page Element 編集")) {
			PageElement targetPe = Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			if(targetPe instanceof PageElementTableDisplay) {
				PanelPeEditRightSupporterTableDisplay.getInstance().locateForTableDisplay((PageElementTableDisplay)targetPe);
			}
			else if(targetPe instanceof PageElement_HyperLink) {
				Panel_PeEdit_Right.getInstance().locateForHyperLink(true);
			}
			else if(targetPe instanceof PageElementCreateForm) {
				Panel_PeEdit_Right.getInstance().locateForCreateForm(true);
			}
			else if(targetPe instanceof PageElementDeleteForm) {
				PanelPeEditRightSupporterDeleteForm.getInstance().locateForDeleteForm((PageElementDeleteForm)targetPe);
			}
			else if(targetPe instanceof PageElementUpdateForm) {
				PanelPeEditRightSupporterUpdateForm.getInstance().locateForUpdateForm((PageElementUpdateForm)targetPe);
			}
			else if(targetPe instanceof PageElementText) {
				Panel_PeEdit_Right.getInstance().locateForText(true);
			}
			else if(targetPe instanceof PageElementLoginForm) {
				Panel_PeEdit_Right.getInstance().locateForLoginForm(true);
			}
			else if(targetPe instanceof PageElementDisplayArea) {
				Panel_PeEdit_Right.getInstance().locateForResultTableDisplay(true);
			}
			else if(targetPe instanceof PageElementSaif) {
				PanelPeEditRightSupporterSaif.getInstance().locateForSaif((PageElementSaif)targetPe);
			}
		}
		else if(cmd.equals("Page Element 削除")) {
			String msg1 = GeneratorProperty.japanese() ? "削除してもよろしいですか？関連する遷移やその遷移プロセスなども削除されます。" : "Delete the page factor?";
			int confirm = JOptionPane.showConfirmDialog(this, msg1, "ページ構成要素の削除", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}
			
			WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();
			PageElement focusedPe = Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			editingPage.removePageElement(focusedPe);

			Panel_PeEdit_Left.getInstance().locateTextPanes();
			Panel_PeEdit_Right.getInstance().removeAll();
		}
		// ページ遷移定義画面への遷移
		else if(cmd.equals("遷移 - ページ遷移定義画面")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	public static PanelPeEditBottom getInstance() {
		return PanelPeEditBottom.obj;
	}





	/*
	 * NOTICE :
	 * ◆テスト用
	 */
	public void setPeType(int jComboBoxIndex) {
		peTypeCombo.setSelectedIndex(jComboBoxIndex);
	}

	/*
	 * NOTICE :
	 * ◆テスト用
	 */
	public void clickAdditionButton() {
		additionButton.doClick();
	}

	/*
	 * NOTICE :
	 * ◆テスト用
	 */
	public void clickTransEditShiftButton() {
		transButton_transEdit.doClick();
	}
	
	
	public static void updateInstance(PanelPeEditBottom newObject) {
		PanelPeEditBottom.obj = newObject;
	}

}
