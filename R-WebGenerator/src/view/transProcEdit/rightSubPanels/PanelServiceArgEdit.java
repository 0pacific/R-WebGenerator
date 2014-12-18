package view.transProcEdit.rightSubPanels;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.port.DataPort;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import transition.TransitionProcess;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Bottom;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.constArrayEdit.FrameConstArrayEdit;
import view.transProcEdit.constArrayEdit.PanelConstArrayEdit;
import view.transProcEdit.constEdit.FrameConstEdit;
import view.transProcEdit.constEdit.PanelConstEdit;
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;

import java.awt.*;

import javax.swing.*;

import pageElement.PageElementSaif;
import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import java.awt.event.*;
import java.util.ArrayList;
import utility.*;

/*
 * SUMMARY :
 * �T�[�r�X�����I���p�l��
 * 
 * Singleton
 */
public class PanelServiceArgEdit extends JPanel implements ActionListener {
	// �T�[�r�X
	public Service service;

	// �ҏW���̃T�[�r�X�����̔ԍ�
	public int editingArgNumber;
	
	// �u���ݕҏW���̃T�[�r�X�����|�[�g�v��\��
	public TppPort editingArgPort;

	// X�����̃p�f�B���O
	public final int PADD_X = 20;

	// ��[�̃p�f�B���O
	public final int PADD_ABOVE = 20;

	// �e�����̗̈擯�m�̊Ԋu
	public final int SPACING_ARGS = 30;
	
	// �����̖��O�E��ށE�������L�q����e�L�X�g�y�C���̏c��
	public final int HEIGHT_TP_NAME = 25;
	public final int HEIGHT_TP_TYPE = 50;
	public final int HEIGHT_TP_DESC = 100;

	// �e�L�X�g�y�C�����m�̊Ԋu�A����уe�L�X�g�y�C���ƃ{�^���̊Ԃ̊Ԋu
	public final int SPACING = 10;

	// �T�[�r�X�����ҏW�{�^���Ƃ��̉��̃��x���i��`�� or ����`�j�̏c��
	public final int BTN_HEIGHT = 30;

	// ��`�ς��ۂ��������x���̉���
	public final int LABEL_RES_WIDTH = 100;


	// �o�͕ҏW�{�^��
	public JButton btnServOutEdit;
	
