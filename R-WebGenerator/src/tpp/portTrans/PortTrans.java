package tpp.portTrans;

import java.io.Serializable;

import tpp.port.DataPort;

public class PortTrans implements Serializable {
	private DataPort startPort;
	private DataPort endPort;
	
	public PortTrans(DataPort startPort, DataPort endPort) {
		this.startPort = startPort;
		this.endPort = endPort;
	}

	public DataPort getStartPort() {
		return startPort;
	}

	public DataPort getEndPort() {
		return endPort;
	}

}
