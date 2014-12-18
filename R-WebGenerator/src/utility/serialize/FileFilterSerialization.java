package utility.serialize;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

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
import tpp.portTrans.*;
import transition.*;
import utility.*;
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





public class FileFilterSerialization extends FileFilter {
	  public boolean accept(File f){
		    /* �f�B���N�g���Ȃ疳�����ŕ\������ */
		    if (f.isDirectory()){
		      return true;
		    }

		    /* �g���q�����o���Atxt��������\������ */
		    String ext = getExtension(f);
		    if (ext != null){
		      if (ext.equals("txt")){
		        return true;
		      }else{
		        return false;
		      }
		    }

		    // �g���q���Ȃ��t�@�C���͕\�����Ȃ�
		    return false;
	  }

	  public String getDescription(){
		  return "�e�L�X�g�t�@�C��";	
	  }

	  /* �g���q�����o�� */
	  private String getExtension(File f){
		  String ext = null;
		  String filename = f.getName();
		  int dotIndex = filename.lastIndexOf('.');
		    
		  if ((dotIndex > 0) && (dotIndex < filename.length() - 1)){
			  ext = filename.substring(dotIndex + 1).toLowerCase();
		  }
		      
		  return ext;
	  }
}
