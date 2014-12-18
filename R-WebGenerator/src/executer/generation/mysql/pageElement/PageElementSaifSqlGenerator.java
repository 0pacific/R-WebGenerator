package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElementSaif;
import pageElement.PageElement_HyperLink;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementSaifSqlGenerator {
	/*
	 * SUMMARY :
	 * テーブル"page_element_saif"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// page_element_saifテーブルを作るSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `page_element_saif` (\n" +
				"`pePrimaryKey` int(11) NOT NULL default '0',\n" +
				"`saifName` varchar(255) NOT NULL default ''," +
				"`kind` enum('int','varchar','text','datetime','date','time','mail','enum') NOT NULL default 'int'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";

		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		// 全Webページ中の、該当するページエレメントの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_saif` (`pePrimaryKey`, `saifName`, `kind`) VALUES\n";

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// サービス引数フォームのページエレメントだけ着目
				if(pe instanceof PageElementSaif) {
					PageElementSaif saif = (PageElementSaif)pe;
					
					// このサービス引数フォームのレコードを作成する
					insertSql +=	"(" +
							Integer.toString(saif.pePrimaryKey) + ", " +
							"'" + saif.getSaifName() + "', " +
							"'" + saif.getSaifKind() + "'" +
							"),\n";

					totalThisKindPeNum++;
				}
				
			}
		}
		// もし全Webページ中のどこにもこの種のページエレメントがないなら、INSERT文はなし
		if(totalThisKindPeNum==0) {
			insertSql = "";
		}
		// １つでもあるなら・・・
		else {
			// 最後の",\n"をカットし、";\n"とする
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		
		return sql + insertSql;
	}
}
