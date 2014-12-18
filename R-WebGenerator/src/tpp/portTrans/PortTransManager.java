package tpp.portTrans;

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
import tpp.service.Service;
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



public class PortTransManager implements Serializable {
	private TransitionProcess transProc;
	private ArrayList<PortTrans> portTransList;
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public PortTransManager(TransitionProcess transProc) {
		this.transProc = transProc;
		portTransList = new ArrayList<PortTrans>();
	}


	
	
	public boolean inputReserved(DataPort port) {
		// port���o�͐�Ƃ��ăf�[�^�g�����X�~�b�V���������񌩂��邩�A�f�o�b�O�̂��߈ꉞ�J�E���g�i0��1�ɂȂ�Ȃ��Ƃ��������j
		int foundCount = 0;
		
		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans trans = getPortTrans(i);
			DataPort endPort = trans.getEndPort();
			if(endPort==port) {
				foundCount++;
			}
		}

		if(foundCount > 1) {
			Debug.error("PortTransManager inputReserved() : �w�肵���|�[�g�ւ̓��͂��Q�ȏ㌟�m���Ă��܂��܂����B");
		}
		else if(foundCount==1) {
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肵���|�[�g�u����́v�i�u�ւ́v�j�|�[�g�g�����X�~�b�V����������Ύ擾����
	 */
	public PortTrans getPortTransByStartPortIfExists(DataPort outPort) {
		// �S�|�[�g�g�����X�~�b�V�������l�ׂ��Ƀ`�F�b�N
		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans pt = getPortTrans(i);

			// �o���|�[�g���w�肳�ꂽ�|�[�g�ƈ�v
			if(pt.getStartPort()==outPort) {
				return pt;
			}
		}

		// �����炸
		Debug.notice("�w�肵���f�[�^�|�[�g����o������|�[�g�g�����X�~�b�V���������邩���ׂ܂������A�Ȃ��悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	public PortTrans getOnePortTransByEndPortIfExists(DataPort inPort) {
		// �S�|�[�g�g�����X�~�b�V�������l�ׂ��Ƀ`�F�b�N
		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans pt = getPortTrans(i);

			// �����|�[�g���w�肳�ꂽ�|�[�g�ƈ�v
			if(pt.getEndPort()==inPort) {
				return pt;
			}
		}

		// �����炸
		Debug.notice("�w�肵���f�[�^�|�[�g�֓�������|�[�g�g�����X�~�b�V���������邩���ׂ܂������A�Ȃ��悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	/*
	 * SUMMARY :
	 * �T�[�r�X�������������TPP�|�[�g��n���āA�ړI�̎�ނ�TPP�������������Ƃ��Ē�`����Ă���Ύ擾����Ƃ����֐��Q
	 */
	public TransitionProcessPart getInputTppToTppPortIfExists(TppPort inputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(inputPort);
		
		// �܂��C���v�b�g����`����Ă��Ȃ�
		if(portTrans==null) {
			Debug.notice("�w�肳�ꂽ�T�[�r�X�����|�[�g�ւ̓��͂���`����ĂȂ������ׂ܂������A�܂���`����Ă��Ȃ��悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		// �����Ƃ��Ē�`����Ă���TPP���擾
		TransitionProcessPart inputTpp = ((TppPort)portTrans.getStartPort()).getTpp();
		return inputTpp;
	}
	/*
	 * �ȍ~�̊֐��͂���������i��̂��̂����g���āATPP�̃^�C�v�������Ă��邩�͌Ăяo�����Ń`�F�b�N����Ηǂ��j
	 */
	// INT�萔�z����擾
	public TppConstArrayInt getInputTppConstArrayIntIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// �܂�INT�萔�z�񂪒�`����Ă��Ȃ�
		if(portTrans==null) {
			Debug.notice("�܂�INT�萔�z�񂪒�`����Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TppConstArrayInt cai = (TppConstArrayInt)startTppPort.getTpp();	// �������ŃG���[�ɂȂ邩��

		return cai;
	}
	// TFD�o�͌^TPP���擾
	public TfdOutputer getInputTfdOutputerTppIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// �܂�TFD�o�͌^TPP����`����Ă��Ȃ�
		if(portTrans==null) {
			Debug.notice("�܂�TFD�o�͌^TPP����`����Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return (TfdOutputer)tpp;
	}
	// STRING�萔�z����擾
	public TppConstArrayString getInputStringTppIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// �܂�STRING�萔�z�񂪒�`����Ă��Ȃ�
		if(portTrans==null) {
			Debug.out("��1210:�܂�STRING�萔�z���TPP����`����Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return (TppConstArrayString)tpp;
	}
	// TPP�i���肵�Ȃ��j���擾
	public TransitionProcessPart getInputTppIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// ����`
		if(portTrans==null) {
			Debug.notice("TPP������`�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return tpp;
	}

	
	
	
	
	// Display Area�̓��̓y�[�W�G�������g�|�[�g���w�肵�A�����֓��͂����TPP�o�͌^TPP��������`����Ă���ΕԂ�
	public TfdOutputer getTfdOutputerForDisplayAreaIfExists(PageElementPort daInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(daInputPort);
		
		// �܂�TFD�o�͌^TPP����`����Ă��Ȃ�
		if(portTrans==null) {
			Debug.notice("�܂�TFD�o�͌^TPP����`����Ă��Ȃ��悤�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return (TfdOutputer)tpp;
	}
	
	
	
	
	
	
	public PortTrans getPortTrans(int index) {
		return portTransList.get(index);
	}
	
	
	
	public int getPortTransNum() {
		return portTransList.size();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肵���|�[�g�g�����X�~�b�V�����𒼐ڒǉ�
	 */
	public void addPortTrans(PortTrans pt) {
		portTransList.add(pt);
	}

	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ���́E�o�̓|�[�g���w�肵�ă|�[�g�g�����X�~�b�V������ǉ�
	 */
	public void addPortTrans(DataPort startPort, DataPort endPort) {
		PortTrans pt = new PortTrans(startPort, endPort);
		addPortTrans(pt);
	}

	
	
	
	
	/*
	 * �w�肵���|�[�g�g�����X�~�b�V�������y����Ȃ�z�폜����
	 * �����č폜�����Ȃ�true, �Ȃ������Ȃ�false��Ԃ�
	 */
	public boolean removePortTrans(PortTrans portTrans) {
		boolean result = portTransList.remove(portTrans);
		if(!result) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "�G���[���������܂����B");
			Debug.error("�������Ƃ����|�[�g�g�����X�~�b�V������������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		/*
		 * �������Ƃ����|�[�g�g�����X�~�b�V�����͌�����A�폜���ꂽ
		 * ���̃|�[�g�g�����X�~�b�V�����̏I�[���T�[�r�X�̈����|�[�g�ł������ꍇ�A�㑱�������폜���˂΂Ȃ�Ȃ�
		 * ��������ꂩ����
		 */

		DataPort endPort = portTrans.getEndPort();
		if(endPort instanceof TppPort) {
			TppPort endTppPort = (TppPort)endPort;
			TransitionProcessPart endTpp = endTppPort.getTpp();
			
			// �������Ƃ��Ă���|�[�g�g�����X�~�b�V�����̏I�[�̓T�[�r�X�̈����|�[�g�ł�����
			if(endTpp instanceof Service) {
				Service endService = (Service)endTpp;

				// �폜����T�[�r�X�̏o�͂��A�������̃T�[�r�X�͉��Ԗڂ̈����Ƃ��Ď󂯎���Ă���H
				int argIndex = endService.getArgIndexByArgPort(endTppPort);

				// �㑱���������S�č폜����i�폜�����|�[�g�g�����X�~�b�V�����̏I�[�������������̂́A���ɍ폜����Ă���킯�ł���j
				endService.removeFollowingArgsIfExist(argIndex);
			}
		}

		/*
		 * �T�[�r�X�̌㑱�����������폜���ꂽ
		 */
		
		return true;
	}

	
	
	
	public ArrayList<PortTrans> getAllPortTransByStartPort(DataPort startPort) {
		ArrayList<PortTrans> portTransListToReturn = new ArrayList<PortTrans>();

		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans portTrans = getPortTrans(i);
			if(portTrans.getStartPort()==startPort) {
				portTransListToReturn.add(portTrans);
			}
		}

		return portTransListToReturn;
	}
	public ArrayList<PortTrans> getAllPortTransByEndPort(DataPort endPort) {
		ArrayList<PortTrans> portTransListToReturn = new ArrayList<PortTrans>();

		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans portTrans = getPortTrans(i);
			if(portTrans.getEndPort()==endPort) {
				portTransListToReturn.add(portTrans);
			}
		}

		return portTransListToReturn;
	}
	
	
	
	
	
	public void removeAllPortTransByStartPort(DataPort startPort) {
		ArrayList<PortTrans> portTransListToRemove = getAllPortTransByStartPort(startPort);
		for(int i=0; i<portTransListToRemove.size(); i++) {
			removePortTrans(portTransListToRemove.get(i));
		}
	}
	public void removeAllPortTransByEndPort(DataPort endPort) {
		ArrayList<PortTrans> portTransListToRemove = getAllPortTransByEndPort(endPort);
		for(int i=0; i<portTransListToRemove.size(); i++) {
			removePortTrans(portTransListToRemove.get(i));
		}
	}
	
	
	
	
	
	public boolean removeOnePortTransByEndPortIfExists(DataPort endPort) {
		PortTrans portTrans = getOnePortTransByEndPortIfExists(endPort);
		if(portTrans==null) {
			return false;
		}
		removePortTrans(portTrans);
		return true;

		/*
		 * �Q�ȏ�̃|�[�g�g�����X�~�b�V�����������邱�Ƃ͑z�肵�Ă��Ȃ�
		 */
	}
	



	public void varDump() {
		String log = "";

		log += "���̑J�ڃv���Z�X���́A�|�[�g�g�����X�~�b�V�����̐��́@" + getPortTransNum() + "�@�ł��B\n";
		
		for(int i=0; i<getPortTransNum(); i++) {
			log += "�@�@" + i + "�ځi0�n�܂�j�̃|�[�g�g�����X�~�b�V�����ɂ��ďo�́F\n";

			PortTrans pt = getPortTrans(i);
			DataPort startPort = pt.getStartPort();
			DataPort endPort = pt.getEndPort();
			log += "�@�@�@�@�o���|�[�g..." + startPort.getClass().getSimpleName() + "�^�ł��B\n";
			log += "�@�@�@�@�����|�[�g..." + endPort.getClass().getSimpleName() + "�^�ł��B\n";
		}

		log += "���̑J�ڃv���Z�X���́A�|�[�g�g�����X�~�b�V�����ɂ��Ẵf�o�b�O�o�͂��I�����܂��B";
		
		Debug.varDump(log, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
