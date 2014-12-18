package view.webPageDefinition;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.Transition;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.transAuthEdit.FrameTransAuthEdit;
import view.webPageDefinition.transAuthEdit.PanelTransAuthEdit;
import webPage.WebPage;
import webPage.WebPageManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.FlowLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import property.GeneratorProperty;

import mainFrame.MainFrame;
import utility.*;

import debug.Debug;





/*
 * Singleton
 */
public class PanelWebPageDefBottom extends JPanel implements ActionListener {
	// パネルサイズ
	public static final int panelWidth = MainFrame.frameWidth;
	public static final int panelHeight = MainFrame.frameHeight - PanelWebPageDefAbove.panelHeight;

	// 背景色
	public static Color backGroundColor = Color.DARK_GRAY;

	// ページ追加用コンポーネント
	private JButton pageAddButton;
	
	public JButton btnLogoutDestPage;

	public JButton btnDeletePage;
	
	// PageElement編集画面への遷移ボタン
	private JButton peEditButton;

	// TransitionProcess編集画面への遷移ボタン
	private JButton tpEditButton;

	public JButton btnEditTransAuth;
	
	// 他画面への遷移ボタン
	private JButton transitionButton_roleEdit;
	private JButton transitionButton_tableList;
	public JButton transitionButton_generate;
	public JButton btnEditTableAuth;

	// シリアライズボタン
	ButtonSaveWork btnSaveWork;
	// デシリアライズボタン
	ButtonLoadWork btnLoadWork;
	
	public SerializableSpringLayout springLayout;
	
	/*
	 * インスタンスは最後！
	 */
	private static PanelWebPageDefBottom obj = new PanelWebPageDefBottom();
	
	
	
