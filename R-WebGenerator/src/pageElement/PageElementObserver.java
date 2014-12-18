package pageElement;

import java.io.Serializable;
import java.util.ArrayList;

import debug.Debug;
import table.SuperTable;
import tpp.TppCreateFormReflection;
import tpp.TppUpdateFormReflection;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import tpp.portTrans.PortTransManager;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import view.transProcEdit.Panel_TpEdit_Above;
import webPage.WebPage;
import webPage.WebPageManager;

/*
 * SUMMARY :
 * �y�[�W�G�������g�̒ǉ���ҏW�A�폜���Ď����A�J�ڃv���Z�X��J�ڍ\���Ȃǂɑ΂��K�؂Ȕ��f���{���N���X
 */
public class PageElementObserver implements Serializable {
	private static PageElementObserver obj = new PageElementObserver();

	
	
	
	private PageElementObserver() {
	}
	
	
	
	
	public void informPeAddition(PageElement pe) {
		if(pe instanceof PageElementCreateForm) {
			informCreateFormAddition((PageElementCreateForm)pe);
		}
		else if(pe instanceof PageElementUpdateForm) {
			informUpdateFormAddition((PageElementUpdateForm)pe);
		}
		else if(pe instanceof PageElementLoginForm) {
			informLoginFormAddition((PageElementLoginForm)pe);
		}
		else if(pe instanceof PageElement_HyperLink) {
			informHyperLinkAddition((PageElement_HyperLink)pe);
		}
		else if(pe instanceof PageElementTableDisplay) {
			// �������Ȃ�
		}
		else if(pe instanceof PageElementDisplayArea) {
			// �������Ȃ�
		}
		else if(pe instanceof PageElementText) {
			// �������Ȃ�
		}
		else if(pe instanceof PageElementSaif) {
			informSaifAddition((PageElementSaif)pe);
		}
		else {
			Debug.error("�z��O�̃y�[�W�G�������g�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	
	
	
	public void informPeEdition(PageElement pe) {
		// ��������
	}

	
	
	
	




	public void informHyperLinkAddition(PageElement_HyperLink hyperLink) {
		// �V�����J�ڂ�����Ēǉ�
		Transition trans = new Transition(hyperLink.parentPage, hyperLink, hyperLink.getDestPage());
		TransitionManager.getInstance().addTransition(trans);
	}

	
	
	public void informLoginFormAddition(PageElementLoginForm loginForm) {
		// �V�����J�ڂ�����Ēǉ�
		Transition trans = new Transition(loginForm.parentPage, loginForm, loginForm.destWebPage);
		TransitionManager.getInstance().addTransition(trans);
	}
	
	
	public void informSaifAddition(PageElementSaif saif) {
		// �e�y�[�W����o�čs���S�J�ڂ̑J�ڃv���Z�X�ɑΉ������y�o�́z�y�[�W�G�������g�|�[�g��SAIF�ɍ��
		ArrayList<Transition> goingTransArray = TransitionManager.getInstance().getTransArrayByStartPage(saif.parentPage);
		for(int i=0; i<goingTransArray.size(); i++) {
			Transition trans = goingTransArray.get(i);
			TransitionProcess transProc = trans.transProc;
			saif.initOutputPePorts(transProc);
		}
	}
	
	
	public void informCreateFormAddition(PageElementCreateForm createForm) {
		// CreateForm���g���K�[�Ƃ����J�ڂ�ǉ�
		Transition trans = new Transition(createForm.parentPage, createForm, createForm.destPage);
		TransitionManager.getInstance().addTransition(trans);

		// �ǉ������J�ڂ̑J�ڃv���Z�X�ɑΉ������y�o�́z�y�[�W�G�������g�|�[�g��Create�t�H�[���ɍ��
		TransitionProcess transProc = trans.transProc;
		createForm.initOutputPePorts(transProc);

		// Create Form Reflection ���ŏ���TPP�Ƃ��Ēǉ����A�Ή�����p�l�������
		TppCreateFormReflection reflection = new TppCreateFormReflection(transProc, createForm);
		transProc.addTpp(reflection);
		Panel_TpEdit_Above.getInstance().addCreateFormReflectionPanel(reflection);
		
		// �|�[�g�g�����X�~�b�V�������쐬���Ēǉ�
		// �iCreate�t�H�[���̏o�̓y�[�W�G�������g�|�[�g����A���t���N�V�����̓���TPP�|�[�g�ցj
		PageElementPort startPort = createForm.outputPePortsHashMap.get(transProc);
		TppPort endPort = reflection.inputPorts.get(0);
		transProc.portTransManager.addPortTrans(startPort, endPort);
	}




	public void informUpdateFormAddition(PageElementUpdateForm updateForm) {
		// updateForm���g���K�[�Ƃ����J�ڂ�ǉ�
		Transition trans = new Transition(updateForm.parentPage, updateForm, updateForm.destPage);
		TransitionManager.getInstance().addTransition(trans);

		// �ǉ������J�ڂ̑J�ڃv���Z�X�ɑΉ������y�o�́z�y�[�W�G�������g�|�[�g��Update�t�H�[���ɍ��
		TransitionProcess transProc = trans.transProc;
		updateForm.initOutputPePorts(transProc);
		
		// Update Form Reflection ���ŏ���TPP�Ƃ��Ēǉ�
		TppUpdateFormReflection reflection = new TppUpdateFormReflection(transProc, updateForm);
		transProc.addTpp(reflection);
		Panel_TpEdit_Above.getInstance().addUpdateFormReflectionPanel(reflection);

		// �|�[�g�g�����X�~�b�V�������쐬���Ēǉ�
		// �iUpdate�t�H�[���̏o�̓y�[�W�G�������g�|�[�g����AUpdate�t�H�[�����t���N�V�����̓���TPP�|�[�g�ցj
		PageElementPort startPort = updateForm.outputPePortsHashMap.get(transProc);
		TppPort endPort = reflection.inputPorts.get(0);
		transProc.portTransManager.addPortTrans(startPort, endPort);
	}


	
	
	
	public void informPeRemovement(PageElement pe) {
		if(pe instanceof PageElementCreateForm) {
			informCreateFormRemovement((PageElementCreateForm)pe);
		}
		else if(pe instanceof PageElementUpdateForm) {
			informUpdateFormRemovement((PageElementUpdateForm)pe);
		}
		else if(pe instanceof PageElementLoginForm) {
			informLoginFormRemovement((PageElementLoginForm)pe);
		}
		else if(pe instanceof PageElement_HyperLink) {
			informHyperLinkRemovement((PageElement_HyperLink)pe);
		}
		else if(pe instanceof PageElementTableDisplay) {
			// �������Ȃ�
		}
		else if(pe instanceof PageElementDisplayArea) {
			informDisplayAreaRemovement((PageElementDisplayArea)pe);
		}
		else if(pe instanceof PageElementText) {
			// �������Ȃ�
		}
		else {
			Debug.error("�z��O�̃y�[�W�G�������g�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}
	
	
	
	
	public void informTableRemovement(SuperTable table) {
		WebPageManager wpm = WebPageManager.getInstance();
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);
				
				if(pe instanceof PageElementCreateForm) {
					PageElementCreateForm createForm = (PageElementCreateForm)pe;
					if(createForm.table==table) {
						webPage.removePageElement(createForm);
					}
				}
				else if(pe instanceof PageElementUpdateForm) {
					PageElementUpdateForm updateForm = (PageElementUpdateForm)pe;
					if(updateForm.table==table) {
						webPage.removePageElement(updateForm);
					}
				}
				else if(pe instanceof PageElementLoginForm) {
					PageElementLoginForm loginForm = (PageElementLoginForm)pe;
					if(loginForm.accountTable==table) {
						webPage.removePageElement(loginForm);
					}
				}
				else if(pe instanceof PageElement_HyperLink) {
					// �������Ȃ�
				}
				else if(pe instanceof PageElementTableDisplay) {
					PageElementTableDisplay tableDisplay = (PageElementTableDisplay)pe;
					if(tableDisplay.table==table) {
						webPage.removePageElement(tableDisplay);
					}
				}
				else if(pe instanceof PageElementDisplayArea) {
				}
				else if(pe instanceof PageElementText) {
				}
				else {
					Debug.error("�z��O�̃y�[�W�G�������g�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
		}
	}
	
	
	
	
	
	public void informCreateFormRemovement(PageElementCreateForm createForm) {
		// ����Create�t�H�[���ɂ��J�ڂ��폜
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByCreateForm = tm.getTransitionByTriggerPe(createForm);
		tm.removeTransition(transByCreateForm);
	}

	
	
	
	
	public void informUpdateFormRemovement(PageElementUpdateForm updateForm) {
		// ����Update�t�H�[���ɂ��J�ڂ��폜
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByUpdateForm = tm.getTransitionByTriggerPe(updateForm);
		tm.removeTransition(transByUpdateForm);
	}
	



	public void informLoginFormRemovement(PageElementLoginForm loginForm) {
		// ����Login�t�H�[���ɂ��J�ڂ��폜
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByLoginForm = tm.getTransitionByTriggerPe(loginForm);
		tm.removeTransition(transByLoginForm);
	}

	
	
	
	
	public void informHyperLinkRemovement(PageElement_HyperLink hyperLink) {
		// ���̃n�C�p�[�����N�ɂ��J�ڂ��폜
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByHyperLink = tm.getTransitionByTriggerPe(hyperLink);
		tm.removeTransition(transByHyperLink);
	}

	
	
	
	public void informDisplayAreaRemovement(PageElementDisplayArea displayArea) {
		WebPage parentPage = displayArea.parentPage;

		// �e�������y�[�W���ց��������S�J�ڂɂ��ď���
		ArrayList<Transition> transArrayToParentPage = TransitionManager.getInstance().getTransArrayByEndPage(parentPage);
		for(int i=0; i<transArrayToParentPage.size(); i++) {
			Transition trans = transArrayToParentPage.get(i);

			// ���̑J�ڂ̑J�ڃv���Z�X
			TransitionProcess transProc = trans.transProc;

			// DisplayArea�֌������|�[�g�g�����X�~�b�V������S�č폜
			PortTransManager ptm = transProc.portTransManager;
			PageElementPort inputPortInThisTransProc = displayArea.inputPePortsHashMap.get(transProc);
			ptm.removeAllPortTransByEndPort(inputPortInThisTransProc);
		}
	}
	
	
	
	
	public static PageElementObserver getInstance() {
		return PageElementObserver.obj;
	}
}
