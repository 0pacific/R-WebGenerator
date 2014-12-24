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
	
	// �Ή�����Web�y�[�W
	public WebPage page;
	
	// �}�E�X�Ń|�C���g����Ă��邩�i�|�C���^����������Ă��邩�j
	private boolean pointed = false;
	
	



	/*
	 * SUMMARY :
	 * �R���X�g���N�^
	 */
	public WebPagePanel(WebPage page, Point point) {
		this.page = page;
		
		// �g��
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		setBorder(eBorder);

		// Web�y�[�W��`��ʓ��ł̍��W
		this.setPosX(point.x);
		this.setPosY(point.y);

		// �}�E�X���X�i
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
