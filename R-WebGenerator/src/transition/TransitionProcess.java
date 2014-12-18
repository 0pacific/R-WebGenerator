package transition;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;

import tpp.*;
import tpp.portTrans.*;
import tpp.service.*;
import webPage.*;

public class TransitionProcess implements Serializable {
	public Transition belongingTrans;
	
	public ArrayList<TransitionProcessPart> tppArray = new ArrayList<TransitionProcessPart>();
	private WebPage startPage;
	private WebPage endPage;

	// �eTPP�̃|�[�g�Ԃ̃f�[�^��n�iPortTrans�j���Ǘ�����C���X�^���X
	public  PortTransManager portTransManager;
	
	// �J�ڌ��E�J�ڐ�y�[�W��\���p�l���̍��㒸�_�̍��W
	private Point startPagePosition;
	private Point endPagePosition;
	public static final Point START_PAGE_POSITION_DEFAULT = new Point(50, 50);
	public static final Point END_PAGE_POSITION_DEFAULT = new Point(500, 50);
	

	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	public TransitionProcess(Transition transition, WebPage startPage, WebPage endPage) {
		this.belongingTrans = transition;
		this.startPage = startPage;
		this.endPage = endPage;

		startPagePosition = TransitionProcess.START_PAGE_POSITION_DEFAULT;
		endPagePosition = TransitionProcess.END_PAGE_POSITION_DEFAULT;

		// �|�[�g�Ԃ̃f�[�^��n���Ǘ�����C���X�^���X
		portTransManager = new PortTransManager(this);
	}
	

	
	
	public void debugTppArray() {
		Debug.notice("���݁A"+getTppNum()+"��TPP�����̂悤�ɕ���ł��܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		for(int tppOrder=0; tppOrder<getTppNum(); tppOrder++) {
			TransitionProcessPart tpp = getTpp(tppOrder);
			if(tpp instanceof Service) {
				Service service = (Service)tpp;
				Debug.notice(tppOrder+"�ԁF�T�[�r�X�u"+service.serviceName+"�v", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppCreateReflection) {
				TppCreateReflection creRef = (TppCreateReflection)tpp;
				Debug.notice(tppOrder+"�ԁFCreate���t���N�V�����@�e�[�u���u"+creRef.table.getTableName()+"�v", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppCreateFormReflection) {
				TppCreateFormReflection creFormRef = (TppCreateFormReflection)tpp;
				Debug.notice(tppOrder+"�ԁFCreate�t�H�[�����t���N�V�����@�e�[�u���u"+creFormRef.createForm.table.getTableName()+"�v", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppIAReflection) {
				Debug.notice(tppOrder+"�ԁFIA���t���N�V����", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstArrayInt) {
				Debug.notice(tppOrder+"�ԁFINT�萔�z��", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstArrayString) {
				Debug.notice(tppOrder+"�ԁFSTRING�萔�z��", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstInt) {
				Debug.notice(tppOrder+"�ԁFINT���m�萔", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstString) {
				Debug.notice(tppOrder+"�ԁFSTRING���m�萔", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else {
				Debug.notice(tppOrder+"�ԁF"+tpp.toString(), getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
	}
	
	

	
	
	/*
	 * SUMMARY :
	 * TPP�ǉ�
	 */
	public void addTpp(TransitionProcessPart tpp) {
		tppArray.add(tpp);
	}
	public void addTpp(int index, TransitionProcessPart tpp) {
		tppArray.add(index, tpp);
	}
	
	
	
	
	
	public boolean removeTpp(TransitionProcessPart tpp) {
		boolean remove = tppArray.remove(tpp);
		if(!remove) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "�G���[���������܂����B");
			Debug.error("�폜���悤�Ƃ���TPP��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}

	
	
	
	public PortTransManager getPortTransManager() {
		return portTransManager;
	}
	
	
	public WebPage getStartPage() {
		return startPage;
	}
	
	
	
	
	public WebPage getEndPage() {
		return endPage;
	}

	
	
	
	
	public Point getStartPagePosition() {
		return startPagePosition;
	}


	
	
	
	public Point getEndPagePosition() {
		return endPagePosition;
	}

	

	public TransitionProcessPart getTpp(int index) {
		return tppArray.get(index);
	}
	
	public int getTppNum() {
		return tppArray.size();
	}

	public int getTppo(TransitionProcessPart argTpp) {
		for(int i=0; i<getTppNum(); i++) {
			TransitionProcessPart tpp = getTpp(i);
			if(tpp==argTpp) {
				return i;
			}
		}

		Debug.error("TPP��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
}
