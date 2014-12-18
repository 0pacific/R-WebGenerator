package view.transProcEdit.constArrayEdit;

import javax.swing.*;

import mainFrame.MainFrame;

import authority.AuthorityManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

import debug.Debug;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import transition.Transition;
import transition.TransitionProcess;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.constEdit.PanelConstEdit;
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;
import executer.generation.mysql.*;
import utility.*;





public class PanelConstArrayEdit extends JPanel implements ActionListener {
	// ���[�h
	public static final int MODE_INT_ARRAY = 0;
	public static final int MODE_STRING_ARRAY = 1;
	public int modeDataType;

	// �t���[����ł̈ʒu
	public static final int posX = 0;
	public static final int posY = 0;

	// �T�C�Y
	public static final int panelWidth = FrameConstArrayEdit.frameWidth;
	public static final int panelHeight = FrameConstArrayEdit.frameHeight;

	// �T�[�r�X
	public Service service;

	// ���ɒ�`����Ă��������ҏW���悤�Ƃ��Ă���̂��ۂ�
	public boolean editing;

	// �ҏW���̃T�[�r�X�����̔ԍ��ƃ^�C�v
	public int argIndex;
	public String argType;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// �w�i�F
	public static Color BG_COLOR = Color.WHITE;
	
	// �萔�z��̗v�f����͂��Ă��炤�e�L�X�g�t�B�[���h�̔z��
	ArrayList<JTextField> textBoxArray;

	// �ҏW���s�{�^���A�L�����Z���{�^��
	public JButton exeBtn;
	public JButton cancelBtn;

	private static PanelConstArrayEdit obj = new PanelConstArrayEdit();
	
	
	
