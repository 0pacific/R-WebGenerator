package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstInt extends TransitionProcessPart {
	public int value;
	

	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public TppConstInt(TransitionProcess transProc, int value) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		this.value = value;
	}



	/*
	 * PURPOSE :
	 * 引数ポートの初期化
	 */
	public void initInputPorts() {
		// 定数を表すTPPなので、引数は存在しない　-> よって何も行わない
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