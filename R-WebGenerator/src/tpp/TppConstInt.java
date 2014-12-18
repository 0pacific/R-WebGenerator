package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstInt extends TransitionProcessPart {
	public int value;
	

	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public TppConstInt(TransitionProcess transProc, int value) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		this.value = value;
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
	
	


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}