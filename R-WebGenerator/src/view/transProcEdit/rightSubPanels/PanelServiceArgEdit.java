package view.transProcEdit.rightSubPanels;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.port.DataPort;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import transition.TransitionProcess;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Bottom;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.constArrayEdit.FrameConstArrayEdit;
import view.transProcEdit.constArrayEdit.PanelConstArrayEdit;
import view.transProcEdit.constEdit.FrameConstEdit;
import view.transProcEdit.constEdit.PanelConstEdit;
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;

import java.awt.*;

import javax.swing.*;

import pageElement.PageElementSaif;
import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import java.awt.event.*;
import java.util.ArrayList;
import utility.*;

/*
 * SUMMARY :
 * サービス引数選択パネル
 * 
 * Singleton
 */
public class PanelServiceArgEdit extends JPanel implements ActionListener {
	// サービス
	public Service service;

	// 編集中のサービス引数の番号
	public int editingArgNumber;
	
	// 「現在編集中のサービス引数ポート」を表す
	public TppPort editingArgPort;

	// X方向のパディング
	public final int PADD_X = 20;

	// 上端のパディング
	public final int PADD_ABOVE = 20;

	// 各引数の領域同士の間隔
	public final int SPACING_ARGS = 30;
	
	// 引数の名前・種類・説明を記述するテキストペインの縦幅
	public final int HEIGHT_TP_NAME = 25;
	public final int HEIGHT_TP_TYPE = 50;
	public final int HEIGHT_TP_DESC = 100;

	// テキストペイン同士の間隔、およびテキストペインとボタンの間の間隔
	public final int SPACING = 10;

	// サービス引数編集ボタンとその横のラベル（定義済 or 未定義）の縦幅
	public final int BTN_HEIGHT = 30;

	// 定義済か否か示すラベルの横幅
	public final int LABEL_RES_WIDTH = 100;


	// 出力編集ボタン
	public JButton btnServOutEdit;
	
