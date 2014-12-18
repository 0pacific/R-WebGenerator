package view.transProcEdit.constArrayEdit;

import javax.swing.*;

import mainFrame.MainFrame;

import authority.AuthorityManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

import debug.Debug;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import transition.Transition;
import transition.TransitionProcess;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.constEdit.PanelConstEdit;
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;
import executer.generation.mysql.*;
import utility.*;





public class PanelConstArrayEdit extends JPanel implements ActionListener {
	// モード
	public static final int MODE_INT_ARRAY = 0;
	public static final int MODE_STRING_ARRAY = 1;
	public int modeDataType;

	// フレーム上での位置
	public static final int posX = 0;
	public static final int posY = 0;

	// サイズ
	public static final int panelWidth = FrameConstArrayEdit.frameWidth;
	public static final int panelHeight = FrameConstArrayEdit.frameHeight;

	// サービス
	public Service service;

	// 既に定義されている引数を編集しようとしているのか否か
	public boolean editing;

	// 編集中のサービス引数の番号とタイプ
	public int argIndex;
	public String argType;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// 背景色
	public static Color BG_COLOR = Color.WHITE;
	
	// 定数配列の要素を入力してもらうテキストフィールドの配列
	ArrayList<JTextField> textBoxArray;

	// 編集実行ボタン、キャンセルボタン
	public JButton exeBtn;
	public JButton cancelBtn;

	private static PanelConstArrayEdit obj = new PanelConstArrayEdit();
	
	
	