	// �폜�{�^��
	public JButton buttonDeleteService;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	
	// �w�i�F
	public static Color backgroundColor = Color.WHITE;

	
	/*
	 * ���C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelServiceArgEdit obj = new PanelServiceArgEdit();
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private PanelServiceArgEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(backgroundColor);
	}

	
	

	
	/*
	 * PURPOSE :
	 * �e�R���|�[�l���g�̍Ĕz�u
	 */
	public void relocate(Service service) {
		// �S�R���|�[�l���g����
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		this.service = service;
		
		// �e�L�X�g�y�C���z��
		ArrayList<JTextPane> textPaneArray_argName = new ArrayList<JTextPane>();

		// �T�[�r�X�����ҏW�{�^���̔z��
		ArrayList<JButton> editBtnArray = new ArrayList<JButton>();

		String[] argNames = service.getArgNames();
		String[] argTypes = service.getArgTypes();
		String[] argDescs = service.getArgDescs();
				
		int argNum = service.getArgNum();
		for(int i=0; i<argNum; i++) {
			// ���������e�L�X�g�y�C��
			String argName = argNames[i];
			JTextPane textPane_argName = new JTextPane();
			String text = japanese ? "����"+(i+1)+": "+argName : "Argument "+(i+1)+" : "+argName;
			textPane_argName.setText(text);
			textPaneArray_argName.add(textPane_argName);
			textPane_argName.setPreferredSize(new Dimension(this.getWidth()-20-20, 20));
			if(i==0) {
				springLayout.putConstraint(SpringLayout.NORTH, textPane_argName, 20, SpringLayout.NORTH, this);
			}
			else {
				springLayout.putConstraint(SpringLayout.NORTH, textPane_argName, 60, SpringLayout.SOUTH, editBtnArray.get(i-1));
			}
			springLayout.putConstraint(SpringLayout.WEST, textPane_argName, 20, SpringLayout.WEST, this);
			add(textPane_argName);

			
			
			// �����ҏW�{�^���̍쐬�E�ǉ�
			// �i�����̎�ނɂ��\�L�͕ς���j
			// ��INT���m�萔�ASTRING���m�萔�Ȃǂ͕ҏW���@��2�ʂ肠�邽�ߕҏW�{�^�����Q�Ȃ̂Œ���
			ButtonServArgEdit editBtn = new ButtonServArgEdit(service, i);
			editBtn.addActionListener(this);
			ButtonServArgEdit editBtnLeft = new ButtonServArgEdit(service, i);	// �Q�̕ҏW�{�^�����K�v�ȏꍇ�̂ݎg���B���̃{�^���������ɗ���
			editBtnLeft.addActionListener(this);
			
			int btnWidth = 0;
			int btnLeftWidth = 0;

			// INT�^�̃T�[�r�X����
			if(argTypes[i].equals(Service.IO_TYPE_CONST_INT)) {
				editBtnLeft.setText(japanese ? "����" : "Input");
				editBtnLeft.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : ���m�萔�@���ړ���");
				btnLeftWidth = 60;
				editBtn.setText(japanese ? "���}����I��" : "Select in Diagram");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : INT�^�T�[�r�X�����@�I��");
				btnWidth = 120;
			}
			// VARCHAR�^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_CONST_STRING)) {
				editBtnLeft.setText(japanese ? "����" : "Input");
				editBtnLeft.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : ���m�萔�@���ړ���");
				btnLeftWidth = 60;
				editBtn.setText(japanese ? "���}����I��" : "Select in Diagram");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : VARCHAR�^�T�[�r�X�����@�I��");
				btnWidth = 120;
			}
			// INT�z��^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_CONST_INT_ARRAY)) {
				editBtn.setText(japanese ? "����" : "Input");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : �萔�z��");
				btnWidth = 60;
			}
			// VARCHAR�z��^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_CONST_STRING_ARRAY)) {
				editBtn.setText(japanese ? "����" : "Input");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : �萔�z��");
				btnWidth = 60;
			}
			// DATETIME�^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_DATETIME)) {
				editBtn.setText(japanese ? "���}����I��" : "Select in Diagram");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : DATETIME�^�T�[�r�X�����@�I��");
				btnWidth = 120;
			}
			// DATE�^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_DATE)) {
				editBtn.setText(japanese ? "���}����I��" : "Select in Diagram");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : DATE�^�T�[�r�X�����@�I��");
				btnWidth = 120;
			}
			// TIME�^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_TIME)) {
				editBtn.setText(japanese ? "���}����I��" : "Select in Diagram");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : TIME�^�T�[�r�X�����@�I��");
				btnWidth = 120;
			}
			// �t�B�[���h�I���^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_OFFSETS)) {
				editBtn.setText(japanese ? "�t�B�[���h��I��" : "Select fields");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : �t�B�[���h�I��");
				btnWidth = 150;
			}
			// TFD�^�̃T�[�r�X����
			else if(argTypes[i].equals(Service.IO_TYPE_TFD)) {
				editBtn.setText(japanese ? "���}����I��" : "Select in Diagram");
				editBtn.setActionCommand("�T�[�r�X�����ҏW�{�^���N���b�N : TFD");
				btnWidth = 220;
			}
			else {
				Debug.informError();
				Debug.error("PanelCsArgs relocate() : �s���Ȉ�����ނł�");
			}

			// �ҏW�{�^�����Q����ꍇ�́A�����̃{�^�����܂��z�u
			if(btnLeftWidth!=0) {
				Slpc.put(springLayout, "N", editBtnLeft, "S", textPane_argName, 10);
				Slpc.put(springLayout, "W", editBtnLeft, "W", this, 20);
				add(editBtnLeft);
				// ���̃{�^����editBtnArray�ɉ�����Ƃ܂������ƂɂȂ�BeditBtnArray�̃{�^�������T�[�r�X�����̐��A�Ƃ����̂�O��ɂ��Ă��邽�߂ł���B

				Slpc.put(springLayout, "N", editBtn, "N", editBtnLeft, 0);
				Slpc.put(springLayout, "W", editBtn, "E", editBtnLeft, 10);
				add(editBtn);
				editBtnArray.add(editBtn);
			}
			// �P�����̏ꍇ
			else {
				springLayout.putConstraint(SpringLayout.NORTH, editBtn, 10, SpringLayout.SOUTH, textPane_argName);
				springLayout.putConstraint(SpringLayout.WEST, editBtn, 20, SpringLayout.WEST, this);
				add(editBtn);
				editBtnArray.add(editBtn);
			}
			
			// ��`�ς��ۂ����L�������x��
			JLabel labelReserved = new JLabel();
			boolean reserved = service.inputPortReserved(i);
			if(reserved) {
				labelReserved.setText(japanese ? "��`��" : "Defined");
			}
			else {
				labelReserved.setText(japanese ? "�I����`" : "!Undefined");
			}
			springLayout.putConstraint(SpringLayout.NORTH, labelReserved, 0, SpringLayout.NORTH, editBtn);
			springLayout.putConstraint(SpringLayout.WEST, labelReserved, 20, SpringLayout.EAST, editBtn);
			add(labelReserved);
		}

		
		// �A�E�g�v�b�g�����̃e�L�X�g�y�C��
		JTextPane outDesc = new JTextPane();
		outDesc.setText(japanese ? "�o�̓e�[�u���\����\n�i�J�ڐ�́u�T�[�r�X�o�̓e�[�u���\���v�I���j" : "Service Output Destination\n(Select 'Service Output Table' on right page)");
		springLayout.putConstraint(SpringLayout.NORTH, outDesc, 60, SpringLayout.SOUTH, editBtnArray.get(editBtnArray.size()-1));	// �Ō�̈����̕ҏW�{�^����肿�傢��
		springLayout.putConstraint(SpringLayout.WEST, outDesc, 20, SpringLayout.WEST, this);
		add(outDesc);

