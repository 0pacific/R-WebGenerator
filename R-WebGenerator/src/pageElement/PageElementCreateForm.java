package pageElement;

import table.SuperTable;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementCreateForm extends PageElement {
	public SuperTable table;

	// Create���郌�R�[�h�̐����w��ł���t�H�[�����ۂ�
	public boolean multiple;

	public WebPage destPage;
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public PageElementCreateForm(WebPage parentPage, SuperTable table, boolean multiple, WebPage destPage) {
		super(parentPage);
		this.table = table;
		this.multiple = multiple;
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
}