	private PanelConstArrayEdit() {
		setBackground(PanelConstArrayEdit.BG_COLOR);

		// SpringLayout
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	

	public boolean locateCompsForConstArrayEdit(Service service, int argIndex) {
		removeAll();
		
		this.service = service;
		this.argIndex = argIndex;
		this.argType = service.argTypes[argIndex];

		// 編集中の引数がINT定数配列かSTRING定数配列かによって、モードを設定
		if(argType.equals(Service.IO_TYPE_CONST_INT_ARRAY)) {
			this.modeDataType = PanelConstArrayEdit.MODE_INT_ARRAY;
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING_ARRAY)) {
			this.modeDataType = PanelConstArrayEdit.MODE_STRING_ARRAY;
		}
		else {
			Debug.error("INT定数配列でもSTRING定数配列でもない定数配列を編集しようとしているようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		
		// 定数配列の要素数
		int constArrayElementNum = service.getElementNumOfConstArrayArg(argIndex);

		// 左余白
		int PADD_LEFT = 20;

		
		// テキストペイン
		JTextPane textPaneDesc = new JTextPane();
		if(modeDataType==PanelConstArrayEdit.MODE_INT_ARRAY) {
			String text = constArrayElementNum + (GeneratorProperty.japanese()?"つの数値をそれぞれ入力して下さい。":"integers need to be input.");
			textPaneDesc.setText(text);
		}
		else if(modeDataType==PanelConstArrayEdit.MODE_STRING_ARRAY) {
			String text = constArrayElementNum + (GeneratorProperty.japanese()?"つの文字列をそれぞれ入力して下さい。":"integers need to be input.");
			textPaneDesc.setText(text);
		}
		Slpc.put(springLayout, "N", textPaneDesc, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneDesc, "W", this, PADD_LEFT);
		add(textPaneDesc);

		
		// 既に定数配列が定義されていたか否か
		editing = service.argDefined(argIndex);
		
		
		// 定数配列の要素数だけテキストフィールドを作って配置
		textBoxArray = new ArrayList<JTextField>();
		for(int i=0; i<constArrayElementNum; i++) {
			JTextField textField = new JTextField();
			textBoxArray.add(textField);
			
			textField.setPreferredSize(new Dimension(100, 20));
			Slpc.put(springLayout, "N", textField, "S", textPaneDesc, 20+(i*30));
			Slpc.put(springLayout, "W", textField, "W", this, PADD_LEFT);
			add(textField);

			// 定義済の定数配列を編集するケースでは、値を入れておく
			if(editing) {
				if(modeDataType==PanelConstArrayEdit.MODE_INT_ARRAY) {
					TppConstArrayInt currentIntArray = (TppConstArrayInt)service.getArgTppIfExists(argIndex);
					int value = currentIntArray.getIntValue(i);
					textField.setText(Integer.toString(value));
				}
				else if(modeDataType==PanelConstArrayEdit.MODE_STRING_ARRAY) {
					TppConstArrayString currentStringArray = (TppConstArrayString)service.getArgTppIfExists(argIndex);
					String value = currentStringArray.getStringValue(i);
					textField.setText(value);
				}
			}
		}

		
		// 編集実行ボタン（最後のテキストフィールドのすぐ下に配置）
		exeBtn = new JButton("　　　　OK　　　　");
		exeBtn.addActionListener(this);
		exeBtn.setActionCommand("編集実行");
		springLayout.putConstraint(SpringLayout.NORTH, exeBtn, 40, SpringLayout.SOUTH, textBoxArray.get(constArrayElementNum-1));
		springLayout.putConstraint(SpringLayout.WEST, exeBtn, 10, SpringLayout.WEST, PanelConstArrayEdit.getInstance());
		add(exeBtn);

		// キャンセルボタン
		cancelBtn = new JButton(GeneratorProperty.japanese()?"キャンセル":"Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("キャンセル");
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 0, SpringLayout.NORTH, exeBtn);
		springLayout.putConstraint(SpringLayout.WEST, cancelBtn, 20, SpringLayout.EAST, exeBtn);
		add(cancelBtn);

		MainFrame.repaintAndValidate();
		FrameConstArrayEdit.repaintAndValidate();

		return true;
	}
	

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		// 編集実行ボタンがクリックされた -> 
		if(cmd.equals("編集実行")) {
			TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;

			
			// 各テキストフィールドの値を取得して配列に格納
			ArrayList<String> inputValueArray = new ArrayList<String>();
			for(int i=0; i<textBoxArray.size(); i++) {
				String value = textBoxArray.get(i).getText();
				inputValueArray.add(value);
			}


			// INT定数配列
			if(this.argType==Service.IO_TYPE_CONST_INT_ARRAY) {
				int[] intValueArray = new int[inputValueArray.size()];
				for(int i=0; i<inputValueArray.size(); i++) {
					try {
						intValueArray[i] = Integer.parseInt(inputValueArray.get(i));
					}
					catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(this, "全ての欄に数値を半角で入力して下さい。");
						return;
					}
				}

				// 再定義の場合
				if(editing) {
					TppConstArrayInt currentIntArray = (TppConstArrayInt)service.getArgTppIfExists(argIndex);
					currentIntArray.setValueArrayByIntArray(intValueArray);

					// この定数配列を定義しなければ定義できない引数は、定義済なら全て削除する（破綻する可能性があるため）
					service.removeFollowingArgsIfExist(argIndex);
				}
				// 初定義の場合
				else {
					TppConstArrayInt newIntArray = new TppConstArrayInt(transProc, intValueArray);

					// 遷移プロセスのTPP配列にこの定数配列を追加（サービスのところに挿入し、サービス以降を右へ押し出す）
					service.transProc.addTpp(service.getTppo(), newIntArray);
					
					// ２つのポートを指定し、新たなポートトランスミッションを追加
					TppPort startPort = newIntArray.outputPort;
					TppPort endPort = service.getInputPort(argIndex);
					transProc.portTransManager.addPortTrans(startPort, endPort);
				}
			}
			// STRING定数配列
			else if(this.argType==Service.IO_TYPE_CONST_STRING_ARRAY) {
				String[] stringValueArray = new String[inputValueArray.size()];
				for(int i=0; i<inputValueArray.size(); i++) {
					if(inputValueArray.get(i).equals("")) {
						JOptionPane.showMessageDialog(this, "全ての欄に文字列を入力して下さい。");
						return;
					}
					stringValueArray[i] = inputValueArray.get(i);
				}

				// 再定義の場合
				if(editing) {
					TppConstArrayString currentStringArray = (TppConstArrayString)service.getArgTppIfExists(argIndex);
					currentStringArray.setValueArrayByStringArray(stringValueArray);

					// この定数配列を定義しなければ定義できない引数は、定義済なら全て削除する（破綻する可能性があるため）
					service.removeFollowingArgsIfExist(argIndex);
				}
				// 初定義の場合
				else {
					TppConstArrayString newStringArray = new TppConstArrayString(transProc, stringValueArray);

					// 遷移プロセスのTPP配列にこの定数配列を追加（サービスのところに挿入し、サービス以降を右へ押し出す）
					service.transProc.addTpp(service.getTppo(), newStringArray);
					
					// ２つのポートを指定し、新たなポートトランスミッションを追加
					TppPort startPort = newStringArray.outputPort;
					TppPort endPort = service.getInputPort(argIndex);
					transProc.portTransManager.addPortTrans(startPort, endPort);
				}
			}
			else {
				Debug.error("tppがINT定数配列でもSTRING定数配列でもないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
		}
		else if(cmd.equals("キャンセル")) {
		}
		else {
			Debug.error("不正なコマンド", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		
		// フィールド選択フレームを非表示に、ジェネレータフレームをフォーカス可能に
		FrameConstArrayEdit.getInstance().setEnabled(false);
		FrameConstArrayEdit.getInstance().setFocusable(false);
		FrameConstArrayEdit.getInstance().setVisible(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();

		// ジェネレータフレーム再描画・再検証
		MainFrame.getInstance().repaintAndValidate();
		
		// サービス引数選択パネルのコンポーネント再配置
		PanelServiceArgEdit.getInstance().relocate();
	}
	
	



	public static PanelConstArrayEdit getInstance() {
		return PanelConstArrayEdit.obj;
	}





	public static void updateInstance(PanelConstArrayEdit newObject) {
		PanelConstArrayEdit.obj = newObject;
	}

}
