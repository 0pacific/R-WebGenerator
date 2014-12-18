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





public class CreateReflectionPanel extends EditPanel {
	public TppCreateReflection reflection;

	// �p�l�����萔
	public static final int PANEL_WIDTH = 120;
	public static final int PANEL_HEIGHT = 70;

	// �C���X�^���X�ϐ��Ƃ��Ẵp�l����
	public int panelWidth;
	public int panelHeight;

	// �e�[�u�������L�ڂ����e�L�X�g�y�C��
	JTextPane textPaneTableName;
	
	// �w�i�F
	public static Color bgColor = Color.PINK;

	
	
	
	
	public CreateReflectionPanel(TppCreateReflection reflection) {
		this.reflection = reflection;

		// �p�l���T�C�Y������
		this.panelWidth = CreateReflectionPanel.PANEL_WIDTH;
		this.panelHeight = CreateReflectionPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(CreateReflectionPanel.bgColor);

		// �g��
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(GeneratorProperty.japanese() ? "���R�[�h�쐬����" : "Record Creation");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// �e�[�u�����̃e�L�X�g�y�C��
		textPaneTableName = new JTextPane();
		textPaneTableName.setText(reflection.table.getTableName());
		textPaneTableName.setEditable(false);
		add(textPaneTableName, BorderLayout.CENTER);
	}
}
