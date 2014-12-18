package transition;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import authority.AuthorityManager;

import pageElement.PageElement;
import webPage.WebPage;

import debug.Debug;


/*
 * Singleton
 */
public class TransitionManager implements Serializable {
	private ArrayList<Transition> transitions = new ArrayList<Transition>();
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�
	 */
	private static TransitionManager obj = new TransitionManager();

	
	
	
	
	private TransitionManager() {
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * �J�ڌ��i�J�ڐ�jWeb�y�[�W���w�肵�A����Web�y�[�W���J�ڌ��i�J�ڐ�j�ƂȂ��Ă��邷�ׂĂ�Transition�C���X�^���X��ArrayList�ŕԋp
	 */
	public ArrayList<Transition> getTransArrayByStartPage(WebPage webPage) {
		ArrayList<Transition> transArray = new ArrayList<Transition>();
		
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			WebPage startPage = trans.startPage;
			if(startPage==webPage) {
				transArray.add(trans);
			}
		}

		return transArray;
	}
	public ArrayList<Transition> getTransArrayByEndPage(WebPage webPage) {
		ArrayList<Transition> transArray = new ArrayList<Transition>();
		
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			WebPage endPage = trans.endPage;
			if(endPage==webPage) {
				transArray.add(trans);
			}
		}

		return transArray;
	}
	public ArrayList<Transition> getTransArrayByStartPageAndEndPage(WebPage startPage, WebPage endPage) {
		ArrayList<Transition> transArray = new ArrayList<Transition>();
		
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			WebPage sp = trans.startPage;
			WebPage ep = trans.endPage;
			if(sp==startPage && ep==endPage) {
				transArray.add(trans);
			}
		}

		return transArray;
	}
	
	
	
	
	
	public Transition getTransitionByTriggerPe(PageElement pe) {
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			if(trans.triggerPe==pe) {
				return trans;
			}
		}

		// �G���[
		Debug.error("Transition�C���X�^���X��������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public int getTransitionIndex(Transition argTrans) {
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			if(trans==argTrans) {
				return i;
			}
		}

		Debug.error("Transition�C���X�^���X��������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	
	
	
	public void addTransition(Transition trans) {
		transitions.add(trans);

		// �ǉ������J�ڂ̑J�ڃv���Z�X
		TransitionProcess transProc = trans.transProc;

		WebPage startPage = trans.startPage;
		WebPage endPage = trans.endPage;

		// �J�ځy���z�y�[�W�̑S�y�[�W�G�������g�́A�y�o�́z�y�[�W�G�������g�|�[�g��������
		for(int i=0; i<startPage.getPageElementNum(); i++) {
			PageElement pe = startPage.getPageElement(i);
			pe.initOutputPePorts(transProc);
		}
		// �J�ځy��z�y�[�W�̑S�y�[�W�G�������g�́A�y���́z�y�[�W�G�������g�|�[�g��������
		for(int i=0; i<endPage.getPageElementNum(); i++) {
			PageElement pe = endPage.getPageElement(i);
			pe.initInputPePorts(transProc);
		}

		
		// AuthorityManager�C���X�^���X�ɕ񍐁i���̑J�ڂɑ΂���S���[���̑J�ڌ��������������Ă��炤�j
		AuthorityManager.getInstance().informTransAddition(trans);
	}

	
	
	
	
	public ArrayList<Transition> getDuplicativeTransitionArray(Transition trans) {
		WebPage startPage = trans.startPage;
		WebPage endPage = trans.endPage;

		// ��������
		ArrayList<Transition> goingTransArray = this.getTransArrayByStartPageAndEndPage(startPage, endPage);
		// �t����
		ArrayList<Transition> comingTransArray = this.getTransArrayByStartPageAndEndPage(endPage, startPage);
	
		// �A��
		ArrayList<Transition> transList = goingTransArray;
		for(int i=0; i<comingTransArray.size(); i++) {
			transList.add(comingTransArray.get(i));
		}

		return transList;
	}
	
	
	

	
	public static boolean duplicativeAboutStartPageAndEndPage(Transition trans1, Transition trans2) {
		WebPage startPage1 = trans1.startPage;
		WebPage startPage2 = trans2.startPage;
		WebPage endPage1 = trans1.endPage;
		WebPage endPage2 = trans2.endPage;

		boolean duplicative = (startPage1==startPage2 && endPage1==endPage2);
		return duplicative;
	}
	
	
	
	
	
	public Transition getTransition(int index) {
		return transitions.get(index);
	}


	
	public ArrayList<Transition> getTransitionByStartPage(WebPage argStartPage) {
		ArrayList<Transition> transArray = new ArrayList<Transition>();
		for(int i=0; i<transitions.size(); i++) {
			Transition trans = transitions.get(i);
			if(trans.startPage==argStartPage) {
				transArray.add(trans);
			}
		}

		return transArray;
	}
	public ArrayList<Transition> getTransitionByEndPage(WebPage argEndPage) {
		ArrayList<Transition> transArray = new ArrayList<Transition>();
		for(int i=0; i<transitions.size(); i++) {
			Transition trans = transitions.get(i);
			if(trans.endPage==argEndPage) {
				transArray.add(trans);
			}
		}

		return transArray;
	}
	
	


	
	public int getTransitionNum() {
		return transitions.size();
	}
	
	
	
	
	
	public static TransitionManager getInstance() {
		return TransitionManager.obj;
	}

	
	
	
	public void informWebPageRemovement(WebPage webPage) {
		ArrayList<Transition> transArrayToRemove = new ArrayList<Transition>();
		
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			
			// ����Web�y�[�W�Ɍ������Ă����J�ڂ͑S�č폜
			if(trans.endPage==webPage) {
				transArrayToRemove.add(trans);
			}
			// ����Web�y�[�W�i�̃y�[�W�G�������g�j����o�Ă����J�ڂ�S�č폜
			else if(trans.triggerPe.parentPage==webPage) {
				transArrayToRemove.add(trans);
			}
		}

		// �폜����ׂ��J�ڂƂ��ă��X�g�A�b�v�����S�Ă̑J�ڂ��폜
		for(int i=0; i<transArrayToRemove.size(); i++) {
			Transition transToRemove = transArrayToRemove.get(i);
			removeTransition(transToRemove);
		}
	}
	
	
	
	
	public boolean removeTransition(Transition trans) {
		boolean remove = transitions.remove(trans);
		if(!remove) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "�G���[���������܂����B");
			Debug.error("�폜���悤�Ƃ����J�ڂ�������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		AuthorityManager.getInstance().informTransRemovement(trans);
		return true;
	}



	public static void updateInstance(TransitionManager newObject) {
		TransitionManager.obj = newObject;
	}

}
