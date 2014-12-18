package view.transProcEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import transition.TransitionProcess;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;
import view.transProcEdit.serviceSelect.PanelForServiceSelection;
import view.transProcEdit.tableSelect.FrameTableSelect;
import view.transProcEdit.tableSelect.PanelTableSelect;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;

import property.GeneratorProperty;

import mainFrame.MainFrame;
import utility.*;

/*
 * Singleton
 */
public class Panel_TpEdit_Bottom extends JPanel implements ActionListener,Serializable {
	private JComboBox tppTypeCombo;
	private static String[] tppTypes;
	public static String TPP_MENU_SERVICE;
	public static String TPP_MENU_TABLE_LOAD;
	public static String TPP_MENU_TABLE_UPDATE;
	public static String TPP_MENU_RECORD_CREATION;
	public static String TPP_MENU_RECORD_DELETION;
	public static String TPP_MENU_IA_ASSIGNMENT;
	private JButton additionButton;
	
	private JButton transEditButton;

	// シリアライズボタン
	ButtonSaveWork btnSaveWork;
	// デシリアライズボタン
	ButtonLoadWork btnLoadWork;

	public SerializableSpringLayout springLayout;
	
	/*
	 * インスタンスは最後！
	 */
	private static Panel_TpEdit_Bottom obj = new Panel_TpEdit_Bottom();


	
	
	

	private Panel_TpEdit_Bottom() {
		setBackground(Color.WHITE);

		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		boolean japanese = GeneratorProperty.japanese();
		Panel_TpEdit_Bottom.TPP_MENU_SERVICE = japanese ? "サービス" : "Service";
		Panel_TpEdit_Bottom.TPP_MENU_TABLE_LOAD = japanese ? "テーブル読出処理" : "Table Load";
		Panel_TpEdit_Bottom.TPP_MENU_TABLE_UPDATE = japanese ? "テーブル更新処理" : "Table Update";
		Panel_TpEdit_Bottom.TPP_MENU_RECORD_CREATION = japanese ? "レコード作成処理" : "Record Creation";
		Panel_TpEdit_Bottom.TPP_MENU_RECORD_DELETION = japanese ? "レコード削除処理" : "Record Deletion";
		Panel_TpEdit_Bottom.TPP_MENU_IA_ASSIGNMENT = japanese ? "個人権限アサイン処理" : "Individual Accessibility Assignment";
		Panel_TpEdit_Bottom.tppTypes = new String[]{
				TPP_MENU_SERVICE,
				TPP_MENU_TABLE_LOAD,
				TPP_MENU_TABLE_UPDATE, 
				TPP_MENU_RECORD_CREATION,
				TPP_MENU_RECORD_DELETION,
				TPP_MENU_IA_ASSIGNMENT
		};
		
		relocateComps();
	}

	

	
	
	/*
	 * PURPOSE :
	 * コンポーネント再配置
	 */
	public void relocateComps() {
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		tppTypeCombo = new JComboBox(Panel_TpEdit_Bottom.tppTypes);
		springLayout.putConstraint(SpringLayout.NORTH, tppTypeCombo, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tppTypeCombo, 10, SpringLayout.WEST, this);
		add(tppTypeCombo);

		additionButton = new JButton(japanese ? "を新たに追加する" : "Add");
		additionButton.addActionListener(this);
		additionButton.setActionCommand("TPP追加");
		springLayout.putConstraint(SpringLayout.NORTH, additionButton, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, additionButton, 0, SpringLayout.EAST, tppTypeCombo);
		add(additionButton);

		transEditButton = new JButton(japanese ? "Webページ・遷移権限定義画面へ" : "GO TO Web Page Definition");
		transEditButton.addActionListener(this);
		transEditButton.setActionCommand("遷移 : ページ遷移定義画面");
		springLayout.putConstraint(SpringLayout.NORTH, transEditButton, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, transEditButton, 30, SpringLayout.EAST, additionButton);
		add(transEditButton);

		// デシリアライズボタン
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "N", this, 10);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -10);
		add(btnLoadWork);
		
		// シリアライズボタン
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "S", btnLoadWork, 20);
		Slpc.put(springLayout, "W", btnSaveWork, "W", btnLoadWork, 0);
		add(btnSaveWork);

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		TransitionProcess editingTransProc = Panel_TpEdit_Above.getInstance().editingTp;
		String cmd = e.getActionCommand();
		
		// TPPの追加
		if(cmd.equals("TPP追加")) {
			String tppType = (String)tppTypeCombo.getSelectedItem();

			if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_SERVICE)) {
				FrameForServiceSelection.getInstance().setVisible(true);
				PanelForServiceSelection.getInstance().locateComps();
				MainFrame.getInstance().setEnabled(false);
				FrameForServiceSelection.getInstance().requestFocus();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_TABLE_LOAD)) {
				FrameTableSelect.getInstance().setVisible(true);
				FrameTableSelect.getInstance().setEnabled(true);
				FrameTableSelect.getInstance().setFocusable(true);
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_ADD_TABLE_READING;
				PanelTableSelect.getInstance().locateButtons();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_RECORD_CREATION)) {
				FrameTableSelect.getInstance().setVisible(true);
				FrameTableSelect.getInstance().setEnabled(true);
				FrameTableSelect.getInstance().setFocusable(true);
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_ADD_CREATE_REFLECTION;
				PanelTableSelect.getInstance().locateButtons();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_RECORD_DELETION)) {
				FrameTableSelect.getInstance().setVisible(true);
				FrameTableSelect.getInstance().setEnabled(true);
				FrameTableSelect.getInstance().setFocusable(true);
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				PanelTableSelect.getInstance().mode = PanelTableSelect.MODE_ADD_DELETE_REFLECTION;
				PanelTableSelect.getInstance().locateButtons();
			}
			else if(tppType.equals(Panel_TpEdit_Bottom.TPP_MENU_IA_ASSIGNMENT)) {
				// IAリフレクションを作成、編集中の遷移プロセスへ最後のTPPとして追加
				TppIAReflection iaReflection = new TppIAReflection(editingTransProc);
				editingTransProc.addTpp(iaReflection);

				// IAリフレクションパネルの作成を左パネルへ依頼
				Panel_TpEdit_Above.getInstance().addIaReflectionPanel(iaReflection, 150, 350);
			}
			// ◆以下、未実装  DBへの反映とかね
		}
		else if(cmd.equals("遷移 : ページ遷移定義画面")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
		else if(cmd.equals("入出力選択モードキャンセル")) {
			PanelServiceArgEdit.getInstance().finishTppSelection();
		}
		
		// TPPパネルを再配置
		Panel_TpEdit_Above.getInstance().locateTppPanels();

		MainFrame.repaintAndValidate();
	}
	
	
	

	public void locateIoSelectionCancelButton() {
		removeAll();
		
		JButton cancelBtn = new JButton(GeneratorProperty.japanese()?"キャンセル":"Cancel");
		Slpc.put(springLayout, "N", cancelBtn, "N", this, 20);
		Slpc.put(springLayout, "W", cancelBtn, "W", this, 20);
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("入出力選択モードキャンセル");
		add(cancelBtn);

		MainFrame.repaintAndValidate();
	}
	
	
	
	public static Panel_TpEdit_Bottom getInstance() {
		return Panel_TpEdit_Bottom.obj;
	}



	public static void updateInstance(Panel_TpEdit_Bottom newObject) {
		Panel_TpEdit_Bottom.obj = newObject;
	}
}
