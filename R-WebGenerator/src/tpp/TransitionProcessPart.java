package tpp;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import tpp.port.TppPort;
import transition.TransitionProcess;





public abstract class TransitionProcessPart implements Serializable {
	// このTPPが属する遷移プロセス
	public TransitionProcess transProc;

	// TPPN
	public int tppn;
	
	// 入力TPPポート配列　いくつあるのか、あるいは１つもないのかはTPPによる
	public ArrayList<TppPort> inputPorts;

	// 出力TPPポート（１つだけ）　あるかどうかはTPPによる
	public TppPort outputPort;

	
	
	
	
	public TransitionProcessPart(TransitionProcess transProc) {
		this.transProc = transProc;
	}

	public TransitionProcess getTransProc() {
		return transProc;
	}

	public int getTppo() {
		return transProc.getTppo(this);
	}
	
	public TppPort getInputPort(int index) {
		return inputPorts.get(index);
	}


	public TppPort getOutputPort() {
		return outputPort;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * TPPポートの初期化
	 */
	public abstract void initInputPorts();
	public abstract void initOutputPort();
}
