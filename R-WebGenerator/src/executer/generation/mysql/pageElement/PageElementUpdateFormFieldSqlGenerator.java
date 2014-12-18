package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElementText;
import pageElement.PageElementUpdateForm;
import pageElement.PageElement_HyperLink;
import pageElement.PageElementTableDisplay;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementUpdateFormFieldSqlGenerator {
	/*
	 * SUMMARY :
	 * テーブル"page_element_update_form_field"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// page_element_update_form_fieldテーブルを作るSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `page_element_update_form_field` (\n" +
				"`pePrimKey` bigint(20) NOT NULL DEFAULT '0',\n" +
				"`offset` int(11) NOT NULL DEFAULT '0'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		// 全Webページ中の、該当するページエレメントの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_update_form_field` (`pePrimKey`, `offset`) VALUES\n";

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// Update Form だけ着目
				if(pe instanceof PageElementUpdateForm) {
					PageElementUpdateForm updateForm = (PageElementUpdateForm)pe;
					
					for(int k=0; k<updateForm.getLimitedOffsetNum(); k++) {
						int offset = updateForm.getLimitedOffset(k);

						// このオフセットのレコードを作成する
						insertSql +=	"(" +
										Integer.toString(updateForm.pePrimaryKey) + ", " +
										Integer.toString(offset) +
										"),\n";
					}
					
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
