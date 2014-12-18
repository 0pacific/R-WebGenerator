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





public class UpdateFormReflectionPanel extends EditPanel {
	public TppUpdateFormReflection reflection;

	// パネル幅定数
	public static final int PANEL_WIDTH = 120;
	public static final int PANEL_HEIGHT = 70;

	// インスタンス変数としてのパネル幅
	public int panelWidth;
	public int panelHeight;

	// 背景色
	public static Color bgColor = Color.PINK;

	
	
	
	
	public UpdateFormReflectionPanel(TppUpdateFormReflection reflection) {
		boolean japanese = GeneratorProperty.japanese();

		this.reflection = reflection;

		// パネルサイズ初期化
		this.panelWidth = UpdateFormReflectionPanel.PANEL_WIDTH;
		this.panelHeight = UpdateFormReflectionPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(UpdateFormReflectionPanel.bgColor);

		// 枠線
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(japanese?"テーブル更新処理":"Table Update");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// テーブル名のテキストペイン
		JTextPane tpTblName = new JTextPane();
		String tableName = reflection.updateForm.getTable().getTableName();
		tpTblName.setText(tableName);
		tpTblName.setEditable(false);
		add(tpTblName, BorderLayout.CENTER);
	}
}
