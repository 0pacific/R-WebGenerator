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
		// ����TPP�|�[�g�͂P�iUpdate�t�H�[�����󂯎��j
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
	}
	public void initOutputPort() {
		// �����̂Ƃ���A�o�̓|�[�g�͂Ȃ��ƍl���Ă���iDDT Load���g���΂悢�Ǝv���̂Łj
	}
}
