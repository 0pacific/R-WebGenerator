package executer.generation.mysql.tpp;

import debug.Debug;
import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppConstArrayInt;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import tpp.service.Service;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppServiceArgsSqlGenerator {
	public String getSql() {
		String sql = "";
		
		// CREATE TABLE
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_service_execution_argument` (`tppn` int(11) NOT NULL DEFAULT '0' COMMENT 'Service���sTPP�̎�L�[',`argNumber` int(11) NOT NULL DEFAULT '0',`argTppn` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n";

		TransitionManager transManager = TransitionManager.getInstance();

		// �S�J�ڃv���Z�X���́A�Y������TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_service_execution_argument` (`tppn`, `argNumber`, `argTppn`) VALUES\n";

		// ���݂���S�Ă̑J�ڃv���Z�X�ɂ��ď���
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// ���̑J�ڃv���Z�X�̊eTPP�ɂ��ď���
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// �T�[�r�X���s ��������
				if(tpp instanceof Service) {
					Service service = (Service)tpp;
					
					// �T�[�r�X�̊e�����ɂ��ď���
					for(int k=0; k<service.getArgNum(); k++) {
						TransitionProcessPart argTpp = service.getArgTppIfExists(k);
						if(argTpp==null) {
							Debug.error("�T�[�r�X�u" + service.serviceName + "�v�̑�" + k + "�����i0�n�܂�j������`�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
							return null;
						}
						int argTppn = argTpp.tppn;
						
						insertSql +=	"(" +
										Integer.toString(service.tppn) + ", " +
										Integer.toString(k) + ", " +
										Integer.toString(argTppn) +
										"),\n";
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
