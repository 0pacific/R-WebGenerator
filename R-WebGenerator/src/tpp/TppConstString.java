package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstString extends TransitionProcessPart {
	public String value;
	

	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public TppConstString(TransitionProcess transProc, String value) {
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
	
	


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}