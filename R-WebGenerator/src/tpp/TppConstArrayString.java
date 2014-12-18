package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstArrayString extends TransitionProcessPart {
	public ArrayList<String> valueArray;
	

	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public TppConstArrayString(TransitionProcess transProc, String[] argArray) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		setValueArrayByStringArray(argArray);
	}



	public void setValueArrayByStringArray(String[] stringArray) {
		valueArray = new ArrayList<String>();
		for(int i=0; i<stringArray.length; i++) {
			valueArray.add(stringArray[i]);
		}
	}

	
	
	
	/*
	 * PURPOSE :
	 * �����|�[�g�̏�����
	 */
	public void initInputPorts() {
		// �萔��\��TPP�Ȃ̂ŁA�����͑��݂��Ȃ��@-> ����ĉ����s��Ȃ�
	}
	public void initOutputPort() {
		this.outputPort = new TppPort(this);
	}
	
	


	/*
	 * PURPOSE :
	 * �z��̗v�f�����擾
	 */
	public int getStringNum() {
		return valueArray.size();
	}



	public String getStringValue(int index) {
		return valueArray.get(index);
	}
}