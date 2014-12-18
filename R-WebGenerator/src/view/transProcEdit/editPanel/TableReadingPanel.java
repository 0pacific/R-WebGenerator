package view.transProcEdit.editPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import property.GeneratorProperty;

import tpp.TppTableReading;
import view.EditPanel;
import view.transProcEdit.Panel_TpEdit_Above;

public class TableReadingPanel extends EditPanel {
	private TppTableReading tblRead;

	// パネル幅定数
	public static final int PANEL_WIDTH = 120;
	public static final int PANEL_HEIGHT = 70;

	// インスタンス変数としてのパネル幅
	private int panelWidth;
	private int panelHeight;

	// 背景色
	public static Color bgColor = Color.ORANGE;
	
	private int posX;
	private int posY;

	public TableReadingPanel(TppTableReading tblRead) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.tblRead = tblRead;

		// パネルサイズ初期化
		this.panelWidth = TableReadingPanel.PANEL_WIDTH;
		this.panelHeight = TableReadingPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(TableReadingPanel.bgColor);

		// 枠線
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(japanese?"テーブル読出処理":"Table Load");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// テーブル名のテキストペイン
		String tblName = tblRead.getTable().getTableName();
		JTextPane tpTblName = new JTextPane();
		tpTblName.setText(tblName);
		tpTblName.setEditable(false);
		add(tpTblName, BorderLayout.CENTER);
	}

	public TppTableReading getTblRead() {
		return tblRead;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int x) {
		posX = x;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int y) {
		posY = y;
	}

	public int getPanelWidth() {
		return panelWidth;
	}

	public int getPanelHeight() {
		return panelHeight;
	}
}
