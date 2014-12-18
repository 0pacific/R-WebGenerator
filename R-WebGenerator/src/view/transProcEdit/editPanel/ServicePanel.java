package view.transProcEdit.editPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import property.GeneratorProperty;

import tpp.service.Service;
import view.EditPanel;
import view.transProcEdit.Panel_TpEdit_Above;

public class ServicePanel extends EditPanel {
	private Service service;
	
	// パネル幅定数
	public static final int PANEL_WIDTH = 120;
	public static final int PANEL_HEIGHT = 70;

	// インスタンス変数としてのパネル幅
	private int panelWidth;
	private int panelHeight;

	// 背景色
	public static Color bgColor = Color.green;

	private int posX;
	private int posY;
	
	public ServicePanel(Service service) {
		this.service = service;

		// パネルサイズ初期化
		this.panelWidth = ServicePanel.PANEL_WIDTH;
		this.panelHeight = ServicePanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(ServicePanel.bgColor);
		
		// 日本語モードか
		boolean japanese = GeneratorProperty.japanese();

		// 枠線
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(japanese ? "サービス" : "Service");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// テーブル名のテキストペイン
		String servName = service.serviceName;
		JTextPane tpServName = new JTextPane();
		tpServName.setText(servName);
		tpServName.setEditable(false);
		add(tpServName, BorderLayout.CENTER);
	}

	public Service getService() {
		return service;
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
