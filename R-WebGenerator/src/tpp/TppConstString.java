package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstString extends TransitionProcessPart {
	public String value;
	

	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public TppConstString(TransitionProcess transProc, String value) {
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
	
	


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}