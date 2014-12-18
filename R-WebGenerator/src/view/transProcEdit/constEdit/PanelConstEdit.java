package view.transProcEdit.constEdit;

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
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;
import executer.generation.mysql.*;
import utility.*;





public class PanelConstEdit extends JPanel implements ActionListener {
	// モード
	public static final int MODE_INT = 0;
	public static final int MODE_STRING = 1;
	public int modeDataType;

	// 既に定義されている引数を編集しようとしているのか否か
	public boolean editing;
	
	// サービス
	public Service service;

	// 編集中のサービス引数の番号とタイプ
	public int argIndex;
	public String argType;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// 背景色
	public static Color BG_COLOR = Color.WHITE;
	
	// 定数を入力させるテキストフィールド
	JTextField valueTextField;

	// 編集実行ボタン、キャンセルボタン
	public JButton exeBtn;
	public JButton cancelBtn;

	private static PanelConstEdit obj = new PanelConstEdit();
	
	
	
	private PanelConstEdit() {
		setBackground(PanelConstEdit.BG_COLOR);

		// SpringLayout
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	

	public boolean locateCompsForConstEdit(Service service, int argIndex) {
		removeAll();
		
		this.service = service;
		this.argIndex = argIndex;
		this.argType = service.argTypes[argIndex];

		// 編集中の引数がINTモノ定数かSTRINGモノ定数かによって、モードを設定
		if(argType.equals(Service.IO_TYPE_CONST_INT)) {
			this.modeDataType = PanelConstEdit.MODE_INT;
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING)) {
			this.modeDataType = PanelConstEdit.MODE_STRING;
		}
		else {
			Debug.error("INTでもSTRINGでもないモノ定数を編集しようとしているようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		// 既にモノ定数が定義されていたか否か
		editing = service.argDefined(argIndex);
		
		// テキストフィールド追加
		valueTextField = new JTextField();
		valueTextField.setPreferredSize(new Dimension(150, 20));
		springLayout.putConstraint(SpringLayout.NORTH, valueTextField, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, valueTextField, 20, SpringLayout.WEST, this);
		add(valueTextField);
		// もし既に定義されていたモノ定数を編集するケースなら、現在の値を入れておく
		if(editing) {
			if(modeDataType==PanelConstEdit.MODE_INT) {
				TppConstInt currentConstInt = (TppConstInt)service.getArgTppIfExists(argIndex);
				int currentValue = currentConstInt.getValue();
				valueTextField.setText(Integer.toString(currentValue));
			}
			else if(modeDataType==PanelConstEdit.MODE_STRING) {
				TppConstString currentConstString = (TppConstString)service.getArgTppIfExists(argIndex);
				String currentValue = currentConstString.getValue();
				valueTextField.setText(currentValue);
			}
		}
		
		// 編集実行ボタン
		exeBtn = new JButton("　　　OK　　　");
		exeBtn.addActionListener(this);
		exeBtn.setActionCommand("編集実行");
		springLayout.putConstraint(SpringLayout.NORTH, exeBtn, 40, SpringLayout.SOUTH, valueTextField);
		springLayout.putConstraint(SpringLayout.WEST, exeBtn, 10, SpringLayout.WEST, this);
		add(exeBtn);

		// キャンセルボタン
		cancelBtn = new JButton(GeneratorProperty.japanese()?"キャンセル":"Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("キャンセル");
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 0, SpringLayout.NORTH, exeBtn);
		springLayout.putConstraint(SpringLayout.WEST, cancelBtn, 20, SpringLayout.EAST, exeBtn);
		add(cancelBtn);
		
		FrameConstEdit.repaintAndValidate();
		return true;
	}
	

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		// 編集実行ボタンがクリックされた -> 
		if(cmd.equals("編集実行")) {
			TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;

			// INTモノ定数
			if(this.argType==Service.IO_TYPE_CONST_INT) {
				try {
					int value = Integer.parseInt(valueTextField.getText());

					// 再定義の場合
					if(editing) {
						TppConstInt currentConstInt = (TppConstInt)service.getArgTppIfExists(argIndex);
						currentConstInt.setValue(value);

						// このモノ定数を定義しなければ定義できない引数は、定義済なら全て削除する（破綻する可能性があるため）
						service.removeFollowingArgsIfExist(argIndex);
					}
					// 初定義の場合
					else {
						TppConstInt newConstInt = new TppConstInt(transProc, value);

						// 遷移プロセスのTPP配列に、このモノ定数を追加（サービスのところに挿入し、サービス以降を右へ押し出す）
						service.transProc.addTpp(service.getTppo(), newConstInt);
						
						// ２つのポートを指定し、新たなポートトランスミッションを追加
						TppPort startPort = newConstInt.outputPort;
						TppPort endPort = service.getInputPort(argIndex);
						transProc.portTransManager.addPortTrans(startPort, endPort);
					}
				}
				catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "数値を半角で入力して下さい。");
					return;
				}
			}
			// STRINGモノ定数
			else if(this.argType==Service.IO_TYPE_CONST_STRING) {
				String value = valueTextField.getText();
				if(value.equals("")) {
					JOptionPane.showMessageDialog(this, "文字列を入力して下さい。");
					return;
				}

				// 再定義の場合
				if(editing) {
					TppConstString currentConstString = (TppConstString)service.getArgTppIfExists(argIndex);
					currentConstString.setValue(value);

					// このモノ定数を定義しなければ定義できない引数は、定義済なら全て削除する（破綻する可能性があるため）
					service.removeFollowingArgsIfExist(argIndex);
				}
				// 初定義の場合
				else {
					TppConstString newConstString = new TppConstString(transProc, value);

					// 遷移プロセスのTPP配列に、このモノ定数を追加（サービスのところに挿入し、サービス以降を右へ押し出す）
					service.transProc.addTpp(service.getTppo(), newConstString);
					
					// ２つのポートを指定し、新たなポートトランスミッションを追加
					TppPort startPort = newConstString.outputPort;
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
			Debug.error("不正なコマンドです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		FrameConstEdit.getInstance().setEnabled(false);
		FrameConstEdit.getInstance().setFocusable(false);
		FrameConstEdit.getInstance().setVisible(false);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().requestFocus();

		MainFrame.repaintAndValidate();
		FrameConstEdit.repaintAndValidate();

		// サービス引数編集パネルのコンポーネント再配置
		PanelServiceArgEdit.getInstance().relocate();
	}
	
	



	public static PanelConstEdit getInstance() {
		return PanelConstEdit.obj;
	}



	public static void updateInstance(PanelConstEdit newObject) {
		PanelConstEdit.obj = newObject;
	}
}
