package view.authEdit;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import utility.*;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;


/*
 * NOTICE :
 * Singleton
 */
public class PanelAuthEditAbove extends JPanel implements ActionListener,Serializable {
	private Role role;
	private SuperTable table;

	public SerializableSpringLayout springLayout;
	
	private final int panelWidth = MainFrame.frameWidth - 20;
	private final int panelHeight = 2000;					// ◆とりあえずこの高さにしてある
	public static final int SCROLL_PANE_HEIGHT = 400;		// この上部パネルを取り込むスクロールペインの縦幅
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static PanelAuthEditAbove obj = new PanelAuthEditAbove();

	
	
	
	
	
	private PanelAuthEditAbove() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		setBackground(Color.WHITE);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * チェックボックス等を配置して権限定義画面を構成する
	 */
	public void relocate(Role role, SuperTable table) {
		boolean japanese = GeneratorProperty.japanese();
		
		setRoleAndTable(role, table);
		
		// 全コンポーネント除去
		removeAll();

		int PADDING_LEFT = 20;
		
		
		// ロールかテーブルが未選択の場合、メッセージだけ表示して終了
		if(role==null || table==null) {
			JTextPane textPaneMessage = new JTextPane();
			textPaneMessage.setText(japanese ? "ロールとテーブルを選択して下さい。" : "Select Role and Table.");
			Slpc.put(springLayout, "N", textPaneMessage, "N", this, 20);
			Slpc.put(springLayout, "W", textPaneMessage, "W", this, PADDING_LEFT);
			add(textPaneMessage);

			MainFrame.repaintAndValidate();
			return;
		}
		

		AuthorityToTable authToTable = AuthorityManager.getInstance().getTableAuth(role, table);
		if(authToTable instanceof AuthorityToDataTable) {
			relocateForEditingAuthToDataTable((AuthorityToDataTable)authToTable);
		}
		else if(authToTable instanceof AuthorityToAccountTable) {
			relocateForEditingAuthToAccountTable((AuthorityToAccountTable)authToTable);
		}
	}

	
	
	
	
