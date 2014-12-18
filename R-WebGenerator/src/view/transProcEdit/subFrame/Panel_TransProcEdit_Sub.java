package view.transProcEdit.subFrame;

import javax.swing.*;
import javax.swing.filechooser.*;

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
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.webPageDefinition.*;
import webPage.*;



public class Panel_TransProcEdit_Sub extends JPanel {
	public SerializableSpringLayout springLayout;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	public static Panel_TransProcEdit_Sub obj = new Panel_TransProcEdit_Sub();

	
	// サービスパネルクリック時に表示するサービス引数一覧パネルと、それを格納するスクロールペイン
	private PanelServiceArgEdit panelCsArgs = PanelServiceArgEdit.getInstance();
	private JScrollPane scrollPane_serviceEdit;



	private Panel_TransProcEdit_Sub() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

	
		setBackground(Color.WHITE);
	}


	
	
	
	/*
	 * PURPOSE :
	 * サービスの各引数の説明、編集ボタンを表示
	 * 
	 * NOTICE :
	 * 出力とその編集ボタンも表示するかもしれない
	 */
	public void showCsArgs(Service service) {
		// 全コンポーネント除去
		removeAll();

		// サービス引数選択パネルを包むスクロールペイン（いつでも配置できるようsetBounds()しておく）
		panelCsArgs.setPreferredSize(new Dimension(this.getWidth()-20, 1000));
		scrollPane_serviceEdit = new JScrollPane();
		scrollPane_serviceEdit.getViewport().add(panelCsArgs);
		scrollPane_serviceEdit.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane_serviceEdit, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane_serviceEdit, 0, SpringLayout.WEST, this);
		add(scrollPane_serviceEdit);
		
		// サービス引数一覧パネルのコンポーネント配置
		panelCsArgs.relocate(service);


		boolean japanese = GeneratorProperty.japanese();
		Frame_TransProcEdit_Sub.getInstance().setTitle(japanese ? "編集 - サービス" : "Configuration - Service");

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	
	
	
	
	
	public void displayPanelCreateReflectionEdit(TppCreateReflection reflection) {
		// 全コンポーネント除去
		removeAll();

		PanelCreateReflectionEdit panel = PanelCreateReflectionEdit.getInstance();
		panel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		Slpc.put(springLayout, "N", panel, "N", this, 0);
		Slpc.put(springLayout, "W", panel, "W", this, 0);
		add(panel);

		panel.locateCompsForCreateReflectionEdit(reflection);

		boolean japanese = GeneratorProperty.japanese();
		Frame_TransProcEdit_Sub.getInstance().setTitle(japanese ? "編集 - レコード作成処理" : "Configuration - Record Creation");
		
		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	
	
	
	public void displayPanelIaReflectionEdit(TppIAReflection reflection) {
		// 全コンポーネント除去
		removeAll();

		// スクロールペインの中に取り込むIAリフレクション編集パネルの縦幅
		int PANEL_HEIGHT = 750;
		
		PanelIaReflectionEdit panel = PanelIaReflectionEdit.getInstance();
		panel.locateCompsForIaReflectionEdit(reflection);
		panel.setPreferredSize(new Dimension(this.getWidth()-20, PANEL_HEIGHT));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(panel);
		scrollPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		Slpc.put(springLayout, "N", scrollPane, "N", this, 0);
		Slpc.put(springLayout, "W", scrollPane, "W", this, 0);
		add(scrollPane);

		boolean japanese = GeneratorProperty.japanese();
		Frame_TransProcEdit_Sub.getInstance().setTitle(japanese ? "編集 - 個人権限アサイン処理" : "Configuration - Individual Accessibility Assignment");

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	


	public static Panel_TransProcEdit_Sub getInstance() {
		return Panel_TransProcEdit_Sub.obj;
	}

}
