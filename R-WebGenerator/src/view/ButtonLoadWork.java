package view;

import javax.swing.*;

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
import tpp.port.*;
import tpp.portTrans.*;
import transition.*;
import utility.*;
import utility.serialize.FileFilterSerialization;
import utility.serialize.Serializer;
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




public class ButtonLoadWork extends JButton implements ActionListener {
	public ButtonLoadWork() {
		super("Load Work");
		if(GeneratorProperty.japanese()) {
			this.setText("��Ƃ�Ǎ�");
		}
		addActionListener(this);
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(new FileFilterSerialization());
		filechooser.setAcceptAllFileFilterUsed(false);
		
		int selected = filechooser.showOpenDialog(this);
	    if (selected == JFileChooser.APPROVE_OPTION){
	    	File file = filechooser.getSelectedFile();
			String filePath = file.getPath();

	    	//�m�F
	    	int confirm = JOptionPane.showConfirmDialog(this, filePath+"�����[�h���܂��B���݂̍�Ɠ��e�͑S�Ď����܂�����낵���ł����H", "��Ƃ̕���", JOptionPane.OK_CANCEL_OPTION);
	    	if(confirm==JOptionPane.CANCEL_OPTION) {
	    		return;
	    	}
			
			Serializer serializer = new Serializer();
			serializer.executeDeserialization(filePath);
		}
		else if (selected == JFileChooser.CANCEL_OPTION){
			return;
		}else if (selected == JFileChooser.ERROR_OPTION){
			JOptionPane.showMessageDialog(this, "�I�G���[���������܂���");
			Debug.error("�G���[�܂��͎�����������", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
	}
}
