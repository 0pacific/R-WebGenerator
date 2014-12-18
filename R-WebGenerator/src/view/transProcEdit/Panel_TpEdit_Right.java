package view.transProcEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.service.Service;
import utility.Slpc;
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import property.GeneratorProperty;

import mainFrame.MainFrame;
import utility.*;

/*
 * Singleton
 */
public class Panel_TpEdit_Right extends JPanel implements ListSelectionListener, ActionListener,Serializable {
	private JButton additionButton;

	public static final String[] serviceNameList = {
		GeneratorProperty.japanese()?"フィールド抽出":"Fields Extraction",
		GeneratorProperty.japanese()?"フィールド合計値計算":"Fields Summing",
		GeneratorProperty.japanese()?"表結合":"Join"
	};

	private JList serviceSelectJList;
	private JScrollPane serviceSelectScrollPane;

	private JTextArea serviceDescTa;


	public SerializableSpringLayout layout;
	
	/*
	 * インスタンスは最後！
	 */
	private static Panel_TpEdit_Right obj = new Panel_TpEdit_Right();

	

	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private Panel_TpEdit_Right() {
		setBackground(Color.LIGHT_GRAY);
		layout = new SerializableSpringLayout();
		setLayout(layout);
		
		// サービス選択リストと、それを包むスクロールペイン
		serviceSelectJList = new JList(serviceNameList);
		serviceSelectJList.addListSelectionListener(this);
		serviceSelectScrollPane = new JScrollPane();
		serviceSelectScrollPane.getViewport().setView(serviceSelectJList);
		serviceSelectScrollPane.setPreferredSize(new Dimension(150, 100));
		
		// サービス説明テキストエリア
		serviceDescTa = new JTextArea(4, 15);
		serviceDescTa.setLineWrap(true);
		
		// 追加実行ボタン
		additionButton = new JButton();
		additionButton.addActionListener(this);
	}

	
	
	
	
	
	
	
	
	

	
	
	
	

	
	
	
	
	/*
	 * PURPOSE :
	 * サービスを追加するための入力コンポーネントを配置する
	 */
	public void locateForService() {
		// 全コンポーネント除去
		removeAll();

		// JTextPane「サービスを選択して下さい」
		JTextPane jtp_service = new JTextPane();
		jtp_service.setText("サービスを選択して下さい");
		jtp_service.setEditable(false);
		jtp_service.setBounds(10, 30, 200, 20);
		add(jtp_service);

		// サービス名選択リスト（を包んだスクロールペイン）
		if(serviceSelectJList.getSelectedIndex()==-1) {
			serviceSelectJList.setSelectedIndex(0);
		}
		serviceSelectScrollPane.setBounds(10, 70, 150, 150);
		add(serviceSelectScrollPane);
		
		// サービス説明JTextArea
		String selectedServiceName = (String)serviceSelectJList.getSelectedValue();
		String selectedServiceDesc
			= Service.getInstanceByServiceName(
										Panel_TpEdit_Above.getInstance().getEditingTp(), selectedServiceName)
										.getDescription();
		serviceDescTa.setText(selectedServiceDesc);
		serviceDescTa.setBounds(10, 240, 250, 60);
		add(serviceDescTa);
		
		// 追加ボタンをサービス追加用にカスタマイズし、追加
		additionButton.setText("サービスを追加する");
		additionButton.setActionCommand("TPP追加 - Service");
		additionButton.setBounds(10, 320, 200, 30);
		add(additionButton);
		
		// 再描画・再検証
		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ListSelectionEventハンドラ
	 */
	public void valueChanged(ListSelectionEvent e) {
		// サービス選択JListの選択項目変更
		if(e.getSource()==serviceSelectJList) {
			locateForService();
		}
	}

	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("TPP追加 - Service")) {
			String serviceName = (String)serviceSelectJList.getSelectedValue();
			Panel_TpEdit_Above.getInstance().addServiceAndServicePanelByServiceName(serviceName);
		}
	}
	
	
	
	
	
	public static Panel_TpEdit_Right getInstance() {
		return Panel_TpEdit_Right.obj;
	}




	public static void updateInstance(Panel_TpEdit_Right newObject) {
		Panel_TpEdit_Right.obj = newObject;
	}
}
