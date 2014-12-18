package tpp;

import java.util.ArrayList;

import table.SuperTable;
import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppTableReading extends TransitionProcessPart implements TfdOutputer{
	private SuperTable table;

	
	
	
	
	public TppTableReading(TransitionProcess transProc, SuperTable table) {
		super(transProc);
		this.table = table;
		initInputPorts();
		initOutputPort();
	}


	
	/*
	 * PURPOSE :
	 * TPPポートの初期化
	 */
	public void initInputPorts() {
		// テーブルリーディングに引数はないので、何もしない
	}
	public void initOutputPort() {
		this.outputPort = new TppPort(this);
	}
	
	public SuperTable getTable() {
		return table;
	}

	public int getOutFieldNum() {
		return table.getFieldNum();
	}

	public ArrayList<String> getOutputFieldWebNames() {
		return table.getFieldNameArray();
	}
}
