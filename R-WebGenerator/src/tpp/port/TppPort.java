package tpp.port;

import tpp.TransitionProcessPart;

/*
 * TPPポートを表すクラス
 */
public class TppPort extends DataPort {
	private TransitionProcessPart tpp;


	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public TppPort(TransitionProcessPart tpp) {
		this.tpp = tpp;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * このTPPポートを所持するTPPを返す
	 */
	public TransitionProcessPart getTpp() {
		return tpp;
	}
}
