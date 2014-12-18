package tpp;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import transition.*;
import utility.*;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;






public class TppIAReflection extends TransitionProcessPart {
	public DataTable dataTable;
	public AccountTable accountTable;
	
	public TppIAReflection(TransitionProcess transProc) {
		super(transProc);
		initInputPorts();
		initOutputPort();
	}

	public void initInputPorts() {
		// ����TPP�|�[�g�͂Q�i�f�[�^�e�[�u��TFD�A�A�J�E���g�e�[�u��TFD�̏��j
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
		this.inputPorts.add(new TppPort(this));
	}

	public TppPort getDataTableTfdInputPort() {
		return this.inputPorts.get(0);
	}
	public TppPort getAccountTableTfdInputPort() {
		return this.inputPorts.get(1);
	}


	
	
	
	public TfdOutputer getTfdOutputerAsDataTableTfdIfExists() {
		TppPort dtTfdInputPort = this.getDataTableTfdInputPort();
		PortTransManager ptm = this.transProc.portTransManager;

		TfdOutputer tfdOutputerAsDtTfd = ptm.getInputTfdOutputerTppIfExists(dtTfdInputPort);	// ���݂��Ȃ����null��������
		return tfdOutputerAsDtTfd;
	}
	
	public TfdOutputer getTfdOutputerAsAccountTableTfdIfExists() {
		TppPort atTfdInputPort = this.getAccountTableTfdInputPort();
		PortTransManager ptm = this.transProc.portTransManager;

		TfdOutputer tfdOutputerAsAtTfd = ptm.getInputTfdOutputerTppIfExists(atTfdInputPort);	// ���݂��Ȃ����null��������
		return tfdOutputerAsAtTfd;
	}

	
	
	

	public void initOutputPort() {
		// �o��TPP�|�[�g�͑��݂��Ȃ�
	}





	/*
	 * 	SUMMARY :
	 *  �������f�[�^�e�[�u��TFD����`����Ă�����A������폜����i�|�[�g�g�����X�~�b�V�����̍폜�j
	 *  �����č폜�����Ȃ�true�A�Ȃ������Ȃ�false��Ԃ�
	 */
	public boolean removeDataTableTfdInputPortTransIfExists() {
		PortTransManager portTransManager = this.transProc.portTransManager;
		PortTrans ptToDtTfdInputPort = portTransManager.getOnePortTransByEndPortIfExists(this.getDataTableTfdInputPort()); 
		
		// �������ꍇ
		if(ptToDtTfdInputPort!=null) {
			boolean remove = portTransManager.removePortTrans(ptToDtTfdInputPort);
			if(!remove) {
				Debug.error("IA���t���N�V�����̃f�[�^�e�[�u�����Ē�`���ꂽ�̂Ńf�[�^�e�[�u��TFD����0�ԓ���TPP�|�[�g�ւ̃|�[�g�g�����X�~�b�V�������폜���悤�Ƃ��܂������A���̃|�[�g�g�����X�~�b�V�����͌��X�Ȃ������Ƃ����������Ȏ��Ԃł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return false;
			}
			return true;
		}

		// �Ȃ������ꍇ
		return false;
	}
	/*
	 * 	SUMMARY :
	 *  �������A�J�E���g�e�[�u��TFD����`����Ă�����A������폜����i�|�[�g�g�����X�~�b�V�����̍폜�j
	 *  �����č폜�����Ȃ�true�A�Ȃ������Ȃ�false��Ԃ�
	 */
	public boolean removeAccountTableTfdInputPortTransIfExists() {
		PortTransManager portTransManager = this.transProc.portTransManager;
		PortTrans ptToAtTfdInputPort = portTransManager.getOnePortTransByEndPortIfExists(this.getAccountTableTfdInputPort()); 
		
		// �������ꍇ
		if(ptToAtTfdInputPort!=null) {
			boolean remove = portTransManager.removePortTrans(ptToAtTfdInputPort);
			if(!remove) {
				Debug.error("IA���t���N�V�����̃A�J�E���g�e�[�u�����Ē�`���ꂽ�̂ŃA�J�E���g�e�[�u��TFD����1�ԓ���TPP�|�[�g�ւ̃|�[�g�g�����X�~�b�V�������폜���悤�Ƃ��܂������A���̃|�[�g�g�����X�~�b�V�����͌��X�Ȃ������Ƃ����������Ȏ��Ԃł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return false;
			}
			return true;
		}

		// �Ȃ������ꍇ
		return false;
	}





	public void removeFromTransProc() {
		// �܂��A����IA���t���N�V�������g���폜
		this.transProc.removeTpp(this);
		
		PortTransManager ptm = this.transProc.portTransManager;
		
		// �f�[�^�e�[�u��TFD���󂯎��|�[�g�g�����X�~�b�V�������폜
		ptm.removeAllPortTransByEndPort(getDataTableTfdInputPort());

		// �A�J�E���g�e�[�u��TFD���󂯎��|�[�g�g�����X�~�b�V�������폜
		ptm.removeAllPortTransByEndPort(getAccountTableTfdInputPort());

		/*
		 * IA���t���N�V�����ɏo�͂͑��݂��Ȃ��̂ŁA�o�͂��󂯎��|�[�g�g�����X�~�b�V�����̍폜�͍l���Ȃ��ėǂ�
		 */
	}

}
