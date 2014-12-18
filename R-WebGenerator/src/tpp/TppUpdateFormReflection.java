package tpp;

import java.util.ArrayList;

import pageElement.PageElementCreateForm;
import pageElement.PageElementUpdateForm;
import tpp.port.TppPort;
import transition.TransitionProcess;

public class TppUpdateFormReflection extends TransitionProcessPart {
	public PageElementUpdateForm updateForm;

	
	
	
	
	public TppUpdateFormReflection(TransitionProcess transProc, PageElementUpdateForm updateForm) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		this.updateForm = updateForm;
	}

	
	
	
	
	public void initInputPorts() {
		// 入力TPPポートは１つ（Updateフォームを受け取る）
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
	}
	public void initOutputPort() {
		// ◆今のところ、出力ポートはなしと考えている（DDT Loadを使えばよいと思うので）
	}
}
