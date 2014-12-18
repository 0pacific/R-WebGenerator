package tpp.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import authority.AuthoritySet;

import debug.Debug;

import role.NormalRole;
import tpp.TransitionProcessPart;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.concreteService.Service1_FieldSelection;
import tpp.service.concreteService.Service2_FieldSumming;
import tpp.service.concreteService.Service3_TableJoin;
import tpp.service.concreteService.Service4_BlankTable;
import tpp.service.concreteService.Service5_PickRecord;
import tpp.service.concreteService.Service6_DateListColumn;
import transition.TransitionProcess;

public abstract class Service extends TransitionProcessPart {
	// �C���v�b�g�E�A�E�g�v�b�g�̃^�C�v��\���萔
	public static final String IO_TYPE_CONST_INT = "CONST_INT";
	public static final String IO_TYPE_CONST_STRING = "CONST_STRING";
	public static final String IO_TYPE_CONST_INT_ARRAY = "CONST_INT_ARRAY";
	public static final String IO_TYPE_CONST_STRING_ARRAY = "CONST_STRING_ARRAY";
	public static final String IO_TYPE_DATETIME = "IO_TYPE_DATETIME";
	public static final String IO_TYPE_DATE = "IO_TYPE_DATE";
	public static final String IO_TYPE_TIME = "IO_TYPE_TIME";
	public static final String IO_TYPE_OFFSETS = "CONST_OFFSETS";
	public static final String IO_TYPE_TFD = "TFD";

	// �T�[�r�X�̎�ނ�\���萔
	public static String SERVICE_TYPE_CALC = GeneratorProperty.japanese()?"�l�����Z":"Arithmetic Operation";				// �l�����Z
	public static String SERVICE_TYPE_FIELD_PROC = GeneratorProperty.japanese()?"�t�B�[���h����":"Field Handling";;		// �t�B�[���h�̏����i�t�B�[���h���o�Ȃǁj
	public static String SERVICE_TYPE_TABLE_PROC = GeneratorProperty.japanese()?"�e�[�u������":"Table Handling";		// �e�[�u���S�̂����������i�\�����Ȃǁj

	// �����������͂���ۂɑ��̈����ɓ����Ă���l��ǂݏo�����Ƃ��K�v�ȃP�[�X������B
	// ��F�u�����N�\�쐬�T�[�r�X�ő�O�����̃t�B�[���h���z�����͂���ہA�t�B�[���h���͂������ƍl����Ƒ������̗񐔂�ǂ܂Ȃ���΂Ȃ�Ȃ��B
	// �����������P�[�X�Ō�҂̈���������`�ł��邱�Ƃ��������߂ɁA���̒萔���֐�����Ԃ��Ă���
	public static final int ARG_NOT_YET_DEFINED = 38294825;
	
	
	// �T�[�r�X��
	public String serviceName;

	// �T�[�r�X�ԍ��i���s�v���O�����̕��ŃT�[�r�X�ɂ��Ă���ԍ��ƍ��킹�邱�Ɓj
	public int serviceNumber;
	
	// �T�[�r�X�̎��
	protected String serviceType;
	
	// �T�[�r�X����
	protected String description;

	// �e�T�[�r�X�����̖��O�A��ށA����
	public String[] argNames;
	public String[] argTypes;
	public String[] argDescs;
	
	// �o�͂̎��
	public String outputType;
	
	// ����������t�B�[���h�I��ΏۂƂ��ĕʂ�TFD�^������K�v�Ƃ���ꍇ�A������L�^����n�b�V���}�b�v
	// �t�B�[���h�I�������̔ԍ�, TFD�^�����̔ԍ��@�Ƃ����̂��g
	public HashMap<Integer, Integer> hmFldSel = new HashMap<Integer, Integer>();

	// ���������STRING�萔�z��Ȃ���INT�萔�z��ł���A�����̗v�f�����ςő���INT���m�萔�^�����̒l�Ƃ����P�[�X�����邽�߁A
	// ���̑Ή����L�^����̂Ɏg����n�b�V���}�b�v�B�L�[�͑O�҂̈����̔ԍ��A�l�͌�҂̈����̔ԍ�
	public HashMap<Integer, Integer> hashMapConstArrayElemNum = new HashMap<Integer, Integer>();

