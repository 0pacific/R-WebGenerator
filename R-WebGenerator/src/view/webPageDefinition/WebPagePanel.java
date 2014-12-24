package view.webPageDefinition;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import debug.Debug;
import view.EditPanel;
import webPage.WebPage;





public class WebPagePanel extends EditPanel implements MouseListener {
	public static final int PANEL_WIDTH = 40;
	public static final int PANEL_HEIGHT = 60;
	
	public static final Color COLOR_LOGOUT_DEST_PAGE = Color.BLACK;
	public static final Border ARBUTTON_PAGE_BORDER = new LineBorder(Color.BLACK,3);
	
	// 対応するWebページ
	public WebPage page;
	
	// マウスでポイントされているか（ポインタが乗っかっているか）
	private boolean pointed = false;
	
	



	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public WebPagePanel(WebPage page, Point point) {
		this.page = page;
		
		// 枠線
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		setBorder(eBorder);

		// Webページ定義画面内での座標
		this.setPosX(point.x);
		this.setPosY(point.y);

		// マウスリスナ
		addMouseListener(PanelWebPageDefAbove.getInstance());
	}






	public void mouseEntered(MouseEvent e) {
		pointed = true;
	}
	public void mouseExited(MouseEvent e) {
		pointed = false;
	}
	
	
	
	
	
	public void mousePressed(MouseEvent e) {
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}