	private PanelWebPageDefBottom() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		locateCompsWebPageDefBottom();
	}

	
	
	
	
	public void locateCompsWebPageDefBottom() {
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		int PADD_LEFT = 20;
		
		// ページ追加のボタン
		pageAddButton = japanese ? new JButton("Webページを作成") : new JButton("Add Web Page");
		Slpc.put(springLayout, "N", pageAddButton, "N", this, 20);
		Slpc.put(springLayout, "W", pageAddButton, "W", this, PADD_LEFT);
		add(pageAddButton);	
		pageAddButton.addActionListener(this);
		pageAddButton.setActionCommand("ページ追加ボタンクリック");

		// ページ削除ボタン
		btnDeletePage = japanese ? new JButton("選択中のWebページを削除") : new JButton("Delete Web Page");
		Slpc.put(springLayout, "N", btnDeletePage, "N", pageAddButton, 0);
		Slpc.put(springLayout, "W", btnDeletePage, "E", pageAddButton, 20);
		add(btnDeletePage);	
		btnDeletePage.addActionListener(this);
		btnDeletePage.setActionCommand("ページ削除");
		
		
		// ログアウト遷移先ページ定義ボタン
		btnLogoutDestPage = japanese ? new JButton("ログアウト時の遷移先とする") : new JButton("Set as Logout Destination");
		Slpc.put(springLayout, "N", btnLogoutDestPage, "N", btnDeletePage, 0);
		Slpc.put(springLayout, "W", btnLogoutDestPage, "E", btnDeletePage, 20);
		add(btnLogoutDestPage);	
		btnLogoutDestPage.addActionListener(this);
		btnLogoutDestPage.setActionCommand("ログアウト遷移先定義");
		
		// Page Element 編集ボタン
		peEditButton = japanese ? new JButton("ページ構成要素の編集") : new JButton("Edit Page Factors");
		Slpc.put(springLayout, "N", peEditButton, "S", pageAddButton, 20);
		Slpc.put(springLayout, "W", peEditButton, "W", this, PADD_LEFT);
		add(peEditButton);
		peEditButton.addActionListener(this);
		peEditButton.setActionCommand("遷移：PageElement編集画面");

		// TransitionProcess編集画面への遷移ボタン
		tpEditButton = japanese ? new JButton("遷移プロセスの編集") : new JButton("Edit Transition Process");
		Slpc.put(springLayout, "N", tpEditButton, "N", peEditButton, 0);
		Slpc.put(springLayout, "W", tpEditButton, "E", peEditButton, 40);
		add(tpEditButton);
		tpEditButton.addActionListener(this);
		tpEditButton.setActionCommand("遷移：TransitionProcess編集画面");

		// 遷移権限編集ボタン
		btnEditTransAuth = japanese ? new JButton("遷移権限の編集") : new JButton("Edit Transition Authority");
		Slpc.put(springLayout, "N", btnEditTransAuth, "N", tpEditButton, 0);
		Slpc.put(springLayout, "W", btnEditTransAuth, "E", tpEditButton, 20);
		add(btnEditTransAuth);
		btnEditTransAuth.addActionListener(this);
		btnEditTransAuth.setActionCommand("遷移：遷移権限編集画面");
		
		// テーブルリスト画面への遷移ボタン
		transitionButton_tableList = japanese ? new JButton("テーブル定義画面へ") : new JButton("Go To Table Definition");
		Slpc.put(springLayout, "N", transitionButton_tableList, "S", peEditButton, 20);
		Slpc.put(springLayout, "W", transitionButton_tableList, "W", this, PADD_LEFT);
		add(transitionButton_tableList);
		transitionButton_tableList.addActionListener(this);
		transitionButton_tableList.setActionCommand("遷移：テーブル編集画面");

		// ロール編集画面への遷移ボタン
		transitionButton_roleEdit = japanese ? new JButton("ロール編集画面へ") : new JButton("Go To Role Definition");
		Slpc.put(springLayout, "N", transitionButton_roleEdit, "N", transitionButton_tableList, 0);
		Slpc.put(springLayout, "W", transitionButton_roleEdit, "E", transitionButton_tableList, 20);
		add(transitionButton_roleEdit);
		transitionButton_roleEdit.addActionListener(this);
		transitionButton_roleEdit.setActionCommand("遷移：ロール編集画面");

		// テーブル権限編集ボタン
		btnEditTableAuth = japanese ? new JButton("操作権限編集画面へ") : new JButton("Go To Accessibility Definition");
		Slpc.put(springLayout, "N", btnEditTableAuth, "N", transitionButton_roleEdit, 0);
		Slpc.put(springLayout, "W", btnEditTableAuth, "E", transitionButton_roleEdit, 20);
		add(btnEditTableAuth);
		btnEditTableAuth.addActionListener(this);
		btnEditTableAuth.setActionCommand("遷移：操作権限編集画面");
		
		// 生成画面への遷移ボタン
		transitionButton_generate = japanese ? new JButton("生成画面へ") : new JButton("Go To Generation");
		Slpc.put(springLayout, "N", transitionButton_generate, "N", btnEditTableAuth, 0);
		Slpc.put(springLayout, "W", transitionButton_generate, "E", btnEditTableAuth, 20);
		add(transitionButton_generate);
		transitionButton_generate.addActionListener(this);
		transitionButton_generate.setActionCommand("遷移：生成画面");

		// シリアライズボタン
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", this, 20);
		Slpc.put(springLayout, "E", btnSaveWork, "E", this, -20);
		add(btnSaveWork);

		// デシリアライズボタン
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", btnSaveWork, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		if(PanelWebPageDefAbove.getInstance().webPagePanelFocused()) {
			informPageFocus();
		}
		else {
			informPageUnfocus();
		}

		if(PanelWebPageDefAbove.getInstance().transtionIsFocused()) {
			informTransitionFocus();
		}
		else {
			informTransitionUnfocus();
		}
	}
	
	
	
	
	public void paintComponent(Graphics g) {
		// 背景塗り潰し
		g.setColor(PanelWebPageDefBottom.backGroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * 各ボタンクリック時のアクション
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		if(cmd.equals("ページ追加ボタンクリック")) {
			while(true) {
				String msg1 = japanese ? "Webページ名を半角英数字で入力\n\"index\"と入力すれば\"index.php\"というWebページになります" : "Enter Web page's name.\nIf you enter \"index\", Web page \"index.php\" will be created.";
				String newPageName = JOptionPane.showInputDialog(this, msg1);
				if(newPageName==null) {	// 入力取り消し
					return;
				}
				else if(newPageName.equals("")) {
					String msg2 = japanese ? "正しく入力して下さい" : "Web page's name can't be blank.";
					JOptionPane.showMessageDialog(this, msg2);
				}
				else if(WebPageManager.getInstance().getPageByName(newPageName+".php") instanceof WebPage) {
					String msg3 = japanese ? newPageName+".phpは既に存在します。違う名前にして下さい。" : newPageName+".php already exists.";
					JOptionPane.showMessageDialog(this, msg3);
				}
				else {
					WebPageManager.getInstance().addNewPage(newPageName + ".php");
					Debug.out("WebPageManagerインスタンスにページ追加 : " + newPageName + ".php");
					break;
				}
			}

			// ページが追加された

			WebPageManager.getInstance().debug();
		}
		else if(cmd.equals("ページ削除")) {
			WebPage targetPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
			if(targetPage==null) {
				Debug.error("ページ削除ボタンが押されましたが、ページがフォーカスされていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			String webPageName = targetPage.pageFileName;
			String msg4 = japanese ? "Webページ\"" + webPageName + "\"を削除しますか？\nページ構成要素や遷移、遷移プロセスも全て削除されます。" : "Delete "+webPageName+"?";
			int confirm = JOptionPane.showConfirmDialog(this, msg4, "Webページの削除", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}

			WebPageManager.getInstance().removeWebPage(targetPage);
			PanelWebPageDefAbove.getInstance().focusedWebPagePanel = null;
		}
		
		else if(cmd.equals("ログアウト遷移先定義")) {
			WebPage focusedPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
			if(focusedPage==null) {
				Debug.error("上部パネルでフォーカスされているページがあるはずなのですが、nullのようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			String msg5 = japanese ? "ログアウト時には、"+focusedPage.pageFileName+"へ戻るようにします。よろしいですか？" : "Users will go to "+focusedPage.pageFileName+" when they logout. OK?";
			int confirm = JOptionPane.showConfirmDialog(this, msg5, japanese?"ログアウト時遷移先の定義":"Logout Destination Page", JOptionPane.OK_CANCEL_OPTION);
			
			if(confirm==JOptionPane.OK_OPTION) {
				WebPageManager.getInstance().setLogoutDestPage(focusedPage);
			}
			// キャンセル
			else if(confirm==JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		
		// 「生成画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：生成画面")) {
			MainFrame.getInstance().shiftToGenerate();
		}

		// 「ロール編集画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：ロール編集画面")) {
			MainFrame.getInstance().shiftToRoleEdit();
		}
		// 「操作権限編集画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：操作権限編集画面")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		// 「テーブル編集画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：テーブル編集画面")) {
			MainFrame.getInstance().shiftToTableList();
		}

		// 「ページ構成要素編集画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：PageElement編集画面")) {
			WebPage targetPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
			if(targetPage==null) {
				Debug.error("Panel_Transition_Bottom actionPerformed() : ページ構成要素を編集しようとしたPageオブジェクトがnullです");
				return;
			}
			MainFrame.getInstance().shiftToPeEdit(targetPage);
		}
		
		// 「遷移プロセス編集画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：TransitionProcess編集画面")) {
			Transition targetTrans = PanelWebPageDefAbove.getInstance().getFocusedTransition();
			if(targetTrans==null) {
				Debug.error("遷移プロセスを編集しようとしたTransitionインスタンスがnullです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			MainFrame.getInstance().shiftToTpEdit(targetTrans);
		}
		else if(cmd.equals("遷移：遷移権限編集画面")) {
			Transition targetTrans = PanelWebPageDefAbove.getInstance().getFocusedTransition();
			if(targetTrans==null) {
				Debug.error("遷移権限を編集しようとしたTransitionインスタンスがnullです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}

			MainFrame.getInstance().setEnabled(false);
			MainFrame.getInstance().setFocusable(false);
			FrameTransAuthEdit.getInstance().setVisible(true);
			FrameTransAuthEdit.getInstance().setEnabled(true);
			FrameTransAuthEdit.getInstance().setFocusable(true);
			FrameTransAuthEdit.getInstance().requestFocus();

			PanelTransAuthEdit.getInstance().relocateCompsForTransAuthEdit(targetTrans);
			FrameTransAuthEdit.repaintAndValidate();
		}

		// 各ページの再配置
		PanelWebPageDefAbove.getInstance().locateWebPagePanels();

		// 下部パネルのコンポーネント再配置
		locateCompsWebPageDefBottom();
		
		// フレームのリペイント
		MainFrame.repaintAndValidate();
	}

	
	
	
	
	public void informPageFocus() {
		peEditButton.setEnabled(true);
		btnDeletePage.setEnabled(true);
		
		// ログアウト遷移先でないWebページがフォーカスされたなら、ログアウト遷移先定義ボタンを有効にする
		WebPage focusedPage = PanelWebPageDefAbove.getInstance().getFocusedWebPage();
		if(focusedPage!=WebPageManager.getInstance().getLogoutDestPage()) {
			btnLogoutDestPage.setEnabled(true);
		}
		else {	// ログアウト遷移先Webページがフォーカスされた場合
			btnLogoutDestPage.setEnabled(false);
		}
	}

	public void informPageUnfocus() {
		peEditButton.setEnabled(false);
		btnDeletePage.setEnabled(false);
		btnLogoutDestPage.setEnabled(false);
	}
	
	public void informTransitionFocus() {
		tpEditButton.setEnabled(true);
		btnEditTransAuth.setEnabled(true);
	}
	
	public void informTransitionUnfocus() {
		tpEditButton.setEnabled(false);
		btnEditTransAuth.setEnabled(false);
	}
	
	
	public static PanelWebPageDefBottom getInstance() {
		return PanelWebPageDefBottom.obj;
	}




	public static void updateInstance(PanelWebPageDefBottom newObject) {
		PanelWebPageDefBottom.obj = newObject;
	}
}
