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

	// �p�l���T�C�Y
	public static final int panelWidth = MainFrame.frameWidth;
	public static final int panelHeight = 400;

	public static Color pagePanelDefaultColor = Color.WHITE;
	public static Color pagePanelFocusedColor = Color.LIGHT_GRAY;
	
	// �w�i�F
	public static Color backGroundColor = Color.WHITE;
	
	// �y�[�W�p�l����Ń}�E�X�v���X�������ہA���̍��W�i���㒸�_����̑��΍��W�j��ۑ�����̂Ɏg��
	// �}�E�X�����[�X���ɁA���̒l���g���ăy�[�W�p�l���̈ړ����s��
	private int pressX;
	private int pressY;

	public WebPagePanel focusedWebPagePanel = null;
	private Transition focusedTrans = null;

	public SerializableSpringLayout springLayout;
	
	
	// �N���b�N������󂪑��̖��Ƃ��Ԃ��Ă���ہA�J�ڂ̃��X�g��\��������|�b�v�A�b�v���j���[
	public JPopupMenu popupTransList;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static PanelWebPageDefAbove obj = new PanelWebPageDefAbove();
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
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
	 * �eWeb�y�[�W�p�l�����Ĕz�u
	 */
	public void locateWebPagePanels() {
		Debug.debug_call(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// �܂��S�R���|�[�l���g�i�SWeb�y�[�W�p�l���j������
		removeAll();

		WebPagePanelManager webPagePanelManager = WebPagePanelManager.getInstance();

		int pageNum = pageManager.getPageNum();
		for(int i=0; i<pageNum; i++) {
			WebPage page = pageManager.getPage(i);
			WebPagePanel webPagePanel = webPagePanelManager.getWebPagePanelByPage(page);

			// Web�y�[�W��`��ʏ�ł̍��W�Ƃ��Č��ݐݒ肳��Ă���X,Y���W���擾
			int posX = webPagePanel.getPosX();
			int posY = webPagePanel.getPosY();
	
			// �t�H�[�J�X���̃y�[�W�̏ꍇ
			if(webPagePanel==focusedWebPagePanel) {
				webPagePanel.setBackground(PanelWebPageDefAbove.pagePanelFocusedColor);
			}
			// ���O�A�E�g�J�ڐ�̃y�[�W�̏ꍇ
			else if(WebPageManager.getInstance().getLogoutDestPage()==page) {
				webPagePanel.setBackground(WebPagePanel.COLOR_LOGOUT_DEST_PAGE);
			}
			// ����ȊO�̏ꍇ
			else {
				webPagePanel.setBackground(PanelWebPageDefAbove.pagePanelDefaultColor);
			}

			
			
			// �ʒu�E�T�C�Y�����A�z�u
			webPagePanel.setPreferredSize(new Dimension(WebPagePanel.PANEL_WIDTH, WebPagePanel.PANEL_HEIGHT));
			springLayout.putConstraint(SpringLayout.NORTH, webPagePanel, posY, SpringLayout.NORTH, this);
			springLayout.putConstraint(SpringLayout.WEST, webPagePanel, posX, SpringLayout.WEST, this);
			add(webPagePanel);
			Debug.out(i+"�ԃp�l��(0-b)��z�u����");
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

		
		// �eWeb�y�[�W�p�l���̉���Web�y�[�W����\��
		WebPagePanelManager webPagePanelManager = WebPagePanelManager.getInstance();
		for(int i=0; i<webPagePanelManager.getPanelNum(); i++) {
			WebPagePanel wpp = webPagePanelManager.getWebPagePanelByIndex(i);
			String pageName = wpp.page.pageFileName;

			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Serif", Font.PLAIN, 10));
			g2.drawString(pageName, wpp.getPosX()+2, wpp.getPosY()+WebPagePanel.PANEL_HEIGHT+10);
		}
		
		
		// �e�J�ڂ�\�����̕`��
		BasicStroke wideStroke = new BasicStroke(1.0f);
		g2.setStroke(wideStroke);
		TransitionManager tm = TransitionManager.getInstance();
		for(int i=0; i<tm.getTransitionNum(); i++) {
			Transition trans = tm.getTransition(i);

			// ���I�u�W�F�N�g�̍쐬
			Point[] pointPair = trans.getArrowPointPair();
			Point startPagePoint = pointPair[0];
			Point endPagePoint = pointPair[1]; 
			ArrowShape as = new ArrowShape(startPagePoint, endPagePoint);

			// �t�H�[�J�X����Ă���J�ږ��͌�ŕ`��
			// �i���̖��œh��ׂ����̂�����邽�߁j
			if(trans==focusedTrans) {
				continue;
			}
			// �t�H�[�J�X����Ă��Ȃ��J�ږ��͂����`��
			else {
				// �D�F�Ŗ��`��
				g2.setColor(Color.LIGHT_GRAY);
				g2.draw(as);
			}

		}

		// �t�H�[�J�X����Ă����󂪂���Ȃ�A�����ł���ƕ`��
		// �i���̖��œh��ׂ����̂�����邽�߁j
		if(focusedTrans instanceof Transition) {
			Point[] focusedTransPointPair = focusedTrans.getArrowPointPair();
			Point startPagePoint = focusedTransPointPair[0];
			Point endPagePoint = focusedTransPointPair[1]; 
			ArrowShape as = new ArrowShape(startPagePoint, endPagePoint);

			// ���Ŗ��`��
			g2.setColor(Color.BLACK);
			g2.draw(as);
		}
	}	

	
	
	
	
	/*
	 * PURPOSE :
	 * �}�E�X�v���X�C�x���g�n���h��
	 */
	public void mousePressed(MouseEvent e) {
		// �y�[�W�p�l���ȊO�ւ̃v���X�̓X���[
		if(!(e.getSource() instanceof WebPagePanel)) {
			Debug.out("1206:Web�y�[�W�p�l���ȊO�ւ̃v���X�Ȃ̂ŁA�������܂���");
			return;
		}

		pressX = e.getX();
		pressY = e.getY();

		Debug.out("1206:Web�y�[�W�p�l�����v���X����܂����@e.getX():"+e.getX()+", e.getY():"+e.getY());
	}
	/*
	 * PURPOSE :
	 * �}�E�X�N���b�N�C�x���g�n���h��
	 */
	public void mouseClicked(MouseEvent e) {
		// Web�y�[�W��`��ʏ㕔�p�l����ł̃N���b�N
		if(e.getSource() instanceof PanelWebPageDefAbove) {
			Debug.out("1206:�㕔�p�l����ł̃N���b�N���m�F");
			this.clickedOnBackground(e);
		}
		// Web�y�[�W�p�l����ł̃N���b�N
		else if(e.getSource() instanceof WebPagePanel) {
			Debug.out("1206:Web�y�[�W�p�l����ł̃N���b�N���m�F");
			clickedOnPagePanel(e);
		}
		else {
			Debug.error("Panel_Transition_Above mouseClicked() : �C�x���g�̔��������z��O�ł��B");
		}
	}
	/*
	 * PURPOSE :
	 * ���̃p�l����i�y�[�W�p�l����Ȃǂł͂Ȃ��j�ŃN���b�N���������ۂɍs������
	 * 
	 * NOTICE :
	 * �J�ږ��N���b�N���ɂ����̊֐������s����邱�ƂɂȂ�
	 */
	public void clickedOnBackground(MouseEvent e) {
		// �N���b�N�_�̍��W
		int pointerX = e.getX();
		int pointerY = e.getY();

		// �u�t�H�[�J�X����Ă���y�[�W�p�l���v��NULL�ɂ��A���C���X�^���X�֕�
		focusedWebPagePanel = null;
		PanelWebPageDefBottom.getInstance().informPageUnfocus();

		// �����ꂩ�̑J�ږ�󂪃N���b�N����Ȃ��������`�F�b�N
		focusedTrans = null;
		boolean arrowClicked = false;
		TransitionManager tm = TransitionManager.getInstance();
		for(int i=0; i<tm.getTransitionNum(); i++) {
			Transition tr = tm.getTransition(i);

			// ���̑J�ږ�󂪃N���b�N���ꂽ
			if(tr.within(pointerX, pointerY)) {
				arrowClicked = true;
				
				// ���̑J�ږ��Ƃ��Ԃ��Ă�J�ږ�󂪂Ȃ������ׂȂ���΂Ȃ�Ȃ�
				ArrayList<Transition> duplicativeTransArray = tm.getDuplicativeTransitionArray(tr);
				if(duplicativeTransArray.size()==1) {	// ���Ԃ��Ă���Ȃ�
					// ���̖��𕁒ʂɃt�H�[�J�X����̂�
					focusedTrans = tr;
					PanelWebPageDefBottom.getInstance().informTransitionFocus();
				}
				else {	// ���Ԃ��Ă��󂪂���
					// �S�J�ږ��i���̑J�ږ�󎩐g���܂ށj���ꂼ��ɑΉ����郁�j���[���쐬���A�|�b�v�A�b�v���j���[�������A�\������
					this.popupTransList = new JPopupMenu();
					for(int transOrd=0; transOrd<duplicativeTransArray.size(); transOrd++) {
						Transition duplicativeTrans = duplicativeTransArray.get(transOrd);
						popupTransList.add(new TransitionMenuItem(duplicativeTrans));
					}
					this.popupTransList.show(e.getComponent(), e.getX(), e.getY());
				}
				
				// �ȍ~�̑J�ڂ͖����i�N���b�N����Ȃ������킯�Ȃ̂Łj
				break;
			}
		}

		// �ǂ̑J�ږ����N���b�N����Ȃ������ꍇ -> ��܂����������s��
		if(!arrowClicked) {
			onTransitionUnfocused();
		}
		
		// �y�[�W�p�l���Ĕz�u
		locateWebPagePanels();
	}
	/*
	 * PURPOSE :
	 * Web�y�[�W�p�l�����N���b�N���ꂽ�Ƃ��̏���
	 */
	public void clickedOnPagePanel(MouseEvent e) {
		// �C�x���g����Web�y�[�W�p�l��
		WebPagePanel sourcePanel = (WebPagePanel)e.getSource();

		// �t�H�[�J�X���ꂽ���Ƃ��L�^
		focusedWebPagePanel = sourcePanel;

		// Web�y�[�W�p�l�����t�H�[�J�X���ꂽ���ƁA����ɂ��J�ږ��̓A���t�H�[�J�X���ꂽ���Ƃ��AWeb�y�[�W��`��ʉ����p�l���ɕ�
		PanelWebPageDefBottom.getInstance().informPageFocus();
		PanelWebPageDefBottom.getInstance().informTransitionUnfocus();

		// �ǂ̑J�ږ��ւ̃N���b�N�ł��Ȃ��͖̂��炩�ł���
		// -> ����đJ�ڂ̓A���t�H�[�J�X���ꂽ�ƌ��Ȃ��A�A���t�H�[�J�X���ɍs���ׂ��������s��
		onTransitionUnfocused();
		
		// Web�y�[�W�p�l���Ĕz�u
		locateWebPagePanels();
	}
	/*
	 * PURPOSE :
	 * �}�E�X�����[�X�C�x���g�n���h��
	 */
	public void mouseReleased(MouseEvent e) {
		// Web�y�[�W�p�l���ȊO�ɑ΂��郊���[�X�̓X���[
		if(!(e.getSource() instanceof WebPagePanel)) {
			Debug.out("1206:Web�y�[�W�p�l���ȊO�ւ̃����[�X�Ȃ̂ŁA�������܂���");
			return;
		}

		// �C�x���g����Web�y�[�W�p�l������U��������
		WebPagePanel sourcePanel = (WebPagePanel)e.getSource();
		remove(sourcePanel);
		
		// �����[�X�ʒu���W
		int pointX = e.getX();
		int pointY = e.getY();

		Debug.out("1206:Web�y�[�W�p�l���������[�X����܂����@e.getX():"+e.getX()+", e.getY():"+e.getY());
		
		// �v���X�ʒu���烊���[�X�ʒu�܂ł̑��΋���
		int xDiff = pointX - pressX;
		int yDiff = pointY - pressY;

		// ��������������Web�y�[�W�p�l���́AWeb�y�[�W��`��ʏ�ł̍��W���Đݒ�
		sourcePanel.setPosX(sourcePanel.getPosX() + xDiff);
		sourcePanel.setPosY(sourcePanel.getPosY() + yDiff);

		// ��������������Web�y�[�W�p�l���̈ʒu�E�T�C�Y�Ē����A�Ĕz�u
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
		
		if(cmd.equals("�|�b�v�A�b�v���j���[���̑J�ڂ̑I��")) {
			TransitionMenuItem sourceMenuItem = (TransitionMenuItem)e.getSource();
			Transition selectedTrans = sourceMenuItem.trans;

			// ���̑J�ڂ�\���J�ږ����t�H�[�J�X���A���̂��Ƃ������p�l���֕�
			focusedTrans = selectedTrans;
			PanelWebPageDefBottom.getInstance().informTransitionFocus();
			
			// �y�[�W�p�l���Ĕz�u
			locateWebPagePanels();
		}
		else {
			Debug.error("�s���ȃA�N�V�����R�}���h�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * Web�y�[�W�J�ڃA���[���A���t�H�[�J�X���ꂽ�Ƃ��̏���
	 * �i���m�ɂ́A�ǂ̑J�ږ��ɂ�������Ȃ��ꏊ���N���b�N���ꂽ�Ƃ��̏����B�܂�A���X�ǂ�Web�y�[�W�J�ڃA���[���t�H�[�J�X����ĂȂ������Ƃ��Ă����s�����j
	 */
	public void onTransitionUnfocused() {
		focusedTrans = null;
		PanelWebPageDefBottom.getInstance().informTransitionUnfocus();
		Debug.out("������̑J�ږ��ւ̃N���b�N�ł��Ȃ��̂ŁA�J�ږ��t�H�[�J�X��NULL�ɂ��܂�");
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
