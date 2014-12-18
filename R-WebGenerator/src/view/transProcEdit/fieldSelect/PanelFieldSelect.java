package view.transProcEdit.fieldSelect;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import property.GeneratorProperty;

import debug.Debug;	

import mainFrame.MainFrame;


import table.SuperTable;
import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import utility.*;

/*
 * SUMMARY :
 * �t�B�[���h�I���^�T�[�r�X�����ҏW�p�l��
 * 
 * NOTICE :
 * Singleton
 */
public class PanelFieldSelect extends JPanel implements ActionListener {
	// �t���[����ł̈ʒu
	public static final int posX = 0;
	public static final int posY = 0;

	// �T�C�Y
	public static final int panelWidth = FrameFieldSelect.frameWidth;
	public static final int panelHeight = FrameFieldSelect.frameHeight;

	// SpringLayout
	SerializableSpringLayout layout;

	// �ҏW�Ώۂ̃T�[�r�X
	public Service service;
	
	// �T�[�r�X�̑扽������ҏW���Ă���̂�
	// �i�������t�B�[���h�I���^�̈����łȂ���΂Ȃ�Ȃ��j
	public int argIndex;

	// �e�t�B�[���h��\���`�F�b�N�{�b�N�X�A���̃T�C�Y�A�Ԋu
	public ArrayList<JCheckBox> fieldCheckBoxes;
	public static final int CB_WIDTH = 100;
	public static final int CB_HEIGHT = 50;
	public static final int CB_SPACING = 20;
	
	// �ҏW���s�{�^���A�L�����Z���{�^��
	public JButton exeBtn;
	public JButton cancelBtn;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static PanelFieldSelect obj = new PanelFieldSelect();

	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private PanelFieldSelect() {
		// SpringLayout���g�p
		layout = new SerializableSpringLayout();
		setLayout(layout);
	}

	
	
	
	
