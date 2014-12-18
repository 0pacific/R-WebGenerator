package view.generation;

import gui.*;
import gui.arrow.*;

import role.Role;
import role.RoleManager;
import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.*;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import executer.generation.GenerationExecuter;
import utility.*;





public class PanelGeneration extends JPanel implements ActionListener,Serializable {
	public SerializableSpringLayout springLayout;

	// 生成を実行するボタン
	JButton generateButton;

	// 他画面への遷移ボタン
	private JButton transBtnWebPageDef;
	
	/*
	 * インスタンスは最後！
	 */
	private static PanelGeneration obj = new PanelGeneration();

	private PanelGeneration() {
		setBackground(Color.WHITE);

		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		initButtons();
		locateComps();
	}

	
	
	
	public void initButtons() {
		// 生成ボタン
		generateButton = new JButton(GeneratorProperty.japanese()?"Webアプリケーションを生成":"Generate Web Application");
		generateButton.addActionListener(this);
		generateButton.setActionCommand("生成実行");

		// Webページ・遷移権限定義画面への遷移ボタン
		transBtnWebPageDef = new JButton(GeneratorProperty.japanese()?"Webページ・遷移権限定義画面へ":"Go To Web Page Definition");
		transBtnWebPageDef.addActionListener(this);
		transBtnWebPageDef.setActionCommand("遷移：Webページ定義画面");
	}
	
	
	
	
	public void locateComps() {
		removeAll();
		
		springLayout.putConstraint(SpringLayout.NORTH, generateButton, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, generateButton, 20, SpringLayout.WEST, this);
		add(generateButton);

		springLayout.putConstraint(SpringLayout.NORTH, transBtnWebPageDef, 20, SpringLayout.SOUTH, generateButton);
		springLayout.putConstraint(SpringLayout.WEST, transBtnWebPageDef, 0, SpringLayout.WEST, generateButton);
		add(transBtnWebPageDef);
	}
	

	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		// 生成
		if(cmd.equals("生成実行")) {
			Debug.out("生成を試みます。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

			GenerationExecuter genExe = GenerationExecuter.getInstance();
			boolean result = genExe.execute();
			if(result) {
				String message =
					japanese ?
					"正常にWebアプリケーションの生成を完了しました。\n" +
					"R-WebGenerator/src/generatedFiles/application/にアプリケーション、\n" +
					"R-WebGenerator/src/generatedFiles/sql/にデータベースのダンプがあります。\n" +
					"それぞれApacheへの配置、MySQL中のデータベース'hoge'への適用を行って下さい。"
					:
					"Generation has been finished successfully.\n" +
					"Application is on R-WebGenerator/src/generatedFiles/application/ and\n" +
					"Dump file of database is on R-WebGenerator/src/generatedFiles/sql/ .\n" +
					"Respectively, put in Apache, apply for MySQL database 'hoge'.";
				JOptionPane.showMessageDialog(this, message, japanese?"生成完了":"Success", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				String message = japanese ? "生成を中断しました。" : "Generation has been suspended.";
				JOptionPane.showMessageDialog(this, message, japanese?"生成中断":"Suspend", JOptionPane.PLAIN_MESSAGE);
			}
		}
		else if(cmd.equals("遷移：Webページ定義画面")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
	}
	
	
	
	
	public static PanelGeneration getInstance() {
		return PanelGeneration.obj;
	}


	public static void updateInstance(PanelGeneration newObject) {
		PanelGeneration.obj = newObject;
	}
}