	// ���������ҏW���`���邽�߂ɁA�܂����̂�������i�P�ȏ�j����`����Ă���K�v������P�[�X�����݂���B
	// ���̑Ή����L�^����̂Ɏg����n�b�V���}�b�v�B�L�[�͑O�҂̈����̔ԍ��A�l�͌�҂̈����B�̔ԍ��z��
	public HashMap<Integer, ArrayList<Integer>> hashMapNeededArgForOtherArg = new HashMap<Integer, ArrayList<Integer>>();
	
	
	
	
	
	
	public Service(TransitionProcess transProc) {
		super(transProc);
		initServiceNumber();

		boolean japanese = GeneratorProperty.japanese();
	}
	
	
	
	

	// argIndex�ԁi0�n�܂�j�̈������`������ɂ�����`�ł��Ȃ��S�Ă̈����̔ԍ���z��ŕԂ�
	public ArrayList<Integer> getFollowingArgIndexArray(int argIndex) {
		// �Ō�ɕԋp����z��
		ArrayList<Integer> argIndexArray = new ArrayList<Integer>();
		
		Iterator iterator = hashMapNeededArgForOtherArg.keySet().iterator();
		while(iterator.hasNext()) {
			Integer thisArgIndex = (Integer)iterator.next();
			ArrayList<Integer> precursiveArgIndexes = hashMapNeededArgForOtherArg.get(thisArgIndex);
			for(int i=0; i<precursiveArgIndexes.size(); i++) {
				Integer idx = precursiveArgIndexes.get(i);
				if(idx.intValue()==argIndex) {
					argIndexArray.add(new Integer(thisArgIndex));
				}
			}
		}

		return argIndexArray;
	}
	
	
	
	
	/*
	 * SUMMARY :
	 * ������ԍ��Ŏw�肵�A���̈������`���Ȃ���Β�`�ł��Ȃ��S�����ɂ��āA����`�ςł���΁��폜����
	 */
	public void removeFollowingArgsIfExist(int argIndex) {
		ArrayList<Integer> followingArgIndexArray = getFollowingArgIndexArray(argIndex);
		for(int i=0; i<followingArgIndexArray.size(); i++) {
			Integer followingArgIndex = followingArgIndexArray.get(i);
			Debug.today("�T�[�r�X�u"+serviceName+"�v��"+argIndex+"�ԁi0-start)�̈������폜���ꂽ�̂ŁA�㑱����"+followingArgIndex+"�ԁi0-start�j�̈������폜���܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			removeArgIfDefined(followingArgIndex.intValue());
		}
	}
	
	
	


	/*
	 * PURPOSE :
	 * �����̎�ނ�\��������iIO_TYPE_CONST_INT�Ȃǁj���A�f�B�x���b�p�[�����̕\���i"����"�Ȃǁj�ɕϊ�
	 */
	public static String convArgTypeExp(String argType) {
		// �e��ނɉ����āA�f�B�x���b�p�[���������₷���\����Ԃ�
		if(argType.equals(Service.IO_TYPE_CONST_INT)) {
			return "�萔�i�����j";
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING)) {
			return "�萔�i������j";
		}
		else if(argType.equals(Service.IO_TYPE_CONST_INT_ARRAY)) {
			return "�萔�i�����z��j";
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING_ARRAY)) {
			return "�萔�i������z��j";
		}
		else if(argType.equals(Service.IO_TYPE_OFFSETS)) {
			return "�P�̃e�[�u������t�B�[���h���P�ȏ�I��";
		}
		else if(argType.equals(Service.IO_TYPE_TFD)) {
			return "�\�`���f�[�^�i�e�[�u���Ǐo������A�\���o�͂���T�[�r�X���w��j";
		}

