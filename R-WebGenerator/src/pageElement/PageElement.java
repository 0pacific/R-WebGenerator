package pageElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;

import tpp.TransitionProcessPart;
import tpp.port.PageElementPort;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;

public abstract class PageElement implements Serializable {
	public WebPage parentPage;
	public int numberInParentPage;
	public HashMap<TransitionProcess, PageElementPort> inputPePortsHashMap;
	public HashMap<TransitionProcess, PageElementPort> outputPePortsHashMap;

	// �y�[�W�G�������g��L�[�i���s�v���O�����̓���ɕK�v�ł���A�f�[�^�x�[�X�ɋL�^�����B��������Ƃ��ɏ��߂ė^�����A�R���X�g���N�^�ł͏���������Ȃ��j
	public int pePrimaryKey;

	
	
	
	
	public PageElement(WebPage parentPage) {
		this.parentPage = parentPage;
		inputPePortsHashMap = new HashMap<TransitionProcess, PageElementPort>();
		outputPePortsHashMap = new HashMap<TransitionProcess, PageElementPort>();
		
		TransitionManager tm = TransitionManager.getInstance();

		// ���̃y�[�W�G�������g���u�����Web�y�[�W����y�o������z�J�ڃv���Z�X�S�Ă�􂢏o���A���ꂼ��ɑΉ�����y�o�́z�y�[�W�G�������g�|�[�g��������
		ArrayList<Transition> goingTransArray = tm.getTransArrayByStartPage(parentPage);
		for(int i=0; i<goingTransArray.size(); i++) {
			Transition goingTrans = goingTransArray.get(i);
			TransitionProcess goingTransProc = goingTrans.transProc;
			this.initOutputPePorts(goingTransProc);
		}
		// ���̃y�[�W�G�������g���u�����Web�y�[�W�ցy����z�J�ڃv���Z�X�S�Ă�􂢏o���A���ꂼ��ɑΉ�����y���́z�y�[�W�G�������g�|�[�g��������
		ArrayList<Transition> comingTransArray = tm.getTransArrayByEndPage(parentPage);
		for(int i=0; i<comingTransArray.size(); i++) {
			Transition comingTrans = comingTransArray.get(i);
			TransitionProcess comingTransProc = comingTrans.transProc;
			this.initInputPePorts(comingTransProc);
		}
	}
		

	public WebPage getParentPage() {
		return parentPage;
	}

	public int getNumberInParentPage() {
		return numberInParentPage;
	}

	public abstract void initInputPePorts(TransitionProcess transProc);
	public abstract void initOutputPePorts(TransitionProcess transProc);
	
	
	
	
	/*
	 * SUMMARY :
	 * PageElement�N���X�̃T�u�N���X�̃C���X�^���X���󂯎��A���̎�ނɉ�����DB���ł̖��̂�Ԃ�
	 * ���̖��̂́ADB��"page_element"�e�[�u����"kind"�t�B�[���h�ɋL�^�������̂ł���
	 */
	public static String givePeExpression(PageElement pe) {
		if(pe instanceof PageElement_HyperLink) {
			return "Hyper Link";
		}
		else if(pe instanceof PageElementDisplayArea) {
			return "Display Area";
		}
		else if(pe instanceof PageElementTableDisplay) {
			return "Table Display";
		}
		else if(pe instanceof PageElementCreateForm) {
			return "Create Form";
		}
		else if(pe instanceof PageElementLoginForm) {
			return "Login Form";
		}
		else if(pe instanceof PageElementText) {
			return "Text";
		}
		else if(pe instanceof PageElementUpdateForm) {
			return "Update Form";
		}
		else if(pe instanceof PageElementSaif) {
			return "Service Argument Input Form";
		}
		/*
		 * ���������F�S��ނ̃y�[�W�G�������g�ɑΉ�������
		 */
		
		// �G���[
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "�G���[���������܂����B");
		Debug.error("�z��O�̃y�[�W�\���v�f�ł��B", "PageElement", Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
}