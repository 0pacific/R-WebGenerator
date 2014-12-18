package utility.serialize;

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
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
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





public class Serializer {
	public void executeSerialization(String filePath) {
		try {
			FileOutputStream outFile;
			ObjectOutputStream outObject;

			SynthesisObject so = new SynthesisObject();
			outFile = new FileOutputStream(filePath);
			outObject = new ObjectOutputStream(outFile);
			outObject.writeObject(so);
			
			outObject.close();
			outFile.close();
		}
		catch(IOException ex) {
			Debug.error("IOExceptionが発生しました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			ex.printStackTrace();
			return;
		}
	}

	
	
	
	public void executeDeserialization(String filePath) {
		try {
			FileInputStream inFile;
			ObjectInputStream inObject;

			inFile = new FileInputStream(filePath);
			inObject = new ObjectInputStream(inFile);
			SynthesisObject so = (SynthesisObject)inObject.readObject();
			so.reflect();

			inObject.close();
			inFile.close();
		}
		catch(IOException ex) {
			Debug.error("IOExceptionが発生しました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			ex.printStackTrace();
			return;
		}
		catch(ClassNotFoundException ex) {
			Debug.error("ClassNotFouncExceptionが発生しました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			ex.printStackTrace();
			return;
		}

		// デシリアライズに成功したら、ロール編集画面へ移動
		MainFrame.getInstance().shiftToRoleEdit();
	}
}