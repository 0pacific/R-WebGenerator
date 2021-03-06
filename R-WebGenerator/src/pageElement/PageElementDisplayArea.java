package pageElement;

import debug.Debug;
import tpp.TfdOutputer;
import tpp.TransitionProcessPart;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementDisplayArea extends PageElement {
	public PageElementDisplayArea(WebPage parentPage) {
		super(parentPage);
	}


	
	
	public TransitionProcessPart getInputTppIfExists(TransitionProcess transProc) {
		PageElementPort inputPort = inputPePortsHashMap.get(transProc);
		TfdOutputer inputTo = transProc.portTransManager.getTfdOutputerForDisplayAreaIfExists(inputPort);

		if(inputTo==null) {
			Debug.notice("まだDisplay Areaに入力するTFD出力型TPPが定義されていないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}
		
		TransitionProcessPart inputTpp = (TransitionProcessPart)inputTo;
		return inputTpp;
	}
	
	



	public void initInputPePorts(TransitionProcess transProc) {
		PageElementPort inputPort = new PageElementPort(this);
		inputPePortsHashMap.put(transProc, inputPort);
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		PageElementPort outputPort = new PageElementPort(this);
		outputPePortsHashMap.put(transProc, outputPort);
	}
}
