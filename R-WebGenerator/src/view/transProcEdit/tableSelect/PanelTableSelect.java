package view.transProcEdit.tableSelect;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.TransitionProcess;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;
import utility.*;

public class PanelTableSelect extends JPanel implements ActionListener {
	public static final int MODE_ADD_TABLE_READING = 0;
	public static final int MODE_EDIT_TABLE_READING = 1;
	public static final int MODE_ADD_CREATE_REFLECTION = 2;
	public static final int MODE_EDIT_CREATE_REFLECTION = 3;
	public static final int MODE_ADD_DELETE_REFLECTION = 4;
	public static final int MODE_EDIT_DELETE_REFLECTION = 5;
	public static final int MODE_SELECT_IA_REFLECTION_DT = 6;
	public static final int MODE_SELECT_IA_REFLECTION_AT = 7;
	public int mode;
	
	// フレーム上での位置
	public static final int posX = 0;
	public static final int posY = 0;

	// サイズ
	public static final int panelWidth = FrameTableSelect.frameWidth;
	public static final int panelHeight = FrameTableSelect.frameHeight;

	// 各データテーブル・アカウントテーブルを表すボタン
	ArrayList<TableButton> buttons;
	
	// 全ボタンの横のパディング
	public static final int btnPaddX = 20;

	// 最初のボタンのY座標
	public static final int firBtnPosY = 20;

	// ボタン同士の間隔
	public static final int btnDist = 20;

	// ボタンの縦幅
	public static final int btnHeight = 50;
	
	public JButton btnCancel;
	
	public SerializableSpringLayout springLayout;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static PanelTableSelect obj = new PanelTableSelect();
	
	
	
	
	private PanelTableSelect() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	public void locateButtons() {
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		TableManager tableManager = TableManager.getInstance();
		buttons = new ArrayList<TableButton>();

		// データテーブル
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			buttons.add(new TableButton(dt));
		}

