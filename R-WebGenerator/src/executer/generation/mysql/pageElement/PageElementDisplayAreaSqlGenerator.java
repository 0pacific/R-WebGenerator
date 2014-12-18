package executer.generation.mysql.pageElement;

import java.util.ArrayList;

import debug.Debug;

import pageElement.PageElement;
import pageElement.PageElementDisplayArea;
import pageElement.PageElement_HyperLink;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementDisplayAreaSqlGenerator {
	/*
	 * SUMMARY :
	 * �e�[�u��"page_element_da"���쐬����SQL���쐬�A�ԋp
	 */
	public String getSql() {
		String sql = "";

		// page_element_da�e�[�u�������SQL
		sql +=	"CREATE TABLE `page_element_da` (" +
				"`pePrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT 'page_element�e�[�u���̎�L�['," +
				"`transitionPrimaryKey` int(11) NOT NULL COMMENT '�J�ڂ̎�L�[�i�ǂ̑J�ڃv���Z�X��ʂ����ꍇ���j'," +
				"`tppn` int(11) NOT NULL DEFAULT '0'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManager�C���X�^���X
		WebPageManager wpm = WebPageManager.getInstance();

		// �SWeb�y�[�W���́A�Y������y�[�W�G�������g�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_da` (`pePrimaryKey`, `transitionPrimaryKey`, `tppn`) VALUES\n";

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// ����Web�y�[�W�̊e�y�[�W�G�������g�ɂ��ď���
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// Display Area�̃y�[�W�G�������g��������
				if(pe instanceof PageElementDisplayArea) {
					PageElementDisplayArea displayArea = (PageElementDisplayArea)pe;

					// Display Area���u�����y�[�W�֌������J�ڂ����ׂĐ􂢏o��
					WebPage parentPage = displayArea.parentPage;
					ArrayList<Transition> comingTransArray = TransitionManager.getInstance().getTransArrayByEndPage(parentPage);
					
					// ���̑S�Ă̑J�ځi�̑J�ڃv���Z�X�j�ɂ��āADisplay Area�Ɍ��ʕ\�����ׂ�TPP��TPPN�𒲂ׁA�P���R�[�h�����
					for(int k=0; k<comingTransArray.size(); k++) {
						// ���̑J�ڂ̎�L�[
						Transition trans = comingTransArray.get(k);
						int transKey = trans.transKey;

						// ���̑J�ڂ̑J�ڃv���Z�X��ʂ����ۂ�Display Area�����ʕ\������TPP��TPPN���擾
						TransitionProcessPart inputTpp = displayArea.getInputTppIfExists(trans.transProc);
						if(inputTpp==null) {
							Debug.error("SQL�̐����Ɏ��s���܂����B���͂̒�`����Ă��Ȃ�Display Area������悤�ł��B�ǂ̑J�ڃv���Z�X�ɂ����ē��͂���ĂȂ��̂��͊���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
							return null;
						}
						int inputTppn = inputTpp.tppn;
						
						insertSql +=	"(" +
								Integer.toString(displayArea.pePrimaryKey) + ", " +
								Integer.toString(transKey) + ", " +
								Integer.toString(inputTppn) +
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
