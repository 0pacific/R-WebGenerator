package executer.generation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;

import webPage.WebPage;
import webPage.WebPageManager;

public class WebPageGenerator implements Serializable {
	public static final String PAGE_FILE_DIRECTORY = "src/generatedFiles/application/";

	
	
	
	
	public WebPageGenerator() {
	}

	
	
	

	public boolean execute() {
		// Web�y�[�W��z�u����f�B���N�g���ɂ���A�S�t�@�C���i�f�B���N�g���ƁAphpEclipse�̃v���W�F�N�g�t�@�C�������j���폜����
		File directory = new File(WebPageGenerator.PAGE_FILE_DIRECTORY);
		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(file.exists() && file.isFile() && !file.getName().equals(".project")) {	// �f�B���N�g���͍폜���Ȃ��i�ėpPHP�t�@�C�����\�ߓ������f�B���N�g���B�Ȃ̂Łj�BphpEclipse�̃v���W�F�N�g�t�@�C�����c��
				boolean delete = file.delete();
				if(!delete) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "�A�v�������������f�B���N�g���̃t�@�C���폜�Ɏ��s���܂����B");
					Debug.error("�t�@�C��'" + file.getName() + "'�𐳏�ɍ폜�ł��܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return false;
				}
			}
		}
		
		// �eWeb�y�[�W��PHP�t�@�C�����쐬���A���̒��ɃR�[�h������
		WebPageManager wpm = WebPageManager.getInstance();
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			boolean result = makeWebPageSourceCode(webPage);
			if(!result) {
				return false;
			}
		}

		return true;
	}

	
	


	public boolean makeWebPageSourceCode(WebPage webPage) {
		try {
			FileWriter writer = new FileWriter(WebPageGenerator.PAGE_FILE_DIRECTORY + webPage.pageFileName);

			String code =	"<?php\n" +
							"\n" +
							"define(\"PAGE_NUMBER\", " + webPage.getWebPageNumber() + ");\n" +
							"\n" +
							"require \"webPageTemplate/webPageTemplate.php\";\n" +
							"\n" +
							"?>";
			writer.write(code);

			writer.close();
		} catch (IOException e) {
			Debug.error("1208:Web�y�[�W�u" + webPage.pageFileName + "�v���쐬�ł��܂���ł���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
}
