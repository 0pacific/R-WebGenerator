package executer.generation.tpp;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.service.*;
import tpp.port.*;
import tpp.portTrans.*;
import transition.*;
import utility.*;
import utility.serialize.Serializer;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class TppOrderArranger {
	public static int serviceArrangeCounterForDebug = 0;
	public static int createReflectionArrangeCounterForDebug = 0;
	public static int iaReflectionArrangeCounterForDebug = 0;
	
	
	
	public void arrange() {
		TransitionManager tm = TransitionManager.getInstance();
		for(int i=0; i<tm.getTransitionNum(); i++) {
			Transition trans = tm.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// �f�o�b�O�o��
			Debug.today(trans.getTransNumber()+"�Ԃ̑J�ڃv���Z�X��TPP�𐮗�J�n���܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			transProc.debugTppArray();
			
			arrangeTppArray(transProc);

			// �f�o�b�O�o��
			Debug.today(trans.getTransNumber()+"�Ԃ̑J�ڃv���Z�X��TPP�𐮗�I�����܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			transProc.debugTppArray();
		}
	}

	
	
	
	public boolean arrangeTppArray(TransitionProcess transProc) {
		boolean anyModificationDone = false;

		if(arrangeServicesAndArgs(transProc)) {
			arrangeTppArray(transProc);
			return true;
		}
		else if(arrangeCreateReflection(transProc)) {
			arrangeTppArray(transProc);
			return true;
		}
		else if(arrangeIaReflection(transProc)) {
			arrangeTppArray(transProc);
			return true;
		}
		/*
		 * �t�H�[�����t���N�V�����E�E�E��ɐ擪�ɂ���͂��Ȃ̂ŏ����͕s�v
		 */

		/*
		 * ��������
		if(tpp instanceof TppDeleteReflection) {
		}
		else if(tpp instanceof TppUpdateReflection) {
		}
		*/

		return false;
	}
	



	public boolean arrangeServicesAndArgs(TransitionProcess transProc) {
		// �f�o�b�O�i���̊֐��̎��s�񐔂��J�E���g����B�ċA�����[�v�����ɖ����I���Ǝ��s�񐔂�0�ɖ߂�j
		TppOrderArranger.serviceArrangeCounterForDebug++;
		Debug.notice("arrangeServicesAndArgs()..."+TppOrderArranger.serviceArrangeCounterForDebug+"��ڂ̎��s�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

		// ���b��
		if(serviceArrangeCounterForDebug==100) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "arrangeServicesAndArgs()�̍ċA�ďo�񐔂�100�ɒB���Ă��邽�߁A�ُ�I�����܂��B�����֘A�����ĉ������B");
			System.exit(0);
		}
		
		// �Ȃ�炩�̃T�[�r�X����т��̈����ɂ��āA�P�x�ł�TPP�����̏C�����s��ꂽ���ۂ�
		// �s��ꂽ�Ȃ�΁A����̌Ăяo���ł̏����͂����ŕ������A������x�ċA�Ăяo�����s���B
		// �s���Ȃ��܂܂��Ƃ��̊֐��̍Ō�܂ōs���ďI������悤�ɂȂ��Ă���
		boolean anyModificationDone = false;
		
		for(int i=0; i<transProc.getTppNum(); i++) {
			TransitionProcessPart tpp = transProc.getTpp(i);

			// �T�[�r�X��������
			if(tpp instanceof Service) {
				Service service = (Service)tpp;
				
				// �܂��A�ŏ��̈�������Ō�̈����܂ł��A�A�����ď��ԂɌ����悤�ɐ��񂷂�
				int argNum = service.getArgNum();
				for(int j=0; j<argNum; j++) {
					/*
					 * ��TPP�łȂ��y�[�W�G�������g�̉\�����ꉞ����H�������悤
					 */
					
					TransitionProcessPart inputTppAsThisArg = service.getArgTppIfExists(j);
					if(inputTppAsThisArg==null) {	// ����������`�̏ꍇ
						JOptionPane.showMessageDialog(MainFrame.getInstance(),transProc.belongingTrans.getDescription()+"�̑J�ڃv���Z�X�ɂ����āA�T�[�r�X�u"+service.serviceName+"�v�̑�"+(j+1)+"��������`����Ă��Ȃ��悤�ł��B�m�F���ĉ������B");
						Debug.notice(transProc.belongingTrans.getDescription()+"�̑J�ڃv���Z�X�ɂ����āA�T�[�r�X�u"+service.serviceName+"�v�̑�"+j+"�����i0�n�܂�j����`����Ă��Ȃ��悤�ł��B", "TppOrderArranger", Thread.currentThread().getStackTrace()[1].getMethodName());
						return false;
					}

					Debug.notice("�T�[�r�X�u"+service.serviceName+"�v�̑�"+j+"�����i0�n�܂�j�́A"+inputTppAsThisArg.getTppo()+"�ԁi0�n�܂�j�ɂ���悤�ł��B", "TppOrderArranger", Thread.currentThread().getStackTrace()[1].getMethodName());

					if(j==0) {
						// �ŏ��̈����̓X���[
						continue;
					}
					else {
						// �O�̈����Ƃ��Ē�`����Ă���TPP��TPPO���擾
						TransitionProcessPart inputTppAsPrevArg = service.getArgTppIfExists(j-1);

						// �������O�̈��������O�ɂ���Ȃ灟�P��Ɉړ����Ă��
						if(inputTppAsThisArg.getTppo()<inputTppAsPrevArg.getTppo()) {
							Debug.notice("�T�[�r�X�������ړ����܂��@�T�[�r�X�u" + service.serviceName + "�v�̑�" + j + "�����i0�n�܂�j���A" + inputTppAsThisArg.getTppo() + "�Ԃ���" + (inputTppAsPrevArg.getTppo()+1) + "�Ԃցi�u�ԁv��0�n�܂�j", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

							ArrayListElemMover.move(transProc.tppArray, inputTppAsThisArg.getTppo(), inputTppAsPrevArg.getTppo()+1);
							anyModificationDone = true;

							transProc.debugTppArray();
						}
					}
				}

				// �T�[�r�X�{�̂����Ō�̈����ł���TPP�����O�ɂ���Ȃ灟�Ō�̈����ł���TPP�̂P���Ɉړ����Ă��
				TransitionProcessPart lastArgTpp = service.getArgTppIfExists(argNum-1);
				if(service.getTppo()<lastArgTpp.getTppo()) {
					Debug.notice("�T�[�r�X�{�̂��ړ����܂��@�T�[�r�X�u" + service.serviceName + "�v���A" + service.getTppo() + "�Ԃ���" + (lastArgTpp.getTppo()+1) + "�Ԃցi�u�ԁv��0�n�܂�j", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

					ArrayListElemMover.move(transProc.tppArray, service.getTppo(), lastArgTpp.getTppo()+1);
					anyModificationDone = true;

					transProc.debugTppArray();
				}
			}
			
			if(!anyModificationDone) {
				// �C������x���s���Ȃ������̂ŁA����̊֐��Ăяo�����s�����u�ԁi��ԊO����for���ɓ���O�j��TPP�����͂܂������Ă���B����ĕ��ʂɎ���TPP�����Ă���
				continue;
			}

			// �C������x�ł��s��ꂽ...
			// ����̊֐��Ăяo�����s�����u�ԁi��ԊO����for���ɓ���O�j��TPP�����͂��͂����Ă���B
			// ����čċA�Ăяo�����s���A����̑����͂������Ɉς˂�B���ꂪ�I�������return���邾���B
			// �܂�A��TPP�����ԂɌ��Ă������A����̊֐����s�ł͂����������A����ȍ~�̊֐����s�ł܂��ŏ���TPP���猩�Ă����Ă��炤�킯�ł���B
			arrangeServicesAndArgs(transProc);

			// �C�����s��ꂽ���Ƃ�`���邽�߂�true��Ԃ��i���̕��ɏ����Ă���ʂ�A������true��Ԃ��̂��u�P��ڂ̎��s�v�ł���ꍇ�݈̂Ӗ�������
			return true;
		}

		
		/*
		 * �ŏ���TPP����Ō��TPP�܂ŁA�C������x���s���Ȃ������ꍇ���������ɒH���
		 */
		
		// �f�o�b�O�i�ċA�������I������̂ŁA�J�E���^���O�ɖ߂��B����ŕʂ̑J�ڃv���Z�X�ɂ��Đ��񂷂�Ƃ��ɐ������J�E���g�ł���j
		TppOrderArranger.serviceArrangeCounterForDebug = 0;

		// �u�ŏ���TPP����Ō��TPP�܂ŁA�C������x���s���Ȃ������v���Ƃ�`���邽�߂�false��Ԃ��Ă��邪�A
		// ����͍���̎��s���u�P��ڂ̎��s�v�ł���ꍇ�A�܂�ċA�ďo���P�x���s���Ȃ������ꍇ�ɂ݈̂Ӗ������B
		// ���̃P�[�X�ł͂܂��Ɂu�J�ڃv���Z�X���̃T�[�r�X�ƈ����ɂ��ďC������x���s���Ă��Ȃ��v�̂����A
		// �ċA�I�Ɏ��s���Ă���ꍇ�ɂ́A������O�̎��s�ŏC���������Ă���̂�����A������false��Ԃ��Ă�������O�̎��s��true���Ԃ����킯�ł���B
		return false;
	}







	public boolean arrangeCreateReflection(TransitionProcess transProc) {
		// �f�o�b�O�i���̊֐��̎��s�񐔂��J�E���g����B�ċA�����[�v�����ɖ����I���Ǝ��s�񐔂�0�ɖ߂�j
		TppOrderArranger.createReflectionArrangeCounterForDebug++;
		Debug.notice("arrangeCreateReflection()..."+TppOrderArranger.createReflectionArrangeCounterForDebug+"��ڂ̎��s�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// Create���t���N�V�����ɂ��āA�P�x�ł�TPP�����̏C�����s��ꂽ���ۂ�
		// �s��ꂽ�Ȃ�΁A����̌Ăяo���ł̏����͂����ŕ������A������x�ċA�Ăяo�����s���B
		// �s���Ȃ��܂܂��Ƃ��̊֐��̍Ō�܂ōs���ďI������悤�ɂȂ��Ă���
		boolean anyModificationDone = false;
		
		for(int i=0; i<transProc.getTppNum(); i++) {
			TransitionProcessPart tpp = transProc.getTpp(i);

			// Create���t���N�V������������
			if(tpp instanceof TppCreateReflection) {
				TppCreateReflection createReflection = (TppCreateReflection)tpp;
				TfdOutputer tfdOutputer = createReflection.getInputTfdOutputerIfExists();
				if(tfdOutputer==null) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), transProc.belongingTrans.getDescription()+"�̑J�ڃv���Z�X�ɂ����āA�u���R�[�h�쐬�����v�ɓn���\�f�[�^����`����Ă��Ȃ��悤�ł��B�m�F���ĉ������B");
					return false;
				}
				
				// �L���X�g
				TransitionProcessPart tfdTpp = (TransitionProcessPart)tfdOutputer;
				
				// Create���t���N�V�����{�̂��A����TFD�Ƃ��Ē�`���ꂽTPP�����O�ɂ���Ȃ�A�P���Ɉړ����Ă��
				int tfdTppo = tfdTpp.getTppo();
				if(createReflection.getTppo()<tfdTppo) {
					ArrayListElemMover.move(transProc.tppArray, createReflection.getTppo(), tfdTppo+1);
					anyModificationDone = true;

					Debug.notice("TPP���ړ��@Create���t���N�V�������A" + createReflection.getTppo() + "�Ԃ���" + (tfdTppo+1) + "�Ԃ�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
			
			if(!anyModificationDone) {
				// �C������x���s���Ȃ������̂ŁA����̊֐��Ăяo�����s�����u�ԁi��ԊO����for���ɓ���O�j��TPP�����͂܂������Ă���B����ĕ��ʂɎ���TPP�����Ă���
				continue;
			}

			// �C������x�ł��s��ꂽ...
			// ����̊֐��Ăяo�����s�����u�ԁi��ԊO����for���ɓ���O�j��TPP�����͂��͂����Ă���B
			// ����čċA�Ăяo�����s���A����̑����͂������Ɉς˂�B���ꂪ�I�������return���邾���B
			// �܂�A��TPP�����ԂɌ��Ă������A����̊֐����s�ł͂����������A����ȍ~�̊֐����s�ł܂��ŏ���TPP���猩�Ă����Ă��炤�킯�ł���B
			arrangeCreateReflection(transProc);

			// �C�����s��ꂽ���Ƃ�`���邽�߂�true��Ԃ��i���̕��ɏ����Ă���ʂ�A������true��Ԃ��̂��u�P��ڂ̎��s�v�ł���ꍇ�݈̂Ӗ�������
			return true;
		}

		
		/*
		 * �ŏ���TPP����Ō��TPP�܂ŁA�C������x���s���Ȃ������ꍇ���������ɒH���
		 */
		
		// �f�o�b�O�i�ċA�������I������̂ŁA�J�E���^���O�ɖ߂��B����ŕʂ̑J�ڃv���Z�X�ɂ��Đ��񂷂�Ƃ��ɐ������J�E���g�ł���j
		TppOrderArranger.createReflectionArrangeCounterForDebug = 0;

		// �u�ŏ���TPP����Ō��TPP�܂ŁA�C������x���s���Ȃ������v���Ƃ�`���邽�߂�false��Ԃ��Ă��邪�A
		// ����͍���̎��s���u�P��ڂ̎��s�v�ł���ꍇ�A�܂�ċA�ďo���P�x���s���Ȃ������ꍇ�ɂ݈̂Ӗ������B
		// ���̃P�[�X�ł͂܂��Ɂu�J�ڃv���Z�X���̃T�[�r�X�ƈ����ɂ��ďC������x���s���Ă��Ȃ��v�̂����A
		// �ċA�I�Ɏ��s���Ă���ꍇ�ɂ́A������O�̎��s�ŏC���������Ă���̂�����A������false��Ԃ��Ă�������O�̎��s��true���Ԃ����킯�ł���B
		return false;
	}






	public boolean arrangeIaReflection(TransitionProcess transProc) {
		// �f�o�b�O�i���̊֐��̎��s�񐔂��J�E���g����B�ċA�����[�v�����ɖ����I���Ǝ��s�񐔂�0�ɖ߂�j
		TppOrderArranger.iaReflectionArrangeCounterForDebug++;
		Debug.notice("arrangeIaReflection()..."+TppOrderArranger.iaReflectionArrangeCounterForDebug+"��ڂ̎��s�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// IA���t���N�V�����ɂ��āA�P�x�ł�TPP�����̏C�����s��ꂽ���ۂ�
		// �s��ꂽ�Ȃ�΁A����̌Ăяo���ł̏����͂����ŕ������A������x�ċA�Ăяo�����s���B
		// �s���Ȃ��܂܂��Ƃ��̊֐��̍Ō�܂ōs���ďI������悤�ɂȂ��Ă���
		boolean anyModificationDone = false;
		
		for(int i=0; i<transProc.getTppNum(); i++) {
			TransitionProcessPart tpp = transProc.getTpp(i);

			// IA���t���N�V������������
			if(tpp instanceof TppIAReflection) {
				TppIAReflection iaReflection = (TppIAReflection)tpp;

				// �f�[�^�e�[�u��TFD�Ƃ��Ē�`���ꂽTPP���擾
				TfdOutputer dtTfdOutputer = iaReflection.getTfdOutputerAsDataTableTfdIfExists();
				if(dtTfdOutputer==null) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), transProc.belongingTrans.getDescription()+"�̑J�ڃv���Z�X�ɂ����āA�u�l�����A�T�C�������v�ɓn���f�[�^�e�[�u���\�f�[�^����`����Ă��Ȃ��悤�ł��B�m�F���ĉ������B");
					return false;
				}
				
				// �L���X�g
				TransitionProcessPart dtTfdTpp = (TransitionProcessPart)dtTfdOutputer;
				
				// IA���t���N�V�����{�̂��A�f�[�^�e�[�u��TFD�Ƃ��Ē�`���ꂽTPP�����O�ɂ���Ȃ�A�P���Ɉړ����Ă��
				int dtTfdTppo = dtTfdTpp.getTppo();
				if(iaReflection.getTppo()<dtTfdTppo) {
					ArrayListElemMover.move(transProc.tppArray, iaReflection.getTppo(), dtTfdTppo+1);
					anyModificationDone = true;

					Debug.notice("TPP���ړ��@IA���t���N�V�������A" + iaReflection.getTppo() + "�Ԃ���" + (dtTfdTppo+1) + "�Ԃ�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}


				// �A�J�E���g�e�[�u��TFD�Ƃ��Ē�`���ꂽTPP���擾
				TfdOutputer atTfdOutputer = iaReflection.getTfdOutputerAsAccountTableTfdIfExists();
				if(atTfdOutputer==null) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), transProc.belongingTrans.getDescription()+"�̑J�ڃv���Z�X�ɂ����āA�u�l�����A�T�C�������v�ɓn���A�J�E���g�e�[�u���\�f�[�^����`����Ă��Ȃ��悤�ł��B�m�F���ĉ������B");
					return false;
				}
				
				// �L���X�g
				TransitionProcessPart atTfdTpp = (TransitionProcessPart)atTfdOutputer;
				
				// IA���t���N�V�����{�̂��A�A�J�E���g�e�[�u��TFD�Ƃ��Ē�`���ꂽTPP�����O�ɂ���Ȃ�A�P���Ɉړ����Ă��
				int atTfdTppo = atTfdTpp.getTppo();
				if(iaReflection.getTppo()<atTfdTppo) {
					ArrayListElemMover.move(transProc.tppArray, iaReflection.getTppo(), atTfdTppo+1);
					anyModificationDone = true;

					Debug.notice("TPP���ړ��@IA���t���N�V�������A" + iaReflection.getTppo() + "�Ԃ���" + (atTfdTppo+1) + "�Ԃ�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
			
			if(!anyModificationDone) {
				// �C������x���s���Ȃ������̂ŁA����̊֐��Ăяo�����s�����u�ԁi��ԊO����for���ɓ���O�j��TPP�����͂܂������Ă���B����ĕ��ʂɎ���TPP�����Ă���
				continue;
			}

			// �C������x�ł��s��ꂽ...
			// ����̊֐��Ăяo�����s�����u�ԁi��ԊO����for���ɓ���O�j��TPP�����͂��͂����Ă���B
			// ����čċA�Ăяo�����s���A����̑����͂������Ɉς˂�B���ꂪ�I�������return���邾���B
			// �܂�A��TPP�����ԂɌ��Ă������A����̊֐����s�ł͂����������A����ȍ~�̊֐����s�ł܂��ŏ���TPP���猩�Ă����Ă��炤�킯�ł���B
			arrangeIaReflection(transProc);

			// �C�����s��ꂽ���Ƃ�`���邽�߂�true��Ԃ��i���̕��ɏ����Ă���ʂ�A������true��Ԃ��̂��u�P��ڂ̎��s�v�ł���ꍇ�݈̂Ӗ�������
			return true;
		}

		
		/*
		 * �ŏ���TPP����Ō��TPP�܂ŁA�C������x���s���Ȃ������ꍇ���������ɒH���
		 */
		
		// �f�o�b�O�i�ċA�������I������̂ŁA�J�E���^���O�ɖ߂��B����ŕʂ̑J�ڃv���Z�X�ɂ��Đ��񂷂�Ƃ��ɐ������J�E���g�ł���j
		TppOrderArranger.iaReflectionArrangeCounterForDebug = 0;

		// �u�ŏ���TPP����Ō��TPP�܂ŁA�C������x���s���Ȃ������v���Ƃ�`���邽�߂�false��Ԃ��Ă��邪�A
		// ����͍���̎��s���u�P��ڂ̎��s�v�ł���ꍇ�A�܂�ċA�ďo���P�x���s���Ȃ������ꍇ�ɂ݈̂Ӗ������B
		// ���̃P�[�X�ł͂܂��Ɂu�J�ڃv���Z�X���̃T�[�r�X�ƈ����ɂ��ďC������x���s���Ă��Ȃ��v�̂����A
		// �ċA�I�Ɏ��s���Ă���ꍇ�ɂ́A������O�̎��s�ŏC���������Ă���̂�����A������false��Ԃ��Ă�������O�̎��s��true���Ԃ����킯�ł���B
		return false;
	}
}






