		// �A�E�g�v�b�g�ҏW�{�^��
		btnServOutEdit = new JButton("Select in Diagram");
		springLayout.putConstraint(SpringLayout.NORTH, btnServOutEdit, 10, SpringLayout.SOUTH, outDesc);
		springLayout.putConstraint(SpringLayout.WEST, btnServOutEdit, 20, SpringLayout.WEST, this);
		btnServOutEdit.addActionListener(this);
		btnServOutEdit.setActionCommand("�T�[�r�X�o�͐�I��");
		add(btnServOutEdit);

		
		
		// �T�[�r�X�폜�{�^��
		buttonDeleteService = new JButton(japanese ? "�폜" : "Delete");
		springLayout.putConstraint(SpringLayout.NORTH, buttonDeleteService, 60, SpringLayout.SOUTH, btnServOutEdit);
		springLayout.putConstraint(SpringLayout.WEST, buttonDeleteService, 20, SpringLayout.WEST, this);
		buttonDeleteService.addActionListener(this);
		buttonDeleteService.setActionCommand("�T�[�r�X�폜");
		add(buttonDeleteService);
		
		
		repaint();
		validate();
		MainFrame.getInstance().repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �e�R���|�[�l���g�̍Ĕz�u
	 * 
	 * NOTICE :
	 * �Ώۂ̃T�[�r�X�͌��݂̂܂܂ōs��
	 */
	public void relocate() {
		relocate(service);
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEvent�n���h��
	 * �E�T�[�r�X�����ҏW�{�^���N���b�N���Ɍďo
	 */
	public void actionPerformed(ActionEvent e) {
		// �{�^�����N���b�N���ꂽ�͂�
		JButton sourceButton = (JButton)e.getSource();

		String cmd = e.getActionCommand();

		// ���Ɖ��̃p�l��
		Panel_TpEdit_Above leftPanel = Panel_TpEdit_Above.getInstance();
		Panel_TpEdit_Bottom bottomPanel = Panel_TpEdit_Bottom.getInstance();

		// �T�[�r�X�����ҏW�{�^���N���b�N
		if(sourceButton instanceof ButtonServArgEdit) {
			ButtonServArgEdit button = (ButtonServArgEdit)sourceButton;
			
			// ���̃T�[�r�X�����̕ҏW�ɓ��邱�Ƃ��L�^���Ă���
			editingArgPort = button.servArgPort;
			editingArgNumber = button.serviceArgIndex;
			
			// �ҏW���悤�Ƃ��Ă�������̎�ނɉ����āA�K�؂ȕҏW���[�h�ֈڍs����
			if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : ���m�萔�@���ړ���")) {
				// ���C���t���[�����g�p�s�\��
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				// ���m�萔�ҏW�t���[���\���@���̒��̃R���|�[�l���g�z�u
				FrameConstEdit.getInstance().setEnabled(true);
				FrameConstEdit.getInstance().setFocusable(true);
				FrameConstEdit.getInstance().setVisible(true);
				PanelConstEdit.getInstance().locateCompsForConstEdit(service, button.argIndex);
			}
			// INT�^�T�[�r�X�����́A���}����̑I��
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : INT�^�T�[�r�X�����@�I��")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_INT;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// VARCHAR�^�T�[�r�X�����́A���}����̑I��
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : VARCHAR�^�T�[�r�X�����@�I��")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_VARCHAR;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// DATETIME�^�T�[�r�X�����́A���}����̑I��
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : DATETIME�^�T�[�r�X�����@�I��")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATETIME;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// DATE�^�T�[�r�X�����́A���}����̑I��
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : DATE�^�T�[�r�X�����@�I��")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATE;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			// TIME�^�T�[�r�X�����́A���}����̑I��
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : TIME�^�T�[�r�X�����@�I��")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_TIME;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : �萔�z��")) {
				// ���̃T�[�r�X����������`�Ȋ֌W�ŁA�萔�z��̗v�f�������ƂȂ�ׂ������f�����Ȃ����� -> �ҏW�s�Ȃ̂ŉ������Ȃ�
				int constArrayElementNum = service.getElementNumOfConstArrayArg(button.argIndex);
				if(constArrayElementNum==-1) {
					/*
					 * ���������F�_�C�A���O�{�b�N�X���o������
					 */
					return;
				}
				
				// �W�F�l���[�^�t���[�����t�H�[�J�X�s�\��
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				// �萔�z��ҏW�t���[���\���@���̒��̃R���|�[�l���g�z�u
				FrameConstArrayEdit.getInstance().setEnabled(true);
				FrameConstArrayEdit.getInstance().setFocusable(true);
				FrameConstArrayEdit.getInstance().setVisible(true);
				PanelConstArrayEdit.getInstance().locateCompsForConstArrayEdit(service, button.argIndex);
			}
			// �t�B�[���h�I�� -> �t�B�[���h�I���t���[����\��
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : �t�B�[���h�I��")) {
				// �W�F�l���[�^�t���[�����t�H�[�J�X�s�\��
				MainFrame.getInstance().setEnabled(false);
				MainFrame.getInstance().setFocusable(false);

				// �t�B�[���h�I���^�T�[�r�X�����ҏW�t���[���\���@���̒��̃R���|�[�l���g�z�u
				FrameFieldSelect.getInstance().setEnabled(true);
				FrameFieldSelect.getInstance().setFocusable(true);
				FrameFieldSelect.getInstance().setVisible(true);
				PanelFieldSelect.getInstance().locateCompsForFieldSelectArgEdit(service, button.argIndex);
				FrameFieldSelect.getInstance().requestFocus();
			}
			// TFD -> ���[�h��؂�ւ��ATFD�^������}����I���ł���悤�ɂ���
			else if(cmd.equals("�T�[�r�X�����ҏW�{�^���N���b�N : TFD")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_TFD;
				removeAll();

				bottomPanel.locateIoSelectionCancelButton();
			}
		}
		// �T�[�r�X�o�͐�I��
		else if(cmd.equals("�T�[�r�X�o�͐�I��")){
			// �o�͐�I�� -> ���[�h��؂�ւ��A�o�͐��I���ł���悤�ɂ���
			if(cmd.equals("�T�[�r�X�o�͐�I��")) {
				leftPanel.mode = Panel_TpEdit_Above.MODE_SELECT_OUTPUT_TFD;
				removeAll();
				bottomPanel.removeAll();
			}
		}
		// �T�[�r�X�폜
		else if(cmd.equals("�T�[�r�X�폜")) {
			int confirm = JOptionPane.showConfirmDialog(this, "�T�[�r�X�u"+service.serviceName+"�v���폜���܂����H", "�T�[�r�X�̍폜", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}

			/*
			 * �T�[�r�X���폜���邱�Ƃ�����
			 */
			
			service.removeService();
		}
		// �G���[
		else {
			JOptionPane.showMessageDialog(this, "�G���[���������܂����B");
			Debug.error("�z��O�̃A�N�V�����R�}���h", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}

		// ���p�l���̃R���|�[�l���g�Ĕz�u
		Panel_TpEdit_Above.getInstance().locateTppPanels();
		
		// �ĕ`��E�Č���
		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * Panel_TpEdit_Left�ɂ����ā@TFD�@�^�T�[�r�X�������I�����ꂽ�Ƃ��ɍs������
	 */
	public void informSelectedTfdAsServiceArg(TfdOutputer selectedTo) {
		DataPort outPort = ((TransitionProcessPart)selectedTo).getOutputPort();
		PortTransManager portTransManager = Panel_TpEdit_Above.getInstance().editingTp.portTransManager;

		// ���Ɉ�������`����Ă����Ȃ�폜����
		service.removeArgIfDefined(editingArgNumber);
		
		// �ҏW���̑J�ڃv���Z�X�ɁA�u�I�����ꂽTPP�̏o��->�T�[�r�X�̑I������Ă�������v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		portTransManager.addPortTrans(outPort, editingArgPort);

		finishTppSelection();
	}

	
	
	
	
	public void informSelectedSaifAsServiceArg(PageElementSaif saif) {
		TransitionProcess editingTransProc = Panel_TpEdit_Above.getInstance().editingTp;
		DataPort saifOutputPort = saif.outputPePortsHashMap.get(editingTransProc);
		PortTransManager portTransManager = editingTransProc.portTransManager;

		// ���Ɉ�������`����Ă����Ȃ�폜����
		service.removeArgIfDefined(editingArgNumber);

		// �ҏW���̑J�ڃv���Z�X�ɁA�u�I�����ꂽSAIF�̏o��->�T�[�r�X�̑I������Ă�������v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		portTransManager.addPortTrans(saifOutputPort, editingArgPort);

		finishTppSelection();
	}
	
	
	
	
	public void informSelectedPeInputPort(PageElementPort port) {
		// �ҏW���̑J�ڃv���Z�X�ɁA�u�T�[�r�X�̏o�̓|�[�g -> �I�����ꂽPageElement�̓��̓|�[�g�v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;
		transProc.portTransManager.addPortTrans(service.outputPort, port);

		finishTppSelection();
	}

	
	
	
	/*
	 * SUMMARY :
	 * �T�[�r�X�̓��o�͂ȂǂƂ��č��p�l������TPP��PE��I������̂��I������Ƃ��Ɏ��s
	 */
	public void finishTppSelection() {
		// �����p�l���̃��[�h��ʏ�ɖ߂�
		Panel_TpEdit_Above.getInstance().mode = Panel_TpEdit_Above.MODE_NORMAL;

		relocate(service);
		Panel_TpEdit_Bottom.getInstance().relocateComps();

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
	}
	
	

	
	/*
	 * PURPOSE :
	 * �C���X�^���X�擾
	 */
	public static PanelServiceArgEdit getInstance() {
		return PanelServiceArgEdit.obj;
	}
	
	
	
	
	public static void updateInstance(PanelServiceArgEdit newObject) {
		PanelServiceArgEdit.obj = newObject;
	}

}
