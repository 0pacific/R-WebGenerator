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
		// 入力TPPポートは１つ（TFDを受け取る）
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
	}

	public TppPort getTfdInputPort() {
		return this.inputPorts.get(0);
	}
	
	public void initOutputPort() {
		// 作成したレコードをそのまま抽出したい場合、Createリフレクションからの出力が必要なので、出力TPPポートを初期化
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

		TfdOutputer tfdOutputer = ptm.getInputTfdOutputerTppIfExists(tfdInputPort);	// 入力TFDが定義されてないならnullがかえる
		return tfdOutputer;
	}





	public void removeFromTransProc() {
		// まず、このCreateリフレクション自身を削除
		this.transProc.removeTpp(this);
		
		PortTransManager ptm = this.transProc.portTransManager;
		
		// TFDからのポートトランスミッションを削除
		ptm.removeAllPortTransByEndPort(getTfdInputPort());

		// このCreateリフレクションの出力を受け取る全てのポートトランスミッションを削除
		// サービスの引数として受け取られていた場合は、後続する引数も自動で削除される
		ptm.removeAllPortTransByStartPort(outputPort);
	}
}
