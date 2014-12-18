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
	// �t���[���ʒu
	public static final int framePosX = 190;
	public static final int framePosY = 300;
	
	// �t���[���T�C�Y
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	// �p�l��
	private PanelForServiceSelection panel;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static FrameForServiceSelection obj = new FrameForServiceSelection();


	
	
	
	
	private FrameForServiceSelection() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// �ʒu�ƃT�C�Y
		setBounds(FrameForServiceSelection.framePosX, FrameForServiceSelection.framePosY,
					FrameForServiceSelection.frameWidth, FrameForServiceSelection.frameHeight);

		// ���C�A�E�g�}�l�[�W���Ȃ�
		setLayout(null);
		
		// �p�l���̔z�u
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
