package pageElement;

import java.util.ArrayList;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;

public class PageElementTableDisplay extends PageElement {
	SuperTable table;
	ArrayList<Integer> limitedOffsets;

	
	
	
	
	public PageElementTableDisplay(WebPage parentPage, SuperTable table, int[] limitedOffsets) {
		super(parentPage);
		this.table = table;
		this.setLimitedOffsets(limitedOffsets);
	}

	public SuperTable getTable() {
		return table;
	}
	
	public void setTable(SuperTable table) {
		this.table = table;
	}

	public int getLimitedOffset(int index) {
		return limitedOffsets.get(index);
	}

	public ArrayList<Integer> getLimitedOffsets() {
		return limitedOffsets;
	}

	public int getLimitedOffsetNum() {
		return limitedOffsets.size();
	}
	
	public void setLimitedOffsets(int[] offsetArray) {
		ArrayList<Integer> limitedOffsetArray = new ArrayList<Integer>();
		for(int i=0; i<offsetArray.length; i++) {
			limitedOffsetArray.add(new Integer(offsetArray[i]));
		}
		this.limitedOffsets = limitedOffsetArray;
	}


	
	public void initInputPePorts(TransitionProcess transProc) {
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		PageElementPort outputPort = new PageElementPort(this);
		outputPePortsHashMap.put(transProc, outputPort);
	}
}
