package pageElement;

import table.SuperTable;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementDeleteForm extends PageElement {
	public SuperTable table;
	public WebPage destPage;
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public PageElementDeleteForm(WebPage parentPage, SuperTable table, WebPage destPage) {
		super(parentPage);
		this.table = table;
		this.destPage = destPage;
	}





	public void initInputPePorts(TransitionProcess transProc) {
		// ���̓y�[�W�G�������g�|�[�g�͑��݂��Ȃ�
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// �o�̓|�[�g�݂̂̏������ƂȂ�
		PageElementPort outputPort = new PageElementPort(this);
		outputPePortsHashMap.put(transProc, outputPort);
	}

	public SuperTable getTable() {
		return table;
	}
}
