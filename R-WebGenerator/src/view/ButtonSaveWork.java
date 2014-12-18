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
			this.setText("作業を保存");
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

			// .txtで終わっていないなら、.txtを付け足す
			if(!filePath.endsWith(".txt")) {
				filePath += ".txt";
			}

			// 同名のファイルが存在しないか確認する。存在するなら上書きするか問いかけ、キャンセルされたらなにもしない
			File confirmFile = new File(filePath);
			if(confirmFile.exists() && confirmFile.isFile()) {
				int confirm = JOptionPane.showConfirmDialog(this, filePath+"は既に存在します。上書きしますか？", "ファイルの上書き保存", JOptionPane.OK_CANCEL_OPTION);
				if(confirm==JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			
			Serializer serializer = new Serializer();
			serializer.executeSerialization(filePath);

			JOptionPane.showMessageDialog(this, filePath+"に作業を保存しました。");
	    }
		else if (selected == JFileChooser.CANCEL_OPTION){
			return;
		}else if (selected == JFileChooser.ERROR_OPTION){
			JOptionPane.showMessageDialog(this, "！エラーが発生しました");
			Debug.error("エラーまたは取り消しが発生", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
	}
}
