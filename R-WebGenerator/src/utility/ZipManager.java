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
	 * １つのファイルまたは複数のファイルを
	 * zip形式のアーカイブにする Zip
	 */
	 
	  // ファイルの読み込みの終わりを表す定数
	  private static final int EOF = -1;
	  // 対象のディレクトリ
	  private File dir;
	  // 親のディレクトリ名
	  private String parentName;
	  // 引数名(ファイル名、ディレクトリ名)
	  private static String argName;
	  // ZIP拡張子
	  private static String zipExtension = ".zip";
	  
	  // コンストラクタ
	  ZipManager (String parentName, File dir) {
	    this.parentName = parentName;
	    this.dir = dir;
	  }
	  
	  public void compound(String argName) {
	      try {
	        // 圧縮ファイル名を取得
	        int pos = argName.lastIndexOf('.');
	        String zipFilename;
	        // ファイルかディレクトリか判定する
	        // このときの判定では . が存在するかで判定している
	        // そのためフォルダに . が存在した場合はエラーとなるので注意
	        if (pos > 0) {
	          // ZIP ファイル名を指定
	          zipFilename = argName.substring(0, pos) + zipExtension;
	        } else {
	          int ind = argName.indexOf(':');
	          argName = argName.substring(ind + 1, argName.length());
	          int las = argName.lastIndexOf(File.separator);
	          // ZIP ファイル名を指定
	          zipFilename = argName.substring(las + 1, argName.length()) + zipExtension;
	        }
	        // 読み込み用 File オブジェクト生成
	        File file = new File(argName);
	        // ZIPへの書き込み用 File オブジェクト生成
	        File zipFile = new File(zipFilename);
	        // ZipOutputStream を生成
	        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
	        // ファイルの圧縮処理
	        if(file.isFile()) {
	          System.out.println("ファイルを圧縮中....");
	          addTargetFile(zos, file);
	          System.out.println("ファイルを圧縮中完了");
	        // ディレクトリの圧縮処理
	        } else if(file.isDirectory()) {
	          System.out.println("ディレクトリを圧縮中....");
	          ZipManager origin = new ZipManager(argName, file);
	          List vec = origin.pathNames();
	          for (int i = 0, k = vec.size(); i < k; i++) {
	            File f = new File((String)vec.get(i));
	            addTargetFile(zos, f);
	          }
	          System.out.println("ディレクトリを圧縮中完了");
	        }
	        zos.close();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	  }
	  // ファイル名の一覧を取得するメソッド
	  private List pathNames() {
	    List list = null;
	    try {
	      // ディレクトリ内を調べる
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
	  // 与えられたファイルを圧縮し、Zipオブジェクトに追加するメソッド
	  private static void addTargetFile(ZipOutputStream zos, File file) {
	    try {
	      // 指定のファイルを読み込む
	      BufferedInputStream bis
	           = new BufferedInputStream(new FileInputStream(file));
	      // ZipEntry を生成
	      ZipEntry target = new ZipEntry(file.getPath());
	      // 書き込み
	      zos.putNextEntry(target);
	      // バッファリングは自動的に行われます。
	      // ここで指定しているバッファサイズは、意味はないです
	      byte buf[] = new byte[1024];
	      int count;
	      while ((count = bis.read(buf, 0, 1024)) != EOF) {
	        zos.write(buf, 0, count);
	      }
	      bis.close();
	      // 書き込み終了
	      zos.closeEntry(); 
	    } catch (Exception e){
	      e.printStackTrace();
	    }
	  }
}

