package utility;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;
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
import tpp.service.*;
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
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.webPageDefinition.*;
import webPage.*;



public class ZipManager {
	 
	/**
	 * �P�̃t�@�C���܂��͕����̃t�@�C����
	 * zip�`���̃A�[�J�C�u�ɂ��� Zip
	 */
	 
	  // �t�@�C���̓ǂݍ��݂̏I����\���萔
	  private static final int EOF = -1;
	  // �Ώۂ̃f�B���N�g��
	  private File dir;
	  // �e�̃f�B���N�g����
	  private String parentName;
	  // ������(�t�@�C�����A�f�B���N�g����)
	  private static String argName;
	  // ZIP�g���q
	  private static String zipExtension = ".zip";
	  
	  // �R���X�g���N�^
	  ZipManager (String parentName, File dir) {
	    this.parentName = parentName;
	    this.dir = dir;
	  }
	  
	  public void compound(String argName) {
	      try {
	        // ���k�t�@�C�������擾
	        int pos = argName.lastIndexOf('.');
	        String zipFilename;
	        // �t�@�C�����f�B���N�g�������肷��
	        // ���̂Ƃ��̔���ł� . �����݂��邩�Ŕ��肵�Ă���
	        // ���̂��߃t�H���_�� . �����݂����ꍇ�̓G���[�ƂȂ�̂Œ���
	        if (pos > 0) {
	          // ZIP �t�@�C�������w��
	          zipFilename = argName.substring(0, pos) + zipExtension;
	        } else {
	          int ind = argName.indexOf(':');
	          argName = argName.substring(ind + 1, argName.length());
	          int las = argName.lastIndexOf(File.separator);
	          // ZIP �t�@�C�������w��
	          zipFilename = argName.substring(las + 1, argName.length()) + zipExtension;
	        }
	        // �ǂݍ��ݗp File �I�u�W�F�N�g����
	        File file = new File(argName);
	        // ZIP�ւ̏������ݗp File �I�u�W�F�N�g����
	        File zipFile = new File(zipFilename);
	        // ZipOutputStream �𐶐�
	        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
	        // �t�@�C���̈��k����
	        if(file.isFile()) {
	          System.out.println("�t�@�C�������k��....");
	          addTargetFile(zos, file);
	          System.out.println("�t�@�C�������k������");
	        // �f�B���N�g���̈��k����
	        } else if(file.isDirectory()) {
	          System.out.println("�f�B���N�g�������k��....");
	          ZipManager origin = new ZipManager(argName, file);
	          List vec = origin.pathNames();
	          for (int i = 0, k = vec.size(); i < k; i++) {
	            File f = new File((String)vec.get(i));
	            addTargetFile(zos, f);
	          }
	          System.out.println("�f�B���N�g�������k������");
	        }
	        zos.close();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	  }
	  // �t�@�C�����̈ꗗ���擾���郁�\�b�h
	  private List pathNames() {
	    List list = null;
	    try {
	      // �f�B���N�g�����𒲂ׂ�
	      String[] fileList = dir.list();
	      File[] files = new File[fileList.length];
	      list = new ArrayList();
	      for(int i = 0; i < fileList.length; i++) {
	        String pathName = parentName + File.separator + fileList[i];
	        files[i] = new File(pathName);
	        if( files[i].isFile() )
	          list.add(files[i].getPath());
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return list;
	  }
	  // �^����ꂽ�t�@�C�������k���AZip�I�u�W�F�N�g�ɒǉ����郁�\�b�h
	  private static void addTargetFile(ZipOutputStream zos, File file) {
	    try {
	      // �w��̃t�@�C����ǂݍ���
	      BufferedInputStream bis
	           = new BufferedInputStream(new FileInputStream(file));
	      // ZipEntry �𐶐�
	      ZipEntry target = new ZipEntry(file.getPath());
	      // ��������
	      zos.putNextEntry(target);
	      // �o�b�t�@�����O�͎����I�ɍs���܂��B
	      // �����Ŏw�肵�Ă���o�b�t�@�T�C�Y�́A�Ӗ��͂Ȃ��ł�
	      byte buf[] = new byte[1024];
	      int count;
	      while ((count = bis.read(buf, 0, 1024)) != EOF) {
	        zos.write(buf, 0, count);
	      }
	      bis.close();
	      // �������ݏI��
	      zos.closeEntry(); 
	    } catch (Exception e){
	      e.printStackTrace();
	    }
	  }
}