	private PanelConstArrayEdit() {
		setBackground(PanelConstArrayEdit.BG_COLOR);

		// SpringLayout
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	

	public boolean locateCompsForConstArrayEdit(Service service, int argIndex) {
		removeAll();
		
		this.service = service;
		this.argIndex = argIndex;
		this.argType = service.argTypes[argIndex];

		// �ҏW���̈�����INT�萔�z��STRING�萔�z�񂩂ɂ���āA���[�h��ݒ�
		if(argType.equals(Service.IO_TYPE_CONST_INT_ARRAY)) {
			this.modeDataType = PanelConstArrayEdit.MODE_INT_ARRAY;
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING_ARRAY)) {
			this.modeDataType = PanelConstArrayEdit.MODE_STRING_ARRAY;
		}
		else {
			Debug.error("INT�萔�z��ł�STRING�萔�z��ł��Ȃ��萔�z���ҏW���悤�Ƃ��Ă���悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		
		// �萔�z��̗v�f��
		int constArrayElementNum = service.getElementNumOfConstArrayArg(argIndex);

		// ���]��
		int PADD_LEFT = 20;

		
		// �e�L�X�g�y�C��
		JTextPane textPaneDesc = new JTextPane();
		if(modeDataType==PanelConstArrayEdit.MODE_INT_ARRAY) {
			String text = constArrayElementNum + (GeneratorProperty.japanese()?"�̐��l�����ꂼ����͂��ĉ������B":"integers need to be input.");
			textPaneDesc.setText(text);
		}
		else if(modeDataType==PanelConstArrayEdit.MODE_STRING_ARRAY) {
			String text = constArrayElementNum + (GeneratorProperty.japanese()?"�̕���������ꂼ����͂��ĉ������B":"integers need to be input.");
			textPaneDesc.setText(text);
		}
		Slpc.put(springLayout, "N", textPaneDesc, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneDesc, "W", this, PADD_LEFT);
		add(textPaneDesc);

		
		// ���ɒ萔�z�񂪒�`����Ă������ۂ�
		editing = service.argDefined(argIndex);
		
		
		// �萔�z��̗v�f�������e�L�X�g�t�B�[���h������Ĕz�u
		textBoxArray = new ArrayList<JTextField>();
		for(int i=0; i<constArrayElementNum; i++) {
			JTextField textField = new JTextField();
			textBoxArray.add(textField);
			
			textField.setPreferredSize(new Dimension(100, 20));
			Slpc.put(springLayout, "N", textField, "S", textPaneDesc, 20+(i*30));
			Slpc.put(springLayout, "W", textField, "W", this, PADD_LEFT);
			add(textField);

			// ��`�ς̒萔�z���ҏW����P�[�X�ł́A�l�����Ă���
			if(editing) {
				if(modeDataType==PanelConstArrayEdit.MODE_INT_ARRAY) {
					TppConstArrayInt currentIntArray = (TppConstArrayInt)service.getArgTppIfExists(argIndex);
					int value = currentIntArray.getIntValue(i);
					textField.setText(Integer.toString(value));
				}
				else if(modeDataType==PanelConstArrayEdit.MODE_STRING_ARRAY) {
					TppConstArrayString currentStringArray = (TppConstArrayString)service.getArgTppIfExists(argIndex);
					String value = currentStringArray.getStringValue(i);
					textField.setText(value);
				}
			}
		}

		
		// �ҏW���s�{�^���i�Ō�̃e�L�X�g�t�B�[���h�̂������ɔz�u�j
		exeBtn = new JButton("�@�@�@�@OK�@�@�@�@");
		exeBtn.addActionListener(this);
		exeBtn.setActionCommand("�ҏW���s");
		springLayout.putConstraint(SpringLayout.NORTH, exeBtn, 40, SpringLayout.SOUTH, textBoxArray.get(constArrayElementNum-1));
		springLayout.putConstraint(SpringLayout.WEST, exeBtn, 10, SpringLayout.WEST, PanelConstArrayEdit.getInstance());
		add(exeBtn);

		// �L�����Z���{�^��
		cancelBtn = new JButton(GeneratorProperty.japanese()?"�L�����Z��":"Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("�L�����Z��");
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 0, SpringLayout.NORTH, exeBtn);
		springLayout.putConstraint(SpringLayout.WEST, cancelBtn, 20, SpringLayout.EAST, exeBtn);
		add(cancelBtn);

		MainFrame.repaintAndValidate();
		FrameConstArrayEdit.repaintAndValidate();

		return true;
	}
	

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		// �ҏW���s�{�^�����N���b�N���ꂽ -> 
		if(cmd.equals("�ҏW���s")) {
			TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;

			
			// �e�e�L�X�g�t�B�[���h�̒l���擾���Ĕz��Ɋi�[
			ArrayList<String> inputValueArray = new ArrayList<String>();
			for(int i=0; i<textBoxArray.size(); i++) {
				String value = textBoxArray.get(i).getText();
				inputValueArray.add(value);
			}


			// INT�萔�z��
			if(this.argType==Service.IO_TYPE_CONST_INT_ARRAY) {
				int[] intValueArray = new int[inputValueArray.size()];
				for(int i=0; i<inputValueArray.size(); i++) {
					try {
						intValueArray[i] = Integer.parseInt(inputValueArray.get(i));
					}
					catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(this, "�S�Ă̗��ɐ��l�𔼊p�œ��͂��ĉ������B");
						return;
					}
				}

				// �Ē�`�̏ꍇ
				if(editing) {
					TppConstArrayInt currentIntArray = (TppConstArrayInt)service.getArgTppIfExists(argIndex);
					currentIntArray.setValueArrayByIntArray(intValueArray);

					// ���̒萔�z����`���Ȃ���Β�`�ł��Ȃ������́A��`�ςȂ�S�č폜����i�j�]����\�������邽�߁j
					service.removeFollowingArgsIfExist(argIndex);
				}
				// ����`�̏ꍇ
				else {
					TppConstArrayInt newIntArray = new TppConstArrayInt(transProc, intValueArray);

					// �J�ڃv���Z�X��TPP�z��ɂ��̒萔�z���ǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X�ȍ~���E�։����o���j
					service.transProc.addTpp(service.getTppo(), newIntArray);
					
					// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
					TppPort startPort = newIntArray.outputPort;
					TppPort endPort = service.getInputPort(argIndex);
					transProc.portTransManager.addPortTrans(startPort, endPort);
				}
			}
			// STRING�萔�z��
			else if(this.argType==Service.IO_TYPE_CONST_STRING_ARRAY) {
				String[] stringValueArray = new String[inputValueArray.size()];
				for(int i=0; i<inputValueArray.size(); i++) {
					if(inputValueArray.get(i).equals("")) {
						JOptionPane.showMessageDialog(this, "�S�Ă̗��ɕ��������͂��ĉ������B");
						return;
					}
					stringValueArray[i] = inputValueArray.get(i);
				}

				// �Ē�`�̏ꍇ
				if(editing) {
					TppConstArrayString currentStringArray = (TppConstArrayString)service.getArgTppIfExists(argIndex);
					currentStringArray.setValueArrayByStringArray(stringValueArray);

					// ���̒萔�z����`���Ȃ���Β�`�ł��Ȃ������́A��`�ςȂ�S�č폜����i�j�]����\�������邽�߁j
					service.removeFollowingArgsIfExist(argIndex);
				}
				// ����`�̏ꍇ
				else {
					TppConstArrayString newStringArray = new TppConstArrayString(transProc, stringValueArray);

					// �J�ڃv���Z�X��TPP�z��ɂ��̒萔�z���ǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X�ȍ~���E�։����o���j
					service.transProc.addTpp(service.getTppo(), newStringArray);
					
					// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
					TppPort startPort = newStringArray.outputPort;
					TppPort endPort = service.getInputPort(argIndex);
					transProc.portTransManager.addPortTrans(startPort, endPort);
				}
			}
			else {
				Debug.error("tpp��INT�萔�z��ł�STRING�萔�z��ł��Ȃ��悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
		}
		else if(cmd.equals("�L�����Z��")) {
		}
		else {
			Debug.error("�s���ȃR�}���h", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		
		// �t�B�[���h�I���t���[�����\���ɁA�W�F�l���[�^�t���[�����t�H�[�J�X�\��
		FrameConstArrayEdit.getInstance().setEnabled(false);
		FrameConstArrayEdit.getInstance().setFocusable(false);
		FrameConstArrayEdit.getInstance().setVisible(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();

		// �W�F�l���[�^�t���[���ĕ`��E�Č���
		MainFrame.getInstance().repaintAndValidate();
		
		// �T�[�r�X�����I���p�l���̃R���|�[�l���g�Ĕz�u
		PanelServiceArgEdit.getInstance().relocate();
	}
	
	



	public static PanelConstArrayEdit getInstance() {
		return PanelConstArrayEdit.obj;
	}





	public static void updateInstance(PanelConstArrayEdit newObject) {
		PanelConstArrayEdit.obj = newObject;
	}

}
