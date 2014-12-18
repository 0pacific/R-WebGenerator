package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementSqlCreator {
	/*
	 * SUMMARY :
	 * テーブル"page_element"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// page_elementテーブルを作るSQL
		sql +=	"CREATE TABLE `page_element` (\n" +
				"`pageNumber` tinyint(4) NOT NULL default '0',\n" +
				"`peNumber` tinyint(4) NOT NULL default '0',\n" +
				"`peKind` enum('Login Form','Table Display','Create Form','Update Form','Display Area','Service Argument Input Form','IA Assignment Form','Text','Hyper Link') NOT NULL default 'Display Area',\n" +
				"`primaryKey` int(11) NOT NULL default '0' COMMENT 'Page Element の主キー',\n" +
				"UNIQUE KEY `page_pe` (`pageNumber`,`peNumber`),\n" +
				"UNIQUE KEY `primaryKey` (`primaryKey`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		sql +=	"INSERT INTO `page_element` (`pageNumber`, `peNumber`, `peKind`, `primaryKey`) VALUES\n";

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			int pageNumber = webPage.getWebPageNumber();

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);
				
				// このページエレメントのレコードを作成する
				sql +=	"(" +
						Integer.toString(pageNumber) + ", " +
						Integer.toString(j) + ", " +
						"'" + PageElement.givePeExpression(pe) + "', " +
						Integer.toString(pe.pePrimaryKey) +
						"),\n";
			}
		}

		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}
