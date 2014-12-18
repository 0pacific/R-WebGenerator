package view.transProcEdit.serviceSelect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;


import javax.swing.*;

import mainFrame.MainFrame;

/*
 * Singleton
 */
public class FrameForServiceSelection extends JFrame {
	// フレーム位置
	public static final int framePosX = 190;
	public static final int framePosY = 300;
	
	// フレームサイズ
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	// パネル
	private PanelForServiceSelection panel;
	
	/*
	 * インスタンスは最後！
	 */
	private static FrameForServiceSelection obj = new FrameForServiceSelection();


	
	
	
	
	private FrameForServiceSelection() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 位置とサイズ
		setBounds(FrameForServiceSelection.framePosX, FrameForServiceSelection.framePosY,
					FrameForServiceSelection.frameWidth, FrameForServiceSelection.frameHeight);

		// レイアウトマネージャなし
		setLayout(null);
		
		// パネルの配置
		panel = PanelForServiceSelection.getInstance();
		panel.setBounds(PanelForServiceSelection.posX, PanelForServiceSelection.posY, PanelForServiceSelection.panelWidth, PanelForServiceSelection.panelHeight);
		getContentPane().add(panel);

		this.repaint();
		this.validate();
		
	}

	
	
	
	
	public static FrameForServiceSelection getInstance() {
		return FrameForServiceSelection.obj;
	}



	public static void updateInstance(FrameForServiceSelection newObject) {
		FrameForServiceSelection.obj = newObject;
	}
}
