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

	// �p�l�����萔
	public static final int PANEL_WIDTH = 120;
	public static final int PANEL_HEIGHT = 70;

	// �C���X�^���X�ϐ��Ƃ��Ẵp�l����
	private int panelWidth;
	private int panelHeight;

	// �w�i�F
	public static Color bgColor = Color.ORANGE;
	
	private int posX;
	private int posY;

	public TableReadingPanel(TppTableReading tblRead) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.tblRead = tblRead;

		// �p�l���T�C�Y������
		this.panelWidth = TableReadingPanel.PANEL_WIDTH;
		this.panelHeight = TableReadingPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(TableReadingPanel.bgColor);

		// �g��
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(japanese?"�e�[�u���Ǐo����":"Table Load");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// �e�[�u�����̃e�L�X�g�y�C��
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
