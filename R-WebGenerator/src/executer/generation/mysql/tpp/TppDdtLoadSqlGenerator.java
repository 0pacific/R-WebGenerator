package executer.generation.mysql.tpp;

import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppDdtLoadSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_et_load` (" +
				"`tppn` int(11) NOT NULL DEFAULT '0'," +
				"`etNumber` int(11) NOT NULL DEFAULT '0'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();
		

		// �S�J�ڃv���Z�X���́A�Y������TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_et_load` (`tppn`, `etNumber`) VALUES\n";

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// ���̑J�ڃv���Z�X�̊eTPP�ɂ��ď���
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// DDT Load ��������
				if(tpp instanceof TppTableReading) {
					TppTableReading ddtLoad = (TppTableReading)tpp;
					
					// ����DDT Load�̃��R�[�h���쐬����
					insertSql +=	"(" +
							Integer.toString(ddtLoad.tppn) + ", " +
							Integer.toString(ddtLoad.getTable().getTableNumber()) +
							"),\n";

					totalThisKindTppNum++;
				}
			}
		}
		// �����S�J�ڃv���Z�X���̂ǂ��ɂ����̎��TPP���Ȃ��Ȃ�AINSERT���͂Ȃ�
		if(totalThisKindTppNum==0) {
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
