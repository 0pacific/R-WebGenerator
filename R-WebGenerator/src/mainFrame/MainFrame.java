package mainFrame;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.util.*;
import java.util.zip.*;
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
import tpp.portTrans.*;
import tpp.service.*;
import transition.*;
import utility.*;
import utility.serialize.Serializer;
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
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;
import view.webPageDefinition.*;
import view.webPageDefinition.transAuthEdit.FrameTransAuthEdit;
import webPage.*;







/*
 * Singleton
 */
public class MainFrame extends JFrame implements Serializable {
	public static final int frameWidth = 1000;
	public static final int frameHeight = 620;
	public JPanel generatorPanel;
	public SerializableSpringLayout genPanelLayout;

	public SerializableSpringLayout springLayout = new SerializableSpringLayout();
	
	/*
	 * インスタンスは最後！
	 */
	private static MainFrame obj = new MainFrame();

	
	
	
	
	/*
	 * PURPOSE :
	 * main()
	 */
	public static void main(String[] args) {
		// メインのJFrame
		MainFrame frame = MainFrame.getInstance();
		
		// JFrame可視化
		frame.setVisible(true);
		frame.setEnabled(true);
		frame.setFocusable(true);

		//frame.test();
		//frame.shiftToTableList();
		frame.shiftToWebPageDefinition();

		frame.requestFocus();
	}
	
	
	
	
	
