package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElement_HyperLink;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementHyerLinkSqlGenerator {
	/*
	 * SUMMARY :
	 * �e�[�u��"page_element_hyper_link"���쐬����SQL���쐬�A�ԋp
	 */
	public String getSql() {
		String sql = "";

		// page_element_hyper_link�e�[�u�������SQL
		sql +=	"CREATE TABLE `page_element_hyper_link` (" +
				"`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0'," +
				"`destPageNumber` int(11) NOT NULL DEFAULT '0'," +
				"`text` varchar(255) NOT NULL DEFAULT ''" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManager�C���X�^���X
		WebPageManager wpm = WebPageManager.getInstance();

		// �SWeb�y�[�W���́A�Y������y�[�W�G�������g�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_hyper_link` (`pePrimaryKey`, `destPageNumber`, `text`) VALUES\n";

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// ����Web�y�[�W�̊e�y�[�W�G�������g�ɂ��ď���
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// �n�C�p�[�����N�̃y�[�W�G�������g��������
				if(pe instanceof PageElement_HyperLink) {
					PageElement_HyperLink hyperLink = (PageElement_HyperLink)pe;
					
					// ���̃n�C�p�[�����N�̃��R�[�h���쐬����
					insertSql +=	"(" +
							Integer.toString(hyperLink.pePrimaryKey) + ", " +
							Integer.toString(hyperLink.getDestPage().getWebPageNumber()) + ", " +
							"'" + hyperLink.getText() + "'" +
							"),\n";

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
