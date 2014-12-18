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
		// Webページを配置するディレクトリにある、全ファイル（ディレクトリと、phpEclipseのプロジェクトファイル除く）を削除する
		File directory = new File(WebPageGenerator.PAGE_FILE_DIRECTORY);
		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(file.exists() && file.isFile() && !file.getName().equals(".project")) {	// ディレクトリは削除しない（汎用PHPファイルが予め入ったディレクトリ達なので）。phpEclipseのプロジェクトファイルも残す
				boolean delete = file.delete();
				if(!delete) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "アプリが生成されるディレクトリのファイル削除に失敗しました。");
					Debug.error("ファイル'" + file.getName() + "'を正常に削除できませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return false;
				}
			}
		}
		
		// 各WebページのPHPファイルを作成し、その中にコードを書く
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
			Debug.error("1208:Webページ「" + webPage.pageFileName + "」を作成できませんでした", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
}
