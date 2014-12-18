package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service2_FieldSumming extends TabularOutputService implements ConstArrayElementNumGetter {
	public Service2_FieldSumming(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// �e�C���v�b�g�̎��
		String[] argTypes = {
				Service.IO_TYPE_TFD,
				Service.IO_TYPE_OFFSETS,
				Service.IO_TYPE_CONST_STRING
		};
		this.argTypes = argTypes;

		// ������TPP�|�[�g�̏������ɂ�argTypes���p�����邽�߁AargTypes�̏�������łȂ���΂Ȃ�Ȃ�
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "�t�B�[���h���v�l�v�Z" : "Fields Summing";
		this.serviceType = Service.SERVICE_TYPE_CALC;

		// ������
		description = GeneratorProperty.japanese()?
						"�\�̂P�ȏ�̗�̍��v��\���A�V���ȂP�̗�����܂��B���͂���\�ƁA���v�������e��̔ԍ����w�肵�ĉ������B":
						"This service makes a new field which stores summed values of at least 1 fields of a table.Define a table to be input and fields to be summed up.";
		

		// �e�C���v�b�g�̖��O
		String[] argNames = {
			japanese ? "�e�[�u��" : "Table",
			japanese ? "�퉉�Z�t�B�[���h" : "Fields to be summed up",
			japanese ? "���v�l�t�B�[���h��" : "Result Field's name"
		};
		this.argNames = argNames;

		// �e�C���v�b�g�ɂ��Ă̐�����
		String[] argDescs = {
			"�\�f�[�^�E�E�E\n" +
			"���}�̒�����\���o�͂�����̂�I��\n" +
			"�i�e�[�u���Ǐo�����A�T�[�r�X�Ȃǁj",

			"�퉉�Z�t�B�[���h�E�E�E\n" +
			"�ǂ̃t�B�[���h�̒l�����v���邩�A\n" +
			"�P�ȏ�̃t�B�[���h��I��",

			"���v�t�B�[���h���E�E�E\n" +
			"�o�͂���\�f�[�^�i�P�t�B�[���h�j\n" +
			"�̃t�B�[���h�������"
		};
		this.argDescs = argDescs;

		// ��0�������`���˂Α�1�������`�ł��Ȃ����Ƃ��L�^
		ArrayList<Integer> precursiveArgIndexArray = new ArrayList<Integer>();
		precursiveArgIndexArray.add(new Integer(0));
		hashMapNeededArgForOtherArg.put(new Integer(1), precursiveArgIndexArray);

		// �������͑�����TFD�̃t�B�[���h�I�t�Z�b�g�z��Ȃ̂ŁA���̂��Ƃ��n�b�V���}�b�v�ɋL�^
		hmFldSel.put(new Integer(1), new Integer(0));

		// �A�E�g�v�b�g�̎��
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		return 1;
	}




	public ArrayList<String> getOutputFieldWebNames() {
		/*
		 * ��������
		 */
		return null;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		if(argIndex==2) {
			return 1;
		}

		Debug.error("�w�肳�ꂽ�T�[�r�X�����ԍ����z��O�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 2;
	}
}
