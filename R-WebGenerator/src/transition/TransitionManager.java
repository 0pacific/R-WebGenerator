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
	 * インスタンス参照変数は最後
	 */
	private static TransitionManager obj = new TransitionManager();

	
	
	
	
	private TransitionManager() {
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * 遷移元（遷移先）Webページを指定し、そのWebページが遷移元（遷移先）となっているすべてのTransitionインスタンスをArrayListで返却
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

		// エラー
		Debug.error("Transitionインスタンスが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public int getTransitionIndex(Transition argTrans) {
		for(int i=0; i<getTransitionNum(); i++) {
			Transition trans = getTransition(i);
			if(trans==argTrans) {
				return i;
			}
		}

		Debug.error("Transitionインスタンスが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	
	
	
	public void addTransition(Transition trans) {
		transitions.add(trans);

		// 追加した遷移の遷移プロセス
		TransitionProcess transProc = trans.transProc;

		WebPage startPage = trans.startPage;
		WebPage endPage = trans.endPage;

		// 遷移【元】ページの全ページエレメントの、【出力】ページエレメントポートを初期化
		for(int i=0; i<startPage.getPageElementNum(); i++) {
			PageElement pe = startPage.getPageElement(i);
			pe.initOutputPePorts(transProc);
		}
		// 遷移【先】ページの全ページエレメントの、【入力】ページエレメントポートを初期化
		for(int i=0; i<endPage.getPageElementNum(); i++) {
			PageElement pe = endPage.getPageElement(i);
			pe.initInputPePorts(transProc);
		}

		
		// AuthorityManagerインスタンスに報告（この遷移に対する全ロールの遷移権限を初期化してもらう）
		AuthorityManager.getInstance().informTransAddition(trans);
	}

	
	
	
	
	public ArrayList<Transition> getDuplicativeTransitionArray(Transition trans) {
		WebPage startPage = trans.startPage;
		WebPage endPage = trans.endPage;

		// 同じ方向
		ArrayList<Transition> goingTransArray = this.getTransArrayByStartPageAndEndPage(startPage, endPage);
		// 逆方向
		ArrayList<Transition> comingTransArray = this.getTransArrayByStartPageAndEndPage(endPage, startPage);
	
		// 連結
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
			
			// そのWebページに向かっていた遷移は全て削除
			if(trans.endPage==webPage) {
				transArrayToRemove.add(trans);
			}
			// そのWebページ（のページエレメント）から出ていた遷移を全て削除
			else if(trans.triggerPe.parentPage==webPage) {
				transArrayToRemove.add(trans);
			}
		}

		// 削除するべき遷移としてリストアップした全ての遷移を削除
		for(int i=0; i<transArrayToRemove.size(); i++) {
			Transition transToRemove = transArrayToRemove.get(i);
			removeTransition(transToRemove);
		}
	}
	
	
	
	
	public boolean removeTransition(Transition trans) {
		boolean remove = transitions.remove(trans);
		if(!remove) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
			Debug.error("削除しようとした遷移が見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		AuthorityManager.getInstance().informTransRemovement(trans);
		return true;
	}



	public static void updateInstance(TransitionManager newObject) {
		TransitionManager.obj = newObject;
	}

}
