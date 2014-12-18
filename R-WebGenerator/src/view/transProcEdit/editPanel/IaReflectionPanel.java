package view.transProcEdit.editPanel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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
import view.webPageDefinition.*;
import webPage.*;





public class IaReflectionPanel extends EditPanel {
	public TppIAReflection reflection;

	// パネル幅定数
	public static final int PANEL_WIDTH = 150;
	public static final int PANEL_HEIGHT = 100;

	// インスタンス変数としてのパネル幅
	public int panelWidth;
	public int panelHeight;

	// データテーブル名を記載したテキストペイン
	JTextPane textPaneDtAtNames;
	
	// 背景色
	public static Color bgColor = Color.LIGHT_GRAY;

	
	
	
	
	public IaReflectionPanel(TppIAReflection reflection) {
		boolean japanese = GeneratorProperty.japanese();

		this.reflection = reflection;

		// パネルサイズ初期化
		this.panelWidth = IaReflectionPanel.PANEL_WIDTH;
		this.panelHeight = IaReflectionPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(IaReflectionPanel.bgColor);

		// 枠線
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(japanese?"個人権限アサイン":"IA Assign");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// テーブル名のテキストペイン
		textPaneDtAtNames = new JTextPane();
		textPaneDtAtNames.setEditable(false);
		add(textPaneDtAtNames, BorderLayout.CENTER);

		updateTextPaneText();
	}


	
	
	
	public void updateTextPaneText() {
		boolean japanese = GeneratorProperty.japanese();

		String text = japanese?"データテーブル：":"Data Table :";
		if(reflection.dataTable==null) {
			text += japanese?"！未定義\n":"!Undefined\n";
		}
		else if(reflection.dataTable instanceof DataTable) {
			text += reflection.dataTable.getTableName() + "\n";
		}
		text += japanese?"アカウントテーブル：":"Account Table :";
		if(reflection.accountTable==null) {
			text += japanese?"！未定義\n":"!Undefined\n";
		}
		else if(reflection.accountTable instanceof AccountTable) {
			text += reflection.accountTable.getTableName();
		}
		textPaneDtAtNames.setText(text);
	}
}
