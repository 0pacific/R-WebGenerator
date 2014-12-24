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





public class CreateFormReflectionPanel extends EditPanel {
	public TppCreateFormReflection reflection;

	// �p�l�����萔
	public static final int PANEL_WIDTH = 120;
	public static final int PANEL_HEIGHT = 70;

	// �C���X�^���X�ϐ��Ƃ��Ẵp�l����
	public int panelWidth;
	public int panelHeight;

	// �w�i�F
	public static Color bgColor = Color.PINK;

	
	
	
	
	public CreateFormReflectionPanel(TppCreateFormReflection reflection) {
		this.reflection = reflection;

		// �p�l���T�C�Y������
		this.panelWidth = CreateFormReflectionPanel.PANEL_WIDTH;
		this.panelHeight = CreateFormReflectionPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(CreateFormReflectionPanel.bgColor);

		// �g��
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(GeneratorProperty.japanese() ? "���R�[�h�쐬����" : "Record Creation");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// �e�[�u�����̃e�L�X�g�y�C��
		JTextPane tpTblName = new JTextPane();
		String tableName = reflection.createForm.table.getTableName();
		tpTblName.setText(tableName);
		tpTblName.setEditable(false);
		add(tpTblName, BorderLayout.CENTER);
	}
}