package view.transProcEdit.fieldSelect;

import javax.swing.JFrame;

import mainFrame.MainFrame;


/*
 * SUMMARY :
 * フィールド選択フレーム
 * 
 * NOTICE :
 * Singleton
 */
public class FrameFieldSelect extends JFrame {
	// フレーム位置
	public static final int framePosX = 400;
	public static final int framePosY = 300;
	
	// フレームサイズ
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	// パネル
	private PanelFieldSelect panel;
	
	
	/*
	 * インスタンスは最後！
	 */
	private static FrameFieldSelect obj = new FrameFieldSelect();

	
	private FrameFieldSelect() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 位置とサイズ
		setBounds(FrameFieldSelect.framePosX, FrameFieldSelect.framePosY,
					FrameFieldSelect.frameWidth, FrameFieldSelect.frameHeight);

		// レイアウトマネージャなし
		setLayout(null);
		
		// パネルの配置
		panel = PanelFieldSelect.getInstance();
		panel.setBounds(PanelFieldSelect.posX, PanelFieldSelect.posY, PanelFieldSelect.panelWidth, PanelFieldSelect.panelHeight);
		getContentPane().add(panel);
	}
	
	
	public static FrameFieldSelect getInstance() {
		return FrameFieldSelect.obj;
	}
	
	
	
	
	public static void updateInstance(FrameFieldSelect newObject) {
		FrameFieldSelect.obj = newObject;
	}

}