	// 削除ボタン
	public JButton buttonDeleteService;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	
	// 背景色
	public static Color backgroundColor = Color.WHITE;

	
	/*
	 * ◆インスタンス参照変数は最後！
	 */
	private static PanelServiceArgEdit obj = new PanelServiceArgEdit();
	
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private PanelServiceArgEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(backgroundColor);
	}

	
	

	
	/*
	 * PURPOSE :
	 * 各コンポーネントの再配置
	 */
	public void relocate(Service service) {
		// 全コンポーネント除去
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		this.service = service;
		
		// テキストペイン配列
		ArrayList<JTextPane> textPaneArray_argName = new ArrayList<JTextPane>();

		// サービス引数編集ボタンの配列
		ArrayList<JButton> editBtnArray = new ArrayList<JButton>();

		String[] argNames = service.getArgNames();
		String[] argTypes = service.getArgTypes();
		String[] argDescs = service.getArgDescs();
				
		int argNum = service.getArgNum();
		for(int i=0; i<argNum; i++) {
			// 引数説明テキストペイン
			String argName = argNames[i];
			JTextPane textPane_argName = new JTextPane();
			String text = japanese ? "引数"+(i+1)+": "+argName : "Argument "+(i+1)+" : "+argName;
			textPane_argName.setText(text);
			textPaneArray_argName.add(textPane_argName);
			textPane_argName.setPreferredSize(new Dimension(this.getWidth()-20-20, 20));
			if(i==0) {
				springLayout.putConstraint(SpringLayout.NORTH, textPane_argName, 20, SpringLayout.NORTH, this);
			}
			else {
				springLayout.putConstraint(SpringLayout.NORTH, textPane_argName, 60, SpringLayout.SOUTH, editBtnArray.get(i-1));
			}
			springLayout.putConstraint(SpringLayout.WEST, textPane_argName, 20, SpringLayout.WEST, this);
			add(textPane_argName);

			
			
			// 引数編集ボタンの作成・追加
			// （引数の種類により表記は変える）
			// ★INTモノ定数、STRINGモノ定数などは編集方法が2通りあるため編集ボタンが２つなので注意
			ButtonServArgEdit editBtn = new ButtonServArgEdit(service, i);
			editBtn.addActionListener(this);
			ButtonServArgEdit editBtnLeft = new ButtonServArgEdit(service, i);	// ２つの編集ボタンが必要な場合のみ使う。このボタンが左側に来る
			editBtnLeft.addActionListener(this);
			
			int btnWidth = 0;
			int btnLeftWidth = 0;

			// INT型のサービス引数
			if(argTypes[i].equals(Service.IO_TYPE_CONST_INT)) {
				editBtnLeft.setText(japanese ? "入力" : "Input");
				editBtnLeft.setActionCommand("サービス引数編集ボタンクリック : モノ定数　直接入力");
				btnLeftWidth = 60;
				editBtn.setText(japanese ? "左図から選択" : "Select in Diagram");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : INT型サービス引数　選択");
				btnWidth = 120;
			}
			// VARCHAR型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_CONST_STRING)) {
				editBtnLeft.setText(japanese ? "入力" : "Input");
				editBtnLeft.setActionCommand("サービス引数編集ボタンクリック : モノ定数　直接入力");
				btnLeftWidth = 60;
				editBtn.setText(japanese ? "左図から選択" : "Select in Diagram");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : VARCHAR型サービス引数　選択");
				btnWidth = 120;
			}
			// INT配列型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_CONST_INT_ARRAY)) {
				editBtn.setText(japanese ? "入力" : "Input");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : 定数配列");
				btnWidth = 60;
			}
			// VARCHAR配列型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_CONST_STRING_ARRAY)) {
				editBtn.setText(japanese ? "入力" : "Input");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : 定数配列");
				btnWidth = 60;
			}
			// DATETIME型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_DATETIME)) {
				editBtn.setText(japanese ? "左図から選択" : "Select in Diagram");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : DATETIME型サービス引数　選択");
				btnWidth = 120;
			}
			// DATE型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_DATE)) {
				editBtn.setText(japanese ? "左図から選択" : "Select in Diagram");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : DATE型サービス引数　選択");
				btnWidth = 120;
			}
			// TIME型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_TIME)) {
				editBtn.setText(japanese ? "左図から選択" : "Select in Diagram");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : TIME型サービス引数　選択");
				btnWidth = 120;
			}
			// フィールド選択型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_OFFSETS)) {
				editBtn.setText(japanese ? "フィールドを選択" : "Select fields");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : フィールド選択");
				btnWidth = 150;
			}
			// TFD型のサービス引数
			else if(argTypes[i].equals(Service.IO_TYPE_TFD)) {
				editBtn.setText(japanese ? "左図から選択" : "Select in Diagram");
				editBtn.setActionCommand("サービス引数編集ボタンクリック : TFD");
				btnWidth = 220;
			}
			else {
				Debug.informError();
				Debug.error("PanelCsArgs relocate() : 不正な引数種類です");
			}

			// 編集ボタンが２つある場合は、左側のボタンをまず配置
			if(btnLeftWidth!=0) {
				Slpc.put(springLayout, "N", editBtnLeft, "S", textPane_argName, 10);
				Slpc.put(springLayout, "W", editBtnLeft, "W", this, 20);
				add(editBtnLeft);
				// 左のボタンはeditBtnArrayに加えるとまずいことになる。editBtnArrayのボタン数＝サービス引数の数、というのを前提にしているためである。

				Slpc.put(springLayout, "N", editBtn, "N", editBtnLeft, 0);
				Slpc.put(springLayout, "W", editBtn, "E", editBtnLeft, 10);
				add(editBtn);
				editBtnArray.add(editBtn);
			}
			// １つだけの場合
			else {
				springLayout.putConstraint(SpringLayout.NORTH, editBtn, 10, SpringLayout.SOUTH, textPane_argName);
				springLayout.putConstraint(SpringLayout.WEST, editBtn, 20, SpringLayout.WEST, this);
				add(editBtn);
				editBtnArray.add(editBtn);
			}
			
			// 定義済か否かを記したラベル
			JLabel labelReserved = new JLabel();
			boolean reserved = service.inputPortReserved(i);
			if(reserved) {
				labelReserved.setText(japanese ? "定義済" : "Defined");
			}
			else {
				labelReserved.setText(japanese ? "！未定義" : "!Undefined");
			}
			springLayout.putConstraint(SpringLayout.NORTH, labelReserved, 0, SpringLayout.NORTH, editBtn);
			springLayout.putConstraint(SpringLayout.WEST, labelReserved, 20, SpringLayout.EAST, editBtn);
			add(labelReserved);
		}

		
		// アウトプット説明のテキストペイン
		JTextPane outDesc = new JTextPane();
		outDesc.setText(japanese ? "出力テーブル表示先\n（遷移先の「サービス出力テーブル表示」選択）" : "Service Output Destination\n(Select 'Service Output Table' on right page)");
		springLayout.putConstraint(SpringLayout.NORTH, outDesc, 60, SpringLayout.SOUTH, editBtnArray.get(editBtnArray.size()-1));	// 最後の引数の編集ボタンよりちょい下
		springLayout.putConstraint(SpringLayout.WEST, outDesc, 20, SpringLayout.WEST, this);
		add(outDesc);

		// アウトプット編集ボタン
		btnServOutEdit = new JButton("Select in Diagram");
		springLayout.putConstraint(SpringLayout.NORTH, btnServOutEdit, 10, SpringLayout.SOUTH, outDesc);
		springLayout.putConstraint(SpringLayout.WEST, btnServOutEdit, 20, SpringLayout.WEST, this);
		btnServOutEdit.addActionListener(this);
		btnServOutEdit.setActionCommand("サービス出力先選択");
		add(btnServOutEdit);

		
		
		// サービス削除ボタン
		buttonDeleteService = new JButton(japanese ? "削除" : "Delete");
		springLayout.putConstraint(SpringLayout.NORTH, buttonDeleteService, 60, SpringLayout.SOUTH, btnServOutEdit);
		springLayout.putConstraint(SpringLayout.WEST, buttonDeleteService, 20, SpringLayout.WEST, this);
		buttonDeleteService.addActionListener(this);
		buttonDeleteService.setActionCommand("サービス削除");
		add(buttonDeleteService);
		
		
		repaint();
		validate();
		MainFrame.getInstance().repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 各コンポーネントの再配置
	 * 
	 * NOTICE :
	 * 対象のサービスは現在のままで行う
	 */
	public void relocate() {
		relocate(service);
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEventハンドラ
	 * ・サービス引数編集ボタンクリック時に呼出
	 */
	public void actionPerformed(ActionEvent e) {
		// ボタンがクリックされたはず
		JButton sourceButton = (JButton)e.getSource();

		String cmd = e.getActionCommand();

		// 左と下のパネル
		Panel_TpEdit_Above leftPanel = Panel_TpEdit_Above.getInstance();
		Panel_TpEdit_Bottom bottomPanel = Panel_TpEdit_Bottom.getInstance();

		// サービス引数編集ボタンクリック
		if(sourceButton instanceof ButtonServArgEdit) {
			ButtonServArgEdit button = (ButtonServArgEdit)sourceButton;
			
			// このサービス引数の編集に入ることを記録しておく
			editingArgPort = button.servArgPort;
			editingArgNumber = button.serviceArgIndex;
			
			// 編集しようとしている引数の種類に応じて、適切な編集モードへ移行する
			if(cmd.equals("サービス引数編集ボタンクリック : モノ定数　直接入力")) {
				// メインフレームを使用不能に
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				// モノ定数編集フレーム表示　その中のコンポーネント配置
				FrameConstEdit.getInstance().setEnabled(true);
				FrameConstEdit.getInstance().setFocusable(true);
				FrameConstEdit.getInstance().setVisible(true);
				PanelConstEdit.getInstance().locateCompsForConstEdit(service, button.argIndex);
			}
			// INT型サービス引数の、左図からの選択
			else if(cmd.equals("サービス引数編集ボタンクリック : INT型サービス引数　選択")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_INT;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// VARCHAR型サービス引数の、左図からの選択
			else if(cmd.equals("サービス引数編集ボタンクリック : VARCHAR型サービス引数　選択")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_VARCHAR;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// DATETIME型サービス引数の、左図からの選択
			else if(cmd.equals("サービス引数編集ボタンクリック : DATETIME型サービス引数　選択")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATETIME;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// DATE型サービス引数の、左図からの選択
			else if(cmd.equals("サービス引数編集ボタンクリック : DATE型サービス引数　選択")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATE;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// TIME型サービス引数の、左図からの選択
			else if(cmd.equals("サービス引数編集ボタンクリック : TIME型サービス引数　選択")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_TIME;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			else if(cmd.equals("サービス引数編集ボタンクリック : 定数配列")) {
				// 他のサービス引数が未定義な関係で、定数配列の要素がいくつとなるべきか判断がつかなかった -> 編集不可なので何もしない
				int constArrayElementNum = service.getElementNumOfConstArrayArg(button.argIndex);
				if(constArrayElementNum==-1) {
					/*
					 * ◆未実装：ダイアログボックスを出したい
					 */
					return;
				}
				
				// ジェネレータフレームをフォーカス不能に
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				// 定数配列編集フレーム表示　その中のコンポーネント配置
				FrameConstArrayEdit.getInstance().setEnabled(true);
				FrameConstArrayEdit.getInstance().setFocusable(true);
				FrameConstArrayEdit.getInstance().setVisible(true);
				PanelConstArrayEdit.getInstance().locateCompsForConstArrayEdit(service, button.argIndex);
			}
			// フィールド選択 -> フィールド選択フレームを表示
			else if(cmd.equals("サービス引数編集ボタンクリック : フィールド選択")) {
				// ジェネレータフレームをフォーカス不能に
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				// フィールド選択型サービス引数編集フレーム表示　その中のコンポーネント配置
				FrameFieldSelect.getInstance().setEnabled(true);
				FrameFieldSelect.getInstance().setFocusable(true);
				FrameFieldSelect.getInstance().setVisible(true);
				PanelFieldSelect.getInstance().locateCompsForFieldSelectArgEdit(service, button.argIndex);
				FrameFieldSelect.getInstance().requestFocus();
			}
			// TFD -> モードを切り替え、TFD型引数を図から選択できるようにする
			else if(cmd.equals("サービス引数編集ボタンクリック : TFD")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_TFD;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
		}
		// サービス出力先選択
		else if(cmd.equals("サービス出力先選択")){
			// 出力先選択 -> モードを切り替え、出力先を選択できるようにする
			if(cmd.equals("サービス出力先選択")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_OUTPUT_TFD;
				removeAll();
				bottomPanel.removeAll();
			}
		}
		// サービス削除
		else if(cmd.equals("サービス削除")) {
			int confirm = JOptionPane.showConfirmDialog(this, "サービス「"+service.serviceName+"」を削除しますか？", "サービスの削除", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}

			/*
			 * サービスを削除することが決定
			 */
			
			service.removeService();
		}
		// エラー
		else {
			JOptionPane.showMessageDialog(this, "エラーが発生しました。");
			Debug.error("想定外のアクションコマンド", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}

		// 左パネルのコンポーネント再配置
		Panel_TpEdit_Above.getInstance().locateTppPanels();
		
		// 再描画・再検証
		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * Panel_TpEdit_Leftにおいて　TFD　型サービス引数が選択されたときに行う処理
	 */
	public void informSelectedTfdAsServiceArg(TfdOutputer selectedTo) {
		DataPort outPort = ((TransitionProcessPart)selectedTo).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;

		// 既に引数が定義されていたなら削除する
		service.removeArgIfDefined(editingArgNumber);
		
		// 編集中の遷移プロセスに、「選択されたTPPの出力->サービスの選択されている引数」というポートトランスミッション追加
		portTransManager.addPortTrans(outPort, editingArgPort);

		finishTppSelection();
	}

	
	
	
	
	public void informSelectedSaifAsServiceArg(PageElementSaif saif) {
		TransitionProcess editingTransProc = Panel_TpEdit_Above.getInstance().editingTp;
		DataPort saifOutputPort = saif.outputPePortsHashMap.get(editingTransProc);
		PortTransManager portTransManager = editingTransProc.portTransManager;

		// 既に引数が定義されていたなら削除する
		service.removeArgIfDefined(editingArgNumber);

		// 編集中の遷移プロセスに、「選択されたSAIFの出力->サービスの選択されている引数」というポートトランスミッション追加
		portTransManager.addPortTrans(saifOutputPort, editingArgPort);

		finishTppSelection();
	}
	
	
	
	
	public void informSelectedPeInputPort(PageElementPort port) {
		// 編集中の遷移プロセスに、「サービスの出力ポート -> 選択されたPageElementの入力ポート」というポートトランスミッション追加
		TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;
		transProc.portTransManager.addPortTrans(service.outputPort, port);

		finishTppSelection();
	}

	
	
	
	/*
	 * SUMMARY :
	 * サービスの入出力などとして左パネルからTPPやPEを選択するのが終わったときに実行
	 */
	public void finishTppSelection() {
		// 左側パネルのモードを通常に戻す
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;

		relocate(service);
		Panel_TpEdit_Bottom.getInstance().relocateComps();

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	

	
	/*
	 * PURPOSE :
	 * インスタンス取得
	 */
	public static PanelServiceArgEdit getInstance() {
		return PanelServiceArgEdit.obj;
	}
	
	
	
	
	public static void updateInstance(PanelServiceArgEdit newObject) {
		PanelServiceArgEdit.obj = newObject;
	}

}
