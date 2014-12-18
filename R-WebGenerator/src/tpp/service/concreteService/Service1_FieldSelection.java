package tpp.service.concreteService;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;
import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service1_FieldSelection extends TabularOutputService {
	public Service1_FieldSelection(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		
		// �e�����̎��
		String[] argTypes = {
			Service.IO_TYPE_TFD,
			Service.IO_TYPE_OFFSETS
		};
		this.argTypes = argTypes;

		// ������TPP�|�[�g�̏������ɂ�argTypes���p�����邽�߁AargTypes�̏�������łȂ���΂Ȃ�Ȃ�
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "�t�B�[���h���o" : "Fields Extraction";
		this.serviceType = Service.SERVICE_TYPE_FIELD_PROC;
		
		// ������
		description = GeneratorProperty.japanese()?
						"�\����P�ȏ�̗�����o���A�V���ȕ\�����܂��B���͂���\�ƁA���o�������e��̔ԍ����w�肵�ĉ������B":
						"This service extracts at least 1 fields and make a new table. Define a table to be input and fields to be extracted.";


		// �e�����̖��O
		String[] argNames = {
			japanese ? "�\�f�[�^" : "Tabular Form Data",
			japanese ? "���o�t�B�[���h" : "Fields to extract"
		};
		this.argNames = argNames;
		
		// �e�����ɂ��Ă̐�����
		String[] argDescs = {
			"�\�f�[�^",
			"���o�t�B�[���h"
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

	
	
	
	
	/*
	 * SUMMARY :
	 * �o��TFD�̃t�B�[���h�����擾
	 */
	public int getOutFieldNum() {
		TppPort fieldSelectInputPort = this.getInputPort(1);
		TppConstArrayInt consArrInt = transProc.portTransManager.getInputTppConstArrayIntIfExists(fieldSelectInputPort);

		if(consArrInt==null) {
			Debug.notice(
					getClass().getSimpleName()+"�^�T�[�r�X�́A�t�B�[���h�I���^�T�[�r�X�������܂���`����Ă��Ȃ��̂ŏo�̓t�B�[���h�����킩��܂���B",
					getClass().getSimpleName(),
					Thread.currentThread().getStackTrace()[1].getMethodName()
			);
			return -1;
		}
		
		return consArrInt.getIntNum();
	}




	public ArrayList<String> getOutputFieldWebNames() {
		// ��������TFD�������Ă������TPP�|�[�g
		TppPort firstPort = this.getInputPort(0);

		// ��������TFD�o�͌^TPP���i��`����Ă���΁j�擾����
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null) {
			Debug.notice("�t�B�[���h���o�T�[�r�X���󂯎��������̕\����`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}
		
		ArrayList<String> allFieldWebNames = tfdOutputerFirst.getOutputFieldWebNames();
	
		// �������Łu�ǂ̃t�B�[���h�𒊏o���邩�v�w�肳��Ă���Ȃ�A�����t�B�[���h�̖��O��allFieldWebNames���璊�o����
		TppPort fieldSelectInputPort = this.getInputPort(1);
		TppConstArrayInt consArrInt = transProc.portTransManager.getInputTppConstArrayIntIfExists(fieldSelectInputPort);
		if(consArrInt==null) {
			Debug.notice(
					getClass().getSimpleName()+"�^�T�[�r�X�́A�t�B�[���h�I���^�T�[�r�X�������܂���`����Ă��Ȃ��̂ŏo�̓t�B�[���h�����킩��܂���B",
					getClass().getSimpleName(),
					Thread.currentThread().getStackTrace()[1].getMethodName()
			);
			return null;
		}

		ArrayList<String> selectedFieldWebNames = new ArrayList<String>();
		for(int i=0; i<consArrInt.getIntNum(); i++) {
			int offset = consArrInt.getIntValue(i);
			String fieldName = allFieldWebNames.get(offset);
			selectedFieldWebNames.add(fieldName);
		}

		return selectedFieldWebNames;
	}

	
	
	
	
	public int getElementNumOfConstArrayArg(int argIndex) {
		// ���̃T�[�r�X�ɂ͕K�v�Ȃ��̂ŌĂяo���Ă鎞�_�ł�������
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "�G���[���������܂����B");
		Debug.error("�z��O�̌Ăяo���ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}





	public void initServiceNumber() {
		serviceNumber = 1;
	}
}
