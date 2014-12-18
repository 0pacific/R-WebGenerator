package tpp;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import tpp.port.TppPort;
import transition.TransitionProcess;





public abstract class TransitionProcessPart implements Serializable {
	// ����TPP��������J�ڃv���Z�X
	public TransitionProcess transProc;

	// TPPN
	public int tppn;
	
	// ����TPP�|�[�g�z��@��������̂��A���邢�͂P���Ȃ��̂���TPP�ɂ��
	public ArrayList<TppPort> inputPorts;

	// �o��TPP�|�[�g�i�P�����j�@���邩�ǂ�����TPP�ɂ��
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
	 * TPP�|�[�g�̏�����
	 */
	public abstract void initInputPorts();
	public abstract void initOutputPort();
}
