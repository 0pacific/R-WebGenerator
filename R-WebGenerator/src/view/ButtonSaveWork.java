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




public class ButtonSaveWork extends JButton implements ActionListener {
	public ButtonSaveWork() {
		super("Save Work");
		if(GeneratorProperty.japanese()) {
			this.setText("��Ƃ�ۑ�");
		}
		addActionListener(this);
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(new FileFilterSerialization());
		filechooser.setAcceptAllFileFilterUsed(false);
		
		int selected = filechooser.showSaveDialog(this);
	    if (selected == JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();
			String filePath = file.getPath();

			// .txt�ŏI����Ă��Ȃ��Ȃ�A.txt��t������
			if(!filePath.endsWith(".txt")) {
				filePath += ".txt";
			}

			// �����̃t�@�C�������݂��Ȃ����m�F����B���݂���Ȃ�㏑�����邩�₢�����A�L�����Z�����ꂽ��Ȃɂ����Ȃ�
			File confirmFile = new File(filePath);
			if(confirmFile.exists() && confirmFile.isFile()) {
				int confirm = JOptionPane.showConfirmDialog(this, filePath+"�͊��ɑ��݂��܂��B�㏑�����܂����H", "�t�@�C���̏㏑���ۑ�", JOptionPane.OK_CANCEL_OPTION);
				if(confirm==JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			
			Serializer serializer = new Serializer();
			serializer.executeSerialization(filePath);

			JOptionPane.showMessageDialog(this, filePath+"�ɍ�Ƃ�ۑ����܂����B");
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
