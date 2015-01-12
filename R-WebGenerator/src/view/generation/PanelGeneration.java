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
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;

import account.AccountManager;
import property.GeneratorProperty;
import mainFrame.MainFrame;
import debug.Debug;
import executer.generation.GenerationExecuter;
import utility.*;
import utility.serialize.Serializer;





public class PanelGeneration extends JPanel implements ActionListener,Serializable {
	public SerializableSpringLayout springLayout;

	// 生成を実行するボタン
	JButton generateButton;
	
	//管理者用データの出力
	JButton adminDataGenerateButton;

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
		boolean japanese = GeneratorProperty.japanese();
		
		// 生成ボタン
		generateButton = new JButton(japanese?"Webアプリケーションを生成":"Generate Web Application");
		generateButton.addActionListener(this);
		generateButton.setActionCommand("生成実行");
		
		// 管理者用データを出力
		adminDataGenerateButton = new JButton(japanese? "管理者用データを出力" : "Generate the Data Admin Uses");
		adminDataGenerateButton.addActionListener(this);
		adminDataGenerateButton.setActionCommand("管理者用データ生成");

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
		
		springLayout.putConstraint(SpringLayout.NORTH, adminDataGenerateButton, 20, SpringLayout.SOUTH, generateButton);
		springLayout.putConstraint(SpringLayout.WEST, adminDataGenerateButton, 0, SpringLayout.WEST, generateButton);
		add(adminDataGenerateButton);

		springLayout.putConstraint(SpringLayout.NORTH, transBtnWebPageDef, 20, SpringLayout.SOUTH, adminDataGenerateButton);
		springLayout.putConstraint(SpringLayout.WEST, transBtnWebPageDef, 0, SpringLayout.WEST, adminDataGenerateButton);
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
		else if(cmd.equals("管理者用データ生成")){
			Debug.out("生成を試みます", getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName());

			boolean result = adminDataGeneration();
			if(result){
				String msg =
						japanese ?
								"管理者用データを生成しました。\n"
								:
									"Generation has been finished successfully.\n";
									JOptionPane.showMessageDialog(this, msg, japanese?"生成完了":"Success", JOptionPane.PLAIN_MESSAGE);



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
	
	
	
	public boolean adminDataGeneration(){
		String filePath = "src/generatedFiles/adminsData.txt";

		//生成データのモードを管理者用に変更
		AccountManager.getInstance().setAdmins();
		// 同名のファイルが存在しないか確認する。存在するなら上書きするか問いかけ、キャンセルされたらなにもしない
		File confirmFile = new File(filePath);
		if(confirmFile.exists() && confirmFile.isFile()) {
			int confirm = JOptionPane.showConfirmDialog(this, filePath+"は既に存在します。上書きしますか？", "ファイルの上書き保存", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {
				//編集モードを元に戻す
				AccountManager.getInstance().setDeveloping();
				return false;
			}
		}

		Serializer serializer = new Serializer();
		serializer.executeSerialization(filePath);

		//編集モードを元に戻す
		AccountManager.getInstance().setDeveloping();
		//JOptionPane.showMessageDialog(this, filePath+"に作業を保存しました。");
		return true;
	}

	
	
	
	
	
	
	public static PanelGeneration getInstance() {
		return PanelGeneration.obj;
	}


	public static void updateInstance(PanelGeneration newObject) {
		PanelGeneration.obj = newObject;
	}
}