	public void test() {
		Tester tester = new Tester();

		//tester.test_debug();
		//tester.test_questionnaire();
		//tester.test_reportSystem();

		//tester.gotoTransProcEdit("register.php", 0);
	}
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private MainFrame() {
		setBounds(170, 270, MainFrame.frameWidth, MainFrame.frameHeight);
		setLayout(new FlowLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    
	    // Generator Panel 初期化・追加
	    generatorPanel = new JPanel();
		generatorPanel.setPreferredSize(new Dimension(MainFrame.frameWidth, MainFrame.frameHeight));
		generatorPanel.setBackground(Color.WHITE);
		genPanelLayout = new SerializableSpringLayout();
	    getContentPane().add(generatorPanel, BorderLayout.CENTER);

	    // KeyListenerは現時点で複数コンポーネントに実装している
		setFocusable(true);	// KeyListenerを追加するときいるとかいらないとか
	}
	
	
	
	
	
	
	public static MainFrame getInstance() {
		return MainFrame.obj;
	}
	
	
	
	
	
	
	/*
	 *生成画面へシフト
	 */
	public void shiftToGenerate() {
		// Generator Panel から全コンポーネントを除去
		clearGeneratorPanel();

		generatorPanel.setLayout(genPanelLayout);
		
		PanelGeneration panel = PanelGeneration.getInstance();
		panel.setPreferredSize(new Dimension(MainFrame.frameWidth, MainFrame.frameHeight));
		Slpc.put(genPanelLayout, "N", panel, "N", generatorPanel, 0);
		Slpc.put(genPanelLayout, "W", panel, "W", generatorPanel, 0);
		generatorPanel.add(panel);
		
		panel.locateComps();

		Frame_TransProcEdit_Sub.getInstance().setVisible(false);
		
		// 再描画・再検証
		MainFrame.repaintAndValidate();
	}

	
	
	/*
	 * Page Element 編集画面へシフト
	 */
	public void shiftToPeEdit(WebPage targetPage) {
		// Generator Panel から全コンポーネントを除去
		clearGeneratorPanel();

		generatorPanel.setLayout(springLayout);
		
		// 左上のパネルの初期化・テキストペイン配置
		Panel_PeEdit_Left panelLeft = Panel_PeEdit_Left.getInstance(); 
		panelLeft.setPreferredSize(new Dimension(Panel_PeEdit_Left.panelWidth, Panel_PeEdit_Left.panelHeight));
		panelLeft.setPage(targetPage);
		panelLeft.locateTextPanes();

	    // 左上のパネルを包むJScrollPaneの初期化・追加
	    JScrollPane sp = new JScrollPane();
	    sp.getViewport().add(panelLeft);
	    sp.getVerticalScrollBar().setUnitIncrement(10);
	    sp.setPreferredSize(new Dimension(Panel_PeEdit_Left.panelWidth+20, Panel_PeEdit_Right.panelHeight));
	    Slpc.put(springLayout, "N", sp, "N", generatorPanel, 0);
	    Slpc.put(springLayout, "W", sp, "W", generatorPanel, 0);
	    generatorPanel.add(sp);
	    
		// 右上のパネル（画面遷移する際、ここのコンポーネントは全て削除）
		Panel_PeEdit_Right panelRight = Panel_PeEdit_Right.getInstance(); 
		panelRight.setPreferredSize(new Dimension(MainFrame.frameWidth-Panel_PeEdit_Left.panelWidth, Panel_PeEdit_Right.panelHeight));
		Slpc.put(springLayout, "N", panelRight, "N", generatorPanel, 0);
	    Slpc.put(springLayout, "W", panelRight, "E", sp, 0);
	    panelRight.removeAllCompsPeEditRight();
	    generatorPanel.add(panelRight);

		// 下のパネル
		PanelPeEditBottom panelBottom = PanelPeEditBottom.getInstance(); 
		panelBottom.setPreferredSize(new Dimension(MainFrame.frameWidth, MainFrame.frameHeight-Panel_PeEdit_Right.panelHeight));
		Slpc.put(springLayout, "N", panelBottom, "S", sp, 0);
	    Slpc.put(springLayout, "W", panelBottom, "W", generatorPanel, 0);
		generatorPanel.add(panelBottom);
		panelBottom.locateComps();
		
		Frame_TransProcEdit_Sub.getInstance().setVisible(false);

		// 再描画・再検証
		repaintGeneratorPanel();
		validate();
	}


	
	
	
	/*
	 * Transition Process 編集画面へシフト
	 */
	public void shiftToTpEdit(Transition targetTransition) {
		// Generator Panel から全コンポーネントを除去
		clearGeneratorPanel();

	    generatorPanel.setLayout(genPanelLayout);
		
		// 上のパネルの初期化・テキストペイン配置
		Panel_TpEdit_Above panelLeft = Panel_TpEdit_Above.getInstance(); 
		panelLeft.setPreferredSize(new Dimension(Panel_TpEdit_Above.panelWidth, Panel_TpEdit_Above.panelHeight));
		panelLeft.setEditingTp(targetTransition.getTransitionProcess());
		panelLeft.locateTppPanels();

		
	    // 上のパネルを包むJScrollPaneの初期化・追加
	    JScrollPane sp = new JScrollPane();
	    sp.getViewport().add(panelLeft);
	    sp.getVerticalScrollBar().setUnitIncrement(10);
	    Dimension size = new Dimension(Panel_TpEdit_Above.spWidth, Panel_TpEdit_Above.panelHeight);
	    sp.setPreferredSize(size);
	    genPanelLayout.putConstraint(SpringLayout.NORTH, sp, 20, SpringLayout.NORTH, generatorPanel);
	    genPanelLayout.putConstraint(SpringLayout.WEST,  sp, 20, SpringLayout.WEST,  generatorPanel);
	    generatorPanel.add(sp);

	    
		// 下のパネル
		Panel_TpEdit_Bottom panelBottom = Panel_TpEdit_Bottom.getInstance(); 
		panelBottom.setPreferredSize(new Dimension(MainFrame.frameWidth-40, MainFrame.frameHeight-Panel_TpEdit_Above.panelHeight));
		genPanelLayout.putConstraint(SpringLayout.NORTH, panelBottom, 10, SpringLayout.SOUTH, sp);
	    genPanelLayout.putConstraint(SpringLayout.WEST, panelBottom, 20, SpringLayout.WEST, generatorPanel);
		generatorPanel.add(panelBottom);
		panelBottom.relocateComps();

		Frame_TransProcEdit_Sub subFrame = Frame_TransProcEdit_Sub.getInstance();
		subFrame.setVisible(true);
		Panel_TransProcEdit_Sub.getInstance().removeAll();
		subFrame.repaintAndValidate();
		
		// 再描画・再検証
		repaintGeneratorPanel();
		validate();
	}

	
	
	
	
	
	
	/*
	 * Webページ定義画面へシフト
	 */
	public void shiftToWebPageDefinition() {
		Frame_TransProcEdit_Sub.getInstance().setVisible(false);

		// Generator Panel から全コンポーネントを除去
		clearGeneratorPanel();

		// Generator Panel の GridBagLayout
		GridBagLayout gridbag = new GridBagLayout();
		generatorPanel.setLayout(gridbag);
		GridBagConstraints constraints = new GridBagConstraints();

	    // 上部パネル追加
		PanelWebPageDefAbove panel = PanelWebPageDefAbove.getInstance();
		panel.setPreferredSize(new Dimension(PanelWebPageDefAbove.panelWidth, PanelWebPageDefAbove.panelHeight));
		panel.setBackground(PanelWebPageDefAbove.backGroundColor);
		panel.locateWebPagePanels();
		Constraints.set(constraints, 0, 0, 1, 1);
	    gridbag.setConstraints(panel, constraints);
		generatorPanel.add(panel);

	    // 下部パネル追加
		PanelWebPageDefBottom button_panel = PanelWebPageDefBottom.getInstance();
	    button_panel.setPreferredSize(new Dimension(PanelWebPageDefBottom.panelWidth, PanelWebPageDefBottom.panelHeight));
		button_panel.setBackground(PanelWebPageDefBottom.backGroundColor);
		Constraints.set(constraints, 0, GridBagConstraints.RELATIVE, 1, 1);
	    gridbag.setConstraints(button_panel, constraints);
		generatorPanel.add(button_panel);
		button_panel.locateCompsWebPageDefBottom();
		
		// 全WebPagePanelインスタンスにMouseListenerとMouseMotionListenerを追加
		// ◆暫定 : Panel_Transition_AboveインスタンスをListenerとして何度も追加することを避けるため、
		//         必ず一度削除してから追加し直すようにしている
		WebPageManager pageManager = WebPageManager.getInstance();
		for(int i=0; i<pageManager.getPageNum(); i++) {
			WebPage page = pageManager.getPage(i);
			WebPagePanel webPagePanel = WebPagePanelManager.getInstance().getWebPagePanelByPage(page);
			webPagePanel.removeMouseListener(PanelWebPageDefAbove.getInstance());
			webPagePanel.removeMouseMotionListener(PanelWebPageDefAbove.getInstance());
			webPagePanel.addMouseListener(PanelWebPageDefAbove.getInstance());
			webPagePanel.addMouseMotionListener(PanelWebPageDefAbove.getInstance());
		}
		
		repaintGeneratorPanel();
		validate();
	}

	/*
	 * ロール定義画面へシフト
	 */
	public void shiftToRoleEdit() {
		Frame_TransProcEdit_Sub.getInstance().setVisible(false);

		// ジェネレータパネルの全コンポーネント除去
		clearGeneratorPanel();

		// ジェネレータパネルにSpringLayoutセット
		generatorPanel.setLayout(genPanelLayout);

		// メインのパネル
		Panel_RoleEdit_Above mainPanel = Panel_RoleEdit_Above.getInstance();
		mainPanel.setBackground(Panel_RoleEdit_Above.backGroundColor);
		//mainPanel.setPreferredSize(new Dimension(MainFrame.frameWidth, Panel_RoleEdit_Above.panelHeight));
		mainPanel.setPreferredSize(new Dimension(1000, 400));
		genPanelLayout.putConstraint(SpringLayout.NORTH, mainPanel, 20, SpringLayout.NORTH, generatorPanel);
		genPanelLayout.putConstraint(SpringLayout.WEST, mainPanel, 20, SpringLayout.WEST, generatorPanel);
		generatorPanel.add(mainPanel);
		mainPanel.refreshRoleTable();
		
		// ボタン配置パネル
		Panel_RoleEdit_Bottom buttonPanel = Panel_RoleEdit_Bottom.getInstance();
		buttonPanel.setBackground(Panel_RoleEdit_Above.backGroundColor);
		buttonPanel.setPreferredSize(new Dimension(MainFrame.frameWidth, MainFrame.frameHeight-Panel_RoleEdit_Above.panelHeight));
		genPanelLayout.putConstraint(SpringLayout.NORTH, buttonPanel, 20, SpringLayout.SOUTH, mainPanel);
		genPanelLayout.putConstraint(SpringLayout.WEST, buttonPanel, 20, SpringLayout.WEST, generatorPanel);
		generatorPanel.add(buttonPanel);
		buttonPanel.locateComps();
		
		MainFrame.repaintAndValidate();
	}

	
	/*
	 * テーブルリスト画面へシフト
	 */
	public void shiftToTableList() {
		Frame_TransProcEdit_Sub.getInstance().setVisible(false);

		clearGeneratorPanel();

		generatorPanel.setLayout(genPanelLayout);

		// 上のパネル
		PanelTableListAbove panelAbove = PanelTableListAbove.getInstance();
		panelAbove.setBackground(PanelTableListAbove.backGroundColor);
		panelAbove.setPreferredSize(new Dimension(generatorPanel.getWidth(), panelAbove.panelHeight));
		genPanelLayout.putConstraint(SpringLayout.NORTH, panelAbove, 0, SpringLayout.NORTH, generatorPanel);
		genPanelLayout.putConstraint(SpringLayout.WEST, panelAbove, 20, SpringLayout.WEST, generatorPanel);
		//genPanelLayout.putConstraint(SpringLayout.EAST, panelAbove, -20, SpringLayout.EAST, generatorPanel);
		generatorPanel.add(panelAbove);
		panelAbove.locateComps();

		// 下のパネル
		PanelTableListBottom panelBottom = PanelTableListBottom.getInstance();
		panelBottom.setBackground(PanelTableListBottom.backGroundColor);
		panelBottom.setPreferredSize(new Dimension(this.getWidth(), MainFrame.frameWidth-panelAbove.panelHeight));
		genPanelLayout.putConstraint(SpringLayout.NORTH, panelBottom, 0, SpringLayout.SOUTH, panelAbove);
		genPanelLayout.putConstraint(SpringLayout.WEST, panelBottom, 20, SpringLayout.WEST, generatorPanel);
		generatorPanel.add(panelBottom);
		panelBottom.locateCompsPanelTableListBottom();

		MainFrame.repaintAndValidate();
	}

	
	/*
	 * フィールドリスト画面へシフト
	 */
	public void shiftToFieldList(SuperTable table) {
		Frame_TransProcEdit_Sub.getInstance().setVisible(false);

		clearGeneratorPanel();

		generatorPanel.setLayout(genPanelLayout);

		Panel_FieldList mainPanel = Panel_FieldList.getInstance();
		mainPanel.setPreferredSize(new Dimension(MainFrame.frameWidth-40, MainFrame.frameHeight-40));
		genPanelLayout.putConstraint(SpringLayout.NORTH, mainPanel, 20, SpringLayout.NORTH, generatorPanel);
		genPanelLayout.putConstraint(SpringLayout.WEST, mainPanel, 20, SpringLayout.WEST, generatorPanel);
		generatorPanel.add(mainPanel);
		mainPanel.locateComps(table);

		repaintGeneratorPanel();
		validate();
	}

	
	/*
	 * 権限定義画面へシフト
	 */
	public void shiftToAuthEdit() {
		Frame_TransProcEdit_Sub.getInstance().setVisible(false);

		// Generator Panel から全コンポーネントを除去
		clearGeneratorPanel();
		generatorPanel.setLayout(genPanelLayout);

		
		// 上部パネル初期化
		PanelAuthEditAbove panelAbove = PanelAuthEditAbove.getInstance(); 
		panelAbove.setPreferredSize(new Dimension(panelAbove.getPanelWidth(), panelAbove.getPanelHeight()));
	    panelAbove.relocate(null, null);

	    int SCROLL_PANE_HEIGHT = 400;
	    
	    // 上部パネルを包むスクロールペイン初期化
	    JScrollPane sp = new JScrollPane();
	    sp.setPreferredSize(new Dimension(MainFrame.frameWidth, SCROLL_PANE_HEIGHT));
	    sp.getViewport().add(panelAbove);
	    sp.getVerticalScrollBar().setUnitIncrement(10);


	    // 下部パネル初期化
		PanelAuthEditBottom panelBottom = PanelAuthEditBottom.getInstance(); 
		panelBottom.setPreferredSize(new Dimension(MainFrame.frameWidth, MainFrame.frameHeight-SCROLL_PANE_HEIGHT));
		panelBottom.relocateCompsPanelAuthEditBottom();

		
	    genPanelLayout.putConstraint(SpringLayout.NORTH, sp, 0, SpringLayout.NORTH, generatorPanel);
//	    genPanelLayout.putConstraint(SpringLayout.SOUTH, sp, PanelAuthEditAbove.SCROLL_PANE_HEIGHT, SpringLayout.NORTH, sp);
	    genPanelLayout.putConstraint(SpringLayout.WEST, sp, 0, SpringLayout.WEST, generatorPanel);
//	    genPanelLayout.putConstraint(SpringLayout.EAST, sp, panelAbove.getPanelWidth(), SpringLayout.WEST, sp);
	    generatorPanel.add(sp);
	    genPanelLayout.putConstraint(SpringLayout.NORTH, panelBottom, 0, SpringLayout.SOUTH, sp);
//	    genPanelLayout.putConstraint(SpringLayout.SOUTH, panelBottom, -10, SpringLayout.SOUTH, generatorPanel);
	    genPanelLayout.putConstraint(SpringLayout.WEST, panelBottom, 0, SpringLayout.WEST, generatorPanel);
//	    genPanelLayout.putConstraint(SpringLayout.EAST, panelBottom, -10, SpringLayout.EAST, generatorPanel);
	    generatorPanel.add(panelBottom);

	    // 再描画・再検証
		MainFrame.repaintAndValidate();
	}

	
	
	public void clearGeneratorPanel() {
		generatorPanel.removeAll();
	}
	
	public JPanel getGeneratorPanel() {
		return generatorPanel;
	}

	/*
	 * PURPOSE :
	 * GeneratorPanel に repaint() させる
	 */
	public void repaintGeneratorPanel() {
		generatorPanel.repaint();
	}

	public void repaintThenValidate() {
		repaintGeneratorPanel();
		validate();
	}

	public static void repaintAndValidate() {
		MainFrame.obj.repaintThenValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}





	public static void updateInstance(MainFrame newObject) {
		MainFrame.obj = newObject;
	}
}
