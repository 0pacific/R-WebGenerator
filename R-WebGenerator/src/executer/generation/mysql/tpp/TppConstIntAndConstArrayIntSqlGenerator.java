package executer.generation.mysql.tpp;

import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppConstArrayInt;
import tpp.TppConstInt;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppConstIntAndConstArrayIntSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_constant_array_int` (" +
				"`tppn` int(11) NOT NULL DEFAULT '0'," +
				"`elementNumber` int(11) NOT NULL DEFAULT '0'," +
				"`value` int(11) NOT NULL DEFAULT '0'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();

		// �S�J�ڃv���Z�X���́A�Y������TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_constant_array_int` (`tppn`, `elementNumber`, `value`) VALUES\n";

		// ���݂���S�Ă̑J�ڃv���Z�X�ɂ��ď���
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// ���̑J�ڃv���Z�X�̊eTPP�ɂ��ď���
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// INT���m�萔�����INT�萔�z�� ��������
				if(tpp instanceof TppConstInt || tpp instanceof TppConstArrayInt) {
					if(tpp instanceof TppConstInt) {
						TppConstInt constInt = (TppConstInt)tpp;

						// ����INT���m�萔�̃��R�[�h���쐬����i�v�f���P�̔z��Ƃ��Č���j
						insertSql +=	"(" +
										Integer.toString(constInt.tppn) + ", " +
										Integer.toString(0) + ", " +
										Integer.toString(constInt.value) +
										"),\n";
					}
					else if(tpp instanceof TppConstArrayInt) {
						TppConstArrayInt constArrayInt = (TppConstArrayInt)tpp;

						// INT�萔�z��̊e�v�f�ɂ��ď���
						for(int k=0; k<constArrayInt.getIntNum(); k++) {
							int intElement = constArrayInt.getIntValue(k);

							// ���̗v�f�̃��R�[�h���쐬����
							insertSql +=	"(" +
											Integer.toString(constArrayInt.tppn) + ", " +
											Integer.toString(k) + ", " +
											Integer.toString(intElement) +
											"),\n";
						}
					}

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
