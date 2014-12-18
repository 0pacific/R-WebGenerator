package view.webPageDefinition;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import transition.*;
import utility.*;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





/*
 * Singleton
 */
public class PanelWebPageDefAbove extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	private WebPageManager pageManager = WebPageManager.getInstance();

	// パネルサイズ
	public static final int panelWidth = MainFrame.frameWidth;
	public static final int panelHeight = 400;

	public static Color pagePanelDefaultColor = Color.WHITE;
	public static Color pagePanelFocusedColor = Color.LIGHT_GRAY;
	
	// 背景色
	public static Color backGroundColor = Color.WHITE;
	
	// ページパネル上でマウスプレスをした際、その座標（左上頂点からの相対座標）を保存するのに使う
	// マウスリリース時に、この値を使ってページパネルの移動を行う
	private int pressX;
	private int pressY;

	public WebPagePanel focusedWebPagePanel = null;
	private Transition focusedTrans = null;

	public SerializableSpringLayout springLayout;
	
	
	// クリックした矢印が他の矢印とだぶっている際、遷移のリストを表示させるポップアップメニュー
	public JPopupMenu popupTransList;
	
	/*
	 * インスタンスは最後！
	 */
	private static PanelWebPageDefAbove obj = new PanelWebPageDefAbove();
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private PanelWebPageDefAbove() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(PanelWebPageDefAbove.backGroundColor);
		addMouseListener(this);
	}
	
	
	
	
	
	public WebPageManager getPageManager() {
		return pageManager;
	}
	
	
	
	

	/*
	 * PURPOSE :
	 * 各Webページパネルを再配置
	 */
	public void locateWebPagePanels() {
		Debug.debug_call(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// まず全コンポーネント（全Webページパネル）を除去
		removeAll();

		WebPagePanelManager webPagePanelManager = WebPagePanelManager.getInstance();

		int pageNum = pageManager.getPageNum();
		for(int i=0; i<pageNum; i++) {
			WebPage page = pageManager.getPage(i);
			WebPagePanel webPagePanel = webPagePanelManager.getWebPagePanelByPage(page);

			// Webページ定義画面上での座標として現在設定されているX,Y座標を取得
			int posX = webPagePanel.getPosX();
			int posY = webPagePanel.getPosY();
	
			// フォーカス中のページの場合
			if(webPagePanel==focusedWebPagePanel) {
				webPagePanel.setBackground(PanelWebPageDefAbove.pagePanelFocusedColor);
			}
			// ログアウト遷移先のページの場合
			else if(WebPageManager.getInstance().getLogoutDestPage()==page) {
				webPagePanel.setBackground(WebPagePanel.COLOR_LOGOUT_DEST_PAGE);
			}
			// それ以外の場合
			else {
				webPagePanel.setBackground(PanelWebPageDefAbove.pagePanelDefaultColor);
			}

			
			
			// 位置・サイズ調整、配置
			webPagePanel.setPreferredSize(new Dimension(WebPagePanel.PANEL_WIDTH, WebPagePanel.PANEL_HEIGHT));
			springLayout.putConstraint(SpringLayout.NORTH, webPagePanel, posY, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.WEST, webPagePanel, posX, SpringLayout.WEST, this);
			add(webPagePanel);
			Debug.out(i+"番パネル(0-b)を配置完了");
		}

		MainFrame.getInstance().repaintAndValidate();

		Debug.debug_return(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * paintComponent()
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(PanelWebPageDefAbove.backGroundColor);
		g2.fillRect(0, 0, getWidth(), getHeight());

		
		// 各Webページパネルの下にWebページ名を表示
		WebPagePanelManager webPagePanelManager = WebPagePanelManager.getInstance();
		for(int i=0; i<webPagePanelManager.getPanelNum(); i++) {
			WebPagePanel wpp = webPagePanelManager.getWebPagePanelByIndex(i);
			String pageName = wpp.page.pageFileName;

			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Serif", Font.PLAIN, 10));
			g2.drawString(pageName, wpp.getPosX()+2, wpp.getPosY()+WebPagePanel.PANEL_HEIGHT+10);
		}
		
		
		// 各遷移を表す矢印の描画
		BasicStroke wideStroke = new BasicStroke(1.0f);
		g2.setStroke(wideStroke);
		TransitionManager tm = TransitionManager.getInstance();
		for(int i=0; i<tm.getTransitionNum(); i++) {
			Transition trans = tm.getTransition(i);

			// 矢印オブジェクトの作成
			Point[] pointPair = trans.getArrowPointPair();
			Point startPagePoint = pointPair[0];
			Point endPagePoint = pointPair[1]; 
			ArrowShape as = new ArrowShape(startPagePoint, endPagePoint);

			// フォーカスされている遷移矢印は後で描画
			// （他の矢印で塗り潰されるのを避けるため）
			if(trans==focusedTrans) {
				continue;
			}
			// フォーカスされていない遷移矢印はすぐ描画
			else {
				// 灰色で矢印描画
				g2.setColor(Color.LIGHT_GRAY);
				g2.draw(as);
			}

		}

		// フォーカスされている矢印があるなら、ここでやっと描画
		// （他の矢印で塗り潰されるのを避けるため）
		if(focusedTrans instanceof Transition) {
			Point[] focusedTransPointPair = focusedTrans.getArrowPointPair();
			Point startPagePoint = focusedTransPointPair[0];
			Point endPagePoint = focusedTransPointPair[1]; 
			ArrowShape as = new ArrowShape(startPagePoint, endPagePoint);

			// 黒で矢印描画
			g2.setColor(Color.BLACK);
			g2.draw(as);
		}
	}	

	
	
	
	
	/*
	 * PURPOSE :
	 * マウスプレスイベントハンドラ
	 */
	public void mousePressed(MouseEvent e) {
		// ページパネル以外へのプレスはスルー
		if(!(e.getSource() instanceof WebPagePanel)) {
			Debug.out("1206:Webページパネル以外へのプレスなので、何もしません");
			return;
		}

		pressX = e.getX();
		pressY = e.getY();

		Debug.out("1206:Webページパネルがプレスされました　e.getX():"+e.getX()+", e.getY():"+e.getY());
	}
	/*
	 * PURPOSE :
	 * マウスクリックイベントハンドラ
	 */
	public void mouseClicked(MouseEvent e) {
		// Webページ定義画面上部パネル上でのクリック
		if(e.getSource() instanceof PanelWebPageDefAbove) {
			Debug.out("1206:上部パネル上でのクリックを確認");
			this.clickedOnBackground(e);
		}
		// Webページパネル上でのクリック
		else if(e.getSource() instanceof WebPagePanel) {
			Debug.out("1206:Webページパネル上でのクリックを確認");
			clickedOnPagePanel(e);
		}
		else {
			Debug.error("Panel_Transition_Above mouseClicked() : イベントの発生源が想定外です。");
		}
	}
	/*
	 * PURPOSE :
	 * このパネル上（ページパネル上などではない）でクリックがあった際に行う処理
	 * 
	 * NOTICE :
	 * 遷移矢印クリック時にもこの関数が実行されることになる
	 */
	public void clickedOnBackground(MouseEvent e) {
		// クリック点の座標
		int pointerX = e.getX();
		int pointerY = e.getY();

		// 「フォーカスされているページパネル」をNULLにし、他インスタンスへ報告
		focusedWebPagePanel = null;
		PanelWebPageDefBottom.getInstance().informPageUnfocus();

		// いずれかの遷移矢印がクリックされなかったかチェック
		focusedTrans = null;
		boolean arrowClicked = false;
		TransitionManager tm = TransitionManager.getInstance();
		for(int i=0; i<tm.getTransitionNum(); i++) {
			Transition tr = tm.getTransition(i);

			// この遷移矢印がクリックされた
			if(tr.within(pointerX, pointerY)) {
				arrowClicked = true;
				
				// この遷移矢印とかぶってる遷移矢印がないか調べなければならない
				ArrayList<Transition> duplicativeTransArray = tm.getDuplicativeTransitionArray(tr);
				if(duplicativeTransArray.size()==1) {	// かぶってる矢印なし
					// この矢印を普通にフォーカスするのみ
					focusedTrans = tr;
					PanelWebPageDefBottom.getInstance().informTransitionFocus();
				}
				else {	// かぶってる矢印がある
					// 全遷移矢印（この遷移矢印自身も含む）それぞれに対応するメニューを作成し、ポップアップメニューを完成、表示する
					this.popupTransList = new JPopupMenu();
					for(int transOrd=0; transOrd<duplicativeTransArray.size(); transOrd++) {
						Transition duplicativeTrans = duplicativeTransArray.get(transOrd);
						popupTransList.add(new TransitionMenuItem(duplicativeTrans));
					}
					this.popupTransList.show(e.getComponent(), e.getX(), e.getY());
				}
				
				// 以降の遷移は無視（クリックされなかったわけなので）
				break;
			}
		}

		// どの遷移矢印もクリックされなかった場合 -> 定まった処理を行う
		if(!arrowClicked) {
			onTransitionUnfocused();
		}
		
		// ページパネル再配置
		locateWebPagePanels();
	}
	/*
	 * PURPOSE :
	 * Webページパネルがクリックされたときの処理
	 */
	public void clickedOnPagePanel(MouseEvent e) {
		// イベント発生Webページパネル
		WebPagePanel sourcePanel = (WebPagePanel)e.getSource();

		// フォーカスされたことを記録
		focusedWebPagePanel = sourcePanel;

		// Webページパネルがフォーカスされたこと、それにより遷移矢印はアンフォーカスされたことを、Webページ定義画面下部パネルに報告
		PanelWebPageDefBottom.getInstance().informPageFocus();
		PanelWebPageDefBottom.getInstance().informTransitionUnfocus();

		// どの遷移矢印へのクリックでもないのは明らかである
		// -> よって遷移はアンフォーカスされたと見なし、アンフォーカス時に行うべき処理を行う
		onTransitionUnfocused();
		
		// Webページパネル再配置
		locateWebPagePanels();
	}
	/*
	 * PURPOSE :
	 * マウスリリースイベントハンドラ
	 */
	public void mouseReleased(MouseEvent e) {
		// Webページパネル以外に対するリリースはスルー
		if(!(e.getSource() instanceof WebPagePanel)) {
			Debug.out("1206:Webページパネル以外へのリリースなので、何もしません");
			return;
		}

		// イベント発生Webページパネルを一旦除去する
		WebPagePanel sourcePanel = (WebPagePanel)e.getSource();
		remove(sourcePanel);
		
		// リリース位置座標
		int pointX = e.getX();
		int pointY = e.getY();

		Debug.out("1206:Webページパネルがリリースされました　e.getX():"+e.getX()+", e.getY():"+e.getY());
		
		// プレス位置からリリース位置までの相対距離
		int xDiff = pointX - pressX;
		int yDiff = pointY - pressY;

		// さっき除去したWebページパネルの、Webページ定義画面上での座標を再設定
		sourcePanel.setPosX(sourcePanel.getPosX() + xDiff);
		sourcePanel.setPosY(sourcePanel.getPosY() + yDiff);

		// さっき除去したWebページパネルの位置・サイズ再調整、再配置
		sourcePanel.setPreferredSize(new Dimension(WebPagePanel.PANEL_WIDTH, WebPagePanel.PANEL_HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, sourcePanel, sourcePanel.getPosY(), SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, sourcePanel, sourcePanel.getPosX(), SpringLayout.WEST, this);
		add(sourcePanel);

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {
	}
	public void mouseDragged(MouseEvent e) {
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("ポップアップメニュー中の遷移の選択")) {
			TransitionMenuItem sourceMenuItem = (TransitionMenuItem)e.getSource();
			Transition selectedTrans = sourceMenuItem.trans;

			// この遷移を表す遷移矢印をフォーカスし、そのことを下部パネルへ報告
			focusedTrans = selectedTrans;
			PanelWebPageDefBottom.getInstance().informTransitionFocus();
			
			// ページパネル再配置
			locateWebPagePanels();
		}
		else {
			Debug.error("不正なアクションコマンドです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * Webページ遷移アローがアンフォーカスされたときの処理
	 * （正確には、どの遷移矢印にも当たらない場所がクリックされたときの処理。つまり、元々どのWebページ遷移アローもフォーカスされてなかったとしても実行される）
	 */
	public void onTransitionUnfocused() {
		focusedTrans = null;
		PanelWebPageDefBottom.getInstance().informTransitionUnfocus();
		Debug.out("いずれの遷移矢印へのクリックでもないので、遷移矢印フォーカスをNULLにします");
	}
	
	
	
	
	
	
	
	
	public boolean webPagePanelFocused() {
		return (focusedWebPagePanel instanceof WebPagePanel);
	}
	
	
	
	public boolean transtionIsFocused() {
		return (focusedTrans instanceof Transition);
	}
	
	
	
	public WebPage getFocusedWebPage() {
		return focusedWebPagePanel.page;
	}

	public Transition getFocusedTransition() {
		return focusedTrans;
	}

	
	
	
	
	public static PanelWebPageDefAbove getInstance() {
		return PanelWebPageDefAbove.obj;
	}

	public static void updateInstance(PanelWebPageDefAbove newObject) {
		PanelWebPageDefAbove.obj = newObject;
	}
}