	public void relocateForEditingAuthToAccountTable(AuthorityToAccountTable authToAt) {
		boolean japanese = GeneratorProperty.japanese();

		AccountTable accountTable = (AccountTable)authToAt.getTable();
		
		int PADDING_LEFT = 20;

		// for文の中でローカルに定義されたコンポーネントなど、後で参照できなくなるコンポーネントをSpringLayoutの基準に用いたいとき、これに格納しておいて参照する
		Component barometerComp = null;
		
		// テキストペイン
		JTextPane textPaneMessage = new JTextPane();
		String msg1 = japanese ?
						"ロール「" + role.getRoleName() + "」のテーブル「" + table.getTableName() + "」に対するアクセス権限を編集します。\n希望に合わせてチェックを入れて下さい。" :
						"Editing accessibility of role \"" + role.getRoleName() + "\" to table \"" + table.getTableName() + "\".";
		textPaneMessage.setText(msg1);
		Slpc.put(springLayout, "N", textPaneMessage, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneMessage, "W", this, PADDING_LEFT);
		add(textPaneMessage);


		for(int accountOrder=0; accountOrder<accountTable.getNormalRoleNum(); accountOrder++) {
			// アカウントオーナーロールとその名前
			NormalRole accountOwnerRole = accountTable.getNormalRole(accountOrder);
			String accountOwnerRoleName = accountOwnerRole.getRoleName();

			// 権限編集中のロールがこのアカウントに対して持っている権限セット
			AuthoritySet authorityToAccount = authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName);
			
			
			// テキストペイン（アカウント種別）...これの鉛直方向の相対位置の基準は、最初のアカウントか２つ目以降のアカウントかで異なる
			JTextPane textPaneAccount = new JTextPane();
			String msg2 = japanese ?
							(accountOrder+1)+". ロール「" + accountOwnerRoleName + "」のアカウントに対する権限を編集します。" :
							(accountOrder+1)+". Editing accessibility to account of role \"" + accountOwnerRoleName + "\".";
			textPaneAccount.setText(msg2);
			Slpc.put(springLayout, "W", textPaneAccount, "W", this, PADDING_LEFT);
			if(accountOrder==0)
				Slpc.put(springLayout, "N", textPaneAccount, "S", textPaneMessage, 20);
			else
				Slpc.put(springLayout, "N", textPaneAccount, "S", barometerComp, 20);
			add(textPaneAccount);

			
			// テキストペイン（RA）
			JTextPane textPaneRa = new JTextPane();
			String msg3 = japanese ?
							(accountOrder+1)+"-1. ロール権限の定義 ... 与えたい権限にチェックを入れて下さい。" :
							(accountOrder+1)+"-1. Role Accessibility Definition ... check data access you want to allow."; 
			textPaneRa.setText(msg3);
			Slpc.put(springLayout, "N", textPaneRa, "S", textPaneAccount, 20);
			Slpc.put(springLayout, "W", textPaneRa, "W", this, PADDING_LEFT);
			add(textPaneRa);

			
			// テキストペイン（Create）
			JTextPane textPaneCreate = new JTextPane();
			String msg4 = japanese ?
					(accountOrder+1)+"-1-1.ロール「"+accountOwnerRoleName+"」のアカウントを作成する権限（Create）" :
						(accountOrder+1)+"-1-1. Create account of role \""+accountOwnerRoleName+"\"";
			textPaneCreate.setText(msg4);
			Slpc.put(springLayout, "N", textPaneCreate, "S", textPaneRa, 10);
			Slpc.put(springLayout, "W", textPaneCreate, "W", this, PADDING_LEFT);
			add(textPaneCreate);

			
			// チェックボックス Create
			JCheckBox createCb = new JCheckBox("Create");
			createCb.addActionListener(this);
			createCb.setActionCommand("AccountTable Create " + accountOwnerRoleName);
			if(authorityToAccount.getCreate())
				createCb.setSelected(true);
			else
				createCb.setSelected(false);
			Slpc.put(springLayout, "N", createCb, "S", textPaneCreate, 5);
			Slpc.put(springLayout, "W", createCb, "W", this, PADDING_LEFT);
			add(createCb);

			
			// テキストペイン（Delete）
			JTextPane textPaneDelete = new JTextPane();
			String msg5 = japanese ?
					(accountOrder+1)+"-1-2.ロール「"+accountOwnerRoleName+"」の任意のアカウントを削除する権限（Delete）" :
						(accountOrder+1)+"-1-2. Delete other users' accounts";
			textPaneDelete.setText(msg5);
			Slpc.put(springLayout, "N", textPaneDelete, "S", createCb, 20);
			Slpc.put(springLayout, "W", textPaneDelete, "W", this, PADDING_LEFT);
			add(textPaneDelete);

			// チェックボックス Delete（ロール権限）
			JCheckBox deleteCb = new JCheckBox("Delete");
			deleteCb.addActionListener(this);
			deleteCb.setActionCommand("AccountTable RaDelete " + accountOwnerRoleName);
			if(authorityToAccount.getDelete())
				deleteCb.setSelected(true);
			else
				deleteCb.setSelected(false);
			Slpc.put(springLayout, "N", deleteCb, "S", textPaneDelete, 5);
			Slpc.put(springLayout, "W", deleteCb, "W", this, PADDING_LEFT);
			add(deleteCb);

			
			// テキストペイン（Read, Write, ExWrite）
			JTextPane textPaneFieldAuth = new JTextPane();
			String msg6 = japanese ?
					(accountOrder+1)+"-1-3.ロール「"+accountOwnerRoleName+"」の任意のアカウント中の、各フィールドに対する権限（Read, Write, ExclusiveWrite）" :
						(accountOrder+1)+"-1-3. Read, Write, ExclusiveWrite each field of other users' accounts;";
			textPaneFieldAuth.setText(msg6);
			Slpc.put(springLayout, "N", textPaneFieldAuth, "S", deleteCb, 20);
			Slpc.put(springLayout, "W", textPaneFieldAuth, "W", this, PADDING_LEFT);
			add(textPaneFieldAuth);

			
			// 各フィールドのRead, Write, ExWrite（ロール権限）
			// ◆ここのコードは個人権限の場合とかなり重複
			int fieldNum = table.getFieldNum();
			for(int i=0; i<fieldNum; i++) {
				Field field = table.getField(i);
				String fieldName = field.getFieldName();

				// このフィールドの部分の、上のコンポーネントからの距離
				int HEIGHT = 30*i + 20;
				
				// フィールド名ラベル
				JLabel fieldNameLabel = new JLabel(fieldName);
				int LABEL_WIDTH = 120;
				fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
				Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
				add(fieldNameLabel);

				JCheckBox checkBoxRaRead = new JCheckBox("Read");
				checkBoxRaRead.addActionListener(this);
				checkBoxRaRead.setActionCommand("AccountTable RaRead " + twoDigit(i) + " " + accountOwnerRoleName);
				// PASSWORD型フィールドの場合、Readはできない
				if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
					checkBoxRaRead.setSelected(false);
					checkBoxRaRead.setEnabled(false);
				}
				
				JCheckBox checkBoxRaWrite = new JCheckBox("Write");
				checkBoxRaWrite.addActionListener(this);
				checkBoxRaWrite.setActionCommand("AccountTable RaWrite " + twoDigit(i) + " " + accountOwnerRoleName);
				// ROLE_NAME型フィールドの場合、Writeはできない
				if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
					checkBoxRaWrite.setSelected(false);
					checkBoxRaWrite.setEnabled(false);
				}

