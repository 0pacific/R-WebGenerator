package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElementLoginForm;
import pageElement.PageElement_HyperLink;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementLoginFormSqlGenerator {
	/*
	 * SUMMARY :
	 * �e�[�u��"page_element_login_form"���쐬����SQL���쐬�A�ԋp
	 */
	public String getSql() {
		String sql = "";

		// page_element_login_form�e�[�u�������SQL
		sql +=	"CREATE TABLE IF NOT EXISTS `page_element_login_form` (" +
				"`pePrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT 'page_element�e�[�u���̎�L�['," +
				"`accountTableNumber` int(11) NOT NULL DEFAULT '0' COMMENT '���Ԃ̃A�J�E���g�e�[�u���̃��O�C���t�H�[����'," +
				"`destPageNumber` int(11) NOT NULL DEFAULT '0' COMMENT '���O�C���������ɍs���y�[�W�̔ԍ�'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManager�C���X�^���X
		WebPageManager wpm = WebPageManager.getInstance();

		// �SWeb�y�[�W���́A�Y������y�[�W�G�������g�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_login_form` (`pePrimaryKey`, `accountTableNumber`, `destPageNumber`) VALUES\n";

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// ����Web�y�[�W�̊e�y�[�W�G�������g�ɂ��ď���
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// ���O�C���t�H�[���̃y�[�W�G�������g��������
				if(pe instanceof PageElementLoginForm) {
					PageElementLoginForm loginForm = (PageElementLoginForm)pe;
					
					// ���̃��O�C���t�H�[���̃��R�[�h���쐬����
					insertSql +=	"(" +
							Integer.toString(loginForm.pePrimaryKey) + ", " +
							Integer.toString(loginForm.accountTable.getTableNumber()) + ", " +
							Integer.toString(loginForm.destWebPage.getWebPageNumber()) +
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
