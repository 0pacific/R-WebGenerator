package executer.generation.mysql.tpp;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;
import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TfdOutputer;
import tpp.TppCreateFormReflection;
import tpp.TppCreateReflection;
import tpp.TppTableReading;
import tpp.TppUpdateFormReflection;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppCreateReflectionSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_create_reflection` (\n" +
				"`tppn` int(11) NOT NULL DEFAULT '0',\n" +
				"`tableNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`tfdTppn` int(11) NOT NULL DEFAULT '0',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();
		

		// �S�J�ڃv���Z�X���́A�Y������TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_create_reflection` (`tppn`, `tableNumber`, `tfdTppn`) VALUES\n";

		// ���݂���S�J�ڃv���Z�X�ɂ��ď���
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// ���̑J�ڃv���Z�X�̊eTPP�ɂ��ď���
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// Create Reflection ��������
				if(tpp instanceof TppCreateReflection) {
					TppCreateReflection createReflection = (TppCreateReflection)tpp;

					// ����CreateReflection�ɓn�����ATFD�o�͌^TPP���擾
					TfdOutputer tfdOutputer = createReflection.getInputTfdOutputerIfExists();
					if(tfdOutputer==null) {
						Debug.notice("���͂���TFD������`��Create���t���N�V���������݂���悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "���͂���\�f�[�^������`�ȁu���R�[�h�쐬�����v�����݂���悤�ł��B�m�F���ĉ������B");
						return null;
					}
					
					// ����Create Reflection�̃��R�[�h���쐬����
					insertSql +=	"(" +
							Integer.toString(createReflection.tppn) + ", " +
							Integer.toString(createReflection.table.getTableNumber()) + ", " +
							Integer.toString(((TransitionProcessPart)tfdOutputer).tppn) +
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
