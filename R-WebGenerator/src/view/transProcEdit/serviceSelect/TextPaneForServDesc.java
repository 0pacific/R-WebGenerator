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
	// パネルやツリーなどとの間のパディング
	public static final int panePadding = 10;
	
	// パネル上での位置
	public static final int posX = JTreeForServiceSelection.posX + JTreeForServiceSelection.treeWidth + panePadding;
	public static final int posY = JTreeForServiceSelection.posY;

	// サイズ
	public static final int paneWidth = FrameForServiceSelection.frameWidth - (JTreeForServiceSelection.posX + JTreeForServiceSelection.treeWidth + (TextPaneForServDesc.panePadding*2));
	public static final int paneHeight = 200;
	
	/*
	 * インスタンスは最後！
	 */
	private static TextPaneForServDesc obj = new TextPaneForServDesc();

	
	
	
	/*
	 * PORPOSE :
	 * コンストラクタ
	 */
	private TextPaneForServDesc() {
		
	}


	
	
	/*
	 * PURPOSE :
	 * 指定したサービスの説明文を書き込む（それまでの説明文は抹消）
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