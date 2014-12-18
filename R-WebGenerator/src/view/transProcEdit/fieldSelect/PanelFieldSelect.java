package view.transProcEdit.fieldSelect;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import property.GeneratorProperty;

import debug.Debug;	

import mainFrame.MainFrame;


import table.SuperTable;
import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import utility.*;

/*
 * SUMMARY :
 * フィールド選択型サービス引数編集パネル
 * 
 * NOTICE :
 * Singleton
 */
public class PanelFieldSelect extends JPanel implements ActionListener {
	// フレーム上での位置
	public static final int posX = 0;
	public static final int posY = 0;

	// サイズ
	public static final int panelWidth = FrameFieldSelect.frameWidth;
	public static final int panelHeight = FrameFieldSelect.frameHeight;

	// SpringLayout
	SerializableSpringLayout layout;

	// 編集対象のサービス
	public Service service;
	
	// サービスの第何引数を編集しているのか
	// （もちろんフィールド選択型の引数でなければならない）
	public int argIndex;

	// 各フィールドを表すチェックボックス、そのサイズ、間隔
	public ArrayList<JCheckBox> fieldCheckBoxes;
	public static final int CB_WIDTH = 100;
	public static final int CB_HEIGHT = 50;
	public static final int CB_SPACING = 20;
	
	// 編集実行ボタン、キャンセルボタン
	public JButton exeBtn;
	public JButton cancelBtn;
	
	/*
	 * インスタンスは最後！
	 */
	private static PanelFieldSelect obj = new PanelFieldSelect();

	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private PanelFieldSelect() {
		// SpringLayoutを使用
		layout = new SerializableSpringLayout();
		setLayout(layout);
	}

	
	
	
	
	public boolean locateCompsForFieldSelectArgEdit(Service service, int argIndex) {
		//Debug.out("サービス「"+service.serviceName+"」の第"+argIndex+"引数（0始まり）を編集します（フィールド選択型サービス引数です）", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

		removeAll();
		
		this.service = service;
		this.argIndex = argIndex;
		TppPort argPort = service.getInputPort(argIndex);
		
		PortTransManager portTransManager = service.transProc.portTransManager;
		portTransManager.varDump();
		
		
		// TFD出力型TPPを受け取るサービス引数ポート（入力TPPポート）取得
		int tarArgIndex = service.hmFldSel.get(new Integer(argIndex)).intValue();
		TppPort tfdArgPort = service.getInputPort(tarArgIndex);

		// TFD出力型TPPの、出力TFDのフィールド数を算出
		TfdOutputer tfdOutputer = portTransManager.getInputTfdOutputerTppIfExists(tfdArgPort);
		int fieldNum = tfdOutputer.getOutFieldNum();

		// TFD出力型TPPを取得
		PortTrans trans = portTransManager.getOnePortTransByEndPortIfExists(tfdArgPort);
		TfdOutputer tfdArg = portTransManager.getInputTfdOutputerTppIfExists(tfdArgPort);
		
		// 各フィールドに対応したチェックボックス
		fieldCheckBoxes = new ArrayList<JCheckBox>();
		for(int i=0; i<fieldNum; i++) {
			JCheckBox cb = new JCheckBox();

			// フィールド名をチェックボックス脇に表示
			String fieldName = tfdArg.getOutputFieldWebNames().get(i);
			cb.setText(fieldName);

			// 一番上のチェックボックス -> フィールド選択パネルからの相対位置指定
			if(i==0) {
				layout.putConstraint(SpringLayout.NORTH, cb, 20, SpringLayout.NORTH, PanelFieldSelect.getInstance());
			}
			// 二番目以降のチェックボックス -> 上のチェックボックスからの相対位置指定
			else {
				layout.putConstraint(SpringLayout.NORTH, cb, 20, SpringLayout.SOUTH, fieldCheckBoxes.get(i-1));
			}
			layout.putConstraint(SpringLayout.WEST, cb, 20, SpringLayout.WEST, PanelFieldSelect.getInstance());

			// フィールド選択パネルへのチェックボックス追加
			add(cb);
			
			// インスタンス変数へのチェックボックス追加
			fieldCheckBoxes.add(cb);
		}

		// 編集実行ボタン
		exeBtn = new JButton(GeneratorProperty.japanese()?"以上のようにフィールドを選択":" OK ");
		exeBtn.addActionListener(this);
		exeBtn.setActionCommand("編集実行");
		layout.putConstraint(SpringLayout.NORTH, exeBtn, 40, SpringLayout.SOUTH, fieldCheckBoxes.get(fieldNum-1));
		layout.putConstraint(SpringLayout.WEST, exeBtn, 10, SpringLayout.WEST, PanelFieldSelect.getInstance());
		add(exeBtn);

		// キャンセルボタン
		cancelBtn = new JButton(GeneratorProperty.japanese()?"キャンセル":"Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("キャンセル");
		layout.putConstraint(SpringLayout.NORTH, cancelBtn, 0, SpringLayout.NORTH, exeBtn);
		layout.putConstraint(SpringLayout.WEST, cancelBtn, 20, SpringLayout.EAST, exeBtn);
		add(cancelBtn);

		repaint();
		validate();
		
		return true;
	}
	

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		// 編集実行ボタンがクリックされた -> INT配列型引数を表す、TPPを作成 -> 遷移プロセス中に、そのTPPから、編集中の引数ポートへの、ポートトランスミッション追加
		if(cmd.equals("編集実行")) {
			ArrayList<Integer> checkedIndexAl = new ArrayList<Integer>();
			
			for(int i=0; i<fieldCheckBoxes.size(); i++) {
				JCheckBox cb = fieldCheckBoxes.get(i);
				boolean checked = cb.isSelected();
				if(checked) {
					checkedIndexAl.add(new Integer(i));
				}
			}

			int[] checkedIndex = new int[checkedIndexAl.size()];
			for(int i=0; i<checkedIndexAl.size(); i++) {
				checkedIndex[i] = checkedIndexAl.get(i);
			}

			// INT定数配列TPPを作成、遷移プロセスのTPP配列に追加（サービスのところに挿入し、サービスを右へ押しやる。末尾に追加するとTPPの実行順序がおかしくなる。一応他クラスで調整するんだけど）
			TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;
			TppConstArrayInt tpp = new TppConstArrayInt(transProc, checkedIndex);
			transProc.addTpp(service.getTppo(), tpp);
			
			// INT定数配列TPPの、出力ポート
			TppPort startPort = tpp.outputPort;

			// 編集中の、フィールド選択型サービス引数ポート
			TppPort endPort = service.getInputPort(argIndex);

			// ２つのポートを指定し、新たなポートトランスミッションを追加
			transProc.portTransManager.addPortTrans(startPort, endPort);
		}
		else if(cmd.equals("キャンセル")) {
		}
			
		// フィールド選択フレームを非表示に、ジェネレータフレームをフォーカス可能に
		FrameFieldSelect.getInstance().setEnabled(false);
		FrameFieldSelect.getInstance().setFocusable(false);
		FrameFieldSelect.getInstance().setVisible(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();

		// ジェネレータフレーム再描画・再検証
		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
		
		// サービス引数選択パネルのコンポーネント再配置
		PanelServiceArgEdit.getInstance().relocate();
	}
	
	
	

	
	public static PanelFieldSelect getInstance() {
		return PanelFieldSelect.obj;
	}





	public static void updateInstance(PanelFieldSelect newObject) {
		PanelFieldSelect.obj = newObject;
	}
}
