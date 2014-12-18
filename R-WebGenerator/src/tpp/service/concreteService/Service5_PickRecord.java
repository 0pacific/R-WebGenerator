package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service5_PickRecord extends TabularOutputService {
	public Service5_PickRecord(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// �e�C���v�b�g�̎��
		String[] argTypes = {
				Service.IO_TYPE_TFD,
				Service.IO_TYPE_CONST_INT,
		};
		this.argTypes = argTypes;

		// ������TPP�|�[�g�̏������ɂ�argTypes���p�����邽�߁AargTypes�̏�������łȂ���΂Ȃ�Ȃ�
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "�萔���R�[�h���o" : "Several Records Extraction";
		this.serviceType = Service.SERVICE_TYPE_TABLE_PROC;

		// ������
		description = GeneratorProperty.japanese()?
						"�\�f�[�^���󂯎��A�ォ��w�肵���������̃��R�[�h�𒊏o���ĐV���ȕ\�f�[�^�Ƃ��ďo�͂��܂��B�\�f�[�^�ƁA���o���郌�R�[�h�����w�肵�ĉ������B":
						"This service receives a table and extract several records on the top, then output a new table. Define a tabular form data and number of records.";

		// �e�C���v�b�g�̖��O
		String[] argNames = {
			japanese ? "�\�f�[�^" : "Tabular Form Data",
			japanese ? "���R�[�h��" : "Quantity of records to be extracted"
		};
		this.argNames = argNames;

		// �e�C���v�b�g�ɂ��Ă̐�����
		String[] argDescs = {
			"�\�f�[�^�E�E�E\n" +
			"���}�̒�����\���o�͂�����̂�I��\n" +
			"�i�e�[�u���Ǐo�����A�T�[�r�X�Ȃǁj",

			"���R�[�h���E�E�E\n" +
			"1�ȏ�̐��������"
		};
		this.argDescs = argDescs;

		// �A�E�g�v�b�g�̎��
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		// �������̓���TPP�|�[�g
		TppPort firstPort = this.getInputPort(0);

		// ��������TFD�o�͌^TPP���i��`����Ă���΁j�擾����
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null) {
			Debug.notice("�萔���R�[�h���o�T�[�r�X���󂯎��\����`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return -1;
		}
		
		return tfdOutputerFirst.getOutFieldNum();
	}




	public ArrayList<String> getOutputFieldWebNames() {
		// �������������Ă������TPP�|�[�g
		TppPort firstPort = this.getInputPort(0);

		// ��������TFD�o�͌^TPP���i��`����Ă���΁j�擾����
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null) {
			Debug.notice("�萔���R�[�h���o�T�[�r�X���󂯎��\����`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray1 = tfdOutputerFirst.getOutputFieldWebNames();
		return fieldNameArray1;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		// ���̃T�[�r�X�ɂ͕K�v�Ȃ��̂ŌĂяo���Ă鎞�_�ł�������
		Debug.error("�z��O�̌Ăяo���ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 5;
	}
}
