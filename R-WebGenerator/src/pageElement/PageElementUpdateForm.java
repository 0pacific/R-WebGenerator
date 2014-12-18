package pageElement;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;

public class PageElementUpdateForm extends PageElement {
	SuperTable table;
	int[] limitedOffsets;
	public WebPage destPage;
	
	
	
	
	public PageElementUpdateForm(WebPage parentPage, SuperTable table, int[] limitedOffsets, WebPage destPage) {
		super(parentPage);
		this.table = table;
		this.limitedOffsets = limitedOffsets;
		this.destPage = destPage;
	}

	public SuperTable getTable() {
		return table;
	}
	
	public void setTable(SuperTable table) {
		this.table = table;
	}

	public int getLimitedOffset(int index) {
		return limitedOffsets[index];
	}

	public int[] getLimitedOffsets() {
		return limitedOffsets;
	}

	public int getLimitedOffsetNum() {
		return limitedOffsets.length;
	}
	
	public void setLimitedOffsets(int[] offsetArray) {
		this.limitedOffsets = offsetArray;
	}

	public void setDestPage(WebPage destPage) {
		this.destPage = destPage;
	}

	
	public void initInputPePorts(TransitionProcess transProc) {
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		PageElementPort outputPort = new PageElementPort(this);
		outputPePortsHashMap.put(transProc, outputPort);
	}
}
