package view.peEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.io.Serializable;

import javax.swing.border.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import mainFrame.MainFrame;

import pageElement.*;
import property.GeneratorProperty;

import debug.Debug;

/*
 * NOTICE : Singleton
 */
@SuppressWarnings("serial")
public class Panel_PeEdit_Left extends JPanel implements MouseListener,Serializable {
	private WebPage page;
	private ArrayList<JTextPane> peTextPanes = new ArrayList<JTextPane>();

	// 現在フォーカスされているJTextPaneを保持する
	private JTextPane focusedTextPane = null;

	public SerializableSpringLayout springLayout;
	
	// パネルのサイズ
	public static final int panelWidth = 300;
	public static final int panelHeight = 1200;

	// JTextPane の縦幅
	private static final int textPaneHeight = 100;

	/*
	 * インスタンス参照変数は最後！
	 */
	private static Panel_PeEdit_Left obj = new Panel_PeEdit_Left();


	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private Panel_PeEdit_Left() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		setBackground(Color.WHITE);
		addMouseListener(this);

		setAutoscrolls(true);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * Page Element 編集画面に遷移した瞬間に行う処理
	 */
	public void initOnShift(WebPage page) {
		setPage(page);
		locateTextPanes();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 各JTextPane（Page Element を表す）の配置を行う
	 */
	public void locateTextPanes() {
		boolean japanese = GeneratorProperty.japanese();
		
		// 全コンポーネント除去
		removeAll();


		// 左余白
		int PADD_LEFT = 20;
		
		
		// テキストエリア（ページ名）
		JTextArea textArea_pageName = new JTextArea(japanese?this.page.pageFileName+"のページ構成要素を編集中":"Editing page factors of \""+this.page.pageFileName+"\"", 1, 20);
		Slpc.put(springLayout, "N", textArea_pageName, "N", this, 10);
		Slpc.put(springLayout, "W", textArea_pageName, "W", this, PADD_LEFT);
		add(textArea_pageName);

		
		for(int i=0; i<page.getPageElementNum(); i++) {
			// JTextPaneオブジェクトが足りなければ追加
			if(peTextPanes.size() <= i) {
				peTextPanes.add(new JTextPane());
				JTextPane jtp = peTextPanes.get(i);
			    jtp.setEditable(false);
			    jtp.addMouseListener(this);
			}

			JTextPane textPane = peTextPanes.get(i);
			textPane.setBackground(Color.WHITE);
			PageElement pe = page.getPageElement(i);

			// EtchedBorderインスタンス
			EtchedBorder eBorder = null;

			// PageElementの種類を示すボーダータイトル
			String borderTitle = null;
			
			// テーブル表示領域の場合
			if(pe instanceof PageElementTableDisplay) {
				borderTitle = japanese ? "テーブル表示" : "Table Display";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.ORANGE, Color.ORANGE);
				
				PageElementTableDisplay td = (PageElementTableDisplay)pe;
				String text = japanese ?
								"テーブル:"+ td.getTable().getTableName() :
								"Table:" + td.getTable().getTableName();
				textPane.setText(text);
			}
			// 結果テーブル表示領域の場合
			else if(pe instanceof PageElementDisplayArea) {
				borderTitle = japanese ? "サービス出力テーブル表示" : "Service Output Table Display";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.GREEN, Color.GREEN);
				textPane.setText("");
			}
			// Createフォームの場合
			else if(pe instanceof PageElementCreateForm) {
				borderTitle = japanese ? "レコード作成フォーム" : "Record Creation Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.PINK, Color.PINK);

				PageElementCreateForm cf = (PageElementCreateForm)pe;
				String tableName = cf.table.getTableName();
				String text = japanese ? 
								"テーブル：" + tableName + "\n" + "遷移先：" + cf.destPage.pageFileName :
								"Table:" + tableName + "\n" + "Destination:" + cf.destPage.pageFileName;
				textPane.setText(text);
			}
			// Deleteフォームの場合
			else if(pe instanceof PageElementDeleteForm) {
				borderTitle = japanese ? "レコード削除フォーム" : "Record Delete Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.PINK, Color.PINK);

				PageElementDeleteForm cf = (PageElementDeleteForm)pe;
				String tableName = cf.table.getTableName();
				String text = japanese ? 
						"テーブル：" + tableName + "\n" + "遷移先：" + cf.destPage.pageFileName :
						"Table:" + tableName + "\n" + "Destination:" + cf.destPage.pageFileName;
				textPane.setText(text);
			}
			// Updateフォームの場合
			else if(pe instanceof PageElementUpdateForm) {
				borderTitle = japanese ? "更新フォーム" : "Update Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.PINK, Color.PINK);

				PageElementUpdateForm updateForm = (PageElementUpdateForm)pe;
				String tableName = updateForm.getTable().getTableName();
				String text = japanese ? 
						"テーブル：" + tableName + "\n" + "遷移先：" + updateForm.destPage.pageFileName :
						"Table:" + tableName + "\n" + "Destination:" + updateForm.destPage.pageFileName;
				textPane.setText(text);
			}
			// SAIFの場合
			else if(pe instanceof PageElementSaif) {
				borderTitle = japanese ? "サービス引数入力フォーム" : "Service Argument Input Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.YELLOW, Color.YELLOW);

				PageElementSaif saif = (PageElementSaif)pe;
				String text = japanese ?
								"パラメータ名： " + saif.getSaifName() + "\n" + "データタイプ： " + saif.getSaifKind() :
								"Parameter Name: " + saif.getSaifName() + "\n" + "Data Type: " + saif.getSaifKind();
				textPane.setText(text);
			}
			// ハイパーリンクの場合
			else if(pe instanceof PageElement_HyperLink) {
				borderTitle = japanese ? "ハイパーリンク" : "Hyperlink";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.BLUE, Color.BLUE);

				PageElement_HyperLink hl = (PageElement_HyperLink)pe;
				String text = japanese ?
								"遷移先：" + hl.getDestPage().pageFileName + "\n" + "テキスト：\"" + hl.getText() + "\"" :
								"Destination:" + hl.getDestPage().pageFileName + "\n" + "Text:\"" + hl.getText() + "\"";
				textPane.setText(text);
			}
			// テキストの場合
			else if(pe instanceof PageElementText) {
				borderTitle = japanese ? "テキスト" : "Text";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.CYAN, Color.CYAN);

