package executer.generation.mysql;

import webPage.WebPage;
import webPage.WebPageManager;

public class PageSqlCreator {
	public PageSqlCreator() {
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * テーブル"pages"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// pagesテーブルを作るSQL
		sql += "CREATE TABLE `pages` (\n"
				+ "`pageNumber` int(11) NOT NULL DEFAULT '0',\n"
				+ "`fileName` varchar(255) NOT NULL DEFAULT '',\n"
				+ "PRIMARY KEY (`pageNumber`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"
				+ "\n";
				
		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			int pageNumber = webPage.getWebPageNumber();
			String pageFileName = webPage.pageFileName;

			// このWebページの番号とファイル名を記載したレコード　を作成するINSERT文を作り、付け足す
			String sql_i = "insert into pages (pageNumber, fileName) values("
							+ pageNumber  + ","
							+ "'" + pageFileName + "'"
							+ ");\n";
			sql += sql_i;
		}

		return sql;
	}
}
