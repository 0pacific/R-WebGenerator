package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;
import tpp.TfdOutputer;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service3_TableJoin extends TabularOutputService {

	public Service3_TableJoin(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// �e�C���v�b�g�̎��
		String[] argTypes = {
				Service.IO_TYPE_TFD,
				Service.IO_TYPE_TFD,
		};
		this.argTypes = argTypes;

		// ������TPP�|�[�g�̏������ɂ�argTypes���p�����邽�߁AargTypes�̏�������łȂ���΂Ȃ�Ȃ�
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "�\����" : "Join";
		this.serviceType = Service.SERVICE_TYPE_TABLE_PROC;

		// ������
		description = GeneratorProperty.japanese()?
						"�Q�̕\�����Ɍ������܂��B�����̕\�A�E���̕\�����ɑI��ŉ������B":
						"This service joins 2 tables. Define a table on the left and one on the right.";


		// �e�C���v�b�g�̖��O
		String[] argNames = {
			japanese ? "�e�[�u���i���j" : "Left Table",
			japanese ? "�e�[�u���i�E�j" : "Right Table"
		};
		this.argNames = argNames;

		// �e�C���v�b�g�ɂ��Ă̐�����
		String[] argDescs = {
			"�\�f�[�^�P�E�E�E\n" +
			"���}�̒����獶���̕\�f�[�^��I��\n" +
			"�i�e�[�u���Ǐo�����A�T�[�r�X�Ȃǁj",
			"�\�f�[�^�Q�E�E�E\n" +
			"���}�̒�����E���̕\�f�[�^��I��\n" +
			"�i�e�[�u���Ǐo�����A�T�[�r�X�Ȃǁj"
		};
		this.argDescs = argDescs;

		// �A�E�g�v�b�g�̎��
		this.outputType = Service.IO_TYPE_TFD;
	}

	
	
	
	
	public int getOutFieldNum() {
		// �������A�������������Ă������TPP�|�[�g
		TppPort firstPort = this.getInputPort(0);
		TppPort secondPort = this.getInputPort(1);

		// �������A��������TFD�o�͌^TPP���i��`����Ă���΁j�擾����
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		TfdOutputer tfdOutputerSecond = transProc.portTransManager.getInputTfdOutputerTppIfExists(secondPort);
		if(tfdOutputerFirst==null || tfdOutputerSecond==null) {
			Debug.notice("�\�����T�[�r�X���󂯎��Q�̕\�̂ǂ��炩�Ȃ�����������`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return -1;
		}
		
		// �Q��TFD�o�͌^TPP���ꂼ��́A�o�̓t�B�[���h���̘a��Ԃ�
		return tfdOutputerFirst.getOutFieldNum() + tfdOutputerSecond.getOutFieldNum();
	}





	public ArrayList<String> getOutputFieldWebNames() {
		// �������A�������������Ă������TPP�|�[�g
		TppPort firstPort = this.getInputPort(0);
		TppPort secondPort = this.getInputPort(1);

		// �������A��������TFD�o�͌^TPP���i��`����Ă���΁j�擾����
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		TfdOutputer tfdOutputerSecond = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null || tfdOutputerSecond==null) {
			Debug.notice("�\�����T�[�r�X���󂯎��Q�̕\�̂ǂ��炩�Ȃ�����������`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray1 = tfdOutputerFirst.getOutputFieldWebNames();
		ArrayList<String> fieldNameArray2 = tfdOutputerSecond.getOutputFieldWebNames();
		for(int i=0; i<fieldNameArray2.size(); i++) {
			fieldNameArray1.add(fieldNameArray2.get(i));
		}
		return fieldNameArray1;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		// ���̃T�[�r�X�ɂ͕K�v�Ȃ��̂ŌĂяo���Ă鎞�_�ł�������
		Debug.error("�z��O�̌Ăяo���ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}

	
	
	
	public void initServiceNumber() {
		serviceNumber = 3;
	}
	
}