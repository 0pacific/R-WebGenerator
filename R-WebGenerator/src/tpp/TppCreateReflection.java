package tpp;

import java.util.ArrayList;

import table.SuperTable;
import tpp.port.TppPort;
import tpp.portTrans.PortTransManager;
import transition.TransitionProcess;

public class TppCreateReflection extends TransitionProcessPart implements TfdOutputer {
	public SuperTable table;
	public TfdOutputer tfdOutputer;
	
	public TppCreateReflection(TransitionProcess transProc, SuperTable table) {
		super(transProc);
		initInputPorts();
		initOutputPort();
		this.table = table;
	}

	public void initInputPorts() {
		// ����TPP�|�[�g�͂P�iTFD���󂯎��j
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
	}

	public TppPort getTfdInputPort() {
		return this.inputPorts.get(0);
	}
	
	public void initOutputPort() {
		// �쐬�������R�[�h�����̂܂ܒ��o�������ꍇ�ACreate���t���N�V��������̏o�͂��K�v�Ȃ̂ŁA�o��TPP�|�[�g��������
		this.outputPort = new TppPort(this);
	}

	public int getOutFieldNum() {
		return table.getFieldNum();
	}

	public ArrayList<String> getOutputFieldWebNames() {
		return table.getFieldNameArray();
	}

	
	
	
	
	public TfdOutputer getInputTfdOutputerIfExists() {
		TppPort tfdInputPort = this.getTfdInputPort();
		PortTransManager ptm = this.transProc.portTransManager;

		TfdOutputer tfdOutputer = ptm.getInputTfdOutputerTppIfExists(tfdInputPort);	// ����TFD����`����ĂȂ��Ȃ�null��������
		return tfdOutputer;
	}





	public void removeFromTransProc() {
		// �܂��A����Create���t���N�V�������g���폜
		this.transProc.removeTpp(this);
		
		PortTransManager ptm = this.transProc.portTransManager;
		
		// TFD����̃|�[�g�g�����X�~�b�V�������폜
		ptm.removeAllPortTransByEndPort(getTfdInputPort());

		// ����Create���t���N�V�����̏o�͂��󂯎��S�Ẵ|�[�g�g�����X�~�b�V�������폜
		// �T�[�r�X�̈����Ƃ��Ď󂯎���Ă����ꍇ�́A�㑱��������������ō폜�����
		ptm.removeAllPortTransByStartPort(outputPort);
	}
}
