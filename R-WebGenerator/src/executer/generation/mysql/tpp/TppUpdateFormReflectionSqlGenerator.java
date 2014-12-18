package executer.generation.mysql.tpp;

import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppTableReading;
import tpp.TppUpdateFormReflection;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppUpdateFormReflectionSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_update_form_reflection` (\n" +
				"`tppn` bigint(20) NOT NULL DEFAULT '0' COMMENT 'TPP�̎�L�[',\n" +
				"`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT '�y�[�W�G�������g�uUpdate Form�v�̎�L�[',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();
		

		// �S�J�ڃv���Z�X���́A�Y������TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_update_form_reflection` (`tppn`, `pePrimaryKey`) VALUES\n";

		// ���݂���S�J�ڃv���Z�X�ɂ��ď���
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// ���̑J�ڃv���Z�X�̊eTPP�ɂ��ď���
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// Update Form Reflection ��������
				if(tpp instanceof TppUpdateFormReflection) {
					TppUpdateFormReflection updateFormReflection = (TppUpdateFormReflection)tpp;
					
					// ����Update Form Reflection�̃��R�[�h���쐬����
					insertSql +=	"(" +
							Integer.toString(updateFormReflection.tppn) + ", " +
							Integer.toString(updateFormReflection.updateForm.pePrimaryKey) +
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
