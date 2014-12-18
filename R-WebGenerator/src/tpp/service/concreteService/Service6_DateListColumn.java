package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.TppConstString;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service6_DateListColumn extends TabularOutputService {
	public Service6_DateListColumn(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// �e�C���v�b�g�̎��
		String[] argTypes = {
				Service.IO_TYPE_DATE,
				Service.IO_TYPE_DATE,
				Service.IO_TYPE_CONST_STRING,
		};
		this.argTypes = argTypes;

		// ������TPP�|�[�g�̏������ɂ�argTypes���p�����邽�߁AargTypes�̏�������łȂ���΂Ȃ�Ȃ�
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "���t�A�ԃt�B�[���h�쐬" : "Date Range Field";
		this.serviceType = Service.SERVICE_TYPE_FIELD_PROC;

		// ������
		description = GeneratorProperty.japanese()?
						"�J�n���t�ƏI�����t���󂯎��A�J�n���t����I�����t�܂ł̓��t���ォ��i�[���Ă������P��̕\�f�[�^���o�͂��܂��B�J�n���t�A�I�����t���ꂼ���n���T�[�r�X�������̓t�H�[���Ȃ����T�[�r�X���w�肵�ĉ������B":
						"This service makes date range field. Define first date and last date.";

		// �e�C���v�b�g�̖��O
		String[] argNames = {
			japanese ? "�J�n���t" : "First Date",
			japanese ? "�I�����t" : "Last Date",
			japanese ? "�t�B�[���h��" : "Field Name"
		};
		this.argNames = argNames;

		// �e�C���v�b�g�ɂ��Ă̐�����
		String[] argDescs = {
			"�J�n���t�E�E�E\n" +
			"���t���o�͂���T�[�r�X�A�܂��̓T�[�r�X�������̓t�H�[����I��",

			"�I�����t�E�E�E\n" +
			"���t���o�͂���T�[�r�X�A�܂��̓T�[�r�X�������̓t�H�[����I��",

			"�t�B�[���h���E�E�E\n" +
			"�o�͂����t�B�[���h�i�P�j�̖��̂����",
		};
		this.argDescs = argDescs;

		// �A�E�g�v�b�g�̎��
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		return 1;
	}




	public ArrayList<String> getOutputFieldWebNames() {
		// ��O�����������Ă������TPP�|�[�g
		TppPort thirdPort = this.getInputPort(2);

		// ��O������TPP���i��`����Ă���΁j�擾����
		TppConstString constString = (TppConstString)transProc.portTransManager.getInputTppIfExists(thirdPort);
		if(constString==null) {
			Debug.notice("��`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray = new ArrayList<String>();
		fieldNameArray.add(constString.getValue());
		return fieldNameArray;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		// ���̃T�[�r�X�ɂ͕K�v�Ȃ��̂ŌĂяo���Ă鎞�_�ł�������
		Debug.error("�z��O�̌Ăяo���ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 6;
	}
}