	public boolean locateCompsForFieldSelectArgEdit(Service service, int argIndex) {
		//Debug.out("�T�[�r�X�u"+service.serviceName+"�v�̑�"+argIndex+"�����i0�n�܂�j��ҏW���܂��i�t�B�[���h�I���^�T�[�r�X�����ł��j", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

		removeAll();
		
		this.service = service;
		this.argIndex = argIndex;
		TppPort argPort = service.getInputPort(argIndex);
		
		PortTransManager portTransManager = service.transProc.portTransManager;
		portTransManager.varDump();
		
		
		// TFD�o�͌^TPP���󂯎��T�[�r�X�����|�[�g�i����TPP�|�[�g�j�擾
		int tarArgIndex = service.hmFldSel.get(new Integer(argIndex)).intValue();
		TppPort tfdArgPort = service.getInputPort(tarArgIndex);

		// TFD�o�͌^TPP�́A�o��TFD�̃t�B�[���h�����Z�o
		TfdOutputer tfdOutputer = portTransManager.getInputTfdOutputerTppIfExists(tfdArgPort);
		int fieldNum = tfdOutputer.getOutFieldNum();

		// TFD�o�͌^TPP���擾
		PortTrans trans = portTransManager.getOnePortTransByEndPortIfExists(tfdArgPort);
		TfdOutputer tfdArg = portTransManager.getInputTfdOutputerTppIfExists(tfdArgPort);
		
		// �e�t�B�[���h�ɑΉ������`�F�b�N�{�b�N�X
		fieldCheckBoxes = new ArrayList<JCheckBox>();
		for(int i=0; i<fieldNum; i++) {
			JCheckBox cb = new JCheckBox();

			// �t�B�[���h�����`�F�b�N�{�b�N�X�e�ɕ\��
			String fieldName = tfdArg.getOutputFieldWebNames().get(i);
			cb.setText(fieldName);

			// ��ԏ�̃`�F�b�N�{�b�N�X -> �t�B�[���h�I���p�l������̑��Έʒu�w��
			if(i==0) {
				layout.putConstraint(SpringLayout.NORTH, cb, 20, SpringLayout.NORTH, PanelFieldSelect.getInstance());
			}
			// ��Ԗڈȍ~�̃`�F�b�N�{�b�N�X -> ��̃`�F�b�N�{�b�N�X����̑��Έʒu�w��
			else {
				layout.putConstraint(SpringLayout.NORTH, cb, 20, SpringLayout.SOUTH, fieldCheckBoxes.get(i-1));
			}
			layout.putConstraint(SpringLayout.WEST, cb, 20, SpringLayout.WEST, PanelFieldSelect.getInstance());

			// �t�B�[���h�I���p�l���ւ̃`�F�b�N�{�b�N�X�ǉ�
			add(cb);
			
			// �C���X�^���X�ϐ��ւ̃`�F�b�N�{�b�N�X�ǉ�
			fieldCheckBoxes.add(cb);
		}

		// �ҏW���s�{�^��
		exeBtn = new JButton(GeneratorProperty.japanese()?"�ȏ�̂悤�Ƀt�B�[���h��I��":" OK ");
		exeBtn.addActionListener(this);
		exeBtn.setActionCommand("�ҏW���s");
		layout.putConstraint(SpringLayout.NORTH, exeBtn, 40, SpringLayout.SOUTH, fieldCheckBoxes.get(fieldNum-1));
		layout.putConstraint(SpringLayout.WEST, exeBtn, 10, SpringLayout.WEST, PanelFieldSelect.getInstance());
		add(exeBtn);

		// �L�����Z���{�^��
		cancelBtn = new JButton(GeneratorProperty.japanese()?"�L�����Z��":"Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("�L�����Z��");
		layout.putConstraint(SpringLayout.NORTH, cancelBtn, 0, SpringLayout.NORTH, exeBtn);
		layout.putConstraint(SpringLayout.WEST, cancelBtn, 20, SpringLayout.EAST, exeBtn);
		add(cancelBtn);

		repaint();
		validate();
		
		return true;
	}
	

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		// �ҏW���s�{�^�����N���b�N���ꂽ -> INT�z��^������\���ATPP���쐬 -> �J�ڃv���Z�X���ɁA����TPP����A�ҏW���̈����|�[�g�ւ́A�|�[�g�g�����X�~�b�V�����ǉ�
		if(cmd.equals("�ҏW���s")) {
			ArrayList<Integer> checkedIndexAl = new ArrayList<Integer>();
			
			for(int i=0; i<fieldCheckBoxes.size(); i++) {
				JCheckBox cb = fieldCheckBoxes.get(i);
				boolean checked = cb.isSelected();
				if(checked) {
					checkedIndexAl.add(new Integer(i));
				}
			}

			int[] checkedIndex = new int[checkedIndexAl.size()];
			for(int i=0; i<checkedIndexAl.size(); i++) {
				checkedIndex[i] = checkedIndexAl.get(i);
			}

			// INT�萔�z��TPP���쐬�A�J�ڃv���Z�X��TPP�z��ɒǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X���E�։������B�����ɒǉ������TPP�̎��s���������������Ȃ�B�ꉞ���N���X�Œ�������񂾂��ǁj
			TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;
			TppConstArrayInt tpp = new TppConstArrayInt(transProc, checkedIndex);
			transProc.addTpp(service.getTppo(), tpp);
			
			// INT�萔�z��TPP�́A�o�̓|�[�g
			TppPort startPort = tpp.outputPort;

			// �ҏW���́A�t�B�[���h�I���^�T�[�r�X�����|�[�g
			TppPort endPort = service.getInputPort(argIndex);

			// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
			transProc.portTransManager.addPortTrans(startPort, endPort);
		}
		else if(cmd.equals("�L�����Z��")) {
		}
			
		// �t�B�[���h�I���t���[�����\���ɁA�W�F�l���[�^�t���[�����t�H�[�J�X�\��
		FrameFieldSelect.getInstance().setEnabled(false);
		FrameFieldSelect.getInstance().setFocusable(false);
		FrameFieldSelect.getInstance().setVisible(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();

		// �W�F�l���[�^�t���[���ĕ`��E�Č���
		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
		
		// �T�[�r�X�����I���p�l���̃R���|�[�l���g�Ĕz�u
		PanelServiceArgEdit.getInstance().relocate();
	}
	
	
	

	
	public static PanelFieldSelect getInstance() {
		return PanelFieldSelect.obj;
	}





	public static void updateInstance(PanelFieldSelect newObject) {
		PanelFieldSelect.obj = newObject;
	}
}