		// �G���[
		Debug.error("�s���ȃT�[�r�X��ނł�", "Service", Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肵����ނ̃T�[�r�X�B�́A���O�ꗗ��ԋp����
	 */
	public static String[] getServNamesByCateg(String categName) {
		boolean japanese = GeneratorProperty.japanese();
		
		String[] servNames = new String[0];
		
Debug.out(categName+"����[��");
Debug.out(Service.SERVICE_TYPE_TABLE_PROC);
Debug.out(Service.SERVICE_TYPE_FIELD_PROC);
Debug.out(Service.SERVICE_TYPE_CALC);
		// �e�[�u������
		if(categName.equals(Service.SERVICE_TYPE_TABLE_PROC)) {
			String[] list = {
					japanese?"�\����":"Join",
					japanese?"�u�����N�\�f�[�^�쐬":"Blank Table",
					japanese?"�萔���R�[�h���o":"Several Records Extraction"
			};
			servNames = list;
		}
		// �t�B�[���h����
		else if(categName.equals(Service.SERVICE_TYPE_FIELD_PROC)) {
			String[] list = {
					japanese?"�t�B�[���h���o":"Fields Extraction",
					japanese?"���t�A�ԃt�B�[���h�쐬":"Date Range Field"
			};
			servNames = list;
		}
		// �l�����Z
		else if(categName.equals(Service.SERVICE_TYPE_CALC)) {
			String[] list = {
					japanese?"�t�B�[���h���v�l�v�Z":"Fields Summing"
			};
			servNames = list;
		}
		// �s���ȃT�[�r�X�J�e�S��
		else {
			Debug.error("�s���ȃT�[�r�X��ނł�", "Service", Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}
		
		return servNames;
	}

	
	
	
	
	public boolean removeArgIfDefined(int argIndex) {
		TppPort argPort = getInputPort(argIndex);
		PortTransManager ptm = transProc.portTransManager;

		// �������폜�i���݂���Ƃ͌���Ȃ��j
		boolean remove = ptm.removeOnePortTransByEndPortIfExists(argPort);
		if(!remove) {
			Debug.notice("��"+argIndex+"�����i0�n�܂�j����`�ςȂ�폜���悤�Ƃ��܂������A����`�������悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		/*
		 * ���������݂����̂ō폜����
		 */

		Debug.notice("��"+argIndex+"�����i0�n�܂�j����`�ς������̂ō폜���܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// ���̈������`���Ȃ���Β�`�ł��Ȃ������i�㑱��������ƌĂԁj��S�Đ􂢏o���B�����Ē�`����Ă���Ȃ�폜����
		removeFollowingArgsIfExist(argIndex);
		
		return true;
	}
	
	
	
	
	public boolean argDefined(int argIndex) {
		TppPort argPort = getInputPort(argIndex);
		PortTrans portTrans = transProc.portTransManager.getOnePortTransByEndPortIfExists(argPort);

		// ��`��
		if(portTrans instanceof PortTrans) {
			return true;
		}

		// ����`
		return false;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ������J�ڃv���Z�X�ɂ����āA�w�肵���ԍ��̈����|�[�g�֊��Ƀf�[�^���Ή����Ă��邩��Ԃ�
	 * �i�܂�A���������Ɏw�肳��Ă��邩��������j
	 */
	public boolean inputPortReserved(int inputPortIndex) {
		TppPort inputPort = this.getInputPort(inputPortIndex);
		PortTransManager ptm = getTransProc().getPortTransManager();
		boolean reserved = ptm.inputReserved(inputPort);
		return reserved;
	}
	
	
	
	
	
	
	public static Service getInstanceByServiceName(TransitionProcess tp, String serviceName) {
		boolean japanese = GeneratorProperty.japanese();
		
		Service cs = null;
		if(serviceName.equals(japanese?"�t�B�[���h���o":"Fields Extraction")) {
			cs = new Service1_FieldSelection(tp);
		}
		else if(serviceName.equals(japanese?"�t�B�[���h���v�l�v�Z":"Fields Summing")) {
			cs = new Service2_FieldSumming(tp);
		}
		else if(serviceName.equals(japanese?"�\����":"Join")) {
			cs = new Service3_TableJoin(tp);
		}
		else if(serviceName.equals(japanese?"�u�����N�\�f�[�^�쐬":"Blank Table")) {
			cs = new Service4_BlankTable(tp);
		}
		else if(serviceName.equals(japanese?"�萔���R�[�h���o":"Several Records Extraction")) {
			cs = new Service5_PickRecord(tp);
		}
		else if(serviceName.equals(japanese?"���t�A�ԃt�B�[���h�쐬":"Date Range Field")) {
			cs = new Service6_DateListColumn(tp);
		}
		else {
			Debug.notice("�w�肳�ꂽ���O�̃T�[�r�X���Ȃ��悤�ł��B", "Service", Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		return cs;
	}

	public String getName() {
		return serviceName;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �C���v�b�g�Ƃ��ĂP�ȏ�̎w��^�C�v�f�[�^���󂯎��T�[�r�X���ۂ���Ԃ�
	 * 
	 * NOTICE :
	 * �E�w��^�C�v�ɍ��v����C���v�b�g�B�����ɖ��܂��Ă��邩�ǂ����͊֒m���Ȃ�
	 * �EdataType�ɂ́AService�N���X��static�萔���w�肷��
	 */
	public boolean canReceiveData(String dataType) {
		for(int i=0; i<argTypes.length; i++) {
			String type = argTypes[i];
			if(type.equals(dataType)) {
				return true;
			}
		}

		return false;
	}

	
	
	
	public int getArgIndexByArgPort(TppPort argPort) {
		for(int i=0; i<getArgNum(); i++) {
			if(getInputPort(i)==argPort) {
				return i;
			}
		}

		JOptionPane.showMessageDialog(MainFrame.getInstance(), "�G���[���������܂����B");
		Debug.error("�����|�[�g��������܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * �C���v�b�g�Ƃ��Ďw��^�C�v�f�[�^�����ƂP�ł���`�ł��邩�ۂ��A��Ԃ�
	 */
	public boolean canReceiveMoreData(String dataType) {
		// �w��^�C�v�f�[�^���P���C���v�b�g�ł��Ȃ��T�[�r�X�ł���
		if(!canReceiveData(dataType)) {
			return false;
		}

		// �e�C���v�b�g�|�[�g�ɂ��ČJ��Ԃ�
		for(int i=0; i<getArgNum(); i++) {
			// i�Ԗڂ̃C���v�b�g�͎w��^�C�v�ł���A���܂����߂��Ă��Ȃ�
			if(argTypes[i].equals(dataType) && !inputPortReserved(i)) {
				return true;
			}
		}

		// �w��^�C�v�̃f�[�^���P�ȏ�C���v�b�g�ł���T�[�r�X�����A���ɑS�Ė��܂��Ă���
		return false;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �C���v�b�g�|�[�g���������i���͊e�T�[�r�X�ɂ��Ă��炩���ߒ�܂������́j
	 */
	public void initInputPorts() {
		inputPorts = new ArrayList<TppPort>();
		for(int i=0; i<argTypes.length; i++) {
			inputPorts.add(new TppPort(this));
		}
	}
	public void initOutputPort() {
		this.outputPort = new TppPort(this);
	}



	// �ȉ��AGetter&Setter
	
	public String getDescription() {
		return description;
	}

	/*
	 * PURPOSE :
	 * �����̐����擾
	 * 
	 * NOTICE :
	 * ��`�ς̈����̐����擾����킯�ł͂Ȃ�
	 */
	public int getArgNum() {
		return argTypes.length;
	}
	
	public String[] getArgNames() {
		return argNames;
	}

	public String[] getArgTypes() {
		return argTypes;
	}

	/*
	 * PURPOSE :
	 * ���������ꗗ�擾
	 */
	public String[] getArgDescs() {
		return argDescs;
	}

	
	
	
	public void removeService() {
		// �S�Ă̈����ɂ��āA���̈�������`�ςȂ�|�[�g�g�����X�~�b�V�������폜
		for(int i=0; i<getArgNum(); i++) {
			removeArgIfDefined(i);
		}

		// ���̃T�[�r�X�̏o�͂��󂯎���Ă���|�[�g�g�����X�~�b�V������S�č폜
		// ���̂Ƃ��A�󂯎���Ă���̂��ʂ̃T�[�r�X�ł������ꍇ�A���̕ʂ̃T�[�r�X�̌㑱����������폜���Ȃ���΂Ȃ�Ȃ����A�����PortTransManager�C���X�^���X�̕��ł���Ă����
		TppPort outPort = getOutputPort();
		this.transProc.portTransManager.removeAllPortTransByStartPort(outPort);

		this.transProc.removeTpp(this);
	}
	

	
	
	public TransitionProcessPart getArgTppIfExists(int argIndex) {
		TppPort argPort = this.getInputPort(argIndex);
		TransitionProcessPart inputTpp = this.transProc.portTransManager.getInputTppIfExists(argPort);
		if(inputTpp==null) {
			Debug.notice("�T�[�r�X�u" + this.serviceName + "�v�̑�" + argIndex + "�����i0�n�܂�j��TPP������`�̂悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		return inputTpp;
	}
	



	public abstract int getElementNumOfConstArrayArg(int argIndex);
	public abstract void initServiceNumber();
}
