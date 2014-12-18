package view.transProcEdit.rightSubPanels;

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
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.portTrans.PortTransManager;
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
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;
import view.webPageDefinition.*;
import webPage.*;





public class PanelCreateReflectionEdit extends JPanel implements ActionListener {
	public TppCreateReflection reflection;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	/*
	 * ◆インスタンス参照変数は最後！
	 */
	private static PanelCreateReflectionEdit obj = new PanelCreateReflectionEdit();
	
	
	
	
	private PanelCreateReflectionEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Color.WHITE);
	}

	
	
	
	
	public void locateCompsForCreateReflectionEdit(TppCreateReflection createReflection) {
		boolean japanese = GeneratorProperty.japanese();
		this.reflection = createReflection;
		
		removeAll();

		int PADD_LEFT = 20;
		
		// テキストエリア
		JTextPane textPaneTitle = new JTextPane();
		textPaneTitle.setFont(new Font("Serif", Font.PLAIN, 17));
		textPaneTitle.setText(japanese ? "レコード作成処理の定義" : "Record Creation Configuration");
		Slpc.put(springLayout, "N", textPaneTitle, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneTitle, "W", this, PADD_LEFT);
		add(textPaneTitle);

		// テキストペイン（テーブル名）
		JTextPane textPaneTableName = new JTextPane();
		textPaneTableName.setFont(new Font("Serif", Font.PLAIN, 14));
		textPaneTableName.setText(japanese ? "1. テーブル\n\n    " + reflection.table.getTableName() : "1. Table\n\n    " + reflection.table.getTableName());
		Slpc.put(springLayout, "N", textPaneTableName, "S", textPaneTitle, 40);
		Slpc.put(springLayout, "W", textPaneTableName, "W", this, PADD_LEFT);
		add(textPaneTableName);

		/*
		 * ◆未実装：テーブルを変更する編集ボタンも置きたい
		 */

		JTextPane textPaneTfd = new JTextPane();
		textPaneTfd.setFont(new Font("Serif", Font.PLAIN, 14));
		String text = japanese ? "2. 表データ : " : "2. Tabular Data : ";
		// TFDが定義済か否かをチェック（定義済ならTFD出力型TPPが、未定義ならnullが返るはずである）
		TransitionProcessPart inputTpp = reflection.transProc.portTransManager.getInputTppToTppPortIfExists(reflection.getTfdInputPort());
		if(inputTpp==null)
			text += japanese ? "未定義" : "Undefined";
		else if(inputTpp instanceof TfdOutputer)
			text += japanese ? "定義済" : "Defined";
		else {
			Debug.error("Createリフレクションに渡すTFDとして、TFD出力型でないTPPが定義されています。");
			Debug.informError();
			return;
		}
		textPaneTfd.setText(text);
		Slpc.put(springLayout, "N", textPaneTfd, "S", textPaneTableName, 40);
		Slpc.put(springLayout, "W", textPaneTfd, "W", this, PADD_LEFT);
		add(textPaneTfd);

		
		
		JButton btnTfdEdit = new JButton(GeneratorProperty.japanese()?"左図から出力元を選択":"Select in Diagram");
		btnTfdEdit.addActionListener(this);
		btnTfdEdit.setActionCommand("TFD編集ボタンクリック");
		Slpc.put(springLayout, "N", btnTfdEdit, "S", textPaneTfd, 10);
		Slpc.put(springLayout, "W", btnTfdEdit, "W", this, PADD_LEFT+20);
		add(btnTfdEdit);
		
		
		// 削除ボタン
		JButton buttonRemoveCreateReflection = new JButton(japanese?"削除":"Delete");
		buttonRemoveCreateReflection.addActionListener(this);
		buttonRemoveCreateReflection.setActionCommand("Createリフレクション削除");
		Slpc.put(springLayout, "N", buttonRemoveCreateReflection, "S", btnTfdEdit, 40);
		Slpc.put(springLayout, "W", buttonRemoveCreateReflection, "W", this, PADD_LEFT);
		add(buttonRemoveCreateReflection);
		
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		if(cmd.equals("TFD編集ボタンクリック")) {
			this.locateCancelButton();
			Panel_TpEdit_Bottom.getInstance().removeAll();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD;

			
		}
		else if(cmd.equals("TFD選択のキャンセル")) {
			this.locateCompsForCreateReflectionEdit(reflection);
			Panel_TpEdit_Bottom.getInstance().relocateComps();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		}
		else if(cmd.equals("Createリフレクション削除")) {
			int confirm = JOptionPane.showConfirmDialog(this, japanese?"レコード作成処理を削除しますか？":"Delete this create reflection?", japanese?"レコード作成処理の削除":"Deletion of create reflection", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}

			/*
			 * Createリフレクションを削除することが決定
			 */
			
			reflection.removeFromTransProc();
		}
		else {
			JOptionPane.showMessageDialog(this, "エラーが発生しました。");
			Debug.error("想定外のアクションコマンドです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}

		Panel_TpEdit_Above.getInstance().locateTppPanels();

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}

	
	

	/*
	 * PURPOSE :
	 * Panel_TpEdit_Leftにおいて　TFD出力型TPPが選択されたときに行う処理
	 */
	public void informSelectedTfdForCreateReflection(TfdOutputer outerTpp) {
		DataPort outPort = ((TransitionProcessPart)outerTpp).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;
		
		// 編集中の遷移プロセスに、「選択されたTPPの出力->Createリフレクションの入力TPPポート（１つだけある）」というポートトランスミッション追加
		portTransManager.addPortTrans(outPort, this.reflection.getTfdInputPort());

		// 左側パネルのモードを通常に戻し、本パネルや下部パネルも再配置する
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		this.locateCompsForCreateReflectionEdit(this.reflection);
		Panel_TpEdit_Bottom.getInstance().relocateComps();

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * 全コンポーネントを除去し、TFD選択中の状態を取り消すキャンセルボタンのみを配置
	 * TFD選択モードに移るときに実行
	 */
	public void locateCancelButton() {
		removeAll();
		
		JButton btnCancel = new JButton(GeneratorProperty.japanese()?"キャンセル":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("TFD選択のキャンセル");
		Slpc.put(springLayout, "N", btnCancel, "N", this, 20);
		Slpc.put(springLayout, "W", btnCancel, "W", this, 20);
		add(btnCancel);

		MainFrame.repaintAndValidate();
	}
	




	public static PanelCreateReflectionEdit getInstance() {
		return PanelCreateReflectionEdit.obj;
	}
	
	
	
	
	
	public static void updateInstance(PanelCreateReflectionEdit newObject) {
		PanelCreateReflectionEdit.obj = newObject;
	}

}
