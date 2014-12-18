package tpp.port;

import tpp.TransitionProcessPart;

/*
 * TPP�|�[�g��\���N���X
 */
public class TppPort extends DataPort {
	private TransitionProcessPart tpp;


	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public TppPort(TransitionProcessPart tpp) {
		this.tpp = tpp;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * ����TPP�|�[�g����������TPP��Ԃ�
	 */
	public TransitionProcessPart getTpp() {
		return tpp;
	}
}
