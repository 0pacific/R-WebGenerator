package view.transProcEdit.serviceSelect;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.service.Service;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.*;


/*
 * Singleton
 */
public class TextPaneForServDesc extends JTextPane {
	// �p�l����c���[�ȂǂƂ̊Ԃ̃p�f�B���O
	public static final int panePadding = 10;
	
	// �p�l����ł̈ʒu
	public static final int posX = JTreeForServiceSelection.posX + JTreeForServiceSelection.treeWidth + panePadding;
	public static final int posY = JTreeForServiceSelection.posY;

	// �T�C�Y
	public static final int paneWidth = FrameForServiceSelection.frameWidth - (JTreeForServiceSelection.posX + JTreeForServiceSelection.treeWidth + (TextPaneForServDesc.panePadding*2));
	public static final int paneHeight = 200;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static TextPaneForServDesc obj = new TextPaneForServDesc();

	
	
	
	/*
	 * PORPOSE :
	 * �R���X�g���N�^
	 */
	private TextPaneForServDesc() {
		
	}


	
	
	/*
	 * PURPOSE :
	 * �w�肵���T�[�r�X�̐��������������ށi����܂ł̐������͖����j
	 */
	public void setDesc(String servName) {
		Service serv = Service.getInstanceByServiceName(null, servName);
		String desc = serv.getDescription();
		setText(desc);
	}
	
	



	public static TextPaneForServDesc getInstance() {
		return TextPaneForServDesc.obj;
	}
}