		// アカウントテーブル
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			buttons.add(new TableButton(at));
		}


		// 各ボタンの配置
		int PADD_SIDE = 40;
		int BTN_HEIGHT = 30;
		int BTN_SPACING = 10;
		for(int i=0; i<buttons.size(); i++) {
			TableButton btn = buttons.get(i);
			btn.addActionListener(this);

			// 一応、ActionCommandは全てのボタンで統一する
			// ボタンの判別のためには、e.getSource()でボタンを取得しいろいろやる
			btn.setActionCommand("テーブルボタンクリック");

			// 配置
			btn.setPreferredSize(new Dimension(panelWidth-PADD_SIDE-PADD_SIDE, BTN_HEIGHT));
			Slpc.put(springLayout, "N", btn, "N", this, 20+(i*(BTN_HEIGHT+BTN_SPACING)));
			Slpc.put(springLayout, "W", btn, "W", this, PADD_SIDE);
			add(btn);
		}

		// キャンセルボタンの配置
		btnCancel = new JButton(japanese?"キャンセル":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("キャンセル");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20+(buttons.size()*(BTN_HEIGHT+BTN_SPACING)));
		Slpc.put(springLayout, "W", btnCancel, "W", this, PADD_SIDE);
		add(btnCancel);
		
		FrameTableSelect.repaintAndValidate();
	}

	
	
	
	public void locateDataTableButtons() {
		removeAll();
		
		boolean japanese = GeneratorProperty.japanese();

		TableManager tableManager = TableManager.getInstance();
		buttons = new ArrayList<TableButton>();

		// データテーブルのみ
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			buttons.add(new TableButton(dt));
		}

		// 各ボタンの配置
		int PADD_SIDE = 40;
		int BTN_HEIGHT = 30;
		int BTN_SPACING = 10;
		for(int i=0; i<buttons.size(); i++) {
			TableButton btn = buttons.get(i);
			btn.addActionListener(this);

			// 一応、ActionCommandは全てのボタンで統一する
			// ボタンの判別のためには、e.getSource()でボタンを取得しいろいろやる
			btn.setActionCommand("テーブルボタンクリック");

			// 配置
			btn.setPreferredSize(new Dimension(this.panelWidth-(2*PADD_SIDE),BTN_HEIGHT));
			Slpc.put(springLayout, "N", btn, "N", this, 20+(i*(BTN_HEIGHT+BTN_SPACING)));
			Slpc.put(springLayout, "W", btn, "W", this, PADD_SIDE);
			add(btn);
		}

		// キャンセルボタンの配置
		btnCancel = new JButton(japanese?"キャンセル":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("キャンセル");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20+(buttons.size()*(BTN_HEIGHT+BTN_SPACING)));
		Slpc.put(springLayout, "W", btnCancel, "W", this, PADD_SIDE);
		add(btnCancel);

		FrameTableSelect.repaintAndValidate();
	}
	

	
	
	public void locateAccountTableButtons() {
		removeAll();
		
		boolean japanese = GeneratorProperty.japanese();

		TableManager tableManager = TableManager.getInstance();
		buttons = new ArrayList<TableButton>();

		// アカウントテーブルのみ
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			buttons.add(new TableButton(at));
		}

		// 各ボタンの配置
		int PADD_SIDE = 40;
		int BTN_HEIGHT = 30;
		int BTN_SPACING = 10;
		for(int i=0; i<buttons.size(); i++) {
			TableButton btn = buttons.get(i);
			btn.addActionListener(this);

			// 一応、ActionCommandは全てのボタンで統一する
			// ボタンの判別のためには、e.getSource()でボタンを取得しいろいろやる
			btn.setActionCommand("テーブルボタンクリック");

			// 配置
			btn.setPreferredSize(new Dimension(this.panelWidth-(2*PADD_SIDE),BTN_HEIGHT));
			Slpc.put(springLayout, "N", btn, "N", this, 20+(i*(BTN_HEIGHT+BTN_SPACING)));
			Slpc.put(springLayout, "W", btn, "W", this, PADD_SIDE);
			add(btn);
		}

		// キャンセルボタンの配置
		btnCancel = new JButton(japanese?"キャンセル":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("キャンセル");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20+(buttons.size()*(BTN_HEIGHT+BTN_SPACING)));
		Slpc.put(springLayout, "W", btnCancel, "W", this, PADD_SIDE);
		add(btnCancel);

		FrameTableSelect.repaintAndValidate();
	}

	
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEventハンドラ
	 */
	public void actionPerformed(ActionEvent e) {
		TransitionProcess editingTransProc = Panel_TpEdit_Above.getInstance().editingTp;
		String cmd = e.getActionCommand();
		
		// テーブルを表すいずれかのボタンがクリックされた
		if(cmd.equals("テーブルボタンクリック")) {
			// クリックされたボタンを取得
			TableButton btn = (TableButton)e.getSource();

			// テーブルリーディング追加モードの場合
			if(mode==PanelTableSelect.MODE_ADD_TABLE_READING) {
				// 遷移プロセス定義画面の左側パネルへ、テーブルを伝える
				// （向こうではテーブルリーディングの作成・遷移プロセスへの追加・パネル追加を行う）
				Panel_TpEdit_Above.getInstance().addTableReadingAndTableReadingPanelByTable(btn.getTable());
			}
			// CreateリフレクションのTFDを編集するモードの場合
			else if(mode==PanelTableSelect.MODE_ADD_CREATE_REFLECTION) {
				TppCreateReflection reflection = new TppCreateReflection(editingTransProc, btn.getTable());
				editingTransProc.addTpp(reflection);	// とりあえず最後尾のTPPとして追加。このCreateリフレクションを編集する度に、適切な位置へ移動させる

				// 作成したCreateリフレクションを表すパネルを左パネルの遷移プロセス図へ追加
				Panel_TpEdit_Above.getInstance().addCreateReflectionPanel(reflection, 100, 200);
			}
			// IAリフレクションのデータテーブル追加モードの場合
			else if(mode==PanelTableSelect.MODE_SELECT_IA_REFLECTION_DT) {
				DataTable dataTable = (DataTable)btn.getTable();

				// 選択されたデータテーブルをIAリフレクション編集パネルに報告し、後は向こうでやってもらう
				PanelIaReflectionEdit.getInstance().informSelectedDataTable(dataTable);
			}
			// IAリフレクションのアカウントテーブル追加モードの場合
			else if(mode==PanelTableSelect.MODE_SELECT_IA_REFLECTION_AT) {
				AccountTable accountTable = (AccountTable)btn.getTable();

				// 選択されたアカウントテーブルをIAリフレクション編集パネルに報告し、後は向こうでやってもらう
				PanelIaReflectionEdit.getInstance().informSelectedAccountTable(accountTable);
			}

			// TPPパネルを再配置
			Panel_TpEdit_Above.getInstance().locateTppPanels();
		}
		else if(cmd.equals("キャンセル")) {
			// 何もしない（フレーム不可視化等は下の方でやる）
		}
		else {
			Debug.error("不正なActionCommandです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		MainFrame.repaintAndValidate();
		FrameTableSelect.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
		
		// テーブル選択フレームを不可視化、ジェネレータフレームを操作可能に
		FrameTableSelect.getInstance().setVisible(false);
		FrameTableSelect.getInstance().setEnabled(false);
		FrameTableSelect.getInstance().setFocusable(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();
	}
	


	public static PanelTableSelect getInstance() {
		return PanelTableSelect.obj;
	}




	public static void updateInstance(PanelTableSelect newObject) {
		PanelTableSelect.obj = newObject;
	}
}
