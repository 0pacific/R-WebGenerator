package tpp;


import java.util.ArrayList;

import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppConstArrayInt extends TransitionProcessPart {
	public ArrayList<Integer> valueArray;
	

	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
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
	public int getIntNum() {
		return valueArray.size();
	}



	public int getIntValue(int index) {
		return valueArray.get(index);
	}
}