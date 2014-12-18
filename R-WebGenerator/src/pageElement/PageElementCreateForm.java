package pageElement;

import table.SuperTable;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementCreateForm extends PageElement {
	public SuperTable table;

	// Createするレコードの数を指定できるフォームか否か
	public boolean multiple;

	public WebPage destPage;
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public PageElementCreateForm(WebPage parentPage, SuperTable table, boolean multiple, WebPage destPage) {
		super(parentPage);
		this.table = table;
		this.multiple = multiple;
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
}
