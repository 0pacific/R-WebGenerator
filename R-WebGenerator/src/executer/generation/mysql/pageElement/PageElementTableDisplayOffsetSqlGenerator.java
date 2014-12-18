package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElementText;
import pageElement.PageElement_HyperLink;
import pageElement.PageElementTableDisplay;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementTableDisplayOffsetSqlGenerator {
	/*
	 * SUMMARY :
	 * �e�[�u��"page_element_table_display_offset"���쐬����SQL���쐬�A�ԋp
	 */
	public String getSql() {
		String sql = "";

		sql +=	"CREATE TABLE IF NOT EXISTS `page_element_table_display_field` (\n" +
				"`pePrimKey` bigint(20) NOT NULL DEFAULT '0',\n" +
				"`offset` int(11) NOT NULL DEFAULT '0'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManager�C���X�^���X
		WebPageManager wpm = WebPageManager.getInstance();

		// �SWeb�y�[�W���́A�Y������y�[�W�G�������g�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_table_display_field` (`pePrimKey`, `offset`) VALUES\n";

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// ����Web�y�[�W�̊e�y�[�W�G�������g�ɂ��ď���
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// Table Display��������
				if(pe instanceof PageElementTableDisplay) {
					PageElementTableDisplay tableDisplay = (PageElementTableDisplay)pe;
					
					for(int k=0; k<tableDisplay.getLimitedOffsetNum(); k++) {
						int offset = tableDisplay.getLimitedOffset(k);

						insertSql +=	"(" +
										Integer.toString(tableDisplay.pePrimaryKey) + ", " +
										Integer.toString(offset) +
										"),\n";
					}

					totalThisKindPeNum++;
				}
				
			}
		}
		// �����SWeb�y�[�W���̂ǂ��ɂ����̎�̃y�[�W�G�������g���Ȃ��Ȃ�AINSERT���͂Ȃ�
		if(totalThisKindPeNum==0) {
			insertSql = "";
		}
		// �P�ł�����Ȃ�E�E�E
		else {
			// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		
		return sql + insertSql;
	}
}
