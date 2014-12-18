package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstArrayString extends TransitionProcessPart {
	public ArrayList<String> valueArray;
	

	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
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
	 * 引数ポートの初期化
	 */
	public void initInputPorts() {
		// 定数を表すTPPなので、引数は存在しない　-> よって何も行わない
	}
	public void initOutputPort() {
		this.outputPort = new TppPort(this);
	}
	
	


	/*
	 * PURPOSE :
	 * 配列の要素数を取得
	 */
	public int getStringNum() {
		return valueArray.size();
	}



	public String getStringValue(int index) {
		return valueArray.get(index);
	}
}