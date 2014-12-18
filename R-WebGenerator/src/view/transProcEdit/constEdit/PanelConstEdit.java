package view.transProcEdit.constEdit;

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
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;
import executer.generation.mysql.*;
import utility.*;





public class PanelConstEdit extends JPanel implements ActionListener {
	// ���[�h
	public static final int MODE_INT = 0;
	public static final int MODE_STRING = 1;
	public int modeDataType;

	// ���ɒ�`����Ă��������ҏW���悤�Ƃ��Ă���̂��ۂ�
	public boolean editing;
	
	// �T�[�r�X
	public Service service;

	// �ҏW���̃T�[�r�X�����̔ԍ��ƃ^�C�v
	public int argIndex;
	public String argType;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// �w�i�F
	public static Color BG_COLOR = Color.WHITE;
	
	// �萔����͂�����e�L�X�g�t�B�[���h
	JTextField valueTextField;

	// �ҏW���s�{�^���A�L�����Z���{�^��
	public JButton exeBtn;
	public JButton cancelBtn;

	private static PanelConstEdit obj = new PanelConstEdit();
	
	
	
	private PanelConstEdit() {
		setBackground(PanelConstEdit.BG_COLOR);

		// SpringLayout
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	

	public boolean locateCompsForConstEdit(Service service, int argIndex) {
		removeAll();
		
		this.service = service;
		this.argIndex = argIndex;
		this.argType = service.argTypes[argIndex];

		// �ҏW���̈�����INT���m�萔��STRING���m�萔���ɂ���āA���[�h��ݒ�
		if(argType.equals(Service.IO_TYPE_CONST_INT)) {
			this.modeDataType = PanelConstEdit.MODE_INT;
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING)) {
			this.modeDataType = PanelConstEdit.MODE_STRING;
		}
		else {
			Debug.error("INT�ł�STRING�ł��Ȃ����m�萔��ҏW���悤�Ƃ��Ă���悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		// ���Ƀ��m�萔����`����Ă������ۂ�
		editing = service.argDefined(argIndex);
		
		// �e�L�X�g�t�B�[���h�ǉ�
		valueTextField = new JTextField();
		valueTextField.setPreferredSize(new Dimension(150, 20));
		springLayout.putConstraint(SpringLayout.NORTH, valueTextField, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, valueTextField, 20, SpringLayout.WEST, this);
		add(valueTextField);
		// �������ɒ�`����Ă������m�萔��ҏW����P�[�X�Ȃ�A���݂̒l�����Ă���
		if(editing) {
			if(modeDataType==PanelConstEdit.MODE_INT) {
				TppConstInt currentConstInt = (TppConstInt)service.getArgTppIfExists(argIndex);
				int currentValue = currentConstInt.getValue();
				valueTextField.setText(Integer.toString(currentValue));
			}
			else if(modeDataType==PanelConstEdit.MODE_STRING) {
				TppConstString currentConstString = (TppConstString)service.getArgTppIfExists(argIndex);
				String currentValue = currentConstString.getValue();
				valueTextField.setText(currentValue);
			}
		}
		
		// �ҏW���s�{�^��
		exeBtn = new JButton("�@�@�@OK�@�@�@");
		exeBtn.addActionListener(this);
		exeBtn.setActionCommand("�ҏW���s");
		springLayout.putConstraint(SpringLayout.NORTH, exeBtn, 40, SpringLayout.SOUTH, valueTextField);
		springLayout.putConstraint(SpringLayout.WEST, exeBtn, 10, SpringLayout.WEST, this);
		add(exeBtn);

		// �L�����Z���{�^��
		cancelBtn = new JButton(GeneratorProperty.japanese()?"�L�����Z��":"Cancel");
		cancelBtn.addActionListener(this);
		cancelBtn.setActionCommand("�L�����Z��");
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 0, SpringLayout.NORTH, exeBtn);
		springLayout.putConstraint(SpringLayout.WEST, cancelBtn, 20, SpringLayout.EAST, exeBtn);
		add(cancelBtn);
		
		FrameConstEdit.repaintAndValidate();
		return true;
	}
	

	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		// �ҏW���s�{�^�����N���b�N���ꂽ -> 
		if(cmd.equals("�ҏW���s")) {
			TransitionProcess transProc = Panel_TpEdit_Above.getInstance().editingTp;

			// INT���m�萔
			if(this.argType==Service.IO_TYPE_CONST_INT) {
				try {
					int value = Integer.parseInt(valueTextField.getText());

					// �Ē�`�̏ꍇ
					if(editing) {
						TppConstInt currentConstInt = (TppConstInt)service.getArgTppIfExists(argIndex);
						currentConstInt.setValue(value);

						// ���̃��m�萔���`���Ȃ���Β�`�ł��Ȃ������́A��`�ςȂ�S�č폜����i�j�]����\�������邽�߁j
						service.removeFollowingArgsIfExist(argIndex);
					}
					// ����`�̏ꍇ
					else {
						TppConstInt newConstInt = new TppConstInt(transProc, value);

						// �J�ڃv���Z�X��TPP�z��ɁA���̃��m�萔��ǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X�ȍ~���E�։����o���j
						service.transProc.addTpp(service.getTppo(), newConstInt);
						
						// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
						TppPort startPort = newConstInt.outputPort;
						TppPort endPort = service.getInputPort(argIndex);
						transProc.portTransManager.addPortTrans(startPort, endPort);
					}
				}
				catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "���l�𔼊p�œ��͂��ĉ������B");
					return;
				}
			}
			// STRING���m�萔
			else if(this.argType==Service.IO_TYPE_CONST_STRING) {
				String value = valueTextField.getText();
				if(value.equals("")) {
					JOptionPane.showMessageDialog(this, "���������͂��ĉ������B");
					return;
				}

				// �Ē�`�̏ꍇ
				if(editing) {
					TppConstString currentConstString = (TppConstString)service.getArgTppIfExists(argIndex);
					currentConstString.setValue(value);

					// ���̃��m�萔���`���Ȃ���Β�`�ł��Ȃ������́A��`�ςȂ�S�č폜����i�j�]����\�������邽�߁j
					service.removeFollowingArgsIfExist(argIndex);
				}
				// ����`�̏ꍇ
				else {
					TppConstString newConstString = new TppConstString(transProc, value);

					// �J�ڃv���Z�X��TPP�z��ɁA���̃��m�萔��ǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X�ȍ~���E�։����o���j
					service.transProc.addTpp(service.getTppo(), newConstString);
					
					// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
					TppPort startPort = newConstString.outputPort;
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
			Debug.error("�s���ȃR�}���h�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		FrameConstEdit.getInstance().setEnabled(false);
		FrameConstEdit.getInstance().setFocusable(false);
		FrameConstEdit.getInstance().setVisible(false);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().requestFocus();

		MainFrame.repaintAndValidate();
		FrameConstEdit.repaintAndValidate();

		// �T�[�r�X�����ҏW�p�l���̃R���|�[�l���g�Ĕz�u
		PanelServiceArgEdit.getInstance().relocate();
	}
	
	



	public static PanelConstEdit getInstance() {
		return PanelConstEdit.obj;
	}



	public static void updateInstance(PanelConstEdit newObject) {
		PanelConstEdit.obj = newObject;
	}
}
