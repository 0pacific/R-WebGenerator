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

	// �p�l�����萔
	public static final int PANEL_WIDTH = 150;
	public static final int PANEL_HEIGHT = 100;

	// �C���X�^���X�ϐ��Ƃ��Ẵp�l����
	public int panelWidth;
	public int panelHeight;

	// �f�[�^�e�[�u�������L�ڂ����e�L�X�g�y�C��
	JTextPane textPaneDtAtNames;
	
	// �w�i�F
	public static Color bgColor = Color.LIGHT_GRAY;

	
	
	
	
	public IaReflectionPanel(TppIAReflection reflection) {
		boolean japanese = GeneratorProperty.japanese();

		this.reflection = reflection;

		// �p�l���T�C�Y������
		this.panelWidth = IaReflectionPanel.PANEL_WIDTH;
		this.panelHeight = IaReflectionPanel.PANEL_HEIGHT;

		setLayout(new BorderLayout());
		setBackground(IaReflectionPanel.bgColor);

		// �g��
		EtchedBorder eBorder = new EtchedBorder(EtchedBorder.RAISED);
		TitledBorder tBorder = new TitledBorder(eBorder);
		tBorder.setTitle(japanese?"�l�����A�T�C��":"IA Assign");
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitlePosition(TitledBorder.TOP);
		tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
		setBorder(tBorder);

		// �e�[�u�����̃e�L�X�g�y�C��
		textPaneDtAtNames = new JTextPane();
		textPaneDtAtNames.setEditable(false);
		add(textPaneDtAtNames, BorderLayout.CENTER);

		updateTextPaneText();
	}


	
	
	
	public void updateTextPaneText() {
		boolean japanese = GeneratorProperty.japanese();

		String text = japanese?"�f�[�^�e�[�u���F":"Data Table :";
		if(reflection.dataTable==null) {
			text += japanese?"�I����`\n":"!Undefined\n";
		}
		else if(reflection.dataTable instanceof DataTable) {
			text += reflection.dataTable.getTableName() + "\n";
		}
		text += japanese?"�A�J�E���g�e�[�u���F":"Account Table :";
		if(reflection.accountTable==null) {
			text += japanese?"�I����`\n":"!Undefined\n";
		}
		else if(reflection.accountTable instanceof AccountTable) {
			text += reflection.accountTable.getTableName();
		}
		textPaneDtAtNames.setText(text);
	}
}
