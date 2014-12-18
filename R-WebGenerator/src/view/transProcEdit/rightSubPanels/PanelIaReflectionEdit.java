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
import tpp.portTrans.PortTrans;
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
import view.transProcEdit.tableSelect.FrameTableSelect;
import view.transProcEdit.tableSelect.PanelTableSelect;
import view.webPageDefinition.*;
import webPage.*;





public class PanelIaReflectionEdit extends JPanel implements ActionListener {
	public TppIAReflection reflection;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	/*
	 * ◆インスタンス参照変数は最後！
	 */
	private static PanelIaReflectionEdit obj = new PanelIaReflectionEdit();
	
	
	
	
	private PanelIaReflectionEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Color.WHITE);
	}

	
	
	
	
	public void locateCompsForIaReflectionEdit(TppIAReflection iaReflection) {
		this.reflection = iaReflection;
		
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();

		int PADD_LEFT = 20;
		
		// テキストペイン
		JTextPane textPaneTitle = new JTextPane();
		textPaneTitle.setText(japanese ? "個人権限アサイン処理の定義" : "Individual Accessibility Assignment");
		Slpc.put(springLayout, "N", textPaneTitle, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneTitle, "W", this, PADD_LEFT);
		add(textPaneTitle);

		// テキストペイン（データテーブル名）
		JTextPane textPaneDtName = new JTextPane();
		if(reflection.dataTable==null) {
			textPaneDtName.setText(japanese ? "！データテーブル：未定義" : "! Data Table : Undefined");
		}
		else {
			textPaneDtName.setText((japanese ? "データテーブル：" : "Data Table : ") + reflection.dataTable.getTableName());
		}
		Slpc.put(springLayout, "N", textPaneDtName, "S", textPaneTitle, 40);
		Slpc.put(springLayout, "W", textPaneDtName, "W", this, PADD_LEFT);
		add(textPaneDtName);

		// データテーブルを選択するためのボタン
		JButton btnDtSelect = new JButton(japanese ? "データテーブルを選択" : "Select Data Table");
		btnDtSelect.addActionListener(this);
		btnDtSelect.setActionCommand("データテーブル選択");
		Slpc.put(springLayout, "N", btnDtSelect, "S", textPaneDtName, 10);
		Slpc.put(springLayout, "W", btnDtSelect, "W", this, PADD_LEFT);
		add(btnDtSelect);
		
		JTextPane textPaneDtTfd = new JTextPane();
		textPaneDtTfd.setText(japanese ? "データテーブル表データ" : "Tabular Form Data of Data Table");
		Slpc.put(springLayout, "N", textPaneDtTfd, "S", btnDtSelect, 20);
		Slpc.put(springLayout, "W", textPaneDtTfd, "W", this, PADD_LEFT);
		add(textPaneDtTfd);

		JButton btnDtTfdEdit = new JButton(japanese ? "左図から選択" : "Select in Diagram");
		btnDtTfdEdit.addActionListener(this);
		btnDtTfdEdit.setActionCommand("データテーブルTFD編集ボタンクリック");
		Slpc.put(springLayout, "N", btnDtTfdEdit, "S", textPaneDtTfd, 20);
		Slpc.put(springLayout, "W", btnDtTfdEdit, "W", this, PADD_LEFT);
		add(btnDtTfdEdit);

		// もしデータテーブルが未定義ならば、データテーブルTFDを選択できないようにボタンを無効化
		if(reflection.dataTable==null) {
			btnDtTfdEdit.setEnabled(false);
		}
		
		// データテーブルTFDが定義済か否かを示すテキストペイン
		JTextPane textPaneDtTfdDefined = new JTextPane();
		// データテーブルTFDが定義済か否かをチェック（定義済ならテーブルリーディングorCreateフォームリフレクションorCreateリフレクションが、未定義ならnullが返るはずである）
		TransitionProcessPart inputTpp = reflection.transProc.portTransManager.getInputTppToTppPortIfExists(reflection.getDataTableTfdInputPort());
		if(inputTpp==null) {
			textPaneDtTfdDefined.setText(japanese ? "！未定義" : "!Undefined");
		}
		else if(inputTpp instanceof TppTableReading || inputTpp instanceof TppCreateReflection || inputTpp instanceof TppCreateFormReflection) {
			textPaneDtTfdDefined.setText(japanese ? "定義済" : "Defined");
		}
		else {
			Debug.error("Iaリフレクションに渡すデータテーブルTFDとして、テーブルリーディングでもCreateリフレクションでもCreateフォームリフレクションでもないTPPが定義されています。");
			return;
		}
		Slpc.put(springLayout, "N", textPaneDtTfdDefined, "N", btnDtTfdEdit, 0);
		Slpc.put(springLayout, "W", textPaneDtTfdDefined, "E", btnDtTfdEdit, 20);
		add(textPaneDtTfdDefined);

		
		
		// テキストペイン（アカウントテーブル名）
		JTextPane textPaneAtName = new JTextPane();
		if(reflection.accountTable==null) {
			textPaneAtName.setText(japanese ? "！アカウントテーブル：未定義" : "! Account Table : Undefined");
		}
		else {
			textPaneAtName.setText((japanese ? "アカウントテーブル：" : "Account Table : ") + reflection.accountTable.getTableName());
		}
		Slpc.put(springLayout, "N", textPaneAtName, "S", textPaneDtTfdDefined, 20);
		Slpc.put(springLayout, "W", textPaneAtName, "W", this, PADD_LEFT);
		add(textPaneAtName);

		// アカウントテーブルを選択するためのボタン
		JButton btnAtSelect = new JButton(japanese ? "アカウントテーブルを選択" : "Select Account Table");
		btnAtSelect.addActionListener(this);
		btnAtSelect.setActionCommand("アカウントテーブル選択");
		Slpc.put(springLayout, "N", btnAtSelect, "S", textPaneAtName, 10);
		Slpc.put(springLayout, "W", btnAtSelect, "W", this, PADD_LEFT);
		add(btnAtSelect);
		
		JTextPane textPaneAtTfd = new JTextPane();
		textPaneAtTfd.setText(japanese ? "アカウントテーブル表データ" : "Tabular Form Data of Account Table");
		Slpc.put(springLayout, "N", textPaneAtTfd, "S", btnAtSelect, 20);
		Slpc.put(springLayout, "W", textPaneAtTfd, "W", this, PADD_LEFT);
		add(textPaneAtTfd);

		JButton btnAtTfdEdit = new JButton(japanese ? "左図から選択" : "Select in Diagram");
		btnAtTfdEdit.addActionListener(this);
		btnAtTfdEdit.setActionCommand("アカウントテーブルTFD編集ボタンクリック");
		Slpc.put(springLayout, "N", btnAtTfdEdit, "S", textPaneAtTfd, 20);
		Slpc.put(springLayout, "W", btnAtTfdEdit, "W", this, PADD_LEFT);
		add(btnAtTfdEdit);

		// もしアカウントテーブルが未定義ならば、アカウントテーブルTFDを選択できないようにボタンを無効化
		if(reflection.accountTable==null) {
			btnAtTfdEdit.setEnabled(false);
		}
		
		// アカウントテーブルTFDが定義済か否かを示すテキストペイン
		JTextPane textPaneAtTfdDefined = new JTextPane();
		// アカウントテーブルTFDが定義済か否かをチェック（定義済ならテーブルリーディングorCreateフォームリフレクションorCreateリフレクションが、未定義ならnullが返るはずである）
		TransitionProcessPart inputAtTfd = reflection.transProc.portTransManager.getInputTppToTppPortIfExists(reflection.getAccountTableTfdInputPort());
		if(inputAtTfd==null) {
			textPaneAtTfdDefined.setText(japanese ? "！未定義" : "!Undefined");
		}
		else if(inputAtTfd instanceof TppTableReading || inputAtTfd instanceof TppCreateReflection || inputAtTfd instanceof TppCreateFormReflection) {
			textPaneAtTfdDefined.setText(japanese ? "定義済" : "Defined");
		}
		else {
			Debug.error("Iaリフレクションに渡すアカウントテーブルTFDとして、テーブルリーディングでもCreateリフレクションでもCreateフォームリフレクションでもないTPPが定義されています。");
			return;
		}
		Slpc.put(springLayout, "N", textPaneAtTfdDefined, "N", btnAtTfdEdit, 0);
		Slpc.put(springLayout, "W", textPaneAtTfdDefined, "E", btnAtTfdEdit, 20);
		add(textPaneAtTfdDefined);

		
		
		// 削除ボタン
		JButton buttonRemoveIaReflection = new JButton(japanese ? "削除" : "Delete");
		buttonRemoveIaReflection.addActionListener(this);
		buttonRemoveIaReflection.setActionCommand("IAリフレクション削除");
		Slpc.put(springLayout, "N", buttonRemoveIaReflection, "S", btnAtTfdEdit, 40);
		Slpc.put(springLayout, "W", buttonRemoveIaReflection, "W", this, PADD_LEFT);
		add(buttonRemoveIaReflection);
		
		
		
		
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("データテーブル選択")) {
			FrameTableSelect.getInstance().setVisible(true);
			FrameTableSelect.getInstance().setEnabled(true);
			FrameTableSelect.getInstance().setFocusable(true);
			MainFrame.getInstance().setEnabled(false);
			MainFrame.getInstance().setFocusable(false);

			PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_SELECT_IA_REFLECTION_DT;
			PanelTableSelect.getInstance().locateDataTableButtons();
		}
		else if(cmd.equals("アカウントテーブル選択")) {
			FrameTableSelect.getInstance().setVisible(true);
			FrameTableSelect.getInstance().setEnabled(true);
			FrameTableSelect.getInstance().setFocusable(true);
			MainFrame.getInstance().setEnabled(false);
			MainFrame.getInstance().setFocusable(false);

			PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_SELECT_IA_REFLECTION_AT;
			PanelTableSelect.getInstance().locateAccountTableButtons();
		}
		else if(cmd.equals("データテーブルTFD編集ボタンクリック")) {
			this.locateCancelButton();
			Panel_TpEdit_Bottom.getInstance().removeAll();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD;
		}
		else if(cmd.equals("アカウントテーブルTFD編集ボタンクリック")) {
			this.locateCancelButton();
			Panel_TpEdit_Bottom.getInstance().removeAll();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD;
		}
		else if(cmd.equals("TFD選択のキャンセル")) {
			this.locateCompsForIaReflectionEdit(reflection);
			Panel_TpEdit_Bottom.getInstance().relocateComps();
			Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		}
		else if(cmd.equals("IAリフレクション削除")) {
			int confirm = JOptionPane.showConfirmDialog(this, "個人権限アサイン処理を削除します。よろしいですか？", "個人権限アサイン処理の削除", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}

			/*
			 * IAリフレクションを削除することが決定
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
	}

	
	

	public void informSelectedDataTable(DataTable dt) {
		// データテーブルを（再）設定
		reflection.dataTable = dt;

		// もしもデータテーブルTFDが定義してあるのであれば、それは破棄する（そのデータテーブルTFDからIAリフレクションの0番入力TPPポートへのポートトランスミッションを削除する）
		boolean remove = reflection.removeDataTableTfdInputPortTransIfExists();

		this.locateCompsForIaReflectionEdit(reflection);
		MainFrame.repaintAndValidate();
	}
	public void informSelectedAccountTable(AccountTable at) {
		// アカウントテーブルを（再）設定
		reflection.accountTable = at;

		// もしもアカウントテーブルTFDが定義してあるのであれば、それは破棄する（そのアカウントテーブルTFDからIAリフレクションの1番入力TPPポートへのポートトランスミッションを削除する）
		boolean remove = reflection.removeAccountTableTfdInputPortTransIfExists();

		this.locateCompsForIaReflectionEdit(reflection);
		MainFrame.repaintAndValidate();
	}
	public void informSelectedDtTfd(TfdOutputer outerTpp) {
		DataPort outPort = ((TransitionProcessPart)outerTpp).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;
		
		// もしもデータテーブルTFDが定義してあったのであれば、それは破棄する（そのデータテーブルTFDからIAリフレクションのデータテーブルTFD入力ポートへのポートトランスミッション削除）
		boolean remove = reflection.removeDataTableTfdInputPortTransIfExists();

		// 編集中の遷移プロセスに、「選択されたTPPの出力->IaリフレクションのデータテーブルTFD入力TPPポート」というポートトランスミッション追加
		portTransManager.addPortTrans(outPort, this.reflection.getDataTableTfdInputPort());

		// 左側パネルのモードを通常に戻し、本パネルや下部パネルも再配置する
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		this.locateCompsForIaReflectionEdit(this.reflection);
		Panel_TpEdit_Bottom.getInstance().relocateComps();

		MainFrame.repaintAndValidate();
	}
	public void informSelectedAtTfd(TfdOutputer outerTpp) {
		DataPort outPort = ((TransitionProcessPart)outerTpp).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;
		
		// もしもアカウントテーブルTFDが定義してあったのであれば、それは破棄する（そのアカウントテーブルTFDからIAリフレクションのアカウントテーブルTFD入力ポートへのポートトランスミッション削除）
		boolean remove = reflection.removeAccountTableTfdInputPortTransIfExists();

		// 編集中の遷移プロセスに、「選択されたTPPの出力->IaリフレクションのデータテーブルTFD入力TPPポート」というポートトランスミッション追加
		portTransManager.addPortTrans(outPort, this.reflection.getAccountTableTfdInputPort());

		// 左側パネルのモードを通常に戻し、本パネルや下部パネルも再配置する
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;
		this.locateCompsForIaReflectionEdit(this.reflection);
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
		Slpc.put(springLayout, "N", btnCancel, "N", this, 150);
		Slpc.put(springLayout, "W", btnCancel, "W", this, 20);
		add(btnCancel);

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	




	public static PanelIaReflectionEdit getInstance() {
		return PanelIaReflectionEdit.obj;
	}




	public static void updateInstance(PanelIaReflectionEdit newObject) {
		PanelIaReflectionEdit.obj = newObject;
	}
}
