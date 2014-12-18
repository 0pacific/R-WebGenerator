package view.transProcEdit.rightSubPanels;

import java.io.Serializable;

import javax.swing.JButton;

import tpp.port.DataPort;
import tpp.port.TppPort;
import tpp.service.Service;

public class ButtonServArgEdit extends JButton implements Serializable{
	public int serviceArgIndex;
	public TppPort servArgPort;
	public int argIndex;
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public ButtonServArgEdit(Service serv, int argPortIndex) {
		this.serviceArgIndex = argPortIndex;
		this.servArgPort = serv.getInputPort(argPortIndex);
		this.argIndex = argPortIndex;
	}
}