				JCheckBox checkBoxRaExWrite = new JCheckBox("ExWrite");
				checkBoxRaExWrite.addActionListener(this);
				checkBoxRaExWrite.setActionCommand("AccountTable RaExWrite " + twoDigit(i) + " " + accountOwnerRoleName);
				// ROLE_NAME型フィールドの場合、ExWriteはできない
				if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
					checkBoxRaExWrite.setSelected(false);
					checkBoxRaExWrite.setEnabled(false);
				}


				// 現在の Read, Write, ExWrite権限に応じて、チェックしたり外したり
				if(authorityToAccount.getRaRead(i))
					checkBoxRaRead.setSelected(true);
				else
					checkBoxRaRead.setSelected(false);
				if(authorityToAccount.getRaWrite(i))
					checkBoxRaWrite.setSelected(true);
				else
					checkBoxRaWrite.setSelected(false);
				if(authorityToAccount.getRaExWrite(i))
					checkBoxRaExWrite.setSelected(true);
				else
					checkBoxRaExWrite.setSelected(false);

				
				// １フィールド分のチェックボックスついか
				Slpc.put(springLayout, "N", checkBoxRaRead, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxRaRead, "E", fieldNameLabel, 20);
				add(checkBoxRaRead);
				Slpc.put(springLayout, "N", checkBoxRaWrite, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxRaWrite, "E", checkBoxRaRead, 20);
				add(checkBoxRaWrite);
				Slpc.put(springLayout, "N", checkBoxRaExWrite, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxRaExWrite, "E", checkBoxRaWrite, 20);
				add(checkBoxRaExWrite);

				// 最後の場合は、フィールド名ラベルを記憶しておく（後で相対位置の基準にする）
				if(i==fieldNum-1) {
					barometerComp = fieldNameLabel;
				}
			}
			// フィールドが１つもなかった場合は、テキストペインを記憶しておく（後で相対位置の基準にする）
			if(fieldNum==0) {
				barometerComp = textPaneFieldAuth;
			}

			
			// Deleteと、各フィールドのRead, Write, ExWrite（個人権限）
			// 権限編集中のロールがこのアカウントのオーナーロールである場合のみ表示
			// ◆ここのコードはロール権限の場合とかなり重複
			if(role==accountOwnerRole) {
				// テキストペイン（IA-Delete）
				JTextPane textPaneIaDelete = new JTextPane();
				String msg7 = japanese ?
						(accountOrder+1)+"-2-1.自分のアカウントを削除する権限（Delete）" :
							(accountOrder+1)+"-2-1. Delete own account";
				textPaneIaDelete.setText(msg7);
				Slpc.put(springLayout, "N", textPaneIaDelete, "S", barometerComp, 20);
				Slpc.put(springLayout, "W", textPaneIaDelete, "W", this, PADDING_LEFT);
				add(textPaneIaDelete);

				// チェックボックス（IA-Delete）
				JCheckBox iaDeleteCb = new JCheckBox("Delete");
				iaDeleteCb.addActionListener(this);
				iaDeleteCb.setActionCommand("AccountTable IaDelete " + accountOwnerRoleName);
				if(authorityToAccount.getIaDelete())
					iaDeleteCb.setSelected(true);
				else
					iaDeleteCb.setSelected(false);
				Slpc.put(springLayout, "N", iaDeleteCb, "S", textPaneIaDelete, 0);
				Slpc.put(springLayout, "W", iaDeleteCb, "W", this, PADDING_LEFT);
				add(iaDeleteCb);


				// テキストペイン（IA-Read, IA-Write, IA-ExWrite）
				JTextPane textPaneFieldAuthIa = new JTextPane();
				String msg8 = japanese ?
						(accountOrder+1)+"-2-2.自分のアカウント中の、各フィールドに対する権限（Read, Write, ExclusiveWrite）" :
							(accountOrder+1)+"-2-2. Read, Write, ExclusiveWrite each field of own account";
				textPaneFieldAuthIa.setText(msg8);
				Slpc.put(springLayout, "N", textPaneFieldAuthIa, "S", iaDeleteCb, 20);
				Slpc.put(springLayout, "W", textPaneFieldAuthIa, "W", this, PADDING_LEFT);
				add(textPaneFieldAuthIa);

				// 各フィールドのRead, Write, ExWrite（個人権限）
				// ◆ここのコードはロール権限の場合とかなり重複
				for(int i=0; i<fieldNum; i++) {
					Field field = table.getField(i);
					String fieldName = field.getFieldName();

					// このフィールドの部分の、上のコンポーネントからの距離
					int HEIGHT = 30*i + 20;
					
					// フィールド名ラベル
					JLabel fieldNameLabel = new JLabel(fieldName);
					int LABEL_WIDTH = 120;
					fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
					Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
					add(fieldNameLabel);

					
					JCheckBox checkBoxIaRead = new JCheckBox("Read");
					checkBoxIaRead.addActionListener(this);
					checkBoxIaRead.setActionCommand("AccountTable IaRead " + twoDigit(i) + " " + accountOwnerRoleName);
					// PASSWORD型フィールドの場合、Readはできない
					if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
						checkBoxIaRead.setSelected(false);
						checkBoxIaRead.setEnabled(false);
					}

					JCheckBox checkBoxIaWrite = new JCheckBox("Write");
					checkBoxIaWrite.addActionListener(this);
					checkBoxIaWrite.setActionCommand("AccountTable IaWrite " + twoDigit(i) + " " + accountOwnerRoleName);

					JCheckBox checkBoxIaExWrite = new JCheckBox("ExWrite");
					checkBoxIaExWrite.addActionListener(this);
					checkBoxIaExWrite.setActionCommand("AccountTable IaExWrite " + twoDigit(i) + " " + accountOwnerRoleName);


					// 現在の Read, Write, ExWrite権限に応じて、チェックしたり外したり
					if(authorityToAccount.getIaRead(i))
						checkBoxIaRead.setSelected(true);
					else
						checkBoxIaRead.setSelected(false);
					if(authorityToAccount.getIaWrite(i))
						checkBoxIaWrite.setSelected(true);
					else
						checkBoxIaWrite.setSelected(false);
					if(authorityToAccount.getIaExWrite(i))
						checkBoxIaExWrite.setSelected(true);
					else
						checkBoxIaExWrite.setSelected(false);

					
					// １フィールド分のチェックボックスついか
					Slpc.put(springLayout, "N", checkBoxIaRead, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", checkBoxIaRead, "E", fieldNameLabel, 20);
					add(checkBoxIaRead);
					Slpc.put(springLayout, "N", checkBoxIaWrite, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", checkBoxIaWrite, "E", checkBoxIaRead, 20);
					add(checkBoxIaWrite);
					Slpc.put(springLayout, "N", checkBoxIaExWrite, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", checkBoxIaExWrite, "E", checkBoxIaWrite, 20);
					add(checkBoxIaExWrite);

					// 最後の場合は、フィールド名ラベルを記憶しておく（後で相対位置の基準にする）
					if(i==fieldNum-1) {
						barometerComp = fieldNameLabel;
					}
				}
				// フィールドが１つもなかった場合は、テキストペインを記憶しておく（後で相対位置の基準にする）
				if(fieldNum==0) {
					barometerComp = textPaneFieldAuthIa;
				}
			}
		}

		MainFrame.repaintAndValidate();
	}
	
	

	
	public void relocateForEditingAuthToDataTable(AuthorityToDataTable authToDt) {
		boolean japanese = GeneratorProperty.japanese();
		DataTable dataTable = (DataTable)authToDt.getTable();
		
		int PADDING_LEFT = 20;

		// for文の中でローカルに定義されたコンポーネントなど、後で参照できなくなるコンポーネントをSpringLayoutの基準に用いたいとき、これに格納しておいて参照する
		Component barometerComp = null;
		
		// テキストペイン
		JTextPane textPaneMessage = new JTextPane();
		String msg1 = japanese ?
				"ロール「" + role.getRoleName() + "」が持つ、テーブル「" + table.getTableName() + "」に対する操作権限を編集中です。\n希望に合わせてチェックを入れて下さい。" :
					"Editing accessibility of role \""+role.getRoleName()+"\" to table \"" + table.getTableName() + "\".";
		textPaneMessage.setText(msg1);
		Slpc.put(springLayout, "N", textPaneMessage, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneMessage, "W", this, PADDING_LEFT);
		add(textPaneMessage);


		// 権限編集中のロールがこのデータテーブルに対して持っている権限セット
		AuthoritySet authSetToDt = authToDt.authSet;
		
		
		// テキストペイン（RA）
		JTextPane textPaneRa = new JTextPane();
		String msg2 = japanese ?
				"1. ロール権限の定義 ... 与えたい権限にチェックを入れて下さい。" :
				"1. Role Accessibility Definition ... check data access you want to allow.";
		textPaneRa.setText(msg2);
		Slpc.put(springLayout, "N", textPaneRa, "S", textPaneMessage, 20);
		Slpc.put(springLayout, "W", textPaneRa, "W", this, PADDING_LEFT);
		add(textPaneRa);

		
		// テキストペイン（Create）
		JTextPane textPaneCreate = new JTextPane();
		String msg3 = japanese ? "1-1. レコード作成" : "1-1. Create Records";
		textPaneCreate.setText(msg3);
		Slpc.put(springLayout, "N", textPaneCreate, "S", textPaneRa, 10);
		Slpc.put(springLayout, "W", textPaneCreate, "W", this, PADDING_LEFT);
		add(textPaneCreate);

		
		// チェックボックス Create
		JCheckBox createCb = new JCheckBox("Create");
		createCb.addActionListener(this);
		createCb.setActionCommand("DataTable Create");
		if(authSetToDt.getCreate())
			createCb.setSelected(true);
		else
			createCb.setSelected(false);
		Slpc.put(springLayout, "N", createCb, "S", textPaneCreate, 5);
		Slpc.put(springLayout, "W", createCb, "W", this, PADDING_LEFT);
		add(createCb);

		
		// テキストペイン（Delete）
		JTextPane textPaneDelete = new JTextPane();
		textPaneDelete.setText(japanese ? "1-2.レコードの削除" : "1-2.Delete records");
		Slpc.put(springLayout, "N", textPaneDelete, "S", createCb, 20);
		Slpc.put(springLayout, "W", textPaneDelete, "W", this, PADDING_LEFT);
		add(textPaneDelete);

		// チェックボックス Delete（ロール権限）
		JCheckBox deleteCb = new JCheckBox("Delete");
		deleteCb.addActionListener(this);
		deleteCb.setActionCommand("DataTable RaDelete");
		if(authSetToDt.getDelete())
			deleteCb.setSelected(true);
		else
			deleteCb.setSelected(false);
		Slpc.put(springLayout, "N", deleteCb, "S", textPaneDelete, 5);
		Slpc.put(springLayout, "W", deleteCb, "W", this, PADDING_LEFT);
		add(deleteCb);

		
		// テキストペイン（Read, Write, ExWrite）
		JTextPane textPaneFieldAuth = new JTextPane();
		textPaneFieldAuth.setText(japanese ? "1-3.各フィールドに対する権限" : "1-3.Accessibility to each field");
		Slpc.put(springLayout, "N", textPaneFieldAuth, "S", deleteCb, 20);
		Slpc.put(springLayout, "W", textPaneFieldAuth, "W", this, PADDING_LEFT);
		add(textPaneFieldAuth);

		
		// 各フィールドのRead, Write, ExWrite（ロール権限）
		// ◆ここのコードは個人権限の場合とかなり重複
		int fieldNum = table.getFieldNum();
		for(int i=0; i<fieldNum; i++) {
			Field field = table.getField(i);
			String fieldName = field.getFieldName();

			// このフィールドの部分の、上のコンポーネントからの距離
			int HEIGHT = 30*i + 20;
			
			// フィールド名ラベル
			JLabel fieldNameLabel = new JLabel(fieldName);
			int LABEL_WIDTH = 70;
			fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
			Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
			add(fieldNameLabel);

			JCheckBox checkBoxRaRead = new JCheckBox(japanese ? "Read（読出）" : "Read");
			checkBoxRaRead.addActionListener(this);
			checkBoxRaRead.setActionCommand("DataTable RaRead " + twoDigit(i));
			// PASSWORD型フィールドの場合、Readはできない
			if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
				checkBoxRaRead.setSelected(false);
				checkBoxRaRead.setEnabled(false);
			}
			
			JCheckBox checkBoxRaWrite = new JCheckBox(japanese ? "Write（更新）" : "Write");
			checkBoxRaWrite.addActionListener(this);
			checkBoxRaWrite.setActionCommand("DataTable RaWrite " + twoDigit(i));

			JCheckBox checkBoxRaExWrite = new JCheckBox(japanese ? "ExclusiveWrite（排他的更新）" : "ExclusiveWrite");
			checkBoxRaExWrite.addActionListener(this);
			checkBoxRaExWrite.setActionCommand("DataTable RaExWrite " + twoDigit(i));


			// 現在の Read, Write, ExWrite権限に応じて、チェックしたり外したり
			if(authSetToDt.getRaRead(i))
				checkBoxRaRead.setSelected(true);
			else
				checkBoxRaRead.setSelected(false);
			if(authSetToDt.getRaWrite(i))
				checkBoxRaWrite.setSelected(true);
			else
				checkBoxRaWrite.setSelected(false);
			if(authSetToDt.getRaExWrite(i))
				checkBoxRaExWrite.setSelected(true);
			else
				checkBoxRaExWrite.setSelected(false);

			
			// １フィールド分のチェックボックスついか
			Slpc.put(springLayout, "N", checkBoxRaRead, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", checkBoxRaRead, "E", fieldNameLabel, 20);
			add(checkBoxRaRead);
			Slpc.put(springLayout, "N", checkBoxRaWrite, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", checkBoxRaWrite, "E", checkBoxRaRead, 20);
			add(checkBoxRaWrite);
			Slpc.put(springLayout, "N", checkBoxRaExWrite, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", checkBoxRaExWrite, "E", checkBoxRaWrite, 20);
			add(checkBoxRaExWrite);

			// 最後の場合は、フィールド名ラベルを記憶しておく（後で相対位置の基準にする）
			if(i==fieldNum-1) {
				barometerComp = fieldNameLabel;
			}
		}
		// フィールドが１つもなかった場合は、テキストペインを記憶しておく（後で相対位置の基準にする）
		if(fieldNum==0) {
			barometerComp = textPaneFieldAuth;
		}


		
		// IA-Definedか否かを切り替えるチェックボックス
		JCheckBox iaSwitchCb = new JCheckBox();
		if(role instanceof GuestRole) {
			// ゲストロールの場合 -> IA-Definedにはできないので、チェックボックスはチェックなしの状態で使用不能にする
			iaSwitchCb.setText(japanese ? "2. 個人権限の定義 ... ゲストロールには定義できません。" : "2. Individual Accessibility ... Can't be defined for Guest Role");
			iaSwitchCb.setSelected(false);
			iaSwitchCb.setEnabled(false);
		} else {
			iaSwitchCb.setText(japanese ? "2. 個人権限の定義 ... ロールに個人権限を持たせたい場合はチェックして下さい。" : "2. Individual Accessibility ... Check if you want to define");
			iaSwitchCb.setEnabled(true);

			if(authSetToDt.haveIa())
				iaSwitchCb.setSelected(true);
			else
				iaSwitchCb.setSelected(false);
		}
		iaSwitchCb.addActionListener(this);
		iaSwitchCb.setActionCommand("DataTable iaSwitch");
		Slpc.put(springLayout, "N", iaSwitchCb, "S", barometerComp, 30);
		Slpc.put(springLayout, "W", iaSwitchCb, "W", this, PADDING_LEFT);
		add(iaSwitchCb);

		// 相対位置の基準として、このチェックボックスを記憶しておく（以降のコンポーネントがこのチェックボックスからの相対位置をSpringLayoutを用いて指定できるように）
		barometerComp = iaSwitchCb;
		
		
		
		// Deleteと、各フィールドのRead, Write, ExWrite（個人権限）
		// 権限編集中のロールがこのデータテーブルに対しIA-Definedである場合のみ表示
		// ◆ここのコードはロール権限の場合とかなり重複
		if(authSetToDt.haveIa()) {
			// テキストペイン（IA-Delete）
			JTextPane textPaneIaDelete = new JTextPane();
			textPaneIaDelete.setText(japanese ? "2-1.自分に対応したレコードを削除" : "2-1.Delete own records");
			Slpc.put(springLayout, "N", textPaneIaDelete, "S", iaSwitchCb, 20);
			Slpc.put(springLayout, "W", textPaneIaDelete, "W", this, PADDING_LEFT);
			add(textPaneIaDelete);

			// チェックボックス（IA-Delete）
			JCheckBox iaDeleteCb = new JCheckBox("Delete");
			iaDeleteCb.addActionListener(this);
			iaDeleteCb.setActionCommand("DataTable IaDelete");
			if(authSetToDt.getIaDelete())
				iaDeleteCb.setSelected(true);
			else
				iaDeleteCb.setSelected(false);
			Slpc.put(springLayout, "N", iaDeleteCb, "S", textPaneIaDelete, 0);
			Slpc.put(springLayout, "W", iaDeleteCb, "W", this, PADDING_LEFT);
			add(iaDeleteCb);


			// テキストペイン（IA-Read, IA-Write, IA-ExWrite）
			JTextPane textPaneFieldAuthIa = new JTextPane();
			textPaneFieldAuthIa.setText(japanese ? "2-2.自分に対応したレコード中の、各フィールドに対する権限" : "2-2.Accessibility to each field of own records");
			Slpc.put(springLayout, "N", textPaneFieldAuthIa, "S", iaDeleteCb, 20);
			Slpc.put(springLayout, "W", textPaneFieldAuthIa, "W", this, PADDING_LEFT);
			add(textPaneFieldAuthIa);

			// 各フィールドのRead, Write, ExWrite（個人権限）
			// ◆ここのコードはロール権限の場合とかなり重複
			for(int i=0; i<fieldNum; i++) {
				Field field = table.getField(i);
				String fieldName = field.getFieldName();

				// このフィールドの部分の、上のコンポーネントからの距離
				int HEIGHT = 30*i + 20;
				
				// フィールド名ラベル
				JLabel fieldNameLabel = new JLabel(fieldName);
				int LABEL_WIDTH = 70;
				fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
				Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
				add(fieldNameLabel);

				
				JCheckBox checkBoxIaRead = new JCheckBox(japanese ? "Read（読出）" : "Read");
				checkBoxIaRead.addActionListener(this);
				checkBoxIaRead.setActionCommand("DataTable IaRead " + twoDigit(i));
				// PASSWORD型フィールドの場合、Readはできない
				if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
					checkBoxIaRead.setSelected(false);
					checkBoxIaRead.setEnabled(false);
				}

				JCheckBox checkBoxIaWrite = new JCheckBox(japanese ? "Write（更新）" : "Write");
				checkBoxIaWrite.addActionListener(this);
				checkBoxIaWrite.setActionCommand("DataTable IaWrite " + twoDigit(i));

				JCheckBox checkBoxIaExWrite = new JCheckBox(japanese ? "ExclusiveWrite（排他的更新）" : "ExclusiveWrite");
				checkBoxIaExWrite.addActionListener(this);
				checkBoxIaExWrite.setActionCommand("DataTable IaExWrite " + twoDigit(i));


				// 現在の Read, Write, ExWrite権限に応じて、チェックしたり外したり
				if(authSetToDt.getIaRead(i))
					checkBoxIaRead.setSelected(true);
				else
					checkBoxIaRead.setSelected(false);
				if(authSetToDt.getIaWrite(i))
					checkBoxIaWrite.setSelected(true);
				else
					checkBoxIaWrite.setSelected(false);
				if(authSetToDt.getIaExWrite(i))
					checkBoxIaExWrite.setSelected(true);
				else
					checkBoxIaExWrite.setSelected(false);

				
				// １フィールド分のチェックボックスついか
				Slpc.put(springLayout, "N", checkBoxIaRead, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxIaRead, "E", fieldNameLabel, 20);
				add(checkBoxIaRead);
				Slpc.put(springLayout, "N", checkBoxIaWrite, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxIaWrite, "E", checkBoxIaRead, 20);
				add(checkBoxIaWrite);
				Slpc.put(springLayout, "N", checkBoxIaExWrite, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxIaExWrite, "E", checkBoxIaWrite, 20);
				add(checkBoxIaExWrite);

				// 最後の場合は、フィールド名ラベルを記憶しておく（後で相対位置の基準にする）
				if(i==fieldNum-1) {
					barometerComp = fieldNameLabel;
				}
			}
			// フィールドが１つもなかった場合は、テキストペインを記憶しておく（後で相対位置の基準にする）
			if(fieldNum==0) {
				barometerComp = textPaneFieldAuthIa;
			}
		}

		
		// データテーブルに対しIA-Definedなロールを調べる
		ArrayList<Role> iaHavingRoles = AuthorityManager.getInstance().getIaDefinedRolesOfDataTable((DataTable)table);
		
		// IA-Definedなロールがいない -> ここで終了
		if(iaHavingRoles.size()==0) {
			MainFrame.repaintAndValidate();
			return;
		}


		/*
		 * 以下、IA-Definedなロールが１つでもいる場合
		 */

		
	
		// ラベル（IAアサイン権限）
		JLabel iaAssignAuthLabel = new JLabel(japanese ? "3. 個人権限アサイン権限 ... どのロールに個人権限をアサインできるようにしますか。" : "3. Indivisual Accessibility Assignment Authority ... Can assign Individual Accessibility to checked roles");
		Slpc.put(springLayout, "W", iaAssignAuthLabel, "W", this, PADDING_LEFT);
		Slpc.put(springLayout, "N", iaAssignAuthLabel, "S", barometerComp, 20);
		add(iaAssignAuthLabel);


		// IA-Definedな各ロールについて処理
		for(int i=0; i<iaHavingRoles.size(); i++) {
			Role iaHavingRole = iaHavingRoles.get(i);

			// このIA-Definedロールの名前を付したチェックボックス
			JCheckBox checkBox = new JCheckBox(iaHavingRole.getRoleName());

			// ActionCommand..."iaAssignAuth ロール名"
			// "iaAssignAuth 学生", "iaAssignAuth 講師"といった感じになる
			checkBox.addActionListener(this);
			String cmd = "iaAssignAuth " + iaHavingRole.getRoleName();
			checkBox.setActionCommand(cmd);

			
			// このチェックボックスに付されたロールに対し、現在編集中のロールがIAアサイン権限を持つならば、チェックしておく（持たないならチェック外す）
			boolean assignable = authSetToDt.checkAssignable(iaHavingRole);
			if(assignable)
				checkBox.setSelected(true);
			else
				checkBox.setSelected(false);

			
			// チェックボックス配置
			Slpc.put(springLayout,	"N",	checkBox,	"S",	iaAssignAuthLabel,	30*i+20);
			Slpc.put(springLayout,	"W",	checkBox,	"W",	this, 				PADDING_LEFT);
			add(checkBox);
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	/*
	 * PURPOSE :
	 * ActionEventハンドラ
	 */
	public void actionPerformed(ActionEvent e) {
		AuthorityToTable authToTable = AuthorityManager.getInstance().getTableAuth(role, table);

		AuthorityToDataTable authToDt = null;
		if(authToTable instanceof AuthorityToDataTable) {
			authToDt = (AuthorityToDataTable)authToTable;
		}
		AuthorityToAccountTable authToAt = null;
		if(authToTable instanceof AuthorityToAccountTable) {
			authToAt = (AuthorityToAccountTable)authToTable;
		}

		
		String cmd = e.getActionCommand();
		JCheckBox sourceCheckBox = (JCheckBox)e.getSource();
		boolean sourceChecked = sourceCheckBox.isSelected();
		
		if(cmd.startsWith("DataTable Create")) {
			authToDt.authSet.setCreate(sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaDelete")) {
			authToDt.authSet.setRaDelete(sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaRead ")) {	// "DataTable RaRead xx"	xxは２桁のオフセット
			int fieldOffset = twoDigit(cmd.substring("DataTable RaRead ".length(), "DataTable RaRead ".length()+2));
			authToDt.authSet.setRaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaWrite ")) {	// "DataTable RaWrite xx"	xxは２桁のオフセット
			int fieldOffset = twoDigit(cmd.substring("DataTable RaWrite ".length(), "DataTable RaWrite ".length()+2));
			authToDt.authSet.setRaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaExWrite ")) {	// "DataTable RaExWrite xx"	xxは２桁のオフセット
			int fieldOffset = twoDigit(cmd.substring("DataTable RaExWrite ".length(), "DataTable RaExWrite ".length()+2));
			authToDt.authSet.setRaExWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.equals("DataTable iaSwitch")) {
			authToDt.authSet.setHavingIa(sourceChecked);

			// IAが追加・除去されたことをAuthorityManagerに知らせる
			if(sourceChecked) {
				AuthorityManager.getInstance().informIaAddition(role, table);
			}
			else if(!sourceChecked) {
				AuthorityManager.getInstance().informIaDeletion(role, table);
			}

			// IAを定義する部分の全コンポーネントを追加/削除したいので、パネルのコンポーネントを再配置
			relocate(role, table);
		}
		else if(cmd.startsWith("DataTable IaDelete")) {
			authToDt.authSet.setIaDelete(sourceChecked);
		}
		else if(cmd.startsWith("DataTable IaRead ")) {	// "DataTable IaRead xx"	xxは２桁のオフセット
			int fieldOffset = twoDigit(cmd.substring("DataTable IaRead ".length(), "DataTable IaRead ".length()+2));
			authToDt.authSet.setIaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable IaWrite ")) {	// "DataTable IaWrite xx"	xxは２桁のオフセット
			int fieldOffset = twoDigit(cmd.substring("DataTable IaWrite ".length(), "DataTable IaWrite ".length()+2));
			authToDt.authSet.setIaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable IaExWrite ")) {	// "DataTable IaExWrite xx"	xxは２桁のオフセット
			int fieldOffset = twoDigit(cmd.substring("DataTable IaExWrite ".length(), "DataTable IaExWrite ".length()+2));
			authToDt.authSet.setIaExWrite(fieldOffset, sourceChecked);
		}
		/*
		 * あるロールに対するIAアサイン権限のON,OFF（データテーブルの場合のみ設定が必要）
		 * コマンドは　"iaAssignAuth " + そのロールの名前
		 */
		else if(cmd.startsWith("iaAssignAuth ")) {
			// IAアサイン対象のロールを取得
			String assignTargetRoleName = cmd.substring("iaAssignAuth ".length());
			Role assignTargetRole = RoleManager.getInstance().getRoleByName(assignTargetRoleName);

			Debug.out("ロール「" + role.getRoleName() + "」の、ロール「" + assignTargetRole.getRoleName() + "」に対するIAアサイン権限を変更します。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			
			// チェックボックスのON,OFFに応じ、そのロールへのIAアサイン権限をON,OFF
			if(sourceChecked) {
				authToDt.authSet.enableIaAssignAuth(assignTargetRole);
				Debug.out("ONにしました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else {
				authToDt.authSet.disableIaAssignAuth(assignTargetRole);
				Debug.out("OFFにしました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		else if(cmd.startsWith("AccountTable Create ")) {
			String accountOwnerRoleName = cmd.substring("AccountTable Create ".length());
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setCreate(sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaDelete ")) {
			String accountOwnerRoleName = cmd.substring("AccountTable RaDelete ".length());
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaDelete(sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaRead ")) {	// "AccountTable RaRead xx yyyyy"	xxは２桁のオフセット、yyyyyはアカウントオーナーロール名
			int fieldOffset = twoDigit(cmd.substring("AccountTable RaRead ".length(), "AccountTable RaRead ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable RaRead ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaWrite ")) {	// "AccountTable RaWrite xx yyyyy"	xxは２桁のオフセット、yyyyyはアカウントオーナーロール名
			int fieldOffset = twoDigit(cmd.substring("AccountTable RaWrite ".length(), "AccountTable RaWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable RaWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaExWrite ")) {	// "AccountTable RaExWrite xx yyyyy"	xxは２桁のオフセット、yyyyyはアカウントオーナーロール名
			int fieldOffset = twoDigit(cmd.substring("AccountTable RaExWrite ".length(), "AccountTable RaExWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable RaExWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaExWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaDelete ")) {
			String accountOwnerRoleName = cmd.substring("AccountTable IaDelete ".length());
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaDelete(sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaRead ")) {	// "AccountTable IaRead xx yyyyy"	xxは２桁のオフセット、yyyyyはアカウントオーナーロール名
			int fieldOffset = twoDigit(cmd.substring("AccountTable IaRead ".length(), "AccountTable IaRead ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable IaRead ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaWrite ")) {	// "AccountTable IaWrite xx yyyyy"	xxは２桁のオフセット、yyyyyはアカウントオーナーロール名
			int fieldOffset = twoDigit(cmd.substring("AccountTable IaWrite ".length(), "AccountTable IaWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable IaWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaExWrite ")) {	// "AccountTable IaExWrite xx yyyyy"	xxは２桁のオフセット、yyyyyはアカウントオーナーロール名
			int fieldOffset = twoDigit(cmd.substring("AccountTable IaExWrite ".length(), "AccountTable IaExWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable IaExWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaExWrite(fieldOffset, sourceChecked);
		}
		else {
			// ◆ERROR : 不正なコマンド
			Debug.informError();
			Debug.error("Panel_AuthEdit_Bottom actionPerformed() : 不正なActionCommandを受信しました : " + cmd, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void setRoleAndTable(Role role, SuperTable table) {
		this.role = role;
		this.table = table;
	}

	
	
	
	
	public int getPanelWidth() {
		return panelWidth;
	}

	
	

	public String twoDigit(int number) {
		if(number<0 || number>=100) {
			Debug.informError();
			Debug.error("不正な値です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		else if(number>=0 && number<10) {
			return "0" + Integer.toString(number);
		}
		return Integer.toString(number);
	}

	
	
	
	public int twoDigit(String twoDigitStr) {
		if(twoDigitStr.length()!=2) {
			Debug.informError();
			Debug.error("不正な値です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		else if(twoDigitStr.startsWith("0")) {
			return Integer.parseInt(twoDigitStr.substring(1));
		}
		return Integer.parseInt(twoDigitStr);
	}
	
	
	
	public int getPanelHeight() {
		return panelHeight;
	}
	
	
	
	
	
	public static PanelAuthEditAbove getInstance() {
		return PanelAuthEditAbove.obj;
	}




	public static void updateInstance(PanelAuthEditAbove newObject) {
		PanelAuthEditAbove.obj = newObject;
	}

}
