package tpp;

import java.util.ArrayList;

import pageElement.PageElementCreateForm;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppCreateFormReflection extends TransitionProcessPart  implements TfdOutputer {
	public PageElementCreateForm createForm;
	
	
	public TppCreateFormReflection(TransitionProcess transProc, PageElementCreateForm createForm) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		this.createForm = createForm;
	}




	public void initInputPorts() {
		// 入力TPPポートは１つ（Createフォームを受け取る）
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
	}

	public void initOutputPort() {
		this.outputPort = new TppPort(this);
	}

	public int getOutFieldNum() {
		return createForm.table.getFieldNum();
	}

	public ArrayList<String> getOutputFieldWebNames() {
		return createForm.table.getFieldNameArray();
	}
}
