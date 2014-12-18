package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstArrayInt extends TransitionProcessPart {
	public ArrayList<Integer> valueArray;
	

	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public TppConstArrayInt(TransitionProcess transProc, int[] argArray) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		setValueArrayByIntArray(argArray);
	}

	
	
	public void setValueArrayByIntArray(int[] intArray) {
		valueArray = new ArrayList<Integer>();
		for(int i=0; i<intArray.length; i++) {
			valueArray.add(new Integer(intArray[i]));
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
	public int getIntNum() {
		return valueArray.size();
	}



	public int getIntValue(int index) {
		return valueArray.get(index);
	}
}