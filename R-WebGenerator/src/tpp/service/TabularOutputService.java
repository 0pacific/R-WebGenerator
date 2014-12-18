package tpp.service;

import java.util.ArrayList;

import tpp.TfdOutputer;
import transition.TransitionProcess;

/*
 * TFD���o�͂���T�[�r�X
 */
public abstract class TabularOutputService extends Service implements TfdOutputer {
	public TabularOutputService(TransitionProcess transProc) {
		super(transProc);
	}
	
	public abstract int getOutFieldNum();

	public abstract ArrayList<String> getOutputFieldWebNames();
}
