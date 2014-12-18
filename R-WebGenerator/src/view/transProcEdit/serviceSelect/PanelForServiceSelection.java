package view.transProcEdit.serviceSelect;

import java.awt.Dimension;
import java.awt.event.*;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.service.Service;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

/*
 * Singleton
 */
public class PanelForServiceSelection extends JPanel implements TreeSelectionListener,ActionListener {
	// �t���[����ł̈ʒu
	public static final int posX = 0;
	public static final int posY = 0;

	// �T�C�Y
	public static final int panelWidth = FrameForServiceSelection.frameWidth;
	public static final int panelHeight = FrameForServiceSelection.frameHeight;
	
	// �T�[�r�X���ꗗ�̃c���[�A���������X�N���[���y�C��
	private JTreeForServiceSelection tree;
	private JScrollPane treeScrPane;
	
	// �T�[�r�X�����̃e�L�X�g�v���[��
	private TextPaneForServDesc descPane;

	// �T�[�r�X�ǉ��{�^���A���̃T�C�Y
	private JButton addButton;
	public static final int addButtonWidth = 200;
	public static final int addButtonHeight = 50;
	
	// �L�����Z���{�^��
	public JButton buttonCancel;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static PanelForServiceSelection obj = new PanelForServiceSelection();
	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private PanelForServiceSelection() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	public void locateComps() {
		removeAll();
		
		boolean japanese = GeneratorProperty.japanese();
		
		// �T�[�r�X���ꗗ�c���[��������
		tree = JTreeForServiceSelection.getInstance();
		tree.setPreferredSize(new Dimension(JTreeForServiceSelection.treeWidth, JTreeForServiceSelection.treeHeight));
		tree.addTreeSelectionListener(this);

		// �T�[�r�X���ꗗ�c���[���i�[����X�N���[���y�C���̏������E�ǉ�
	    treeScrPane = new JScrollPane(tree);
	    treeScrPane.setPreferredSize(new Dimension(JTreeForServiceSelection.treeWidth, JTreeForServiceSelection.treeHeight));
	    Slpc.put(springLayout, "N", treeScrPane, "N", this, 20);
		Slpc.put(springLayout, "W", treeScrPane, "W", this, 20);
	    treeScrPane.getVerticalScrollBar().setUnitIncrement(10);
		add(treeScrPane);

		// �T�[�r�X�����̃e�L�X�g�y�C�����������E�ǉ�
		descPane = TextPaneForServDesc.getInstance();
		descPane.setPreferredSize(new Dimension(FrameForServiceSelection.frameWidth-JTreeForServiceSelection.treeWidth-40,JTreeForServiceSelection.treeHeight));
		//descPane.setPreferredSize(new Dimension(FrameForServiceSelection.frameWidth-treeScrPane.getWidth()-20, treeScrPane.getHeight()));
		Slpc.put(springLayout, "N", descPane, "N", treeScrPane, 0);
		Slpc.put(springLayout, "W", descPane, "E", treeScrPane, 10);
		descPane.setEditable(false);
		add(descPane);

		// �T�[�r�X�̒ǉ������肷��{�^��
		addButton = new JButton(japanese?"�I�𒆂̃T�[�r�X��ǉ�":"Add Service");
		addButton.addActionListener(this);
		addButton.setActionCommand("�T�[�r�X�ǉ��{�^��");
		Slpc.put(springLayout, "N", addButton, "S", treeScrPane, 20);
		Slpc.put(springLayout, "W", addButton, "W", this, 20);
		add(addButton);

		// �L�����Z���{�^��
		buttonCancel = new JButton(japanese?"�L�����Z��":"Cancel");
		buttonCancel.addActionListener(this);
		buttonCancel.setActionCommand("�L�����Z��");
		Slpc.put(springLayout, "N", buttonCancel, "N", addButton, 0);
		Slpc.put(springLayout, "W", buttonCancel, "E", addButton, 20);
		add(buttonCancel);
		
		FrameForServiceSelection.getInstance().repaint();
		FrameForServiceSelection.getInstance().validate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * �c���[�I���C�x���g�n���h��
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if(!(e.getSource() instanceof JTreeForServiceSelection)) {
			JOptionPane.showMessageDialog(this, "�G���[���������܂����B");
			Debug.error("PanelForServiceSelection valueChanged() : �T�[�r�X���ꗗ�c���[�ȊO����̃C�x���g�ʒm�ł�");
			return;
		}

		String selectedServName = tree.getLastSelectedPathComponent().toString();
		Service selectedServ = Service.getInstanceByServiceName(null, selectedServName);

		// �T�[�r�X���ȊO�̕������N���b�N���ꂽ
		if(selectedServ==null) {
			return;
		}
		
		String desc = selectedServ.getDescription();
		descPane.setText(desc);

		repaint();
		validate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * �{�^���N���b�N�C�x���g�n���h��
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		boolean japanese = GeneratorProperty.japanese();

		// �T�[�r�X�ǉ��{�^�����N���b�N���ꂽ
		if(cmd.equals("�T�[�r�X�ǉ��{�^��")) {
			String servName = tree.getLastSelectedPathComponent().toString();

			// �m�F�_�C�A���O
			int confirm = JOptionPane.showConfirmDialog(
				this,
				japanese?"�T�[�r�X�u"+servName+"�v��ǉ����܂����H":"Add Service '"+servName+"'?",
				japanese?"�T�[�r�X�̒ǉ�":"Service Addition",
				JOptionPane.OK_CANCEL_OPTION
			);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}
			Panel_TpEdit_Above.getInstance().addServiceAndServicePanelByServiceName(servName);

			
			FrameForServiceSelection.getInstance().setVisible(false);
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setFocusable(true);
			MainFrame.getInstance().requestFocus();

			JOptionPane.showMessageDialog(
				MainFrame.getInstance(),
				japanese?
						"�T�[�r�X�u"+servName+"�v��\���p�l�����ǉ�����܂����B\n�p�l�����N���b�N���A�����̕ҏW���s���ĉ������B":
						"Service '"+servName+"' added.\nClick Panel and Edit Args.",
				japanese?"�T�[�r�X�ǉ�����":"Service Addition",
				JOptionPane.PLAIN_MESSAGE,
				null
			);
		}
		// �L�����Z���{�^���N���b�N
		else if(cmd.equals("�L�����Z��")) {
			FrameForServiceSelection.getInstance().setVisible(false);
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setFocusable(true);
			MainFrame.getInstance().requestFocus();
		}
	}
	
	


	/*
	 * PURPOSE :
	 * �C���X�^���X�擾
	 */
	public static PanelForServiceSelection getInstance() {
		return PanelForServiceSelection.obj;
	}


	public static void updateInstance(PanelForServiceSelection newObject) {
		PanelForServiceSelection.obj = newObject;
	}
}
