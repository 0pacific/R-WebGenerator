package view.transProcEdit.fieldSelect;

import java.io.Serializable;

import javax.swing.JButton;

import tpp.service.Service;

public class ButtonFieldSelect extends JButton implements Serializable {
	public Service service;
	public int argIndex;
	
	public ButtonFieldSelect(Service servuce, int argIndex) {
		this.service = service;
		this.argIndex = argIndex;
	}
}
