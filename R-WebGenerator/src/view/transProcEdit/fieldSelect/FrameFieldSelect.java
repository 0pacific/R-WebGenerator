package view.transProcEdit.fieldSelect;

import javax.swing.JFrame;

import mainFrame.MainFrame;


/*
 * SUMMARY :
 * �t�B�[���h�I���t���[��
 * 
 * NOTICE :
 * Singleton
 */
public class FrameFieldSelect extends JFrame {
	// �t���[���ʒu
	public static final int framePosX = 400;
	public static final int framePosY = 300;
	
	// �t���[���T�C�Y
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;

	// �p�l��
	private PanelFieldSelect panel;
	
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static FrameFieldSelect obj = new FrameFieldSelect();

	
	private FrameFieldSelect() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// �ʒu�ƃT�C�Y
		setBounds(FrameFieldSelect.framePosX, FrameFieldSelect.framePosY,
					FrameFieldSelect.frameWidth, FrameFieldSelect.frameHeight);

		// ���C�A�E�g�}�l�[�W���Ȃ�
		setLayout(null);
		
		// �p�l���̔z�u
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
