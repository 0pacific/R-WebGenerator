package view.transProcEdit.editPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import debug.Debug;

import pageElement.PageElement;
import pageElement.PageElement_HyperLink;
import view.EditPanel;
import view.transProcEdit.Panel_TpEdit_Above;
import webPage.WebPage;


public abstract class TpPagePanel extends EditPanel {
	public static final int panelWidth = 120;

	public static Color BG_COLOR = Color.MAGENTA;
	
	public WebPage page;
	
	public ArrayList<PageElementPanel> pePanels;

	public static final int PE_PANEL_SPACE = 10;
	
	
	
	
	
	public TpPagePanel(WebPage page) {
		this.page = page;

		setLayout(null);
		
		setBackground(TpPagePanel.BG_COLOR);
		setOpaque(false);
	}

	
	
	
	
	public void setPanelBorder(String title) {
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(title);
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);
	}

	
	
	
	
	/*
	 * 
	 */
	public void locatePePanels() {
		// �S�R���|�[�l���g����
		removeAll();
		
		pePanels = new ArrayList<PageElementPanel>();

		for(int i=0; i<page.getPageElementNum(); i++) {
			PageElement pe = page.getPageElement(i);
			PageElementPanel pePanel = new PageElementPanel(pe);
			
			int X_PADD = 10;
			int Y_PADD = 20;
			int SPACE = TpPagePanel.PE_PANEL_SPACE;

			int X = X_PADD;
			int HEIGHT = PageElementPanel.panelHeight;
			int Y = Y_PADD+((HEIGHT+SPACE)*i);
			int WIDTH = getWidth()-(X_PADD*2);
			pePanel.setBounds(X, Y, WIDTH, HEIGHT);
			
			// �y�[�W�p�l���ցA����PageElementPanel��ǉ�
			add(pePanel);

			// �z���PageElementPanel��ǉ��i��L��pePanels.get(i-1)�Ƃ��������̂��߁j
			pePanels.add(pePanel);
		}
	}

	
	
	
	/*
	 * PURPOSE :
	 * �e�y�[�W�G�������g�p�l���́APanel_TpEdit_Left�̍��㒸�_����̍��W���Z�b�g����
	 * ���̍��W�́A�J�ڌ��i��j�y�[�W�p�l���̍��W�ɁA�J�ڌ��i��j�y�[�W�p�l�����㒸�_����̃y�[�W�G�������g�p�l���̑��΍��W�𑫂������̂ɂȂ�
	 */
	public void setPePanelsPos() {
		for(int i=0; i<pePanels.size(); i++) {
			PageElementPanel pep = pePanels.get(i);
			pep.setPosX(getX()+pep.getX());
			pep.setPosY(getY()+pep.getY());
		}
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �Ή�����Web�y�[�W��Page Element������A���̃p�l���̏c�������肵�Ԃ�
	 */
	public int calcHeight() {
		int height = 50 + PageElementPanel.panelHeight
						+ ((page.getPageElementNum()-1)*(TpPagePanel.PE_PANEL_SPACE+PageElementPanel.panelHeight));
		return height;
	}
	
	
	
	
	public WebPage getPage() {
		return page;
	}
}
