package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service4_BlankTable extends TabularOutputService implements ConstArrayElementNumGetter {
	public Service4_BlankTable(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// �e�C���v�b�g�̎��
		String[] argTypes = {
				Service.IO_TYPE_CONST_INT,
				Service.IO_TYPE_CONST_INT,
				Service.IO_TYPE_CONST_STRING_ARRAY
		};
		this.argTypes = argTypes;

		// ������TPP�|�[�g�̏������ɂ�argTypes���p�����邽�߁AargTypes�̏�������łȂ���΂Ȃ�Ȃ�
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "�u�����N�\�f�[�^�쐬" : "Blank Table";
		this.serviceType = Service.SERVICE_TYPE_TABLE_PROC;

		// ������
		description = GeneratorProperty.japanese()?
						"�f�[�^�^�C�v���l���������A�S�Ă̗����󗓂ƂȂ����\���쐬���܂��B�s���A�񐔁A�e��̖��O���w�肵�ĉ������B":
						"This service makes a blank table whose fields don't have any data types, any values. Define Numbers of Record/Field, and a name of each field.";
		

		// �e�C���v�b�g�̖��O
		String[] argNames = {
			japanese ? "�s��" : "Records Quantity",
			japanese ? "��" : "Fields Quantity",
			japanese ? "�e�t�B�[���h�̖���" : "Fields' names"
		};
		this.argNames = argNames;

		// �e�C���v�b�g�ɂ��Ă̐�����
		String[] argDescs = {
			"�s���E�E�E\n" +
			"1�ȏ�̐�������́B�܂��́A\n" +
			"���}�̒����琮�����o�͂�����̂�I��\n" +
			"�i�T�[�r�X�A�������̓t�H�[���Ȃǁj",

			"�񐔁E�E�E\n" +
			"1�ȏ�̐�������́B�܂��́A\n" +
			"���}�̒����琮�����o�͂�����̂�I��\n" +
			"�i�T�[�r�X�A�������̓t�H�[���Ȃǁj",

			"�e�t�B�[���h�̖��́E�E�E\n" +
			"�e��̖��̂����\n" +
			"�񐔂̓��͌�ɓ��͂��ĉ������B"
		};
		this.argDescs = argDescs;

		// ��1�������`���˂Α�2�������`�ł��Ȃ����Ƃ��L�^
		ArrayList<Integer> precursiveArgIndexArray = new ArrayList<Integer>();
		precursiveArgIndexArray.add(new Integer(1));
		hashMapNeededArgForOtherArg.put(new Integer(2), precursiveArgIndexArray);

		// ��O�����̗v�f���͑������̗񐔂��̂��̂Ȃ̂ŁA���̂��Ƃ��n�b�V���}�b�v�ɋL�^
		this.hashMapConstArrayElemNum.put(new Integer(2), new Integer(1));

		// �A�E�g�v�b�g�̎��
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		TppPort fieldNumInputPort = this.getInputPort(1);
		TppConstInt constInt = (TppConstInt)transProc.portTransManager.getInputTppToTppPortIfExists(fieldNumInputPort);

		if(constInt==null) {
			Debug.notice("�u�����N�\�쐬�T�[�r�X�̗񐔂���`����Ă��炸�o��TFD�̗񐔂��킩��Ȃ���ԂŁAgetOutFieldNum()���Ăяo����܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return -1;
		}

		int fieldNum = constInt.value;
		return fieldNum;
	}




	public ArrayList<String> getOutputFieldWebNames() {
		TppPort fieldNamesInputPort = this.getInputPort(2);
		TppConstArrayString constArrayString = (TppConstArrayString)transProc.portTransManager.getInputTppToTppPortIfExists(fieldNamesInputPort);

		if(constArrayString==null) {
			Debug.notice("�u�����N�\�쐬�T�[�r�X�̑�O�����i�t�B�[���h���̔z��j����`����Ă��炸�o��TFD�̃t�B�[���h�����킩��Ȃ���Ԃł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray = constArrayString.valueArray;
		return fieldNameArray;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		if(argIndex==2) {
			return this.getOutFieldNum();	// �񐔂���`����Ă��Ȃ����߂܂��킩��Ȃ��ꍇ�́A-1���Ԃ��Ă���
		}

		// �����ł�-1���Ԃ邪�A�G���[�o�͂Ȃ̂ŃG���[���Ƃ������Ƃ͂킩�邾�낤
		Debug.error("�w�肳�ꂽ�T�[�r�X�����ԍ����z��O�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 4;
	}
}
