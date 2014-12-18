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
	 * コンストラクタ
	 */
	public PageElementDeleteForm(WebPage parentPage, SuperTable table, WebPage destPage) {
		super(parentPage);
		this.table = table;
		this.destPage = destPage;
	}





	public void initInputPePorts(TransitionProcess transProc) {
		// 入力ページエレメントポートは存在しない
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// 出力ポートのみの初期化となる
		PageElementPort outputPort = new PageElementPort(this);
		outputPePortsHashMap.put(transProc, outputPort);
	}

	public SuperTable getTable() {
		return table;
	}
}
