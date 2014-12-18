package view.transProcEdit.serviceArgsWindow;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mainFrame.MainFrame;

/*
 * Singleton
 */
public class FrameServiceArgs extends JFrame {
	public static final int frameWidth = 880;
	public static final int frameHeight = 495;

	// パネル
	PanelServiceArgs panel;

	/*
	 * インスタンス参照変数は最後！
	 */
	private static FrameServiceArgs obj = new FrameServiceArgs();
	
	
	
	
	private FrameServiceArgs() {
		setLayout(new FlowLayout());
		setBounds(400, 150, FrameServiceArgs.frameWidth, FrameServiceArgs.frameHeight);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    // パネルの初期化・追加
	    panel = new PanelServiceArgs();
		panel.setPreferredSize(new Dimension(FrameServiceArgs.frameWidth, FrameServiceArgs.frameHeight));
		getContentPane().add(panel);
	}




	public static FrameServiceArgs getInstance() {
		return FrameServiceArgs.obj;
	}



	public static void updateInstance(FrameServiceArgs newObject) {
		FrameServiceArgs.obj = newObject;
	}
}