				PageElementText textPe = (PageElementText)pe;
				textPane.setText(textPe.containingText);
			}
			// ログインフォームの場合
			if(pe instanceof PageElementLoginForm) {
				borderTitle = japanese ? "ログインフォーム" : "Login Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.MAGENTA, Color.MAGENTA);
				
				PageElementLoginForm loginForm = (PageElementLoginForm)pe;
				String atName = loginForm.accountTable.getTableName();

				String destWebPageFileName = loginForm.destWebPage.pageFileName;

				String text = japanese ?
								"アカウントテーブル：\n" + atName + "\n" + "遷移先：\n" + destWebPageFileName :
								"Account Table:\n" + atName + "\n" + "Destination:" + destWebPageFileName;
				textPane.setText(text);
			}

			// フォーカスされている→赤枠に変更、テキストペインの背景色変更
			if(textPane==focusedTextPane) {
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.RED, Color.RED);
				textPane.setBackground(Color.LIGHT_GRAY);
			}

			// ボーダーセット
			TitledBorder tBorder = new TitledBorder(eBorder);
			tBorder.setTitle(borderTitle);
			tBorder.setTitleJustification(TitledBorder.LEFT);
			tBorder.setTitlePosition(TitledBorder.TOP);
			tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
			textPane.setBorder(tBorder);
			
			int WIDTH = Panel_PeEdit_Left.panelWidth-(PADD_LEFT*2);
			textPane.setPreferredSize(new Dimension(WIDTH, Panel_PeEdit_Left.textPaneHeight));
			Slpc.put(springLayout, "N", textPane, "S", textArea_pageName, 20+(i*(Panel_PeEdit_Left.textPaneHeight+20)));
			Slpc.put(springLayout, "W", textPane, "W", this, PADD_LEFT);
			add(textPane);
		}

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}
	
	
	public WebPage getEditingPage() {
		return page;
	}
	
	public void setPage(WebPage page) {
		this.page = page;
	}

	public void mousePressed(MouseEvent e) {
	}
	public void mouseClicked(MouseEvent e) {
		// テキストペインの外側部分（パネル）でのクリック -> フォーカスを外して終了
		if(!(e.getSource() instanceof JTextPane)) {
			focusedTextPane = null;
			PanelPeEditBottom.getInstance().informPageElementUnfocus();
			locateTextPanes();
			return;
		}

		// テキストペインの内側部分でのクリック -> フォーカスを合わせる
		JTextPane jtp = (JTextPane)e.getSource();
		focusedTextPane = jtp;
		PanelPeEditBottom.getInstance().informPageElementFocus();

		// ◆暫定　何かにpeを使うと思う
		PageElement pe = getCorrespondantPageElement(jtp);

		// テキストペイン再配置
		locateTextPanes();
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	public PageElement getCorrespondantPageElement(JTextPane jtp) {
		for(int i=0; i<peTextPanes.size(); i++) {
			JTextPane peTextPane = peTextPanes.get(i);
			if(peTextPane==jtp) {
				PageElement requiredPageElement = page.getPageElement(i);
				return requiredPageElement;
			}
		}
		
		Debug.error("Panel_PeEdit_Left getCorrespondantPageElement() : PageElementインスタンスが見つかりません");
		return null;
	}

	public JTextPane getFocusedTextPane() {
		return focusedTextPane;
	}
	
	public PageElement getFocusedPageElement() {
		JTextPane focusedTp = getFocusedTextPane();
		if(focusedTp==null) {
			return null;
		}
		return getCorrespondantPageElement(focusedTp);
	}
	
	public static Panel_PeEdit_Left getInstance() {
		return Panel_PeEdit_Left.obj;
	}
	
	public static void updateInstance(Panel_PeEdit_Left newObject) {
		Panel_PeEdit_Left.obj = newObject;
	}

}
